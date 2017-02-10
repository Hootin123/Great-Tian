package com.xtr.company.controller;

import com.google.common.collect.Maps;
import com.sun.media.sound.UlawCodec;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.dto.gateway.request.TradeQueryRequest;
import com.xtr.api.dto.gateway.response.DefrayPayResponse;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.api.service.salary.RapidlyPayrollService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.comm.jd.util.JdPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by xuewu on 2016/6/12.
 */
@Controller
public class TestGatewayController {


    @Resource
    JdPayService jdPayService;

    @Resource
    CustomerRechargesService customerRechargesService;

    @Resource
    CompanyRechargesService companyRechargesService;

    @Resource
    RapidlyPayrollService rapidlyPayrollService;

    @Resource
    CompanysService companysService;

    @RequestMapping("/test.htm")
    @ResponseBody
    public Map test(ModelAndView model, HttpServletRequest request) {
        Map<Object, Object> objectObjectHashMap = Maps.newHashMap();

        String orderId = getid();
        DefrayPayRequest dr = new DefrayPayRequest(BusinessType.CUSTOMER_WITHDRAW);
        dr.setPayeeBankCode("CEB");//*收款银行编码
        dr.setPayeeAccountType("C");//*收款帐户类型  对私户=P；对公户=C
        dr.setPayeeBankAssociatedCode("308290003118"); //对公时必填，联行号
        dr.setPayeeCardType("DE"); //收款卡种  借记卡=DE；信用卡=CR
        dr.setPayeeAccountNo("621492060163529*"); //*收款帐户号
        dr.setPayeeAccountName("薛武543543");//*收款帐户名称
        dr.setTradeSubject("用户提现53454"); //订单摘要  商品描述，订单标题，关键描述信息
        dr.setTradeAmount("1");//*订单交易金额  单位：分，大于0。
        dr.setOutTradeNo(orderId);//订单ID 唯一

        DefrayPayResponse response = null;
        try {
            response = jdPayService.defrayPay(dr);
            System.out.println(response.getTradeStatus());//com.xtr.comm.jd.util.CodeConst 交易状态
        }catch (BusinessException be){
            objectObjectHashMap.put("exception", be.getMessage());
            be.printStackTrace();
        }
        objectObjectHashMap.put("request", dr.createReqMap(true));
        objectObjectHashMap.put("response", response == null ? response : response.createReqMap(true));


        return objectObjectHashMap;
    }

    @RequestMapping("/{nofifyType}/jd_notify.htm")
    @ResponseBody
    public String notify(@RequestParam Map<String, String> requestPara, @PathVariable String nofifyType) throws Exception {
        String url = String.format("/%s/jd_notify.htm", nofifyType);

        JdPayConfig jdPayConfig = jdPayService.getJdPayConfig();

        System.out.println("===========提现 异步回调===========");
        if(url.equals(jdPayConfig.getCompanyWithdrawNotifyUrl().trim())) {
            //提现回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.COMPANY_WITHDRAW);
            System.out.println(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            companyRechargesService.doJdResponse(nr);
        }else if(url.equals(jdPayConfig.getCustomerWithdrawNotifyUrl().trim())){
            //提现回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.CUSTOMER_WITHDRAW);
            System.out.println(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            customerRechargesService.doJdResponse(nr);
        }else if (url.equals(jdPayConfig.getRapidlyWithdrawNotifyUrl())) {
            //急速发工资提现回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.RAPIDLY_WITHDRAW);
            System.out.println(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            rapidlyPayrollService.doJdResponse(nr);
        } else if(url.equals(jdPayConfig.getCustomerValidateNotifyUrl())){
            //测试账户回调
            System.out.println("===========测试账户回调===========");
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.VALIDATE_CUSTOMER);
            System.out.println(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            companysService.doJdResponse(nr);
        }else{
            //未知回调
            System.out.println("===========未知 异步回调===========");

        }

        System.out.println("===========提现 异步回调===========");
        return "success";
    }



    @RequestMapping("/jd_tools.htm")
    public ModelAndView jdTool(HttpServletRequest request) throws Exception {
        return new ModelAndView("xtr/jdtool");
    }

    @RequestMapping(value = "/jd_tools.htm", method = RequestMethod.POST)
    @ResponseBody
    public Object execute(HttpServletRequest request, @RequestParam String btype, @RequestParam String orderId, @RequestParam String amount, @RequestParam int type) throws Exception {
        try {

            BusinessType businessType = BusinessType.valueOf(type);
            String urlp = null;
            if(type == BusinessType.COMPANY_WITHDRAW.getCode()){
                urlp = "withdraw_company";
            }else if(type == BusinessType.CUSTOMER_WITHDRAW.getCode()){
                urlp = "withdraw_customer";
            }else if(type == BusinessType.VALIDATE_CUSTOMER.getCode()){
                urlp = "validate_customer";
            }else if(type == BusinessType.RAPIDLY_WITHDRAW.getCode()){
                urlp = "withdraw_rapidly";
            }else{
                return "未知操作";
            }
            if(btype.equals("query")){
                TradeQueryRequest req = new TradeQueryRequest(businessType);
                req.setOutTradeNo(orderId);
                return jdPayService.tradeQuery(req);
            }else if(btype.equals("sendsucc")){
                Map<String, String> requestPara = Maps.newHashMap();
                requestPara.put("out_trade_no", orderId);
                requestPara.put("trade_status", CodeConst.TRADE_FINI);
                requestPara.put("response_code", "0000");
                return notify(requestPara, urlp);
            }else if(btype.equals("sendfail_card")){
                Map<String, String> requestPara = Maps.newHashMap();
                requestPara.put("out_trade_no", orderId);
                requestPara.put("trade_status", CodeConst.TRADE_CLOS);
                requestPara.put("trade_respcode", "ILLEGAL_BANK_CARD_NO");
                requestPara.put("trade_respmsg", "银行卡错误。这是手动发送的失败");
                requestPara.put("response_message", "银行卡错误。这是手动发送的失败");
                requestPara.put("response_code", "0000");
                return notify(requestPara, urlp);
            }else if(btype.equals("sendfail_balance")){
                Map<String, String> requestPara = Maps.newHashMap();
                requestPara.put("out_trade_no", orderId);
                requestPara.put("trade_status", CodeConst.TRADE_CLOS);
                requestPara.put("trade_respcode", "ACCOUNT_BALANCE_NOT_ENOUGH");
                requestPara.put("trade_respmsg", "余额不足。这是手动发送的失败");
                requestPara.put("response_message", "余额不足。这是手动发送的失败");
                requestPara.put("response_code", "0000");
                return notify(requestPara, urlp);
            }else if(btype.equals("refund")){
                Map<String, String> requestPara = Maps.newHashMap();
                requestPara.put("out_trade_no", orderId);
                requestPara.put("trade_status", CodeConst.TRADE_REFUND);
                requestPara.put("refund_amount", amount);
                requestPara.put("response_code", "0000");
                return notify(requestPara, urlp);
            }else if(btype.equals("queryaccount")){
                return jdPayService.accountQuery();
            }
            return "未知处理";
        }catch (Exception e){
            return e;
        }
    }


    static int seq = 0;
    public synchronized static String getid() {
        int num = seq++ % 100;
        return System.currentTimeMillis() + "" + num;
    }


}
