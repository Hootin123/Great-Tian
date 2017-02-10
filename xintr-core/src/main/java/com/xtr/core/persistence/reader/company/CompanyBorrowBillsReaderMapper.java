package com.xtr.core.persistence.reader.company;

import java.math.BigDecimal;
import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.GenericCriteria;
import com.xtr.api.dto.company.CompanyRepayAccountDto;
import org.apache.ibatis.annotations.Param;

import com.xtr.api.domain.company.CompanyBorrowBillsBean;

public interface CompanyBorrowBillsReaderMapper {
	
	/**
	 * 根据企业借款订单id查询账单列表
	 * 
	 * @param borrowOrderId	企业借款订单id
	 */
	List<CompanyBorrowBillsBean> selectListByOrderId(@Param("borrowOrderId") Long borrowOrderId);
    
	/**
     * 分页查询
     * 
     * @param companyBorrowBillsBean
     */
    List<CompanyBorrowBillsBean> selectPageList(CompanyBorrowBillsBean companyBorrowBillsBean);

	/**
	 * 根据条件查询还款账单
	 *
	 * @param companyId
	 * @param pageBounds
	 * @return
	 */
	PageList<CompanyRepayAccountDto> selectRepayPageList(@Param("companyId") Long companyId, PageBounds pageBounds);

	/**
	 * 查询企业最近一个月的还款
	 *
	 * @param genericCriteria
	 * @return
	 */
	BigDecimal selectRepayMoney(GenericCriteria genericCriteria);

	/**
	 * 查询今日订单量
	 * @return
     */
	int getTodayOrderNum(@Param("orderType") Integer orderType);

}
