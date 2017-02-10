package com.xtr.comm.sbt.api;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>计算结果</p>
 *
 * @author 任齐
 * @createTime: 2016/9/12 10:04.
 */
public class CalcResult implements Serializable{

    private Map<String, CalcDetail> r;

    public Map<String, CalcDetail> getR() {
        return r;
    }

    public void setR(Map<String, CalcDetail> r) {
        this.r = r;
    }
}
