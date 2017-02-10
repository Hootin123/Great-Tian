package com.xtr.api.service.sys;

import com.xtr.api.domain.sys.SysRoleRelBean;

import java.util.List;

/**
 * <p>角色关联服务接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 13:55
 */

public interface SysRoleRelService {


    /**
     * 根据用户ID、、关联类型获取关联关系
     *
     * @param objId
     * @param relType
     * @return
     */
    List<SysRoleRelBean> queryByObjId(Long objId,Integer relType);

    /**
     * 根据用户ID、、关联类型获取关联关系
     *
     * @param roleId
     * @param relType
     * @return
     */
    List<SysRoleRelBean> queryByRoleId(Long roleId, Integer relType);
}
