package com.xtr.company.controller.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.api.service.salary.PayRuleService;
import com.xtr.comm.basic.BusinessException;
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

/**
 * <p>记薪规则控制器</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:13.
 */
@Controller
@RequestMapping("salary_setting")
public class PayRuleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayRuleController.class);

    @Resource
    private PayRuleService payRuleService;

    /**
     * 记薪规则数据
     *
     * @return
     */
    @RequestMapping(value = "payrule.htm", method = RequestMethod.GET)
    @ResponseBody
    public ResultResponse payRult(HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        PayRuleBean payRuleBean = payRuleService.getPayRuleByCompanyId(comapnyId);
        if(null != payRuleBean){
            resultResponse.setData(payRuleBean);
        }
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 保存记薪规则
     *
     * @param mav
     * @param request
     * @param payRuleBean
     * @return
     */
    @RequestMapping(value = "save_payrule.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse savePayrule(ModelAndView mav,
                                   HttpServletRequest request,
                                   PayRuleBean payRuleBean) {

        ResultResponse resultResponse = new ResultResponse();
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();
        Long memberId = companyMembersBean.getMemberId();
        Long deptId = companyMembersBean.getMemberDepId();

        payRuleBean.setCompanyId(comapnyId);
        payRuleBean.setCompanyMenberId(memberId);
        payRuleBean.setDeptId(deptId);

        try {
            payRuleService.saveOrUpdatePayRule(payRuleBean);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

}
