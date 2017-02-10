package com.xtr.api.service.sbt;

import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.sbt.api.City;

import java.util.List;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/18 14:53.
 */
public interface SbtCityService {

    /**
     * 获取城市列表
     * @return
     */
    List<City> getCities();

    /**
     * 保存城市信息
     *
     * @param city
     * @throws BusinessException
     */
    void saveCity(City city) throws BusinessException;

    /**
     * 删除城市信息
     * @throws BusinessException
     */
    void deleteAll() throws BusinessException;
}
