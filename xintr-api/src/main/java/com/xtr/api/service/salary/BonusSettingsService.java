package com.xtr.api.service.salary;

import com.xtr.api.domain.salary.BonusSettingsBean;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * <p>奖金设置Service</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:43.
 */
public interface BonusSettingsService {

    /**
     * 保存奖金设置
     *
     * @param comapnyId
     * @param memberId
     * @param deptId
     * @param bonusNames
     * @throws BusinessException
     */
    void saveBonusSettings(Long comapnyId, Long memberId, Long deptId, String bonusNames) throws BusinessException;

    /**
     * 获取奖金设置列表
     *
     * @param bonusSettingsBean
     * @return
     */
    List<BonusSettingsBean> getCompanyBonusList(BonusSettingsBean bonusSettingsBean);

    /**
     * 删除奖金设置
     *
     * @param bonusIds
     * @throws BusinessException
     */
    void deleteBonusSettings(Long companyId, String bonusIds) throws BusinessException;

}
