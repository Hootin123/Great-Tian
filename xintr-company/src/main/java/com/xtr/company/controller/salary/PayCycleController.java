package com.xtr.company.controller.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>计薪周期</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/19 10:22
 */
@Controller
@RequestMapping("payCycle")
public class PayCycleController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayCycleController.class);

    @Resource
    private PayCycleService payCycleService;

    /**
     * 初始化计薪周期
     *
     * @param request
     * @param response
     * @param startDay         计薪开始日
     * @param payWay           计薪方式
     * @param isSocialSecurity 是否计算社保公积金
     * @param yearMonth   计薪开始年月
     */
    @RequestMapping(value = "initPayCycle.htm", method = RequestMethod.POST)
    @ResponseBody
    public void initPayCycle(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam("startDay") String startDay, @RequestParam("payWay") String payWay,
                             @RequestParam("isSocialSecurity") String isSocialSecurity, @RequestParam("yearMonth") String yearMonth) {
        //获取当前登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        ResultResponse resultResponse = new ResultResponse();
        try {
            int result = payCycleService.initPayCycle(yearMonth, startDay, companyMembersBean.getMemberCompanyId(),payWay,isSocialSecurity);
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
