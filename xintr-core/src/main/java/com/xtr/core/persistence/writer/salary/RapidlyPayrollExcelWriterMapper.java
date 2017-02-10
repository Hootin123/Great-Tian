package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;

public interface RapidlyPayrollExcelWriterMapper {
    /**
     *  根据主键删除数据库的记录,rapidly_payroll_excel
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    int insert(RapidlyPayrollExcelBean record);

    /**
     *  动态字段,写入数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    int insertSelective(RapidlyPayrollExcelBean record);

    /**
     *  根据指定主键获取一条数据库记录,rapidly_payroll_excel
     *
     * @param id
     */
    RapidlyPayrollExcelBean selectByPrimaryKey(Long id);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RapidlyPayrollExcelBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    int updateByPrimaryKey(RapidlyPayrollExcelBean record);
}