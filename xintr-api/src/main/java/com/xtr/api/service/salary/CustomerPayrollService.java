package com.xtr.api.service.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.salary.BonusSettingsBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.CustomerPayrollDetailBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.salary.CustomerPayrollQueryDto;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/9/21 9:40
 */
public interface CustomerPayrollService {

    /**
     * 查询所有工资单
     *
     * @param customerPayrollDto
     * @return
     */
    List<CustomerPayrollDto> selectCustomerPayroll(CustomerPayrollDto customerPayrollDto);

    /**
     * 分页查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
    ResultResponse selectCustomerPayrollPageList(CustomerPayrollDto customerPayrollDto);

    /**
     * 分页查询工资单,不存在则自动生成
     *
     * @param customerPayrollDto
     * @return
     */
    ResultResponse selectPageList(CustomerPayrollDto customerPayrollDto) throws Exception;

    /**
     * 根据计薪周期主键获取工资单
     *
     * @param customerPayrollDto
     */
    Map<Long, CustomerPayrollBean> getCustomerPayrollByPayCycleId(CustomerPayrollDto customerPayrollDto) throws Exception;

    /**
     * 筛选工资单
     *
     * @param customerPayrollQueryDto
     * @return
     */
    ResultResponse selectCustomerPayroll(CustomerPayrollQueryDto customerPayrollQueryDto);

    /**
     * 获取工资发放总金额
     *
     * @param payCycleId
     * @return
     */
    BigDecimal getTotalWages(Long payCycleId);

    /**
     * 新增奖金
     *
     * @param bonusSettingsBean
     * @throws BusinessException
     */
    void saveBonusSettings(BonusSettingsBean bonusSettingsBean) throws Exception;

    /**
     * 计算奖金
     *
     * @param customerPayrollBean
     * @param bonusSettingsBeanList
     */
    CustomerPayrollBean calcBonus(CustomerPayrollBean customerPayrollBean,
                                                          List<BonusSettingsBean> bonusSettingsBeanList, List<CustomerPayrollDetailBean> list);

    /**
     * 根据来源id删除奖金
     *
     * @param companyId
     * @param resourceId
     * @return
     */
    int deleteByResourceId(Long companyId, Long resourceId) throws Exception;

    /**
     *  动态字段,写入数据库记录,customer_payroll
     *
     * @param record
     */
    int insertSelective(CustomerPayrollBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_payroll
     *
     * @param record
     */
//    int updateByPrimaryKeySelective(CustomerPayrollBean record);

    /**
     * 更新员工工资单状态
     *
     * @param cycleId
     * @return
     */
    int updatePayRollStatusByCycle( long cycleId);

}
