package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyBorrowBillsBean;

public interface CompanyBorrowBillsWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_borrow_bills
     *
     * @param billId
     */
    int deleteByPrimaryKey(Long billId);

    /**
     *  新写入数据库记录,company_borrow_bills
     *
     * @param record
     */
    int insert(CompanyBorrowBillsBean record);

    /**
     *  动态字段,写入数据库记录,company_borrow_bills
     *
     * @param record
     */
    int insertSelective(CompanyBorrowBillsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_borrow_bills
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyBorrowBillsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_borrow_bills
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyBorrowBillsBean record);
}