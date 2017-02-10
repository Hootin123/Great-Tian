package com.xtr.company.controller.login;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyLogBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.service.company.CompanyLogService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.enums.CompanyMeMemberSignEnum;
import com.xtr.comm.enums.ErrorCodeEnum;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import com.xtr.company.util.SessionUtils;
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
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abiao on 2016/6/28.
 */
@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyLogService companyLogService;

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping({"loginInit.htm"})
    public ModelAndView loginInit(ModelAndView mav, HttpServletRequest request, CompanyMembersBean companyMembersBean) {
        mav.setViewName("xtr/login/login");
        SessionUtils.removeUser(request);
        return mav;
    }

    /*
    * <p>商户账户登录</p>
    * @auther 何成彪
    * @createTime2016/7/1 10:01
    */
    @RequestMapping(value = "login.htm")
    @ResponseBody
    public ResultResponse login(HttpServletRequest request, ModelAndView mav, CompanyMembersBean companyMembersBean) {

        ResultResponse resultResponse = new ResultResponse();
        String memberLogname = companyMembersBean.getMemberLogname();


        try {
            String verifyCode = (String) request.getSession().getAttribute("verifyCode");
            if (StringUtils.isEmpty(verifyCode) || companyMembersBean == null || StringUtils.isEmpty(companyMembersBean.getVerifycode())) {
//                LOGGER.error("验证码不正确：verifyCode=" + verifyCode + ",getVerifycode=" + companyMembersBean.getVerifycode().toLowerCase());
                LOGGER.error("验证码不正确：verifyCode、getVerifycode为null");
                resultResponse.setMsgCode("0");
                resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
                return resultResponse;

            }

            if (!verifyCode.toLowerCase().equals(companyMembersBean.getVerifycode().toLowerCase())) {
                LOGGER.error("验证码不正确：verifyCode=" + verifyCode + ",getVerifycode=" + companyMembersBean.getVerifycode().toLowerCase());
                resultResponse.setMsgCode("0");
                resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
                return resultResponse;
            }
            //邮箱规则验证
  /*      if(!ValidatorUtils.isEmail(memberLogname)){
            resultResponse.setMessage(ErrorCodeEnum.EMAIL_FORMAT_ERROR.getMessage());
            return resultResponse;
        }*/
            //判断账户是否存在
            CompanyMembersBean resultBean = companyMembersService.findByMemberLogname(memberLogname);
            if (resultBean == null) {
                resultResponse.setMsgCode("no-register");//表示未注册
                resultResponse.setMessage(ErrorCodeEnum.MEMBER_LOGNAME_NOT_EXIST.getMessage());
                return resultResponse;
            } else {
                int memberSign = resultBean.getMemberSign();
                if(resultBean.getMemberStatus()!=null){
                    int member_status=resultBean.getMemberStatus();
                    if (member_status ==2) {
                        resultResponse.setMessage("该用户已关闭");
                        return resultResponse;
                    }
                    if (member_status ==3) {
                        resultResponse.setMessage(ErrorCodeEnum.MEMBER_LOGNAME_NOT_EXIST.getMessage());
                        return resultResponse;
                    }
                }
                if (memberSign == CompanyMeMemberSignEnum.UNACTIVE.getCode()) {
                    resultResponse.setMessage(ErrorCodeEnum.UNACTIVE.getMessage());
                    return resultResponse;
                }
                //密码校验
                String resultMemberPassword = resultBean.getMemberPassword();
                String memberPhone = resultBean.getMemberPhone();
                String memberPassword = companyMembersBean.getMemberPassword();
                if (!resultMemberPassword.equals(StringUtils.getMD5two(memberPassword).toLowerCase())) {
                    resultResponse.setMessage(ErrorCodeEnum.MEMBER_LOGNAME_OR_PASSWORD_ERROR.getMessage());
                    return resultResponse;
                } else {
                    Long memberCompanyId = resultBean.getMemberCompanyId();
                    CompanysBean obj = new CompanysBean();
                    if (memberCompanyId == null || StringUtils.isEmpty(memberCompanyId)) {
                        obj = null;
                    } else {
                        ResultResponse resultResponseCompanys = companysService.selectCompanyInfoDetail(memberCompanyId);
                        if (!resultResponseCompanys.isSuccess()) {
                            resultResponse.setMessage(resultResponseCompanys.getMessage());
                            return resultResponse;
                        } else {
                            obj = (CompanysBean) resultResponseCompanys.getData();
                        }
                    }
                    //页面显示用户姓名
                    String userName = resultBean.getMemberName();
                    if(StringUtils.isEmpty(userName)) {
                        userName = resultBean.getMemberLogname();
                    }
                    request.getSession().setAttribute("memberLogname", memberLogname);
                    request.getSession().setAttribute("userName", userName);
                    request.getSession().setAttribute("memberid", resultBean.getMemberId());
                    request.getSession().setAttribute("memberPhone", resultBean.getMemberPhone());//需要手机号码
                    request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);
                    request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean);
                    request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, obj);

                    //插入日志
                    //*========数据库日志=========*//
                    CompanyLogBean sysLogBean = new CompanyLogBean();
                    //所属模块
                    sysLogBean.setModelName("登录");
                    //用户所做的操作
                    sysLogBean.setOperation("登录");
                    //日志类型  0：操作日志 1：异常日志
                    sysLogBean.setType(Integer.valueOf(0));
                    //请求Ip
                    sysLogBean.setRequestIp(request.getRemoteAddr());
                    //服务器名称
                    sysLogBean.setServerName(InetAddress.getLocalHost().getHostName());
                    //操作人
                    sysLogBean.setUserId(resultBean.getMemberId());
                    //创建时间
                    sysLogBean.setCreateTime(new Date());
                    companyLogService.insert(sysLogBean);

                    //如果是邮箱输入则设置状态码1为邮箱

                    if(memberLogname.contains("@")){

                        resultResponse.setMsgCode("1");//默认设置状态码为1时
                    }
                    resultResponse.setSuccess(true);


            }



            }
        } catch (Exception e) {
            resultResponse.setMessage("通讯异常，请稍后再试");
            LOGGER.error(e.getMessage(), e);
        }

        return resultResponse;
    }


    /**
     * 图形验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "generateImages.htm")
    public void authImages(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int w = 160, h = 34;
        String key = request.getSession().getId() + Constant.VCOD_EIMG;

        String verifyCode = VerifyCodeUtils.outputVerifyImage(w, h, response.getOutputStream(), 4);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, verifyCode);
        request.getSession().setAttribute("verifyCode", verifyCode);
    }

    /*
    * <p>图形验证码判断</p>
    * @auther 徐龙
    * @createTime2016/8/2 10:01
    */
    @RequestMapping(value = "getCheckCode.htm")
    @ResponseBody
    public ResultResponse checkCode(HttpServletRequest request, @RequestParam("vcodeimg") String vcodeimg) {
        ResultResponse resultResponse = new ResultResponse();
        String verifyCode = (String) request.getSession().getAttribute("verifyCode");
        if (StringUtils.isEmpty(verifyCode) || !verifyCode.toLowerCase().equals(vcodeimg.toLowerCase())) {
            resultResponse.setSuccess(false);
        }else{
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * <p>登录用户为邮箱时，跳转到默认修改登录用户为手机号</p>
     * @auther zhangshuai
     *
     * @createTime2016/8/3
     *
     * @return
     */
    @RequestMapping(value = "loginChangeLoginName.htm")
    public  ModelAndView loginChangeLoginName(ModelAndView mv,HttpServletRequest request){
        mv.setViewName("xtr/login/loginNameChangePhone");//返回到修改登录名为邮箱的页面
        return mv;
    }


    /**
     * <p>登录：修改登录名为手机号进行登录  然后跳转到登录页面 </p>
     * @auther zhangshuai
     *
     * @createTime2016/8/3
     *
     * @return
     */
    @RequestMapping(value = "loginChangeLoginNameToPhone.htm")
    @ResponseBody
    @SystemControllerLog(operation = "修改登录名为手机号", modelName = "用户登录")
    public ResultResponse  loginChangeLoginNameToPhone(HttpServletRequest request, ModelAndView mav){
       ResultResponse resultResponse = new ResultResponse();
       //获取登录名  手机号码
        try{

            String memberPhone = request.getParameter("memberPhone");
            long  memberid =  Long.valueOf(request.getParameter("memberId"));
            CompanyMembersBean co = new CompanyMembersBean();
            co.setMemberPhone(memberPhone);//电话号码
            co.setMemberLogname(memberPhone);//登录名
            co.setMemberId(memberid);//设置主键
            //获取当前的标示
            String code = request.getParameter("code");//ajax 携带过来
            if ("200".equals(code)||"201".equals(code)) {
                //更新
                //登录
                int a = companyMembersService.updateByPrimaryKeySelective(co);
                resultResponse.setSuccess(true);//直接js跳转到home页面
                request.getSession().setAttribute("memberLogname", memberPhone);
                request.getSession().setAttribute("memberPhone", memberPhone);
                request.getSession().setAttribute("memberid", memberid);
                CompanyMembersBean resultBean =(CompanyMembersBean) request.getSession().getAttribute(CommonConstants.LOGIN_USER_KEY);
                resultBean.setMemberLogname(memberPhone);
                request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);//重新设置session
                request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean); //重新设置session
            }
        }catch(Exception e){
            resultResponse.setMessage("通讯异常，请稍后再试");
            LOGGER.error(e.getMessage(), e);
        }

     return resultResponse;
   }

    /**
     * 校验当前手机号是否已经被使用了
     * @return
     */
    @RequestMapping(value="checkPhoneHasUsed.htm")
    @ResponseBody
    public  Map checkPhoneByUsed(HttpServletRequest request){

        Map map = new HashMap();
        //获取memberid
        long  memberid = (Long) request.getSession().getAttribute("memberid");
        String memberPhone = request.getParameter("memberPhone");

        /**根据手机号作为member_logname 跟 member_phone 进行查询  如果存在判断其memberid是否相同  如果不同则说明手机号已被使用  相同或者没查询到则进行更新
         相同时进行更新2个字段    没查询到则根据memberid查询 进行更新即可
         **/
        CompanyMembersBean com = new CompanyMembersBean();
        com.setMemberPhone(memberPhone);
        //改成list
        List<CompanyMembersBean> list = companyMembersService.selectByPhoneOrLogname(com);
        //判断是否为空
        if(list==null||list.size()==0){
            //该手机号未被使用
            map.put("success",true);
            map.put("code","200");//说明当前手机号未被使用
            map.put("memberid",memberid);


        }else{
                CompanyMembersBean companyMembersBean = list.get(0);//默认获取第一条
                if (memberid == companyMembersBean.getMemberId()) {
                    map.put("success", true);
                    map.put("code", "201");//说明当前手机号使用了 且是用户自己的
                    map.put("memberid", memberid);

                } else {
                    //说明手机号已被使用
                    map.put("success", false);
                    map.put("code", "400");//手机号已被使用
                    map.put("memberid", memberid);

                }
            }

        return map;
    }






}
