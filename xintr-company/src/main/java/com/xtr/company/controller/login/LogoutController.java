package com.xtr.company.controller.login;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.constant.CommonConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by xuewu on 2016/7/26.
 */
@Controller
public class LogoutController {

    @RequestMapping(value = "logout.htm")
    @SystemControllerLog(operation = "安全退出", modelName = "用户退出")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null){
            session.removeAttribute("memberLogname");
            session.removeAttribute("userName");
            session.removeAttribute("memberid");
            session.removeAttribute(CommonConstants.LOGIN_USER_KEY);
            session.removeAttribute(CommonConstants.SESSION_USER);
            session.removeAttribute(CommonConstants.LOGIN_COMPANY_KEY);
        }
        return new ModelAndView("redirect:" + request.getContextPath());
    }

}
