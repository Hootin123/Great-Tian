package com.xtr.core.persistence.reader.company;

import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.dto.company.CompanysDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyDepsReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,company_deps
     *
     * @param depId
     */
    CompanyDepsBean selectByPrimaryKey(Long depId);


    /**
     * 根据公司Id、部门名称验证该部门是否存在
     *
     * @param depCompanyId
     * @param deptName
     * @return
     */
    CompanyDepsBean checkDeptByName(@Param("depCompanyId") Long depCompanyId, @Param("deptName") String deptName);

    /**
     * 根据企业主键获取部门信息
     *
     * @param companyId
     * @return
     */
    List<CompanysDto> getCompanyTree(Long companyId);

    /**
     * 查询基本部门
     *
     * @param companyId
     * @return
     */
    List<CompanyDepsBean> selectBaseDept(Long companyId);


    /**
     * 查询直部门
     *
     * @param depId
     * @return
     */
    List<CompanyDepsBean> selectChildren(Long depId);

    /**
     * 查询所有部门
     *
     * @param companyId
     * @return
     */
    List<CompanyDepsBean> selectAllDeptByCompanyId(Long companyId);

    /**
     * 根据id列表查询部门
     *
     * @param ids
     * @return
     */
    List<CompanyDepsBean> selectByIds(String[] ids);

    /**
     * 根据部门名称模糊查询部门信息
     *
     * @param companyId
     * @param deptName
     * @return
     */
    List<CompanyDepsBean> selectByDeptName(@Param("companyId") Long companyId, @Param("deptName") String deptName);


    /**
     * 根据企业社保订单号部门信息
     *
     * @param companyShebaoId
     * @return
     */
    List<CompanysDto> getDepsTreeByCompanyShebaoId(Long companyShebaoId);

    /**
     *根据公司名称验证名称是否已经存在
     * @param companyDepsBean
     * @return
     */
    int checkRepeatDeptByName(CompanyDepsBean companyDepsBean);
}