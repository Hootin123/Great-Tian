package com.xtr.core.service.shebao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.*;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.customer.CustomersSupplementDto;
import com.xtr.api.dto.shebao.ResultSheBaoDto;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersSupplementService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.basic.Config;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.excel.ExcelExportException;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.sbt.SbtResponse;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.*;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropUtils;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.company.CompanyShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.*;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoOrderDescWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomersSupplementWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author Xuewu
 * @Date 2016/9/18.
 */
@Service("companyShebaoService")
public class CompanyShebaoServiceImpl implements CompanyShebaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyShebaoService.class);

    @Resource
    private CompanyShebaoOrderReaderMapper companyShebaoOrderReaderMapper;

    @Resource
    private CompanyShebaoOrderWriterMapper companyShebaoOrderWriterMapper;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private CustomerShebaoOrderDescReaderMapper customerShebaoOrderDescReaderMapper;

    @Resource
    private CustomerShebaoOrderDescWriterMapper customerShebaoOrderDescWriterMapper;

    @Resource
    private CustomerShebaoOrderReaderMapper customerShebaoOrderReaderMapper;

    @Resource
    private CustomerShebaoOrderWriterMapper customerShebaoOrderWriterMapper;

    @Resource
    private CustomerShebaoWriterMapper customerShebaoWriterMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CustomersSupplementService customersSupplementService;

    @Resource
    private SbtService sbtService;

    @Resource
    private CustomerShebaoService customerShebaoService;

    @Resource
    private CustomerShebaoReaderMapper customerShebaoReaderMapper;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    public CompanyShebaoOrderBean selectByPrimaryKey(Long id){
        return companyShebaoOrderReaderMapper.selectByPrimaryKey(id);
    }
    @Resource
    private CustomersService customersService;

    @Resource
    private CustomersSupplementReaderMapper customersSupplementReaderMapper;

    @Resource
    private CustomersSupplementWriterMapper customersSupplementWriterMapper;

    @Override
    public CompanyShebaoOrderBean selectLastOrderByCompanyAndCity(long companyId, String joinCityCode) {
        return null;
    }

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    /**
     * 查询账单数据
     * @param city  城市编码
     * @param is_current  类型, 1:当前账单 0:历史账单
     * @return
     */
    @Override
    public List<CompanyShebaoOrderDto> getPayOrder(Long companyId, String city, int is_current) {
        return companyShebaoOrderReaderMapper.selectPayOrder(companyId, city, is_current);
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public CompanyShebaoOrderBean createCompanyShebaoOrder(long companyId, City city, String cityCode) throws BusinessException {

        if(city == null) {
            throw new BusinessException("当前城市缴纳月份基础信息不存在");
        }

        CompanyShebaoOrderBean companyShebaoOrderBean = new CompanyShebaoOrderBean();

        String orderNumber = idGeneratorService.getOrderId(BusinessEnum.COMPANY_SHEBAO_ORDER);
        companyShebaoOrderBean.setCompanyId(companyId);
        companyShebaoOrderBean.setOrderNumber(orderNumber);
        companyShebaoOrderBean.setJoinCityName(city.getCname());
        companyShebaoOrderBean.setJoinCityCode(cityCode);
        try {
            companyShebaoOrderBean.setOrderDate(simpleDateFormat.parse(city.getMonth()));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("当前城市缴纳月份为空", e);
        }

        companyShebaoOrderBean.setStatus(0);
        Date lastDate = null;
        if(StringUtils.isEmpty(city.getLastDate())){
            Calendar instance = Calendar.getInstance();
            instance.setTime(companyShebaoOrderBean.getOrderDate());
            instance.set(Calendar.DAY_OF_MONTH, city.getSubdl());
            if(city.getRule() == 1){
                instance.add(Calendar.MONTH, -1);
            }
            lastDate = instance.getTime();
        }else{
            try {
                lastDate = simpleDateFormat2.parse(city.getLastDate());
            } catch (ParseException e) {
                e.printStackTrace();
                throw new BusinessException("截止日期错误", e);
            }
        }
        Date requireLastDate = DateUtil.addWorkDay(lastDate, -5);
        Date orderLastDate = DateUtil.addWorkDay(lastDate, -2);
        Date now = new Date();

        if(now.after(orderLastDate)) {
            LOGGER.error("now="+now+"orderLastDate="+orderLastDate);
            throw new BusinessException("已过订单处理窗口期");
        }else if(now.after(requireLastDate)) {//处于订单窗口期
            companyShebaoOrderBean.setStatus(1);
        }else if(now.before(requireLastDate)) {
            companyShebaoOrderBean.setStatus(0);
        }

        companyShebaoOrderBean.setLastTime(lastDate);
        companyShebaoOrderBean.setRequireLastTime(requireLastDate);
        companyShebaoOrderBean.setOrderLastTime(orderLastDate);
        companyShebaoOrderBean.setCreateTime(now);
        companyShebaoOrderBean.setIsCurrent(1);//设置为当前订单
        int insert = companyShebaoOrderWriterMapper.insert(companyShebaoOrderBean);

        if(insert > 0) {
            //更新该地区员工当前缴纳月
            customerShebaoWriterMapper.updateCurrentMonth(companyId, cityCode, companyShebaoOrderBean.getOrderDate(), companyShebaoOrderBean.getId());
            //更新其他订单为历史订单
            companyShebaoOrderWriterMapper.updateOtherToDefault(companyId, cityCode, companyShebaoOrderBean.getId());
        }


        return companyShebaoOrderBean;
    }

    @Override
    public CompanyShebaoOrderBean getCompanyShebaoOrderByCity(long companyId, City city, String cityCode) throws BusinessException {
        CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoOrderReaderMapper.selectOrderByCityAndMonth(companyId, cityCode, city.getMonth());
        if(companyShebaoOrderBean == null) {
            companyShebaoOrderBean = createCompanyShebaoOrder(companyId, city, cityCode);
        }
        return companyShebaoOrderBean;
    }

    /**
     * 根据主键查询社保订单
     * @param id
     * @return
     */
    @Override
    public CompanyShebaoOrderBean getCompanyShebaoOrderById(Long id) {
        if(null != id){
            return companyShebaoOrderReaderMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    /**
     * 查询当前账单个数
     * @param comapnyId
     * @param status
     * @return
     */
    @Override
    public int getCompanyShebaoOrderCount(Long comapnyId, int status) {
        if(null != comapnyId){
            return companyShebaoOrderReaderMapper.selectCompanyShebaoOrderCount(comapnyId, status);
        }
        return 0;
    }

    /**
     * 提交社保通订单
     * @param id
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse updateSubmitOrder(Long id, String type,Long companyId)throws BusinessException,Exception {
        //提交社保通订单基础验证
        long startDateValidator = System.currentTimeMillis();
        validatorSubmitOrderBase( id,  type, companyId);
        LOGGER.info("提交社保通订单基础验证执行时间：" + (System.currentTimeMillis()-startDateValidator));

        //根据企业订单ID获取所有的员工订单
        long startDateQueryAll = System.currentTimeMillis();
        CustomerShebaoOrderBean customerShebaoOrderBean = new CustomerShebaoOrderBean();
        customerShebaoOrderBean.setCompanyShebaoOrderId(id);
        List<CustomerShebaoOrderBean> customerShebaoOrderBeens = customerShebaoOrderReaderMapper.selectShebaoOrderList(customerShebaoOrderBean);
        LOGGER.info("根据企业订单ID获取所有的员工订单执行时间：" + (System.currentTimeMillis()-startDateQueryAll));
        if(customerShebaoOrderBeens==null || customerShebaoOrderBeens.size()<=0){
            throw new BusinessException("无社保公积金订单");
        }

        ResultResponse resultResponse = new ResultResponse();
        long startDateQueryAssem = System.currentTimeMillis();
        //拼装要调用提交代缴订单的社保通的参数
        OrderPlace orderPlace=assemOrderPlaceForSubmitOrder(id,customerShebaoOrderBeens);
        LOGGER.info("拼装社保通订单数据:"+JSON.toJSONString(orderPlace));
        LOGGER.info("拼装要调用提交代缴订单的社保通的参数执行时间：" + (System.currentTimeMillis()-startDateQueryAssem));

        SheBaoTong sheBaoTong = new SheBaoTong(true);
        SbtResponse sbtResponse = null;

        //提交代缴订单之前先取消一次,不管成功还是失败
        CompanyShebaoOrderBean shebaoOrderBeanCheck=selectByPrimaryKey(id);
        if(shebaoOrderBeanCheck!=null && StringUtils.isStrNull(shebaoOrderBeanCheck.getShebaotongServiceNumber())){
            long startDateQueryFirstCancel = System.currentTimeMillis();
            SbtResponse cancelResponseBefore=sheBaoTong.orderManager(shebaoOrderBeanCheck.getShebaotongServiceNumber(), 0);
            JSONObject cancelResBefore = JSON.parseObject(cancelResponseBefore.getData());
            LOGGER.info("提交代缴订单之前取消订单返回结果:"+JSON.toJSONString(cancelResBefore));
            LOGGER.info("提交代缴订单之前先取消一次执行时间：" + (System.currentTimeMillis()-startDateQueryFirstCancel));
        }

        //调用社保通提交代缴订单接口
        long startDateQueryFirstExecute = System.currentTimeMillis();
        sbtResponse = sheBaoTong.orderPlace(orderPlace);
        JSONObject res = JSON.parseObject(sbtResponse.getData());

//        String aaa="{\"amount\":9789.00,\"ordsum\":9789.00,\"refundFeeDtl\":[{\"id\":\"310101199308011642\",\"co\":1550.00,\"dtl\":[{\"orgDiff\":950.0000,\"empDiff\":400.0000,\"insCode\":\"yanglao\"},{\"orgDiff\":450.0000,\"empDiff\":100.0000,\"insCode\":\"yiliao\"},{\"orgDiff\":50.0000,\"empDiff\":3.0000,\"insCode\":\"dabing_yiliao\"},{\"orgDiff\":40.0000,\"empDiff\":10.0000,\"insCode\":\"shiye\"},{\"orgDiff\":20.0000,\"empDiff\":0.0000,\"insCode\":\"gongshang\"},{\"orgDiff\":40.0000,\"empDiff\":0.0000,\"insCode\":\"shengyu\"}],\"desc\":\"临时退单\",\"name\":\"常建华\",\"service\":0.00,\"month\":\"201611\",\"feeType\":\"normal\",\"sum\":2063.00,\"code\":\"shebao\",\"type\":\"beijingshebao\",\"in\":513.00},{\"id\":\"310101199308011642\",\"co\":600.00,\"dtl\":[{\"orgDiff\":600.0000,\"empDiff\":600.0000,\"insCode\":\"gongjijin\"}],\"desc\":\"临时退单\",\"name\":\"常建华\",\"service\":0.00,\"month\":\"201611\",\"feeType\":\"normal\",\"sum\":1200.00,\"code\":\"gongjijin\",\"type\":\"beijinggongjijin\",\"in\":600.00}],\"ordRefundFee\":3263.00,\"service\":0.00,\"orderid\":\"111111111\",\"success\":[{\"id\":\"652901198805105737\",\"co\":1550.0000,\"odf\":0.0000,\"status\":\"add\",\"name\":\"穆学而\",\"month\":\"201611\",\"base\":5000.0000,\"sum\":2063.0000,\"code\":\"shebao\",\"type\":\"beijingshebao\",\"in\":513.0000},{\"id\":\"370681199405028489\",\"co\":1550.0000,\"odf\":0.0000,\"status\":\"add\",\"name\":\"花贝\",\"month\":\"201611\",\"base\":5000.0000,\"sum\":2063.0000,\"code\":\"shebao\",\"type\":\"beijingshebao\",\"in\":513.0000},{\"id\":\"652901198805105737\",\"co\":1550.0000,\"odf\":0.0000,\"status\":\"overdue\",\"name\":\"穆学而\",\"month\":\"201610\",\"base\":5000.0000,\"sum\":2063.0000,\"code\":\"shebao\",\"type\":\"beijingshebao\",\"in\":513.0000},{\"id\":\"370681199405028489\",\"co\":1550.0000,\"odf\":0.0000,\"status\":\"overdue\",\"name\":\"花贝\",\"month\":\"201610\",\"base\":5000.0000,\"sum\":2063.0000,\"code\":\"shebao\",\"type\":\"beijingshebao\",\"in\":513.0000},{\"id\":\"652901198805105737\",\"co\":600.0000,\"odf\":0.0000,\"status\":\"add\",\"name\":\"穆学而\",\"month\":\"201611\",\"base\":5000.0000,\"sum\":1200.0000,\"code\":\"gongjijin\",\"type\":\"beijinggongjijin\",\"in\":600.0000},{\"id\":\"370681199405028489\",\"co\":600.0000,\"odf\":0.0000,\"status\":\"add\",\"name\":\"花贝\",\"month\":\"201611\",\"base\":5000.0000,\"sum\":1200.0000,\"code\":\"gongjijin\",\"type\":\"beijinggongjijin\",\"in\":600.0000},{\"id\":\"652901198805105737\",\"co\":600.0000,\"odf\":0.0000,\"status\":\"overdue\",\"name\":\"穆学而\",\"month\":\"201610\",\"base\":5000.0000,\"sum\":1200.0000,\"code\":\"gongjijin\",\"type\":\"beijinggongjijin\",\"in\":600.0000},{\"id\":\"370681199405028489\",\"co\":600.0000,\"odf\":0.0000,\"status\":\"overdue\",\"name\":\"花贝\",\"month\":\"201610\",\"base\":5000.0000,\"sum\":1200.0000,\"code\":\"gongjijin\",\"type\":\"beijinggongjijin\",\"in\":600.0000}],\"ordPaymentFee\":0.00}  ";
//        JSONObject res = JSON.parseObject(aaa);

        LOGGER.info("调用社保通返回结果:"+JSON.toJSONString(res));
        LOGGER.info("调用社保通提交代缴订单接口执行时间：" + (System.currentTimeMillis()-startDateQueryFirstExecute));

        if(null != res.getInteger("errcode")){//调用社保通接口返回错误
            throw new BusinessException(res.getString("msg"));
        }

        //如果有员工失败信息,则处理员工失败
        JSONArray fail = res.getJSONArray("fail");
        if(null != fail && fail.size()>0){//有异常数据
            //拼装FAIL
            long startDateQueryFirstAssemFail = System.currentTimeMillis();
            Map<Long,List<Integer>> dataCheckMap=assemErrorTextForEach( customerShebaoOrderBeens);
            ResultSheBaoDto resultFailDto=assemFailData( fail, id, dataCheckMap);
            LOGGER.info("有异常情况,返回拼装的失败信息集合:"+ JSON.toJSONString(resultFailDto));
            LOGGER.info("拼装FAIL执行时间：" + (System.currentTimeMillis()-startDateQueryFirstAssemFail));
            if(!StringUtils.isStrNull(type) && "resubmit".equals(type)){//继续提交,,正常的提交成功
                if(res.getJSONArray("success")!=null && res.getJSONArray("success").size()>0){
                    long startDateQueryFirstSubmit = System.currentTimeMillis();
                    //提交订单
                    CompanyShebaoOrderBean shebaoOrderBean=successSubmit(res,id, resultFailDto);
                    LOGGER.info("提交订单执行时间：" + (System.currentTimeMillis()-startDateQueryFirstSubmit));
                    resultResponse.setData(shebaoOrderBean);
                }else{
                    //插入异常信息
                    long startDateQueryFirstFail = System.currentTimeMillis();
                    storeErrorInfo(id, resultFailDto.getFailOrderList(),resultFailDto.getFailDescList(),1);
                    LOGGER.info("插入异常信息执行时间：" + (System.currentTimeMillis()-startDateQueryFirstFail));
                    throw new BusinessException("无正确信息的人员,请修改后提交");
                }
            }else{//不继续提交,将异常信息存储下来,并取消订单
                if(res.getJSONArray("success")!=null && res.getJSONArray("success").size()>0){//将成功的员工信息清空
                    List<String> successCardList=assemCardList(res.getJSONArray("success"));
                    List<CustomersBean> successCustomerList=null;
                    if(successCardList!=null && successCardList.size()>0){
                        long startDateQueryCustomerInfo = System.currentTimeMillis();
                        successCustomerList=customersReaderMapper.selectCustomersForeachByIdcard(successCardList);
                        LOGGER.info("查询成功员工信息执行时间：" + (System.currentTimeMillis()-startDateQueryCustomerInfo));
                    }
                    List<CustomerShebaoOrderBean> successCustomerOrderList=null;
                    if(successCustomerList!=null && successCustomerList.size()>0){
                        long startDateAssemOrder = System.currentTimeMillis();
                        successCustomerOrderList=assemCustomerOrderList(successCustomerList,res.getJSONArray("success"),id);//获取拼装的成功的员工的订单信息
                        LOGGER.info("获取拼装的成功的员工的订单信息执行时间：" + (System.currentTimeMillis()-startDateAssemOrder));
                    }
                    // 更改员工错误信息为空
                    long startDateUpdErrorText = System.currentTimeMillis();
                    updateCustomerDescErrorText(successCustomerOrderList,id);
//                    updateCustomerDescErrorTextForNull(successCustomerOrderList);
                    LOGGER.info("更改员工错误信息为空执行时间：" + (System.currentTimeMillis()-startDateUpdErrorText));
                    //更改订单的返回的错误消息为空
                    long startDateUpdResponseMsg = System.currentTimeMillis();
                    updateErrorMsgForNull(successCustomerOrderList);
                    LOGGER.info("更改订单的返回的错误消息为空执行时间：" + (System.currentTimeMillis()-startDateUpdResponseMsg));
                }
                //插入异常信息
                long startDateQueryFirstFail = System.currentTimeMillis();
                storeErrorInfo(id, resultFailDto.getFailOrderList(),resultFailDto.getFailDescList(),1);
                LOGGER.info("插入异常信息执行时间：" + (System.currentTimeMillis()-startDateQueryFirstFail));
                //取消订单
                long startDateQuerySecondCancel = System.currentTimeMillis();
                SbtResponse cancelResponse=sheBaoTong.orderManager(res.getString("orderid"), 0);
                JSONObject cancelRes = JSON.parseObject(cancelResponse.getData());
                LOGGER.info("取消订单执行时间：" + (System.currentTimeMillis()-startDateQuerySecondCancel));
                LOGGER.info("有异常情况,取消订单返回结果:"+ JSON.toJSONString(cancelRes));
                resultResponse.setMessage("fail");
                resultResponse.setSuccess(false);
                return resultResponse;

            }
        }else{//无异常,提交成功
            if(res.getJSONArray("success")!=null && res.getJSONArray("success").size()>0){
                long startDateQueryFirstSubmit = System.currentTimeMillis();
                //提交订单
                ResultSheBaoDto resultFailDto=new ResultSheBaoDto();
                CompanyShebaoOrderBean shebaoOrderBean=successSubmit(res,id,resultFailDto);
                LOGGER.info("提交订单执行时间：" + (System.currentTimeMillis()-startDateQueryFirstSubmit));

                resultResponse.setData(shebaoOrderBean);
            }else{
                throw new BusinessException("无正常的账单,请修改后再提交");
            }

        }

        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 提交社保通订单基础验证
     * @param id
     * @param type
     * @param companyId
     */
    private void validatorSubmitOrderBase(Long id, String type,Long companyId){
        if(id==null){
            throw new BusinessException("请传递参数");
        }
        if(companyId==null){
            throw new BusinessException("获取不到企业信息");
        }
        //没签约社保公积金协议不能提交账单
        ////签约类型：1代发协议 2垫发协议 3社保代缴协议',
        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectLastData(companyId, CompanyProtocolConstants.PROTOCOL_TYPE_DJ);
//        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectIsUserFulByTypeAndTime(companyId, CompanyProtocolConstants.PROTOCOL_TYPE_DJ, DateUtil.getCurrDateOfDate(DateUtil.dateString));
        if (companyProtocolsBean != null) {
            //协议状态:1待审批 2签约 3即将到期 4合约到期 5冻结
            if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 1) {
                throw new BusinessException("社保公积金代缴协议还未审批，不能进行社保公积金提交账单业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 4) {
                throw new BusinessException("社保公积金代缴协议合约已到期，不能进行社保公积金提交账单业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 5) {
                throw new BusinessException("社保公积金代缴协议合约已被冻结，不能进行社保公积金提交账单业务");
            }
        } else {
            throw new BusinessException("未签署社保公积金代缴协议，不能进行社保公积金提交账单业务");
        }
    }
    /**
     * 获取失败信息集合
     * @param fail
     * @param companyOrderId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private ResultSheBaoDto assemFailData(JSONArray fail,Long companyOrderId,Map<Long,List<Integer>> dataCheckMap)throws BusinessException,Exception{
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();
        //获取有异常信息的所有员工身份证
        List<String> failCardList=assemCardList(fail);

        List<CustomersBean> failCustomerList=new ArrayList<>();
        if(failCardList!=null && failCardList.size()>0){//获取失败人员的员工信息
            failCustomerList=customersReaderMapper.selectCustomersForeachByIdcard(failCardList);
        }
        //拼装的汇总数据信息
        List<CustomerShebaoOrderDescBean> descList=new ArrayList<>();
        //将基础错误信息放入订单中
        List<CustomerShebaoOrderBean> failList=new ArrayList<>();
        if(failCustomerList!=null && failCustomerList.size()>0){
            for(CustomersBean customerBean:failCustomerList){
                for(int i=0,len=fail.size(); i<len; i++){
                    JSONObject error = fail.getJSONObject(i);
                    String idCard = error.getString("id");
                    String status=StringUtils.isStrNull(error.getString("status"))?"":error.getString("status");
                    String code=StringUtils.isStrNull(error.getString("code"))?"":error.getString("code");
                    String month=StringUtils.isStrNull(error.getString("month"))?"":error.getString("month");
                    if(!StringUtils.isStrNull(idCard) && idCard.equals(customerBean.getCustomerIdcard())){
                        if("add".equals(status)){
                            status="增员";
                        }else if("overdue".equals(status)){
                            status="补缴";
                        }else if("keep".equals(status)){
                            status="续缴";
                        }else if("basechg".equals(status)){
                            status="调基";
                        }else if("stop".equals(status)){
                            status="停缴";
                        }
                        if("shebao".equals(code)){
                            code="社保";
                        }else if("gongjijin".equals(code)){
                            code="公积金";
                        }else if("info".equals(code)){
                            code="基础信息";
                        }
                        String errorReason=code+status+month+error.getString("msg");//失败原因
                        String errorText=status+month+"失败";//失败详情

                        //拼装汇总数据信息
                        boolean descFlag=false;//判断员工是否已经有错误信息,false代表没有
                        for(CustomerShebaoOrderDescBean descBean:descList){
                            if(descBean.getCustomerId().longValue()==customerBean.getCustomerId().longValue()){
                                descFlag=true;
                                descBean.setOrderErrorText((StringUtils.isStrNull(descBean.getOrderErrorText())?"":descBean.getOrderErrorText())+errorReason+";");
                                if(!StringUtils.isStrNull(code) && "社保".equals(code)){
                                    descBean.setSbErrorText((StringUtils.isStrNull(descBean.getSbErrorText())?"":descBean.getSbErrorText())+errorText+";");
                                    descBean.setSbErrorReason((StringUtils.isStrNull(descBean.getSbErrorReason())?"":descBean.getSbErrorReason())+errorReason+";");
                                }else if(!StringUtils.isStrNull(code) && "公积金".equals(code)){
                                    descBean.setGjjErrorText((StringUtils.isStrNull(descBean.getGjjErrorText())?"":descBean.getGjjErrorText())+errorText+";");
                                    descBean.setGjjErrorReason((StringUtils.isStrNull(descBean.getGjjErrorReason())?"":descBean.getGjjErrorReason())+errorReason+";");
                                }else
                                {
                                    if(dataCheckMap!=null && dataCheckMap.size()>0){
                                        List<Integer> checkList=dataCheckMap.get(descBean.getCustomerId());
                                        if(checkList!=null && checkList.size()>0){
                                            if(checkList.contains(1)){
                                                if(!StringUtils.isStrNull(status)){
                                                    descBean.setSbErrorText((StringUtils.isStrNull(descBean.getSbErrorText())?"":descBean.getSbErrorText())+errorText+";");
                                                }
                                                descBean.setSbErrorReason((StringUtils.isStrNull(descBean.getSbErrorReason())?"":descBean.getSbErrorReason())+errorReason+";");
                                            }
                                            if(checkList.contains(2)){
                                                if(!StringUtils.isStrNull(status)){
                                                    descBean.setGjjErrorText((StringUtils.isStrNull(descBean.getGjjErrorText())?"":descBean.getGjjErrorText())+errorText+";");
                                                }
                                                descBean.setGjjErrorReason((StringUtils.isStrNull(descBean.getGjjErrorReason())?"":descBean.getGjjErrorReason())+errorReason+";");
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if(!descFlag){
                            CustomerShebaoOrderDescBean orderDescBean=new CustomerShebaoOrderDescBean();
                            orderDescBean.setCustomerId(customerBean.getCustomerId());
                            orderDescBean.setOrderErrorText(errorReason+";");
                            orderDescBean.setCompanyShebaoOrderId(companyOrderId);
                            if(!StringUtils.isStrNull(code) && "社保".equals(code)){
                                orderDescBean.setSbErrorText(errorText+";");
                                orderDescBean.setSbErrorReason(errorReason+";");
                            }else if(!StringUtils.isStrNull(code) && "公积金".equals(code)){
                                orderDescBean.setGjjErrorText(errorText+";");
                                orderDescBean.setGjjErrorReason(errorReason+";");
                            }else{
                                if(dataCheckMap!=null && dataCheckMap.size()>0){
                                    List<Integer> checkList=dataCheckMap.get(customerBean.getCustomerId());
                                    if(checkList!=null && checkList.size()>0){
                                        if(checkList.contains(1)){
                                            if(!StringUtils.isStrNull(status)){
                                                orderDescBean.setSbErrorText(errorText+";");
                                            }
                                            orderDescBean.setSbErrorReason(errorReason+";");
                                        }
                                        if(checkList.contains(2)){
                                            if(!StringUtils.isStrNull(status)){
                                                orderDescBean.setGjjErrorText(errorText+";");
                                            }
                                            orderDescBean.setGjjErrorReason(errorReason+";");
                                        }
                                    }
                                }
                            }
                            descList.add(orderDescBean);
                        }

                        CustomerShebaoOrderBean failBean=new CustomerShebaoOrderBean();
                        failBean.setCustomerId(customerBean.getCustomerId());
                        failBean.setCompanyShebaoOrderId(companyOrderId);
                        failBean.setResponseMsg(errorReason);
                        if("社保".equals(code)){
                            failBean.setRequireType(ShebaoConstants.RELATION_TYPE_SHEBAO);
                        }else if("公积金".equals(code)){
                            failBean.setRequireType(ShebaoConstants.RELATION_TYPE_GONGJIJING);
                        }else{
                            failBean.setRequireType(ShebaoConstants.RELATION_TYPE_BASE);
                        }
                        if("增员".equals(status)){
                            failBean.setOrderType(ShebaoConstants.OrderType.ZY);
                        }else if("补缴".equals(status)){
                            failBean.setOrderType(ShebaoConstants.OrderType.BJ);
                        }else if("续缴".equals(status)){
                            failBean.setOrderType(ShebaoConstants.OrderType.HJ);
                        }else if("调基".equals(status)){
                            failBean.setOrderType(ShebaoConstants.OrderType.TJ);
                        }else if("停缴".equals(status)){
                            failBean.setOrderType(ShebaoConstants.OrderType.STOP);
                        }
                        if(!StringUtils.isStrNull(month)){
                            failBean.setOverdueMonth(DateUtil.dayFormatter.parse(month));
                        }
                        failList.add(failBean);
                    }
                }
            }

        }

        List<CustomerShebaoOrderBean> resultFailList=new ArrayList<>();
        //获取社保公积金订单信息
        CustomerShebaoOrderBean orderBean=new CustomerShebaoOrderBean();
        orderBean.setCompanyShebaoOrderId(companyOrderId);
        List<CustomerShebaoOrderBean> allList=customerShebaoOrderReaderMapper.selectShebaoOrderList(orderBean);

        if(allList!=null && allList.size()>0){//将基础信息存入订单信息
            //清空原来的错误信息
            for(CustomerShebaoOrderBean allBean:allList){
                allBean.setResponseMsg("");
            }
            for(CustomerShebaoOrderBean allBean:allList){
                if(failList!=null && failList.size()>0){
                    for(CustomerShebaoOrderBean failBean:failList){
                        if(failBean.getCustomerId()!=null && allBean.getCustomerId()!=null && failBean.getCustomerId().longValue()==allBean.getCustomerId().longValue()){
                            if(failBean.getRequireType()!=null && failBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                if(allBean.getRequireType()!=null && allBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO
                                        && allBean.getOrderType()!=null && failBean.getOrderType()!=null && allBean.getOrderType().intValue()==failBean.getOrderType().intValue()){
                                    if(failBean.getOrderType().intValue()==ShebaoConstants.OrderType.BJ){//补缴
                                        if(allBean.getOverdueMonth()!=null && failBean.getOverdueMonth()!=null && DateUtil.dayFormatter.format(allBean.getOverdueMonth()).equals(DateUtil.dayFormatter.format(failBean.getOverdueMonth()))){
                                            allBean.setResponseMsg((StringUtils.isStrNull(allBean.getResponseMsg())?"":allBean.getResponseMsg())+failBean.getResponseMsg()+";");
                                        }
                                    }else{
                                        allBean.setResponseMsg((StringUtils.isStrNull(allBean.getResponseMsg())?"":allBean.getResponseMsg())+failBean.getResponseMsg()+";");
                                    }
                                }
                            }else if(failBean.getRequireType()!=null && failBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                if(allBean.getRequireType()!=null && allBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING
                                        && allBean.getOrderType()!=null && failBean.getOrderType()!=null && allBean.getOrderType().intValue()==failBean.getOrderType().intValue()){
                                    if(failBean.getOrderType().intValue()==ShebaoConstants.OrderType.BJ){//补缴
                                        if(allBean.getOverdueMonth()!=null && failBean.getOverdueMonth()!=null && DateUtil.dayFormatter.format(allBean.getOverdueMonth()).equals(DateUtil.dayFormatter.format(failBean.getOverdueMonth()))){
                                            allBean.setResponseMsg((StringUtils.isStrNull(allBean.getResponseMsg())?"":allBean.getResponseMsg())+failBean.getResponseMsg()+";");
                                        }
                                    }else{
                                        allBean.setResponseMsg((StringUtils.isStrNull(allBean.getResponseMsg())?"":allBean.getResponseMsg())+failBean.getResponseMsg()+";");
                                    }
                                }
                            }else{
                                allBean.setResponseMsg((StringUtils.isStrNull(allBean.getResponseMsg())?"":allBean.getResponseMsg())+failBean.getResponseMsg()+";");
                            }
                        }
                    }
                    if(!StringUtils.isStrNull(allBean.getResponseMsg())){
                        resultFailList.add(allBean);
                    }
                }
            }
        }
        resultSheBaoDto.setFailDescList(descList);
        resultSheBaoDto.setFailOrderList(resultFailList);
        return resultSheBaoDto;
    }

    /**
     * 拼装数据,为了后面异常信息不把未提交需求的也算上
     * @param customerShebaoOrderBeens
     * @return
     */
    private Map<Long,List<Integer>> assemErrorTextForEach(List<CustomerShebaoOrderBean> customerShebaoOrderBeens){
        Map<Long,List<Integer>> resultMap=new HashMap<>();
        if(customerShebaoOrderBeens!=null && customerShebaoOrderBeens.size()>0){
            for(CustomerShebaoOrderBean orderBean:customerShebaoOrderBeens){
                if(orderBean.getCustomerId()!=null && orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                    if(resultMap.containsKey(orderBean.getCustomerId())){
                        resultMap.get(orderBean.getCustomerId()).add(1);
                    }else{
                        List<Integer> type=new ArrayList<>();
                        type.add(1);
                        resultMap.put(orderBean.getCustomerId(),type);
                    }
                }else if(orderBean.getCustomerId()!=null && orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                    if(resultMap.containsKey(orderBean.getCustomerId())){
                        resultMap.get(orderBean.getCustomerId()).add(2);
                    }else{
                        List<Integer> type=new ArrayList<>();
                        type.add(2);
                        resultMap.put(orderBean.getCustomerId(),type);
                    }
                }
            }
        }
        return resultMap;
    }
    /**
     * 拼装要调用提交代缴订单的社保通的参数
     * @param id
     * @return
     */
    private OrderPlace assemOrderPlaceForSubmitOrder(Long id,List<CustomerShebaoOrderBean> customerShebaoOrderBeens){

        //拼装企业订单下的所有员工的订单信息,为了后面增员时,增加社保的数据不用再查公积金的数据
        Map<Long,List<CustomerShebaoOrderBean>> customerOrderMap=assemCustomerOrderMap(customerShebaoOrderBeens);

        //拼装企业订单下的所有员工的信息,为了后面获取员工信息时,不用再查询
        Map<Long,CustomersBean> customerMap=assemCustomerBean(customerShebaoOrderBeens);
        if(customerMap==null || customerMap.size()<=0){
            throw new BusinessException("无社保公积金订单");
        }

        //社保通接口需要传递的封装的实例对象
        OrderPlace orderPlace = new OrderPlace();
        Config config = PropUtils.load("classpath:config/common.properties");
        String usercode = config.get("sbt.usercode");
        orderPlace.setUsercode(usercode);//唯一标识符

        JSONArray emps = new JSONArray();//报增人员的信息集合，若有报增人员，此集合为必需值
        JSONArray add = new JSONArray();//报增人员集合
        JSONArray keep = new JSONArray();//续缴人员集合
        JSONArray basechg = new JSONArray();//调基人员集合
        JSONArray stop = new JSONArray();//停缴人员集合
        JSONArray overdue = new JSONArray();//补缴人员集合

        // 缓存增员用户id
        Set<Long> zyCustomer = new HashSet<>();
        // 缓存补缴用户id
        Set<Long> bjCustomer = new HashSet<>();
        // 缓存调基用户id
        Set<Long> tjCustomer = new HashSet<>();
        // 缓存停缴用户id
        Set<Long> stopCustomer = new HashSet<>();

        //拼装各操作人员信息集合
        for(CustomerShebaoOrderBean shebaoOrderBean : customerShebaoOrderBeens){
            int orderType = shebaoOrderBean.getOrderType();//获取操作类型1增员，2汇缴，3调基，4,停缴, 5补缴
            int requireType = shebaoOrderBean.getRequireType();//1社保,2公积金
            long customerId = shebaoOrderBean.getCustomerId();//获取员工ID
            //获取员工信息
            CustomersBean customersBean = customerMap.get(customerId);
            if(customersBean==null){
                throw new BusinessException("获取不到员工ID为["+customerId+"]的员工信息");
            }
            if(orderType == ShebaoConstants.OrderType.ZY && !zyCustomer.contains(customerId)){// 增员
                //添加报增人员手机号\性别等信息
                emps.add(assemAddCustomerInfo(customersBean));
                //添加报增人员
                add.add(assemAddInfo(customersBean,shebaoOrderBean,customerOrderMap));
                zyCustomer.add(customerId);
            }else if(orderType == ShebaoConstants.OrderType.HJ){// 汇缴
                keep.add(assemOtherInfo(customersBean, orderType,requireType,shebaoOrderBean,customerOrderMap));
            }else if(orderType == ShebaoConstants.OrderType.TJ && !tjCustomer.contains(customerId)){// 调基
                basechg.add(assemOtherInfo(customersBean, orderType,requireType,shebaoOrderBean,customerOrderMap));
                tjCustomer.add(customerId);
            }else if(orderType == ShebaoConstants.OrderType.STOP && !stopCustomer.contains(customerId)){// 停缴
                stop.add(assemOtherInfo(customersBean, orderType,requireType,shebaoOrderBean,customerOrderMap));
                stopCustomer.add(customerId);
            }else if(orderType == ShebaoConstants.OrderType.BJ && !bjCustomer.contains(customerId)){// 补缴
                JSONObject bjObj=assemBjInfo(customersBean,customerOrderMap);
                if(bjObj!=null){
                    overdue.add(bjObj);
                    bjCustomer.add(customerId);
                }
            }
        }
        if(add!=null && add.size() > 0){
            orderPlace.setAdd(add);
        }
        if(keep!=null &&keep.size() > 0){
            orderPlace.setKeep(keep);
        }
        if(overdue!=null &&overdue.size() > 0){
            orderPlace.setOverdue(overdue);
        }
        if(stop!=null &&stop.size() > 0){
            orderPlace.setStop(stop);
        }
        if(basechg!=null &&basechg.size() > 0){
            orderPlace.setBasechg(basechg);
        }
        if(emps!=null &&emps.size() > 0){
            orderPlace.setEmps(emps);
        }

        if(orderPlace.getAdd()==null && orderPlace.getKeep()==null && orderPlace.getOverdue()==null && orderPlace.getStop()==null && orderPlace.getBasechg()==null){
            throw new BusinessException("该企业月份无社保公积金订单");
        }
        return orderPlace;
    }

    /**
     * 拼装企业订单下的所有员工的订单信息
     * @param customerShebaoOrderBeens
     * @return
     */
    private Map<Long,List<CustomerShebaoOrderBean>> assemCustomerOrderMap(List<CustomerShebaoOrderBean> customerShebaoOrderBeens){
        Map<Long,List<CustomerShebaoOrderBean>> orderMap=new HashMap<>();
        for(CustomerShebaoOrderBean bean:customerShebaoOrderBeens){
            if(orderMap.get(bean.getCustomerId())!=null){
                orderMap.get(bean.getCustomerId()).add(bean);
            }else{
                List<CustomerShebaoOrderBean> beanList=new ArrayList<>();
                beanList.add(bean);
                orderMap.put(bean.getCustomerId(),beanList);
            }
        }
        return orderMap;
    }
    /**
     * 获取企业订单下的所有员工信息并拼装
     * @param customerShebaoOrderBeens
     * @return
     */
    private Map<Long,CustomersBean> assemCustomerBean(List<CustomerShebaoOrderBean> customerShebaoOrderBeens){
        List<Long> customerIdList=new ArrayList<>();
        for(CustomerShebaoOrderBean bean:customerShebaoOrderBeens){
            if(bean.getCustomerId()!=null && !customerIdList.contains(bean.getCustomerId())){
                customerIdList.add(bean.getCustomerId());
            }
        }
        Map<Long,CustomersBean> customersMap=null;
        if(customerIdList!=null && customerIdList.size()>0){
            //根据员工ID批量获取员工信息
            List<CustomersBean> beanList=customersService.selectCustomersForeach(customerIdList);
            if(beanList!=null && beanList.size()>0){
                customersMap=new HashMap<>();
                for(CustomersBean bean:beanList){
                    if(StringUtils.isStrNull(bean.getCustomerIdcard())){
                        throw new BusinessException("员工["+bean.getCustomerId()+","+bean.getCustomerTurename()+"]的身份证号不能为空,请添加员工身份证号信息");
                    }
                    customersMap.put(bean.getCustomerId(),bean);
                }
            }
        }
        return customersMap;
    }

    /**
     * 拼装报增人员详细信息
     * @param customersBean
     * @return
     */
    private JSONObject assemAddCustomerInfo(CustomersBean customersBean){
        JSONObject emp = new JSONObject();
        emp.put("id", customersBean.getCustomerIdcard());//身份证号
        emp.put("name", customersBean.getCustomerTurename());//员工姓名
        emp.put("idtype", "idcard");//ID类型
        emp.put("phone", customersBean.getCustomerPhone());//手机号
        if(customersBean.getCustomerSex()!=null){
            if(customersBean.getCustomerSex().intValue()== CustomerConstants.CUSTOMER_SEX_WOMEN){
                emp.put("gender", "女");//性别
            }else if(customersBean.getCustomerSex().intValue()== CustomerConstants.CUSTOMER_SEX_MAN){
                emp.put("gender", "男");//性别
            }
        }
        if(!StringUtils.isStrNull(customersBean.getCustomerBirthdayMonth())){
            emp.put("birthday", customersBean.getCustomerBirthdayMonth());//生日
        }
        return emp;
    }

    /**
     * 拼装报增人员
     * @param customersBean
     * @param shebaoOrderBean
     * @param customerOrderMap
     * @return
     */
    private JSONObject assemAddInfo(CustomersBean customersBean,CustomerShebaoOrderBean shebaoOrderBean,Map<Long,List<CustomerShebaoOrderBean>> customerOrderMap){
        JSONObject zy = new JSONObject();
        zy.put("id", customersBean.getCustomerIdcard());
        zy.put("name", customersBean.getCustomerTurename());

        if(shebaoOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_SHEBAO){
            //添加报增人员的社保基数与类型
            zy.put("ins", new InsType(shebaoOrderBean.getShebaoType(), shebaoOrderBean.getOrderBase().intValue()));

            //获取对应的公积金信息
            List<CustomerShebaoOrderBean> orderList=customerOrderMap.get(customersBean.getCustomerId());
            if(orderList!=null && orderList.size()>0){
                for(CustomerShebaoOrderBean orderBean:orderList){
                    if(orderBean.getOrderType()!=null && orderBean.getOrderType().intValue()==ShebaoConstants.OrderType.ZY
                            &&orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                        zy.put("hf", new InsType(orderBean.getShebaoType(), orderBean.getOrderBase().intValue()));
                        break;
                    }
                }
            }
        }else if(shebaoOrderBean.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING){
            zy.put("hf", new InsType(shebaoOrderBean.getShebaoType(), shebaoOrderBean.getOrderBase().intValue()));
            //获取对应的社保信息
            List<CustomerShebaoOrderBean> orderList=customerOrderMap.get(customersBean.getCustomerId());
            if(orderList!=null && orderList.size()>0){
                for(CustomerShebaoOrderBean orderBean:orderList){
                    if(orderBean.getOrderType()!=null && orderBean.getOrderType().intValue()==ShebaoConstants.OrderType.ZY
                            &&orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                        zy.put("ins", new InsType(orderBean.getShebaoType(), orderBean.getOrderBase().intValue()));
                        break;
                    }
                }
            }
        }
        return zy;
    }

    /**
     * 拼装汇缴\调基\停缴
     * @param customersBean
     * @return
     */
    private JSONObject assemOtherInfo(CustomersBean customersBean, int orderType,int requireType,CustomerShebaoOrderBean shebaoOrderBean,Map<Long,List<CustomerShebaoOrderBean>> customerOrderMap){
        JSONObject other = new JSONObject();
        other.put("id", customersBean.getCustomerIdcard());//身份证号
        other.put("name", customersBean.getCustomerTurename());//员工姓名
        if(orderType==ShebaoConstants.OrderType.HJ){//汇缴
//            if(requireType == ShebaoConstants.RELATION_TYPE_SHEBAO){
//                hj.put("ins", new InsType(shebaoOrderBean.getShebaoType(), shebaoOrderBean.getOrderBase().intValue()));
//            }
//            if(requireType == ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                hj.put("hf", new InsType(shebaoOrderBean.getShebaoType(), shebaoOrderBean.getOrderBase().intValue()));
//            }
        }else if(orderType == ShebaoConstants.OrderType.TJ){// 调基
            if(requireType == ShebaoConstants.RELATION_TYPE_SHEBAO){
                other.put("insbase", shebaoOrderBean.getOrderBase().intValue());
                //获取对应的公积金信息
                List<CustomerShebaoOrderBean> orderList=customerOrderMap.get(customersBean.getCustomerId());
                if(orderList!=null && orderList.size()>0){
                    for(CustomerShebaoOrderBean orderBean:orderList){
                        if(orderBean.getOrderType()!=null && orderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ
                                &&orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                            other.put("hfbase", shebaoOrderBean.getOrderBase().intValue());
                            break;
                        }
                    }
                }
            }else if(requireType == ShebaoConstants.RELATION_TYPE_GONGJIJING){
                other.put("hfbase", shebaoOrderBean.getOrderBase().intValue());
                //获取对应的社保信息
                List<CustomerShebaoOrderBean> orderList=customerOrderMap.get(customersBean.getCustomerId());
                if(orderList!=null && orderList.size()>0){
                    for(CustomerShebaoOrderBean orderBean:orderList){
                        if(orderBean.getOrderType()!=null && orderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ
                                &&orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                            other.put("insbase", shebaoOrderBean.getOrderBase().intValue());
                            break;
                        }
                    }
                }
            }
        }else if(orderType == ShebaoConstants.OrderType.STOP){// 停缴
            other.put("ins", false);
            other.put("hf", false);
            if(requireType == ShebaoConstants.RELATION_TYPE_SHEBAO){
                other.put("ins", true);
                //获取对应的公积金信息
                List<CustomerShebaoOrderBean> orderList=customerOrderMap.get(customersBean.getCustomerId());
                if(orderList!=null && orderList.size()>0){
                    for(CustomerShebaoOrderBean orderBean:orderList){
                        if(orderBean.getOrderType()!=null && orderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ
                                &&orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                            other.put("hf", true);
                            break;
                        }
                    }
                }
            }else if(requireType == ShebaoConstants.RELATION_TYPE_GONGJIJING){
                other.put("hf", true);
                //获取对应的社保信息
                List<CustomerShebaoOrderBean> orderList=customerOrderMap.get(customersBean.getCustomerId());
                if(orderList!=null && orderList.size()>0){
                    for(CustomerShebaoOrderBean orderBean:orderList){
                        if(orderBean.getOrderType()!=null && orderBean.getOrderType().intValue()==ShebaoConstants.OrderType.STOP
                                &&orderBean.getRequireType()!=null && orderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                            other.put("ins", true);
                            break;
                        }
                    }
                }
            }
        }
        return other;
    }

    /**
     * 拼装补缴员工信息
     * @param customersBean
     * @param customerOrderMap
     * @return
     */
    private JSONObject assemBjInfo(CustomersBean customersBean,Map<Long,List<CustomerShebaoOrderBean>> customerOrderMap){
        //获取补缴信息
        List<CustomerShebaoOrderBean> allList = customerOrderMap.get(customersBean.getCustomerId());
        List<CustomerShebaoOrderBean> bjList=new ArrayList<>();
        if(allList!=null && allList.size()>0){
            for(CustomerShebaoOrderBean bean:allList){
                if(bean!=null && bean.getOrderType()!=null && bean.getOrderType().intValue()==ShebaoConstants.OrderType.BJ){
                    bjList.add(bean);
                }
            }
        }
        //遍历补缴信息
        if(null != bjList && bjList.size() > 0){
            JSONObject overdueEmp = new JSONObject();
            overdueEmp.put("id", customersBean.getCustomerIdcard());
            overdueEmp.put("name", customersBean.getCustomerTurename());
            JSONArray bjsb = new JSONArray();//补缴社保信息
            JSONArray bjgjj = new JSONArray();//补缴公积金信息
            for(CustomerShebaoOrderBean bj : bjList){
                String month = DateUtil.formatDateTime("yyyyMM", bj.getOverdueMonth());
                int base = bj.getOrderBase().intValue();
                JSONObject sb = new JSONObject();
                sb.put("month", month);
                sb.put("base", base);
                if(bj.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_SHEBAO){
                    bjsb.add(sb);
                }else if(bj.getRequireType().intValue() == ShebaoConstants.RELATION_TYPE_GONGJIJING){
                    bjgjj.add(sb);
                }
            }
            overdueEmp.put("ins", bjsb);
            overdueEmp.put("hf", bjgjj);
            return overdueEmp;
        }
        return null;
    }

    /**
     * 更改异常信息
     * @param failOrderList
     * @param failDescList
     */
    private void storeErrorInfo(Long companyOrderId,List<CustomerShebaoOrderBean> failOrderList,List<CustomerShebaoOrderDescBean> failDescList,int type){

        if(failDescList!=null && failDescList.size()>0){
            customerShebaoOrderDescWriterMapper.updateErrorTextBatchDetail(companyOrderId,failDescList);
        }
        if(failOrderList!=null && failOrderList.size()>0){
            for(CustomerShebaoOrderBean errorOrderBean:failOrderList){
                //更改失败人员订单的返回错误信息
                if(type==2){
                    errorOrderBean.setSbtStatus(ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_SUBMITEXCEPTION);
                }
            }
            customerShebaoOrderWriterMapper.updateErrorMsgBatch(failOrderList);
        }
    }
    /**
     * 成功提交订单
     * @param res
     * @param companyOrderId
     */
    private CompanyShebaoOrderBean successSubmit(JSONObject res,long companyOrderId,ResultSheBaoDto resultFailDto)throws BusinessException,Exception{
        JSONArray success = res.getJSONArray("success");
        List<String> successCardList=assemCardList(success);//成功员工身份证集合
        List<CustomersBean> successCustomerList=null;
        if(successCardList!=null && successCardList.size()>0){
            long startDateQueryCustomerInfo = System.currentTimeMillis();
            successCustomerList=customersReaderMapper.selectCustomersForeachByIdcard(successCardList);
            LOGGER.info("查询成功员工信息执行时间：" + (System.currentTimeMillis()-startDateQueryCustomerInfo));
        }
        List<CustomerShebaoOrderBean> successCustomerOrderList=null;
        if(successCustomerList!=null && successCustomerList.size()>0){
            long startDateAssemOrder = System.currentTimeMillis();
            successCustomerOrderList=assemCustomerOrderList(successCustomerList,success,companyOrderId);//获取拼装的成功的员工的订单信息
            LOGGER.info("获取拼装的成功的员工的订单信息执行时间：" + (System.currentTimeMillis()-startDateAssemOrder));
        }

        //1.更改成功人员的错误信息
        final CopyOnWriteArrayList<CustomerShebaoOrderBean>  customerOrderCopyList = new CopyOnWriteArrayList(successCustomerOrderList.toArray());
        final long companyOrderIdThread=companyOrderId;
        final ResultSheBaoDto resultFailDtoThread=resultFailDto;

        //3.更改员工补收补退信息
        //获取总的补差费用(个人补退的不算)
        long startDateAssemBu = System.currentTimeMillis();
        ResultSheBaoDto shebaoDto=updatePaymentAndRefundInfo( res, companyOrderId);
        LOGGER.info("拼装补收补退信息并获取补差费用执行时间：" + (System.currentTimeMillis()-startDateAssemBu));
        BigDecimal priceAddtion=shebaoDto.getPriceAddtion();
        //4.更改企业订单信息
        CompanyShebaoOrderBean record=new CompanyShebaoOrderBean();
        record.setShebaotongTotalAmount(res.getBigDecimal("ordsum"));
        record.setShebaotongServiceAmount(res.getBigDecimal("service"));
        record.setShebaotongReceiveAmount(res.getBigDecimal("ordPaymentFee"));
        record.setShebaotongBackAmount(res.getBigDecimal("ordRefundFee"));
        record.setId(companyOrderId);
        record.setPriceAddtion(priceAddtion);
        record.setStatus(ShebaoConstants.COMPANY_ORDER_WILLPAY);

        long startDateAmount = System.currentTimeMillis();
        ResultSheBaoDto znDto= queryZnAmountList( success,successCustomerList, companyOrderId, record);
        LOGGER.info("获取滞纳费,社保费等执行时间：" + (System.currentTimeMillis()-startDateAmount));
        record=znDto.getCompanyShebaoOrderBean();
        record.setShebaotongServiceNumber(res.getString("orderid"));//社保通订单编号
        record.setShebaotongReturnMessage(JSON.toJSONString(res));//社保通返回数据


        //更改成功订单的返回的错误消息为空(必须先清空,因为后面的添加错误信息,有可能只有社保或者公积金一个有错)
        long startDateUptResponseMsg = System.currentTimeMillis();
        updateErrorMsgForNull(customerOrderCopyList);
        LOGGER.info("更改成功订单的返回的错误消息为空执行时间：" + (System.currentTimeMillis()-startDateUptResponseMsg));
        //更新员工订单的社保通状态(这儿不能用线程,是因为后面要更新企业订单的信息,就是根据这个状态更新的,所以这儿必须要同步)
        long startDateResponseMsg2 = System.currentTimeMillis();
        updateCustomerState(resultFailDto.getFailOrderList());
        LOGGER.info("更改失败人员订单的返回错误信息执行时间：" + (System.currentTimeMillis()-startDateResponseMsg2));
//        if(resultFailDto.getFailOrderList()!=null && resultFailDto.getFailOrderList().size()>0){
//            long startDateResponseMsg2 = System.currentTimeMillis();
//            for(CustomerShebaoOrderBean errorOrderBean:resultFailDto.getFailOrderList()){
//                //更改失败人员订单的返回错误信息
//                errorOrderBean.setSbtStatus(ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_FAIL);
//                customerShebaoOrderWriterMapper.updateErrorMsg(errorOrderBean);
//            }
//            LOGGER.info("更改失败人员订单的返回错误信息执行时间：" + (System.currentTimeMillis()-startDateResponseMsg2));
//        }
        //更新员工订单的企业金额与员工金额
        long startUptOrderAmount = System.currentTimeMillis();
        //拼装数据
        ResultSheBaoDto successOrderDto=assemSuccessOrderAmountData(successCustomerList, success, companyOrderId);
        //更新数据
        if(successOrderDto!=null && successOrderDto.getFailOrderList()!=null && successOrderDto.getFailOrderList().size()>0){
            customerShebaoOrderWriterMapper.updateShebaotongPayBatch(successOrderDto.getFailOrderList());
        }
        LOGGER.info("更新员工订单的企业金额与员工金额执行时间：" + (System.currentTimeMillis()-startUptOrderAmount));
        final ResultSheBaoDto znDtoThread=znDto;
        //更新滞纳费
        Thread t5 = new Thread() {
            public void run() {
                long startDate5 = System.currentTimeMillis();
                if(znDtoThread.getFailOrderList()!=null && znDtoThread.getFailOrderList().size()>0){
                    CopyOnWriteArrayList<CustomerShebaoOrderBean>  znOrderListThread = new CopyOnWriteArrayList(znDtoThread.getFailOrderList().toArray());
                    if(znOrderListThread!=null && znOrderListThread.size()>0){
                        for(CustomerShebaoOrderBean znBean:znOrderListThread){
                            customerShebaoOrderWriterMapper.updateOverdueByCondition(znBean);
                        }
                    }
                }
                LOGGER.info("更新滞纳费执行时间：" + (System.currentTimeMillis()-startDate5));
            }
        };
        t5.start();

        //更新错误信息
        Thread t1 = new Thread() {
            public void run() {
                long startDate1 = System.currentTimeMillis();
                // 更改成功员工错误信息为空
                updateCustomerDescErrorText(customerOrderCopyList,companyOrderIdThread);
                LOGGER.info("更改成功人员的错误信息为空执行时间：" + (System.currentTimeMillis()-startDate1));
                long startDate2 = System.currentTimeMillis();
                CopyOnWriteArrayList<CustomerShebaoOrderDescBean>  descCopyList=null;
                if(resultFailDtoThread.getFailDescList()!=null && resultFailDtoThread.getFailDescList().size()>0){
                    //失败人员汇总数据集合
                    descCopyList = new CopyOnWriteArrayList(resultFailDtoThread.getFailDescList().toArray());
                }
                storeErrorInfo(companyOrderIdThread,null,descCopyList,2);//(订单失败更改移到外面同步)
                LOGGER.info("更改失败人员的错误信息执行时间：" + (System.currentTimeMillis()-startDate2));
            }
        };
        t1.start();

        List<CustomersSupplementBean> supplementList=shebaoDto.getSupplementList();
        //更新补收补退信息
        if(supplementList!=null && supplementList.size()>0){
            //插入之前先清空补收补退信息
            final CopyOnWriteArrayList<CustomersSupplementBean>  supplementListThread = new CopyOnWriteArrayList(supplementList.toArray());
            Thread t = new Thread() {
                public void run() {
                    long startDate5 = System.currentTimeMillis();
                    if(supplementListThread!=null && supplementListThread.size()>0){
                        customersSupplementWriterMapper.deleteByCompanyOrderId(companyOrderIdThread);
                        customersSupplementWriterMapper.insertBatch(supplementListThread);
                    }
                    LOGGER.info("添加补收补退信息执行时间：" + (System.currentTimeMillis()-startDate5));
                }
            };
            t.start();
        }
        return record;
    }

    /**
     * 拼装成功的数据
     * @param successCustomerList
     * @param success
     * @param companyOrderId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private ResultSheBaoDto assemSuccessOrderAmountData(List<CustomersBean> successCustomerList,JSONArray success,Long companyOrderId)throws BusinessException,Exception{
        BigDecimal zeroDecimal=new BigDecimal("0");
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();
        List<CustomerShebaoOrderBean> successList=new ArrayList<>();
        if(successCustomerList!=null && successCustomerList.size()>0){
            for(CustomersBean customerBean:successCustomerList){
                for(int i=0,len=success.size(); i<len; i++){
                    JSONObject successObj = success.getJSONObject(i);
                    String idCard = successObj.getString("id");
                    String status=StringUtils.isStrNull(successObj.getString("status"))?"":successObj.getString("status");
                    String code=StringUtils.isStrNull(successObj.getString("code"))?"":successObj.getString("code");
                    String month=StringUtils.isStrNull(successObj.getString("month"))?"":successObj.getString("month");
                    BigDecimal co=StringUtils.isStrNull(successObj.getString("co")) || "-".equals(successObj.getString("co"))?zeroDecimal:new BigDecimal(successObj.getString("co"));
                    BigDecimal in=StringUtils.isStrNull(successObj.getString("in")) || "-".equals(successObj.getString("in"))?zeroDecimal:new BigDecimal(successObj.getString("in"));
                    if(!StringUtils.isStrNull(idCard) && idCard.equals(customerBean.getCustomerIdcard())){
                        CustomerShebaoOrderBean successBean=new CustomerShebaoOrderBean();
                        successBean.setCustomerId(customerBean.getCustomerId());
                        successBean.setCompanyShebaoOrderId(companyOrderId);
                        successBean.setShebaotongOrgPay(co);
                        successBean.setShebaotongEmpPay(in);
                        if("shebao".equals(code)){
                            successBean.setRequireType(ShebaoConstants.RELATION_TYPE_SHEBAO);
                        }else if("gongjijin".equals(code)){
                            successBean.setRequireType(ShebaoConstants.RELATION_TYPE_GONGJIJING);
                        }else if("info".equals(code)){
                            successBean.setRequireType(ShebaoConstants.RELATION_TYPE_BASE);
                        }
                        if("add".equals(status)){
                            successBean.setOrderType(ShebaoConstants.OrderType.ZY);
                        }else if("overdue".equals(status)){
                            successBean.setOrderType(ShebaoConstants.OrderType.BJ);
                        }else if("keep".equals(status)){
                            successBean.setOrderType(ShebaoConstants.OrderType.HJ);
                        }else if("basechg".equals(status)){
                            successBean.setOrderType(ShebaoConstants.OrderType.TJ);
                        }else if("stop".equals(status)){
                            successBean.setOrderType(ShebaoConstants.OrderType.STOP);
                        }
                        if(!StringUtils.isStrNull(month)){
                            successBean.setOverdueMonth(DateUtil.dayFormatter.parse(month));
                        }
                        successList.add(successBean);
                    }
                }
            }
        }
        List<CustomerShebaoOrderBean> resultSuccessList=new ArrayList<>();
        //获取社保公积金订单信息
        CustomerShebaoOrderBean orderBean=new CustomerShebaoOrderBean();
        orderBean.setCompanyShebaoOrderId(companyOrderId);
        List<CustomerShebaoOrderBean> allList=customerShebaoOrderReaderMapper.selectShebaoOrderList(orderBean);
        if(allList!=null && allList.size()>0){//将基础信息存入订单信息
            //清空原来的错误信息
            for(CustomerShebaoOrderBean allBean:allList){
                allBean.setShebaotongOrgPay(zeroDecimal);
                allBean.setShebaotongEmpPay(zeroDecimal);
            }
            for(CustomerShebaoOrderBean allBean:allList){
                if(successList!=null && successList.size()>0){
                    for(CustomerShebaoOrderBean successBean:successList){
                        if(successBean.getCustomerId()!=null && allBean.getCustomerId()!=null && successBean.getCustomerId().longValue()==allBean.getCustomerId().longValue()){
                            if(successBean.getRequireType()!=null && successBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
                                if(allBean.getRequireType()!=null && allBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO
                                        && allBean.getOrderType()!=null && successBean.getOrderType()!=null && allBean.getOrderType().intValue()==successBean.getOrderType().intValue()){
                                    if(successBean.getOrderType().intValue()==ShebaoConstants.OrderType.BJ){//补缴
                                        if(allBean.getOverdueMonth()!=null && successBean.getOverdueMonth()!=null && DateUtil.dayFormatter.format(allBean.getOverdueMonth()).equals(DateUtil.dayFormatter.format(successBean.getOverdueMonth()))){
                                            allBean.setShebaotongOrgPay(allBean.getShebaotongOrgPay().add(successBean.getShebaotongOrgPay()));
                                            allBean.setShebaotongEmpPay(allBean.getShebaotongEmpPay().add(successBean.getShebaotongEmpPay()));
                                        }
                                    }else{
                                        allBean.setShebaotongOrgPay(allBean.getShebaotongOrgPay().add(successBean.getShebaotongOrgPay()));
                                        allBean.setShebaotongEmpPay(allBean.getShebaotongEmpPay().add(successBean.getShebaotongEmpPay()));
                                    }
                                }
                            }else if(successBean.getRequireType()!=null && successBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
                                if(allBean.getRequireType()!=null && allBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING
                                        && allBean.getOrderType()!=null && successBean.getOrderType()!=null && allBean.getOrderType().intValue()==successBean.getOrderType().intValue()){
                                    if(successBean.getOrderType().intValue()==ShebaoConstants.OrderType.BJ){//补缴
                                        if(allBean.getOverdueMonth()!=null && successBean.getOverdueMonth()!=null && DateUtil.dayFormatter.format(allBean.getOverdueMonth()).equals(DateUtil.dayFormatter.format(successBean.getOverdueMonth()))){
                                            allBean.setShebaotongOrgPay(allBean.getShebaotongOrgPay().add(successBean.getShebaotongOrgPay()));
                                            allBean.setShebaotongEmpPay(allBean.getShebaotongEmpPay().add(successBean.getShebaotongEmpPay()));
                                        }
                                    }else{
                                        allBean.setShebaotongOrgPay(allBean.getShebaotongOrgPay().add(successBean.getShebaotongOrgPay()));
                                        allBean.setShebaotongEmpPay(allBean.getShebaotongEmpPay().add(successBean.getShebaotongEmpPay()));
                                    }
                                }
                            }else{
                                allBean.setShebaotongOrgPay(allBean.getShebaotongOrgPay().add(successBean.getShebaotongOrgPay()));
                                allBean.setShebaotongEmpPay(allBean.getShebaotongEmpPay().add(successBean.getShebaotongEmpPay()));
                            }
                        }
                    }
                    if(allBean.getShebaotongOrgPay().compareTo(zeroDecimal)!=0 || allBean.getShebaotongEmpPay().compareTo(zeroDecimal)!=0){
                        resultSuccessList.add(allBean);
                    }
                }
            }
        }
        resultSheBaoDto.setFailOrderList(resultSuccessList);
        return resultSheBaoDto;
    }
    /**
     * 获取滞纳费并获取要变更的滞纳费的列表
     * @param success
     * @param successCustomerList
     * @param companyOrderId
     * @param record
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private ResultSheBaoDto queryZnAmountList(JSONArray success,List<CustomersBean> successCustomerList,Long companyOrderId,CompanyShebaoOrderBean record)throws BusinessException,Exception{
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();
        BigDecimal overdueAmount=new BigDecimal("0");//滞纳费
        BigDecimal shebaoAmount=new BigDecimal("0");//社保费用
        BigDecimal gjjAmount=new BigDecimal("0");//公积金费用
        BigDecimal compareBigDecimal=new BigDecimal("0");
        List<CustomerShebaoOrderBean> znList=new ArrayList<>();
        for (int i = 0, len = success.size(); i < len; i++) {
            JSONObject successObj = success.getJSONObject(i);
            String idCard = successObj.getString("id");
            String code = successObj.getString("code");
            String month = successObj.getString("month");
            String co=StringUtils.isStrNull(successObj.getString("co"))?"0":successObj.getString("co");
            String in=StringUtils.isStrNull(successObj.getString("in"))?"0":successObj.getString("in");
            String odf=StringUtils.isStrNull(successObj.getString("odf"))?"0":successObj.getString("odf");
            overdueAmount=overdueAmount.add(new BigDecimal(odf));
            if(!StringUtils.isStrNull(code) && "shebao".equals(code)){
                shebaoAmount=shebaoAmount.add(new BigDecimal(co)).add(new BigDecimal(in));
            }else if(!StringUtils.isStrNull(code) && "gongjijin".equals(code)){
                gjjAmount=gjjAmount.add(new BigDecimal(co)).add(new BigDecimal(in));
            }
            //更新补缴的滞纳费
            if(!StringUtils.isStrNull(odf) && compareBigDecimal.compareTo(new BigDecimal(odf))!=0){
                if(successCustomerList!=null && successCustomerList.size()>0){
                    for(CustomersBean znCustomerBean:successCustomerList){
                        if(!StringUtils.isStrNull(idCard) && !StringUtils.isStrNull(znCustomerBean.getCustomerIdcard()) && idCard.equals(znCustomerBean.getCustomerIdcard())
                                &&  !StringUtils.isStrNull(code) && !StringUtils.isStrNull(month)){
                            CustomerShebaoOrderBean updateOrderBean=new CustomerShebaoOrderBean();
                            updateOrderBean.setCustomerId(znCustomerBean.getCustomerId());
                            updateOrderBean.setCompanyShebaoOrderId(companyOrderId);
                            if(!StringUtils.isStrNull(code) && "shebao".equals(code)){
                                updateOrderBean.setRequireType(ShebaoConstants.RELATION_TYPE_SHEBAO);
                            }else if(!StringUtils.isStrNull(code) && "gongjijin".equals(code)){
                                updateOrderBean.setRequireType(ShebaoConstants.RELATION_TYPE_GONGJIJING);
                            }
                            updateOrderBean.setOrderType(ShebaoConstants.OrderType.BJ);
                            updateOrderBean.setOverdueMonth(DateUtil.dayFormatter.parse(month));
                            updateOrderBean.setShebaoOrderOverdue(new BigDecimal(odf));
                            znList.add(updateOrderBean);
//                            customerShebaoOrderWriterMapper.updateOverdueByCondition(updateOrderBean);
                        }
                    }
                }
            }
        }
        record.setShebaotongOverdueAmount(overdueAmount);//滞纳费
        record.setShebaotongShebaoAmount(shebaoAmount);//社保费用
        record.setShebaotongGjjAmount(gjjAmount);//公积金费用
        resultSheBaoDto.setCompanyShebaoOrderBean(record);
        resultSheBaoDto.setFailOrderList(znList);
        return  resultSheBaoDto;
    }
    /**
     * 更新返回消息为空
     * @param successCustomerOrderList
     */
    private void updateErrorMsgForNull(List<CustomerShebaoOrderBean> successCustomerOrderList)throws BusinessException,Exception{
//        if (successCustomerOrderList!=null && successCustomerOrderList.size()>0) {
//            CopyOnWriteArrayList<CustomerShebaoOrderBean>  successCustomerListThread = new CopyOnWriteArrayList(successCustomerOrderList.toArray());
//            final List<Exception> errorList = new ArrayList();
//            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
//            int size = successCustomerOrderList.size();
//            ExecutorService pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
//            for (final CustomerShebaoOrderBean customerShebaoOrderBean : successCustomerListThread) {
//                rowResult.add(pool.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            customerShebaoOrderWriterMapper.updateErrorMsgForNull(customerShebaoOrderBean);
//                        } catch (Exception e) {
//                            //添加一次信息
//                            errorList.add(e);
//                        }
//                    }
//                }));
//
//            }
//            //等待处理结果
//            for (Future f : rowResult) {
//                f.get();
//            }
//            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
//            pool.shutdown();
//            //出现异常
//            if (!errorList.isEmpty()) {
//                //抛出异常信息
//                throw new BusinessException(errorList.get(0).getMessage());
//            }
//        }
//        if(successCustomerOrderList!=null && successCustomerOrderList.size()>0){
//            for(CustomerShebaoOrderBean updateOrderBean:successCustomerOrderList){
//                customerShebaoOrderWriterMapper.updateErrorMsgForNull(updateOrderBean);
//            }
//        }
        if(successCustomerOrderList!=null && successCustomerOrderList.size()>0){
            customerShebaoOrderWriterMapper.updateErrorMsgForNullBatch(successCustomerOrderList);
        }
    }

    /**
     * 更新返回消息为空
     * @param failCustomerOrderList
     */
    private void updateCustomerState(List<CustomerShebaoOrderBean> failCustomerOrderList)throws BusinessException,Exception{
//        if (failCustomerOrderList!=null && failCustomerOrderList.size()>0) {
//            CopyOnWriteArrayList<CustomerShebaoOrderBean>  failCustomerListThread = new CopyOnWriteArrayList(failCustomerOrderList.toArray());
//            final List<Exception> errorList = new ArrayList();
//            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
//            int size = failCustomerListThread.size();
//            ExecutorService pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
//            for (final CustomerShebaoOrderBean customerShebaoOrderBean : failCustomerListThread) {
//                rowResult.add(pool.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            //更改失败人员订单的返回错误信息
//                            customerShebaoOrderBean.setSbtStatus(ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_FAIL);
//                            customerShebaoOrderWriterMapper.updateErrorMsg(customerShebaoOrderBean);
//                        } catch (Exception e) {
//                            //添加一次信息
//                            errorList.add(e);
//                        }
//                    }
//                }));
//            }
//            //等待处理结果
//            for (Future f : rowResult) {
//                f.get();
//            }
//            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
//            pool.shutdown();
//            //出现异常
//            if (!errorList.isEmpty()) {
//                //抛出异常信息
//                throw new BusinessException(errorList.get(0).getMessage());
//            }
//        }
        if(failCustomerOrderList!=null && failCustomerOrderList.size()>0){
            for(CustomerShebaoOrderBean customerShebaoOrderBean:failCustomerOrderList){
                customerShebaoOrderBean.setSbtStatus(ShebaoConstants.CUSTOMER_ORDER_SBTSTATE_SUBMITEXCEPTION);
            }
            customerShebaoOrderWriterMapper.updateErrorMsgBatch(failCustomerOrderList);
        }

    }

    /**
     * 更新补收补退信息
     * @param res
     * @param companyOrderId
     * @return
     */
    private ResultSheBaoDto updatePaymentAndRefundInfo(JSONObject res,long companyOrderId){
        ResultSheBaoDto resultSheBaoDto=new ResultSheBaoDto();

        BigDecimal zeroBigDecimal=new BigDecimal("0");
        BigDecimal ordPaymentFee=res.getString("ordPaymentFee")!=null?new BigDecimal(res.getString("ordPaymentFee")):zeroBigDecimal;
        BigDecimal ordRefundFee=res.getString("ordRefundFee")!=null?new BigDecimal(res.getString("ordRefundFee")):zeroBigDecimal;
        JSONArray paymentFeeDtl = res.getJSONArray("paymentFeeDtl");//补收
        JSONArray refundFeeDtl = res.getJSONArray("refundFeeDtl");//补退
        List<String> paymentCardList=assemCardList(paymentFeeDtl);
        List<String> refundCardList=assemCardList(refundFeeDtl);
        List<CustomersBean> paymentCustomerList=null;
        if(paymentCardList!=null && paymentCardList.size()>0){
            paymentCustomerList=customersReaderMapper.selectCustomersForeachByIdcard(paymentCardList);
        }
        List<CustomersBean> refundCustomerList=null;
        if(refundCardList!=null && refundCardList.size()>0){
            refundCustomerList=customersReaderMapper.selectCustomersForeachByIdcard(refundCardList);
        }
        List<CustomersSupplementBean> supplementList=new ArrayList<>();
        BigDecimal personSelfBackAmount=new BigDecimal("0");
        personSelfBackAmount=updateReceiveAndBackInfo(paymentCustomerList,paymentFeeDtl,supplementList,companyOrderId,1,personSelfBackAmount);
        personSelfBackAmount=updateReceiveAndBackInfo(refundCustomerList,refundFeeDtl,supplementList,companyOrderId,2,personSelfBackAmount);
        BigDecimal priceAddtion=ordPaymentFee.subtract(ordRefundFee.subtract(personSelfBackAmount));
        resultSheBaoDto.setPriceAddtion(priceAddtion);
        resultSheBaoDto.setSupplementList(supplementList);
        return resultSheBaoDto;
    }
    /**
     * 将提交成功的订单的错误信息清空
     * @param successCustomerOrderList
     */
    private void updateCustomerDescErrorText(List<CustomerShebaoOrderBean> successCustomerOrderList,Long companyOrderId){
        if(successCustomerOrderList!=null && successCustomerOrderList.size()>0){
            List<Long> shebaoListForNull=new ArrayList();
            List<Long> gjjListForNull=new ArrayList();
            for(CustomerShebaoOrderBean customerShebaoOrderBean:successCustomerOrderList){
                if(customerShebaoOrderBean.getCustomerId()!=null && customerShebaoOrderBean.getCompanyShebaoOrderId()!=null){
                    if(customerShebaoOrderBean.getRequireType()!=null && customerShebaoOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
//                        customerShebaoOrderDescWriterMapper.updateErrorInfoEmpty(1,customerShebaoOrderBean.getCompanyShebaoOrderId(),customerShebaoOrderBean.getCustomerId());
                        shebaoListForNull.add(customerShebaoOrderBean.getCustomerId());
                    }else if(customerShebaoOrderBean.getRequireType()!=null && customerShebaoOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                        customerShebaoOrderDescWriterMapper.updateErrorInfoEmpty(2,customerShebaoOrderBean.getCompanyShebaoOrderId(),customerShebaoOrderBean.getCustomerId());
                        gjjListForNull.add(customerShebaoOrderBean.getCustomerId());
                    }
                }
            }
            if(shebaoListForNull!=null && shebaoListForNull.size()>0){
                customerShebaoOrderDescWriterMapper.updateErrorInfoEmptyBatch(1,companyOrderId,shebaoListForNull);
            }
            if(gjjListForNull!=null && gjjListForNull.size()>0){
                customerShebaoOrderDescWriterMapper.updateErrorInfoEmptyBatch(2,companyOrderId,gjjListForNull);
            }
        }
    }

//    /**
//     * 将提交成功的订单的错误信息清空
//     * @param successCustomerOrderList
//     * @throws BusinessException
//     * @throws Exception
//     */
//    private void updateCustomerDescErrorTextForNull(List<CustomerShebaoOrderBean> successCustomerOrderList)throws BusinessException,Exception{
//
//        if (successCustomerOrderList!=null && successCustomerOrderList.size()>0) {
//            CopyOnWriteArrayList<CustomerShebaoOrderBean>  successCustomerListThread = new CopyOnWriteArrayList(successCustomerOrderList.toArray());
//            final List<Exception> errorList = new ArrayList();
//            List<Future> rowResult = new CopyOnWriteArrayList<Future>();
//            int size = successCustomerListThread.size();
//            ExecutorService pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
//            for (final CustomerShebaoOrderBean customerShebaoOrderBean : successCustomerListThread) {
//                rowResult.add(pool.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            if(customerShebaoOrderBean.getCustomerId()!=null && customerShebaoOrderBean.getCompanyShebaoOrderId()!=null){
//                                if(customerShebaoOrderBean.getRequireType()!=null && customerShebaoOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO){
//                                    customerShebaoOrderDescWriterMapper.updateErrorInfoEmpty(1,customerShebaoOrderBean.getCompanyShebaoOrderId(),customerShebaoOrderBean.getCustomerId());
//                                }else if(customerShebaoOrderBean.getRequireType()!=null && customerShebaoOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING){
//                                    customerShebaoOrderDescWriterMapper.updateErrorInfoEmpty(2,customerShebaoOrderBean.getCompanyShebaoOrderId(),customerShebaoOrderBean.getCustomerId());
//                                }
//                            }
//                        } catch (Exception e) {
//                            //添加一次信息
//                            errorList.add(e);
//                        }
//                    }
//                }));
//            }
//            //等待处理结果
//            for (Future f : rowResult) {
//                f.get();
//            }
//            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
//            pool.shutdown();
//            //出现异常
//            if (!errorList.isEmpty()) {
//                //抛出异常信息
//                throw new BusinessException(errorList.get(0).getMessage());
//            }
//        }
//    }

    /**
     * 获取身份证LIST
     * @param jsonArray
     * @return
     */
    private List<String> assemCardList(JSONArray jsonArray){
        List<String> idCardList=new ArrayList<>();
        if(jsonArray!=null && jsonArray.size()>0){
            for(int i=0,len=jsonArray.size(); i<len; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                String idCard = obj.getString("id");
                if(!StringUtils.isStrNull(idCard)){
                    if(!idCardList.contains(idCard)){
                        idCardList.add(idCard);
                    }
                }
            }
        }
        return idCardList;
    }

    /**
     * 拼装员工订单信息
     * @param successCustomerList
     * @param success
     * @param companyOrderId
     * @return
     */
    private List<CustomerShebaoOrderBean> assemCustomerOrderList(List<CustomersBean> successCustomerList,JSONArray success,long companyOrderId){
        List<CustomerShebaoOrderBean> successCustomerOrderList=new ArrayList<>();
        if(successCustomerList!=null && successCustomerList.size()>0) {
            for (CustomersBean customerBean : successCustomerList) {
                for (int i = 0, len = success.size(); i < len; i++) {
                    JSONObject successObj = success.getJSONObject(i);
                    String idCard = successObj.getString("id");
                    String status=successObj.getString("status");
                    String code=successObj.getString("code");
                    String month=successObj.getString("month");
                    if (!StringUtils.isStrNull(idCard) && idCard.equals(customerBean.getCustomerIdcard())) {
                        CustomerShebaoOrderBean orderBean=new CustomerShebaoOrderBean();
                        orderBean.setCustomerId(customerBean.getCustomerId());
                        orderBean.setCompanyShebaoOrderId(companyOrderId);
                        if(StringUtils.isStrNull(code) || "info".equals(code)){
                            continue;
                        }
                        if("shebao".equals(code)){
                            orderBean.setRequireType(ShebaoConstants.RELATION_TYPE_SHEBAO);
                        }else if("gongjijin".equals(code)){
                            orderBean.setRequireType(ShebaoConstants.RELATION_TYPE_GONGJIJING);
                        }
                        if("add".equals(status)){
                            orderBean.setOrderType(ShebaoConstants.OrderType.ZY);
                        }else if("overdue".equals(status)){
                            orderBean.setOrderType(ShebaoConstants.OrderType.BJ);
                            if(!StringUtils.isStrNull(month)){
                                orderBean.setOverdueMonth(DateUtil.stringToDate(month,"yyyyMM"));
                            }
                        }else if("keep".equals(status)){
                            orderBean.setOrderType(ShebaoConstants.OrderType.HJ);
                        }else if("basechg".equals(status)){
                            orderBean.setOrderType(ShebaoConstants.OrderType.TJ);
                        }else if("stop".equals(status)){
                            orderBean.setOrderType(ShebaoConstants.OrderType.STOP);
                        }
                        successCustomerOrderList.add(orderBean);
                    }
                }
            }
        }
        return successCustomerOrderList;
    }

    /**
     * 更改员工社保公积金状态
     * @param successCustomerOrderList
     * @param flag
     */
    private void updateCustomerOrderState(List<CustomerShebaoOrderBean> successCustomerOrderList,int flag){
        if(successCustomerOrderList!=null && successCustomerOrderList.size()>0){
            for(CustomerShebaoOrderBean customerOrderBean:successCustomerOrderList){
                CustomerShebaoBean record=new CustomerShebaoBean();
                record.setCustomerId(customerOrderBean.getCustomerId());
                record.setCurrentCompanyOrderId(customerOrderBean.getCompanyShebaoOrderId());
                if(flag==1){//更改成功员工的状态
                    if(customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.ZY
                            ||customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.HJ
                            ||customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ){
                        record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_ING);
                        record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_ING);
                    }else if(customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.STOP){
                        record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_STOP);
                        record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_STOP);
                    }
                }else if(flag==2){//更改失败员工的状态
                    if(customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.ZY
                            ||customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.HJ
                            ||customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.TJ
                            ||customerOrderBean.getOrderType().intValue()==ShebaoConstants.OrderType.STOP){
                        record.setSbShebaotongStatus(ShebaoConstants.SBT_STATE_FAIL);
                        record.setGjjShebaotongStatus(ShebaoConstants.SBT_STATE_FAIL);
                    }
                }
                if(customerOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_SHEBAO && customerOrderBean.getOrderType()!=null && customerOrderBean.getOrderType().intValue()!=ShebaoConstants.OrderType.BJ){
                    customerShebaoWriterMapper.updateSbAndGjjStateByCondition(1,record);
                }else if(customerOrderBean.getRequireType().intValue()==ShebaoConstants.RELATION_TYPE_GONGJIJING && customerOrderBean.getOrderType()!=null && customerOrderBean.getOrderType().intValue()!=ShebaoConstants.OrderType.BJ){
                    customerShebaoWriterMapper.updateSbAndGjjStateByCondition(2,record);
                }
            }
        }
    }

    /**
     * 更改员工补收补退信息
     * @param paymentCustomerList
     * @param paymentFeeDtl
     * @param supplementList
     * @param companyOrderId
     * @param flag
     */
    private BigDecimal  updateReceiveAndBackInfo(List<CustomersBean> paymentCustomerList,JSONArray paymentFeeDtl,List<CustomersSupplementBean> supplementList,long companyOrderId,int flag,BigDecimal personSelfBackAmount){
        if(paymentCustomerList!=null && paymentCustomerList.size()>0){
            for(CustomersBean customerBean:paymentCustomerList){
                for (int i = 0, len = paymentFeeDtl.size(); i < len; i++) {
                    JSONObject successObj = paymentFeeDtl.getJSONObject(i);
                    String idCard = successObj.getString("id");
                    String code=successObj.getString("code");
                    if (!StringUtils.isStrNull(idCard) && idCard.equals(customerBean.getCustomerIdcard())) {
                        CustomersSupplementBean customersSupplementBean=new CustomersSupplementBean();
                        for(CustomersSupplementBean supplementBean:supplementList){
                            if(supplementBean.getSupplementCustomerId()!=null && supplementBean.getSupplementCustomerId().longValue()==customerBean.getCustomerId().longValue()){
                                customersSupplementBean=supplementBean;
                                break;
                            }
                        }
                        if("shebao".equals(code)){
                            JSONArray dtlArray=successObj.getJSONArray("dtl");
                            if(flag==1){
                                if(dtlArray!=null && dtlArray.size()>0){
                                    customersSupplementBean.setSupplementSbReceiveDetail(dtlArray.toJSONString());
                                }
                                customersSupplementBean.setSupplementSbReceiveReason(successObj.getString("desc"));
                            }else if(flag==2){
                                if(dtlArray!=null && dtlArray.size()>0){
                                    customersSupplementBean.setSupplementSbBackDetail(dtlArray.toJSONString());
                                }
                                customersSupplementBean.setSupplementSbBackReason(successObj.getString("desc"));
                            }
                            if(dtlArray!=null && dtlArray.size()>0){
                                for(int j = 0, lenj= dtlArray.size(); j < lenj; j++){
                                    JSONObject detailObj = dtlArray.getJSONObject(j);
                                    String orgDiff=StringUtils.isStrNull(detailObj.getString("orgDiff"))?"0":detailObj.getString("orgDiff");
                                    String empDiff=StringUtils.isStrNull(detailObj.getString("empDiff"))?"0":detailObj.getString("empDiff");
                                    if(flag==1){//补收
                                        if(customersSupplementBean.getSupplementSbCompanyReceive()!=null) {
                                            customersSupplementBean.setSupplementSbCompanyReceive(customersSupplementBean.getSupplementSbCompanyReceive().add(new BigDecimal(orgDiff)));
                                        }else{
                                            customersSupplementBean.setSupplementSbCompanyReceive(new BigDecimal(orgDiff));
                                        }
                                        if(customersSupplementBean.getSupplementSbSelfReceive()!=null) {
                                            customersSupplementBean.setSupplementSbSelfReceive(customersSupplementBean.getSupplementSbSelfReceive().add(new BigDecimal(empDiff)));
                                        }else{
                                            customersSupplementBean.setSupplementSbSelfReceive(new BigDecimal(empDiff));
                                        }
                                    }else if(flag==2){//补退
                                        if(customersSupplementBean.getSupplementSbCompanyBack()!=null){
                                            customersSupplementBean.setSupplementSbCompanyBack(customersSupplementBean.getSupplementSbCompanyBack().add(new BigDecimal(orgDiff)));
                                        }else{
                                            customersSupplementBean.setSupplementSbCompanyBack(new BigDecimal(orgDiff));
                                        }
                                        if(customersSupplementBean.getSupplementSbSelfBack()!=null){
                                            personSelfBackAmount=personSelfBackAmount.add(new BigDecimal(empDiff));
                                            customersSupplementBean.setSupplementSbSelfBack(customersSupplementBean.getSupplementSbSelfBack().add(new BigDecimal(empDiff)));
                                        }else{
                                            personSelfBackAmount=personSelfBackAmount.add(new BigDecimal(empDiff));
                                            customersSupplementBean.setSupplementSbSelfBack(new BigDecimal(empDiff));
                                        }
                                    }
                                }
                            }
                        }else if("gongjijin".equals(code)){
                            JSONArray dtlArray=successObj.getJSONArray("dtl");
                            if(flag==1){
                                if(dtlArray!=null && dtlArray.size()>0){
                                    customersSupplementBean.setSupplementGjjReceiveDetail(dtlArray.toJSONString());
                                }
                                customersSupplementBean.setSupplementGjjReceiveReason(successObj.getString("desc"));
                            }else if(flag==2){
                                if(dtlArray!=null && dtlArray.size()>0){
                                    customersSupplementBean.setSupplementGjjBackDetail(dtlArray.toJSONString());
                                }
                                customersSupplementBean.setSupplementGjjBackReason(successObj.getString("desc"));
                            }

                            if(dtlArray!=null && dtlArray.size()>0){
                                for(int j = 0, lenj= dtlArray.size(); j < lenj; j++){
                                    JSONObject detailObj = dtlArray.getJSONObject(j);
                                    String orgDiff=StringUtils.isStrNull(detailObj.getString("orgDiff"))?"0":detailObj.getString("orgDiff");
                                    String empDiff=StringUtils.isStrNull(detailObj.getString("empDiff"))?"0":detailObj.getString("empDiff");
                                    if(flag==1){//补收
                                        if(customersSupplementBean.getSupplementGjjCompanyReceive()!=null){
                                            customersSupplementBean.setSupplementGjjCompanyReceive(customersSupplementBean.getSupplementGjjCompanyReceive().add(new BigDecimal(orgDiff)));
                                        }else{
                                            customersSupplementBean.setSupplementGjjCompanyReceive(new BigDecimal(orgDiff));
                                        }
                                        if(customersSupplementBean.getSupplementGjjSelfReceive()!=null){
                                            customersSupplementBean.setSupplementGjjSelfReceive(customersSupplementBean.getSupplementGjjSelfReceive().add(new BigDecimal(empDiff)));
                                        }else{
                                            customersSupplementBean.setSupplementGjjSelfReceive(new BigDecimal(empDiff));
                                        }
                                    }else if(flag==2){//补退
                                        if(customersSupplementBean.getSupplementGjjCompanyBack()!=null){
                                            customersSupplementBean.setSupplementGjjCompanyBack(customersSupplementBean.getSupplementGjjCompanyBack().add(new BigDecimal(orgDiff)));
                                        }else{
                                            customersSupplementBean.setSupplementGjjCompanyBack(new BigDecimal(orgDiff));
                                        }
                                        if(customersSupplementBean.getSupplementGjjSelfBack()!=null){
                                            personSelfBackAmount=personSelfBackAmount.add(new BigDecimal(empDiff));
                                            customersSupplementBean.setSupplementGjjSelfBack(customersSupplementBean.getSupplementGjjSelfBack().add(new BigDecimal(empDiff)));
                                        }else{
                                            personSelfBackAmount=personSelfBackAmount.add(new BigDecimal(empDiff));
                                            customersSupplementBean.setSupplementGjjSelfBack(new BigDecimal(empDiff));
                                        }
                                    }
                                }
                            }
                        }
                        if(customersSupplementBean.getSupplementCustomerId()==null){
                            customersSupplementBean.setSupplementCustomerId(customerBean.getCustomerId());
                            customersSupplementBean.setSupplementCompanyOrderId(companyOrderId);
                            customersSupplementBean.setSupplementIsBack(ShebaoConstants.SUPPLEMENT_BACK_NO);
                            supplementList.add(customersSupplementBean);
                        }
                    }
                }
            }
        }
        return personSelfBackAmount;
    }
    /**
     * 继续提交订单
     * @param id
     * @return
     */
    private ResultResponse resubmit(Long id) {
        return null;
    }

    /**
     * 查询企业账单所有地区
     * @param comapnyId
     * @return
     */
    @Override
    public List<CompanyShebaoOrderBean> getOrderCities(Long comapnyId) {
        if(null != comapnyId){
            return companyShebaoOrderReaderMapper.selectOrderCities(comapnyId);
        }
        return null;
    }

    @Override
    public boolean updateOrderDetail(long companyOrderId) {
        CompanyShebaoOrderBean updateBean = new CompanyShebaoOrderBean();
        updateBean.setId(companyOrderId);

        //社保 公积金 详情
        int sbZyCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.SHEBAO.getCode(), 1);
        int sbHjCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.SHEBAO.getCode(), 2);
        int sbTjCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.SHEBAO.getCode(), 3);
        int sbStopCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.SHEBAO.getCode(), 4);
        int sbBjCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.SHEBAO.getCode(), 5);

        int gjjZyCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.GJJ.getCode(), 1);
        int gjjHjCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.GJJ.getCode(), 2);
        int gjjTjCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.GJJ.getCode(), 3);
        int gjjStopCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.GJJ.getCode(), 4);
        int gjjBjCount = customerShebaoOrderReaderMapper.selectCountByCompanyOrderIdAndOrderType(companyOrderId, ShebaoTypeEnum.GJJ.getCode(), 5);

        JSONObject sbDetail = new JSONObject();
        if(sbZyCount != 0)
            sbDetail.put("增员", sbZyCount);
        if(sbHjCount != 0)
            sbDetail.put("续缴", sbHjCount);
        if(sbTjCount != 0)
            sbDetail.put("调基", sbTjCount);
        if(sbStopCount != 0)
            sbDetail.put("停缴", sbStopCount);
        if(sbBjCount != 0)
            sbDetail.put("补缴", sbBjCount);
        updateBean.setOrderSbDetail(sbDetail.toJSONString());

        JSONObject gjjDetail = new JSONObject();
        if(gjjZyCount != 0)
            gjjDetail.put("增员", gjjZyCount);
        if(gjjHjCount != 0)
            gjjDetail.put("续缴", gjjHjCount);
        if(gjjTjCount != 0)
            gjjDetail.put("调基", gjjTjCount);
        if(gjjStopCount != 0)
            gjjDetail.put("停缴", gjjStopCount);
        if(gjjBjCount != 0)
            gjjDetail.put("补缴", gjjBjCount);
        updateBean.setOrderGjjDetail(gjjDetail.toJSONString());

        //服务费
        BigDecimal priceSum = new BigDecimal(0d);
        BigDecimal priceSb = customerShebaoOrderReaderMapper.selectOrgSum(companyOrderId, ShebaoTypeEnum.SHEBAO.getCode());
        BigDecimal priceGjj = customerShebaoOrderReaderMapper.selectOrgSum(companyOrderId, ShebaoTypeEnum.GJJ.getCode());
        if(priceSb != null)
            priceSum = priceSum.add(priceSb);
        if(priceGjj != null)
            priceSum = priceSum.add(priceGjj);

        updateBean.setPriceSb(priceSb);
        updateBean.setPriceGjj(priceGjj);
        double priceSingle = PropertyUtils.getDoubleValue("shebao.service.charge", 29.8);
        int customerCount = customerShebaoOrderReaderMapper.selectCustomerCount(companyOrderId);
        updateBean.setCustomerCount(customerCount);
        updateBean.setPriceService(new BigDecimal(priceSingle * customerCount));
        updateBean.setPriceSingle(new BigDecimal(priceSingle));

        updateBean.setPriceSum(priceSum.add(updateBean.getPriceService()));

        updateBean.setStatus(1);//修改订单状态为待提交

        return companyShebaoOrderWriterMapper.updateByPrimaryKeySelective(updateBean) > 0;
    }

    /**
     * 查询当前账单列表
     * @param companyId
     * @param city
     * @param is_current
     * @return
     */
    @Override
    public List<CompanyShebaoOrderDto> selectCurrentOrderList(Long companyId, String city, int is_current,String flag) {
        return companyShebaoOrderReaderMapper.selectCurrentOrderList(companyId, city, is_current,flag);
    }

    /**
     * 查询当前账单数量
     * @param companyId
     * @return
     */
    public int selectCurrentOrderCount(Long companyId){
        return companyShebaoOrderReaderMapper.selectCurrentOrderCount(companyId);
    }

//    /**
//     * 移除异常信息
//     * @param orderPlace
//     * @param fail
//     */
//    private OrderPlace continueSubmit(OrderPlace orderPlace,JSONArray fail){
//        for(int i=0,len=fail.size(); i<len; i++){
//            JSONObject error = fail.getJSONObject(i);
//            String id = error.getString("id");
//            String code=error.getString("code");
//            String status=error.getString("status");
//            String month=error.getString("month");
//            if(!StringUtils.isStrNull(status)){
//                if(status.equals("add")){//移除新增的用户异常
//                    JSONArray jsonArray=orderPlace.getAdd();
//                    for(int j=0,lenj=jsonArray.size(); j<lenj; j++){
//                        JSONObject removeObj=jsonArray.getJSONObject(j);
//                        String removeId=removeObj.getString("id");
//                        if(removeId.equals(id)){
//                            if("shebao".equals(code)){
//                                removeObj.remove("ins");
//                            }else if("gongjijin".equals(code)){
//                                removeObj.remove("hf");
//                            }else if("info".equals(code)){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            if(StringUtils.isStrNull(removeObj.getString("ins")) && StringUtils.isStrNull(removeObj.getString("hf"))){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            break;
//                        }
//                    }
//                }else if(status.equals("keep")){//移除汇款的用户异常
//                    JSONArray jsonArray=orderPlace.getKeep();
//                    for(int j=0,lenj=jsonArray.size(); j<lenj; j++){
//                        JSONObject removeObj=jsonArray.getJSONObject(j);
//                        String removeId=removeObj.getString("id");
//                        if(removeId.equals(id)){
//                            jsonArray.remove(removeObj);
//                            break;
//                        }
//                    }
//                }else if(status.equals("basechg")){//移除调基的用户异常
//                    JSONArray jsonArray=orderPlace.getKeep();
//                    for(int j=0,lenj=jsonArray.size(); j<lenj; j++){
//                        JSONObject removeObj=jsonArray.getJSONObject(j);
//                        String removeId=removeObj.getString("id");
//                        if(removeId.equals(id)){
//                            if("shebao".equals(code)){
//                                removeObj.remove("insbase");
//                            }else if("gongjijin".equals(code)){
//                                removeObj.remove("hfbase");
//                            }else if("info".equals(code)){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            if(StringUtils.isStrNull(removeObj.getString("insbase")) && StringUtils.isStrNull(removeObj.getString("hfbase"))){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            break;
//                        }
//                    }
//                }else if(status.equals("overdue")){//移除补缴的用户异常
//                    JSONArray jsonArray=orderPlace.getKeep();
//                    for(int j=0,lenj=jsonArray.size(); j<lenj; j++){
//                        JSONObject removeObj=jsonArray.getJSONObject(j);
//                        String removeId=removeObj.getString("id");
//                        if(removeId.equals(id)){
//                            if("shebao".equals(code)){
//                                JSONArray insArray=removeObj.getJSONArray("ins");
//                                for(int k=0,lenk=insArray.size(); k<lenk; k++){
//                                    JSONObject insObj=insArray.getJSONObject(k);
//                                    if(insObj.getString("month").equals(month)){
//                                        insArray.remove(insObj);
//                                    }
//                                }
//                            }else if("gongjijin".equals(code)){
//                                JSONArray hfArray=removeObj.getJSONArray("hf");
//                                for(int k=0,lenk=hfArray.size(); k<lenk; k++){
//                                    JSONObject hfObj=hfArray.getJSONObject(k);
//                                    if(hfObj.getString("month").equals(month)){
//                                        hfArray.remove(hfObj);
//                                    }
//                                }
//                            }else if("info".equals(code)){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            if(removeObj.getJSONArray("ins").isEmpty() && removeObj.getJSONArray("hf").isEmpty()){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            break;
//                        }
//                    }
//                }else if(status.equals("stop")){//移除停缴的用户异常
//                    JSONArray jsonArray=orderPlace.getKeep();
//                    for(int j=0,lenj=jsonArray.size(); j<lenj; j++){
//                        JSONObject removeObj=jsonArray.getJSONObject(j);
//                        String removeId=removeObj.getString("id");
//                        if(removeId.equals(id)){
//                            if("shebao".equals(code)){
//                                removeObj.put("ins",false);
//                            }else if("gongjijin".equals(code)){
//                                removeObj.put("hf",false);
//                            }else if("info".equals(code)){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            if(!removeObj.getBooleanValue("ins")&& !removeObj.getBooleanValue("hf")){
//                                jsonArray.remove(removeObj);
//                                break;
//                            }
//                            break;
//                        }
//                    }
//                }
//
//            }
//        }
//        return orderPlace;
//    }

    /**
     * 获取补收补退详细信息
     * @param supplementCompanyOrderId
     * @return
     */
    public CustomersSupplementDto queryAddtionDetail(Long supplementCompanyOrderId){
        CustomersSupplementDto customersSupplementDto=new CustomersSupplementDto();
        List<CustomersSupplementDto> supplementDtos=customersSupplementReaderMapper.selectByCompanyOrderId(supplementCompanyOrderId);
        //企业补收总费用
        BigDecimal companyReceiveTotal=new BigDecimal("0");
        //企业补退总费用
        BigDecimal companyBackTotal=new BigDecimal("0");
        //个人补收总费用
        BigDecimal selfReceiveTotal=new BigDecimal("0");
        //个人补退总费用
        BigDecimal selfBackTotal=new BigDecimal("0");
        if(supplementDtos!=null && supplementDtos.size()>0){
            for(CustomersSupplementDto dto:supplementDtos){
                BigDecimal supplementSbCompanyReceive=dto.getSupplementSbCompanyReceive()!=null?dto.getSupplementSbCompanyReceive():new BigDecimal("0");
                BigDecimal supplementSbCompanyBack=dto.getSupplementSbCompanyBack()!=null?dto.getSupplementSbCompanyBack():new BigDecimal("0");
                BigDecimal supplementSbSelfReceive=dto.getSupplementSbSelfReceive()!=null?dto.getSupplementSbSelfReceive():new BigDecimal("0");
                BigDecimal supplementSbSelfBack=dto.getSupplementSbSelfBack()!=null?dto.getSupplementSbSelfBack():new BigDecimal("0");
                BigDecimal supplementGjjCompanyReceive=dto.getSupplementGjjCompanyReceive()!=null?dto.getSupplementGjjCompanyReceive():new BigDecimal("0");
                BigDecimal supplementGjjCompanyBack=dto.getSupplementGjjCompanyBack()!=null?dto.getSupplementGjjCompanyBack():new BigDecimal("0");
                BigDecimal supplementGjjSelfReceive=dto.getSupplementGjjSelfReceive()!=null?dto.getSupplementGjjSelfReceive():new BigDecimal("0");
                BigDecimal supplementGjjSelfBack=dto.getSupplementGjjSelfBack()!=null?dto.getSupplementGjjSelfBack():new BigDecimal("0");
                companyReceiveTotal=companyReceiveTotal.add(supplementSbCompanyReceive);
                companyReceiveTotal=companyReceiveTotal.add(supplementGjjCompanyReceive);//企业补收
                companyBackTotal=companyBackTotal.add(supplementSbCompanyBack);
                companyBackTotal=companyBackTotal.add(supplementGjjCompanyBack);//企业补退
                selfReceiveTotal=selfReceiveTotal.add(supplementSbSelfReceive);
                selfReceiveTotal=selfReceiveTotal.add(supplementGjjSelfReceive);//个人补收
                selfBackTotal=selfBackTotal.add(supplementSbSelfBack);
                selfBackTotal=selfBackTotal.add(supplementGjjSelfBack);//个人补退
                dto.setCompanySbDiff(supplementSbCompanyReceive.subtract(supplementSbCompanyBack));
                dto.setCompanyGjjDiff(supplementGjjCompanyReceive.subtract(supplementGjjCompanyBack));
                dto.setSelfSbDiff(supplementSbSelfReceive.subtract(supplementSbSelfBack));
                dto.setSelfGjjDiff(supplementGjjSelfReceive.subtract(supplementGjjSelfBack));
                dto.setSbDiffInfo(supplementSbCompanyReceive.add(supplementSbSelfReceive).subtract(supplementSbCompanyBack));
                dto.setGjjDiffInfo(supplementGjjCompanyReceive.add(supplementGjjSelfReceive).subtract(supplementGjjCompanyBack));
            }
        }
        //企业补差总费用
        BigDecimal companyDiffTotal=companyReceiveTotal.add(selfReceiveTotal).subtract(companyBackTotal);
        customersSupplementDto.setCompanyDiffTotal(companyDiffTotal);
        customersSupplementDto.setCompanyBackTotal(companyBackTotal);
        customersSupplementDto.setCompanyReceiveTotal(companyReceiveTotal);
        customersSupplementDto.setSelfBackTotal(selfBackTotal);
        customersSupplementDto.setSelfReceiveTotal(selfReceiveTotal);
        customersSupplementDto.setSupplementDtos(supplementDtos);
        return customersSupplementDto;
    }



    /**
     * 生成社保公积金订单,并返回订单本身
     *
     * @param companyId
     * @param companyOrderId
     * @return
     */
    public void generateSalaryOrder(Long companyId,Long companyOrderId) {
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        //类型
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.SOCIAL_TYPE);
        //充值金额
//        companyRechargesBean.setRechargeMoney(totalWages);
        //企业ID
        companyRechargesBean.setRechargeCompanyId(companyId);
        //周期ID
        companyRechargesBean.setExcelId(companyOrderId);
        //创建时间
        companyRechargesBean.setRechargeAddtime(new Date());
        //订单号
        String code = idGeneratorService.getOrderId(BusinessEnum.SEND_SALARY);
        companyRechargesBean.setRechargeNumber(code);
        //状态
        companyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
        Long id = companyRechargesService.insertSelective(companyRechargesBean);
        if (id == null || id.longValue() <= 0L) {
            throw new BusinessException("生成社保公积金订单失败");
        }
    }

    /**
     * 提交账单生成订单
     * @param companyId
     * @param companyOrderId
     * @param priceSumDecimal
     */
    public void generateSalaryOrder(Long companyId,Long companyOrderId,BigDecimal priceSumDecimal) {
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        //类型
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.SOCIAL_TYPE);
        //充值金额
//        companyRechargesBean.setRechargeMoney(totalWages);
        //企业ID
        companyRechargesBean.setRechargeCompanyId(companyId);
        //周期ID
        companyRechargesBean.setExcelId(companyOrderId);
        //创建时间
        companyRechargesBean.setRechargeAddtime(new Date());
        //订单号
        String code = idGeneratorService.getOrderId(BusinessEnum.SEND_SALARY);
        companyRechargesBean.setRechargeNumber(code);
        //状态
        companyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
        companyRechargesBean.setRechargeMoney(priceSumDecimal);
        Long id = companyRechargesService.insertSelective(companyRechargesBean);
        if (id == null || id.longValue() <= 0L) {
            throw new BusinessException("生成社保公积金订单失败");
        }
    }

    @Override
    public boolean updateOrderStatus(String orderNumber, int sbtStatus) {
        int status;
        if(sbtStatus == 3 || sbtStatus == 4) {
            status = 5;
        }else if(sbtStatus > 4 && sbtStatus <= 6) {
            status = sbtStatus + 1;
        }else{
            return false;
        }

        return companyShebaoOrderWriterMapper.updateStatusByOrderNuber(orderNumber, status) > 0;
    }



    /**
     * 查询历史账单
     * @param map
     * @return
     */
    @Override
    public ResultResponse queryHistoryPaybills(Map map) {
        if (map==null&&map.size()<0){
            throw new BusinessException("map封装参数为空");
        }
        ResultResponse resultResponse = new ResultResponse();
        List<CompanyShebaoOrderDto> list = companyShebaoOrderReaderMapper.selectHistoryBills(map);
        resultResponse.setData(list);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 查看差额详情,获取详细信息
     * @param supplementCompanyOrderId
     * @param supplementCustomerId
     * @return
     */
    public List<CustomersSupplementDto> customerAddtionDetail(Long supplementCompanyOrderId,Long supplementCustomerId,String payMonth,Integer type){
        if(supplementCompanyOrderId==null || supplementCustomerId==null || type==null || StringUtils.isStrNull(payMonth)){
            throw new BusinessException("查看差额详情,请传递参数");
        }
        CustomersSupplementBean CustomersSupplementBean=new CustomersSupplementBean();
        CustomersSupplementBean.setSupplementCompanyOrderId(supplementCompanyOrderId);
        CustomersSupplementBean.setSupplementCustomerId(supplementCustomerId);
        CustomersSupplementBean resultBean=customersSupplementService.selectByCompanyOrderIdAndCustomerId(CustomersSupplementBean);
        if(resultBean!=null){
            //获取所有的CODE及对应的NAME
            CustomerShebaoOrderBean customerShebaoOrderBean=new CustomerShebaoOrderBean();
            customerShebaoOrderBean.setCompanyShebaoOrderId(supplementCompanyOrderId);
            List<CustomerShebaoOrderBean> codeNameList=customerShebaoOrderReaderMapper.selectShebaoOrderList( customerShebaoOrderBean);
            Map<String,String> codeNameMap=new HashMap<>();
            if(codeNameList!=null && codeNameList.size()>0){
                for(CustomerShebaoOrderBean orderBean:codeNameList){
                    String orderDetail=orderBean.getOrderDetail();
                    if(!StringUtils.isStrNull(orderDetail)){
                        JSONArray jsonArray=JSON.parseArray(orderDetail);
                        if(!jsonArray.isEmpty()) {
                            for (int i = 0, len = jsonArray.size(); i < len; i++) {
                                JSONObject detail = jsonArray.getJSONObject(i);
                                String code = detail.getString("code");
                                String name = detail.getString("name");
                                if(!StringUtils.isStrNull(code) && !StringUtils.isStrNull(name)){
                                    if(StringUtils.isStrNull(codeNameMap.get(code))){
                                        codeNameMap.put(code,name);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            List<CustomersSupplementDto>  dtoList=new ArrayList<>();
            String sbReceiveDetail=resultBean.getSupplementSbReceiveDetail();
            String sbBackDetail=resultBean.getSupplementSbBackDetail();
            String gjjReceiveDetail=resultBean.getSupplementGjjReceiveDetail();
            String gjjBackDetail=resultBean.getSupplementGjjBackDetail();
            if(type==1){//社保
                assemAddtionDetail(sbReceiveDetail,dtoList,payMonth,"补收费用",codeNameMap);
                assemAddtionDetail(sbBackDetail,dtoList,payMonth,"补退费用",codeNameMap);
            }else if(type==2){//公积金
                assemAddtionDetail(gjjReceiveDetail,dtoList,payMonth,"补收费用",codeNameMap);
                assemAddtionDetail(gjjBackDetail,dtoList,payMonth,"补退费用",codeNameMap);
            }
            return dtoList;
        }else{
            return null;
        }
    }



    /**
     * 拼装补差详情
     * @param detailStr
     * @param dtoList
     * @param payMonth
     * @param type
     * @return
     */
    private List<CustomersSupplementDto> assemAddtionDetail(String detailStr,List<CustomersSupplementDto> dtoList,String payMonth,String type,Map<String,String> codeNameMap){
        if(!StringUtils.isStrNull(detailStr)){
            JSONArray jsonArray=JSON.parseArray(detailStr);
            if(!jsonArray.isEmpty()) {
                for (int i = 0, len = jsonArray.size(); i < len; i++) {
                    JSONObject detail = jsonArray.getJSONObject(i);
                    String insCode = detail.getString("insCode");
                    String orgDiff = detail.getString("orgDiff");
                    String empDiff = detail.getString("empDiff");
                    CustomersSupplementDto dto = new CustomersSupplementDto();
                    dto.setPayMonth(payMonth);
                    //从详情里面拿CODE对应的名称,如果没有,则只能一个一个拼装,但有可能有漏掉的
                    if(codeNameMap!=null && codeNameMap.size()>0){
                        if (!StringUtils.isStrNull(insCode) && !StringUtils.isStrNull(codeNameMap.get(insCode))) {
                            dto.setPayName(codeNameMap.get(insCode));
                        }
                    }else{
                        if (!StringUtils.isStrNull(insCode)) {
                            if ("yanglao".equals(insCode)) {
                                dto.setPayName("养老保险");
                            } else if ("yiliao".equals(insCode)) {
                                dto.setPayName("医疗保险");
                            } else if ("canjijin".equals(insCode)) {
                                dto.setPayName("残疾人保障金");
                            } else if ("shiye".equals(insCode)) {
                                dto.setPayName("失业保险");
                            }else if ("gongshang".equals(insCode)) {
                                dto.setPayName("工伤保险");
                            }else if ("shengyu".equals(insCode)) {
                                dto.setPayName("生育保险");
                            }else if ("gongjijin".equals(insCode)) {
                                dto.setPayName("公积金");
                            } else if ("buchong_gongjijin".equals(insCode)) {
                                dto.setPayName("补充公积金");
                            } else if ("dabing_yiliao".equals(insCode)) {
                                dto.setPayName("大病医疗");
                            }
                        }
                    }

                    if (!StringUtils.isStrNull(orgDiff)) {
                        dto.setPayCompanyAmount(new BigDecimal(orgDiff));
                    }
                    if (!StringUtils.isStrNull(empDiff)) {
                        dto.setPaySelfAmount(new BigDecimal(empDiff));
                    }
                    dto.setPayType(type);
                    dtoList.add(dto);
                }
            }
        }
        return dtoList;
    }

    /**
     * 取消订单
     * @param orderStr
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    public ResultResponse cancelOrder(String orderStr)throws BusinessException,Exception{
        ResultResponse resultResponse=new ResultResponse();
        SheBaoTong sheBaoTong = new SheBaoTong(true);
        SbtResponse cancelResponse=sheBaoTong.orderManager(orderStr, 0);
        JSONObject cancelRes = JSON.parseObject(cancelResponse.getData());
        LOGGER.info("取消订单返回结果:"+JSON.toJSONString(cancelRes));
        if(!StringUtils.isStrNull(cancelRes.getString("result")) && "1".equals(cancelRes.getString("result"))){
            CompanyShebaoOrderBean orderBean=companyShebaoOrderReaderMapper.selectByOrderNumber(orderStr);
            if(orderBean!=null){
                //删除recharge订单(不能删除,因为这个单还要关联账单流水)

                //清空错误信息
                customerShebaoOrderDescWriterMapper.updateErrorInfoEmptyForCancelOrder(orderBean.getId());
            }
            resultResponse.setMessage("success");
            resultResponse.setSuccess(true);
            return resultResponse;
        }else{
            if(cancelRes!=null && !StringUtils.isStrNull(cancelRes.getString("msg"))){
                throw new BusinessException(cancelRes.getString("msg"));
            }else{
                throw new BusinessException("取消订单失败");
            }

        }
    }

    /**
     * 查询公司的社保订单是否提交
     * @param map
     * @return
     */
    @Override
    public CompanyShebaoOrderBean selectShebaoOrderByMap(Map map) {
        return companyShebaoOrderReaderMapper.selectShebaoOrderByMap(map);
    }

    /**
     * 根据过滤条件获取所有用户财务付款的订单
     * @param companyShebaoOrderDto
     * @return
     */
    public ResultResponse selectPayInfoByCondition(CompanyShebaoOrderDto companyShebaoOrderDto){
        LOGGER.info("根据过滤条件获取所有用户财务付款的订单,传递参数:" + JSON.toJSONString(companyShebaoOrderDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companyShebaoOrderDto != null) {
            PageBounds pageBounds = new PageBounds(companyShebaoOrderDto.getPageIndex(), companyShebaoOrderDto.getPageSize());
            List<CompanyShebaoOrderDto> list = companyShebaoOrderReaderMapper.selectPayInfoByCondition(companyShebaoOrderDto,pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据过滤条件获取所有用户财务未付款的订单
     * @return
     */
    public ResultResponse selectPayInfoNoPayByCondition(CompanyShebaoOrderDto companyShebaoOrderDto){
        LOGGER.info("根据过滤条件获取所有用户财务付款的订单,传递参数:" + JSON.toJSONString(companyShebaoOrderDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companyShebaoOrderDto != null) {
            PageBounds pageBounds = new PageBounds(companyShebaoOrderDto.getPageIndex(), companyShebaoOrderDto.getPageSize());
            List<CompanyShebaoOrderDto> list = companyShebaoOrderReaderMapper.selectPayInfoNoPayByCondition(pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 获取所有用户财务付款的订单数量
     * @return
     */
    public int selectPayInfoCount(){
        return companyShebaoOrderReaderMapper.selectPayInfoCount();
    }

    /**
     * 获取所有用户财务未付款的订单数量
     * @return
     */
    public int selectPayInfoNoPayCount(){
        return companyShebaoOrderReaderMapper.selectPayInfoNoPayCount();
    }

    /**
     * 根据过滤条件获取所有用户财务付款的订单(不分页)
     * @param companyShebaoOrderDto
     * @return
     */
    public List<CompanyShebaoOrderDto> selectPayInfoByConditionNoPage(CompanyShebaoOrderDto companyShebaoOrderDto){
        return companyShebaoOrderReaderMapper.selectPayInfoByCondition(companyShebaoOrderDto);
    }

    /**
     * 根据所有财务付款的企业信息
     * @return
     */
    public List<CompanyShebaoOrderDto> selectPayInfoWithCompanyInfo(){
        return companyShebaoOrderReaderMapper.selectPayInfoWithCompanyInfo();
    }

    /**
     * 添加社保公积金付款信息
     * @param companyShebaoOrderDto
     * @param memberId
     */
    @Transactional
    public void addCompanyOrderPayInfo(CompanyShebaoOrderDto companyShebaoOrderDto,Long memberId,String webRoot)throws BusinessException,Exception{
        //获取社保通的订单编号
        CompanyShebaoOrderBean orderBean=companyShebaoOrderReaderMapper.selectByPrimaryKey(companyShebaoOrderDto.getId());
        if(orderBean==null){
            throw new BusinessException("获取不到企业社保公积金订单信息");
        }
        //验证
        validatorAddPayInfo( companyShebaoOrderDto);
        //添加财务处理人
        companyShebaoOrderDto.setShebaotongPayOperator(memberId);
        //变更状态
        companyShebaoOrderDto.setStatus(ShebaoConstants.COMPANY_ORDER_SUPPLYSUCCESS);
//        companyShebaoOrderDto.setShebaotongPayState(2);
        //更新付款信息
        int count=companyShebaoOrderWriterMapper.updateByPrimaryKeySelective(companyShebaoOrderDto);
        if(count<=0){
            throw new BusinessException("添加付款信息失败");
        }

        //更改社保通接口的状态为已通过
//        SheBaoTong sheBaoTong = new SheBaoTong(true);
//        String payTemplatePath="/resource/upload/template/testShebaoPay.jpg";
//        String payTempaltePath = webRoot + payTemplatePath;
//        ByteArrayOutputStream outputStream = null;
//        File imageFile=new File(payTempaltePath);
//        BufferedImage bufferedImage = ImageIO.read(imageFile);
//        outputStream = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "jpg", outputStream);
//        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        String prove=encoder.encode(outputStream.toByteArray());
//        LOGGER.info("base64:"+prove);
//        SbtResponse payResponseBefore=sheBaoTong.orderManager(orderBean.getShebaotongServiceNumber(), 2, prove, "jpg");
        String payImgName=companyShebaoOrderDto.getShebaotongPayImg();
        String sufferName=payImgName.substring(payImgName.lastIndexOf(".")+1);

        byte[] browserStream= AliOss.downloadFileByte(PropertyUtils.getString("oss.bucketName.img"),payImgName);
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        String prove=encoder.encode(browserStream);
        LOGGER.info("base64:"+prove);
        SheBaoTong sheBaoTong = new SheBaoTong(true);
        SbtResponse payResponseBefore=sheBaoTong.orderManager(orderBean.getShebaotongServiceNumber(), 2, prove, sufferName);
        JSONObject payResBefore = JSON.parseObject(payResponseBefore.getData());
        LOGGER.info("更改社保通状态为已付款返回结果:"+JSON.toJSONString(payResBefore));
        if(StringUtils.isStrNull(payResBefore.getString("result")) || (!StringUtils.isStrNull(payResBefore.getString("result")) && !"1".equals(payResBefore.getString("result")))){
            throw new BusinessException("更改社保通状态为已付款失败");
        }

    }

    /**
     * 添加社保公积金付款信息相关验证
     * @param companyShebaoOrderDto
     */
    private void validatorAddPayInfo(CompanyShebaoOrderDto companyShebaoOrderDto){
        if(companyShebaoOrderDto==null){
            throw new BusinessException("请填写内容");
        }
        if(StringUtils.isStrNull(companyShebaoOrderDto.getShebaotongPayBankName())){
            throw new BusinessException("请输入开户行");
        }
        if(StringUtils.isStrNull(companyShebaoOrderDto.getShebaotongPayCompanyAccount())){
            throw new BusinessException("请输入企业账户");
        }
        if(StringUtils.isStrNull(companyShebaoOrderDto.getShebaotongPayBankNo())){
            throw new BusinessException("请输入企业账号");
        }
        if(StringUtils.isStrNull(companyShebaoOrderDto.getShebaotongPayBatch())){
            throw new BusinessException("请输入银行交易流水号");
        }
        if(StringUtils.isStrNull(companyShebaoOrderDto.getShebaotongPayImg())){
            throw new BusinessException("请选择汇款成功证明图片");
        }
    }

    /**
     * 更新
     * @param record
     * @return
     */
    public int updateByPrimaryKeySelective(CompanyShebaoOrderBean record){
        return companyShebaoOrderWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 查询当前员工的社保公积金信息
     * @param customerId
     * @return
     */
    @Override
    public ResultResponse searchInfoDetails(Long customerId) throws Exception {

        if(StringUtils.isEmpty(customerId)){
            throw new BusinessException("参数customerId不能为空");
        }
        ResultResponse resultResponse = new ResultResponse();
        SimpleDateFormat sm = new SimpleDateFormat("yyyyMM");
        String shebaoFirstMonth=null;
        String gjjFirstMonth =null;
        //基本信息
        Map map =customerShebaoService.selectBaseInfo(customerId);

        String cityCode = map.get("join_city_code")==null?"未设置":map.get("join_city_code").toString();
        String currentMonth =null;
        if(!StringUtils.isEmpty(map.get("current_month"))){
            currentMonth= sm.format(map.get("current_month"));
        }
        Double sbBase = map.get("sb_base")==null?null:  ((BigDecimal)map.get("sb_base")).doubleValue();
        Double gjjBase= map.get("gjj_base")==null?null:((BigDecimal)map.get("gjj_base")).doubleValue();

        Map<String,Object> returnMap = new HashMap<String,Object>();
        CustomerShebaoOrderBean customerShebaoOrderBean =null;
        //查询社保缴纳起始年月
        if(!StringUtils.isEmpty(map.get("is_sb_keep"))&&(Integer)map.get("is_sb_keep")==1) {
            customerShebaoOrderBean = new CustomerShebaoOrderBean();
            customerShebaoOrderBean.setCustomerId(customerId);
            customerShebaoOrderBean.setRequireType(1);//社保类型
            customerShebaoOrderBean.setOrderType(1);//增员
            CustomerShebaoOrderBean customerShebaoOrderShebao = customerShebaoOrderReaderMapper.selectByCodition(customerShebaoOrderBean);
            if(customerShebaoOrderShebao!=null&&!StringUtils.isEmpty(customerShebaoOrderShebao.getOverdueMonth())){
                shebaoFirstMonth=sm.format(customerShebaoOrderShebao.getOverdueMonth());
                returnMap.put("shebaoFirstMonth",shebaoFirstMonth);
            }
            //查询社保详情
            Basic basic = sbtService.getSupplementaryPay(cityCode,currentMonth);
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic,map.get("sb_type").toString());
            Map cal = customerShebaoService.cal(sbBase, socialBase);
            SocialBase socialBaseKey = (SocialBase) cal.keySet().toArray()[0];
            String orderDetailSb = JSONObject.toJSONString(cal.get(socialBaseKey));
            LOGGER.info("社保订单详情："+orderDetailSb);
            returnMap.put("orderDetailSb",orderDetailSb);
            returnMap.put("isSbKeep",1);//缴纳
        }else{
            //保存未缴纳
            returnMap.put("isSbKeep",0);//未缴纳
        }
        //查询公积金缴纳起始月
        if(!StringUtils.isEmpty(map.get("is_gjj_keep"))&&(Integer)map.get("is_gjj_keep")==1){
            customerShebaoOrderBean = new CustomerShebaoOrderBean();
            customerShebaoOrderBean.setCustomerId(customerId);
            customerShebaoOrderBean.setRequireType(2);//社保类型
            customerShebaoOrderBean.setOrderType(1);//增员
            CustomerShebaoOrderBean customerShebaoOrderGjj = customerShebaoOrderReaderMapper.selectByCodition(customerShebaoOrderBean);
            if(customerShebaoOrderGjj!=null&&!StringUtils.isEmpty(customerShebaoOrderGjj.getOverdueMonth())){
                gjjFirstMonth=sm.format(customerShebaoOrderGjj.getOverdueMonth());
                returnMap.put("gjjFirstMonth",gjjFirstMonth);
            }
            //查询公积金详情
            Basic basic = sbtService.getSupplementaryPay(cityCode,currentMonth);
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic,map.get("gjj_type").toString());
            Map cal = customerShebaoService.cal(gjjBase, socialBase);
            SocialBase socialBaseKey = (SocialBase) cal.keySet().toArray()[0];
            String orderDetailGjj = JSONObject.toJSONString(cal.get(socialBaseKey));
            LOGGER.info("公积金订单详情："+orderDetailGjj);
            returnMap.put("orderDetailGjj",orderDetailGjj);
            returnMap.put("isGjjKeep",1);//缴纳
        }else{
            returnMap.put("isGjjKeep",0);//未缴纳
        }

        returnMap.put("infoMap",map);
        resultResponse.setData(returnMap);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    @Override
    public boolean deleteCompanyOrderIfEmpty(Long id) {
        return companyShebaoOrderWriterMapper.deleteCompanyOrderIfEmpty(id) >= 1;
    }

    /**
     * 查询所有的社保公积金订单（后台管理用）
     * @param map
     * @return
     */
    @Override
    public ResultResponse selectAllOrdersByMap(Map<String, Object> map) throws BusinessException{
        if(map==null){
            LOGGER.error("当前参数为空");
            throw new BusinessException("当前封装参数为空");
        }
        ResultResponse resultResponse = new ResultResponse();
        List<CompanyShebaoOrderDto> list = companyShebaoOrderReaderMapper.selectAllOrdersByMap(map);
        resultResponse.setSuccess(true);
        resultResponse.setData(list);
        return resultResponse;
    }

    /**
     * 查询该公司未付款账单数量
     * @param companyId
     * @return
     */
    @Override
    public long selectUnpayOrders(long companyId) {
        return companyShebaoOrderReaderMapper.selectUnpayOrders(companyId);
    }

    /**
     * 查询所有的未提交的账单
     * @param companyId
     * @return
     */
    @Override
    public List<CompanyShebaoOrderDto> selectLastOrder(long companyId) {
        return companyShebaoOrderReaderMapper.selectLastOrder(companyId);
    }
}

