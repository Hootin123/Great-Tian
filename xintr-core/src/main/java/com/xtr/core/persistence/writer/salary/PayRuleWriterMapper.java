package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.PayRuleBean;

public interface PayRuleWriterMapper {
    /**
     * 根据主键删除数据库的记录,pay_rule
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,pay_rule
     *
     * @param record
     */
    int insert(PayRuleBean record);

    /**
     * 动态字段,写入数据库记录,pay_rule
     *
     * @param record
     */
    int insertSelective(PayRuleBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,pay_rule
     *
     * @param record
     */
    int updateByPrimaryKeySelective(PayRuleBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,pay_rule
     *
     * @param record
     */
    int updateByPrimaryKey(PayRuleBean record);

    /**
     * 根据企业ID更改是否计算公积金社保状态
     * @param payRuleBean
     * @return
     */
    int updateIsSocialSecurity(PayRuleBean payRuleBean);
}