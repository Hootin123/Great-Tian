package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyDepsBean;
import org.apache.ibatis.annotations.Param;

public interface CompanyDepsWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_deps
     *
     * @param depId
     */
    int deleteByPrimaryKey(Long depId);

    /**
     *  新写入数据库记录,company_deps
     *
     * @param record
     */
    int insert(CompanyDepsBean record);

    /**
     *  动态字段,写入数据库记录,company_deps
     *
     * @param record
     */
    int insertSelective(CompanyDepsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_deps
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyDepsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_deps
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyDepsBean record);

    /**
     * 软删除部门
     * @param id
     * @return
     */
    int deleteDeps(long id);

    /**
     * 修改部门领导
     * @param depId
     * @param leaderId
     * @return
     */
    int updateLeader(@Param("depId") long depId, @Param("leaderId") Long leaderId);

    /**
     * 设置部门根节点
     * @param rootId
     * @param companyId
     * @return
     */
    int addRootDept(@Param("rootId") long rootId, @Param("companyId") long companyId);

    /**
     * 删除部门领导
     * @param customerId
     */
    int clearLeader(@Param("leaderId") Long customerId);

    /**
     * 更新根级部门名称
     * @param companyId
     * @param name
     * @return
     */
    int updateRootDeptName(@Param("companyId") long companyId, @Param("name") String name);
}