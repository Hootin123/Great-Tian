package com.xtr.api.service.company;

import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * 薛武
 */
public interface CompanyDeptService {

    /**
     * 获取全部部门 树结构
     *
     * @param companyId
     * @return
     */
    CompanyDepsBean getTree(Long companyId);


    /**
     * 获取全部部门
     *
     * @param companyId
     * @return
     */
    List<CompanyDepsBean> getAllDept(Long companyId, Long depId);

    /**
     * 保存货更新
     *
     * @param companyDepsBean
     * @param deptEditUserId
     * @return
     */
    boolean saveOrUpdate(CompanyDepsBean companyDepsBean, Long deptEditUserId);

    /**
     * 根据指定主键获取一条数据库记录,company_deps
     *
     * @param depId
     */
    CompanyDepsBean selectByPrimaryKey(Long depId) throws BusinessException;

    /**
     * 软删除部门
     *
     * @param id
     * @param companyId
     * @return
     * @throws BusinessException
     */
    boolean deleteDept(long id, Long companyId) throws BusinessException;

    /**
     * 修改部门领导
     *
     * @param depId
     * @param leaderId
     * @return
     * @throws BusinessException
     */
    boolean updateLeader(long depId, Long leaderId) throws BusinessException;

    /**
     * 初始化公司根级部门
     *
     * @param companysBean
     * @return
     */
    long initDept(CompanysBean companysBean);

    /**
     * 更新根级部门名称
     *
     * @param companyId
     * @param name
     * @return
     */
    int updateRootDeptName(long companyId, String name);

    /**
     * 根据部门名称模糊查询部门信息
     *
     * @param companyId
     * @param deptName
     * @return
     */
    List<CompanyDepsBean> selectByDeptName(Long companyId, String deptName);

    /**
     * 修改部门名称
     * @param companyDepsBean
     * @param deptEditUserId
     * @param newDeptName
     * @throws BusinessException
     */
    void modifyDepts(CompanyDepsBean companyDepsBean, Long deptEditUserId,String newDeptName) throws BusinessException;

    /**
     * 新增部门
     * @param companyDepsBean
     * @param deptEditUserId
     * @throws BusinessException
     */
    void createDept(CompanyDepsBean companyDepsBean, Long deptEditUserId) throws BusinessException;
}
