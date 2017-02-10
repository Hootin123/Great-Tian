package com.xtr.core.persistence.writer.sys;

import com.xtr.api.domain.sys.DictionaryBean;

public interface DictionaryWriterMapper {
    /**
     *  根据主键删除数据库的记录,dictionary
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,dictionary
     *
     * @param record
     */
    int insert(DictionaryBean record);

    /**
     *  动态字段,写入数据库记录,dictionary
     *
     * @param record
     */
    int insertSelective(DictionaryBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,dictionary
     *
     * @param record
     */
    int updateByPrimaryKeySelective(DictionaryBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,dictionary
     *
     * @param record
     */
    int updateByPrimaryKey(DictionaryBean record);
}