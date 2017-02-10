package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.company.CompanyMsgsBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.sbt.SbtBasicBean;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.sbt.SbtBasicService;
import com.xtr.api.service.sbt.SbtCityService;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;
import com.xtr.comm.sbt.api.SocialBase;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.company.CompanyShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.company.CompanysReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.xtr.comm.util.StringUtils.isStrNull;

/**
 * 创建社保企业订单
 *
 * @author 薛武
 * @createTime: 2016/9/26 14:30.
 */
@Service("shebaoOrderTask")
public class ShebaoOrderTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShebaoOrderTask.class);

    @Resource
    private CompanyShebaoOrderReaderMapper companyShebaoOrderReaderMapper;

    @Resource
    private CompanyShebaoOrderWriterMapper companyShebaoOrderWriterMapper;

    @Resource
    private CustomerShebaoOrderReaderMapper customerShebaoOrderReaderMapper;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private SbtService sbtService;

    @Resource
    private CustomerShebaoReaderMapper customerShebaoReaderMapper;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Resource
    private CustomerShebaoService customerShebaoService;

    @Resource
    private CustomerShebaoWriterMapper customerShebaoWriterMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CompanyMsgsService companyMsgsService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private CompanysReaderMapper companysReaderMapper;

    @Override
    public void run() throws Exception {
        //更新已过需求窗口期的订单 git test
        //所有待提交订单
        try {
            List<CompanyShebaoOrderBean> companyShebaoOrderBeans = companyShebaoOrderReaderMapper.selectOrderTimeOrder();
            LOGGER.info("已过需求日的当前订单：【" + JSONObject.toJSONString(companyShebaoOrderBeans) + "】");
            if(companyShebaoOrderBeans != null && companyShebaoOrderBeans.size()>0) {
                //批量获取企业名称
                List<Map<String,Object>> mapList=queryCompanyNameBatch(companyShebaoOrderBeans);
                for (CompanyShebaoOrderBean companyShebaoOrderBean : companyShebaoOrderBeans) {
                    //更新订单状态 和 详情
                    try{
                        companyShebaoService.updateOrderDetail(companyShebaoOrderBean.getId());
                        //发送短信消息和系统消息
                        sendMsg( companyShebaoOrderBean, mapList);
                    }catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("更新订单详情失败【" + companyShebaoOrderBean + "】", e);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("更新已过需求窗口期的订单失败", e);
        }


        //关闭所有已过订单窗口期 待提交、待付款订单
        try {
            int count = companyShebaoOrderWriterMapper.updateUnSubmitOrder();
            LOGGER.info("关闭已过订单窗口期 待提交、待付款订单：【" + count + "】");
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("更新已过订单窗口期的订单失败", e);
        }


        //生成已过截止日订单的下月新订单
        try {
            List<CompanyShebaoOrderBean> timeOutOrderBeans = companyShebaoOrderReaderMapper.selectTimeOutOrder();
            LOGGER.info("已过截止日的当前订单：【" + JSONObject.toJSONString(timeOutOrderBeans) + "】");
            if(timeOutOrderBeans != null && timeOutOrderBeans.size()>0) {
                for (CompanyShebaoOrderBean timeOutOrderBean : timeOutOrderBeans) {
                        try {
                            City city = sbtService.getCityByCode(timeOutOrderBean.getJoinCityCode());
                            if(city == null)
                                continue;
                            Basic basic = sbtService.getBasic(timeOutOrderBean.getJoinCityCode());

                            //获取当前企业订单
                            CompanyShebaoOrderBean newOrderBean = companyShebaoService.getCompanyShebaoOrderByCity(timeOutOrderBean.getCompanyId(), city, timeOutOrderBean.getJoinCityCode());
                            LOGGER.info("生成企业社保新订单：【" + JSONObject.toJSONString(newOrderBean) + "】");
                            if(newOrderBean.getOrderDate().after(timeOutOrderBean.getOrderDate())) {

                                //生成所有缴纳中员工的汇缴订单
                                //社保汇缴订单
                                List<CustomerShebaoBean> sbKeepCustomerShebaoBeans = customerShebaoReaderMapper.selectKeepCustomer(newOrderBean.getCompanyId(), newOrderBean.getJoinCityCode(), ShebaoTypeEnum.SHEBAO.getCode(), newOrderBean.getOrderDate());
                                LOGGER.info("本月社保汇缴用户：【" + JSONObject.toJSONString(sbKeepCustomerShebaoBeans) + "】");
                                if(sbKeepCustomerShebaoBeans != null && sbKeepCustomerShebaoBeans.size()>0) {
                                    for (CustomerShebaoBean sbKeepCustomerShebaoBean : sbKeepCustomerShebaoBeans) {
                                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, sbKeepCustomerShebaoBean.getSbType());
                                        
                                        //订单类型(1增员，2汇缴，3调基，4停缴, 5补缴)
                                        int orderType;
                                        BigDecimal base = sbKeepCustomerShebaoBean.getSbBase();
                                        BigDecimal newBase = customerShebaoOrderService.resetBase(socialBase, sbKeepCustomerShebaoBean, ShebaoTypeEnum.SHEBAO);
                                        //判断基数范围 s
                                        
                                        if(sbKeepCustomerShebaoBean.getSbShebaotongStatus() != 2){
                                            orderType = 1;
                                        }else {
                                            if(newBase != null) {
                                                orderType = 3;
                                                base = newBase;
                                            }else{
                                                orderType = 2;
                                            }
                                        }

                                        //创建需求订单
                                        customerShebaoOrderService.createOrder(sbKeepCustomerShebaoBean.getCustomerId(), orderType, newOrderBean.getId(), ShebaoTypeEnum.SHEBAO, newOrderBean.getOrderDate(), base, socialBase);
                                        customerShebaoOrderService.updateCustomerOrderDesc(newOrderBean.getId(), sbKeepCustomerShebaoBean.getCustomerId(), ShebaoTypeEnum.SHEBAO, orderType, city.getMonth());
                                    }
                                }

                                //公积金汇缴订单
                                List<CustomerShebaoBean> gjjKeepCustomerShebaoBeans = customerShebaoReaderMapper.selectKeepCustomer(newOrderBean.getCompanyId(), newOrderBean.getJoinCityCode(), ShebaoTypeEnum.GJJ.getCode(), newOrderBean.getOrderDate());
                                LOGGER.info("本月公积金汇缴用户：【" + JSONObject.toJSONString(gjjKeepCustomerShebaoBeans) + "】");
                                if(gjjKeepCustomerShebaoBeans != null && gjjKeepCustomerShebaoBeans.size()>0) {
                                    for (CustomerShebaoBean gjjKeepCustomerShebaoBean : gjjKeepCustomerShebaoBeans) {
                                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, gjjKeepCustomerShebaoBean.getGjjType());
                                        
                                        //订单类型(1增员，2汇缴，3调基，4停缴, 5补缴)
                                        int orderType;
                                        BigDecimal base = gjjKeepCustomerShebaoBean.getGjjBase();
                                        //判断基数范围
                                        BigDecimal newBase = customerShebaoOrderService.resetBase(socialBase, gjjKeepCustomerShebaoBean, ShebaoTypeEnum.GJJ);
                                        //如果员工未曾缴纳成功 则直接创建增员，缴纳成功着看是否有基数变动， 变动就创建 调基订单
                                        if(gjjKeepCustomerShebaoBean.getGjjShebaotongStatus() != 2){
                                            orderType = 1;
                                        }else {
                                            if(newBase != null) {
                                                orderType = 3;
                                                base = newBase;
                                            }else{
                                                orderType = 2;
                                            }
                                        }

                                        //创建需求订单
                                        customerShebaoOrderService.createOrder(gjjKeepCustomerShebaoBean.getCustomerId(), orderType, newOrderBean.getId(), ShebaoTypeEnum.GJJ, newOrderBean.getOrderDate(), base, socialBase);
                                        customerShebaoOrderService.updateCustomerOrderDesc(newOrderBean.getId(), gjjKeepCustomerShebaoBean.getCustomerId(), ShebaoTypeEnum.GJJ, orderType, city.getMonth());
                                    }
                                }

                                //生成所有待停缴员工的停缴订单
                                //本月社保待停缴员工
                                List<CustomerShebaoBean> sbStopCustomerShebaoBeen = customerShebaoReaderMapper.selectStopCustomer(newOrderBean.getCompanyId(), newOrderBean.getJoinCityCode(), ShebaoTypeEnum.SHEBAO.getCode(), newOrderBean.getOrderDate());
                                customerShebaoWriterMapper.updateStopCustomer(newOrderBean.getCompanyId(), newOrderBean.getJoinCityCode(), ShebaoTypeEnum.SHEBAO.getCode(), newOrderBean.getOrderDate());
                                LOGGER.info("本月社保停缴用户：【" + JSONObject.toJSONString(sbStopCustomerShebaoBeen) + "】");
                                if(sbStopCustomerShebaoBeen != null && sbStopCustomerShebaoBeen.size()>0) {
                                    for (CustomerShebaoBean customerShebaoBean : sbStopCustomerShebaoBeen) {
                                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, customerShebaoBean.getSbType());
                                        //创建停缴需求订单
                                        customerShebaoOrderService.createOrder(customerShebaoBean.getCustomerId(), 4, newOrderBean.getId(), ShebaoTypeEnum.SHEBAO, newOrderBean.getOrderDate(), customerShebaoBean.getSbBase(), socialBase);
                                        customerShebaoOrderService.updateCustomerOrderDesc(newOrderBean.getId(), customerShebaoBean.getCustomerId(), ShebaoTypeEnum.SHEBAO, 4, city.getMonth());
                                    }
                                }

                                //本月公积金待停缴员工
                                List<CustomerShebaoBean> gjjStopCustomerShebaoBeen = customerShebaoReaderMapper.selectStopCustomer(newOrderBean.getCompanyId(), newOrderBean.getJoinCityCode(), ShebaoTypeEnum.GJJ.getCode(), newOrderBean.getOrderDate());
                                LOGGER.info("本月公积金停缴用户：【" + JSONObject.toJSONString(gjjStopCustomerShebaoBeen) + "】");
                                customerShebaoWriterMapper.updateStopCustomer(newOrderBean.getCompanyId(), newOrderBean.getJoinCityCode(), ShebaoTypeEnum.GJJ.getCode(), newOrderBean.getOrderDate());
                                if(gjjStopCustomerShebaoBeen != null && gjjStopCustomerShebaoBeen.size()>0) {
                                    for (CustomerShebaoBean customerShebaoBean : gjjStopCustomerShebaoBeen) {
                                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, customerShebaoBean.getGjjType());
                                        //创建停缴需求订单
                                        customerShebaoOrderService.createOrder(customerShebaoBean.getCustomerId(), 4, newOrderBean.getId(), ShebaoTypeEnum.GJJ, newOrderBean.getOrderDate(), customerShebaoBean.getGjjBase(), socialBase);
                                        customerShebaoOrderService.updateCustomerOrderDesc(newOrderBean.getId(), customerShebaoBean.getCustomerId(), ShebaoTypeEnum.GJJ, 4, city.getMonth());
                                    }
                                }
                                //工资重算
                                //更新员工工资单状态
                                PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(timeOutOrderBean.getCompanyId());
                                if(payCycleBean != null){
                                    customerPayrollWriterMapper.updatePayRollStatusByCycle(payCycleBean.getId());
                                }
                            }//if end
                    }catch (Exception e){
                        e.printStackTrace();
                        LOGGER.error("生成新企业社保订单失败", e);
                        continue;
                    }
                }//for end
            }//if end
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("生成已过截止日订单的下月新订单失败", e);
        }

        //维护员工社保表的当前缴纳月份和当前企业订单，ps 只要企业订单已过截止日就不算当前订单
//        try{
//            customerShebaoWriterMapper.upToDateCurrent();
//        }catch (Exception e) {
//            e.printStackTrace();
//            LOGGER.error("维护员工社保表的当前缴纳失败", e);
//        }

    }

    /**
     * 根据企业ID批量查询企业信息与管理员手机号
     * @param companyShebaoOrderBeans
     * @return
     */
    private List<Map<String,Object>> queryCompanyNameBatch(List<CompanyShebaoOrderBean> companyShebaoOrderBeans){
        List<Long> companyIdList=new ArrayList<>();
        for(CompanyShebaoOrderBean orderBean:companyShebaoOrderBeans){
            if(orderBean.getCompanyId()!=null && !companyIdList.contains(orderBean.getCompanyId())){
                companyIdList.add(orderBean.getCompanyId());
            }
        }
        List<Map<String,Object>> mapList=companysReaderMapper.selectByPrimaryKeyBatch(companyIdList);
        return mapList;
    }

    /**
     * 发送消息
     * @param companyShebaoOrderBean
     * @param mapList
     * @throws Exception
     */
    private void sendMsg(CompanyShebaoOrderBean companyShebaoOrderBean,List<Map<String,Object>> mapList)throws Exception{
        Long companyId=companyShebaoOrderBean.getCompanyId();
        String areaName=companyShebaoOrderBean.getJoinCityName();
        String orderDate= DateUtil.dateFormatter.format(companyShebaoOrderBean.getOrderLastTime());
        String month=DateUtil.monthFormatterLine.format(companyShebaoOrderBean.getOrderDate());
        //获取企业名称与管理员手机号码
        String companyName="";
        String memberPhone="";
        if(mapList!=null && mapList.size()>0){
            for(Map<String,Object> map:mapList){
                if(map.get("company_id")!=null && ((Long)map.get("company_id")).longValue()==companyId.longValue()){
                    companyName=map.get("company_name")!=null?(String)map.get("company_name"):"";
                    memberPhone=map.get("member_phone")!=null?(String)map.get("member_phone"):"";
                    break;
                }
            }
        }
        //判断是否有社保协议与发工资协议
        String msgCont="";
        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectLastData(companyId, CompanyProtocolConstants.PROTOCOL_TYPE_DJ);
        if (companyProtocolsBean != null && companyProtocolsBean.getProtocolCurrentStatus()!=null &&
                (companyProtocolsBean.getProtocolCurrentStatus().intValue()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH || companyProtocolsBean.getProtocolCurrentStatus().intValue()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL)) {
            CompanyProtocolsBean companySalaryProtocolsBean = companyProtocolsService.selectLastData(companyId, CompanyProtocolConstants.PROTOCOL_TYPE_DF);
            if(companySalaryProtocolsBean != null && companySalaryProtocolsBean.getProtocolCurrentStatus()!=null &&
                    (companySalaryProtocolsBean.getProtocolCurrentStatus().intValue()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH || companySalaryProtocolsBean.getProtocolCurrentStatus().intValue()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL)){
                msgCont ="尊敬的"+companyName+"：您好，您"+month+"月的"+areaName+"社保账单已出，请在"+orderDate+"前付款，逾期未付款将造成发放工资和缴纳社保异常！";
            }else{
                msgCont ="尊敬的"+companyName+"：您好，您"+month+"月的"+areaName+"社保账单已出，请在"+orderDate+"前付款，逾期未付款将造成缴纳社保异常！";
            }
        } else {
            LOGGER.error("提交账单提示["+companyId+","+companyName+","+month+"月]"+"未签署社保协议");
        }
        if(!StringUtils.isStrNull(msgCont)){
            //发送系统消息
            sendSystemMsg( companyName, msgCont, companyId,month);
            //发送手机消息
            if(!StringUtils.isStrNull(memberPhone)){
                sendPhoneMsg( memberPhone, msgCont, companyId, month, companyName );
            }else{
                LOGGER.error("提交账单提示["+companyId+","+companyName+","+month+"月]"+"没有管理员手机号,不能发短信提醒");
            }
        }
    }

    /**
     * 发送系统消息
     * @param companyName
     * @param msgCont
     * @param companyId
     * @param month
     */
    private void sendSystemMsg(String companyName,String msgCont,Long companyId,String month){

        CompanyMsgsBean companyMsgsBean = new CompanyMsgsBean();
        String msgTitle="提交账单提示";
        companyMsgsBean.setMsgTitle(msgTitle);//标题
        companyMsgsBean.setMsgCont(msgCont);//消息内容
        companyMsgsBean.setMsgType(2);// 2为消息提醒类型
        companyMsgsBean.setMsgAddtime(new Date());//消息生成时间
        companyMsgsBean.setMsgCompanyId(companyId);//企业id
        companyMsgsBean.setMsgCompanyName(companyName);//企业名称
//        companyMsgsBean.setMsgDepId(collaborationCompanyBean.getMemberDepId());//long 类型
        companyMsgsBean.setMsgFromCompanyId(0L);//long  类型
        companyMsgsBean.setMsgSign(1);//状态  1：未读   2：已读  0：删除
        companyMsgsBean.setMsgClass(1);//1：收件箱  2：发件箱
        //插入发送消息
        int result = companyMsgsService.insert(companyMsgsBean);
        if(result<=0){
            LOGGER.error("提交账单提示["+companyId+","+companyName+","+month+"月]"+"插入系统消息失败");
        }else{
            LOGGER.error("提交账单提示["+companyId+","+companyName+","+month+"月]"+"插入系统消息成功");
        }
    }

    /**
     * 发送手机消息
     * @param memberPhone
     * @param msgCont
     * @param companyId
     * @param month
     * @param companyName
     * @throws Exception
     */
    private void sendPhoneMsg(String memberPhone,String msgCont,Long companyId,String month,String companyName )throws Exception{
        SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(memberPhone, msgCont);
        if(!sendMsgResponse.isSuccess()){
            LOGGER.error("提交账单提示["+companyId+","+companyName+","+month+"月]"+"发送手机短信失败");
        }else{
            LOGGER.error("提交账单提示["+companyId+","+companyName+","+month+"月]"+"发送手机短信成功");
        }
    }

}
