package com.xtr.api.dto.gateway.base;

/**
 * Created by xuewu on 2016/8/4.
 */
public interface GatewayLog {

    int getLogType();

    String getLogContent();

    BusinessType getBusinessType();

    String getBusinessId();

    String getApiName();
}
