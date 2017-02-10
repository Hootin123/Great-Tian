package com.xtr.company.controller.payday;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.company.util.SessionUtils;
import com.xtr.company.web.IExcelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>发工资</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 14:26
 */
@Controller
public class PaySalaryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaySalaryController.class);


    @Resource
    private IExcelView excelView;

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CustomersService customersService;

    /**
     * 发工资页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("payroll.htm")
    public ModelAndView payroll(HttpServletRequest request, ModelAndView mav,
                                @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                @RequestParam(required = false, defaultValue = "8") int pageSize,
                                @RequestParam(required = false) String sort) {
        mav.setViewName("xtr/payday/payroll");
        //获取登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);

        //获取页面数据
        CompanySalaryExcelBean companySalaryExcelBean = new CompanySalaryExcelBean();
        companySalaryExcelBean.setPageIndex(pageIndex);
        companySalaryExcelBean.setPageSize(pageSize);
        companySalaryExcelBean.setExcelCompanyId(companyMembersBean.getMemberCompanyId());
        ResultResponse resultResponse = companySalaryExcelService.selectPageList(companySalaryExcelBean);

        if (resultResponse != null && resultResponse.isSuccess()) {
            mav.addObject("excelData", resultResponse.getData());
            mav.addObject("paginator", resultResponse.getPaginator());
        }
//        mav.addObject("tomorrowDate",DateUtil.addDate(new Date(),1));
        mav.addObject("tomorrowDate", new Date());
        //发工资模板文件路径
        String payrollPath = PropertyUtils.getString("company.download.payroll.path");
        mav.addObject("payrollPath", payrollPath);
        return mav;
    }

    /**
     * 维护组织成员页面
     *
     * @return
     */
    @RequestMapping("orgmember.htm")
    public ModelAndView orgMember(ModelAndView mav, HttpServletRequest request) {
        try {
            Long compayId = SessionUtils.getUser(request).getMemberCompanyId();
            List<CompanysDto> list = companyDepsService.getCompanyTree(compayId);
            //获取组织树
            mav.addObject("companyTree", list);
            if (!list.isEmpty()) {
                CustomersBean customersBean = new CustomersBean();
                customersBean.setCustomerCompanyId(compayId);
                customersBean.setCustomerDepId(list.get(0).getDepId());
                customersBean.setPageSize(8);
                //获取成员数据
                ResultResponse resultResponse = customersService.selectPageList(customersBean);
                if (resultResponse.isSuccess()) {
                    CompanysBean companysBean = SessionUtils.getAttr(request, CommonConstants.LOGIN_COMPANY_KEY);
                    //更新公司名称
                    List<CustomersBean> customersBeanList = (List<CustomersBean>) resultResponse.getData();
                    int size = customersBeanList.size();
                    for (int i = 0; i < size; i++) {
                        CustomersBean customersBean1 = customersBeanList.get(i);
                        customersBean1.setCustomerCompanyName(companysBean.getCompanyName());
                    }
                    mav.addObject("customerData", resultResponse.getData());
                    mav.addObject("paginator", resultResponse.getPaginator());
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        mav.setViewName("xtr/payday/orgmember");
        return mav;
    }

    /**
     * 根据部门Id获取成员信息
     *
     * @param pageIndex
     * @param pageSize
     * @param deptId
     * @return
     */
    @RequestMapping("getCustomerByDeptId.htm")
    @ResponseBody
    public void getCustomerByDeptId(HttpServletResponse response, HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                    @RequestParam(required = false, defaultValue = "8") int pageSize,
                                    @RequestParam("deptId") Long deptId) {
        Long compayId = SessionUtils.getUser(request).getMemberCompanyId();
        CustomersBean customersBean = new CustomersBean();
        customersBean.setPageIndex(pageIndex);
        customersBean.setPageSize(pageSize);
        customersBean.setCustomerCompanyId(compayId);
        customersBean.setCustomerDepId(deptId);
        //获取成员数据
        ResultResponse resultResponse = customersService.selectPageList(customersBean);
        LOGGER.info("成员信息：" + JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }


    /**
     * 发工资详情页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("paySalaryDetail.htm")
    public ModelAndView paySalary(ModelAndView mav, @RequestParam("excelId") Long excelId) {
        mav.setViewName("xtr/payday/paySalaryDetail");
        if (excelId != null) {
            mav.addObject("excelData", companySalaryExcelService.selectByPrimaryKey(excelId));
        }
        return mav;
    }

    /**
     * 文件模板下载
     */
//    @RequestMapping("salary/download.htm")
//    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
////        String filePath = request.getSession().getServletContext().getRealPath(PropertyUtils.getString("company.download.salary.path"));
//        String filePath = PropertyUtils.getString("company.download.salary.path");
//        excelView.download(filePath, PropertyUtils.getString("company.file.salary.name"), response);
//    }

    /**
     * 根据文件路径下载
     *
     * @param response
     * @param excelId
     */
    @RequestMapping("downloadExcel.htm")
    public void download(HttpServletResponse response,
                         @RequestParam("excelId") Long excelId) {
        CompanySalaryExcelBean companySalaryExcelBean = companySalaryExcelService.selectByPrimaryKey(excelId);
        if (companySalaryExcelBean != null) {
//            String filePath = request.getSession().getServletContext().getRealPath(companySalaryExcelBean.getExcelPath());
//            String filePath = companySalaryExcelBean.getExcelPath();
            String groupName = companySalaryExcelBean.getExcelGroupName();
            String remoteFilename = companySalaryExcelBean.getExcelPath();
            try {
//                excelView.download(groupName, remoteFilename, companySalaryExcelBean.getExcelName(), response);
                excelView.download(PropertyUtils.getString("oss.bucketName.file"), companySalaryExcelBean.getExcelPath(), companySalaryExcelBean.getExcelName(), response);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
