package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import org.apache.ibatis.annotations.Param;

public interface CompanyMoneyRecordsWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_money_records
     *
     * @param recordId
     */
    int deleteByPrimaryKey(Long recordId);

    /**
     *  新写入数据库记录,company_money_records
     *
     * @param record
     */
    int insert(CompanyMoneyRecordsBean record);

    /**
     *  动态字段,写入数据库记录,company_money_records
     *
     * @param record
     */
    int insertSelective(CompanyMoneyRecordsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_money_records
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyMoneyRecordsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_money_records
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyMoneyRecordsBean record);

    /**
     * 根据企业id删除数据库记录
     *
     * @param companyId
     * @return
     */
    int deleteByCompanyId(@Param("companyId") Long companyId);
}