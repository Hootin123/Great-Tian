package com.xtr.manager.controller.market;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.station.StationCollaborationBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.manager.util.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/12 14:21
 */
@Controller
@RequestMapping("companyIntent")
public class CooperationIntentController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CooperationIntentController.class);

    @Resource
    private StationCollaborationService stationCollaborationService;

    /**
     * 合作意向管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("companyIntent.htm")
    public ModelAndView toCompanyIntentManagePage(ModelAndView mav) {
        mav.setViewName("xtr/market/intent/cooperationIntent");
        return mav;
    }

    /**
     * 合作意向处理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCompanyIntentHandlePage.htm")
    public ModelAndView toCompanyIntentHandlePage(ModelAndView mav,Long itemId) {
        LOGGER.info("跳转到合作意向处理页面,传递参数ID:"+itemId);
        StationCollaborationBean stationCollaborationBean=stationCollaborationService.selectStationCollaborationInfoById(itemId);
        mav.addObject("stationCollaborationBean",stationCollaborationBean);
        mav.setViewName("xtr/market/intent/cooperationIntentHandle");
        return mav;
    }

    /**
     * 点击搜索按钮事件
     *
     * @param stationCollaborationBean
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse companyIntentSearch(StationCollaborationBean stationCollaborationBean) {
        ResultResponse resultResponse = stationCollaborationService.selectStationCollaborationInfoPageList(stationCollaborationBean);
        LOGGER.info("点击搜索获取合作意向请求结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 处理合作意向
     *
     * @param stationCollaborationBean
     * @return
     */
    @RequestMapping("handleIntent.htm")
    @ResponseBody
    public ResultResponse handleIntent(HttpServletRequest request,@RequestBody StationCollaborationBean stationCollaborationBean) {
        LOGGER.info("处理合作意向,传递参数:" + JSON.toJSONString(stationCollaborationBean));
        ResultResponse resultResponse=new ResultResponse();
        try{
            if(stationCollaborationBean!=null){
                //获取登录人ID
                SysUserBean sysUserBean = SessionUtils.getUser(request);
                Long userId=sysUserBean.getId();
                stationCollaborationBean.setRecallTime(new Date());
                stationCollaborationBean.setAdminUser(Integer.valueOf(sysUserBean.getId().toString()));
                stationCollaborationBean.setAdminUsername(sysUserBean.getUserName());
                int count=stationCollaborationService.updateByPrimaryKeySelective(stationCollaborationBean);
                if(count<=0){
                    throw new BusinessException("处理合作意向失败");
                }
                resultResponse.setSuccess(true);
            }else{
                throw new BusinessException("处理合作意向,传递参数不能为空");
            }
        }catch(BusinessException e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("处理合作意向返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 合作意向管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("cooperationIntentExport.htm")
    public ModelAndView toCooperationIntentExportPage(ModelAndView mav) {
        mav.setViewName("xtr/market/intent/cooperationIntentExport");
        return mav;
    }

    /**
     * 导出
     * @param request
     * @param response
     * @param startTime
     * @param endTime
     */
    @RequestMapping("batchExportCollaboration.htm")
    public void batchExportCollaboration(HttpServletRequest request,HttpServletResponse response,Date startTime,Date endTime) {

        LOGGER.info("导出合作意向,传递参数:"+startTime+","+endTime);
        String webRoot = request.getRealPath("/");

        String payTemplatePath = PropertyUtils.getString("station.download.export.template.path");

        String downloadPath = PropertyUtils.getString("finance.download.path");


        //设置没有缓存
        response.reset();
        try {
            File file = stationCollaborationService.generatorSalaryExcel(webRoot, payTemplatePath, downloadPath,startTime,endTime);

            String fileName = file.getName();
            if (StringUtils.isNotBlank(fileName)) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            FileInputStream inputStream = new FileInputStream(file);

            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            OutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }
}
