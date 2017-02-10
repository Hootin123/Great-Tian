package com.xtr.company.controller.home;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.xtr.comm.util.DateUtil;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.service.company.CompanysService;
import com.xtr.comm.basic.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StationAjaxController extends BaseController {

	@Resource
	private CompanysService companysService;

	@RequestMapping("newslist.htm")
	@ResponseBody
	public ResultResponse newslist(HttpServletRequest request,HttpServletResponse response){
		ResultResponse resultResponse = new ResultResponse();
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("data", "");
		try{
			
			int pidx = Integer.parseInt(this.getparam(request, "pidx", "1"));
			int pnum = Integer.parseInt(this.getparam(request, "pnum", SystemConstants.DEFAULT_PAGENUM+""));
			int pidx1=pnum*(pidx-1);
			
			int type=Integer.parseInt(this.getparam(request, "type", "0"));
			
			if(type<1){
				result.put("code", "1");
				result.put("msg", "参数错误(type)");
				return null;
			}
			
			
			Map<String,Object> par=new HashMap<String,Object>();
			par.put("news_type", type);
			par.put("news_state", 1);
			par.put("orderby", 1);
			
			int total=companysService.findListPageCount(par);
			
			par.put("pidx", pidx1);
			par.put("pnum", pnum);
			
			List<Map<String,Object>> list=companysService.findListPage(par);
			for (Map<String,Object> map:list) {
				String imgbak=map.get("news_img").toString().replace("http://thisadmi.xintairuan.com/upload/","/uploadimg/");
				imgbak=imgbak.replace("/upload/","/uploadimg/");
				map.put("news_img",imgbak);
				map.put("news_content",map.get("news_content").toString().replace("/upload/","/uploadimg/"));
			}
			result.put("code", "0");
			result.put("msg", "ok");
			result.put("data", list);
			result.put("total", total);
			
			if(null!=list && list.size()>0)
				result.put("pages", PageHelper.pages(total, pnum, pidx, "", "...").get("page"));
			
			
			
		}catch(Exception e){
			result.put("code", "-1");
			result.put("msg", "网络异常");
			return null;
		}
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
		resultResponse.setSuccess(true);
		resultResponse.setData(result);
		return resultResponse;
	}
	
	/**
	 * 新闻详情
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("newsdetail.htm")
	@ResponseBody
	public ResultResponse newsdetail(HttpServletRequest request,HttpServletResponse response){
		ResultResponse resultResponse = new ResultResponse();
		Map<String,Object> result=new HashMap<String,Object>();
		result.put("data", "");
		try{
			
			long id=Long.parseLong(this.getparam(request, "id", "0"));
			
			if(id<1){
				result.put("code", "1");
				result.put("msg", "参数错误(id)");
				return null;
			}
			
			Map<String,Object> par=new HashMap<String,Object>();
			par.put("news_id", id);
			par.put("news_state", 1);
			
			Map<String,Object> map=companysService.find(par);
			
			if(map==null){
				result.put("code", "2");
				result.put("msg", "内容不存在");
				return null;
			}

			map.put("news_content",map.get("news_content").toString().replace("/upload/","/uploadimg/"));
			//返回成功
			result.put("code", "0");
			result.put("msg", "ok");
			result.put("data", map);
			
			
			
		}catch(Exception e){
			result.put("code", "-1");
			result.put("msg", "网络异常");
			return null;
		}
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
		resultResponse.setSuccess(true);
		resultResponse.setData(result);
		return resultResponse;
	}

	/**
	 * 添加用户合作意向
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="addCollaboration")
	@ResponseBody
	public Object addCollaboration(HttpServletRequest request,HttpServletResponse response){

		Map<String,Object> result=new HashMap<String,Object>();
		result.put("data", "");

		try{

			String phone=this.getparam(request, "phone", "");

			if(phone.length()!=11){
				result.put("code", "1");
				result.put("msg", "请填写有效手机号码");
				return result;
			}

			Map<String,Object> par=new HashMap<String,Object>();
			par.put("item_mobile", phone);

			Map<String,Object> cb=companysService.stationCollaborationFind(par);


			//Date now=DateUtil.getCurrentDatetime().getCurrentDatetime();
			par=new HashMap<String,Object>();
			if(cb==null){
				par.put("item_mobile", phone);
				par.put("item_type", 1);
				par.put("adddate", DateUtil.getCurrentDatetime());
				par.put("sign", 1);
				par.put("item_times", 1);
				par.put("item_mobile", phone);

				int cnt=companysService.stationCollaborationAdd(par);
			}else{
				par.put("adddate", DateUtil.getCurrentDatetime());
				par.put("item_times_autoIncrease", 1);

				par.put("item_id", cb.get("item_id"));
				int cnt=companysService.stationCollaborationUpdate(par);
			}


			result.put("code", "0");
			result.put("msg", "ok");

		}catch(Exception e){
			e.printStackTrace();
			result.put("code", "-1");
			result.put("msg", "网络异常");
			return result;
		}

		return result;
	}

}
