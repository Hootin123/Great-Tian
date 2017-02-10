package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyLogBean;

public interface CompanyLogWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_log
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,company_log
     *
     * @param record
     */
    int insert(CompanyLogBean record);

    /**
     *  动态字段,写入数据库记录,company_log
     *
     * @param record
     */
    int insertSelective(CompanyLogBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_log
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyLogBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_log
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyLogBean record);
}