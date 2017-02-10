package com.xtr.api.domain.sys;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;

public class DictionaryDataBean extends BaseObject implements Serializable {
    /**
     * 主键,所属表字段为dictionary_data.id
     */
    private Long id;

    /**
     * dictionary中的值,所属表字段为dictionary_data.dict_value
     */
    private String dictValue;

    /**
     * 字典名字,所属表字段为dictionary_data.dictdata_name
     */
    private String dictdataName;

    /**
     * 字典值,所属表字段为dictionary_data.dictdata_value
     */
    private String dictdataValue;

    /**
     * 0默认为不固定，1固定；固定就不能再去修改了,所属表字段为dictionary_data.isfixed
     */
    private Integer isfixed;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 获取 主键 字段:dictionary_data.id
     *
     * @return dictionary_data.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:dictionary_data.id
     *
     * @param id dictionary_data.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 dictionary中的值 字段:dictionary_data.dict_value
     *
     * @return dictionary_data.dict_value, dictionary中的值
     */
    public String getDictValue() {
        return dictValue;
    }

    /**
     * 设置 dictionary中的值 字段:dictionary_data.dict_value
     *
     * @param dictValue dictionary_data.dict_value, dictionary中的值
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue == null ? null : dictValue.trim();
    }

    /**
     * 获取 字典名字 字段:dictionary_data.dictdata_name
     *
     * @return dictionary_data.dictdata_name, 字典名字
     */
    public String getDictdataName() {
        return dictdataName;
    }

    /**
     * 设置 字典名字 字段:dictionary_data.dictdata_name
     *
     * @param dictdataName dictionary_data.dictdata_name, 字典名字
     */
    public void setDictdataName(String dictdataName) {
        this.dictdataName = dictdataName == null ? null : dictdataName.trim();
    }

    /**
     * 获取 字典值 字段:dictionary_data.dictdata_value
     *
     * @return dictionary_data.dictdata_value, 字典值
     */
    public String getDictdataValue() {
        return dictdataValue;
    }

    /**
     * 设置 字典值 字段:dictionary_data.dictdata_value
     *
     * @param dictdataValue dictionary_data.dictdata_value, 字典值
     */
    public void setDictdataValue(String dictdataValue) {
        this.dictdataValue = dictdataValue == null ? null : dictdataValue.trim();
    }

    /**
     * 获取 0默认为不固定，1固定；固定就不能再去修改了 字段:dictionary_data.isfixed
     *
     * @return dictionary_data.isfixed, 0默认为不固定，1固定；固定就不能再去修改了
     */
    public Integer getIsfixed() {
        return isfixed;
    }

    /**
     * 设置 0默认为不固定，1固定；固定就不能再去修改了 字段:dictionary_data.isfixed
     *
     * @param isfixed dictionary_data.isfixed, 0默认为不固定，1固定；固定就不能再去修改了
     */
    public void setIsfixed(Integer isfixed) {
        this.isfixed = isfixed;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}