package com.xtr.core.service.gateway;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.gateway.GatewayLogBean;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.base.GatewayLog;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.dto.gateway.request.TradeQueryRequest;
import com.xtr.api.dto.gateway.response.AccountQueryResponse;
import com.xtr.api.dto.gateway.response.DefrayPayResponse;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.comm.jd.util.JdPayConfig;
import com.xtr.core.persistence.writer.gateway.GatewayLogWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by xuewu on 2016/8/9.
 */
@Service("jdPayService")
public class TestJdPayServiceImpl implements JdPayService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TestJdPayServiceImpl.class);

    @Autowired
    JdPayConfig jdPayConfig;

    @Autowired
    private GatewayLogWriterMapper gatewayLogWriterMapper;

    private DateFormat jdRequestTimeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");

    @Override
    public DefrayPayResponse defrayPay(DefrayPayRequest requestPara) throws BusinessException {
        DefrayPayResponse response = new DefrayPayResponse(requestPara.getBusinessType());
        if(requestPara == null || StringUtils.isBlank(requestPara.getPayeeBankCode()) || StringUtils.isBlank(requestPara.getPayeeAccountType())
                || StringUtils.isBlank(requestPara.getPayeeAccountNo()) || StringUtils.isBlank(requestPara.getPayeeAccountName())
                || StringUtils.isBlank(requestPara.getOutTradeNo()) || StringUtils.isBlank(requestPara.getTradeAmount())
                || StringUtils.isBlank(requestPara.getTradeSubject()) || StringUtils.isBlank(requestPara.getPayeeCardType())
                ||requestPara.getTradeAmount().contains(".")
                ){
            response.setTradeStatus(CodeConst.TRADE_CLOS);
            response.setResponseMessage("付款信息无效");
            response.setResponseCode("XTR_PAY_INFO_ERR");
            return response;
        }


        try {
            String notifyUrl = "";
            String apiUrl = jdPayConfig.getBaseUrl() + jdPayConfig.getDefrayPayUrl();
            if (requestPara.getBusinessType() == BusinessType.COMPANY_WITHDRAW) {
                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getCompanyWithdrawNotifyUrl();
            }else if(requestPara.getBusinessType() == BusinessType.CUSTOMER_WITHDRAW){
                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getCompanyWithdrawNotifyUrl();
            }else if(requestPara.getBusinessType() == BusinessType.VALIDATE_CUSTOMER){
                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getCustomerValidateNotifyUrl();
            }else if(requestPara.getBusinessType() == BusinessType.RAPIDLY_WITHDRAW){
                notifyUrl = jdPayConfig.getNotifyBaseUrl() + jdPayConfig.getRapidlyWithdrawNotifyUrl();
            }else {
                response.setTradeStatus(CodeConst.TRADE_CLOS);
                response.setResponseMessage("未知业务类型");
                response.setResponseCode("XTR_BUSS_TYPE_ERR");
                return response;
            }

            //生成数据
            requestPara.setSignType("SHA-256");
            requestPara.setTradeCurrency("CNY");
            requestPara.setRequestDatetime(getRequestTime());
            requestPara.setCustomerNo(jdPayConfig.getCustomerNo());
            requestPara.setNotifyUrl(notifyUrl);
            requestPara.setApiName(apiUrl);

            //请求
            doLog(requestPara);

            response.setTradeStatus(getStatusById(requestPara.getOutTradeNo()));
            response.setLogContent(response.getTradeStatus());
            doLog(response);

        } catch (Exception e) {
            throw new BusinessException("gateway_err_code", e.getMessage(), e);
        }

        return response;
    }

    @Override
    public NotifyResponse tradeQuery(TradeQueryRequest requestPara) throws BusinessException {
        if(requestPara == null || StringUtils.isBlank(requestPara.getOutTradeNo()))
            throw new BusinessException("查询信息无效！");

        NotifyResponse nr = new NotifyResponse(requestPara.getBusinessType());
        try {
            //生成数据
            String apiName = jdPayConfig.getBaseUrl() + jdPayConfig.getTradeQueryUrl();
            requestPara.setRequestDatetime(getRequestTime());
            requestPara.setCustomerNo(jdPayConfig.getCustomerNo());
            requestPara.setApiName(apiName);
            doLog(requestPara);

            //验证数据
            nr.setTradeStatus(getStatusById(requestPara.getOutTradeNo()));
            nr.setLogContent(nr.getTradeStatus());
            doLog(nr);

        } catch (Exception e) {
            throw new BusinessException("gateway_err_code", e.getMessage(), e);
        }
        return nr;
    }

    @Override
    public NotifyResponse verifySingNotify(Map<String, String> requestMap, BusinessType businessType) throws BusinessException {
        NotifyResponse nr = new NotifyResponse(businessType);
        try {
            Map<String, String> decodeMap = requestMap;

            nr.setResult(decodeMap);
            nr.setLogContent(JSON.toJSONString(decodeMap));
            doLog(nr);
            if (decodeMap == null) {
                throw new BusinessException("gateway_err_code", "验证签名不成功");
            }

        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("gateway_err_code", e.getMessage(), e);
        } catch (Exception e) {
            throw new BusinessException("gateway_err_code", e.getMessage(), e);
        }

        return nr;
    }

    @Override
    public AccountQueryResponse accountQuery() throws BusinessException {
        return null;
    }

    @Override
    public JdPayConfig getJdPayConfig() {
        return jdPayConfig;
    }

    public String getStatusById(String orderId){
        if(StringUtils.isEmpty(orderId)) {
            return CodeConst.TRADE_CLOS;
        }else if(orderId.endsWith("9"))
            return CodeConst.TRADE_FINI;
        else if(orderId.endsWith("8"))
            return CodeConst.TRADE_CLOS;
        return CodeConst.TRADE_WPAR;

    }

    public void doLog(GatewayLog log) {
        if (log == null) return;
        LOGGER.info("=测试用==========" + (log.getLogType() == 0 ? "Request" : "Response") + " GatewayLog start================");
        LOGGER.info("ApiName:\t" + log.getApiName());
        LOGGER.info("BusinessId:\t" + log.getBusinessId());
        LOGGER.info("LogContent:\t" + log.getLogContent());
        LOGGER.info("BusinessType:\t" + log.getBusinessType().getCode());
        LOGGER.info("LogType（0：请求JD，1：京东回调）:\t" + log.getLogType());
        LOGGER.info("=测试用==========GatewayLog end=================");

        GatewayLogBean gatewayLogBean = new GatewayLogBean();
        gatewayLogBean.setApiName(log.getApiName());
        gatewayLogBean.setBusinessId(log.getBusinessId());
        gatewayLogBean.setLogContent(log.getLogContent());
        gatewayLogBean.setBusinessType(log.getBusinessType().getCode());
        gatewayLogBean.setLogType(log.getLogType());
        gatewayLogBean.setLogDate(new Date());

        gatewayLogWriterMapper.insert(gatewayLogBean);
    }

    public String getRequestTime() {
        return jdRequestTimeFormat.format(new Date());
    }



}
