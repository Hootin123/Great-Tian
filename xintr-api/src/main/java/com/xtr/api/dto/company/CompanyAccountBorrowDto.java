package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyBorrowBillsBean;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>账户应付款数据</p>
 *
 * @author 任齐
 * @createTime: 2016/06/28 下午6:50:05
 */
public class CompanyAccountBorrowDto implements Serializable {

	/**
	 * 还款总额
	 */
	private BigDecimal repaymentTotal;

	/**
	 * 30日到期
	 */
	private BigDecimal monthRepayment;

	/**
	 * 剩余额度
	 */
	private BigDecimal residualAmount;

	private List<CompanyRepayAccountDto> bills;

	public CompanyAccountBorrowDto() {
		
	}

	public BigDecimal getRepaymentTotal() {
		return repaymentTotal;
	}

	public void setRepaymentTotal(BigDecimal repaymentTotal) {
		this.repaymentTotal = repaymentTotal;
	}

	public BigDecimal getMonthRepayment() {
		return monthRepayment;
	}

	public void setMonthRepayment(BigDecimal monthRepayment) {
		this.monthRepayment = monthRepayment;
	}

	public BigDecimal getResidualAmount() {
		return residualAmount;
	}

	public void setResidualAmount(BigDecimal residualAmount) {
		this.residualAmount = residualAmount;
	}

	public List<CompanyRepayAccountDto> getBills() {
		return bills;
	}

	public void setBills(List<CompanyRepayAccountDto> bills) {
		this.bills = bills;
	}
}