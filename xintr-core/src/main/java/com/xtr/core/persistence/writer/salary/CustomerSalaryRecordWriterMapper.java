package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.CustomerSalaryRecordBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerSalaryRecordWriterMapper {
    /**
     *  根据主键删除数据库的记录,customer_salary_record
     *
     * @param salaryRecordId
     */
    int deleteByPrimaryKey(Long salaryRecordId);

    /**
     *  新写入数据库记录,customer_salary_record
     *
     * @param record
     */
    int insert(CustomerSalaryRecordBean record);

    /**
     *  动态字段,写入数据库记录,customer_salary_record
     *
     * @param record
     */
    int insertSelective(CustomerSalaryRecordBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_salary_record
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerSalaryRecordBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,customer_salary_record
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerSalaryRecordBean record);

    /**
     * 调薪删除当前时间之后的调薪记录
     * @param customerId
     * @param effDate
     * @return
     */
    int deleteByUpdateSalary(@Param("customerId")long customerId, @Param("effDate")Date effDate);
}