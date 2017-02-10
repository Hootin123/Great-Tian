package com.xtr.api.service.company;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyBorrowBillsBean;
import com.xtr.api.dto.company.CompanyRepayAccountDto;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>企业借款账单</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 17:15
 */
public interface CompanyBorrowBillsService {
	
	/**
	 * 根据企业借款订单id查询账单列表
	 * 
	 * @param borrowOrderId	企业借款订单id
	 */
	List<CompanyBorrowBillsBean> selectListByOrderId(Long borrowOrderId);
    
	/**
     * 分页查询
     * 
     * @param companyBorrowBillsBean
     */
    ResultResponse selectPageList(CompanyBorrowBillsBean companyBorrowBillsBean);

	/**
	 * 根据条件查询还款账单
	 *
	 * @param companyId
	 * @param pageBounds
     * @return
     */
	PageList<CompanyRepayAccountDto> selectRepayPageList(Long companyId, Pager pageBounds);

	/**
	 * 新写入数据库记录,company_borrow_bills
	 *
	 * @param companyBorrowBillsBean
     */
	int addBorrowBills(CompanyBorrowBillsBean companyBorrowBillsBean) throws BusinessException;

	/**
	 * 查询企业最近一个月的还款
	 *
	 * @param companyId
	 * @param state 不传为所有，1:最近一个月
	 * @return
	 */
	BigDecimal selectRepayMoney(Long companyId, Integer state);
}
