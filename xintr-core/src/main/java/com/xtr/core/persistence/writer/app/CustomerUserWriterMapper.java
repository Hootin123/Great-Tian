package com.xtr.core.persistence.writer.app;

import com.xtr.api.domain.app.CustomerUserBean;

public interface CustomerUserWriterMapper {
    /**
     *  根据主键删除数据库的记录,customer_user
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,customer_user
     *
     * @param record
     */
    int insert(CustomerUserBean record);

    /**
     *  动态字段,写入数据库记录,customer_user
     *
     * @param record
     */
    int insertSelective(CustomerUserBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_user
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerUserBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,customer_user
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerUserBean record);
}