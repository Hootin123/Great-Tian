
package com.xtr.api.service.account;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.comm.basic.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>客户子账户接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/3 12:13
 */

public interface SubAccountService {

    /**
     * 新增子账户
     *
     * @param subAccountBean
     * @return
     */
    int insert(SubAccountBean subAccountBean) throws Exception;

    /**
     * 处理账户业务之前，检查账户状态及校验值
     *
     * @throws BusinessException
     */
    void checkAccountStatus(SubAccountBean subAccountBean) throws BusinessException;

    /**
     * 处理账户业务成功之后，更新账户校验值
     *
     * @param subAccountBean
     * @return
     */
    void updateAccountCheckValue(SubAccountBean subAccountBean) throws BusinessException;

    /**
     * 获取MD5
     *
     * @param subAccountBean
     * @return
     */
    String getMd5Code(SubAccountBean subAccountBean) throws Exception;

    /**
     * 账户充值 生成流水
     *
     * @param custId
     * @param amount
     * @param recordSource 资金变动来源
     * @param resourceId   资金变动来源 ID
     * @param property
     * @return
     * @throws Exception
     */
    void rechargeRecords(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException;


    /**
     * 账户充值  不生成流水
     *
     * @param custId
     * @param amount
     * @param property
     * @return
     * @throws BusinessException
     */
    void recharge(Long custId, BigDecimal amount, Long resourceId, int property) throws BusinessException;

    /**
     * 贷款充值--充值的金额为冻结不可用
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @throws BusinessException
     */
    void loadRecharge(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException;

    /**
     * 插入资金流水
     *
     * @param custId
     * @param amount
     * @param recordType
     * @param recordSource
     * @param resourceId
     * @param property
     */
    void insertMoneyRecords(Long custId, BigDecimal amount,
                            Integer recordType, Integer recordSource, Long resourceId, int property) throws BusinessException;

    /**
     * 账户扣款
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws BusinessException
     */
    void deduct(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException;

    /**
     * 账户扣款
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws BusinessException
     */
    void deduct(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property,BigDecimal prementAmount) throws BusinessException;

    /**
     * 账户资金冻结
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws BusinessException
     */
    void frozen(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException;

    /**
     * 账户资金解冻返还
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws Exception
     */
    void backwash(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException;

    /**
     * 账户资金解冻扣款
     *
     * @param custId
     * @param amount
     * @param recordSource
     * @param resourceId
     * @param property
     * @return
     * @throws Exception
     */
    void thawDeduct(Long custId, BigDecimal amount, Integer recordSource, Long resourceId, int property) throws BusinessException;

    /**
     * 根据custId查询账户详情
     *
     * @param custId
     * @param property 1-个人 2-企业
     * @return
     */
    SubAccountBean selectByCustId(Long custId, int property);

    /**
     * 更新账户余额自动转入控制
     *
     * @param custId
     * @param property
     * @param isAutoTransfer
     * @return
     */
    int updateAutoTransfer(Long custId, int property, int isAutoTransfer);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,sub_account
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SubAccountBean record);

    /**
     * 京东支付失败，减退冻结金额 企业
     *
     * @param companyRechargesBean
     * @param desc
     */
    void backPayoff(CompanyRechargesBean companyRechargesBean, String desc);

    /**
     * 京东支付失败，减退冻结金额 个人
     *
     * @param customerRechargesBean
     * @param desc
     */
    void backPayoff(CustomerRechargesBean customerRechargesBean, String desc);

    /**
     * 批量插入员工账户
     * @param list
     * @return
     */
    int insertBatch(List<SubAccountBean> list);


}
