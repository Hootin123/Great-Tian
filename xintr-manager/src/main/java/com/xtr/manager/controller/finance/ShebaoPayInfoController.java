package com.xtr.manager.controller.finance;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.UploadUtils;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by allycw3 on 2016/10/10.
 */
@Controller
@RequestMapping("shebaoPayInfo")
public class ShebaoPayInfoController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ShebaoPayInfoController.class);

    @Resource
    private CompanyShebaoService companyShebaoService;
    /**
     * 社保公积金付款管理页面
     * @param mav
     * @return
     */
    @RequestMapping("toShebaoPayInfoPage.htm")
    public ModelAndView toShebaoPayInfoPage(ModelAndView mav) {
        //获取所有企业名称
        List<CompanyShebaoOrderDto> orderList=companyShebaoService.selectPayInfoWithCompanyInfo();
        mav.addObject("orderList",orderList);
        mav.setViewName("xtr/finance/shebaoPay/shebaoPayInfo");
        return mav;
    }

    /**
     * 添加付款信息页面
     * @param mav
     * @return
     */
    @RequestMapping("toShebaoAddPage.htm")
    public ModelAndView toShebaoAddPage(ModelAndView mav,Long companyOrderId) {
        mav.addObject("companyOrderId",companyOrderId);
        mav.setViewName("xtr/finance/shebaoPay/shebaoAdd");
        return mav;
    }

    /**
     * 初始化数据
     * @return
     */
    @RequestMapping("queryCount.htm")
    @ResponseBody
    public ResultResponse queryCount() {
        ResultResponse resultResponse=new ResultResponse();
        //获取所有用户财务付款的订单数量
        int payAllCount=companyShebaoService.selectPayInfoCount();
        //获取所有用户财务未付款的订单数量
        int noPayCount=companyShebaoService.selectPayInfoNoPayCount();
        resultResponse.setData(payAllCount);
        resultResponse.setMessage(String.valueOf(noPayCount));
        resultResponse.setSuccess(true);
        LOGGER.info("获取财务社保公积金付款管理返回财务付款和款付款所有数量："+ payAllCount+"财务未付款数量:"+noPayCount);
        return resultResponse;
    }

    /**
     * 初始化数据列表
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(CompanyShebaoOrderDto companyShebaoOrderDto,Integer selType) {
        ResultResponse resultResponse=new ResultResponse();
        if(selType!=null && selType.intValue()==0){//根据过滤条件查询
            resultResponse=companyShebaoService.selectPayInfoByCondition(companyShebaoOrderDto);
        }if(selType!=null && selType.intValue()==1){//待付款订单
            resultResponse=companyShebaoService.selectPayInfoNoPayByCondition(companyShebaoOrderDto);
        }else if(selType!=null && selType.intValue()==2){//所有订单
            CompanyShebaoOrderDto allDto=new CompanyShebaoOrderDto();
            allDto.setPageIndex(companyShebaoOrderDto.getPageIndex());
            allDto.setPageSize(companyShebaoOrderDto.getPageSize());
            resultResponse=companyShebaoService.selectPayInfoByCondition(allDto);
        }

        LOGGER.info("获取财务社保公积金付款管理返回数据："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 添加社保公积金付款信息
     * @return
     */
    @RequestMapping("addCompanyOrderPayInfo.htm")
    @ResponseBody
    public ResultResponse addCompanyOrderPayInfo(HttpServletRequest request,CompanyShebaoOrderDto companyShebaoOrderDto) {
        LOGGER.info("添加社保公积金付款信息,传递参数："+ JSON.toJSONString(companyShebaoOrderDto));
        ResultResponse resultResponse=new ResultResponse();
        try{
            SysUserBean userBean=SessionUtils.getUser(request);
            String webRoot = request.getRealPath("/");
            companyShebaoService.addCompanyOrderPayInfo(companyShebaoOrderDto,userBean.getId(),webRoot);
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("添加社保公积金付款信息"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("添加社保公积金付款信息失败");
            LOGGER.error("添加社保公积金付款信息",e);
        }
        LOGGER.info("添加社保公积金付款信息,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    @RequestMapping("uploadShebaoPayImg.htm")
    @ResponseBody
    public String uploadShebaoPayImg(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        LOGGER.info("上传财务付款图片");
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            String fileName= UploadUtils.upload(multipartFile,"img");
            resultResponse.setSuccess(true);
            resultResponse.setData(fileName);
        }catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("上传财务付款图片", e);
        }
        LOGGER.info("上传财务付款图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }
}
