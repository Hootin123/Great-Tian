package com.xtr.sys;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.service.sys.SysMenuService;
import com.xtr.api.service.sys.SysUserService;
import com.xtr.comm.basic.RandomNumber;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/motan_client.xml")
public class Client {
//
    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Test
    public void test() {

//        resultResponse = sysMenuService.getRootMenuByUser(9l);
//        System.out.println(JSON.toJSONString(resultResponse));
    }

}  