package com.xtr.manager.controller.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.sys.SysMenuService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.manager.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/15 11:03
 */
@Controller
public class HomeController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 主页面
     *
     * @param mav
     * @param request
     * @return
     */
    @RequestMapping("home.htm")
    @SystemControllerLog(operation = "用户登录", modelName = "系统管理")
    public ModelAndView home(ModelAndView mav, HttpServletRequest request) {
        mav.setViewName("xtr/sys/home");
        //查询用户的菜单
        SysUserBean sysUserBean = SessionUtils.getUser(request);
        //如果是超级管理员，则用户userId为空
        Long userId = sysUserBean.getSuperAdmin().intValue() == 1 ? null : sysUserBean.getId();
        ResultResponse resultResponse = sysMenuService.selectMenuByUserId(userId);
        mav.addObject("menuList", resultResponse.getData());
        mav.addObject("nickName", sysUserBean.getNickName());
        return mav;
    }

    @RequestMapping("index.htm")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("xtr/sys/index");
        return mav;
    }

    @RequestMapping("error404.htm")
    public ModelAndView error404(ModelAndView mav) {
        mav.setViewName("comm/404");
        return mav;
    }

    @RequestMapping("error505.htm")
    public ModelAndView error505(ModelAndView mav) {
        mav.setViewName("comm/505");
        return mav;
    }


}
