package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanySocialOrdersBean;

import java.math.BigDecimal;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/10 14:44
 */
public class CompanySocialOrdersDto extends CompanySocialOrdersBean {

    /**
     * 红包折扣金额
     */
    private BigDecimal redMoney;

    public CompanySocialOrdersDto() {
    }

    public BigDecimal getRedMoney() {
        return redMoney;
    }

    public void setRedMoney(BigDecimal redMoney) {
        this.redMoney = redMoney;
    }
}
