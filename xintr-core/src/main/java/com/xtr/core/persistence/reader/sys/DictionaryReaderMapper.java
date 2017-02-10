package com.xtr.core.persistence.reader.sys;

import com.xtr.api.domain.sys.DictionaryBean;

import java.util.List;

public interface DictionaryReaderMapper {


    /**
     * 根据指定主键获取一条数据库记录,dictionary
     *
     * @param id
     */
    DictionaryBean selectByPrimaryKey(Long id);

    /**
     * 获取所有字典数据
     *
     * @return
     */
    List<DictionaryBean> getDictionaryData();

    /**
     * 获取顶级节点
     *
     * @return
     */
    DictionaryBean getParentDict();

    /**
     * 根据
     * @param dictValue
     * @return
     */
    DictionaryBean selectByDictValue(String dictValue);

}