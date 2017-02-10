package com.xtr.company.controller.customer;

import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersStationBean;
import com.xtr.api.service.customer.CustomerUpdateSalaryService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersStationService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author Xuewu
 * @Date 2016/8/17.
 */
@Controller
@RequestMapping("/customerSalary")
public class CustomerSalaryController extends BaseController {

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomersStationService customersStationService;

    @Resource
    private CustomerUpdateSalaryService customerUpdateSalaryService;
    /**
     * 跳转至 定薪
     * @param request
     * @param customerId
     * @return
     */
    @RequestMapping("toSetSalary.htm")
    public ModelAndView toSetSalary(HttpServletRequest request, long customerId, long payrollId) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        ModelAndView modelAndView = new ModelAndView("xtr/customer/customerSetSalary");
        CustomersBean customersBean = customersService.selectByPrimaryKey(customerId);
        CustomersStationBean station = customersStationService.selectCutomerStationByCustomerId(customerId);
//        if(!loginCompanyObj.getCompanyId().equals(customersBean.getCustomerCompanyId()))
//            throw new BusinessException("员工信息异常");
        if(station.getStationCustomerState() > 2)
            throw new BusinessException("员工信息异常");
        modelAndView.addObject("customer", customersBean);
        modelAndView.addObject("station", station);
        modelAndView.addObject("payrollId", payrollId);
        return modelAndView;
    }

    /**
     * 跳转至 调薪
     * @param request
     * @param customerId
     * @return
     */
    @RequestMapping("toUpdateSalary.htm")
    public ModelAndView toUpdateSalary(HttpServletRequest request, long customerId, long payrollId, Date lastDate) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        ModelAndView modelAndView = new ModelAndView("xtr/customer/customerUpdateSalary");
        CustomersBean customersBean = customersService.selectByPrimaryKey(customerId);
        CustomersStationBean station = customersStationService.selectCutomerStationByCustomerId(customerId);
//        if(!loginCompanyObj.getCompanyId().equals(customersBean.getCustomerCompanyId()))
//            throw new BusinessException("员工信息异常");
        if(station.getStationCustomerState() > 2)
            throw new BusinessException("员工信息异常");
        modelAndView.addObject("customer", customersBean);
        modelAndView.addObject("station", station);
        modelAndView.addObject("payrollId", payrollId);
        modelAndView.addObject("currentSalary", customerUpdateSalaryService.getLastSalary(customerId, lastDate));
        return modelAndView;
    }

    /**
     *  跳转到批量上传
     * @param type
     * @return
     */
    @RequestMapping("toBatchUpload.htm")
    public ModelAndView toBatchUpload(@RequestParam String type){
        ModelAndView modelAndView = new ModelAndView("xtr/customer/customerSalaryBatchUpload");
        modelAndView.addObject("type", type);
        return modelAndView;
    }


    /**
     * 定薪
     * @param customerId
     * @param probationSalary
     * @param regularSalary
     * @return
     */
    @RequestMapping("setSalary.htm")
    @ResponseBody
    public ResultResponse setSalary(long customerId, @RequestParam(required = false) BigDecimal probationSalary, @RequestParam(required = false) BigDecimal regularSalary){
        try {
            CustomersBean customersBean = customerUpdateSalaryService.setCustomerSalary(customerId, probationSalary, regularSalary);
            if(customersBean != null) {
                customerUpdateSalaryService.updatePayRoll(customersBean);
                return ResultResponse.buildSuccess(null);
            }else{
                return ResultResponse.buildFail(null).message("操作失败");
            }
        }catch (Exception ex){
            return ResultResponse.buildFail(null).message(ex.getMessage());
        }
    }

    /**
     * 获取目标日期的基本工资
     * @param customerId
     * @param lastDate
     * @return
     */
    @RequestMapping("getLastSalary.htm")
    @ResponseBody
    public ResultResponse getLastSalary(long customerId, Date lastDate) {
        try {
            return ResultResponse.buildSuccess(customerUpdateSalaryService.getLastSalary(customerId, lastDate));
        }catch (Exception e) {
            e.printStackTrace();
            return ResultResponse.buildSuccess(0);
        }
    }


    /**
     * 调薪
     * @param customerId
     * @param salary
     * @param date
     * @param reason
     * @return
     */
    @RequestMapping("updateSalary.htm")
    @ResponseBody
    public ResultResponse updateSalary(long customerId, @RequestParam BigDecimal salary, @RequestParam Date date, @RequestParam(required = false) String reason){
        try {
            if(salary==null){
                return ResultResponse.buildFail(null).message("请输入调薪后基本工资");
            }
            if(reason != null && reason.length() > 20) {
                return ResultResponse.buildFail(null).message("调薪原因长度不能超过20");
            }
            String regexSalary = "^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$";
            if (!Pattern.matches(regexSalary, salary.toString())) {
                return ResultResponse.buildFail(null).message("请输入正确的调薪后基本工资");
            }
            if(customerUpdateSalaryService.insertSalaryRecord(customerId, salary, date, reason)){
                return ResultResponse.buildSuccess(null);
            }else {
                return ResultResponse.buildFail(null).message("操作失败");
            }
        }catch (Exception e){
            return ResultResponse.buildFail(null).message(e.getMessage());
        }
    }


    /**
     * 批量导入定薪
     * @return
     */
    @RequestMapping("/batch.htm")
    @ResponseBody
    public ResultResponse setSalaryBatch(HttpServletRequest request, @RequestParam String fileName, @RequestParam int type) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        InputStream inputStream= AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"),fileName);
        LinkedHashMap<String, String> msg = new LinkedHashMap<>();

        Long loginCompanyId = getLoginCompanyId(request);
        int maxRow = PropertyUtils.getIntValue("excel.max.row", 500);

        if(type == 1) {
            List<Object[]> dataFromExcel = ExcelUtil.getDataFromExcel07InculdeBlankCell(inputStream, 2, 0, 3);
            if(dataFromExcel.size() > maxRow) {
                return ResultResponse.buildFail(null).message("最多支持" + maxRow + "行数据导入");
            }
            msg = (LinkedHashMap<String, String>) customerUpdateSalaryService.setSalaryBatch(dataFromExcel, true, loginCompanyId);
        }else if(type == 2) {
            List<Object[]> dataFromExcel = ExcelUtil.getDataFromExcel07InculdeBlankCell(inputStream, 2, 0, 4);
            if(dataFromExcel.size() > maxRow) {
                return ResultResponse.buildFail(null).message("最多支持" + maxRow + "行数据导入");
            }
            msg = (LinkedHashMap<String, String>) customerUpdateSalaryService.updateSalaryBatch(dataFromExcel, true, loginCompanyId);
        }else if(type == 3) {
            List<Object[]> dataFromExcel = ExcelUtil.getDataFromExcel07InculdeBlankCell(inputStream, 2, 0, 5);
            if(dataFromExcel.size() > maxRow) {
                return ResultResponse.buildFail(null).message("最多支持" + maxRow + "行数据导入");
            }
            msg = (LinkedHashMap<String, String>) customerUpdateSalaryService.updatePreTaxAfterTaxBatch(dataFromExcel, true, loginCompanyId);
        }else if(type == 4) {
            List<Object[]> dataFromExcel = ExcelUtil.getDataFromExcel07InculdeBlankCell(inputStream, 2, 0, 2);
            if(dataFromExcel.size() > maxRow) {
                return ResultResponse.buildFail(null).message("最多支持" + maxRow + "行数据导入");
            }
            msg = (LinkedHashMap<String, String>) customerUpdateSalaryService.updateAbsenceDayBatch(dataFromExcel, true, loginCompanyId);
        }else{
            return ResultResponse.buildFail(null).message("未知操作类型");
        }

        resultResponse.setSuccess(msg.size() == 0);
        StringBuffer message = new StringBuffer();
        Set<String> keys = msg.keySet();

        for (String key : keys) {
            String[] split = key.split(",");
            Integer row = Integer.valueOf(split[0]);
            Integer col = Integer.valueOf(split[1]);
            message.append("第" + (row + 3) + "行，" + (col + 1) + "列:"+msg.get(key)+"<br/>");
        }
        resultResponse.setMessage(message.toString());
        return resultResponse;
    }

}
