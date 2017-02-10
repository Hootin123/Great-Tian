package com.xtr.core.persistence.reader.salary;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RapidlyPayrollReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,rapidly_payroll
     *
     * @param id
     */
    RapidlyPayrollBean selectByPrimaryKey(Long id);

    /**
     * 分页查询工资单
     *
     * @param rapidlyPayrollBean
     * @param pageBounds
     * @return
     */
    PageList<RapidlyPayrollBean> selectPageList(RapidlyPayrollBean rapidlyPayrollBean, PageBounds pageBounds);

    /**
     * 通过excelId查询所有工资单
     *
     * @param excelId
     * @param companyId
     * @return
     */
    List<RapidlyPayrollBean> selectByExcelId(@Param("companyId") Long companyId, @Param("excelId") Long excelId);

    /**
     * 通过excelId获取对应待发放的工资单
     *
     * @param excelId
     * @param companyId
     * @return
     */
    List<RapidlyPayrollBean> selectByExcelIdState(@Param("companyId") Long companyId, @Param("excelId") Long excelId);

    /**
     * 根据提现订单号查询工资单
     *
     * @param orderNo
     * @return
     */
    RapidlyPayrollBean selectByOrderNo(String orderNo);

    /**
     * 根据主键查询信息
     * @param id
     * @return
     */
    RapidlyPayrollBean selectByPrimaryKeyId(@Param("id") Long id);
}