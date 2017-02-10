package com.xtr.company.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/6/12.
 */
@Controller
public class TestController {



    /**
     * 首页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/index.htm")
    public ModelAndView index(ModelAndView model, HttpServletRequest request) {
        model.setViewName("xtr/home");
        return model;
    }
}
