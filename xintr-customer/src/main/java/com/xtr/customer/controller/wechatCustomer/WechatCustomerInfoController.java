package com.xtr.customer.controller.wechatCustomer;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersPersonalBean;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.service.customer.CustomersPersonalService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersStationService;
import com.xtr.api.service.wechatCustomer.WechatCustomerInfoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.util.UploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/9/6 9:07
 */
@Controller
@RequestMapping("wechatCustomerInfo")
public class WechatCustomerInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WechatCustomerInfoController.class);

    @Resource
    private WechatCustomerInfoService wechatCustomerInfoService;

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomersPersonalService customersPersonalService;

    @Resource
    private CustomersStationService customersStationService;

    /**
     * 个人信息填写页面
     * @param mav
     * @param customerId
     * @return
     */
    @RequestMapping("toWechatCustomerMainInfoPage.htm")
    public ModelAndView toCustomerMainInfoPage( ModelAndView mav,Long customerId,Long companyId)throws Exception {
        LOGGER.error("个人信息填写页面,customerId:"+customerId);
        mav.setViewName("xtr/wechatCustomer/wechatCustomerMainInfo");
        CustomersDto customersDto = customersService.selectCustomersByCustomerId(customerId);
        mav.addObject("customersDto", customersDto);
        mav.addObject("customerId", customerId);
        mav.addObject("companyId", companyId);
        return mav;
    }

    /**
     * 移动端信息管理页面
     * @param mav
     * @param customerId
     * @return
     */
    @RequestMapping("toWechatCustomerManagerInfoPage.htm")
    public ModelAndView toWechatCustomerManagerInfoPage( ModelAndView mav,Long customerId,Long companyId)throws Exception {
        LOGGER.error("移动端信息管理页面,customerId:"+customerId);
        mav.setViewName("xtr/wechatCustomer/wechatCustomerManagerInfo");
        CustomersDto customersDto = customersService.selectCustomersByCustomerId(customerId);
        //获取年龄
        String birthdayTime=customersDto.getCustomerBirthdayMonth();
        if(!StringUtils.isStrNull(birthdayTime)){
            int birthdayInt=Integer.parseInt(birthdayTime.substring(0,4));
            int nowInt=Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            mav.addObject("ageStr",(nowInt- birthdayInt+1<=0)?"":String.valueOf(nowInt- birthdayInt+1));
        }
        //司龄
        Date enterTime = customersDto.getStationEnterTime();
        if (enterTime != null) {
            Integer diffDays = DateUtil.getDiffDaysOfTwoDateByNegative(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("yyyy-MM-dd").format(enterTime));
            if (diffDays < 0) {
                mav.addObject("diffYear", 0);
            } else {
                BigDecimal diffYear = new BigDecimal(String.valueOf(diffDays)).divide(new BigDecimal("365"), 1, BigDecimal.ROUND_HALF_DOWN);
                mav.addObject("diffYear", diffYear);
            }
        } else {
            mav.addObject("diffYear", 0);
        }
        mav.addObject("customersDto", customersDto);
        mav.addObject("customerId", customerId);
        mav.addObject("companyId", companyId);
        return mav;
    }

    /**
     * 修改员工基本信息
     * @param customersBean
     * @param customersPersonalBean
     * @return
     */
    @RequestMapping(value="modifyMainInfo.htm")
    @ResponseBody
    public ResultResponse modifyMainInfo(CustomersBean customersBean, CustomersPersonalBean customersPersonalBean) {
        LOGGER.error("修改员工基本信息,customerId:"+customersBean.getCustomerId());
        LOGGER.info("修改员工基本信息,传递参数：基本信息:"+ JSON.toJSONString(customersBean)+",个人信息:"+JSON.toJSONString(customersPersonalBean));
        ResultResponse resultResponse = new ResultResponse();
        try{
            wechatCustomerInfoService.modifyMainInfo(customersBean,customersPersonalBean);
            //查询是否跳到入职规范页面
            int count=customersService.selectCountForIsRedirect(customersBean.getCustomerId());
            if(count>0){
                resultResponse.setData(1);
                //更改跳到入职规范状态
                CustomersBean modifyCustomersBean=new CustomersBean();
                modifyCustomersBean.setCustomerId(customersBean.getCustomerId());
                modifyCustomersBean.setCustomerIsRedirect(CustomerConstants.CUSTOMER_ISCOMPLEMENT_NO);
                customersService.updateIsRedirectState(modifyCustomersBean);
            }
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("修改员工基本信息", e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("修改员工基本信息", e);
        }
        LOGGER.info("修改员工基本信息,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 编辑三大信息的基本信息
     * @param customersBean
     * @return
     */
    @RequestMapping(value="modifyEditBaseInfo.htm")
    @ResponseBody
    public ResultResponse modifyEditBaseInfo(CustomersBean customersBean) {
        LOGGER.error("编辑三大信息的基本信息,customerId:"+customersBean.getCustomerId());
        LOGGER.info("编辑三大信息的基本信息,传递基本信息参数:" + JSON.toJSONString(customersBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            wechatCustomerInfoService.modifyEditBaseInfo(customersBean);
            resultResponse.setSuccess(true);
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("编辑三大信息的基本信息", e.getMessage());
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("编辑三大信息的基本信息",e);
        }
        LOGGER.info("编辑三大信息的基本信息,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 编辑三大信息的个人信息
     * @param customersPersonalBean
     * @return
     */
    @RequestMapping(value="modifyEditPersonalInfo.htm")
    @ResponseBody
    public ResultResponse modifyEditPersonalInfo(CustomersPersonalBean customersPersonalBean) {
        LOGGER.error("编辑三大信息的个人信息,customerId:"+customersPersonalBean.getPersonalCustomerId());
        LOGGER.info("编辑三大信息的个人信息,传递个人信息参数:" + JSON.toJSONString(customersPersonalBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            wechatCustomerInfoService.modifyEditPersonalInfo(customersPersonalBean);
            resultResponse.setSuccess(true);
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("编辑三大信息的个人信息", e.getMessage());
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("编辑三大信息的个人信息",e);
        }
        LOGGER.info("编辑三大信息的个人信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 上传员工微信端身份证图片
     * @param multipartFile
     * @param response
     * @return
     */
    @RequestMapping("wechatCustomerUploadIdCard.htm")
    @ResponseBody
    public String wechatCustomerUploadIdCard(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        LOGGER.info("上传员工微信端身份证图片");
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            String fileName=UploadUtils.upload(multipartFile,"img");
            resultResponse.setSuccess(true);
            resultResponse.setData(fileName);
        }catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("上传员工微信端身份证图片", e);
        }
        LOGGER.info("上传员工微信端身份证图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }
}
