package com.xtr.api.service.shebao;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;

/**
 * Created by allycw3 on 2016/10/13.
 */
public interface ShebaoDepoitService {
    /**
     * 社保公积金扣款
     * @param companyRechargeId
     * @return
     */
    ResultResponse shebaoOrderDebit(Long companyRechargeId, String salaryOrderFlag, CompanyRechargesBean companyRechargesBean, CompanyRechargesBean checkBean)throws BusinessException,Exception;

    /**
     * 扣款前验证垫付金额\还需支付金额\可用余额是否有变动
     *
     * @param checkBean
     * @param paymentBean
     * @param companyId
     */
    void checkDifferentAmount(CompanyRechargesBean checkBean, CompanyRechargesBean paymentBean, Long companyId);

    /**
     * 获取红包金额信息
     * @param comapnyId
     * @param paymentBean
     */
    RedEnvelopeBean assemRedWalletInfo(Long comapnyId, CompanyRechargesBean paymentBean);

    /**
     * 获取最新的垫付金额\垫付比例及红包
     *
     * @param companyId
     * @param totalWages
     * @return
     */
    CompanyRechargesBean queryPaymentInfoForShebao(Long companyId, BigDecimal totalWages);

    /**
     * 生成充值订单
     *
     * @param companyRechargesBean
     * @return
     */
    void generateCzOrderForShebao(CompanyRechargesBean companyRechargesBean);

    /**
     * 检查企业账户协议状态
     *
     * @param companyId
     */
    void checkShebaoProtocol(Long companyId);

    /**
     * 社保公积金充值订单生成
     * @param companyRechargesBean
     * @param companyId
     */
    void shebaoRecharge(CompanyRechargesBean companyRechargesBean,Long companyId);
}
