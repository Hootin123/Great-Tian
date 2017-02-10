package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>账户资产数据</p>
 *
 * @author 任齐
 * @createTime: 2016/06/28 下午6:50:05
 */
public class CompanyAccountPropertyDto implements Serializable {
	
	/**
	 * 账户余额
	 */
	private BigDecimal balance;
	
	/**
	 * 冻结余额
	 */
	private BigDecimal block;
	
	/**
	 * 可用余额
	 */
	private BigDecimal available;

	/**
	 * 借款金额
	 */
	private BigDecimal borrow;
	
	/**
	 * 发工资日
	 */
	private List<CompanySalaryExcelBean> payDates;

	/**
	 * 冻结总余额百分比
	 */
	private Double blockPercent = 0.0;

	/**
	 * 可用余额百分比
	 */
	private Double availablePercent = 0.0;

	/**
	 * 红包账户余额
	 */
	private BigDecimal redAmout;

	public CompanyAccountPropertyDto() {
		
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getBlock() {
		return block;
	}

	public void setBlock(BigDecimal block) {
		this.block = block;
	}

	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public List<CompanySalaryExcelBean> getPayDates() {
		return payDates;
	}

	public void setPayDates(List<CompanySalaryExcelBean> payDates) {
		this.payDates = payDates;
	}

	public Double getBlockPercent() {
		return blockPercent;
	}

	public void setBlockPercent(Double blockPercent) {
		this.blockPercent = blockPercent;
	}

	public Double getAvailablePercent() {
		return availablePercent;
	}

	public void setAvailablePercent(Double availablePercent) {
		this.availablePercent = availablePercent;
	}

	public BigDecimal getRedAmout() {
		return redAmout;
	}

	public void setRedAmout(BigDecimal redAmout) {
		this.redAmout = redAmout;
	}

	public BigDecimal getBorrow() {
		return borrow;
	}

	public void setBorrow(BigDecimal borrow) {
		this.borrow = borrow;
	}
}
