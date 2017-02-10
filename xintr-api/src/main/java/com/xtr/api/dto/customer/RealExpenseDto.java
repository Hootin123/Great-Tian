package com.xtr.api.dto.customer;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 报销计算dto
 * @Author Xuewu
 * @Date 2016/9/8.
 */
public class RealExpenseDto implements Serializable {

    //实际额度
    private BigDecimal expenseMax;

    //总报销金额
    private BigDecimal expenseSum;

    //显示报销金额
    private BigDecimal realExpense;

    //显示绩效补充
    private BigDecimal lastExpenseMax;

    //用户报销额度
    private BigDecimal customerCurrentExpense;

    public RealExpenseDto() {
    }

    public RealExpenseDto(BigDecimal expenseMax, BigDecimal expenseSum, BigDecimal realExpense, BigDecimal lastExpenseMax, BigDecimal customerCurrentExpense) {
        this.expenseMax = expenseMax;
        this.expenseSum = expenseSum;
        this.realExpense = realExpense;
        this.lastExpenseMax = lastExpenseMax;
        this.customerCurrentExpense = customerCurrentExpense;
    }

    public BigDecimal getExpenseMax() {
        return expenseMax;
    }

    public void setExpenseMax(BigDecimal expenseMax) {
        this.expenseMax = expenseMax;
    }

    public BigDecimal getExpenseSum() {
        return expenseSum;
    }

    public void setExpenseSum(BigDecimal expenseSum) {
        this.expenseSum = expenseSum;
    }

    public BigDecimal getRealExpense() {
        return realExpense;
    }

    public void setRealExpense(BigDecimal realExpense) {
        this.realExpense = realExpense;
    }

    public BigDecimal getLastExpenseMax() {
        return lastExpenseMax;
    }

    public void setLastExpenseMax(BigDecimal lastExpenseMax) {
        this.lastExpenseMax = lastExpenseMax;
    }

    public BigDecimal getCustomerCurrentExpense() {
        return customerCurrentExpense;
    }

    public void setCustomerCurrentExpense(BigDecimal customerCurrentExpense) {
        this.customerCurrentExpense = customerCurrentExpense;
    }
}
