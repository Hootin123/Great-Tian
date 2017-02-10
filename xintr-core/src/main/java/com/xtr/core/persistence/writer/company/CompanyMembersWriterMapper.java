package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyMenuVisitRecordBean;
import com.xtr.api.domain.company.CompanyRoleRelBean;
import com.xtr.api.domain.company.TodoMaterBean;

public interface CompanyMembersWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_members
     *
     * @param memberId
     */
    int deleteByPrimaryKey(Long memberId);

    /**
     *  新写入数据库记录,company_members
     *
     * @param record
     */
    int insert(CompanyMembersBean record);

    /**
     *  动态字段,写入数据库记录,company_members
     *
     * @param record
     */
    int insertSelective(CompanyMembersBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_members
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyMembersBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_members
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyMembersBean record);
    int updateByCondition(CompanyMembersBean record);

    /**
     * 通过手机号更改用户信息
     * @param companyMembersBean
     * @return
     */
    int updateByPhone(CompanyMembersBean companyMembersBean);

    /**
     *插入用户返回longId
     * @param companyMembersBean
     * @return
     */
    Long insertForReturnId(CompanyMembersBean companyMembersBean);

    /*
   * <p>增加系统菜单角色关联表</p>
   * @createTime2016/8/17 13:55
   */
    int companyRoleRelBeanAdd(CompanyRoleRelBean companyRoleRelBean);

    /**
     *  除数据库的记录,company_role_rel
     *
     * @param companyRoleRelBean
     */
    int deleteRoleRelBeanById(CompanyRoleRelBean companyRoleRelBean);

    /**
     * 新增待办事项
     * @param materBean
     * @return
     */
    int addTodoMater(TodoMaterBean materBean);

    /**
     * 更新待办事项
     * @param todoMaterBean
     * @return
     */
    int updateMaterById(TodoMaterBean todoMaterBean);

    /**
     *逻辑删除待办事项
     * @param materId
     * @return
     */
    int deleteMaterById(long materId);

    /**
     * 保存访问记录
     * @param saveVisitRecord
     * @return
     */
    int saveVisitRecord(CompanyMenuVisitRecordBean saveVisitRecord);
}