package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 企业上传工资文档
 */
public class CompanySocialOrdersBean extends BaseObject implements Serializable {

    private Long orderId;

    private Long orderCompanyId;

    private Long orderDepId;

    private Long orderCompanyMemberId;

    private Date orderAddtime;

    private String orderNumber;

    private String orderSupplierNumber;

    private Integer orderPeopleNumber;

    private Integer orderType;

    private Integer shOrderCount;

    private Double orderMoney;

    private Double orderMoneyServer;

    private Double orderSupplierMoneyServer;

    private Date orderPaydate;

    private String orderCitys;

    private Integer orderStatus;

    private Date orderClosetime;

    private String orderDetail;

    private String orderUpExcel;

    private String orderUpImg;

    private String orderErrExcel;

    private Long orderEditMemberid;

    private Date orderEditTime;

    private Long orderRechargeId;

    private Date orderBeginDate;

    private Date orderEndDate;

    private String orderState;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderCompanyId() {
        return orderCompanyId==null?null:orderCompanyId;
    }

    public void setOrderCompanyId(Long orderCompanyId) {
        this.orderCompanyId = orderCompanyId;
    }

    public Long getOrderDepId() {
        return orderDepId==null?null:orderDepId;
    }

    public void setOrderDepId(Long orderDepId) {
        this.orderDepId = orderDepId;
    }

    public Long getOrderCompanyMemberId() {
        return orderCompanyMemberId==null?null:orderCompanyMemberId;
    }

    public void setOrderCompanyMemberId(Long orderCompanyMemberId) {
        this.orderCompanyMemberId = orderCompanyMemberId;
    }

    public Date getOrderAddtime() {
        return orderAddtime==null?null:orderAddtime;
    }

    public void setOrderAddtime(Date orderAddtime) {
        this.orderAddtime = orderAddtime;
    }

    public String getOrderNumber() {
        return orderNumber==null?null:orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderSupplierNumber() {
        return orderSupplierNumber==null?null:orderSupplierNumber;
    }

    public void setOrderSupplierNumber(String orderSupplierNumber) {
        this.orderSupplierNumber = orderSupplierNumber;
    }

    public Integer getOrderPeopleNumber() {
        return orderPeopleNumber==null?null:orderPeopleNumber;
    }

    public void setOrderPeopleNumber(Integer orderPeopleNumber) {
        this.orderPeopleNumber = orderPeopleNumber;
    }

    public Integer getOrderType() {
        return orderType==null?null:orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Double getOrderMoney() {
        return orderMoney==null?null:orderMoney;
    }

    public void setOrderMoney(Double orderMoney) {
        this.orderMoney = orderMoney;
    }

    public Double getOrderMoneyServer() {
        return orderMoneyServer==null?null:orderMoneyServer;
    }

    public void setOrderMoneyServer(Double orderMoneyServer) {
        this.orderMoneyServer = orderMoneyServer;
    }

    public Double getOrderSupplierMoneyServer() {
        return orderSupplierMoneyServer==null?null:orderSupplierMoneyServer;
    }

    public void setOrderSupplierMoneyServer(Double orderSupplierMoneyServer) {
        this.orderSupplierMoneyServer = orderSupplierMoneyServer;
    }

    public Date getOrderPaydate() {
        return orderPaydate==null?null:orderPaydate;
    }

    public void setOrderPaydate(Date orderPaydate) {
        this.orderPaydate = orderPaydate;
    }

    public String getOrderCitys() {
        return orderCitys==null?null:orderCitys;
    }

    public void setOrderCitys(String orderCitys) {
        this.orderCitys = orderCitys;
    }

    public Integer getOrderStatus() {
        return orderStatus==null?null:orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderClosetime() {
        return orderClosetime==null?null:orderClosetime;
    }

    public void setOrderClosetime(Date orderClosetime) {
        this.orderClosetime = orderClosetime;
    }

    public String getOrderDetail() {
        return orderDetail==null?null:orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderUpExcel() {
        return orderUpExcel==null?null:orderUpExcel;
    }

    public void setOrderUpExcel(String orderUpExcel) {
        this.orderUpExcel = orderUpExcel;
    }

    public String getOrderUpImg() {
        return orderUpImg==null?null:orderUpImg;
    }

    public void setOrderUpImg(String orderUpImg) {
        this.orderUpImg = orderUpImg;
    }

    public String getOrderErrExcel() {
        return orderErrExcel==null?null:orderErrExcel;
    }

    public void setOrderErrExcel(String orderErrExcel) {
        this.orderErrExcel = orderErrExcel;
    }

    public Long getOrderEditMemberid() {
        return orderEditMemberid==null?null:orderEditMemberid;
    }

    public void setOrderEditMemberid(Long orderEditMemberid) {
        this.orderEditMemberid = orderEditMemberid;
    }

    public Date getOrderEditTime() {
        return orderEditTime==null?null:orderEditTime;
    }

    public void setOrderEditTime(Date orderEditTime) {
        this.orderEditTime = orderEditTime;
    }

    public Long getOrderRechargeId() {
        return orderRechargeId==null?null:orderRechargeId;
    }

    public void setOrderRechargeId(Long orderRechargeId) {
        this.orderRechargeId = orderRechargeId;
    }

    public Date getOrderBeginDate() {
        return orderBeginDate==null?null:orderBeginDate;
    }

    public void setOrderBeginDate(Date orderBeginDate) {
        this.orderBeginDate = orderBeginDate;
    }

    public Date getOrderEndDate() {
        return orderEndDate==null?null:orderEndDate;
    }

    public void setOrderEndDate(Date orderEndDate) {
        this.orderEndDate = orderEndDate;
    }

    public String getOrderState() {
        return orderState==null?null:orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Integer getShOrderCount() {
        return shOrderCount;
    }

    public void setShOrderCount(Integer shOrderCount) {
        this.shOrderCount = shOrderCount;
    }
}