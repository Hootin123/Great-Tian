package com.xtr.core.persistence.reader.customer;

import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomerShebaoOrderReaderMapper {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    CustomerShebaoOrderBean selectByPrimaryKey(Long id);

    /**
     * 查询社保订单列表
     * @param customerShebaoOrderBean
     * @return
     */
    List<CustomerShebaoOrderBean> selectShebaoOrderList(CustomerShebaoOrderBean customerShebaoOrderBean);

    /**
     * 查询一条社保订单
     * @param customerShebaoOrderBean
     * @return
     */
    CustomerShebaoOrderBean selectShebaoOrder(CustomerShebaoOrderBean customerShebaoOrderBean);

    /**
     * 查询每个企业订单的提交数量
     * @param companyOrderId
     * @param requireType
     * @param orderType
     * @return
     */
    int selectCountByCompanyOrderIdAndOrderType(@Param("companyOrderId") long companyOrderId, @Param("requireType") int requireType, @Param("orderType") int orderType);

    /**
     * 查询企业部分总额
     * @param companyOrderId
     * @return
     */
    BigDecimal selectOrgSum(@Param("companyOrderId") long companyOrderId, @Param("requireType") int requireType);

    /**
     * 查询员工人头总额
     * @param companyOrderId
     * @return
     */
    int selectCustomerCount(@Param("companyOrderId") long companyOrderId);

    /**
     * 根据周期ID查询员工社保总额
     * @param cycleId
     * @param month
     * @param count
     * @return
     */
    List<Map> selectEmpSumByCycleId(@Param("cycleId") Long cycleId, @Param("orderMonth") Date month, @Param("protocolCount") int count);

    /**
     * 根据回调信息查询员工订单
     * @param companyOrderId
     * @param customerId
     * @param orderMonth
     * @param requireType
     * @return
     */
    CustomerShebaoOrderBean selectByNotifiy(@Param("companyOrderId") Long companyOrderId, @Param("customerId") Long customerId, @Param("orderMonth") Date orderMonth, @Param("requireType") int requireType);

    /**
     * 查询员工上一次的缴纳订单
     * @param currentCompanyOrderId
     * @param customerId
     * @return
     */
    List<CustomerShebaoOrderBean> selectCustomerLastOrder(@Param("currentCompanyOrderId") Long currentCompanyOrderId, @Param("customerId") Long customerId);

    /**
     * 根据缴纳日期查询社保缴纳订单数量
     * @param overDate
     * @param customerId
     * @return
     */
    int selectOverMonth(@Param("overDate") Date overDate, @Param("customerId") long customerId);

    /**
     * 查询最小的社保或者公积金订单
     * @param customerShebaoOrderBean
     * @return
     */
    CustomerShebaoOrderBean selectByCodition(CustomerShebaoOrderBean customerShebaoOrderBean);

    Long selectCustomerLastOrderId(@Param("customerId") Long customerId, @Param("currentCompanyOrderId") Long currentCompanyOrderId);

    /**
     * 查询员工补缴开始结束日期
     * @param customerId
     * @param currentCompanyOrderId
     * @param code
     * @return
     */
    Map selectBjStartEndDate(@Param("customerId") long customerId, @Param("currentCompanyOrderId") Long currentCompanyOrderId, @Param("rtype") int code);

    /**
     * 查询补缴基数
     * @param customerId
     * @param currentCompanyOrderId
     * @param code
     * @return
     */
    List<BigDecimal> selectBjBase(@Param("customerId") long customerId, @Param("currentCompanyOrderId") Long currentCompanyOrderId, @Param("rtype") int code);

    /**
     * 查询补缴月份
     * @param customerId
     * @param currentCompanyOrderId
     * @param code
     * @return
     */
    List selectBjDate(@Param("customerId") long customerId, @Param("currentCompanyOrderId") Long currentCompanyOrderId, @Param("rtype") int code);

    /**
     * 查询该公司错误订单下的所有员工id
     * @param companyShebaoOrderId
     * @return
     */
   List<Map<String,Object>> selectErrorCustomerIdList(Long companyShebaoOrderId);

    /**
     * 根据企业订单ID获取社保公积金订单
     * @param companyShebaoOrderId
     * @return
     */
    List<CustomerShebaoOrderBean> selectByCompanyOrderId(Long companyShebaoOrderId);

    /**
     * 查询是否离职
     * @param customerId
     * @return
     */
    int selectDismiss(Long customerId);

    /**
     *获取成功的员工订单数据
     * @param customerShebaoOrderBean
     * @return
     */
    List<CustomerShebaoOrderBean> selectSuccessShebaoOrderList(CustomerShebaoOrderBean customerShebaoOrderBean);
}