package com.xtr.api.service.account;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;

/**
 * <p>红包</p>
 *
 * @author 任齐
 * @createTime: 2016/8/2 16:43
 */
public interface RedEnvelopeService {

    /**
     * 发红包
     *
     * @param redEnvelopeBean
     * @throws BusinessException
     */
    void sendRedEnvelope(RedEnvelopeBean redEnvelopeBean) throws BusinessException;

    /**
     * 注册发红包
     *
     * @param companyId
     */
    void sendRegisterEnvelope(Long companyId) throws BusinessException;

    /**
     * 分页查询红包记录
     *
     * @param redEnvelopeBean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultResponse getRedEnvelopePage(RedEnvelopeBean redEnvelopeBean, int pageIndex, int pageSize);

    /**
     * 根据红包id查询红包记录
     *
     * @param redId
     * @return
     */
    RedEnvelopeBean getByRedId(Long redId);

    /**
     * 查询企业可以使用的一个红包
     *
     * @param companyId 企业id
     * @param type      业务类型
     * @return
     */
    RedEnvelopeBean getByCompanyId(Long companyId, int type);

    /**
     * 检查红包状态
     * 不存在|已过期
     *
     * @param companyId
     * @param redId
     * @throws BusinessException
     */
    void checkRedEnvelopeState(Long companyId, Long redId) throws BusinessException;

    /**
     * 更新红包使用状态
     *
     * @param redId
     * @param state
     * @throws BusinessException
     */
    void updateState(Long redId, Integer state) throws BusinessException;

    /**
     * 查询企业红包余额
     *
     * @param companyId
     * @param scope
     * @return
     */
    BigDecimal getAmout(Long companyId, Integer scope);

}
