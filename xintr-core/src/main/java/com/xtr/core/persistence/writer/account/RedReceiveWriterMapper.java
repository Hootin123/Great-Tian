package com.xtr.core.persistence.writer.account;

import com.xtr.api.domain.account.RedReceiveBean;

public interface RedReceiveWriterMapper {


    /**
     * 插入一条红包使用记录
     *
     * @param record
     * @return
     */
    int insert(RedReceiveBean record);

}