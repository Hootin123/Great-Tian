package com.xtr.appapi.controller;

import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * 社保通回调
 */
@Controller
@RequestMapping("/callback")
public class SbtNotifyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SbtNotifyController.class);

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    /**
     * 订单状态更新
     */
    @RequestMapping(value = "/order/state_update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String stateUpdate(@RequestParam Map<String, String> requestPara) {

        try {
            LOGGER.info("============ 社保通订单状态更新回调 ============");
            LOGGER.info("requestPara {}", requestPara);

            String orderid = requestPara.get("orderid");
            int status = Integer.valueOf(requestPara.get("status"));

            companyShebaoService.updateOrderStatus(orderid, status);

        }catch (Exception e){
            LOGGER.info("社保通订单状态更新回调异常", e.getMessage());
            return "0";
        }

        return "1";
    }

    /**
     * 人员缴交状态更新
     * @param requestPara
     */
    @RequestMapping(value = "/pay/state_update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String payStateUpdate(@RequestParam Map<String, String> requestPara) throws ParseException {

        try {
            LOGGER.info("============ 社保通人员缴交状态更新回调 ============");
            LOGGER.info("requestPara {}", requestPara);

            String id = requestPara.get("id");
            String orderid = requestPara.get("orderid");
            String type = requestPara.get("type");
            String status = requestPara.get("status");
            String comment = requestPara.get("comment");
            String month = requestPara.get("month");

            if(StringUtils.isBlank(id) ||StringUtils.isBlank(orderid) ||StringUtils.isBlank(type) || StringUtils.isBlank(status) || StringUtils.isBlank(month)) {
                LOGGER.info("社保通人员缴交状态更新回调:信息不全无法更新");
            }

            ShebaoTypeEnum shebaoTypeEnum;
            if("shebao".equalsIgnoreCase(type)) {
                shebaoTypeEnum = ShebaoTypeEnum.SHEBAO;
            }else{
                shebaoTypeEnum = ShebaoTypeEnum.GJJ;
            }
            Integer sbtStatus = Integer.valueOf(status);
            Date orderDate = DateUtil.sbtSimpleDateFormat.parse(month);

            boolean result = customerShebaoOrderService.updateShebaotongStatus(id, orderid, shebaoTypeEnum, sbtStatus, orderDate, comment);

            LOGGER.info("更新结果 {}", result);


        }catch (Exception e){
            LOGGER.info("社保通人员缴交状态更新回调异常", e.getMessage());
            return "0";
        }

        return "1";
    }

    /**
     * 人员材料审核状态更新
     * @param requestPara
     */
    @RequestMapping(value = "/materials/state_update.htm", method = RequestMethod.POST)
    @ResponseBody
    public String materialsStateUpdate(@RequestParam Map<String, String> requestPara) {

        try {
            LOGGER.info("============ 社保通人员材料审核状态更新回调 ============");
            LOGGER.info("requestPara {}", requestPara);

            String idCard = requestPara.get("id");
            Integer status = Integer.valueOf(requestPara.get("status"));
            String comment = requestPara.get("comment");

            Integer approveState;
            if(new Integer(1) == status) {
                approveState = 3;
            }else{
                approveState = 4;
            }
            customersService.updateShebaoStatusByIdcard(idCard, approveState, comment);

        }catch (Exception e){
            LOGGER.info("社保通人员材料审核状态更新回调异常", e.getMessage());
            return "0";
        }
        return "1";
    }

}