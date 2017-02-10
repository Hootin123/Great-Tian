package com.xtr.api.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.DictionaryBean;
import com.xtr.api.domain.sys.DictionaryDataBean;

import java.util.List;

/**
 * <p>数据字典</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/1 13:05
 */
public interface DictionaryService {

    /**
     * 获取顶级节点
     *
     * @return
     */
    DictionaryBean getParentDict();


    /**
     * 获取所有字典数据
     *
     * @return
     */
    List<DictionaryBean> getDictionaryData();

    /**
     * 新增数据字典分类
     *
     * @param dictionaryBean
     * @return
     */
    ResultResponse addDict(DictionaryBean dictionaryBean);

    /**
     * 新增字典
     *
     * @param dictionaryDataBean
     * @return
     */
    ResultResponse addDictData(DictionaryDataBean dictionaryDataBean);


    /**
     * 更新数据字典分类
     *
     * @param dictionaryBean
     * @return
     */
    ResultResponse updateDict(DictionaryBean dictionaryBean);

    /**
     * 更新数据字典
     *
     * @param dictionaryDataBean
     * @return
     */
    ResultResponse updateDictData(DictionaryDataBean dictionaryDataBean);

    /**
     * 删除数据字典分类
     *
     * @param id
     * @return
     */
    ResultResponse deleteDict(Long id);

    /**
     * 删除字典数据
     *
     * @param id
     * @return
     */
    int deleteDictData(Long id);

    /**
     * 根据字典值获取所有的字典数据
     *
     * @param dictionaryDataBean
     * @return
     */
    ResultResponse selectPageList(DictionaryDataBean dictionaryDataBean);

    /**
     * 根据指定主键获取一条数据库记录,dictionary_data
     *
     * @param id
     */
    DictionaryDataBean selectByPrimaryKey(Long id);

    /**
     * 根据指定主键获取一条数据库记录,dictionary
     *
     * @param id
     */
    DictionaryBean selectDictByPrimaryKey(Long id);

    /**
     * 获取所有的字典数据
     *
     * @return
     */
    List<DictionaryDataBean> selectDictData();

    /**
     * 根据字典值获取所有的字典数据
     *
     * @param dictValue
     * @return
     */
    List<DictionaryDataBean> selectByDictValue(String dictValue);


}
