package com.xtr.manager.controller.customer;

import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.customer.CustomerUnPayOrderDto;
import com.xtr.api.service.customer.CustomerPayFaildService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.manager.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author Xuewu
 * @Date 2016/8/30.
 */
@Controller
@RequestMapping("/customerPay")
public class CustomerPayController extends BaseController {

    @Resource
    private CustomerPayFaildService customerPayFaildService;

    @RequestMapping(value = "/list.htm", method = RequestMethod.GET)
    public ModelAndView list() {
        return new ModelAndView("xtr/customer/pay/list");
    }


    @RequestMapping(value = "/page.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse page(CustomerUnPayOrderDto dto, HttpServletRequest request) {

        return customerPayFaildService.selectPayFailedOrder(dto);
    }

    @RequestMapping(value = "/sysMakeUp.htm")
    @ResponseBody
    public ResultResponse sysMakeUp(long rechargeId, HttpServletRequest request) {
        try {
            SysUserBean user = SessionUtils.getUser(request);
            return ResultResponse.buildSuccess(customerPayFaildService.sysMakeUp(rechargeId, user));
        }catch (BusinessException be){
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            return ResultResponse.buildFail(null).message("操作失败");
        }

    }


    @RequestMapping(value = "/handMakeUp.htm")
    @ResponseBody
    public ResultResponse handMakeUp(long rechargeId, String remark, HttpServletRequest request) {
        try {
            SysUserBean user = SessionUtils.getUser(request);
            return ResultResponse.buildSuccess(customerPayFaildService.handMakeUp(rechargeId, remark, user));
        }catch (BusinessException be){
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }

    @RequestMapping(value = "/sysMakeUpShebao.htm")
    @ResponseBody
    public ResultResponse sysMakeUpShebao(long rechargeId, HttpServletRequest request) {
        try {
            SysUserBean user = SessionUtils.getUser(request);
            return ResultResponse.buildSuccess(customerPayFaildService.sysMakeUpShebao(rechargeId, user));
        }catch (BusinessException be){
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }

    @RequestMapping(value = "/handMakeUpShebao.htm")
    @ResponseBody
    public ResultResponse handMakeUpShebao(long rechargeId, String remark, HttpServletRequest request) {
        try {
            SysUserBean user = SessionUtils.getUser(request);
            return ResultResponse.buildSuccess(customerPayFaildService.handMakeUpShebao(rechargeId, remark, user));
        }catch (BusinessException be){
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }

    @RequestMapping(value = "/handMakeUpRapidly.htm")
    @ResponseBody
    public ResultResponse handMakeUpRapidly(long rechargeId, String remark, HttpServletRequest request) {
        try {
            SysUserBean user = SessionUtils.getUser(request);
            return ResultResponse.buildSuccess(customerPayFaildService.handMakeUpRapidly(rechargeId, remark, user));
        }catch (BusinessException be){
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }

    @RequestMapping(value = "/sysMakeUpRapidly.htm")
    @ResponseBody
    public ResultResponse sysMakeUpRapidly(long rechargeId, HttpServletRequest request) {
        try {
            SysUserBean user = SessionUtils.getUser(request);
            return ResultResponse.buildSuccess(customerPayFaildService.sysMakeUpRapidly(rechargeId, user));
        }catch (BusinessException be){
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }
}
