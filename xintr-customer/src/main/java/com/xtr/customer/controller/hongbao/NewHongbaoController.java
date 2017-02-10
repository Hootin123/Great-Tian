package com.xtr.customer.controller.hongbao;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.hongbao.NewHongbaoDto;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.h5wallet.H5walletService;
import com.xtr.api.service.h5wallet.HongbaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import com.xtr.customer.util.SessionUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * 新版红包controller层
 * @author:zhangshuai
 * @date: 2016/9/27.
 */
@Controller
@RequestMapping("newHongbao")
public class NewHongbaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewHongbaoController.class);

    @Resource
    private HongbaoService hongbaoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private H5walletService h5walletService;
    /**
     * 跳转到活动详情页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "toActivityDetail.htm")
    public ModelAndView jumpToActivityDetail(ModelAndView modelAndView,HttpServletRequest request){
        LOGGER.error("获取参数mmtoken："+(String)request.getParameter("mmtoken"));
        LOGGER.error("获取参数companyContactPlace："+(String)request.getParameter("获取参数companyContactPlace"));
        modelAndView.addObject("mmtoken",(String)request.getParameter("mmtoken"));
        modelAndView.addObject("companyContactPlace",(String)request.getParameter("companyContactPlace"));
        modelAndView.setViewName("xtr/newHongbao/activeDetails");
        return modelAndView;
    }

    /**
     * 跳转到活动注册页面 A方式
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toActivityRegister.htm")
    public ModelAndView jumpToActivityRegister(ModelAndView modelAndView,HttpServletRequest request){
        LOGGER.error("获取参数mmtoken："+(String)request.getParameter("mmtoken"));
        LOGGER.error("获取参数companyContactPlace："+(String)request.getParameter("获取参数companyContactPlace"));
        //发送脉脉回调
        try {
            if(!StringUtils.isStrNull((String)request.getParameter("mmtoken"))){
                LOGGER.error("发送脉脉回调：mmtoken="+(String)request.getParameter("mmtoken"));
                String url="https://maimai.cn/hb_pingback";
                HttpClient httpclient = new HttpClient();
                PostMethod methodpost = new PostMethod(url);
                httpclient.getParams().setContentCharset("UTF-8");
                methodpost.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
                methodpost.setRequestHeader("Accept-Charset","utf-8");
                methodpost.setRequestHeader("Connection", "close");
                methodpost.addParameter("mmtoken", (String)request.getParameter("mmtoken"));
                methodpost.releaseConnection();
                int state =httpclient.executeMethod(methodpost);
                if(state== 200 ){
                    String responses= methodpost.getResponseBodyAsString();
                    LOGGER.error("发送脉脉回调："+responses);
                }else{
                    LOGGER.error("发送脉脉回调失败：state="+state);

                }

            }

        } catch (Exception e) {
            LOGGER.error("发送脉脉回调失败：mmtoken=" + (String)request.getParameter("mmtoken"), e.getMessage(), e);
        }

        modelAndView.addObject("mmtoken",(String)request.getParameter("mmtoken"));
        modelAndView.addObject("companyContactPlace",(String)request.getParameter("companyContactPlace"));
        modelAndView.setViewName("xtr/newHongbao/newRegister");
        return modelAndView;
    }

    /**
     * A类型活动注册
     * @param request
     * @return
     */
    @RequestMapping(value ="activityRegister.htm")
    @ResponseBody
    public ResultResponse activityRegister(HttpServletRequest request, NewHongbaoDto newHongbaoDto){
        ResultResponse resultResponse = null;
        LOGGER.error("获取参数mmtoken："+(String)request.getParameter("mmtoken"));
        LOGGER.error("获取参数companyContactPlace："+(String)request.getParameter("获取参数companyContactPlace"));
        try{
            String companyContactPlace = (String)request.getParameter("companyContactPlace");
            String mmtoken = (String)request.getParameter("mmtoken");

            if(!StringUtils.isStrNull(mmtoken)){
                companyContactPlace = "maimai";
            }

//            //发送脉脉回调
//            try {
//                if(!StringUtils.isStrNull(mmtoken)){
//                    LOGGER.error("register发送脉脉回调：mmtoken="+mmtoken);
//                    String url="https://maimai.cn/hb_pingback";
//                    HttpClient httpclient = new HttpClient();
//                    PostMethod methodpost = new PostMethod(url);
//                    httpclient.getParams().setContentCharset("UTF-8");
//                    methodpost.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
//                    methodpost.setRequestHeader("Accept-Charset","utf-8");
//                    methodpost.setRequestHeader("Connection", "close");
//                    methodpost.addParameter("mmtoken", mmtoken);
//                    methodpost.releaseConnection();
//                    int state =httpclient.executeMethod(methodpost);
//                    if(state== 200 ){
//                        String responses= methodpost.getResponseBodyAsString();
//                        LOGGER.error("register发送脉脉回调："+responses);
//                    }else{
//                        LOGGER.error("register发送脉脉回调失败：state="+state);
//
//                    }
//
//                }
//
//            } catch (Exception e) {
//                LOGGER.error("register发送脉脉回调失败：mmtoken=" + mmtoken, e.getMessage(), e);
//            }

             resultResponse = hongbaoService.activityRegister(newHongbaoDto,companyContactPlace);
             request.getSession().setAttribute("newHongbaoDto",resultResponse.getData());
        }catch (Exception e){
            LOGGER.error("A类型注册异常信息："+e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * H5上传营业执照及上传公司logo
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("uploadFile.htm")
    @ResponseBody
    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        LOGGER.info("请求上传接口。。。。。");
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
                    LOGGER.error("上传营业执照图片,上传文件失败");
                    resultResponse.setMessage("上传文件失败");
                }
            }else{
                LOGGER.error("上传营业执照图片,没有上传文件");
                resultResponse.setMessage("没有上传文件");
            }
        }catch (IOException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("上传营业执照图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }


    /**
     * 发送注册手机验证码
     * @param memberPhone
     * @return
     */
    @RequestMapping(value="sendMsgByNewHongbao.htm")
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
     * 判断手机号是否已注册
     * @return
     */
    @RequestMapping(value="judgePhoneNumber.htm")
    @ResponseBody
    public ResultResponse judgePhoneNumber(HttpServletRequest request){
        ResultResponse resultResponse = new ResultResponse();
        try{
            String phone = request.getParameter("phoneNumber");
            LOGGER.info("判断手机号是否已被注册："+phone);
            CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
            checkCompanyBean.setMemberPhone(phone);
            List<CompanyMembersBean> hasCom = companyMembersService.selectByPhoneOrLogname(checkCompanyBean);
            if(hasCom!=null&&hasCom.size()>0){
                resultResponse.setMessage("手机号已被注册");
            }else{
                //发送短信验证码
                resultResponse= hongbaoService.sendPhoneMsgByH5(phone);
            }
        }catch (Exception e){
           LOGGER.error("判断手机号是否已注册出现错误："+e.getMessage());
           resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 生成图形验证码
     * @param request
     * @param response
     */
    @RequestMapping(value="getImgCode.htm")
    public void getImgCode(HttpServletRequest request, HttpServletResponse response){
        try{
            String key = request.getSession().getId()+ Constant.VCOD_EIMG;
            //生成验证码
            //h5walletService.generateH5ValidatorImages(key,response);
            int w = 100, h = 40;
            //生成验证码图片并放入response中
            String verifyCode = VerifyCodeUtils.outputVerifyImage(w,h,response.getOutputStream(),4);
            LOGGER.info("新版红包登录生成验证码:"+verifyCode);
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
     * A类活动登录
     * @param request
     * @return
     */
    @RequestMapping(value="activityLogin.htm")
    @ResponseBody
    public ResultResponse activityLogin(HttpServletRequest request,NewHongbaoDto newHongbaoDto){
        ResultResponse resultResponse = new ResultResponse();
        try{
             String key = request.getSession().getId() + Constant.VCOD_EIMG;
              newHongbaoDto.setKey(key);
             resultResponse = hongbaoService.activityLogin(newHongbaoDto);
             if(resultResponse.isSuccess()){
                CompanyMembersBean resultBean=(CompanyMembersBean)resultResponse.getData();
                //根据企业ID获取企业信息放入session中
                CompanysBean companyBean=companysService.selectCompanyByCompanyId(resultBean.getMemberCompanyId());
                request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);
                request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean);
                request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, companyBean);
               // resultResponse.setData(resultBean.getMemberCompanyId());
                 Map map = new HashMap();
                 map.put("companyId",resultBean.getMemberCompanyId());//公司id
                 //判断是否完善信息
                 if(StringUtils.isStrNull(companyBean.getCompanyName())||StringUtils.isStrNull(companyBean.getCompanyAddress())
                         ||StringUtils.isStrNull(companyBean.getCompanyNumber())||StringUtils.isStrNull(companyBean.getCompanyOrganizationImg())
                         ||StringUtils.isStrNull(companyBean.getCompanyCorporation())||StringUtils.isStrNull(companyBean.getCompanyCorporationPhone())||
                         StringUtils.isEmpty(companyBean.getCompanyAuditStatus())){
                  map.put("returnStr","perfectInfo");
                 }else{
                     //判断是否收集过资料
                     if(StringUtils.isEmpty(companyBean.getCompanyIsCollectInfo())||companyBean.getCompanyIsCollectInfo()!=1){
                         //收集资料页面标识
                         map.put("returnStr","collectInfo");
                     }else{
                         //判断审核状态
                         map.put("returnStr",companyBean.getCompanyAuditStatus());
                         if(!StringUtils.isEmpty(companyBean.getCompanyAuditStatus())){
                             if(companyBean.getCompanyAuditStatus()==1)
                             map.put("reason",StringUtils.isStrNull(companyBean.getCompanyDatumDismissreason())==true?"":companyBean.getCompanyDatumDismissreason());

                         }
                     }
                 }


                resultResponse.setData(map);
            }
        }catch (Exception e){
          LOGGER.error("登录出现异常错误："+e.getMessage());
          resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }


    /**
     * 跳转至资料审核页面
     * @param modelAndView
     * @return
     */
     @RequestMapping(value="toActivityApprove.htm")
     public ModelAndView jumpToActivityApprove(ModelAndView modelAndView,HttpServletRequest request){
      modelAndView.setViewName("xtr/newHongbao/approve");
       CompanysBean companysBean = SessionUtils.getCompany(request);
      //获取修改页面的标识
      if(!StringUtils.isStrNull(request.getParameter("editFlag"))){
          CompanysBean companys =companysService.selectCompanyByCompanyId(companysBean.getCompanyId());
          modelAndView.addObject("companyAddress",companys.getCompanyAddress());//地址
          modelAndView.addObject("companyNumber",companys.getCompanyNumber());//营业执照编号
          modelAndView.addObject("companyName",companysBean.getCompanyName());
          //公司logo
          if(StringUtils.isStrNull(companys.getCompanyLogo())){
              modelAndView.addObject("companyLogo","");
          }else{
              modelAndView.addObject("companyLogo",companys.getCompanyLogo());
              modelAndView.addObject("companyLogoUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+companys.getCompanyLogo());
          }
          if(StringUtils.isStrNull(companys.getCompanyOrganizationImg())){
              modelAndView.addObject("companyOrganizationImg","");
          }else{
              modelAndView.addObject("companyOrganizationImg",companys.getCompanyOrganizationImg());
              modelAndView.addObject("companyOrganizationImgUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+companys.getCompanyOrganizationImg());
          }
          modelAndView.addObject("companyCorporation",companys.getCompanyCorporation());
          modelAndView.addObject("companyCorporationPhone",companys.getCompanyCorporationPhone());
      }else{
          modelAndView.addObject("companyLogo","");
          modelAndView.addObject("companyOrganizationImg","");
          NewHongbaoDto newHongbaoDto =null;
          if(null==companysBean){
              newHongbaoDto = (NewHongbaoDto) request.getSession().getAttribute("newHongbaoDto");
              modelAndView.addObject("companyName",newHongbaoDto.getCompanyName());
              modelAndView.addObject("companyId",newHongbaoDto.getCompanyId());
          }else{
              modelAndView.addObject("companyName",companysBean.getCompanyName());
              modelAndView.addObject("companyId",companysBean.getCompanyId());
          }


      }


      return modelAndView;
     }

    /**
     * 资料审核提交
     * @param request
     * @param companysBean
     * @return
     */
     @RequestMapping(value="activityApprove.htm")
     @ResponseBody
     public ResultResponse activityApprove(HttpServletRequest request, CompanysBean companysBean){
      ResultResponse resultResponse = new ResultResponse();
      CompanysBean companys = SessionUtils.getCompany(request);
      Long companyId =null;
      if(null==companys)
          companyId = companysBean.getCompanyId();
      else
          companyId =companys.getCompanyId();

      CompanysBean com = companysService.selectCompanyByCompanyId(companyId);
      try{
         companysBean.setCompanyId(companyId);//公司id
         resultResponse = hongbaoService.activityApprove(companysBean);
         Map map = new HashMap();
         map.put("companyId",companyId);
         map.put("isCollect",com.getCompanyIsCollectInfo());
         resultResponse.setData(map);
          //提交资料判断是否已经收集过资料
      }catch (Exception e){
        LOGGER.error("资料完善出现异常错误："+e.getMessage());
      }
      return resultResponse;
     }

    /**
     * 跳转至信息收集页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="infoCollect.htm")
     public ModelAndView jumpToInfoCollect(ModelAndView modelAndView,String companyId){
        modelAndView.setViewName("xtr/newHongbao/personalInfo");
        modelAndView.addObject("companyId",companyId);
         return modelAndView;
     }

    /**
     * 信息收集提交
     * @param request
     * @return
     */
     @RequestMapping(value="infoCollectSubmit.htm")
     @ResponseBody
     public ResultResponse infoCollectSubmit(HttpServletRequest request,NewHongbaoDto newHongbaoDto){

     // CompanysBean companysBean = SessionUtils.getCompany(request);
      Long companyId = newHongbaoDto.getCompanyId();
         CompanysBean companysBean = companysService.selectCompanyByCompanyId(companyId);
      ResultResponse resultResponse = new ResultResponse();
      try{
          resultResponse = hongbaoService.infoCollectSubmit(newHongbaoDto);
          newHongbaoDto = (NewHongbaoDto) resultResponse.getData();
          Map map = new HashMap();
          map.put("newHongbaoDto",newHongbaoDto);
          if(companysBean.getCompanyAuditStatus()==null||companysBean.getCompanyAuditStatus()==0){
              //待审核
            map.put("perInfo","wait");
          }else if(companysBean.getCompanyAuditStatus()==1){
              //审核未通过
              map.put("perInfo","noPass");
              map.put("reason",companysBean.getCompanyDatumDismissreason());
              map.put("companyId",companysBean.getCompanyId());
          }else if(companysBean.getCompanyAuditStatus()==2){
              //审核通过
              map.put("perInfo","pass");
          }
          resultResponse.setData(map);
      }catch (Exception e){
       LOGGER.error("信息收集提交出现错误："+e.getMessage());
      }
      return resultResponse;
     }

    /**
     * 跳转成功提示页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toPromptSuccess.htm")
    @ResponseBody
     public ModelAndView jumpToPromptSuccessPage(ModelAndView modelAndView){
         modelAndView.setViewName("xtr/newHongbao/promptSuccess");
         return modelAndView;
     }

    /**
     * 跳转至失败提示页面
     * @param request
     * @param modelAndView
     * @return
     */
     @RequestMapping(value="toPromptDefeat.htm")
      public ModelAndView jumpToPromptDefeatPage(HttpServletRequest request,ModelAndView modelAndView) throws Exception{
      String reason = request.getParameter("reason");
      String companyId = request.getParameter("companyId");
      modelAndView.setViewName("xtr/newHongbao/promptDefeat");
      modelAndView.addObject("reason",new String(reason.getBytes("iso8859-1"),"utf-8"));//失败原因
      modelAndView.addObject("companyId",companyId);//公司Id
      return modelAndView;
      }

    /**
     * 跳转至审核提示页面
     * @param modelAndView
     * @return
     */
      @RequestMapping(value="toPromptWait.htm")
      public ModelAndView jumpToPromptWaitPage(ModelAndView modelAndView){
          modelAndView.setViewName("xtr/newHongbao/promptWait");
          return modelAndView;
      }

    /**
     * 跳转至普通的注册页面
     * @param modelAndViel
     * @return
     */
     @RequestMapping(value="toCommonlyRegister.htm")
     public ModelAndView jumpToCommonlyRegister(ModelAndView modelAndViel){
       modelAndViel.setViewName("xtr/newHongbao/register");
       return modelAndViel;
     }


    /**
     * 跳转至注册成功页面
     * @return
     */
     @RequestMapping(value="toRegisterSuccess.htm")
     public ModelAndView jumpToRegisterSuccess(ModelAndView modelAndView){
       modelAndView.setViewName("xtr/newHongbao/success");
       return modelAndView;
     }



}
