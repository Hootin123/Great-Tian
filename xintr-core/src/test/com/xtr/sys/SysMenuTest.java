package com.xtr.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysMenuBean;
import com.xtr.api.domain.sys.SysMenuBtnBean;
import com.xtr.api.service.sys.SysMenuBtnService;
import com.xtr.api.service.sys.SysMenuService;
import com.xtr.comm.util.URLUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>系统菜单单元测试</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 10:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationcontext.xml")
public class SysMenuTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleTest.class);

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysMenuBtnService sysMenuBtnService;

    @Test
    public void testSysMenu() {
        //新增
//        insertSysMenu();
        //修改
//        updateSysMenu();
        //删除
//        deleteSysMenu();
        //根据用户id查询父菜单菜单
//        getRootMenuByUser();
        //根据用户id查询子菜单菜单
//        getChildMenuByUser();
        //根据用户id查询按钮
//        getMenuBtnByUser();
        //获取顶级菜单
//        getRootMenu();
        //获取子菜单
//        getChildMenu();
        //构建树形数据
        buildData();
    }

    /**
     * 新增系统菜单
     */
    public void insertSysMenu() {
        LOGGER.info("新增系统菜单开始···");
        SysMenuBean sysMenuBean = new SysMenuBean();
        sysMenuBean.setMenuName("测试菜单4");
        sysMenuBean.setUrl("sys/sysMenu.htm");
        sysMenuBean.setActions("dataList.htm");
        sysMenuBean.setIsDelete(new Integer(0));
        sysMenuBean.setSort(new Integer(4));
        sysMenuBean.setCreateTime(new Date());


        List<SysMenuBtnBean> list = new ArrayList<SysMenuBtnBean>();
        SysMenuBtnBean sysMenuBtnBean = new SysMenuBtnBean();
        sysMenuBtnBean.setBtnName("新增");
        sysMenuBtnBean.setMenuId(sysMenuBean.getId());
        sysMenuBtnBean.setBtnType("add");
        sysMenuBtnBean.setActionUrls("add.htm");
        list.add(sysMenuBtnBean);

        sysMenuBtnBean = new SysMenuBtnBean();
        sysMenuBtnBean.setBtnName("修改");
        sysMenuBtnBean.setMenuId(sysMenuBean.getId());
        sysMenuBtnBean.setBtnType("update");
        sysMenuBtnBean.setActionUrls("update.htm");
        list.add(sysMenuBtnBean);

        sysMenuBtnBean = new SysMenuBtnBean();
        sysMenuBtnBean.setBtnName("删除");
//        sysMenuBtnBean.setMenuId(sysMenuBean.getId());
        sysMenuBtnBean.setBtnType("delete");
        sysMenuBtnBean.setActionUrls("delete.htm");
        list.add(sysMenuBtnBean);

        sysMenuBean.setMenuBtnBeanList(list);
        ResultResponse resultResponse = sysMenuService.saveSysMenu(sysMenuBean);
        LOGGER.info(JSON.toJSONString(resultResponse));

        LOGGER.info("新增系统菜单成功···");
    }

    /**
     * 更新系统菜单
     */
    public void updateSysMenu() {
        LOGGER.info("更新系统菜单开始···");
        SysMenuBean sysMenuBean = new SysMenuBean();
        sysMenuBean.setSort(new Integer(3));
        sysMenuBean.setId(3l);


        List<SysMenuBtnBean> list = new ArrayList<SysMenuBtnBean>();
        SysMenuBtnBean sysMenuBtnBean = new SysMenuBtnBean();
//        sysMenuBtnBean.setBtnName("新增");
//        sysMenuBtnBean.setBtnType("add");
//        sysMenuBtnBean.setActionUrls("add.htm");
//        list.add(sysMenuBtnBean);
//
//        sysMenuBtnBean = new SysMenuBtnBean();
//        sysMenuBtnBean.setBtnName("修改");
//        sysMenuBtnBean.setBtnType("update");
//        sysMenuBtnBean.setActionUrls("update.htm");
//        list.add(sysMenuBtnBean);

//        sysMenuBtnBean = new SysMenuBtnBean();
        sysMenuBtnBean.setId(2l);
        sysMenuBtnBean.setBtnName("删除");
//        sysMenuBtnBean.setMenuId(sysMenuBean.getId());
        sysMenuBtnBean.setBtnType("delete");
        sysMenuBtnBean.setActionUrls("delete.htm");
        list.add(sysMenuBtnBean);

        sysMenuBean.setMenuBtnBeanList(list);
        ResultResponse resultResponse = sysMenuService.updateByPrimaryKeySelective(sysMenuBean);
        LOGGER.info(JSON.toJSONString(resultResponse));

        LOGGER.info("更新系统菜单成功···");
    }

    /**
     * 删除系统菜单
     */
    public void deleteSysMenu() {
        LOGGER.info("删除系统菜单开始···");
        ResultResponse resultResponse = sysMenuService.deleteByPrimaryKey(5l);
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("删除系统菜单成功···");
    }

    /**
     * 根据用户id查询父菜单菜单
     */
    public void getRootMenuByUser() {
        LOGGER.info("根据用户id查询父菜单菜单开始···");
        ResultResponse resultResponse = sysMenuService.getRootMenuByUser(9l);
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("根据用户id查询父菜单菜单成功···");
    }

    /**
     * 根据用户id查询子菜单菜单
     */
    public void getChildMenuByUser() {
        LOGGER.info("根据用户id查询子菜单菜单开始···");
        ResultResponse resultResponse = sysMenuService.getChildMenuByUser(1l, 9l);
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("根据用户id查询子菜单菜单成功···");
    }

    /**
     * 根据用户id查询按钮
     */
    public void getMenuBtnByUser() {
        LOGGER.info("根据用户id查询按钮开始···");
        ResultResponse resultResponse = sysMenuBtnService.getMenuBtnByUser(9l);
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("根据用户id查询按钮成功···");
    }

    /**
     * 获取顶级菜单
     */
    public void getRootMenu() {
        LOGGER.info("获取顶级菜单开始···");
        List<SysMenuBean> list = sysMenuService.getRootMenu();
        LOGGER.info(JSON.toJSONString(list));
        LOGGER.info("获取顶级菜单成功···");
    }

    /**
     * 获取子菜单
     */
    public void getChildMenu() {
        LOGGER.info("获取子菜单开始···");
        List<SysMenuBean> list = sysMenuService.getChildMenu();
        LOGGER.info(JSON.toJSONString(list));
        LOGGER.info("获取子菜单成功···");
    }

    /**
     * 构建树形数据
     *
     * @return
     */
    public void buildData() {
        List<SysMenuBean> rootMenus = (List<SysMenuBean>) sysMenuService.getRootMenuByUser(9l).getData();//根节点
        List<SysMenuBean> childMenus = (List<SysMenuBean>) sysMenuService.getChildMenuByUser(1l,9l).getData();//子节点
        List<SysMenuBtnBean> childBtns = (List<SysMenuBtnBean>) sysMenuBtnService.getMenuBtnByUser(9l).getData();//按钮操作
        //能够访问的url列表
        List<String> accessUrls = new ArrayList<String>();
        //菜单对应的按钮
        Map<String, List> menuBtnMap = new HashMap<String, List>();
        for (SysMenuBean menu : childMenus) {
            //判断URL是否为空
            if (StringUtils.isNotBlank(menu.getUrl())) {
                List<String> btnTypes = new ArrayList<String>();
                for (SysMenuBtnBean btn : childBtns) {
                    if (menu.getId().equals(btn.getMenuId())) {
                        btnTypes.add(btn.getBtnType());
                        URLUtils.getBtnAccessUrls(menu.getUrl(), btn.getActionUrls(), accessUrls);
                    }
                }
                menuBtnMap.put(menu.getUrl(), btnTypes);
                URLUtils.getBtnAccessUrls(menu.getUrl(), menu.getActions(), accessUrls);
                accessUrls.add(menu.getUrl());
            }
        }
        LOGGER.info(JSON.toJSONString(accessUrls));
    }
}
