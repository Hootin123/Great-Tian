package com.xtr.manager.util;


import com.xtr.api.domain.sys.SysUserBean;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * <p>Session 工具类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 16:13
 */
public final class SessionUtils {

    protected static final Logger logger = Logger.getLogger(SessionUtils.class);

    private static final String SESSION_USER = "session_user";

    private static final String SESSION_VALIDATECODE = "session_validatecode";//验证码


    private static final String SESSION_ACCESS_URLS = "session_access_urls"; //系统能够访问的URL


    private static final String SESSION_MENUBTN_MAP = "session_menubtn_map"; //系统菜单按钮


    /**
     * 设置session的值
     *
     * @param request
     * @param key
     * @param value
     */
    public static void setAttr(HttpServletRequest request, String key, Object value) {
        request.getSession(true).setAttribute(key, value);
    }


    /**
     * 获取session的值
     *
     * @param request
     * @param key
     */
    public static <T> T getAttr(HttpServletRequest request, String key) {
        Object obj = request.getSession(true).getAttribute(key);
        if (null != obj) {
            return (T) obj;
        }
        return null;
    }

    /**
     * 删除Session值
     *
     * @param request
     * @param key
     */
    public static void removeAttr(HttpServletRequest request, String key) {
        request.getSession(true).removeAttribute(key);
    }

    /**
     * 设置用户信息 到session
     *
     * @param request
     * @param user
     */
    public static void setUser(HttpServletRequest request, SysUserBean user) {
        request.getSession(true).setAttribute(SESSION_USER, user);
    }


    /**
     * 从session中获取用户信息
     *
     * @param request
     * @return SysUser
     */
    public static SysUserBean getUser(HttpServletRequest request) {
        return getAttr(request, SESSION_USER);
    }


    /**
     * 从session中获取用户信息
     *
     * @param request
     * @return SysUser
     */
    public static void removeUser(HttpServletRequest request) {
        removeAttr(request, SESSION_USER);
    }


    /**
     * 设置验证码 到session
     *
     * @param request
     * @param validateCode
     */
    public static void setValidateCode(HttpServletRequest request, String validateCode) {
        request.getSession(true).setAttribute(SESSION_VALIDATECODE, validateCode);
    }


    /**
     * 从session中获取验证码
     *
     * @param request
     * @return SysUser
     */
    public static String getValidateCode(HttpServletRequest request) {
        return getAttr(request, SESSION_VALIDATECODE);
    }


    /**
     * 从session中获删除验证码
     *
     * @param request
     * @return SysUser
     */
    public static void removeValidateCode(HttpServletRequest request) {
        removeAttr(request, SESSION_VALIDATECODE);
    }

    /**
     * 判断当前登录用户是否超级管理员
     *
     * @param request
     * @return
     */
//    public static boolean isAdmin(HttpServletRequest request) { //判断登录用户是否超级管理员
//        CompanyMembersBean user = getUser(request);
//        if (user == null || user.getSuperAdmin() != Constant.SuperAdmin.YES.key) {
//            return false;
//        }
//        return true;
//    }


    /**
     * 判断当前登录用户是否超级管理员
     *
     * @param request
     * @return
     */
    public static void setAccessUrl(HttpServletRequest request, List<String> accessUrls) { //判断登录用户是否超级管理员
        setAttr(request, SESSION_ACCESS_URLS, accessUrls);
    }


    /**
     * 判断URL是否可访问
     *
     * @param request
     * @return
     */
    public static boolean isAccessUrl(HttpServletRequest request, String url) {
        List<String> accessUrls = getAttr(request, SESSION_ACCESS_URLS);
        return !(accessUrls == null || accessUrls.isEmpty() || !accessUrls.contains(url));
    }


    /**
     * 设置菜单按钮
     *
     * @param request
     * @param btnMap
     */
    public static void setMemuBtnMap(HttpServletRequest request, Map<String, List> btnMap) { //判断登录用户是否超级管理员
        setAttr(request, SESSION_MENUBTN_MAP, btnMap);
    }

    /**
     * 获取菜单按钮
     *
     * @param request
     * @param menuUri
     */
    public static List<String> getMemuBtnListVal(HttpServletRequest request, String menuUri) { //判断登录用户是否超级管理员
        Map btnMap = getAttr(request, SESSION_MENUBTN_MAP);
        if (btnMap == null || btnMap.isEmpty()) {
            return null;
        }
        return (List<String>) btnMap.get(menuUri);
    }

}