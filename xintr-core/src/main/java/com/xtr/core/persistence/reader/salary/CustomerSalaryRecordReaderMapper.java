package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.CustomerSalaryRecordBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomerSalaryRecordReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,customer_salary_record
     *
     * @param salaryRecordId
     */
    CustomerSalaryRecordBean selectByPrimaryKey(Long salaryRecordId);

    /**
     * 根据员工ID和生效日期查询调薪记录
     * @param customerId
     * @param effDate
     * @return
     */
    CustomerSalaryRecordBean selectByCustomerIdAndEffDate(@Param("customerId")long customerId, @Param("effDate")Date effDate);

    /**
     * 根据员工ID和生效日期查询向前最近一条调薪记录
     * @param customerId
     * @param effDate
     * @return
     */
    CustomerSalaryRecordBean selectLastRecordByDate(@Param("customerId")long customerId, @Param("effDate")Date effDate);

    /**
     * 根据员工ID和生效日期查询向后最近一条调薪记录
     * @param customerId
     * @param effDate
     * @return
     */
    CustomerSalaryRecordBean selectFirstRecordByDate(@Param("customerId")long customerId, @Param("effDate")Date effDate);

    /**
     * 根据员工ID和生效日期查询向后最近一条调薪记录
     * @param customerId
     * @param effDate
     * @return
     */
    List<CustomerSalaryRecordBean> selectByDateArea(@Param("customerId")long customerId, @Param("startDate")Date effDate, @Param("endDate")Date endDate);

    /**
     * 根据员工ID批量获取新的工资基数
     * @param list
     * @param effDate
     * @return
     */
    List<CustomerSalaryRecordBean> selectNewSalaryBatch(@Param("list")List<Long> list, @Param("effDate")Date effDate);
}