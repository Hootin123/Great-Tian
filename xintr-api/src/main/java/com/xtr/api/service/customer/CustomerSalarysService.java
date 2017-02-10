package com.xtr.api.service.customer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.dto.customer.CustomerUnPayOrderDto;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * <p>工资单</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 18:20
 */

public interface CustomerSalarysService {

    /**
     * 根据指定主键获取一条数据库记录,customer_salarys
     *
     * @param salaryId
     */
    CustomerSalarysBean selectByPrimaryKey(Long salaryId) throws BusinessException;

    /**
     * 新写入数据库记录,customer_salarys
     *
     * @param record
     */
    int insert(CustomerSalarysBean record) throws BusinessException;

    /**
     * 根据企业id删除工资单
     *
     * @param companyId
     */
    void deleteByCompanyId(Long companyId);

    /**
     * 查询工资单信息
     *
     * @param customerSalarysBean
     * @return
     */
    List<CustomerSalarysBean> selectCustomerSalarys(CustomerSalarysBean customerSalarysBean) throws BusinessException;

    /**
     * 根据excelId获取工资单
     *
     * @param excelId
     * @return
     * @throws BusinessException
     */
    List<CustomerSalarysBean> selectByExcelId(Long excelId) throws BusinessException;

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_salarys
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerSalarysBean record);

}
