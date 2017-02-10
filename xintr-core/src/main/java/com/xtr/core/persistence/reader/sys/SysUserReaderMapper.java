package com.xtr.core.persistence.reader.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.sys.SysUserBean;
import org.apache.ibatis.annotations.Param;

public interface SysUserReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,sys_user
     *
     * @param id
     */
    SysUserBean selectByPrimaryKey(Long id);


    /**
     * 分页查询用户信息
     *
     * @param sysUserBean
     * @return
     */
    PageList<SysUserBean> listPage(SysUserBean sysUserBean, PageBounds pageBounds);

    /**
     * 根据手机号或邮箱地址查询用户信息
     *
     * @param userName
     * @param id
     * @return
     */
    SysUserBean selectByEmailOrPhone(@Param("userName") String userName,@Param("id") Long id);

}