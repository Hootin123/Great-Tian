package com.xtr.api.service.salary;

import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.comm.basic.BusinessException;

/**
 * <p>记薪规则service</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:36.
 */
public interface PayRuleService {

    /**
     * 保存记薪规则
     *
     * @param payRuleBean
     * @throws BusinessException
     */
    void saveOrUpdatePayRule(PayRuleBean payRuleBean) throws BusinessException;

    /**
     * 查询企业记薪规则
     *
     * @param companyId
     * @return
     */
    PayRuleBean getPayRuleByCompanyId(Long companyId);

    /**
     * 根据企业ID更改是否计算公积金社保状态
     * @param payRuleBean
     * @return
     */
    int updateIsSocialSecurity(PayRuleBean payRuleBean);

}
