package com.xtr.comm.sbt.api;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>社保公积金基础数据</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 14:09.
 */
public class Basic implements Serializable, Cloneable {

    /**
     * 缴纳月份
     */
    private int month;
    private List<SocialBase> ins;
    private List<SocialBase> hf;

    public Basic() {
    }

    public List<SocialBase> getIns() {
        return ins;
    }

    public void setIns(List<SocialBase> ins) {
        this.ins = ins;
    }

    public List<SocialBase> getHf() {
        return hf;
    }

    public void setHf(List<SocialBase> hf) {
        this.hf = hf;
    }

    public String toJSONString(){
        return JSON.toJSONString(this);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

}
