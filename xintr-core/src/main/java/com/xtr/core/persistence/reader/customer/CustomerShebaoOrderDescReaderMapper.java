package com.xtr.core.persistence.reader.customer;

import com.xtr.api.domain.customer.CustomerShebaoOrderDescBean;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerShebaoOrderDescReaderMapper {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    CustomerShebaoOrderDescBean selectByPrimaryKey(Long id);

    /**
     * 查询是否补缴社保公积金
     * @param memerId
     * @param orderId
     * @param requireType
     * @return
     */
    int selectBjShebaoGjj(@Param("memerId") Long memerId, @Param("orderId") Long orderId, @Param("requireType") int requireType);

    /**
     * 根据公司订单ID和员工ID查询
     * @param companyOrderId
     * @param customerId
     * @return
     */
    CustomerShebaoOrderDescBean selectByCompanyOrderIdAndCustomerId(@Param("companyOrderId") long companyOrderId, @Param("customerId") long customerId);

    List<CustomerShebaoOrderDto> selectShebaoOrders(CustomerShebaoOrderDto customerShebaoOrderDto);

    /**
     * 查询企业错误订单数
     * @param map
     * @return
     */
    long selectErrorOrderCount( Map map);

    /**
     * 查询订单错误列表
     * @param orderId
     * @return
     */
    List<CustomerShebaoOrderDto> selectShebaoErrorOrder(@Param("orderId") Long orderId);

    /**
     *判断是否有异常
     * @param orderId
     * @return
     */
    List<CustomerShebaoOrderDto> selectShebaoErrorOrderForNew(@Param("orderId") Long orderId);
}