package com.xtr.manager.controller.sys;

import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.sys.SysUserService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.util.MethodUtil;
import com.xtr.manager.util.SessionUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * <p>登录</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/15 10:51
 */
@Controller
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private SysUserService sysUserService;

    /**
     * 登录页面
     *
     * @param mav
     * @return
     */
    @RequestMapping({"", "/", "login.htm"})
    public ModelAndView toIndex(HttpServletRequest request, ModelAndView mav) {
        mav.setViewName("xtr/sys/login");
        return mav;
    }

    /**
     * 退出登录
     *
     * @param request
     * @param mav
     * @return
     */
    @RequestMapping("loginOut.htm")
    @SystemControllerLog(operation = "退出登录", modelName = "操作员管理")
    public ModelAndView loginOut(HttpServletRequest request, ModelAndView mav) {
        mav.setViewName("xtr/sys/login");
        //清除session
        SessionUtils.removeUser(request);
        return mav;
    }

    /**
     * 登录
     *
     * @param request
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("toLogin.htm")
    @ResponseBody
    public ResultResponse toLogin(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (StringUtils.isBlank(username)) {
                resultResponse.setMessage("用户名不能为空");
                return resultResponse;
            } else if (StringUtils.isBlank(password)) {
                resultResponse.setMessage("密码不能为空");
                return resultResponse;
            } else {
                String msg = "用户登录日志:";
                SysUserBean sysUserBean = sysUserService.selectByEmailOrPhone(username, null);
                if (sysUserBean == null) {
                    resultResponse.setMessage(msg + "[" + username + "] 该用户不存在");
                    return resultResponse;
                } else if (MethodUtil.ecompareMD5(password + sysUserBean.getSalt(), sysUserBean.getPwd())) {
                    sysUserBean.setLoginCount(Integer.valueOf(sysUserBean.getLoginCount().intValue() + 1));
                    sysUserBean.setLoginTime(new Date());
                    this.sysUserService.updateByPrimaryKeySelective(sysUserBean);
                    SessionUtils.setUser(request, sysUserBean);
                    LOGGER.debug(msg + "[" + username + "] 登录成功");
                    resultResponse.setSuccess(true);
                } else {
                    LOGGER.debug(msg + "[" + username + "] 密码输入错误");
                    resultResponse.setMessage("您输入的密码有误");
                }
            }
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

    @RequestMapping("isUserExist.htm")
    public void isUserExist(HttpServletResponse response, @RequestParam("username") String username) throws IOException {
        if (StringUtils.isNotBlank(username)) {
            response.getWriter().print(true);
        }
    }
}
