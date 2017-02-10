package com.xtr.manager.controller.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.TreeNode;
import com.xtr.api.domain.sys.*;
import com.xtr.api.service.sys.SysMenuBtnService;
import com.xtr.api.service.sys.SysMenuService;
import com.xtr.api.service.sys.SysRoleRelService;
import com.xtr.api.service.sys.SysRoleService;
import com.xtr.api.util.TreeUtil;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>角色管理</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/19 20:54
 */
@Controller
@RequestMapping("sysRole")
public class SysRoleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuBtnService sysMenuBtnService;

    @Resource
    private SysRoleRelService sysRoleRelService;


    /**
     * 角色管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("role.htm")
    public ModelAndView role(ModelAndView mav) {
        mav.setViewName("xtr/sys/role/role");
        return mav;
    }


    /**
     * 新增/修改用户页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("add.htm")
    public ModelAndView add(ModelAndView mav, Long id) {
        if (id != null) {
            mav.addObject("role", sysRoleService.selectByPrimaryKey(id));
        }
        mav.setViewName("xtr/sys/role/add");
        return mav;
    }

    /**
     * 新增用户
     *
     * @param sysRoleBean
     */
    @RequestMapping("save.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增/修改角色", modelName = "角色管理")
    public ResultResponse save(HttpServletRequest request, SysRoleBean sysRoleBean, Long[] menuIds, Long[] btnIds) {
        ResultResponse resultResponse = new ResultResponse();
        if (sysRoleBean != null) {
            try {
                if (sysRoleBean.getId() != null) {
                    sysRoleBean.setUpdateTime(new Date());
//                    result = sysRoleService.updateByPrimaryKeySelective(sysRoleBean);
                    resultResponse = sysRoleService.updateSysRole(sysRoleBean, menuIds, btnIds);
                } else {
                    sysRoleBean.setCreateTime(new Date());
                    sysRoleBean.setCreateUser(SessionUtils.getUser(request).getId());
//                    result = sysRoleService.insert(sysRoleBean);
                    resultResponse = sysRoleService.addSysRole(sysRoleBean, menuIds, btnIds);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                resultResponse.setMessage(e.getMessage());
            }
        }
        return resultResponse;
    }

    /**
     * 数据列表
     *
     * @param sysRoleBean
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(SysRoleBean sysRoleBean) {
        return sysRoleService.selectPageList(sysRoleBean);
    }

    /**
     * 删除角色
     *
     * @return
     */
    @RequestMapping("delete.htm")
    @ResponseBody
    @SystemControllerLog(operation = "删除角色", modelName = "角色管理")
    public ResultResponse delete(Long id) {
        return sysRoleService.deleteByPrimaryKey(id);
    }

    /**
     * 获取属性菜单
     *
     * @param response
     */
    @RequestMapping("getMenuTree.htm")
    @ResponseBody
    public void getMenuTree(HttpServletResponse response, HttpServletRequest request, Long roleId) {
        List<TreeNode> menuTree = treeMenu(request, roleId);
        HtmlUtil.writerJson(response, JSON.toJSONString(menuTree));
    }

    /**
     * 构建树形菜单
     *
     * @return
     */
    private List<TreeNode> treeMenu(HttpServletRequest request, Long roleId) {
        SysUserBean sysUserBean = SessionUtils.getUser(request);
        //获取所有根节点
        List<SysMenuBean> rootMenus = sysMenuService.getRootMenu();
        //获取所有子节点
        List<SysMenuBean> childMenus = sysMenuService.getChildMenu();
        //获取所有按钮
        List<SysMenuBtnBean> childBtns = sysMenuBtnService.queryByAll();
        TreeUtil util = null;
        if (roleId != null) {
            //获取角色对应的菜单
            List<SysRoleRelBean> listMenu = sysRoleRelService.queryByRoleId(roleId, SysRoleRelBean.RelType.MENU.key);
            //获取角色对应的按钮
            List<SysRoleRelBean> listBtn = sysRoleRelService.queryByRoleId(roleId, SysRoleRelBean.RelType.MENU.key);
            util = new TreeUtil(rootMenus, childMenus, childBtns, getMenuMap(listMenu), getMenuMap(listBtn));
        } else {
            util = new TreeUtil(rootMenus, childMenus, childBtns);
        }
        return util.getTreeNode();
    }

    /**
     * 组装菜单map
     *
     * @param list
     * @return
     */
    public Map<Long, Long> getMenuMap(List<SysRoleRelBean> list) {
        Map<Long, Long> menuMap = new HashMap();
        if (!list.isEmpty()) {
            for (SysRoleRelBean sysRoleRelBean : list) {
                menuMap.put(sysRoleRelBean.getObjId(), sysRoleRelBean.getObjId());
            }
        }
        return menuMap;
    }


}
