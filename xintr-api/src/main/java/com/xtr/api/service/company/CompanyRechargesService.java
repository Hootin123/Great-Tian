package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>企业充值提现</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 11:46
 */
public interface CompanyRechargesService {

    /**
     * 根据主键查询数据库记录
     *
     * @param rechargeId
     * @return
     */
    CompanyRechargesBean selectByPrimaryKey(Long rechargeId);

    /**
     * 分页查询订单
     *
     * @param rechargeType
     * @param rechargeState
     * @param dateType
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultResponse selectPageList(Long comapnyId, Integer rechargeType, Integer rechargeState, int dateType, int pageIndex, int pageSize);

    /**
     * 分页查询企业流水
     *
     * @param companyRechargeDto
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultResponse selectRechargeList(CompanyRechargeDto companyRechargeDto, int pageIndex, int pageSize);

    /**
     * 企业充值
     *
     * @param companyRechargesBean
     * @return
     * @throws BusinessException
     */
    CompanyRechargesBean addRecharge(CompanyRechargesBean companyRechargesBean) throws BusinessException;

    /**
     *  根据借款订单创建充值订单
     * @param borrowOrderId
     * @throws BusinessException
     */
    void addRechargeByBorrowOrder(long borrowOrderId) throws BusinessException;

    /**
     * 企业提现
     *
     * @param companyId
     * @param rechargeMoney
     * @param rechargeClient
     * @param rechargeBank
     *@param bankAccountName
     * @param rechargeBanknumber
     * @param bankSubbranch @return
     * @throws BusinessException
     */
    CompanyRechargesBean companyWithdrawals(Long companyId, BigDecimal rechargeMoney, Integer rechargeClient, String rechargeBank, String bankAccountName, String rechargeBanknumber, String bankSubbranch) throws BusinessException;

    /**
     * 发工资
     *
     * @param totleCost
     * @param memberCompanyId
     * @param excelId
     * @throws BusinessException
     */
    void sendSalary(BigDecimal totleCost, Long memberCompanyId, Long excelId) throws BusinessException;

    /**
     * 审核充值
     *
     * @param rechargeId
     * @param memberId
     * @param state
     * @return
     */
    ResultResponse auditRecharge(Long rechargeId, Long memberId, int state) throws BusinessException;


    /**
     * 提现审核
     *
     * @param rechargeId
     * @param memberId
     * @param rechargeSerialNumber
     * @param rechargeBak
     * @return
     * @throws BusinessException
     */
    ResultResponse auditWithdrawals(Long rechargeId, Long memberId, String rechargeSerialNumber, String rechargeBak) throws BusinessException;


    /**
     * 社保订单银行支付
     * @param companyRechargesBean
     * @param orderId
     * @param orderPaytype
     * @return
     */
    int companySocialOrderBankPay(CompanyRechargesBean companyRechargesBean, long orderId, int orderPaytype) throws BusinessException;

    /**
     * 根据企业id删除数据库记录
     *
     * @param companyId
     * @return
     */
    int deleteByCompanyId(Long companyId) throws BusinessException;

    /**
     * 更新企业充值提现状态
     *
     * @param excelCompanyId
     * @param excelId
     * @throws BusinessException
     */
    void updateRechargeState(Long excelCompanyId, Long excelId, Integer state) throws BusinessException;


    /**
     * 处理京东回调
     * @param response
     */
    void doJdResponse(NotifyResponse response);

    /**
     * 插入企业订单
     * @param companyRechargesBean
     * @return
     */
    Long insertSelective(CompanyRechargesBean companyRechargesBean);

    /**
     * 根据订薪周期查询是否有生成的订单
     * @param companyRechargesBean
     * @return
     */
    List<CompanyRechargesBean> selectByExcelIdAndType(CompanyRechargesBean companyRechargesBean);

    /**
     * 根据ID更新企业订单
     * @param companyRechargesBean
     * @return
     */
    int updateByPrimaryKeySelective(CompanyRechargesBean companyRechargesBean);

    /**
     * 根据订薪周期查询是否存在某个状态的订单
     * @param companyRechargesBean
     * @return
     */
    int selectCountByCondition(CompanyRechargesBean companyRechargesBean);

    /**
     * 更新垫付与实际支付额度
     * @param record
     * @return
     */
    int updateScaleAndRealAmount(CompanyRechargesBean record);

    /**
     *根据订薪周期查询是否存在某个状态的订单信息
     * @param companyRechargesBean
     * @return
     */
    List<CompanyRechargesBean> selectListByCondition(CompanyRechargesBean companyRechargesBean);

    /**
     * 企业发工资生成订单,并返回订单本身
     *
     * @param companyId      公司Id
     * @param rsourceId      来源Id-工资单主键
     * @param rechargeType   类型 CompanyRechargeConstant
     * @param totalWages     金额
     * @param zeroBigDeciaml 红包金额
     * @param rechargeName   订单名称
     * @return
     */
    CompanyRechargesBean generateOrder(Long companyId, Long rsourceId,int rechargeType, BigDecimal totalWages,
                                              BigDecimal zeroBigDeciaml, String rechargeName);

    /**
     * 获取垫付金额及垫付比例
     *
     * @param companyId
     * @param totalWages
     * @return
     */
    CompanyRechargesBean queryPaymentInfo(Long companyId, BigDecimal totalWages);
}
