package com.xtr.core.persistence.reader.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.sys.DictionaryDataBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictionaryDataReaderMapper {


    /**
     * 根据指定主键获取一条数据库记录,dictionary_data
     *
     * @param id
     */
    DictionaryDataBean selectByPrimaryKey(Long id);

    /**
     * 根据字典值分页获取字典数据
     *
     * @param dictionaryDataBean
     * @param pageBounds
     * @return
     */
    PageList<DictionaryDataBean> listPage(DictionaryDataBean dictionaryDataBean, PageBounds pageBounds);

    /**
     * 根据字典值获取所有的字典数据
     *
     * @param dictValue
     * @return
     */
    List<DictionaryDataBean> selectByDictValue(String dictValue);


    /**
     * 根据字典值获取所有的字典数据
     *
     * @param dictValue
     * @param dictdataValue
     * @return
     */
    List<DictionaryDataBean> selectByDictDataValue(@Param("dictValue") String dictValue, @Param("dictdataValue") String dictdataValue);

    /**
     * 获取所有的字典数据
     *
     * @return
     */
    List<DictionaryDataBean> selectDictData();
}