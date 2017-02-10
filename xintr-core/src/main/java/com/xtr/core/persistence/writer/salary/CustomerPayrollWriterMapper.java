package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.CustomerPayrollBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface CustomerPayrollWriterMapper {
    /**
     * 根据主键删除数据库的记录,customer_payroll
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,customer_payroll
     *
     * @param record
     */
    int insert(CustomerPayrollBean record);

    /**
     * 动态字段,写入数据库记录,customer_payroll
     *
     * @param record
     */
    int insertSelective(CustomerPayrollBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,customer_payroll
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerPayrollBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,customer_payroll
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerPayrollBean record);

    /**
     * 更新工资明细
     *
     * @param customerId
     * @param preTax
     * @param afterTax
     * @param sb
     * @param gjj
     */
    int updateTaxByCyclIdAndCustomerId(@Param("customerId") long customerId, @Param("payCycleId") Long payCycleId, @Param("preTax") BigDecimal preTax, @Param("afterTax") BigDecimal afterTax, @Param("sb") BigDecimal sb, @Param("gjj") BigDecimal gjj);

    /**
     * 更新缺勤天数
     *
     * @param customerId
     * @param days
     */
    void updateAbsenceDayByCyclIdAndCustomerId(@Param("customerId") long customerId, @Param("payCycleId") Long payCycleId, @Param("days") BigDecimal days);

    void updateAllow(@Param("paycycleId") Long paycycleId, @Param("type") Integer type);


    /**
     * 更新员工工资单状态
     *
     * @param customerId
     * @param cycleId
     * @return
     */
    int updatePayRollStatus(@Param("customerId") long customerId, @Param("cycleId") long cycleId);


    /**
     * 更新员工工资单状态
     *
     * @param cycleId
     * @return
     */
    int updatePayRollStatusByCycle(@Param("cycleId") long cycleId);


    /**
     * 根据人员id、公司id、计薪周期id删除工资单
     *
     * @param companyId
     * @param customerId
     * @param cycleId
     * @return
     */
    int deleteByCustomerId(@Param("companyId") long companyId, @Param("customerId") Long customerId, @Param("cycleId") long cycleId);

    /**
     * 更新员工工资单的三个状态
     *
     * @param customerId
     * @param cycleId
     * @return
     */
    int updatePayRollThreeStatus(@Param("customerId") long customerId, @Param("cycleId") long cycleId);
}