package com.xtr.core.persistence.writer.customer;

import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public interface CustomerJobsWriterMapper {
    /**
     * 添加
     * @param param
     * @return
     */
    public int add(Map<String,Object> param);
}
