

package com.xtr.manager.controller;


import com.xtr.api.basic.ResultResponse;
import com.xtr.comm.custom.MyStringTrimmerEditor;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.MsgPropertyUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public BaseController() {
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = DateUtil.dateTimeFormatter;
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
        binder.registerCustomEditor(String.class, new MyStringTrimmerEditor(false));
    }

    protected BindException validateRequestBean(Validator validator, Object entity) throws Exception {
        Class target = entity.getClass();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(entity, StringUtils.firstCharToLow(target.getSimpleName()));
        BindException errors = new BindException(binder.getBindingResult());
        validator.validate(entity, errors);
        return errors;
    }

    protected PrintWriter initPrintWriter(String fileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;  filename=" + fileName);
        PrintWriter out = response.getWriter();
        return out;
    }

    protected void writeData(PrintWriter writer, String data, boolean isEnd) throws Exception {
        writer.write(data);
        writer.flush();
        if (isEnd) {
            writer.close();
        }

    }

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

    public String getParaToString(String para) {
        String result = "";
        if (para != null && !"".equals(para)) {
            try {
                byte[] temp = para.getBytes("iso-8859-1");
                result = new String(temp, "utf-8");
            } catch (Exception var5) {
                logger.info(var5.getMessage(), var5);
            }

            return result;
        } else {
            return result;
        }
    }

    protected int getLoginUserId(HttpServletRequest request) {
        byte loginUserId = -1;
        return loginUserId;
    }

    public ResultResponse wrapServiceResult(ResultResponse serviceResult, MsgPropertyUtil msgUtil) {
        if (serviceResult != null && msgUtil != null) {
            ResultResponse resultResponse = new ResultResponse();
            resultResponse.setComment(serviceResult.getComment());
            resultResponse.setData(serviceResult.getData());
            if (!StringUtils.isStrNull(serviceResult.getMsgCode())) {
                resultResponse.setMessage(msgUtil.getMsg(serviceResult.getMsgCode(), new Object[0]));
            }

            resultResponse.setSuccess(serviceResult.isSuccess());
            return resultResponse;
        } else {
            return null;
        }
    }

    public void dataToCookie(HttpServletResponse response, String key, String value) {
        try {
            StringBuffer e = new StringBuffer();
            e.append(key);
            e.append("=" + value + ";");
            String domain = PropertyUtils.getString("HXSHOP_DOMAIN");
            if (domain != null) {
                e.append("Domain=" + domain + ";");
            }

            e.append("Path=/;HTTPOnly");
            response.setHeader("P3P", "CP=\'CP=CAO PSA OUR\'");
            response.setHeader("Set-Cookie", e.toString());
        } catch (Exception var6) {
            logger.error(var6.getMessage(), var6);
        }

    }

    public void deleteCartCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie[] arr$ = cookies;
            int len$ = cookies.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Cookie cookie = arr$[i$];
                if (cookie.getName().startsWith("PG")) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }

    }

    public Map<String, Integer> getPGCookie(HttpServletRequest request) {
        HashMap map = new HashMap();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie[] arr$ = cookies;
            int len$ = cookies.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                Cookie cookie = arr$[i$];
                if (cookie.getName().startsWith("PG")) {
                    Integer nowCount = Integer.valueOf(cookie.getValue());
                    map.put(cookie.getName(), nowCount);
                }
            }
        }

        return map;
    }

    protected String getJsonpCallBack(HttpServletRequest request) {
        return request.getParameter("jsonpCallBack");
    }

    public int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String value = request.getParameter(paramName);
        return org.apache.commons.lang.StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }
}
