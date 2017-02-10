package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanySalaryExcelBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface CompanySalaryExcelWriterMapper {
    /**
     * 根据主键删除数据库的记录,company_salary_excel
     *
     * @param excelId
     */
    int deleteByPrimaryKey(Long excelId);

    /**
     * 新写入数据库记录,company_salary_excel
     *
     * @param record
     */
    int insert(CompanySalaryExcelBean record);

    /**
     * 动态字段,写入数据库记录,company_salary_excel
     *
     * @param record
     */
    int insertSelective(CompanySalaryExcelBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,company_salary_excel
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanySalaryExcelBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,company_salary_excel
     *
     * @param record
     */
    int updateByPrimaryKey(CompanySalaryExcelBean record);


    /**
     * 更新工资单状态
     *
     * @param excelId
     * @param excelGrantState
     * @param oldGrantState
     * @return
     */
    int updateGrantState(@Param("excelId") Long excelId, @Param("excelGrantState") Integer excelGrantState,
                         @Param("oldGrantState") Integer oldGrantState);

    /**
     * 更新工资单实发金额  状态必须为发放中
     *
     * @param excelId
     * @param amount
     * @return
     */
    int updateSalaryTotal(@Param("excelId") Long excelId, @Param("amount") BigDecimal amount);

    /**
     * 根据企业id删除
     *
     * @param companyId
     * @return
     */
    int deleteByCompanyId(@Param("companyId") Long companyId);
}