package com.xtr.api.dto.shebao;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 员工社保公积金总额实体
 * @Author Xuewu
 * @Date 2016/10/9.
 */
public class CustomerShebaoSumDto implements Serializable {

    private Long customerId;

    //社保总额
    private BigDecimal sbSum = new BigDecimal(0);

    //公积金总额
    private BigDecimal gjjSum = new BigDecimal(0);

    //企业社保部份总额
    private BigDecimal sbOrgSum = new BigDecimal(0);
    //企业公积金部份总额
    private BigDecimal gjjOrgSum = new BigDecimal(0);
    //企业部份总额
    private BigDecimal totalOrgSum=new BigDecimal(0);

    public BigDecimal getTotalOrgSum() {
        return totalOrgSum;
    }

    public void setTotalOrgSum(BigDecimal totalOrgSum) {
        this.totalOrgSum = totalOrgSum;
    }

    public BigDecimal getSbOrgSum() {
        return sbOrgSum;
    }

    public void setSbOrgSum(BigDecimal sbOrgSum) {
        this.sbOrgSum = sbOrgSum;
    }

    public BigDecimal getGjjOrgSum() {
        return gjjOrgSum;
    }

    public void setGjjOrgSum(BigDecimal gjjOrgSum) {
        this.gjjOrgSum = gjjOrgSum;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getSbSum() {
        return sbSum;
    }

    public void setSbSum(BigDecimal sbSum) {
        this.sbSum = sbSum;
    }

    public BigDecimal getGjjSum() {
        return gjjSum;
    }

    public void setGjjSum(BigDecimal gjjSum) {
        this.gjjSum = gjjSum;
    }
}
