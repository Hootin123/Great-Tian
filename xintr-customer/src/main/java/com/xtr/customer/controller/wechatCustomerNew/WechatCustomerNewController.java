package com.xtr.customer.controller.wechatCustomerNew;


import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;

import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.customer.CustomerWechatBean;

import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.customer.CustomerPayrollWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatDto;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.h5wallet.HongbaoService;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.wechatCustomer.WechatCustomerBindService;

import com.xtr.comm.basic.BusinessException;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:zhangshuai
 * @date: 2016/11/9.
 */
@Controller
@RequestMapping("wechatCustomer")
public class WechatCustomerNewController extends BaseController{


    private static final Logger LOGGER = LoggerFactory.getLogger(WechatCustomerNewController.class);

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

    @Resource
    private CompanyProtocolsService  companyProtocolsService;

    /**
     * 工资单入口
     * @param request
     * @return
     */
    @RequestMapping(value="payOrderEntrance.htm")
    @ResponseBody
    public ModelAndView  payOrderEntrance(HttpServletRequest request,ModelAndView modelAndView) {

        try{
            LOGGER.info("员工微信查看工资单请求开始》》》》》》》》》");
            WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);//获取员工微信
            LOGGER.info("员工微信查看工资单请求返回openId：" + openIdByRequest.getOpenid());

            String openId = openIdByRequest.getOpenid(); //获取微信号的opneId
            CustomerWechatBean customerWechatBean = customersService.selectCustomerWechatByOpenId(openId); //查询当前是否有员工绑定记录
            if(null==customerWechatBean){
                //去绑定
                modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerBind");
                modelAndView.addObject("from","seePayOrder");//来自查看工资单的入口
                return modelAndView;
            }else{
                //根据手机号查询
                Long companyId = customerWechatBean.getWechatCompanyId();
                Long customerId = customerWechatBean.getWechatCustomerId();
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

                   //跳转至工资单页面
                   int currentYear =Calendar.getInstance().get(Calendar.YEAR);
                   modelAndView.addObject("companyId",companyId);//公司id
                   modelAndView.addObject("customerId",customerId);//员工id
                   modelAndView.addObject("currentYear",currentYear);//当前年份
                   modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPayOrder");
            }

        }catch (Exception e){
            LOGGER.error("工资单入口访问出现异常信息："+e.getMessage(),e);
        }

      return modelAndView;
    }

    /**
     * 个人资料入口
     * @param request
     * @param modelAndView
     * @return
     */
   @RequestMapping(value="personalInfoEntrance.htm")
   public ModelAndView  personalInfoEntrance(HttpServletRequest request,ModelAndView modelAndView){

        try{
//            LOGGER.info("员工微信查看个人资料请求开始 》》》》》》》》》");
//            WeChatSessionInfo openIdByRequest = weChatUtil.getOpenIdByRequest(request);//获取员工微信
//            LOGGER.info("员工微信查看个人资料返回openId：" + openIdByRequest.getOpenid());

           // String openId = openIdByRequest.getOpenid(); //获取微信号的opneId
            CustomerWechatBean customerWechatBean = customersService.selectCustomerWechatByOpenId("oZYHMsxH4B26fGY8yJ75Slmyt2_o"); //查询当前是否有员工绑定记录
            if(null==customerWechatBean){
                //去绑定
                modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerBind");
                modelAndView.addObject("from","personInfo");//来自个人资料的绑定
                return modelAndView;

            }else {

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

                //判断该公司是否签约
                //判断信息是否补全
                Long companyId =customer.getCustomerCompanyId();
                Long customerId = customer.getCustomerId();
                List<CompanyProtocolsBean> protocolsBeanList = companyProtocolsService.selectUsefulListForCompanyId(companyId);
                CustomerWechatDto  customerWechatDto =  wechatCustomerBindService.selectCustomerInfo(Long.valueOf(customerId));
                if(null!=customerWechatDto){
                    if(!StringUtils.isStrNull(customerWechatDto.getCustomerIdcardImgFront())){
                        customerWechatDto.setCustomerIdcardImgFrontCopy(customerWechatDto.getCustomerIdcardImgFront());
                        customerWechatDto.setCustomerIdcardImgFront("http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerWechatDto.getCustomerIdcardImgFront());

                    }
                    if(!StringUtils.isStrNull(customerWechatDto.getCustomerIdcardImgBack())){
                        customerWechatDto.setCustomerIdcardImgBackCopy(customerWechatDto.getCustomerIdcardImgBack());
                        customerWechatDto.setCustomerIdcardImgBack("http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerWechatDto.getCustomerIdcardImgBack());

                    }
                }
                if(null==protocolsBeanList || protocolsBeanList.size()<=0){
                    if(null!=customerWechatDto){
                        //未签约协议 补全
                        if(customerWechatDto.getCustomerIsComplement()==1){
                            modelAndView.addObject("Flag","noprotocol-buquan");//未签约  补全
                            modelAndView.addObject("customerWechatDto",customerWechatDto);
                            modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
                        }else{
                            modelAndView.addObject("Flag","noprotocol-nobuquan");//未签约  未补全
                            modelAndView.addObject("customerWechatDto",customerWechatDto);
                            modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
                        }
                    }
                }else{
                    //有协议
                    if(null!=customerWechatDto){
                        if(customerWechatDto.getCustomerIsComplement()==1){
                            //补全了  有协议  直接显示
                            modelAndView.addObject("Flag","protocol-buquan");//签约  补全
                            modelAndView.addObject("customerWechatDto",customerWechatDto);
                            modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
                        }else{
                            //去信息收集页面
                            modelAndView.addObject("Flag","protocol-buquan");//签约  未补全
                            modelAndView.addObject("customerWechatDto",customerWechatDto);
                            modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerInfoCollection");
                        }

                    }

                }

            }
       }catch (Exception e){
        LOGGER.error("访问个人资料请求出现异常错误："+e.getMessage(),e);
       }
       return modelAndView;
   }

    /**
     * 员工绑定
     * @return
     */
   @RequestMapping(value="bind.htm")
   @ResponseBody
   public ResultResponse  customerBind(HttpServletRequest request,String phoneNumber,String phoneCode,String from){

       ResultResponse resultResponse =  new ResultResponse();

       try{
           //获取微信号openId
           Map<String,String> map = this.wechatUserInfo(request);
//           map.put("openId","22221111");
           if(StringUtils.isEmpty(map.get("openId"))){
               resultResponse.setMessage("openId没获取到！");
               return resultResponse;
           }
           //查询是否绑定了

           CustomerWechatBean customerWechatBean = customersService.selectCustomerWechatByOpenId(map.get("openId"));
           if(null==customerWechatBean){
             resultResponse = wechatCustomerBindService.customerBind(phoneNumber,phoneCode,map);
           }else{
               //已绑定
               resultResponse.setMessage("您已绑定，请退出重新点击查询！");
           }
        }catch (Exception e){
         resultResponse.setMessage(e.getMessage());
         LOGGER.error("员工绑定出现异常错误："+e.getMessage(),e);
      }
     return resultResponse;
   }


    /**
     * 绑定失败
     * @param modelAndView
     * @return
     */
   @RequestMapping(value="bindFail.htm")
   public ModelAndView  toBindDefeat(ModelAndView modelAndView){
       modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerBindDefeat");
       return  modelAndView;
   }

    /**
     * 绑定成功
     * @param modelAndView
     * @return
     */
  @RequestMapping(value="bindSuccess.htm")
  public ModelAndView toBindSuccess(ModelAndView modelAndView,String customerName,String customerId,String  companyId,String from,
                                    String phoneNumber) throws UnsupportedEncodingException {

      modelAndView.addObject("customerName",new String(customerName.getBytes("iso8859-1"),"utf-8"));
      modelAndView.addObject("customerId",customerId);
      modelAndView.addObject("companyId",companyId);
      modelAndView.addObject("from",from);
      modelAndView.addObject("phone",phoneNumber);
      modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerBindSuccess");
      return modelAndView;
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
     * 跳转到工资单列表页面
     * @param modelAndView
     * @param customerId
     * @param companyId
     * @return
     */
    @RequestMapping(value="toPayOrderPage.htm")
    public  ModelAndView  toPayOrderPage(ModelAndView modelAndView,String customerId,String companyId){

        LOGGER.info("跳转至工资单列表页面参数：customerId ==="+customerId+"   companyId ===="+companyId);
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        modelAndView.addObject("customerId",customerId);
        modelAndView.addObject("companyId",companyId);
        modelAndView.addObject("currentYear",currentYear);
        modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPayOrder");
        return modelAndView;
    }


    /**
     * 查询该员工 年度工资
     * @param request
     * @return
     */
    @RequestMapping(value="queryYearOrders.htm")
    @ResponseBody
    public ResultResponse  queryYearPayOrders(HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();
        try{
            String companyId = request.getParameter("companyId");
            String customerId = request.getParameter("customerId");
            String year = request.getParameter("currentYear");

            //如果没有传值当前年 则系统默认的年份
            if(StringUtils.isStrNull(year)){
                 year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            }

            if(StringUtils.isStrNull(companyId)||StringUtils.isStrNull(customerId)||StringUtils.isStrNull(year)){
                resultResponse.setMessage("参数为空，请退出重新进入");
                return resultResponse;
            }

            List<CustomerWechatBindDto> orderPayOrders = wechatCustomerBindService.selectYearPayorders(companyId,customerId,year);
            String payDayStr ="";
            List<CustomerWechatBindDto> lists = new ArrayList<CustomerWechatBindDto>();
            if(null!=orderPayOrders && orderPayOrders.size()>0){
                    SimpleDateFormat sm = new SimpleDateFormat("yyyy.MM.dd");
                    for (CustomerWechatBindDto cu :orderPayOrders){
                        if(!StringUtils.isEmpty(cu.getPayDay())){
                            payDayStr=sm.format(cu.getPayDay());
                            cu.setPayDayStr(payDayStr);
                            lists.add(cu);
                        }
                    }
            }
            resultResponse.setSuccess(true);
            resultResponse.setData(lists);
        }catch (Exception e){
           LOGGER.error("员工查询年度工资出现异常错误："+e.getMessage(),e);

        }
        return resultResponse;
    }



    /**
     * 跳转至工资详情页面
     * @param modelAndView
     * @param request
     * @return
     */
    @RequestMapping(value="toPayOrderDetail.htm")
    public  ModelAndView  toPayOrderDetail(ModelAndView modelAndView,HttpServletRequest request){

      String customerId = request.getParameter("customerId");
      String payrollId = request.getParameter("payrollId");
      String companyId = request.getParameter("companyId");
      String payCycleId = request.getParameter("payCycleId");
      String month = request.getParameter("month");

      LOGGER.info("跳转至工资详情页面参数："+customerId+" 公司id："+companyId+" 工资单id："+payrollId);


      //查询工资单详情
      if(StringUtils.isStrNull(customerId)||StringUtils.isStrNull(payrollId)||StringUtils.isStrNull(companyId)||
              StringUtils.isStrNull(payCycleId)){
          LOGGER.info("页面传递必须参数为空");
      }
        //查询当前的工资单详情
        CustomerPayrollWechatBindDto  salaryDetail = wechatCustomerBindService.selectSalaryDetailBy(payrollId,companyId,customerId,payCycleId);

        //查询奖金与津贴
        //查询所有津贴与奖金
        CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
        customerPayrollDto.setCustomerId(Long.parseLong(customerId));
        customerPayrollDto.setCompanyId(Long.parseLong(companyId));
        customerPayrollDto.setPayCycleId(Long.parseLong(payCycleId));

        List<CustomerPayrollDto> list =customerPayrollService.selectCustomerPayroll(customerPayrollDto);
        if(list!=null&&list.size()>0){
            modelAndView.addObject("bonusList",list.get(0).getBonusList());
            modelAndView.addObject("allowanceList",list.get(0).getAllowanceList());
        }
        modelAndView.addObject("month",month);
        if(null!=salaryDetail){
            modelAndView.addObject("detail",salaryDetail);
        }
        // todo 社保提醒
       modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerOrderDelate");

       return modelAndView;
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
    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) {
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
                    resultResponse.setMessage("上传成功");
                    //更新数据库
//                    Map<String,Object> map = new HashMap<String,Object>();
//                    map.put("customerId",request.getSession().getAttribute("customerIdNew"));
//                    map.put("customerImg",newFileName);
//                    resultResponse = customersService.updateCustomerImgByCustomerId(map);//更新员工头像
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
     * 员工信息收集
     * @return
     */
    @RequestMapping(value="personInfoCollect.htm")
    @ResponseBody
    public ResultResponse  personInfoCollection(HttpServletRequest request,CustomersBean customer){

      ResultResponse resultResponse = new ResultResponse();
      try{
          Map<String,Object> map = new HashMap<String,Object>();
          //获取参数
          int updateFlag = customersService.updateCustomerInfoByCustomerId(customer);
          if(updateFlag==1){
          resultResponse.setSuccess(true);
          }

      }catch (Exception e){
          resultResponse.setMessage(e.getMessage());
          LOGGER.error("员工信息收集出现异常错误："+e.getMessage(),e);
      }
      return resultResponse;
    }


    /**
     * 员工查看个人信息绑定后 跳转
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="afterBindToPersonInfo.htm")
    public ModelAndView afterBindToPersonInfo(ModelAndView modelAndView,String companyId,String customerId,String phone){

        if(StringUtils.isStrNull(companyId)||StringUtils.isStrNull(customerId)){
            LOGGER.info("员工查看个人信息绑定后跳转参数为空");
        }
        //判断该公司是否签约
        //判断信息是否补全
        List<CompanyProtocolsBean> protocolsBeanList = companyProtocolsService.selectUsefulListForCompanyId(Long.valueOf(companyId));
        CustomerWechatDto  customerWechatDto =  wechatCustomerBindService.selectCustomerInfo(Long.valueOf(customerId));
        if(null!=customerWechatDto){
            if(!StringUtils.isStrNull(customerWechatDto.getCustomerIdcardImgFront())){
                customerWechatDto.setCustomerIdcardImgFrontCopy(customerWechatDto.getCustomerIdcardImgFront());
                customerWechatDto.setCustomerIdcardImgFront("http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerWechatDto.getCustomerIdcardImgFront());

            }
            if(!StringUtils.isStrNull(customerWechatDto.getCustomerIdcardImgBack())){
                customerWechatDto.setCustomerIdcardImgBackCopy(customerWechatDto.getCustomerIdcardImgBack());
                customerWechatDto.setCustomerIdcardImgBack("http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerWechatDto.getCustomerIdcardImgBack());

            }
        }
        if(null==protocolsBeanList||protocolsBeanList.size()<=0){
            if(null!=customerWechatDto){
                //未签约协议 补全
                if(customerWechatDto.getCustomerIsComplement()==1){
                    modelAndView.addObject("Flag","noprotocol-buquan");//未签约  补全
                    modelAndView.addObject("customerWechatDto",customerWechatDto);
                    modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
                }else{
                    modelAndView.addObject("Flag","noprotocol-nobuquan");//未签约  未补全
                    modelAndView.addObject("customerWechatDto",customerWechatDto);
                    modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
                }
            }
        }else{
            //有协议
           if(null!=customerWechatDto){
               if(customerWechatDto.getCustomerIsComplement()==1){
                   //补全了  有协议  直接显示
                   modelAndView.addObject("Flag","protocol-buquan");//签约  补全
                   modelAndView.addObject("customerWechatDto",customerWechatDto);
                   modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
               }else{
                   //去信息收集页面
                   modelAndView.addObject("Flag","protocol-buquan");//签约  未补全
                   modelAndView.addObject("customerWechatDto",customerWechatDto);
                   modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerInfoCollection");
               }

           }

        }

        return modelAndView;
    }

    /**
     * 信息采集后
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="hasCollectionInfo.htm")
    public  ModelAndView hasCollectionInfo(ModelAndView modelAndView,String from,Long customerId){
       if(StringUtils.isStrNull(from)||StringUtils.isEmpty(customerId)){
           LOGGER.info("信息采集后跳转参数为空");
       }
        CustomerWechatDto  customerWechatDto =  wechatCustomerBindService.selectCustomerInfo(customerId);
        if(null!=customerWechatDto){
            if(!StringUtils.isStrNull(customerWechatDto.getCustomerIdcardImgFront())){
                customerWechatDto.setCustomerIdcardImgFrontCopy(customerWechatDto.getCustomerIdcardImgFront());
                customerWechatDto.setCustomerIdcardImgFront("http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerWechatDto.getCustomerIdcardImgFront());

            }
            if(!StringUtils.isStrNull(customerWechatDto.getCustomerIdcardImgBack())){
                customerWechatDto.setCustomerIdcardImgBackCopy(customerWechatDto.getCustomerIdcardImgBack());
                customerWechatDto.setCustomerIdcardImgBack("http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customerWechatDto.getCustomerIdcardImgBack());

            }
        }
        modelAndView.addObject("customerWechatDto",customerWechatDto);
        modelAndView.addObject("Flag",from);
        modelAndView.setViewName("xtr/wechatCustomerNew/newWechatCustomerPersonalData");
       return modelAndView;
    }


}
