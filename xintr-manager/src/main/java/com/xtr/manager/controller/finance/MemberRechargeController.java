package com.xtr.manager.controller.finance;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.customer.CustomerReachrgeDto;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CustomerRechargeConstant;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.FileUtils;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.manager.util.SessionUtils;
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
import java.util.List;

/**
 * <p>个人提现批次查询</p>
 *
 * @author 任齐
 * @createTime: 2016/7/7 16:03
 */
@Controller("adminMemberRechargeController")
@RequestMapping("personal")
public class MemberRechargeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberRechargeController.class);

    @Resource
    private CustomerRechargesService customerRechargesService;

    /**
     * 个人提现查询页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("index.htm")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("xtr/finance/personal/index");
        return mav;
    }

    /**
     * 个人提现查询
     *
     * @param mav
     * @param state
     * @param auditName
     * @param date_str
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(ModelAndView mav,
                                   @RequestParam(value = "state", required = false) Integer state,
                                   @RequestParam(value = "auditName", required = false) String auditName,
                                   @RequestParam(value = "date_str", required = false) String date_str,
                                   @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {

        return customerRechargesService.selectBatchPageList(date_str, state, auditName, pageIndex, pageSize);
    }

    /**
     * 批次提现详情
     * @param mav
     * @return
     */
    @RequestMapping("detail.htm")
    public ModelAndView detail(ModelAndView mav, @RequestParam("batchNumber") String batchNumber) {

        List<CustomerReachrgeDto> rechargeData = customerRechargesService.selectDetailList(batchNumber);
        mav.addObject("rechargeData", rechargeData);
        mav.setViewName("xtr/finance/personal/detail");
        return mav;
    }

    /**
     * 审批
     *
     * @param auditType     1:审核一个, 0:关闭所有审核
     * @param batchNumber
     * @return
     */
    @RequestMapping(value = "audit.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse audit(
            HttpServletRequest request,
            @RequestParam(value = "auditType") Integer auditType,
            @RequestParam(value = "batchNumber", required = false) String batchNumber) {

        ResultResponse resultResponse = new ResultResponse();

        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();

            customerRechargesService.auditWithdrawals(memberId, auditType, batchNumber);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            resultResponse.setMessage("审批失败<br/>原因：" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

    /**
     * 批量下载
     *
     * 1. 生成文件
     * 2. 下载文件
     *
     * @param batchNumber
     * @return
     */
    @RequestMapping("download.htm")
    public void download(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "batchNumber") String batchNumber) throws UnsupportedEncodingException {

        String webRoot = request.getRealPath("/");

        String payTemplatePath = PropertyUtils.getString("finance.download.paytemplate.path");

        String downloadPath = PropertyUtils.getString("finance.download.path");

        File file = customerRechargesService.generatorSalaryExcel(batchNumber, webRoot, payTemplatePath, downloadPath);

        String fileName = file.getName();

        //设置没有缓存
        response.reset();

        if (StringUtils.isNotBlank(fileName)) {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }

        try {

            FileInputStream inputStream = new FileInputStream(file);

            response.setHeader("content-disposition", "attachment;filename=" + fileName);
            response.setContentType("application/octet-stream");
            OutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

}
