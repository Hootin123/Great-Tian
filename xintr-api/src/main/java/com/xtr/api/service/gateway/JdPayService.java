package com.xtr.api.service.gateway;

import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.AccountQueryRequest;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.dto.gateway.request.TradeQueryRequest;
import com.xtr.api.dto.gateway.response.AccountQueryResponse;
import com.xtr.api.dto.gateway.response.DefrayPayResponse;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.JdPayConfig;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by xuewu on 2016/8/3.
 */
public interface JdPayService {

    /**
     * 京东代付请求
     * @param requestPara
     * @return
     */
    DefrayPayResponse defrayPay(DefrayPayRequest requestPara) throws BusinessException;


    /**
     * 交易查询
     * @param requestPara
     * @return
     */
    NotifyResponse tradeQuery(TradeQueryRequest requestPara) throws BusinessException;

    /**
     * 验证异步回调数据
     * @param requestMap
     * @return
     * @throws UnsupportedEncodingException
     */
    NotifyResponse verifySingNotify(Map<String, String> requestMap, BusinessType businessType) throws BusinessException;

    /**
     * 查询账户余额
     * @param
     * @return
     * @throws UnsupportedEncodingException
     */
    AccountQueryResponse accountQuery() throws BusinessException;

    /**
     * 获取jd格式 请求时间
     * @return
     */
    JdPayConfig getJdPayConfig();
}
