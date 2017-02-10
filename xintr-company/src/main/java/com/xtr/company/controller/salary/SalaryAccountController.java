package com.xtr.company.controller.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.salary.PayRuleService;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>薪酬核算</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/11/8 10:28
 */
@Controller
@RequestMapping("salaryAccount")
public class SalaryAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalaryAccountController.class);

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private PayRuleService payRuleService;

    @Resource
    private CustomerPayrollService customerPayrollService;

    /**
     * 薪酬核算页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("salaryAccount.htm")
    public ModelAndView salaryAccount(HttpServletRequest request, ModelAndView mav) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前公司的计薪周期
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyMembersBean.getMemberCompanyId());
        if (payCycleBean != null) {
            //获取计薪规则
            PayRuleBean payRuleBean = payRuleService.getPayRuleByCompanyId(payCycleBean.getCompanyId());
            if (payRuleBean != null) {
                //是否计算社保公积金 0:否  1:是
                mav.addObject("isSocialSecurity", payRuleBean.getIsSocialSecurity());
            }
            mav.addObject("payCycle", payCycleBean);
            mav.setViewName("xtr/salary/salaryAccount");
        } else {
            //跳转到计薪周期设置引导
            mav.setViewName("xtr/salary/salaryOne");
        }
        return mav;
    }

    /**
     * 分页查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
    @RequestMapping(value = "dataList.htm", method = RequestMethod.POST)
    @ResponseBody
    public void dataList(HttpServletRequest request, HttpServletResponse response,
                         CustomerPayrollDto customerPayrollDto) {
        ResultResponse resultResponse = new ResultResponse();
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        customerPayrollDto.setCompanyId(companyMembersBean.getMemberCompanyId());
        try {
            resultResponse = customerPayrollService.selectPageList(customerPayrollDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

}
