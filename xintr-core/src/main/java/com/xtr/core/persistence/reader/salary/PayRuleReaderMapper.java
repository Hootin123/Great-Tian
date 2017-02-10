package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.PayRuleBean;
import org.apache.ibatis.annotations.Param;

public interface PayRuleReaderMapper {


    /**
     *  根据指定主键获取一条数据库记录,pay_rule
     *
     * @param id
     */
    PayRuleBean selectByPrimaryKey(Long id);

    /**
     * 根据企业id查询记薪规则
     *
     * @param companyId
     * @return
     */
    PayRuleBean selectByCompanyId(@Param("companyId") Long companyId);
}