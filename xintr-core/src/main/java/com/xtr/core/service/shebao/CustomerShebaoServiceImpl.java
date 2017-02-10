package com.xtr.core.service.shebao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderDescBean;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.shebao.JrOrderDto;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.jd.util.StringUtils;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.IncType;
import com.xtr.comm.sbt.api.InsType;
import com.xtr.comm.sbt.api.SocialBase;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderDescReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoOrderDescWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoWriterMapper;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @Author Xuewu
 * @Date 2016/9/13.
 */
@Service("customerShebaoService")
public class CustomerShebaoServiceImpl implements CustomerShebaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerShebaoService.class);

    @Resource
    private SbtService sbtService;

    // 社保通sdk
    private SheBaoTong sheBaoTong = new SheBaoTong(true);

    @Resource
    private CustomerShebaoReaderMapper customerShebaoReaderMapper;

    @Resource
    private CustomerShebaoWriterMapper customerShebaoWriterMapper;

    @Resource
    private CustomerShebaoOrderDescWriterMapper customerShebaoOrderDescWriterMapper;

    @Resource
    private CustomerShebaoOrderDescReaderMapper customerShebaoOrderDescReaderMapper;

    @Resource
    private CustomerShebaoOrderReaderMapper customerShebaoOrderReaderMapper;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Override
    public int updateByPrimaryKey(CustomerShebaoBean record) {
        return customerShebaoWriterMapper.updateByPrimaryKey(record);
    }

    /**
     * 自行计算
     * @param base
     * @param socialBase
     * @return
     * @throws BusinessException
     * @throws IOException
     */
//    public Map cal(double base, SocialBase socialBase) throws BusinessException, IOException {
//        if(base < socialBase.getMin()) {
//            LOGGER.error("缴纳基数低于最低额度base="+base+",socialBase.getMin()="+socialBase.getMin());
//            throw new BusinessException("缴纳基数低于最低额度");
//        }
//        if(base > socialBase.getMax()) {
//            LOGGER.error("缴纳基数低于最低额度base=" + base + ",socialBase.getMax()=" + socialBase.getMax());
//            throw new BusinessException("缴纳基数大于最高额度");
//        }
//        List<IncType> inc = socialBase.getInc();
//
//        List<Map<String, Object>> rows = new ArrayList<>();
//        double orgSum = 0;
//        double empSum = 0;
//        if(inc != null) {
//            for (IncType incType : inc) {
//
//                double rowBase = base;
//
//                if(incType.getMin() != 0d) {
//                    rowBase = Math.max(rowBase, incType.getMin());
//                }
//
//                if(incType.getMax() != 0d) {
//                    rowBase = Math.min(rowBase, incType.getMax());
//                }
//
//                Map<String, Object> row = new HashMap<>();
//                row.put("base", base);
//                row.put("orgprop", incType.getOrgprop());
//                double orgBase = rowBase * incType.getOrgprop() / 100;
//                orgBase = formatterSbMoney(orgBase);
//                row.put("orgbase", orgBase);
//                row.put("empprop", incType.getEmpprop());
//
//                double empBase = rowBase * incType.getEmpprop() / 100;
//                empBase = formatterSbMoney(empBase);
//                row.put("empbase", empBase);
//                row.put("code", incType.getCode());
//                row.put("name", incType.getName());
//                row.put("rowBase", rowBase);
//                rows.add(row);
//
//                orgSum += orgBase;
//                empSum += empBase;
//            }
//        }
//        Map result = new LinkedHashMap();
//
//        socialBase.setOrgSum(orgSum);
//        socialBase.setEmpSum(empSum);
//
//        result.put(socialBase, rows);
//
//        result.put("rows", rows);
//        result.put("orgSum", orgSum);
//        result.put("empSum", empSum);
//        return result;
//    }

    /**
     * 根据sbt接口计算
     * @param base
     * @param socialBase
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    @Override
    public Map cal(double base, SocialBase socialBase) throws BusinessException, IOException {
        if(base < socialBase.getMin())
            throw new BusinessException("缴纳基数低于最低额度");

        if(base > socialBase.getMax())
            throw new BusinessException("缴纳基数大于最高额度");

        List<IncType> inc = socialBase.getInc();

        Map<String, String> incName = new HashMap<>();
        for (IncType incType : inc) {
            incName.put(incType.getCode(), incType.getName());
        }

        List<InsType> insTypes = new ArrayList<>();
        insTypes.add(new InsType(socialBase.getType(), base));
        JSONArray results = sheBaoTong.calculate(insTypes);
        JSONObject jsonObject = results.getJSONObject(0);

        Map result = new LinkedHashMap();
        List<Map<String, Object>> rows = new ArrayList<>();

        double orgSum = 0;
        double empSum = 0;
        for (String incCode : jsonObject.keySet()) {
            JSONObject rowDetail = jsonObject.getJSONObject(incCode);

            Map<String, Object> row = new HashMap<>();

            row.put("code", incCode);
            row.put("name", incName.get(incCode));

            row.put("base", base);
            row.put("rowBase", rowDetail.get("base"));

            String coproportion = rowDetail.getString("coproportion");
            if(StringUtils.isNotBlank(coproportion) && coproportion.contains("×")) {
                coproportion = coproportion.split("×")[1];
            }else if(StringUtils.isNotBlank(coproportion) && !coproportion.contains("%")){
                coproportion = "-";
            }
            row.put("orgprop", coproportion);

            Double co = rowDetail.getDouble("co");
            row.put("orgbase", co);

            String inproportion = rowDetail.getString("inproportion");
            if(StringUtils.isNotBlank(inproportion) && inproportion.contains("×")) {
                inproportion = inproportion.split("×")[1];
            }else if(StringUtils.isNotBlank(inproportion) && !inproportion.contains("%")){
                inproportion = "-";
            }
            row.put("empprop", inproportion);
            Double in = rowDetail.getDouble("in");
            row.put("empbase", in);

            row.put("sum", rowDetail.get("sum"));
            rows.add(row);

            orgSum += co;
            empSum += in;
        }
        socialBase.setOrgSum(orgSum);
        socialBase.setEmpSum(empSum);

        result.put(socialBase, rows);

        result.put("rows", rows);
        result.put("orgSum", orgSum);
        result.put("empSum", empSum);
        return result;
    }

    @Override
    public List<Map> calOverDueBySbt(List<JrOrderDto> overOrders, SocialBase socialBase) throws BusinessException, IOException {
        List<Map> result = new ArrayList<>();

        List<IncType> inc = socialBase.getInc();

        Map<String, String> incName = new HashMap<>();
        for (IncType incType : inc) {
            incName.put(incType.getCode(), incType.getName());
        }

        List<InsType> insTypes = new ArrayList<>();
        for (JrOrderDto overOrder : overOrders) {
            insTypes.add(new InsType(socialBase.getType(), overOrder.getBase(), overOrder.getMonth()));
        }
        JSONArray results = sheBaoTong.calcoverdue(insTypes);

        if(results!=null && results.size()>0){
            //验证基数是否正确
            for(int index = 0; index < results.size(); index++ ){
                JSONObject jsonObject = results.getJSONObject(index);
                if(null != jsonObject.getInteger("errcode")){//调用社保通接口返回错误
                    throw new BusinessException(StringUtils.isNotBlank(jsonObject.getString("msg"))?jsonObject.getString("msg"):"计算失败");
                }
            }

            for(int index = 0; index < results.size(); index++ ){
                JSONObject jsonObject = results.getJSONObject(index);

                Map rowResult = new HashMap();
                List<Map<String, Object>> rows = new ArrayList<>();

                double orgSum = 0;
                double empSum = 0;

                for (String incCode : jsonObject.keySet()) {
                    JSONObject rowDetail = jsonObject.getJSONObject(incCode);

                    Map<String, Object> row = new HashMap<>();

                    row.put("code", incCode);
                    row.put("name", incName.get(incCode));

                    row.put("base", overOrders.get(index).getBase());
                    row.put("rowBase", rowDetail.get("base"));

                    Double co = rowDetail.getDouble("co");
                    row.put("orgbase", co);

                    Double in = rowDetail.getDouble("in");
                    row.put("empbase", in);

                    row.put("sum", rowDetail.getDouble("sum"));
                    row.put("odf", rowDetail.getDouble("odf"));
                    rows.add(row);

                    orgSum += co;
                    empSum += in;
                }

                rowResult.put("rows", rows);
                rowResult.put("orgSum", new BigDecimal(orgSum).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                rowResult.put("empSum", new BigDecimal(empSum).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue());
                rowResult.put("month", overOrders.get(index).getMonth());
                result.add(rowResult);
            }
        }else{
            LOGGER.error("调用社保通返回的结果是空");
            throw new BusinessException("计算失败");
        }


        return result;
    }

    public SocialBase getSocialBaseByBasic(ShebaoTypeEnum shebaoTypeEnum, Basic basic, String type) throws BusinessException {
        List<SocialBase> ins = null;
        if(shebaoTypeEnum == ShebaoTypeEnum.SHEBAO) {
            ins = basic.getIns();
        }else if(shebaoTypeEnum == ShebaoTypeEnum.GJJ){
            ins = basic.getHf();
        }

        if(ins == null)
            throw new BusinessException("暂无基础数据");

        SocialBase socialBase = null;
        if(ins != null && ins.size() > 0) {
            for (SocialBase sb : ins) {
                if(sb.getType().equalsIgnoreCase(type)) {
                    socialBase = sb;
                    break;
                }
            }
        }
        if(socialBase == null)
            throw new BusinessException("暂无该缴纳类型基础数据");
        return socialBase;
    }

    @Override
    public Date getUpdateBaseDate(String joinCity, ShebaoTypeEnum shebaoTypeEnum, String type) {
        Basic basic = sbtService.getBasic(joinCity);
        SocialBase socialBase = getSocialBaseByBasic(shebaoTypeEnum, basic, type);
        try {
            return DateUtil.sbtSimpleDateFormat.parse(socialBase.getBchgmonth());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public ResultResponse selectPage(CustomerShebaoBean bean) {
        PageBounds pageBounds = new PageBounds(bean.getPageIndex(), bean.getPageSize());
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(true);
        PageList<Map> list = customerShebaoReaderMapper.selectPage(bean, pageBounds);

        if(list != null) {
            for (Map map : list) {
                if(map.get("current_company_order_id") != null && map.get("customer_id") != null) {
                    Long customerId = Long.valueOf(map.get("customer_id") + "");
                    Long currentCompanyOrderId = Long.valueOf(map.get("current_company_order_id") + "");
                    CustomerShebaoOrderDescBean desc = customerShebaoOrderDescWriterMapper.selectByCompanyOrderIdAndCustomerId(currentCompanyOrderId, customerId);
                    map.put("currentDesc", desc);
                    Long lastCompanyOrderId = customerShebaoOrderReaderMapper.selectCustomerLastOrderId(customerId, currentCompanyOrderId);
                    int i = customerShebaoOrderReaderMapper.selectDismiss(customerId);
                    map.put("isWaitDismissing", i > 0);
                    if(lastCompanyOrderId != null) {//上笔订单错误信息
                        map.put("lastResult", customerShebaoOrderService.getOrderFaildMsg(customerId, lastCompanyOrderId));
                    }
                    //当前订单错误信息(超过社保通截止日,则消失)
                    if(map.get("isTimeOut") != null && ((Long)map.get("isTimeOut")).longValue()<=0){
                        map.put("currentResult", customerShebaoOrderService.getCurrentOrderFaildMsg(customerId, currentCompanyOrderId));
                    }

                }
            }
        }

        resultResponse.setPaginator(list.getPaginator());
        resultResponse.setData(list);
        return resultResponse;
    }




    @Override
    public List<CustomerShebaoOrderDto> getCustomerOrders(CustomerShebaoOrderDto customerShebaoOrderDto) {
        if(null != customerShebaoOrderDto){


            List<CustomerShebaoOrderDto> dtoList= customerShebaoOrderDescReaderMapper.selectShebaoOrders(customerShebaoOrderDto);
            if(dtoList!=null && dtoList.size()>0){

                CustomerShebaoOrderBean customerShebaoOrderBean=new CustomerShebaoOrderBean();
                customerShebaoOrderBean.setCompanyShebaoOrderId(customerShebaoOrderDto.getCompanyShebaoOrderId());
                //出现增员成功,补缴失败或者增员失败,补缴成功这一类,清空相应的errorText
                List<CustomerShebaoOrderBean> orderList= customerShebaoOrderReaderMapper.selectSuccessShebaoOrderList( customerShebaoOrderBean);

                for(CustomerShebaoOrderDto shebaoOrderDto:dtoList){
                    if(orderList!=null && orderList.size()>0){
                        shebaoOrderDto.setSbZyText("");
                        shebaoOrderDto.setGjjZyText("");
                        shebaoOrderDto.setSbHjText("");
                        shebaoOrderDto.setGjjHjText("");
                        shebaoOrderDto.setSbTjText("");
                        shebaoOrderDto.setGjjTjText("");
                        shebaoOrderDto.setSbStopText("");
                        shebaoOrderDto.setGjjStopText("");
                        shebaoOrderDto.setSbBjText("");
                        shebaoOrderDto.setGjjBjText("");

                        for(CustomerShebaoOrderBean orderBean:orderList){
                            if(orderBean.getCustomerId()!=null && shebaoOrderDto.getCustomerId()!=null && orderBean.getCustomerId().intValue()==shebaoOrderDto.getCustomerId().intValue()){
                                if(orderBean.getOrderType()!=null){
                                    if(orderBean.getOrderType().intValue()== ShebaoConstants.OrderType.ZY){
                                        if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                            shebaoOrderDto.setSbZyText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }else if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                            shebaoOrderDto.setGjjZyText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }
                                    }else if(orderBean.getOrderType().intValue()== ShebaoConstants.OrderType.HJ){
                                        if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                            shebaoOrderDto.setSbHjText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }else if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                            shebaoOrderDto.setGjjHjText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }
                                    }else if(orderBean.getOrderType().intValue()== ShebaoConstants.OrderType.TJ){
                                        if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                            shebaoOrderDto.setSbTjText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }else if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                            shebaoOrderDto.setGjjTjText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }
                                    }else if(orderBean.getOrderType().intValue()== ShebaoConstants.OrderType.STOP){
                                        if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                            shebaoOrderDto.setSbStopText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }else if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                            shebaoOrderDto.setGjjStopText(DateUtil.dayFormatter.format(orderBean.getOverdueMonth()));
                                        }
                                    }else if(orderBean.getOrderType().intValue()== ShebaoConstants.OrderType.BJ){
                                        if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                            shebaoOrderDto.setSbBjText(shebaoOrderDto.getSbBjText()+DateUtil.dayFormatter.format(orderBean.getOverdueMonth())+"&nbsp;");
                                        }else if(orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                            shebaoOrderDto.setGjjBjText(shebaoOrderDto.getGjjBjText()+DateUtil.dayFormatter.format(orderBean.getOverdueMonth())+"&nbsp;");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return dtoList;
        }
        return null;
    }

    @Override
    public CustomerShebaoBean selectCustomerShebaoByCustomerId(long customerId) {
        return customerShebaoReaderMapper.selectByCustomerId(customerId);
    }

    /**
     * 批量更新员工公积金社保的社保通返回状态
     * @param type
     * @param status
     * @param companyShebaoOrderId
     * @param list
     * @return
     */
    public int updateSbAndGjjStateBatch(int type,int status,  long companyShebaoOrderId,List<Long> list){
        return customerShebaoWriterMapper.updateSbAndGjjStateBatch(type,status,companyShebaoOrderId,list);
    }

    @Override
    public int selectKeepCustomerCount(ShebaoTypeEnum shebaoTypeEnum, Long memberCompanyId) {
        return customerShebaoReaderMapper.selectKeepCustomerCount(shebaoTypeEnum.getCode(), memberCompanyId);
    }

    @Override
    public List<Map> selectJoinCityByCompanyId(long companyId) {
        return customerShebaoReaderMapper.selectJoinCityByCompanyId(companyId);
    }



    public double formatterSbMoney(double money){
        String strMoney = money + "";
        int indexOf = strMoney.indexOf(".");
        if(indexOf != -1) {
            String last = strMoney.substring(indexOf + 1);
            if(last.length() >= 2) {
                String index1 = last.substring(1, 1);
                if(!"0".equals(index1)) {
                    return new Double(strMoney.substring(0, indexOf)+"."+(new Integer(last.substring(0, 1)) + 1));
                }
                return money;
            }
        }
        return money;
    }


    @Override
    public Map selectBaseInfo(Long customerId) {
        return customerShebaoReaderMapper.selectBaseInfo(customerId);
    }

    @Override
    public List<Map> selectKeepSbAndGjjType(long companyId) {
        return customerShebaoReaderMapper.selectKeepSbAndGjjType(companyId);
    }


}
