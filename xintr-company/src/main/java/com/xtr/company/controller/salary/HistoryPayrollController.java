package com.xtr.company.controller.salary;

import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>历史工资单</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/26 14:04
 */
@Controller
@RequestMapping("historypayroll")
public class HistoryPayrollController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyPayrollController.class);

    @Resource
    private CustomerPayrollService customerPayrollService;

    @Resource
    private PayCycleService payCycleService;

    /**
     * 历史工资单页面
     *
     * @param request
     * @param mav
     * @return
     */
    @RequestMapping("historypayroll.htm")
    public ModelAndView historyPayroll(HttpServletRequest request, ModelAndView mav) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前公司的计薪周期
        List<PayCycleBean> list = payCycleService.selectYearByCompanyId(companyMembersBean.getMemberCompanyId());
        if (list != null && !list.isEmpty()) {
            LinkedHashMap<String, List<PayCycleBean>> map = new LinkedHashMap<>();
            for (PayCycleBean payCycleBean : list) {
                map.put(payCycleBean.getYear(), payCycleService.selectByCompanyIdAndYear(companyMembersBean.getMemberCompanyId(), payCycleBean.getYear()));
            }
            mav.addObject("map", map);
        }
        mav.setViewName("xtr/salary/historypayroll");
        return mav;
    }

    /**
     * 查询数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public void dataList(HttpServletRequest request, HttpServletResponse response, CustomerPayrollDto customerPayrollDto) {
        ResultResponse resultResponse = new ResultResponse();
        try {//获取当前登录用户
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            customerPayrollDto.setCompanyId(companyMembersBean.getMemberCompanyId());
            Pager pageBounds = new Pager(customerPayrollDto.getPageIndex(), customerPayrollDto.getPageSize());
            //根据计薪周期查询工资单
            resultResponse = customerPayrollService.selectCustomerPayrollPageList(customerPayrollDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        }
        resultResponse.setSuccess(true);
        HtmlUtil.writerJson(response, resultResponse);
    }
}
