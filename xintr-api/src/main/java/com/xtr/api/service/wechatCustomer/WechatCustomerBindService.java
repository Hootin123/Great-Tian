package com.xtr.api.service.wechatCustomer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.dto.customer.CustomerPayrollWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatBindDto;
import com.xtr.api.dto.customer.CustomerWechatDto;
import com.xtr.api.dto.customer.CustomerYearSalaryDto;
import com.xtr.comm.basic.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * @author:zhangshuai
 * @date: 2016/9/7.
 */
public interface WechatCustomerBindService {
    /**
     * 员工绑定
     * @param phone     手机号
     * @param phoneCode 短信验证码
     * @param map       微信号信息map
     * @return
     */
    ResultResponse customerBind(String phone, String phoneCode, Map<String, String> map) throws Exception;

    /**
     * 查询员工信息 与当前最近一次发工资的时间
     * @param map
     * @return
     */
    CustomerWechatBindDto selectCustomerInfoByCondition(Map<String, Object> map) throws BusinessException;

    /**
     * 查询本年度的员工的工资列表
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
     * 查询所有的津贴
     * @param payrollId
     * @return
     */
    List<CustomerPayrollWechatBindDto> selectPayrollDetailByPayrollId(Long payrollId);

    /**
     * 员工微信 查询年度工资单
     * @param companyId
     * @param customerId
     * @param year
     * @return
     */
    List<CustomerWechatBindDto> selectYearPayorders(String companyId, String customerId, String year) throws Exception;

    /**
     * 新版员工微信查看当前工资单详情
     * @param payrollId
     * @param companyId
     * @param customerId
     * @return
     */
    CustomerPayrollWechatBindDto selectSalaryDetailBy(String payrollId, String companyId, String customerId,String payCycleId);

    /**
     * 新版员工微信查看个人资料
     * @param customerId
     * @return
     */
    CustomerWechatDto selectCustomerInfo(Long customerId);
}
