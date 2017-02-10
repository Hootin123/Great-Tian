package com.xtr.api.domain.company;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.xtr.api.basic.BaseObject;

/**
 * 
 * <p>企业借款账单</p>
 *
 * @author 任齐
 * @createTime: 2016/06/29 上午10:26:03
 */
public class CompanyBorrowBillsBean extends BaseObject implements Serializable{
    /**
     *  账单id,所属表字段为company_borrow_bills.bill_id
     */
    private Long billId;

    /**
     *  账单期数,所属表字段为company_borrow_bills.bill_index
     */
    private Integer billIndex;

    /**
     *  借款订单id,所属表字段为company_borrow_bills.bill_borrow_order_id
     */
    private Long billBorrowOrderId;

    /**
     *  企业id,所属表字段为company_borrow_bills.bill_company_id
     */
    private Long billCompanyId;

    /**
     *  部门id,所属表字段为company_borrow_bills.bill_dep_id
     */
    private Long billDepId;

    /**
     *  账单金额 只包含 本金、利息 ，本期账单需要支付=bill_money+bill_money_break,所属表字段为company_borrow_bills.bill_money
     */
    private BigDecimal billMoney;

    /**
     *  本金,所属表字段为company_borrow_bills.bill_money_base
     */
    private BigDecimal billMoneyBase;

    /**
     *  利息,所属表字段为company_borrow_bills.bill_money_interest
     */
    private BigDecimal billMoneyInterest;

    /**
     *  违约金,所属表字段为company_borrow_bills.bill_money_break
     */
    private BigDecimal billMoneyBreak;

    /**
     *  账单创建时间,所属表字段为company_borrow_bills.bill_addtime
     */
    private Date billAddtime;

    /**
     *  账单理论还款时间,所属表字段为company_borrow_bills.bill_repaytime
     */
    private Date billRepaytime;

    /**
     *  账单实际还款时间,所属表字段为company_borrow_bills.bill_actual_repaytime
     */
    private Date billActualRepaytime;

    /**
     *  0未还款 1已还款,所属表字段为company_borrow_bills.bill_sign
     */
    private Integer billSign;

    /**
     *  是否该订单最后一期账单 0不是 1是,所属表字段为company_borrow_bills.bill_islast
     */
    private Integer billIslast;

    /**
     *  同一个业务标识(时间戳,精确到毫秒),所属表字段为company_borrow_bills.tran_id
     */
    private Long tranId;

    /**
     *  已减免违约金，理论违约金=bill_money_break+bill_money_break_coupon，需要支付违约金bill_money_break,所属表字段为company_borrow_bills.bill_money_break_coupon
     */
    private BigDecimal billMoneyBreakCoupon;

    /**
     *  违约金操作记录，需包含每次操作，管理员id和金额,所属表字段为company_borrow_bills.bill_break_edit_remark
     */
    private String billBreakEditRemark;

    /**
     *  提醒还款次数,所属表字段为company_borrow_bills.bill_remind_payments_count
     */
    private Integer billRemindPaymentsCount;

    /**
     *  获取 账单id 字段:company_borrow_bills.bill_id 
     *
     *  @return company_borrow_bills.bill_id, 账单id
     */
    public Long getBillId() {
        return billId;
    }

    /**
     *  设置 账单id 字段:company_borrow_bills.bill_id 
     *
     *  @param billId company_borrow_bills.bill_id, 账单id
     */
    public void setBillId(Long billId) {
        this.billId = billId;
    }

    /**
     *  获取 账单期数 字段:company_borrow_bills.bill_index 
     *
     *  @return company_borrow_bills.bill_index, 账单期数
     */
    public Integer getBillIndex() {
        return billIndex;
    }

    /**
     *  设置 账单期数 字段:company_borrow_bills.bill_index 
     *
     *  @param billIndex company_borrow_bills.bill_index, 账单期数
     */
    public void setBillIndex(Integer billIndex) {
        this.billIndex = billIndex;
    }

    /**
     *  获取 借款订单id 字段:company_borrow_bills.bill_borrow_order_id 
     *
     *  @return company_borrow_bills.bill_borrow_order_id, 借款订单id
     */
    public Long getBillBorrowOrderId() {
        return billBorrowOrderId;
    }

    /**
     *  设置 借款订单id 字段:company_borrow_bills.bill_borrow_order_id 
     *
     *  @param billBorrowOrderId company_borrow_bills.bill_borrow_order_id, 借款订单id
     */
    public void setBillBorrowOrderId(Long billBorrowOrderId) {
        this.billBorrowOrderId = billBorrowOrderId;
    }

    /**
     *  获取 企业id 字段:company_borrow_bills.bill_company_id 
     *
     *  @return company_borrow_bills.bill_company_id, 企业id
     */
    public Long getBillCompanyId() {
        return billCompanyId;
    }

    /**
     *  设置 企业id 字段:company_borrow_bills.bill_company_id 
     *
     *  @param billCompanyId company_borrow_bills.bill_company_id, 企业id
     */
    public void setBillCompanyId(Long billCompanyId) {
        this.billCompanyId = billCompanyId;
    }

    /**
     *  获取 部门id 字段:company_borrow_bills.bill_dep_id 
     *
     *  @return company_borrow_bills.bill_dep_id, 部门id
     */
    public Long getBillDepId() {
        return billDepId;
    }

    /**
     *  设置 部门id 字段:company_borrow_bills.bill_dep_id 
     *
     *  @param billDepId company_borrow_bills.bill_dep_id, 部门id
     */
    public void setBillDepId(Long billDepId) {
        this.billDepId = billDepId;
    }

    /**
     *  获取 账单金额 只包含 本金、利息 ，本期账单需要支付=bill_money+bill_money_break 字段:company_borrow_bills.bill_money 
     *
     *  @return company_borrow_bills.bill_money, 账单金额 只包含 本金、利息 ，本期账单需要支付=bill_money+bill_money_break
     */
    public BigDecimal getBillMoney() {
        return billMoney;
    }

    /**
     *  设置 账单金额 只包含 本金、利息 ，本期账单需要支付=bill_money+bill_money_break 字段:company_borrow_bills.bill_money 
     *
     *  @param billMoney company_borrow_bills.bill_money, 账单金额 只包含 本金、利息 ，本期账单需要支付=bill_money+bill_money_break
     */
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    /**
     *  获取 本金 字段:company_borrow_bills.bill_money_base 
     *
     *  @return company_borrow_bills.bill_money_base, 本金
     */
    public BigDecimal getBillMoneyBase() {
        return billMoneyBase;
    }

    /**
     *  设置 本金 字段:company_borrow_bills.bill_money_base 
     *
     *  @param billMoneyBase company_borrow_bills.bill_money_base, 本金
     */
    public void setBillMoneyBase(BigDecimal billMoneyBase) {
        this.billMoneyBase = billMoneyBase;
    }

    /**
     *  获取 利息 字段:company_borrow_bills.bill_money_interest 
     *
     *  @return company_borrow_bills.bill_money_interest, 利息
     */
    public BigDecimal getBillMoneyInterest() {
        return billMoneyInterest;
    }

    /**
     *  设置 利息 字段:company_borrow_bills.bill_money_interest 
     *
     *  @param billMoneyInterest company_borrow_bills.bill_money_interest, 利息
     */
    public void setBillMoneyInterest(BigDecimal billMoneyInterest) {
        this.billMoneyInterest = billMoneyInterest;
    }

    /**
     *  获取 违约金 字段:company_borrow_bills.bill_money_break 
     *
     *  @return company_borrow_bills.bill_money_break, 违约金
     */
    public BigDecimal getBillMoneyBreak() {
        return billMoneyBreak;
    }

    /**
     *  设置 违约金 字段:company_borrow_bills.bill_money_break 
     *
     *  @param billMoneyBreak company_borrow_bills.bill_money_break, 违约金
     */
    public void setBillMoneyBreak(BigDecimal billMoneyBreak) {
        this.billMoneyBreak = billMoneyBreak;
    }

    /**
     *  获取 账单创建时间 字段:company_borrow_bills.bill_addtime 
     *
     *  @return company_borrow_bills.bill_addtime, 账单创建时间
     */
    public Date getBillAddtime() {
        return billAddtime;
    }

    /**
     *  设置 账单创建时间 字段:company_borrow_bills.bill_addtime 
     *
     *  @param billAddtime company_borrow_bills.bill_addtime, 账单创建时间
     */
    public void setBillAddtime(Date billAddtime) {
        this.billAddtime = billAddtime;
    }

    /**
     *  获取 账单理论还款时间 字段:company_borrow_bills.bill_repaytime 
     *
     *  @return company_borrow_bills.bill_repaytime, 账单理论还款时间
     */
    public Date getBillRepaytime() {
        return billRepaytime;
    }

    /**
     *  设置 账单理论还款时间 字段:company_borrow_bills.bill_repaytime 
     *
     *  @param billRepaytime company_borrow_bills.bill_repaytime, 账单理论还款时间
     */
    public void setBillRepaytime(Date billRepaytime) {
        this.billRepaytime = billRepaytime;
    }

    /**
     *  获取 账单实际还款时间 字段:company_borrow_bills.bill_actual_repaytime 
     *
     *  @return company_borrow_bills.bill_actual_repaytime, 账单实际还款时间
     */
    public Date getBillActualRepaytime() {
        return billActualRepaytime;
    }

    /**
     *  设置 账单实际还款时间 字段:company_borrow_bills.bill_actual_repaytime 
     *
     *  @param billActualRepaytime company_borrow_bills.bill_actual_repaytime, 账单实际还款时间
     */
    public void setBillActualRepaytime(Date billActualRepaytime) {
        this.billActualRepaytime = billActualRepaytime;
    }

    /**
     *  获取 0未还款 1已还款 字段:company_borrow_bills.bill_sign 
     *
     *  @return company_borrow_bills.bill_sign, 0未还款 1已还款
     */
    public Integer getBillSign() {
        return billSign;
    }

    /**
     *  设置 0未还款 1已还款 字段:company_borrow_bills.bill_sign 
     *
     *  @param billSign company_borrow_bills.bill_sign, 0未还款 1已还款
     */
    public void setBillSign(Integer billSign) {
        this.billSign = billSign;
    }

    /**
     *  获取 是否该订单最后一期账单 0不是 1是 字段:company_borrow_bills.bill_islast 
     *
     *  @return company_borrow_bills.bill_islast, 是否该订单最后一期账单 0不是 1是
     */
    public Integer getBillIslast() {
        return billIslast;
    }

    /**
     *  设置 是否该订单最后一期账单 0不是 1是 字段:company_borrow_bills.bill_islast 
     *
     *  @param billIslast company_borrow_bills.bill_islast, 是否该订单最后一期账单 0不是 1是
     */
    public void setBillIslast(Integer billIslast) {
        this.billIslast = billIslast;
    }

    /**
     *  获取 同一个业务标识(时间戳,精确到毫秒) 字段:company_borrow_bills.tran_id 
     *
     *  @return company_borrow_bills.tran_id, 同一个业务标识(时间戳,精确到毫秒)
     */
    public Long getTranId() {
        return tranId;
    }

    /**
     *  设置 同一个业务标识(时间戳,精确到毫秒) 字段:company_borrow_bills.tran_id 
     *
     *  @param tranId company_borrow_bills.tran_id, 同一个业务标识(时间戳,精确到毫秒)
     */
    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    /**
     *  获取 已减免违约金，理论违约金=bill_money_break+bill_money_break_coupon，需要支付违约金bill_money_break 字段:company_borrow_bills.bill_money_break_coupon 
     *
     *  @return company_borrow_bills.bill_money_break_coupon, 已减免违约金，理论违约金=bill_money_break+bill_money_break_coupon，需要支付违约金bill_money_break
     */
    public BigDecimal getBillMoneyBreakCoupon() {
        return billMoneyBreakCoupon;
    }

    /**
     *  设置 已减免违约金，理论违约金=bill_money_break+bill_money_break_coupon，需要支付违约金bill_money_break 字段:company_borrow_bills.bill_money_break_coupon 
     *
     *  @param billMoneyBreakCoupon company_borrow_bills.bill_money_break_coupon, 已减免违约金，理论违约金=bill_money_break+bill_money_break_coupon，需要支付违约金bill_money_break
     */
    public void setBillMoneyBreakCoupon(BigDecimal billMoneyBreakCoupon) {
        this.billMoneyBreakCoupon = billMoneyBreakCoupon;
    }

    /**
     *  获取 违约金操作记录，需包含每次操作，管理员id和金额 字段:company_borrow_bills.bill_break_edit_remark 
     *
     *  @return company_borrow_bills.bill_break_edit_remark, 违约金操作记录，需包含每次操作，管理员id和金额
     */
    public String getBillBreakEditRemark() {
        return billBreakEditRemark;
    }

    /**
     *  设置 违约金操作记录，需包含每次操作，管理员id和金额 字段:company_borrow_bills.bill_break_edit_remark 
     *
     *  @param billBreakEditRemark company_borrow_bills.bill_break_edit_remark, 违约金操作记录，需包含每次操作，管理员id和金额
     */
    public void setBillBreakEditRemark(String billBreakEditRemark) {
        this.billBreakEditRemark = billBreakEditRemark == null ? null : billBreakEditRemark.trim();
    }

    /**
     *  获取 提醒还款次数 字段:company_borrow_bills.bill_remind_payments_count 
     *
     *  @return company_borrow_bills.bill_remind_payments_count, 提醒还款次数
     */
    public Integer getBillRemindPaymentsCount() {
        return billRemindPaymentsCount;
    }

    /**
     *  设置 提醒还款次数 字段:company_borrow_bills.bill_remind_payments_count 
     *
     *  @param billRemindPaymentsCount company_borrow_bills.bill_remind_payments_count, 提醒还款次数
     */
    public void setBillRemindPaymentsCount(Integer billRemindPaymentsCount) {
        this.billRemindPaymentsCount = billRemindPaymentsCount;
    }
}