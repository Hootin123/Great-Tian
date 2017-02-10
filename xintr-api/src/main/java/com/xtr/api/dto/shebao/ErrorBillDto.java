package com.xtr.api.dto.shebao;

import java.io.Serializable;

/**
 * @author:zhangshuai
 * @date: 2016/10/17.
 */
public class ErrorBillDto implements Serializable{

    private String customerId;//员工id
    private String trueName;//姓名
    private String  depName;//部门
    private String phone;//手机号

    private String gjjBase;//公积金基数
    private String sbBase;//社保

    private String sbText;//社保详情
    private String sbReason;//社保原因

    private String gjjText;//公积金详情

    private String gjjReason;//公积金原因


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getSbText() {
        return sbText;
    }

    public void setSbText(String sbText) {
        this.sbText = sbText;
    }

    public String getSbReason() {
        return sbReason;
    }

    public void setSbReason(String sbReason) {
        this.sbReason = sbReason;
    }

    public String getGjjText() {
        return gjjText;
    }

    public void setGjjText(String gjjText) {
        this.gjjText = gjjText;
    }

    public String getGjjReason() {
        return gjjReason;
    }

    public void setGjjReason(String gjjReason) {
        this.gjjReason = gjjReason;
    }



    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGjjBase() {
        return gjjBase;
    }

    public void setGjjBase(String gjjBase) {
        this.gjjBase = gjjBase;
    }

    public String getSbBase() {
        return sbBase;
    }

    public void setSbBase(String sbBase) {
        this.sbBase = sbBase;
    }

}
