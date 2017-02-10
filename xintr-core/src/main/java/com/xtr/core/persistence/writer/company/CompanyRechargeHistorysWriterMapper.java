package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyRechargeHistorysBean;

public interface CompanyRechargeHistorysWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_recharge_historys
     *
     * @param historyId
     */
    int deleteByPrimaryKey(Long historyId);

    /**
     *  新写入数据库记录,company_recharge_historys
     *
     * @param record
     */
    int insert(CompanyRechargeHistorysBean record);

    /**
     *  动态字段,写入数据库记录,company_recharge_historys
     *
     * @param record
     */
    int insertSelective(CompanyRechargeHistorysBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_recharge_historys
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyRechargeHistorysBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_recharge_historys
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyRechargeHistorysBean record);
}