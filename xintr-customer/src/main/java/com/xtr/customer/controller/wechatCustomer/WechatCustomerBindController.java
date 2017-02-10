package com.xtr.customer.controller.wechatCustomer;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xtr.api.basic.ResultResponse;

import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomerWechatBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.customer.CustomerPayrollWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatBindDto;
import com.xtr.api.dto.customer.CustomerYearSalaryDto;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.h5wallet.HongbaoService;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.wechatCustomer.WechatCustomerBindService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.wechat.util.WeChatSessionInfo;
import com.xtr.wechat.util.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 微信绑定
 * @author:zhangshuai
 * @date: 2016/9/6.
 */
@Controller
@RequestMapping("wechatCustomerBind")
public class WechatCustomerBindController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatCustomerBindController.class);
    private  static final SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private WeChatUtil weChatUtil;

    @Resource
    private CustomersService customersService;

    @Resource
    private HongbaoService hongbaoService;

    @Resource
    private WechatCustomerBindService wechatCustomerBindService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CustomerPayrollService customerPayrollService;

    @RequestMapping(value="testBindSendMsg.htm")
    public ModelAndView testBindSendMsg(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/wechatCustomer/wechatCustomerBind");
        return modelAndView;
    }



    /**
     * 员工微信端入口
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="customerBindEntrance.htm")
    public ModelAndView customerBindEntrance(ModelAndView modelAndView,HttpServletRequest request){

        /**
         * 微信端打开 1、获取openid 2、判断员工是否有绑定的微信号 3、没有绑定或者没有查询到当权员工的信息 则跳转到员工绑定页面
         *            4、查询到当前员工的绑定记录则去显示员工首页信息
         */
        try {
            LOGGER.info("微信请求开始。。。。。。。");
            WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);
            LOGGER.info("员工微信端入口请求微信端返回："+openIdByRequest.getOpenid());
           //获取微信号的opneId
            String openId = openIdByRequest.getOpenid();
            //查询当前是否有员工绑定记录
            CustomerWechatBean customerWechatBean = customersService.selectCustomerWechatByOpenId(openId);
            if(null==customerWechatBean){
                //跳转至绑定页面
                modelAndView.setViewName("xtr/wechatCustomer/wechatCustomerBind");
            }else{
                //判断是否完善信息了
                //根据手机号查询
                String phone =customerWechatBean.getWechatCustomerPhone();
                //查询用户
               CustomersDto customer =null;
               List<CustomersDto> list = customersService.selectLastCustomerByPhone(phone);
               for (CustomersDto customersDto:list){
                   if(customersDto.getStationCustomerState()==1||customersDto.getStationCustomerState()==2){
                       //在职状态
                       customer = customersDto;
                       break;
                   }
               }
               if(customer==null){
                   customer = list.get(0);
               }
             //存放session
               CompanysBean companysBean = companysService.selectCompanyByCompanyId(customer.getCustomerCompanyId());
               request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY,companysBean);
                if(customer.getCustomerIsComplement()==1){
                   //判断是否看过企业规范
                    int count=customersService.selectCountForIsRedirect(customer.getCustomerId());
                    if(count>0){
                        //更改跳到入职规范状态
                        CustomersBean modifyCustomersBean=new CustomersBean();
                        modifyCustomersBean.setCustomerId(customer.getCustomerId());
                        modifyCustomersBean.setCustomerIsRedirect(CustomerConstants.CUSTOMER_ISCOMPLEMENT_NO);
                        customersService.updateIsRedirectState(modifyCustomersBean);
                        //跳转到入职须知
                        //查询入职须知
                        CompanysBean cop =companysService.selectCompanyEnterRequireById(customer.getCustomerCompanyId());
                        modelAndView.addObject("companyEnterRequire",cop.getCompanyEnterRequire());//入职须知
                        request.getSession().setAttribute("companyEnterRequire",cop.getCompanyEnterRequire());
                        modelAndView = new ModelAndView("redirect:toJoinTask.htm?customerId="+customer.getCustomerId()+"&companyId="+customer.getCustomerCompanyId());
                        LOGGER.error("跳转到入职须知接口参数：customerId======"+customer.getCustomerId());
                        return modelAndView;
                    }

                }else {
                    //跳到个人信息页面
                    modelAndView = new ModelAndView("redirect:/wechatCustomerInfo/toWechatCustomerMainInfoPage.htm?customerId="+customer.getCustomerId()+"&companyId="+customer.getCustomerCompanyId());
                    LOGGER.error("跳转到个人完善信息接口参数：customerId======"+customer.getCustomerId()+" companyId====="+customer.getCustomerCompanyId());
                    return  modelAndView;
                }

                Long customerId = customer.getCustomerId();
                Long companyId = customer.getCustomerCompanyId();
                //根据手机号查询员工信息
                LOGGER.error("跳转至员工首页：customerId======"+customerId+" companyId====="+companyId);
                modelAndView = new ModelAndView("redirect:toCustomerIndex.htm?customerId="+customerId+"&companyId="+companyId);
            }

        }catch ( Exception e ){
            LOGGER.error("员工微信端入口请求出现异常错误："+e.getMessage(),e);
        }

        return modelAndView;
    }

    /**
     * 跳转到员工首页
      * @param modelAndView
     * @param request
     * @return
     */
   @RequestMapping(value="toCustomerIndex.htm")
   public ModelAndView jumpToCustomerIndexPage(ModelAndView modelAndView,HttpServletRequest request){
       try{
           LOGGER.info("跳转到员工首页");

           //获取员工id 及企业id
           String customerId = request.getParameter("customerId");
           String companyId = request.getParameter("companyId");

           LOGGER.error("跳到首页customerId："+customerId+"  companyId公司id："+companyId);
           /**
            * 1、员工姓名 头像、 距离下个月发工资的时间
            *
            * 2、员工该年度所有发放工资单的列表
            *
            * 3、工资详情
            */
           //获取员工的基本信息
           modelAndView.setViewName("xtr/wechatCustomer/wechatCustomerIndex");
           Map<String,Object> map1 = new HashMap<String,Object>();
           Map<String,Object> map2 = new HashMap<String,Object>();
           map1.put("customerId",customerId);
           map1.put("companyId",companyId);
           request.getSession().setAttribute("customerIdNew",customerId);
           CustomerWechatBindDto customerWechatBindDto = wechatCustomerBindService.selectCustomerInfoByCondition(map1);
           //获取当前的年份
           String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
           //查询当前员工本年度的工资
           Long payCycleId = null;
           if(customerWechatBindDto!=null){
               payCycleId = customerWechatBindDto.getPayCycleId();
           }
           map2.put("customerId",customerId);
           map2.put("companyId",companyId);
           map2.put("year",currentYear);
           map2.put("payCycleId",payCycleId);//记薪周期id
           CustomerYearSalaryDto currentMonthSalary = wechatCustomerBindService.selectCustomerYearSalarys(map2);
           if(null!=customerWechatBindDto) {
               //员工姓名 头像
               // modelAndView.addObject("customerImg",customerWechatBindDto.getCustomerImg());
               if (customerWechatBindDto != null && !StringUtils.isStrNull(customerWechatBindDto.getCustomerImg())) {
                   modelAndView.addObject("customerImgUrl", "http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/" + customerWechatBindDto.getCustomerImg());
               } else {
                   modelAndView.addObject("customerImg", "");
               }
               modelAndView.addObject("customerTurename", customerWechatBindDto.getCustomerTurename());//姓名
               //当前年份
               modelAndView.addObject("year", customerWechatBindDto.getYear());
               modelAndView.addObject("month", customerWechatBindDto.getMonth());
               modelAndView.addObject("currentYear", currentYear);

               int days = returnDays(customerWechatBindDto.getPayDay());
               LOGGER.info("距下个月发薪还有：：：：："+days);
               modelAndView.addObject("days", days);//距下个月发工资还剩多少天
               modelAndView.addObject("payCycleId", customerWechatBindDto.getPayCycleId());//记薪周期id
               //当前那个月份高亮
               if (currentMonthSalary != null) {
                   modelAndView.addObject("month", currentMonthSalary.getMonth());
                   //工资详情
                   modelAndView.addObject("baseWages", StringUtils.isEmpty(currentMonthSalary.getRealWage())==true?0:currentMonthSalary.getRealWage());//实发工资
                   modelAndView.addObject("payrollId", currentMonthSalary.getCustomerPayrollId());//工资单id
               } else {
                   modelAndView.addObject("baseWages", 0);//实发工资
               }
               modelAndView.addObject("companyId", companyId);//公司Id
               modelAndView.addObject("customerId", customerId);//公司Id
           }else{
               CustomersBean customersBean = customersService.selectNameById(Long.valueOf(customerId));
               modelAndView.addObject("currentYear", currentYear);
               modelAndView.addObject("month", String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1));
               modelAndView.addObject("companyId", companyId);//公司Id
               modelAndView.addObject("customerId", customerId);//公司Id
               modelAndView.addObject("customerImg", "");
               modelAndView.addObject("days","30");
               modelAndView.addObject("baseWages",0);
              modelAndView.addObject("customerTurename",customersBean.getCustomerTurename());
           }
        }catch ( Exception e ){
           e.printStackTrace();
          LOGGER.error("跳转到员工首页出现异常错误"+e.getMessage());
        }
        return modelAndView;
   }

    //返回当前日距下个月发薪日的天数
    private int returnDays(Date payDay) throws ParseException {
        int days=0;
        SimpleDateFormat sm = new SimpleDateFormat("MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.setTime(payDay);
        int dayPay =ca.get(Calendar.DATE);//发薪日
        int dayMonth=ca.get(Calendar.MONTH)+1;//发薪月

        //当前时间
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DATE);
        int currentMonth = cal.get(Calendar.MONTH)+1;

       if(currentDay>dayPay){
           currentMonth+=1;
           String str1=currentMonth+"-"+dayPay;
           String str2=cal.get(Calendar.MONTH)+1+"-"+currentDay;
           //两个日期的相隔天数
           Date date1 = sm.parse(str1);
           Date date2 = sm.parse(str2);
           Long diffTimes = date1.getTime() - date2.getTime();
           Long diffDays = diffTimes / (3600 * 1000 * 24);

           days= Math.abs(diffDays.intValue());
       }else{
           days= Math.abs(dayPay-currentDay);
       }

        return days;
    }


    /**
     * 跳转至入职任务页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toJoinTask.htm")
    public ModelAndView jumpToJoinTaskPage(ModelAndView modelAndView,HttpServletRequest request){

        if(!StringUtils.isStrNull(request.getParameter("customerId"))){
            modelAndView.addObject("customerId",request.getParameter("customerId"));
            request.getSession().setAttribute("customerIdNew",request.getParameter("customerId"));
        }
        //获取公司id
        if(!StringUtils.isStrNull(request.getParameter("companyId"))){
            modelAndView.addObject("companyId",request.getParameter("companyId"));
        }
        CompanysBean cop =companysService.selectCompanyEnterRequireById(Long.valueOf(request.getParameter("companyId")));
        if(cop!=null){
            modelAndView.addObject("companyEnterRequire",cop.getCompanyEnterRequire());//入职须知
            request.getSession().setAttribute("companyEnterRequire",cop.getCompanyEnterRequire());
        }
        LOGGER.info("跳转到员工入职规范页面参数： 员工Id，"+request.getParameter("customerId")+"  公司id，"+request.getParameter("companyId"));
        modelAndView.setViewName("xtr/wechatCustomer/wechatCustomerEntry");
        return modelAndView;
    }


    /**
     * 跳转至工资详情页面
     * @param modelAndView
     * @return
     */
   @RequestMapping(value="toCustomerSalaryDetail.htm")
   public ModelAndView jumpToSalaryDetail(ModelAndView modelAndView,HttpServletRequest request){

       try{
           Long payrollId =null;
           if(!StringUtils.isStrNull(request.getParameter("payrollId"))) {
               //获取该年该月份的工资单
               payrollId= Long.valueOf(request.getParameter("payrollId"));//工资单id
           }
           //记薪周期id
            Long payCycleId = Long.valueOf(request.getParameter("payCycleId"));
           //年月
           String year = request.getParameter("year");
           String month = request.getParameter("month");
           LOGGER.info("当前月份》》》》》"+month);
           long customerId = Long.valueOf(request.getParameter("customerId"));//员工id;
           long companyId =Long.valueOf(request.getParameter("companyId"));
           LOGGER.error("跳转到公司详情： 员工Id，"+customerId+"  公司id："+companyId);

           Map<String,Object> map = new HashMap<>();
           map.put("customerId",customerId);
           map.put("payrollId",payrollId);//工资单id
           map.put("payCycleId",payCycleId);
           map.put("year",year);
           map.put("month",month);
           CustomerPayrollWechatBindDto cuto = wechatCustomerBindService.selectSalaryDetail(map);
           //查询所有津贴与奖金
           CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
           customerPayrollDto.setCustomerId(customerId);
           customerPayrollDto.setCompanyId(companyId);
           customerPayrollDto.setPayCycleId(payCycleId);

           List<CustomerPayrollDto> list =customerPayrollService.selectCustomerPayroll(customerPayrollDto);
           if(list!=null&&list.size()>0){
               modelAndView.addObject("bonusList",list.get(0).getBonusList());
               modelAndView.addObject("allowanceList",list.get(0).getAllowanceList());
           }
//           List<CustomerPayrollWechatBindDto>  list = wechatCustomerBindService.selectPayrollDetailByPayrollId(payrollId);
           modelAndView.addObject("cuto",cuto);
           modelAndView.addObject("month",month);
           modelAndView.setViewName("xtr/wechatCustomer/wechatCustomerSalary");
       }catch ( Exception e ){
              LOGGER.error("跳转到工资详情出现异常错误："+e.getMessage());
       }

       return modelAndView;
   }

    /**
     * 点击每个月份返回该月员工的工资值
     * @return
     */
    @RequestMapping(value="searchMonthSalary.htm")
    @ResponseBody
    public ResultResponse searchMonthSalary(HttpServletRequest  request){
        ResultResponse resultResponse = new ResultResponse();
        try{
            //获取年份
            String year = request.getParameter("year");
            String month = request.getParameter("month");
            //获取记薪周期id

            String customerId= request.getParameter("customerId");
            LOGGER.error("点击每个月份返回该月员工的工资值：customerId"+customerId);
            //公司id
            String companyId = request.getParameter("companyId");

            if(!StringUtils.isStrNull(year)&&!StringUtils.isStrNull(month)&&!StringUtils.isStrNull(customerId)){
                Map<String,Object> map = new HashMap();
                map.put("customerId",customerId);
                map.put("companyId",companyId);
                map.put("year",year);
                map.put("month",month);
                CustomerWechatBindDto customerWechatBindDto = wechatCustomerBindService.selectCustomerInfoByCondition(map);
                if(null!=customerWechatBindDto){
                map.put("year",year);
                map.put("month",month);
                map.put("payCycleId",customerWechatBindDto.getPayCycleId());
                }else{
                    resultResponse.setMessage("工资还没有发");
                    return resultResponse;
                }

                CustomerYearSalaryDto currentMonthSalary = wechatCustomerBindService.selectCustomerYearSalarys(map);
                if(null==currentMonthSalary){
                    LOGGER.info("当前年月工资还没有");
                    resultResponse.setMessage("查询失败，您本月还没有工资");
                }else{
                    resultResponse.setSuccess(true);
                    resultResponse.setMessage("查询成功");
                    //封装返回参数
                    Map<String,Object> resultMap = new HashMap<String,Object>();
                    resultMap.put("payCycleId",customerWechatBindDto.getPayCycleId());//计薪周期id
                    resultMap.put("baseWages",currentMonthSalary.getRealWage());//工资
                    resultMap.put("payrollid",currentMonthSalary.getCustomerPayrollId());//工资单id
                    resultResponse.setData(resultMap);
                }
            }else{
                resultResponse.setMessage("查询失败");
            }
        }catch ( Exception e ){
            LOGGER.error("查询当月基本工资出现错误："+e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }

        return resultResponse;
    }






    /**
     * 点击年份查询该年度 1月份的工资   默认显示1月份工资高亮
     * @return
     */
    @RequestMapping(value="getNextYearJanSalary.htm")
    @ResponseBody
    public ResultResponse getYearMonthSalary(HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();
        try{

            try{
                //获取年份
                String year = request.getParameter("year");
                //获取记薪周期id

                String customerId= request.getParameter("customerId");
                LOGGER.error("点击年份查询该年度 1月份的工资   默认显示1月份工资高亮 customerId:"+customerId);
                //公司id
                String companyId = request.getParameter("companyId");

                if(!StringUtils.isStrNull(year)&&!StringUtils.isStrNull(customerId)){
                    Map<String,Object> map = new HashMap();
                    map.put("customerId",customerId);
                    map.put("companyId",companyId);
                    map.put("year",year);
                    CustomerWechatBindDto customerWechatBindDto = wechatCustomerBindService.selectCustomerInfoByCondition(map);
                    if(null!=customerWechatBindDto){
                        map.put("year",year);
                        map.put("payCycleId",customerWechatBindDto.getPayCycleId());
                    }else{
                        resultResponse.setMessage("查询失败");
                        return resultResponse;
                    }

                    CustomerYearSalaryDto currentMonthSalary = wechatCustomerBindService.selectCustomerYearSalarys(map);
                    if(null==currentMonthSalary){
                        LOGGER.info("当前年月工资还没有");
                        resultResponse.setMessage("查询失败，您本月还没有工资");
                    }else{
                        resultResponse.setSuccess(true);
                        resultResponse.setMessage("查询成功");
                        //封装返回参数
                        Map<String,Object> resultMap = new HashMap<String,Object>();
                        resultMap.put("payCycleIdValue",customerWechatBindDto.getPayCycleId());//计薪周期id
                        resultMap.put("baseWages",currentMonthSalary.getRealWage());//工资
                        resultMap.put("payrollid",currentMonthSalary.getCustomerPayrollId());//工资单id
                        resultMap.put("month",currentMonthSalary.getMonth());
                        resultResponse.setData(resultMap);
                    }

                }else{
                    resultResponse.setMessage("查询失败");
                }
            }catch ( Exception e ){
                LOGGER.error("查询当月基本工资出现错误："+e.getMessage());
                resultResponse.setMessage(e.getMessage());
            }
        }catch ( Exception e ){
           LOGGER.error("当前查询工资出现错误");

        }

        return resultResponse;
    }



    /**
     * 员工绑定
     * @param request
     * @return
     */
   @RequestMapping(value="customerBind.htm")
   @ResponseBody
   public ResultResponse customerBind(HttpServletRequest request,String phone,String phoneCode){

       ResultResponse resultResponse = new ResultResponse();
       try{
           //获取微信号openId
           Map<String,String> map = this.wechatUserInfo(request);
           resultResponse = wechatCustomerBindService.customerBind(phone,phoneCode,map);
       }catch ( BusinessException e ) {
         LOGGER.error("员工绑定出现异常错误："+e.getMessage());
         resultResponse.setMessage(e.getMessage());
       }catch(Exception e){
           resultResponse.setMessage("员工绑定失败");
           LOGGER.error("员工绑定失败",e);
       }
       return resultResponse;
   }

    /**
     * 判断是否能获取到用户的微信信息
     * @param request
     * @return
     */
    public Map<String,String> wechatUserInfo(HttpServletRequest request) {

        Map<String,String> returnMap = new HashMap<String,String>();//返回信息的map
        try {
            WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);
            if(openIdByRequest!=null) {
                Map map = weChatUtil.userInfo(openIdByRequest.getOpenid());
                //重新封装
                returnMap.put("openId",(String)map.get("openid"));//openId
                returnMap.put("nickName",(String)map.get("nickname"));//昵称
            }


        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return returnMap;

    }
    /**
     * 发送注册手机验证码
     * @param memberPhone
     * @return
     */
    @RequestMapping(value="sendPhoneMsg.htm")
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
     * H5上传营业执照及上传公司logo
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("customerBindUpload.htm")
    @ResponseBody
    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(multipartFile!=null){
                String fileName = multipartFile.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String[] filenames={"jpg", "png", "jpeg"};
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
                if(multipartFile.getSize()>(2*1024*1024)){
                    resultResponse.setMessage("头像大小不能超过2M");
                    resultResponse.setSuccess(false);
                    return JSON.toJSONString(resultResponse);
                }

                boolean result= AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
                if(result){
                    resultResponse.setSuccess(true);

                    //更新数据库
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("customerId",request.getSession().getAttribute("customerIdNew"));
                    map.put("customerImg",newFileName);
                    resultResponse = customersService.updateCustomerImgByCustomerId(map);//更新员工头像
                    resultResponse.setData(newFileName);//新的头像
                }else{
                    LOGGER.error("员工上传头像失败");
                    resultResponse.setMessage("上传失败");
                }
            }else{
                LOGGER.error("员工没有上传头像");
                resultResponse.setMessage("没有上传文件");
            }
        }catch (IOException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("员工上传头像,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }

    /**
     * 修改员工头像
     * @return
     */
    @RequestMapping(value="editCustomerImg.htm")
    @ResponseBody
    public  ResultResponse  editCustomerImg(HttpServletRequest request){

         ResultResponse resultResponse = new ResultResponse();
         try{
              Long customerId = Long.valueOf(request.getParameter("customerId"));
              String customerImg = request.getParameter("customerImg");
              Map<String,Object> map = new HashMap<String,Object>();
              map.put("customerId",customerId);
              map.put("customerImg",customerImg);
              resultResponse = customersService.updateCustomerImgByCustomerId(map);//更新员工头像

         }catch ( Exception e ){
         LOGGER.error("更新员工头像失败");
         resultResponse.setMessage("操作失败");
         }

         return resultResponse;
    }

    /**
     * 跳转至错误页面
     */
    @RequestMapping(value="error.htm")
    public  ModelAndView jumpToErrorPage(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/wechatCustomer/error");
        return modelAndView;
    }

}
