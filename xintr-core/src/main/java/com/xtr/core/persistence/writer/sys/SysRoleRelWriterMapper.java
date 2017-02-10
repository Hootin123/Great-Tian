package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.SysRoleRelBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleRelWriterMapper {
    /**
     * 新写入数据库记录,sys_role_rel
     *
     * @param record
     */
    int insert(SysRoleRelBean record);

    /**
     * 动态字段,写入数据库记录,sys_role_rel
     *
     * @param record
     */
    int insertSelective(SysRoleRelBean record);

    /**
     * 根据用户ID、关联类型删除关联关系
     *
     * @param objId
     * @param relType
     * @return
     */
    int deleteByObjId(@Param("objId") Long objId, @Param("relType") Integer relType);

    /**
     * 根据角色ID、关联类型删除关联关系
     *
     * @param roleId
     * @param relType
     * @return
     */
    int deleteByRoleId(@Param("roleId") Long roleId, @Param("relType") Integer relType);


}