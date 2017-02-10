package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.dto.gateway.response.DefrayPayResponse;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.core.persistence.reader.salary.RapidlyPayrollExcelReaderMapper;
import com.xtr.core.persistence.reader.salary.RapidlyPayrollReaderMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollExcelWriterMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>急速发工资提现任务</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/11/9 17:11
 */
@Service("rapidlyWithdrawalsTask")
public class RapidlyWithdrawalsTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidlyWithdrawalsTask.class);

    @Resource
    private RapidlyPayrollExcelReaderMapper rapidlyPayrollExcelReaderMapper;

    @Resource
    private RapidlyPayrollExcelWriterMapper rapidlyPayrollExcelWriterMapper;

    @Resource
    private RapidlyPayrollReaderMapper rapidlyPayrollReaderMapper;

    @Resource
    private RapidlyPayrollWriterMapper rapidlyPayrollWriterMapper;

    @Resource
    private BankCodeService bankCodeService;

    @Resource
    private JdPayService jdPayService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    /**
     * 急速发工资
     *
     * @throws Exception
     */
    public void run() throws Exception {
        LOGGER.info("开始执行急速发工资任务···");
        //获取状态为待发放的工资单  0:未发放 1:待发放  2:发放中  3:已发放
        List<RapidlyPayrollExcelBean> list = rapidlyPayrollExcelReaderMapper.selectRapidlyPayroll();
        if (list != null && !list.isEmpty()) {
            int size = list.size();
            LOGGER.info("需要发工资的数量【" + size + "】");
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<>();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            for (final RapidlyPayrollExcelBean rapidlyPayrollExcelBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //工资发放
                            payOff(rapidlyPayrollExcelBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("rapidlyPayrollExcelId【" + rapidlyPayrollExcelBean.getId() + "】出现异常···" + e.getMessage(), e);
                        }
                    }
                }));

            }
            //等待处理结果
            for (Future f : rowResult) {
                f.get();
            }
            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
            pool.shutdown();
            //出现异常
            if (!errorList.isEmpty()) {
                //抛出异常信息
                throw new BusinessException(errorList.get(0).getMessage());
            }
        }
        LOGGER.info("急速发工资任务执行结束···");
    }


    /**
     * 工资发放
     *
     * @param rapidlyPayrollExcelBean
     */
    public void payOff(RapidlyPayrollExcelBean rapidlyPayrollExcelBean) {
        //获取对应待发放的工资单
        List<RapidlyPayrollBean> list = rapidlyPayrollReaderMapper.selectByExcelIdState(rapidlyPayrollExcelBean.getCompanyId(), rapidlyPayrollExcelBean.getId());
        if (list != null && !list.isEmpty()) {
            //更新文档工资发放状态  0:未发放 1:待发放  2:发放中  3:已发放
            rapidlyPayrollExcelBean.setGrantState(2);
            for (int i = 0; i < list.size(); i++) {
                try {
                    payday(list.get(i));
                } catch (Exception e) {
                    LOGGER.error("rapidlyPaayrollId【" + list.get(i).getId() + "】急速发工资工资发放失败···");
                }
            }
        } else {
            //更新文档工资发放状态  0:未发放 1:待发放  2:发放中  3:已发放
            rapidlyPayrollExcelBean.setGrantState(3);
        }
        rapidlyPayrollExcelWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollExcelBean);
    }

    /**
     * 发工资
     *
     * @param rapidlyPayrollBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void payday(RapidlyPayrollBean rapidlyPayrollBean) {
        LOGGER.info("急速发工资参数：" + JSON.toJSONString(rapidlyPayrollBean));
        DefrayPayRequest dr = new DefrayPayRequest(BusinessType.RAPIDLY_WITHDRAW);
        //根据Id获取员工姓名
//        CustomersBean customersBean = rapidlyPayrollBean.getCustomerName();
        //根据银行名称获取银行编码
        BankCodeBean bankCodeBean = bankCodeService.selectByBankName(rapidlyPayrollBean.getBankAccount());
        if (bankCodeBean != null)
            //收款银行编码
            dr.setPayeeBankCode(bankCodeBean.getBankCode());
        //收款帐户类型  对私户=P；对公户=C
        dr.setPayeeAccountType("P");
        //收款帐户号
        dr.setPayeeAccountNo(rapidlyPayrollBean.getBankNumber());
        //收款帐户名称
        dr.setPayeeAccountName(rapidlyPayrollBean.getCustomerName());
        //订单ID 唯一
        dr.setOutTradeNo(rapidlyPayrollBean.getOrderNo());
        //订单交易金额  单位：分，大于0
        dr.setTradeAmount(rapidlyPayrollBean.getRealWage().multiply(new BigDecimal(100)).longValue() + "");
        //订单摘要  商品描述，订单标题，关键描述信息
        dr.setTradeSubject("发工资自动提现");
        //收款卡种  借记卡=DE；信用卡=CR
        dr.setPayeeCardType("DE");

        //提现
//        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);
        LOGGER.debug("京东提现参数：" + JSON.toJSONString(dr));
        DefrayPayResponse defrayPayResponse = jdPayService.defrayPay(dr);

        if (!StringUtils.equals(defrayPayResponse.getTradeStatus(), CodeConst.TRADE_CLOS)) {
            //工资单是否已发 0:未发 1:已发
            rapidlyPayrollBean.setIsPayOff(1);
            //回调结果 0待回调 1成功 2....各种错误定义
            rapidlyPayrollBean.setCallbackState(Integer.valueOf(0));
        } else {
            //失败 判断是否客户原因
            if (CodeConst.isCustomerReasons(defrayPayResponse.getResponseCode())) {
                //工资单是否已发 0:未发 1:已发
                rapidlyPayrollBean.setIsPayOff(1);
                //支付错误信息
                rapidlyPayrollBean.setFailMsg(defrayPayResponse.getResponseMessage());
                //回调结果 0待回调 1成功 2....各种错误定义
                rapidlyPayrollBean.setCallbackState(Integer.valueOf(2));
            } else {
                //重置订单号
                rapidlyPayrollBean.setOrderNo(UUID.randomUUID().toString());
                //回调结果 0待回调 1成功 2....各种错误定义
                rapidlyPayrollBean.setCallbackState(Integer.valueOf(-1));
                rapidlyPayrollBean.setIsPayOff(0);
            }

            rapidlyPayrollBean.setPayStatus(1);
            rapidlyPayrollBean.setFailMsg(defrayPayResponse.getResponseMessage());
        }
        //更新提现申请
        rapidlyPayrollWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollBean);
    }
}
