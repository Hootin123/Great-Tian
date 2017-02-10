package com.xtr.company.controller.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class BaseController {
	protected Log logger = LogFactory.getLog(this.getClass()); 
	
	protected void deal(ModelAndView mad){
		mad.addObject("title","this");
	}
	
	/**
	 * 跳转到首页
	 * @param request
	 * @return
	 */
	public ModelAndView redirectIndex(HttpServletRequest request){
		return new ModelAndView(new RedirectView(request.getContextPath()+"/home/main.do"));
	}
	
	/**
	 * 跳转到登陆界面
	 * @param request
	 * @return
	 */
	public ModelAndView redirectLogin(HttpServletRequest request){
		return new ModelAndView(new RedirectView(request.getContextPath()+"/login.do"));
	}
	
	/**
	 * 设置cookie
	 * @param response
	 * @param name  cookie名字
	 * @param value cookie值
	 * @param maxAge cookie生命周期  以秒为单位
	 */
	public void addCookie(HttpServletResponse response,String name,String value,int maxAge){
	    Cookie cookie = new Cookie(name,value);
	    cookie.setPath("/");
	    if(maxAge>0)  cookie.setMaxAge(maxAge);
	    response.addCookie(cookie);
	}
	
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    
	    return cookieMap;
	}
	
	/**
	 * 验证是否登录
	 * @param request
	 * @return
	 */
//	protected Boolean isLogin(HttpServletRequest request){
//		Account account=authAccount(request);
//		if(account==null){
//			return false;
//		}
//		return true;
//	}
	/**
	 * 验证登录，如果能得到Account对象，说明登录成功
	 * @param request
	 * @return
	 */
//	protected Account authAccount(HttpServletRequest request){
//		
//		
//		
//		String token=getCookie(request,SystemConstants.COOKIE_TOCKEN_NAME);
//		if(StringUtils.isEmpty(token)){
//			return null;
//		}
//		Account account=null;
//		try {
//			String accountJson=AES.decrypt(token, AES.KEY);
//			Gson gson=new Gson();
//			account=gson.fromJson(accountJson, Account.class);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		return account;
//	}
	
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	protected String getCookie(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            if(cookie.getName().equals(name)){
	            	return cookie.getValue();
	            }
	        }
	    }
		return null;
	}
	
	protected String createXmlResult(String transactionName,String code,String description,String data){
		String xmlresult = "<PWBResponse>{transactionName}<code>{code}</code><description>{description}</description>{data}</PWBResponse>";
		xmlresult=xmlresult.replace("{transactionName}", transactionName).replace("{code}", code).replace("{description}", description).replace("{data}", data);
		return xmlresult;
	}
	
	protected void resultPrint(HttpServletResponse response,String result){
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			PrintWriter pw=response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 清除登录session
	 * @param request
	 */
	protected void clearLoginSession(HttpServletRequest request){
		request.getSession().setAttribute("memberid", "");
		request.getSession().setAttribute("membername", "");
		request.getSession().setAttribute("companyid", "");
		request.getSession().setAttribute("companyname", "");
		request.getSession().setAttribute("depid", "");
		request.getSession().setAttribute("rolename", "");
		request.getSession().setAttribute("logid", "");
	}
	
	/**
	 * 获取参数
	 * @param request
	 * @param param
	 * @param def
	 * @return
	 */
	protected String getparam(HttpServletRequest request,String param,String def){
		String str=request.getParameter(param);
		if(null==str || str.equals("null"))
			str="";
		
		if(str.length()<1 && def.length()>0)
			str=def;
		
		return str;
	}
}
