package com.xtr.company.controller.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.salary.RapidlyPayrollService;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.company.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>工资单汇总</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/11/3 15:07
 */
@Controller
@RequestMapping("payrollSummary")
public class PayrollSummaryController {

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private RapidlyPayrollService rapidlyPayrollService;

    /**
     * 工资单汇总界面
     *
     * @param mav
     * @return
     */
    @RequestMapping("payrollSummary.htm")
    public ModelAndView payrollSummary(ModelAndView mav, HttpServletRequest request) {
        CompanyMembersBean companyBean = SessionUtils.getUser(request);
        List<String> list = payCycleService.selectYearPayroll(companyBean.getMemberCompanyId());
        if (list != null && !list.isEmpty()) {
            mav.addObject("listYear", list);
        }
        mav.setViewName("xtr/salary/payrollSummary");
        return mav;
    }

    /**
     * 获取汇总数据
     *
     * @param year
     * @return
     */
    @RequestMapping("getPayrollSummary.htm")
    @ResponseBody
    public ResultResponse getPayrollSummary(HttpServletRequest request, @RequestParam("year") String year) {
        CompanyMembersBean companyBean = SessionUtils.getUser(request);
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setData(payCycleService.selectPayrollSummary(companyBean.getMemberCompanyId(), year));
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 根据excelId加载急速发工资数据
     *
     * @param rapidlyPayrollBean
     * @return
     */
    @RequestMapping("loadRapidlyData.htm")
    @ResponseBody
    public void loadRapidlyData(HttpServletRequest request, HttpServletResponse response,RapidlyPayrollBean rapidlyPayrollBean) {
        HtmlUtil.writerJson(response,rapidlyPayrollService.selectPageList(rapidlyPayrollBean));
    }
}
