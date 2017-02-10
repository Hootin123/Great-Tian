package com.xtr.core.service.customer;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomerMsgsBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.dto.customer.CustomWithDrawlsBatchDto;
import com.xtr.api.dto.customer.CustomerReachrgeDto;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.customer.CustomerMsgsService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomersSupplementService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.CustomerRechargeConstant;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.excel.ExcelDoc;
import com.xtr.comm.excel.ExcelExporter;
import com.xtr.comm.excel.WorkModel;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.core.persistence.reader.company.CompanysReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerRechargesReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerRechargesWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>企业员工充值提现</p>
 *
 * @author 任齐
 * @createTime: 2016/7/8 11:25
 */
@Service("customerRechargesService")
public class CustomerRechargesServiceImpl implements CustomerRechargesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRechargesService.class);

    @Resource
    private CustomerRechargesReaderMapper customerRechargesReaderMapper;

    @Resource
    private CustomerRechargesWriterMapper customerRechargesWriterMapper;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanysReaderMapper companysReaderMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private CustomerMsgsService customerMsgsService;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private CustomersSupplementService customersSupplementService;

    /**
     * 员工提现
     *
     * @param customerRechargesBean
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int customerWithdrawals(CustomerRechargesBean customerRechargesBean) throws BusinessException {
        if (null == customerRechargesBean) {
            throw new BusinessException("个人充值提现参数为空");
        }

        LOGGER.info("接受参数：" + JSON.toJSONString(customerRechargesBean));

        // 记录添加时间
        customerRechargesBean.setRechargeAddtime(new Date());
        // 审核状态
        customerRechargesBean.setRechargeState(0);
        // 是否体现到账户资金变化
        customerRechargesBean.setRechargeIstorecord(0);
        // 是否已经导出
        customerRechargesBean.setRechargeIstoexcel(0);

        // 生成订单号
        String orderId = idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS);
        customerRechargesBean.setRechargeNumber(orderId);

        // 查询提现卡号
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerRechargesBean.getRechargeCustomerId());
        if (null != customersBean) {
            customerRechargesBean.setRechargeBank(customersBean.getCustomerBank());
            customerRechargesBean.setRechargeBanknumber(customersBean.getCustomerBanknumber());
        }

        int result = 0;
        try {

            result = customerRechargesWriterMapper.insert(customerRechargesBean);

            /**
             * 冻结金额
             *
             * 减可用，加冻结
             */
            LOGGER.info("账户金额冻结：客户Id【" + customerRechargesBean.getRechargeCustomerId() + "】  扣款金额【" + customerRechargesBean.getRechargeMoney() + "】");
            subAccountService.frozen(customerRechargesBean.getRechargeCustomerId(), customerRechargesBean.getRechargeMoney(), CustomerRechargeConstant.WITHDRAW_TYPE, customerRechargesBean.getRechargeId(), AccountType.PEOPLE);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = 0;
            throw new BusinessException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据企业id删除
     *
     * @param companyId
     */
    @Override
    public void deleteByCompanyId(Long companyId) {
        if (null != companyId) {
            customerRechargesWriterMapper.deleteByCompanyId(companyId);
        }
    }

    /**
     * 分页查询个人提现批次
     *
     * @param startTime
     * @param state
     * @param auditName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultResponse selectBatchPageList(String startTime, Integer state, String auditName, int pageIndex, int pageSize) {

        ResultResponse resultResponse = new ResultResponse();

        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);

        PageList<CustomWithDrawlsBatchDto> list = customerRechargesReaderMapper.selectBatchPageList(startTime, state, auditName, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 分页查询个人充值提现详情
     *
     * @param customerRechargesBean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultResponse selectPageList(CustomerRechargesBean customerRechargesBean, int pageIndex, int pageSize) {

        ResultResponse resultResponse = new ResultResponse();
        if (null != customerRechargesBean) {
            PageBounds pageBounds = new PageBounds(pageIndex, pageSize);

            PageList<CustomerRechargesBean> list = customerRechargesReaderMapper.selectPageList(customerRechargesBean, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = list.getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
            LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        }
        return resultResponse;
    }

    /**
     * 查询提现列表
     *
     * @param batchNumber
     * @return
     */
    @Override
    public List<CustomerReachrgeDto> selectDetailList(String batchNumber) {
        if (StringUtils.isNotBlank(batchNumber)) {
            return customerRechargesReaderMapper.selectDetailList(batchNumber);
        }
        return null;
    }

    /**
     * 审核提现
     *
     * @param memberId
     * @param auditType
     * @param batchNumber
     * @throws BusinessException
     */
    @Override
    public void auditWithdrawals(Long memberId, Integer auditType, String batchNumber) throws BusinessException {

        if (auditType == 1 && StringUtils.isBlank(batchNumber)) {
            throw new BusinessException("个人提现参数为空");
        }

        if (null == memberId) {
            throw new BusinessException("审核人参数为空");
        }

        List<CustomerReachrgeDto> list = null;
        // 关闭所有审核
        if (auditType == 0) {
            list = customerRechargesReaderMapper.selectDetailList(null);
        }

        // 关闭单批审核
        if (auditType == 1) {
            list = customerRechargesReaderMapper.selectDetailList(batchNumber);
        }

        if (!CollectionUtils.isEmpty(list)) {
            try {
                ExecutorService executorService = Executors.newFixedThreadPool(3);
                for (CustomerRechargesBean item : list) {
                    // 处理每一条提现
                    withDrawals(executorService, item, memberId);
                }
                executorService.shutdown();
            } catch (Exception e) {
                throw new BusinessException(e.getMessage());
            }
        }
    }

    /**
     * 生成一个批次的工资单
     *
     * @param batchNumber
     * @param webRoot
     * @param payTemplatePath
     * @param downloadPath
     * @return
     */
    @Override
    public File generatorSalaryExcel(String batchNumber, String webRoot, String payTemplatePath, String downloadPath) {
        if (StringUtils.isNotBlank(batchNumber)) {

            String payTempaltePath = webRoot + payTemplatePath;

            List<CustomerReachrgeDto> list = customerRechargesReaderMapper.selectDetailList(batchNumber);
            if (!CollectionUtils.isEmpty(list)) {

                List<WorkModel> dataset = new ArrayList<WorkModel>();
                int count = 1;

                Long companyId = 0L;
                for (CustomerReachrgeDto item : list) {

                    String bankNumber = item.getRechargeBanknumber();
                    String bankName = item.getRechargeBank();
                    String uname = item.getUname();
                    String money = item.getRechargeMoney().doubleValue() + "";

                    dataset.add(new WorkModel(count + "", bankNumber, bankName, uname, money, "对私", "借记卡"));
                    count++;
                    if (companyId == 0 && null != item.getCompanyId()) {
                        companyId = item.getCompanyId();
                    }
                }

                CompanysBean companysBean = companysReaderMapper.selectByPrimaryKey(companyId);

                String companyName = companysBean.getCompanyName();

                String filePath = webRoot + downloadPath + "/" + companyName + "_" + System.currentTimeMillis() + ".xls";

                File file = new File(filePath);

                ExcelDoc excelDoc = new ExcelDoc(companyName);
//                ExcelDoc excelDoc = new ExcelDoc(companyName + "_2016年5月员工工资单");
                excelDoc.setTemplateHead(payTempaltePath);
                excelDoc.setStartRow(1);
                excelDoc.setDateSet(dataset);
                excelDoc.setOutPath(file.getPath());

                HSSFWorkbook workbook = ExcelExporter.workbook(excelDoc);
                HSSFCellStyle cellStyle = workbook.createCellStyle();

                HSSFFont font = workbook.createFont();
                font.setFontHeightInPoints((short) 10);
                cellStyle.setFont(font);

                excelDoc.setRowCellStyle(cellStyle);
                excelDoc.setRowHeight((short) 450);

                ExcelExporter.export(excelDoc, workbook);

                return file;
            }
        }
        return null;
    }

    /**
     * 处理提现操作
     *
     * @param customerRechargesBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void withDrawals(ExecutorService executorService, final CustomerRechargesBean customerRechargesBean, Long memberId) throws BusinessException {
        try {

            // 解冻扣款
            subAccountService.thawDeduct(customerRechargesBean.getRechargeCustomerId(), customerRechargesBean.getRechargeMoney(), customerRechargesBean.getRechargeType(),
                    customerRechargesBean.getRechargeId(), AccountType.PEOPLE);

            customerRechargesBean.setRechargeState(CustomerRechargeConstant.WITHDRAW_TYPE);
            customerRechargesBean.setRechargeAuditFirstTime(new Date());
            customerRechargesBean.setRechargeAuditFirstMember(memberId);
            customerRechargesWriterMapper.updateByPrimaryKey(customerRechargesBean);

            // 发送消息
            final CustomersBean customersBean = customersReaderMapper.selectById(customerRechargesBean.getRechargeCustomerId());
            if (null != customersBean) {
                CustomerMsgsBean customerMsgsBean = new CustomerMsgsBean();
                String msgTitle = "提现申请处理成功提醒";
                final String wh = com.xtr.comm.util.StringUtils.getHideCard(customerRechargesBean.getRechargeBanknumber(), 4);
                String msgCont = "您好，您" + customerRechargesBean.getRechargeMoney() + "元提现至" + wh + "账户的申请已处理成功，请耐心等待银行处理。";
                customerMsgsBean.setMsgTitle(msgTitle);//标题
                customerMsgsBean.setMsgCont(msgCont);//消息内容
                customerMsgsBean.setMsgType(3);// 3为账户变动
                customerMsgsBean.setMsgAddtime(new Date());//消息生成时间
                customerMsgsBean.setMsgCustomerId(customerRechargesBean.getRechargeCustomerId());// 员工id
                customerMsgsBean.setMsgCustomerName(customersBean.getCustomerTurename());// 员工姓名
                customerMsgsBean.setMsgSign(1);//状态  1：未读   2：已读  0：删除
                customerMsgsBean.setMsgClass(1);//1：收件箱  2：发件箱
                customerMsgsService.saveMsg(customerMsgsBean);

                // 异步发送短信
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // 发送短信
                        try {
                            String content = "您好，您" + customerRechargesBean.getRechargeMoney() + "元提现到账户" + wh + "的申请已处理成功，银行处理可能需要等待2-4个小时，请耐心等待。";
                            sendMsgService.sendMsg(customersBean.getCustomerPhone(), content);

                            LOGGER.info("员工ID[{}],手机号码[{}],短信内容:[{}]", customerRechargesBean.getRechargeCustomerId(), customersBean.getCustomerPhone(), content);

                        } catch (IOException e) {
                            LOGGER.error("短信发送失败", e);
                        }
                    }
                });

            }
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 根据第三方平台获取所有待审批的提现申请
     *
     * @return
     */
    public List<CustomerRechargesBean> getAllWithdrawals(Integer rechargeStation) {
        return customerRechargesReaderMapper.getAllWithdrawals(rechargeStation);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer_recharges
     *
     * @mbggenerated
     */
    public int updateByPrimaryKeySelective(CustomerRechargesBean record) {
        int result = customerRechargesWriterMapper.updateByPrimaryKeySelective(record);
        //解冻返还
        if (result > 0) {
            subAccountService.backwash(record.getRechargeCustomerId(), record.getRechargeMoney(), CustomerRechargeConstant.BACKWASH, record.getRechargeId(), AccountType.PEOPLE);
        }
        return customerRechargesWriterMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    @SystemServiceLog(operation = "处理京东回调", modelName = "员工提现")
    public void doJdResponse(NotifyResponse response) {
        LOGGER.info("京东回调：充值处理----" + response);
        if (response == null || response.getBusinessType() != BusinessType.CUSTOMER_WITHDRAW) {
            return;
        }
        CustomerRechargesBean rechargesBean = customerRechargesReaderMapper.selectByNumber(response.getOutTradeNo());

        if(rechargesBean.getRechargeState() != 2) {//订单未处于待回调状态
            LOGGER.error("订单未处于待回调状态且非退款交易接收回调：" + response.getOutTradeNo());
            //return;
        }

        String tradeStatus = response.getTradeStatus();
        String tradeRespcode = response.getTradeRespcode();
        String tradeRespmsg = response.getTradeRespmsg();

        rechargesBean.setRechargeRecallTime(new Date());

        LOGGER.info("tradeStatus:" + tradeStatus);
        if (CodeConst.TRADE_WPAR.equals(tradeStatus) || CodeConst.TRADE_BUID.equals(tradeStatus) || CodeConst.TRADE_ACSU.equals(tradeStatus)) {
            LOGGER.info("订单等待中，暂不处理");
        } else if (CodeConst.TRADE_FINI.equals(tradeStatus)) {//交易成功
            LOGGER.info("订单交易成功");
            // 避免重复审核
            if (rechargesBean.getRechargeRecallResult() == 0) {
                // 更新订单状态
                rechargesBean.setRechargeRecallResult(1);
                customerRechargesWriterMapper.updateByPrimaryKey(rechargesBean);
                //解冻并扣款
//                subAccountService.thawDeduct(rechargesBean.getRechargeCustomerId(), rechargesBean.getRechargeMoney(),
//                        CompanyRechargeConstant.WITHDRAW_TYPE, rechargesBean.getRechargeId(), AccountType.PEOPLE);
            }

        } else if (CodeConst.TRADE_REFUND.equals(tradeStatus)) {
            //成功后退款处理
            LOGGER.info("订单退款");
            LOGGER.info("tradeRespcode:" + tradeRespcode + "  message:" + tradeRespmsg);

            //if (rechargesBean.getRechargeRecallResult() == 1) {
                //退款处理  还原订单状态
                rechargesBean.setRechargeRecallResult(2);
                rechargesBean.setRechargeState(1);
                customerRechargesWriterMapper.updateByPrimaryKey(rechargesBean);

                //退款金额
                BigDecimal refundAmount = new BigDecimal(response.getRefundAmount());
                refundAmount = refundAmount.divide(new BigDecimal(100));

                //充值退款金额
//                subAccountService.rechargeRecords(rechargesBean.getRechargeCustomerId(), refundAmount, 9, rechargesBean.getRechargeId(), 1);
//                //冻结退款金额
//                subAccountService.frozen(rechargesBean.getRechargeCustomerId(), refundAmount, 9, rechargesBean.getRechargeId(), 1);

                //g更新工资单状态
                if(new Integer(2).equals(rechargesBean.getRechargeType())){
                    Long resourceId = rechargesBean.getResourceId();
                    CustomerPayrollBean customerPayrollBean = new CustomerPayrollBean();
                    customerPayrollBean.setId(resourceId);
                    customerPayrollBean.setPayStatus(1);
                    customerPayrollBean.setFailMsg("退款金额：" + refundAmount + "，原因：" + tradeRespmsg);
                    customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
                }else if(new Integer(8).equals(rechargesBean.getRechargeType())){
                    Long resourceId = rechargesBean.getResourceId();
//                    CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();
//                    companyShebaoOrderBean.setId(resourceId);
//                    companyShebaoOrderBean.setShebaoCustomerPayStatus(1);
//                    companyShebaoOrderBean.setShebaoFailMsg("退款金额：" + refundAmount + "，原因：" + tradeRespmsg);
//                    companyShebaoService.updateByPrimaryKeySelective(companyShebaoOrderBean);

                    //更新失败信息
                    CustomersSupplementBean customersSupplementBean=new CustomersSupplementBean();
                    customersSupplementBean.setSupplementId(resourceId);
                    customersSupplementBean.setSupplementFailMsg("退款金额：" + refundAmount + "，原因：" + tradeRespmsg);
                    customersSupplementBean.setSupplementPayStatus(1);
                    customersSupplementService.updateByPrimaryKeySelective(customersSupplementBean);
                }
            //}
        } else {
            LOGGER.info("订单失败");

            LOGGER.info("tradeRespcode:" + tradeRespcode + "  message:" + tradeRespmsg);
            if (CodeConst.isCustomerReasons(tradeRespcode) || (tradeRespmsg != null && tradeRespmsg.indexOf("可用余额不足") >= 0)) {//客户原因造成无法体现，不再体现，包括“薪太软在京东账户的余额不足”的情况
                LOGGER.info("支付信息有误，");
                //支付信息有误，解冻处理
                if (rechargesBean.getRechargeRecallResult() == 0) {
                    // 更新订单状态
                    rechargesBean.setRechargeRecallResult(2);
                    rechargesBean.setRechargeState(1);//1初审失败 支付失败不再支付，解冻金额
                    rechargesBean.setRechargeAuditFirstRemark(tradeRespmsg);
                    customerRechargesWriterMapper.updateByPrimaryKey(rechargesBean);
                    //解冻处理
//                    subAccountService.backwash(rechargesBean.getRechargeCustomerId(), rechargesBean.getRechargeMoney(), CustomerRechargeConstant.BACKWASH, rechargesBean.getRechargeId(), 1);
                }
            } else {
                //不做处理
                rechargesBean.setRechargeRecallResult(0);
                rechargesBean.setRechargeState(0);//重新发起
                rechargesBean.setRechargeAuditFirstRemark(tradeRespmsg);
                //重置订单号
//                rechargesBean.setRechargeNumber(UUID.randomUUID().toString());
                rechargesBean.setRechargeNumber(idGeneratorService.getOrderId(BusinessEnum.PERSONAL_WITHDRAWALS));
                customerRechargesWriterMapper.updateByPrimaryKey(rechargesBean);
            }
            //g更新工资单状态
            if(new Integer(2).equals(rechargesBean.getRechargeType())) {
                Long resourceId = rechargesBean.getResourceId();
                CustomerPayrollBean customerPayrollBean = new CustomerPayrollBean();
                customerPayrollBean.setId(resourceId);
                customerPayrollBean.setPayStatus(1);
                customerPayrollBean.setFailMsg(tradeRespmsg);
                customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
            }else if(new Integer(8).equals(rechargesBean.getRechargeType())){
                Long resourceId = rechargesBean.getResourceId();
//                CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();
//                companyShebaoOrderBean.setId(resourceId);
//                companyShebaoOrderBean.setShebaoCustomerPayStatus(1);
//                companyShebaoOrderBean.setShebaoFailMsg(tradeRespmsg);
//                companyShebaoService.updateByPrimaryKeySelective(companyShebaoOrderBean);

                //更新失败信息
                CustomersSupplementBean customersSupplementBean=new CustomersSupplementBean();
                customersSupplementBean.setSupplementId(resourceId);
                customersSupplementBean.setSupplementFailMsg(tradeRespmsg);
                customersSupplementBean.setSupplementPayStatus(1);
                customersSupplementService.updateByPrimaryKeySelective(customersSupplementBean);
            }
        }
    }

    /**
     *根据第三方平台获取所有社保公积金补退的提现申请
     * @param rechargeStation
     * @return
     */
    public List<CustomerRechargesBean> getBackWithdrawals(Integer rechargeStation){
        return customerRechargesReaderMapper.getBackWithdrawals(rechargeStation);
    }
}
