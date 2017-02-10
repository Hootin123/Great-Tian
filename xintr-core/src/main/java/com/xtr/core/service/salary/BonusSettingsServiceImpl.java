package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xtr.api.domain.salary.BonusSettingsBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.service.salary.BonusSettingsService;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.StringUtils;
import com.xtr.core.persistence.reader.salary.BonusSettingsReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.salary.BonusSettingsWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>奖金设置Service实现</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:44.
 */
@Service("bonusSettingsService")
public class BonusSettingsServiceImpl implements BonusSettingsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonusSettingsService.class);

    @Resource
    private BonusSettingsWriterMapper bonusSettingsWriterMapper;

    @Resource
    private BonusSettingsReaderMapper bonusSettingsReaderMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomerPayrollService customerPayrollService;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

//    @Resource
//    private PayrollAccountService payrollAccountService;

    /**
     * 保存奖金设置
     *
     * @param comapnyId
     * @param memberId
     * @param deptId
     * @param bonusNames
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void saveBonusSettings(Long comapnyId, Long memberId, Long deptId, String bonusNames) throws BusinessException {
        try {
            if (null == comapnyId) {
                throw new BusinessException("企业id不能为空");
            }

            if (null == memberId) {
                throw new BusinessException("员工id不能为空");
            }

            if (null == bonusNames || bonusNames.equals("")) {
                throw new BusinessException("奖金类型参数为空");
            }

            LOGGER.info("saveBonusSettings 接受参数：" + JSON.toJSONString(bonusNames));

            Date cur = new Date();

            JSONArray array = JSON.parseArray(bonusNames);
            for (int i = 0, len = array.size(); i < len; i++) {
                String name = array.getString(i);
                BonusSettingsBean bonusSettingsBean = new BonusSettingsBean();
                bonusSettingsBean.setCreateTime(cur);
                bonusSettingsBean.setCompanyId(comapnyId);
                bonusSettingsBean.setCompanyMenberId(memberId);
                bonusSettingsBean.setDeptId(deptId);
                bonusSettingsBean.setBonusName(name);
                bonusSettingsWriterMapper.insert(bonusSettingsBean);
                customerPayrollService.saveBonusSettings(bonusSettingsBean);
            }

            updateIsBonus(comapnyId);

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 获取奖金设置列表
     *
     * @param bonusSettingsBean
     * @return
     */
    @Override
    public List<BonusSettingsBean> getCompanyBonusList(BonusSettingsBean bonusSettingsBean) {
        if (null != bonusSettingsBean) {
            return bonusSettingsReaderMapper.selectList(bonusSettingsBean);
        }
        return null;
    }

    /**
     * 删除奖金设置
     *
     * @param bonusIds
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void deleteBonusSettings(Long comapnyId, String bonusIds) throws BusinessException {
        if (StringUtils.isBlank(bonusIds)) {
            throw new BusinessException("奖金id参数为空");
        }

        try {
            JSONArray array = JSON.parseArray(bonusIds);

            if (null != array && array.size() > 0) {
                for (int i = 0, len = array.size(); i < len; i++) {
                    Long id = array.getLong(i);
                    bonusSettingsWriterMapper.deleteByPrimaryKey(id);
                    customerPayrollService.deleteByResourceId(comapnyId,id);
                }
                updateIsBonus(comapnyId);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    private void updateIsBonus(Long comapnyId) throws BusinessException {
        // 更新工资单
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(comapnyId);
        if (null != payCycleBean) {
//            CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByCompanyIdAndPayCycleId(payCycleBean.getCompanyId(), payCycleBean.getId(), null);
//            if (null != customerPayrollBean) {
//                CustomerPayrollBean cuTemp = new CustomerPayrollBean();
////                cuTemp.setId(customerPayrollBean.getId());
//                cuTemp.setPayCycleId(payCycleBean.getId());
//                cuTemp.setIsBonusUpdate(1);
//                payrollAccountService.updateByPrimaryKeySelective(cuTemp);
            customerPayrollWriterMapper.updateAllow(payCycleBean.getId(), 2);
//            }
        }
    }

}
