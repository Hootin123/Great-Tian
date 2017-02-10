package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.salary.RapidlyPayrollService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.core.persistence.reader.salary.RapidlyPayrollExcelReaderMapper;
import com.xtr.core.persistence.reader.salary.RapidlyPayrollReaderMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollExcelWriterMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/10/28 10:16
 */
@Service("rapidlyPayrollService")
public class RapidlyPayrollServiceImpl implements RapidlyPayrollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidlyPayrollServiceImpl.class);

    @Resource
    private RapidlyPayrollWriterMapper rapidlyPayrollWriterMapper;

    @Resource
    private RapidlyPayrollReaderMapper rapidlyPayrollReaderMapper;

    @Resource
    private RapidlyPayrollExcelWriterMapper rapidlyPayrollExcelWriterMapper;

    @Resource
    private RapidlyPayrollExcelReaderMapper rapidlyPayrollExcelReaderMapper;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CompanyRechargesService companyRechargesService;


    /**
     * 批量插入工资单
     *
     * @param record
     */
    public int batchInsert(RapidlyPayrollExcelBean record) {
        List<RapidlyPayrollBean> list = record.getPayrollList();
        if (list != null && !list.isEmpty()) {
            List<RapidlyPayrollBean> arrayList = new ArrayList<>();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                list.get(i).setCompanyId(record.getCompanyId());
                list.get(i).setRapidlyPayrollExcelId(record.getId());
                list.get(i).setCreateTime(new Date());
                list.get(i).setOrderNo(UUID.randomUUID().toString());
                //工资单是否已发 0:未发 1:已发
                list.get(i).setIsPayOff(0);
                arrayList.add(list.get(i));
                if (i % 1000 == 0 || i == size - 1) {
                    rapidlyPayrollWriterMapper.batchInsert(arrayList);
                    arrayList.clear();
                }
            }
            return size;
        }
        return 0;
    }

    /**
     * 分页查询工资单
     *
     * @param rapidlyPayrollBean
     * @return
     */
    public ResultResponse selectPageList(RapidlyPayrollBean rapidlyPayrollBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageList<RapidlyPayrollBean> list = rapidlyPayrollReaderMapper.selectPageList(rapidlyPayrollBean, new PageBounds(rapidlyPayrollBean.getPageIndex(), rapidlyPayrollBean.getPageSize()));
        resultResponse.setPaginator(list.getPaginator());
        resultResponse.setData(list);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 通过companyId,excelId查询所有工资单
     *
     * @param excelId
     * @param companyId
     * @return
     */
    public List<RapidlyPayrollBean> selectByExcelId(Long companyId, Long excelId) {
        return rapidlyPayrollReaderMapper.selectByExcelId(companyId, excelId);
    }

    /**
     * 生成工资单并生成充值订单
     *
     * @param rapidlyPayrollExcelBean
     * @return
     */
    public ResultResponse payOff(RapidlyPayrollExcelBean rapidlyPayrollExcelBean) {
        ResultResponse resultResponse = new ResultResponse();
        //检查工资单标题
        List<RapidlyPayrollExcelBean> list = rapidlyPayrollExcelReaderMapper.selectByExcelTitle(rapidlyPayrollExcelBean.getId(),rapidlyPayrollExcelBean.getCompanyId(),rapidlyPayrollExcelBean.getExcelTitle());
        if (list != null && !list.isEmpty()){
            throw new BusinessException("已存在工资表标题【"+rapidlyPayrollExcelBean.getExcelTitle()+"】");
        }
        //检查企业协议状态
        payrollAccountService.checkCompany(rapidlyPayrollExcelBean.getCompanyId());
        //是否生成工资单 0:未生成 1:已生成
        rapidlyPayrollExcelBean.setIsGeneratePayroll(1);
        //工资单状态 0:未发放 1:待发放  2:发放中  3:已发放
        rapidlyPayrollExcelBean.setGrantState(1);
        //更新工资单
        rapidlyPayrollExcelWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollExcelBean);
        //批量生成工资单
        batchInsert(rapidlyPayrollExcelBean);
        //生成订单
        companyRechargesService.generateOrder(rapidlyPayrollExcelBean.getCompanyId(),
                rapidlyPayrollExcelBean.getId(),
                CompanyRechargeConstant.RAPIDLY_PAYROLL_TYPE,
                rapidlyPayrollExcelBean.getTotalWages(),
                new BigDecimal(0),
                rapidlyPayrollExcelBean.getExcelTitle());
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 快速发工资员工体现京东回调处理
     *
     * @param response
     */
    @SystemServiceLog(operation = "处理京东回调", modelName = "急速发工资员工提现")
    public void doJdResponse(NotifyResponse response) {
        if (response != null && response.getBusinessType() == BusinessType.RAPIDLY_WITHDRAW) {
            LOGGER.info("京东回调：快速发工资提现处理----" + JSON.toJSONString(response));

            String tradeStatus = response.getTradeStatus();
            if (CodeConst.TRADE_WPAR.equals(tradeStatus) || CodeConst.TRADE_BUID.equals(tradeStatus) || CodeConst.TRADE_ACSU.equals(tradeStatus)) {
                LOGGER.info("急速发工资订单等待中，暂不处理");
            } else {
                String tradeRespcode = response.getTradeRespcode();
                String tradeRespmsg = response.getTradeRespmsg();
                //获取工资单
                RapidlyPayrollBean rapidlyPayrollBean = rapidlyPayrollReaderMapper.selectByOrderNo(response.getOutTradeNo());
                if (rapidlyPayrollBean == null){
                    LOGGER.error("交易记录为找到OutTradeNo=" + response.getOutTradeNo());
                    return;
                }

                if(rapidlyPayrollBean.getCallbackState() != 0 && !CodeConst.TRADE_REFUND.equals(tradeStatus)) {//订单未处于待回调状态且非退款交易不接收回调
                    LOGGER.error("订单未处于待回调状态且非退款交易接收回调：" + response.getOutTradeNo());
                    //return;
                }

                if (CodeConst.TRADE_FINI.equals(tradeStatus)) {//交易成功
                    LOGGER.info("急速发工资订单交易成功");
                    //更新工资单回调状态  回调状态  0待回调 1成功 2....各种错误定义
                    rapidlyPayrollBean.setCallbackState(1);
                } else if (CodeConst.TRADE_REFUND.equals(tradeStatus)) {
                    //成功后退款处理
                    LOGGER.info("急速发工资订单退款");
                    LOGGER.info("tradeRespcode:" + tradeRespcode + "  message:" + tradeRespmsg);
                    //退款金额
                    BigDecimal refundAmount = new BigDecimal(response.getRefundAmount());
                    refundAmount = refundAmount.divide(new BigDecimal(100));
                    //更新工资单回调状态  回调状态  0待回调 1成功 2....各种错误定义
                    rapidlyPayrollBean.setCallbackState(2);
                    rapidlyPayrollBean.setPayStatus(1);
                    rapidlyPayrollBean.setFailMsg("退款金额：" + refundAmount + "，原因：" + tradeRespmsg);
                } else {
                    LOGGER.info("急速发工资订单失败");
                    LOGGER.info("tradeRespcode:" + tradeRespcode + "  message:" + tradeRespmsg);
                    if (CodeConst.isCustomerReasons(tradeRespcode)  || (tradeRespmsg != null && tradeRespmsg.indexOf("可用余额不足") >= 0)) {//客户原因造成无法体现，不再体现，包括“薪太软在京东账户的余额不足”的情况
                        LOGGER.info("支付信息有误");
                        //更新工资单回调状态  回调状态  0待回调 1成功 2....各种错误定义
                        rapidlyPayrollBean.setCallbackState(2);
                    } else {//我司原因，需重新发起提现
                        //更新工资单回调状态  回调状态  0待回调 1成功 2....各种错误定义
                        rapidlyPayrollBean.setCallbackState(-1);
                        //工资单是否已发 0:未发 1:已发
                        rapidlyPayrollBean.setIsPayOff(0);
                        //更新订单号
                        rapidlyPayrollBean.setOrderNo(UUID.randomUUID().toString());
                        //更新工资单文档Excel工资单发放状态
                        RapidlyPayrollExcelBean rapidlyPayrollExcelBean = new RapidlyPayrollExcelBean();
                        //主键
                        rapidlyPayrollExcelBean.setId(rapidlyPayrollBean.getRapidlyPayrollExcelId());
                        //工资单发放状态 0:未发放 1:待发放  2:发放中  3:已发放
                        rapidlyPayrollExcelBean.setGrantState(2);
                        rapidlyPayrollExcelWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollExcelBean);
                    }
                    rapidlyPayrollBean.setPayStatus(1);
                    rapidlyPayrollBean.setFailMsg(tradeRespmsg);
                }
                //更新工资单
                rapidlyPayrollWriterMapper.updateByPrimaryKeySelective(rapidlyPayrollBean);
            }


        }
    }
}
