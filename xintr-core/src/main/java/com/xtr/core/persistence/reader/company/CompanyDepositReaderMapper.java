package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.company.CompanyDepositBean;
import com.xtr.api.dto.company.CompanyDepositDto;

import java.util.List;

public interface CompanyDepositReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,company_deposit
     *
     * @param depositId
     */
    CompanyDepositBean selectByPrimaryKey(Long depositId);

    /**
     * 根据企业ID获取提现账户
     * @param depositCompanyId
     * @return
     */
    CompanyDepositBean selectByCompanyId(Long depositCompanyId);

    /**
     * 根据过滤条件获取所有用户提现账户
     * @param companyDepositDto
     * @return
     */
    List<CompanyDepositDto> selectByCondition(CompanyDepositDto companyDepositDto);

    /**
     * 根据过滤条件获取所有用户提现账户(分页)
     * @param companyDepositDto
     * @return
     */
    List<CompanyDepositDto> selectByCondition(CompanyDepositDto companyDepositDto,PageBounds pageBounds);
}