package com.xtr.api.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;

/**
 * <p>用户信息服务接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/13 14:03
 */
public interface SysUserService {

    /**
     * 根据指定主键获取一条数据库记录,sys_user
     *
     * @param id
     */
    SysUserBean selectByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,sys_user
     *
     * @param record
     */
    int addSysUser(SysUserBean record);


    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_user
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysUserBean record);

    /**
     * 根据主键删除数据库的记录,sys_user
     *
     * @param id
     */
    ResultResponse deleteByPrimaryKey(Long id);

    /**
     * 新增用户授权
     *
     * @param userId
     * @param roleIds
     * @return
     */
    ResultResponse addUserRole(Long userId, Long[] roleIds);

    /**
     * 分页查询用户信息
     *
     * @param sysUserBean
     * @return
     */
    ResultResponse selectPageList(SysUserBean sysUserBean);

    /**
     * 根据手机号或邮箱地址查询用户信息
     *
     * @param userName
     * @return
     */
    SysUserBean selectByEmailOrPhone(String userName, Long id);

    /**
     * 新增用户
     *
     * @param userName
     * @param pwd
     * @return
     */
    ResultResponse insert(String userName, String pwd);

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param configPwd
     * @return
     */
    ResultResponse updatePwd(Long userId, String oldPwd, String newPwd, String configPwd) throws Exception;

}
