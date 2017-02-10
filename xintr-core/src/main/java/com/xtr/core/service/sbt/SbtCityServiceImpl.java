package com.xtr.core.service.sbt;

import com.xtr.api.service.sbt.SbtCityService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.sbt.api.City;
import com.xtr.core.persistence.reader.sbt.SbtCityReaderMapper;
import com.xtr.core.persistence.writer.sbt.SbtCityWriterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/18 16:14.
 */
@Service("sbtCityService")
public class SbtCityServiceImpl implements SbtCityService {

    @Resource
    private SbtCityReaderMapper sbtCityReaderMapper;

    @Resource
    private SbtCityWriterMapper sbtCityWriterMapper;

    @Override
    public List<City> getCities() {
        return sbtCityReaderMapper.selectCities();
    }

    /**
     * 保存城市信息
     *
     * @param city
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void saveCity(City city) throws BusinessException {
        try {
            sbtCityWriterMapper.insert(city);
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    /**
     * 删除所有城市
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void deleteAll() throws BusinessException {
        try {
            sbtCityWriterMapper.deleteAll();
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }
}
