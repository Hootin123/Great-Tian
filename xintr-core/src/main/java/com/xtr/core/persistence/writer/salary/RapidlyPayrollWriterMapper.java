package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.RapidlyPayrollBean;

import java.util.List;

public interface RapidlyPayrollWriterMapper {
    /**
     * 根据主键删除数据库的记录,rapidly_payroll
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,rapidly_payroll
     *
     * @param record
     */
    int insert(RapidlyPayrollBean record);

    /**
     * 批量插入工资单
     *
     * @param list
     * @return
     */
    int batchInsert(List<RapidlyPayrollBean> list);

    /**
     * 动态字段,写入数据库记录,rapidly_payroll
     *
     * @param record
     */
    int insertSelective(RapidlyPayrollBean record);

    /**
     * 根据指定主键获取一条数据库记录,rapidly_payroll
     *
     * @param id
     */
    RapidlyPayrollBean selectByPrimaryKey(Long id);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,rapidly_payroll
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RapidlyPayrollBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,rapidly_payroll
     *
     * @param record
     */
    int updateByPrimaryKey(RapidlyPayrollBean record);
}