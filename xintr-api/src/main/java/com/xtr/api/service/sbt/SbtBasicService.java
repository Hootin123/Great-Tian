package com.xtr.api.service.sbt;

import com.xtr.api.domain.sbt.SbtBasicBean;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.sbt.api.Basic;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/9/18 14:53.
 */
public interface SbtBasicService {

    /**
     * 根据城市和月份查询
     * @param city
     * @param month
     * @return
     */
    SbtBasicBean getByCityAndMonth(String city, int month);

    /**
     * 保存基础数据
     * @param city
     * @param month
     * @param data
     * @throws BusinessException
     */
    void saveBasic(String city, int month, String data) throws BusinessException;

    /**
     * 更新基础数据
     * @param sbtBasicBean
     * @throws BusinessException
     */
    void updateBasic(SbtBasicBean sbtBasicBean) throws BusinessException;

    /**
     * 查询社保公积金基础数据
     * @param cityCode
     * @return
     */
    Basic getByCityCode(String cityCode);
}
