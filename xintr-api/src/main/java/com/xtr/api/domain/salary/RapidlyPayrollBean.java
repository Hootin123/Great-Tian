package com.xtr.api.domain.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RapidlyPayrollBean extends BaseObject implements Serializable{
    /**
     *  主键,所属表字段为rapidly_payroll.id
     */
    private Long id;

    /**
     *  公司主键,所属表字段为rapidly_payroll.company_id
     */
    private Long companyId;

    /**
     *  客户姓名,所属表字段为rapidly_payroll.customer_name
     */
    private String customerName;

    /**
     *  身份证号,所属表字段为rapidly_payroll.id_card
     */
    private String idCard;

    /**
     *  开户行,所属表字段为rapidly_payroll.bank_account
     */
    private String bankAccount;

    /**
     *  工资卡号,所属表字段为rapidly_payroll.bank_number
     */
    private String bankNumber;

    /**
     *  实发金额(元),所属表字段为rapidly_payroll.real_wage
     */
    private BigDecimal realWage;

    /**
     *  急速发工资excelId,所属表字段为rapidly_payroll.rapidly_payroll_excel_id
     */
    private Long rapidlyPayrollExcelId;

    /**
     *  工资单是否已发 0:未发 1:已发,所属表字段为rapidly_payroll.is_pay_off
     */
    private Integer isPayOff;

    /**
     *  财务操作人,所属表字段为rapidly_payroll.finance_user
     */
    private Long financeUser;

    /**
     *  支付错误信息,所属表字段为rapidly_payroll.fail_msg
     */
    private String failMsg;

    /**
     *  支付状态  1失败过,所属表字段为rapidly_payroll.pay_status
     */
    private Integer payStatus;

    /**
     *  创建时间,所属表字段为rapidly_payroll.create_time
     */
    private Date createTime;

    /**
     *  备注,所属表字段为rapidly_payroll.remark
     */
    private String remark;

    /**
     * 提现订单号
     */
    private String orderNo;

    /**
     * 回调状态  0待回调 1成功 2....各种错误定义
     */
    private Integer callbackState;


    /**
     * 获取 主键 字段:rapidly_payroll.id
     *
     * @return rapidly_payroll.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:rapidly_payroll.id
     *
     * @param id rapidly_payroll.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 公司主键 字段:rapidly_payroll.company_id
     *
     * @return rapidly_payroll.company_id, 公司主键
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 公司主键 字段:rapidly_payroll.company_id
     *
     * @param companyId rapidly_payroll.company_id, 公司主键
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 客户姓名 字段:rapidly_payroll.customer_name
     *
     * @return rapidly_payroll.customer_name, 客户姓名
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置 客户姓名 字段:rapidly_payroll.customer_name
     *
     * @param customerName rapidly_payroll.customer_name, 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    /**
     * 获取 身份证号 字段:rapidly_payroll.id_card
     *
     * @return rapidly_payroll.id_card, 身份证号
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置 身份证号 字段:rapidly_payroll.id_card
     *
     * @param idCard rapidly_payroll.id_card, 身份证号
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    /**
     * 获取 开户行 字段:rapidly_payroll.bank_account
     *
     * @return rapidly_payroll.bank_account, 开户行
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * 设置 开户行 字段:rapidly_payroll.bank_account
     *
     * @param bankAccount rapidly_payroll.bank_account, 开户行
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    /**
     * 获取 工资卡号 字段:rapidly_payroll.bank_number
     *
     * @return rapidly_payroll.bank_number, 工资卡号
     */
    public String getBankNumber() {
        return bankNumber;
    }

    /**
     * 设置 工资卡号 字段:rapidly_payroll.bank_number
     *
     * @param bankNumber rapidly_payroll.bank_number, 工资卡号
     */
    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber == null ? null : bankNumber.trim();
    }

    /**
     * 获取 实发金额(元) 字段:rapidly_payroll.real_wage
     *
     * @return rapidly_payroll.real_wage, 实发金额(元)
     */
    public BigDecimal getRealWage() {
        return realWage;
    }

    /**
     * 设置 实发金额(元) 字段:rapidly_payroll.real_wage
     *
     * @param realWage rapidly_payroll.real_wage, 实发金额(元)
     */
    public void setRealWage(BigDecimal realWage) {
        this.realWage = realWage;
    }

    /**
     * 获取 急速发工资excelId 字段:rapidly_payroll.rapidly_payroll_excel_id
     *
     * @return rapidly_payroll.rapidly_payroll_excel_id, 急速发工资excelId
     */
    public Long getRapidlyPayrollExcelId() {
        return rapidlyPayrollExcelId;
    }

    /**
     * 设置 急速发工资excelId 字段:rapidly_payroll.rapidly_payroll_excel_id
     *
     * @param rapidlyPayrollExcelId rapidly_payroll.rapidly_payroll_excel_id, 急速发工资excelId
     */
    public void setRapidlyPayrollExcelId(Long rapidlyPayrollExcelId) {
        this.rapidlyPayrollExcelId = rapidlyPayrollExcelId;
    }

    /**
     * 获取 工资单是否已发 0:未发 1:已发 字段:rapidly_payroll.is_pay_off
     *
     * @return rapidly_payroll.is_pay_off, 工资单是否已发 0:未发 1:已发
     */
    public Integer getIsPayOff() {
        return isPayOff;
    }

    /**
     * 设置 工资单是否已发 0:未发 1:已发 字段:rapidly_payroll.is_pay_off
     *
     * @param isPayOff rapidly_payroll.is_pay_off, 工资单是否已发 0:未发 1:已发
     */
    public void setIsPayOff(Integer isPayOff) {
        this.isPayOff = isPayOff;
    }

    /**
     * 获取 财务操作人 字段:rapidly_payroll.finance_user
     *
     * @return rapidly_payroll.finance_user, 财务操作人
     */
    public Long getFinanceUser() {
        return financeUser;
    }

    /**
     * 设置 财务操作人 字段:rapidly_payroll.finance_user
     *
     * @param financeUser rapidly_payroll.finance_user, 财务操作人
     */
    public void setFinanceUser(Long financeUser) {
        this.financeUser = financeUser;
    }

    /**
     * 获取 支付错误信息 字段:rapidly_payroll.fail_msg
     *
     * @return rapidly_payroll.fail_msg, 支付错误信息
     */
    public String getFailMsg() {
        return failMsg;
    }

    /**
     * 设置 支付错误信息 字段:rapidly_payroll.fail_msg
     *
     * @param failMsg rapidly_payroll.fail_msg, 支付错误信息
     */
    public void setFailMsg(String failMsg) {
        this.failMsg = failMsg == null ? null : failMsg.trim();
    }

    /**
     * 获取 支付状态  1失败过 字段:rapidly_payroll.pay_status
     *
     * @return rapidly_payroll.pay_status, 支付状态  1失败过
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置 支付状态  1失败过 字段:rapidly_payroll.pay_status
     *
     * @param payStatus rapidly_payroll.pay_status, 支付状态  1失败过
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取 创建时间 字段:rapidly_payroll.create_time
     *
     * @return rapidly_payroll.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 字段:rapidly_payroll.create_time
     *
     * @param createTime rapidly_payroll.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取 备注 字段:rapidly_payroll.remark
     *
     * @return rapidly_payroll.remark, 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 备注 字段:rapidly_payroll.remark
     *
     * @param remark rapidly_payroll.remark, 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getCallbackState() {
        return callbackState;
    }

    public void setCallbackState(Integer callbackState) {
        this.callbackState = callbackState;
    }
}