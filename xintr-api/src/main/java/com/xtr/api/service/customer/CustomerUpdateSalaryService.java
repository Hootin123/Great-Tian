package com.xtr.api.service.customer;

import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author Xuewu 定薪
 * @Date 2016/8/17.
 */
public interface CustomerUpdateSalaryService {


    /**
     * 新增调薪记录
     * @param customerId
     * @param salary
     * @param date
     * @param reason
     * @return
     */
    boolean insertSalaryRecord(long customerId, BigDecimal salary, Date date, String reason) throws Exception;


    /**
     * 根据传入日期获得员工的最近一次调薪金额
     * @param customerId
     * @param date
     * @return
     */
    BigDecimal getLastSalary(long customerId, Date date);


    /**
     * 根据时间区间 计算工作日
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException
     */
    List<Date> calculateWorkDay(Date startDate, Date endDate) throws BusinessException;


    /**
     * 根据计薪周期计算应出勤天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException
     */
    int calculateWorkNumberDay(Date startDate, Date endDate) throws BusinessException;


    /**
     * 刷新员工基本工资
     * @param customersBean
     * @return
     * @throws Exception
     */
    CustomersBean calculateSalary(CustomersBean customersBean, PayCycleBean payCycleBean, List<Date> workDays) throws BusinessException;


    /**
     * 刷新员工基本工资
     * @param customerId
     * @return
     * @throws BusinessException
     */
    CustomersBean calculateSalary(Long customerId) throws BusinessException;


    /**
     *  根据公司ID更新员工基本工资
      * @param companyId
     * @throws BusinessException
     */
    void calulateSalaryByCompanyId(long companyId) throws BusinessException;


    /**
     * 定薪
     * @param customerId
     * @param probationSalary
     * @param regularSalary
     * @return
     */
    CustomersBean setCustomerSalary(long customerId, BigDecimal probationSalary, BigDecimal regularSalary) throws Exception;


    /**
     * 批量导入定薪
     * @param dataFromExcel
     * @param isImport
     * @return
     */
    Map<String, String> setSalaryBatch(List<Object[]> dataFromExcel, boolean isImport, long companyId);

    /**
     * 批量调薪
     * @param dataFromExcel
     * @param isImport
     * @return
     */
    Map<String, String> updateSalaryBatch(List<Object[]> dataFromExcel, boolean isImport, long companyId);


    /**
     * 批量设置工资明细
     * @param dataFromExcel
     * @param isImport
     * @return
     */
    Map<String, String> updatePreTaxAfterTaxBatch(List<Object[]> dataFromExcel, boolean isImport, long companyId);


    /**
     * 批量设置考勤
     * @param dataFromExcel
     * @param doImport
     * @return
     */
    Map<String, String> updateAbsenceDayBatch(List<Object[]> dataFromExcel, boolean doImport, long companyId);

    /**
     * 更新员工最后一条工资单
     * @param customerId
     */
    void updatePayRoll(long customerId) throws Exception;

    /**
     * 更新员工最后一条工资单
     * @param customersBean
     * @throws Exception
     */
    void updatePayRoll(CustomersBean customersBean) throws Exception;
}
