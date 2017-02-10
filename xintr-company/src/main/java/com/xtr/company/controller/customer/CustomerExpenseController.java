package com.xtr.company.controller.customer;

import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.customer.CustomerExpenseDto;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.salary.CustomerExpenseService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 报销管理
 * @Author Xuewu
 * @Date 2016/9/5.
 */
@Controller
@RequestMapping("expense")
public class CustomerExpenseController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerExpenseController.class);

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomerExpenseService customerExpenseService;

    /**
     * 报销管理首页
     * @param request
     * @param mav
     * @return
     */
    @RequestMapping(value = "expense.htm")
    public ModelAndView payrollAccount(HttpServletRequest request, ModelAndView mav) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前公司的计薪周期
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyMembersBean.getMemberCompanyId());
        if (payCycleBean != null) {
            int unsetCount =customerExpenseService.selectUnSetCount(companyMembersBean.getMemberCompanyId(), payCycleBean.getId(), payCycleBean.getStartDate());
            int allCount =customersService.selectLiveCountByCompanyId(companyMembersBean.getMemberCompanyId(), payCycleBean.getId(), payCycleBean.getStartDate());
            mav.addObject("payCycle", payCycleBean);
            mav.addObject("unsetCount",  unsetCount);
            mav.addObject("allCount",  allCount);
            mav.setViewName("xtr/salary/expenseList");
        } else {//tgfdg
            mav.addObject("reloadUrl",  request.getContextPath() + "/expense/expense.htm");
            mav.setViewName("xtr/salary/oneenter");
        }
        return mav;
    }


    /**
     * 调额
     * @param request
     * @param customerId
     * @return
     */
    @RequestMapping(value = "toUpdateExpense.htm")
    public ModelAndView toUpdateExpense(HttpServletRequest request, long customerId) {
        CustomersBean customersBean = customersService.selectByPrimaryKey(customerId);
        ModelAndView modelAndView = new ModelAndView("xtr/salary/updateExpense");
        modelAndView.addObject("customerId", customerId);
        modelAndView.addObject("customer", customersBean);
        return modelAndView;
    }

    /**
     * 批量调额
     * @param request
     * @return
     */
    @RequestMapping(value = "toBatchUpdateExpense.htm")
    public ModelAndView toBatchUpdateExpense(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("xtr/salary/batchUpdateExpense");
        return modelAndView;
    }


    /**
     * 批量调额
     * @param request
     * @return
     */
    @RequestMapping(value = "toExpense.htm")
    public ModelAndView toExpense(HttpServletRequest request, @RequestParam long customerId) {
        ModelAndView modelAndView = new ModelAndView("xtr/salary/expense");
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        CustomersBean customersBean = customersService.selectByPrimaryKey(customerId);
        modelAndView.addObject("customer", customersBean);
        modelAndView.addObject("customerId", customerId);
        modelAndView.addObject("expenses", customerExpenseService.selectExpense(customerId, companyMembersBean.getMemberCompanyId()));
        return modelAndView;
    }

    /**
     * 查询列表
     * @param request
     * @param dto
     * @return
     */
    @RequestMapping(value = "list.htm")
    @ResponseBody
    public String list(HttpServletRequest request, CustomerExpenseDto dto) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyMembersBean.getMemberCompanyId());
        dto.setPayCycleId(payCycleBean.getId());
        dto.setCompanyId(companyMembersBean.getMemberCompanyId());
        return JSONObject.toJSONString(customerExpenseService.selectPage(dto, payCycleBean.getStartDate()));
    }

    /**
     * 调额
     * @param customerId
     * @param expense
     * @return
     */
    @RequestMapping(value = "updateExpense.htm")
    @ResponseBody
    public ResultResponse updateExpense(long customerId, BigDecimal expense){
        try {
            return ResultResponse.buildSuccess(customerExpenseService.updateExpense(customerId, expense));
        }catch (BusinessException be){
            LOGGER.info(be.getMessage());
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            return ResultResponse.buildFail(null).message("操作失败");
        }

    }

    /**
     * 批量调额
     * @param request
     * @param lt
     * @param gt
     * @param bl
     * @return
     */
    @RequestMapping(value = "batchUpdateExpense.htm")
    @ResponseBody
    public ResultResponse batchUpdateExpense(HttpServletRequest request, @RequestParam(required = false) BigDecimal lt, @RequestParam(required = false) BigDecimal gt, float bl){
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        try {
            return ResultResponse.buildSuccess(customerExpenseService.batchUpdateExpense(companyMembersBean.getMemberCompanyId(), gt, lt, bl));
        }catch (BusinessException be){
            LOGGER.info(be.getMessage());
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            e.printStackTrace();
            return ResultResponse.buildFail(null).message("操作失败");
        }

    }

    /**
     * 报销
     * @return
     */
    @RequestMapping(value = "customerExpense.htm")
    @ResponseBody
    public ResultResponse expense(@RequestBody CustomerExpenseDto dto, HttpServletRequest request){
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        try {
            return ResultResponse.buildSuccess(customerExpenseService.expense(dto, companyMembersBean.getMemberCompanyId()));
        }catch (BusinessException be){
            LOGGER.info(be.getMessage());
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e){
            LOGGER.info(e.getMessage());
            e.printStackTrace();
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }
}
