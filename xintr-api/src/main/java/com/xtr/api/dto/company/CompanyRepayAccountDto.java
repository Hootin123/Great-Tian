package com.xtr.api.dto.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>企业待还款账单</p>
 *
 * @author 任齐
 * @createTime: 2016/7/3 15:10
 */
public class CompanyRepayAccountDto extends BaseObject implements Serializable {

    private Long billId;

    private Long orderId;

    private BigDecimal billMoney;

    private Date biilRepaytime;

    private Date billAddtime;

    private String description;

    public CompanyRepayAccountDto() {

    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public Date getBiilRepaytime() {
        return biilRepaytime;
    }

    public void setBiilRepaytime(Date biilRepaytime) {
        this.biilRepaytime = biilRepaytime;
    }

    public Date getBillAddtime() {
        return billAddtime;
    }

    public void setBillAddtime(Date billAddtime) {
        this.billAddtime = billAddtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
