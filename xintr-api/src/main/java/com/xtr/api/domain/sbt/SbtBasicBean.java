package com.xtr.api.domain.sbt;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 社保通基础数据
 */
public class SbtBasicBean extends BaseObject implements Serializable {
    /**
     *  主键,所属表字段为sbt_basic.id
     */
    private Long id;

    /**
     *  城市,所属表字段为sbt_basic.city
     */
    private String city;

    /**
     *  月份,所属表字段为sbt_basic.month
     */
    private Integer month;

    /**
     *  基础数据，存储json,所属表字段为sbt_basic.data
     */
    private String data;

    /**
     *  创建时间,所属表字段为sbt_basic.create_time
     */
    private Date createTime;

    /**
     *  获取 主键 字段:sbt_basic.id 
     *
     *  @return sbt_basic.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     *  设置 主键 字段:sbt_basic.id 
     *
     *  @param id sbt_basic.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *  获取 城市 字段:sbt_basic.city 
     *
     *  @return sbt_basic.city, 城市
     */
    public String getCity() {
        return city;
    }

    /**
     *  设置 城市 字段:sbt_basic.city 
     *
     *  @param city sbt_basic.city, 城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     *  获取 月份 字段:sbt_basic.month 
     *
     *  @return sbt_basic.month, 月份
     */
    public Integer getMonth() {
        return month;
    }

    /**
     *  设置 月份 字段:sbt_basic.month 
     *
     *  @param month sbt_basic.month, 月份
     */
    public void setMonth(Integer month) {
        this.month = month;
    }

    /**
     *  获取 基础数据，存储json 字段:sbt_basic.data 
     *
     *  @return sbt_basic.data, 基础数据，存储json
     */
    public String getData() {
        return data;
    }

    /**
     *  设置 基础数据，存储json 字段:sbt_basic.data 
     *
     *  @param data sbt_basic.data, 基础数据，存储json
     */
    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }

    /**
     *  获取 创建时间 字段:sbt_basic.create_time 
     *
     *  @return sbt_basic.create_time, 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     *  设置 创建时间 字段:sbt_basic.create_time 
     *
     *  @param createTime sbt_basic.create_time, 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}