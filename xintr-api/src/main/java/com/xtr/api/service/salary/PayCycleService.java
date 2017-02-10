package com.xtr.api.service.salary;

import com.xtr.api.domain.salary.PayCycleBean;

import java.util.List;

/**
 * <p>计薪周期</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/19 9:54
 */

public interface PayCycleService {

    /**
     * 新写入数据库记录,pay_cycle
     *
     * @param record
     */
    int insert(PayCycleBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,pay_cycle
     *
     * @param record
     */
    int updateByPrimaryKeySelective(PayCycleBean record);

    /**
     * 根据指定主键获取一条数据库记录,pay_cycle
     *
     * @param id
     */
    PayCycleBean selectByPrimaryKey(Long id);

    /**
     * 获取未生成工资单的计薪周期
     *
     * @return
     */
    List<PayCycleBean> getPayCycleBy();

    /**
     * 根据公司Id查询计薪周期表
     *
     * @param companyId
     * @return
     */
    PayCycleBean selectByCompanyId(Long companyId);

    /**
     * 初始化计薪周期
     *
     * @param yearMonth
     * @param startDate
     * @param companyId
     * @param payWay
     * @param isSocialSecurity
     * @return
     */
    int initPayCycle(String yearMonth, String startDate, Long companyId, String payWay, String isSocialSecurity);

    /**
     * 初始化计薪周期
     *
     * @param yearMonth
     * @param startDate
     * @param endDate
     * @param companyId
     * @return
     */
    int initPayCycle(String yearMonth, String startDate, String endDate, Long companyId);

    /**
     * 生成下月计薪周期
     *
     * @param payCycleBean
     */
    int generatePayCycle(PayCycleBean payCycleBean);

    /**
     * 获取未发工资的计薪周期
     *
     * @return
     */
    List<PayCycleBean> getPayCycleSalary();

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
    List<PayCycleBean> selectByCompanyIdAndYear(Long companyId, String year);

    /**
     * 根据公司id获取计薪周期年度
     *
     * @param companyId
     * @return
     */
    List<String> selectYearPayroll(Long companyId);

    /**
     * 根据公司Id，年度查询工资单
     *
     * @param companyId
     * @param year
     * @return
     */
    List<PayCycleBean> selectPayrollSummary(Long companyId, String year);

    /**
     * 更新计薪周期工资发放状态为已发放
     *
     * @param payCycleId
     * @return
     */
    PayCycleBean updatePayCyclePayOff(Long payCycleId);

}
