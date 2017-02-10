package com.xtr.api.dto.account;

import com.xtr.api.domain.account.SubAccountBean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/14 18:40
 */
public class SubAccountDto extends SubAccountBean implements Serializable {

    //是否有新的消息 1有 2无
    private Integer isNewMessage;
    //当月是否有工资 1有 2无
    private Integer isHaveSaraly;
    //实发工资
    private BigDecimal realSaraly;
    private BigDecimal amount;

    public Integer getIsNewMessage() {
        return isNewMessage;
    }

    public void setIsNewMessage(Integer isNewMessage) {
        this.isNewMessage = isNewMessage;
    }

    public Integer getIsHaveSaraly() {
        return isHaveSaraly;
    }

    public void setIsHaveSaraly(Integer isHaveSaraly) {
        this.isHaveSaraly = isHaveSaraly;
    }

    public BigDecimal getRealSaraly() {
        return realSaraly;
    }

    public void setRealSaraly(BigDecimal realSaraly) {
        this.realSaraly = realSaraly;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
