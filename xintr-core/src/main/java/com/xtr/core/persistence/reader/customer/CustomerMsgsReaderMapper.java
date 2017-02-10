package com.xtr.core.persistence.reader.customer;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.customer.CustomerMsgsBean;

import java.util.List;

public interface CustomerMsgsReaderMapper {


    /**
     *  根据指定主键获取一条数据库记录,customer_msgs
     *
     * @param msgId
     */
    CustomerMsgsBean selectByPrimaryKey(Long msgId);

    /**
     * 根据客户过滤条件获取信息列表
     * @param customerMsgsBean
     * @return
     */
    List<CustomerMsgsBean> selectMsgByCustomerCondition(CustomerMsgsBean customerMsgsBean,PageBounds pageBounds);

    /**
     * 根据客户编码获取未读消息列表
     * @param customerMsgsBean
     * @return
     */
    List<CustomerMsgsBean> selectNoReaderMsgByCustomerId(CustomerMsgsBean customerMsgsBean);
}