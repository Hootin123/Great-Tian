package com.xtr.api.dto.shebao;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Xuewu
 * @Date 2016/9/19.
 */
public class JrOrderDto implements Serializable {

    private Long customerId;

    private double base;

    private String cityCode;

    private Integer isOver;

    private String type;

    private String month;

    private Integer shebaoType; //1是社保，2是公积金

    private List<JrOrderDto> overOrders;

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

    public List<JrOrderDto> getOverOrders() {
        return overOrders;
    }

    public void setOverOrders(List<JrOrderDto> overOrders) {
        this.overOrders = overOrders;
    }

    public Integer getShebaoType() {
        return shebaoType;
    }

    public void setShebaoType(Integer shebaoType) {
        this.shebaoType = shebaoType;
    }
}
