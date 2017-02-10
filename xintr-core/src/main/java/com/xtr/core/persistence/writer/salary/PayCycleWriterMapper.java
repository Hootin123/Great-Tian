package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.PayCycleBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface PayCycleWriterMapper {
    /**
     *  根据主键删除数据库的记录,pay_cycle
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,pay_cycle
     *
     * @param record
     */
    int insert(PayCycleBean record);

    /**
     *  动态字段,写入数据库记录,pay_cycle
     *
     * @param record
     */
    int insertSelective(PayCycleBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,pay_cycle
     *
     * @param record
     */
    int updateByPrimaryKeySelective(PayCycleBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,pay_cycle
     *
     * @param record
     */
    int updateByPrimaryKey(PayCycleBean record);

    /**
     * 更改是否生成工资单状态
     * @param isGeneratePayroll
     * @param id
     * @return
     */
    int updateGenerateState(@Param("isGeneratePayroll") int isGeneratePayroll, @Param("id") long id);
}