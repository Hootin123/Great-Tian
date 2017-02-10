package com.xtr.core.persistence.writer.customer;

import com.xtr.api.domain.customer.CustomersSupplementBean;

import java.util.List;

/**
 * Created by allycw3 on 2016/9/28.
 */
public interface CustomersSupplementWriterMapper {

    /**
     *
     * @param list
     * @return
     */
    int insertBatch(List<CustomersSupplementBean> list);

    /**
     * 更新是否补退到员工账户状态
     * @param customersSupplementBean
     * @return
     */
    int updateByPrimaryKeySelective(CustomersSupplementBean customersSupplementBean);

    /**
     * 删除
     * @param supplementCompanyOrderId
     * @return
     */
    int deleteByCompanyOrderId(Long supplementCompanyOrderId);
}
