package com.xtr.core.persistence.writer.account;

import com.xtr.api.domain.account.BankCodeBean;

public interface BankCodeWriterMapper {
    /**
     *  根据主键删除数据库的记录,bank_code
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,bank_code
     *
     * @param record
     */
    int insert(BankCodeBean record);

    /**
     *  动态字段,写入数据库记录,bank_code
     *
     * @param record
     */
    int insertSelective(BankCodeBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,bank_code
     *
     * @param record
     */
    int updateByPrimaryKeySelective(BankCodeBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,bank_code
     *
     * @param record
     */
    int updateByPrimaryKey(BankCodeBean record);
}