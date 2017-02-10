package com.xtr.customer.controller.hongbao;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyActivityBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.h5wallet.HongbaoService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import com.xtr.customer.util.ParseReturnXmlUtils;
import com.xtr.customer.util.SessionUtils;
import com.xtr.wechat.util.WeChatRedPackUtil;
import com.xtr.wechat.util.WeChatSessionInfo;
import com.xtr.wechat.util.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * h5 微信红包
 *@author  zhansghuai
 * @date 2016/8/25.
 */

@RequestMapping("h5Hongbao")
@Controller
public class H5HongbaoController {


    private static final Logger LOGGER = LoggerFactory.getLogger(H5HongbaoController.class);

    @Resource
    private HongbaoService hongbaoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyMembersService companyMembersService;
    @Resource
    private IdGeneratorService idGeneratorService;

    @Autowired
    private WeChatUtil weChatUtil;

    @Autowired
    WeChatRedPackUtil weChatRedPackUtil;
    /**
     * 跳转至登录页面
     * @return
     */
    @RequestMapping(value="toHongbaoLoginPage.htm")
    public ModelAndView toLoginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("xtr/hongbao/hongbaoLogin");
        return  modelAndView;
    }

    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value="hongbaoLogin.htm")
    @ResponseBody
    public ResultResponse login(HttpServletRequest request,CompanyMembersBean companyMembersBean){
        ResultResponse resultResponse=new ResultResponse();
//        String key = request.getSession().getId() + Constant.VCOD_EIMG;
        try {
            resultResponse= hongbaoService.login(companyMembersBean);
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
     * 跳转至注册页面
     * @return
     */
    @RequestMapping(value="toHongbaoRegisterPage.htm")
    public ModelAndView toRegisterPage(ModelAndView modelAndView,HttpServletRequest request){
        WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);
        //获取注册来源
        String companyContactPlace =request.getParameter("registerFrom");//注册来源
        LOGGER.info("当前的注册来源：" +companyContactPlace);
        //获取地推编号  01~15
        if(!StringUtils.isStrNull(request.getParameter("pushNumber"))){
            modelAndView.addObject("pushMumber",request.getParameter("pushNumber"));
        }//地推编号
        //查询当前多少hr领取红包
        long  receiveCounts = companysService.selectActivityRedCounts();
        modelAndView.addObject("receiveCounts",receiveCounts);
        modelAndView.addObject("companyContactPlace",companyContactPlace);
        modelAndView.setViewName("xtr/hongbao/hongbaoRegister");
        return modelAndView;
    }

    /**
     * 用户注册
     * @return
     */
    @RequestMapping(value="hongbaoRegister.htm")
    @ResponseBody
   public ResultResponse register(HttpServletRequest request,CompanyMembersBean companyMembersBean,String registerFrom,String companyName,String phoneCode,String pushNumber){

        LOGGER.info("注册来源： "+registerFrom +"地推来源"+pushNumber);
        ResultResponse resultResponse=new ResultResponse();
        try {
            //获取手机号
            String phone = companyMembersBean.getMemberPhone();
            //公司名称
            String cname = companyMembersBean.getCompanyName();
            resultResponse= hongbaoService.hongbaoRegister(companyMembersBean, registerFrom,companyName, phoneCode,pushNumber);
            if(resultResponse.isSuccess()){
                CompanyMembersBean resultBean=(CompanyMembersBean)resultResponse.getData();
                //根据企业ID获取企业信息放入session中
                CompanysBean companyBean=companysService.selectCompanyByCompanyId(resultBean.getMemberCompanyId());
                request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);
                request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean);
                request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, companyBean);
                request.getSession().setAttribute("phone",phone);
                request.setAttribute("companyName",cname);
                request.getSession().setAttribute("registerFrom",registerFrom);
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
     * 发送注册手机验证码
     * @param memberPhone
     * @return
     */
    @RequestMapping(value="sendMsgByHongbao.htm")
    @ResponseBody
    public ResultResponse sendMsgByHongbao(String memberPhone,HttpServletRequest request) {

        ResultResponse resultResponse = new ResultResponse();
        try{
            resultResponse= hongbaoService.sendPhoneMsgByH5(memberPhone);
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
     * h5生成验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="generateH5HongbaoValidatorImages.htm")
    public void generateH5ValidatorImages(HttpServletRequest request, HttpServletResponse response) {
        try{
            String key = request.getSession().getId()+ Constant.VCOD_EIMG;
            //生成验证码
            //hongbaoService.generateH5ValidatorImages(key,response);
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
     * H5上传营业执照及上传公司logo
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("h5HongbaoUploadOrganizeImg.htm")
    @ResponseBody
    public String h5UploadOrganizeImg( @RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(multipartFile!=null){
                String fileName = multipartFile.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String[] filenames={"jpg", "png", "jpeg", "bmp"};
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
                //上传文件是否大于5M
                if(multipartFile.getSize()>(5*1024*1024)){
                    resultResponse.setMessage("上传文件大小不能超过5M");
                    resultResponse.setSuccess(false);
                    return JSON.toJSONString(resultResponse);
                }

                boolean result= AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
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
     * 查询上传文件名称
     * @param type
     * @return
     */
    @RequestMapping("queryHongbaoUploadFileName.htm")
    @ResponseBody
    public ResultResponse queryUploadFileName(String type) {
        ResultResponse resultResponse = new ResultResponse();
        String newFileName = UUID.randomUUID().toString() + "." + type;
        resultResponse.setData(newFileName);
        LOGGER.info("H5上传营业执照图片,返回图片名称："+ newFileName);
        return resultResponse;
    }



    /**
     * 跳转至忘记密码页面
     * @return
     */
    @RequestMapping(value="toHongbaoForgertPasswordPage.htm")
    public ModelAndView toForgetPassword(ModelAndView modelAndView) {
        modelAndView.setViewName("xtr/hongbao/hongbaoResetPassword");
        return modelAndView;
    }


    /**
     * 用户重置密码
     * @return
     */
    @RequestMapping(value="hongbaoResetPasswor.htm")
    @ResponseBody
    public ResultResponse resetPassword(HttpServletRequest request,String phoneNum,String phoneCode,String confirmPassword,String newPassword){

        ResultResponse resultResponse = new ResultResponse();
        //调用后台接口判断
        try{
        if(StringUtils.isStrNull(phoneNum)){
            resultResponse.setMessage("手机号不能为空");
            return resultResponse;
        }

            resultResponse = hongbaoService.resetPassword(phoneNum,phoneCode,confirmPassword,newPassword);


        }catch ( Exception e ){
          LOGGER.error("用户忘记密码操作出现异常错误 "+e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }

        return resultResponse;
    }



    /**
     * 跳转到完善资料页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toPerfectInformationPage.htm")
    public ModelAndView toPerfectInformationPage(ModelAndView modelAndView,HttpServletRequest request){

         //获得公司信息
         CompanysBean companysBean = SessionUtils.getCompany(request);
         companysBean =companysService.selectCompanyByCompanyId(companysBean.getCompanyId());
        //领取完善信息红包的人数
        long perfectInfoReceiveCounts = companysService.selectCountsOfPerfectInfo();
        modelAndView.addObject("companyName",companysBean.getCompanyName());//公司名称
        modelAndView.addObject("companyAddress",companysBean.getCompanyAddress());//公司地址
        if (StringUtils.isStrNull(companysBean.getCompanyLogo())) {
            modelAndView.addObject("companyLogoUrl", "http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/" + companysBean.getCompanyLogo());//公司logo
            modelAndView.addObject("companyOrganizationImgUrl", "http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/" + companysBean.getCompanyOrganizationImg());
            modelAndView.addObject("companyLogo","");
            modelAndView.addObject("companyOrganizationImg","");
        }else{
            modelAndView.addObject("companyLogoUrl", "http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/" + companysBean.getCompanyLogo());//公司logo
            modelAndView.addObject("companyOrganizationImgUrl", "http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/" + companysBean.getCompanyOrganizationImg());
            modelAndView.addObject("companyLogo",companysBean.getCompanyLogo());
            modelAndView.addObject("companyOrganizationImg",companysBean.getCompanyOrganizationImg());
        }
        modelAndView.addObject("companyNumber",companysBean.getCompanyNumber());
        modelAndView.addObject("companyCorporation",companysBean.getCompanyCorporation());//法人代表
        modelAndView.addObject("companyCorporationPhone",companysBean.getCompanyCorporationPhone());//联系电话
        modelAndView.addObject("companyScale",companysBean.getCompanyScale());//公司规模
        modelAndView.addObject("companyBelongIndustry",companysBean.getCompanyBelongIndustry());//公司所属行业

        //返回了解渠道的值
        String returnChannel = returnChannel(companysBean);
        modelAndView.addObject("companyChannel",returnChannel);//了解渠道
        modelAndView.addObject("counts",perfectInfoReceiveCounts);
        modelAndView.setViewName("xtr/hongbao/perfectInformation");
        return modelAndView;
    }

    /**
     * 返回渠道
     * @param companysBean
     * @return
     */
    private String returnChannel(CompanysBean companysBean) {
        String str="";
        if(!StringUtils.isEmpty(companysBean.getCompanyChannel())){
            Integer channel =companysBean.getCompanyChannel();

            if(channel==1){
                str="百度360，等搜索引擎";
            }else if(channel==2){
                str="微信，QQ等社交平台";
            }else if(channel==3){
                str="推销";
            }else if(channel==4){
                str="广告";
            }else if(channel==5){
                str="朋友介绍";
            }else if(channel==6){
                str ="其他";
            }
        }
        return str;
    }

    /**
     * 完善信息
     * @param request
     * @param companysBean
     * @return
     */
    @RequestMapping(value="perfectInformation.htm")
    @ResponseBody
    public ResultResponse perfectInformation(HttpServletRequest request,CompanysBean companysBean){

    ResultResponse resultResponse = new ResultResponse();
    try{
     CompanysBean com = SessionUtils.getCompany(request);
     companysBean.setCompanyId(com.getCompanyId());
     resultResponse = hongbaoService.perfectInformation(companysBean);//完善信息
    }catch ( Exception e ){
        LOGGER.info("完善信息出现错误："+e.getMessage());
        resultResponse.setMessage(e.getMessage());
    }

    return resultResponse;
    }



    /**
     * 跳转到活动页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toMenuPage.htm")
    public ModelAndView toMenuPage(ModelAndView modelAndView,HttpServletRequest request){
        modelAndView.setViewName("xtr/hongbao/hongbaoMenu");


        //获取注册来的标识
        if(!StringUtils.isStrNull(request.getParameter("registerFrom"))){
            modelAndView.addObject("registerFrom",request.getParameter("registerFrom"));
        }
        //当前登录账号
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获得公司信息
        CompanysBean companysBean = SessionUtils.getCompany(request);
        companysBean = companysService.selectCompanyByCompanyId(companysBean.getCompanyId());
        //获取当前登录账号已领取多少元 还剩多少元

        Long account = companysService.selectHasReceiveAccount(companyMembersBean.getMemberId());
        modelAndView.addObject("account",account);
        modelAndView.addObject("memberName",companyMembersBean.getMemberName());//注册成功后显示
        modelAndView.addObject("memberPhone",companyMembersBean.getMemberPhone());//注册成功后显示
        modelAndView.addObject("approveReason",companysBean.getCompanyDatumDismissreason());//公司审核不通过原因
        //审核状态
        if(StringUtils.isEmpty(companysBean.getCompanyAuditStatus()))
            modelAndView.addObject("auditStatus","null");//公司信息审核的状态
             else
            modelAndView.addObject("auditStatus",companysBean.getCompanyAuditStatus());

        return modelAndView;
    }

    /**
     * 跳转到活动指南页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toFreeGuidePage.htm")
    public ModelAndView toFreeGuidePage(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/hongbao/hongbaoFreeGuide");
        return modelAndView;
    }


    /**
     * 判断修改的手机号是否存在
     * @param request
     * @return
     */
    @RequestMapping(value="hasRegisterPhone.htm")
    @ResponseBody
    public ResultResponse hasRegisterPhone(HttpServletRequest request){

      ResultResponse resultResponse =  new ResultResponse();
      try {
          String phoneNum = request.getParameter("phoneNum");
          if(StringUtils.isStrNull(phoneNum)){
              resultResponse.setMessage("手机号不能为空");
              return resultResponse;
          }
          //将每次发送短信的手机号存到session中
          request.getSession().setAttribute("phoneNum",phoneNum);
          CompanyMembersBean companyMembersBean = new CompanyMembersBean();
          companyMembersBean.setMemberPhone(phoneNum);
          List<CompanyMembersBean> list = companyMembersService.selectByPhoneOrLogname(companyMembersBean);

          if(null==list||list.size()<0) {
              resultResponse.setMessage("手机号不存在");
          }else {
              //获取当前的memberId
              companyMembersBean = list.get(0);
              resultResponse.setData(companyMembersBean.getMemberId());//把每次的memberId带过去
              resultResponse.setSuccess(true);
              resultResponse.setMessage("手机号存在");
          }
      }catch ( Exception e ){
          LOGGER.error("判断手机号是否是注册的手机号出现错误" +e.getMessage());
          resultResponse.setMessage("未知错误");
      }
      return resultResponse;
    }

    /**
     * 从忘记密码跳转到 登录页面
     * @return
     */
    @RequestMapping(value="returnLogin.htm")
    public ModelAndView modelAndView(HttpServletRequest  request,ModelAndView modelAndView){
        WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);
       //多少人领取红包
        //查询当前多少hr领取红包
        long  receiveCounts = companysService.selectActivityRedCounts();
        modelAndView.addObject("returnLogin","returnLogin");
        modelAndView.addObject("receiveCounts",receiveCounts);
        modelAndView.setViewName("xtr/hongbao/hongbaoRegister");
        return modelAndView;
    }


    /**
     * 该登录的用户所属公司是否已完善信息
     * @param request
     * @return
     */
    @RequestMapping(value="hasPerfectInfo.htm")
    @ResponseBody
    public ResultResponse hasPerfectInfo(HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();
        try{
            CompanysBean companysBean = SessionUtils.getCompany(request);
            companysBean =companysService.selectCompanyByCompanyId(companysBean.getCompanyId());
            //公司名称  公司地址 营业执照编号  营业执照扫描件  联系电话  了解渠道
            if(!StringUtils.isEmpty(companysBean)&&!StringUtils.isStrNull(companysBean.getCompanyName())&&!StringUtils.isStrNull(companysBean.getCompanyAddress())
                    &&!StringUtils.isStrNull(companysBean.getCompanyNumber())&&
                    !StringUtils.isStrNull(companysBean.getCompanyOrganizationImg())&&!StringUtils.isStrNull(companysBean.getCompanyCorporationPhone())
                    &&!StringUtils.isEmpty(companysBean.getCompanyChannel())){
                if(!StringUtils.isEmpty(companysBean.getCompanyAuditStatus())){
                    if((companysBean.getCompanyAuditStatus()==0||companysBean.getCompanyAuditStatus()==2)){
                        resultResponse.setSuccess(true);
                        resultResponse.setMessage("企业信息已完善");
                        return resultResponse;
                    }else{
                        resultResponse.setMessage("您还未完善信息，请先完善信息");
                        return resultResponse;
                    }
                }else{
                    resultResponse.setMessage("您还未完善信息，请先完善信息");
                    return resultResponse;
                }


            }else{
                resultResponse.setMessage("您还未完善信息，请先完善信息");
            }
        }catch ( Exception e ){
            resultResponse.setSuccess(true);
            resultResponse.setMessage("网络错误，请重新刷新");

        }

        return resultResponse;
    }


    /**
     * 领取红包
     * @param request
     * @return
     */
//    @RequestMapping(value="receiveHongbao.htm")
//    @ResponseBody
//    public ResultResponse receiveHongbao(HttpServletRequest request){
//        ResultResponse resultResponse = new ResultResponse();
//        //当前登录账号
//        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
//        //获取登录的公司
//        CompanysBean companysBean = SessionUtils.getCompany(request);
//        companysBean =companysService.selectCompanyByCompanyId(companyMembersBean.getMemberCompanyId());
//        CompanyActivityBean activity =null;
//        try{
//            //获取红包标识
//            if(StringUtils.isStrNull(request.getParameter("activityReceive")) || StringUtils.isEmpty(companyMembersBean)){
//                //跳到登录页面  即调一次重新登录页面即可
//                resultResponse.setSuccess(true);
//                resultResponse.setMessage("重新登录");
//                resultResponse.setMsgCode("returnLogin");
//                return resultResponse;
//            }
//           String activityReceive = request.getParameter("activityReceive");
//            if(!activityReceive.equals("1")&& !activityReceive.equals("2")){
//                resultResponse.setMessage("非法链接！");
//                return resultResponse;
//            }
//
//            //判断当前时间是否在活动范围
//            boolean flag = hasOutTime();
//            if(!flag){
//                resultResponse.setSuccess(false);
//                resultResponse.setMessage("该活动已过期");
//                return resultResponse;
//            }else{
//                //判断用户注册时间是否在活动期间
//                //判断公司的完善信息是否在活动时间
//                if(activityReceive.equals("1")){
//                    //判断注册时间是否在此范围内
//                    flag = hasOtherOutTime(companyMembersBean.getRegisterTime());
//                    if(!flag){
//                        resultResponse.setSuccess(false);
//                        resultResponse.setMessage("您的账号注册时间不在该活动时间范围");
//                        return resultResponse;
//                    }
//                }else if(activityReceive.equals("2")){
//                    flag = hasOtherOutTime(companysBean.getCompanyEditime());
//                    if(!flag){
//                        resultResponse.setSuccess(false);
//                        resultResponse.setMessage("完善信息最后修改时间不在该活动时间范围");
//                        return resultResponse;
//                    }
//                }
//            }

            //判断请求来源是否来自微信端
//           Map<String,Object> map = this.wechatUserInfo(request);
//           if(map==null||map.get("success")==false){
//               //说明是外部连接
//               resultResponse.setSuccess(true);
//               resultResponse.setMessage("非微信端");
//               resultResponse.setMsgCode("no-wechat");
//               return resultResponse;
//           }
//           //业务操作  判断是否领取过  是否关注过
//
//           // 判断是否关注过
//            if(map!=null&&map.get("success")==true&&map.get("subscribe")==0){
//                resultResponse.setSuccess(true);
//                resultResponse.setMessage("还未关注过公众号");
//                resultResponse.setMsgCode("no-subscribe");//未关注标识
//                return resultResponse;
//            }
//
//            //如果是20元则判断是否完善信息
//            if(activityReceive.equals("2")) {
//                if(!StringUtils.isEmpty(companysBean.getCompanyAuditStatus())){
//                    if (StringUtils.isEmpty(companysBean) || StringUtils.isStrNull(companysBean.getCompanyName()) || StringUtils.isStrNull(companysBean.getCompanyAddress())
//                            || StringUtils.isStrNull(companysBean.getCompanyNumber()) ||
//                            StringUtils.isStrNull(companysBean.getCompanyOrganizationImg()) || StringUtils.isStrNull(companysBean.getCompanyCorporationPhone())
//                            || StringUtils.isEmpty(companysBean.getCompanyChannel())) {
//
//                        resultResponse.setMessage("请先完善信息");
//                        return resultResponse;
//
//                    }else{
//                        if(companysBean.getCompanyAuditStatus()!=2){
//                        if(companysBean.getCompanyAuditStatus()==0){
//                            resultResponse.setMessage("信息还在审核中");
//                        }else if(companysBean.getCompanyAuditStatus()==1){
//                            resultResponse.setMessage("审核已驳回，请重新完善信息");
//                           }
//
//                            return resultResponse;
//                        }
//
//                    }
//                }else{
//                    resultResponse.setMessage("请先完善信息");
//                    return resultResponse;
//                }
//
//            }
//
//            Map <String,Object>params = new HashMap<String,Object>();
//            params.put("activityMemberId",companyMembersBean.getMemberId());
//            params.put("activityReceive",Integer.valueOf(activityReceive));//5元红包类型
//            CompanyActivityBean companyActivityBean =companysService.selectReds(params) ;
//            if(companyActivityBean!=null){
//                //红包已领取过
//               resultResponse.setMessage("红包已经领取过");
//                return resultResponse;
//            }
//            String ip = request.getRemoteAddr();//ip地址
//            String orderNum;
//            //随机订单号
//           if(activityReceive.equals("1")){
//                orderNum = idGeneratorService.getOrderId(BusinessEnum.COMPANY_WECHAT_FIVE_REDS);
//           }else{
//               orderNum = idGeneratorService.getOrderId(BusinessEnum.COMPANY_WECHAT_ADUIT_REDS);
//           }
//            //插入红包记录
//            //封装参数
//            activity  = new CompanyActivityBean();
//            activity.setActivityCompanyId(companyMembersBean.getMemberCompanyId());
//            activity.setActivityMemberId(companyMembersBean.getMemberId());
//            activity.setActivityCreateTime(new Date());
//            activity.setActivityHasReceive(1);//已领取
//            activity.setActivityIp(ip);
//            activity.setActivityName("微信红包");
//            activity.setActivityOrderNumber(orderNum);//订单
//            activity.setActivityReceive(Integer.valueOf(activityReceive));//红包类型  1为5元  2为20元
//            if(activityReceive.equals("1"))
//                activity.setReceiveAccount(5);//5元金额
//
//            else
//                activity.setReceiveAccount(20);//20元金额
//
//            //保存
//            activity=companysService.saveCompanyActivity(activity);
//
//            LOGGER.info("插入一条红包发送记录"+activity.getActivityId());
//
//            if(!StringUtils.isEmpty(activity.getActivityId())){
//
//                String openId = (String) map.get("openid");
//////                //钱
//                int money=0;
//                if(activityReceive.equals("1")){
//                    money = 500;//分 5元
//                }else if(activityReceive.equals("2")){
//                    money = 2000;//分
//                }
//
//                String resultContent =weChatRedPackUtil.PaySetCont(openId,orderNum,money,ip);
//                LOGGER.info("发送红包订单："+orderNum+"   红包返回："+resultContent);
//
//                //更新数据库  保存微信返回值
//                Map updateMap = new HashMap();
//                updateMap.put("activityId",activity.getActivityId());
//                updateMap.put("wechatReturnMsg", ParseReturnXmlUtils.parseXml(resultContent));
//                //更新数据库
//                int updateResult = companysService.updateActivity(updateMap);
//                LOGGER.info("更新活动id："+activity.getActivityId()+" 更新操作影响的行数："+updateResult);
//                resultResponse.setSuccess(true);
//                resultResponse.setMessage("发送红包");
//                resultResponse.setMsgCode("send-reds");
//                resultResponse.setData(activityReceive);
//                return resultResponse;
//            }else{
//                resultResponse.setMessage("领取失败");
//            }
//
//        }catch ( Exception e ){
//            e.printStackTrace();
//            Map updateMap = new HashMap();
//            if (activity != null){
//                updateMap.put("activityId",activity.getActivityId());
//                updateMap.put("wechatReturnMsg", "异常错误");
//                int updateResult = companysService.updateActivity(updateMap);
//                LOGGER.info("更新活动id："+activity.getActivityId()+" 更新操作影响的行数："+updateResult);
//            }
//            else{
//                LOGGER.error("领红包出现异常错误：更新活动activity=null");
//            }
//
//           LOGGER.error("领红包出现异常错误："+e.getMessage());
//            resultResponse.setMessage("领取失败");
//            LOGGER.info("当前发红包出现错误");
//        }
//        return resultResponse;
//    }

    /**
     * 判端当前时间活动期间
     * @return
     */
    private boolean hasOutTime() {
        boolean flag = false;
        try {
            String startTimeStr = PropertyUtils.getString("weixin.receive.startTime");
            String endTimeStr = PropertyUtils.getString("weixin.receive.endTime");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            if(date.getTime()-(sf.parse(startTimeStr).getTime())>=0 && date.getTime()-(sf.parse(endTimeStr).getTime())<=0){
                flag = true;
            }else{
                flag=false;
            }
        }catch ( Exception e ){
            e.getMessage();
        }

        return flag;
    }

    /**
     * 判断传入的时间是否在活动时间范围
     * @return
     */
    private boolean hasOtherOutTime(Date time) {
        boolean flag = false;
        try {
            String startTimeStr = PropertyUtils.getString("weixin.receive.startTime");
            String endTimeStr = PropertyUtils.getString("weixin.receive.endTime");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(time.getTime()-(sf.parse(startTimeStr).getTime())>=0 && time.getTime()-(sf.parse(endTimeStr).getTime())<=0){
                flag = true;
            }
            return flag;
        }catch ( Exception e ){
            e.getMessage();
        }

        return flag;
    }

    /**
     * 判断是否能获取到用户的微信信息
     * @param request
     * @return
     */
    public Map<String,Object> wechatUserInfo(HttpServletRequest request) {

        Map<String,Object> returnMap = new HashMap<String,Object>();//返回信息的map
        try {
            WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);
            if (openIdByRequest == null) {
               returnMap.put("success",false);//来自外部的标识
            } else {

                 Map map = weChatUtil.userInfo(openIdByRequest.getOpenid());
                 returnMap.put("subscribe",map.get("subscribe"));
                 returnMap.put("success",true);
                 returnMap.put("infoMap",map);//微信用户信息的map
                 returnMap.put("openid", map.get("openid"));//微信用户信息的map
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return returnMap;

    }




}
