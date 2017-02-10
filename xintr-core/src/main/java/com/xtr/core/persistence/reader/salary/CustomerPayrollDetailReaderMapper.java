package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.CustomerPayrollDetailBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerPayrollDetailReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,customer_payroll_detail
     *
     * @param id
     */
    CustomerPayrollDetailBean selectByPrimaryKey(Long id);

    /**
     * 根据工资单主键查询津贴
     *
     * @param customerPayrollId
     * @return
     */
    List<CustomerPayrollDetailBean> selectAllowanceByPayrollId(@Param("customerPayrollId") Long customerPayrollId);

    /**
     * 根据工资单主键查询奖金
     *
     * @param customerPayrollId
     * @return
     */
    List<CustomerPayrollDetailBean> selectBonusByPayrollId(@Param("customerPayrollId") Long customerPayrollId);

    /**
     * 根据工资单主键获取奖金总额
     *
     * @param customerPayrollId
     * @return
     */
    BigDecimal getTotalBonus(Long customerPayrollId);
}