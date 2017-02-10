package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyIntentBean;

public interface CompanyIntentWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_intent
     *
     * @param intentId
     */
    int deleteByPrimaryKey(Long intentId);

    /**
     *  新写入数据库记录,company_intent
     *
     * @param record
     */
    int insert(CompanyIntentBean record);

    /**
     *  动态字段,写入数据库记录,company_intent
     *
     * @param record
     */
    int insertSelective(CompanyIntentBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_intent
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyIntentBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_intent
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyIntentBean record);
}