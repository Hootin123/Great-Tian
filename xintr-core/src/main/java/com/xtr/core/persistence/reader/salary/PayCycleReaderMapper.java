package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.PayCycleBean;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PayCycleReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,pay_cycle
     *
     * @param id
     */
    PayCycleBean selectByPrimaryKey(Long id);

    /**
     * 根据公司Id查询计薪周期表
     *
     * @param companyId
     * @return
     */
    PayCycleBean selectByCompanyId(Long companyId);


    /**
     * 根据公司ID和日期查询周期
     *
     * @param companyId
     * @param date
     * @return
     */
    PayCycleBean selectByCompanyIdAndDate(@Param("companyId") Long companyId, @Param("date") Date date);

    /**
     * 获取未生成工资单的计薪周期
     *
     * @return
     */
    List<PayCycleBean> selectPayCycle();

    /**
     * 获取未发工资的计薪周期
     *
     * @return
     */
    List<PayCycleBean> selectPayCycleSalary();

    /**
     * 根据公司id获取年度
     *
     * @param companyId
     * @return
     */
    List<PayCycleBean> selectYearByCompanyId(Long companyId);

    /**
     * 根据公司id和年度查询工资单
     *
     * @param companyId
     * @param year
     * @return
     */
    List<PayCycleBean> selectByCompanyIdAndYear(@Param("companyId") Long companyId, @Param("year") String year);

    /**
     * 根据公司id获取计薪周期年度
     *
     * @param companyId
     * @return
     */
    List<String> selectYearPayroll(@Param("companyId") Long companyId);

    /**
     * 根据公司Id，年度查询工资单
     *
     * @param companyId
     * @param year
     * @return
     */
    List<PayCycleBean> selectPayrollSummary(@Param("companyId") Long companyId, @Param("year") String year);

    /**
     * 查询最新的已审批的计薪周期
     *
     * @param companyId
     * @return
     */
    PayCycleBean selectLastApprovedInfo(@Param("companyId") Long companyId);

    /**
     * 根据计薪周期主键获取工资发放总额，税前工资，社保公积金
     *
     * @param payCycleId
     * @return
     */
    PayCycleBean getSalaryByPayCycleId(Long payCycleId);

}