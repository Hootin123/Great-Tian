package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.*;
import com.xtr.api.dto.company.CompanyMenuDto;


import java.util.List;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public interface CompanyMembersService {
/*    *//**
     * 查询
     * @param param
     * @return
     *//*
    Map<String,Object> find(Map<String,Object> param);*/

    /**
     * 检查企业是否已经签约
     *
     * @param companyId
     */
    boolean checkCompanyIsSign(Long companyId);

    /*
    * <p>增加企业登录注册信息</p>
    * @auther 何成彪
    * @createTime2016/7/3 13:55
    */
    int insert(CompanyMembersBean companyMembersBean);

    /*
    * <p>根据注册账号查找企业注册信息</p>
    * @auther 何成彪
    * @createTime2016/7/3 13:58
    */
    CompanyMembersBean  findByMemberLogname(String memberLogname);
    /*
    * <p>方法说明</p>
    * @auther 何成彪
    * @createTime2016/7/3 13:59
    */
    int updateByCondition(CompanyMembersBean companyMembersBean);

    /*
    * <p>根据条件查询企业注册信息</p>
    * @auther 何成彪
    * @createTime2016/7/4 13:22
    */
    CompanyMembersBean findByCondition(Map map);

    /**
     * 根据账号ID获取账户详细信息
     * @param memberId
     * @return
     */
    CompanyMembersBean selectCompanyMembersBean(long memberId);

    /**
     *  根据ID更新用户信息
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyMembersBean record);

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
     * 通过手机号更改用户信息
     * @param companyMembersBean
     * @return
     */
    int updateByPhone(CompanyMembersBean companyMembersBean);

    /**
     * 根据手机号用户信息
     * @param memberLogname
     * @return
     */
    List<CompanyMembersBean> findByMemberPhone(String memberLogname);

    /***
     * 通过手机号 或者 登录名进行查询用户信息
     * @param companyMembersBean
     * @return
     */
  List<CompanyMembersBean> selectByPhoneOrLogname(CompanyMembersBean companyMembersBean );

    /**
     *插入用户返回longId
     * @param companyMembersBean
     * @return
     */
    Long insertForReturnId(CompanyMembersBean companyMembersBean);

    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    List<CompanyMenuBean> selectMenuByUserId(Long userId);

    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    Map<String,String> selectCompanyMenuBeanByUserId(Long userId);

    /**
     * 根据状态查询所属角色
     *
     * @param state
     * @return
     */
    List<CompanyRoleBean> selectCompanyRoleBeanByState(Integer state);

    /*
    * <p>增加系统菜单角色关联表</p>
    * @createTime2016/8/17 13:55
    */
    int companyRoleRelBeanAdd(CompanyRoleRelBean companyRoleRelBean);

    /**
     * 根据管理员查询用户信息
     * @param companyMembersBean
     * @return
     */
    List<CompanyMembersBean> selectByCompanyMembersBean(CompanyMembersBean companyMembersBean);

    /**
     *  除数据库的记录,company_role_rel
     *
     * @param companyRoleRelBean
     */
    int deleteRoleRelBeanById(CompanyRoleRelBean companyRoleRelBean);

    /**
     * 获取企业管理员
     *
     * @param companyId
     * @return
     */
    CompanyMembersBean getCompanyManager(Long companyId);

    /**
     * 查询企业用户待办事项
     * @param memberId
     * @return
     */
    List<TodoMaterBean> selectAllTodomaterByMemberId(Long memberId);

    /**
     * 新增待办事项
     * @param materBean
     * @return
     */
    TodoMaterBean addCompanyMemberMater(TodoMaterBean materBean);

    /**
     * 更新待办事项
     * @param todoMaterBean
     * @return
     */
    int updateMaterById(TodoMaterBean todoMaterBean);

    /**
     * 逻辑删除
     * @param materId
     * @return
     */
    int deleteMaterById(long materId);
    /**
     * 根据员工ID获取菜单的访问权限
     * @param memberId
     * @param menuId
     * @return
     */
    int selectCountForMemberVisitMenu(Long memberId,Long menuId);

    /**
     * 根据菜单名称获取菜单ID
     * @param menuName
     * @return
     */
    long selectMenuIdByMenuName(String menuName);

    /**
     * 判断该用户是都第一次访问该目录（首页、员工管理、工资核算）
     * @param map
     * @return
     */
    CompanyMenuVisitRecordBean selectVisitRecord(Map<String, Object> map);

    /**
     * 保存首次访问目录的记录
     * @param saveVisitRecord
     * @return
     */
    int saveVisitRecord(CompanyMenuVisitRecordBean saveVisitRecord);

    /**
     * 查询当前公司登录过后home页面的菜单显示
     * @param
     * @return
     */
    List<CompanyMenuDto> selectMenusByUserId(Long userId);
}
