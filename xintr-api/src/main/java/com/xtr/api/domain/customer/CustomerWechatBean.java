package com.xtr.api.domain.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工绑定微信实体
 * @author:zhangshuai
 * @date: 2016/9/7.
 */
public class CustomerWechatBean extends BaseObject implements Serializable {


    /**员工绑定微信号表自增主键**/
    private Integer wechatId;
    /**员工id**/
    private Long wechatCustomerId;
    /**员工微信openId**/
    private String wechatOpenId;
    /**员工微信号名称**/
    private String wechatNickName;
    /**员工绑定时间**/
    private Date wechatBindTime;
    /**员工手机号**/
    private String  wechatCustomerPhone;
    /**公司id**/
    private Long wechatCompanyId;

    public Long getWechatCompanyId() {
        return wechatCompanyId;
    }

    public void setWechatCompanyId(Long wechatCompanyId) {
        this.wechatCompanyId = wechatCompanyId;
    }

    public Integer getWechatId() {
        return wechatId;
    }

    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    public Long getWechatCustomerId() {
        return wechatCustomerId;
    }

    public void setWechatCustomerId(Long wechatCustomerId) {
        this.wechatCustomerId = wechatCustomerId;
    }

    public String getWechatOpenId() {
        return wechatOpenId;
    }

    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId;
    }

    public String getWechatNickName() {
        return wechatNickName;
    }

    public void setWechatNickName(String wechatNickName) {
        this.wechatNickName = wechatNickName;
    }

    public Date getWechatBindTime() {
        return wechatBindTime;
    }

    public void setWechatBindTime(Date wechatBindTime) {
        this.wechatBindTime = wechatBindTime;
    }

    public String getWechatCustomerPhone() {
        return wechatCustomerPhone;
    }

    public void setWechatCustomerPhone(String wechatCustomerPhone) {
        this.wechatCustomerPhone = wechatCustomerPhone;
    }
}
