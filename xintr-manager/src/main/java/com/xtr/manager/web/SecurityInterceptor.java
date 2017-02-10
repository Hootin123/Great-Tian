package com.xtr.manager.web;

import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 判断用户权限，未登录用户跳转到登录页面
 *
 * @author zhangfeng
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);
    // 需要安全验证的 URL
//    private List<String> includedUrls;
    // 不需要安全过滤的 URL
    private List<String> excludedUrls;


    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        StringBuffer requestUrl = request.getRequestURL();

//		logger.info("requestUri = " + requestUri + "; param = " + JSON.toJSONString(request.getParameterMap()));


        if (excludedUrls != null) {
            for (String url : excludedUrls) {
                // 请求地址匹配到 excludedUrls里面地址，可以通过拦截器，不需要登录
                if (requestUrl.toString().matches(url)) {
                    return true;
                }
            }
        }
        SysUserBean sysUserBean = SessionUtils.getUser(request);
        if (sysUserBean != null)
            return true;
        else {
            /**非法登录，跳转到登录页面*/
            response.sendRedirect(request.getContextPath() + "/login.htm");
            return false;
        }
//        return super.preHandle(request, response, handler);
    }


    public void setExcludedUrls(List<String> excludedUrls) {
        this.excludedUrls = excludedUrls;
    }

}
