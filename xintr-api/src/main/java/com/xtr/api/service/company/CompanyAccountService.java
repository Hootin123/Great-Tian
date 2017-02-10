package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.dto.company.CompanyAccountPropertyDto;
import com.xtr.comm.basic.BusinessException;

/**
 * <p>我的账户</p>
 *
 * @author 任齐
 * @createTime: 2016/7/1 9:47
 */
public interface CompanyAccountService {

    /**
     * 根据企业id查询企业账户资产
     *
     * @param companyId
     */
    CompanyAccountPropertyDto selectAccountProperty(Long companyId) throws BusinessException;

    /**
     * 根据企业id查询企业应付款数据
     *
     * @param companyId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws BusinessException
     */
    ResultResponse selectAccountBorrow(Long companyId, int pageIndex, int pageSize) throws BusinessException;

    /**
     * 查询账户余额与借款金额
     * @param memberCompanyId
     * @return
     */
    CompanyAccountPropertyDto selectAccountInfo(Long memberCompanyId) throws Exception;
}
