package com.xtr.api.service.order;

import com.xtr.comm.enums.BusinessEnum;

/**
 * <p>ID生成服务</p>
 *
 * @author 任齐
 * @createTime: 2016/8/4 13:23
 */
public interface IdGeneratorService {

    /**
     * 根据业务类型获取一个最新的订单id
     *
     * @param businessEnum
     * @return
     */
    String getOrderId(BusinessEnum businessEnum);

}
