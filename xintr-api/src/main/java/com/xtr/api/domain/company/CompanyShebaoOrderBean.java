package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CompanyShebaoOrderBean extends BaseObject implements Serializable {
    /**
     *  ,所属表字段为company_shebao_order.id
     */
    private Long id;

    /**
     *  ,所属表字段为company_shebao_order.company_id
     */
    private Long companyId;

    /**
     *  ,所属表字段为company_shebao_order.order_number
     */
    private String orderNumber;

    /**
     *  参保地区名字,所属表字段为company_shebao_order.join_city_name
     */
    private String joinCityName;

    /**
     *  参保地区code,所属表字段为company_shebao_order.join_city_code
     */
    private String joinCityCode;

    /**
     *  缴费日期,所属表字段为company_shebao_order.order_date
     */
    private Date orderDate;

    /**
     * 调基月份
     */
    private Date updateBaseDate;

    /**
     *  社保详情,所属表字段为company_shebao_order.order_sb_detail
     */
    private String orderSbDetail;

    /**
     *  公积金详情,所属表字段为company_shebao_order.order_gjj_detail
     */
    private String orderGjjDetail;

    /**
     *  费用总额,所属表字段为company_shebao_order.price_sum
     */
    private BigDecimal priceSum;

    /**
     *  社保费用,所属表字段为company_shebao_order.price_sb
     */
    private BigDecimal priceSb;

    /**
     *  公积金费用,所属表字段为company_shebao_order.price_gjj
     */
    private BigDecimal priceGjj;

    /**
     *  补差费用,所属表字段为company_shebao_order.price_addtion
     */
    private BigDecimal priceAddtion;

    /**
     *  服务费,所属表字段为company_shebao_order.price_service
     */
    private BigDecimal priceService;

    /**
     *  预留费用字段,所属表字段为company_shebao_order.price_var1
     */
    private BigDecimal priceVar1;

    /**
     *  服务费单价（每人）,所属表字段为company_shebao_order.price_single
     */
    private BigDecimal priceSingle;

    /**
     *  员工数量(按人头计算）,所属表字段为company_shebao_order.customer_count
     */
    private Integer customerCount;

    /**
     *  订单状态,所属表字段为company_shebao_order.status
     */
    private Integer status;

    /**
     *  社保通截止日,所属表字段为company_shebao_order.last_time
     */
    private Date lastTime;

    /**
     *  需求窗口期截止日,所属表字段为company_shebao_order.require_last_time
     */
    private Date requireLastTime;

    /**
     *  订单提交截止日,所属表字段为company_shebao_order.order_last_time
     */
    private Date orderLastTime;

    /**
     *  提交时间,所属表字段为company_shebao_order.submit_time
     */
    private Date submitTime;


    private Integer isCurrent;

    /**
     *  创建时间,所属表字段为company_shebao_order.create_time
     */
    private Date createTime;
    //社保通费用总额
    private BigDecimal shebaotongTotalAmount;
    //社保通服务费
    private BigDecimal shebaotongServiceAmount;
    //社保通补收费
    private BigDecimal shebaotongReceiveAmount;
    //社保通补退费
    private BigDecimal shebaotongBackAmount;
    //社保通滞纳费
    private BigDecimal shebaotongOverdueAmount;
    //社保通社保费
    private BigDecimal shebaotongShebaoAmount;
    //社保通公积金费
    private BigDecimal shebaotongGjjAmount;
    //代缴费用
    private BigDecimal sbAndGjjAmount;
    //服务商编号字段
    private String shebaotongServiceNumber;
    //付款开户行
    private String shebaotongPayBankName;
    //付款企业账户
    private String shebaotongPayCompanyAccount;
    //付款企业账号
    private String shebaotongPayBankNo;
    //付款企业银行交易流水号
    private String shebaotongPayBatch;
    //财务付款状态 1待付款 2已付款
    private Integer shebaotongPayState;
    //财务处理人
    private Long shebaotongPayOperator;
    //提交账单社保通返回信息
    private String shebaotongReturnMessage;
    //财务付款图片证明
    private String shebaotongPayImg;
    //支付错误信息
    private String shebaoFailMsg;
    //支付状态  1失败过
    private Integer shebaoCustomerPayStatus;
    //财务操作人
    private String shebaoFinanceUser;
    //备注
    private String shebaoRemark;

    public String getShebaoRemark() {
        return shebaoRemark;
    }

    public void setShebaoRemark(String shebaoRemark) {
        this.shebaoRemark = shebaoRemark;
    }

    public String getShebaoFinanceUser() {
        return shebaoFinanceUser;
    }

    public void setShebaoFinanceUser(String shebaoFinanceUser) {
        this.shebaoFinanceUser = shebaoFinanceUser;
    }

    public String getShebaoFailMsg() {
        return shebaoFailMsg;
    }

    public void setShebaoFailMsg(String shebaoFailMsg) {
        this.shebaoFailMsg = shebaoFailMsg;
    }

    public Integer getShebaoCustomerPayStatus() {
        return shebaoCustomerPayStatus;
    }

    public void setShebaoCustomerPayStatus(Integer shebaoCustomerPayStatus) {
        this.shebaoCustomerPayStatus = shebaoCustomerPayStatus;
    }

    public String getShebaotongPayImg() {
        return shebaotongPayImg;
    }

    public void setShebaotongPayImg(String shebaotongPayImg) {
        this.shebaotongPayImg = shebaotongPayImg;
    }

    public String getShebaotongReturnMessage() {
        return shebaotongReturnMessage;
    }

    public void setShebaotongReturnMessage(String shebaotongReturnMessage) {
        this.shebaotongReturnMessage = shebaotongReturnMessage;
    }

    public String getShebaotongServiceNumber() {
        return shebaotongServiceNumber;
    }

    public void setShebaotongServiceNumber(String shebaotongServiceNumber) {
        this.shebaotongServiceNumber = shebaotongServiceNumber;
    }

    public String getShebaotongPayBankName() {
        return shebaotongPayBankName;
    }

    public void setShebaotongPayBankName(String shebaotongPayBankName) {
        this.shebaotongPayBankName = shebaotongPayBankName;
    }

    public String getShebaotongPayCompanyAccount() {
        return shebaotongPayCompanyAccount;
    }

    public void setShebaotongPayCompanyAccount(String shebaotongPayCompanyAccount) {
        this.shebaotongPayCompanyAccount = shebaotongPayCompanyAccount;
    }

    public String getShebaotongPayBankNo() {
        return shebaotongPayBankNo;
    }

    public void setShebaotongPayBankNo(String shebaotongPayBankNo) {
        this.shebaotongPayBankNo = shebaotongPayBankNo;
    }

    public String getShebaotongPayBatch() {
        return shebaotongPayBatch;
    }

    public void setShebaotongPayBatch(String shebaotongPayBatch) {
        this.shebaotongPayBatch = shebaotongPayBatch;
    }

    public Integer getShebaotongPayState() {
        return shebaotongPayState;
    }

    public void setShebaotongPayState(Integer shebaotongPayState) {
        this.shebaotongPayState = shebaotongPayState;
    }

    public Long getShebaotongPayOperator() {
        return shebaotongPayOperator;
    }

    public void setShebaotongPayOperator(Long shebaotongPayOperator) {
        this.shebaotongPayOperator = shebaotongPayOperator;
    }

    public BigDecimal getSbAndGjjAmount() {
        return sbAndGjjAmount;
    }

    public void setSbAndGjjAmount(BigDecimal sbAndGjjAmount) {
        this.sbAndGjjAmount = sbAndGjjAmount;
    }

    public BigDecimal getShebaotongTotalAmount() {
        return shebaotongTotalAmount;
    }

    public void setShebaotongTotalAmount(BigDecimal shebaotongTotalAmount) {
        this.shebaotongTotalAmount = shebaotongTotalAmount;
    }

    public BigDecimal getShebaotongServiceAmount() {
        return shebaotongServiceAmount;
    }

    public void setShebaotongServiceAmount(BigDecimal shebaotongServiceAmount) {
        this.shebaotongServiceAmount = shebaotongServiceAmount;
    }

    public BigDecimal getShebaotongReceiveAmount() {
        return shebaotongReceiveAmount;
    }

    public void setShebaotongReceiveAmount(BigDecimal shebaotongReceiveAmount) {
        this.shebaotongReceiveAmount = shebaotongReceiveAmount;
    }

    public BigDecimal getShebaotongBackAmount() {
        return shebaotongBackAmount;
    }

    public void setShebaotongBackAmount(BigDecimal shebaotongBackAmount) {
        this.shebaotongBackAmount = shebaotongBackAmount;
    }

    public BigDecimal getShebaotongOverdueAmount() {
        return shebaotongOverdueAmount;
    }

    public void setShebaotongOverdueAmount(BigDecimal shebaotongOverdueAmount) {
        this.shebaotongOverdueAmount = shebaotongOverdueAmount;
    }

    public BigDecimal getShebaotongShebaoAmount() {
        return shebaotongShebaoAmount;
    }

    public void setShebaotongShebaoAmount(BigDecimal shebaotongShebaoAmount) {
        this.shebaotongShebaoAmount = shebaotongShebaoAmount;
    }

    public BigDecimal getShebaotongGjjAmount() {
        return shebaotongGjjAmount;
    }

    public void setShebaotongGjjAmount(BigDecimal shebaotongGjjAmount) {
        this.shebaotongGjjAmount = shebaotongGjjAmount;
    }

    /**
     *  获取  字段:company_shebao_order.id 
     *
     *  @return company_shebao_order.id, 
     */
    public Long getId() {
        return id;
    }

    /**
     *  设置  字段:company_shebao_order.id 
     *
     *  @param id company_shebao_order.id, 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *  获取  字段:company_shebao_order.company_id 
     *
     *  @return company_shebao_order.company_id, 
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     *  设置  字段:company_shebao_order.company_id 
     *
     *  @param companyId company_shebao_order.company_id, 
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     *  获取  字段:company_shebao_order.order_number 
     *
     *  @return company_shebao_order.order_number, 
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     *  设置  字段:company_shebao_order.order_number 
     *
     *  @param orderNumber company_shebao_order.order_number, 
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    /**
     *  获取 参保地区名字 字段:company_shebao_order.join_city_name 
     *
     *  @return company_shebao_order.join_city_name, 参保地区名字
     */
    public String getJoinCityName() {
        return joinCityName;
    }

    /**
     *  设置 参保地区名字 字段:company_shebao_order.join_city_name 
     *
     *  @param joinCityName company_shebao_order.join_city_name, 参保地区名字
     */
    public void setJoinCityName(String joinCityName) {
        this.joinCityName = joinCityName == null ? null : joinCityName.trim();
    }

    /**
     *  获取 参保地区code 字段:company_shebao_order.join_city_code 
     *
     *  @return company_shebao_order.join_city_code, 参保地区code
     */
    public String getJoinCityCode() {
        return joinCityCode;
    }

    /**
     *  设置 参保地区code 字段:company_shebao_order.join_city_code 
     *
     *  @param joinCityCode company_shebao_order.join_city_code, 参保地区code
     */
    public void setJoinCityCode(String joinCityCode) {
        this.joinCityCode = joinCityCode == null ? null : joinCityCode.trim();
    }

    /**
     *  获取 缴费日期 字段:company_shebao_order.order_date 
     *
     *  @return company_shebao_order.order_date, 缴费日期
     */
    public Date getOrderDate() {
        return orderDate;
    }

    /**
     *  设置 缴费日期 字段:company_shebao_order.order_date 
     *
     *  @param orderDate company_shebao_order.order_date, 缴费日期
     */
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    /**
     *  获取 社保详情 字段:company_shebao_order.order_sb_detail 
     *
     *  @return company_shebao_order.order_sb_detail, 社保详情
     */
    public String getOrderSbDetail() {
        return orderSbDetail;
    }

    /**
     *  设置 社保详情 字段:company_shebao_order.order_sb_detail 
     *
     *  @param orderSbDetail company_shebao_order.order_sb_detail, 社保详情
     */
    public void setOrderSbDetail(String orderSbDetail) {
        this.orderSbDetail = orderSbDetail == null ? null : orderSbDetail.trim();
    }

    /**
     *  获取 公积金详情 字段:company_shebao_order.order_gjj_detail 
     *
     *  @return company_shebao_order.order_gjj_detail, 公积金详情
     */
    public String getOrderGjjDetail() {
        return orderGjjDetail;
    }

    /**
     *  设置 公积金详情 字段:company_shebao_order.order_gjj_detail 
     *
     *  @param orderGjjDetail company_shebao_order.order_gjj_detail, 公积金详情
     */
    public void setOrderGjjDetail(String orderGjjDetail) {
        this.orderGjjDetail = orderGjjDetail == null ? null : orderGjjDetail.trim();
    }

    /**
     *  获取 费用总额 字段:company_shebao_order.price_sum 
     *
     *  @return company_shebao_order.price_sum, 费用总额
     */
    public BigDecimal getPriceSum() {
        return priceSum;
    }

    /**
     *  设置 费用总额 字段:company_shebao_order.price_sum 
     *
     *  @param priceSum company_shebao_order.price_sum, 费用总额
     */
    public void setPriceSum(BigDecimal priceSum) {
        this.priceSum = priceSum;
    }

    /**
     *  获取 社保费用 字段:company_shebao_order.price_sb 
     *
     *  @return company_shebao_order.price_sb, 社保费用
     */
    public BigDecimal getPriceSb() {
        return priceSb;
    }

    /**
     *  设置 社保费用 字段:company_shebao_order.price_sb 
     *
     *  @param priceSb company_shebao_order.price_sb, 社保费用
     */
    public void setPriceSb(BigDecimal priceSb) {
        this.priceSb = priceSb;
    }

    /**
     *  获取 公积金费用 字段:company_shebao_order.price_gjj 
     *
     *  @return company_shebao_order.price_gjj, 公积金费用
     */
    public BigDecimal getPriceGjj() {
        return priceGjj;
    }

    /**
     *  设置 公积金费用 字段:company_shebao_order.price_gjj 
     *
     *  @param priceGjj company_shebao_order.price_gjj, 公积金费用
     */
    public void setPriceGjj(BigDecimal priceGjj) {
        this.priceGjj = priceGjj;
    }

    /**
     *  获取 补差费用 字段:company_shebao_order.price_addtion 
     *
     *  @return company_shebao_order.price_addtion, 补差费用
     */
    public BigDecimal getPriceAddtion() {
        return priceAddtion;
    }

    /**
     *  设置 补差费用 字段:company_shebao_order.price_addtion 
     *
     *  @param priceAddtion company_shebao_order.price_addtion, 补差费用
     */
    public void setPriceAddtion(BigDecimal priceAddtion) {
        this.priceAddtion = priceAddtion;
    }

    /**
     *  获取 服务费 字段:company_shebao_order.price_service 
     *
     *  @return company_shebao_order.price_service, 服务费
     */
    public BigDecimal getPriceService() {
        return priceService;
    }

    /**
     *  设置 服务费 字段:company_shebao_order.price_service 
     *
     *  @param priceService company_shebao_order.price_service, 服务费
     */
    public void setPriceService(BigDecimal priceService) {
        this.priceService = priceService;
    }

    /**
     *  获取 预留费用字段 字段:company_shebao_order.price_var1 
     *
     *  @return company_shebao_order.price_var1, 预留费用字段
     */
    public BigDecimal getPriceVar1() {
        return priceVar1;
    }

    /**
     *  设置 预留费用字段 字段:company_shebao_order.price_var1 
     *
     *  @param priceVar1 company_shebao_order.price_var1, 预留费用字段
     */
    public void setPriceVar1(BigDecimal priceVar1) {
        this.priceVar1 = priceVar1;
    }

    /**
     *  获取 服务费单价（每人） 字段:company_shebao_order.price_single 
     *
     *  @return company_shebao_order.price_single, 服务费单价（每人）
     */
    public BigDecimal getPriceSingle() {
        return priceSingle;
    }

    /**
     *  设置 服务费单价（每人） 字段:company_shebao_order.price_single 
     *
     *  @param priceSingle company_shebao_order.price_single, 服务费单价（每人）
     */
    public void setPriceSingle(BigDecimal priceSingle) {
        this.priceSingle = priceSingle;
    }

    /**
     *  获取 员工数量(按人头计算） 字段:company_shebao_order.customer_count 
     *
     *  @return company_shebao_order.customer_count, 员工数量(按人头计算）
     */
    public Integer getCustomerCount() {
        return customerCount;
    }

    /**
     *  设置 员工数量(按人头计算） 字段:company_shebao_order.customer_count 
     *
     *  @param customerCount company_shebao_order.customer_count, 员工数量(按人头计算）
     */
    public void setCustomerCount(Integer customerCount) {
        this.customerCount = customerCount;
    }

    /**
     *  获取 订单状态 字段:company_shebao_order.status 
     *
     *  @return company_shebao_order.status, 订单状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *  设置 订单状态 字段:company_shebao_order.status 
     *
     *  @param status company_shebao_order.status, 订单状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *  获取 社保通截止日 字段:company_shebao_order.last_time 
     *
     *  @return company_shebao_order.last_time, 社保通截止日
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     *  设置 社保通截止日 字段:company_shebao_order.last_time 
     *
     *  @param lastTime company_shebao_order.last_time, 社保通截止日
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     *  获取 需求窗口期截止日 字段:company_shebao_order.require_last_time 
     *
     *  @return company_shebao_order.require_last_time, 需求窗口期截止日
     */
    public Date getRequireLastTime() {
        return requireLastTime;
    }

    /**
     *  设置 需求窗口期截止日 字段:company_shebao_order.require_last_time 
     *
     *  @param requireLastTime company_shebao_order.require_last_time, 需求窗口期截止日
     */
    public void setRequireLastTime(Date requireLastTime) {
        this.requireLastTime = requireLastTime;
    }

    /**
     *  获取 订单提交截止日 字段:company_shebao_order.order_last_time 
     *
     *  @return company_shebao_order.order_last_time, 订单提交截止日
     */
    public Date getOrderLastTime() {
        return orderLastTime;
    }

    /**
     *  设置 订单提交截止日 字段:company_shebao_order.order_last_time 
     *
     *  @param orderLastTime company_shebao_order.order_last_time, 订单提交截止日
     */
    public void setOrderLastTime(Date orderLastTime) {
        this.orderLastTime = orderLastTime;
    }

    /**
     *  获取 提交时间 字段:company_shebao_order.submit_time 
     *
     *  @return company_shebao_order.submit_time, 提交时间
     */
    public Date getSubmitTime() {
        return submitTime;
    }

    /**
     *  设置 提交时间 字段:company_shebao_order.submit_time 
     *
     *  @param submitTime company_shebao_order.submit_time, 提交时间
     */
    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    /**
     *  获取 创建时间 字段:company_shebao_order.create_time 
     *
     *  @return company_shebao_order.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     *  设置 创建时间 字段:company_shebao_order.create_time 
     *
     *  @param createTime company_shebao_order.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Integer isCurrent) {
        this.isCurrent = isCurrent;
    }

    public Date getUpdateBaseDate() {
        return updateBaseDate;
    }

    public void setUpdateBaseDate(Date updateBaseDate) {
        this.updateBaseDate = updateBaseDate;
    }
}