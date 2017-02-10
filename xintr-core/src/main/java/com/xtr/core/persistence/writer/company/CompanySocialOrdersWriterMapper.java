package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanySocialOrdersBean;

public interface CompanySocialOrdersWriterMapper {

    /**
     * 动态字段，插入数据库
     * @param companySocialOrdersBean
     * @return
     */
    int insertSelective(CompanySocialOrdersBean companySocialOrdersBean);

    /**
     * 动态字段，根据主键更新
     * @param companySocialOrdersBean
     * @return
     */
    int updateByPrimaryKeySelective(CompanySocialOrdersBean companySocialOrdersBean);
}