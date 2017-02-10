package com.xtr.manager.controller.market;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.basic.BaseController;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.util.FileUtils;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.RandomDataUtil;
import com.xtr.manager.util.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/18 10:48
 */
@Controller
@RequestMapping("protocolManage")
public class protocolManageController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(protocolManageController.class);

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanysService companysService;

    /**
     * 客户签约管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toProtocolManagePage.htm")
    public ModelAndView toProtocolManagePage(ModelAndView mav) {
        mav.addObject("protocolTypeMap",CompanyProtocolConstants.PROTOCOLTYPEMAP);
        mav.setViewName("xtr/market/protocol/protocolManageInfo");
        return mav;
    }

    /**
     * 协议录入1页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toProtocolEnteringFirstPage.htm")
    public ModelAndView toProtocolEnteringFirstPage(ModelAndView mav, Long companyId, Integer operationShowState) {
        ResultResponse resultResponse = companysService.selectCompanyInfoDetail(companyId);
        mav.addObject("companyDetail", resultResponse.getData());
        mav.addObject("operationShowState", operationShowState);
        mav.setViewName("xtr/market/protocol/protocolEnteringFirst");
        return mav;
    }

    /**
     * 协议录入2页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toProtocolEnteringSecondPage.htm")
    public ModelAndView toProtocolEnteringSecondPage(HttpServletRequest request,ModelAndView mav, String formDataStr, Integer operationShowState) {
        CompanysBean companysBean = new CompanysBean();
        try {
            formDataStr = URLDecoder.decode(formDataStr, "UTF-8");
            companysBean = JSONObject.parseObject(formDataStr, CompanysBean.class);
            //生成协议编号
            String protocolCode = "XY" + companysBean.getCompanyId() + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + (int)(Math.random() * 10000);
            mav.addObject("protocolCode", protocolCode);
            //获取登录人,该登录人为业务经理
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            mav.addObject("protocolBusinessManager", sysUserBean.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.addObject("companysBean", companysBean);
        mav.addObject("operationShowState", operationShowState);
        mav.setViewName("xtr/market/protocol/protocolEnteringSecond");
        return mav;
    }

    /**
     * 协议修改页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toProtocolModifyPage.htm")
    public ModelAndView toProtocolModifyPage(ModelAndView mav, Long protocolId) {
        CompanyProtocolsDto protocolDto=companyProtocolsService.selectProtocolAndCompanyById(protocolId);
        mav.addObject("protocolDto", protocolDto);
        mav.setViewName("xtr/market/protocol/protocolModifyState");
        return mav;
    }

    /**
     * 协议添加页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toAddProtocolPage.htm")
    public ModelAndView toAddProtocolPage(HttpServletRequest request,ModelAndView mav, Long companyId,Integer operationShowState) {
        ResultResponse resultResponse = companysService.selectCompanyInfoDetail(companyId);
//        //生成协议编号
//        String protocolCode = "XY" + companyId + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + (int)(Math.random() * 10000);
//        mav.addObject("protocolCode", protocolCode);
        //获取登录人,该登录人为业务经理
        SysUserBean sysUserBean = SessionUtils.getUser(request);
        mav.addObject("protocolBusinessManager", sysUserBean.getUserName());
        mav.addObject("companyDetail", resultResponse.getData());
        mav.addObject("operationShowState", operationShowState);
        mav.addObject("protocolTypeMap",CompanyProtocolConstants.PROTOCOLTYPEMAP);
        mav.setViewName("xtr/market/protocol/addProtocol");
        return mav;
    }

    /**
     * 点击第一个搜索按钮事件
     *
     * @param companyProtocolsDto
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse searchPtorocolManageInfo(CompanyProtocolsDto companyProtocolsDto) {
        ResultResponse resultResponse = companyProtocolsService.selectProtocolByCondition(companyProtocolsDto);
        LOGGER.info("根据过滤条件查询企业协议,获取结果信息：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 点击第二个搜索按钮事件
     * @param companyProtocolsDto
     * @return
     */
    @RequestMapping("queryCompanysProtocol.htm")
    @ResponseBody
    public ResultResponse queryCompanysProtocol(CompanyProtocolsDto companyProtocolsDto) {
        ResultResponse resultResponse = companyProtocolsService.queryCompanysProtocol(companyProtocolsDto);
        LOGGER.info("点击第二个搜索按钮事件,获取结果信息：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 点击确定按钮,更改企业信息,提交协议信息
     * @param companyProtocolsBean
     * @return
     */
    @RequestMapping(value="commitProtocol.htm",method= RequestMethod.POST)
    @ResponseBody
    public ResultResponse commitProtocol(HttpServletRequest request,CompanyProtocolsBean companyProtocolsBean,CompanysBean companysBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            //获取登录人ID
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long userId=sysUserBean.getId();
            resultResponse=companyProtocolsService.updateProtocolAndUpdateCompany(companyProtocolsBean,companysBean,userId);
        }catch(BusinessException e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }catch(Exception e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("点击确定按钮,更改企业信息,提交协议信息,获取结果信息：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 点击确定按钮,修改协议状态
     * @param companyProtocolsBean
     * @return
     */
    @RequestMapping(value="modifyProtocolState.htm",method= RequestMethod.POST)
    @ResponseBody
    public ResultResponse modifyProtocolState(CompanyProtocolsBean companyProtocolsBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(companyProtocolsBean!=null && companyProtocolsBean.getProtocolId()!=null){
                resultResponse=companyProtocolsService.updateCurrentStateByIdBusiness(companyProtocolsBean);
            }else{
                throw new BusinessException("点击确定按钮,修改协议状态,传递的参数不能为空");
            }
        }catch(BusinessException e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("点击确定按钮,修改协议状态,返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 查询征信资料
     * @param companyId
     * @return
     */
    @RequestMapping("companyCreditSearch.htm")
    @ResponseBody
    public ResultResponse companyCreditSearch(HttpServletRequest request,HttpServletResponse response,Long companyId) {
//        //设置没有缓存
//        response.reset();
//        OutputStream outputStream=null;
        ResultResponse resultResponse=new ResultResponse();
        try {
            String currentTimeStr=new SimpleDateFormat("yyyyMMdd").format(new Date());
            String verificationCode = RandomDataUtil.getRandomID(6);
            String dirName="ZX"+currentTimeStr+verificationCode;
            String currentPath=request.getSession().getServletContext().getRealPath("")+File.separator+dirName+File.separator;
            LOGGER.info("企业征信资料临时目录:"+currentPath);
            resultResponse =companyProtocolsService.queryCreditInformations(currentPath,companyId);

             if(resultResponse.isSuccess()){
//                 LOGGER.info("查询征信资料,下载资料前,获取文件名称:"+((String)resultResponse.getData()));
//                 File downloadFile=new File(currentPath+File.separator+((String)resultResponse.getData()));
//                 FileInputStream inputStream=new FileInputStream(downloadFile);
//                 response.setHeader("content-disposition", "attachment;filename="+((String)resultResponse.getData()));
//                 response.setContentType("application/octet-stream");
//                 outputStream = response.getOutputStream();
//                 int len = 0;
//                 byte[] buffer = new byte[1024];
//                 while ((len = inputStream.read(buffer)) > 0) {
//                     outputStream.write(buffer, 0, len);
//                 }
//                 inputStream.close();
//                //删除临时文件
//                 File tempDir=new File(currentPath);
//                 FileUtils.deleteQuietly(tempDir);
                 resultResponse.setData(currentPath);
             }
        } catch (BusinessException e) {
            LOGGER.error("查询征信资料,"+e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        } catch (IOException e) {
            LOGGER.error("查询征信资料,"+e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        } finally{
//            try {
//                if(null != outputStream) {
//                    outputStream.flush();
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                LOGGER.error("查询征信资料,"+e.getMessage());
//                resultResponse.setSuccess(false);
//                resultResponse.setMessage(e.getMessage());
//            }
        }
        LOGGER.info("查询征信资料,返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param currentPath
     */
    @RequestMapping("downloadFile.htm")
    public void downloadFile(HttpServletRequest request,HttpServletResponse response,String currentPath) {
        //设置没有缓存
        response.reset();
        FileInputStream inputStream=null;
        OutputStream outputStream=null;
        File downloadFile=null;
        File delFile=null;
        try{
            String fileName=CompanyProtocolConstants.PROTOCOL_CREDIT_TEMPDIRNAME+CompanyProtocolConstants.PROTOCOL_CREDIT_ENDNAME;
            LOGGER.info("查询征信资料,下载资料前,获取文件名称:"+fileName);
            delFile=new File(currentPath);
            downloadFile=new File(currentPath+fileName);
            inputStream=new FileInputStream(downloadFile);
            response.setHeader("content-disposition", "attachment;filename="+fileName);
            response.setContentType("application/octet-stream");
            outputStream = response.getOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        }catch (BusinessException e) {
            LOGGER.error("查询征信资料,"+e.getMessage());
        } catch (IOException e) {
            LOGGER.error("查询征信资料,"+e.getMessage());
        } finally{
            try {
                if(null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
                if(null != inputStream) {
                    inputStream.close();
                }
                if(null != delFile){
                    //删除临时文件
//                    File tempDir=new File(currentPath);
                    FileUtils.deleteQuietly(delFile);
                }

            } catch (IOException e) {
                LOGGER.error("查询征信资料,"+e.getMessage());
            }
        }
    }

    /**
     * 添加企业协议
     * @param companyProtocolsBean
     * @return
     */
    @RequestMapping(value="addCompanyProtocol.htm",method= RequestMethod.POST)
    @ResponseBody
    public ResultResponse addCompanyProtocol(CompanyProtocolsBean companyProtocolsBean, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();
            resultResponse=companyProtocolsService.addCompanyProtocol(companyProtocolsBean, memberId);
        }catch(BusinessException e){
            LOGGER.error(e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }catch(Exception e){
            LOGGER.error(e.getMessage(),e);
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("添加企业协议,获取结果信息：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

}
