package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 2016/8/26.
 */
public class TodoMaterBean extends BaseObject implements Serializable {

    /**主键***/
    private Integer materId;
    /**备注内容***/
    private String materContent;
    /**企业账号id***/
    private Long materMemberId;
    /**企业id***/
    private Long materCompanyId;
    /**创建时间***/
    private Date materCreateTime;
    /**更新时间***/
    private Date  materUpdateTime;

    /**删除标识  0未删除 1删除**/
    private Integer materStatus;

    public Integer getMaterStatus() {
        return materStatus;
    }

    public void setMaterStatus(Integer materStatus) {
        this.materStatus = materStatus;
    }

    public Integer getMaterId() {
        return materId;
    }

    public void setMaterId(Integer materId) {
        this.materId = materId;
    }

    public String getMaterContent() {
        return materContent;
    }

    public void setMaterContent(String materContent) {
        this.materContent = materContent;
    }

    public Long getMaterMemberId() {
        return materMemberId;
    }

    public void setMaterMemberId(Long materMemberId) {
        this.materMemberId = materMemberId;
    }

    public Long getMaterCompanyId() {
        return materCompanyId;
    }

    public void setMaterCompanyId(Long materCompanyId) {
        this.materCompanyId = materCompanyId;
    }

    public Date getMaterCreateTime() {
        return materCreateTime;
    }

    public void setMaterCreateTime(Date materCreateTime) {
        this.materCreateTime = materCreateTime;
    }


    public void setMaterUpdateTime(Date materUpdateTime) {
        this.materUpdateTime = materUpdateTime;
    }

    public Date getMaterUpdateTime() {
        return materUpdateTime;
    }
}
