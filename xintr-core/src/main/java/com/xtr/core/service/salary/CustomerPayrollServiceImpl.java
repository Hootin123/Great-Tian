package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.salary.BonusSettingsBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.CustomerPayrollDetailBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.dto.salary.CustomerPayrollQueryDto;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/9/21 10:22
 */
@Service("customerPayrollService")
public class CustomerPayrollServiceImpl implements CustomerPayrollService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerPayrollServiceImpl.class);

    @Resource
    private CustomerPayrollReaderMapper customerPayrollReaderMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private PayrollAccountService payrollAccountService;

    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    /**
     * 查询所有工资单
     *
     * @param customerPayrollDto
     * @return
     */
    public List<CustomerPayrollDto> selectCustomerPayroll(CustomerPayrollDto customerPayrollDto) {
        Long startDate = System.currentTimeMillis();
        List<CustomerPayrollDto> list = customerPayrollReaderMapper.selectPageList(customerPayrollDto);
        list = ransformationPayroll(list);
        Long endDate = System.currentTimeMillis();
        LOGGER.info("查询工资单时间：" + DateUtil.dateDiff(startDate, endDate));
        return list;
    }

    /**
     * 分页查询工资单
     *
     * @param customerPayrollDto
     * @return
     */
    public ResultResponse selectCustomerPayrollPageList(CustomerPayrollDto customerPayrollDto) {
        Long startDate = System.currentTimeMillis();
        ResultResponse resultResponse = new ResultResponse();
        List<CustomerPayrollDto> list = customerPayrollReaderMapper.selectPageList(customerPayrollDto, new PageBounds(customerPayrollDto.getPageIndex(), customerPayrollDto.getPageSize()));
        resultResponse.setPaginator(((PageList) list).getPaginator());
        //转换津贴和奖金明细
        resultResponse.setData(ransformationPayroll(list));
        resultResponse.setSuccess(true);
        Long endDate = System.currentTimeMillis();
        LOGGER.info("分页查询工资单耗时：" + DateUtil.dateDiff(startDate, endDate));
        return resultResponse;
    }

    /**
     * 转换工资单
     *
     * @param list
     * @return
     */
    private List<CustomerPayrollDto> ransformationPayroll(List<CustomerPayrollDto> list) {
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                CustomerPayrollDto payrollDto = list.get(i);
                if (StringUtils.isNotBlank(payrollDto.getAllowanceJsonStr())) {
                    //津贴
                    payrollDto.setAllowanceList(JSON.parseArray(payrollDto.getAllowanceJsonStr(), CustomerPayrollDetailBean.class));
                }
                if (StringUtils.isNotBlank(payrollDto.getBonusJsonStr())) {
                    //奖金
                    payrollDto.setBonusList(JSON.parseArray(payrollDto.getBonusJsonStr(), CustomerPayrollDetailBean.class));
                }
            }
        }
        return list;
    }

    /**
     * 分页查询工资单,不存在则自动生成
     *
     * @param customerPayrollDto
     * @return
     */
    public ResultResponse selectPageList(CustomerPayrollDto customerPayrollDto) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        //检查是否已生成工资单,获取计薪周期
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customerPayrollDto.getCompanyId());
        if (payCycleBean != null) {
            customerPayrollDto.setStartDate(DateUtil.date2String(payCycleBean.getStartDate(), DateUtil.DATEYEARFORMATTER));
            customerPayrollDto.setEndDate(DateUtil.date2String(payCycleBean.getEndDate(), DateUtil.DATEYEARFORMATTER));
            PageList list1 = customersReaderMapper.selectCustomerPageList(customerPayrollDto, new PageBounds(customerPayrollDto.getPageIndex(), customerPayrollDto.getPageSize()));
            if (!list1.isEmpty()) {
                customerPayrollDto.setPayCycleId(payCycleBean.getId());
                //判断是否已生成工资单，如果已经生成工资单，则直接查询
                if (payCycleBean.getIsGeneratePayroll() == 0) {//未生成工资单
                    customerPayrollDto.setCustomerList(list1);
                    //获取所有的工资单
                    Map<Long, CustomerPayrollBean> map = getCustomerPayrollByPayCycleId(customerPayrollDto);
                    //生成工资单
                    payrollAccountService.generatePayroll(list1, map, payCycleBean);
                    //获取工资单
                    List list = selectCustomerPayroll(customerPayrollDto);
                    resultResponse.setPaginator(list1.getPaginator());
                    resultResponse.setData(list);
                } else {
                    //已生成工资单，直接查询
                    resultResponse = selectCustomerPayrollPageList(customerPayrollDto);
                }
            }

        }
        resultResponse.setSuccess(true);
        LOGGER.info("薪资核算查询成功···");
        return resultResponse;
    }

    /**
     * 根据计薪周期主键获取工资单
     *
     * @param customerPayrollDto
     */
    public Map<Long, CustomerPayrollBean> getCustomerPayrollByPayCycleId(CustomerPayrollDto customerPayrollDto) throws Exception {
        Map<Long, CustomerPayrollBean> map = new LinkedHashMap<>();
        if (customerPayrollDto != null) {
            List<CustomerPayrollDto> list = selectCustomerPayroll(customerPayrollDto);
            if (!list.isEmpty()) {
                for (CustomerPayrollBean customerPayrollBean : list) {
                    map.put(customerPayrollBean.getCustomerId(), customerPayrollBean);
                }
            }
        }
        return map;
    }

    /**
     * 筛选工资单
     *
     * @param customerPayrollQueryDto
     * @return
     */
    public ResultResponse selectCustomerPayroll(CustomerPayrollQueryDto customerPayrollQueryDto) {
        //获取当前计薪周期
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customerPayrollQueryDto.getCompanyId());
        //是否生成工资单 0:未生成 1:已生成
        if (payCycleBean.getIsGeneratePayroll() == 0) {
            //未生成工资单
            throw new BusinessException("当前计薪周期【" + payCycleBean.getCurrentPayCycle() + "】还未生成工资单，请稍后再试");
        }
        //检查是否已经生成工资单
        ResultResponse resultResponse = new ResultResponse();
        PageList<CustomerPayrollDto> list = customerPayrollReaderMapper.selectCustomerPayroll(customerPayrollQueryDto, new PageBounds(customerPayrollQueryDto.getPageIndex(), customerPayrollQueryDto.getPageSize()));
        resultResponse.setPaginator(list.getPaginator());
        //转换工资单
        resultResponse.setData(ransformationPayroll(list));
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 获取工资发放总金额
     *
     * @param payCycleId
     * @return
     */
    public BigDecimal getTotalWages(Long payCycleId) {
        return customerPayrollReaderMapper.getTotalWages(payCycleId);
    }

    /**
     * 新增奖金
     *
     * @param bonusSettingsBean
     * @throws BusinessException
     */
    public void saveBonusSettings(BonusSettingsBean bonusSettingsBean) throws Exception {
        //获取当前计薪周期
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(bonusSettingsBean.getCompanyId());
        if (payCycleBean != null) {
            CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
            customerPayrollDto.setCompanyId(bonusSettingsBean.getCompanyId());
            customerPayrollDto.setPayCycleId(payCycleBean.getId());
            //获取所有工资单
            List<CustomerPayrollDto> list = customerPayrollReaderMapper.selectPageList(customerPayrollDto);
            if (list != null && !list.isEmpty()) {
                int size = list.size();
                final List<BonusSettingsBean> bonusList = new ArrayList<>();
                bonusList.add(bonusSettingsBean);
                pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
                List<Future> rowResult = new CopyOnWriteArrayList<>();
                for (int i = 0; i < size; i++) {
                    final CustomerPayrollDto customerPayrollDto1 = list.get(i);
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            List<CustomerPayrollDetailBean> beanList = null;
                            if (StringUtils.isNotBlank(customerPayrollDto1.getBonusJsonStr())) {
                                beanList = JSON.parseArray(customerPayrollDto1.getBonusJsonStr(), CustomerPayrollDetailBean.class);
                            }
                            calcBonus(customerPayrollDto1, bonusList, beanList);
                            //更新工资单
                            customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollDto1);
                        }
                    });

                }
                //等待处理结果
                for (Future f : rowResult) {
                    f.get();
                }
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
                pool.shutdown();
            }
        }
    }

    /**
     * 计算奖金
     *
     * @param customerPayrollBean
     * @param bonusSettingsBeanList
     */
    public CustomerPayrollBean calcBonus(CustomerPayrollBean customerPayrollBean,
                                         List<BonusSettingsBean> bonusSettingsBeanList, List<CustomerPayrollDetailBean> list) {
        if (bonusSettingsBeanList != null && !bonusSettingsBeanList.isEmpty()) {
            if (list == null) {
                list = new ArrayList<>();
            }
            for (BonusSettingsBean bonusSettingsBean : bonusSettingsBeanList) {
                //员工工资单明细 津贴
                CustomerPayrollDetailBean customerPayrollDetailBean = new CustomerPayrollDetailBean();
                //主键
                customerPayrollDetailBean.setId(customerPayrollBean.getId() + bonusSettingsBean.getId());
                //工资单主键
//                customerPayrollDetailBean.setCustomerPayrollId(customerPayrollBean.getId());
                //明细项名称
                customerPayrollDetailBean.setDetailName(bonusSettingsBean.getBonusName());
                //明细项值
                customerPayrollDetailBean.setDetailValue(new BigDecimal(0));
                //明细项类型 0:津贴  1:奖金
                customerPayrollDetailBean.setDetailType(Integer.valueOf(1));
                //来源id
                customerPayrollDetailBean.setResourceId(bonusSettingsBean.getId());
                //新增员工工资单明细 奖金
                list.add(customerPayrollDetailBean);
            }
            customerPayrollBean.setBonusJsonStr(JSON.toJSONString(list));
        }
        return customerPayrollBean;
    }

    /**
     * 根据来源id删除奖金
     *
     * @param companyId
     * @param resourceId
     * @return
     */
    public int deleteByResourceId(Long companyId, final Long resourceId) throws Exception {
        //获取当前计薪周期
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
        if (payCycleBean != null) {
            final CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
            customerPayrollDto.setCompanyId(companyId);
            customerPayrollDto.setPayCycleId(payCycleBean.getId());
            //获取所有工资单
            final List<CustomerPayrollDto> list = customerPayrollReaderMapper.selectPageList(customerPayrollDto);
            if (list != null && !list.isEmpty()) {
                int size = list.size();
                pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
                List<Future> rowResult = new CopyOnWriteArrayList<>();
                for (int i = 0; i < size; i++) {
                    final int finalI = i;
                    pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            CustomerPayrollDto customerPayrollDto1 = list.get(finalI);
                            if (StringUtils.isNotBlank(customerPayrollDto1.getBonusJsonStr())) {
                                List<CustomerPayrollDetailBean> beanList = JSON.parseArray(customerPayrollDto1.getBonusJsonStr(), CustomerPayrollDetailBean.class);
                                if (beanList != null && !beanList.isEmpty()) {
                                    List<CustomerPayrollDetailBean> beanList1 = new ArrayList<>();
                                    //奖金总额
                                    BigDecimal totalBonus = new BigDecimal(0);
                                    for (int j = 0; j < beanList.size(); j++) {
                                        if (beanList.get(j).getResourceId().intValue() != resourceId.intValue()) {
                                            beanList1.add(beanList.get(j));
                                            totalBonus.add(beanList.get(j).getDetailValue());
                                        }
                                    }
                                    //奖金明细
                                    customerPayrollDto1.setBonusJsonStr(JSON.toJSONString(beanList1));
                                    //奖金总额
                                    customerPayrollDto1.setTotalBonus(totalBonus);
                                    //更新工资单
                                    customerPayrollWriterMapper.updateByPrimaryKeySelective(customerPayrollDto1);
                                }
                            }

                        }
                    });
                }
                //等待处理结果
                for (Future f : rowResult) {
                    f.get();
                }
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
                pool.shutdown();
            }
        }
        return 1;
    }

    /**
     * 动态字段,写入数据库记录,customer_payroll
     *
     * @param record
     */
    public int insertSelective(CustomerPayrollBean record) {
        return customerPayrollWriterMapper.insertSelective(record);
    }


    /**
     * 更新员工工资单状态
     *
     * @param cycleId
     * @return
     */
    @CacheEvict(value = "redisManager", key = "#cycleId + 'ShebaoBase'")
    public int updatePayRollStatusByCycle(long cycleId) {
        return customerPayrollWriterMapper.updatePayRollStatusByCycle(cycleId);
    }
}
