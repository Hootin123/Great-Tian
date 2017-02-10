package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.CustomerPayrollDetailBean;
import org.apache.ibatis.annotations.Param;

public interface CustomerPayrollDetailWriterMapper {
    /**
     * 根据主键删除数据库的记录,customer_payroll_detail
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,customer_payroll_detail
     *
     * @param record
     */
    int insert(CustomerPayrollDetailBean record);

    /**
     * 动态字段,写入数据库记录,customer_payroll_detail
     *
     * @param record
     */
    int insertSelective(CustomerPayrollDetailBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,customer_payroll_detail
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerPayrollDetailBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,customer_payroll_detail
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerPayrollDetailBean record);

}