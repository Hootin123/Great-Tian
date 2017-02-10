package com.xtr.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysRoleBean;
import com.xtr.api.service.sys.SysRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>角色信息单元测试</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 9:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationcontext.xml")
public class SysRoleTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleTest.class);

    @Resource
    private SysRoleService sysRoleService;

    @Test
    public void testSysRole() {
        //新增
        insertSysRole();
        //修改
//        updateSysRole();
        //删除
//        deleteSysRole();
    }

    /**
     * 新增角色
     */
    public void insertSysRole() {
        LOGGER.info("新增角色开始···");
        SysRoleBean sysRoleBean = new SysRoleBean();
        sysRoleBean.setRoleName("测试角色名称6");
        sysRoleBean.setState(new Integer(0));
        sysRoleBean.setDescr("测试角色名称6描述");
        sysRoleBean.setCreateTime(new Date());
        sysRoleBean.setCreateUser(14l);
        ResultResponse resultResponse = sysRoleService.addSysRole(sysRoleBean,new Long[]{3l},new Long[]{1l,2l,3l});
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("新增角色成功···");
    }

    /**
     * 修改角色
     */
    public void updateSysRole() {
        LOGGER.info("修改角色开始···");
        SysRoleBean sysRoleBean = new SysRoleBean();
        sysRoleBean.setState(new Integer(1));
        sysRoleBean.setId(1l);
        ResultResponse resultResponse = sysRoleService.updateSysRole(sysRoleBean,new Long[]{3l},new Long[]{1l,2l,3l});
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("修改角色成功···");
    }

    /**
     * 删除角色
     */
    public void deleteSysRole() {
        LOGGER.info("修改角色开始···");
        ResultResponse resultResponse = sysRoleService.deleteByPrimaryKey(1l);
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("修改角色成功···");
    }
}
