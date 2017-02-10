package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.salary.*;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.customer.RealExpenseDto;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.shebao.CustomerShebaoSumDto;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.customer.CustomerUpdateSalaryService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.salary.*;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.reader.salary.*;
import com.xtr.core.persistence.writer.account.SubAccountWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import com.xtr.core.persistence.writer.salary.PayCycleWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * <p>工资核算</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/17 11:19
 */
@Service("payrollAccountService")
public class PayrollAccountServiceImpl implements PayrollAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayrollAccountServiceImpl.class);

    @Resource
    private CustomerPayrollReaderMapper customerPayrollReaderMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private PayCycleWriterMapper payCycleWriterMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private PayRuleReaderMapper payRuleReaderMapper;

    @Resource
    private AllowanceSettingReaderMapper allowanceSettingReaderMapper;

    @Resource
    private BonusSettingsReaderMapper bonusSettingsReaderMapper;

    @Resource
    private CustomerUpdateSalaryService customerUpdateSalaryService;

    @Resource
    private AllowanceApplyService allowanceApplyService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private CustomerExpenseService customerExpenseService;

    @Resource
    private CustomerPayrollService customerPayrollService;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Resource
    private SubAccountWriterMapper subAccountWriterMapper;

    @Resource
    private RapidlyPayrollExcelService rapidlyPayrollExcelService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;


    /**
     * 根据公司id、员工id生成工资单
     *
     * @param companyId
     * @param customerId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void generatePayroll(Long companyId, Long customerId) throws Exception {
        LOGGER.info("公司id【" + companyId + "】,员工id【" + customerId + "】开始生成工资单···");
        //根据公司id获取计薪周期
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
        if (payCycleBean != null) {
            //判断员工是否已生成工资单
            CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByCompanyIdAndPayCycleId(companyId, payCycleBean.getId(), customerId);
            CustomersDto customersDto = customersReaderMapper.selectByCustomerId(customerId, payCycleBean.getStartDate(), payCycleBean.getEndDate());
            if (customersDto != null) {
                List<CustomersDto> list = new ArrayList<>();
                list.add(customersDto);
                Map<Long, CustomerPayrollBean> map = new HashMap<>();
                if (customerPayrollBean != null) {
                    //已生成工资单 则更新工资单
                    customerPayrollBean.setIsPayruleUpdate(1);
                    customerPayrollBean.setIsAllowanceUpdate(1);
                    customerPayrollBean.setIsBonusUpdate(1);
                    map.put(customerPayrollBean.getCustomerId(), customerPayrollBean);
                }
                //生成工资单
                generatePayroll(list, map, payCycleBean);
            }
        }
    }

    /**
     * 根据计薪周期生成工资单
     *
     * @param payCycleBean
     */
    public void generatePayroll(PayCycleBean payCycleBean) throws Exception {
        //根据公司id获取计薪周期
//        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(payCycleBean.getCompanyId());
        if (payCycleBean != null) {
            //根据工资主键获取所有员工
            List<CustomersDto> list = customersReaderMapper.selectByCompanyId(payCycleBean.getCompanyId(), payCycleBean.getStartDate(), payCycleBean.getEndDate());
            if (list != null && !list.isEmpty()) {
                CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
                customerPayrollDto.setCompanyId(payCycleBean.getCompanyId());
                customerPayrollDto.setCustomerList(list);
                customerPayrollDto.setPayCycleId(payCycleBean.getId());
                //获取所有的工资单
                Map<Long, CustomerPayrollBean> map = customerPayrollService.getCustomerPayrollByPayCycleId(customerPayrollDto);
                //生成工资单
                generatePayroll(list, map, payCycleBean);
            }
            payCycleBean.setIsGeneratePayroll(1);
            //更新计薪周期
            payCycleWriterMapper.updateByPrimaryKeySelective(payCycleBean);
        } else {
//            throw new BusinessException("公司id【" + companyId + "】计薪周期不存在");
        }
    }

    /**
     * 生成工资单
     *
     * @param list         需要生成工资单的人员列表
     * @param map          已生成工资单的工资单列表
     * @param payCycleBean 计薪周期
     * @throws Exception
     */
    public void generatePayroll(List<CustomersDto> list, final Map<Long, CustomerPayrollBean> map, final PayCycleBean payCycleBean) throws Exception {
        if (!list.isEmpty()) {
            //获取计薪周期内应出勤天数
            final BigDecimal attendanceNumberDay = new BigDecimal(customerUpdateSalaryService.calculateWorkNumberDay(payCycleBean.getStartDate(), payCycleBean.getEndDate()));
            //获取计薪规则
            final PayRuleBean payRuleBean = payRuleReaderMapper.selectByCompanyId(payCycleBean.getCompanyId());
            //获取津贴设置
            final List<AllowanceSettingBean> allowanceSettingBeanList = getAllowanceSettingBeanList(payCycleBean.getCompanyId());
            //获取津贴适用的员工
            final Map<Long, Map<Long, Long>> allowanceMap = allowanceApplyService.getCompanyAllowanceApplyMembers(payCycleBean.getCompanyId());
            //获取奖金设置
            final List<BonusSettingsBean> bonusSettingsBeanList = getBonusSettingsBeanList(payCycleBean.getCompanyId());
            if (payRuleBean == null) {
                throw new BusinessException("companyId【" + payCycleBean.getCompanyId() + "】算工资前请先进行【薪资设置】");
            }
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<>();
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            for (final CustomersDto customersDto : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //生成工资单
                            generatePayroll(payRuleBean, allowanceSettingBeanList, bonusSettingsBeanList, customersDto, map, attendanceNumberDay, allowanceMap, payCycleBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("customerId【" + customersDto.getCustomerId() + "】出现异常···" + e.getMessage(), e);
                        }
                    }
                }));

            }
            //等待处理结果
            for (Future f : rowResult) {
                f.get();
            }
            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
            pool.shutdown();
            //出现异常
            if (!errorList.isEmpty()) {
                //抛出异常信息
                throw new BusinessException(errorList.get(0).getMessage());
            }
        }
    }

    /**
     * 更新工资单
     *
     * @param list
     * @param payCycleBean
     */
    public void updateCustomerPayroll(List<CustomerPayrollBean> list, final PayCycleBean payCycleBean) throws ExecutionException, InterruptedException {
        if (!list.isEmpty()) {
            //获取计薪周期内应出勤天数
            final BigDecimal attendanceNumberDay = new BigDecimal(customerUpdateSalaryService.calculateWorkNumberDay(payCycleBean.getStartDate(), payCycleBean.getEndDate()));
            //获取计薪规则
            final PayRuleBean payRuleBean = payRuleReaderMapper.selectByCompanyId(payCycleBean.getCompanyId());
            //获取津贴设置
            final List<AllowanceSettingBean> allowanceSettingBeanList = getAllowanceSettingBeanList(payCycleBean.getCompanyId());
            //获取津贴适用的员工
            final Map<Long, Map<Long, Long>> allowanceMap = allowanceApplyService.getCompanyAllowanceApplyMembers(payCycleBean.getCompanyId());
            if (payRuleBean == null) {
                throw new BusinessException("companyId【" + payCycleBean.getCompanyId() + "】算工资前请先进行【薪资设置】");
            }
            final List<Exception> errorList = new ArrayList();
            List<Future> rowResult = new CopyOnWriteArrayList<>();
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            for (final CustomerPayrollBean customerPayrollBean : list) {
                rowResult.add(pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //生成工资单
                            generatePayroll(payRuleBean, allowanceSettingBeanList, customerPayrollBean, attendanceNumberDay, allowanceMap, payCycleBean);
                        } catch (Exception e) {
                            //添加一次信息
                            errorList.add(e);
                            //输出异常信息
                            LOGGER.error("customerId【" + customerPayrollBean.getCustomerId() + "】出现异常···" + e.getMessage(), e);
                        }
                    }
                }));

            }
            //等待处理结果
            for (Future f : rowResult) {
                f.get();
            }
            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
            pool.shutdown();
            //出现异常
            if (!errorList.isEmpty()) {
                //抛出异常信息
                throw new BusinessException(errorList.get(0).getMessage());
            }
        }
    }

    /**
     * 生成工资单
     *
     * @param payRuleBean
     * @param allowanceSettingBeanList
     * @param customerPayrollBean
     * @param attendanceNumberDay
     * @param allowanceMap
     * @param payCycleBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void generatePayroll(PayRuleBean payRuleBean, List<AllowanceSettingBean> allowanceSettingBeanList,
                                 CustomerPayrollBean customerPayrollBean, BigDecimal attendanceNumberDay,
                                 Map<Long, Map<Long, Long>> allowanceMap, PayCycleBean payCycleBean) {
        CustomersDto customersDto = customersReaderMapper.selectByCustomerId(customerPayrollBean.getCustomerId(), payCycleBean.getStartDate(), payCycleBean.getEndDate());
        if (customersDto != null) {
            //将已存在的工资单更新
            updateCustomerPayroll(customerPayrollBean, payRuleBean, allowanceSettingBeanList, customersDto, attendanceNumberDay, allowanceMap, payCycleBean);
        } else {
            customerPayrollBean.setIsAllowanceUpdate(Integer.valueOf(0));
            customerPayrollBean.setIsBonusUpdate(Integer.valueOf(0));
            customerPayrollBean.setIsPayruleUpdate(Integer.valueOf(0));
            //更新工资单
            customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
        }
    }


    /**
     * 生成工资单
     *
     * @param payRuleBean              计薪规则
     * @param allowanceSettingBeanList 津贴设置
     * @param bonusSettingsBeanList    奖金设置
     * @param customersDto             人员信息
     * @param map                      已生成工资单的工资单列表
     * @param attendanceNumberDay      计薪周期内应出勤天数
     * @param allowanceMap             津贴适用的员工
     * @param payCycleBean             计薪周期
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void generatePayroll(PayRuleBean payRuleBean, List<AllowanceSettingBean> allowanceSettingBeanList,
                                List<BonusSettingsBean> bonusSettingsBeanList, CustomersDto customersDto,
                                Map<Long, CustomerPayrollBean> map, BigDecimal attendanceNumberDay,
                                Map<Long, Map<Long, Long>> allowanceMap, PayCycleBean payCycleBean) {
        //检查该员工是否已生成工资单，如果已经生成，则将该工资单重新进行生成
        if (!map.isEmpty() && map.containsKey(customersDto.getCustomerId())) {
            //已经存在工资单
            CustomerPayrollBean customerPayrollBean = map.get(customersDto.getCustomerId());
            //将已存在的工资单更新
            updateCustomerPayroll(customerPayrollBean, payRuleBean, allowanceSettingBeanList, customersDto, attendanceNumberDay, allowanceMap, payCycleBean);
        } else {
            //不存在工资单
            insertCustoemrPayroll(payRuleBean, allowanceSettingBeanList, bonusSettingsBeanList, customersDto, attendanceNumberDay, allowanceMap, payCycleBean);
        }
    }

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
    public void updateCustomerPayroll(CustomerPayrollBean customerPayrollBean, PayRuleBean payRuleBean,
                                      List<AllowanceSettingBean> allowanceSettingBeanList, CustomersDto customersDto,
                                      BigDecimal attendanceNumberDay, Map<Long, Map<Long, Long>> allowanceMap,
                                      PayCycleBean payCycleBean) {
        boolean flag = false;
        if (customerPayrollBean.getIsAllowanceUpdate().intValue() == 1) {
            //删除该工资单对应的所有津贴，重新计算
            customerPayrollBean.setAllowanceJsonStr(null);
            //清空津贴合计
            customerPayrollBean.setTotalAllowance(new BigDecimal(0));
            //津贴设置被改变 需重新计算津贴
            calcAllowance(customersDto, customerPayrollBean, allowanceSettingBeanList, allowanceMap, attendanceNumberDay);
            flag = true;
        }
        if (customerPayrollBean.getIsBonusUpdate().intValue() == 1) {
//            //删除该工资单对应的所有奖金，重新计算  明细项类型 0:津贴  1:奖金
//            customerPayrollDetailWriterMapper.deleteByCustomerPayrollId(customerPayrollBean.getId(), Integer.valueOf(1));
//            //奖金设置被改变  需重新计算奖金
//            calcBonusUpdate(customersDto, customerPayrollBean, allowanceSettingBeanList, allowanceMap);
//            flag = true;
            //奖金发生改变，清空奖金缓存
//            cacheManager.getCacheNames();
//            cacheManager.getCache("redisManager").evict(customerPayrollBean.getId() + "bonus");
        }
        if (customerPayrollBean.getIsPayruleUpdate().intValue() == 1) {
            //计薪规则被改变，需重新计算工资
            flag = true;
        }
        if (flag) {
            calcBaseSalary(customerPayrollBean, payRuleBean, customersDto, attendanceNumberDay, payCycleBean.getId());
            customerPayrollBean.setIsAllowanceUpdate(Integer.valueOf(0));
            customerPayrollBean.setIsBonusUpdate(Integer.valueOf(0));
            customerPayrollBean.setIsPayruleUpdate(Integer.valueOf(0));
            //更新工资单
            customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
        }
    }


    /**
     * 新增工资单
     *
     * @param payRuleBean              计薪规则
     * @param allowanceSettingBeanList 津贴设置
     * @param bonusSettingsBeanList    奖金设置
     * @param customersDto             人员信息
     * @param attendanceNumberDay      计薪周期内应出勤天数
     * @param allowanceMap             津贴适用的员工
     * @param payCycleBean             计薪周期
     */
    private void insertCustoemrPayroll(PayRuleBean payRuleBean, List<AllowanceSettingBean> allowanceSettingBeanList,
                                       List<BonusSettingsBean> bonusSettingsBeanList, CustomersDto customersDto,
                                       BigDecimal attendanceNumberDay, Map<Long, Map<Long, Long>> allowanceMap,
                                       PayCycleBean payCycleBean) {
        CustomerPayrollBean customerPayrollBean = new CustomerPayrollBean();
        //计算基本工资
        calcBaseSalary(customerPayrollBean, payRuleBean, customersDto, attendanceNumberDay, payCycleBean.getId());
        //保存基本工资
        customerPayrollBean.setCreateTime(new Date());
        customerPayrollWriterMapper.insertSelective(customerPayrollBean);
        //计算津贴
        customerPayrollBean.setTotalAllowance(new BigDecimal(0));
        calcAllowance(customersDto, customerPayrollBean, allowanceSettingBeanList, allowanceMap, attendanceNumberDay);
        //计算奖金
        customerPayrollBean.setTotalBonus(new BigDecimal(0));
        customerPayrollBean = customerPayrollService.calcBonus(customerPayrollBean, bonusSettingsBeanList, null);

        customerPayrollBean = calcBaseSalary(customersDto, customerPayrollBean, payRuleBean, attendanceNumberDay);
        //更新工资单的津贴总额
        customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
    }

    /**
     * 计算基本工资
     *
     * @param customerPayrollBean 工资单
     * @param payRuleBean         计薪规则
     * @param customersBean       人员信息
     * @param attendanceNumberDay 计薪周期内应出勤天数
     * @param payCycleId          计薪周期id
     */
    public CustomerPayrollBean calcBaseSalary(CustomerPayrollBean customerPayrollBean, PayRuleBean payRuleBean,
                                              CustomersBean customersBean, BigDecimal attendanceNumberDay,
                                              Long payCycleId) {
        //员工id
        customerPayrollBean.setCustomerId(customersBean.getCustomerId());
        //公司id
        customerPayrollBean.setCompanyId(customersBean.getCustomerCompanyId());
        //部门id
        customerPayrollBean.setDeptId(customersBean.getCustomerDepId());
        //计算周期id
        customerPayrollBean.setPayCycleId(payCycleId);
        BigDecimal baseWages = customersBean.getCustomerCurrentSalary() == null ? new BigDecimal(0) : customersBean.getCustomerCurrentSalary();
        //获取基本工资
        customerPayrollBean.setBaseWages(baseWages);
        BigDecimal addWages = customersBean.getNewSalary() == null ? new BigDecimal(0) : customersBean.getNewSalary();
        //获取调薪后基本工资
        customerPayrollBean.setAddWages(addWages);

        //是否计算社保公积金 0:否  1:是
        if (StringUtils.equals(payRuleBean.getIsSocialSecurity(), "0")) {
            //社保公积金
            customerPayrollBean.setSocialSecurityFund(new BigDecimal(0));
            //社保
            customerPayrollBean.setSocialSecurity(new BigDecimal(0));
            //公积金
            customerPayrollBean.setFund(new BigDecimal(0));
        } else {
            if (getSbGjgMap(payCycleId) != null) {
                CustomerShebaoSumDto customerShebaoSumDto = getSbGjgMap(payCycleId).get(customersBean.getCustomerId());
                if (customerShebaoSumDto == null) {
                    customerShebaoSumDto = new CustomerShebaoSumDto();
                }
                //共进
                customerPayrollBean.setSocialSecurity(customerShebaoSumDto.getSbSum());
                //公积金
                customerPayrollBean.setFund(customerShebaoSumDto.getGjjSum());
                //社保公积金
                customerPayrollBean.setSocialSecurityFund(customerShebaoSumDto.getSbSum().add(customerShebaoSumDto.getGjjSum()));
                //社保公积金公司部分
                customerPayrollBean.setSocialSecurityFundCorp(customerShebaoSumDto.getTotalOrgSum());
            }
        }

        customerPayrollBean = calcBaseSalary(customersBean, customerPayrollBean, payRuleBean, attendanceNumberDay);

        return customerPayrollBean;
    }

    /**
     * 计算基本工资
     *
     * @param customerPayrollBean
     * @param payRuleBean
     * @param attendanceNumberDay
     * @return
     */
    private CustomerPayrollBean calcBaseSalary(CustomersBean customersBean, CustomerPayrollBean customerPayrollBean,
                                               PayRuleBean payRuleBean, BigDecimal attendanceNumberDay) {
        //获取缺勤天数
        BigDecimal absenceDayNumber = customerPayrollBean.getAbsenceDayNumber();
        absenceDayNumber = absenceDayNumber == null ? new BigDecimal(0) : absenceDayNumber;
        //缺勤天数大于实际工作日，缺勤扣款等于基本
        if (absenceDayNumber.subtract(attendanceNumberDay).doubleValue() >= 0) {
            //缺勤扣款
            customerPayrollBean.setAttendanceDeduction(customerPayrollBean.getBaseWages());
        } else {
            //缺勤扣款
            customerPayrollBean.setAttendanceDeduction(getDailyAverageWage(customerPayrollBean, payRuleBean, attendanceNumberDay, absenceDayNumber));
        }
        //考勤工资
        BigDecimal attendanceSalary = caleAttendanceSalary(customerPayrollBean, payRuleBean, attendanceNumberDay);
        //获取报销金额，绩效补充
        RealExpenseDto realExpenseDto = customerExpenseService.selectExpense(customersBean, customerPayrollBean.getPayCycleId(), attendanceSalary);
        if (realExpenseDto != null) {
            //报销金额
            customerPayrollBean.setWipedAmount(realExpenseDto.getRealExpense());
            //绩效补充
            customerPayrollBean.setAppraisalsSupplement(realExpenseDto.getLastExpenseMax());
        }
        //应发工资 = 考勤工资 + 津贴 + 奖金 + 税前补发/扣款 - 报销金额
        BigDecimal shouldAmount = attendanceSalary.add(customerPayrollBean.getTotalAllowance() == null ? new BigDecimal(0) : customerPayrollBean.getTotalAllowance());
        shouldAmount = shouldAmount.add(customerPayrollBean.getTotalBonus() == null ? new BigDecimal(0) : customerPayrollBean.getTotalBonus());
        shouldAmount = shouldAmount.add(customerPayrollBean.getPretax() == null ? new BigDecimal(0) : customerPayrollBean.getPretax());
        shouldAmount = shouldAmount.subtract(customerPayrollBean.getWipedAmount() == null ? new BigDecimal(0) : customerPayrollBean.getWipedAmount());
        customerPayrollBean.setShouldAmount(shouldAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        //税前工资 = 应发工资 - 社保公积金/个人
        BigDecimal grossPay = shouldAmount.subtract(customerPayrollBean.getSocialSecurityFund() == null ? new BigDecimal(0) : customerPayrollBean.getSocialSecurityFund());
        //计算个税
        BigDecimal personalTax = calcPersonalTax(grossPay);
        customerPayrollBean.setPersonalTax(personalTax.setScale(2, BigDecimal.ROUND_HALF_UP));
        //实发工资 = 税前工资  - 个税 + 税后补发/扣款 + 报销金额
        BigDecimal realWage = grossPay.subtract(personalTax).add(customerPayrollBean.getAfterTax() == null ? new BigDecimal(0) : customerPayrollBean.getAfterTax());
        realWage = realWage.add(customerPayrollBean.getWipedAmount());
        customerPayrollBean.setRealWage(realWage.setScale(2, BigDecimal.ROUND_HALF_UP));

        return customerPayrollBean;
    }

    /**
     * 计算奖金
     *
     * @param customerPayrollBean
     * @param bonusSettingsBeanList 奖金设置
     */
//    @CacheEvict(value = "redisManager", key = "#customerPayrollBean.id+'bonus'")
//    public void calcBonus(CustomerPayrollBean
//                                  customerPayrollBean, List<BonusSettingsBean> bonusSettingsBeanList) {
//        if (bonusSettingsBeanList != null && !bonusSettingsBeanList.isEmpty()) {
//            for (BonusSettingsBean bonusSettingsBean : bonusSettingsBeanList) {
////                customerPayrollBean.setTotalAllowance(customerPayrollBean.getTotalAllowance().add(bonusSettingsBean.getAmount()));
//                //员工工资单明细 津贴
//                CustomerPayrollDetailBean customerPayrollDetailBean = new CustomerPayrollDetailBean();
//                //工资单主键
//                customerPayrollDetailBean.setCustomerPayrollId(customerPayrollBean.getId());
//                //明细项名称
//                customerPayrollDetailBean.setDetailName(bonusSettingsBean.getBonusName());
//                //明细项值
//                customerPayrollDetailBean.setDetailValue(new BigDecimal(0));
//                //明细项类型 0:津贴  1:奖金
//                customerPayrollDetailBean.setDetailType(Integer.valueOf(1));
//                //来源id
//                customerPayrollDetailBean.setResourceId(bonusSettingsBean.getId());
//                //新增员工工资单明细 奖金
//                customerPayrollDetailWriterMapper.insert(customerPayrollDetailBean);
//            }
//        }
//    }


    /**
     * 津贴计算
     *
     * @param customersDto
     * @param customerPayrollBean
     * @param allowanceSettingBeanList
     * @param allowanceMap             津贴适用的员工
     * @param attendanceNumberDay      计薪周期内应出勤天数
     */
    public CustomerPayrollBean calcAllowance(CustomersDto customersDto, CustomerPayrollBean customerPayrollBean,
                                             List<AllowanceSettingBean> allowanceSettingBeanList, Map<Long, Map<Long, Long>> allowanceMap,
                                             BigDecimal attendanceNumberDay) {
        if (allowanceSettingBeanList != null && !allowanceSettingBeanList.isEmpty()) {
            //获取缺勤天数
            BigDecimal absenceDayNumber = customerPayrollBean.getAbsenceDayNumber();
            absenceDayNumber = absenceDayNumber == null ? new BigDecimal(0) : absenceDayNumber;
            //实际出勤天数 = 计薪周期内应出勤天数 - 缺勤天数
            BigDecimal attendanceDay = attendanceNumberDay.subtract(absenceDayNumber);
            List<CustomerPayrollDetailBean> list = new ArrayList<>();
            for (AllowanceSettingBean allowanceSettingBean : allowanceSettingBeanList) {
                if (!allowanceMap.isEmpty() && allowanceMap.containsKey(allowanceSettingBean.getId())) {
                    //获取津贴适用的员工
                    Map<Long, Long> map = allowanceMap.get(allowanceSettingBean.getId());
                    //员工工资单明细 津贴
                    CustomerPayrollDetailBean customerPayrollDetailBean = new CustomerPayrollDetailBean();
                    if (map.containsKey(customerPayrollBean.getCustomerId())) {
                        //判断员工是否适用该津贴
                        if (isAllowanceApply(allowanceSettingBean, customersDto, absenceDayNumber)) {
                            //津贴方式  0:每月 1:每出勤日
                            if (allowanceSettingBean.getAllowanceType().intValue() == 0) {
                                //明细项值
                                customerPayrollDetailBean.setDetailValue(allowanceSettingBean.getAmount());
                                //津贴总额
                                customerPayrollBean.setTotalAllowance(customerPayrollBean.getTotalAllowance().add(allowanceSettingBean.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            } else if (allowanceSettingBean.getAllowanceType().intValue() == 1) {
                                //明细项值
                                customerPayrollDetailBean.setDetailValue(allowanceSettingBean.getAmount().multiply(attendanceDay));
                                //津贴总额
                                customerPayrollBean.setTotalAllowance(customerPayrollBean.getTotalAllowance().add(customerPayrollDetailBean.getDetailValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                            }
                        }
                    }
                    if (customerPayrollDetailBean.getDetailValue() == null) {
                        //明细项值
                        customerPayrollDetailBean.setDetailValue(new BigDecimal(0));
                    }
                    //工资单主键
                    customerPayrollDetailBean.setCustomerPayrollId(customerPayrollBean.getId());
                    //明细项名称
                    customerPayrollDetailBean.setDetailName(allowanceSettingBean.getAllowanceName());
                    //明细项类型 0:津贴  1:奖金
                    customerPayrollDetailBean.setDetailType(Integer.valueOf(0));
                    //来源id
                    customerPayrollDetailBean.setResourceId(allowanceSettingBean.getId());
                    list.add(customerPayrollDetailBean);
                }
            }
            if (!list.isEmpty()) {
                customerPayrollBean.setAllowanceJsonStr(JSON.toJSONString(list));
            }
        }
        return customerPayrollBean;
    }


    /**
     * 判断员工是否适用该津贴
     *
     * @param allowanceSettingBean
     * @param customersDto
     * @param absenceDayNumber     实际缺勤天数
     * @return
     */

    public boolean isAllowanceApply(AllowanceSettingBean allowanceSettingBean, CustomersDto customersDto,
                                    BigDecimal absenceDayNumber) {
        boolean flag = false;
        //判断员工的聘用形式  聘用形式 1正式 2劳务
        if (customersDto.getStationEmployMethod().intValue() == 1) {
            //员工状态 1入职 2转正 3离职 4删除
            //正式工  试用期   转正
            if (customersDto.getStationCustomerState().intValue() == 1 && allowanceSettingBean.getFormallyProbationPeriod().intValue() == 1
                    || ((customersDto.getStationCustomerState().intValue() == 2 || customersDto.getStationCustomerState().intValue() == 3 || customersDto.getStationCustomerState().intValue() == 4)
                    && allowanceSettingBean.getFormallyPromotion().intValue() == 1)) {
                flag = true;
            }

        } else { //劳务
            flag = customersDto.getStationCustomerState().intValue() == 1 && allowanceSettingBean.getLaborPromotionPeriod().intValue() == 1
                    || ((customersDto.getStationCustomerState().intValue() == 2 || customersDto.getStationCustomerState().intValue() == 3 || customersDto.getStationCustomerState().intValue() == 4)
                    && allowanceSettingBean.getLaborPromotion().intValue() == 1);
        }

        if (flag) {
            //判断是否启用津贴适用出勤范围
            if (allowanceSettingBean.getIsApplyRange().intValue() == 1) {
                //缺勤天数大于时，不享受该津贴
                int day = allowanceSettingBean.getAbsenceDay();
                if (absenceDayNumber != null && absenceDayNumber.doubleValue() > day) {
                    flag = false;
                }
            } else {
                //不启用适用范围，则默认全部适用
            }
        }
        return flag;
    }


    /**
     * 计算个人所得税
     * 个人所得税税率表（一）
     * 级数	 应纳税所得额(含税)	                 应纳税所得额(不含税)	        税率(%)	       速算扣除数
     * 1	不超过1500元的	                        不超过1455元的	           3	         0
     * 2	超过1500元至4,500元的部分	       超过1455元至4,155元的部分	           10	        105
     * 3	超过4,500元至9,000元的部分	       超过4,155元至7,755元的部分	           20	        555
     * 4	超过9,000元至35,000元的部分	       超过7,755元至27,255元的部分	       25	       1,005
     * 5	超过35,000元至55,000元的部分	   超过27,255元至41,255元的部分	       30	       2,775
     * 6	超过55,000元至80,000元的部分	   超过41,255元至57,505元的部分	       35	       5,505
     * 7	超过80,000元的部分	               超过57,505的部分	               45	       13,505
     *
     * @param grossPay 税前工资
     * @return
     */
    private BigDecimal calcPersonalTax(BigDecimal grossPay) {
        //获取个税起征点基数
        String taxThreshold = PropertyUtils.getString("salary.tax.threshold");
        taxThreshold = StringUtils.isBlank(taxThreshold) ? "3500" : taxThreshold;
        //应纳税工资 = 税前工资 - 个税起征点
        BigDecimal taxableSalary = grossPay.subtract(new BigDecimal(taxThreshold));
        if (taxableSalary.doubleValue() > 0) {
            if (taxableSalary.doubleValue() < 1455) {
                return taxableSalary.multiply(new BigDecimal(0.03)).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (taxableSalary.doubleValue() < 4155) {
                return taxableSalary.multiply(new BigDecimal(0.1)).subtract(new BigDecimal(105)).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (taxableSalary.doubleValue() < 7755) {
                return taxableSalary.multiply(new BigDecimal(0.2)).subtract(new BigDecimal(555)).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (taxableSalary.doubleValue() < 27255) {
                return taxableSalary.multiply(new BigDecimal(0.25)).subtract(new BigDecimal(1005)).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (taxableSalary.doubleValue() < 41255) {
                return taxableSalary.multiply(new BigDecimal(0.3)).subtract(new BigDecimal(2775)).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else if (taxableSalary.doubleValue() < 57505) {
                return taxableSalary.multiply(new BigDecimal(0.35)).subtract(new BigDecimal(5505)).setScale(2, BigDecimal.ROUND_HALF_UP);
            } else {
                return taxableSalary.multiply(new BigDecimal(0.45)).subtract(new BigDecimal(13505)).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return new BigDecimal(0);
    }

    /**
     * 计算考勤工资
     *
     * @param customerPayrollBean 工资单
     * @param payRuleBean         计薪规则
     * @param attendanceNumberDay 计薪周期内应出勤天数
     * @return
     */
    private BigDecimal caleAttendanceSalary(CustomerPayrollBean customerPayrollBean, PayRuleBean payRuleBean,
                                            BigDecimal attendanceNumberDay) {
        //获取缺勤天数
        BigDecimal absenceDayNumber = customerPayrollBean.getAbsenceDayNumber();
        absenceDayNumber = absenceDayNumber == null ? new BigDecimal(0) : absenceDayNumber;
        if (absenceDayNumber.doubleValue() == 0) {
            return customerPayrollBean.getBaseWages();
        }
        //缺勤天数大于实际工作日，考勤工资等于0
        if (absenceDayNumber.subtract(attendanceNumberDay).doubleValue() >= 0) {
            return new BigDecimal(0);
        }
        //计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法
        if (payRuleBean.getPayWay().intValue() == 1) {
            //出勤天数 = 计薪周期内应出勤天数 - 缺勤天数
            BigDecimal atttendanceDays = attendanceNumberDay.subtract(absenceDayNumber);

            //计薪临界点  0:以11天为临界点  2:以21.75天为临界点
            if (payRuleBean.getPayCriticalPoint().intValue() == 0) {
                //以11天为临界点
                if (atttendanceDays.doubleValue() < 11) {
                    //考勤工资 = 基本工资 / 21.75 * 出勤天数
                    return customerPayrollBean.getBaseWages().divide(new BigDecimal(21.75), 5, BigDecimal.ROUND_HALF_UP).multiply(atttendanceDays).setScale(2, BigDecimal.ROUND_HALF_UP);
                } else if (atttendanceDays.doubleValue() >= 11) {
                    //考勤工资 = 基本工资 - 基本工资 / 21.75 * 缺勤天数
                    return customerPayrollBean.getBaseWages().subtract(customerPayrollBean.getBaseWages().divide(new BigDecimal(21.75), 5, BigDecimal.ROUND_HALF_UP).multiply(absenceDayNumber)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
            } else {
                //以21.75天为临界点
                if (atttendanceDays.doubleValue() < 21.75) {
                    //考勤工资 = 基本工资 / 21.75 * 出勤天数
                    return customerPayrollBean.getBaseWages().divide(new BigDecimal(21.75), 5, BigDecimal.ROUND_HALF_UP).multiply(atttendanceDays).setScale(2, BigDecimal.ROUND_HALF_UP);
                } else if (atttendanceDays.doubleValue() >= 21.75) {
                    //考勤工资 = 基本工资 - 基本工资 / 21.75 * 缺勤天数
                    return customerPayrollBean.getBaseWages().subtract(customerPayrollBean.getBaseWages().divide(new BigDecimal(21.75), 5, BigDecimal.ROUND_HALF_UP).multiply(absenceDayNumber)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
            }
        } else {
            //实际工作日计薪法
            //考勤工资 = 基本工资 - 基本工资 / 本月实际工作日 * 缺勤天数
            return customerPayrollBean.getBaseWages().subtract(customerPayrollBean.getBaseWages().divide(attendanceNumberDay, 5, BigDecimal.ROUND_HALF_UP).multiply(absenceDayNumber)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return new BigDecimal(0);
    }


    /**
     * 根据公司主键获取津贴设置
     *
     * @param companyId
     * @return
     */
    private List<AllowanceSettingBean> getAllowanceSettingBeanList(Long companyId) {
        //获取津贴设置
        AllowanceSettingBean allowanceSettingBean = new AllowanceSettingBean();
        allowanceSettingBean.setCompanyId(companyId);
        return allowanceSettingReaderMapper.selectList(allowanceSettingBean);
    }

    /**
     * 根据公司主键获取奖金设置
     *
     * @param companyId
     * @return
     */
    private List<BonusSettingsBean> getBonusSettingsBeanList(Long companyId) {
        BonusSettingsBean bonusSettingsBean = new BonusSettingsBean();
        bonusSettingsBean.setCompanyId(companyId);
        return bonusSettingsReaderMapper.selectList(bonusSettingsBean);
    }

    /**
     * 根据计薪周期主键获取工资单
     *
     * @param customerPayrollDto
     */
//    public Map<Long, CustomerPayrollBean> getCustomerPayrollByPayCycleId(CustomerPayrollDto customerPayrollDto) throws Exception {
//        Map<Long, CustomerPayrollBean> map = new LinkedHashMap<>();
//        if (customerPayrollDto != null) {
//            List<CustomerPayrollDto> list = selectCustomerPayroll(customerPayrollDto);
//            if (!list.isEmpty()) {
//                for (CustomerPayrollBean customerPayrollBean : list) {
//                    map.put(customerPayrollBean.getCustomerId(), customerPayrollBean);
//                }
//            }
//        }
//        return map;
//    }

    /**
     * 分页查询工资单,不存在则自动生成
     *
     * @param customerPayrollDto
     * @return
     */
//    public ResultResponse selectPageList(CustomerPayrollDto customerPayrollDto) throws Exception {
//        ResultResponse resultResponse = new ResultResponse();
//        //检查是否已生成工资单,获取计薪周期
//        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customerPayrollDto.getCompanyId());
//        if (payCycleBean != null) {
//            customerPayrollDto.setStartDate(DateUtil.date2String(payCycleBean.getStartDate(), DateUtil.DATEYEARFORMATTER));
//            customerPayrollDto.setEndDate(DateUtil.date2String(payCycleBean.getEndDate(), DateUtil.DATEYEARFORMATTER));
//            PageList list1 = customersReaderMapper.selectCustomerPageList(customerPayrollDto, new PageBounds(customerPayrollDto.getPageIndex(), customerPayrollDto.getPageSize()));
//            if (!list1.isEmpty()) {
//                customerPayrollDto.setCustomerList(list1);
//                customerPayrollDto.setPayCycleId(payCycleBean.getId());
//                //获取所有的工资单
//                Map<Long, CustomerPayrollBean> map = customerPayrollService.getCustomerPayrollByPayCycleId(customerPayrollDto);
//                //生成工资单
//                generatePayroll(list1, map, payCycleBean);
//                //获取工资单
//                List list = selectCustomerPayroll(customerPayrollDto);
//                resultResponse.setData(list);
//                resultResponse.setPaginator(list1.getPaginator());
//            }
//        }
//
//        resultResponse.setSuccess(true);
//        LOGGER.info("薪资核算查询成功···");
//        return resultResponse;
//    }

    /**
     * 查询工资单
     *
     * @param customerPayrollDto
     * @return
     */

//    public List<CustomerPayrollDto> selectCustomerPayroll(CustomerPayrollDto customerPayrollDto) throws Exception {
//        String startDate = DateUtil.getCurrentDatetime();
//        List<CustomerPayrollDto> list = customerPayrollReaderMapper.selectPageList(customerPayrollDto);
//        list = selectCustomerPayroll(list);
//        String endDate = DateUtil.getCurrentDatetime();
//        LOGGER.info("查询工资单时间：" + DateUtil.dateDiff(startDate, endDate));
//        return list;
//    }

    /**
     * 查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
//    public List<CustomerPayrollDto> selectCustomerPayroll(CustomerPayrollDto customerPayrollDto, PageBounds pageBounds) throws Exception {
//        String startDate = DateUtil.getCurrentDatetime();
//        List<CustomerPayrollDto> list = customerPayrollReaderMapper.selectPageList(customerPayrollDto, pageBounds);
//        for (int i = 0; i < list.size(); i++) {
//            CustomerPayrollDto payrollDto = list.get(i);
//            payrollDto.setAllowanceList(payrollAccountService.selectAllowanceByPayrollId(payrollDto.getId()));
//            //查询奖金
//            payrollDto.setBonusList(payrollAccountService.selectBonusByPayrollId(payrollDto.getId()));
//        }
//        String endDate = DateUtil.getCurrentDatetime();
//        LOGGER.info("分页查询工资单时间：" + DateUtil.dateDiff(startDate, endDate));
//        return list;
//    }

    /**
     * 查询津贴和奖金
     *
     * @param list
     * @return
     */
//    private List<CustomerPayrollDto> selectCustomerPayroll(final List<CustomerPayrollDto> list) throws ExecutionException, InterruptedException {
//        if (list != null && !list.isEmpty()) {
//            int size = list.size();
//            final List<Exception> errorList = new ArrayList();
//            List<Future> rowResult = new CopyOnWriteArrayList<>();
//            //创建线程安全的List集合
//            final List<CustomerPayrollDto> list1 = Collections.synchronizedList(new ArrayList<CustomerPayrollDto>());
//            pool = Executors.newFixedThreadPool(size > 100 ? 100 : size);
//            for (int i = 0; i < size; i++) {
//                final CustomerPayrollDto customerPayrollDto1 = list.get(i);
//                rowResult.add(pool.submit(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            customerPayrollDto1.setAllowanceList(payrollAccountService.selectAllowanceByPayrollId(customerPayrollDto1.getId()));
//                            //查询奖金
//                            customerPayrollDto1.setBonusList(payrollAccountService.selectBonusByPayrollId(customerPayrollDto1.getId()));
//                            list1.add(customerPayrollDto1);
//                        } catch (Exception e) {
//                            //添加一次信息
//                            errorList.add(e);
//                        }
//                    }
//                }));
//            }
//            //等待处理结果
//            for (Future f : rowResult) {
//                f.get();
//            }
//            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
//            pool.shutdown();
//            //出现异常
//            if (!errorList.isEmpty()) {
//                //抛出异常信息
//                throw new BusinessException(errorList.get(0).getMessage());
//            }
//            LOGGER.info("线程池之前数量：" + list.size());
//            LOGGER.info("线程池返回数量：" + list1.size());
//            return list1;
//        }
//        return list;
//    }

    /**
     * 根据工资单主键查询津贴
     *
     * @param customerPayrollId
     * @return
     */
//    @Cacheable(value = "redisManager", key = "#customerPayrollId+'allowance'")
//    public List<CustomerPayrollDetailBean> selectAllowanceByPayrollId(Long customerPayrollId) {
//        return customerPayrollDetailReaderMapper.selectAllowanceByPayrollId(customerPayrollId);
//    }

    /**
     * 根据工资单主键查询奖金
     *
     * @param customerPayrollId
     * @return
     */
//    @Cacheable(value = "redisManager", key = "#customerPayrollId+'bonus'")
//    public List<CustomerPayrollDetailBean> selectBonusByPayrollId(Long customerPayrollId) {
//        return customerPayrollDetailReaderMapper.selectBonusByPayrollId(customerPayrollId);
//    }

    /**
     * 分页查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
//    public ResultResponse selectPageList(CustomerPayrollDto customerPayrollDto, Pager pager) throws Exception {
//        ResultResponse resultResponse = new ResultResponse();
//        List<CustomerPayrollDto> list = selectCustomerPayroll(customerPayrollDto, PageBounds.create(pager));
//        resultResponse.setData(list);
//        resultResponse.setPaginator(((PageList) list).getPaginator());
//        resultResponse.setSuccess(true);
//        return resultResponse;
//    }

    /**
     * 筛选工资单
     *
     * @param customerPayrollQueryDto
     * @return
     */
//    public ResultResponse selectCustomerPayroll(CustomerPayrollQueryDto customerPayrollQueryDto) {
//        //获取当前计薪周期
//        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customerPayrollQueryDto.getCompanyId());
//        //是否生成工资单 0:未生成 1:已生成
//        if (payCycleBean.getIsGeneratePayroll() == 0) {
//            //未生成工资单
//            throw new BusinessException("当前计薪周期【" + payCycleBean.getCurrentPayCycle() + "】还未生成工资单，请稍后再试");
//        }
//        //检查是否已经生成工资单
//        ResultResponse resultResponse = new ResultResponse();
//        PageList<CustomerPayrollDto> list = customerPayrollReaderMapper.selectCustomerPayroll(customerPayrollQueryDto, PageBounds.create(new Pager(customerPayrollQueryDto.getPageIndex(), customerPayrollQueryDto.getPageSize())));
//        resultResponse.setData(list);
//        resultResponse.setPaginator(list.getPaginator());
//        resultResponse.setSuccess(true);
//        return resultResponse;
//    }


    /**
     * 获取缺勤扣款
     *
     * @param customerPayrollBean
     * @param payRuleBean
     * @param attendanceNumberDay 应出勤天数
     * @param absenceDayNumber    缺勤天数
     * @return
     */
    private BigDecimal getDailyAverageWage(CustomerPayrollBean customerPayrollBean, PayRuleBean payRuleBean,
                                           BigDecimal attendanceNumberDay, BigDecimal absenceDayNumber) {


        if (absenceDayNumber.doubleValue() == 0) {
            return new BigDecimal(0);
        }

        if (absenceDayNumber.subtract(attendanceNumberDay).doubleValue() >= 0) {
            return customerPayrollBean.getBaseWages();
        }

        //计薪方式 1：21.75天标准计薪法  2：实际工作日计薪法
        if (payRuleBean.getPayWay().intValue() == 1) {
            //21.75天标准计薪法
            //日工资
            BigDecimal dailyAverageWage = customerPayrollBean.getBaseWages().divide(new BigDecimal(21.75), 5, BigDecimal.ROUND_HALF_UP);
            //计薪临界点  0:以11天为临界点  2:以21.75天为临界点
            if (payRuleBean.getPayCriticalPoint() == 0) {
                if (attendanceNumberDay.subtract(absenceDayNumber).subtract(new BigDecimal(11)).doubleValue() < 0) {
                    //缺勤扣款 = 基本工资 - 日工资 * （应出勤天数 - 缺勤天数）
                    return customerPayrollBean.getBaseWages().subtract(dailyAverageWage.multiply(attendanceNumberDay.subtract(absenceDayNumber)));
                } else {
                    //缺勤工资= 日工资 * 缺勤天数
                    return dailyAverageWage.multiply(absenceDayNumber);
                }
            } else {
                if (attendanceNumberDay.subtract(absenceDayNumber).subtract(new BigDecimal(21.75)).doubleValue() < 0) {
                    //缺勤扣款 = 基本工资 - 日工资 * （应出勤天数 - 缺勤天数）
                    return customerPayrollBean.getBaseWages().subtract(dailyAverageWage.multiply(attendanceNumberDay.subtract(absenceDayNumber)));
                } else {
                    //缺勤工资= 日工资 * 缺勤天数
                    return dailyAverageWage.multiply(absenceDayNumber);
                }
            }

        } else {
            //实际工作日计薪法   缺勤扣款 = 基本工资 / 应出勤天数 * 缺勤天数
            return customerPayrollBean.getBaseWages().divide(attendanceNumberDay, 5, BigDecimal.ROUND_HALF_UP).multiply(absenceDayNumber);
        }
    }


    /**
     * 更新基本工资
     *
     * @param customerPayrollDto
     * @param type               0:缺勤天数  1:税前补发/扣款 2:税后补发/扣款
     * @return
     */
    public ResultResponse updateBasePay(CustomerPayrollDto customerPayrollDto, Integer type) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByPrimaryKey(customerPayrollDto.getId());
        if (customerPayrollBean != null) {
            //获取计薪周期
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(customerPayrollBean.getPayCycleId());
            //获取计薪周期内应出勤天数
            BigDecimal attendanceNumberDay = new BigDecimal(customerUpdateSalaryService.calculateWorkNumberDay(payCycleBean.getStartDate(), payCycleBean.getEndDate()));
            //获取计薪规则
            PayRuleBean payRuleBean = payRuleReaderMapper.selectByCompanyId(customerPayrollBean.getCompanyId());
            //人员信息
            CustomersDto customersDto = customersReaderMapper.selectByCustomerId(customerPayrollBean.getCustomerId(), payCycleBean.getStartDate(), payCycleBean.getEndDate());
            if (type.intValue() == 0) {
                //缺勤天数
                customerPayrollBean.setAbsenceDayNumber(customerPayrollDto.getAbsenceDayNumber());
                //获取缺勤天数
                BigDecimal absenceDayNumber = customerPayrollBean.getAbsenceDayNumber();
                absenceDayNumber = absenceDayNumber == null ? new BigDecimal(0) : absenceDayNumber;
                //缺勤天数大于实际工作日，缺勤扣款等于基本
                if (absenceDayNumber.subtract(attendanceNumberDay).doubleValue() >= 0) {
                    //缺勤扣款
                    customerPayrollBean.setAttendanceDeduction(customerPayrollBean.getBaseWages());
                } else {
                    //缺勤扣款
                    customerPayrollBean.setAttendanceDeduction(getDailyAverageWage(customerPayrollBean, payRuleBean, attendanceNumberDay, absenceDayNumber));
                }
                //津贴规则发生变化
                customerPayrollBean.setIsAllowanceUpdate(Integer.valueOf(1));
                //获取津贴设置
                List<AllowanceSettingBean> allowanceSettingBeanList = getAllowanceSettingBeanList(payCycleBean.getCompanyId());
                //获取津贴适用的员工
                Map<Long, Map<Long, Long>> allowanceMap = allowanceApplyService.getCompanyAllowanceApplyMembers(payCycleBean.getCompanyId());
                //更新工资单
                updateCustomerPayroll(customerPayrollBean, payRuleBean, allowanceSettingBeanList, customersDto, attendanceNumberDay, allowanceMap, payCycleBean);
            } else {
                if (type.intValue() == 1) {
                    //税前补发/扣款
                    customerPayrollBean.setPretax(customerPayrollDto.getPretax());
                    //计薪规则发生变化
                    customerPayrollBean.setIsPayruleUpdate(Integer.valueOf(1));
                } else if (type.intValue() == 2) {
                    //税后补发/扣款
                    customerPayrollBean.setAfterTax(customerPayrollDto.getAfterTax());
                    //计薪规则发生变化
                    customerPayrollBean.setIsPayruleUpdate(Integer.valueOf(1));
                } else if (type.intValue() == 3) {
                    //社保
                    customerPayrollBean.setSocialSecurity(customerPayrollDto.getSocialSecurity());
                    BigDecimal fund = customerPayrollBean.getFund() == null ? new BigDecimal(0) : customerPayrollBean.getFund();
                    customerPayrollBean.setSocialSecurityFund(customerPayrollDto.getSocialSecurity().add(fund));
                    //计薪规则发生变化
                    customerPayrollBean.setIsPayruleUpdate(Integer.valueOf(1));
                } else if (type.intValue() == 4) {
                    //公积金
                    customerPayrollBean.setFund(customerPayrollDto.getFund());
                    BigDecimal socialSecurity = customerPayrollBean.getSocialSecurity() == null ? new BigDecimal(0) : customerPayrollBean.getSocialSecurity();
                    customerPayrollBean.setSocialSecurityFund(customerPayrollDto.getFund().add(socialSecurity));
                    //计薪规则发生变化
                    customerPayrollBean.setIsPayruleUpdate(Integer.valueOf(1));
                }
                //计薪规则发生变化，需重新计算工资
                customerPayrollBean = calcBaseSalary(customerPayrollBean, payRuleBean, customersDto, attendanceNumberDay, payCycleBean.getId());
                //更新工资单
                customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
            }
            customerPayrollDto.setId(customerPayrollBean.getId());
            customerPayrollDto.setPayCycleId(customerPayrollBean.getPayCycleId());
            List<CustomerPayrollDto> list = customerPayrollService.selectCustomerPayroll(customerPayrollDto);
            if (list != null && list.size() > 0) {
                resultResponse.setData(list.get(0));
            }

            resultResponse.setSuccess(true);
        } else {
            throw new BusinessException("工资单不存在");
        }
        return resultResponse;
    }


    /**
     * 补充工资单明细
     *
     * @param customerPayrollDto
     * @return
     */
    public ResultResponse updateBasePay(CustomerPayrollDto customerPayrollDto) {
        ResultResponse resultResponse = new ResultResponse();
        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByPrimaryKey(customerPayrollDto.getId());
        if (customerPayrollBean != null) {
            //获取计薪周期
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(customerPayrollBean.getPayCycleId());
            //获取计薪周期内应出勤天数
            BigDecimal attendanceNumberDay = new BigDecimal(customerUpdateSalaryService.calculateWorkNumberDay(payCycleBean.getStartDate(), payCycleBean.getEndDate()));
            //获取计薪规则
            PayRuleBean payRuleBean = payRuleReaderMapper.selectByCompanyId(customerPayrollBean.getCompanyId());

            //税前补发/扣款
            customerPayrollBean.setPretax(customerPayrollDto.getPretax());
            //税后补发/扣款
            customerPayrollBean.setAfterTax(customerPayrollDto.getAfterTax());
            //社保
            customerPayrollBean.setSocialSecurity(customerPayrollDto.getSocialSecurity());
            BigDecimal fund = customerPayrollBean.getFund() == null ? new BigDecimal(0) : customerPayrollBean.getFund();
            customerPayrollBean.setSocialSecurityFund(customerPayrollDto.getSocialSecurity().add(fund));
            //公积金
            customerPayrollBean.setFund(customerPayrollDto.getFund());
            BigDecimal socialSecurity = customerPayrollBean.getSocialSecurity() == null ? new BigDecimal(0) : customerPayrollBean.getSocialSecurity();
            customerPayrollBean.setSocialSecurityFund(customerPayrollDto.getFund().add(socialSecurity));

            CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerPayrollBean.getCustomerId());
            if (customerPayrollBean.getIsPayruleUpdate().intValue() == 1) {
                //计薪规则发生变化，需重新计算工资
                customerPayrollBean = calcBaseSalary(customerPayrollBean, payRuleBean, customersBean, attendanceNumberDay, payCycleBean.getId());
            } else {
                //重算基本工资
                customerPayrollBean = calcBaseSalary(customersBean, customerPayrollBean, payRuleBean, attendanceNumberDay);
            }
            //更新工资单
            customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
            resultResponse.setData(customerPayrollBean);
            resultResponse.setSuccess(true);
        } else {
            throw new BusinessException("工资单不存在");
        }
        return resultResponse;
    }


    /**
     * 修改奖金
     *
     * @param parentId
     * @param id
     * @param detailValue
     * @return
     */
    public ResultResponse bonusUpdate(Long parentId, Long id, BigDecimal detailValue) {
        ResultResponse resultResponse = new ResultResponse();
        //获取工资单信息
        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByPrimaryKey(parentId);
        if (StringUtils.isNotBlank(customerPayrollBean.getBonusJsonStr())) {
            //获取计薪周期
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(customerPayrollBean.getPayCycleId());
            //获取计薪周期内应出勤天数
            BigDecimal attendanceNumberDay = new BigDecimal(customerUpdateSalaryService.calculateWorkNumberDay(payCycleBean.getStartDate(), payCycleBean.getEndDate()));
            //获取计薪规则
            PayRuleBean payRuleBean = payRuleReaderMapper.selectByCompanyId(customerPayrollBean.getCompanyId());
            //奖金明细
            List<CustomerPayrollDetailBean> bonusList = JSON.parseArray(customerPayrollBean.getBonusJsonStr(), CustomerPayrollDetailBean.class);
            //奖金总金额
            BigDecimal totalBonus = new BigDecimal(0);
            for (int i = 0; i < bonusList.size(); i++) {
                CustomerPayrollDetailBean customerPayrollDetailBean = bonusList.get(i);
                if (id.doubleValue() == customerPayrollDetailBean.getId().doubleValue()) {
                    //修改奖金金额
                    customerPayrollDetailBean.setDetailValue(detailValue);
                    bonusList.set(i, customerPayrollDetailBean);
                }
                totalBonus = totalBonus.add(customerPayrollDetailBean.getDetailValue());
            }
            //设置奖金总额度
            customerPayrollBean.setTotalBonus(totalBonus);
            //设置奖金
            customerPayrollBean.setBonusJsonStr(JSON.toJSONString(bonusList));

            CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerPayrollBean.getCustomerId());
            if (customerPayrollBean.getIsPayruleUpdate().intValue() == 1) {
                //计薪规则发生变化，需重新计算工资
                customerPayrollBean = calcBaseSalary(customerPayrollBean, payRuleBean, customersBean, attendanceNumberDay, payCycleBean.getId());
            } else {
                //重算基本工资
                customerPayrollBean = calcBaseSalary(customersBean, customerPayrollBean, payRuleBean, attendanceNumberDay);
            }
            //更新工资单
            customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollBean);
        }
        resultResponse.setData(customerPayrollBean);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 审批计薪周期对应的工资单
     *
     * @param payCycleId
     * @return
     */
    @Transactional
    public ResultResponse approvalPayroll(Long payCycleId) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        //更改计薪周期审批状态
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(payCycleId);
        if (payCycleBean.getApprovalState().intValue() == 1) {
            throw new BusinessException("【" + payCycleBean.getCurrentPayCycle() + "】工资单已审批，请刷新界面");
        }
        int day = DateUtil.getDiffDaysOfTwoDateByNegative(DateUtil.formatCurrentDate(), DateUtil.date2String(payCycleBean.getStartDate(), DateUtil.DATEYEARFORMATTER));
        if (day < 0) {
            throw new BusinessException("当前日期小于计薪周期开始日期，不能审批工资单");
        }
        //工资单审批状态: 0:未审批  1:审批通过
        payCycleBean.setApprovalState(Integer.valueOf(1));
        //更新
        if (payCycleBean.getIsGeneratePayroll().intValue() == 0) {
            //还未任务生成工资单,立即生成工资单
            generatePayroll(payCycleBean);
        }
        //获取发生过更新的工资单，
        List<CustomerPayrollBean> list = customerPayrollReaderMapper.selectChangeCustomerPayroll(payCycleBean.getCompanyId(), payCycleId);
        if (list != null && !list.isEmpty()) {
            //存在更新过的工资单，工资单需重新计算
            updateCustomerPayroll(list, payCycleBean);
        }
        //是否生成工资单 0:未生成 1:已生成
        payCycleBean.setIsGeneratePayroll(Integer.valueOf(1));
        //发薪日期
        payCycleBean.setPayDay(getPayDay(payCycleBean));
        //发薪人数
        payCycleBean.setPeopleNumber(customerPayrollReaderMapper.getTotalPeopleNumber(payCycleBean.getId()));

        //根据计薪周期主键获取工资发放总额，税前工资，社保公积金
        PayCycleBean cycleBean = payCycleReaderMapper.getSalaryByPayCycleId(payCycleBean.getId());
        if (cycleBean != null) {
            //工资发放总额
            payCycleBean.setTotalWages(cycleBean.getTotalWages());
            //税前工资
            payCycleBean.setPretaxPayroll(cycleBean.getPretaxPayroll());
            //社保公积金
            payCycleBean.setSocialSecurityFund(cycleBean.getSocialSecurityFund());
        }
        //更新计薪周期
        int result = payCycleWriterMapper.updateByPrimaryKeySelective(payCycleBean);
        if (result > 0) {
            //生成下月计薪周期
            result = payCycleService.generatePayCycle(payCycleBean);
            if (result > 0) {
                resultResponse.setSuccess(true);
                final Long companyId = payCycleBean.getCompanyId();
                Thread t = new Thread() {
                    public void run() {
                        Long startDate = System.currentTimeMillis();
                        //  刷新基本工资  应该异步处理
                        customerUpdateSalaryService.calulateSalaryByCompanyId(companyId);
                        Long endDate = System.currentTimeMillis();
                        LOGGER.info("刷新基本工资执行时间：" + DateUtil.dateDiff(startDate, endDate));
                    }
                };
                t.start();
            } else {
                throw new BusinessException("【" + payCycleBean.getCurrentPayCycle() + "】工资单审批失败");
            }

        } else {
            throw new BusinessException("【" + payCycleBean.getCurrentPayCycle() + "】工资单审批失败");
        }
        return resultResponse;
    }

    /**
     * 扣款
     *
     * @param payCycleBean
     */
    public void debit(PayCycleBean payCycleBean) {
//        //检查企业账户协议状态
//        checkCompany(payCycleBean.getCompanyId());

        //检查企业账户余额是否足够,如果足够，直接扣款  记录产生来源 1充值+ 2提现- 3公司发工资',
        subAccountService.deduct(payCycleBean.getCompanyId(), payCycleBean.getTotalWages(), 3, payCycleBean.getId(), AccountType.COMPANY, null);
        //更新收支记录状态
//        companyRechargesService.updateRechargeState(payCycleBean.getCompanyId(), payCycleBean.getId(), GrantStateConstant.GRANT_STATE_2);
    }

    /**
     * 检查企业账户协议状态
     *
     * @param companyId
     */
    public void checkCompany(Long companyId) {
        ////签约类型：1代发协议 2垫发协议',
        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectIsUserFulByTypeAndTime(companyId, 1, DateUtil.getCurrDateOfDate(DateUtil.dateString));
        if (companyProtocolsBean != null) {
            //协议状态:1待审批 2签约 3即将到期 4合约到期 5冻结
            if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 1) {
                throw new BusinessException("代发协议还未审批，不能进行工资代发业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 4) {
                throw new BusinessException("代发协议合约已到期，不能进行工资代发业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 5) {
                throw new BusinessException("代发协议合约已被冻结，不能进行工资代发业务");
            }
        } else {
            throw new BusinessException("未签署代发协议，不能进行工资代发业务");
        }
    }

    /**
     * 发工资
     *
     * @param payCycleId
     * @return
     */
    @Transactional
    public ResultResponse payOff(Long payCycleId, CompanyMembersBean companyMembersBean) {
        ResultResponse resultResponse = new ResultResponse();
        //更改计薪周期审批状态
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(payCycleId);

        //工资单审批状态: 0:未审批  1:审批通过
        if (payCycleBean.getApprovalState().intValue() == 0) {
            throw new BusinessException("工资单还未审批，不能进行工资发放");
        }
        if (payCycleBean.getIsPayOff().intValue() == 1 || payCycleBean.getIsPayOff().intValue() == 2) {
            throw new BusinessException("该工资单正在发放中");
        }
        if (payCycleBean.getIsPayOff().intValue() == 3) {
            throw new BusinessException("该工资单工资已发放完毕");
        }
        //检查企业账户协议状态
        checkCompany(payCycleBean.getCompanyId());
        //检查订单是否已生成
        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.SEND_SALARY_TYPE);
        companyRechargesBean.setExcelId(payCycleId);
        List<CompanyRechargesBean> list = companyRechargesService.selectByExcelIdAndType(companyRechargesBean);
        if (list != null && list.size() > 0) {
            throw new BusinessException("该工资单已经生成订单");
        }
        //判断用户是否有访问账户权限
        if (validateCustomerRole(companyMembersBean)) {//有访问权限
            resultResponse.setData(CompanyConstant.COMPANYMEMBER_VISITMENU_YES);
        } else {//无访问权限
            resultResponse.setData(CompanyConstant.COMPANYMEMBER_VISITMENU_NO);
        }
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 发工资扣款
     *
     * @param payCycleId
     * @return
     */
    @Transactional
    public ResultResponse salaryOrderDebit(Long payCycleId, String salaryOrderFlag, CompanyRechargesBean companyRechargesBean, CompanyRechargesBean checkBean) {
        ResultResponse resultResponse = new ResultResponse();
        //检查企业账户协议状态
        checkCompany(checkBean.getRechargeCompanyId());
        //工资发放总金额
        BigDecimal totalWages = new BigDecimal(0);
        //订单类型
        int rechargeType = -1;
        //账户余额扣款
        if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
            if (checkBean.getRechargeType() == CompanyRechargeConstant.RAPIDLY_PAYROLL_TYPE) {
                //急速发工资  返回薪资发放总金额
                RapidlyPayrollExcelBean rapidlyPayrollExcelBean = rapidlyPayrollExcelService.updateRapidlyPayOff(payCycleId);
                totalWages = rapidlyPayrollExcelBean.getTotalWages();
                rechargeType = CompanyRechargeConstant.RAPIDLY_PAYROLL_TYPE;
            } else if (checkBean.getRechargeType() == CompanyRechargeConstant.SEND_SALARY_TYPE) {
                //薪资核算  更新计薪周期  返回薪资发放总金额
                PayCycleBean payCycleBean = payCycleService.updatePayCyclePayOff(payCycleId);
                totalWages = payCycleBean.getTotalWages();
                rechargeType = CompanyRechargeConstant.SEND_SALARY_TYPE;
            }
        } else if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE.equals(salaryOrderFlag)) {//银行汇款
            if (companyRechargesBean.getRechargeName().endsWith(CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_RAPIDLYCHARGE)) {
                //急速发工资  返回薪资发放总金额
                RapidlyPayrollExcelBean rapidlyPayrollExcelBean = rapidlyPayrollExcelService.updateRapidlyPayOff(checkBean.getExcelId());
                totalWages = rapidlyPayrollExcelBean.getTotalWages();
                rechargeType = CompanyRechargeConstant.RAPIDLY_PAYROLL_TYPE;
            } else if (companyRechargesBean.getRechargeName().endsWith(CompanyRechargeConstant.COMPANY_RECHARGE_ORDERNAME_SALARYRECHARGE)) {
                //薪资核算  更新计薪周期  返回薪资发放总金额
                PayCycleBean payCycleBean = payCycleService.updatePayCyclePayOff(checkBean.getExcelId());
                totalWages = payCycleBean.getTotalWages();
                rechargeType = CompanyRechargeConstant.SEND_SALARY_TYPE;
            }
        }

        //TODO 到时候这儿记得要减去红包
        //获取最新的订单信息
        CompanyRechargesBean paymentBean = companyRechargesService.queryPaymentInfo(checkBean.getRechargeCompanyId(), totalWages);

        //拼装要更新的订单信息
        CompanyRechargesBean queryRechargeIdBean = new CompanyRechargesBean();
        queryRechargeIdBean.setRechargeType(rechargeType);
        queryRechargeIdBean.setExcelId(payCycleId);
        queryRechargeIdBean.setRechargeId(checkBean.getRechargeId());
        List<CompanyRechargesBean> list = companyRechargesService.selectByExcelIdAndType(queryRechargeIdBean);
        if (list == null || list.size() <= 0) {
            throw new BusinessException("充值发工资订单审核,获取不到发工资的订单信息");
        }
        //获取发工资订单ID
        Long rechargeId = list.get(0).getRechargeId();
        CompanyRechargesBean successCompanyRechargesBean = new CompanyRechargesBean();
        //订单ID
        successCompanyRechargesBean.setRechargeId(rechargeId);
        //订单交易成功
        successCompanyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_SUCCESS);
        //最后支付日期
        successCompanyRechargesBean.setRechargeRecallTime(new Date());

        //更新订单最新信息
        if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_AVIAMOUNT.equals(salaryOrderFlag)) {
            //余额扣款前验证垫付金额\还需支付金额\可用余额是否有变动
            checkDifferentAmount(checkBean, paymentBean, checkBean.getRechargeCompanyId());
            //扣款
            totalWages = paymentBean.getRechargeRealAmount();
            subAccountService.deduct(checkBean.getRechargeCompanyId(), totalWages, 3, successCompanyRechargesBean.getRechargeId(), AccountType.COMPANY, paymentBean.getRechargePaymentAmount());
            successCompanyRechargesBean.setRechargeStation(CompanyRechargeConstant.COMPANY_RECHARGE_STATION_YOUE);
            //垫付比例和垫付金额
            successCompanyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
            successCompanyRechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
            //实际支付额度
            successCompanyRechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
            int count = companyRechargesService.updateByPrimaryKeySelective(successCompanyRechargesBean);
            if (count <= 0) {
                throw new BusinessException("充值发工资订单从账户余额扣款,更新发工资订单失败");
            }
        } else if (CompanyRechargeConstant.COMPANY_RECHARGE_SALARY_COMPARE.equals(salaryOrderFlag)) {
            //充值的话,实际支付金额取充值时的金额,不取最新的数据
            totalWages = companyRechargesBean.getRechargeRealAmount();
            subAccountService.deduct(checkBean.getRechargeCompanyId(), totalWages, 3, successCompanyRechargesBean.getRechargeId(), AccountType.COMPANY, companyRechargesBean.getRechargePaymentAmount());
            successCompanyRechargesBean.setRechargeSerialNumber(companyRechargesBean.getRechargeSerialNumber());
            successCompanyRechargesBean.setRechargeBak(companyRechargesBean.getRechargeBak());
            successCompanyRechargesBean.setRechargeStation(CompanyRechargeConstant.COMPANY_RECHARGE_STATION_HUIKUAN);
            successCompanyRechargesBean.setRechargeBank(companyRechargesBean.getRechargeBank());
            successCompanyRechargesBean.setRechargeBanknumber(companyRechargesBean.getRechargeBanknumber());
            //垫付比例和垫付金额
            successCompanyRechargesBean.setRechargePaymentRate(companyRechargesBean.getRechargePaymentRate());
            successCompanyRechargesBean.setRechargePaymentAmount(companyRechargesBean.getRechargePaymentAmount());
            //实际支付额度
            successCompanyRechargesBean.setRechargeRealAmount(companyRechargesBean.getRechargeRealAmount());
            int count = companyRechargesService.updateByPrimaryKeySelective(successCompanyRechargesBean);
            if (count <= 0) {
                throw new BusinessException("充值发工资订单审核,更新发工资订单失败");
            }
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 扣款前验证垫付金额\还需支付金额\可用余额是否有变动
     *
     * @param checkBean
     * @param paymentBean
     * @param companyId
     */
    public void checkDifferentAmount(CompanyRechargesBean checkBean, CompanyRechargesBean paymentBean, Long companyId) {
        //扣款前验证垫付金额\还需支付金额\可用余额是否有变动
        if (checkBean.getRechargePaymentAmount().compareTo(paymentBean.getRechargePaymentAmount()) != 0) {//垫付金额
            throw new BusinessException("垫付金额有变动,请重新打开详情页面查看最新垫付金额");
        }
        //获取最新的可用余额
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyId, AccountType.COMPANY);
        BigDecimal paymentAmount = (subAccountBean != null && subAccountBean.getCashAmout() != null) ? subAccountBean.getCashAmout() : new BigDecimal("0");
        if (paymentAmount.compareTo(checkBean.getCanUserCashAmount()) != 0) {//可用余额
            throw new BusinessException("可用余额有变动,请重新打开详情页面查看最新可用余额");
        }
        if (checkBean.getRechargeRealAmount().compareTo(paymentBean.getRechargeRealAmount()) != 0) {//实际支付金额
            throw new BusinessException("实际支付金额有变动,请重新打开详情页面查看最新实际支付金额");
        }
    }

    /**
     * 获取发薪日期
     *
     * @param payCycleBean
     * @return
     */
    public Date getPayDay(PayCycleBean payCycleBean) {
        //获取计薪规则
        PayRuleBean payRuleBean = payRuleReaderMapper.selectByCompanyId(payCycleBean.getCompanyId());
        if (payRuleBean.getPayDay() == null) {
            throw new BusinessException("请设置发薪日");
        }
        Date payDay = DateUtil.stringToDate(payCycleBean.getYear() + "-" + payCycleBean.getMonth() + "-" + payRuleBean.getPayDay(), DateUtil.DATEYEARFORMATTER);
        if (payRuleBean.getPayDayType().intValue() == 1) {
            //次月
            payDay = DateUtil.addDateOfMonth(payDay, 1);
        }
        return payDay;
    }

    /**
     * 判断用户是否有访问账户权限
     *
     * @param companyMembersBean
     * @return true有访问权限 false无访问权限
     */
    public boolean validateCustomerRole(CompanyMembersBean companyMembersBean) {
        if (companyMembersBean.getMemberIsdefault() != null && companyMembersBean.getMemberIsdefault().intValue() == 1) {//管理员有一切的访问权限
            return true;
        } else {
            long menuId = companyMembersService.selectMenuIdByMenuName(CompanyConstant.COMPANYMEMBER_VISITMENU_VALIDATENAME);
            //判断是否有"我的账户"的访问权限
            int count_role = companyMembersService.selectCountForMemberVisitMenu(companyMembersBean.getMemberId(), menuId);

            return count_role > 0;
        }

    }

    /**
     * 生成订单,并返回订单本身
     *
     * @param companyId
     * @param payCycleId
     * @return
     */
    public CompanyRechargesBean generateSalaryOrder(Long companyId, Long payCycleId) {
        BigDecimal totalWages = customerPayrollService.getTotalWages(payCycleId);
        BigDecimal zeroBigDeciaml = new BigDecimal("0");
        totalWages = totalWages == null ? zeroBigDeciaml : totalWages;
//        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
//        //类型
//        companyRechargesBean.setRechargeType(CompanyRechargeConstant.SEND_SALARY_TYPE);
//        //充值金额
//        companyRechargesBean.setRechargeMoney(totalWages);
//        //企业ID
//        companyRechargesBean.setRechargeCompanyId(companyId);
//        //周期ID
//        companyRechargesBean.setExcelId(payCycleId);
//        //创建时间
//        companyRechargesBean.setRechargeAddtime(new Date());
//        //订单号
//        String code = idGeneratorService.getOrderId(BusinessEnum.SEND_SALARY);
//        companyRechargesBean.setRechargeNumber(code);
//        //状态
//        companyRechargesBean.setRechargeState(CompanyRechargeConstant.COMPANY_RECHARGE_STATE_WAIT);
        //订单名称
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(payCycleId);
        String name = "";
        if (payCycleBean != null) {
            name = payCycleBean.getYear() + "年" + payCycleBean.getMonth() + "月发工资订单";
        } else {
            name = new SimpleDateFormat("yyyy年MM月").format(new Date()) + "发工资订单";
        }
//        companyRechargesBean.setRechargeName(name);
////
//
//        //TODO 到时候记得添加红包
////        RedEnvelopeBean redEnvelopeBean=redEnvelopeService.getByCompanyId(companyId,CompanyRechargeConstant.SEND_SALARY_TYPE);
////        if(redEnvelopeBean!=null && redEnvelopeBean.getRedMoney()!=null){
////            companyRechargesBean.setRedWalletAmount(redEnvelopeBean.getRedMoney());
////        }else{
//        companyRechargesBean.setRedWalletAmount(zeroBigDeciaml);
////        }
//        //垫付金额,垫付比例
//        CompanyRechargesBean paymentBean = companyRechargesService.queryPaymentInfo(companyId, totalWages);
//        companyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
//        companyRechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
//        //实际支付额度
//        companyRechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
//        Long id = companyRechargesService.insertSelective(companyRechargesBean);
//        if (id == null || id.longValue() <= 0L) {
//            throw new BusinessException("新增发工资订单失败");
//        }
//        companyRechargesBean.setRechargeId(id);

        return companyRechargesService.generateOrder(companyId, payCycleId, CompanyRechargeConstant.SEND_SALARY_TYPE, totalWages, zeroBigDeciaml, name);
//        return companyRechargesBean;
    }

    /**
     * 我的账户进入详情页面,并继续付款显示的发工资详情页面信息
     * 垫付\还需支付\可用余额取最新的值
     *
     * @param rechargeId
     * @return
     */
    public CompanyRechargesBean showSalaryOrder(Long rechargeId, BigDecimal totalWages) {
        CompanyRechargesBean companyRechargesBean = companyRechargesService.selectByPrimaryKey(rechargeId);
        if (companyRechargesBean != null) {
//            BigDecimal totalWages = customerPayrollService.getTotalWages(companyRechargesBean.getExcelId());
//            BigDecimal zeroBigDeciaml = new BigDecimal("0");
//            totalWages = totalWages == null ? zeroBigDeciaml : totalWages;
            //垫付金额,垫付比例
            CompanyRechargesBean paymentBean = companyRechargesService.queryPaymentInfo(companyRechargesBean.getRechargeCompanyId(), totalWages);
            companyRechargesBean.setRechargePaymentRate(paymentBean.getRechargePaymentRate());
            companyRechargesBean.setRechargePaymentAmount(paymentBean.getRechargePaymentAmount());
            //实际支付额度
            companyRechargesBean.setRechargeRealAmount(paymentBean.getRechargeRealAmount());
        }
        return companyRechargesBean;
    }

    /**
     * 获取垫付金额及垫付比例
     *
     * @param companyId
     * @param totalWages
     * @return
     */
//    public CompanyRechargesBean queryPaymentInfo(Long companyId, BigDecimal totalWages) {
//        BigDecimal zeroBigDeciaml = new BigDecimal("0");
//        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
//        //垫付金额,垫付比例
//        CompanyProtocolsBean companyProtocolsBean = new CompanyProtocolsBean();
//        companyProtocolsBean.setProtocolCompanyId(companyId);
//        companyProtocolsBean.setProtocolContractType(CompanyProtocolConstants.PROTOCOL_TYPE_FF);
//        List<CompanyProtocolsBean> protocolList = companyProtocolsService.selectByContractType(companyProtocolsBean);
//        if (protocolList != null && protocolList.size() > 0) {
//            for (CompanyProtocolsBean protocolsBean : protocolList) {
//                if (protocolsBean != null && protocolsBean.getProtocolCurrentStatus() != null
//                        && (protocolsBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
//                        || protocolsBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL)) {
//                    BigDecimal paymentRate = protocolsBean.getProtocolScale() == null ? zeroBigDeciaml : protocolsBean.getProtocolScale();
//                    companyRechargesBean.setRechargePaymentRate(paymentRate);
//                    BigDecimal rechargePaymentAmount = paymentRate.multiply(totalWages).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
//                    companyRechargesBean.setRechargePaymentAmount(rechargePaymentAmount);
//                    break;
//                }
//            }
//        }
//
//
//        if (companyRechargesBean.getRechargePaymentAmount() == null || companyRechargesBean.getRechargePaymentRate() == null) {
//            companyRechargesBean.setRechargePaymentRate(zeroBigDeciaml);
//            companyRechargesBean.setRechargePaymentAmount(zeroBigDeciaml);
//        }
//
//        //判断是否超过垫付额度，如果超过，则取垫付额度，如果没超过，取垫付全额
//        SubAccountBean subAccountBean = subAccountWriterMapper.selectByCustId(companyId, 2);
//        if (subAccountBean != null && subAccountBean.getUncashAmount() != null) {
//            BigDecimal uncashAmount = subAccountBean.getUncashAmount() != null ? subAccountBean.getUncashAmount() : new BigDecimal("0");
//            if (uncashAmount.compareTo(companyRechargesBean.getRechargePaymentAmount()) < 0) {
//                companyRechargesBean.setRechargePaymentAmount(uncashAmount);
//            }
//        }
//        LOGGER.info("新建发工资订单,获取垫付比例:" + companyRechargesBean.getRechargePaymentRate() + ",获取垫付金额:" + companyRechargesBean.getRechargePaymentAmount());
//        //实际支付额度
//        companyRechargesBean.setRechargeRealAmount(totalWages.subtract(companyRechargesBean.getRechargePaymentAmount()));
//        return companyRechargesBean;
//    }

    /**
     * 生成充值订单
     *
     * @param companyRechargesBean
     * @return
     */
    public ResultResponse generateCzOrder(CompanyRechargesBean companyRechargesBean) {
        ResultResponse resultResponse = new ResultResponse();
        Integer rechragesType = companyRechargesBean.getRechargeType();
        //订单类型
        companyRechargesBean.setRechargeType(CompanyRechargeConstant.RECHARGE_TYPE);
        companyRechargesBean = companyRechargesService.addRecharge(companyRechargesBean);
        if (companyRechargesBean != null && companyRechargesBean.getRechargeId() != null && companyRechargesBean.getRechargeId().longValue() > 0) {
            //更新发工资订单
            CompanyRechargesBean successCompanyRechargesBean = new CompanyRechargesBean();
            successCompanyRechargesBean.setExcelId(companyRechargesBean.getExcelId());
            successCompanyRechargesBean.setRechargeType(rechragesType);
            //垫付比例和垫付金额
            successCompanyRechargesBean.setRechargePaymentRate(companyRechargesBean.getRechargePaymentRate());
            successCompanyRechargesBean.setRechargePaymentAmount(companyRechargesBean.getRechargePaymentAmount());
            //实际支付额度
            successCompanyRechargesBean.setRechargeRealAmount(companyRechargesBean.getRechargeRealAmount());
            int count = companyRechargesService.updateScaleAndRealAmount(successCompanyRechargesBean);
            if (count > 0) {
                resultResponse.setData(companyRechargesBean.getRechargeId());
                resultResponse.setSuccess(true);
            } else {
                LOGGER.error("发工资充值订单生成,更新发工资订单实际支付额度失败");
                resultResponse.setMessage("发工资充值订单生成,更新发工资订单实际支付额度失败");
            }
        } else {
            LOGGER.error("发工资充值订单生成失败");
            resultResponse.setMessage("发工资充值订单生成失败");
        }
        return resultResponse;
    }

    /**
     * 获取社保公积金
     *
     * @return
     */
    public Map<Long, CustomerShebaoSumDto> getSbGjgMap(Long payCycleId) {
        return customerShebaoOrderService.getCustomerShebaoBase(payCycleId);
    }
}
