package com.xtr.comm.sbt.api;

import java.io.Serializable;

/**
 * <p>计算结果</p>
 *
 * @author 任齐
 * @createTime: 2016/9/12 10:04.
 */
public class CalcDetail implements Serializable{

    private int base;
    private int co;
    private int in;
    private int odf;
    private String inproportion;
    private String coproportion;
    private int sum;

    public CalcDetail() {
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getCo() {
        return co;
    }

    public void setCo(int co) {
        this.co = co;
    }

    public int getIn() {
        return in;
    }

    public void setIn(int in) {
        this.in = in;
    }

    public String getInproportion() {
        return inproportion;
    }

    public void setInproportion(String inproportion) {
        this.inproportion = inproportion;
    }

    public String getCoproportion() {
        return coproportion;
    }

    public void setCoproportion(String coproportion) {
        this.coproportion = coproportion;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getOdf() {
        return odf;
    }

    public void setOdf(int odf) {
        this.odf = odf;
    }

}
