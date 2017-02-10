package com.xtr.manager.controller.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBean;
import com.xtr.api.domain.sys.SysMenuBtnBean;
import com.xtr.api.service.sys.SysMenuBtnService;
import com.xtr.api.service.sys.SysMenuService;
import com.xtr.comm.annotation.SystemControllerLog;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/15 14:22
 */
@Controller
@RequestMapping("sysMenu")
public class SysMenuController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuBtnService sysMenuBtnService;

    /**
     * 菜单管理页面
     *
     * @param mav
     * @param parentId
     * @return
     */
    @RequestMapping("menu.htm")
    public ModelAndView menu(ModelAndView mav, String parentId) {
        List<SysMenuBean> list = null;
        if (!StringUtils.isBlank(parentId) && !StringUtils.equals(parentId, "undefined")) {
            list = sysMenuService.selectChildMenuByPk(Long.valueOf(parentId));
            mav.addObject("isChild", Integer.valueOf(1));
            mav.addObject("parentId", parentId);
        } else {
            list = sysMenuService.getRootMenu();
            mav.addObject("isChild", Integer.valueOf(0));
        }

        mav.addObject("listMenu", list);
        mav.setViewName("xtr/sys/menu/menu");
        return mav;
    }

    /**
     * 新增菜单页面
     *
     * @param mav
     * @param menuId
     * @param parentId
     * @return
     */
    @RequestMapping({"addMenuPage.htm"})
    public ModelAndView addMenuPage(ModelAndView mav, String menuId, String parentId) {
        Long la = Long.getLong(menuId);
        if (StringUtils.isNotBlank(menuId) && !StringUtils.equals(menuId, "undefined")) {
            mav.addObject("menu", sysMenuService.selectByPrimaryKey(Long.valueOf(menuId)));
        }

        mav.addObject("parentId", parentId);
        mav.setViewName("xtr/sys/menu/addMenu");
        return mav;
    }

    /**
     * 新增菜单
     *
     * @param request
     * @param sysMenuBean
     * @return
     */
    @RequestMapping("addMenu.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增/修改菜单", modelName = "菜单管理")
    public ResultResponse addMenu(HttpServletRequest request, SysMenuBean sysMenuBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (sysMenuBean != null) {
            if (sysMenuBean.getId() == null) {
                sysMenuBean.setCreateTime(new Date());
                resultResponse = sysMenuService.saveSysMenu(sysMenuBean);
                LOGGER.info("新增菜单:[" + JSON.toJSONString(sysMenuBean) + "]");
            } else {
                sysMenuBean.setUpdateTime(new Date());
                resultResponse = sysMenuService.updateByPrimaryKeySelective(sysMenuBean);
                LOGGER.info("修改菜单:[" + JSON.toJSONString(sysMenuBean) + "]");
            }
        }
        return resultResponse;
    }

    /**
     * 根据id删除
     *
     * @param menuId
     * @return
     */
    @RequestMapping("deleteMenu.htm")
    @ResponseBody
    @SystemControllerLog(operation = "删除菜单", modelName = "菜单管理")
    public ResultResponse delete(@RequestParam("menuId") Long menuId) {
        ResultResponse resultResponse = new ResultResponse();
        SysMenuBean sysMenuBean = sysMenuService.selectByPrimaryKey(menuId);
        if (sysMenuBean != null) {
            resultResponse = sysMenuService.deleteByPrimaryKey(menuId);
            //如果parentId为空,则删除该菜单下的所有子菜单
            if (sysMenuBean.getParentId() == null) {
                resultResponse = sysMenuService.deleteChildMenuByParentId(menuId);
            }
        }
        return resultResponse;
    }

    /**
     * 系统图标页面
     *
     * @param mav
     * @return
     */
    @RequestMapping({"icon.htm"})
    public ModelAndView fortawesome(ModelAndView mav) {
        mav.setViewName("xtr/sys/menu/icon");
        return mav;
    }


}
