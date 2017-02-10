package com.xtr.company.controller.login;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.api.service.station.StationSmsRecordsService;
import com.xtr.api.service.station.StationSmscontService;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.enums.CompanyMeMemberIsdefaultEnum;
import com.xtr.comm.enums.CompanyMeMemberSignEnum;
import com.xtr.comm.enums.ErrorCodeEnum;
import com.xtr.comm.enums.StationSmsRecordsTypeEnum;
import com.xtr.comm.jd.util.*;
import com.xtr.comm.util.*;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
 * Created by abiao on 2016/6/23.
 */
@Controller
public class RegisterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StationSmscontService stationSmscontService;
    @Resource
    private StationSmsRecordsService stationSmsRecordsService;

    @Resource
    private StationCollaborationService stationCollaborationService;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    /*
    * <p>方法说明</p>
    * @auther 何成彪
    * @createTime2016/6/30 19:03
    */
    @RequestMapping(value = "registerInit.htm")
    public ModelAndView getRegister(ModelAndView model) {
        model.setViewName("xtr/login/register");
        return model;
    }

    /*
    * <p>注册跳转激活页面</p>
    * @auther 何成彪
    * @createTime2016/6/30 19:03
    */
    @RequestMapping(value = "activateInfo.htm")
    @ResponseBody
    public ModelAndView getActivateInfo(HttpServletRequest request,ModelAndView mav, String email, String phone) {

        String emailLogPage = Constant.PRE_EMAIL + email.substring(email.indexOf("@") + 1);
        String phoneShow = phone;
        StringBuilder sb = new StringBuilder(phoneShow);
        if(!com.xtr.comm.jd.util.StringUtils.isBlank(phoneShow)){
            if (phoneShow.length()>4){
                if (phoneShow.length() == 5){
                    phoneShow = sb.replace(1, 5, "****").toString();
                }else if (phoneShow.length() == 6){
                    phoneShow = sb.replace(2, 6, "****").toString();
                }else if (phoneShow.length() > 6){
                    int intStart = 0;
                    int intEnd = 0;
                    intStart = (phoneShow.length()-4) / 2;
                    intEnd = intStart + 4;
                    phoneShow = sb.replace(intStart, intEnd, "****").toString();
                }
            }
        }
        mav.setViewName("xtr/login/activateInfo");
        mav.addObject("email", email);
        mav.addObject("phone", phone);
        mav.addObject("phoneShow", phoneShow);
        mav.addObject("emailLogPage", "loginInit.htm");

        ResultResponse resultResponse = new ResultResponse();
        try {

                CompanyMembersBean resultCompanyMembersBean = companyMembersService.findByMemberLogname(phone);
                if (resultCompanyMembersBean != null) {

                    Long memberCompanyId = resultCompanyMembersBean.getMemberCompanyId();
                    CompanysBean obj;
                    if (memberCompanyId == null || StringUtils.isEmpty(memberCompanyId)) {
                        obj = null;
                    } else {
                        ResultResponse resultResponseCompanys = companysService.selectCompanyInfoDetail(memberCompanyId);
                        if (!resultResponseCompanys.isSuccess()) {
                            resultResponse.setMessage(resultResponseCompanys.getMessage());
                            //return resultResponse;
                            obj = null;
                        } else {
                            obj = (CompanysBean) resultResponseCompanys.getData();
                            obj.setCompanyAddime(new Date());
                        }
                    }

                    request.getSession().setAttribute("memberLogname", phone);
                    request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultCompanyMembersBean);
                    request.getSession().setAttribute(CommonConstants.SESSION_USER, resultCompanyMembersBean);
                    request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, obj);

                    mav.addObject("emailLogPage", "/home.htm");
                    return mav;
                } else {
                    LOGGER.info("客户信息未找到phone=" + phone);
                }

        } catch (Exception ex) {
            String errorMsg = ex.toString();
            LOGGER.info("客户信息异常phone=" + phone);
            return mav;
        }
        return mav;
    }

    /*
    * <p>商户注册</p>
    * @auther 徐龙
    * @createTime2016/8/3 11:47
    */
    @RequestMapping(value = "registerByPhone.htm")
    @ResponseBody
    public void registerphone(HttpServletRequest request, HttpServletResponse response, CompanyMembersBean companyMembersBean) {
        LOGGER.info("company register:>>>>>>>>>>>>>>>>>>>====" + companyMembersBean);
        ResultResponse resultResponse = new ResultResponse();
        String memberLogname = companyMembersBean.getMemberPhone();
        List<CompanyMembersBean> resultBean = companyMembersService.findByMemberPhone(memberLogname);
        if (resultBean.size()==0) {
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setSuccess(false);
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /*
    * <p>商户注册</p>
    * @auther 何成彪
    * @createTime2016/7/4 11:47
    */
    @RequestMapping(value = "register.htm")
    @ResponseBody
    public void register(HttpServletRequest request, HttpServletResponse response, CompanyMembersBean companyMembersBean) {
        LOGGER.info("company register:>>>>>>>>>>>>>>>>>>>====" + companyMembersBean);
        String memberName =companyMembersBean.getMemberLogname();
        ResultResponse resultResponse = new ResultResponse();
        String memberPassword = companyMembersBean.getMemberPassword();
        String memberLogname = companyMembersBean.getMemberPhone();
        String companyName = companyMembersBean.getCompanyName();
        String confirmMemberPassword = companyMembersBean.getConfirmMemberPassword();
        String memberPowerNames = companyMembersBean.getMemberPowerNames();

        if (!memberPassword.equals(confirmMemberPassword)) {
            resultResponse.setMessage(ErrorCodeEnum.CONFIRMPASSWORD_ERROR.getMessage());
            HtmlUtil.writerJson(response, resultResponse);
            return;
        }
  /*      //验证码校验
        String vcodeimg = companyMembersBean.getVcodeimg();
        //获取缓存服务器上验证码
        String key = request.getSession().getId()+Constant.VCOD_EIMG;
        if(!checkVcodeimg(vcodeimg,key)){
            resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
            Random random=new Random(System.currentTimeMillis());
            return resultResponse;
        }*/
        //加密处理
        String processMemberPassword = StringUtils.getMD5two(memberPassword);
        companyMembersBean.setMemberPassword(processMemberPassword);
        List<CompanyMembersBean> resultBean = companyMembersService.findByMemberPhone(memberLogname);
        if (resultBean.size()==0) {
            //插入企业信息
            CompanysBean companysBean = new CompanysBean();
            companysBean.setCompanyName(companyName);
            companysBean.setCompanyContactTel(memberLogname);
            companysBean.setCompanyContactName(companyMembersBean.getMemberLogname());
            companysBean.setCompanyContactPlace(memberPowerNames);
            companysBean.setCompanyAddime(new Date());
            Long companyId = companysService.insertByCompanyId(companysBean);
            if(companyId==null || companyId<=0){
                resultResponse.setMessage("插入企业信息失败");
                HtmlUtil.writerJson(response, resultResponse);
                return;
            }
            companysBean.setCompanyId(companyId);
            LOGGER.info("注册企业,插入企业信息后返回结果:" + companyId);


            //companyMembersBean.setMemberSign(CompanyMeMemberSignEnum.UNACTIVE.getCode());
            companyMembersBean.setMemberIsdefault(CompanyMeMemberIsdefaultEnum.IS_MANAGER.getCode());
            companyMembersBean.setRegisterTime(new Date());
            companyMembersBean.setMemberName(memberName);
            if (memberLogname.contains("@")){
                companyMembersBean.setMemberEmail(memberLogname);
            }
            else{
                companyMembersBean.setMemberLogname(memberLogname);
            }
            companyMembersBean.setMemberCompanyId(companysBean.getCompanyId());
            companyMembersBean.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
            companyMembersBean.setMemberStatus(1);
            int result = companyMembersService.insert(companyMembersBean);

            // 注册成功发红包
            redEnvelopeService.sendRegisterEnvelope(companyId);

            LOGGER.info("result:>>>>>>" + result);
            //新注册用户时,发起合作意向开始
            try{
                if(companysBean!=null){
                    companysBean.setCompanyCorporationPhone(companyMembersBean.getMemberPhone());
                }
                ResultResponse collaborationResponse=stationCollaborationService.updateOpeationType(companysBean, StationCollaborationConstants.STATIONCOLLABRATION_TYPE_NEWUSER);
                LOGGER.info("新用户注册发起合作意向,返回结果:"+JSON.toJSONString(collaborationResponse));
                if(!collaborationResponse.isSuccess()){
                    resultResponse.setMessage(collaborationResponse.getMessage());
                    HtmlUtil.writerJson(response, resultResponse);
                    return;
                }
            }catch(Exception e){
                resultResponse.setMessage(e.getMessage());
                LOGGER.error("新用户注册发起合作意向,返回结果:"+JSON.toJSONString(resultResponse));
                HtmlUtil.writerJson(response, resultResponse);
                return;
            }
            //新注册用户时,发起合作意向结束


//            try {
//                ResultResponse resultResponseCompanys = companysService.selectCompanyByCompanyNumber(memberLogname);
//                if(!resultResponseCompanys.isSuccess()){
//                    //resultResponse.setMessage(resultResponseCompanys.getMessage());
//                    LOGGER.info("获取企业Id失败:>>>>>>"+memberLogname);
//                }
//                else {
//                    CompanysBean obj = (CompanysBean)resultResponseCompanys.getData();
//                    Long companyId = obj.getCompanyId();
//
//                    companyMembersBean.setMemberCompanyId(companyId);
//                    companyMembersService.updateByCondition(companyMembersBean);
//                }
//            }
//            catch (Exception e) {
//                //resultResponse.setMessage("通讯异常，请稍后再试");
//                LOGGER.error(e.getMessage(), e);
//            }


            resultResponse.setSuccess(true);
            resultResponse.setData(companyMembersBean);
        } else {
            resultResponse.setSuccess(false);
        }
//        //激活邮件发送
//        String host = PropertyUtils.getString("email.host");
//        String servername = PropertyUtils.getString("email.servername");
//        String serverpwd = PropertyUtils.getString("email.serverpwd");
//        String memgerPhone = companyMembersBean.getMemberPhone();
//
//        Random random = new Random(System.currentTimeMillis());
//        String smscode = RandomNumber.getRandomNumberByLength(random, 30);
//
//        //邮件内容
//        StringBuffer sb = new StringBuffer(
//                "你好!\n" +
//                        "感谢你注册薪太软企业账号，薪太软为你提供一站式薪酬福利解决方案，包括：代发工资、垫发工资、代缴社保公积金等服务。\n" +
//                        "你的登录邮箱为：" + memberLogname + "。请点击以下链接激活账号：\n");
//        String basePath = PropertyUtils.getString("basePath");
//        sb.append("<a href='" + basePath + "/activeRegister.htm?activeCode=");
//
//        sb.append(memberLogname + memgerPhone + smscode);
//        sb.append("&memberLogname=" + memberLogname);
//        sb.append("'>");
//        sb.append(memberLogname + memgerPhone + smscode);
//        sb.append("</a><br/>\r\n<img src='http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/fbe293d0-9e8e-4148-b752-d7ccc3ef30c9.png'/>"
//        );
//        boolean ret = EmailUtils.sendMail(host, servername, serverpwd, memberLogname, sb.toString(), "薪太软验证码", 0);
//        //激活码存入缓存服务器
//        String key = memberLogname + memgerPhone + smscode;
//        LOGGER.info("result5:>>>>>>");
//        generateRedisValue(key, memberLogname + memgerPhone + smscode);
//        LOGGER.info("result3:>>>>>>");
        HtmlUtil.writerJson(response, resultResponse);
//        LOGGER.info("result4:>>>>>>");
    }

    /*
    * <p>重新发送邮件</p>
    * @auther 徐龙
    * @createTime2016/7/22 11:47
    */
    @RequestMapping(value = "againregister.htm")
    @ResponseBody
    public ResultResponse againregister(HttpServletRequest request, HttpServletResponse response, @RequestParam("memberLogname") String memberLogname, @RequestParam("memgerPhone") String memgerPhone) {
        ResultResponse resultResponse = new ResultResponse();
        //激活邮件发送
        String host = PropertyUtils.getString("email.host");
        String servername = PropertyUtils.getString("email.servername");
        String serverpwd = PropertyUtils.getString("email.serverpwd");

        Random random = new Random(System.currentTimeMillis());
        String smscode = RandomNumber.getRandomNumberByLength(random, 30);
        //邮件内容
        StringBuffer sb = new StringBuffer(
                "你好!\n" +
                        "感谢你注册薪太软企业账号，薪太软为你提供一站式薪酬福利解决方案，包括：代发工资、垫发工资、代缴社保公积金等服务。\n" +
                        "你的登录邮箱为：" + memberLogname + "。请点击以下链接激活账号：\n");
        String basePath = PropertyUtils.getString("basePath");
        sb.append("<a href='" + basePath + "/activeRegister.htm?activeCode=");

        sb.append(memberLogname + memgerPhone + smscode);
        sb.append("&memberLogname=" + memberLogname);
        sb.append("'>");
        sb.append(memberLogname + memgerPhone + smscode);
        sb.append("</a><br/>\r\n<img src='http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/fbe293d0-9e8e-4148-b752-d7ccc3ef30c9.png'/>"
        );
        boolean ret = EmailUtils.sendMail(host, servername, serverpwd, memberLogname, sb.toString(), "薪太软验证码", 0);
        //激活码存入缓存服务器
        String key = memberLogname + memgerPhone + smscode;
        LOGGER.info("result5:>>>>>>");
        generateRedisValue(key, memberLogname + memgerPhone + smscode);
        LOGGER.info("result3:>>>>>>");
        resultResponse.setSuccess(true);
        HtmlUtil.writerJson(response, resultResponse);
        LOGGER.info("result4:>>>>>>");
        return resultResponse;
    }

    /*
    * <p>注册邮箱激活</p>
    * @auther 何成彪
    * @createTime2016/7/12 16:08
    */
    @RequestMapping(value = "activeRegister.htm")
    @ResponseBody
    public ModelAndView activeRegister(HttpServletRequest request, ModelAndView mav, String memberLogname, String activeCode) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            ValueOperations<String, Object> fetchCacheOperations = redisTemplate.opsForValue();
            String value = (String) fetchCacheOperations.get(activeCode);
            if (activeCode.equals(value)) {
                CompanyMembersBean resultCompanyMembersBean = companyMembersService.findByMemberLogname(memberLogname);
                if (resultCompanyMembersBean != null) {
                    if(resultCompanyMembersBean.getMemberSign()==3){
                        resultResponse.setSuccess(true);
                        resultResponse.setData(memberLogname);
                        mav.setViewName("xtr/login/registerSuccess");
                        mav.addObject("memberLogname", memberLogname);
                        mav.addObject("memberTitle", "你的薪太软企业账号于"+memberLogname+"被激活，已可登录使用！");
                        mav.addObject("memberUrl", "loginInit.htm");
                        return mav;
                    }
                    //注册时间
                    Date registerTime = resultCompanyMembersBean.getRegisterTime();
                    if (isOvertime(DateUtil.formatDateTime("yyyy-MM-dd hh:mm:ss", new Date()), DateUtil.formatDateTime("yyyy-MM-dd hh:mm:ss", registerTime))) {
                        mav.setViewName("xtr/login/register");
                        String errorMsg = ErrorCodeEnum.REGISTERACTIVE_OVERTIME.getMessage();
                        mav.addObject("errorMsg", errorMsg);
                        return mav;
                    }

                    resultCompanyMembersBean.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
                    companyMembersService.updateByCondition(resultCompanyMembersBean);

                    Long memberCompanyId = resultCompanyMembersBean.getMemberCompanyId();
                    CompanysBean obj;
                    if (memberCompanyId == null || StringUtils.isEmpty(memberCompanyId)) {
                        obj = null;
                    } else {
                        ResultResponse resultResponseCompanys = companysService.selectCompanyInfoDetail(memberCompanyId);
                        if (!resultResponseCompanys.isSuccess()) {
                            resultResponse.setMessage(resultResponseCompanys.getMessage());
                            //return resultResponse;
                            obj = null;
                        } else {
                            obj = (CompanysBean) resultResponseCompanys.getData();
                            obj.setCompanyAddime(new Date());
                        }
                    }

                    request.getSession().setAttribute("memberLogname", memberLogname);
                    request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultCompanyMembersBean);
                    request.getSession().setAttribute(CommonConstants.SESSION_USER, resultCompanyMembersBean);
                    request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, obj);

                    resultResponse.setSuccess(true);
                    resultResponse.setData(memberLogname);
                    mav.setViewName("xtr/login/registerSuccess");
                    mav.addObject("memberLogname", memberLogname);
                    mav.addObject("memberTitle", "恭喜你的薪太软企业账号注册成功！");
                    mav.addObject("memberUrl", "home.htm");
                    return mav;
                } else {
                    mav.setViewName("xtr/login/register");
                    String errorMsg = "激活失败";
                    mav.addObject("errorMsg", errorMsg);
                    return mav;

                }
            } else {
                mav.setViewName("xtr/login/register");
                String errorMsg = ErrorCodeEnum.ACTIVECODE_ERROR.getMessage();
                mav.addObject("errorMsg", errorMsg);
                return mav;
            }
        } catch (Exception ex) {
            mav.setViewName("xtr/login/register");
            String errorMsg = ex.toString();
            mav.addObject("errorMsg", errorMsg);
            return mav;
        }


        //return mav;
    }

    /*
    * <p>注册手机验证码短息发送</p>
    * @auther 何成彪
    * @createTime2016/6/26 11:57
    */
    @RequestMapping(value = "sendMShortMessageCompanyRegist.htm")
    @ResponseBody
    public ResultResponse sendMShortMessageCompanyRegist(ModelAndView mav, CompanyMembersBean companyMembersBean) {
        ResultResponse resultResponse = new ResultResponse();
        String memberPhone = companyMembersBean.getMemberPhone();
        //电话号码验证
        if (checkPhoneByUsed(memberPhone)) {
            resultResponse.setMessage(ErrorCodeEnum.PHONE_USED_BY_MEMBER_LOGNAME.getMessage());
            return resultResponse;
        }
        //短信模板
//        StationSmscontBean stationSmscontBean = stationSmscontService.selectByPrimaryKey(1);
        StationSmscontBean stationSmscontBean = stationSmscontService.selectBySmsType(StationCollaborationConstants.STATIONSMSCONT_TYPE_FIRST);
        if (stationSmscontBean == null) {
            resultResponse.setMessage(ErrorCodeEnum.SMS_TEMPLATE_ERROR.getMessage());
            return resultResponse;
        }
        //生成随机数
        Random random = new Random(System.currentTimeMillis());
        String smsCont = stationSmscontBean.getSmsCont() == null ? "" : stationSmscontBean.getSmsCont();
        String smscode = RandomNumber.getRandomNumberByLength(random, 6);

        String paraSmsCode = PropertyUtils.getString("environ.istest");    //短信验证码生成环境，如果是test测试环境，永远是111111
        if ("1".equals(paraSmsCode)){
            smscode= "111111";
        }
        LOGGER.info("smscode:>>>>>>" + smscode);
        //短信内容
        smsCont = smsCont.replace("[变量1]", smscode);
        //该手机号当日已发验证短信数
        long smsNum = stationSmsRecordsService.getCountByCondition(memberPhone, DateUtil.formatCurrentDate());

        if (smsNum + 1 > Constant.SMS_LIMIT) {
            resultResponse.setMessage(ErrorCodeEnum.SMS_OVER_LIMIT.getMessage());
            return resultResponse;
        }
        // 写入短信记录
        StationSmsRecordsBean stationSmsRecordsBean = new StationSmsRecordsBean();
        stationSmsRecordsBean.setRecordPhone(memberPhone);
        stationSmsRecordsBean.setRecordType(StationSmsRecordsTypeEnum.USER.getCode());
        stationSmsRecordsBean.setRecordCont(smsCont);
        stationSmsRecordsBean.setRecordAddtime(new Date());
        stationSmsRecordsService.insert(stationSmsRecordsBean);
        //准备短信发送参数
        String url = PropertyUtils.getString("sms.url");
        Map<String, String> sendSmsParam = new HashMap<String, String>();
        sendSmsParam.put("usersource", "4");
        sendSmsParam.put("username", "xintairuan1");
        sendSmsParam.put("userpwd", "15ec96046f8d019f012416ff7ad775b2");
        sendSmsParam.put("usermobile", memberPhone);
        sendSmsParam.put("cont", smsCont);
        try {
            Map<String, Object> ret = HttpUtils.getUrlContentByPost(url, sendSmsParam);
            if (ret != null) {

                String res = ret.get("response").toString();
                try {
                    res = URLDecoder.decode(res, "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();

                List<Map<String, Object>> list = gson.fromJson(res, List.class);
                Map<String, Object> retmap = list.get(0);
                if (retmap != null && !retmap.isEmpty()) {
                    double sign = Double.parseDouble(retmap.get("sign").toString());
                    double start = Double.parseDouble(retmap.get("start").toString());
                    String datastring = retmap.get("datastring") == null ? "" : retmap.get("datastring").toString();
                    if (sign != 1 || start != 1 || !datastring.equals("1")) {
                        resultResponse.setMessage(ErrorCodeEnum.SMS_SEND_FAILURE.getMessage());
                        return resultResponse;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ValueOperations<String, Object> generateCacheOperations = redisTemplate.opsForValue();
        String memverLogname = companyMembersBean.getMemberLogname();
        String key = memverLogname + memberPhone;
        generateCacheOperations.set(key, smscode, 60, TimeUnit.SECONDS);
        resultResponse.setMessage(smscode);
        return resultResponse;
    }

    /*
    * <p>验证手机短信是否正确</p>
    * @auther 何成彪
    * @createTime2016/6/26 16:22
    */
    @RequestMapping(value = "checkMSMRandomNum.htm")
    @ResponseBody
    public ResultResponse checkMSMRandomNum(CompanyMembersBean companyMembersBean) {
        ResultResponse resultResponse = new ResultResponse();
        String smscode = companyMembersBean.getSmscode();
        String memberPhone = companyMembersBean.getMemberPhone();
        String memberLogname = companyMembersBean.getMemberLogname();
        ValueOperations<String, Object> fetchCacheOperations = redisTemplate.opsForValue();
        String fetchChcheValue = (String) fetchCacheOperations.get(memberLogname + memberPhone);
        if (smscode.equals(fetchChcheValue)) {
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage(ErrorCodeEnum.MSHORTMESSAGE_ERROR.getMessage());
        }
        return resultResponse;
    }

    /*
    * <p>缓存服务器上生成验证码</p>
    * @auther 何成彪
    * @createTime2016/7/3 17:15
    */
    void generateVcodeimg(String key, Random random) {
        ValueOperations<String, Object> fetchCacheOperations = redisTemplate.opsForValue();
        fetchCacheOperations.set(key, RandomNumber.getRandomNumberByLength(random, 4));
    }

    /*
    * <p>在缓存服务器上生成数据</p>
    * @auther 何成彪
    * @createTime2016/7/12 13:17
    */
    void generateRedisValue(String key, String value) {
        ValueOperations<String, Object> fetchCacheOperations = redisTemplate.opsForValue();
        fetchCacheOperations.set(key, value);

    }

    /**
     * 图形验证码校验
     *
     * @param request
     * @param companyMembersBean
     */
    @RequestMapping(value = "checkVerifycode.htm")
    @ResponseBody
    public ResultResponse checkVerifycode(HttpServletRequest request, CompanyMembersBean companyMembersBean) {
        ResultResponse resultResponse = new ResultResponse();
        String verifycode = companyMembersBean.getVerifycode();
        String key = request.getSession().getId() + Constant.VCOD_EIMG;
        if (!checkVcodeimg(verifycode, key)) {
            resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        } else {
            resultResponse.setSuccess(true);
            return resultResponse;
        }
    }
    //TODO 图形验证码

    /**
     * 图形验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "authImages.htm")
    public void authImages(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int w = 200, h = 80;

        String verifyCode = VerifyCodeUtils.outputVerifyImage(w, h, response.getOutputStream(), 4);
        request.getSession().setAttribute("imgcode", verifyCode);
    }

    boolean checkVcodeimg(String parm, String key) {
        boolean vcodeimgFlag = false;
        ValueOperations<String, Object> fetchCacheOperations = redisTemplate.opsForValue();
        String value = (String) fetchCacheOperations.get(key);
        if ((!StringUtils.isEmpty(value)) && value.toLowerCase().equals(parm.toLowerCase())) {
            vcodeimgFlag = true;
        }
        else{
            LOGGER.error("验证码不正确：value=" + value + ",getVerifycode=" + parm.toLowerCase());
        }
        return vcodeimgFlag;
    }

    /*
    * <p>验证注册账户</p>
    * @auther 何成彪
    * @createTime2016/6/29 17:02
    */
    @RequestMapping(value = "checkMemberLogname.htm")
    @ResponseBody
    public ResultResponse checkMemberLogname(String memberLogname) {
        ResultResponse resultResponse = new ResultResponse();
        LOGGER.info("memberLogname:" + memberLogname);

        CompanyMembersBean com = new CompanyMembersBean();
        com.setMemberPhone(memberLogname);
        List<CompanyMembersBean>  hasCom = companyMembersService.selectByPhoneOrLogname(com);
        if(null!=hasCom && hasCom.size()>0){
            resultResponse.setMessage(ErrorCodeEnum.MEMBER_LOGNAME_USED.getMessage());
            resultResponse.setSuccess(false);
        }

//        CompanyMembersBean companyMembersBean = companyMembersService.findByMemberLogname(memberLogname);
//        if (companyMembersBean == null) {
//            resultResponse.setSuccess(true);
//            return resultResponse;
//        } else {
//            LOGGER.info("companyMembersBean:" + JSON.toJSONString(companyMembersBean));
//            int memberSign = companyMembersBean.getMemberSign();
//            if (memberSign == CompanyMeMemberSignEnum.UNACTIVE.getCode()) {
//                Date registerTime = companyMembersBean.getRegisterTime();
//                //账户注册但是没有激活并且超过时间限制
//                if (isOvertime(DateUtil.formatDateTime("yyyy-MM-dd hh:mm:ss", new Date()), DateUtil.formatDateTime("yyyy-MM-dd hh:mm:ss", registerTime))) {
//                    resultResponse.setMessage(CompanyMeMemberSignEnum.UNACTIVE_AND_OVERTIME.getMessage());
//                    resultResponse.setSuccess(true);
//
//                } else {
//                    resultResponse.setMessage(CompanyMeMemberSignEnum.UNACTIVE.getMessage());
//                }
//            }
//            resultResponse.setMessage(ErrorCodeEnum.MEMBER_LOGNAME_USED.getMessage());
//        }
        return resultResponse;
    }

    /*
    * <p>验证手机号是否已经被注册账户使用</p>
    * @auther 何成彪
    * @createTime2016/7/4 13:52
    */
    boolean checkPhoneByUsed(String memberPhone) {
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("member_phone", memberPhone);
        CompanyMembersBean resultMap = companyMembersService.findByCondition(par);
        return resultMap != null;
    }

    /*
    * <p>验证激活时候超时</p>
    * @auther 何成彪
    * @createTime2016/7/4 13:52
    */
    boolean isOvertime(String currenyTime, String registerTime) {
        Long diffSeconds = DateUtil.getDiffSecondsofTwoDate(currenyTime, registerTime);
        return diffSeconds >= Constant.REGISTER_DELAY_TIME;
    }
}
