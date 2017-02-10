package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.DictionaryDataBean;

public interface DictionaryDataWriterMapper {
    /**
     * 根据主键删除数据库的记录,dictionary_data
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,dictionary_data
     *
     * @param record
     */
    int insert(DictionaryDataBean record);

    /**
     * 动态字段,写入数据库记录,dictionary_data
     *
     * @param record
     */
    int insertSelective(DictionaryDataBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,dictionary_data
     *
     * @param record
     */
    int updateByPrimaryKeySelective(DictionaryDataBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,dictionary_data
     *
     * @param record
     */
    int updateByPrimaryKey(DictionaryDataBean record);

    /**
     * 根据字典值删除字典明细
     *
     * @param dictValue
     * @return
     */
    int deleteByDictValue(String dictValue);
}