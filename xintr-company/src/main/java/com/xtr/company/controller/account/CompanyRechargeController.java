package com.xtr.company.controller.account;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.*;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.*;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.enums.ClienEnum;
import com.xtr.comm.util.StringUtils;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>我的账户->充值、提现</p>
 *
 * @author 任齐
 * @createTime: 2016/6/29 17:45
 */
@Controller
public class CompanyRechargeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRechargeController.class);

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyDepositService companyDepositService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanyMembersService companyMembersService;

    /**
     * 用户充值页面
     *
     * @param mav
     */
    @RequestMapping(value = "recharge.htm", method = RequestMethod.GET)
    public ModelAndView recharge(ModelAndView mav, HttpServletRequest request) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        // 查询余额，银行卡账户名，银行卡号
        SubAccountBean subAccountBean = subAccountService.selectByCustId(comapnyId, AccountType.COMPANY);
        if(null != subAccountBean){
            mav.addObject("cashAmout", subAccountBean.getCashAmout());
        }
        //mav.addObject("cashAmout", subAccountBean.getCashAmout());

        mav.setViewName("xtr/account/recharge");
        return mav;
    }




    @RequestMapping(value = "recharge_step2.htm", method = RequestMethod.GET)
    public ModelAndView rechargeStep2(ModelAndView mav, HttpServletRequest request, @RequestParam("rechargeId") Long rechargeId) {
        CompanyRechargesBean companyRechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
        mav.addObject("rechargeData", companyRechargesBean);
        mav.setViewName("xtr/account/rechargeStep2");
        return mav;
    }

    /**
     * 用户提现页面
     *
     * @param mav
     */
    @RequestMapping(value = "withdraw.htm", method = RequestMethod.GET)
    public ModelAndView withdraw(ModelAndView mav, HttpServletRequest request) {
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        // 查询企业余额
        SubAccountBean subAccountBean = subAccountService.selectByCustId(comapnyId, AccountType.COMPANY);
        mav.addObject("cashAmout", subAccountBean.getCashAmout());

        // 查询企业账户
        CompanysBean companyData = companysService.selectCompanyByCompanyId(comapnyId);
        mav.addObject("companyData", companyData);

        CompanyDepositBean companyDepositBean = companyDepositService.selectByCompanyId(comapnyId);
        mav.addObject("deposit", companyDepositBean);

        mav.setViewName("xtr/account/withdraw");
        return mav;
    }


    /**
     * 企业充值
     *
     * @param companyRechargesBean
     * @return
     */
    @RequestMapping(value = "recharge.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse addRecharge(HttpServletRequest request, CompanyRechargesBean companyRechargesBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {

            LOGGER.info("###### 开始企业充值申请 ######");

            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Long comapnyId = companyMembersBean.getMemberCompanyId();

            companyRechargesBean.setRechargeCompanyId(comapnyId);
            companyRechargesBean = companyRechargesService.addRecharge(companyRechargesBean);
            resultResponse.setData(companyRechargesBean.getRechargeId());
            resultResponse.setSuccess(true);

            LOGGER.info("###### 企业充值申请成功 ######");

        } catch (Exception e) {
            resultResponse.setMessage("充值失败");
            LOGGER.error("###### 企业充值申请失败 ######", e);
        }
        return resultResponse;
    }


    /**
     * 企业充值成功页面
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping(value = "rechargeSuccess.htm", method = RequestMethod.GET)
    public ModelAndView rechargeSuccess(ModelAndView mav, @RequestParam("rechargeId") Long rechargeId) {
        CompanyRechargesBean companyRechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
        mav.addObject("rechargeData", companyRechargesBean);
        mav.setViewName("xtr/account/rechargeSuccess");
        return mav;
    }

    /**
     * 企业提现
     *
     * @param rechargeMoney
     * @return
     */
    @RequestMapping(value = "withdraw.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse addWithdraw(HttpServletRequest request,
                                      @RequestParam("rechargeMoney") BigDecimal rechargeMoney) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            LOGGER.info("###### 开始企业提现申请 ######");

            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Long comapnyId = companyMembersBean.getMemberCompanyId();

            CompanyDepositBean companyDepositBean = companyDepositService.selectByCompanyId(comapnyId);
            if(companyDepositBean == null)
                return ResultResponse.buildFail(null).message("未设置提现账户");

            CompanyRechargesBean companyRechargesBean = companyRechargesService.companyWithdrawals(comapnyId, rechargeMoney, ClienEnum.WEB.getCode(),
                    companyDepositBean.getDepositBankName(), companyDepositBean.getDepositBankAccountName(), companyDepositBean.getDepositBankNumber(),
                    companyDepositBean.getDepositSubBankName());
            resultResponse.setData(companyRechargesBean.getRechargeId());
            resultResponse.setSuccess(true);

            LOGGER.info("###### 企业充值提现成功 ######");

        } catch (BusinessException e) {
            resultResponse.setMessage("提现失败");
            LOGGER.error("###### 企业提现申请失败 ######", e);
        }
        return resultResponse;
    }


    /**
     * 企业提现成功页面
     *
     * @param mav
     * @param rechargeId
     * @return
     */
    @RequestMapping(value = "withdrawSuccess.htm", method = RequestMethod.GET)
    public ModelAndView withdrawSuccess(ModelAndView mav, @RequestParam("rechargeId") Long rechargeId) {
        CompanyRechargesBean companyRechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
        mav.addObject("rechargeData", companyRechargesBean);
        mav.setViewName("xtr/account/withdrawSuccess");
        return mav;
    }



    /**
     * 新版首页充值   判断公司是否签约过协议
     * @param mav
     */
    @RequestMapping(value = "newRecharge.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse newRecharge(ModelAndView mav, HttpServletRequest request,String menu) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();
        //判断是否有我的账户权限
        ResultResponse  resultResponse = new ResultResponse();
        Map<String,Object> map =  new HashMap<String,Object>();
        boolean flag = false;
        try{

            if(StringUtils.isEmpty(menu)){
                resultResponse.setMessage("传参错误");
                resultResponse.setSuccess(false);
                return resultResponse;
            }
//           menu = new String(menu.getBytes("iso8859-1"),"utf-8");
            //判断是否有管理员权限
            if(companyMembersBean.getMemberIsdefault()!=null && companyMembersBean.getMemberIsdefault().intValue()==1){//管理员有一切的访问权限
                flag =true;
            }else{
                long menuId=companyMembersService.selectMenuIdByMenuName(menu);
                int role = companyMembersService.selectCountForMemberVisitMenu(companyMembersBean.getMemberId(),menuId);

                if(role>0)
                    flag=true;
            }

            map.put("flag",flag);
            //判断4个协议是否有一个是有效的
            CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
            companyProtocolsBean.setProtocolCompanyId(comapnyId);
            List<CompanyProtocolsBean> protocolList=companyProtocolsService.selectUsefulProtocolsByState(companyProtocolsBean);
            if(null == protocolList || protocolList.size()<=0){
                LOGGER.info("没有签约协议");
                map.put("hasProto",false);

            }else{
                map.put("hasProto",true);
            }

            resultResponse.setData(map);
            resultResponse.setSuccess(true);
            resultResponse.setMessage("操作成功");
        } catch ( Exception e ){
            LOGGER.error("当前点击充值出现异常错误："+e.getMessage(),e);
        }


        return resultResponse;
    }

}
