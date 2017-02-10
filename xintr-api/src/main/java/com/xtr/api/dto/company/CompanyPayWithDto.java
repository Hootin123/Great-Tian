package com.xtr.api.dto.company;

import java.io.Serializable;
import java.util.List;

import com.xtr.api.domain.company.CompanyBorrowBillsBean;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;

/**
 * <p>企业借款账单详情</p>
 *
 * @author 任齐
 * @createTime: 2016/06/28 下午6:32:15
 */
public class CompanyPayWithDto implements Serializable {
	
	/**
	 * 企业借款订单
	 */
	private CompanyBorrowOrdersBean order;
	
	/**
	 * 账单列表
	 */
	private List<CompanyBorrowBillsBean> bills;
	
	public CompanyPayWithDto() {
	}

	public CompanyBorrowOrdersBean getOrder() {
		return order;
	}

	public void setOrder(CompanyBorrowOrdersBean order) {
		this.order = order;
	}

	public List<CompanyBorrowBillsBean> getBills() {
		return bills;
	}

	public void setBills(List<CompanyBorrowBillsBean> bills) {
		this.bills = bills;
	}
	
	
}
