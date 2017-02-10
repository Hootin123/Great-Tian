package com.xtr.core.persistence.writer.account;

import com.xtr.api.domain.account.RedEnvelopeBean;

public interface RedEnvelopeWriterMapper {

    /**
     * 插入一条红包记录
     *
     * @param record
     * @return
     */
    int insert(RedEnvelopeBean record);

    int updateByPrimaryKeySelective(RedEnvelopeBean record);

}