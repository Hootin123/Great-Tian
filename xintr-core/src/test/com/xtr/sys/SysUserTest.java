package com.xtr.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.sys.SysUserService;
import com.xtr.comm.util.MethodUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>用户信息单元测试</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/13 16:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationcontext.xml")
//@Transactional
public class SysUserTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserTest.class);

    @Resource
    private SysUserService sysUserService;

    @Test
    public void testSysUser() throws Exception {
        //新增用户
        insertSysUser();
        //更新用户
//        updateSysUser();
        //删除用户
//        deleteSysUser();
        //给用户授权
//        addUserRole();
    }

    /**
     * 新增用户
     */
    public void insertSysUser() throws Exception {
        LOGGER.info("插入用户信息开始···");
        SysUserBean sysUserBean = new SysUserBean();
        sysUserBean.setMobilePhone("15857134834");
        sysUserBean.setNickName("测试用户5");
        sysUserBean.setPwd(MethodUtil.MD5("132456"));
        sysUserBean.setState(0);
        sysUserBean.setIsDelete(0);
        sysUserBean.setCreateUser(123l);
        sysUserBean.setCreateTime(new Date());
         sysUserService.addSysUser(sysUserBean);
//        LOGGER.info(JSON.toJSONString(result));
        LOGGER.info("插入用户信息成功···");
    }

    /**
     * 更新用户
     */
    public void updateSysUser() {
        LOGGER.info("更新用户信息开始···");
        SysUserBean sysUserBean = new SysUserBean();
        sysUserBean.setId(11l);
        sysUserBean.setNickName("测试用户3");
  sysUserService.updateByPrimaryKeySelective(sysUserBean);
//        LOGGER.info(JSON.toJSONString(result));
        LOGGER.info("更新用户信息成功···");
    }

    /**
     * 删除用户
     */
    public void deleteSysUser() {
        LOGGER.info("删除用户信息开始···");
        ResultResponse resultResponse = sysUserService.deleteByPrimaryKey(8l);
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("删除用户信息成功···");
    }

    /**
     * 给指定的用户授权
     */
    public void addUserRole() {
        LOGGER.info("用户授权开始···");
        ResultResponse resultResponse = sysUserService.addUserRole(9l, new Long[]{7l});
        LOGGER.info(JSON.toJSONString(resultResponse));
        LOGGER.info("用户授权成功···");
    }
}
