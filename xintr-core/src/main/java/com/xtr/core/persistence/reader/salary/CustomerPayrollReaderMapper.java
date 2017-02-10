package com.xtr.core.persistence.reader.salary;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.dto.customer.CustomerUnPayOrderDto;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.salary.CustomerPayrollQueryDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CustomerPayrollReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,customer_payroll
     *
     * @param id
     */
    CustomerPayrollBean selectByPrimaryKey(Long id);

    /**
     * 分页查询工资单
     *
     * @param customerPayrollDto
     * @param pageBounds
     * @return
     */
    PageList<CustomerPayrollDto> selectPageList(CustomerPayrollDto customerPayrollDto, PageBounds pageBounds);

    /**
     * 查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
    List<CustomerPayrollDto> selectPageList(CustomerPayrollDto customerPayrollDto);

    /**
     * 根据Id查询工资单详情
     *
     * @param id
     * @return
     */
    CustomerPayrollDto selectById(Long id);

    /**
     * 根据聘用形式、员工状态、所属部门、津贴方式查询工资单
     *
     * @param customerPayrollQueryDto
     * @return
     */
    PageList<CustomerPayrollDto> selectCustomerPayroll(CustomerPayrollQueryDto customerPayrollQueryDto, PageBounds pageBounds);

    /**
     * 根据公司id、计薪id、员工id获取工资单
     *
     * @param companyId
     * @param payCycleId
     * @return
     */
    CustomerPayrollBean selectByCompanyIdAndPayCycleId(@Param("companyId") Long companyId, @Param("payCycleId") Long payCycleId, @Param("customerId") Long customerId);

    /**
     * 根据计薪周期id获取所有工资单
     *
     * @param payCycleId
     * @return
     */
    List<CustomerPayrollBean> selectByPayCycleId(Long payCycleId);

    /**
     * 根据计薪id获取工资未发放的工资单
     *
     * @param payCycleId
     * @return
     */
    List<CustomerPayrollBean> selectPayrollByPayCycleId(Long payCycleId);

    /**
     * 根据员工ID查询最新工资单
     *
     * @param customerId
     * @param payCycleId
     * @return
     */
    CustomerPayrollBean selectByCustomerId(@Param("customerId") Long customerId, @Param("payCycleId") Long payCycleId);

    /**
     * 获取工资发放总金额
     *
     * @param payCycleId
     * @return
     */
    BigDecimal getTotalWages(Long payCycleId);

    /**
     * 获取发薪总人数
     *
     * @param payCycleId
     * @return
     */
    Integer getTotalPeopleNumber(Long payCycleId);

    /**
     * 查询未支付订单
     *
     * @param dto
     * @param pageBounds
     * @return
     */
    PageList<Map<String, Object>> selectPayFailedOrder(CustomerUnPayOrderDto dto, PageBounds pageBounds);

    /**
     * 根据公司id、计薪周期id查询需要重新计算的工资单
     *
     * @param companyId
     * @param payCycleId
     * @return
     */
    List<CustomerPayrollBean> selectChangeCustomerPayroll(@Param("companyId") Long companyId, @Param("payCycleId") Long payCycleId);

    /**
     * 根据过滤条件查询列表
     *
     * @param dto
     * @param pageBounds
     * @return
     */
    PageList<Map<String, Object>> selectPayFailedOrderList(CustomerUnPayOrderDto dto, PageBounds pageBounds);

}