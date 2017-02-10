package com.xtr;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.service.sys.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/motan_client.xml")
public class Client {

    @Resource
    private SysUserService sysUserService;

    @Test
    public void test() {
//        ResultResponse resultResponse = sysUserService.selectByPrimaryKey(10l);
//        System.out.println(JSON.toJSONString(resultResponse));
    }

}  