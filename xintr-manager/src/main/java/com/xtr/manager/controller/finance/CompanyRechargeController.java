package com.xtr.manager.controller.finance;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.comm.basic.BusinessException;
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

/**
 * <p>企业充值流水查询</p>
 *
 * @author 任齐
 * @createTime: 2016/7/7 16:03
 */
@Controller("adminCompanyRechargeController")
@RequestMapping("recharge")
public class CompanyRechargeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRechargeController.class);

    @Resource
    private CompanyRechargesService companyRechargesService ;


    /**
     * 企业充值流水查询
     *
     * @param mav
     * @return
     */
    @RequestMapping("index.htm")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("xtr/finance/recharge/index");
        return mav;
    }

    /**
     * 企业充值流水查询
     *
     * @param mav
     * @param state
     * @param auditName
     * @param companyName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(ModelAndView mav,
                              @RequestParam(value = "state", required = false) Integer state,
                              @RequestParam(value = "auditName", required = false) String auditName,
                              @RequestParam(value = "companyName", required = false) String companyName,
                              @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                              @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {

        CompanyRechargeDto companyRechargeDto = new CompanyRechargeDto();
        companyRechargeDto.setRechargeType(1);
        if (StringUtils.isNotBlank(auditName)) {
            companyRechargeDto.setUserName(auditName);
        }
        if (StringUtils.isNotBlank(companyName)) {
            companyRechargeDto.setCompanyName(companyName);
        }
        if (null != state) {
            companyRechargeDto.setRechargeState(state);
        }

        return companyRechargesService.selectRechargeList(companyRechargeDto,pageIndex, pageSize);
    }

    /**
     * 企业充值审核
     *
     * @param mav
     * @param rechargeId
     * @param state
     * @return
     */
    @RequestMapping(value = "auditRecharge.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse auditRecharge(
                HttpServletRequest request, ModelAndView mav,
                                        @RequestParam(value = "rechargeId") Long rechargeId,
                                        @RequestParam(value = "state") Integer state) {

        ResultResponse resultResponse = new ResultResponse();
        try {

            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();

            resultResponse = companyRechargesService.auditRecharge(rechargeId, memberId, state);
        } catch (BusinessException e) {
            resultResponse.setMessage("充值审核失败<br/>原因：" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

}
