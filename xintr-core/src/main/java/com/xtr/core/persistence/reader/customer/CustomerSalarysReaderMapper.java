package com.xtr.core.persistence.reader.customer;

import com.xtr.api.domain.customer.CustomerSalarysBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerSalarysReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,customer_salarys
     *
     * @param salaryId
     */
    CustomerSalarysBean selectByPrimaryKey(Long salaryId);

    /**
     * 查询工资单信息
     *
     * @param customerSalarysBean
     * @return
     */
    List<CustomerSalarysBean> selectCustomerSalarys(CustomerSalarysBean customerSalarysBean);

    /**
     * 根据excelId获取工资单
     *
     * @param excelId
     * @return
     */
    List<CustomerSalarysBean> selectByExcelId(@Param("excelId") Long excelId);
}