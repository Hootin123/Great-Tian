package com.xtr.core.persistence.reader.gateway;

import com.xtr.api.domain.gateway.GatewayLogBean;

public interface GatewayLogReaderMapper {

    GatewayLogBean selectByPrimaryKey(Long id);

}