package com.xtr.api.domain.company;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.xtr.api.basic.BaseObject;

/**
 * <p>企业借款订单</p>
 *
 * @author 任齐
 * @createTime: 2016/06/29 上午10:25:42
 */
public class CompanyBorrowOrdersBean extends BaseObject implements Serializable {
	
    /**
     *  id,所属表字段为company_borrow_orders.order_id
     */
    private Long orderId;

    /**
     *  企业id,所属表字段为company_borrow_orders.order_company_id
     */
    private Long orderCompanyId;

    /**
     *  单位部门id,所属表字段为company_borrow_orders.order_dep_id
     */
    private Long orderDepId;

    /**
     *  订单金额,所属表字段为company_borrow_orders.order_money
     */
    private BigDecimal orderMoney;

    /**
     *  订单创建日期,所属表字段为company_borrow_orders.order_addtime
     */
    private Date orderAddtime;

    /**
     *  订单编号,所属表字段为company_borrow_orders.order_number
     */
    private String orderNumber;

    /**
     *  状态 0待审核 1用户取消 2b端确认打款 3平台确认打款 4审核失败 5审核成功 6平台垫发放款 7发放失败 8发放成功 9还款完成 10未打款关闭 11确认取消订单 12融资失败,所属表字段为company_borrow_orders.order_state
     */
    private Integer orderState;

    /**
     *  订单取消时间(没取消是为空)与order_state相关,所属表字段为company_borrow_orders.order_canceltime
     */
    private Date orderCanceltime;

    /**
     *  订单审核时间与order_state相关,所属表字段为company_borrow_orders.order_audittime
     */
    private Date orderAudittime;

    /**
     *  放款时间与order_state相关,所属表字段为company_borrow_orders.order_lendtime
     */
    private Date orderLendtime;

    /**
     *  0无效订单 1有效订单,所属表字段为company_borrow_orders.order_sign
     */
    private Integer orderSign;

    /**
     *  订单修改者id(平台成员id)与order_sign相关,所属表字段为company_borrow_orders.order_edit_member
     */
    private Long orderEditMember;

    /**
     *  订单修改时间与order_sign相关,所属表字段为company_borrow_orders.order_edit_time
     */
    private Date orderEditTime;

    /**
     *  计息方式 1按天 2按月,所属表字段为company_borrow_orders.order_interest_type
     */
    private Integer orderInterestType;

    /**
     *  借款期限,所属表字段为company_borrow_orders.order_interest_cycle
     */
    private Integer orderInterestCycle;

    /**
     *  订单还款方式 1到期还本付息 2按月付息到期还本 3.等额本息,所属表字段为company_borrow_orders.order_repay_type
     */
    private Integer orderRepayType;

    /**
     *  订单约定还款日期,若order_repay_type为2,则为约定第一次还款日期,所属表字段为company_borrow_orders.order_repay_starttime
     */
    private Date orderRepayStarttime;

    /**
     *  还款结束日期,order_repay_type为1则与order_repay_starttime相等,若order_repay_type为2,则按期数计算,所属表字段为company_borrow_orders.order_repay_endtime
     */
    private Date orderRepayEndtime;

    /**
     *  订单还款期数 order_repay_type为1则为1,所属表字段为company_borrow_orders.order_cycle
     */
    private Integer orderCycle;

    /**
     *  还款分期单位 0一次付清 1天 2月 3年,所属表字段为company_borrow_orders.order_cycle_unit
     */
    private Integer orderCycleUnit;

    /**
     *  借款利率,扩大100倍,所属表字段为company_borrow_orders.order_rate
     */
    private BigDecimal orderRate;

    /**
     *  订单优惠券,所属表字段为company_borrow_orders.order_prop_id
     */
    private Integer orderPropId;

    /**
     *  优惠券类型 1借款抵现 2借款减息 3投资抵现 4投资加息 5投资返现,所属表字段为company_borrow_orders.order_prop_coupon_type
     */
    private Integer orderPropCouponType;

    /**
     *  抵现值 order_coupon_type为1s时值金额,为2时值为减息利率(扩大100倍),所属表字段为company_borrow_orders.order_prop_coupon_value
     */
    private BigDecimal orderPropCouponValue;

    /**
     *  垫付企业实际到账金额,所属表字段为company_borrow_orders.order_money_arrival
     */
    private BigDecimal orderMoneyArrival;

    /**
     *  优惠折算过后的金额(需要还款金额),所属表字段为company_borrow_orders.order_actual_money
     */
    private BigDecimal orderActualMoney;

    /**
     *  折算过后还款利息(实际计算的利息),所属表字段为company_borrow_orders.order_actual_rate
     */
    private BigDecimal orderActualRate;

    /**
     *  服务费百分比,所属表字段为company_borrow_orders.order_actual_server_rate
     */
    private BigDecimal orderActualServerRate;

    /**
     *  保证金比例,所属表字段为company_borrow_orders.order_actual_deposit_rate
     */
    private BigDecimal orderActualDepositRate;

    /**
     *  服务费,所属表字段为company_borrow_orders.order_actual_server
     */
    private BigDecimal orderActualServer;

    /**
     *  保证金,所属表字段为company_borrow_orders.order_actual_deposit
     */
    private BigDecimal orderActualDeposit;

    /**
     *  订单完结时间(最后一次实际还款时间),所属表字段为company_borrow_orders.order_repay_overtime
     */
    private Date orderRepayOvertime;

    /**
     *  类型 1垫付订单 2募集订单(融资) 3代发工资订单,所属表字段为company_borrow_orders.order_type
     */
    private Integer orderType;

    /**
     *  款借百分比,所属表字段为company_borrow_orders.order_borrowpercent
     */
    private BigDecimal orderBorrowpercent;

    /**
     *  企业发放总工资,所属表字段为company_borrow_orders.order_salaryall
     */
    private BigDecimal orderSalaryall;

    /**
     *  s申请垫付月份,所属表字段为company_borrow_orders.order_applytime
     */
    private Date orderApplytime;

    /**
     *  发薪日,所属表字段为company_borrow_orders.order_salary_day
     */
    private Date orderSalaryDay;

    /**
     *  约定汇款日期,所属表字段为company_borrow_orders.order_appoint_remittance_date
     */
    private Date orderAppointRemittanceDate;

    /**
     *  实际汇款到账时间,所属表字段为company_borrow_orders.order_remittance_time
     */
    private Date orderRemittanceTime;

    /**
     *  取消原因,所属表字段为company_borrow_orders.order_cancel_remark
     */
    private String orderCancelRemark;

    /**
     *  审核原因,所属表字段为company_borrow_orders.order_audit_remark
     */
    private String orderAuditRemark;

    /**
     *  融资理由,所属表字段为company_borrow_orders.order_finance_resion
     */
    private String orderFinanceResion;

    /**
     *  期望到账日期,所属表字段为company_borrow_orders.order_expect_day
     */
    private Date orderExpectDay;

    private String orderMoneyValue;

    private String orderActualServerValue;

    private String orderRateValue;

    private String companyName;

    private String companyContactTel;

    private String orderExpectDayValue;

    private String orderAddtimeValue;

    public String getOrderAuditUser() {
        return orderAuditUser;
    }

    public void setOrderAuditUser(String orderAuditUser) {
        this.orderAuditUser = orderAuditUser;
    }

    public String getOrderFinanceUser() {
        return orderFinanceUser;
    }

    public void setOrderFinanceUser(String orderFinanceUser) {
        this.orderFinanceUser = orderFinanceUser;
    }

    public String getOrderLendUser() {
        return orderLendUser;
    }

    public void setOrderLendUser(String orderLendUser) {
        this.orderLendUser = orderLendUser;
    }

    public Date getOrderFinanceTime() {
        return orderFinanceTime;
    }

    public void setOrderFinanceTime(Date orderFinanceTime) {
        this.orderFinanceTime = orderFinanceTime;
    }

    /**
     *  借款受理操作人

     */
    private String orderAuditUser;

    /**
     *  融资处理操作人
     */
    private String orderFinanceUser;

    /**
     *  放款操作人
     */
    private String orderLendUser;

    /**
     *  融资处理时间
     */
    private Date orderFinanceTime;



    /**
     *  到期时间
     */
    private Date orderExpireTime;

    /**
     * 获取 id 字段:company_borrow_orders.order_id
     *
     * @return company_borrow_orders.order_id, id
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置 id 字段:company_borrow_orders.order_id
     *
     * @param orderId company_borrow_orders.order_id, id
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取 企业id 字段:company_borrow_orders.order_company_id
     *
     * @return company_borrow_orders.order_company_id, 企业id
     */
    public Long getOrderCompanyId() {
        return orderCompanyId;
    }

    /**
     * 设置 企业id 字段:company_borrow_orders.order_company_id
     *
     * @param orderCompanyId company_borrow_orders.order_company_id, 企业id
     */
    public void setOrderCompanyId(Long orderCompanyId) {
        this.orderCompanyId = orderCompanyId;
    }

    /**
     * 获取 单位部门id 字段:company_borrow_orders.order_dep_id
     *
     * @return company_borrow_orders.order_dep_id, 单位部门id
     */
    public Long getOrderDepId() {
        return orderDepId;
    }

    /**
     * 设置 单位部门id 字段:company_borrow_orders.order_dep_id
     *
     * @param orderDepId company_borrow_orders.order_dep_id, 单位部门id
     */
    public void setOrderDepId(Long orderDepId) {
        this.orderDepId = orderDepId;
    }

    /**
     * 获取 订单金额 字段:company_borrow_orders.order_money
     *
     * @return company_borrow_orders.order_money, 订单金额
     */
    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    /**
     * 设置 订单金额 字段:company_borrow_orders.order_money
     *
     * @param orderMoney company_borrow_orders.order_money, 订单金额
     */
    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    /**
     * 获取 订单创建日期 字段:company_borrow_orders.order_addtime
     *
     * @return company_borrow_orders.order_addtime, 订单创建日期
     */
    public Date getOrderAddtime() {
        return orderAddtime;
    }

    /**
     * 设置 订单创建日期 字段:company_borrow_orders.order_addtime
     *
     * @param orderAddtime company_borrow_orders.order_addtime, 订单创建日期
     */
    public void setOrderAddtime(Date orderAddtime) {
        this.orderAddtime = orderAddtime;
    }

    /**
     * 获取 订单编号 字段:company_borrow_orders.order_number
     *
     * @return company_borrow_orders.order_number, 订单编号
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * 设置 订单编号 字段:company_borrow_orders.order_number
     *
     * @param orderNumber company_borrow_orders.order_number, 订单编号
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    /**
     * 获取 状态 0待审核 1用户取消 2b端确认打款 3平台确认打款 4审核失败 5审核成功 6平台垫发放款 7发放失败 8发放成功 9还款完成 10未打款关闭 11确认取消订单 12融资失败 字段:company_borrow_orders.order_state
     *
     * @return company_borrow_orders.order_state, 状态 0待审核 1用户取消 2b端确认打款 3平台确认打款 4审核失败 5审核成功 6平台垫发放款 7发放失败 8发放成功 9还款完成 10未打款关闭 11确认取消订单 12融资失败
     */
    public Integer getOrderState() {
        return orderState;
    }

    /**
     * 设置 状态 0待审核 1用户取消 2b端确认打款 3平台确认打款 4审核失败 5审核成功 6平台垫发放款 7发放失败 8发放成功 9还款完成 10未打款关闭 11确认取消订单 12融资失败 字段:company_borrow_orders.order_state
     *
     * @param orderState company_borrow_orders.order_state, 状态 0待审核 1用户取消 2b端确认打款 3平台确认打款 4审核失败 5审核成功 6平台垫发放款 7发放失败 8发放成功 9还款完成 10未打款关闭 11确认取消订单 12融资失败
     */
    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    /**
     * 获取 订单取消时间(没取消是为空)与order_state相关 字段:company_borrow_orders.order_canceltime
     *
     * @return company_borrow_orders.order_canceltime, 订单取消时间(没取消是为空)与order_state相关
     */
    public Date getOrderCanceltime() {
        return orderCanceltime;
    }

    /**
     * 设置 订单取消时间(没取消是为空)与order_state相关 字段:company_borrow_orders.order_canceltime
     *
     * @param orderCanceltime company_borrow_orders.order_canceltime, 订单取消时间(没取消是为空)与order_state相关
     */
    public void setOrderCanceltime(Date orderCanceltime) {
        this.orderCanceltime = orderCanceltime;
    }

    /**
     * 获取 订单审核时间与order_state相关 字段:company_borrow_orders.order_audittime
     *
     * @return company_borrow_orders.order_audittime, 订单审核时间与order_state相关
     */
    public Date getOrderAudittime() {
        return orderAudittime;
    }

    /**
     * 设置 订单审核时间与order_state相关 字段:company_borrow_orders.order_audittime
     *
     * @param orderAudittime company_borrow_orders.order_audittime, 订单审核时间与order_state相关
     */
    public void setOrderAudittime(Date orderAudittime) {
        this.orderAudittime = orderAudittime;
    }

    /**
     * 获取 放款时间与order_state相关 字段:company_borrow_orders.order_lendtime
     *
     * @return company_borrow_orders.order_lendtime, 放款时间与order_state相关
     */
    public Date getOrderLendtime() {
        return orderLendtime;
    }

    /**
     * 设置 放款时间与order_state相关 字段:company_borrow_orders.order_lendtime
     *
     * @param orderLendtime company_borrow_orders.order_lendtime, 放款时间与order_state相关
     */
    public void setOrderLendtime(Date orderLendtime) {
        this.orderLendtime = orderLendtime;
    }

    /**
     * 获取 0无效订单 1有效订单 字段:company_borrow_orders.order_sign
     *
     * @return company_borrow_orders.order_sign, 0无效订单 1有效订单
     */
    public Integer getOrderSign() {
        return orderSign;
    }

    /**
     * 设置 0无效订单 1有效订单 字段:company_borrow_orders.order_sign
     *
     * @param orderSign company_borrow_orders.order_sign, 0无效订单 1有效订单
     */
    public void setOrderSign(Integer orderSign) {
        this.orderSign = orderSign;
    }

    /**
     * 获取 订单修改者id(平台成员id)与order_sign相关 字段:company_borrow_orders.order_edit_member
     *
     * @return company_borrow_orders.order_edit_member, 订单修改者id(平台成员id)与order_sign相关
     */
    public Long getOrderEditMember() {
        return orderEditMember;
    }

    /**
     * 设置 订单修改者id(平台成员id)与order_sign相关 字段:company_borrow_orders.order_edit_member
     *
     * @param orderEditMember company_borrow_orders.order_edit_member, 订单修改者id(平台成员id)与order_sign相关
     */
    public void setOrderEditMember(Long orderEditMember) {
        this.orderEditMember = orderEditMember;
    }

    /**
     * 获取 订单修改时间与order_sign相关 字段:company_borrow_orders.order_edit_time
     *
     * @return company_borrow_orders.order_edit_time, 订单修改时间与order_sign相关
     */
    public Date getOrderEditTime() {
        return orderEditTime;
    }

    /**
     * 设置 订单修改时间与order_sign相关 字段:company_borrow_orders.order_edit_time
     *
     * @param orderEditTime company_borrow_orders.order_edit_time, 订单修改时间与order_sign相关
     */
    public void setOrderEditTime(Date orderEditTime) {
        this.orderEditTime = orderEditTime;
    }

    /**
     * 获取 计息方式 1按天 2按月 字段:company_borrow_orders.order_interest_type
     *
     * @return company_borrow_orders.order_interest_type, 计息方式 1按天 2按月
     */
    public Integer getOrderInterestType() {
        return orderInterestType;
    }

    /**
     * 设置 计息方式 1按天 2按月 字段:company_borrow_orders.order_interest_type
     *
     * @param orderInterestType company_borrow_orders.order_interest_type, 计息方式 1按天 2按月
     */
    public void setOrderInterestType(Integer orderInterestType) {
        this.orderInterestType = orderInterestType;
    }

    /**
     * 获取 借款期限 字段:company_borrow_orders.order_interest_cycle
     *
     * @return company_borrow_orders.order_interest_cycle, 借款期限
     */
    public Integer getOrderInterestCycle() {
        return orderInterestCycle;
    }

    /**
     * 设置 借款期限 字段:company_borrow_orders.order_interest_cycle
     *
     * @param orderInterestCycle company_borrow_orders.order_interest_cycle, 借款期限
     */
    public void setOrderInterestCycle(Integer orderInterestCycle) {
        this.orderInterestCycle = orderInterestCycle;
    }

    /**
     * 获取 订单还款方式 1到期还本付息 2按月付息到期还本 3.等额本息 字段:company_borrow_orders.order_repay_type
     *
     * @return company_borrow_orders.order_repay_type, 订单还款方式 1到期还本付息 2按月付息到期还本 3.等额本息
     */
    public Integer getOrderRepayType() {
        return orderRepayType;
    }

    /**
     * 设置 订单还款方式 1到期还本付息 2按月付息到期还本 3.等额本息 字段:company_borrow_orders.order_repay_type
     *
     * @param orderRepayType company_borrow_orders.order_repay_type, 订单还款方式 1到期还本付息 2按月付息到期还本 3.等额本息
     */
    public void setOrderRepayType(Integer orderRepayType) {
        this.orderRepayType = orderRepayType;
    }

    /**
     * 获取 订单约定还款日期,若order_repay_type为2,则为约定第一次还款日期 字段:company_borrow_orders.order_repay_starttime
     *
     * @return company_borrow_orders.order_repay_starttime, 订单约定还款日期,若order_repay_type为2,则为约定第一次还款日期
     */
    public Date getOrderRepayStarttime() {
        return orderRepayStarttime;
    }

    /**
     * 设置 订单约定还款日期,若order_repay_type为2,则为约定第一次还款日期 字段:company_borrow_orders.order_repay_starttime
     *
     * @param orderRepayStarttime company_borrow_orders.order_repay_starttime, 订单约定还款日期,若order_repay_type为2,则为约定第一次还款日期
     */
    public void setOrderRepayStarttime(Date orderRepayStarttime) {
        this.orderRepayStarttime = orderRepayStarttime;
    }

    /**
     * 获取 还款结束日期,order_repay_type为1则与order_repay_starttime相等,若order_repay_type为2,则按期数计算 字段:company_borrow_orders.order_repay_endtime
     *
     * @return company_borrow_orders.order_repay_endtime, 还款结束日期,order_repay_type为1则与order_repay_starttime相等,若order_repay_type为2,则按期数计算
     */
    public Date getOrderRepayEndtime() {
        return orderRepayEndtime;
    }

    /**
     * 设置 还款结束日期,order_repay_type为1则与order_repay_starttime相等,若order_repay_type为2,则按期数计算 字段:company_borrow_orders.order_repay_endtime
     *
     * @param orderRepayEndtime company_borrow_orders.order_repay_endtime, 还款结束日期,order_repay_type为1则与order_repay_starttime相等,若order_repay_type为2,则按期数计算
     */
    public void setOrderRepayEndtime(Date orderRepayEndtime) {
        this.orderRepayEndtime = orderRepayEndtime;
    }

    /**
     * 获取 订单还款期数 order_repay_type为1则为1 字段:company_borrow_orders.order_cycle
     *
     * @return company_borrow_orders.order_cycle, 订单还款期数 order_repay_type为1则为1
     */
    public Integer getOrderCycle() {
        return orderCycle;
    }

    /**
     * 设置 订单还款期数 order_repay_type为1则为1 字段:company_borrow_orders.order_cycle
     *
     * @param orderCycle company_borrow_orders.order_cycle, 订单还款期数 order_repay_type为1则为1
     */
    public void setOrderCycle(Integer orderCycle) {
        this.orderCycle = orderCycle;
    }

    /**
     * 获取 还款分期单位 0一次付清 1天 2月 3年 字段:company_borrow_orders.order_cycle_unit
     *
     * @return company_borrow_orders.order_cycle_unit, 还款分期单位 0一次付清 1天 2月 3年
     */
    public Integer getOrderCycleUnit() {
        return orderCycleUnit;
    }

    /**
     * 设置 还款分期单位 0一次付清 1天 2月 3年 字段:company_borrow_orders.order_cycle_unit
     *
     * @param orderCycleUnit company_borrow_orders.order_cycle_unit, 还款分期单位 0一次付清 1天 2月 3年
     */
    public void setOrderCycleUnit(Integer orderCycleUnit) {
        this.orderCycleUnit = orderCycleUnit;
    }

    /**
     * 获取 借款利率,扩大100倍 字段:company_borrow_orders.order_rate
     *
     * @return company_borrow_orders.order_rate, 借款利率,扩大100倍
     */
    public BigDecimal getOrderRate() {
        return orderRate;
    }

    /**
     * 设置 借款利率,扩大100倍 字段:company_borrow_orders.order_rate
     *
     * @param orderRate company_borrow_orders.order_rate, 借款利率,扩大100倍
     */
    public void setOrderRate(BigDecimal orderRate) {
        this.orderRate = orderRate;
    }

    /**
     * 获取 订单优惠券 字段:company_borrow_orders.order_prop_id
     *
     * @return company_borrow_orders.order_prop_id, 订单优惠券
     */
    public Integer getOrderPropId() {
        return orderPropId;
    }

    /**
     * 设置 订单优惠券 字段:company_borrow_orders.order_prop_id
     *
     * @param orderPropId company_borrow_orders.order_prop_id, 订单优惠券
     */
    public void setOrderPropId(Integer orderPropId) {
        this.orderPropId = orderPropId;
    }

    /**
     * 获取 优惠券类型 1借款抵现 2借款减息 3投资抵现 4投资加息 5投资返现 字段:company_borrow_orders.order_prop_coupon_type
     *
     * @return company_borrow_orders.order_prop_coupon_type, 优惠券类型 1借款抵现 2借款减息 3投资抵现 4投资加息 5投资返现
     */
    public Integer getOrderPropCouponType() {
        return orderPropCouponType;
    }

    /**
     * 设置 优惠券类型 1借款抵现 2借款减息 3投资抵现 4投资加息 5投资返现 字段:company_borrow_orders.order_prop_coupon_type
     *
     * @param orderPropCouponType company_borrow_orders.order_prop_coupon_type, 优惠券类型 1借款抵现 2借款减息 3投资抵现 4投资加息 5投资返现
     */
    public void setOrderPropCouponType(Integer orderPropCouponType) {
        this.orderPropCouponType = orderPropCouponType;
    }

    /**
     * 获取 抵现值 order_coupon_type为1s时值金额,为2时值为减息利率(扩大100倍) 字段:company_borrow_orders.order_prop_coupon_value
     *
     * @return company_borrow_orders.order_prop_coupon_value, 抵现值 order_coupon_type为1s时值金额,为2时值为减息利率(扩大100倍)
     */
    public BigDecimal getOrderPropCouponValue() {
        return orderPropCouponValue;
    }

    /**
     * 设置 抵现值 order_coupon_type为1s时值金额,为2时值为减息利率(扩大100倍) 字段:company_borrow_orders.order_prop_coupon_value
     *
     * @param orderPropCouponValue company_borrow_orders.order_prop_coupon_value, 抵现值 order_coupon_type为1s时值金额,为2时值为减息利率(扩大100倍)
     */
    public void setOrderPropCouponValue(BigDecimal orderPropCouponValue) {
        this.orderPropCouponValue = orderPropCouponValue;
    }

    /**
     * 获取 垫付企业实际到账金额 字段:company_borrow_orders.order_money_arrival
     *
     * @return company_borrow_orders.order_money_arrival, 垫付企业实际到账金额
     */
    public BigDecimal getOrderMoneyArrival() {
        return orderMoneyArrival;
    }

    /**
     * 设置 垫付企业实际到账金额 字段:company_borrow_orders.order_money_arrival
     *
     * @param orderMoneyArrival company_borrow_orders.order_money_arrival, 垫付企业实际到账金额
     */
    public void setOrderMoneyArrival(BigDecimal orderMoneyArrival) {
        this.orderMoneyArrival = orderMoneyArrival;
    }

    /**
     * 获取 优惠折算过后的金额(需要还款金额) 字段:company_borrow_orders.order_actual_money
     *
     * @return company_borrow_orders.order_actual_money, 优惠折算过后的金额(需要还款金额)
     */
    public BigDecimal getOrderActualMoney() {
        return orderActualMoney;
    }

    /**
     * 设置 优惠折算过后的金额(需要还款金额) 字段:company_borrow_orders.order_actual_money
     *
     * @param orderActualMoney company_borrow_orders.order_actual_money, 优惠折算过后的金额(需要还款金额)
     */
    public void setOrderActualMoney(BigDecimal orderActualMoney) {
        this.orderActualMoney = orderActualMoney;
    }

    /**
     * 获取 折算过后还款利息(实际计算的利息) 字段:company_borrow_orders.order_actual_rate
     *
     * @return company_borrow_orders.order_actual_rate, 折算过后还款利息(实际计算的利息)
     */
    public BigDecimal getOrderActualRate() {
        return orderActualRate;
    }

    /**
     * 设置 折算过后还款利息(实际计算的利息) 字段:company_borrow_orders.order_actual_rate
     *
     * @param orderActualRate company_borrow_orders.order_actual_rate, 折算过后还款利息(实际计算的利息)
     */
    public void setOrderActualRate(BigDecimal orderActualRate) {
        this.orderActualRate = orderActualRate;
    }

    /**
     * 获取 服务费百分比 字段:company_borrow_orders.order_actual_server_rate
     *
     * @return company_borrow_orders.order_actual_server_rate, 服务费百分比
     */
    public BigDecimal getOrderActualServerRate() {
        return orderActualServerRate;
    }

    /**
     * 设置 服务费百分比 字段:company_borrow_orders.order_actual_server_rate
     *
     * @param orderActualServerRate company_borrow_orders.order_actual_server_rate, 服务费百分比
     */
    public void setOrderActualServerRate(BigDecimal orderActualServerRate) {
        this.orderActualServerRate = orderActualServerRate;
    }

    /**
     * 获取 保证金比例 字段:company_borrow_orders.order_actual_deposit_rate
     *
     * @return company_borrow_orders.order_actual_deposit_rate, 保证金比例
     */
    public BigDecimal getOrderActualDepositRate() {
        return orderActualDepositRate;
    }

    /**
     * 设置 保证金比例 字段:company_borrow_orders.order_actual_deposit_rate
     *
     * @param orderActualDepositRate company_borrow_orders.order_actual_deposit_rate, 保证金比例
     */
    public void setOrderActualDepositRate(BigDecimal orderActualDepositRate) {
        this.orderActualDepositRate = orderActualDepositRate;
    }

    /**
     * 获取 服务费 字段:company_borrow_orders.order_actual_server
     *
     * @return company_borrow_orders.order_actual_server, 服务费
     */
    public BigDecimal getOrderActualServer() {
        return orderActualServer;
    }

    /**
     * 设置 服务费 字段:company_borrow_orders.order_actual_server
     *
     * @param orderActualServer company_borrow_orders.order_actual_server, 服务费
     */
    public void setOrderActualServer(BigDecimal orderActualServer) {
        this.orderActualServer = orderActualServer;
    }

    /**
     * 获取 保证金 字段:company_borrow_orders.order_actual_deposit
     *
     * @return company_borrow_orders.order_actual_deposit, 保证金
     */
    public BigDecimal getOrderActualDeposit() {
        return orderActualDeposit;
    }

    /**
     * 设置 保证金 字段:company_borrow_orders.order_actual_deposit
     *
     * @param orderActualDeposit company_borrow_orders.order_actual_deposit, 保证金
     */
    public void setOrderActualDeposit(BigDecimal orderActualDeposit) {
        this.orderActualDeposit = orderActualDeposit;
    }

    /**
     * 获取 订单完结时间(最后一次实际还款时间) 字段:company_borrow_orders.order_repay_overtime
     *
     * @return company_borrow_orders.order_repay_overtime, 订单完结时间(最后一次实际还款时间)
     */
    public Date getOrderRepayOvertime() {
        return orderRepayOvertime;
    }

    /**
     * 设置 订单完结时间(最后一次实际还款时间) 字段:company_borrow_orders.order_repay_overtime
     *
     * @param orderRepayOvertime company_borrow_orders.order_repay_overtime, 订单完结时间(最后一次实际还款时间)
     */
    public void setOrderRepayOvertime(Date orderRepayOvertime) {
        this.orderRepayOvertime = orderRepayOvertime;
    }

    /**
     * 获取 类型 1垫付订单 2募集订单(融资) 3代发工资订单 字段:company_borrow_orders.order_type
     *
     * @return company_borrow_orders.order_type, 类型 1垫付订单 2募集订单(融资) 3代发工资订单
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * 设置 类型 1垫付订单 2募集订单(融资) 3代发工资订单 字段:company_borrow_orders.order_type
     *
     * @param orderType company_borrow_orders.order_type, 类型 1垫付订单 2募集订单(融资) 3代发工资订单
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取 款借百分比 字段:company_borrow_orders.order_borrowpercent
     *
     * @return company_borrow_orders.order_borrowpercent, 款借百分比
     */
    public BigDecimal getOrderBorrowpercent() {
        return orderBorrowpercent;
    }

    /**
     * 设置 款借百分比 字段:company_borrow_orders.order_borrowpercent
     *
     * @param orderBorrowpercent company_borrow_orders.order_borrowpercent, 款借百分比
     */
    public void setOrderBorrowpercent(BigDecimal orderBorrowpercent) {
        this.orderBorrowpercent = orderBorrowpercent;
    }

    /**
     * 获取 企业发放总工资 字段:company_borrow_orders.order_salaryall
     *
     * @return company_borrow_orders.order_salaryall, 企业发放总工资
     */
    public BigDecimal getOrderSalaryall() {
        return orderSalaryall;
    }

    /**
     * 设置 企业发放总工资 字段:company_borrow_orders.order_salaryall
     *
     * @param orderSalaryall company_borrow_orders.order_salaryall, 企业发放总工资
     */
    public void setOrderSalaryall(BigDecimal orderSalaryall) {
        this.orderSalaryall = orderSalaryall;
    }

    /**
     * 获取 s申请垫付月份 字段:company_borrow_orders.order_applytime
     *
     * @return company_borrow_orders.order_applytime, s申请垫付月份
     */
    public Date getOrderApplytime() {
        return orderApplytime;
    }

    /**
     * 设置 s申请垫付月份 字段:company_borrow_orders.order_applytime
     *
     * @param orderApplytime company_borrow_orders.order_applytime, s申请垫付月份
     */
    public void setOrderApplytime(Date orderApplytime) {
        this.orderApplytime = orderApplytime;
    }

    /**
     * 获取 发薪日 字段:company_borrow_orders.order_salary_day
     *
     * @return company_borrow_orders.order_salary_day, 发薪日
     */
    public Date getOrderSalaryDay() {
        return orderSalaryDay;
    }

    /**
     * 设置 发薪日 字段:company_borrow_orders.order_salary_day
     *
     * @param orderSalaryDay company_borrow_orders.order_salary_day, 发薪日
     */
    public void setOrderSalaryDay(Date orderSalaryDay) {
        this.orderSalaryDay = orderSalaryDay;
    }

    /**
     * 获取 约定汇款日期 字段:company_borrow_orders.order_appoint_remittance_date
     *
     * @return company_borrow_orders.order_appoint_remittance_date, 约定汇款日期
     */
    public Date getOrderAppointRemittanceDate() {
        return orderAppointRemittanceDate;
    }

    /**
     * 设置 约定汇款日期 字段:company_borrow_orders.order_appoint_remittance_date
     *
     * @param orderAppointRemittanceDate company_borrow_orders.order_appoint_remittance_date, 约定汇款日期
     */
    public void setOrderAppointRemittanceDate(Date orderAppointRemittanceDate) {
        this.orderAppointRemittanceDate = orderAppointRemittanceDate;
    }

    /**
     * 获取 实际汇款到账时间 字段:company_borrow_orders.order_remittance_time
     *
     * @return company_borrow_orders.order_remittance_time, 实际汇款到账时间
     */
    public Date getOrderRemittanceTime() {
        return orderRemittanceTime;
    }

    /**
     * 设置 实际汇款到账时间 字段:company_borrow_orders.order_remittance_time
     *
     * @param orderRemittanceTime company_borrow_orders.order_remittance_time, 实际汇款到账时间
     */
    public void setOrderRemittanceTime(Date orderRemittanceTime) {
        this.orderRemittanceTime = orderRemittanceTime;
    }

    /**
     * 获取 取消原因 字段:company_borrow_orders.order_cancel_remark
     *
     * @return company_borrow_orders.order_cancel_remark, 取消原因
     */
    public String getOrderCancelRemark() {
        return orderCancelRemark;
    }

    /**
     * 设置 取消原因 字段:company_borrow_orders.order_cancel_remark
     *
     * @param orderCancelRemark company_borrow_orders.order_cancel_remark, 取消原因
     */
    public void setOrderCancelRemark(String orderCancelRemark) {
        this.orderCancelRemark = orderCancelRemark == null ? null : orderCancelRemark.trim();
    }

    /**
     * 获取 审核原因 字段:company_borrow_orders.order_audit_remark
     *
     * @return company_borrow_orders.order_audit_remark, 审核原因
     */
    public String getOrderAuditRemark() {
        return orderAuditRemark;
    }

    /**
     * 设置 审核原因 字段:company_borrow_orders.order_audit_remark
     *
     * @param orderAuditRemark company_borrow_orders.order_audit_remark, 审核原因
     */
    public void setOrderAuditRemark(String orderAuditRemark) {
        this.orderAuditRemark = orderAuditRemark == null ? null : orderAuditRemark.trim();
    }

    /**
     * 获取 融资理由 字段:company_borrow_orders.order_finance_resion
     *
     * @return company_borrow_orders.order_finance_resion, 融资理由
     */
    public String getOrderFinanceResion() {
        return orderFinanceResion;
    }

    /**
     * 设置 融资理由 字段:company_borrow_orders.order_finance_resion
     *
     * @param orderFinanceResion company_borrow_orders.order_finance_resion, 融资理由
     */
    public void setOrderFinanceResion(String orderFinanceResion) {
        this.orderFinanceResion = orderFinanceResion == null ? null : orderFinanceResion.trim();
    }

    /**
     * 获取 期望到账日期 字段:company_borrow_orders.order_expect_day
     *
     * @return company_borrow_orders.order_expect_day, 期望到账日期
     */
    public Date getOrderExpectDay() {
        return orderExpectDay;
    }

    /**
     * 设置 期望到账日期 字段:company_borrow_orders.order_expect_day
     *
     * @param orderExpectDay company_borrow_orders.order_expect_day, 期望到账日期
     */
    public void setOrderExpectDay(Date orderExpectDay) {
        this.orderExpectDay = orderExpectDay;
    }

    public String getOrderMoneyValue() {
        return orderMoneyValue;
    }

    public void setOrderMoneyValue(String orderMoneyValue) {
        this.orderMoneyValue = orderMoneyValue;
    }

    public String getOrderActualServerValue() {
        return orderActualServerValue;
    }

    public void setOrderActualServerValue(String orderActualServerValue) {
        this.orderActualServerValue = orderActualServerValue;
    }

    public String getOrderRateValue() {
        return orderRateValue;
    }

    public void setOrderRateValue(String orderRateValue) {
        this.orderRateValue = orderRateValue;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContactTel() {
        return companyContactTel;
    }

    public void setCompanyContactTel(String companyContactTel) {
        this.companyContactTel = companyContactTel;
    }

    public String getOrderExpectDayValue() {
        return orderExpectDayValue;
    }

    public void setOrderExpectDayValue(String orderExpectDayValue) {
        this.orderExpectDayValue = orderExpectDayValue;
    }


    public Date getOrderExpireTime() {
        return orderExpireTime;
    }

    public void setOrderExpireTime(Date orderExpireTime) {
        this.orderExpireTime = orderExpireTime;
    }

    public String getOrderAddtimeValue() {
        return orderAddtimeValue;
    }

    public void setOrderAddtimeValue(String orderAddtimeValue) {
        this.orderAddtimeValue = orderAddtimeValue;
    }
}