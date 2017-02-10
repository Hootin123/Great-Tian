package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.api.service.salary.PayRuleService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.reader.salary.PayRuleReaderMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import com.xtr.core.persistence.writer.salary.PayRuleWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>记薪规则service实现</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:37.
 */
@Service("payRuleService")
public class PayRuleServiceImpl implements PayRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayRuleService.class);

    @Resource
    private PayRuleWriterMapper payRuleWriterMapper;

    @Resource
    private PayRuleReaderMapper payRuleReaderMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private CustomerPayrollReaderMapper customerPayrollReaderMapper;

    /**
     * 保存记薪规则
     *
     * @param payRuleBean
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void saveOrUpdatePayRule(PayRuleBean payRuleBean) throws BusinessException {
        try {

            if(null == payRuleBean.getCompanyId()){
                throw new BusinessException("企业id不能为空");
            }

            if(null == payRuleBean.getCompanyMenberId()){
                throw new BusinessException("员工id不能为空");
            }

            LOGGER.info("savePayRule 接受参数：" + JSON.toJSONString(payRuleBean));

            PayRuleBean temp = this.getPayRuleByCompanyId(payRuleBean.getCompanyId());
            if(null != temp){
                payRuleBean.setId(temp.getId());
            }

            if(null == payRuleBean.getId()){
                payRuleBean.setCreateTime(new Date());
                payRuleWriterMapper.insert(payRuleBean);
            } else{
                // 不能修改
                payRuleBean.setPayStartDay(null);
                payRuleWriterMapper.updateByPrimaryKeySelective(payRuleBean);
            }
            // 更新工资单
            updateIsPayrule(payRuleBean.getCompanyId());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 查询企业记薪规则
     *
     * @param companyId
     * @return
     */
    @Override
    public PayRuleBean getPayRuleByCompanyId(Long companyId) {
        if(null != companyId){
            return payRuleReaderMapper.selectByCompanyId(companyId);
        }
        return null;
    }

    private void updateIsPayrule(Long companyId) throws BusinessException {
        // 更新工资单
        try {
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
            if (null != payCycleBean) {
                customerPayrollWriterMapper.updateAllow(payCycleBean.getId(), 3);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 根据企业ID更改是否计算公积金社保状态
     * @param payRuleBean
     * @return
     */
    public int updateIsSocialSecurity(PayRuleBean payRuleBean){
        return payRuleWriterMapper.updateIsSocialSecurity(payRuleBean);
    }
}
