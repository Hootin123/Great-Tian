package com.xtr.api.service.company;


import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>企业上传工资文档</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:09
 */
public interface CompanySalaryExcelService {

    /**
     * 根据指定主键获取一条数据库记录,company_salary_excel
     *
     * @param excelId
     */
    CompanySalaryExcelBean selectByPrimaryKey(Long excelId);


    /**
     * 新写入数据库记录,company_salary_excel
     *
     * @return
     */
    Long insert(CompanySalaryExcelBean companySalaryExcelBean) throws BusinessException;

    /**
     * 分页查询
     *
     * @param companySalaryExcelBean
     * @return
     */
    ResultResponse selectPageList(CompanySalaryExcelBean companySalaryExcelBean);

    /**
     * 根据主键删除数据库的记录,company_salary_excel
     *
     * @param excelId
     */
    int deleteByPrimaryKey(Long excelId) throws BusinessException;

    /**
     * 根据企业id删除
     *
     * @param companyId
     * @return
     * @throws BusinessException
     */
    int deleteByCompanyId(Long companyId) throws BusinessException;

    /**
     * 获取待处理工资单
     *
     * @return
     * @throws BusinessException
     */
    List<CompanySalaryExcelBean> getPendingPayroll(String sameDay, Integer excelGrantState) throws BusinessException;

    /**
     * 更新工资单状态
     *
     * @param excelId
     * @param excelGrantState
     * @param oldGrantState
     * @return
     */
    int updateGrantState(Long excelId, Integer excelGrantState, Integer oldGrantState);


    /**
     * 更新工资单实发金额  状态必须为发放中
     *
     * @param excelId
     * @param amount
     * @return
     */
    int updateSalaryTotal(Long excelId, BigDecimal amount);

    /**
     * 根据企业编号获取发工资信息
     *
     * @param companySalaryExcelBean
     * @return
     */
    ResultResponse selectSalaryPageList(CompanySalaryExcelBean companySalaryExcelBean);

    /**
     * 根据月份查询工资单
     *
     * @param companyId
     * @param month
     * @return
     */
    CompanySalaryExcelBean selectSalaryByMonth(Long companyId, Integer month, Integer year);
}
