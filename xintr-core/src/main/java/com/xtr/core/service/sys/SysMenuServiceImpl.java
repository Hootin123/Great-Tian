package com.xtr.core.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBean;
import com.xtr.api.domain.sys.SysMenuBtnBean;
import com.xtr.api.dto.sys.SysMenuDto;
import com.xtr.api.service.sys.SysMenuService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.sys.SysMenuReaderMapper;
import com.xtr.core.persistence.writer.sys.SysMenuBtnWriterMapper;
import com.xtr.core.persistence.writer.sys.SysMenuWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>系统菜单服务实现类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 10:07
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuWriterMapper sysMenuWriterMapper;

    @Resource
    private SysMenuReaderMapper sysMenuReaderMapper;

    @Resource
    private SysMenuBtnWriterMapper sysMenuBtnWriterMapper;


    /**
     * 根据指定主键获取一条数据库记录,sys_menu
     *
     * @param id
     */
    public SysMenuBean selectByPrimaryKey(Long id) {
        if (id != null) {
            return sysMenuReaderMapper.selectByPrimaryKey(id);
        } else {
            throw new BusinessException("系统菜单查询失败，参数不能为空");
        }
    }

    /**
     * 根据主键删除数据库的记录,sys_menu
     *
     * @param id
     */
    @SystemServiceLog(operation = "删除菜单", modelName = "菜单管理")
    public ResultResponse deleteByPrimaryKey(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        if (id != null) {
            int result = sysMenuWriterMapper.deleteByPrimaryKey(id);
            if (result > 0) {
                //删除按钮
                sysMenuBtnWriterMapper.deleteByMenuId(id);
                resultResponse.setSuccess(true);
            }
        } else {
            resultResponse.setMessage("系统菜单删除失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 新写入数据库记录,sys_menu
     *
     * @param record
     */
    @SystemServiceLog(operation = "新增菜单", modelName = "菜单管理")
    public ResultResponse saveSysMenu(SysMenuBean record) {
        ResultResponse resultResponse = new ResultResponse();
        if (record != null) {
            record.setIsDelete(0);
            int result = sysMenuWriterMapper.insert(record);
            if (result > 0) {
                resultResponse.setSuccess(true);
                //新增按钮
                saveBtns(record.getId(), record.getMenuBtnBeanList());
            }
        } else {
            resultResponse.setMessage("系统菜单新增失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 保存系统菜单按钮
     *
     * @param menuId
     * @param btnBeanList
     */
    private void saveBtns(Long menuId, List<SysMenuBtnBean> btnBeanList) {
        if (btnBeanList == null || btnBeanList.isEmpty()) {
            return;
        }

        for (SysMenuBtnBean sysMenuBtnBean : btnBeanList) {
            if (sysMenuBtnBean.getId() != null && StringUtils.equals("1", sysMenuBtnBean.getDeleteFlag())) {
                sysMenuBtnWriterMapper.deleteByPrimaryKey(sysMenuBtnBean.getId());
                continue;
            }
            sysMenuBtnBean.setMenuId(menuId);
            if (sysMenuBtnBean.getId() == null) {
                sysMenuBtnWriterMapper.insert(sysMenuBtnBean);
            } else {
                sysMenuBtnWriterMapper.updateByPrimaryKeySelective(sysMenuBtnBean);
            }
        }
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sys_menu
     *
     * @param record
     */
    @SystemServiceLog(operation = "修改菜单", modelName = "菜单管理")
    public ResultResponse updateByPrimaryKeySelective(SysMenuBean record) {
        ResultResponse resultResponse = new ResultResponse();
        if (record != null) {
            int result = sysMenuWriterMapper.updateByPrimaryKeySelective(record);
            if (result > 0) {
                resultResponse.setSuccess(true);
                //更新按钮
                saveBtns(record.getId(), record.getMenuBtnBeanList());
            }
        } else {
            resultResponse.setMessage("系统菜单更新失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据用户id查询父菜单菜单
     *
     * @param userId
     * @return
     */
    public ResultResponse getRootMenuByUser(Long userId) {
        ResultResponse resultResponse = new ResultResponse();
        if (userId != null) {
            List<SysMenuBean> list = sysMenuReaderMapper.getRootMenuByUser(userId);
            resultResponse.setData(list);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("根据用户id查询父菜单失败，参数不能为空");
        }
        return resultResponse;
    }


    /**
     * 根据用户id查询子菜单菜单
     *
     * @param parentId
     * @param userId
     * @return
     */
    public ResultResponse getChildMenuByUser(Long parentId, Long userId) {
        ResultResponse resultResponse = new ResultResponse();
        if (userId != null) {
            List<SysMenuBean> list = sysMenuReaderMapper.getChildMenuByUser(parentId, userId);
            resultResponse.setData(list);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("根据用户id查询子菜单失败，参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 获取顶级菜单
     *
     * @return
     */
    public List<SysMenuBean> getRootMenu() {
        return sysMenuReaderMapper.getRootMenu();
    }

    /**
     * 获取子菜单
     *
     * @return
     */
    public List<SysMenuBean> getChildMenu() {
        return sysMenuReaderMapper.getChildMenu();
    }


    /**
     * 根据id查询所有的子按钮
     *
     * @param parentId
     * @return
     */
    public List<SysMenuBean> selectChildMenuByPk(Long parentId) {
        return sysMenuReaderMapper.selectChildMenuByPk(parentId);
    }


    /**
     * 根据父id删除所有子菜单
     *
     * @param parentId
     * @return
     */
    public ResultResponse deleteChildMenuByParentId(Long parentId) {
        ResultResponse resultResponse = new ResultResponse();
        if (parentId != null) {
            sysMenuReaderMapper.deleteChildMenuByParentId(parentId);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }


    /**
     * 根据用户id查询所属菜单
     *
     * @param userId
     * @return
     */
    public ResultResponse selectMenuByUserId(Long userId) {
        ResultResponse resultResponse = new ResultResponse();
        List<SysMenuDto> list = sysMenuReaderMapper.selectMenuByUserId(userId);
        if (!list.isEmpty()) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                SysMenuDto sysMenuDto = list.get(i);
                List<SysMenuBean> menuBeanList = sysMenuReaderMapper.getChildMenuByUser(sysMenuDto.getId(), userId);
                sysMenuDto.setChildMenuList(menuBeanList);
                list.set(i, sysMenuDto);
            }
        }
        resultResponse.setData(list);
        resultResponse.setSuccess(true);
        return resultResponse;
    }


    /**
     * 根据角色Id获取所有菜单
     *
     * @param roleId
     * @return
     */
    public List<SysMenuBean> selectMenuByRoleId(Long roleId) {
        return null;
    }

}
