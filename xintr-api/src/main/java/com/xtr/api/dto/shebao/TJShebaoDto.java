package com.xtr.api.dto.shebao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/9/21 13:38
 */
public class TJShebaoDto implements Serializable {

    private Long customerId;

    private double base;

    private String cityCode;

    private Integer isOver;

    private String type;
    //当前缴纳月
    private String month;
    //关联的类型 1社保 2公积金
    private Integer relationType;

    //企业社保订单ID
    private Long companyShebaoOrderId;

    //员工ID集合
    private List<Long> customerIdList=null;
    //更新前的旧的基数
    private BigDecimal oldBase;
    //基数范围最低基数
    private BigDecimal minBase;
    //基数范围最高基数
    private BigDecimal maxBase;

    public BigDecimal getOldBase() {
        return oldBase;
    }

    public void setOldBase(BigDecimal oldBase) {
        this.oldBase = oldBase;
    }

    public BigDecimal getMinBase() {
        return minBase;
    }

    public void setMinBase(BigDecimal minBase) {
        this.minBase = minBase;
    }

    public BigDecimal getMaxBase() {
        return maxBase;
    }

    public void setMaxBase(BigDecimal maxBase) {
        this.maxBase = maxBase;
    }

    public List<Long> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<Long> customerIdList) {
        this.customerIdList = customerIdList;
    }

    public Long getCompanyShebaoOrderId() {
        return companyShebaoOrderId;
    }

    public void setCompanyShebaoOrderId(Long companyShebaoOrderId) {
        this.companyShebaoOrderId = companyShebaoOrderId;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getIsOver() {
        return isOver;
    }

    public void setIsOver(Integer isOver) {
        this.isOver = isOver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
