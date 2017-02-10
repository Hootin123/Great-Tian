package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.BorrowInfoBean;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;

public interface CompanyBorrowOrdersWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_borrow_orders
     *
     * @param orderId
     */
    int deleteByPrimaryKey(Long orderId);

    /**
     *  新写入数据库记录,company_borrow_orders
     *
     * @param record
     */
    int insert(CompanyBorrowOrdersBean record);

    /**
     *  动态字段,写入数据库记录,company_borrow_orders
     *
     * @param record
     */
    int insertSelective(CompanyBorrowOrdersBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_borrow_orders
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyBorrowOrdersBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_borrow_orders
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyBorrowOrdersBean record);

    /**
     * 动态字段获取主键,写入数据库记录,borrow_info
     *
     * @param borrowInfoBean
     */
    int insertBorrowInfoBeanById(BorrowInfoBean borrowInfoBean);


}