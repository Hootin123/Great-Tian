package com.xtr.core.persistence.writer.customer;

import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public interface CustomerItemsWriterMapper {
    /**
     * 批量插入
     * @param param
     */
    public void addList(Map<String,Object> param);
}
