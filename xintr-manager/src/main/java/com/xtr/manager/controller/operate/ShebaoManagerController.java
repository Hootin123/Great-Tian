package com.xtr.manager.controller.operate;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepositBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.customer.CustomersSupplementDto;
import com.xtr.api.dto.shebao.ErrorBillDto;
import com.xtr.api.dto.shebao.HistoryBillDto;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by allycw3 on 2016/10/10.
 */
@Controller
@RequestMapping("shebaoManager")
public class ShebaoManagerController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ShebaoManagerController.class);

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CustomerShebaoService customerShebaoService;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;


    /**
     * 合作意向管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toShebaoInfoPage.htm")
    public ModelAndView toShebaoInfoPage(ModelAndView mav) {
        mav.setViewName("xtr/operate/shebao/shebaoInfoOther");
        return mav;
    }

    /**
     * 查询所有的社保公积金账单
     * @return
     */
    @RequestMapping(value="queryAllOrderBills.htm")
    @ResponseBody
    public ResultResponse queryAllOrderBills(HttpServletRequest request, HistoryBillDto historyBillDto){

        ResultResponse resultResponse = new ResultResponse();
        try {
         if(StringUtils.isEmpty(historyBillDto)){
             resultResponse.setMessage("封装参数对象为空");
             LOGGER.info("当前封装参数为空："+historyBillDto);
             return  resultResponse;
         }
         Date orderDate =null;
         Map<String,Object> map = new HashMap<String,Object>();
         map.put("companyName",historyBillDto.getCompanyName());
            if(!StringUtils.isStrNull(historyBillDto.getOrderDate())){
                SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
                orderDate = sm.parse(historyBillDto.getOrderDate());
                LOGGER.info("当前的订单时间："+sm.format(orderDate));
            }
         map.put("orderDate",orderDate);
         map.put("joinCityName",historyBillDto.getJoinCityName());

         resultResponse = companyShebaoService.selectAllOrdersByMap(map);
         LOGGER.info("接口返回值："+ JSON.toJSONString(resultResponse));
            if(StringUtils.isEmpty(resultResponse.getData())) {
                List<CompanyShebaoOrderDto> list = (List<CompanyShebaoOrderDto>) resultResponse.getData();
                LOGGER.info("当前查询的账单数量：" + list.size());
            }

        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("当前查询社保公积金账单出现异常："+e.getMessage(),e);
        }
        return resultResponse;
    }


    /**
     * 跳转至账单详情页面
     * @return
     */
    @RequestMapping(value="toOrderDetail.htm")
    public ModelAndView jumpToOrderDetail(ModelAndView modelAndView,Long companyId,Long orderId){
        //查询该公司下所有的部门
        modelAndView.setViewName("xtr/operate/shebao/shebaoOrderDetail");
        List<CompanysDto> list =  companyDepsService.getCompanyTree(companyId);
        //获取企业订单
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(orderId);
        modelAndView.addObject("shebaoOrder", companyShebaoOrderBean);

        modelAndView.addObject("companyShebaoOrderId",orderId);
        modelAndView.addObject("companyId",companyId);
        modelAndView.addObject("companysDtos",list);
         return  modelAndView;
    }

       /**
        * 查询该账单下所有的员工社保公积金
        * @param response
        * @param customerShebaoOrderDto
        */
       @RequestMapping("payorderDetailList.htm")
       public void payorderDetailList(HttpServletResponse response, CustomerShebaoOrderDto customerShebaoOrderDto) {
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
        mav.setViewName("xtr/operate/shebao/addtion_detail");
        return mav;
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
        mav.setViewName("xtr/operate/shebao/error_payorder_detail");
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
                errorBillDto.setDepName(map.get("dep_name").toString());
                errorBillDto.setTrueName(map.get("customer_turename").toString());
                errorBillDto.setPhone(map.get("customer_phone").toString());
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

}
