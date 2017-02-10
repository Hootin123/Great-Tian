package com.xtr.api.service.account;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.RedReceiveBean;
import com.xtr.comm.basic.BusinessException;

/**
 * <p>红包使用记录</p>
 *
 * @author 任齐
 * @createTime: 2016/8/2 16:59
 */
public interface RedReceiveService {

    /**
     * 根据红包id或订单id查询红包使用记录
     *
     * @param redId
     * @param orderId
     * @return
     */
    RedReceiveBean getRedReceive(Long redId, Long orderId);

    /**
     * 分页查询红包使用记录
     *
     * @param redReceiveBean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultResponse getRedEnvelopePage(RedReceiveBean redReceiveBean, int pageIndex, int pageSize);

    /**
     * 使用红包(插入一条红包使用记录)
     *
     * @param redReceiveBean
     * @throws BusinessException
     */
    void useRedEnvelope(RedReceiveBean redReceiveBean) throws BusinessException;

}
