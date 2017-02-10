package com.xtr.api.service.customer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.customer.CustomerUnPayOrderDto;
import com.xtr.comm.basic.BusinessException;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Xuewu
 * @Date 2016/8/30.
 */
public interface CustomerPayFaildService {


    /**
     * 查询支付失败的发工资订单
     * @param dto
     * @return
     */
    ResultResponse selectPayFailedOrder(CustomerUnPayOrderDto dto);


    /**
     * 系统补发工资
     * @param rechargeId
     * @param loginUserObj
     * @return
     * @throws BusinessException
     */
    boolean sysMakeUp(Long rechargeId, SysUserBean loginUserObj) throws BusinessException;

    /**
     * 手动补
     * @param rechargeId
     * @param remark
     * @param user
     * @return
     */
    boolean handMakeUp(long rechargeId, String remark, SysUserBean user) throws BusinessException;

    /**
     * 自动处理社保公积金个人补退
     * @param rechargeId
     * @param loginUserObj
     * @return
     * @throws BusinessException
     */
    boolean sysMakeUpShebao(Long rechargeId, SysUserBean loginUserObj) throws BusinessException;

    /**
     * 手动处理社保公积金个人补退
     * @param rechargeId
     * @param remark
     * @param user
     * @return
     * @throws BusinessException
     */
    boolean handMakeUpShebao(long rechargeId, String remark, SysUserBean user) throws BusinessException;

    /**
     * 自动处理急速发工资
     * @param rechargeId
     * @param loginUserObj
     * @return
     * @throws BusinessException
     */
    boolean sysMakeUpRapidly(Long rechargeId, SysUserBean loginUserObj) throws BusinessException;

    /**
     * 手动处理急速发工资
     * @param rechargeId
     * @param remark
     * @param user
     * @return
     * @throws BusinessException
     */
    boolean handMakeUpRapidly(long rechargeId, String remark, SysUserBean user) throws BusinessException;
}
