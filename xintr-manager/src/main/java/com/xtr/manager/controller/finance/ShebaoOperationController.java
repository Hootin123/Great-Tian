package com.xtr.manager.controller.finance;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.comm.basic.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by allycw3 on 2016/10/20.
 */
@Controller
@RequestMapping("shebaoOperation")
public class ShebaoOperationController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ShebaoPayInfoController.class);

    @Resource
    private CompanyShebaoService companyShebaoService;
    /**
     * 社保公积金付款管理页面
     * @param mav
     * @return
     */
    @RequestMapping("toShebaoOperationPage.htm")
    public ModelAndView toShebaoOperationPage(ModelAndView mav) {
        mav.setViewName("xtr/finance/shebaoPay/shebaoOperation");
        return mav;
    }

    //取消订单
    @RequestMapping("cancelOrder.htm")
    @ResponseBody
    public ResultResponse cancelOrder(String orderStr) {
        ResultResponse resultResponse=new ResultResponse();
        try{
            resultResponse= companyShebaoService.cancelOrder(orderStr);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("取消订单"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("取消订单失败");
            LOGGER.error("取消订单",e);
        }
        LOGGER.info("取消订单结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }
}
