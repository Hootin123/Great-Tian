package com.xtr.api.service.customer;

import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.customer.CustomerMsgsBean;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/14 15:31
 */
public interface CustomerMsgsService {

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
    AppResponse selectMsgByCustomerCondition(CustomerMsgsBean customerMsgsBean)throws BusinessException;

    /**
     * 根据客户编码获取各种消息状态的消息列表
     * @param customerMsgsBean
     * @return
     */
    List<CustomerMsgsBean> selectNoReaderMsgByCustomerId(CustomerMsgsBean customerMsgsBean);

    /**
     * 保存员工消息
     *
     * @param customerMsgsBean
     * @throws BusinessException
     */
    void saveMsg(CustomerMsgsBean customerMsgsBean) throws BusinessException;
}
