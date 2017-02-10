package com.xtr.company.controller.account;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.dto.company.CompanyAccountPropertyDto;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyAccountService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.company.util.SessionUtils;
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
 * <p>我的账户</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 9:22
 */
@Controller
public class CompanyAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyAccountController.class);

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CompanyAccountService companyAccountService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    /**
     * 我的账户,资产页面页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("account.htm")
    public ModelAndView account(ModelAndView mav, HttpServletRequest request) {
        try {

            mav.setViewName("xtr/account/account");

            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            if (null == companyMembersBean) {
                mav.addObject("nocontract", 1);
                return mav;
            }

            Long comapnyId = companyMembersBean.getMemberCompanyId();
            CompanyAccountPropertyDto companyAccountPropertyDto = companyAccountService.selectAccountProperty(comapnyId);
            mav.addObject("accountData", companyAccountPropertyDto);

            SubAccountBean subAccountBean = subAccountService.selectByCustId(comapnyId, AccountType.COMPANY);
            if (null == subAccountBean) {
                mav.addObject("nocontract", 1);
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return mav;
    }

    /**
     * 收支记录页面，
     *
     * @param mav
     * @param rechargeType 不传查询全部，1:充值，2:提现，3:发工资，4:借款
     * @param pageIndex    页码
     * @param pageSize     每页条数
     */
    @RequestMapping("balanceOfPayments.htm")
    public ModelAndView balanceOfPayments(ModelAndView mav,
                                          HttpServletRequest request,
                                          @RequestParam(value = "type", required = false) Integer rechargeType,
                                          @RequestParam(value = "dateType", defaultValue = "0") int dateType,
                                          @RequestParam(value = "state", required = false) Integer state,
                                          @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                          @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        // 充值、提现
        mav.addObject("type", rechargeType);
        mav.addObject("dateType", dateType);
        mav.addObject("state", state);

        ResultResponse resultResponse = companyRechargesService.selectPageList(comapnyId, rechargeType, state, dateType, pageIndex, pageSize);
        if (resultResponse.isSuccess()) {
            mav.addObject("rechargeData", resultResponse.getData());
            mav.addObject("paginator", resultResponse.getPaginator());
        }

        mav.setViewName("xtr/account/balanceOfPayments");

        return mav;
    }

    /**
     * 充值详情
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping("rechargeDetail.htm")
    public ModelAndView rechargeDetail(ModelAndView mav, @RequestParam(value = "rechargeid") Long rechargeId) {
        if (null != rechargeId) {
            mav.addObject("rechargeData", companyRechargesService.selectByPrimaryKey(rechargeId));
        }
        mav.setViewName("xtr/account/rechargeDetail");
        return mav;
    }

    /**
     * 提现详情
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping("withdrawalsDetail.htm")
    public ModelAndView withdrawalsDetail(ModelAndView mav, @RequestParam(value = "rechargeid") Long rechargeId) {
        if (null != rechargeId) {
            mav.addObject("withdrawalsData", companyRechargesService.selectByPrimaryKey(rechargeId));
        }
        mav.setViewName("xtr/account/withdrawalsDetail");
        return mav;
    }

    /**
     * 红包列表页面
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "red_list.htm", method = RequestMethod.GET)
    public ModelAndView red_list(ModelAndView mav,
                                 HttpServletRequest request,
                                 @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "8") int pageSize) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        RedEnvelopeBean redEnvelopeBean = new RedEnvelopeBean();
        redEnvelopeBean.setCompanyId(comapnyId);
        redEnvelopeBean.setState(1);

        ResultResponse resultResponse = redEnvelopeService.getRedEnvelopePage(redEnvelopeBean, pageIndex, pageSize);
        if (resultResponse.isSuccess()) {
            mav.addObject("redData", resultResponse.getData());
            mav.addObject("paginator", resultResponse.getPaginator());
        }

        mav.setViewName("xtr/account/red_list");
        return mav;
    }

    /**
     * 红包详细信息
     *
     * @param redId
     * @return
     */
    @RequestMapping(value = "red_detail.htm", method = RequestMethod.GET)
    @ResponseBody
    public ResultResponse red_detail(@RequestParam("redId") Long redId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            RedEnvelopeBean redEnvelopeBean = redEnvelopeService.getByRedId(redId);
            resultResponse.setData(redEnvelopeBean);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }
}