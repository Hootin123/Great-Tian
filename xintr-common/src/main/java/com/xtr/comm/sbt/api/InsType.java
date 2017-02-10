package com.xtr.comm.sbt.api;

import java.io.Serializable;

/**
 * <p>社保公积金类型</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 16:44.
 */
public class InsType implements Serializable {

    private String type;
    private double base;
    private String month;

    public InsType(String type, double base) {
        this.type = type;
        this.base = base;
    }

    public InsType(String type, double base, String month) {
        this.type = type;
        this.base = base;
        this.month = month;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBase() {
        return base;
    }

    public void setBase(double base) {
        this.base = base;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
