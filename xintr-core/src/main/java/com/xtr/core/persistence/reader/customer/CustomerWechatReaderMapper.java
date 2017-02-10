package com.xtr.core.persistence.reader.customer;

import com.xtr.api.domain.customer.CustomerWechatBean;
import com.xtr.api.dto.customer.CustomerPayrollWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatDto;
import com.xtr.api.dto.customer.CustomerYearSalaryDto;

import java.util.List;
import java.util.Map;

/**
 * @author:zhangshuai
 * @date: 2016/9/7.
 */
public interface CustomerWechatReaderMapper {
    /**
     * 查询员工绑定信息
     * @param openId
     * @return
     */
    CustomerWechatBean selectCustomerWechatByOpenId(String openId);

    /**
     * 查询员工信息及最近一次发工资的时间
     * @param map
     * @return
     */
    CustomerWechatBindDto selectCustomerInfoByCondition(Map<String, Object> map);

    /**
     * 查询员工本年的工资列表
     * @param map
     * @return
     */
    CustomerYearSalaryDto selectCustomerYearSalarys(Map<String, Object> map);

    /**
     * 查询员工的工资详情
     * @param map
     * @return
     */
    CustomerPayrollWechatBindDto selectSalaryDetail(Map<String, Object> map);

    /**
     * 查询当前工资单的津贴明细
     * @param payrollId
     * @return
     */
    List<CustomerPayrollWechatBindDto> selectPayrollDetailByPayrollId(Long payrollId);

    /**
     * 查询员工当年的年度工资
     * @param  map
     * @return
     */
    List<CustomerWechatBindDto> selectYearPayorders(Map map);

    /**
     * 员工新版微信查看工资单详情
     * @param map
     * @return
     */
    CustomerPayrollWechatBindDto selectSalaryDetailBy(Map<String, Object> map);

    /**
     * 新版员工微信 查看信息
     * @param customerId
     * @return
     */
    CustomerWechatDto selectCustomerInfo(Long customerId);
}
