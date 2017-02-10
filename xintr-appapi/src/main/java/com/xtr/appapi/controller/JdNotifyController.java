package com.xtr.appapi.controller;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.api.service.salary.RapidlyPayrollService;
import com.xtr.comm.jd.util.JdPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by xuewu on 2016/8/9.
 */

@Controller
public class JdNotifyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdNotifyController.class);

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


    @RequestMapping("/{nofifyType}/jd_notify.htm")
    @ResponseBody
    public String notify(@RequestParam Map<String, String> requestPara, @PathVariable String nofifyType) throws Exception {
        String url = String.format("/%s/jd_notify.htm", nofifyType);

        JdPayConfig jdPayConfig = jdPayService.getJdPayConfig();

        LOGGER.info("===========提现 异步回调===========");
        if (url.equals(jdPayConfig.getCompanyWithdrawNotifyUrl().trim())) {
            //提现回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.COMPANY_WITHDRAW);
            LOGGER.info(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            companyRechargesService.doJdResponse(nr);
        } else if (url.equals(jdPayConfig.getCustomerWithdrawNotifyUrl().trim())) {
            //提现回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.CUSTOMER_WITHDRAW);
            LOGGER.info(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            customerRechargesService.doJdResponse(nr);
        } else if (url.equals(jdPayConfig.getRapidlyWithdrawNotifyUrl())) {
            //急速发工资提现回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.RAPIDLY_WITHDRAW);
            LOGGER.info(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            rapidlyPayrollService.doJdResponse(nr);
        } else if (url.equals(jdPayConfig.getCustomerValidateNotifyUrl())) {
            //测试账户回调
            NotifyResponse nr = jdPayService.verifySingNotify(requestPara, BusinessType.VALIDATE_CUSTOMER);
            LOGGER.info(nr.getOutTradeNo() + "   --   " + nr.getTradeStatus());
            companysService.doJdResponse(nr);
        } else {
            //未知回调
            LOGGER.info("===========未知 异步回调===========");
        }

        LOGGER.info("===========提现 异步回调===========");
        return "success";
    }
}
