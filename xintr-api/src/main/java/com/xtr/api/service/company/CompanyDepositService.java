package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepositBean;
import com.xtr.api.dto.company.CompanyDepositDto;

import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/5 14:44
 */
public interface CompanyDepositService {

    /**
     *  根据主键删除数据库的记录,company_deposit
     *
     * @param depositId
     */
    int deleteByPrimaryKey(Long depositId);

    /**
     *  新写入数据库记录,company_deposit
     *
     * @param record
     */
    int insert(CompanyDepositBean record);

    /**
     *  动态字段,写入数据库记录,company_deposit
     *
     * @param record
     */
    int insertSelective(CompanyDepositBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_deposit
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyDepositBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_deposit
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyDepositBean record);

    /**
     *  根据指定主键获取一条数据库记录,company_deposit
     *
     * @param depositId
     */
    CompanyDepositBean selectByPrimaryKey(Long depositId);

    /**
     * 根据企业ID获取提现账户
     * @param companyId
     * @return
     */
    CompanyDepositBean selectByCompanyId(Long companyId);

    /**
     * 根据过滤条件获取所有用户提现账户
     * @param companyDepositDto
     * @return
     */
    ResultResponse selectByCondition(CompanyDepositDto companyDepositDto);
}
