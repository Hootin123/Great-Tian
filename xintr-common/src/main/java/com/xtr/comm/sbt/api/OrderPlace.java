package com.xtr.comm.sbt.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * <p>提交代缴订单请求对象</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 14:33.
 */
public class OrderPlace implements Serializable{

    // 此用户在社保通中的唯一标识符
    private String usercode;

    // 如若用户需要开票，则需求填入开票信息
    private JSONObject invoice;

    // 报增人员的信息集合，若有报增人员，此集合为必需值
    private JSONArray emps;

    // 报增人员集合
    private JSONArray add;

    // 续缴人员集合
    private JSONArray keep;

    // 停缴人员集合
    private JSONArray stop;

    // 补缴人员集合
    private JSONArray overdue;

    // 调基人员集合
    private JSONArray basechg;

    public OrderPlace() {
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public JSONObject getInvoice() {
        return invoice;
    }

    public void setInvoice(JSONObject invoice) {
        this.invoice = invoice;
    }

    public JSONArray getEmps() {
        return emps;
    }

    public void setEmps(JSONArray emps) {
        this.emps = emps;
    }

    public JSONArray getAdd() {
        return add;
    }

    public void setAdd(JSONArray add) {
        this.add = add;
    }

    public JSONArray getKeep() {
        return keep;
    }

    public void setKeep(JSONArray keep) {
        this.keep = keep;
    }

    public JSONArray getStop() {
        return stop;
    }

    public void setStop(JSONArray stop) {
        this.stop = stop;
    }

    public JSONArray getOverdue() {
        return overdue;
    }

    public void setOverdue(JSONArray overdue) {
        this.overdue = overdue;
    }

    public JSONArray getBasechg() {
        return basechg;
    }

    public void setBasechg(JSONArray basechg) {
        this.basechg = basechg;
    }
}
