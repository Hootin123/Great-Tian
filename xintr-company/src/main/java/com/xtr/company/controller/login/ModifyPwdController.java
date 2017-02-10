package com.xtr.company.controller.login;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.dto.company.CompanyMembersDto;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.station.StationSmsRecordsService;
import com.xtr.api.service.station.StationSmscontService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.enums.ErrorCodeEnum;
import com.xtr.comm.enums.StationSmsRecordsTypeEnum;
import com.xtr.comm.util.*;
import com.xtr.comm.util.StringUtils;
import org.apache.commons.lang3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/22 16:27
 */
@Controller
public class ModifyPwdController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPwdController.class);

    @Resource
    CompanyMembersService companyMembersService;
    @Resource
    private StationSmsRecordsService stationSmsRecordsService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private StationSmscontService stationSmscontService;
    /**
     * 获取企业信息
     * @param mav
     * @return
     */
    @RequestMapping("getModifyPwdInfo.htm")
    @ResponseBody
    public ResultResponse getModifyPwdInfo(HttpServletRequest request,ModelAndView mav) {
        ResultResponse resultResponse=new ResultResponse();
        //企业员工里存储的公司信息是缓存信息
        CompanyMembersBean companyMemberBean=getLoginUserObj(request);
        CompanysBean companyBean=getLoginCompanyObj(request);
        companyMemberBean.setCompanyName(companyBean.getCompanyName());
        resultResponse.setData(companyMemberBean);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 修改密码
     * @param request
     * @param mav
     * @param companyMembersDto
     * @return
     */
    @RequestMapping("modifyPwdOperation.htm")
    @ResponseBody
    @SystemControllerLog(operation = "修改密码", modelName = "账户设置")
    public ResultResponse modifyPwdOperation(HttpServletRequest request, ModelAndView mav, @RequestBody CompanyMembersDto companyMembersDto) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("修改密码，获取传递参数："+ JSON.toJSONString(companyMembersDto));
        String newPwd=companyMembersDto.getNewPwd();
        String confirmNewPwd=companyMembersDto.getConfirmNewPwd();
        try{
            if(companyMembersDto!=null && !StringUtils.isStrNull(newPwd) && !StringUtils.isStrNull(confirmNewPwd)
                    && companyMembersDto.getMemberId()!=null && !StringUtils.isStrNull(companyMembersDto.getMemberPassword())){//参数不能为空
                //根据用户ID获取用户密码
                CompanyMembersBean resultMemberBean=companyMembersService.selectCompanyMembersBean(companyMembersDto.getMemberId());
                if(resultMemberBean!=null){
                    //验证原密码是否正确
                    if((StringUtils.getMD5two(companyMembersDto.getMemberPassword()).toLowerCase()).equals(resultMemberBean.getMemberPassword())){//原密码正确
                        //验证新密码与确认密码是否一致,验证新密码是否符合密码规范
                        if(newPwd.equals(confirmNewPwd)){
                            //更改密码
                            CompanyMembersBean updateMemberBean=new CompanyMembersBean();
                            updateMemberBean.setMemberId(companyMembersDto.getMemberId());
                            updateMemberBean.setMemberPassword(StringUtils.getMD5two(newPwd));//密码加密
                            companyMembersService.updateByPrimaryKeySelective(updateMemberBean);
                            resultResponse.setSuccess(true);
                        }else{
                            LOGGER.info("新密码与确认密码不一致");
                            resultResponse.setMessage("新密码与确认密码不一致");
                        }
                    }else{
                        LOGGER.info("原密码不正确");
                        resultResponse.setMessage("修改密码,原密码不正确");
                    }
                }else{
                    LOGGER.info("修改密码，获取不到原用户信息");
                    resultResponse.setMessage("修改密码，获取不到原用户信息");
                }

            }else{
                LOGGER.info("修改密码,传递的参数不能有空值");
                resultResponse.setMessage("修改密码,传递的参数不能有空值");
            }
        }catch(BusinessException e){
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 用户账户设置  修改手机号
     * @param request
     *
     * @return
     */
    @RequestMapping(value="changeMemberPhone.htm")
    @ResponseBody
    @SystemControllerLog(operation = "修改名字/手机号", modelName = "账户设置")
    public ResultResponse changeMemberPhone(HttpServletRequest request,CompanyMembersDto companyMembersDto){


     ResultResponse resultResponse = new ResultResponse();

      try {

          //需要获取的参数
          Long memberId =companyMembersDto.getMemberId();
          String newMemberPhone =  companyMembersDto.getNewMemberPhone();
          String userName =companyMembersDto.getUserName();

          CompanyMembersBean companyMembersBean = getLoginUserObj(request);
          //判断手机号是否篡改了
          if(!StringUtils.isStrNull(newMemberPhone)){

          if( !StringUtils.isEmpty(request.getSession().getAttribute("newPhoneNumber")) &&!newMemberPhone.equals (request.getSession().getAttribute("newPhoneNumber"))){
              LOGGER.info("当前手机号已经在提交前修改了");
              resultResponse.setSuccess(false);
              return resultResponse;
          }}
          if (null == companyMembersBean) {
              memberId = Long.valueOf(request.getSession().getAttribute("memberid").toString());
          } else {
              memberId = companyMembersBean.getMemberId();
          }
          if (org.apache.commons.lang3.StringUtils.isBlank(companyMembersDto.getNewMemberPhone())) {
             //说明是只点了确定
              CompanyMembersBean comp = companyMembersService.selectCompanyMembersBean(memberId);
              if(null==comp){
                  resultResponse.setSuccess(false);
                 resultResponse.setMessage("当前修改的用户数据库不存在");
                  return resultResponse;
              }

              CompanyMembersBean com =  new CompanyMembersBean();
              com.setMemberId(memberId);
              com.setMemberName(companyMembersDto.getUserName());

              int a =companyMembersService.updateByPrimaryKeySelective(com);
              if(a<1||a==0){
                  resultResponse.setSuccess(false);
                  resultResponse.setMessage("当前修改的用户数据库不存在");
                  return resultResponse;
              }
               resultResponse.setSuccess(true);
              resultResponse.setData(userName);
              resultResponse.setMsgCode(newMemberPhone);
              request.getSession().removeAttribute("newPhoneNumber");//清除session值
              request.getSession().setAttribute("userName",userName);
              request.getSession().setAttribute("memberid", memberId);
              CompanyMembersBean resultBean = (CompanyMembersBean) request.getSession().getAttribute(CommonConstants.LOGIN_USER_KEY);
              resultBean.setMemberLogname(companyMembersDto.getNewMemberPhone());
              request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);//重新设置session
              request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean); //重新设置session


              return resultResponse;
          } else{
              //进行更新操作
              //判断当前用户是否存在
              CompanyMembersBean comp = companyMembersService.selectCompanyMembersBean(memberId);
              if(null==comp){
                 resultResponse.setMessage("当前修改的用户数据库不存在");
                  resultResponse.setSuccess(false);
                  return resultResponse;
              }
              //判断当前手机号是否存在
              CompanyMembersBean com1 = new CompanyMembersBean();
              com1.setMemberPhone(newMemberPhone);
              List<CompanyMembersBean>  hasCom = companyMembersService.selectByPhoneOrLogname(com1);
              if(null!=hasCom && hasCom.size()>0){
                  resultResponse.setMessage("手机号被使用");
                  resultResponse.setSuccess(false);
                  return resultResponse;
              }

              CompanyMembersBean com2 =  new CompanyMembersBean();
              com2.setMemberId(memberId);
              com2.setMemberLogname(newMemberPhone);
              com2.setMemberPhone(newMemberPhone);
              com2.setMemberName(userName);
              int a =companyMembersService.updateByPrimaryKeySelective(com2);
              if(a!=1||a<0){
                  resultResponse.setMessage("修改异常");
                  resultResponse.setSuccess(false);
                  return resultResponse;
              }

              resultResponse.setSuccess(true);
              resultResponse.setMessage("成功");
              resultResponse.setData(companyMembersDto.getUserName());
              resultResponse.setMsgCode(newMemberPhone);
              request.getSession().removeAttribute("newPhoneNumber");//清除session值
              request.getSession().setAttribute("memberLogname",newMemberPhone);
              request.getSession().setAttribute("memberPhone", newMemberPhone);
              request.getSession().setAttribute("memberid", memberId);
              request.getSession().setAttribute("userName", userName);
              CompanyMembersBean resultBean = (CompanyMembersBean) request.getSession().getAttribute(CommonConstants.LOGIN_USER_KEY);
              resultBean.setMemberLogname(companyMembersDto.getNewMemberPhone());
              request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);//重新设置session
              request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean); //重新设置session
      }

      }catch ( BusinessException e ){
              LOGGER.info("当前修改手机号异常信息："+e.getMessage());
          resultResponse.setMessage(e.getMessage());
          resultResponse.setSuccess(false);
          return resultResponse;
      }

        return resultResponse;
    }

    /*
       * <p>注册手机验证码短息发送</p>
       * @auther 何成彪
       * @createTime2016/6/26 11:57
       */
    @RequestMapping(value = "sendMShortMessageCompanyModifyPhone.htm")
    @ResponseBody
    public ResultResponse sendMShortMessageCompanyRegist(HttpServletRequest request) {

        ResultResponse resultResponse = new ResultResponse();
        String memberPhone =null;

        if(!org.apache.commons.lang3.StringUtils.isBlank(request.getParameter("memberPhone"))){
             memberPhone = request.getParameter("memberPhone");
             request.getSession().setAttribute("newPhoneNumber",memberPhone);
        }
        CompanyMembersBean companyMembersBean = getLoginUserObj(request);
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


}
