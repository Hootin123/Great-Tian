package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyDepositBean;

public interface CompanyDepositWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_deposit
     *
     * @param depositId
     */
    int deleteByPrimaryKey(Long depositId);

    /**
     *  新写入数据库记录,company_deposit
     *
     * @param record
     */
    int insert(CompanyDepositBean record);

    /**
     *  动态字段,写入数据库记录,company_deposit
     *
     * @param record
     */
    int insertSelective(CompanyDepositBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_deposit
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyDepositBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_deposit
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyDepositBean record);
}