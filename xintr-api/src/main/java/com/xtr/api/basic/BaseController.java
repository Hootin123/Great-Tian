package com.xtr.api.basic;


import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.custom.MyStringTrimmerEditor;
import com.xtr.comm.editor.DateEditor;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
//        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new DateEditor(
                DateUtil.dateTimeFormatter, false));
//        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(
//                Integer.class, true));
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(
                Long.class, true));
        binder.registerCustomEditor(String.class, new MyStringTrimmerEditor(true));
    }

    /**
     * 校验绑定对象
     */
    protected BindException validateRequestBean(Validator validator, Object entity)
            throws Exception {
        Class<? extends Object> target = entity.getClass();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(entity, StringUtils.firstCharToLow(target.getSimpleName()));
        BindException errors = new BindException(binder.getBindingResult());
        validator.validate(entity, errors);
        return errors;
    }

    /**
     * 初始化输出
     *
     * @param fileName
     * @param response
     * @return
     * @throws Exception
     */
    protected PrintWriter initPrintWriter(String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;  filename=" + fileName);
        PrintWriter out = response.getWriter();//放在第一句是会出现乱码
        return out;
    }

    /**
     * 输出数据
     *
     * @param writer
     * @param data
     * @param isEnd
     * @throws Exception
     */
    protected void writeData(PrintWriter writer, String data, boolean isEnd) throws Exception {
        writer.write(data);
        writer.flush();
        if (isEnd) {
            writer.close();
        }
    }

    /**
     * 根据request取得IP
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        String[] ips = ip.split(",");

        if (ips.length > 0) {
            ip = ips[0];
        }

        return ip;
    }

    /**
     * 将url中文参数转换为utf-8格式
     *
     * @param para
     * @return string
     */
    public String getParaToString(String para) {
        // 将url参数转换为utf-8格式
        String result = "";
        if (para == null || "".equals(para)) {
            return result;
        }
        byte[] temp;
        try {
            temp = para.getBytes("iso-8859-1");
            result = new String(temp, "utf-8");
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取登录用户id
     *
     * @param request
     * @return
     */
    protected Long getLoginUserId(HttpServletRequest request) {
        Long loginUserId = -1l;
        if (request.getSession() != null) {
            CompanyMembersBean obj = (CompanyMembersBean)request.getSession().getAttribute(CommonConstants.LOGIN_USER_KEY);
            if (obj != null) {
                loginUserId = Long.valueOf(obj.getMemberId());
            }
        }
        return loginUserId;
    }

    /**
     * 获取登录用户所属企业id
     *
     * @param request
     * @return
     */
    protected Long getLoginCompanyId(HttpServletRequest request) {
        Long loginCompanyId = -1l;
        if (request.getSession() != null) {
            CompanysBean obj = (CompanysBean)request.getSession().getAttribute(CommonConstants.LOGIN_COMPANY_KEY);
            if (obj != null) {
                loginCompanyId = Long.valueOf(obj.getCompanyId());
            }
        }
        return loginCompanyId;
    }


    /**
     * 获取登录用户对象
     *
     * @param request
     * @return
     */
    protected CompanyMembersBean getLoginUserObj(HttpServletRequest request) {
        CompanyMembersBean loginUserObj = null;
        if (request.getSession() != null) {
            CompanyMembersBean obj = (CompanyMembersBean)request.getSession().getAttribute(CommonConstants.LOGIN_USER_KEY);
            if (obj != null) {
                loginUserObj = obj;
            }
        }
        return loginUserObj;
    }

    /**
     * 获取登录用户所属公司对象
     *
     * @param request
     * @return
     */
    public CompanysBean getLoginCompanyObj(HttpServletRequest request) {
        CompanysBean loginCompanyObj = null;
        if (request.getSession() != null) {
            CompanysBean obj = (CompanysBean)request.getSession().getAttribute(CommonConstants.LOGIN_COMPANY_KEY);
            if (obj != null) {
                loginCompanyObj = obj;
            }
        }
        return loginCompanyObj;
    }


    /**
     * 更新数据放入cookie中
     *
     * @param response
     * @param key      cookie名字
     * @param value    cookie值
     * @Title: dataToCookie
     * @author zhangfeng
     */
    public void dataToCookie(HttpServletResponse response, String key, String value) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(key);
            sb.append("=" + value + ";");
            String domain = PropertyUtils.getString("HXSHOP_DOMAIN");
            if (domain != null) {
                sb.append("Domain=" + domain + ";");
            }
            sb.append("Path=/;HTTPOnly");
            response.setHeader("P3P", "CP='CP=CAO PSA OUR'");
            response.setHeader("Set-Cookie", sb.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 登录成功，删除本地购物车商品
     */
    public void deleteCartCookies(HttpServletRequest request,
                                  HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("PG")) {
                    //清除本地购物车
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    //cookie.setDomain(PropertyUtils.getString("HXSHOP_DOMAIN"));
                    response.addCookie(cookie);
                }
            }
        }
    }

    //遍历cookies将本地购物车的数据加入Map
    public Map<String, Integer> getPGCookie(HttpServletRequest request) {

        Map<String, Integer> map = new HashMap<String, Integer>();

        //获取cookies
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().startsWith("PG")) {
                    Integer nowCount = Integer.valueOf(cookie.getValue());
                    map.put(cookie.getName(), nowCount);
                }
            }
        }
        return map;
    }

    /**
     * 获取请求中的jsonp返回函数名称
     *
     * @param request
     * @return
     * @Title: getJsonpCallBack
     * @author zhangfeng
     */
    protected String getJsonpCallBack(HttpServletRequest request) {
        return request.getParameter("jsonpCallBack");
    }


    public int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String value = request.getParameter(paramName);
        return org.apache.commons.lang.StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }
}
