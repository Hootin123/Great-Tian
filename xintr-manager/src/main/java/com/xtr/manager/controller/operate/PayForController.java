package com.xtr.manager.controller.operate;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;
import com.xtr.api.domain.company.BorrowInfoBean;
import com.xtr.api.domain.company.FileResourcesBean;
import com.xtr.api.domain.station.StationCollaborationBean;
import com.xtr.api.domain.company.CompanyMsgsBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyBorrowOrdersService;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.comm.util.DateUtil;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("operate/payfor")
public class PayForController {

    public static final Logger LOGGER = LoggerFactory.getLogger(PayForController.class);

    @Resource
    private CompanyBorrowOrdersService companyBorrowOrdersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanyMsgsService companyMsgsService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private CompanyRechargesService companyRechargesService;

    /**
     * 借款列表页面
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "index.htm")
    public ModelAndView index(HttpServletRequest request, ModelAndView mav){
        mav.setViewName("xtr/operate/payfor/index");
        return mav;
    }

    /**
     * 放款列表页面
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "indexcredit.htm")
    public ModelAndView indexcredit(HttpServletRequest request, ModelAndView mav){
        mav.setViewName("xtr/operate/payfor/credit");
        return mav;
    }

    @RequestMapping(value = "dataList.htm")
    @ResponseBody
    public ResultResponse dataList(@RequestParam(value = "orderState", required = false) Integer orderState,
                                   @RequestParam(value = "companyName", required = false) String companyName,
                                   @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "6") int pageSize){

        CompanyBorrowOrdersBean companyBorrowOrdersBean=new CompanyBorrowOrdersBean();
        companyBorrowOrdersBean.setOrderState(orderState);
        companyBorrowOrdersBean.setCompanyName(companyName);
        return companyBorrowOrdersService.selectPageBorrowOrdersList(companyBorrowOrdersBean, pageIndex, pageSize);
    }

    /**
     * 放款审核处理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("getborrowInfo.htm")
    public ModelAndView toCompanyIntentHandlePage(ModelAndView mav,String itemId,Long orderId,String orderMoney,Long orderCompanyId,String companyName,String orderAddtime,String companyContactTel) {
        LOGGER.info("跳转到合作意向处理页面,传递参数ID:"+itemId);
        BorrowInfoBean borrowInfoBean=new BorrowInfoBean();
        borrowInfoBean.setBorrowOrderNumber(itemId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy.MM.dd HH:mm:ss");
        Date convertResult = null;
        try {
            convertResult = sdf.parse(orderAddtime);
            mav.addObject("orderAddtime",convertResult.getMonth()+1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<BorrowInfoBean> list=companyBorrowOrdersService.selectBorrowInfoBeanList(borrowInfoBean);
        mav.addObject("listBorrowInfoBean",list);
        mav.addObject("orderId",orderId);
        mav.addObject("orderMoney",orderMoney);
        mav.addObject("orderCompanyId",orderCompanyId);
        mav.addObject("companyName",companyName);
        mav.addObject("companyContactTel",companyContactTel);
        mav.setViewName("xtr/operate/payfor/creditHandle");
        return mav;
    }

    /**
     * 借款信息保存
     * @param companyBorrowOrdersBean
     * @return
     */
    @RequestMapping(value = "borrowInfoEdit.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse updateRecharge(HttpServletRequest request,CompanyBorrowOrdersBean companyBorrowOrdersBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean= SessionUtils.getUser(request);
            if(companyBorrowOrdersBean.getOrderState()==6){
                companyBorrowOrdersBean.setOrderLendtime(new Date());
                companyBorrowOrdersBean.setOrderLendUser(sysUserBean.getId().toString());
                subAccountService.loadRecharge(companyBorrowOrdersBean.getOrderCompanyId(),companyBorrowOrdersBean.getOrderMoney(),4,companyBorrowOrdersBean.getOrderId(),2);
                //封装消息实体类
                CompanyMsgsBean companyMsgsBean= new CompanyMsgsBean();
                companyMsgsBean.setMsgTitle("放款审核");
                companyMsgsBean.setMsgCont("您好，您申请的"+companyBorrowOrdersBean.getOrderAddtimeValue()+"月垫发工资的"+companyBorrowOrdersBean.getOrderMoney()+"元已到您的账户，请注意核查。");
                companyMsgsBean.setMsgType(2);
                companyMsgsBean.setMsgAddtime(new Date());
                companyMsgsBean.setMsgCompanyId(companyBorrowOrdersBean.getOrderCompanyId());
                companyMsgsBean.setMsgCompanyName(companyBorrowOrdersBean.getCompanyName());
                companyMsgsBean.setMsgSign(1);
                companyMsgsBean.setMsgClass(1);
                //插入发送消息
                int result = companyMsgsService.insert(companyMsgsBean);
                if(result>0){
                    String smscode="您好,您"+companyBorrowOrdersBean.getOrderMoney()+"元垫发"+companyBorrowOrdersBean.getOrderAddtimeValue()+"月工资的借款已到您的账户,请您核查.";
                    sendMsgService.sendMsg(companyBorrowOrdersBean.getCompanyContactTel(),smscode);
                }

                companyRechargesService.addRechargeByBorrowOrder(companyBorrowOrdersBean.getOrderId());

            }
            if(companyBorrowOrdersBean.getOrderState()==2){
                companyBorrowOrdersBean.setOrderCanceltime(new Date());
                companyBorrowOrdersBean.setOrderAuditUser(sysUserBean.getId().toString());
            }
            if(companyBorrowOrdersBean.getOrderState()==3){
                companyBorrowOrdersBean.setOrderAudittime(new Date());
                companyBorrowOrdersBean.setOrderAuditUser(sysUserBean.getId().toString());
            }
            if(companyBorrowOrdersBean.getOrderState()==4){
                companyBorrowOrdersBean.setOrderFinanceTime(new Date());
                companyBorrowOrdersBean.setOrderFinanceUser(sysUserBean.getId().toString());
            }
            companyBorrowOrdersService.updateCompanyBorrowOrdersBeanId(companyBorrowOrdersBean);
            resultResponse.setSuccess(true);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 借款信息保存
     * @param borrowInfoBean
     * @return
     */
    @RequestMapping(value = "borrowInfoAdd.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse addRecharge(HttpServletRequest request,BorrowInfoBean borrowInfoBean,CompanyBorrowOrdersBean companyBorrowOrdersBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean= SessionUtils.getUser(request);
            companyBorrowOrdersBean.setOrderFinanceTime(new Date());
            companyBorrowOrdersBean.setOrderFinanceUser(sysUserBean.getId().toString());
            int orderInterestType = companyBorrowOrdersBean.getOrderInterestType();
            int orderInterestCycle= companyBorrowOrdersBean.getOrderInterestCycle();
            if (orderInterestType == 1){
                companyBorrowOrdersBean.setOrderExpireTime(DateUtil.addDate(new Date(),orderInterestCycle));
            }
            else{
                companyBorrowOrdersBean.setOrderExpireTime(DateUtil.addDateOfMonth(new Date(),orderInterestCycle));
            }
            companyBorrowOrdersService.updateCompanyBorrowOrdersBeanId(companyBorrowOrdersBean);

            String[] ary = borrowInfoBean.getBorrowAccountMoneyValue().split(",");
            String[] aryName = borrowInfoBean.getBorrowBankName().split(",");
            String[] aryNo = borrowInfoBean.getBorrowBankNo().split(",");
            String[] aryNumber = borrowInfoBean.getBorrowSerialNumber().split(",");
            String[] aryRemark = borrowInfoBean.getBorrowRemark().split(",");
            for(int i=0;i<ary.length;i++){
                BorrowInfoBean infoBean=new BorrowInfoBean();
                infoBean.setBorrowOrderNumber(borrowInfoBean.getBorrowOrderNumber());
                infoBean.setBorrowBankName(aryName[i]);
                infoBean.setBorrowBankNo(aryNo[i]);
                infoBean.setBorrowSerialNumber(aryNumber[i]);
                infoBean.setBorrowAccountMoney(new BigDecimal(ary[i]));
                infoBean.setBorrowRemark(aryRemark[i]);
                infoBean.setBorrowAddtime(new Date());
                companyBorrowOrdersService.addBorrowInfoBean(infoBean);
            }
            resultResponse.setSuccess(true);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

}
