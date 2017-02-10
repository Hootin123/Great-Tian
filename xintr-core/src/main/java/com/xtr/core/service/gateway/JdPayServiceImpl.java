//package com.xtr.core.service.gateway;
//
//import com.alibaba.fastjson.JSON;
//import com.xtr.api.domain.gateway.GatewayLogBean;
//import com.xtr.api.dto.gateway.base.BusinessType;
//import com.xtr.api.dto.gateway.base.GatewayLog;
//import com.xtr.api.dto.gateway.request.AccountQueryRequest;
//import com.xtr.api.dto.gateway.request.DefrayPayRequest;
//import com.xtr.api.dto.gateway.request.TradeQueryRequest;
//import com.xtr.api.dto.gateway.response.AccountQueryResponse;
//import com.xtr.api.dto.gateway.response.DefrayPayResponse;
//import com.xtr.api.dto.gateway.response.NotifyResponse;
//import com.xtr.api.service.gateway.JdPayService;
//import com.xtr.comm.annotation.SystemServiceLog;
//import com.xtr.comm.basic.BusinessException;
//import com.xtr.comm.jd.enctypt.Contants;
//import com.xtr.comm.jd.enctypt.RequestUtil;
//import com.xtr.comm.jd.util.CodeConst;
//import com.xtr.comm.jd.util.JdPayConfig;
//import com.xtr.core.persistence.writer.gateway.GatewayLogWriterMapper;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.UnsupportedEncodingException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//
///**
// * Created by xuewu on 2016/8/3.
// */
//@Service("jdPayService")
//public class JdPayServiceImpl implements JdPayService {
//
//    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JdPayServiceImpl.class);
//
//    @Autowired
//    private JdPayConfig jdPayConfig;
//
//    @Autowired
//    private GatewayLogWriterMapper gatewayLogWriterMapper;
//
//    private DateFormat jdRequestTimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
//
//    @Override
//    @SystemServiceLog(operation = "代付请求", modelName = "京东代付")
//    public DefrayPayResponse defrayPay(DefrayPayRequest requestPara) throws BusinessException {
//        DefrayPayResponse response = new DefrayPayResponse(requestPara.getBusinessType());
//
//        if(requestPara == null || StringUtils.isBlank(requestPara.getPayeeBankCode()) || StringUtils.isBlank(requestPara.getPayeeAccountType())
//                || StringUtils.isBlank(requestPara.getPayeeAccountNo()) || StringUtils.isBlank(requestPara.getPayeeAccountName())
//                || StringUtils.isBlank(requestPara.getOutTradeNo()) || StringUtils.isBlank(requestPara.getTradeAmount())
//                || StringUtils.isBlank(requestPara.getTradeSubject()) || StringUtils.isBlank(requestPara.getPayeeCardType())
//                  ||requestPara.getTradeAmount().contains(".")
//                ){
//            response.setTradeStatus(CodeConst.TRADE_CLOS);
//            response.setResponseMessage("付款信息无效");
//            response.setResponseCode("XTR_PAY_INFO_ERR");
//            return response;
//        }
//
//        try {
//            String responseText = "";
//            String notifyUrl = "";
//            String apiUrl = jdPayConfig.getBaseUrl() + jdPayConfig.getDefrayPayUrl();
//            if (requestPara.getBusinessType() == BusinessType.COMPANY_WITHDRAW) {
//                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getCompanyWithdrawNotifyUrl();
//            } else if(requestPara.getBusinessType() == BusinessType.CUSTOMER_WITHDRAW){
//                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getCustomerWithdrawNotifyUrl();
//            } else if(requestPara.getBusinessType() == BusinessType.RAPIDLY_WITHDRAW){
//                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getRapidlyWithdrawNotifyUrl();
//            } else if(requestPara.getBusinessType() == BusinessType.VALIDATE_CUSTOMER){
//                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getCustomerValidateNotifyUrl();
//            } else {
//                response.setTradeStatus(CodeConst.TRADE_CLOS);
//                response.setResponseMessage("未知业务类型");
//                response.setResponseCode("XTR_BUSS_TYPE_ERR");
//                return response;
//            }
//
//            //生成数据
//            requestPara.setPayTool("TRAN");
//            requestPara.setSignType("SHA-256");
//            requestPara.setTradeCurrency("CNY");
//            requestPara.setRequestDatetime(getRequestTime());
//            requestPara.setCustomerNo(jdPayConfig.getCustomerNo());
//            requestPara.setNotifyUrl(notifyUrl);
//            requestPara.setApiName(apiUrl);
//            Map<String, String> paramMap = requestPara.createReqMap(true);
//
//            //请求
//            RequestUtil reqUtil = new RequestUtil(jdPayConfig);
//            responseText = reqUtil.tradeRequestSSL(paramMap, apiUrl, Contants.encryptType_RSA);
//            doLog(requestPara);
//
//            //验证数据
//            Map<String, String> map = reqUtil.verifySingReturnData(responseText);
//            response.setResult(map);
//            response.setLogContent(responseText);
//            doLog(response);
//
//            if (map == null) {
//                throw new BusinessException("gateway_err_code", "验证签名不成功");
//            }
//
//            //验证状态
//            String responseCode = response.getResponseCode();
//            String tradeStatus = response.getTradeStatus();
//            rescode(responseCode, tradeStatus, false);
//
//        } catch (Exception e) {
//            throw new BusinessException("gateway_err_code", e.getMessage(), e);
//        }
//
//        return response;
//    }
//
//    @Override
//    @SystemServiceLog(operation = "交易查询", modelName = "京东代付")
//    public NotifyResponse tradeQuery(TradeQueryRequest requestPara) throws BusinessException {
//        if(requestPara == null || StringUtils.isBlank(requestPara.getOutTradeNo()))
//            throw new BusinessException("查询信息无效！");
//
//        NotifyResponse nr = new NotifyResponse(requestPara.getBusinessType());
//        try {
//            RequestUtil demoUtil = new RequestUtil(jdPayConfig);
//            String responseText = "";
//
//            //生成数据
//            String apiName = jdPayConfig.getBaseUrl() + jdPayConfig.getTradeQueryUrl();
//            requestPara.setRequestDatetime(getRequestTime());
//            requestPara.setCustomerNo(jdPayConfig.getCustomerNo());
//            requestPara.setApiName(apiName);
//
//            //请求
//            responseText = demoUtil.tradeRequestSSL(requestPara.createReqMap(true), apiName, null);
//            doLog(requestPara);
//
//            //验证数据
//            Map<String, String> map = demoUtil.verifySingReturnData(responseText);
//            nr.setResult(map);
//            nr.setLogContent(responseText);
//            doLog(nr);
//
//            if (map == null) {
//                throw new BusinessException("gateway_err_code", "验证签名不成功");
//            }
//
//            //验证状态
//            String responseCode = nr.getResponseCode();
//            String tradeStatus = nr.getTradeStatus();
//            rescode(responseCode, tradeStatus, false);
//
//        } catch (UnsupportedEncodingException e) {
//            throw new BusinessException("gateway_err_code", e.getMessage(), e);
//        } catch (Exception e) {
//            throw new BusinessException("gateway_err_code", e.getMessage(), e);
//        }
//        return nr;
//    }
//
//    @Override
//    @SystemServiceLog(operation = "回调验证", modelName = "京东代付")
//    public NotifyResponse verifySingNotify(Map<String, String> requestMap, BusinessType businessType) throws BusinessException {
//
//        RequestUtil requestUtil = new RequestUtil(jdPayConfig);
//        NotifyResponse nr = new NotifyResponse(businessType);
//        try {
//            Map<String, String> decodeMap = requestUtil.verifySingNotify(requestMap);
//
//            nr.setResult(decodeMap);
//            nr.setLogContent(JSON.toJSONString(decodeMap));
//            doLog(nr);
//            if (decodeMap == null) {
//                throw new BusinessException("gateway_err_code", "验证签名不成功");
//            }
//
//            String response_code = nr.getResponseCode();
//            String tradeStatus = nr.getTradeStatus();
//            rescode(response_code, tradeStatus, false);
//
//        } catch (UnsupportedEncodingException e) {
//            throw new BusinessException("gateway_err_code", e.getMessage(), e);
//        } catch (Exception e) {
//            throw new BusinessException("gateway_err_code", e.getMessage(), e);
//        }
//
//        return nr;
//    }
//
//
//    @Override
//    @SystemServiceLog(operation = "账户余额查询", modelName = "京东代付")
//    public AccountQueryResponse accountQuery() throws BusinessException {
//        AccountQueryRequest request = new AccountQueryRequest();
//        RequestUtil demoUtil = new RequestUtil(jdPayConfig);
//        AccountQueryResponse response = new AccountQueryResponse();
//        try {
//            request.setCustomerNo(jdPayConfig.getCustomerNo());
//            request.setRequestDatetime(getRequestTime());
//
//            String responseText = demoUtil.tradeRequestSSL(request.createReqMap(true),"https://mapi.jdpay.com/npp10/account_query",null);
//            doLog(request);
//
//            Map<String,String> map = demoUtil.verifySingReturnData(responseText);
//            response.setResult(map);
//            response.setLogContent(responseText);
//            doLog(response);
//
//            if(map==null){
//                throw new BusinessException("gateway_err_code", "验证签名不成功");
//            }
//
//            String response_code = response.getResponseCode();
//            rescode(response_code, null, true);
//        } catch (Exception e) {
//            throw new BusinessException("gateway_err_code", e.getMessage(), e);
//        }
//
//        return response;
//    }
//
//    @Override
//    public JdPayConfig getJdPayConfig() {
//        return jdPayConfig;
//    }
//
//    public String getRequestTime() {
//        return jdRequestTimeFormat.format(new Date());
//    }
//
//    public void doLog(GatewayLog log) {
//        if (log == null) return;
//        LOGGER.info("===========" + (log.getLogType() == 0 ? "Request" : "Response") + " GatewayLog start================");
//        LOGGER.info("ApiName:\t" + log.getApiName());
//        LOGGER.info("BusinessId:\t" + log.getBusinessId());
//        LOGGER.info("LogContent:\t" + log.getLogContent());
//        LOGGER.info("BusinessType:\t" + log.getBusinessType().getCode());
//        LOGGER.info("LogType（0：请求JD，1：京东回调）:\t" + log.getLogType());
//        LOGGER.info("===========GatewayLog end=================");
//
//        GatewayLogBean gatewayLogBean = new GatewayLogBean();
//        gatewayLogBean.setApiName(log.getApiName());
//        gatewayLogBean.setBusinessId(log.getBusinessId());
//        gatewayLogBean.setLogContent(log.getLogContent());
//        gatewayLogBean.setBusinessType(log.getBusinessType().getCode());
//        gatewayLogBean.setLogType(log.getLogType());
//        gatewayLogBean.setLogDate(new Date());
//
//        gatewayLogWriterMapper.insert(gatewayLogBean);
//    }
//
//    private static boolean rescode(String response_code, String trade_status, boolean isQuery) throws BusinessException {
//        if (CodeConst.SUCCESS.equals(response_code)) {//如果response_code返回0000，表示请求逻辑正常，进一步判断订单状态
//            return true;
//        } else if (isQuery) {//isQuery如果是true，非0000的返回编码表示查询异常，不能按失败处理
//            throw new BusinessException("京东查询异常，建议不做数据处理");
//        } else if (!CodeConst.isContainCode(response_code)) {//返回编码不包含在配置中的
//            if (trade_status == null || StringUtils.isEmpty(trade_status)) {
//                throw new BusinessException("京东返回编码不包含在配置中的,未知");
//            } else {//如果有trade_status，按trade_status状态判断
//                return true;
//            }
//        } else if (CodeConst.SYSTEM_ERROR.equals(response_code) || CodeConst.RETURN_PARAM_NULL.equals(response_code)) {
//            throw new BusinessException("京东未知错误");
//        } else if (CodeConst.OUT_TRADE_NO_EXIST.equals(response_code)) {
//            return true;//京东外部交易号已经存在
//        } else {
//            throw new BusinessException("失败:" + CodeConst.getMsg(response_code));
//        }
//    }
//}
