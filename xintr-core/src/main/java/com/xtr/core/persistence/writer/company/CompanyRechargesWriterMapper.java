package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyRechargesBean;
import org.apache.ibatis.annotations.Param;

public interface CompanyRechargesWriterMapper {
    /**
     *  根据主键删除数据库的记录,company_recharges
     *
     * @param rechargeId
     */
    int deleteByPrimaryKey(Long rechargeId);

    /**
     *  新写入数据库记录,company_recharges
     *
     * @param record
     */
    int insert(CompanyRechargesBean record);

    /**
     *  动态字段,写入数据库记录,company_recharges
     *
     * @param record
     */
    int insertSelective(CompanyRechargesBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_recharges
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanyRechargesBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,company_recharges
     *
     * @param record
     */
    int updateByPrimaryKey(CompanyRechargesBean record);

    /**
     * 根据企业id删除数据库记录
     *
     * @param companyId
     * @return
     */
    int deleteByComapnyId(@Param("companyId") Long companyId);

    /**
     * 更新垫付与实际支付额度
     * @param record
     * @return
     */
    int updateScaleAndRealAmount(CompanyRechargesBean record);
}