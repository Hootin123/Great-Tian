package com.xtr.core.persistence.writer.customer;

import com.xtr.api.domain.customer.CustomerMoneyRecordsBean;
import org.apache.ibatis.annotations.Param;

public interface CustomerMoneyRecordsWriterMapper {
    /**
     *  根据主键删除数据库的记录,customer_money_records
     *
     * @param recordId
     */
    int deleteByPrimaryKey(Long recordId);

    /**
     *  新写入数据库记录,customer_money_records
     *
     * @param record
     */
    int insert(CustomerMoneyRecordsBean record);

    /**
     *  动态字段,写入数据库记录,customer_money_records
     *
     * @param record
     */
    int insertSelective(CustomerMoneyRecordsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_money_records
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerMoneyRecordsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,customer_money_records
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerMoneyRecordsBean record);

    /**
     * 根据企业id删除
     *
     * @param companyId
     */
    void deleteByCompanyId(@Param("companyId") Long companyId);
}