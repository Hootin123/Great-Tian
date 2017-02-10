package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RapidlyPayrollExcelReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,rapidly_payroll_excel
     *
     * @param id
     */
    RapidlyPayrollExcelBean selectByPrimaryKey(Long id);

    /**
     * 获取状态为待发放的工资单
     *
     * @return
     */
    List<RapidlyPayrollExcelBean> selectRapidlyPayroll();

    /**
     * 根据excel标题获取工资单
     * @param id
     * @param excelTitle
     * @return
     */
    List<RapidlyPayrollExcelBean> selectByExcelTitle(@Param("id") Long id, @Param("companyId") Long companyId, @Param("excelTitle") String excelTitle);
}