package com.xtr.company.controller.salary;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.salary.CustomerPayrollDetailBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.salary.CustomerPayrollExportDto;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.salary.*;
import com.xtr.api.util.ExcelReportUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.company.util.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>当月工资</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/24 13:14
 */
@Controller
@RequestMapping("monthlypayroll")
public class MonthlyPayrollController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyPayrollController.class);

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CustomerPayrollService customerPayrollService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private PayRuleService payRuleService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private RapidlyPayrollExcelService rapidlyPayrollExcelService;


    /**
     * 当月工资页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("monthlypayroll.htm")
    public ModelAndView monthlyPayroll(HttpServletRequest request, ModelAndView mav) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取当前公司的计薪周期
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyMembersBean.getMemberCompanyId());
        if (payCycleBean != null) {
            mav.addObject("payCycleBean", payCycleBean);
            mav.addObject("totalWages", customerPayrollService.getTotalWages(payCycleBean.getId()));
            try {
                mav.addObject("payDay", payrollAccountService.getPayDay(payCycleBean));
            } catch (Exception e) {
            }
            //计薪周期是否结束
            int day = DateUtil.getDiffDaysOfTwoDateByNegative(DateUtil.formatCurrentDate(), DateUtil.date2String(payCycleBean.getEndDate(), DateUtil.DATEYEARFORMATTER));
            if (day > 0) {
                mav.addObject("isEnd", 0);//已结束
            } else {
                mav.addObject("isEnd", 1);//未结束
            }
            //是否开始
            day = DateUtil.getDiffDaysOfTwoDateByNegative(DateUtil.formatCurrentDate(), DateUtil.date2String(payCycleBean.getStartDate(), DateUtil.DATEYEARFORMATTER));
            if (day < 0) {
                mav.addObject("isStart", 0);//未开始
            } else {
                mav.addObject("isStart", 1);//已开始
            }
            //获取计薪规则
            PayRuleBean payRuleBean = payRuleService.getPayRuleByCompanyId(payCycleBean.getCompanyId());
            if (payRuleBean != null) {
                //是否计算社保公积金 0:否  1:是
                mav.addObject("isSocialSecurity", payRuleBean.getIsSocialSecurity());
            }
        }
        mav.setViewName("xtr/salary/monthlypayroll");
        return mav;
    }


    /**
     * 审批通过
     *
     * @param payCycleId
     * @return
     */
    @RequestMapping(value = "approvalPayroll.htm", method = RequestMethod.POST)
    @ResponseBody
    public void approvalPayroll(HttpServletResponse response, Long payCycleId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = payrollAccountService.approvalPayroll(payCycleId);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("审核出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 发工资
     *
     * @param response
     * @param payCycleId
     */
    @RequestMapping(value = "payOff.htm", method = RequestMethod.POST)
    @ResponseBody
    public void payOff(HttpServletRequest request, HttpServletResponse response, Long payCycleId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            if (payCycleId != null && companyMembersBean != null) {
                resultResponse = payrollAccountService.payOff(payCycleId, companyMembersBean);
            } else {
                resultResponse.setMessage("发工资出现异常，请刷新界面重试");
            }

        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("发工资出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 导出
     *
     * @param request
     * @param response
     * @param exportStr
     */
    @RequestMapping(value = "onExport.htm", method = RequestMethod.GET)
    public void onExport(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("customerPayrollExportDtos") String exportStr,
                         @RequestParam("payCycleId") Long payCycleId) throws Exception {
        if (StringUtils.isNotBlank(exportStr)) {
            CustomerPayrollExportDto[] customerPayrollExportDtos = JSON.parseArray(exportStr, CustomerPayrollExportDto.class).toArray(new CustomerPayrollExportDto[0]);
            if (customerPayrollExportDtos != null && customerPayrollExportDtos.length > 0) {
                //获取当前登录用户
                CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
                CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
                customerPayrollDto.setCompanyId(companyMembersBean.getMemberCompanyId());
                customerPayrollDto.setPayCycleId(payCycleId);
                //获取计薪周期
                PayCycleBean payCycleBean = payCycleService.selectByPrimaryKey(payCycleId);
                //获取计薪规则
                PayRuleBean payRuleBean = payRuleService.getPayRuleByCompanyId(companyMembersBean.getMemberCompanyId());
                //根据计薪周期获取工资单
                List<CustomerPayrollDto> list = customerPayrollService.selectCustomerPayroll(customerPayrollDto);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ExcelReportUtil.ExportSetInfo exportSetInfo = getExportSetInfo(customerPayrollExportDtos, list, payRuleBean);
                //文件名
                String fileName = "【" + payCycleBean.getCurrentPayCycle() + "】工资单";
                //设置标题
                exportSetInfo.setTitles(new String[]{fileName});
                exportSetInfo.setOut(baos);
                ExcelReportUtil.export2Excel2(customerPayrollExportDtos, payCycleBean, list, exportSetInfo);
                InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

                //设置没有缓存
                response.reset();

                if (StringUtils.isNotBlank(fileName)) {
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                }

                response.setHeader("content-disposition", "attachment;filename=" + fileName + ".xls");
                response.setContentType("application/octet-stream");
                OutputStream outputStream = response.getOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    /**
     * 获取设置信息
     *
     * @param customerPayrollExportDtos
     * @param list
     * @return
     */
    private ExcelReportUtil.ExportSetInfo getExportSetInfo(CustomerPayrollExportDto[] customerPayrollExportDtos, List<CustomerPayrollDto> list,
                                                           PayRuleBean payRuleBean) throws UnsupportedEncodingException {
        if (customerPayrollExportDtos != null && customerPayrollExportDtos.length > 0 && list != null && !list.isEmpty()) {
            ExcelReportUtil.ExportSetInfo exportSetInfo = new ExcelReportUtil.ExportSetInfo();
            List<String[]> headNameList = new ArrayList<>();
            String[] headNames = new String[customerPayrollExportDtos.length];
            CustomerPayrollDto customerPayrollDto = list.get(0);
            //津贴
            List<CustomerPayrollDetailBean> allowanceList = customerPayrollDto.getAllowanceList();
            //奖金
            List<CustomerPayrollDetailBean> bonusList = customerPayrollDto.getBonusList();
            for (int i = 0; i < customerPayrollExportDtos.length; i++) {
                CustomerPayrollExportDto customerPayrollExportDto = customerPayrollExportDtos[i];
                //类型 0:基本工资 1:津贴 2:奖金
                String key = customerPayrollExportDto.getKey();
                if (customerPayrollExportDto.getType() == 0) {
                    if (StringUtils.equals(key, "userName")) {
                        headNames[i] = "姓名";
                    } else if (StringUtils.equals(key, "deptName")) {
                        headNames[i] = "部门";
                    } else if (StringUtils.equals(key, "mobilePhone")) {
                        headNames[i] = "手机号";
                    } else if (StringUtils.equals(key, "entryDate")) {
                        headNames[i] = "入职日期";
                    } else if (StringUtils.equals(key, "employMethod")) {
                        headNames[i] = "聘用形式";
                    } else if (StringUtils.equals(key, "idCard")) {
                        headNames[i] = "身份证";
                    } else if (StringUtils.equals(key, "bankNumber")) {
                        headNames[i] = "工资卡";
                    } else if (StringUtils.equals(key, "bankAccount")) {
                        headNames[i] = "开户行";
                    } else if (StringUtils.equals(key, "baseWages")) {
                        headNames[i] = "基本工资";
                    } else if (StringUtils.equals(key, "addWages")) {
                        headNames[i] = "调薪后基本工资";
                    } else if (StringUtils.equals(key, "attendanceDeduction")) {
                        headNames[i] = "缺勤扣款";
                    } else if (StringUtils.equals(key, "absenceDayNumber")) {
                        headNames[i] = "缺勤天数";
                    } else if (StringUtils.equals(key, "socialSecurity")) {
                        headNames[i] = "社保";
                    } else if (StringUtils.equals(key, "fund")) {
                        headNames[i] = "公积金";
                    } else if (StringUtils.equals(key, "pretax")) {
                        headNames[i] = "税前补发/扣款";
                    } else if (StringUtils.equals(key, "shouldAmount")) {
                        headNames[i] = "应发工资";
                    } else if (StringUtils.equals(key, "personalTax")) {
                        headNames[i] = "个税";
                    } else if (StringUtils.equals(key, "afterTax")) {
                        headNames[i] = "税后补发/扣款";
                    } else if (StringUtils.equals(key, "realWage")) {
                        headNames[i] = "实发工资";
                    } else if (StringUtils.equals(key, "wipedAmount")) {
                        headNames[i] = "报销金额";
                    } else if (StringUtils.equals(key, "appraisalsSupplement")) {
                        headNames[i] = "绩效补充";
                    }
                } else if (customerPayrollExportDto.getType() == 1) {
                    key = new String(key.getBytes("ISO-8859-1"), "UTF-8");
                    //津贴
                    if (allowanceList != null && !allowanceList.isEmpty()) {
                        for (int j = 0; j < allowanceList.size(); j++) {
                            if (StringUtils.equals(key, allowanceList.get(j).getDetailName().toString())) {
                                headNames[i] = allowanceList.get(j).getDetailName();
                                break;
                            }
                        }
                    }
                } else if (customerPayrollExportDto.getType() == 2) {
                    key = new String(key.getBytes("ISO-8859-1"), "UTF-8");
                    //奖金
                    if (bonusList != null && !bonusList.isEmpty()) {
                        for (int j = 0; j < bonusList.size(); j++) {
                            if (StringUtils.equals(key, bonusList.get(j).getDetailName().toString())) {
                                headNames[i] = bonusList.get(j).getDetailName();
                                break;
                            }
                        }
                    }
                }
            }
            headNameList.add(headNames);
            exportSetInfo.setHeadNames(headNameList);
            return exportSetInfo;
        }
        return null;
    }


    /**
     * 生成发工资订单
     *
     * @param request
     * @param response
     * @param payCycleId
     */
    @RequestMapping("generateSalaryOrder.htm")
    @ResponseBody
    public void generateSalaryOrder(HttpServletRequest request, HttpServletResponse response, Long payCycleId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            CompanyRechargesBean companyRechargesBean = payrollAccountService.generateSalaryOrder(companyMembersBean.getMemberCompanyId(), payCycleId);
            resultResponse.setSuccess(true);
            resultResponse.setData(companyRechargesBean);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("生成发工资订单出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 发工资订单页面
     *
     * @param request
     * @param payCycleId
     * @param mav
     * @return
     */
    @RequestMapping("salaryOrderDetail.htm")
    public ModelAndView salaryOrderDetail(HttpServletRequest request, Long payCycleId, ModelAndView mav, Long rechargeId) {
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        if (rechargeId != null) {//从我的账户过来的发工资订单详情
            companyRechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
            companyRechargesBean = payrollAccountService.showSalaryOrder(rechargeId, companyRechargesBean.getRechargeMoney());
            mav.addObject("salaryOrderFlag", "myAccount");//作用于关闭弹框,这个关一层
        } else {//从发工资过来的订单详情
            companyRechargesBean = payrollAccountService.generateSalaryOrder(companyMembersBean.getMemberCompanyId(), payCycleId);//新建发工资订单并返回新建订单的信息
            mav.addObject("salaryOrderFlag", "salary");//作用于关闭弹框,这个关两层
        }
        //获取可用余额
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyMembersBean.getMemberCompanyId(), AccountType.COMPANY);
        BigDecimal cashAmout = new BigDecimal("0");
        if (subAccountBean == null) {
            mav.addObject("errorMsg", "账户未开通");
            mav.addObject("cashAmout", cashAmout);
        } else if (StringUtils.equals("01", subAccountBean.getState())) {//00-生效,01-冻结,02-注销
            mav.addObject("errorMsg", "账户已锁定");
            mav.addObject("cashAmout", cashAmout);
        } else if (StringUtils.equals("02", subAccountBean.getState())) {
            mav.addObject("errorMsg", "账户已注销");
            mav.addObject("cashAmout", cashAmout);
        } else {
            mav.addObject("errorMsg", "");
            mav.addObject("cashAmout", subAccountBean.getCashAmout());
        }
        mav.addObject("companyRechargesBean", companyRechargesBean);
        mav.setViewName("xtr/salary/salaryOrderDetail");
        return mav;
    }

    /**
     * 发工资余额扣款
     *
     * @param response
     * @param payCycleId
     */
    @RequestMapping("commitSalaryOrder.htm")
    @ResponseBody
    public void commitSalaryOrder(HttpServletRequest request, HttpServletResponse response, Long payCycleId, CompanyRechargesBean checkBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            checkBean.setRechargeCompanyId(companyMembersBean.getMemberCompanyId());
            resultResponse = payrollAccountService.salaryOrderDebit(payCycleId, CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT, null, checkBean);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("发工资余额扣款", e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("发工资扣款出现异常，请刷新界面重试");
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 发工资充值订单生成
     *
     * @param request
     * @param companyRechargesBean
     * @return
     */
    @RequestMapping(value = "salaryRecharge.htm")
    @ResponseBody
    public ResultResponse salaryRecharge(HttpServletRequest request, CompanyRechargesBean companyRechargesBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            LOGGER.info("发工资充值订单,传递参数:" + JSON.toJSONString(companyRechargesBean));
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Long companyId = companyMembersBean.getMemberCompanyId();
            //订单名称
            String name = null;
            //充值前验证垫付金额\还需支付金额\可用余额是否有变动
            BigDecimal totalWages = new BigDecimal(0);
            if (companyRechargesBean.getRechargeType() == CompanyRechargeConstant.SEND_SALARY_TYPE) {
                //薪资核算
                PayCycleBean payCycleBean = payCycleService.selectByPrimaryKey(companyRechargesBean.getExcelId());
                if (payCycleBean != null) {
                    totalWages = payCycleBean.getTotalWages();
                    name = payCycleBean.getYear() + "年" + payCycleBean.getMonth() + "月" + CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_SALARYRECHARGE;
                }
            } else if (companyRechargesBean.getRechargeType() == CompanyRechargeConstant.RAPIDLY_PAYROLL_TYPE) {
                //急速发工资
                RapidlyPayrollExcelBean rapidlyPayrollExcelBean = rapidlyPayrollExcelService.selectByPrimaryKey(companyRechargesBean.getExcelId());
                if (rapidlyPayrollExcelBean != null) {
                    totalWages = rapidlyPayrollExcelBean.getTotalWages();
                    name = rapidlyPayrollExcelBean.getExcelTitle() + CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_RAPIDLYCHARGE;
                }
            }
            CompanyRechargesBean paymentBean = companyRechargesService.queryPaymentInfo(companyId, totalWages);
            payrollAccountService.checkDifferentAmount(companyRechargesBean, paymentBean, companyId);

            //订单关联的企业ID
            companyRechargesBean.setRechargeCompanyId(companyId);
            //订单支付渠道
            companyRechargesBean.setRechargeStation(CompanyRechargeConstant.COMPANY_RECHARGE_STATION_HUIKUAN);
            //订单名称
            companyRechargesBean.setRechargeName(name);
            //垫付比例
            companyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
            resultResponse = payrollAccountService.generateCzOrder(companyRechargesBean);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("发工资充值订单生成", e.getMessage());
        } catch (Exception e) {
            resultResponse.setMessage("发工资充值订单生成失败");
            LOGGER.error("发工资充值订单生成", e);
        }
        LOGGER.info("发工资充值订单生成,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

}
