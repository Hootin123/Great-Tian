package com.xtr.core.service.sys;

import com.xtr.api.domain.sys.SysRoleRelBean;
import com.xtr.api.service.sys.SysRoleRelService;
import com.xtr.core.persistence.reader.sys.SysRoleRelReaderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 13:56
 */
@Service("sysRoleRelService")
public class SysRoleRelServiceImpl implements SysRoleRelService {


    @Resource
    private SysRoleRelReaderMapper sysRoleRelReaderMapper;


    /**
     * 根据用户ID、、关联类型获取关联关系
     *
     * @param objId
     * @param relType
     * @return
     */
    public List<SysRoleRelBean> queryByObjId(Long objId, Integer relType) {
        return sysRoleRelReaderMapper.queryByObjId(objId, relType);
    }


    /**
     * 根据用户ID、、关联类型获取关联关系
     *
     * @param roleId
     * @param relType
     * @return
     */
    public List<SysRoleRelBean> queryByRoleId(Long roleId, Integer relType) {
        return sysRoleRelReaderMapper.queryByRoleId(roleId, relType);
    }

}
