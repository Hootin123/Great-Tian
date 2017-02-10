package com.xtr.company.controller.salary;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyMenuVisitRecordBean;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.salary.CustomerPayrollQueryDto;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.salary.*;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>工资核算</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/16 9:39
 */
@Controller
@RequestMapping("payrollAccount")
public class PayrollAccountController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollAccountController.class);

    @Resource
    private CustomerPayrollService customerPayrollService;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CustomersService customersService;

    @Resource
    private AllowanceSettingService allowanceSettingService;

    @Resource
    private PayRuleService payRuleService;

    @Resource
    private CompanyMembersService companyMembersService;

    /**
     * 薪资核算首页
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "payrollaccount.htm")
    public ModelAndView payrollAccount(HttpServletRequest request, ModelAndView mav) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前公司的计薪周期
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyMembersBean.getMemberCompanyId());
        if (payCycleBean != null) {
            try {
                //判断是否首次访问过
                Map<String, Object> visitMap = new HashMap<String, Object>();
                visitMap.put("type", 3);//首页的类型
                visitMap.put("memberId", companyMembersBean.getMemberId());
                CompanyMenuVisitRecordBean companyMenuVisitRecordBean = companyMembersService.selectVisitRecord(visitMap);
                //页面带一个标识
                int insertFlag = 0;
                if (null == companyMenuVisitRecordBean) {
                    //说明之前没登陆过
                    //插入一条记录
                    CompanyMenuVisitRecordBean saveVisitRecord = new CompanyMenuVisitRecordBean();
                    saveVisitRecord.setIp(request.getRemoteAddr());
                    saveVisitRecord.setMemberId(companyMembersBean.getMemberId());
                    saveVisitRecord.setCompanyId(companyMembersBean.getMemberCompanyId());
                    saveVisitRecord.setType(3);//工资核算
                    saveVisitRecord.setVisitTime(new Date());
                    insertFlag = companyMembersService.saveVisitRecord(saveVisitRecord);
                    if (insertFlag == 1) {
                        //带标识去页面
                        mav.addObject("firstVisitSalary", "firstVisitSalary");
                    }
                }

                Map<String, Integer> map = customersService.querySaralyInfo(DateUtil.dateToString(payCycleBean.getStartDate(), DateUtil.DATEYEARFORMATTER), DateUtil.dateToString(payCycleBean.getEndDate(), DateUtil.DATEYEARFORMATTER), companyMembersBean.getMemberCompanyId());
                mav.addObject("customerInfo", map);
                //获取津贴设置
                AllowanceSettingBean allowanceSettingBean = new AllowanceSettingBean();
                allowanceSettingBean.setCompanyId(companyMembersBean.getMemberCompanyId());
                List<AllowanceSettingBean> allowanceList = allowanceSettingService.getCompanyAllowanceSettingList(allowanceSettingBean);
                mav.addObject("allowanceList", allowanceList);
                //获取计薪规则
                PayRuleBean payRuleBean = payRuleService.getPayRuleByCompanyId(payCycleBean.getCompanyId());
                if (payRuleBean != null) {
                    //是否计算社保公积金 0:否  1:是
                    mav.addObject("isSocialSecurity", payRuleBean.getIsSocialSecurity());
                }
            } catch (ParseException e) {
            }
            mav.addObject("payCycle", payCycleBean);
            mav.setViewName("xtr/salary/payrollaccount");
        } else {
            mav.addObject("reloadUrl", "payrollaccount.htm");
            mav.setViewName("xtr/salary/oneenter");
        }
        return mav;
    }

    /**
     * 获取当前公司对应的津贴
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getCompanyAllowanceSettingList.htm", method = RequestMethod.POST)
    public void getCompanyAllowanceSettingList(HttpServletRequest request, HttpServletResponse response) {
        ResultResponse resultResponse = new ResultResponse();
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取津贴设置
        AllowanceSettingBean allowanceSettingBean = new AllowanceSettingBean();
        allowanceSettingBean.setCompanyId(companyMembersBean.getMemberCompanyId());
        List<AllowanceSettingBean> allowanceList = allowanceSettingService.getCompanyAllowanceSettingList(allowanceSettingBean);
        resultResponse.setData(allowanceList);
        HtmlUtil.writerJson(response, resultResponse);
    }

    @RequestMapping(value = "toOneenter.htm")
    public ModelAndView jumpToOneenter(ModelAndView modelAndView) {
        modelAndView.setViewName("xtr/salary/oneenter");
        return modelAndView;
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

    /**
     * 筛选工资单
     *
     * @param request
     * @param response
     * @param customerPayrollQueryDto
     */
    @RequestMapping(value = "queryCustomerPayroll.htm", method = RequestMethod.POST)
    @ResponseBody
    public void queryCustomerPayroll(HttpServletRequest request, HttpServletResponse response,
                                     CustomerPayrollQueryDto customerPayrollQueryDto) {
        ResultResponse resultResponse = new ResultResponse();
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        customerPayrollQueryDto.setCompanyId(companyMembersBean.getMemberCompanyId());
        try {
            resultResponse = customerPayrollService.selectCustomerPayroll(customerPayrollQueryDto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 更新基本工资
     *
     * @param customerPayrollDto
     * @param type               0:缺勤天数  1:税前补发/扣款 2:税后补发/扣款
     * @return
     */
    @RequestMapping(value = "updateBasePay.htm", method = RequestMethod.POST)
    @ResponseBody
    public void updateBasePay(HttpServletResponse response, CustomerPayrollDto customerPayrollDto,
                              Integer type) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = payrollAccountService.updateBasePay(customerPayrollDto, type);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("修改缺勤天数出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 奖金更新
     *
     * @param response
     * @param parentId
     * @param id
     * @param detailValue
     */
    @RequestMapping(value = "bonusUpdate.htm", method = RequestMethod.POST)
    @ResponseBody
    public void bonusUpdate(HttpServletResponse response, @RequestParam("parentId") Long parentId, @RequestParam("id") Long id, @RequestParam("detailValue") BigDecimal detailValue) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = payrollAccountService.bonusUpdate(parentId, id, detailValue);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("修改奖金出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 初始化计薪周期
     *
     * @param response
     * @param yearMonth
     * @param startDate
     * @param endDate
     */
    @RequestMapping(value = "initPayCycle.htm", method = RequestMethod.POST)
    @ResponseBody
    public void initPayCycle(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("yearMonth") String yearMonth, @RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        ResultResponse resultResponse = new ResultResponse();
        try {
            int result = payCycleService.initPayCycle(yearMonth, startDate, endDate, companyMembersBean.getMemberCompanyId());
            if (result > 0) {
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("初始化计薪周期失败，请刷新界面重试");
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("初始化计薪周期出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }



}
