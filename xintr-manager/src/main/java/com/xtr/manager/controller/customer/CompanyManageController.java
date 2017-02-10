package com.xtr.manager.controller.customer;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepositBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.TradeQueryRequest;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.company.CompanyDepositService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.api.basic.BaseController;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.FileUtils;
import com.xtr.comm.util.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/10 14:49
 */
@Controller
@RequestMapping("companyManage")
public class CompanyManageController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyManageController.class);

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanyDepositService companyDepositService;

    @Resource
    private JdPayService jdPayService;

    /**
     * 企业信息管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("companyManageInfo.htm")
    public ModelAndView toCompanyManageInfoPage(ModelAndView mav) {
        mav.addObject("protocolTypeMap",CompanyProtocolConstants.PROTOCOLTYPEMAPHAVE);
        mav.setViewName("xtr/customer/company/companyManageInfo");
        return mav;
    }

    /**
     * 企业详细信息页面
     *
     * @param mav
     * @param companyId
     * @return
     */
    @RequestMapping("companyManageDetail.htm")
    public ModelAndView toCompanyManageDetailPage(ModelAndView mav, Long companyId,Integer isReturnParam,String flag) {

        ResultResponse resultResponse = companysService.selectCompanyInfoDetail(companyId);
        CompanyDepositBean companyDepositBean=companyDepositService.selectByCompanyId(companyId);
        LOGGER.info("进入企业详细信息页面请求结果：" + JSON.toJSONString(resultResponse));
        LOGGER.info("进入企业详细信息页面请求结果：提现信息："+JSON.toJSONString(companyDepositBean));
        if(StringUtils.isNotBlank(flag)){
            //联系小计的标识
            mav.addObject("flag",flag);
        }
        mav.addObject("companyDetail", resultResponse.getData());
        mav.addObject("isReturnParam", isReturnParam);
        mav.addObject("companyDepositBean", companyDepositBean);
        mav.setViewName("xtr/customer/company/companyManageDetail");
        return mav;
    }

    /**
     * 企业修改协议页面
     *
     * @param mav
     * @param companyId
     * @return
     */
    @RequestMapping("companyModifyProtocol.htm")
    public ModelAndView tocompanyModifyProtocolPage(ModelAndView mav, Long companyId) {
        List<CompanyProtocolsBean> protocolList = companyProtocolsService.selectByCompanyId(companyId);
        mav.addObject("protocolList", protocolList);
        mav.addObject("companyId", companyId);
        mav.setViewName("xtr/customer/company/modifyProtocol");
        return mav;
    }

    /**
     * 企业审核页面
     *
     * @param mav
     * @param companyId
     * @return
     */
    @RequestMapping("toCompanyApprovePage.htm")
    public ModelAndView toCompanyApprovePage(ModelAndView mav, Long companyId) {
        ResultResponse resultResponse = companysService.selectCompanyInfoDetail(companyId);
        mav.addObject("companyApproveDetail", resultResponse.getData());
        mav.setViewName("xtr/customer/company/companyApprove");
        return mav;
    }

    /**
     * 点击搜索按钮事件
     *
     * @param companysDto
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse companyManageSearch(CompanysDto companysDto) {
        ResultResponse resultResponse = companysService.selectCompanyInfoPageList(companysDto);
        LOGGER.info("点击搜索获取企业信息请求结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 根据企业编号获取提现、充值信息
     *
     * @param companyRechargeDto
     * @return
     */
    @RequestMapping("companyRechargeDetailSearch.htm")
    @ResponseBody
    public ResultResponse companyRechargeDetailSearch(CompanyRechargeDto companyRechargeDto) {
        ResultResponse resultResponse = companysService.selectCompanyRechargeList(companyRechargeDto);
        LOGGER.info("获取企业充值或提现记录请求结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 根据企业编号获取发工资信息
     *
     * @param companySalaryExcelBean
     * @return
     */
    @RequestMapping("companyPayoffDetailSearch.htm")
    @ResponseBody
    public ResultResponse companyPayoffDetailSearch(CompanySalaryExcelBean companySalaryExcelBean) {
        ResultResponse resultResponse = companysService.selectCompanySalaryList(companySalaryExcelBean);
        LOGGER.info("获取企业发工资记录请求结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 修改企业协议
     */
    @RequestMapping("modifyProtocol.htm")
    @ResponseBody
    public ResultResponse modifyProtocol(int dtype, int ftype, String dno, String fno, Long companyId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = companyProtocolsService.updateCompanyProtocol(dtype, ftype, dno, fno, companyId);
            LOGGER.info("获取修改企业协议请求结果：" + JSON.toJSONString(resultResponse));
        } catch (BusinessException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 根据企业ID查询协议(分页)
     * @param companyProtocolsBean
     * @return
     */
    @RequestMapping("companyProtocolDetailSearch.htm")
    @ResponseBody
    public ResultResponse companyProtocolDetailSearch(CompanyProtocolsBean companyProtocolsBean) {
        ResultResponse resultResponse = companyProtocolsService.selectPageListByCompanyId(companyProtocolsBean);
        LOGGER.info("根据企业ID查询协议(分页),返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 企业资料审核
     * @param companyId
     * @return
     */
    @RequestMapping("companyApprove.htm")
    @ResponseBody
    public ResultResponse companyApprove(Long companyId,Integer status,String companyAuditRemark) {
        LOGGER.info("企业资料审核,传递参数：企业ID:" + companyId+",状态"+status+",驳回原因:"+companyAuditRemark);
        ResultResponse resultResponse=new ResultResponse();
        if(companyId!=null && status!=null){
            CompanysBean companysBean=new CompanysBean();
            companysBean.setCompanyId(companyId);
            companysBean.setCompanyAuditStatus(status);
            if(status== CompanyConstant.COMPANYMEMBER_AUDITSTATUS_BACK){
                companysBean.setCompanyAuditRemark(companyAuditRemark);
            }
            int count = companysService.updateByPrimaryKeySelective(companysBean);
            if(count<=0){
                resultResponse.setMessage("企业资料审核失败");
            }else{
                resultResponse.setSuccess(true);
            }
        }else{
            resultResponse.setMessage("企业资料审核,传递参数不能为空");
        }
        LOGGER.info("企业资料审核,返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 根据文件名称下载或浏览文件
     * @param response
     * @param fileName
     */
    @RequestMapping("browseFile.htm")
    public void browseFile(HttpServletResponse response, String fileName) {
        //设置没有缓存
        response.reset();
        InputStream browserStream=null;
        OutputStream outputStream=null;
        try{
            LOGGER.info("根据文件名称下载或浏览文件,获取文件名称:"+fileName);
            if(fileName.toLowerCase().endsWith(".png")
                    ||fileName.toLowerCase().endsWith(".jpg")
                    ||fileName.toLowerCase().endsWith(".jpeg")
                    ||fileName.toLowerCase().endsWith(".bmp")
                    ||fileName.toLowerCase().endsWith(".gif")){//图片
                browserStream= AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"),fileName);
            }
            if(browserStream!=null){
                response.setHeader("content-disposition", "attachment;filename="+fileName);
                response.setContentType("application/octet-stream");
//                response.setContentType("image/*");
                outputStream = response.getOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = browserStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
            }
        } catch (IOException e) {
            LOGGER.error("根据文件名称下载或浏览文件,"+e.getMessage());
        } finally{
            try {
                if(null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
                if(null != browserStream) {
                    browserStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("根据文件名称下载或浏览文件,"+e.getMessage());
            }
        }
    }

    @RequestMapping("/queryPayStatus")
    @ResponseBody
    public ResultResponse queryPayStatus(String orderId, long companyId){
        if(StringUtils.isBlank(orderId)){
            return ResultResponse.buildFail(null).message("请输入正确的订单号");
        }
        TradeQueryRequest request = new TradeQueryRequest(BusinessType.VALIDATE_CUSTOMER);
        request.setOutTradeNo(orderId);
        NotifyResponse notifyResponse = jdPayService.tradeQuery(request);
        companysService.doJdResponse(notifyResponse);
        return ResultResponse.buildSuccess(companysService.selectCompanyInfoDetail(companyId));
    }
}
