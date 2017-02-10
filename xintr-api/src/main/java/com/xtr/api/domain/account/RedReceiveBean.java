package com.xtr.api.domain.account;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RedReceiveBean extends BaseObject implements Serializable{
    /**
     *  id,主键,所属表字段为red_receive.id
     */
    private Long id;

    /**
     *  红包id,所属表字段为red_receive.red_id
     */
    private Long redId;

    /**
     *  订单id,使用方主键,所属表字段为red_receive.order_id
     */
    private Long orderId;

    /**
     *  企业id,所属表字段为red_receive.company_id
     */
    private Long companyId;

    /**
     *  使用金额,所属表字段为red_receive.receive_money
     */
    private BigDecimal receiveMoney;

    /**
     *  使用类型(8社保),所属表字段为red_receive.resource_type
     */
    private Integer resourceType;

    /**
     *  使用时间,所属表字段为red_receive.add_time
     */
    private Date addTime;

    /**
     *  获取 id,主键 字段:red_receive.id 
     *
     *  @return red_receive.id, id,主键
     */
    public Long getId() {
        return id;
    }

    /**
     *  设置 id,主键 字段:red_receive.id 
     *
     *  @param id red_receive.id, id,主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *  获取 红包id 字段:red_receive.red_id 
     *
     *  @return red_receive.red_id, 红包id
     */
    public Long getRedId() {
        return redId;
    }

    /**
     *  设置 红包id 字段:red_receive.red_id 
     *
     *  @param redId red_receive.red_id, 红包id
     */
    public void setRedId(Long redId) {
        this.redId = redId;
    }

    /**
     *  获取 订单id,使用方主键 字段:red_receive.order_id 
     *
     *  @return red_receive.order_id, 订单id,使用方主键
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     *  设置 订单id,使用方主键 字段:red_receive.order_id 
     *
     *  @param orderId red_receive.order_id, 订单id,使用方主键
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     *  获取 企业id 字段:red_receive.company_id 
     *
     *  @return red_receive.company_id, 企业id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     *  设置 企业id 字段:red_receive.company_id 
     *
     *  @param companyId red_receive.company_id, 企业id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     *  获取 使用金额 字段:red_receive.receive_money 
     *
     *  @return red_receive.receive_money, 使用金额
     */
    public BigDecimal getReceiveMoney() {
        return receiveMoney;
    }

    /**
     *  设置 使用金额 字段:red_receive.receive_money 
     *
     *  @param receiveMoney red_receive.receive_money, 使用金额
     */
    public void setReceiveMoney(BigDecimal receiveMoney) {
        this.receiveMoney = receiveMoney;
    }

    /**
     *  获取 使用类型(8社保) 字段:red_receive.resource_type 
     *
     *  @return red_receive.resource_type, 使用类型(8社保)
     */
    public Integer getResourceType() {
        return resourceType;
    }

    /**
     *  设置 使用类型(8社保) 字段:red_receive.resource_type 
     *
     *  @param resourceType red_receive.resource_type, 使用类型(8社保)
     */
    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    /**
     *  获取 使用时间 字段:red_receive.add_time 
     *
     *  @return red_receive.add_time, 使用时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     *  设置 使用时间 字段:red_receive.add_time 
     *
     *  @param addTime red_receive.add_time, 使用时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}