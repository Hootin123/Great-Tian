package com.xtr.core.persistence.writer.customer;

import com.xtr.api.domain.customer.CustomerMsgsBean;

import java.util.List;

public interface CustomerMsgsWriterMapper {
    /**
     *  根据主键删除数据库的记录,customer_msgs
     *
     * @param msgId
     */
    int deleteByPrimaryKey(Long msgId);

    /**
     *  新写入数据库记录,customer_msgs
     *
     * @param record
     */
    int insert(CustomerMsgsBean record);

    /**
     *  动态字段,写入数据库记录,customer_msgs
     *
     * @param record
     */
    int insertSelective(CustomerMsgsBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_msgs
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerMsgsBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,customer_msgs
     *
     * @param record
     */
    int updateByPrimaryKey(CustomerMsgsBean record);

    /**
     * 批量更改消息状态为已读
     * @param list
     * @return
     */
    int updateMsgStateList(List<CustomerMsgsBean> list);
}