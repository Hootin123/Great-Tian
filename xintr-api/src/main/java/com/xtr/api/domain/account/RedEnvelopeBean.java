package com.xtr.api.domain.account;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RedEnvelopeBean extends BaseObject implements Serializable {
    /**
     *  红包主键,所属表字段为red_envelope.red_id
     */
    private Long redId;

    /**
     *  红包名称,所属表字段为red_envelope.name
     */
    private String name;

    /**
     *  活动id,所属表字段为red_envelope.activity_id
     */
    private Long activityId;

    /**
     *  企业id,所属表字段为red_envelope.company_id
     */
    private Long companyId;

    /**
     *  红包个数,所属表字段为red_envelope.red_count
     */
    private Integer redCount;

    /**
     *  红包金额,所属表字段为red_envelope.red_money
     */
    private BigDecimal redMoney;

    /**
     *  红包使用范围,(0任意范围 8社保),所属表字段为red_envelope.red_scope
     */
    private Integer redScope;

    /**
     *  红包发布来源 1:注册 2:H5活动,所属表字段为red_envelope.source_type
     */
    private Integer sourceType;

    /**
     *  发红包时间,所属表字段为red_envelope.add_time
     */
    private Date addTime;

    /**
     *  红包描述,所属表字段为red_envelope.description
     */
    private String description;

    /**
     *  红包使用开始时间,所属表字段为red_envelope.use_start_time
     */
    private Date useStartTime;

    /**
     *  红包使用结束时间,所属表字段为red_envelope.use_end_time
     */
    private Date useEndTime;

    /**
     *  红包状态 0:取消 1:未使用 2:已使用,所属表字段为red_envelope.state
     */
    private Integer state;

    /**
     *  获取 红包主键 字段:red_envelope.red_id 
     *
     *  @return red_envelope.red_id, 红包主键
     */
    public Long getRedId() {
        return redId;
    }

    /**
     *  设置 红包主键 字段:red_envelope.red_id 
     *
     *  @param redId red_envelope.red_id, 红包主键
     */
    public void setRedId(Long redId) {
        this.redId = redId;
    }

    /**
     *  获取 红包名称 字段:red_envelope.name 
     *
     *  @return red_envelope.name, 红包名称
     */
    public String getName() {
        return name;
    }

    /**
     *  设置 红包名称 字段:red_envelope.name 
     *
     *  @param name red_envelope.name, 红包名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     *  获取 活动id 字段:red_envelope.activity_id 
     *
     *  @return red_envelope.activity_id, 活动id
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     *  设置 活动id 字段:red_envelope.activity_id 
     *
     *  @param activityId red_envelope.activity_id, 活动id
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    /**
     *  获取 企业id 字段:red_envelope.company_id 
     *
     *  @return red_envelope.company_id, 企业id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     *  设置 企业id 字段:red_envelope.company_id 
     *
     *  @param companyId red_envelope.company_id, 企业id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     *  获取 红包个数 字段:red_envelope.red_count 
     *
     *  @return red_envelope.red_count, 红包个数
     */
    public Integer getRedCount() {
        return redCount;
    }

    /**
     *  设置 红包个数 字段:red_envelope.red_count 
     *
     *  @param redCount red_envelope.red_count, 红包个数
     */
    public void setRedCount(Integer redCount) {
        this.redCount = redCount;
    }

    /**
     *  获取 红包金额 字段:red_envelope.red_money 
     *
     *  @return red_envelope.red_money, 红包金额
     */
    public BigDecimal getRedMoney() {
        return redMoney;
    }

    /**
     *  设置 红包金额 字段:red_envelope.red_money 
     *
     *  @param redMoney red_envelope.red_money, 红包金额
     */
    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }

    /**
     *  获取 红包使用范围,(0任意范围 8社保) 字段:red_envelope.red_scope 
     *
     *  @return red_envelope.red_scope, 红包使用范围,(0任意范围 8社保)
     */
    public Integer getRedScope() {
        return redScope;
    }

    /**
     *  设置 红包使用范围,(0任意范围 8社保) 字段:red_envelope.red_scope 
     *
     *  @param redScope red_envelope.red_scope, 红包使用范围,(0任意范围 8社保)
     */
    public void setRedScope(Integer redScope) {
        this.redScope = redScope;
    }

    /**
     *  获取 红包发布来源 1:注册 2:H5活动 字段:red_envelope.source_type 
     *
     *  @return red_envelope.source_type, 红包发布来源 1:注册 2:H5活动
     */
    public Integer getSourceType() {
        return sourceType;
    }

    /**
     *  设置 红包发布来源 1:注册 2:H5活动 字段:red_envelope.source_type 
     *
     *  @param sourceType red_envelope.source_type, 红包发布来源 1:注册 2:H5活动
     */
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    /**
     *  获取 发红包时间 字段:red_envelope.add_time 
     *
     *  @return red_envelope.add_time, 发红包时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     *  设置 发红包时间 字段:red_envelope.add_time 
     *
     *  @param addTime red_envelope.add_time, 发红包时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     *  获取 红包描述 字段:red_envelope.description 
     *
     *  @return red_envelope.description, 红包描述
     */
    public String getDescription() {
        return description;
    }

    /**
     *  设置 红包描述 字段:red_envelope.description 
     *
     *  @param description red_envelope.description, 红包描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     *  获取 红包使用开始时间 字段:red_envelope.use_start_time 
     *
     *  @return red_envelope.use_start_time, 红包使用开始时间
     */
    public Date getUseStartTime() {
        return useStartTime;
    }

    /**
     *  设置 红包使用开始时间 字段:red_envelope.use_start_time 
     *
     *  @param useStartTime red_envelope.use_start_time, 红包使用开始时间
     */
    public void setUseStartTime(Date useStartTime) {
        this.useStartTime = useStartTime;
    }

    /**
     *  获取 红包使用结束时间 字段:red_envelope.use_end_time 
     *
     *  @return red_envelope.use_end_time, 红包使用结束时间
     */
    public Date getUseEndTime() {
        return useEndTime;
    }

    /**
     *  设置 红包使用结束时间 字段:red_envelope.use_end_time 
     *
     *  @param useEndTime red_envelope.use_end_time, 红包使用结束时间
     */
    public void setUseEndTime(Date useEndTime) {
        this.useEndTime = useEndTime;
    }

    /**
     *  获取 红包状态 0:取消 1:未使用 2:已使用 字段:red_envelope.state 
     *
     *  @return red_envelope.state, 红包状态 0:取消 1:未使用 2:已使用
     */
    public Integer getState() {
        return state;
    }

    /**
     *  设置 红包状态 0:取消 1:未使用 2:已使用 字段:red_envelope.state 
     *
     *  @param state red_envelope.state, 红包状态 0:取消 1:未使用 2:已使用
     */
    public void setState(Integer state) {
        this.state = state;
    }
}