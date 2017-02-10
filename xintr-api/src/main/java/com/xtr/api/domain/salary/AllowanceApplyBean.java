package com.xtr.api.domain.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class AllowanceApplyBean extends BaseObject implements Serializable {
    /**
     *  主键,所属表字段为allowance_apply.id
     */
    private Long id;

    /**
     *  津贴设置id,所属表字段为allowance_apply.allowance_id
     */
    private Long allowanceId;

    /**
     *  适用类型 1:全体职员 2:部门 3:员工,所属表字段为allowance_apply.type
     */
    private Byte type;

    /**
     *  插入时间,所属表字段为allowance_apply.create_time
     */
    private Date createTime;

    /**
     *  适用人员数据,所属表字段为allowance_apply.user_data
     */
    private String userData;

    /**
     *  获取 主键 字段:allowance_apply.id 
     *
     *  @return allowance_apply.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     *  设置 主键 字段:allowance_apply.id 
     *
     *  @param id allowance_apply.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *  获取 津贴设置id 字段:allowance_apply.allowance_id 
     *
     *  @return allowance_apply.allowance_id, 津贴设置id
     */
    public Long getAllowanceId() {
        return allowanceId;
    }

    /**
     *  设置 津贴设置id 字段:allowance_apply.allowance_id 
     *
     *  @param allowanceId allowance_apply.allowance_id, 津贴设置id
     */
    public void setAllowanceId(Long allowanceId) {
        this.allowanceId = allowanceId;
    }

    /**
     *  获取 适用类型 1:全体职员 2:部门 3:员工 字段:allowance_apply.type 
     *
     *  @return allowance_apply.type, 适用类型 1:全体职员 2:部门 3:员工
     */
    public Byte getType() {
        return type;
    }

    /**
     *  设置 适用类型 1:全体职员 2:部门 3:员工 字段:allowance_apply.type 
     *
     *  @param type allowance_apply.type, 适用类型 1:全体职员 2:部门 3:员工
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     *  获取 插入时间 字段:allowance_apply.create_time 
     *
     *  @return allowance_apply.create_time, 插入时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     *  设置 插入时间 字段:allowance_apply.create_time 
     *
     *  @param createTime allowance_apply.create_time, 插入时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     *  获取 适用人员数据 字段:allowance_apply.user_data 
     *
     *  @return allowance_apply.user_data, 适用人员数据
     */
    public String getUserData() {
        return userData;
    }

    /**
     *  设置 适用人员数据 字段:allowance_apply.user_data 
     *
     *  @param userData allowance_apply.user_data, 适用人员数据
     */
    public void setUserData(String userData) {
        this.userData = userData == null ? null : userData.trim();
    }
}