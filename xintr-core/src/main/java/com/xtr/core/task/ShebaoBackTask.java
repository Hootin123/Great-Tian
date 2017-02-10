package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomersSupplementService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.GrantStateConstant;
import com.xtr.comm.constant.RecordSourceConstant;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.customer.CustomersSupplementReaderMapper;
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
 * Created by allycw3 on 2016/10/9.
 */
@Service("shebaoBackTask")
public class ShebaoBackTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShebaoBackTask.class);

    @Resource
    private CustomersSupplementService customersSupplementService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CustomerRechargesService customerRechargesService;
    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        String sameDay = DateUtil.formatCurrentDate();
        //获取工资单状态为发放中的数据
        LOGGER.info("开始执行【" + sameDay + "】社保工资金补退任务");
        //获取所有符合补退的员工补退信息
        List<CustomersSupplementBean> list = customersSupplementService.selectBackInfoByCompanyOrderId();
        if (!list.isEmpty()) {
            LOGGER.info("【" + sameDay + "】需要社保公积金补退的数量【" + list.size() + "】");
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            BigDecimal zeroDecimal=new BigDecimal("0");
            for (final CustomersSupplementBean customersSupplementBean : list) {
                BigDecimal sbSelfBack=customersSupplementBean.getSupplementSbSelfBack()==null?zeroDecimal:customersSupplementBean.getSupplementSbSelfBack();
                BigDecimal gjjSelfBack=customersSupplementBean.getSupplementGjjSelfBack()==null?zeroDecimal:customersSupplementBean.getSupplementGjjSelfBack();
                customersSupplementBean.setSupplementBackTotal(sbSelfBack.add(gjjSelfBack));
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //补退
                            payday(customersSupplementBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("ID【" + customersSupplementBean.getSupplementId() + "】出现异常···" + e.getMessage(), e);
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
        LOGGER.info("结束执行【" + sameDay + "】社保公积金补退任务");
    }
    /**
     * 社保公积金补退
     */
    public void payday(final CustomersSupplementBean customersSupplementBean) {
        LOGGER.info("社保公积金补退参数：" + JSON.toJSONString(customersSupplementBean));
        //获取员工信息  账户性质 1-个人 2-企业
        SubAccountBean subAccountBean = subAccountService.selectByCustId(customersSupplementBean.getSupplementCustomerId(), 1);
        if (subAccountBean != null) {
            //给企业员工发工资
            LOGGER.info("用户充值ID：" + customersSupplementBean.getSupplementCustomerId() + "   " + JSON.toJSONString(customersSupplementBean));
            subAccountService.rechargeRecords(customersSupplementBean.getSupplementCustomerId(), customersSupplementBean.getSupplementBackTotal(), RecordSourceConstant.RECORD_SOURCE_1, customersSupplementBean.getSupplementId(), AccountType.PEOPLE);
            //判断员工是否设置自动提现  是否自动转入 0:非自动 1;自动
            LOGGER.info("是否自动提现:" + subAccountBean.getIsAutoTransfer().intValue());
            if (subAccountBean.getIsAutoTransfer().intValue() == 1) {
                CustomerRechargesBean customerRechargesBean = new CustomerRechargesBean();
                //用户id
                customerRechargesBean.setRechargeCustomerId(customersSupplementBean.getSupplementCustomerId());
                //批次号
                String batchNo = UUID.randomUUID().toString();
                customerRechargesBean.setBatchNumber(batchNo);
                //操作类型 类型 1充值 2提现 3发工资 8社保公积金
                customerRechargesBean.setRechargeType(CompanyRechargeConstant.SOCIAL_TYPE);
                //第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
                customerRechargesBean.setRechargeStation(Integer.valueOf(2));
                //注备
                customerRechargesBean.setRechargeBak("社保公积金补退自动提现");
                //提现金额
                customerRechargesBean.setRechargeMoney(customersSupplementBean.getSupplementBackTotal());
                //来源id
                customerRechargesBean.setResourceId(customersSupplementBean.getSupplementId());
                LOGGER.info("提现申请:" + JSON.toJSONString(customerRechargesBean));
                //发起提现申请
                customerRechargesService.customerWithdrawals(customerRechargesBean);
                //解冻扣款
                subAccountService.thawDeduct(customerRechargesBean.getRechargeCustomerId(), customerRechargesBean.getRechargeMoney(), RecordSourceConstant.RECORD_SOURCE_2, customerRechargesBean.getRechargeId(), AccountType.PEOPLE);
            }
            //更新是否补退状态  是否补退到员工账户 1 是 2 否
            customersSupplementBean.setSupplementIsBack(1);
            customersSupplementService.updateByPrimaryKeySelective(customersSupplementBean);
        } else {
            LOGGER.info("用户ID：" + customersSupplementBean.getSupplementCustomerId() + "账户不存在，无法进行社保公积金补退");
        }
    }
}
