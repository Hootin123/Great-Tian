package com.xtr.core.persistence.writer.customer;

import com.xtr.api.domain.customer.CustomerSalarysBean;
import org.apache.ibatis.annotations.Param;

public interface CustomerSalarysWriterMapper {
    /**
     *  根据主键删除数据库的记录,customer_salarys
     *
     * @param salaryId
     */
    int deleteByPrimaryKey(Long salaryId);

    /**
     * 根据企业id删除
     *
     * @param companyId
     * @return
     */
    int deleteByCompanyId(@Param("companyId") Long companyId);

    /**
     *  新写入数据库记录,customer_salarys
     *
     * @param record
     */
    int insert(CustomerSalarysBean record);

    /**
     *  动态字段,写入数据库记录,customer_salarys
     *
     * @param record
     */
    int insertSelective(CustomerSalarysBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_salarys
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerSalarysBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,customer_salarys
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerSalarysBean record);
}