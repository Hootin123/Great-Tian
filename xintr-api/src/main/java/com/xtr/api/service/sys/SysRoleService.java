package com.xtr.api.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysRoleBean;

import java.util.List;

/**
 * <p>角色信息服务接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 9:48
 */

public interface SysRoleService {

    /**
     * 新写入数据库记录,sys_role
     *
     * @param record
     */
    int insert(SysRoleBean record);

    /**
     * 根据指定主键获取一条数据库记录,sys_role
     *
     * @param id
     */
    SysRoleBean selectByPrimaryKey(Long id);

    /**
     * 根据主键删除数据库的记录,sys_role
     *
     * @param id
     */
    ResultResponse deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,sys_role
     *
     * @param record
     */
    ResultResponse addSysRole(SysRoleBean record, Long[] menuIds, Long[] btnIds);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_role
     *
     * @param record
     */
    ResultResponse updateSysRole(SysRoleBean record, Long[] menuIds, Long[] btnIds);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_role
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SysRoleBean record);

    /**
     * 分页查询用户信息
     *
     * @param sysRoleBean
     * @return
     */
    ResultResponse selectPageList(SysRoleBean sysRoleBean);

    /**
     * 获取所有角色列表
     *
     * @return
     */
    List<SysRoleBean> queryAll();

}
