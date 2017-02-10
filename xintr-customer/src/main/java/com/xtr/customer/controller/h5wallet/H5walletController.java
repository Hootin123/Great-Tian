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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.UUID;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/8 17:23
 */
@Controller
@RequestMapping("h5wallet")
public class H5walletController {

    private static final Logger LOGGER = LoggerFactory.getLogger(H5walletController.class);

    @Resource
    private H5walletService h5walletService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanysService companysService;

    /**
     * 跳转到欢迎页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletWelcomePage.htm")
    public ModelAndView toH5walletWelcomePage(ModelAndView mav) {
        mav.setViewName("xtr/h5wallet/h5walletWelcome");
        return mav;
    }

    /**
     * 跳转到登录页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletLoginPage.htm")
    public ModelAndView toH5walletLoginPage(ModelAndView mav) {
        mav.setViewName("xtr/h5wallet/h5walletLogin");
        return mav;
    }

    /**
     * 跳转到注册页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletRegisterPage.htm")
    public ModelAndView toH5walletRegisterPage(ModelAndView mav) {
        mav.setViewName("xtr/h5wallet/h5walletRegister");
        return mav;
    }

    /**
     * 跳转到主页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletHomePage.htm")
    public ModelAndView toH5walletHomePage(ModelAndView mav,Long companyId) {
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletHome");
        return mav;
    }

    /**
     * 跳转到代发页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletIssuingPage.htm")
    public ModelAndView toH5walletIssuingPage(ModelAndView mav,Long companyId) {
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletIssuing");
        return mav;
    }

    /**
     * 跳转到垫发页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletMatPage.htm")
    public ModelAndView toH5walletMatPage(ModelAndView mav,Long companyId) {
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletMat");
        return mav;
    }

    /**
     * 跳转到审核页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletApprovePage.htm")
    public ModelAndView toH5walletApprovePage(ModelAndView mav,Long companyId) {
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletApprove");
        return mav;
    }

    /**
     * 跳转到审核等待页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletApproveWaitPage.htm")
    public ModelAndView toH5walletApproveWaitPage(ModelAndView mav,Long companyId) {
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletApproveWait");
        return mav;
    }

    /**
     * 跳转到审核失败页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletApproveFailedPage.htm")
    public ModelAndView toH5walletApproveFailedPage(ModelAndView mav,Long companyId,String companyAuditRemark)throws Exception {
        if(!StringUtils.isStrNull(companyAuditRemark)){
            companyAuditRemark=URLDecoder.decode(companyAuditRemark,"utf-8");
        }
        LOGGER.info("跳转到审核失败页面，失败原因："+companyAuditRemark );
        mav.addObject("companyAuditRemark",companyAuditRemark);
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletApproveFailed");
        return mav;
    }

    /**
     * 跳转到审核成功页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletApproveSuccessPage.htm")
    public ModelAndView toH5walletApproveSuccessPage(ModelAndView mav,Long companyId) {
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/h5wallet/h5walletApproveSuccess");
        return mav;
    }

    /**
     * H5生成验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="generateH5ValidatorImages.htm")
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

    /**
     * H5发送注册手机验证码
     * @param memberPhone
     * @return
     */
    @RequestMapping(value="sendPhoneMsgByH5.htm")
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
     * H5用户登录
     * @param request
     * @param companyMembersBean
     * @param h5Code
     * @return
     */
    @RequestMapping("h5Login.htm")
    @ResponseBody
    public ResultResponse h5Login(HttpServletRequest request,CompanyMembersBean companyMembersBean,String h5Code) {
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
    @RequestMapping("h5Register.htm")
    @ResponseBody
    public ResultResponse h5Register(HttpServletRequest request,CompanyMembersBean companyMembersBean,String h5Code,String companyName,String phoneCode) {
        ResultResponse resultResponse=new ResultResponse();
        String key = request.getSession().getId() + Constant.VCOD_EIMG;
        try {
            resultResponse= h5walletService.register(companyMembersBean, h5Code, companyName, phoneCode, key,"");
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
     * H5审核
     * @param companysBean
     * @return
     * @throws Exception
     */
    @RequestMapping("h5Approve.htm")
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

//    /**
//     * H5上传营业执照图片
//     * @param file
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("h5UploadOrganizeImg.htm")
//    @ResponseBody
//    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile file,HttpServletResponse response,String fileName) {
//        response.setContentType("text/html; charset=utf-8");
//        ResultResponse resultResponse = new ResultResponse();
//        try {
//            if(file!=null && !StringUtils.isStrNull(fileName)){
//                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
//                String[] filenames={"jpg", "png", "jpeg", "gif"};
////                String newFileName = UUID.randomUUID().toString() + "." + extension;
//                String newFileName = fileName;
//                boolean checkFlag = false;//上传文件类型是否符合规范,false代表不符合
//                for (String filenameCheck : filenames) {
//                    if(filenameCheck.equalsIgnoreCase(extension)) {
//                        checkFlag = true;
//                        break;
//                    }
//                }
//                if(!checkFlag) {
//                    resultResponse.setSuccess(false);
//                    resultResponse.setMessage("文件后缀名错误");
//                    return JSON.toJSONString(resultResponse);
//                }
////                String fileName = multipartFile.getOriginalFilename();
////                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
////                String newFileName = UUID.randomUUID().toString() + "." + suffix;
//                boolean result=AliOss.uploadFile(file.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
//                if(result){
//                    resultResponse.setSuccess(true);
//                    resultResponse.setData(newFileName);
//                }else{
//                    LOGGER.error("H5上传营业执照图片,上传文件失败");
//                    resultResponse.setMessage("上传文件失败");
//                }
//            }else{
//                LOGGER.error("H5上传营业执照图片,没有上传文件");
//                resultResponse.setMessage("没有上传文件");
//            }
//        }catch (IOException e){
//            resultResponse.setMessage(e.getMessage());
//            LOGGER.error(e.getMessage(), e);
//        }
//        LOGGER.info("H5上传营业执照图片,返回结果："+ JSON.toJSONString(resultResponse));
//        return JSON.toJSONString(resultResponse);
//    }

    @RequestMapping("h5UploadOrganizeImg.htm")
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
    @RequestMapping("queryUploadFileName.htm")
    @ResponseBody
    public ResultResponse queryUploadFileName(String type) {
        ResultResponse resultResponse = new ResultResponse();
        String newFileName = UUID.randomUUID().toString() + "." + type;
        resultResponse.setData(newFileName);
        LOGGER.info("H5上传营业执照图片,返回图片名称："+ newFileName);
        return resultResponse;
    }
    /**
     * H5审核判断
     * @param companysBean
     * @return
     * @throws Exception
     */
    @RequestMapping("h5ApproveCheck.htm")
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
     * 跳转到agree页面
     * @return
     */
    @RequestMapping(value="toH5walletAgreePage.htm")
    public  ModelAndView jumpWalletAgree(ModelAndView modelAndView){

        modelAndView.setViewName("xtr/h5wallet/h5walletAgree");
        return modelAndView;
    }



    /**
     * 跳转到产品介绍介绍页面
     * @param mv
     * @return
     */
    @RequestMapping(value = "toProduct.htm")
    public ModelAndView jumpProductIntroduction(ModelAndView mv){
        mv.setViewName("xtr/h5wallet/h5walletProduct");
        return  mv  ;
    }

    /**
     * 分享有礼页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toShareGift.htm")
    public  ModelAndView jumpShareGift(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/h5wallet/h5walletShareGift");
      return  modelAndView;
    }


    /**
     * 用户根据手机号查询奖励
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="searchAllReward")
    public  ModelAndView searchAllReward(HttpServletRequest request,ModelAndView modelAndView){


        return modelAndView;
    }

}
