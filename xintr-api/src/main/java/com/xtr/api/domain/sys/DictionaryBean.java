package com.xtr.api.domain.sys;

import java.io.Serializable;
import java.util.List;

public class DictionaryBean implements Serializable{

    /**
     * 主键
     */
    private Long id;

    /**
     * 字典值,所属表字段为dictionary.dict_value
     */
    private String dictValue;

    /**
     * 字典名称,所属表字段为dictionary.dict_name
     */
    private String dictName;

    /**
     * 字典明细
     */
    private List<DictionaryDataBean> dataBeanList;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父Id
     */
    private Long parentId;

    /**
     * 获取 字典值 字段:dictionary.dict_value
     *
     * @return dictionary.dict_value, 字典值
     */
    public String getDictValue() {
        return dictValue;
    }

    /**
     * 设置 字典值 字段:dictionary.dict_value
     *
     * @param dictValue dictionary.dict_value, 字典值
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue == null ? null : dictValue.trim();
    }

    /**
     * 获取 字典名称 字段:dictionary.dict_name
     *
     * @return dictionary.dict_name, 字典名称
     */
    public String getDictName() {
        return dictName;
    }

    /**
     * 设置 字典名称 字段:dictionary.dict_name
     *
     * @param dictName dictionary.dict_name, 字典名称
     */
    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public List<DictionaryDataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<DictionaryDataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}