package com.xtr.company.controller.shebao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderDescBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.customer.CustomersSupplementDto;
import com.xtr.api.dto.shebao.ErrorBillDto;
import com.xtr.api.dto.shebao.HistoryBillDto;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.company.controller.customer.CustomerManagerController;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("shebao")
public class CompanyShebaoController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyShebaoController.class);
    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private CustomerShebaoService customerShebaoService;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CustomersService customersService;
    /**
     * 当前账单
     *
     * @param mav
     * @param city 账单地区
     * @return
     */
    @RequestMapping("current_payorder.htm")
    public ModelAndView currentPayorder(HttpServletRequest request, ModelAndView mav, String city) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前企业ID
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        try{
            customerShebaoOrderService.updateNoReadOrders(comapnyId);
        }catch (Exception e){
            LOGGER.error("更新当前所有未读账单为已读："+e.getMessage(),e);
        }

        //根据企业订单获取所有的账单地区
        List<CompanyShebaoOrderBean> cities = companyShebaoService.getOrderCities(comapnyId);
        //获取当前异常账单的数量
        Map map = new HashMap();
        map.put("isCurrent",1);//当前的标识
        map.put("companyId",comapnyId);
        long currentYc =  customerShebaoOrderService.getErrorOrderCount(map);
        LOGGER.info("当前的异常账单数量："+currentYc);
        mav.addObject("cities", cities);
        mav.addObject("currentYc",currentYc);
        mav.setViewName("xtr/shebao/current_payorder");

        return mav;
    }

    /**
     * 获取当前账单列表
     * @param request
     * @param city
     * @return
     */
    @RequestMapping("queryCurrentOrderList.htm")
    public void queryCurrentOrderList(HttpServletRequest request,HttpServletResponse response,String city,String flag) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前企业ID
        Long comapnyId = companyMembersBean.getMemberCompanyId();
        //获取当前账单,待提交账单的数量
        int dtj = companyShebaoService.getCompanyShebaoOrderCount(comapnyId, ShebaoConstants.COMPANY_ORDER_WILLSUBMIT);
        //获取企业的当前账单数量
        int currentCount=companyShebaoService.selectCurrentOrderCount(comapnyId);
        //获取企业的当前账单
        List<CompanyShebaoOrderDto> companyShebaoOrders = companyShebaoService.selectCurrentOrderList(comapnyId, city, ShebaoConstants.CURRENT_ORDER_YES,flag);
        //拼装截止日期提交订单天数提醒
        Date nowDate=new Date();
        if(companyShebaoOrders!=null && companyShebaoOrders.size()>0){
            for(CompanyShebaoOrderDto dto:companyShebaoOrders){
                Date lastTime=dto.getOrderLastTime();
                if(lastTime!=null){
                    String lastTimeStr=DateUtil.dateFormatter.format(lastTime);
                    String nowTimeStr=DateUtil.dateFormatter.format(nowDate);
                    int diffCount=DateUtil.getDiffDaysOfTwoDateByNegative(lastTimeStr,nowTimeStr);
                    diffCount=diffCount+1;//当天的也算
                    if(diffCount>0){
                        dto.setDiffDays(diffCount);
                    }
                }
            }
        }
        ResultResponse resultResponse=new ResultResponse();
        resultResponse.setData(companyShebaoOrders);
        resultResponse.setMessage(String.valueOf(dtj)+"##"+String.valueOf(currentCount));
        resultResponse.setSuccess(true);
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 历史账单
     *
     * @param mav
     * @param city 账单地区
     * @return
     */
    @RequestMapping("history_payorder.htm")
    public ModelAndView history_payorder(HttpServletRequest request, ModelAndView mav, String city) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long companyId = companyMembersBean.getMemberCompanyId();
        Map searchErrorMap = new HashMap();
        searchErrorMap.put("isCurrent",0);//历史的
        searchErrorMap.put("companyId",companyId);
        long yc = customerShebaoOrderService.getErrorOrderCount(searchErrorMap);
        // 账单地区
        List<CompanyShebaoOrderBean> cities = companyShebaoService.getOrderCities(companyId);
        Map map = new HashMap();
        map.put("companyId",companyId);
        ResultResponse resultResponse= companyShebaoService.queryHistoryPaybills(map);
        List<CompanyShebaoOrderDto> companyShebaoOrders = (List<CompanyShebaoOrderDto>) resultResponse.getData();
        mav.addObject("companyShebaoOrders", companyShebaoOrders);
        mav.addObject("yc", yc);
        mav.addObject("cities", cities);
        mav.setViewName("xtr/shebao/history_payorder");
        return mav;
    }

    /**
     * 账单详情
     *
     * @param mav
     * @return
     */
    @RequestMapping("payorder_detail.htm")
    public ModelAndView payorderDetail(ModelAndView mav, HttpServletRequest request,
                                       CustomerShebaoOrderDto customerShebaoOrderDto,
                                       @RequestParam("orderId") Long orderId) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        //获取企业部门
//        List<CompanysDto> companysDtos = companyDepsService.getCompanyTree(comapnyId);
//        mav.addObject("companysDtos", companysDtos);

        List<CompanysDto> companysDtos = companyDepsService.getDepsTreeByCompanyShebaoId(orderId);
        mav.addObject("companysDtos", companysDtos);


        customerShebaoOrderDto.setCompanyShebaoOrderId(orderId);

        if (org.apache.commons.lang.StringUtils.isNotBlank(customerShebaoOrderDto.getMemberName())) {
            mav.addObject("memberName", customerShebaoOrderDto.getMemberName());
        }

        //获取企业订单
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(orderId);
        mav.addObject("shebaoOrder", companyShebaoOrderBean);

//        //获取企业下每个用户账单需求
//        List<CustomerShebaoOrderDto> customerShebaoOrderDtos = customerShebaoService.getCustomerOrders(customerShebaoOrderDto);
//        mav.addObject("data", customerShebaoOrderDtos);
        mav.addObject("companyShebaoOrderId", orderId);
        mav.setViewName("xtr/shebao/payorder_detail");

        return mav;
    }

    /**
     * 获取当前账户所有用户需求
     * @param response
     * @param customerShebaoOrderDto
     */
    @RequestMapping("payorderDetailList.htm")
    public void payorderDetailList(HttpServletResponse response,CustomerShebaoOrderDto customerShebaoOrderDto) {
        LOGGER.info("获取当前账户所有用户需求,传递参数:"+JSON.toJSONString(customerShebaoOrderDto));
        ResultResponse resultResponse=new ResultResponse();
        if(customerShebaoOrderDto==null || customerShebaoOrderDto.getCompanyShebaoOrderId()==null){
            resultResponse.setSuccess(false);
            resultResponse.setMessage("请传递参数");
        }else{
            //获取企业订单
            CompanyShebaoOrderBean orderBean=companyShebaoService.selectByPrimaryKey(customerShebaoOrderDto.getCompanyShebaoOrderId());
            if(orderBean==null || orderBean.getStatus()==null){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("获取不到企业订单信息");
            }else{
                //获取企业下每个用户账单需求
                if(orderBean.getStatus().intValue()>=2){//已提交账单
                    customerShebaoOrderDto.setIsSubmitAccount(1);
                }else{//未提交账单
                    customerShebaoOrderDto.setIsSubmitAccount(2);
                }
                List<CustomerShebaoOrderDto> customerShebaoOrderDtos = customerShebaoService.getCustomerOrders(customerShebaoOrderDto);
                if(customerShebaoOrderDtos!=null && customerShebaoOrderDtos.size()>0){
                    for(CustomerShebaoOrderDto dto:customerShebaoOrderDtos){
                        if(orderBean.getStatus().intValue()>=2){//已提交账单
                            dto.setIsSubmitAccount(1);
                        }else{//未提交账单
                            dto.setIsSubmitAccount(2);
                        }
                    }
                }
                resultResponse.setData(customerShebaoOrderDtos);
                resultResponse.setSuccess(true);
            }

        }
        LOGGER.info("获取当前账户所有用户需求,返回结果:"+JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);

    }
    /**
     * 社保详情
     *
     * @param memerId
     * @param orderId
     * @return
     */
    @RequestMapping("shebao_detail.htm")
    public void shebaoDetail(HttpServletResponse response,
                             @RequestParam("memberId") Long memerId,
                             @RequestParam("orderId") Long orderId) {
        LOGGER.info("查询社保详情,传递参数:员工ID:"+memerId+",企业订单ID:"+orderId);
        ResultResponse resultResponse = new ResultResponse();
        try{
            CustomerShebaoOrderDto customerShebaoOrderDto = customerShebaoOrderService.getShebaoOrder(memerId, orderId, ShebaoConstants.RELATION_TYPE_SHEBAO);
            if(StringUtils.isEmpty(customerShebaoOrderDto)){
                resultResponse.setMessage("查询社保详情失败");
            }else{
                resultResponse.setData(customerShebaoOrderDto);
                resultResponse.setSuccess(true);}
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("查询社保详情"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("查询社保详情失败");
            LOGGER.error("查询社保详情",e);
        }
        LOGGER.info("查询社保详情,返回结果:"+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 公积金详情
     *
     * @param memerId
     * @param orderId
     * @return
     */
    @RequestMapping("gjj_detail.htm")
    public void gjjDetail(HttpServletResponse response,
                          @RequestParam("memberId") Long memerId,
                          @RequestParam("orderId") Long orderId) {

        LOGGER.info("查询公积金详情,传递参数:员工ID:"+memerId+",企业订单ID:"+orderId);
        ResultResponse resultResponse = new ResultResponse();
        try{
            CustomerShebaoOrderDto customerShebaoOrderDto = customerShebaoOrderService.getShebaoOrder(memerId, orderId, ShebaoConstants.RELATION_TYPE_GONGJIJING);
            if(null != customerShebaoOrderDto){
                resultResponse.setData(customerShebaoOrderDto);
                resultResponse.setSuccess(true);
            }
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("查询公积金详情"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("查询社保详情失败");
            LOGGER.error("查询公积金详情",e);
        }
        LOGGER.info("查询公积金详情,返回结果:"+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }


    /**
     * 费用补差页面
     * @param mav
     * @return
     */
    @RequestMapping("addtion_detail.htm")
    public ModelAndView addtion_detail(ModelAndView mav,Long supplementCompanyOrderId,String orderDateStr) {
        //根据企业订单获取所有的账单地区
        CustomersSupplementDto customersSupplementDto=new CustomersSupplementDto();
        if(supplementCompanyOrderId!=null){
            customersSupplementDto = companyShebaoService.queryAddtionDetail(supplementCompanyOrderId);
        }
        mav.addObject("customersSupplementDto", customersSupplementDto);
        mav.addObject("orderDateStr", orderDateStr);
        mav.setViewName("xtr/shebao/addtion_detail");
        return mav;
    }

    /**
     * 提交账单
     * @param id
     * @param type
     * @param response
     */
    @RequestMapping(value = "submit_order.htm", method = RequestMethod.POST)
    public void submit_order(HttpServletRequest request,@RequestParam("orderId") Long id,
                             @RequestParam("type")  String type,
                             HttpServletResponse response) {
        LOGGER.info("提交账单,传递参数:账单类型:"+type+",企业订单ID:"+id);
        ResultResponse resultResponse = new ResultResponse();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Long companyId = companyMembersBean.getMemberCompanyId();
            long startDate = System.currentTimeMillis();
            resultResponse = companyShebaoService.updateSubmitOrder(id, type,companyId);
            if(resultResponse.isSuccess()){
                //重新获取企业订单费用
                long startDateCompanyAssem = System.currentTimeMillis();
                companyShebaoService.updateOrderDetail(id);
                LOGGER.info("重新获取企业订单费用执行时间：" + (System.currentTimeMillis()-startDateCompanyAssem));
                long startDateCompanyUpt = System.currentTimeMillis();
                //获取企业订单原始信息
                CompanyShebaoOrderBean shebaoOrderBean=companyShebaoService.selectByPrimaryKey(id);
                //更改企业订单信息
                CompanyShebaoOrderBean record=(CompanyShebaoOrderBean)resultResponse.getData();
                //获取总金额
                BigDecimal compareDecimal=new BigDecimal("0");
                BigDecimal priceSumDecimal=new BigDecimal("0");
                if(shebaoOrderBean!=null){
                    BigDecimal priceSb=shebaoOrderBean.getPriceSb()!=null?shebaoOrderBean.getPriceSb():compareDecimal;
                    BigDecimal priceGjj=shebaoOrderBean.getPriceGjj()!=null?shebaoOrderBean.getPriceGjj():compareDecimal;
                    BigDecimal priceService=shebaoOrderBean.getPriceService()!=null?shebaoOrderBean.getPriceService():compareDecimal;
                    //priceSumDecimal=record.getShebaotongShebaoAmount().add(record.getShebaotongGjjAmount()).add(priceService);
                    priceSumDecimal=record.getShebaotongTotalAmount().subtract(record.getShebaotongServiceAmount()).add(priceService);
                }
//                if(record.getPriceAddtion()!=null && record.getPriceAddtion().compareTo(compareDecimal)!=0){//加上补差费用
//                    priceSumDecimal=priceSumDecimal.add(record.getPriceAddtion());
//                }
//                if(record.getShebaotongOverdueAmount()!=null && record.getShebaotongOverdueAmount().compareTo(compareDecimal)!=0){//加上滞纳费
//                    priceSumDecimal=priceSumDecimal.add(record.getShebaotongOverdueAmount());
//                }
                record.setPriceSum(priceSumDecimal);
                record.setSubmitTime(new Date());//提交时间
                companyShebaoService.updateByPrimaryKeySelective(record);
                LOGGER.info("更新企业社保公积金社保通费用执行时间：" + (System.currentTimeMillis()-startDateCompanyUpt));
                //生成社保公积金订单
                long startDateGenerateRecharge = System.currentTimeMillis();
                companyShebaoService.generateSalaryOrder(companyId,id,priceSumDecimal);
                LOGGER.info("生成社保公积金订单执行时间：" + (System.currentTimeMillis()-startDateGenerateRecharge));
            }
            LOGGER.info("整个提交账单执行时间：" + (System.currentTimeMillis()-startDate));
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("提交账单"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("提交账单失败");
            LOGGER.error("提交账单",e);
        }
        LOGGER.info("提交账单,返回结果:"+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 订单提交异常页面
     * @param mav
     * @return
     */
    @RequestMapping("order_error.htm")
    public ModelAndView order_error(ModelAndView mav, @RequestParam("orderId") Long orderId,
                                    @RequestParam("is_current") int isCurrent) {
       try {
           List<CustomerShebaoOrderDto> errorOrders = customerShebaoOrderService.selectShebaoErrorOrderForNew(orderId);
           String  sbError =null;
           String gjjError=null;
           List<CustomerShebaoOrderDto> list = new ArrayList<CustomerShebaoOrderDto>();
           for (CustomerShebaoOrderDto cu:errorOrders){
               sbError="";
               gjjError="";
               if(!StringUtils.isStrNull(cu.getSbErrorText())){
                   if(cu.getOrderType()==1){
                       sbError="增员失败：";
                   }else if(cu.getOrderType()==2){
                       sbError="续缴失败：";
                   }else if(cu.getOrderType()==3){
                       sbError="调基失败：";
                   }else if(cu.getOrderType()==4){
                       sbError="停缴失败：";
                   }else if(cu.getOrderType()==5){
                       sbError="补缴失败：";
                   }
                   sbError+=DateUtil.sbtSimpleDateFormat.format(cu.getOverdueMonth());
                   cu.setSbErrorText(sbError);
                }

               if(!StringUtils.isStrNull(cu.getGjjErrorText())){
                   if(cu.getOrderType()==1){
                       sbError="增员失败：";
                   }else if(cu.getOrderType()==2){
                       sbError="续缴失败：";
                   }else if(cu.getOrderType()==3){
                       sbError="调基失败：";
                   }else if(cu.getOrderType()==4){
                       sbError="停缴失败：";
                   }else if(cu.getOrderType()==5){
                       sbError="补缴失败：";
                   }

                  gjjError+= DateUtil.sbtSimpleDateFormat.format(cu.getOverdueMonth());
                  cu.setGjjErrorText(gjjError);
               }
               list.add(cu);
           }

           mav.addObject("errorOrders", list);
           mav.addObject("orderId", orderId);
           mav.addObject("isCurrent", isCurrent);

           mav.setViewName("xtr/shebao/order_error");
       }catch (Exception e){
         LOGGER.error("当前提交出现异常信息："+e.getMessage(),e);
       }
        return mav;
    }


    /**
     * 获取历史账单列表
     * @return
     */
    @RequestMapping(value="queryHistoryPayOrder.htm")
    @ResponseBody
    public ResultResponse queryHistoryPayOrder(HttpServletRequest request, HistoryBillDto historyBillDto){

        ResultResponse resultResponse = new ResultResponse();
        CompanysBean companysBean = SessionUtils.getCompany(request);
        Date orderDate = null;
        try{
            Long companyId =companysBean.getCompanyId();
            if(!StringUtils.isStrNull(historyBillDto.getOrderDate())){
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
                orderDate = sm.parse(historyBillDto.getOrderDate()+"-01");
            }
            Map map = new HashMap<String,Object>();
            map.put("companyId",companyId);
            map.put("orderDate", StringUtils.isStrNull(historyBillDto.getOrderDate())==true?"":orderDate);
            map.put("joinCityCode",historyBillDto.getJoinCityCode());
            //查询该公司是有异常账单

            resultResponse = companyShebaoService.queryHistoryPaybills(map);

        }catch (Exception e){
            LOGGER.error("查询历史账单列表出现错误："+e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 异常账单页面
     * @param  mav
     * @param orderId
     * @return
     */
    @RequestMapping(value="errorBillDetail.htm")
    public ModelAndView errorBillDetail(ModelAndView mav,Long orderId,HttpServletRequest request){

        LOGGER.info("返回到异常信息页面账单id："+orderId);
        //获取企业订单
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(orderId);
        mav.addObject("shebaoOrder", companyShebaoOrderBean);

        mav.addObject("companyShebaoOrderId", orderId);
        mav.setViewName("xtr/shebao/error_payorder_detail");
        return mav;
    }


    /**
     * 查询异常账单下的所有员工信息
     * @param request
     * @param customerShebaoOrderDto
     * @return
     */
    @RequestMapping(value="queryAllErrorBillCustomers.htm")
    @ResponseBody
    public ResultResponse queryAllErrorBillCustomers(HttpServletRequest request,CustomerShebaoOrderDto customerShebaoOrderDto){
        ResultResponse resultResponse = new ResultResponse();
        try{
            LOGGER.info("获取当前账户所有用户需求,传递参数:"+JSON.toJSONString(customerShebaoOrderDto));

            //获取企业下每个用户账单需求
            //异常信息的订单下所有的用户id
            //List<CustomerShebaoOrderDto> customerShebaoOrderDtos = customerShebaoService.getCustomerOrders(customerShebaoOrderDto);
            List<Map<String,Object>> listMap = customerShebaoOrderService.selectErrorCustomerIdList(customerShebaoOrderDto.getCompanyShebaoOrderId());
            List listSbArr = null;
            List listGjjArr =null;
            List<ErrorBillDto> list = new ArrayList<ErrorBillDto>();
            ErrorBillDto errorBillDto = null;
            //打印出返回的map
            List  dayinList = new ArrayList();
            String sbText=""; //社保异常拼接      xxx,xxx
            String gjjText="";//公积金异常拼接    xxx,xxx
            String sbReason="";//社保原因拼接     xxx,xxx
            String gjjReason="";//公积金原因拼接  xxx,xxx
            for(Map<String,Object>  map:listMap){
                //新建一个
                errorBillDto = new ErrorBillDto();
                errorBillDto.setCustomerId(map.get("customer_id").toString());
                errorBillDto.setDepName(map.get("dep_name")==null?"":map.get("dep_name").toString());
                errorBillDto.setTrueName(map.get("customer_turename")==null?"":map.get("customer_turename").toString());
                errorBillDto.setPhone(map.get("customer_phone")==null?"":map.get("customer_phone").toString());
                errorBillDto.setSbBase(map.get("sb_base") == null ? "" : map.get("sb_base").toString());
                errorBillDto.setGjjBase(map.get("gjj_base") == null ? "" : map.get("gjj_base").toString());
                Map<Integer, List> returnMap = customerShebaoOrderService.getOrderFaildMsg(Long.valueOf(map.get("customer_id").toString()), customerShebaoOrderDto.getCompanyShebaoOrderId());
                listSbArr = returnMap.get(1);//社保的
                listGjjArr =returnMap.get(2);//公积金的
                //社保消息
                if(listSbArr!=null&&listSbArr.size()>0) {
                    for (Object sbObj : listSbArr) {
                        if (!StringUtils.isEmpty(sbObj)) {
                            Map reasonSbMap = (Map) JSON.parse(sbObj.toString());
                            sbText+=reasonSbMap.get("text")==null?"":reasonSbMap.get("text").toString()+",";
                            sbReason+=reasonSbMap.get("reason")==null?"":reasonSbMap.get("reason").toString()+",";
                        }

                    }
                }else{
                    sbText="";
                    sbReason="";
                }


                //公积金
                if(listGjjArr!=null&&listGjjArr.size()>0){
                    for(Object gjjObj:listGjjArr){
                        if(!StringUtils.isEmpty(gjjObj)){
                            Map resonGjjMap= (Map) JSON.parse(gjjObj.toString());
                            gjjText+=resonGjjMap.get("text")==null?"":resonGjjMap.get("text").toString()+",";
                            gjjReason+=resonGjjMap.get("reason")==null?"":resonGjjMap.get("reason").toString()+",";
                        }
                    }

                }else{
                    gjjText="";
                    gjjReason="";
                }
                errorBillDto.setGjjText(gjjText);
                errorBillDto.setSbText(sbText);
                errorBillDto.setSbReason(sbReason);
                errorBillDto.setGjjReason(gjjReason);
                list.add(errorBillDto);
                dayinList.add(returnMap);//打印出所有的返回错误信息的信息
            }

            LOGGER.info("封装的对象list集合："+JSON.toJSONString(list));
            LOGGER.info("所有的错误数据"+JSON.toJSONString(dayinList));
            resultResponse.setSuccess(true);
            resultResponse.setData(list);

            LOGGER.info("获取当前账户所有用户需求,返回结果:"+JSON.toJSONString(resultResponse));
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查询异常账单下的所有员工信息出现异常错误："+e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 补差页面个人详情
     * @param supplementCompanyOrderId
     * @return
     */
    @RequestMapping("customerAddtionDetail.htm")
    @ResponseBody
    public ResultResponse customerAddtionDetail(Long supplementCompanyOrderId,Long supplementCustomerId,String payMonth,Integer type) {
        ResultResponse resultResponse=new ResultResponse();
        try{
            List<CustomersSupplementDto> supplementDtoList = companyShebaoService.customerAddtionDetail( supplementCompanyOrderId, supplementCustomerId, payMonth, type);
            if(supplementDtoList!=null && supplementDtoList.size()>0){
                resultResponse.setSuccess(true);
                resultResponse.setData(supplementDtoList);
            }else{
                throw new BusinessException("无补收补退信息");
            }
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("补差页面个人详情"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("查询补差页面个人详情失败");
            LOGGER.error("补差页面个人详情",e);
        }
        return resultResponse;
    }

    /**
     * 跳转至社保资料审核页面
     * @param mav
     * @return
     */
    @RequestMapping(value="jumpToShebaoApprovePage.htm")
    public ModelAndView jumpToShebaoApprovePage(ModelAndView mav,Long customerId){

        try{
            mav.setViewName("xtr/shebao/promptDefeat");
            mav.addObject("customerId",customerId);
            //获取该员工的一些基本信息
            CustomersBean customer = customersService.selectByPrimaryKey(customerId);
            if(customer!=null){
                mav.addObject("customerTruename",customer.getCustomerTurename());
                mav.addObject("customerSex",customer.getCustomerSex());
                mav.addObject("customerPhone",customer.getCustomerPhone());
                mav.addObject("customerIdcard",customer.getCustomerIdcard());
                //身份证正面
                if(!StringUtils.isStrNull(customer.getCustomerIdcardImgFront())){
                    mav.addObject("imgFront",customer.getCustomerIdcardImgFront());
                    mav.addObject("imgFrontUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customer.getCustomerIdcardImgFront());
                }else{
                    mav.addObject("imgFront","");
                }
                //身份证反面
                if(!StringUtils.isStrNull(customer.getCustomerIdcardImgBack())){
                    mav.addObject("imgBack",customer.getCustomerIdcardImgBack());
                    mav.addObject("imgBackUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+customer.getCustomerIdcardImgBack());
                }else{
                    mav.addObject("imgBack","");
                }
                //失败原因
                mav.addObject("reason",customer.getCustomerSocialFailReason());

            }else{
                LOGGER.info("跳转至社保资料审核页面没有查询到员工信息");
            }

        }catch (Exception e){
            LOGGER.error("跳转至社保资料审核页面出现错误："+e.getMessage());
        }
        return mav;
    }

    /**
     * 社保资料审核重新提交
     * @param request
     * @return
     */
    @RequestMapping(value="shebaoApproveSubmit.htm")
    @ResponseBody
    public ResultResponse shebaoApproveSubmit(HttpServletRequest request, CustomersDto customersDto){
        ResultResponse resultResponse = new ResultResponse();
        try{
            CustomersBean customersBean = new CustomersBean();
            if(customersDto!=null){
                customersBean.setCustomerId(customersDto.getCustomerId());//员工id
                customersBean.setCustomerSex(customersDto.getCustomerSex());//员工性别
                customersBean.setCustomerIdcard(customersDto.getCustomerIdcard());//员工身份证号
                customersBean.setCustomerIdcardImgFront(customersDto.getCustomerIdcardImgFront());//身份证正面
                customersBean.setCustomerIdcardImgBack(customersDto.getCustomerIdcardImgBack());//身份证背面
                customersBean.setCustomerSocialApproveState(2);//待审核
                int result = customersService.updateByPrimaryKeySelective(customersBean);
                if(result==1){
                    resultResponse.setMessage("成功提交");
                    resultResponse.setSuccess(true);
                }else{
                    resultResponse.setMessage("更新失败");
                }

            }else{
                resultResponse.setMessage("封装参数对象为空");
            }

        }catch (Exception e){
            LOGGER.error(" 社保资料审核重新提交出现异常错误："+e.getMessage());
        }
        return resultResponse;
    }



    /**
     * 社保资料审核身份证图片上传
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("shebaoUploadFile.htm")
    @ResponseBody
    public String shebaoUploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {

        LOGGER.info("社保资料审核提交.......");
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
                //上传文件是否大于2M
                if(multipartFile.getSize()>(2*1024*1024)){
                    resultResponse.setMessage("上传文件大小不能超过2M");
                    resultResponse.setSuccess(false);
                    return JSON.toJSONString(resultResponse);
                }

                boolean result= AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
                if(result){
                    resultResponse.setSuccess(true);
                    resultResponse.setData(newFileName);
                }else{
                    LOGGER.error("上传身份证图片,上传文件失败");
                    resultResponse.setMessage("上传文件失败");
                }
            }else{
                LOGGER.error("上传身份证图片,没有上传文件");
                resultResponse.setMessage("没有上传文件");
            }
        }catch (IOException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("上传身份证图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }

    /**
     * 跳转至详情页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="jumpToInfoDetailPage.htm")
    public ModelAndView jumpToInfoDetailPage(ModelAndView modelAndView,HttpServletRequest request){
        modelAndView.setViewName("xtr/shebao/infoDetail");
        modelAndView.addObject("customerId",request.getParameter("customerId"));
        return modelAndView;
    }

    /**
     * 查询员工个人社保公积金信息
     * @param request
     * @return
     */
    @RequestMapping(value="searchInfoDetails")
    @ResponseBody
    public ResultResponse searchInfoDetails(HttpServletRequest request,Long customerId){
        ResultResponse resultResponse = new ResultResponse();

        try{
            resultResponse = companyShebaoService.searchInfoDetails(customerId);
        }catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("查询员工个人社保公积金信息出现异常错误："+e.getMessage());
        }
        return resultResponse;
    }



}

