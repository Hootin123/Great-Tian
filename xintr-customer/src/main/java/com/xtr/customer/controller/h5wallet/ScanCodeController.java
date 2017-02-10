package com.xtr.customer.controller.h5wallet;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.h5wallet.H5walletService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

/**
 * 用户扫码登录 注册 完善资料
 * Created by admin on 2016/8/22.
 */

@Controller
@RequestMapping("scanCode")
public class ScanCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(H5walletController.class);


    @Resource
    private H5walletService h5walletService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanysService companysService;





    /**
     * 跳转到登录页面
     * @param mav
     * @return
     */
    @RequestMapping(value="toH5ScanLoginPage.htm")
    public ModelAndView toH5walletLoginPage(ModelAndView mav,@RequestParam(value="registerCampaignCode",required = false) String registerCampaignCode, HttpServletRequest request) {
        if(!com.xtr.comm.jd.util.StringUtils.isBlank(registerCampaignCode)){
            request.getSession().setAttribute("registerCampaignCode", registerCampaignCode);
        }

        mav.setViewName("xtr/h5scan/h5scanLogin");
        return mav;
    }

    /**
     * 跳转到注册页面
     * @param mav
     * @return
     */
    @RequestMapping(value="toH5ScanRegisterPage.htm")
    public ModelAndView toH5walletRegisterPage(ModelAndView mav,@RequestParam(value="registerCampaignCode",required = false) String registerCampaignCode, HttpServletRequest request) {
        if(!com.xtr.comm.jd.util.StringUtils.isBlank(registerCampaignCode)){
            request.getSession().setAttribute("registerCampaignCode", registerCampaignCode);
        }
        mav.setViewName("xtr/h5scan/h5scanRegister");
        return mav;
    }

    /**
     * 跳转到agree页面
     * @return
     */
    @RequestMapping(value="toH5ScanAgreePage.htm")
    public  ModelAndView jumpWalletAgree(ModelAndView modelAndView){

        modelAndView.setViewName("xtr/h5scan/h5scanAgree");
        return modelAndView;
    }

    /**
     * H5发送注册手机验证码
     * @param memberPhone
     * @return
     */
    @RequestMapping(value="sendPhoneMsgByScanCode.htm")
    @ResponseBody
    public ResultResponse sendPhoneMsgByH5(String memberPhone) {

        ResultResponse resultResponse = new ResultResponse();
        try{
            resultResponse= h5walletService.sendPhoneMsgByH5(memberPhone);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }catch(IOException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }catch(Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("H5发送注册手机验证码,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 登陆后的审核判断
     * @param companysBean
     * @return
     * @throws Exception
     */
    @RequestMapping("h5ScanApproveCheck.htm")
    @ResponseBody
    public ResultResponse h5ApproveCheck(CompanysBean companysBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanysBean resultBean= h5walletService.h5CheckApprove(companysBean);
            resultResponse.setSuccess(true);
            resultResponse.setData(resultBean);
        }catch (BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("H5审核判断,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 跳转到立即验证的页面
     * @param mav
     * @return
     */
    @RequestMapping(value="toAuthenticationPage.htm")
    public  ModelAndView  jumpToAuthenticationPage(ModelAndView mav,Long companyId){

        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5scan/h5scanPachet");
        return  mav;
    }

    /**
     * 跳转到等待审核页面
     * @param mav
     * @param companyId
     * @return
     */
    @RequestMapping(value="toh5scanApproveWait.htm")
    public ModelAndView jumpToApproveWaitPage(ModelAndView mav,Long companyId){
     mav.addObject("companyId",companyId);
     mav.setViewName("xtr/h5scan/h5scanApproveWait");
     return mav;
    }


    /**
     * 跳转到成功审核的页面
     * @return
     */
    @RequestMapping(value="toh5scanApproveSuccess.htm")
    public ModelAndView jumpToApproveSuccessPage(ModelAndView modelAndView,long companyId){
        modelAndView.addObject("companyId",companyId);
        modelAndView.setViewName("xtr/h5scan/h5scanApproveSuccess");
        return modelAndView;
    }

    /**
     * 跳转到审核失败的页面
     * @return
     */
    @RequestMapping(value="toh5scanApproveFailed.htm")
    public ModelAndView jumptoApproveFailedPage(ModelAndView modelAndView,HttpServletRequest request) throws UnsupportedEncodingException {
       long companyId = Long.valueOf(request.getParameter("companyId"));
        String companyAuditRemark = StringUtils.isStrNull(request.getParameter("companyAuditRemark"))==true?"":request.getParameter("companyAuditRemark");
     if(!StringUtils.isStrNull(companyAuditRemark)){
         companyAuditRemark= URLDecoder.decode(companyAuditRemark,"utf-8");
     }
     modelAndView.addObject("companyId",companyId);
     modelAndView.addObject("companyAuditRemark",companyAuditRemark) ;
     modelAndView.setViewName("xtr/h5scan/h5scanApproveFailed");
     return modelAndView;
    }


    /**
     * 跳转到审核页面
     * @param modelAndView
     * @param companyId
     * @return
     */
    @RequestMapping(value="toScanApprove.htm")
    public ModelAndView jumpToApprovePage(ModelAndView modelAndView,long companyId,String companyName,String companyCorporationPhone) throws UnsupportedEncodingException {

        if(!StringUtils.isStrNull(companyName)){
            companyName= URLDecoder.decode(companyName,"utf-8");
        }
        modelAndView.addObject("companyName",companyName);
        modelAndView.addObject("companyCorporationPhone",companyCorporationPhone);
        modelAndView.addObject("companyId",companyId);
        modelAndView.setViewName("xtr/h5scan/h5scanApprove");
        return modelAndView;
    }

    /**
     * H5用户登录
     * @param request
     * @param companyMembersBean
     * @param h5Code
     * @return
     */
    @RequestMapping(value="h5ScanLogin.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse h5Login(HttpServletRequest request, CompanyMembersBean companyMembersBean, String h5Code) {
        ResultResponse resultResponse=new ResultResponse();
        String key = request.getSession().getId() + Constant.VCOD_EIMG;
        try {
            resultResponse= h5walletService.login(companyMembersBean, h5Code, key);
            if(resultResponse.isSuccess()){
                CompanyMembersBean resultBean=(CompanyMembersBean)resultResponse.getData();
                //根据企业ID获取企业信息放入session中
                CompanysBean companyBean=companysService.selectCompanyByCompanyId(resultBean.getMemberCompanyId());
                request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);
                request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean);
                request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, companyBean);
                resultResponse.setData(resultBean.getMemberCompanyId());
            }
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }catch(Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("H5注册用户,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * H5注册用户
     * @param request
     * @param companyMembersBean
     * @param h5Code
     * @param companyName
     * @param phoneCode
     * @return
     */
    @RequestMapping(value="h5ScanRegister.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse h5Register(HttpServletRequest request,CompanyMembersBean companyMembersBean,String h5Code,String companyName,String phoneCode) {
        ResultResponse resultResponse=new ResultResponse();
        String key = request.getSession().getId() + Constant.VCOD_EIMG;

               try {
            //获取手机号
            String phone = companyMembersBean.getMemberPhone();
            //公司名称
            String cname = companyMembersBean.getCompanyName();

            //活动代码
            String registerCampaignCode =  (String)request.getSession().getAttribute("registerCampaignCode");
            resultResponse= h5walletService.register(companyMembersBean, h5Code, companyName, phoneCode, key,registerCampaignCode);
            if(resultResponse.isSuccess()){
                CompanyMembersBean resultBean=(CompanyMembersBean)resultResponse.getData();
                //根据企业ID获取企业信息放入session中
                CompanysBean companyBean=companysService.selectCompanyByCompanyId(resultBean.getMemberCompanyId());
                request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);
                request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean);
                request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, companyBean);
                request.getSession().setAttribute("phone",phone);
                request.setAttribute("companyName",cname);
                resultResponse.setData(resultBean.getMemberCompanyId());
            }
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }catch(Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("H5注册用户,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 查看
     * @param type
     * @return
     */
    @RequestMapping("queryScanUploadFileName.htm")
    @ResponseBody
    public ResultResponse queryUploadFileName(String type) {
        ResultResponse resultResponse = new ResultResponse();
        String newFileName = UUID.randomUUID().toString() + "." + type;
        resultResponse.setData(newFileName);
        LOGGER.info("H5上传营业执照图片,返回图片名称："+ newFileName);
        return resultResponse;
    }

    /**
     * H5生成验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="generateScanH5ValidatorImages.htm")
    public void generateH5ValidatorImages(HttpServletRequest request, HttpServletResponse response) {
        try{
            String key = request.getSession().getId()+ Constant.VCOD_EIMG;
            //生成验证码
            //h5walletService.generateH5ValidatorImages(key,response);
            int w = 100, h = 40;
            //生成验证码图片并放入response中
            String verifyCode = VerifyCodeUtils.outputVerifyImage(w,h,response.getOutputStream(),4);
            LOGGER.info("H5注册生成验证码:"+verifyCode);
            //将验证码存入缓存
            ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
            valueOperations.set(key,verifyCode);
        }catch(IOException e){
            LOGGER.error(e.getMessage());
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    @RequestMapping(value="toSuccessPage.htm")
    public ModelAndView jumpToSuccessPage(ModelAndView modelAndView) {
        modelAndView.setViewName("xtr/h5scan/h5scanSubmit");
        return modelAndView;
    }
    /**
     * H5上传营业执照图片
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("h5ScanUploadOrganizeImg.htm")
    @ResponseBody
    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile,HttpServletResponse response) {
        LOGGER.info("上传H5图片");
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(multipartFile!=null){
//                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String fileName = multipartFile.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String[] filenames={"jpg", "png", "jpeg", "gif"};
                String newFileName = UUID.randomUUID().toString() + "." + suffix;
                boolean checkFlag = false;//上传文件类型是否符合规范,false代表不符合
                for (String filenameCheck : filenames) {
                    if(filenameCheck.equalsIgnoreCase(suffix)) {
                        checkFlag = true;
                        break;
                    }
                }
                if(!checkFlag) {
                    resultResponse.setSuccess(false);
                    resultResponse.setMessage("文件后缀名错误");
                    return JSON.toJSONString(resultResponse);
                }
                boolean result=AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
                if(result){
                    resultResponse.setSuccess(true);
                    resultResponse.setData(newFileName);
                }else{
                    LOGGER.error("H5上传营业执照图片,上传文件失败");
                    resultResponse.setMessage("上传文件失败");
                }
            }else{
                LOGGER.error("H5上传营业执照图片,没有上传文件");
                resultResponse.setMessage("没有上传文件");
            }
        }catch (IOException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("H5上传营业执照图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }
    /**
     * H5审核
     * @param companysBean
     * @return
     * @throws Exception
     */
    @RequestMapping("h5ScanApprove.htm")
    @ResponseBody
    public ResultResponse h5Approve(CompanysBean companysBean) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse= h5walletService.h5Approve(companysBean);
        }catch (BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("H5审核,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

}
