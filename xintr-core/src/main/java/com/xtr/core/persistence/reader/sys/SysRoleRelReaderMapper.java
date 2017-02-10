package com.xtr.core.persistence.reader.sys;

import com.xtr.api.domain.sys.SysRoleRelBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleRelReaderMapper {


    /**
     * 根据用户ID、、关联类型获取关联关系
     *
     * @param objId
     * @param relType
     * @return
     */
    List<SysRoleRelBean> queryByObjId(@Param("objId") Long objId, @Param("relType") Integer relType);


    /**
     * 根据用户ID、、关联类型获取关联关系
     *
     * @param roleId
     * @param relType
     * @return
     */
    List<SysRoleRelBean> queryByRoleId(@Param("roleId") Long roleId, @Param("relType") Integer relType);

}