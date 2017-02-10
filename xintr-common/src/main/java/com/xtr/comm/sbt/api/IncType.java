package com.xtr.comm.sbt.api;

import java.io.Serializable;

/**
 * <p>包含的险种和社保公积金类型</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 14:16.
 */
public class IncType implements Serializable {

    private String code;
    private String name;
    private double max;
    private double min;
    private double empprop;
    private double orgprop;

    public IncType() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getEmpprop() {
        return empprop;
    }

    public void setEmpprop(double empprop) {
        this.empprop = empprop;
    }

    public double getOrgprop() {
        return orgprop;
    }

    public void setOrgprop(double orgprop) {
        this.orgprop = orgprop;
    }
}
