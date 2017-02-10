package com.xtr.core.service.shebao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderDescBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.shebao.CustomerShebaoSumDto;
import com.xtr.api.dto.shebao.JrOrderDto;
import com.xtr.api.dto.shebao.ResultSheBaoDto;
import com.xtr.api.dto.shebao.TJShebaoDto;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;
import com.xtr.comm.sbt.api.SocialBase;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.company.CompanyProtocolsReaderMapper;
import com.xtr.core.persistence.reader.company.CompanyShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderDescReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoOrderDescWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author Xuewu
 * @Date 2016/9/19.
 */
@Service("customerShebaoOrderService")
public class CustomerShebaoOrderServiceImpl implements CustomerShebaoOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerShebaoOrderServiceImpl.class);

    @Resource
    private CustomerShebaoOrderReaderMapper customerShebaoOrderReaderMapper;

    @Resource
    private CustomerShebaoOrderWriterMapper customerShebaoOrderWriterMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private SbtService sbtService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private CustomerShebaoService customerShebaoService;

    @Resource
    private CustomerShebaoReaderMapper customerShebaoReaderMapper;

    @Resource
    private CustomerShebaoWriterMapper customerShebaoWriterMapper;

    @Resource
    private CustomerShebaoOrderDescReaderMapper customerShebaoOrderDescReaderMapper;

    @Resource
    private CustomerShebaoOrderDescWriterMapper customerShebaoOrderDescWriterMapper;

    @Resource
    private CompanyShebaoOrderReaderMapper companyShebaoOrderReaderMapper;

    @Resource
    private CompanyShebaoOrderWriterMapper companyShebaoOrderWriterMapper;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CompanyProtocolsReaderMapper companyProtocolsReaderMapper;

    @Override
    @Transactional
    public boolean jrShebao(JrOrderDto dto, long companyId) throws BusinessException {
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(dto.getCustomerId());
        if(customersBean == null) {
            throw new BusinessException("员工信息不存在");
        }
        City city = sbtService.getCityByCode(dto.getCityCode());

        if(city == null)
            throw new BusinessException("无该城市基本信息");

        //获取当前企业订单
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderByCity(companyId, city, dto.getCityCode());
        if(companyShebaoOrderBean == null) {
            throw new BusinessException("企业订单不存在，请重试");
        }

        validateOrderDate(companyShebaoOrderBean);

        Date month = null;//缴纳月份
        try {
            month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("缴纳月份错误", e);
        }
        //更新员工社保参数表
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());

        Basic basic = sbtService.getBasic(dto.getCityCode());
        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, dto.getType());

        if(customerShebaoBean == null) {//新增记录
            customerShebaoBean = new CustomerShebaoBean();
            customerShebaoBean.setCustomerId(dto.getCustomerId());
            customerShebaoBean.setCompanyId(companyId);
            customerShebaoBean.setCurrentCompanyOrderId(companyShebaoOrderBean.getId());
            customerShebaoBean.setJoinCityName(city.getCname());
            customerShebaoBean.setJoinCityCode(dto.getCityCode());
            customerShebaoBean.setSbType(dto.getType());
            customerShebaoBean.setSbTypeName(socialBase.getName());
            customerShebaoBean.setSbBase(new BigDecimal(dto.getBase()));
            customerShebaoBean.setSbShebaotongStatus(1);
            customerShebaoBean.setIsSbKeep(1);
            customerShebaoBean.setSbJoinDate(month);
            customerShebaoBean.setCurrentMonth(month);
            customerShebaoBean.setCreateTime(new Date());
            customerShebaoWriterMapper.insert(customerShebaoBean);
        }else{

            if(new Integer(1).equals(customerShebaoBean.getIsSbKeep()) && !new Integer(4).equals(customerShebaoBean.getSbShebaotongStatus())) {
                throw new BusinessException("请先停缴社保");
            }

            if(new Integer(2).equals(customerShebaoBean.getSbShebaotongStatus())) {

                //todo 删除其他订单并创建汇缴订单！！！！！ 不能异常了
                throw new BusinessException("社保通当前正在缴纳中");
            }
//            CompanyShebaoOrderBean oldCompanyShebaoOrder = companyShebaoOrderReaderMapper.selectOrderByCityAndMonth(companyId, customerShebaoBean.getJoinCityCode(), city.getMonth());
//            if(oldCompanyShebaoOrder != null) {
//                //清除上个地区历史需求订单
//                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(), oldCompanyShebaoOrder.getId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);
//            }
            customerShebaoBean.setCurrentCompanyOrderId(companyShebaoOrderBean.getId());
            customerShebaoBean.setJoinCityName(city.getCname());
            customerShebaoBean.setJoinCityCode(dto.getCityCode());
            customerShebaoBean.setSbType(dto.getType());
            customerShebaoBean.setSbTypeName(socialBase.getName());
            customerShebaoBean.setSbBase(new BigDecimal(dto.getBase()));
            customerShebaoBean.setSbShebaotongStatus(1);
            customerShebaoBean.setIsSbKeep(1);
            customerShebaoBean.setSbJoinDate(month);
            customerShebaoBean.setCurrentMonth(month);
            customerShebaoBean.setSbStopDate(null);

            customerShebaoWriterMapper.updateByPrimaryKey(customerShebaoBean);
        }

        //清除社保历史需求订单
        cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(), companyShebaoOrderBean.getId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);

        //创建缴纳需求订单
        createOrder(dto.getCustomerId(), 1, companyShebaoOrderBean.getId(), ShebaoTypeEnum.SHEBAO, month, new BigDecimal(dto.getBase()), socialBase);
        updateCustomerOrderDesc(companyShebaoOrderBean.getId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO, 1, city.getMonth());

        if(dto.getIsOver() != null) {//创建补缴订单
            bj(dto.getOverOrders(),dto.getCityCode(), dto.getType(), companyShebaoOrderBean.getId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean jrGjj(JrOrderDto dto, Long companyId) throws BusinessException {
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null) {
            throw new BusinessException("员工社保信息不存在，请先缴纳社保");
        }

        if(new Integer(0).equals(customerShebaoBean.getIsSbKeep())) {
            throw new BusinessException("缴纳公积金时必须缴纳社保，请先设置缴纳社保");
        }

        if(new Integer(1).equals(customerShebaoBean.getIsGjjKeep()) && !new Integer(4).equals(customerShebaoBean.getGjjShebaotongStatus())) {
            throw new BusinessException("请先停缴公积金");
        }

        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null)
            throw new BusinessException("无该城市基本信息");

        Date month = null;//缴纳月份
        try {
            month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("缴纳月份错误");
        }

        //获取当前企业订单
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderByCity(companyId, city, customerShebaoBean.getJoinCityCode());
//        if(companyShebaoOrderBean == null) {
//            throw new BusinessException("企业订单不存在，请重试");
//        }
        validateOrderDate(companyShebaoOrderBean);

        //更新 社保基本信息
        if(new Integer(2).equals(customerShebaoBean.getSbShebaotongStatus())) {
            //todo 删除其他订单并创建汇缴订单！！！！！ 不能异常了
            throw new BusinessException("社保通当前正在缴纳中");
        }
        Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, dto.getType());

        customerShebaoBean.setGjjType(dto.getType());
        customerShebaoBean.setGjjTypeName(socialBase.getName());
        customerShebaoBean.setGjjBase(new BigDecimal(dto.getBase()));
        customerShebaoBean.setGjjShebaotongStatus(1);
        customerShebaoBean.setIsGjjKeep(1);
        customerShebaoBean.setGjjJoinDate(month);
        customerShebaoBean.setGjjStopDate(null);
        customerShebaoWriterMapper.updateByPrimaryKey(customerShebaoBean);

        //创建缴纳订单
        createOrder(dto.getCustomerId(), 1, customerShebaoBean.getCurrentCompanyOrderId(), ShebaoTypeEnum.GJJ, month, new BigDecimal(dto.getBase()), socialBase);
        updateCustomerOrderDesc(customerShebaoBean.getCurrentCompanyOrderId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ, 1, city.getMonth());
        if(dto.getIsOver() != null) {
            bj(dto.getOverOrders(), customerShebaoBean.getJoinCityCode(), dto.getType(), customerShebaoBean.getCurrentCompanyOrderId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ);
        }

        return true;
    }


    @Override
    public void updateCustomerOrderDesc(long companyOrderId, long customerId, ShebaoTypeEnum shebaoTypeEnum, int orderType, String month) {
        CustomerShebaoOrderDescBean desc = customerShebaoOrderDescWriterMapper.selectByCompanyOrderIdAndCustomerId(companyOrderId, customerId);
        if(desc == null) {
            desc = new CustomerShebaoOrderDescBean();
            desc.setCustomerId(customerId);
            desc.setCompanyShebaoOrderId(companyOrderId);
        }
        if(shebaoTypeEnum == ShebaoTypeEnum.SHEBAO) {
            //1增员，2汇缴，3调基，4,停缴, 5补缴
            if(orderType == 1) {
                desc.setSbZyText(month);
                desc.setSbHjText(null);
                desc.setSbStopText(null);
                desc.setSbTjText(null);
            } else if(orderType == 2) {
                desc.setSbZyText(null);
                desc.setSbHjText(month);
                desc.setSbStopText(null);
                desc.setSbTjText(null);
            }else if(orderType == 3) {
                desc.setSbZyText(null);
                desc.setSbBjText(null);
                desc.setSbHjText(null);
                desc.setSbStopText(null);
                desc.setSbTjText(month);
            }else if(orderType == 4) {
                desc.setSbZyText(null);
                desc.setSbBjText(null);
                desc.setSbHjText(null);
                desc.setSbStopText(month);
                desc.setSbTjText(null);
            }else if(orderType == 5) {
                desc.setSbBjText(month);
            }
        }else{
            //1增员，2汇缴，3调基，4,停缴, 5补缴
            if(orderType == 1) {
                desc.setGjjZyText(month);
                desc.setGjjHjText(null);
                desc.setGjjStopText(null);
                desc.setGjjTjText(null);
            }else if(orderType == 2) {
                desc.setGjjZyText(null);
                desc.setGjjHjText(month);
                desc.setGjjStopText(null);
                desc.setGjjTjText(null);
            }else if(orderType == 3) {
                desc.setGjjZyText(null);
                desc.setGjjBjText(null);
                desc.setGjjHjText(null);
                desc.setGjjStopText(null);
                desc.setGjjTjText(month);
            }else if(orderType == 4) {
                desc.setGjjZyText(null);
                desc.setGjjBjText(null);
                desc.setGjjHjText(null);
                desc.setGjjStopText(month);
                desc.setGjjTjText(null);
            }else if(orderType == 5) {
                desc.setGjjBjText(month);
            }
        }
        if(desc.getId() == null) {
            customerShebaoOrderDescWriterMapper.insert(desc);
        }else{
            customerShebaoOrderDescWriterMapper.updateByPrimaryKey(desc);
        }
    }

    @Override
    public Map<Integer, List> getOrderFaildMsg(long customerId, long companyShebaoOrderId) {
        //查询上月缴纳订单状态
        Map<Integer, List> lastResult = new HashMap<>();
        List<CustomerShebaoOrderBean> lastOrders = customerShebaoOrderReaderMapper.selectCustomerLastOrder(companyShebaoOrderId, customerId);
        if(lastOrders != null) {
            List<Date> sbBjCgDates = new ArrayList<>();
            List<Date> gjjBjCgDates = new ArrayList<>();

            lastResult.put(ShebaoTypeEnum.GJJ.getCode(), new ArrayList<>());
            lastResult.put(ShebaoTypeEnum.SHEBAO.getCode(), new ArrayList<>());
            for (CustomerShebaoOrderBean lastOrder : lastOrders) {
                //订单类型(1增员，2汇缴，3调基，4停缴, 5补缴)
                Integer shebaoTypeEnum = lastOrder.getRequireType();
                String text = (lastOrder.getSbtStatus() == 2  || lastOrder.getSbtStatus() == 3? "失败：" : "成功：") + DateUtil.sbtSimpleDateFormat.format(lastOrder.getOverdueMonth());
                if(lastOrder.getOrderType() == 1) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'增员" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 2) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'续缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 3) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'调基" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 4) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'停缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 5) {//补缴合并
//                    lastResult.get(shebaoTypeEnum).add("{'text':'补缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");

                    if(shebaoTypeEnum == ShebaoTypeEnum.GJJ.getCode()) {
                        if(lastOrder.getSbtStatus() == 1) {
                            gjjBjCgDates.add(lastOrder.getOverdueMonth());
                        }else{
                            lastResult.get(shebaoTypeEnum).add("{'text':'补缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                        }
                    }else{
                        if(lastOrder.getSbtStatus() == 1) {
                            sbBjCgDates.add(lastOrder.getOverdueMonth());
                        }else{
                            lastResult.get(shebaoTypeEnum).add("{'text':'补缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                        }
                    }
                }
            }
            //合并上月补缴信息
            if(sbBjCgDates.size() > 0) {
                Collections.sort(sbBjCgDates);
                List<String> strings = joinDate(sbBjCgDates);
                for (String string : strings) {
                    lastResult.get(ShebaoTypeEnum.SHEBAO.getCode()).add("{'text':'补缴成功：" + string+"','reason':'null'}");
                }
            }

            if(gjjBjCgDates.size() > 0) {
                Collections.sort(gjjBjCgDates);
                List<String> strings = joinDate(gjjBjCgDates);
                for (String string : strings) {
                    lastResult.get(ShebaoTypeEnum.GJJ.getCode()).add("{'text':'补缴成功：" + string+"','reason':'null'}");
                }
            }

        }
        return lastResult;
    }

    /**
     * 获取社保订单
     * @param memerId
     * @param orderId
     * @return
     */
    @Override
    public CustomerShebaoOrderDto getShebaoOrder(Long memerId, Long orderId, int requireType) {
        if(null != memerId && null != orderId){
            CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
            customerShebaoOrderBean.setCustomerId(memerId);
            customerShebaoOrderBean.setCompanyShebaoOrderId(orderId);
            customerShebaoOrderBean.setRequireType(requireType);
            customerShebaoOrderBean.setOrderType(5);
            CustomerShebaoOrderBean order = customerShebaoOrderReaderMapper.selectShebaoOrder(customerShebaoOrderBean);

            CustomerShebaoOrderDto customerShebaoOrderDto = null;
            if(order==null || !StringUtils.isNotBlank(order.getOrderDetail())){
                if(requireType==1){
                    throw new BusinessException("无社保详情");
                }else if(requireType==2){
                    throw new BusinessException("无公积金详情");
                }
            }
            if(null != order){
                customerShebaoOrderDto = new CustomerShebaoOrderDto();
                customerShebaoOrderDto.setCustomberOrder(order);
            }

//            // 查询是否有补缴
//            int count = customerShebaoOrderDescReaderMapper.selectBjShebaoGjj(memerId, orderId, requireType);
//            if(null != customerShebaoOrderDto && count > 0){
//                customerShebaoOrderBean.setOrderType(5);
//                List<CustomerShebaoOrderBean> bjList = customerShebaoOrderReaderMapper.selectShebaoOrderList(customerShebaoOrderBean);
//                customerShebaoOrderDto.setBjList(bjList);
//            }

            //查询所有未失败的订单
            List<CustomerShebaoOrderBean> successList=customerShebaoOrderReaderMapper.selectSuccessShebaoOrderList(customerShebaoOrderBean);
            customerShebaoOrderDto.setBjList(successList);
            return customerShebaoOrderDto;
        }
        return null;
    }

    public int cleanOrderByCustomerIdAndCompanyOrderId(long customerId, long companyOrderId, int requireType, Object... orderType) {
        if(orderType == null || orderType.length < 1) {
            return 0;
        }

        return customerShebaoOrderWriterMapper.deleteHisOrder(customerId, companyOrderId, requireType, StringUtils.join(orderType, ","));
    }


    public void bj(List<JrOrderDto> list, String cityCode, String type, long companyShebaoOrderId, long customerId, ShebaoTypeEnum sheBaoType) {
        if(list != null && list.size() > 0) {
            //清除社保历史需求订单
            cleanOrderByCustomerIdAndCompanyOrderId(customerId, companyShebaoOrderId, sheBaoType.getCode(), 5);
            if(list.size() > 1) {
                JrOrderDto firstJrOrderDto = list.get(0);
                JrOrderDto lastJrOrderDto = list.get(list.size() - 1);
                String month = firstJrOrderDto.getMonth() + "-" + lastJrOrderDto.getMonth();
                updateCustomerOrderDesc(companyShebaoOrderId, customerId, sheBaoType, 5, month);
            }else{
                JrOrderDto firstJrOrderDto = list.get(0);
                updateCustomerOrderDesc(companyShebaoOrderId, customerId, sheBaoType, 5, firstJrOrderDto.getMonth());
            }
            Basic basic = sbtService.getBasic(cityCode);
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(sheBaoType, basic, type);
            List<Map> calResults = null;
            try {
                calResults = customerShebaoService.calOverDueBySbt(list, socialBase);
            } catch (IOException e) {
                e.printStackTrace();
                throw new  BusinessException("补缴计算失败");
            }
            int index = 0;
            for (JrOrderDto jrShebaoDto : list) {
                Map calResult = (Map) calResults.get(index);
                index++;

                CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
                customerShebaoOrderBean.setCustomerId(customerId);
                customerShebaoOrderBean.setCompanyShebaoOrderId(companyShebaoOrderId);
                customerShebaoOrderBean.setShebaoType(type);
                customerShebaoOrderBean.setRequireType(sheBaoType.getCode());
                customerShebaoOrderBean.setOrderType(5);
                customerShebaoOrderBean.setOrderBase(new BigDecimal(jrShebaoDto.getBase()));
                try {
                    customerShebaoOrderBean.setOverdueMonth(DateUtil.sbtSimpleDateFormat.parse(jrShebaoDto.getMonth()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new BusinessException("补缴日期格式异常");
                }
                String orderDetail = JSONObject.toJSONString(calResult.get("rows"));
                customerShebaoOrderBean.setOrderDetail(orderDetail);
                customerShebaoOrderBean.setOrgSum(new BigDecimal(calResult.get("orgSum") + ""));
                customerShebaoOrderBean.setEmpSum(new BigDecimal(calResult.get("empSum") + ""));
                customerShebaoOrderBean.setStatus(0);
                customerShebaoOrderBean.setPayrollStatus(0);
                customerShebaoOrderBean.setSbtStatus(0);
                customerShebaoOrderBean.setCreateTime(new Date());
                customerShebaoOrderWriterMapper.insert(customerShebaoOrderBean);
            }
        }

    }

    @Override
    public void cleanBj(long companyShebaoOrderId, long customerId, ShebaoTypeEnum sheBaoType) {
        cleanOrderByCustomerIdAndCompanyOrderId(customerId, companyShebaoOrderId, sheBaoType.getCode(), 5);
        updateCustomerOrderDesc(companyShebaoOrderId, customerId, sheBaoType, 5, "");
    }


    /**
     * 创建需求订单
     * @param customerId
     * @param orderType
     * @param companyOrderId
     * @param shebaoTypeEnum
     * @param base
     * @return
     */
    public CustomerShebaoOrderBean createOrder(long customerId, int orderType, long companyOrderId, ShebaoTypeEnum shebaoTypeEnum, Date orderMonth, BigDecimal base, SocialBase socialBase){
        //创建缴纳需求订单
        CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
        customerShebaoOrderBean.setCustomerId(customerId);
        customerShebaoOrderBean.setOrderType(orderType);
        customerShebaoOrderBean.setCompanyShebaoOrderId(companyOrderId);
        customerShebaoOrderBean.setRequireType(shebaoTypeEnum.getCode());
        customerShebaoOrderBean.setShebaoType(socialBase.getType());
        customerShebaoOrderBean.setOrderBase(base);
        customerShebaoOrderBean.setOverdueMonth(orderMonth);
        if(orderType != 4) {
            try {
                Map cal = customerShebaoService.cal(base.doubleValue(), socialBase);
                SocialBase socialBaseKey = (SocialBase) cal.keySet().toArray()[0];
                String orderDetail = JSONObject.toJSONString(cal.get(socialBaseKey));
                customerShebaoOrderBean.setOrderDetail(orderDetail);
                customerShebaoOrderBean.setOrgSum(new BigDecimal(socialBaseKey.getOrgSum()));
                customerShebaoOrderBean.setEmpSum(new BigDecimal(socialBaseKey.getEmpSum()));
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException("社保各项值计算失败");
            }
        }
        customerShebaoOrderBean.setStatus(0);
        customerShebaoOrderBean.setPayrollStatus(0);
        customerShebaoOrderBean.setSbtStatus(0);
        customerShebaoOrderBean.setCreateTime(new Date());
        customerShebaoOrderWriterMapper.insert(customerShebaoOrderBean);
        return customerShebaoOrderBean;
    }

    /**
     * 社保公积金停缴
     * @param dto
     * @param stopDataParam
     * @return
     * @throws BusinessException
     */
    @Transactional
    public CompanyShebaoOrderBean stopPayment(TJShebaoDto dto,String stopDataParam) throws Exception {
        //社保公积金停缴的相关验证
        ResultSheBaoDto resultSheBaoDto=validateStopPaymentBase(dto,stopDataParam);

        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean=resultSheBaoDto.getCustomerShebaoBean();
        LOGGER.info("社保公积金停缴,返回员工社保公积金基本信息:"+ JSON.toJSONString(customerShebaoBean));

        //获取企业订单信息
        CompanyShebaoOrderBean companyShebaoOrderBean=resultSheBaoDto.getCompanyShebaoOrderBean();
        LOGGER.info("社保公积金停缴,返回企业订单信息:"+ JSON.toJSONString(companyShebaoOrderBean));

        //获取城市信息
        City city=resultSheBaoDto.getCity();
        LOGGER.info("社保公积金停缴,返回城市信息:"+ JSON.toJSONString(city));

        //将停缴时间转换为DATE
        Date stopDate=DateUtil.stringToDate(stopDataParam,DateUtil.DATEMONTHFORLINE);
        //企业订单ID
        dto.setCompanyShebaoOrderId(customerShebaoBean.getCurrentCompanyOrderId());
        //当前缴纳月份
//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        Date month=customerShebaoBean.getCurrentMonth();
        String monthStr=DateUtil.dayFormatter.format(month);

        //停缴日期验证
        validateStopPayment( dto, stopDataParam, month, customerShebaoBean);

        String nowMonthStr=DateUtil.dateToString(month,DateUtil.DATEMONTHFORMAT);//当前缴纳月份字符串格式
        String stopDateStr=DateUtil.dateToString(stopDate,DateUtil.DATEMONTHFORMAT);//要缴纳的月份字符串格式
        //停缴的日期和当前账单月份相等的情况下才能删除历史订单并新增员工订单,其它未来的停缴任务由定时任务来完成
        //社保通状态为未交或为空,则删除员工订单;社保通状态为未交或空之外的状态,则删除员工订单,并新增员工订单
        if(stopDateStr.equals(nowMonthStr)){
            //清除地区历史需求订单
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);
                //如果之前无缴纳,或才缴纳没有提交到社保通直接停缴,更改企业订单的详情(这儿由于事务的原因,到controller那儿再操作一遍),更改员工基本信息是否缴纳为否,将企业汇总信息更新为空
                if(customerShebaoBean.getSbShebaotongStatus()==null || (customerShebaoBean.getSbShebaotongStatus()!=null && customerShebaoBean.getSbShebaotongStatus().intValue()!=ShebaoConstants.SBT_STATE_ING)){
                    //更新员工社保公积金基本信息,只更改是否缴纳为否
                    updateStopPayment(dto, customerShebaoBean, stopDate,1);
                    updateCustomerDescIsNull(dto.getCompanyShebaoOrderId(), dto.getCustomerId(),dto.getRelationType());
                }else{//正常的停缴
                    //新增员工订单并更新用户社保公积金汇总数据
                    addStopPaymentOrder(dto,customerShebaoBean);
                    updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO, ShebaoConstants.OrderType.STOP, monthStr);
                    //更新员工社保公积金基本信息,更改是否缴纳为否并且更改缴纳日期
                    updateStopPayment(dto, customerShebaoBean, stopDate,2);
                }
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ.getCode(), 1, 2, 3, 4, 5);
                //如果之前无缴纳,或才缴纳没有提交到社保通直接停缴,更改企业订单的详情(这儿由于事务的原因,到controller那儿再操作一遍),更改员工基本信息是否缴纳为否,将企业汇总信息更新为空
                if(customerShebaoBean.getGjjShebaotongStatus()==null || (customerShebaoBean.getGjjShebaotongStatus()!=null && customerShebaoBean.getGjjShebaotongStatus().intValue()!=ShebaoConstants.SBT_STATE_ING)){
                    //更新员工社保公积金基本信息,只更改是否缴纳为否
                    updateStopPayment(dto, customerShebaoBean, stopDate,1);
                    updateCustomerDescIsNull(dto.getCompanyShebaoOrderId(), dto.getCustomerId(),dto.getRelationType());
                }else{//正常的停缴
                    //新增员工订单并更新用户社保公积金汇总数据
                    addStopPaymentOrder(dto,customerShebaoBean);
                    updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ, ShebaoConstants.OrderType.STOP, monthStr);
                    //更新员工社保公积金基本信息,更改是否缴纳为否并且更改缴纳日期
                    updateStopPayment(dto, customerShebaoBean, stopDate,2);
                }
            }
        }else{
            //更新员工社保公积金基本信息,只更改缴纳日期
            updateStopPayment(dto, customerShebaoBean, stopDate,3);
        }
        return companyShebaoOrderBean;
    }

    /**
     * 社保公积金停缴的基础验证
     * @param dto
     */
    public ResultSheBaoDto validateStopPaymentBase(TJShebaoDto dto,String stopDatestr)throws BusinessException,Exception{
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();
        if(dto==null || dto.getCustomerId()==null || dto.getRelationType()==null
                ||(dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }
        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null || customerShebaoBean.getCurrentCompanyOrderId()==null) {
            throw new BusinessException("无员工社保公积金基本信息");
        }
        //获取企业订单信息
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(customerShebaoBean.getCurrentCompanyOrderId());
        if(companyShebaoOrderBean==null){
            throw new BusinessException("无企业订单信息");
        }

        //获取城市信息
        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
            throw new BusinessException("无该城市缴纳月份");
        }
        //当前缴纳月份
//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        if(customerShebaoBean.getCurrentMonth()==null){
            throw new BusinessException("无该城市缴纳月份");
        }
        Date month=customerShebaoBean.getCurrentMonth();
        Date stopDate =null;
        if(!StringUtils.isEmpty(stopDatestr)){
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
            stopDate=sm.parse(stopDatestr);
        }else{
            throw new BusinessException("未设置停缴月份");
        }

        if((stopDate.getTime()-month.getTime()<=0) &&companyShebaoOrderBean!=null && companyShebaoOrderBean.getStatus()!=null && companyShebaoOrderBean.getStatus().intValue()>=2){
            throw new BusinessException("账单已提交或关闭,不能停缴");
        }
        //当前时间在订单提交截止日之后则不能操作
        Date orderLastTime=companyShebaoOrderBean.getOrderLastTime();
        if(orderLastTime!=null && new Date().after(orderLastTime)){
            throw new BusinessException("已经超过订单提交截止日,不能停缴");
        }

        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            //公积金缴纳了,但没有停缴,则社保不能停缴,必须公积金先停缴,社保才能停缴;如果公积金没有缴纳,社保则直接可以停缴
            if(customerShebaoBean.getGjjStopDate()==null && customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep()==ShebaoConstants.KEEP_YES){
                throw new BusinessException("公积金处于缴纳状态，不能停缴社保");
            }
        }

        Date stopPaymentDate=null;//将要停缴的月份
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            if(customerShebaoBean.getSbStopDate()!=null){
                stopPaymentDate=customerShebaoBean.getSbStopDate();
            }
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            if(customerShebaoBean.getGjjStopDate()!=null){
                stopPaymentDate=customerShebaoBean.getGjjStopDate();
            }
        }
        if(stopPaymentDate==null//停缴日期为空,缴纳了才能停缴,没缴纳不能停缴
                ||(stopPaymentDate!=null && stopPaymentDate.before(month))){//如果停缴日期<当前缴纳月份,缴纳了才能停缴,没缴纳不能停缴
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                if(customerShebaoBean.getIsSbKeep()==null ){
                    throw new BusinessException("该员工没有缴纳社保,不能停缴");
                }
                if(customerShebaoBean.getIsSbKeep()!=null && customerShebaoBean.getIsSbKeep().intValue()==ShebaoConstants.KEEP_NO){
                    throw new BusinessException("该员工没有缴纳社保,不能停缴");
                }
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                if(customerShebaoBean.getIsGjjKeep()==null ){
                    throw new BusinessException("该员工没有缴纳公积金,不能停缴");
                }
                if(customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep().intValue()==ShebaoConstants.KEEP_NO){
                    throw new BusinessException("该员工没有缴纳公积金,不能停缴");
                }
            }
        }else {//停缴日期不为空
            //如果当前缴纳月份==停缴月份，则提示"当前月为该员工离职月,不能修改或提交需求"
            String nowMonthStr = DateUtil.dateToString(month, DateUtil.DATEMONTHFORMAT);//当前缴纳月份字符串格式
            String stopDateStr = DateUtil.dateToString(stopPaymentDate, DateUtil.DATEMONTHFORMAT);//要停缴的月份字符串格式
//            if (nowMonthStr.equals(stopDateStr)) {
//                throw new BusinessException("当前月为该员工离职月,不能修改或提交需求");
//            }
            if (stopPaymentDate.after(month)) {//如果停缴月份>当前缴纳月份,则提示"该员工已设置社保停缴"
                resultSheBaoDto.setAlreadyStopInfo("sureMsg");
                resultSheBaoDto.setAlreadyStopDateStr(stopDateStr);
            }
        }
        resultSheBaoDto.setCustomerShebaoBean(customerShebaoBean);
        resultSheBaoDto.setCompanyShebaoOrderBean(companyShebaoOrderBean);
        resultSheBaoDto.setCity(city);
        return resultSheBaoDto;
    }

    @Override
    public SocialBase getGjjBjMonth(Date obmonth, Date oemonth, long customerId) {
        Date oEndMonth = null;
        Date oStartMonth = null;
        try {
            Calendar endMonth = Calendar.getInstance();
            endMonth.setTime(oemonth);
            for(;endMonth.getTimeInMillis() >= obmonth.getTime(); endMonth.add(Calendar.MONTH, -1)) {
                if(customerShebaoOrderReaderMapper.selectOverMonth(endMonth.getTime(), customerId) > 0) {//有社保缴纳
                    if(oEndMonth == null) {
                        oEndMonth = endMonth.getTime();
                    }
                    oStartMonth = endMonth.getTime();
                }else{
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("计算公积金补缴月份失败：{}", e);
        }
        SocialBase socialBase = new SocialBase();
        if(oEndMonth != null) {
            socialBase.setOemonth(DateUtil.sbtSimpleDateFormat.format(oEndMonth));
            socialBase.setObmonth(DateUtil.sbtSimpleDateFormat.format(oStartMonth));
        }
        return socialBase;
    }

    /**
     * 社保公积金停缴的停缴月份验证
     * @param stopDataParam
     * @param stopDataParam
     */
    private void validateStopPayment(TJShebaoDto dto,String stopDataParam,Date month,CustomerShebaoBean customerShebaoBean)throws BusinessException,Exception{
        if(com.xtr.comm.util.StringUtils.isStrNull(stopDataParam)){
            throw new BusinessException("请选择停缴日期");
        }
        //将停缴时间转换为DATE
        Date stopDate=DateUtil.stringToDate(stopDataParam,DateUtil.DATEMONTHFORLINE);
        if(stopDate.before(month)){
            throw new BusinessException("停缴选择的月份不能小于当前账单月份");
        }
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            //公积金缴纳了,但没有停缴,则社保不能停缴,必须公积金先停缴,社保才能停缴;如果公积金没有缴纳,社保则直接可以停缴
            if(customerShebaoBean.getGjjStopDate()==null && customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep()==ShebaoConstants.KEEP_YES){
                throw new BusinessException("公积金处于缴纳状态，不能停缴社保");
            }
            if(customerShebaoBean.getGjjStopDate()!=null && customerShebaoBean.getGjjStopDate().after(stopDate)){
                throw new BusinessException("缴纳公积金时必须先缴纳社保");
            }
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            if(customerShebaoBean.getSbStopDate()!=null && stopDate.after(customerShebaoBean.getSbStopDate())){
                throw new BusinessException("选择的停缴月份晚于社保停缴月份，请重新选择停缴月份");
            }
        }
    }
    /**
     * 创建停缴需求订单
     * @param dto
     * @param customerShebaoBean
     */
    private void addStopPaymentOrder(TJShebaoDto dto,CustomerShebaoBean customerShebaoBean)throws Exception{
        CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
        customerShebaoOrderBean.setCustomerId(dto.getCustomerId());
        customerShebaoOrderBean.setCompanyShebaoOrderId(dto.getCompanyShebaoOrderId());
        customerShebaoOrderBean.setShebaoType(dto.getType());
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            customerShebaoOrderBean.setRequireType(ShebaoTypeEnum.SHEBAO.getCode());
            customerShebaoOrderBean.setShebaoType(customerShebaoBean.getSbType());
            customerShebaoOrderBean.setOrderBase(customerShebaoBean.getSbBase());
            customerShebaoOrderBean.setOverdueMonth(customerShebaoBean.getCurrentMonth());
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            customerShebaoOrderBean.setRequireType(ShebaoTypeEnum.GJJ.getCode());
            customerShebaoOrderBean.setShebaoType(customerShebaoBean.getGjjType());
            customerShebaoOrderBean.setOrderBase(customerShebaoBean.getGjjBase());
            customerShebaoOrderBean.setOverdueMonth(customerShebaoBean.getCurrentMonth());
        }
        customerShebaoOrderBean.setOrderType(ShebaoConstants.OrderType.STOP);
        customerShebaoOrderBean.setStatus(ShebaoConstants.ORDER_STATUS_NEW);
        customerShebaoOrderBean.setPayrollStatus(ShebaoConstants.PAYROLL_STATUS_NO);
        customerShebaoOrderBean.setSbtStatus(ShebaoConstants.SBT_ORDERSTATUS_INIT);
        customerShebaoOrderBean.setCreateTime(new Date());
        int shebaoOrderCount=customerShebaoOrderWriterMapper.insert(customerShebaoOrderBean);
        if(shebaoOrderCount<=0){
            throw new BusinessException("新增社保公积金停缴订单失败");
        }
    }

    /**
     * 更新停缴员工社保公积金基本信息
     * @param dto
     * @param customerShebaoBean
     */
    private void updateStopPayment(TJShebaoDto dto,CustomerShebaoBean customerShebaoBean,Date stopDate,int type){
        CustomerShebaoBean uptCustomerShebaoBean=new CustomerShebaoBean();
        uptCustomerShebaoBean.setId(customerShebaoBean.getId());
        if(type==1){//当前月停缴,并且无社保通状态
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                uptCustomerShebaoBean.setIsSbKeep(ShebaoConstants.KEEP_NO);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                uptCustomerShebaoBean.setIsGjjKeep(ShebaoConstants.KEEP_NO);
            }
        }else if(type==2){//当前月停缴,有社保通状态
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                uptCustomerShebaoBean.setSbStopDate(stopDate);
                uptCustomerShebaoBean.setIsSbKeep(ShebaoConstants.KEEP_NO);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                uptCustomerShebaoBean.setGjjStopDate(stopDate);
                uptCustomerShebaoBean.setIsGjjKeep(ShebaoConstants.KEEP_NO);
            }
        }else if(type==3){//未来月份停缴
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                uptCustomerShebaoBean.setSbStopDate(stopDate);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                uptCustomerShebaoBean.setGjjStopDate(stopDate);
            }
        }
        int count=customerShebaoWriterMapper.updateByPrimaryKeySelective(uptCustomerShebaoBean);
        if(count<=0){
            throw new BusinessException("更新社保公积金信息失败");
        }
    }

    /**
     * 将员工汇总数据清空
     * @param companyOrderId
     * @param customerId
     * @param shebaoTypeEnum
     */
    private void updateCustomerDescIsNull(Long companyOrderId, Long customerId,int shebaoTypeEnum){
        CustomerShebaoOrderDescBean desc = customerShebaoOrderDescWriterMapper.selectByCompanyOrderIdAndCustomerId(companyOrderId, customerId);
        if(desc != null) {
            if(shebaoTypeEnum == ShebaoConstants.RELATION_TYPE_SHEBAO) {
                desc.setSbZyText(null);
                desc.setSbHjText(null);
                desc.setSbStopText(null);
                desc.setSbTjText(null);
                desc.setSbBjText(null);
                customerShebaoOrderDescWriterMapper.updateByPrimaryKey(desc);
            }else if(shebaoTypeEnum == ShebaoConstants.RELATION_TYPE_GONGJIJING){
                desc.setGjjZyText(null);
                desc.setGjjBjText(null);
                desc.setGjjHjText(null);
                desc.setGjjStopText(null);
                desc.setGjjTjText(null);
                customerShebaoOrderDescWriterMapper.updateByPrimaryKey(desc);
            }
        }
    }
    /**
     * 社保公积金调基
     * @param dto
     * @return
     * @throws BusinessException
     */
    @Transactional
    public CompanyShebaoOrderBean adjustBase(TJShebaoDto dto,BigDecimal adjustAmount) throws Exception {
        //调基相关验证
        ResultSheBaoDto resultSheBaoDto=validateAdjustBase( dto, adjustAmount);

        //获取员工社保公积金基本信息
        CustomerShebaoBean customerShebaoBean=resultSheBaoDto.getCustomerShebaoBean();
        //企业订单ID
        dto.setCompanyShebaoOrderId(customerShebaoBean.getCurrentCompanyOrderId());
        LOGGER.info("社保公积金调基,返回员工社保公积金基本信息:"+ JSON.toJSONString(customerShebaoBean));

        //获取企业社保公积金订单信息
        CompanyShebaoOrderBean companyShebaoOrderBean=resultSheBaoDto.getCompanyShebaoOrderBean();
        LOGGER.info("社保公积金调基,返回企业订单信息:"+ JSON.toJSONString(companyShebaoOrderBean));

        //获取城市信息
        City city=resultSheBaoDto.getCity();
        LOGGER.info("社保公积金调基,返回城市信息:"+ JSON.toJSONString(city));

//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());//当前缴纳月份
        Date month=customerShebaoBean.getCurrentMonth();
        String monthStr=DateUtil.dayFormatter.format(month);

        //根据新调的基数重新计算社保和公积金
        Map cal=resultSheBaoDto.getCal();
        LOGGER.info("社保公积金调基,返回重新计算的社保和公积金:"+ JSON.toJSONString(cal));

        //如果之前无缴纳,或才缴纳没有提交到社保通直接调基,则增员,历史的补缴订单不能删除
        if(customerShebaoBean.getSbShebaotongStatus()==null || (customerShebaoBean.getSbShebaotongStatus()!=null && customerShebaoBean.getSbShebaotongStatus().intValue()==ShebaoConstants.SBT_STATE_NO)){
            //清除地区历史需求订单
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4);
                updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO, ShebaoConstants.OrderType.ZY, monthStr);
                createOrder(dto.getCustomerId(), ShebaoConstants.OrderType.ZY, dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO, month, adjustAmount, (SocialBase) cal.keySet().toArray()[0]);
                customerShebaoBean.setSbBase(adjustAmount);
                customerShebaoBean.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_NO);
                customerShebaoBean.setIsSbKeep(ShebaoConstants.KEEP_YES);
                customerShebaoBean.setSbJoinDate(month);
                customerShebaoBean.setSbStopDate(null);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ.getCode(), 1, 2, 3, 4);
                updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ, ShebaoConstants.OrderType.ZY, monthStr);
                createOrder(dto.getCustomerId(), ShebaoConstants.OrderType.ZY, dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ, month, adjustAmount, (SocialBase) cal.keySet().toArray()[0]);
                customerShebaoBean.setGjjBase(adjustAmount);
                customerShebaoBean.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_NO);
                customerShebaoBean.setIsGjjKeep(ShebaoConstants.KEEP_YES);
                customerShebaoBean.setGjjJoinDate(month);
                customerShebaoBean.setGjjStopDate(null);
            }
            customerShebaoBean.setCurrentMonth(month);
            customerShebaoWriterMapper.updateByPrimaryKey(customerShebaoBean);
        }else if(customerShebaoBean.getSbShebaotongStatus()!=null && customerShebaoBean.getSbShebaotongStatus().intValue()==ShebaoConstants.SBT_STATE_FAIL){//如果审核失败,调基,则增员
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);
                updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO, ShebaoConstants.OrderType.ZY, monthStr);
                createOrder(dto.getCustomerId(), ShebaoConstants.OrderType.ZY, dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO, month, adjustAmount, (SocialBase) cal.keySet().toArray()[0]);
                customerShebaoBean.setSbBase(adjustAmount);
                customerShebaoBean.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_NO);
                customerShebaoBean.setIsSbKeep(ShebaoConstants.KEEP_YES);
                customerShebaoBean.setSbJoinDate(month);
                customerShebaoBean.setSbStopDate(null);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ.getCode(), 1, 2, 3, 4, 5);
                updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ, ShebaoConstants.OrderType.ZY, monthStr);
                createOrder(dto.getCustomerId(), ShebaoConstants.OrderType.ZY, dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ, month, adjustAmount, (SocialBase) cal.keySet().toArray()[0]);
                customerShebaoBean.setGjjBase(adjustAmount);
                customerShebaoBean.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_NO);
                customerShebaoBean.setIsGjjKeep(ShebaoConstants.KEEP_YES);
                customerShebaoBean.setGjjJoinDate(month);
                customerShebaoBean.setGjjStopDate(null);
            }
            customerShebaoBean.setCurrentMonth(month);
            customerShebaoWriterMapper.updateByPrimaryKey(customerShebaoBean);
        }else{//正常调基
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);//清除地区历史需求订单
                addAdjustBaseOrder(dto,customerShebaoBean,adjustAmount,cal,ShebaoConstants.OrderType.TJ);//新增员工调基订单
                updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO, ShebaoConstants.OrderType.TJ, monthStr);//更新用户社保公积金汇总数据
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ.getCode(), 1, 2, 3, 4, 5);//清除地区历史需求订单
                addAdjustBaseOrder(dto,customerShebaoBean,adjustAmount,cal,ShebaoConstants.OrderType.TJ);//新增员工调基订单
                updateCustomerOrderDesc(dto.getCompanyShebaoOrderId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ, ShebaoConstants.OrderType.TJ, monthStr);//更新用户社保公积金汇总数据
            }
            //更新调基员工社保公积金基本信息
            updateAdjustBase(dto,customerShebaoBean,adjustAmount);
        }
        return companyShebaoOrderBean;
    }

    /**
     * 调基相关验证
     * @param dto
     * @param adjustAmount
     */
    private ResultSheBaoDto validateAdjustBase(TJShebaoDto dto,BigDecimal adjustAmount)throws BusinessException,Exception{
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();

        if(adjustAmount==null){
            throw new BusinessException("请选择调基基数");
        }
        String adjustAmountCheck = "^[0-9]+(.[0-9]{1,2})?$";
        if(!Pattern.matches(adjustAmountCheck, adjustAmount.toString())){
            throw new BusinessException("基数格式不正确");
        }
        if(dto==null || dto.getCustomerId()==null || dto.getRelationType()==null ||
                (dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }
        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null || customerShebaoBean.getCurrentCompanyOrderId()==null) {
            throw new BusinessException("无员工社保公积金基本信息");
        }
        //获取企业订单信息
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(customerShebaoBean.getCurrentCompanyOrderId());
        if(companyShebaoOrderBean==null){
            throw new BusinessException("无企业订单信息");
        }
        if(companyShebaoOrderBean!=null && companyShebaoOrderBean.getStatus()!=null && companyShebaoOrderBean.getStatus().intValue()>=2){
            throw new BusinessException("账单已提交或关闭,不能调基");
        }
        //获取城市信息
        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
            throw new BusinessException("无该城市缴纳月份");
        }

        //当前时间在订单提交截止日之后则不能操作
        Date orderLastTime=companyShebaoOrderBean.getOrderLastTime();
        if(orderLastTime!=null && new Date().after(orderLastTime)){
            throw new BusinessException("已经超过订单提交截止日,不能调基");
        }

//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());//当前缴纳月份
        if(customerShebaoBean.getCurrentMonth()==null){
            throw new BusinessException("无该城市缴纳月份");
        }
        Date month=customerShebaoBean.getCurrentMonth();
        String monthStr=DateUtil.dayFormatter.format(month);
        String nowMonthStr=DateUtil.dateToString(month,DateUtil.DATEMONTHFORMAT);//当前缴纳月份字符串格式
        Date validatorDate=null;
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            validatorDate=queryUpdateBaseDate(customerShebaoBean.getJoinCityCode(), ShebaoTypeEnum.SHEBAO, customerShebaoBean.getSbType());
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            validatorDate=queryUpdateBaseDate(customerShebaoBean.getJoinCityCode(), ShebaoTypeEnum.GJJ, customerShebaoBean.getGjjType());
        }
        if(validatorDate!=null){
            String currentMonthStr=DateUtil.dateToString(validatorDate,DateUtil.DATEMONTHFORMAT);//当前时间月份字符串格式
            if(!currentMonthStr.equals(nowMonthStr)){
                LOGGER.error("本月该参保地区不能进行社保基数调整,当前月:"+nowMonthStr+",验证月:"+currentMonthStr);
                throw new BusinessException("本月非社保局调基窗口月，不能调整社保基数");
            }
        }else{
            LOGGER.error("本月该参保地区不能进行社保基数调整,当前月:"+nowMonthStr+",验证月:空");
            throw new BusinessException("本月非社保局调基窗口月，不能调整社保基数");
        }
        //根据新调的基数重新计算社保和公积金
        Map cal=caculateForAdjustBase(dto,customerShebaoBean,adjustAmount);
        //调基金额不能超过基数范围
        SocialBase socialBase = (SocialBase) cal.keySet().toArray()[0];
        if(socialBase == null) {
            throw new BusinessException("无社保公积金基础数据,请联系管理员");
        }
        if(adjustAmount.doubleValue() < socialBase.getMin()) {
            LOGGER.error("调整的基数低于最低额度,adjustAmount.doubleValue():"+adjustAmount.doubleValue()+",socialBase.getMin():"+socialBase.getMin());
            throw new BusinessException("调整的基数低于最低额度");
        }
        if(adjustAmount.doubleValue() > socialBase.getMax()) {
            LOGGER.error("调整的基数大于最高额度,adjustAmount.doubleValue():"+adjustAmount.doubleValue()+",socialBase.getMax():"+socialBase.getMax());
            throw new BusinessException("调整的基数大于最高额度");
        }
        //不允许基数和之前的相同
        BigDecimal oldBigDecimal=null;
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            oldBigDecimal=customerShebaoBean.getSbBase();
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            oldBigDecimal=customerShebaoBean.getGjjBase();
        }
        if(adjustAmount.compareTo(oldBigDecimal)==0){
            throw new BusinessException("调整的基数和调整前的基数相同");
        }

        resultSheBaoDto.setCustomerShebaoBean(customerShebaoBean);
        resultSheBaoDto.setCity(city);
        resultSheBaoDto.setCompanyShebaoOrderBean(companyShebaoOrderBean);
        resultSheBaoDto.setCal(cal);
        return resultSheBaoDto;
    }
    /**
     * 根据新调的基数重新计算社保和公积金
     * @param dto
     * @param customerShebaoBean
     * @param adjustAmount
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private Map caculateForAdjustBase(TJShebaoDto dto,CustomerShebaoBean customerShebaoBean,BigDecimal adjustAmount)throws Exception{
        Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
        Map cal=null;

        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, customerShebaoBean.getSbType());
            cal = customerShebaoService.cal(adjustAmount.doubleValue(),socialBase);
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, customerShebaoBean.getGjjType());
            cal = customerShebaoService.cal(adjustAmount.doubleValue(), socialBase);
        }
        if(cal==null || cal.size()<=0){
            throw new BusinessException("调基计算社保或公积金失败");
        }
        return cal;
    }
    /**
     * 创建调基需求订单
     * @param dto
     * @param customerShebaoBean
     */
    private void addAdjustBaseOrder(TJShebaoDto dto,CustomerShebaoBean customerShebaoBean,BigDecimal adjustAmount,Map cal,int orderType)throws Exception{
        CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
        customerShebaoOrderBean.setCustomerId(dto.getCustomerId());
        customerShebaoOrderBean.setCompanyShebaoOrderId(dto.getCompanyShebaoOrderId());
        customerShebaoOrderBean.setShebaoType(dto.getType());
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            customerShebaoOrderBean.setRequireType(ShebaoTypeEnum.SHEBAO.getCode());
            customerShebaoOrderBean.setShebaoType(customerShebaoBean.getSbType());
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            customerShebaoOrderBean.setRequireType(ShebaoTypeEnum.GJJ.getCode());
            customerShebaoOrderBean.setShebaoType(customerShebaoBean.getGjjType());
        }
        customerShebaoOrderBean.setOverdueMonth(customerShebaoBean.getCurrentMonth());
        customerShebaoOrderBean.setOrderBase(adjustAmount);
        customerShebaoOrderBean.setOrderType(orderType);
        customerShebaoOrderBean.setStatus(ShebaoConstants.ORDER_STATUS_NEW);
        customerShebaoOrderBean.setPayrollStatus(ShebaoConstants.PAYROLL_STATUS_NO);
        customerShebaoOrderBean.setSbtStatus(ShebaoConstants.SBT_ORDERSTATUS_INIT);
        customerShebaoOrderBean.setCreateTime(new Date());

        SocialBase socialBase = (SocialBase) cal.keySet().toArray()[0];
        String orderDetail = JSONObject.toJSONString(cal.get(socialBase));
        customerShebaoOrderBean.setOrderDetail(orderDetail);//订单详情
        customerShebaoOrderBean.setOrgSum(new BigDecimal(socialBase.getOrgSum()));
        customerShebaoOrderBean.setEmpSum(new BigDecimal(socialBase.getEmpSum()));

        int shebaoOrderCount=customerShebaoOrderWriterMapper.insert(customerShebaoOrderBean);
        if(shebaoOrderCount<=0){
            throw new BusinessException("新增社保公积金调基订单失败");
        }
    }

    /**
     * 更新调基员工社保公积金基本信息
     * @param dto
     * @param customerShebaoBean
     */
    private void updateAdjustBase(TJShebaoDto dto,CustomerShebaoBean customerShebaoBean,BigDecimal adjustAmount){
        CustomerShebaoBean uptCustomerShebaoBean=new CustomerShebaoBean();
        uptCustomerShebaoBean.setId(customerShebaoBean.getId());
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            uptCustomerShebaoBean.setSbBase(adjustAmount);
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            uptCustomerShebaoBean.setGjjBase(adjustAmount);
        }
        int count=customerShebaoWriterMapper.updateByPrimaryKeySelective(uptCustomerShebaoBean);
        if(count<=0){
            throw new BusinessException("更新社保公积金信息失败");
        }
    }

    /**
     * 批量缴纳
     * @param dto
     * @param companyId
     * @throws BusinessException
     * @throws Exception
     */
    @Transactional
    public void batchPayment(TJShebaoDto dto,Long companyId)throws Exception{
        //批量缴纳基本数据验证
        validateBatchPaymentForBase(dto,companyId);
        List<Long> customerIdList=dto.getCustomerIdList();

        //根据新调的基数重新计算社保和公积金
        BigDecimal base=new BigDecimal(dto.getBase());//基数
        CustomerShebaoBean queryCustomerShebaoBean=new CustomerShebaoBean();
        queryCustomerShebaoBean.setJoinCityCode(dto.getCityCode());
        queryCustomerShebaoBean.setSbType(dto.getType());
        queryCustomerShebaoBean.setGjjType(dto.getType());
        Map cal=caculateForAdjustBase(dto,queryCustomerShebaoBean,base);
        SocialBase socialBase = (SocialBase) cal.keySet().toArray()[0];

        //批量缴纳基数范围验证
        validateBatchPaymentForRange(base,socialBase);

        //获取城市信息
        City city = sbtService.getCityByCode(dto.getCityCode());
        if(city == null) {
            throw new BusinessException("无该城市基本信息");
        }
        LOGGER.info("批量缴纳员工社保公积金,获取城市信息:"+JSON.toJSONString(city));

        //获取当前企业订单
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderByCity(companyId, city, dto.getCityCode());
        if(companyShebaoOrderBean == null) {
            throw new BusinessException("企业订单不存在，请重试");
        }
        LOGGER.info("批量缴纳员工社保公积金,获取当前企业订单信息:"+JSON.toJSONString(companyShebaoOrderBean));

        long startTime=new Date().getTime();

        //批量更新员工社保公积金基本信息
        updateCustomerShebaoBatch(dto,companyId,companyShebaoOrderBean,city,customerIdList);
        LOGGER.info("批量缴纳员工社保公积金,更新员工社保参数所花时间:"+(new Date().getTime()-startTime));

        //清除历史需求订单并获取拼装的用户ID字符串
        String customerIdStrs=clearHistoryOrderBatch(dto,companyShebaoOrderBean,customerIdList);
        LOGGER.info("批量缴纳员工社保公积金,清除历史需求订单所花时间:"+(new Date().getTime()-startTime));

        //创建缴纳需求订单
        createPaymentOrderBatch(dto,cal,companyShebaoOrderBean,base,customerIdList);
        LOGGER.info("批量缴纳员工社保公积金,创建缴纳需求订单所花时间:"+(new Date().getTime()-startTime));

        //批量更新用户社保公积金汇总数据
        updateDescBatch( dto, companyShebaoOrderBean,customerIdList, customerIdStrs,city);
        LOGGER.info("批量缴纳员工社保公积金,创建缴纳需求订单所花时间:"+(new Date().getTime()-startTime));
    }

    /**
     * 查询企业异常订单数
     * @param
     * @return
     */
    @Override
    public long getErrorOrderCount(Map map) {
        if(null != map.get("companyId")){
            return customerShebaoOrderDescReaderMapper.selectErrorOrderCount(map);
        }
        return 0;
    }

    /**
     * 查询订单失败列表
     * @param orderId
     * @return
     */
    @Override
    public List<CustomerShebaoOrderDto> getShebaoErrorOrder(Long orderId) {
        if(null != orderId){
            return customerShebaoOrderDescReaderMapper.selectShebaoErrorOrder(orderId);
        }
        return null;
    }

    /**
     * 批量缴纳基本数据验证
     * @param dto
     * @param companyId
     */
    private void validateBatchPaymentForBase(TJShebaoDto dto,Long companyId)throws Exception{
        if(companyId==null || dto==null || dto.getRelationType()==null ||
                (dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }
        if(!StringUtils.isNotBlank(dto.getCityCode())){
            throw new BusinessException("请输入参保地区");
        }
        if(!StringUtils.isNotBlank(dto.getType())){
            throw new BusinessException("请输入缴纳类型");
        }
        if(dto.getCustomerIdList()==null || dto.getCustomerIdList().size()<=0){
            throw new BusinessException("请选择员工");
        }
    }

    /**
     * 批量缴纳基数范围验证
     * @param base
     * @param socialBase
     */
    private void validateBatchPaymentForRange(BigDecimal base,SocialBase socialBase)throws Exception{
        if(socialBase == null) {
            throw new BusinessException("无社保公积金基础数据,请联系管理员");
        }
        if(base.doubleValue() < socialBase.getMin()) {
            throw new BusinessException("基数低于最低额度");
        }
        if(base.doubleValue() > socialBase.getMax()) {
            throw new BusinessException("基数大于最高额度");
        }
    }

    /**
     * 批量更新员工社保公积金基本信息
     * @param dto
     * @param companyId
     * @param companyShebaoOrderBean
     * @param city
     * @param customerIdList
     * @throws BusinessException
     * @throws Exception
     */
    private void updateCustomerShebaoBatch(TJShebaoDto dto,Long companyId,CompanyShebaoOrderBean companyShebaoOrderBean,City city,
                                           List<Long> customerIdList)throws Exception{
        List<CustomerShebaoBean> batchAddList=new ArrayList<>();
        List<CustomerShebaoBean> batchEditList=new ArrayList<>();
        Date nowDate=new Date();
        BigDecimal base=new BigDecimal(dto.getBase());
        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        List<CustomerShebaoBean> customerShebaoBeanList=customerShebaoReaderMapper.selectBatchForCustomerId(customerIdList);
        //如果已经存在员工社保公积金基本信息,则更改,如果不存在,则新增
        for(Long customerId:customerIdList){
            boolean isHaveShebaoData=false;//根据员工判断是否有社保公积金基本信息
            for(CustomerShebaoBean checkBean:customerShebaoBeanList){
                if(checkBean.getCustomerId()!=null && checkBean.getCustomerId().longValue()==customerId.longValue()){
                    isHaveShebaoData=true;
                    break;
                }
            }
            CustomerShebaoBean addCustomerShebaoBean = new CustomerShebaoBean();
            addCustomerShebaoBean.setCustomerId(dto.getCustomerId());
            addCustomerShebaoBean.setCompanyId(companyId);
            addCustomerShebaoBean.setCurrentCompanyOrderId(companyShebaoOrderBean.getId());
            addCustomerShebaoBean.setJoinCityName(city.getCname());
            addCustomerShebaoBean.setJoinCityCode(dto.getCityCode());
            addCustomerShebaoBean.setCurrentMonth(month);
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){//社保
                addCustomerShebaoBean.setSbType(dto.getType());
                addCustomerShebaoBean.setSbBase(base);
                addCustomerShebaoBean.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_NO);
                addCustomerShebaoBean.setIsSbKeep(ShebaoConstants.KEEP_YES);
                addCustomerShebaoBean.setSbJoinDate(month);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){//公积金
                addCustomerShebaoBean.setGjjType(dto.getType());
                addCustomerShebaoBean.setGjjBase(base);
                addCustomerShebaoBean.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_NO);
                addCustomerShebaoBean.setIsGjjKeep(ShebaoConstants.KEEP_YES);
                addCustomerShebaoBean.setGjjJoinDate(month);
            }
            if(isHaveShebaoData){//无社保公积金基本信息
                batchEditList.add(addCustomerShebaoBean);
            }else{
                addCustomerShebaoBean.setCreateTime(nowDate);
                batchAddList.add(addCustomerShebaoBean);
            }
        }
        if(batchAddList!=null && batchAddList.size()>0){//添加新增的信息
            customerShebaoWriterMapper.insertBatch(batchAddList);
        }
        if(batchEditList!=null && batchEditList.size()>0){//添加修改的信息
            customerShebaoWriterMapper.updateBatch(batchEditList);
        }
    }

    /**
     * 清除历史需求订单
     * @param dto
     * @param companyShebaoOrderBean
     * @param customerIdList
     */
    private String clearHistoryOrderBatch(TJShebaoDto dto,CompanyShebaoOrderBean companyShebaoOrderBean,List<Long> customerIdList){
        StringBuffer customerIdBuffer=new StringBuffer();
        for(int i=0,len=customerIdList.size();i<len;i++){
            Long customerId=customerIdList.get(i);
            if(i==len-1){
                customerIdBuffer.append(String.valueOf(customerId));
            }else{
                customerIdBuffer.append(String.valueOf(customerId)).append(",");
            }
        }
        //将customerID拼装成String
        String customerIdStrs=customerIdBuffer.toString();
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){//社保
            customerShebaoOrderWriterMapper.deleteBatch(customerIdStrs, companyShebaoOrderBean.getId(), ShebaoTypeEnum.SHEBAO.getCode(),"1, 2, 3, 4, 5");
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){//公积金
            customerShebaoOrderWriterMapper.deleteBatch(customerIdStrs, companyShebaoOrderBean.getId(), ShebaoTypeEnum.GJJ.getCode(),"1, 2, 3, 4, 5");
        }
        return customerIdStrs;
    }

    /**
     * 创建缴纳需求订单
     * @param dto
     * @param cal
     * @param companyShebaoOrderBean
     * @param base
     * @param customerIdList
     */
    private void createPaymentOrderBatch(TJShebaoDto dto,Map cal,CompanyShebaoOrderBean companyShebaoOrderBean,BigDecimal base,
                                         List<Long> customerIdList){
        SocialBase socialBase = (SocialBase) cal.keySet().toArray()[0];
        String orderDetail = JSONObject.toJSONString(cal.get(socialBase));
        BigDecimal orgSum=new BigDecimal(socialBase.getOrgSum());
        BigDecimal empSum=new BigDecimal(socialBase.getOrgSum());
        List<CustomerShebaoOrderBean> customerShebaoOrderBeanList=new ArrayList<>();
        Date nowDate=new Date();
        for(Long customerId:customerIdList){
            CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
            customerShebaoOrderBean.setCustomerId(customerId);
            customerShebaoOrderBean.setOrderType(ShebaoConstants.OrderType.ZY);
            customerShebaoOrderBean.setCompanyShebaoOrderId(companyShebaoOrderBean.getId());
            customerShebaoOrderBean.setShebaoType(dto.getType());
            customerShebaoOrderBean.setOrderBase(base);
            customerShebaoOrderBean.setOrderDetail(orderDetail);
            customerShebaoOrderBean.setOrgSum(orgSum);
            customerShebaoOrderBean.setEmpSum(empSum);
            customerShebaoOrderBean.setStatus(ShebaoConstants.ORDER_STATUS_NEW);
            customerShebaoOrderBean.setPayrollStatus(ShebaoConstants.PAYROLL_STATUS_NO);
            customerShebaoOrderBean.setSbtStatus(ShebaoConstants.SBT_ORDERSTATUS_INIT);
            customerShebaoOrderBean.setCreateTime(nowDate);
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){//社保
                customerShebaoOrderBean.setRequireType(ShebaoConstants.RELATION_TYPE_SHEBAO);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){//公积金
                customerShebaoOrderBean.setRequireType(ShebaoConstants.RELATION_TYPE_GONGJIJING);
            }
            customerShebaoOrderBeanList.add(customerShebaoOrderBean);
        }
        if(customerShebaoOrderBeanList!=null && customerShebaoOrderBeanList.size()>0){
            customerShebaoOrderWriterMapper.insertBatch(customerShebaoOrderBeanList);
        }
    }

    /**
     * 批量更新用户社保公积金汇总数据
     * @param dto
     * @param companyShebaoOrderBean
     * @param customerIdList
     * @param customerIdStrs
     * @param city
     */
    private void updateDescBatch(TJShebaoDto dto,CompanyShebaoOrderBean companyShebaoOrderBean,List<Long> customerIdList,String customerIdStrs,City city){
        List<CustomerShebaoOrderDescBean> descList = customerShebaoOrderDescWriterMapper.selectBatchByCustomerAndCompanyOrder(companyShebaoOrderBean.getId(), customerIdStrs);
        List<CustomerShebaoOrderDescBean> addDescList=new ArrayList<>();
        List<CustomerShebaoOrderDescBean> editDescList=new ArrayList<>();
        for(Long customerId:customerIdList){
            boolean isHaveDesc=false;//是否存在该员工社保公积金汇总信息
            for(CustomerShebaoOrderDescBean descBean:descList){
                if(descBean.getCustomerId()!=null && customerId.longValue()==descBean.getCustomerId().longValue()){
                    isHaveDesc=true;
                    break;
                }
            }
            CustomerShebaoOrderDescBean desc = new CustomerShebaoOrderDescBean();
            desc.setCustomerId(customerId);
            desc.setCompanyShebaoOrderId(companyShebaoOrderBean.getId());
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){//社保
                desc.setSbBjText(null);
                desc.setSbHjText(city.getMonth());
                desc.setSbStopText(null);
                desc.setSbTjText(null);
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){//公积金
                desc.setGjjBjText(null);
                desc.setGjjHjText(city.getMonth());
                desc.setGjjStopText(null);
                desc.setGjjTjText(null);
            }
            if(isHaveDesc){
                editDescList.add(desc);
            }else{
                addDescList.add(desc);
            }
        }
        if(addDescList!=null && addDescList.size()>0){
            customerShebaoOrderDescWriterMapper.insertBatch(addDescList);
        }
        if(editDescList!=null && editDescList.size()>0){
            customerShebaoOrderDescWriterMapper.updateBatch(editDescList);
        }
    }

//    /**
//     * 社保公积金停缴验证
//     * @param dto
//     * @throws BusinessException
//     * @throws Exception
//     */
//    public void paymentValidator(TJShebaoDto dto)throws Exception{
//        if(dto==null || dto.getRelationType()==null || dto.getCustomerId()==null
//                ||(dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
//            throw new BusinessException("无员工信息");
//        }
//        //获取社保公积金基本信息
//        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
//        if(customerShebaoBean == null) {
//            throw new BusinessException("无员工社保公积金基本信息");
//        }
//        LOGGER.info("社保公积金停缴验证,返回员工社保公积金基本信息:"+ JSON.toJSONString(customerShebaoBean));
//
//        //获取城市信息
//        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
//        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
//            throw new BusinessException("无该城市缴纳月份");
//        }
//        LOGGER.info("社保公积金停缴验证,返回城市信息:"+ JSON.toJSONString(city));
//
//        //详细验证
//        paymentValidatorDetail(dto,customerShebaoBean,city);
//    }

//    /**
//     * 停缴详细验证
//     * @param dto
//     * @param customerShebaoBean
//     * @param city
//     * @throws BusinessException
//     * @throws Exception
//     */
//    private void paymentValidatorDetail(TJShebaoDto dto,CustomerShebaoBean customerShebaoBean,City city)throws Exception{
//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());//当前缴纳月份
//        Date stopPaymentDate=null;//将要停缴的月份
//        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
//            if(customerShebaoBean.getSbStopDate()!=null){
//                stopPaymentDate=customerShebaoBean.getSbStopDate();
//            }
//        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//            if(customerShebaoBean.getSbStopDate()!=null){
//                stopPaymentDate=customerShebaoBean.getGjjStopDate();
//            }
//        }
//        if(stopPaymentDate==null//停缴日期为空,缴纳了才能停缴,没缴纳不能停缴
//                ||(stopPaymentDate!=null && stopPaymentDate.before(month))){//如果停缴日期<当前缴纳月份,缴纳了才能停缴,没缴纳不能停缴
//            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
//                if(customerShebaoBean.getIsSbKeep()==null ){
//                    throw new BusinessException("该员工没有缴纳社保,不能停缴");
//                }
//                if(customerShebaoBean.getIsSbKeep()!=null && customerShebaoBean.getIsSbKeep().intValue()==ShebaoConstants.KEEP_NO){
//                    throw new BusinessException("该员工没有缴纳社保,不能停缴");
//                }
//            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                if(customerShebaoBean.getIsGjjKeep()==null ){
//                    throw new BusinessException("该员工没有缴纳公积金,不能停缴");
//                }
//                if(customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep().intValue()==ShebaoConstants.KEEP_NO){
//                    throw new BusinessException("该员工没有缴纳公积金,不能停缴");
//                }
//            }
//        }else {//停缴日期不为空
//            if (stopPaymentDate.after(month)) {//如果停缴月份>当前缴纳月份,则提示"该员工已设置社保停缴"
//                throw new BusinessException("sureMsg");
//            }
//            //如果当前缴纳月份==停缴月份，则提示"当前月为该员工离职月,不能修改或提交需求"
//            String nowMonthStr = DateUtil.dateToString(month, DateUtil.DATEMONTHFORMAT);//当前缴纳月份字符串格式
//            String stopDateStr = DateUtil.dateToString(stopPaymentDate, DateUtil.DATEMONTHFORMAT);//要停缴的月份字符串格式
//            if (nowMonthStr.equals(stopDateStr)) {
//                throw new BusinessException("当前月为该员工离职月,不能修改或提交需求");
//            }
//        }
//    }

    /**
     *公积金取消停缴验证
     * @param dto
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    public boolean cancelGjjValidator(TJShebaoDto dto)throws Exception{
        if(dto==null || dto.getCustomerId()==null || dto.getRelationType()==null
                ||(dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }
        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null) {
            throw new BusinessException("无员工社保公积金基本信息");
        }
        LOGGER.info("社保公积金停缴,返回员工社保公积金基本信息:"+ JSON.toJSONString(customerShebaoBean));

        //获取城市信息
        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
            throw new BusinessException("无该城市缴纳月份");
        }
        LOGGER.info("社保公积金停缴,返回城市信息:"+ JSON.toJSONString(city));
        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());//当前缴纳月份
        //点击“取消”或者右上角的叉时，判断是否有停缴社保月份>=“当前账单月份”的停缴社保需求，如果有则提示必须进行停缴设置，不允许退出。
        return !(dto.getRelationType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING && !(month.after(customerShebaoBean.getSbStopDate())));
    }

    /**
     * 社保公积金调基验证
     * @param dto
     * @throws BusinessException
     * @throws Exception
     */
    public TJShebaoDto adjustBaseValidator(TJShebaoDto dto)throws Exception{
        if(dto==null || dto.getCustomerId()==null || dto.getRelationType()==null ||
                (dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }
        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null) {
            throw new BusinessException("无员工社保公积金基本信息");
        }
        LOGGER.info("社保公积金调基验证,返回员工社保公积金基本信息:"+ JSON.toJSONString(customerShebaoBean));

        //获取城市信息
        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
            throw new BusinessException("无该城市缴纳月份");
        }
        LOGGER.info("社保公积金调基验证,返回城市信息:"+ JSON.toJSONString(city));

//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());//当前缴纳月份
        if(customerShebaoBean.getCurrentMonth()==null){
            throw new BusinessException("无该城市缴纳月份");
        }
        Date month=customerShebaoBean.getCurrentMonth();
        String monthStr=DateUtil.dayFormatter.format(month);
        String nowMonthStr=DateUtil.dateToString(month,DateUtil.DATEMONTHFORMAT);//当前缴纳月份字符串格式
        Date validatorDate=null;
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            validatorDate=queryUpdateBaseDate(customerShebaoBean.getJoinCityCode(), ShebaoTypeEnum.SHEBAO, customerShebaoBean.getSbType());
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            validatorDate=queryUpdateBaseDate(customerShebaoBean.getJoinCityCode(), ShebaoTypeEnum.GJJ, customerShebaoBean.getGjjType());
        }
        if(validatorDate!=null){
            String currentMonthStr=DateUtil.dateToString(validatorDate,DateUtil.DATEMONTHFORMAT);//当前时间月份字符串格式
            if(!currentMonthStr.equals(nowMonthStr)){
                LOGGER.error("本月该参保地区不能进行社保基数调整,当前月:"+nowMonthStr+",验证月:"+currentMonthStr);
                throw new BusinessException("本月非社保局调基窗口月，不能调整社保基数");
            }
        }else{
            LOGGER.error("本月该参保地区不能进行社保基数调整,当前月:"+nowMonthStr+",验证月:空");
            throw new BusinessException("本月非社保局调基窗口月，不能调整社保基数");
        }

        //获取基数
        BigDecimal oldBigDecimal=null;
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            oldBigDecimal=customerShebaoBean.getSbBase();
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            oldBigDecimal=customerShebaoBean.getGjjBase();
        }
        if(oldBigDecimal==null){
            throw new BusinessException("无缴纳基数");
        }
        Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
        SocialBase socialBase=null;
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, customerShebaoBean.getSbType());
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, customerShebaoBean.getGjjType());
        }
        if(socialBase==null){
            throw new BusinessException("无社保公积金基础数据,请联系管理员");
        }
        TJShebaoDto resultDto=new TJShebaoDto();
        resultDto.setOldBase(oldBigDecimal);
        resultDto.setMinBase(new BigDecimal(socialBase.getMin()));
        resultDto.setMaxBase(new BigDecimal(socialBase.getMax()));
//        resultDto.setMonth(city.getMonth());
        resultDto.setMonth(monthStr);
        return resultDto;
    }

    /**
     * 获取参保地区的调基月
     * @param joinCity
     * @param shebaoTypeEnum
     * @param type
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private Date queryUpdateBaseDate(String joinCity, ShebaoTypeEnum shebaoTypeEnum, String type) throws BusinessException,Exception{
        Basic basic = sbtService.getBasic(joinCity);
        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(shebaoTypeEnum, basic, type);
        if(socialBase.getBchgmonth()!=null){
            return DateUtil.dayFormatterLine.parse(socialBase.getBchgmonth());
        }else{
            return null;
        }
    }
    /**
     * 验证当前地区是否可以提交员工订单
     * @param companyShebaoOrderBean
     * @return
     */
    public boolean validateOrderDate(CompanyShebaoOrderBean companyShebaoOrderBean){
        try {
//            long time = DateUtil.sbtSimpleDateFormat.parse(DateUtil.sbtSimpleDateFormat.format(new Date())).getTime();
            long time =  new Date().getTime();

            if(time > companyShebaoOrderBean.getOrderLastTime().getTime()) {
                LOGGER.error("new Date()="+new Date()+",getOrderLastTime="+companyShebaoOrderBean.getOrderLastTime());
                throw new BusinessException("已过订单截止日");
            }
            if(companyShebaoOrderBean.getStatus() > 1) {
                throw new BusinessException("企业订单已提交");
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("订单日期异常");
        }
        return true;
    }

    /**
     * 更新企业订单详情
     * @param companyShebaoOrderBean
     * @return
     */
    public boolean isNeedUpdateCompanyOrderDetail(CompanyShebaoOrderBean companyShebaoOrderBean) {
        try {
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
            long time = s.parse(s.format(new Date())).getTime();
            if(time >= companyShebaoOrderBean.getRequireLastTime().getTime() && time <= companyShebaoOrderBean.getOrderLastTime().getTime() && companyShebaoOrderBean.getStatus() == 1) {
                companyShebaoService.updateOrderDetail(companyShebaoOrderBean.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("", "订单日期异常", e);
        }
        return true;
    }

    /**
     * 批量更新员工订单的社保通状态
     * @param sbtStatus
     * @param companyShebaoOrderId
     * @param list
     * @return
     */
    public int updateSbtStateBatch(int sbtStatus, long companyShebaoOrderId,List<Long> list){
        return customerShebaoOrderWriterMapper.updateSbtStateBatch(sbtStatus,companyShebaoOrderId,list);
    }

    @Cacheable(value = "redisManager",key = "#payCycleId+'ShebaoBase'")
    @Override
    public Map<Long, CustomerShebaoSumDto> getCustomerShebaoBase(Long payCycleId) {
        Map<Long, CustomerShebaoSumDto> result = new HashMap<>();
        PayCycleBean payCycleBean = payCycleService.selectByPrimaryKey(payCycleId);
        if (payCycleBean != null) {
            //更新所有未扣除工资的社保订单
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.valueOf(payCycleBean.getYear()), Integer.valueOf(payCycleBean.getMonth()) - 1, 1);
            Date month = calendar.getTime();
            customerShebaoOrderWriterMapper.updateNoPayrollOrder(payCycleBean.getCompanyId(), payCycleBean.getId(), month);

            int count = companyProtocolsReaderMapper.selectUsefulProtocolCount(payCycleBean.getCompanyId(), 3);

            List<Map> rows = customerShebaoOrderReaderMapper.selectEmpSumByCycleId(payCycleBean.getId(), month, count);
            if (rows != null) {
                for (Map row : rows) {
                    Long customerId = Long.valueOf(row.get("cid") + "");
                    Integer requireType = Integer.valueOf(row.get("requireType") + "");
                    BigDecimal empSum=new BigDecimal("0");
                    if(row.get("sbtEmpSum")!=null){
                        empSum = new BigDecimal(row.get("sbtEmpSum") + "");
                    }else{
                        empSum = new BigDecimal(row.get("empSum") + "");
                    }
                    BigDecimal orgSum=new BigDecimal("0");
                    if(row.get("sbtOrgSum")!=null){
                        orgSum = new BigDecimal(row.get("sbtOrgSum") + "");
                    }else{
                        if(row.get("orgSum")!=null){
                            orgSum = new BigDecimal(row.get("orgSum") + "");
                        }
                    }
                    CustomerShebaoSumDto customerShebaoSumDto = result.get(customerId);
                    if (customerShebaoSumDto == null) {
                        customerShebaoSumDto = new CustomerShebaoSumDto();
                        customerShebaoSumDto.setCustomerId(customerId);
                        result.put(customerId, customerShebaoSumDto);
                    }
                    if (requireType == 1) {
                        customerShebaoSumDto.setSbSum(empSum);
                        customerShebaoSumDto.setTotalOrgSum(customerShebaoSumDto.getTotalOrgSum().add(orgSum));
                        customerShebaoSumDto.setSbOrgSum(orgSum);

                    } else {
                        customerShebaoSumDto.setGjjSum(empSum);
                        customerShebaoSumDto.setTotalOrgSum(customerShebaoSumDto.getTotalOrgSum().add(orgSum));
                        customerShebaoSumDto.setGjjOrgSum(orgSum);
                    }

                }
            }
        }
        return result;
    }

    @Override
    public boolean updateShebaotongStatus(String idCard, String orderNumber, ShebaoTypeEnum shebaoTypeEnum, int status, Date orderMonth, String comment) throws ParseException {
        CustomersBean customersBean = customersReaderMapper.selectByIdCard(idCard);

        if(customersBean == null){
            LOGGER.info("回调员工不存在");
            return false;
        }

        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoOrderReaderMapper.selectByOrderNumber(orderNumber);

        if(customersBean == null){
            LOGGER.info("回调企业订单不存在");
            return false;
        }

        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(customersBean.getCustomerId());

        if(customersBean == null){
            LOGGER.info("回调员工社保信息不存在");
            return false;
        }

        CustomerShebaoOrderBean shebaoOrderBean = customerShebaoOrderReaderMapper.selectByNotifiy(companyShebaoOrderBean.getId(), customersBean.getCustomerId(), orderMonth, shebaoTypeEnum.getCode());

        if(shebaoOrderBean == null) {
            LOGGER.info("回调员工需求订单不存在");
            return false;
        }
        CustomerShebaoOrderBean updateBean = new CustomerShebaoOrderBean();
        updateBean.setId(shebaoOrderBean.getId());
        updateBean.setResponseMsg(comment);
        if(status == 1) {
            updateBean.setSbtStatus(1);
        }else{//订单失败，创建当前月的增员订单
            updateBean.setSbtStatus(2);
            City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
            CompanyShebaoOrderBean currentCompanyOrder = companyShebaoOrderReaderMapper.selectOrderByCityAndMonth(customerShebaoBean.getCompanyId(), customerShebaoBean.getJoinCityCode(), city.getMonth());
            if(currentCompanyOrder != null && currentCompanyOrder.getOrderDate().after(companyShebaoOrderBean.getOrderDate())) {//当前企业订单不为空，并在回调企业订单之后
                //删除当前该员工的所有需求订单，并创建新的增员订单
                BigDecimal base = null;
                String type = null;
                boolean isKeep = false;
                if(shebaoTypeEnum == ShebaoTypeEnum.GJJ) {
                    base = customerShebaoBean.getGjjBase();
                    type = customerShebaoBean.getGjjType();
                    isKeep = customerShebaoBean.getIsGjjKeep() == 1;
                }else{
                    base = customerShebaoBean.getSbBase();
                    type = customerShebaoBean.getSbType();
                    isKeep = customerShebaoBean.getIsSbKeep() == 1;
                }
                if(isKeep) {
                    Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
                    SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(shebaoTypeEnum, basic, type);

                    int deletedOrderCount = cleanOrderByCustomerIdAndCompanyOrderId(customerShebaoBean.getCustomerId(), currentCompanyOrder.getId(), shebaoTypeEnum.getCode(), 1, 2, 3, 4, 5);
                    LOGGER.info("删除员工已有社保需求订单数量", deletedOrderCount);
                    createOrder(customerShebaoBean.getCustomerId(), 1, currentCompanyOrder.getId(), shebaoTypeEnum, DateUtil.sbtSimpleDateFormat.parse(city.getMonth()), base, socialBase);
                }
            }

            //更新员工状态为缴纳异常
            customerShebaoWriterMapper.updateSbtStatus(customersBean.getCustomerId(), shebaoTypeEnum.getCode(), 4);
        }
        //如果订单为停缴则更新员工状态为停缴
        if(shebaoOrderBean.getOrderType() == 4) {
            if(status == 1) { //更新员工社保通状态为停缴
                customerShebaoWriterMapper.updateSbtStatus(customersBean.getCustomerId(), shebaoTypeEnum.getCode(), 3);
            }
        }else if(shebaoOrderBean.getOrderType() == 1 || shebaoOrderBean.getOrderType() == 2) {
            if(status == 0) { //更新员工社保通状态为停缴
                customerShebaoWriterMapper.updateSbtStatus(customersBean.getCustomerId(), shebaoTypeEnum.getCode(), 3);
            }
        }
        int count = customerShebaoOrderWriterMapper.updateByPrimaryKeySelective(updateBean);
        return count > 0;
    }

    /**
     *判断是否有异常
     * @param orderId
     * @return
     */
    public List<CustomerShebaoOrderDto> selectShebaoErrorOrderForNew( Long orderId){
        return customerShebaoOrderDescReaderMapper.selectShebaoErrorOrderForNew(orderId);
    }

    public static List<String> joinDate(List<Date> dates){
        Date last = null;
        Date change = null;
        List<String> result = new ArrayList<>();
        for (Iterator<Date> iter = dates.iterator(); iter.hasNext();) {
            Date date = iter.next();
            if(last == null){
                last = date;
                change = date;
            }
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(last);
            c2.setTime(date);
            if(c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) != 1 && !change.equals(date)) {
                if(last.equals(change)) {
                    result.add(DateUtil.sbtSimpleDateFormat.format(change));
                }else{
                    result.add(DateUtil.sbtSimpleDateFormat.format(change) + "-" + DateUtil.sbtSimpleDateFormat.format(last));
                }
                change = date;
            }
            if(!iter.hasNext()) {
                if(!change.equals(date)) {
                    result.add(DateUtil.sbtSimpleDateFormat.format(change) + "-" + DateUtil.sbtSimpleDateFormat.format(date));
                }else{
                    result.add(DateUtil.sbtSimpleDateFormat.format(change));
                }

            }
            last = date;
        }
        return result;
    }

    /**
     * 付款成功后更新社保通订单的状态
     * @return
     */
    public int updateSbtStateForPayment(CustomerShebaoOrderBean customerShebaoOrderBean){
        return customerShebaoOrderWriterMapper.updateSbtStateForPayment(customerShebaoOrderBean);
    }

    @Override
    public Map selectExistBjData(long customerId, Long currentCompanyOrderId, ShebaoTypeEnum shebaoTypeEnum) {
        Map map = customerShebaoOrderReaderMapper.selectBjStartEndDate(customerId, currentCompanyOrderId, shebaoTypeEnum.getCode());
        if(map == null) {
            return null;
        }

        List<BigDecimal> bases = customerShebaoOrderReaderMapper.selectBjBase(customerId, currentCompanyOrderId, shebaoTypeEnum.getCode());
        map.put("bases", bases);

        return map;
    }

    /**
     * 查询该公司错误订单下的所有员工id
     * @param companyShebaoOrderId
     * @return
     */
    @Override
    public List<Map<String,Object>> selectErrorCustomerIdList(Long companyShebaoOrderId) {
        return customerShebaoOrderReaderMapper.selectErrorCustomerIdList(companyShebaoOrderId);
    }


    @Override
    public List<Date> selectBjDate(long customerId, Long currentCompanyOrderId, ShebaoTypeEnum shebaoTypeEnum) {
        return customerShebaoOrderReaderMapper.selectBjDate(customerId, currentCompanyOrderId, shebaoTypeEnum.getCode());
    }
    /**
     * 社保公积金停缴的基础验证
     * @param dto
     */
    public ResultSheBaoDto validateStopPaymentBase(TJShebaoDto dto)throws BusinessException,Exception{
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();
        if(dto==null || dto.getCustomerId()==null || dto.getRelationType()==null
                ||(dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }
        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null || customerShebaoBean.getCurrentCompanyOrderId()==null) {
            throw new BusinessException("无员工社保公积金基本信息");
        }
        //获取企业订单信息
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(customerShebaoBean.getCurrentCompanyOrderId());
        if(companyShebaoOrderBean==null){
            throw new BusinessException("无企业订单信息");
        }

        //获取城市信息
        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
            throw new BusinessException("无该城市缴纳月份");
        }
        //当前缴纳月份
//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        if(customerShebaoBean.getCurrentMonth()==null){
            throw new BusinessException("无该城市缴纳月份");
        }
        Date month=customerShebaoBean.getCurrentMonth();

        //当前时间在订单提交截止日之后则不能操作
        Date orderLastTime=companyShebaoOrderBean.getOrderLastTime();
        if(orderLastTime!=null && new Date().after(orderLastTime)){
            throw new BusinessException("已经超过订单提交截止日,不能停缴");
        }

        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            //公积金缴纳了,但没有停缴,则社保不能停缴,必须公积金先停缴,社保才能停缴;如果公积金没有缴纳,社保则直接可以停缴
            if(customerShebaoBean.getGjjStopDate()==null && customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep()==ShebaoConstants.KEEP_YES){
                throw new BusinessException("公积金处于缴纳状态，不能停缴社保");
            }
        }

        Date stopPaymentDate=null;//将要停缴的月份
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            if(customerShebaoBean.getSbStopDate()!=null){
                stopPaymentDate=customerShebaoBean.getSbStopDate();
            }
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            if(customerShebaoBean.getGjjStopDate()!=null){
                stopPaymentDate=customerShebaoBean.getGjjStopDate();
            }
        }
        if(stopPaymentDate==null//停缴日期为空,缴纳了才能停缴,没缴纳不能停缴
                ||(stopPaymentDate!=null && stopPaymentDate.before(month))){//如果停缴日期<当前缴纳月份,缴纳了才能停缴,没缴纳不能停缴
            if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
                if(customerShebaoBean.getIsSbKeep()==null ){
                    throw new BusinessException("该员工没有缴纳社保,不能停缴");
                }
                if(customerShebaoBean.getIsSbKeep()!=null && customerShebaoBean.getIsSbKeep().intValue()==ShebaoConstants.KEEP_NO){
                    throw new BusinessException("该员工没有缴纳社保,不能停缴");
                }
            }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                if(customerShebaoBean.getIsGjjKeep()==null ){
                    throw new BusinessException("该员工没有缴纳公积金,不能停缴");
                }
                if(customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep().intValue()==ShebaoConstants.KEEP_NO){
                    throw new BusinessException("该员工没有缴纳公积金,不能停缴");
                }
            }
        }else {//停缴日期不为空
            //如果当前缴纳月份==停缴月份，则提示"当前月为该员工离职月,不能修改或提交需求"
            String nowMonthStr = DateUtil.dateToString(month, DateUtil.DATEMONTHFORMAT);//当前缴纳月份字符串格式
            String stopDateStr = DateUtil.dateToString(stopPaymentDate, DateUtil.DATEMONTHFORMAT);//要停缴的月份字符串格式
//            if (nowMonthStr.equals(stopDateStr)) {
//                throw new BusinessException("当前月为该员工离职月,不能修改或提交需求");
//            }
            if (stopPaymentDate.after(month)) {//如果停缴月份>当前缴纳月份,则提示"该员工已设置社保停缴"
                resultSheBaoDto.setAlreadyStopInfo("sureMsg");
                resultSheBaoDto.setAlreadyStopDateStr(stopDateStr);
            }
        }
        if(stopPaymentDate!=null && !StringUtils.isNotBlank(resultSheBaoDto.getAlreadyStopDateStr())){
            resultSheBaoDto.setAlreadyStopDateStr(DateUtil.dateToString(stopPaymentDate, DateUtil.DATEMONTHFORLINE));
        }
        resultSheBaoDto.setCustomerShebaoBean(customerShebaoBean);
        resultSheBaoDto.setCompanyShebaoOrderBean(companyShebaoOrderBean);
        resultSheBaoDto.setCity(city);
        return resultSheBaoDto;
    }

    public BigDecimal resetBase(SocialBase socialBase, CustomerShebaoBean customerShebaoBean, ShebaoTypeEnum shebaoTypeEnum){
        boolean isChg = false;
        BigDecimal base = null;
        BigDecimal sbtBase = null;
        BigDecimal max = new BigDecimal(socialBase.getMax());
        BigDecimal min = new BigDecimal(socialBase.getMin());

        if(shebaoTypeEnum == ShebaoTypeEnum.GJJ) {
            base = customerShebaoBean.getGjjBase();
            sbtBase = customerShebaoBean.getGjjSbtBase();
        }else if(shebaoTypeEnum == ShebaoTypeEnum.SHEBAO){
            base = customerShebaoBean.getSbBase();
            sbtBase = customerShebaoBean.getSbSbtBase();
        }else{
            return null;
        }

        if(base.compareTo(max) > 0) {
            isChg = true;
            base = max;
        }else if(base.compareTo(min) < 0) {
            isChg = true;
            base = min;
        }else if(sbtBase != null && !sbtBase.equals(base)) {
            isChg = true;
        }

        if(isChg) {
            CustomerShebaoBean updateBean = new CustomerShebaoBean();
            updateBean.setId(customerShebaoBean.getId());
            if(shebaoTypeEnum == ShebaoTypeEnum.GJJ) {
                updateBean.setGjjBase(base);
            }else if(shebaoTypeEnum == ShebaoTypeEnum.SHEBAO){
                updateBean.setSbBase(base);
            }
            customerShebaoWriterMapper.updateByPrimaryKeySelective(updateBean);
            return base;
        }
        return null;
    }


//    public BigDecimal resetBase(SocialBase socialBase, CustomerShebaoBean customerShebaoBean, ShebaoTypeEnum shebaoTypeEnum){
//        if(shebaoTypeEnum == ShebaoTypeEnum.GJJ) {
//            boolean isChg = false;
//            double gjjBase = customerShebaoBean.getGjjBase().doubleValue();
//            if(gjjBase > socialBase.getMax()) {
//                isChg = true;
//                customerShebaoBean.setGjjBase(new BigDecimal(socialBase.getMax()));
//            }else if(gjjBase < socialBase.getMin()) {
//                isChg = true;
//                customerShebaoBean.setGjjBase(new BigDecimal(socialBase.getMin()));
//            }
//            if(isChg){
//                customerShebaoWriterMapper.updateByPrimaryKey(customerShebaoBean);
//                return customerShebaoBean.getGjjBase();
//            }
//        }else if(shebaoTypeEnum == ShebaoTypeEnum.SHEBAO){
//            boolean isChg = false;
//            double sbBase = customerShebaoBean.getSbBase().doubleValue();
//            if(sbBase > socialBase.getMax()) {
//                isChg = true;
//                customerShebaoBean.setSbBase(new BigDecimal(socialBase.getMax()));
//            }else if(sbBase < socialBase.getMin()) {
//                isChg = true;
//                customerShebaoBean.setSbBase(new BigDecimal(socialBase.getMin()));
//            }
//            if(isChg){
//                customerShebaoWriterMapper.updateByPrimaryKey(customerShebaoBean);
//            }
//            return customerShebaoBean.getSbBase();
//        }
//        return null;
//    }

    /**
     * 社保公积金取消停缴
     * @param dto
     * @return
     * @throws BusinessException
     */
    @Transactional
    public CompanyShebaoOrderBean cancelStopPayment(TJShebaoDto dto) throws BusinessException,Exception {
        if(dto==null || dto.getCustomerId()==null || dto.getRelationType()==null
                ||(dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_SHEBAO && dto.getRelationType().intValue()!=ShebaoConstants.RELATION_TYPE_GONGJIJING)){
            throw new BusinessException("无员工和企业订单信息");
        }

        //获取社保公积金基本信息
        CustomerShebaoBean customerShebaoBean = customerShebaoReaderMapper.selectByCustomerId(dto.getCustomerId());
        if(customerShebaoBean == null || customerShebaoBean.getCurrentCompanyOrderId()==null) {
            throw new BusinessException("无员工社保公积金基本信息");
        }
        Date stopDate=null;
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            stopDate=customerShebaoBean.getSbStopDate();
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            stopDate=customerShebaoBean.getGjjStopDate();
        }
        if(stopDate==null){
            throw new BusinessException("cancel");
        }
        //获取企业订单信息
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.getCompanyShebaoOrderById(customerShebaoBean.getCurrentCompanyOrderId());
        if(companyShebaoOrderBean==null){
            throw new BusinessException("无企业订单信息");
        }

        //当前账单已提交则不能取消停缴
        if(companyShebaoOrderBean.getStatus()!=null && companyShebaoOrderBean.getStatus().intValue()>1){
            throw new BusinessException("该账单已提交,不能取消停缴");
        }

        //获取城市信息
        City city = sbtService.getCityByCode(customerShebaoBean.getJoinCityCode());
        if(city == null || com.xtr.comm.util.StringUtils.isStrNull(city.getMonth())) {
            throw new BusinessException("无该城市缴纳月份");
        }

        //当前缴纳月份
//        Date month = DateUtil.sbtSimpleDateFormat.parse(city.getMonth());
        if(customerShebaoBean.getCurrentMonth()==null){
            throw new BusinessException("无该城市缴纳月份");
        }
        Date month=customerShebaoBean.getCurrentMonth();
        String monthStr=DateUtil.dayFormatter.format(month);

        int nowMonthInt=Integer.parseInt(DateUtil.dateToString(month,DateUtil.DATEMONTHFORMAT));//当前缴纳月份

        int stopDateInt=Integer.parseInt(DateUtil.dateToString(stopDate,DateUtil.DATEMONTHFORMAT));//要停缴的月份
        if(stopDateInt<nowMonthInt){
            throw new BusinessException("cancel");
        }
        //取消公积金之前必须要取消社保,换句话说就是社保不能有停缴
        if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            //判断社保是否有停缴
            Date sbStopDate=customerShebaoBean.getSbStopDate();
            if(sbStopDate!=null){
                int sbStopDateInt=Integer.parseInt(DateUtil.dateToString(sbStopDate,DateUtil.DATEMONTHFORMAT));//要停缴的月份
                if(sbStopDateInt>=nowMonthInt){
                    throw new BusinessException("当前有社保停缴，不能取消公积金停缴");
                }
            }
        }

        BigDecimal compareDecimal=new BigDecimal("0");
        if(dto.getRelationType().intValue()== ShebaoConstants.RELATION_TYPE_SHEBAO){
            if(customerShebaoBean.getSbStopDate()!=null){
                if(stopDateInt>nowMonthInt){//取消将要停缴的月份
                    customerShebaoWriterMapper.updateForStopDate(1,customerShebaoBean.getCurrentCompanyOrderId(),customerShebaoBean.getCustomerId());
                }else if(stopDateInt==nowMonthInt){//取消当前停缴的月份
                    //当前停缴不能取消停缴
                    throw new BusinessException("当前停缴不能取消");
//                    if(customerShebaoBean.getSbShebaotongStatus()!=null && customerShebaoBean.getSbShebaotongStatus()==ShebaoConstants.SBT_STATE_ING){//停缴之前是缴纳中
//                        //清空停缴日期,更新是否缴纳为是(如果超出基数范围,则更改基数)
//                        Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
//                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, customerShebaoBean.getSbType());
//                        if(socialBase==null){
//                            throw new BusinessException("无社保公积金基础数据,请联系管理员");
//                        }
//                        BigDecimal oldBase=customerShebaoBean.getSbBase()!=null?customerShebaoBean.getSbBase():compareDecimal;
//                        BigDecimal minBase=new BigDecimal(socialBase.getMin());
//                        BigDecimal maxBase=new BigDecimal(socialBase.getMax());
//                        BigDecimal sbtBase=customerShebaoBean.getSbSbtBase()!=null?customerShebaoBean.getSbSbtBase():compareDecimal;
//                        boolean isBoundIndexForBase=false;//是否超出基数范围,false代表没超出
//                        if(oldBase.compareTo(minBase)<0){
//                            oldBase=minBase;
//                            isBoundIndexForBase=true;
//                        }else if(oldBase.compareTo(maxBase)>0){
//                            oldBase=maxBase;
//                            isBoundIndexForBase=true;
//                        }else if(sbtBase != null && !sbtBase.equals(oldBase)) {
//                            isBoundIndexForBase = true;
//                        }
//                        CustomerShebaoBean record=new CustomerShebaoBean();
//                        record.setCustomerId(customerShebaoBean.getCustomerId());
//                        record.setCurrentCompanyOrderId(customerShebaoBean.getCurrentCompanyOrderId());
////                        record.setIsSbKeep(ShebaoConstants.KEEP_NO);
//                        record.setSbBase(oldBase);
//                        customerShebaoWriterMapper.updateForStopDateCondition(1,record);
//                        //删除需求订单,并添加汇缴订单(如果超出基数范围,则添加调基订单)
//                        cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);
//                        if(isBoundIndexForBase){//添加调基订单
//                            Map cal=caculateForAdjustBase(dto,customerShebaoBean,oldBase);
//                            addAdjustBaseOrder(dto,customerShebaoBean,oldBase,cal,ShebaoConstants.OrderType.TJ);//新增员工调基订单
//                        }else{//添加汇缴订单
//                            createOrder(dto.getCustomerId(), 1, companyShebaoOrderBean.getId(), ShebaoTypeEnum.SHEBAO, month, oldBase, socialBase);
//                        }
//                        //更新企业社保公积金
//
//                        //更新汇总数据
//                        updateCustomerOrderDesc(companyShebaoOrderBean.getId(), dto.getCustomerId(), ShebaoTypeEnum.SHEBAO, 1, monthStr);
//                    }else{
//                        //清空停缴日期,更新是否缴纳为否
//                        CustomerShebaoBean record=new CustomerShebaoBean();
//                        record.setCustomerId(customerShebaoBean.getCustomerId());
//                        record.setCurrentCompanyOrderId(customerShebaoBean.getCurrentCompanyOrderId());
//                        record.setIsSbKeep(ShebaoConstants.KEEP_NO);
//                        customerShebaoWriterMapper.updateForStopDateCondition(1,record);
//                        //删除需求订单
//                        cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.SHEBAO.getCode(), 1, 2, 3, 4, 5);
//                        //更新企业社保公积金
//
//                        //更新汇总数据
//                        updateCustomerDescIsNull(dto.getCompanyShebaoOrderId(), dto.getCustomerId(),dto.getRelationType());
//                    }
                }
            }
        }else if(dto.getRelationType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
            if(customerShebaoBean.getGjjStopDate()!=null){
                if(stopDateInt>nowMonthInt){//取消将要停缴的月份
                    customerShebaoWriterMapper.updateForStopDate(2,customerShebaoBean.getCurrentCompanyOrderId(),customerShebaoBean.getCustomerId());
                }else if(stopDateInt==nowMonthInt){//取消当前停缴的月份
                    //当前停缴不能取消停缴
                    throw new BusinessException("当前停缴不能取消");
//                    if(customerShebaoBean.getGjjShebaotongStatus()!=null && customerShebaoBean.getGjjShebaotongStatus()==ShebaoConstants.SBT_STATE_ING){//停缴之前是缴纳中
//                        //清空停缴日期,更新是否缴纳为是(如果超出基数范围,则更改基数)
//                        Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
//                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, customerShebaoBean.getGjjType());
//                        if(socialBase==null){
//                            throw new BusinessException("无社保公积金基础数据,请联系管理员");
//                        }
//                        BigDecimal oldBase=customerShebaoBean.getGjjBase()!=null?customerShebaoBean.getGjjBase():compareDecimal;
//                        BigDecimal minBase=new BigDecimal(socialBase.getMin());
//                        BigDecimal maxBase=new BigDecimal(socialBase.getMax());
//                        BigDecimal sbtBase=customerShebaoBean.getGjjSbtBase()!=null?customerShebaoBean.getGjjSbtBase():compareDecimal;
//                        boolean isBoundIndexForBase=false;//是否超出基数范围,false代表没超出
//                        if(oldBase.compareTo(minBase)<0){
//                            oldBase=minBase;
//                            isBoundIndexForBase=true;
//                        }else if(oldBase.compareTo(maxBase)>0){
//                            oldBase=maxBase;
//                            isBoundIndexForBase=true;
//                        }else if(sbtBase != null && !sbtBase.equals(oldBase)) {
//                            isBoundIndexForBase = true;
//                        }
//                        CustomerShebaoBean record=new CustomerShebaoBean();
//                        record.setCustomerId(customerShebaoBean.getCustomerId());
//                        record.setCurrentCompanyOrderId(customerShebaoBean.getCurrentCompanyOrderId());
////                        record.setIsGjjKeep(ShebaoConstants.KEEP_NO);
//                        record.setGjjBase(oldBase);
//                        customerShebaoWriterMapper.updateForStopDateCondition(2,record);
//                        //删除需求订单,并添加汇缴订单(如果超出基数范围,则添加调基订单)
//                        cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ.getCode(), 1, 2, 3, 4, 5);
//                        if(isBoundIndexForBase){//添加调基订单
//                            Map cal=caculateForAdjustBase(dto,customerShebaoBean,oldBase);
//                            addAdjustBaseOrder(dto,customerShebaoBean,oldBase,cal,ShebaoConstants.OrderType.TJ);//新增员工调基订单
//                        }else{//添加汇缴订单
//                            createOrder(dto.getCustomerId(), 1, companyShebaoOrderBean.getId(), ShebaoTypeEnum.GJJ, month, oldBase, socialBase);
//                        }
//                        //更新企业社保公积金
//
//                        //更新汇总数据
//                        updateCustomerOrderDesc(companyShebaoOrderBean.getId(), dto.getCustomerId(), ShebaoTypeEnum.GJJ, 1, monthStr);
//                    }else{
//                        //清空停缴日期,更新是否缴纳为否
//                        CustomerShebaoBean record=new CustomerShebaoBean();
//                        record.setCustomerId(customerShebaoBean.getCustomerId());
//                        record.setCurrentCompanyOrderId(customerShebaoBean.getCurrentCompanyOrderId());
//                        record.setIsGjjKeep(ShebaoConstants.KEEP_NO);
//                        customerShebaoWriterMapper.updateForStopDateCondition(2,record);
//                        //删除需求订单
//                        cleanOrderByCustomerIdAndCompanyOrderId(dto.getCustomerId(),dto.getCompanyShebaoOrderId(), ShebaoTypeEnum.GJJ.getCode(), 1, 2, 3, 4, 5);
//                        //更新企业社保公积金
//
//                        //更新汇总数据
//                        updateCustomerDescIsNull(dto.getCompanyShebaoOrderId(), dto.getCustomerId(),dto.getRelationType());
//                    }
                }
            }
        }
        return companyShebaoOrderBean;
    }

    /**
     * 根据企业订单ID获取社保公积金订单
     * @param companyShebaoOrderId
     * @return
     */
    public List<CustomerShebaoOrderBean> selectByCompanyOrderId(Long companyShebaoOrderId){
        return customerShebaoOrderReaderMapper.selectByCompanyOrderId(companyShebaoOrderId);
    }

    /**
     * 查询该公司当前未读账单的数量
     * @param companyId
     * @return
     */
    @Override
    public Long selectNoReadOrderCount(Long companyId) throws Exception{
        return companyShebaoOrderReaderMapper.selectNoReadOrderCount(companyId);
    }

    /**
     * 将该公司未读账单改成已读账单
     * @param companyId
     */
    @Override
    public void updateNoReadOrders(Long companyId) throws Exception {
         companyShebaoOrderWriterMapper.updateNoReadOrders(companyId);
    }

    public Map<Integer, List> getCurrentOrderFaildMsg(long customerId, long companyShebaoOrderId) {
        //查询上月缴纳订单状态
        Map<Integer, List> lastResult = new HashMap<>();
        List<CustomerShebaoOrderBean> lastOrders = customerShebaoOrderReaderMapper.selectCustomerLastOrder(companyShebaoOrderId, customerId);
        if(lastOrders != null) {
            List<Date> sbBjCgDates = new ArrayList<>();
            List<Date> gjjBjCgDates = new ArrayList<>();

            lastResult.put(ShebaoTypeEnum.GJJ.getCode(), new ArrayList<>());
            lastResult.put(ShebaoTypeEnum.SHEBAO.getCode(), new ArrayList<>());
            for (CustomerShebaoOrderBean lastOrder : lastOrders) {
                //订单类型(1增员，2汇缴，3调基，4停缴, 5补缴)
                Integer shebaoTypeEnum = lastOrder.getRequireType();
                String text = (lastOrder.getSbtStatus() == 2  || lastOrder.getSbtStatus() == 3? "失败：" : "成功：") + DateUtil.sbtSimpleDateFormat.format(lastOrder.getOverdueMonth());
                if(lastOrder.getOrderType() == 1) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'提交增员" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 2) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'提交续缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 3) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'提交调基" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 4) {
                    lastResult.get(shebaoTypeEnum).add("{'text':'提交停缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                }else if(lastOrder.getOrderType() == 5) {//补缴合并
//                    lastResult.get(shebaoTypeEnum).add("{'text':'补缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");

                    if(shebaoTypeEnum == ShebaoTypeEnum.GJJ.getCode()) {
                        if(lastOrder.getSbtStatus() == 1) {
                            gjjBjCgDates.add(lastOrder.getOverdueMonth());
                        }else{
                            lastResult.get(shebaoTypeEnum).add("{'text':'提交补缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                        }
                    }else{
                        if(lastOrder.getSbtStatus() == 1) {
                            sbBjCgDates.add(lastOrder.getOverdueMonth());
                        }else{
                            lastResult.get(shebaoTypeEnum).add("{'text':'提交补缴" + text+"','reason':'" + lastOrder.getResponseMsg() + "'}");
                        }
                    }
                }
            }
            //合并上月补缴信息
            if(sbBjCgDates.size() > 0) {
                Collections.sort(sbBjCgDates);
                List<String> strings = joinDate(sbBjCgDates);
                for (String string : strings) {
                    lastResult.get(ShebaoTypeEnum.SHEBAO.getCode()).add("{'text':'提交补缴成功：" + string+"','reason':'null'}");
                }
            }

            if(gjjBjCgDates.size() > 0) {
                Collections.sort(gjjBjCgDates);
                List<String> strings = joinDate(gjjBjCgDates);
                for (String string : strings) {
                    lastResult.get(ShebaoTypeEnum.GJJ.getCode()).add("{'text':'提交补缴成功：" + string+"','reason':'null'}");
                }
            }

        }
        return lastResult;
    }

    /**
     * 获取当前月社保补交信息
     * @param companyShebaoOrderId
     * @return
     */
    @Override
    public List<Map<String,Object>> getOverdueDetail(Long companyShebaoOrderId,Long customerId,Integer requireType){
        List<Map<String,Object>> overdueDetailList = new ArrayList<Map<String,Object>>();
        LOGGER.info("获取当前月社保补交信息:companyShebaoOrderId="+ companyShebaoOrderId + ",customerId=" + customerId + ",requireType=" + requireType);
        try {
            if (companyShebaoOrderId ==null || customerId == null || requireType == null){
                LOGGER.error("获取当前月社保补交信息失败，参数为null");
                return null;
            }
            CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
            customerShebaoOrderBean.setCompanyShebaoOrderId(companyShebaoOrderId);
            customerShebaoOrderBean.setCustomerId(customerId);
            customerShebaoOrderBean.setRequireType(requireType);
            customerShebaoOrderBean.setOrderType(5);
            List<CustomerShebaoOrderBean> customerShebaoOrders=customerShebaoOrderReaderMapper.selectShebaoOrderList(customerShebaoOrderBean);
            for (CustomerShebaoOrderBean customerShebaoOrder : customerShebaoOrders) {
                Date overdueMonth = customerShebaoOrder.getOverdueMonth();
                BigDecimal orderBase = customerShebaoOrder.getOrderBase();
                boolean isMatch = false;

                if (overdueDetailList != null) {
                    for (Map<String, Object> overdueDetail : overdueDetailList) {
                       BigDecimal orderBase_2 = (BigDecimal)overdueDetail.get("orderBase");

                        if (orderBase_2.compareTo(orderBase) == 0) {
                            overdueDetail.put("overdueMonth", overdueDetail.get("overdueMonth") + "," + DateUtil.sbtSimpleDateFormat.format(overdueMonth));
                            isMatch = true;
                            break;
                        }
                    }
                }

                if (!isMatch) {
                    Map<String, Object> orderMap = new HashMap<String, Object>();
                    orderMap.put("overdueMonth", DateUtil.sbtSimpleDateFormat.format(overdueMonth));
                    orderMap.put("orderBase", orderBase);
                    overdueDetailList.add(orderMap);
                }
            }


        }
        catch(Exception e){
            overdueDetailList = null;
            LOGGER.error("获取当前月社保补交信息失败",e);
        }
        return overdueDetailList;
    }
}
