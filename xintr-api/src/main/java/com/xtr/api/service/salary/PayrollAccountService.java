package com.xtr.api.service.salary;

import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.salary.*;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.salary.CustomerPayrollQueryDto;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>工资核算</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/16 17:37
 */
public interface PayrollAccountService {

    /**
     * 根据公司id、员工id生成工资单
     *
     * @param companyId
     * @param customerId
     */
    void generatePayroll(Long companyId, Long customerId) throws Exception;

    /**
     * 分页查询工资单,不存在则自动生成
     *
     * @param customerPayrollDto
     * @return
     */
//    ResultResponse selectPageList(CustomerPayrollDto customerPayrollDto) throws Exception;

    /**
     * 根据工资单主键查询津贴
     *
     * @param customerPayrollId
     * @return
     */
//    List<CustomerPayrollDetailBean> selectAllowanceByPayrollId(Long customerPayrollId);

    /**
     * 根据工资单主键查询奖金
     *
     * @param customerPayrollId
     * @return
     */
//    List<CustomerPayrollDetailBean> selectBonusByPayrollId(Long customerPayrollId);

    /**
     * 分页查询工资单
     *
     * @param customerPayrollDto
     * @param pageBounds
     * @return
     */
//    ResultResponse selectPageList(CustomerPayrollDto customerPayrollDto, Pager pageBounds) throws Exception;

    /**
     * 筛选工资单
     *
     * @param customerPayrollQueryDto
     * @return
     */
//    ResultResponse selectCustomerPayroll(CustomerPayrollQueryDto customerPayrollQueryDto);

    /**
     * 查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
//    List<CustomerPayrollDto> selectCustomerPayroll(CustomerPayrollDto customerPayrollDto) throws Exception;

    /**
     * 根据计薪周期生成工资单
     *
     * @param payCycleBean
     */
    void generatePayroll(PayCycleBean payCycleBean) throws Exception;

    /**
     * 生成工资单
     *
     * @param list         需要生成工资单的人员列表
     * @param map          已生成工资单的工资单列表
     * @param payCycleBean 计薪周期
     * @throws Exception
     */
    void generatePayroll(List<CustomersDto> list, final Map<Long, CustomerPayrollBean> map, final PayCycleBean payCycleBean) throws Exception;

    /**
     * 更新已存在的工资单
     *
     * @param customerPayrollBean      工资单
     * @param payRuleBean              计薪规则
     * @param allowanceSettingBeanList 津贴设置
     * @param customersDto             人员信息
     * @param attendanceNumberDay      计薪周期内应出勤天数
     * @param allowanceMap             津贴适用的员工
     * @param payCycleBean             计薪周期
     */
    void updateCustomerPayroll(CustomerPayrollBean customerPayrollBean, PayRuleBean payRuleBean,
                               List<AllowanceSettingBean> allowanceSettingBeanList, CustomersDto customersDto,
                               BigDecimal attendanceNumberDay, Map<Long, Map<Long, Long>> allowanceMap,
                               PayCycleBean payCycleBean);

    /**
     * 计算奖金
     *
     * @param customerPayrollBean
     * @param bonusSettingsBeanList 奖金设置
     */
//    void calcBonus(CustomerPayrollBean
//                           customerPayrollBean, List<BonusSettingsBean> bonusSettingsBeanList);

    /**
     * 津贴计算
     *
     * @param customersDto
     * @param customerPayrollBean
     * @param allowanceSettingBeanList
     * @param allowanceMap             津贴适用的员工
     * @param attendanceNumberDay      计薪周期内应出勤天数
     */
    CustomerPayrollBean calcAllowance(CustomersDto customersDto,
                                      CustomerPayrollBean customerPayrollBean, List<AllowanceSettingBean> allowanceSettingBeanList,
                                      Map<Long, Map<Long, Long>> allowanceMap, BigDecimal attendanceNumberDay);

    /**
     * 更新基本工资
     *
     * @param customerPayrollDto
     * @return
     */
    ResultResponse updateBasePay(CustomerPayrollDto customerPayrollDto, Integer type) throws Exception;

    /**
     * 补充工资单明细
     *
     * @param customerPayrollDto
     * @return
     */
    ResultResponse updateBasePay(CustomerPayrollDto customerPayrollDto);

    /**
     * 修改奖金
     *
     * @param parentId
     * @param id
     * @param detailValue
     * @return
     */
    ResultResponse bonusUpdate(Long parentId, Long id, BigDecimal detailValue);

    /**
     * 审批计薪周期对应的工资单
     *
     * @param payCycleId
     * @return
     */
    ResultResponse approvalPayroll(Long payCycleId) throws Exception;

    /**
     * 检查企业账户协议状态
     *
     * @param companyId
     */
    void checkCompany(Long companyId);

    /**
     * 发工资
     *
     * @param payCycleId
     * @return
     */
    ResultResponse payOff(Long payCycleId, CompanyMembersBean companyMembersBean);

    /**
     * 获取工资发放总金额
     *
     * @param payCycleId
     * @return
     */
//    BigDecimal getTotalWages(Long payCycleId);

    /**
     * 获取发薪日期
     *
     * @param payCycleBean
     * @return
     */
    Date getPayDay(PayCycleBean payCycleBean);

    /**
     * 新增奖金
     *
     * @param bonusSettingsBean
     * @throws BusinessException
     */
//    void saveBonusSettings(BonusSettingsBean bonusSettingsBean) throws BusinessException, ExecutionException, InterruptedException;

    /**
     * 生成订单,并返回订单本身
     *
     * @param companyId
     * @param payCycleId
     * @return
     */
    CompanyRechargesBean generateSalaryOrder(Long companyId, Long payCycleId);

    /**
     * 发工资扣款
     *
     * @param payCycleId
     * @return
     */
    ResultResponse salaryOrderDebit(Long payCycleId, String salaryOrderFlag, CompanyRechargesBean companyRechargesBean, CompanyRechargesBean checkBean);

    /**
     * 判断用户是否有访问账户权限
     *
     * @param companyMembersBean
     * @return true有访问权限 false无访问权限
     */
    boolean validateCustomerRole(CompanyMembersBean companyMembersBean);

    /**
     * 获取垫付金额及垫付比例
     *
     * @param companyId
     * @param totalWages
     * @return
     */
//    CompanyRechargesBean queryPaymentInfo(Long companyId, BigDecimal totalWages);

    /**
     * 我的账户进入详情页面,并继续付款显示的发工资详情页面信息
     * 垫付\还需支付\可用余额取最新的值
     *
     * @param rechargeId
     * @param totalWages 工资发放总额
     * @return
     */
    CompanyRechargesBean showSalaryOrder(Long rechargeId,BigDecimal totalWages);

    /**
     * 扣款前验证垫付金额\还需支付金额\可用余额是否有变动
     *
     * @param checkBean
     * @param paymentBean
     * @param companyId
     */
    void checkDifferentAmount(CompanyRechargesBean checkBean, CompanyRechargesBean paymentBean, Long companyId);

    /**
     * 生成充值订单
     *
     * @param companyRechargesBean
     * @return
     */
    ResultResponse generateCzOrder(CompanyRechargesBean companyRechargesBean);

    /**
     * 根据来源id删除奖金
     *
     * @param resourceId
     * @return
     */
//    int deleteByResourceId(Long resourceId);
}
