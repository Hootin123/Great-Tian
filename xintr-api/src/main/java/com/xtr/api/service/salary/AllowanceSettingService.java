package com.xtr.api.service.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.dto.salary.AllowanceSettingDto;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * <p>津贴设置Service</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:53.
 */
public interface AllowanceSettingService {

    /**
     * 保存津贴设置
     *
     * @param comapnyId
     * @param memberId
     * @param deptId
     * @param allowanceSettingBean
     * @throws BusinessException
     */
    Long saveAllowanceSetting(Long comapnyId, Long memberId, Long deptId, AllowanceSettingDto allowanceSettingBean) throws BusinessException;

    /**
     * 修改津贴设置
     *
     * @param comapnyId
     * @param allowanceSettingBeans
     * @throws BusinessException
     */
    void updateAllowanceSettings(Long comapnyId, List<AllowanceSettingDto> allowanceSettingBeans) throws BusinessException;

    /**
     * 查询津贴列表
     *
     * @param allowanceSettingBean
     * @return
     */
    List<AllowanceSettingBean> getCompanyAllowanceSettingList(AllowanceSettingBean allowanceSettingBean);

    /**
     * 删除津贴
     *
     * @param ids
     * @throws BusinessException
     */
    void deleteAllowanceSettings(Long companyId, String ids) throws BusinessException;

    /**
     * 获取公司的组织架构及成员
     *
     * @param comapnyId
     * @return
     */
    ResultResponse getOrgMembers(Long comapnyId);
}
