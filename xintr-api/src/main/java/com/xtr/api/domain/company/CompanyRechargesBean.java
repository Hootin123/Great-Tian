package com.xtr.api.domain.company;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.xtr.api.basic.BaseObject;

/**
 * 
 * <p>企业充值提现</p>
 *
 * @author 任齐
 * @createTime: 2016/06/29 上午10:26:48
 */
public class CompanyRechargesBean extends BaseObject implements Serializable {
    /**
     *  id,所属表字段为company_recharges.recharge_id
     */
    private Long rechargeId;

    /**
     *  类型 1充值 2提现 3发工资 8缴社保,所属表字段为company_recharges.recharge_type
     */
    private Integer rechargeType;

    /**
     *  充值金额,所属表字段为company_recharges.recharge_money
     */
    private BigDecimal rechargeMoney;

    /**
     *  企业id,所属表字段为company_recharges.recharge_company_id
     */
    private Long rechargeCompanyId;

    /**
     * 工资单id,所属表字段为company_recharges.excel_id
     */
    private Long excelId;

    /**
     *  创建时间,所属表字段为company_recharges.recharge_addtime
     */
    private Date rechargeAddtime;

    /**
     *  订单号,所属表字段为company_recharges.recharge_number
     */
    private String rechargeNumber;

    /**
     *  银行流水号,所属表字段为company_recharges.recharge_serial_number
     */
    private String rechargeSerialNumber;

    /**
     *  注备，如充值平台等,所属表字段为company_recharges.recharge_bak
     */
    private String rechargeBak;

    /**
     *  支付渠道 0没有(提现) 1连连 2京东 3易宝 4网银 5账户余额支付 6银行汇款
     */
    private Integer rechargeStation;

    /**
     *  客户端 1web 2wap 3ios 4android,所属表字段为company_recharges.recharge_client
     */
    private Integer rechargeClient;

    /**
     *  充值的钱服务器,所属表字段为company_recharges.recharge_moneyserver
     */
    private BigDecimal rechargeMoneyserver;

    /**
     *  充值银行,所属表字段为company_recharges.recharge_bank
     */
    private String rechargeBank;

    /**
     *  充值的银行区,所属表字段为company_recharges.recharge_bankarea
     */
    private String rechargeBankarea;

    /**
     *  充值银行账号,所属表字段为company_recharges.recharge_banknumber
     */
    private String rechargeBanknumber;

    /**
     *  充值的钱现在,所属表字段为company_recharges.recharge_moneynow
     */
    private BigDecimal rechargeMoneynow;

    /**
     *  是否导出 0否 1是,所属表字段为company_recharges.recharge_istoexcel
     */
    private Integer rechargeIstoexcel;

    /**
     *  系统补贴,所属表字段为company_recharges.recharge_moneybt
     */
    private BigDecimal rechargeMoneybt;

    /**
     *  是否体现到账户资金变化 0否 1是,所属表字段为company_recharges.recharge_istorecord
     */
    private Integer rechargeIstorecord;

    /**
     *  回调时间(成功/失败) 充值才有,所属表字段为company_recharges.recharge_recall_time
     */
    private Date rechargeRecallTime;

    /**
     *  回调结果 0成功 1后面是各种错误,所属表字段为company_recharges.recharge_recall_result
     */
    private Integer rechargeRecallResult;

    /**
     *  状态 0提交订单 1交易失败 2交易成功
     */
    private Integer rechargeState;

    /**
     *  初审时间,所属表字段为company_recharges.recharge_audit_first_time
     */
    private Date rechargeAuditFirstTime;

    /**
     *  初审人,所属表字段为company_recharges.recharge_audit_first_member
     */
    private Long rechargeAuditFirstMember;

    /**
     *  复审时间,所属表字段为company_recharges.recharge_audit_second_time
     */
    private Date rechargeAuditSecondTime;

    /**
     *  复审人,所属表字段为company_recharges.recharge_audit_second_member
     */
    private Long rechargeAuditSecondMember;


    /**
     * 银行 账户名称
     */
    private String bankAccountName;


    /**
     * 银行 开户支行
     */
    private String bankSubbranch;

    /**
     *  初审备注,所属表字段为customer_recharges.recharge_audit_first_remark
     */
    private String rechargeAuditFirstRemark;
    //订单名称
    private String rechargeName;
    //垫付比例
    private BigDecimal rechargePaymentRate;
    //垫付金额
    private BigDecimal rechargePaymentAmount;
    //实际支付金额
    private BigDecimal rechargeRealAmount;
    //红包金额
    private BigDecimal redWalletAmount;
    //可用余额
    private BigDecimal canUserCashAmount;
    //红包ID
    private Long rechargeRedId;

    public Long getRechargeRedId() {
        return rechargeRedId;
    }

    public void setRechargeRedId(Long rechargeRedId) {
        this.rechargeRedId = rechargeRedId;
    }

    public BigDecimal getCanUserCashAmount() {
        return canUserCashAmount;
    }

    public void setCanUserCashAmount(BigDecimal canUserCashAmount) {
        this.canUserCashAmount = canUserCashAmount;
    }

    public BigDecimal getRedWalletAmount() {
        return redWalletAmount;
    }

    public void setRedWalletAmount(BigDecimal redWalletAmount) {
        this.redWalletAmount = redWalletAmount;
    }

    public String getRechargeName() {
        return rechargeName;
    }

    public void setRechargeName(String rechargeName) {
        this.rechargeName = rechargeName;
    }

    public BigDecimal getRechargePaymentRate() {
        return rechargePaymentRate;
    }

    public void setRechargePaymentRate(BigDecimal rechargePaymentRate) {
        this.rechargePaymentRate = rechargePaymentRate;
    }

    public BigDecimal getRechargePaymentAmount() {
        return rechargePaymentAmount;
    }

    public void setRechargePaymentAmount(BigDecimal rechargePaymentAmount) {
        this.rechargePaymentAmount = rechargePaymentAmount;
    }

    public BigDecimal getRechargeRealAmount() {
        return rechargeRealAmount;
    }

    public void setRechargeRealAmount(BigDecimal rechargeRealAmount) {
        this.rechargeRealAmount = rechargeRealAmount;
    }

    /**
     *  获取 id 字段:company_recharges.recharge_id
     *
     *  @return company_recharges.recharge_id, id
     */
    public Long getRechargeId() {
        return rechargeId;
    }

    /**
     *  设置 id 字段:company_recharges.recharge_id
     *
     *  @param rechargeId company_recharges.recharge_id, id
     */
    public void setRechargeId(Long rechargeId) {
        this.rechargeId = rechargeId;
    }

    /**
     *  获取 类型 1充值 2提现 8缴社保 字段:company_recharges.recharge_type
     *
     *  @return company_recharges.recharge_type, 类型 1充值 2提现 8缴社保
     */
    public Integer getRechargeType() {
        return rechargeType;
    }

    /**
     *  设置 类型 1充值 2提现 8缴社保 字段:company_recharges.recharge_type
     *
     *  @param rechargeType company_recharges.recharge_type, 类型 1充值 2提现 8缴社保
     */
    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    /**
     *  获取 充值金额 字段:company_recharges.recharge_money
     *
     *  @return company_recharges.recharge_money, 充值金额
     */
    public BigDecimal getRechargeMoney() {
        return rechargeMoney;
    }

    /**
     *  设置 充值金额 字段:company_recharges.recharge_money
     *
     *  @param rechargeMoney company_recharges.recharge_money, 充值金额
     */
    public void setRechargeMoney(BigDecimal rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    /**
     *  获取 企业id 字段:company_recharges.recharge_company_id
     *
     *  @return company_recharges.recharge_company_id, 企业id
     */
    public Long getRechargeCompanyId() {
        return rechargeCompanyId;
    }

    /**
     *  设置 企业id 字段:company_recharges.recharge_company_id
     *
     *  @param rechargeCompanyId company_recharges.recharge_company_id, 企业id
     */
    public void setRechargeCompanyId(Long rechargeCompanyId) {
        this.rechargeCompanyId = rechargeCompanyId;
    }

    /**
     *  获取 创建时间 字段:company_recharges.recharge_addtime
     *
     *  @return company_recharges.recharge_addtime, 创建时间
     */
    public Date getRechargeAddtime() {
        return rechargeAddtime;
    }

    /**
     *  设置 创建时间 字段:company_recharges.recharge_addtime
     *
     *  @param rechargeAddtime company_recharges.recharge_addtime, 创建时间
     */
    public void setRechargeAddtime(Date rechargeAddtime) {
        this.rechargeAddtime = rechargeAddtime;
    }

    /**
     *  获取 订单号 字段:company_recharges.recharge_number
     *
     *  @return company_recharges.recharge_number, 订单号
     */
    public String getRechargeNumber() {
        return rechargeNumber;
    }

    /**
     *  设置 订单号 字段:company_recharges.recharge_number
     *
     *  @param rechargeNumber company_recharges.recharge_number, 订单号
     */
    public void setRechargeNumber(String rechargeNumber) {
        this.rechargeNumber = rechargeNumber == null ? null : rechargeNumber.trim();
    }

    /**
     *  获取 银行流水号 字段:company_recharges.recharge_serial_number
     *
     *  @return company_recharges.recharge_serial_number, 银行流水号
     */
    public String getRechargeSerialNumber() {
        return rechargeSerialNumber;
    }

    /**
     *  设置 银行流水号 字段:company_recharges.recharge_serial_number
     *
     *  @param rechargeSerialNumber company_recharges.recharge_serial_number, 银行流水号
     */
    public void setRechargeSerialNumber(String rechargeSerialNumber) {
        this.rechargeSerialNumber = rechargeSerialNumber == null ? null : rechargeSerialNumber.trim();
    }

    /**
     *  获取 注备，如充值平台等 字段:company_recharges.recharge_bak
     *
     *  @return company_recharges.recharge_bak, 注备，如充值平台等
     */
    public String getRechargeBak() {
        return rechargeBak;
    }

    /**
     *  设置 注备，如充值平台等 字段:company_recharges.recharge_bak
     *
     *  @param rechargeBak company_recharges.recharge_bak, 注备，如充值平台等
     */
    public void setRechargeBak(String rechargeBak) {
        this.rechargeBak = rechargeBak == null ? null : rechargeBak.trim();
    }

    /**
     *  获取 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银 字段:company_recharges.recharge_station
     *
     *  @return company_recharges.recharge_station, 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
     */
    public Integer getRechargeStation() {
        return rechargeStation;
    }

    /**
     *  设置 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银 字段:company_recharges.recharge_station
     *
     *  @param rechargeStation company_recharges.recharge_station, 第三方充值平台 0没有(提现) 1连连 2京东 3易宝 4网银
     */
    public void setRechargeStation(Integer rechargeStation) {
        this.rechargeStation = rechargeStation;
    }

    /**
     *  获取 客户端 1web 2wap 3ios 4android 字段:company_recharges.recharge_client
     *
     *  @return company_recharges.recharge_client, 客户端 1web 2wap 3ios 4android
     */
    public Integer getRechargeClient() {
        return rechargeClient;
    }

    /**
     *  设置 客户端 1web 2wap 3ios 4android 字段:company_recharges.recharge_client
     *
     *  @param rechargeClient company_recharges.recharge_client, 客户端 1web 2wap 3ios 4android
     */
    public void setRechargeClient(Integer rechargeClient) {
        this.rechargeClient = rechargeClient;
    }

    /**
     *  获取 充值的钱服务器 字段:company_recharges.recharge_moneyserver
     *
     *  @return company_recharges.recharge_moneyserver, 充值的钱服务器
     */
    public BigDecimal getRechargeMoneyserver() {
        return rechargeMoneyserver;
    }

    /**
     *  设置 充值的钱服务器 字段:company_recharges.recharge_moneyserver
     *
     *  @param rechargeMoneyserver company_recharges.recharge_moneyserver, 充值的钱服务器
     */
    public void setRechargeMoneyserver(BigDecimal rechargeMoneyserver) {
        this.rechargeMoneyserver = rechargeMoneyserver;
    }

    /**
     *  获取 充值银行 字段:company_recharges.recharge_bank
     *
     *  @return company_recharges.recharge_bank, 充值银行
     */
    public String getRechargeBank() {
        return rechargeBank;
    }

    /**
     *  设置 充值银行 字段:company_recharges.recharge_bank
     *
     *  @param rechargeBank company_recharges.recharge_bank, 充值银行
     */
    public void setRechargeBank(String rechargeBank) {
        this.rechargeBank = rechargeBank == null ? null : rechargeBank.trim();
    }

    /**
     *  获取 充值的银行区 字段:company_recharges.recharge_bankarea
     *
     *  @return company_recharges.recharge_bankarea, 充值的银行区
     */
    public String getRechargeBankarea() {
        return rechargeBankarea;
    }

    /**
     *  设置 充值的银行区 字段:company_recharges.recharge_bankarea
     *
     *  @param rechargeBankarea company_recharges.recharge_bankarea, 充值的银行区
     */
    public void setRechargeBankarea(String rechargeBankarea) {
        this.rechargeBankarea = rechargeBankarea == null ? null : rechargeBankarea.trim();
    }

    /**
     *  获取 充值银行账号 字段:company_recharges.recharge_banknumber
     *
     *  @return company_recharges.recharge_banknumber, 充值银行账号
     */
    public String getRechargeBanknumber() {
        return rechargeBanknumber;
    }

    /**
     *  设置 充值银行账号 字段:company_recharges.recharge_banknumber
     *
     *  @param rechargeBanknumber company_recharges.recharge_banknumber, 充值银行账号
     */
    public void setRechargeBanknumber(String rechargeBanknumber) {
        this.rechargeBanknumber = rechargeBanknumber == null ? null : rechargeBanknumber.trim();
    }

    /**
     *  获取 充值的钱现在 字段:company_recharges.recharge_moneynow
     *
     *  @return company_recharges.recharge_moneynow, 充值的钱现在
     */
    public BigDecimal getRechargeMoneynow() {
        return rechargeMoneynow;
    }

    /**
     *  设置 充值的钱现在 字段:company_recharges.recharge_moneynow
     *
     *  @param rechargeMoneynow company_recharges.recharge_moneynow, 充值的钱现在
     */
    public void setRechargeMoneynow(BigDecimal rechargeMoneynow) {
        this.rechargeMoneynow = rechargeMoneynow;
    }

    /**
     *  获取 是否导出 0否 1是 字段:company_recharges.recharge_istoexcel
     *
     *  @return company_recharges.recharge_istoexcel, 是否导出 0否 1是
     */
    public Integer getRechargeIstoexcel() {
        return rechargeIstoexcel;
    }

    /**
     *  设置 是否导出 0否 1是 字段:company_recharges.recharge_istoexcel
     *
     *  @param rechargeIstoexcel company_recharges.recharge_istoexcel, 是否导出 0否 1是
     */
    public void setRechargeIstoexcel(Integer rechargeIstoexcel) {
        this.rechargeIstoexcel = rechargeIstoexcel;
    }

    /**
     *  获取 系统补贴 字段:company_recharges.recharge_moneybt
     *
     *  @return company_recharges.recharge_moneybt, 系统补贴
     */
    public BigDecimal getRechargeMoneybt() {
        return rechargeMoneybt;
    }

    /**
     *  设置 系统补贴 字段:company_recharges.recharge_moneybt
     *
     *  @param rechargeMoneybt company_recharges.recharge_moneybt, 系统补贴
     */
    public void setRechargeMoneybt(BigDecimal rechargeMoneybt) {
        this.rechargeMoneybt = rechargeMoneybt;
    }

    /**
     *  获取 是否体现到账户资金变化 0否 1是 字段:company_recharges.recharge_istorecord
     *
     *  @return company_recharges.recharge_istorecord, 是否体现到账户资金变化 0否 1是
     */
    public Integer getRechargeIstorecord() {
        return rechargeIstorecord;
    }

    /**
     *  设置 是否体现到账户资金变化 0否 1是 字段:company_recharges.recharge_istorecord
     *
     *  @param rechargeIstorecord company_recharges.recharge_istorecord, 是否体现到账户资金变化 0否 1是
     */
    public void setRechargeIstorecord(Integer rechargeIstorecord) {
        this.rechargeIstorecord = rechargeIstorecord;
    }

    /**
     *  获取 回调时间(成功/失败) 充值才有 字段:company_recharges.recharge_recall_time
     *
     *  @return company_recharges.recharge_recall_time, 回调时间(成功/失败) 充值才有
     */
    public Date getRechargeRecallTime() {
        return rechargeRecallTime;
    }

    /**
     *  设置 回调时间(成功/失败) 充值才有 字段:company_recharges.recharge_recall_time
     *
     *  @param rechargeRecallTime company_recharges.recharge_recall_time, 回调时间(成功/失败) 充值才有
     */
    public void setRechargeRecallTime(Date rechargeRecallTime) {
        this.rechargeRecallTime = rechargeRecallTime;
    }

    /**
     *  获取 回调结果 0成功 1后面是各种错误 字段:company_recharges.recharge_recall_result
     *
     *  @return company_recharges.recharge_recall_result, 回调结果 0成功 1后面是各种错误
     */
    public Integer getRechargeRecallResult() {
        return rechargeRecallResult;
    }

    /**
     *  设置 回调结果 0成功 1后面是各种错误 字段:company_recharges.recharge_recall_result
     *
     *  @param rechargeRecallResult company_recharges.recharge_recall_result, 回调结果 0成功 1后面是各种错误
     */
    public void setRechargeRecallResult(Integer rechargeRecallResult) {
        this.rechargeRecallResult = rechargeRecallResult;
    }

    /**
     *  获取 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 字段:company_recharges.recharge_state
     *
     *  @return company_recharges.recharge_state, 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过
     */
    public Integer getRechargeState() {
        return rechargeState;
    }

    /**
     *  设置 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过 字段:company_recharges.recharge_state
     *
     *  @param rechargeState company_recharges.recharge_state, 状态 0待审核 1初审失败 2初审通过 3复审失败 4复审通过
     */
    public void setRechargeState(Integer rechargeState) {
        this.rechargeState = rechargeState;
    }

    /**
     *  获取 初审时间 字段:company_recharges.recharge_audit_first_time
     *
     *  @return company_recharges.recharge_audit_first_time, 初审时间
     */
    public Date getRechargeAuditFirstTime() {
        return rechargeAuditFirstTime;
    }

    /**
     *  设置 初审时间 字段:company_recharges.recharge_audit_first_time
     *
     *  @param rechargeAuditFirstTime company_recharges.recharge_audit_first_time, 初审时间
     */
    public void setRechargeAuditFirstTime(Date rechargeAuditFirstTime) {
        this.rechargeAuditFirstTime = rechargeAuditFirstTime;
    }

    /**
     *  获取 初审人 字段:company_recharges.recharge_audit_first_member
     *
     *  @return company_recharges.recharge_audit_first_member, 初审人
     */
    public Long getRechargeAuditFirstMember() {
        return rechargeAuditFirstMember;
    }

    /**
     *  设置 初审人 字段:company_recharges.recharge_audit_first_member
     *
     *  @param rechargeAuditFirstMember company_recharges.recharge_audit_first_member, 初审人
     */
    public void setRechargeAuditFirstMember(Long rechargeAuditFirstMember) {
        this.rechargeAuditFirstMember = rechargeAuditFirstMember;
    }

    /**
     *  获取 复审时间 字段:company_recharges.recharge_audit_second_time
     *
     *  @return company_recharges.recharge_audit_second_time, 复审时间
     */
    public Date getRechargeAuditSecondTime() {
        return rechargeAuditSecondTime;
    }

    /**
     *  设置 复审时间 字段:company_recharges.recharge_audit_second_time
     *
     *  @param rechargeAuditSecondTime company_recharges.recharge_audit_second_time, 复审时间
     */
    public void setRechargeAuditSecondTime(Date rechargeAuditSecondTime) {
        this.rechargeAuditSecondTime = rechargeAuditSecondTime;
    }

    /**
     *  获取 复审人 字段:company_recharges.recharge_audit_second_member
     *
     *  @return company_recharges.recharge_audit_second_member, 复审人
     */
    public Long getRechargeAuditSecondMember() {
        return rechargeAuditSecondMember;
    }

    /**
     *  设置 复审人 字段:company_recharges.recharge_audit_second_member
     *
     *  @param rechargeAuditSecondMember company_recharges.recharge_audit_second_member, 复审人
     */
    public void setRechargeAuditSecondMember(Long rechargeAuditSecondMember) {
        this.rechargeAuditSecondMember = rechargeAuditSecondMember;
    }

    public Long getExcelId() {
        return excelId;
    }

    public void setExcelId(Long excelId) {
        this.excelId = excelId;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankSubbranch() {
        return bankSubbranch;
    }

    public void setBankSubbranch(String bankSubbranch) {
        this.bankSubbranch = bankSubbranch;
    }

    public String getRechargeAuditFirstRemark() {
        return rechargeAuditFirstRemark;
    }

    public void setRechargeAuditFirstRemark(String rechargeAuditFirstRemark) {
        this.rechargeAuditFirstRemark = rechargeAuditFirstRemark;
    }
}