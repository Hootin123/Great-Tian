package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 11:29
 */

public interface CompanyDepsService {

    /**
     * 根据指定主键获取一条数据库记录,company_deps
     *
     * @param depId
     */
    CompanyDepsBean selectByPrimaryKey(Long depId) throws BusinessException;

    /**
     * 新写入数据库记录,company_deps
     *
     * @param record
     */
    void insert(CompanyDepsBean record) throws BusinessException;

    /**
     * 根据公司Id、部门名称验证该部门是否存在
     *
     * @param depParentId
     * @param deptName
     * @return
     */
    ResultResponse checkDeptByName(Long depParentId, String deptName) throws BusinessException;

    /**
     * 修改部门名称
     *
     * @param deptId
     * @return
     */
    void updateDeptName(Long deptId, String deptName) throws BusinessException;

    /**
     * 修改部门名称
     *
     * @param deptId
     * @return
     */
    void deleteDept(Long deptId) throws BusinessException;

    /**
     * 根据企业主键获取部门信息
     *
     * @param companyId
     * @return
     */
    List<CompanysDto> getCompanyTree(Long companyId) throws BusinessException;

    /**
     * 根据企业社保订单号部门信息
     *
     * @param companyShebaoId
     * @return
     */
    List<CompanysDto> getDepsTreeByCompanyShebaoId(Long companyShebaoId) throws BusinessException;
}
