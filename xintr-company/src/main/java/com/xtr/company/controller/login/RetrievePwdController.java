package com.xtr.company.controller.login;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.station.StationSmsRecordsService;
import com.xtr.api.service.station.StationSmscontService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.enums.CompanyMeMemberSignEnum;
import com.xtr.comm.enums.ErrorCodeEnum;
import com.xtr.comm.enums.StationSmsRecordsTypeEnum;
import com.xtr.comm.util.*;
import com.xtr.comm.verify.VerifyCodeUtils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/21 17:37
 */
@Controller
public class RetrievePwdController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrievePwdController.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private StationSmscontService stationSmscontService;

    @Resource
    private StationSmsRecordsService stationSmsRecordsService;

    @Resource
    private SendMsgService sendMsgService;

    /**
     * 跳转到根据邮箱修改密码第一步页面
     * @param model
     * @return
     */
    @RequestMapping(value="toRetrievePwdFirstPage.htm")
    public ModelAndView toRetrievePwdFirstPage(ModelAndView model) {
        model.setViewName("xtr/login/retrievePwd/retrievePwdByEmailFirst");
        return model;
    }

    /**
     * 跳转到根据邮箱修改密码第二步页面
     * @param model
     * @return
     */
    @RequestMapping(value="toRetrievePwdSecondPage.htm")
    public ModelAndView toRetrievePwdSecondPage(ModelAndView model,String loginName,String  memberEmail) {
        model.addObject("loginName",loginName);
        model.addObject("memberEmail",memberEmail);
        model.setViewName("xtr/login/retrievePwd/retrievePwdByEmailSecond");
        return model;
    }

    /**
     * 跳转到根据邮箱修改密码第三步页面
     * @param model
     * @return
     */
    @RequestMapping(value="toRetrievePwdThirdPage.htm")
    public ModelAndView toRetrievePwdThirdPage(ModelAndView model,String loginName) {
        model.addObject("loginName",loginName);
        model.setViewName("xtr/login/retrievePwd/retrievePwdByEmailThird");
        return model;
    }

    /**
     * 跳转到根据手机修改密码第一步页面
     * @param model
     * @return
     */
    @RequestMapping(value="toRetrievePwdByPhoneFirstPage.htm")
    public ModelAndView toRetrievePwdByPhoneFirstPage(ModelAndView model) {
        model.setViewName("xtr/login/retrievePwd/retrievePwdByPhoneFirst");
        return model;
    }

    /**
     * 忘记密码生成验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="generateMailValidatorImages.htm")
    public void authImages(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{
            int w = 160, h = 34;
            String key = request.getSession().getId()+ Constant.VCOD_EIMG;
            String verifyCode = VerifyCodeUtils.outputVerifyImage(w,h,response.getOutputStream(),4);
            LOGGER.info("忘记密码生成验证码:"+verifyCode);
            ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
            valueOperations.set(key,verifyCode);
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 发送找回密码邮件
     * @param request
     * @param loginName
     * @param emailCode
     * @return
     */
    @RequestMapping(value="sendEmailByRetrievePwd.htm")
    @ResponseBody
    public ResultResponse sendEmailByRetrievePwd(HttpServletRequest request,String loginName,String emailCode){
        ResultResponse resultResponse = new ResultResponse();
        try{
            if(StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(emailCode)){
                String key = request.getSession().getId()+Constant.VCOD_EIMG;
                if(!checkVcodeimg(emailCode,key)){//验证码不正确
                    resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
                }else{//验证码正确
                    //通过用户名验证用户是否合法
                    CompanyMembersBean companyMembersBean=new CompanyMembersBean();
                    companyMembersBean.setMemberLogname(loginName);
                    companyMembersBean.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
                    CompanyMembersBean resultBean=companyMembersService.selectByLoginNameActive(companyMembersBean);
                    if(resultBean!=null){
                        //发送密码确认邮件
//                        CompanyMembersBean resultBean=companyMemberList.get(0);
                        if(resultBean.getMemberEmail()!=null){
                            boolean result=sendEmailActiveInfo(resultBean);
                            if(result){
                                resultResponse.setSuccess(true);
                                resultResponse.setData(resultBean);
                            }else{
                                resultResponse.setMessage("发送邮箱失败");
                            }
                        }else{
                            resultResponse.setMessage("该用户没有绑定邮箱");
                        }
                    }else{
                        resultResponse.setMessage("用户名不正确");
                    }
                }
            }else{
                resultResponse.setMessage("请填入用户名或者验证码");
            }
        }catch(BusinessException e){
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("发送找回密码邮件,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 验证验证码详细
     * @param codeParam
     * @param key
     * @return
     */
    private boolean checkVcodeimg(String codeParam,String key){
        boolean vcodeimgFlag = false;
        LOGGER.error("忘记密码验证码比较:输入的验证码:"+codeParam);
        ValueOperations<String,Object> fetchCacheOperations=redisTemplate.opsForValue();
        String value =(String)fetchCacheOperations.get(key);
        LOGGER.error("忘记密码验证码比较:存储的验证码:"+value+",输入的验证码:"+codeParam+",比较结果:"+(value.toLowerCase().equals(codeParam.toLowerCase())));
        if(value.toLowerCase().equals(codeParam.toLowerCase())){
            vcodeimgFlag =true;
        }
        return vcodeimgFlag;
    }

    /**
     * 找回密码发送邮件
     * @param resultBean
     * @return
     */
    private boolean sendEmailActiveInfo(CompanyMembersBean resultBean){
        //邮件服务器
        String host= PropertyUtils.getString("email.host");
        //发送者邮箱
        String servername=PropertyUtils.getString( "email.servername");
        //发送者邮箱密码
        String serverpwd=PropertyUtils.getString( "email.serverpwd");
        //发送者手机号
//        String memgerPhone = resultBean.getMemberPhone();
        //接收者用户名
//        String memberLogname=resultBean.getMemberLogname();
        //接收者邮箱
        String memberEmail=resultBean.getMemberEmail();
        //生成邮箱验证码
        String verifyCode = VerifyCodeUtils.generateVerifyCode(6);
        LOGGER.info(resultBean.getMemberLogname()+"找回密码发送邮件,邮箱验证码："+verifyCode);

        //邮件内容
        StringBuffer sb=new StringBuffer(
                "你好!<br/>" +
                        "你的登录邮箱为"+memberEmail+",邮件验证码为(有效期30分钟):<br/>"+
                        "<Strong>"+verifyCode+"</Strong><br/>"+
                        "薪太软为你提供一站式薪酬福利解决方案，包括：代发工资、垫发工资、代缴社保公积金等服务。<br/>" +
                        "薪太软微信公众账号：<br/>");
        sb.append("<img src='http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/fbe293d0-9e8e-4148-b752-d7ccc3ef30c9.png'/>");
        //发送邮件
        boolean sendMessageResult= EmailUtils.sendMail(host, servername, serverpwd, memberEmail, sb.toString(), "薪太软邮件验证码",0);
        if(sendMessageResult){//将验证码存入缓存，并保留30分钟
            String key =CompanyConstant.COMPANYMEMBER_RETRIEVEPWD_CODE+memberEmail;
            ValueOperations<String,Object> fetchCacheOperations=redisTemplate.opsForValue();
            fetchCacheOperations.set(key,verifyCode,30, TimeUnit.MINUTES);
        }
        return sendMessageResult;
    }

    /**
     * 邮箱验证码确认
     * @param loginName
     * @param memberEmail
     * @param emailCode
     * @return
     */
    @RequestMapping(value="emailCodeValidator.htm")
    @ResponseBody
    public ResultResponse emailCodeValidator(String loginName,String  memberEmail,String emailCode){
        ResultResponse resultResponse = new ResultResponse();
        try{
            if(StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(emailCode) && StringUtils.isNotBlank(memberEmail)){
                String key =CompanyConstant.COMPANYMEMBER_RETRIEVEPWD_CODE+memberEmail;
                ValueOperations<String,Object> fetchCacheOperations=redisTemplate.opsForValue();
                Object resultObj=fetchCacheOperations.get(key);
                if(resultObj!=null){
                    String validatorCode=resultObj.toString();
                    LOGGER.info(loginName+"邮箱验证码："+validatorCode);
                    if(emailCode.toLowerCase().equals(validatorCode.toLowerCase())){
                        resultResponse.setSuccess(true);
                    }else{
                        resultResponse.setMessage("验证码不正确");
                    }
                }else{
                    resultResponse.setMessage("邮件验证码失效");
                }
            }else{
                resultResponse.setMessage("传递参数不正确");
            }
        }catch(BusinessException e){
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("邮箱验证码确认,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 重置密码
     * @param loginName
     * @param newPwd
     * @param confirmNewPwd
     * @return
     */
    @RequestMapping("modifyPwdAgain.htm")
    @ResponseBody
    @SystemControllerLog(operation = "重置密码", modelName = "忘记密码模块")
    public ResultResponse modifyPwdAgain(String loginName,String  newPwd,String confirmNewPwd) {
        ResultResponse resultResponse=new ResultResponse();
        try{
            if(StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(newPwd) && StringUtils.isNotBlank(confirmNewPwd)){
                if(newPwd.equals(confirmNewPwd)){
                    //更改密码
                    CompanyMembersBean updateMemberBean=new CompanyMembersBean();
                    updateMemberBean.setMemberLogname(loginName);
                    updateMemberBean.setMemberPassword(com.xtr.comm.util.StringUtils.getMD5two(newPwd));//密码加密
                    int count=companyMembersService.updateByCondition(updateMemberBean);
                    if(count<=0){
                        LOGGER.info("找回密码更新密码失败");
                        resultResponse.setMessage("找回密码更新密码失败");
                    }else{
                        resultResponse.setSuccess(true);
                    }
                }else{
                    LOGGER.info("找回密码，新密码与确认密码不一致");
                    resultResponse.setMessage("新密码与确认密码不一致");
                }
            }else{
                LOGGER.info("找回密码,传递的参数不能有空值");
                resultResponse.setMessage("找回密码,传递的参数不能有空值");
            }
        }catch(BusinessException e){
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("重置密码,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 通过手机号找回密码,验证验证码
     * @param emailCode
     * @return
     */
    @RequestMapping("checkVcodeimgByPhone.htm")
    @ResponseBody
    public ResultResponse checkVcodeimgByPhone(HttpServletRequest request,String emailCode,String memberPhone) {
        ResultResponse resultResponse=new ResultResponse();
        if(StringUtils.isNotBlank(emailCode) ) {
            String key = request.getSession().getId() + Constant.VCOD_EIMG;
            if (!checkVcodeimg(emailCode, key)) {//验证码不正确
                LOGGER.info("通过手机号找回密码,验证验证码,验证码不正确");
                resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
            } else {//验证码正确,发送短信
                resultResponse=sendPhoneMsgByRetriedPwd(memberPhone);
            }
        }else{
            LOGGER.info("通过手机号找回密码,验证验证码,传递的参数不能有空值");
            resultResponse.setMessage("请输入验证码");
        }
        LOGGER.info("通过手机号找回密码,验证验证码,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 通过手机号找回密码,发送短信
     * @param memberPhone
     * @return
     */
    private ResultResponse sendPhoneMsgByRetriedPwd(String memberPhone) {
        ResultResponse resultResponse = new ResultResponse();
        if (!StringUtils.isNotBlank(memberPhone)) {
            LOGGER.info("通过手机号找回密码,发送短信,手机号码不能为空");
            resultResponse.setMessage("手机号码不能为空");
            return resultResponse;
        }
        //验证手机号是否是注册用户,是否合法
        CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
        checkCompanyBean.setMemberPhone(memberPhone);
        List<CompanyMembersBean> companyMemberList=companyMembersService.selectCanLoginByPhone(checkCompanyBean);
        if (companyMemberList==null || companyMemberList.size()<=0) {
            LOGGER.info("通过手机号找回密码,发送短信,手机号不正确");
            resultResponse.setMessage("手机号不正确");
            return resultResponse;
        }
        //短信模板
        StationSmscontBean stationSmscontBean = stationSmscontService.selectBySmsType(StationCollaborationConstants.STATIONSMSCONT_TYPE_FIRST);
        if (stationSmscontBean == null) {
            LOGGER.info("通过手机号找回密码,发送短信,获取不到短信模板");
            resultResponse.setMessage(ErrorCodeEnum.SMS_TEMPLATE_ERROR.getMessage());
            return resultResponse;
        }
//        //生成随机数
//        Random random = new Random(System.currentTimeMillis());
//        String smscode = RandomNumber.getRandomNumberByLength(random, 6);

        try{
            //该手机号当日已发验证短信数
            long smsNum = stationSmsRecordsService.getCountByCondition(memberPhone, DateUtil.formatCurrentDate());

            if (smsNum + 1 > Constant.SMS_LIMIT) {
                LOGGER.info("通过手机号找回密码,发送短信,超过当天发信息上限");
                resultResponse.setMessage(ErrorCodeEnum.SMS_OVER_LIMIT.getMessage());
                return resultResponse;
            }

            String verificationCode = RandomDataUtil.getRandomID(6);
            String paraSmsCode = PropertyUtils.getString("environ.istest");    //短信验证码生成环境，如果是test测试环境，永远是111111
            if ("1".equals(paraSmsCode)){
                verificationCode= "111111";
            }

            LOGGER.info("通过手机号找回密码,发送短信验证码：" + verificationCode);
            //短信内容,用随机数替代验证码
            String smsCont = stationSmscontBean.getSmsCont() == null ? "" : stationSmscontBean.getSmsCont();
            smsCont = smsCont.replace("[变量1]", verificationCode);
//                SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(memberPhone, "验证码：" + verificationCode);
            SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(memberPhone, smsCont);
            LOGGER.info("通过手机号找回密码,发送短信,返回结果:"+JSON.toJSONString(sendMsgResponse));
            if (!sendMsgResponse.isSuccess()) {
                LOGGER.info("通过手机号找回密码,发送短信,"+sendMsgResponse.getError());
                resultResponse.setMessage(sendMsgResponse.getError());
            } else {
                // 写入短信记录
                StationSmsRecordsBean stationSmsRecordsBean = new StationSmsRecordsBean();
                stationSmsRecordsBean.setRecordPhone(memberPhone);
                stationSmsRecordsBean.setRecordType(StationSmsRecordsTypeEnum.MANAGER.getCode());
                stationSmsRecordsBean.setRecordCont(smsCont);
                stationSmsRecordsBean.setRecordAddtime(new Date());
                stationSmsRecordsBean.setRecordMbId(stationSmscontBean.getSmsId());
                stationSmsRecordsBean.setRecordUserId(companyMemberList.get(0).getMemberId());
                stationSmsRecordsService.insert(stationSmsRecordsBean);
                //将验证码放入缓存服务器 --只保留一分钟
                redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.verification.code") + memberPhone, verificationCode, 600 , TimeUnit.SECONDS);
                resultResponse.setSuccess(true);
            }
        }catch(Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

    /**
     * 通过手机号找回密码,更改密码
     * @param companyMembersBean
     * @param phoneCode
     * @return
     */
    @RequestMapping("modifyPwdByPhone.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更改密码", modelName = "忘记密码模块")
    public ResultResponse modifyPwdByPhone(CompanyMembersBean companyMembersBean,String phoneCode) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("通过手机号找回密码,更改密码,传递参数："+ JSON.toJSONString(companyMembersBean)+",手机验证码:"+phoneCode);
        try{
            if(companyMembersBean!=null && StringUtils.isNotBlank(companyMembersBean.getMemberPhone())
                    && StringUtils.isNotBlank(companyMembersBean.getMemberPassword())
                    && StringUtils.isNotBlank(companyMembersBean.getConfirmMemberPassword())
                    && StringUtils.isNotBlank(phoneCode)){
                if(companyMembersBean.getMemberPassword().equals(companyMembersBean.getConfirmMemberPassword())){
                    //验证手机验证码
                    Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + companyMembersBean.getMemberPhone());
                    if (code != null && StringUtils.equals(phoneCode.toLowerCase(), String.valueOf(code).toLowerCase())) {
                        //更改密码
                        CompanyMembersBean updateMemberBean=new CompanyMembersBean();
                        updateMemberBean.setMemberPhone(companyMembersBean.getMemberPhone());
                        updateMemberBean.setMemberPassword(com.xtr.comm.util.StringUtils.getMD5two(companyMembersBean.getMemberPassword()));//密码加密
                        int count=companyMembersService.updateByPhone(updateMemberBean);
                        if(count<=0){
                            LOGGER.info("通过手机找回密码更新密码失败");
                            resultResponse.setMessage("通过手机找回密码更新密码失败");
                        }else{
                            resultResponse.setSuccess(true);
                        }
                    }else{
                        LOGGER.info("通过手机号找回密码,更改密码,手机验证码不正确");
                        resultResponse.setMessage("手机验证码不正确");
                    }
                }else{
                    LOGGER.info("通过手机号找回密码,更改密码,新密码与确认密码不一致");
                    resultResponse.setMessage("新密码与确认密码不一致");
                }
            }else{
                LOGGER.info("通过手机号找回密码,更改密码,传递的参数不能有空值");
                resultResponse.setMessage("传递的参数不能有空值");
            }
        }catch(BusinessException e){
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("通过手机号找回密码,更改密码,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }
}
