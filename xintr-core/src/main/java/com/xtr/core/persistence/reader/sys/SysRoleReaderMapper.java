package com.xtr.core.persistence.reader.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.sys.SysRoleBean;
import com.xtr.api.domain.sys.SysUserBean;

import java.util.List;

public interface SysRoleReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,sys_role
     *
     * @param id
     */
    SysRoleBean selectByPrimaryKey(Long id);

    /**
     * 分页查询用户信息
     *
     * @param sysRoleBean
     * @return
     */
    PageList<SysUserBean> listPage(SysRoleBean sysRoleBean, PageBounds pageBounds);


    /**
     * 获取所有角色列表
     *
     * @return
     */
    List<SysRoleBean> queryAll();

}