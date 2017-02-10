package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class CompanyIntentBean extends BaseObject implements Serializable {
    /**
     *  意向主键id,所属表字段为company_intent.intent_id
     */
    private Long intentId;

    /**
     *  企业id,所属表字段为company_intent.company_id
     */
    private Long companyId;

    /**
     *  用户id,所属表字段为company_intent.member_id
     */
    private Long memberId;

    /**
     *  1:新用户注册,2:代发工资,所属表字段为company_intent.intent_type
     */
    private Integer intentType;

    /**
     *  首次发起时间,所属表字段为company_intent.first_init_time
     */
    private Date firstInitTime;

    /**
     *  处理时间,所属表字段为company_intent.exec_time
     */
    private Date execTime;

    /**
     *  处理人id,所属表字段为company_intent.exec_uid
     */
    private Long execUid;

    /**
     *  发起次数,所属表字段为company_intent.init_count
     */
    private Integer initCount;

    /**
     *  处理状态 0：未处理，1：关闭意向，2：已处理,所属表字段为company_intent.intent_state
     */
    private Integer intentState;

    /**
     * 获取 意向主键id 字段:company_intent.intent_id
     *
     * @return company_intent.intent_id, 意向主键id
     */
    public Long getIntentId() {
        return intentId;
    }

    /**
     * 设置 意向主键id 字段:company_intent.intent_id
     *
     * @param intentId company_intent.intent_id, 意向主键id
     */
    public void setIntentId(Long intentId) {
        this.intentId = intentId;
    }

    /**
     * 获取 企业id 字段:company_intent.company_id
     *
     * @return company_intent.company_id, 企业id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 企业id 字段:company_intent.company_id
     *
     * @param companyId company_intent.company_id, 企业id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 用户id 字段:company_intent.member_id
     *
     * @return company_intent.member_id, 用户id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置 用户id 字段:company_intent.member_id
     *
     * @param memberId company_intent.member_id, 用户id
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取 1:新用户注册,2:代发工资 字段:company_intent.intent_type
     *
     * @return company_intent.intent_type, 1:新用户注册,2:代发工资
     */
    public Integer getIntentType() {
        return intentType;
    }

    /**
     * 设置 1:新用户注册,2:代发工资 字段:company_intent.intent_type
     *
     * @param intentType company_intent.intent_type, 1:新用户注册,2:代发工资
     */
    public void setIntentType(Integer intentType) {
        this.intentType = intentType;
    }

    /**
     * 获取 首次发起时间 字段:company_intent.first_init_time
     *
     * @return company_intent.first_init_time, 首次发起时间
     */
    public Date getFirstInitTime() {
        return firstInitTime;
    }

    /**
     * 设置 首次发起时间 字段:company_intent.first_init_time
     *
     * @param firstInitTime company_intent.first_init_time, 首次发起时间
     */
    public void setFirstInitTime(Date firstInitTime) {
        this.firstInitTime = firstInitTime;
    }

    /**
     * 获取 处理时间 字段:company_intent.exec_time
     *
     * @return company_intent.exec_time, 处理时间
     */
    public Date getExecTime() {
        return execTime;
    }

    /**
     * 设置 处理时间 字段:company_intent.exec_time
     *
     * @param execTime company_intent.exec_time, 处理时间
     */
    public void setExecTime(Date execTime) {
        this.execTime = execTime;
    }

    /**
     * 获取 处理人id 字段:company_intent.exec_uid
     *
     * @return company_intent.exec_uid, 处理人id
     */
    public Long getExecUid() {
        return execUid;
    }

    /**
     * 设置 处理人id 字段:company_intent.exec_uid
     *
     * @param execUid company_intent.exec_uid, 处理人id
     */
    public void setExecUid(Long execUid) {
        this.execUid = execUid;
    }

    /**
     * 获取 发起次数 字段:company_intent.init_count
     *
     * @return company_intent.init_count, 发起次数
     */
    public Integer getInitCount() {
        return initCount;
    }

    /**
     * 设置 发起次数 字段:company_intent.init_count
     *
     * @param initCount company_intent.init_count, 发起次数
     */
    public void setInitCount(Integer initCount) {
        this.initCount = initCount;
    }

    /**
     * 获取 处理状态 0：未处理，1：关闭意向，2：已处理 字段:company_intent.intent_state
     *
     * @return company_intent.intent_state, 处理状态 0：未处理，1：关闭意向，2：已处理
     */
    public Integer getIntentState() {
        return intentState;
    }

    /**
     * 设置 处理状态 0：未处理，1：关闭意向，2：已处理 字段:company_intent.intent_state
     *
     * @param intentState company_intent.intent_state, 处理状态 0：未处理，1：关闭意向，2：已处理
     */
    public void setIntentState(Integer intentState) {
        this.intentState = intentState;
    }
}