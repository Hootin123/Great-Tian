package com.xtr.core.persistence.reader.customer;

import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.dto.customer.CustomersSupplementDto;

import java.util.List;

/**
 * Created by allycw3 on 2016/9/28.
 */
public interface CustomersSupplementReaderMapper {

    /**
     * 根据企业订单ID获取补收补退信息
     * @param supplementCompanyOrderId
     * @return
     */
    List<CustomersSupplementDto> selectByCompanyOrderId(long supplementCompanyOrderId);

    /**
     * 根据企业订单ID获取符合补退的信息
     * @return
     */
    List<CustomersSupplementBean> selectBackInfoByCompanyOrderId();

    /**
     * 根据企业订单ID和员工获取员工的补收,补退详情
     * @param CustomersSupplementBean
     * @return
     */
    CustomersSupplementBean selectByCompanyOrderIdAndCustomerId(CustomersSupplementBean CustomersSupplementBean);
}
