package com.xtr.company.controller.account;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.*;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.dto.company.CompanyAccountPropertyDto;
import com.xtr.api.dto.company.CompanySocialOrdersDto;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.*;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.salary.RapidlyPayrollExcelService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.ShebaoDepoitService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.RedActivityConstant;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.MathUtils;
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
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>我的账户</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 9:22
 */
@Controller
@RequestMapping("account")
public class MyAccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccountController.class);

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CompanyBorrowOrdersService companyBorrowOrdersService;

    @Resource
    private CompanyAccountService companyAccountService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    @Resource
    private CompanySocialOrdersService companySocialOrdersService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private ShebaoDepoitService shebaoDepoitService;

    @Resource
    private RapidlyPayrollExcelService rapidlyPayrollExcelService;

    /**
     * 我的账户页面(新)
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "index.htm", method = RequestMethod.GET)
    public ModelAndView myAccount(ModelAndView mav, HttpServletRequest request) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        if (null == companyMembersBean) {
            LOGGER.error("companyMembersBean为空");
            mav.addObject("nocontract", 1);
            return mav;
        }

        Long comapnyId = companyMembersBean.getMemberCompanyId();
        CompanyAccountPropertyDto companyAccountPropertyDto = companyAccountService.selectAccountProperty(comapnyId);
        mav.addObject("accountData", companyAccountPropertyDto);
//        LOGGER.error("comapnyId=" + comapnyId );
        SubAccountBean subAccountBean = subAccountService.selectByCustId(comapnyId, AccountType.COMPANY);
        if (null == subAccountBean) {
            LOGGER.error("subAccountBean为空");
            mav.addObject("nocontract", 1);
        }

        //判断4个协议是否有一个是有效的
        CompanyProtocolsBean companyProtocolsBean = new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(comapnyId);
        List<CompanyProtocolsBean> protocolList = companyProtocolsService.selectUsefulProtocolsByState(companyProtocolsBean);
        if (null == protocolList || protocolList.size() <= 0) {
            LOGGER.error("我的账户页面,没有有效的协议,不能充值或提现");
            mav.addObject("nocontract", 1);
        }
        mav.setViewName("xtr/account/index");
        return mav;
    }

    /**
     * 订单列表
     *
     * @param request
     * @param rechargeType
     * @param rechargeState
     * @param dateType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "orders.htm")
    public void orders(HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestParam(required = false) Integer rechargeType,
                       @RequestParam(required = false) Integer rechargeState,
                       @RequestParam(defaultValue = "0", required = false) int dateType,
                       @RequestParam(defaultValue = "0", required = false) int pageIndex,
                       @RequestParam(defaultValue = "9", required = false) int pageSize) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();
        ResultResponse resultResponse = companyRechargesService.selectPageList(comapnyId, rechargeType, rechargeState, dateType, pageIndex, pageSize);
        HtmlUtil.writerJson(response, resultResponse);

    }

    /**
     * 充值详情
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping("recharge_detail.htm")
    public ModelAndView rechargeDetail(ModelAndView mav, @RequestParam(value = "id") Long rechargeId) {
        if (null != rechargeId) {
            mav.addObject("rechargeData", companyRechargesService.selectByPrimaryKey(rechargeId));
        }
        mav.setViewName("xtr/account/recharge_detail");
        return mav;
    }

    /**
     * 提现详情
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping("withdraw_detail.htm")
    public ModelAndView withdrawalsDetail(ModelAndView mav, @RequestParam(value = "id") Long rechargeId) {
        if (null != rechargeId) {
            mav.addObject("withdrawalsData", companyRechargesService.selectByPrimaryKey(rechargeId));
        }
        mav.setViewName("xtr/account/withdraw_detail");
        return mav;
    }

    /**
     * 借款详情
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping("borrowDetail.htm")
    public ModelAndView borrowDetail(ModelAndView mav, @RequestParam(value = "id") Long rechargeId) {
        if (null != rechargeId) {
            CompanyRechargesBean companyRechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
            mav.addObject("companyRechargesBean", companyRechargesBean);
            if (companyRechargesBean != null) {
                CompanyBorrowOrdersBean borrowInfoBean = companyBorrowOrdersService.selectByOrderNumber(companyRechargesBean.getRechargeNumber());
                mav.addObject("borrowInfoBean", borrowInfoBean);

            }
        }
        mav.setViewName("xtr/account/borrow_detail");
        return mav;
    }

    /**
     * 社保公积金详情
     *
     * @param mav
     * @return
     */
    @RequestMapping("social_detail.htm")
    public ModelAndView socialDetail(HttpServletRequest request, ModelAndView mav, @RequestParam(value = "id") Long orderRechagrId) {
        if (null != orderRechagrId) {
            CompanySocialOrdersDto companySocialOrdersBean = companySocialOrdersService.getByOrderRechagrId(orderRechagrId);
            mav.addObject("socialOrderData", companySocialOrdersBean);

            Double orderMoney = companySocialOrdersBean.getOrderMoney();
            Double orderMoneyServer = companySocialOrdersBean.getOrderMoneyServer();

            // 订单金额 = 订单金额+薪太软服务费
            Double orderMoneyTotal = orderMoney + orderMoneyServer;

            mav.addObject("orderMoneyTotal", MathUtils.toMoney(orderMoneyTotal));
            mav.addObject("realMoney", MathUtils.toMoney(orderMoneyTotal));

            // 代付款
            if (companySocialOrdersBean.getOrderStatus() == 3) {

                CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
                Long comapnyId = companyMembersBean.getMemberCompanyId();

                // 查询有没有代缴社保的红包
                RedEnvelopeBean redEnvelopeBean = redEnvelopeService.getByCompanyId(comapnyId, CompanyRechargeConstant.SOCIAL_TYPE);
                if (null != redEnvelopeBean) {

                    boolean used = false;

                    Integer count = companySocialOrdersBean.getOrderPeopleNumber();

                    if (redEnvelopeBean.getActivityId().equals(RedActivityConstant.AC_22) && count >= 5) {
                        used = true;
                    }

                    if (redEnvelopeBean.getActivityId().equals(RedActivityConstant.AC_66) && count >= 15) {
                        used = true;
                    }

                    if (used) {
                        mav.addObject("redMoney", MathUtils.toMoney(redEnvelopeBean.getRedMoney()));
                        Double realMoney = MathUtils.toMoney(orderMoneyTotal - redEnvelopeBean.getRedMoney().doubleValue());
                        mav.addObject("realMoney", realMoney);
                    }
                }
            }
        }
        mav.setViewName("xtr/account/social_detail");
        return mav;
    }

    /**
     * 发工资详情
     *
     * @param mav
     * @return
     */
    @RequestMapping("salary_detail.htm")
    public ModelAndView salaryDetail(ModelAndView mav, @RequestParam(value = "id") Long rechargeId) {
        if (null != rechargeId) {
            CompanyRechargesBean rechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
            if (rechargesBean != null) {
                PayCycleBean payCycleBean = new PayCycleBean();
                //工资发放总额
                BigDecimal totalWages = new BigDecimal(0);
                if (rechargesBean.getRechargeType() == CompanyRechargeConstant.SEND_SALARY_TYPE) {
                    //获取计薪周期数据
                    payCycleBean = payCycleService.selectByPrimaryKey(rechargesBean.getExcelId());
                    if (payCycleBean != null)
                        totalWages = payCycleBean.getTotalWages();
                } else if (rechargesBean.getRechargeType() == CompanyRechargeConstant.RAPIDLY_PAYROLL_TYPE) {
                    //急速发工资
                    RapidlyPayrollExcelBean rapidlyPayrollExcelBean = rapidlyPayrollExcelService.selectByPrimaryKey(rechargesBean.getExcelId());
                    //发工资人数
                    payCycleBean.setPeopleNumber(rapidlyPayrollExcelBean.getPayrollNumber());
                    //发薪日期
                    payCycleBean.setPayDay(rapidlyPayrollExcelBean.getPayDay());
                    //创建时间
                    mav.addObject("payCycleBean", payCycleBean);
                    totalWages = rapidlyPayrollExcelBean.getTotalWages();
                }
                mav.addObject("payCycleBean", payCycleBean);
                mav.addObject("isDedit", "yes");
                //如果状态是提交订单,获取是否是在等待充值审核,如果是等待充值审核,则查看详情不能再次扣款
                if (rechargesBean.getRechargeState() == CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT) {
                    CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
                    companyRechargesBean.setExcelId(rechargesBean.getExcelId());
                    companyRechargesBean.setRechargeType(CompanyRechargeConstant.RECHARGE_TYPE);
                    companyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
                    int count = companyRechargesService.selectCountByCondition(companyRechargesBean);
                    if (count > 0) {//充值后扣款前显示详情
                        mav.addObject("isDedit", "no");
                        mav.addObject("rechargesBean", rechargesBean);
                    } else {//充值前显示详情,垫付\还需支付,取最新的数据
                        CompanyRechargesBean newRechargesBean = payrollAccountService.showSalaryOrder(rechargeId, totalWages);
                        mav.addObject("rechargesBean", newRechargesBean);
                    }
                } else {
                    mav.addObject("rechargesBean", rechargesBean);
                }
            }
        }
        mav.setViewName("xtr/account/salaryDetail");
        return mav;
    }


    /**
     * 新的社保公积金详情页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("social_detail_new.htm")
    public ModelAndView socialDetailNew(HttpServletRequest request, ModelAndView mav, Long companyRechargeId) {
        if (companyRechargeId != null) {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            //获取订单信息
            CompanyRechargesBean rechargesBean = companyRechargesService.selectByPrimaryKey(companyRechargeId);
            //获取社保公积金详情列表
            if (rechargesBean != null && rechargesBean.getExcelId() != null) {
                //获取企业社保公积金订单
                CompanyShebaoOrderBean orderBean = companyShebaoService.selectByPrimaryKey(rechargesBean.getExcelId());
                if (orderBean != null) {
                    //如果状态是提交订单,获取是否是在等待充值审核,如果是等待充值审核,则查看详情不能再次扣款
                    if (rechargesBean != null && rechargesBean.getRechargeState() == CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT) {
                        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
                        companyRechargesBean.setExcelId(rechargesBean.getExcelId());
                        companyRechargesBean.setRechargeType(CompanyRechargeConstant.RECHARGE_TYPE);
                        companyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
                        companyRechargesBean.setRechargeCompanyId(rechargesBean.getRechargeCompanyId());
                        List<CompanyRechargesBean> rechargeList = companyRechargesService.selectListByCondition(companyRechargesBean);
                        if (rechargeList != null && rechargeList.size() > 0) {//充值后扣款前显示详情
                            CompanyRechargesBean newRechargeBean = rechargeList.get(0);
                            mav.addObject("isDedit", "no");
                            //获取红包额度
                            RedEnvelopeBean redBean = redEnvelopeService.getByRedId(newRechargeBean.getRechargeRedId());
                            BigDecimal redMoney = new BigDecimal("0");
                            if (redBean != null && redBean.getRedMoney() != null) {
                                redMoney = redBean.getRedMoney();
                            }
                            mav.addObject("redMoney", redMoney);
                            mav.addObject("rechargesBean", newRechargeBean);
                        } else {//充值前显示详情,垫付\还需支付,取最新的数据
                            if (rechargesBean != null) {
                                BigDecimal totalWages = orderBean.getPriceSum();
                                BigDecimal zeroBigDeciaml = new BigDecimal("0");
                                totalWages = totalWages == null ? zeroBigDeciaml : totalWages;
                                //垫付金额,垫付比例
                                CompanyRechargesBean paymentBean = shebaoDepoitService.queryPaymentInfoForShebao(rechargesBean.getRechargeCompanyId(), totalWages);
                                rechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
                                rechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
                                mav.addObject("redMoney", paymentBean.getRedWalletAmount());
                                //实际支付额度
                                rechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
                            }
                            mav.addObject("rechargesBean", rechargesBean);
                        }
                    } else {
                        mav.addObject("isDedit", "no");
                        //获取红包额度
                        RedEnvelopeBean redBean = redEnvelopeService.getByRedId(rechargesBean.getRechargeRedId());
                        BigDecimal redMoney = new BigDecimal("0");
                        if (redBean != null && redBean.getRedMoney() != null) {
                            redMoney = redBean.getRedMoney();
                        }
                        mav.addObject("redMoney", redMoney);
                        mav.addObject("rechargesBean", rechargesBean);
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
                    mav.addObject("companyRechargeId", companyRechargeId);
                    BigDecimal priceSb = orderBean.getShebaotongShebaoAmount() != null ? orderBean.getShebaotongShebaoAmount() : new BigDecimal("0");
                    BigDecimal priceGjj = orderBean.getShebaotongGjjAmount() != null ? orderBean.getShebaotongGjjAmount() : new BigDecimal("0");
                    BigDecimal priceOverdue = orderBean.getShebaotongOverdueAmount() != null ? orderBean.getShebaotongOverdueAmount() : new BigDecimal("0");
                    orderBean.setSbAndGjjAmount(priceSb.add(priceGjj).add(priceOverdue));
                    mav.addObject("orderBean", orderBean);
                }
            }
        }
        mav.setViewName("xtr/account/shebaoDetail");
        return mav;
    }

    /**
     * 社保公积金余额扣款
     *
     * @param response
     * @param companyRechargeId
     */
    @RequestMapping("shebaoOrderDebit.htm")
    public void shebaoOrderDebit(HttpServletResponse response, Long companyRechargeId, CompanyRechargesBean checkBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = shebaoDepoitService.shebaoOrderDebit(companyRechargeId, CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT, null, checkBean);
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
     * 社保公积金充值订单生成
     *
     * @param request
     * @param companyRechargesBean
     * @return
     */
    @RequestMapping(value = "shebaoRecharge.htm")
    @ResponseBody
    public ResultResponse shebaoRecharge(HttpServletRequest request, CompanyRechargesBean companyRechargesBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            LOGGER.info("社保公积金充值订单,传递参数:" + JSON.toJSONString(companyRechargesBean));
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Long companyId = companyMembersBean.getMemberCompanyId();
            shebaoDepoitService.shebaoRecharge(companyRechargesBean, companyId);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("社保公积金充值订单生成", e.getMessage());
        } catch (Exception e) {
            resultResponse.setMessage("社保公积金充值订单生成失败");
            LOGGER.error("社保公积金充值订单生成", e);
        }
        LOGGER.info("社保公积金充值订单生成,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }
}