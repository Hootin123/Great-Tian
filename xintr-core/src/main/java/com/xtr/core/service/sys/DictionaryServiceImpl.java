package com.xtr.core.service.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.DictionaryBean;
import com.xtr.api.domain.sys.DictionaryDataBean;
import com.xtr.api.service.sys.DictionaryService;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.core.persistence.reader.sys.DictionaryDataReaderMapper;
import com.xtr.core.persistence.reader.sys.DictionaryReaderMapper;
import com.xtr.core.persistence.writer.sys.DictionaryDataWriterMapper;
import com.xtr.core.persistence.writer.sys.DictionaryWriterMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>数据字典服务实现/p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/1 13:09
 */
@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryServiceImpl.class);

    @Resource
    private DictionaryReaderMapper dictionaryReaderMapper;

    @Resource
    private DictionaryWriterMapper dictionaryWriterMapper;

    @Resource
    private DictionaryDataReaderMapper dictionaryDataReaderMapper;

    @Resource
    private DictionaryDataWriterMapper dictionaryDataWriterMapper;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 获取顶级节点
     *
     * @return
     */
    public DictionaryBean getParentDict() {
        return dictionaryReaderMapper.getParentDict();
    }

    /**
     * 获取所有字典数据
     *
     * @return
     */
    public List<DictionaryBean> getDictionaryData() {
        return dictionaryReaderMapper.getDictionaryData();
    }

    /**
     * 新增数据字典分类
     *
     * @param dictionaryBean
     * @return
     */
    @SystemServiceLog(operation = "新增字典分类", modelName = "数据字典")
    public ResultResponse addDict(DictionaryBean dictionaryBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (dictionaryBean != null) {
            //检查关键字是否已存在
            DictionaryBean dictionaryBean1 = dictionaryReaderMapper.selectByDictValue(dictionaryBean.getDictValue());
            if (dictionaryBean1 == null) {
                int result = dictionaryWriterMapper.insert(dictionaryBean);
                if (result > 0) {
                    resultResponse.setSuccess(true);
                }
            } else {
                resultResponse.setMessage("关键字【" + dictionaryBean.getDictValue() + "】已存在");
            }
        } else {
            resultResponse.setMessage("新增字典分类参数不能为空");
        }
        return resultResponse;
    }


    /**
     * 新增字典
     *
     * @param dictionaryDataBean
     * @return
     */
    @SystemServiceLog(operation = "新增数据字典", modelName = "数据字典")
    public ResultResponse addDictData(DictionaryDataBean dictionaryDataBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (dictionaryDataBean != null) {
            //检查关键字是否已存在
            List<DictionaryDataBean> list = dictionaryDataReaderMapper.selectByDictDataValue(dictionaryDataBean.getDictValue(), dictionaryDataBean.getDictdataValue());
            if (list.isEmpty()) {
                int result = dictionaryDataWriterMapper.insert(dictionaryDataBean);
                if (result > 0) {
                    resultResponse.setSuccess(true);
                    //更新缓存
                    redisTemplate.opsForValue().set(dictionaryDataBean.getDictValue(), dictionaryDataReaderMapper.selectByDictValue(dictionaryDataBean.getDictValue()));
                }

            } else {
                resultResponse.setMessage("当前分类已存在关键字【" + dictionaryDataBean.getDictdataValue() + "】");
            }
        } else {
            resultResponse.setMessage("新增字典参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 更新数据字典
     *
     * @param dictionaryBean
     * @return
     */
    @SystemServiceLog(operation = "修改字典分类", modelName = "数据字典")
    public ResultResponse updateDict(DictionaryBean dictionaryBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (dictionaryBean != null) {
            int result = dictionaryWriterMapper.updateByPrimaryKeySelective(dictionaryBean);
            if (result > 0)
                resultResponse.setSuccess(true);

        } else {
            resultResponse.setMessage("修改字典分类参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 更新数据字典dictionaryDataBean
     *
     * @param dictionaryDataBean
     * @return
     */
    @SystemServiceLog(operation = "修改数据字典", modelName = "数据字典")
    public ResultResponse updateDictData(DictionaryDataBean dictionaryDataBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (dictionaryDataBean != null) {
            int result = dictionaryDataWriterMapper.updateByPrimaryKeySelective(dictionaryDataBean);
            if (result > 0) {
                resultResponse.setSuccess(true);
                //更新缓存
                redisTemplate.opsForValue().set(dictionaryDataBean.getDictValue(), dictionaryDataReaderMapper.selectByDictValue(dictionaryDataBean.getDictValue()));
            }
        } else {
            resultResponse.setMessage("修改字典参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 删除数据字典分类
     *
     * @param id
     * @return
     */
    @SystemServiceLog(operation = "删除字典分类", modelName = "数据字典")
    public ResultResponse deleteDict(Long id) {
        ResultResponse resultResponse = new ResultResponse();
        if (id != null) {
            //删除字典
            dictionaryWriterMapper.deleteByPrimaryKey(id);
        } else {
            resultResponse.setMessage("删除字典分类参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 删除字典数据
     *
     * @param id
     * @return
     */
    @SystemServiceLog(operation = "删除数据字典", modelName = "数据字典")
    public int deleteDictData(Long id) {
        return dictionaryDataWriterMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据指定主键获取一条数据库记录,dictionary
     *
     * @param id
     */
    public DictionaryBean selectDictByPrimaryKey(Long id) {
        return dictionaryReaderMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据字典值获取所有的字典数据
     *
     * @param dictionaryDataBean
     * @return
     */
    public ResultResponse selectPageList(DictionaryDataBean dictionaryDataBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(dictionaryDataBean.getPageIndex(), dictionaryDataBean.getPageSize());
        PageList<DictionaryDataBean> list = dictionaryDataReaderMapper.listPage(dictionaryDataBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 根据指定主键获取一条数据库记录,dictionary_data
     *
     * @param id
     */
    public DictionaryDataBean selectByPrimaryKey(Long id) {
        return dictionaryDataReaderMapper.selectByPrimaryKey(id);
    }


    /**
     * 获取所有的字典数据
     *
     * @return
     */
    public List<DictionaryDataBean> selectDictData() {
        return dictionaryDataReaderMapper.selectDictData();
    }

    /**
     * 初始化字典到缓存中
     */
    public void initDictCache() {
        List<DictionaryDataBean> list = selectDictData();
        if (!list.isEmpty()) {
            List<DictionaryDataBean> dataBeanList = new ArrayList<>();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                DictionaryDataBean dictionaryDataBean = list.get(i);
                dataBeanList.add(dictionaryDataBean);
                if ((i < size - 1 && !StringUtils.equals(list.get(i + 1).getDictValue(), dictionaryDataBean.getDictValue()))
                        || i == size - 1) {
                    redisTemplate.opsForValue().set(PropertyUtils.getString("dict.cache.key") + dictionaryDataBean.getDictValue(), dataBeanList);
                    dataBeanList.clear();
                }
            }
        }
    }

    /**
     * 根据字典值获取所有的字典数据
     *
     * @param dictValue
     * @return
     */
    public List<DictionaryDataBean> selectByDictValue(String dictValue) {
        return dictionaryDataReaderMapper.selectByDictValue(dictValue);
    }

}
