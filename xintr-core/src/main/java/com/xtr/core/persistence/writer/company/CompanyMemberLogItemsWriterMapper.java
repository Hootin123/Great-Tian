package com.xtr.core.persistence.writer.company;

import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public interface CompanyMemberLogItemsWriterMapper {
    /**
     * 添加日志子项
     * @param param
     */
    public void add(Map<String,Object> param);
}
