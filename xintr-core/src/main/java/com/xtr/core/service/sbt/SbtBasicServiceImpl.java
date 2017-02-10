package com.xtr.core.service.sbt;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.sbt.SbtBasicBean;
import com.xtr.api.service.sbt.SbtBasicService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.core.persistence.reader.sbt.SbtBasicReaderMapper;
import com.xtr.core.persistence.writer.sbt.SbtBasicWriterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/18 14:55.
 */
@Service("sbtBasicService")
public class SbtBasicServiceImpl implements SbtBasicService {

    @Resource
    private SbtBasicReaderMapper sbtBasicReaderMapper;

    @Resource
    private SbtBasicWriterMapper sbtBasicWriterMapper;

    /**
     * 根据城市和月份查询
     * @param city
     * @param month
     * @return
     */
    @Override
    public SbtBasicBean getByCityAndMonth(String city, int month) {
        if(null != city){
            return sbtBasicReaderMapper.selectByCityAndMonth(city, month);
        }
        return null;
    }

    /**
     * 保存基础数据
     * @param city
     * @param month
     * @param data
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void saveBasic(String city, int month, String data) throws BusinessException {
        SbtBasicBean sbtBasicBean = new SbtBasicBean();
        sbtBasicBean.setCreateTime(new Date());
        sbtBasicBean.setCity(city);
        sbtBasicBean.setMonth(month);
        sbtBasicBean.setData(data);

        try {
            sbtBasicWriterMapper.insert(sbtBasicBean);
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    /**
     * 更新基础数据
     * @param sbtBasicBean
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void updateBasic(SbtBasicBean sbtBasicBean) throws BusinessException {
        try {
            sbtBasicWriterMapper.updateDataByPrimaryKey(sbtBasicBean);
        }catch (Exception e){
            throw new BusinessException(e);
        }
    }

    /**
     * 查询社保公积金基础数据
     * @param cityCode
     * @return
     */
    @Override
    public Basic getByCityCode(String cityCode) {
        if(org.apache.commons.lang.StringUtils.isNotBlank(cityCode)){
            //todo 还要加个条件  数据更新时间为今天
            SbtBasicBean sbtBasicBean = sbtBasicReaderMapper.selectMaxMonthBasic(cityCode);
            if(null != sbtBasicBean){
                return JSON.parseObject(sbtBasicBean.getData(), Basic.class);
            }
        }
        return null;
    }
}
