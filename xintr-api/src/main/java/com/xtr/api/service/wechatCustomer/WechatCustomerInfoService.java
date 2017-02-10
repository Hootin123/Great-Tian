package com.xtr.api.service.wechatCustomer;

import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersPersonalBean;
import com.xtr.comm.basic.BusinessException;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/9/6 9:15
 */
public interface WechatCustomerInfoService {

    /**
     * 员工修改基本信息
     * @param customersBean
     * @param customersPersonalBean
     * @throws BusinessException
     */
    void modifyMainInfo(CustomersBean customersBean, CustomersPersonalBean customersPersonalBean)throws BusinessException;

    /**
     * 员工编辑三大信息中的基本信息
     * @param customersBean
     * @throws BusinessException
     */
    void modifyEditBaseInfo(CustomersBean customersBean)throws BusinessException;

    /**
     * 员工编辑三大信息中的个人信息
     * @param customersPersonalBean
     * @throws BusinessException
     */
    void modifyEditPersonalInfo(CustomersPersonalBean customersPersonalBean)throws BusinessException;
}
