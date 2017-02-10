package com.xtr.core.persistence.writer.customer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.domain.customer.CustomersBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CustomersWriterMapper {
    /**
     * 根据主键删除数据库的记录,customers
     *
     * @param customerId
     */
    int deleteByPrimaryKey(Long customerId);

    /**
     * 新写入数据库记录,customers
     *
     * @param record
     */
    int insert(CustomersBean record);

    /**
     * 动态字段,写入数据库记录,customers
     *
     * @param record
     */
    int insertSelective(CustomersBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,customers
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomersBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,customers
     *
     * @param record
     */
    int updateByPrimaryKey(CustomersBean record);

    /**
     * 冻结企业员工账号
     *
     * @param customerId
     * @return
     */
    int frozenCustomers(Long customerId);

    /**
     * 启用企业员工账号
     *
     * @param customerId
     * @return
     */
    int enableCustomers(Long customerId);

    /**
     * 修改员工所属部门
     *
     * @param customerId
     * @param deptId
     */
    void updateDeptId(@Param("customerId") Long customerId, @Param("deptId") Long deptId, @Param("deptName") String deptName);

    /**
     * 根据主键更新部分字段
     * @param customersBean
     * @return
     */
    int updateByCustomerId(CustomersBean customersBean);

    /**
     * 定薪
     * @return
     */
    int setCustomersSalary(@Param("customerId") long customerId, @Param("currentSalary") BigDecimal currentSalary, @Param("probationSalary") BigDecimal probationSalary, @Param("regularSalary") BigDecimal regularSalary);

    /**
     * 更新员工头像
     * @param map
     * @return
     */
    int updateCustomerImgByCustomerId(Map map);

    /**
     * 更改是否跳转到入职规范状态
     * @param customersBean
     * @return
     */
    int updateIsRedirectState(CustomersBean customersBean);

    /**
     * 更改是否补全资料状态
     * @param customersBean
     * @return
     */
    int updateIsComplementState(CustomersBean customersBean);

    /**
     * 更新员工社保资料审核状态
     * @param idCard
     * @param approveState
     * @param comment
     * @return
     */
    int updateShebaoStatusByIdcard(@Param("idCard") String idCard, @Param("approveState") Integer approveState, @Param("sbcomment") String comment);

    /**
     *批量更改发送短信时间
     * @param list
     * @return
     */
    int updateSendMsgTimeBatch(@Param("list") List<Long> list);

    /**
     * 批量更改未补全信息
     * @param list
     * @return
     */
    int updateInCompleteCustomersBatch(@Param("list") List<CustomersBean> list);

    /**
     * 新版员工微信 资料补全
     * @param customer
     * @return
     */
    int updateCustomerInfoByCustomerId(CustomersBean customer);
}