package com.xtr.company.controller;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.service.sys.SysUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/6/12.
 */
@Controller
public class TestController {

    @Resource
    private SysUserService sysUserService;


    /**
     * 首页面
     *
     * @param model
     * @return
     */
    @RequestMapping("/index.htm")
    public ModelAndView index(ModelAndView model) {
//        ResultResponse resultResponse = sysUserService.selectByPrimaryKey(11l);
        model.setViewName("xtr/home");
        return model;
    }
}
