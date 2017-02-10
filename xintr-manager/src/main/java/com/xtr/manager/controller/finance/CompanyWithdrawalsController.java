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
 * <p>企业提现流水查询</p>
 *
 * @author 任齐
 * @createTime: 2016/7/7 16:03
 */
@Controller("adminCompanyWithdrawalsController")
@RequestMapping("withdrawal")
public class CompanyWithdrawalsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyWithdrawalsController.class);

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
        mav.setViewName("xtr/finance/withdrawal/index");
        return mav;
    }

    /**
     * 企业提现流水查询
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
        companyRechargeDto.setRechargeType(2);
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
     * 企业提现审核
     *
     * @param mav
     * @param rechargeId
     * @param rechargeSerialNumber
     * @param rechargeBak
     * @return
     */
    @RequestMapping(value = "auditWithdrawals.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse auditWithdrawals(
            HttpServletRequest request, ModelAndView mav,
            @RequestParam(value = "rechargeId") Long rechargeId,
            @RequestParam(value = "rechargeSerialNumber") String rechargeSerialNumber,
            @RequestParam(value = "rechargeBak") String rechargeBak) {

        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();

            resultResponse = companyRechargesService.auditWithdrawals(rechargeId, memberId, rechargeSerialNumber, rechargeBak);
        } catch (BusinessException e) {
            resultResponse.setMessage("提现审核失败<br/>原因：" + e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

}
