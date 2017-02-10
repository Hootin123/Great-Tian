package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanySalaryExcelReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,company_salary_excel
     *
     * @param excelId
     */
    CompanySalaryExcelBean selectByPrimaryKey(Long excelId);

    /**
     * 分页查询
     *
     * @param companySalaryExcelBean
     * @return
     */
    PageList selectPageList(CompanySalaryExcelBean companySalaryExcelBean, PageBounds pageBounds);


    /***
     * 获取工资单
     *
     * @param sameDay
     * @return
     */
    List<CompanySalaryExcelBean> getPendingPayroll(@Param("sameDay") String sameDay, @Param("excelGrantState") Integer excelGrantState);

    /**
     * 获取最后N次发工资
     *
     * @param companyId
     * @param limit
     * @return
     */
    List<CompanySalaryExcelBean> getLastSalary(@Param("companyId") Long companyId, @Param("limit") Integer limit);

    /**
     * 根据企业信息获取发工资信息列表
     *
     * @param companySalaryExcelBean
     * @return
     */
    List<CompanySalaryExcelBean> selectSalaryPageList(CompanySalaryExcelBean companySalaryExcelBean);


    /**
     * 根据月份查询工资单
     *
     * @param companyId
     * @param excelMonth
     * @return
     */
    CompanySalaryExcelBean selectSalaryByMonth(@Param("companyId") Long companyId, @Param("excelMonth") Integer excelMonth,@Param("excelYear")Integer excelYear);

}