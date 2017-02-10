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
import com.xtr.comm.util.UploadUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
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
import java.io.IOException;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/9/13 16:54
 */
@Controller
@RequestMapping("h5walletNew")
public class H5walletNewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(H5walletNewController.class);

    @Resource
    private H5walletService h5walletService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanysService companysService;

    /**
     * 跳转到注册页面
     * @param mav
     * @return
     */
    @RequestMapping("toH5walletRegisterPage.htm")
    public ModelAndView toH5walletRegisterPage(ModelAndView mav,String companyContactPlace) {
        mav.addObject("companyContactPlace",companyContactPlace);
        mav.setViewName("xtr/h5walletNew/h5walletRegister");
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
        mav.setViewName("xtr/h5walletNew/h5walletApprove");
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
        mav.setViewName("xtr/h5walletNew/h5walletApproveWait");
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
    public ResultResponse h5Register(HttpServletRequest request,CompanyMembersBean companyMembersBean,String h5Code,String companyName,String phoneCode,String companyContactPlace) {
        ResultResponse resultResponse=new ResultResponse();
        String key = request.getSession().getId() + Constant.VCOD_EIMG;
        try {
            resultResponse= h5walletService.register(companyMembersBean, h5Code, companyName, phoneCode, key,companyContactPlace);
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

    @RequestMapping("h5UploadOrganizeImg.htm")
    @ResponseBody
    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        LOGGER.info("上传H5图片");
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            String fileName= UploadUtils.upload(multipartFile,"img");
            resultResponse.setSuccess(true);
            resultResponse.setData(fileName);
        }catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("上传员工微信端身份证图片", e);
        }
        LOGGER.info("H5上传营业执照图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
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

        modelAndView.setViewName("xtr/h5walletNew/h5walletAgree");
        return modelAndView;
    }
}
