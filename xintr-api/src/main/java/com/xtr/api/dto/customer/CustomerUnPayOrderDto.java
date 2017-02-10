package com.xtr.api.dto.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;

/**
 * 未支付订单查询dto
 *
 * @Author Xuewu
 * @Date 2016/8/30.
 */
public class CustomerUnPayOrderDto extends BaseObject implements Serializable {

    //处理状态
    private Integer rechargeState;

    //订单状态
    private Integer payStatus;

    private String rechargeBumber;

    private String financeUser;

    private String companyName;

    //处理类型
    private Integer opeationType;

    public Integer getOpeationType() {
        return opeationType;
    }

    public void setOpeationType(Integer opeationType) {
        this.opeationType = opeationType;
    }

    public Integer getRechargeState() {
        return rechargeState;
    }

    public void setRechargeState(Integer rechargeState) {
        this.rechargeState = rechargeState;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getRechargeBumber() {
        return rechargeBumber;
    }

    public void setRechargeBumber(String rechargeBumber) {
        this.rechargeBumber = rechargeBumber;
    }

    public String getFinanceUser() {
        return financeUser;
    }

    public void setFinanceUser(String financeUser) {
        this.financeUser = financeUser;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
