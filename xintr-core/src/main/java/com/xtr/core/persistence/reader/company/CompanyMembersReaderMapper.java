package com.xtr.core.persistence.reader.company;

import com.xtr.api.domain.company.*;
import com.xtr.api.dto.company.CompanyMenuDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyMembersReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,company_members
     *
     * @param memberId
     */
    CompanyMembersBean selectByPrimaryKey(Long memberId);
    /**
     * 查询
     * @param memberLogname
     * @return
     */

    CompanyMembersBean selectByMemberLogname(String memberLogname);


    CompanyMembersBean selectByCondition(CompanyMembersBean bean);


    /**
     * 查询企业成员个数
     *
     * @param companyMembersBean
     */
    Long selectCount(CompanyMembersBean companyMembersBean);
    CompanyMembersBean selectByCondition(Map map);

    /**
     * 根据登录名查询激活的用户信息
     * @param companyMembersBean
     * @return
     */
    CompanyMembersBean selectByLoginNameActive(CompanyMembersBean companyMembersBean);

    /**
     * 根据手机号用户信息
     * @param companyMembersBean
     * @return
     */
    List<CompanyMembersBean> selectByPhone(CompanyMembersBean companyMembersBean);

    /**
     * 根据手机号查询可以登录用户
     * @param companyMembersBean
     * @return
     */
    List<CompanyMembersBean> selectCanLoginByPhone(CompanyMembersBean companyMembersBean);

    /**
     * 根据手机号用户信息
     * @param memberLogname
     * @return
     */
    List<CompanyMembersBean> findByMemberPhone(String memberLogname);

    /**
     * 根据手机号 或者 登录名来查询用户信息
     * @param companyMembersBean
     * @return
     */
    List<CompanyMembersBean> selectByPhoneOrLogname(CompanyMembersBean companyMembersBean);

    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    List<CompanyMenuBean> selectMenuByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    Map<String,String> selectCompanyMenuBeanByUserId(@Param("userId") Long userId);

    /**
     * 根据状态查询所属角色
     *
     * @param state
     * @return
     */
    List<CompanyRoleBean> selectCompanyRoleBeanByState(@Param("state") Integer state);

    /**
     * 根据管理员查询用户信息
     * @param companyMembersBean
     * @return
     */
    List<CompanyMembersBean> selectByCompanyMembersBean(CompanyMembersBean companyMembersBean);

    /**
     * 查询主键列表
     *
     * @param companyMembersBean
     * @return
     */
    List<Long> selectPrimaryKeyByCompanyMembersBean(CompanyMembersBean companyMembersBean);

    CompanyMembersBean selectCompanyManager(@Param("companyId") Long companyId);

    /**
     * 查询当前企业账户的所有待办事项
     * @param memberId
     * @return
     */
    List<TodoMaterBean> selectAllTodomaterByMemberId(Long memberId);

    /**
     * 根据员工ID获取菜单的访问权限
     * @param memberId
     * @param menuId
     * @return
     */
    int selectCountForMemberVisitMenu(@Param("memberId") Long memberId,@Param("menuId") Long menuId);

    /**
     * 根据菜单名称获取菜单ID
     * @param menuName
     * @return
     */
    long selectMenuIdByMenuName(@Param("menuName") String menuName);

    /**
     * 查询之前是否访问过
     * @param map
     * @return
     */
    CompanyMenuVisitRecordBean selectVisitRecord(Map<String, Object> map);

    /**
     * 新版home页面查询所有的一级菜单
     * @param userId
     * @return
     */
    List<CompanyMenuDto> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 新版home页面插叙所有的二级菜单
      * @param id
     * @param userId
     * @return
     */
    List<CompanyMenuBean> getChildMenuByUser(@Param("id") Long id, @Param("userId") Long userId);
}