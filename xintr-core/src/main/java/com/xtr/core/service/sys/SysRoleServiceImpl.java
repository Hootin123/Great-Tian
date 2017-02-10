package com.xtr.core.service.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysRoleBean;
import com.xtr.api.domain.sys.SysRoleRelBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.sys.SysRoleService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.sys.SysRoleReaderMapper;
import com.xtr.core.persistence.writer.sys.SysRoleRelWriterMapper;
import com.xtr.core.persistence.writer.sys.SysRoleWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>角色服务实现类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 9:50
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleWriterMapper sysRoleWriterMapper;

    @Resource
    private SysRoleReaderMapper sysRoleReaderMapper;

    @Resource
    private SysRoleRelWriterMapper sysRoleRelWriterMapper;



    /**
     * 根据指定主键获取一条数据库记录,sys_role
     *
     * @param id
     */
    public SysRoleBean selectByPrimaryKey(Long id) {
        if (id != null) {
            return sysRoleReaderMapper.selectByPrimaryKey(id);
        } else {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 新写入数据库记录,sys_role
     *
     * @param record
     */
    public int insert(SysRoleBean record) {
        if (record != null) {
            return sysRoleWriterMapper.insert(record);
        } else {
            throw new BusinessException("新增角色参数为空");
        }
    }

    /**
     * 根据主键删除数据库的记录,sys_role
     *
     * @param id
     */
    @SystemServiceLog(operation = "删除角色", modelName = "角色管理")
    public ResultResponse deleteByPrimaryKey(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        if (id != null) {
            int result = sysRoleWriterMapper.deleteByPrimaryKey(id);
            if (result > 0) {
                resultResponse.setSuccess(true);
            }
        } else {
            resultResponse.setMessage("角色删除失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 新写入数据库记录,sys_role
     *
     * @param record
     */
    public ResultResponse addSysRole(SysRoleBean record, Long[] menuIds, Long[] btnIds) {
        ResultResponse resultResponse = new ResultResponse();
        if (record != null) {
            int result = sysRoleWriterMapper.insert(record);
            if (result > 0) {
                //新增角色&菜单关系
                addRoleMenuRel(record.getId(), menuIds);
                //新增角色&按钮关系
                addRoleBtnRel(record.getId(), btnIds);
                resultResponse.setSuccess(true);
            }
        } else {
            resultResponse.setMessage("角色插入失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_role
     *
     * @param record
     */
    @SystemServiceLog(operation = "新增角色", modelName = "角色管理")
    public ResultResponse updateSysRole(SysRoleBean record, Long[] menuIds, Long[] btnIds) {
        ResultResponse resultResponse = new ResultResponse();
        if (record != null) {
            int result = sysRoleWriterMapper.updateByPrimaryKeySelective(record);
            if (result > 0) {
                //清除关联关系
                sysRoleRelWriterMapper.deleteByRoleId(record.getId(), SysRoleRelBean.RelType.MENU.key);
                //清除关联关系
                sysRoleRelWriterMapper.deleteByRoleId(record.getId(), SysRoleRelBean.RelType.BTN.key);
                //新增角色&菜单关系
                addRoleMenuRel(record.getId(), menuIds);
                //新增角色&按钮关系
                addRoleBtnRel(record.getId(), btnIds);
                resultResponse.setSuccess(true);
            }
        } else {
            resultResponse.setMessage("角色更新失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 新增角色&菜单关系
     *
     * @param roleId
     * @param menuIds
     */
    private void addRoleMenuRel(Long roleId, Long[] menuIds) {
        if (roleId == null || menuIds == null || menuIds.length < 1) {
            return;
        }
        for (Long menuId : menuIds) {
            SysRoleRelBean rel = new SysRoleRelBean();
            rel.setRoleId(roleId);
            rel.setObjId(menuId);
            rel.setRelType(SysRoleRelBean.RelType.MENU.key);
            sysRoleRelWriterMapper.insert(rel);
        }
    }

    /**
     * 新增角色&按钮关系
     *
     * @param roleId
     * @param btnIds
     */
    private void addRoleBtnRel(Long roleId, Long[] btnIds) {
        if (roleId == null || btnIds == null || btnIds.length < 1) {
            return;
        }
        for (Long btnId : btnIds) {
            SysRoleRelBean rel = new SysRoleRelBean();
            rel.setRoleId(roleId);
            rel.setObjId(btnId);
            rel.setRelType(SysRoleRelBean.RelType.BTN.key);
            sysRoleRelWriterMapper.insert(rel);
        }
    }


    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_role
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(SysRoleBean record) {
        if (record != null) {
            return sysRoleWriterMapper.updateByPrimaryKeySelective(record);
        } else {
            throw new BusinessException("修改角色参数为空");
        }
    }

    /**
     * 分页查询用户信息
     *
     * @param sysRoleBean
     * @return
     */
    public ResultResponse selectPageList(SysRoleBean sysRoleBean){
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(sysRoleBean.getPageIndex(), sysRoleBean.getPageSize());
        PageList<SysUserBean> list = sysRoleReaderMapper.listPage(sysRoleBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        return resultResponse;
    }


    /**
     * 获取所有角色列表
     *
     * @return
     */
    public List<SysRoleBean> queryAll(){
        return sysRoleReaderMapper.queryAll();
    }

}
