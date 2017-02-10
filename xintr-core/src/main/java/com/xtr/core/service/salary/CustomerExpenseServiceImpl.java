package com.xtr.core.service.salary;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerExpenseDetailBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.customer.CustomerExpenseDto;
import com.xtr.api.dto.customer.RealExpenseDto;
import com.xtr.api.service.salary.CustomerExpenseService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.BigDecimalUtil;
import com.xtr.core.persistence.reader.customer.CustomerExpenseDetailReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerExpenseDetailWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomersWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import com.xtr.core.persistence.writer.salary.PayCycleWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报销管理实现
 * @Author Xuewu
 * @Date 2016/9/5.
 */
@Service("customerExpenseService")
public class CustomerExpenseServiceImpl implements CustomerExpenseService {

    @Resource
    private CustomerExpenseDetailReaderMapper customerExpenseDetailReaderMapper;

    @Resource
    private CustomerExpenseDetailWriterMapper customerExpenseDetailWriterMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CustomersWriterMapper customersWriterMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private PayCycleWriterMapper payCycleWriterMapper;


    @Override
//    @Transactional
    public ResultResponse selectPage(CustomerExpenseDto dto, Date startDate) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(dto.getPageIndex(), dto.getPageSize());
        PageList<Map<String, Object>> pages = customerExpenseDetailReaderMapper.selectPage(dto, startDate, pageBounds);
        resultResponse.setSuccess(true);
        resultResponse.setData(pages);
        resultResponse.setPaginator(pages.getPaginator());
        return resultResponse;
    }

    @Override
    public boolean updateExpense(long customerId, BigDecimal expense) throws BusinessException {
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
        if(customersBean == null)
            throw new BusinessException("员工信息不存在");

        if(expense == null || expense.compareTo(BigDecimal.ZERO) < 0 || expense.scale() > 2)
            throw new BusinessException("额度输入不正确");

        if(customersBean.getCustomerRegularTime() == null)
            throw new BusinessException("请先定薪");

        BigDecimal customerCurrentSalary = customersBean.getCustomerCurrentSalary();
//        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByCustomerId(customerId, payCycleBean.getId());
//        BigDecimal realWage = customerPayrollBean.getRealWage();
        if(expense.compareTo(customerCurrentSalary) > 0) {
            throw new BusinessException("额度已大于当月基本工资");
        }

        CustomersBean updateBean = new CustomersBean();
        updateBean.setCustomerId(customerId);
        updateBean.setCustomerCurrentExpense(expense);
        updateBean.setCustomerIsExpense(1);
        if(customersWriterMapper.updateByPrimaryKeySelective(updateBean) > 0) {
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());
            payCycleWriterMapper.updateGenerateState(0, payCycleBean.getId());
            customerPayrollWriterMapper.updatePayRollStatus(customerId, payCycleBean.getId());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int batchUpdateExpense(long companyId, BigDecimal gt, BigDecimal lt, float bl) throws BusinessException {
        if(gt == null && lt == null)
            throw new BusinessException("请输入参考金额");

        if(bl > 100 || bl < 0)
            throw new BusinessException("比例输入不正确");

        bl = bl / 100;
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
        int i = customerExpenseDetailWriterMapper.batchUpdateExpense(companyId, gt, lt, bl);
        payCycleWriterMapper.updateGenerateState(0, payCycleBean.getId());
        customerExpenseDetailWriterMapper.updatePayRollStatusByBatch(companyId, payCycleBean.getId(), gt, lt);
        return i;
    }

    @Override
    public boolean expense(CustomerExpenseDto dto, Long companyId) throws BusinessException {
        if(dto == null)
            return false;
        if(dto.getBeans() == null && dto.getDeleteId() == null)
            return false;
        if(dto.getCustomerId() == null){
            throw new BusinessException("用户不能为空");
        }
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);

        List<CustomerExpenseDetailBean> beans = dto.getBeans();
        if(beans != null) {
            Date now = new Date();
            for (CustomerExpenseDetailBean bean : beans) {
                if(StringUtils.isBlank(bean.getExpenseType()) || bean.getExpenseType().length() > 20) {
                    throw new BusinessException("请输入正确的报销类型");
                }

                if(bean.getExpenseAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessException("请输入正确的报销金额");
                }

                if(bean.getId() == null) { //新增
                    CustomerExpenseDetailBean customerExpenseDetailBean = new CustomerExpenseDetailBean();
                    customerExpenseDetailBean.setCustomerId(dto.getCustomerId());
                    customerExpenseDetailBean.setCompanyId(companyId);
                    customerExpenseDetailBean.setCycleId(payCycleBean.getId());
                    customerExpenseDetailBean.setExpenseAmount(bean.getExpenseAmount().setScale(2));
                    customerExpenseDetailBean.setExpenseDate(now);
                    customerExpenseDetailBean.setCreateTime(now);
                    customerExpenseDetailBean.setUpdateTime(now);
                    customerExpenseDetailBean.setExpenseType(bean.getExpenseType());

                    customerExpenseDetailWriterMapper.insert(customerExpenseDetailBean);
                }else{//修改
                    CustomerExpenseDetailBean customerExpenseDetailBean = new CustomerExpenseDetailBean();
                    customerExpenseDetailBean.setId(bean.getId());
                    customerExpenseDetailBean.setExpenseAmount(bean.getExpenseAmount().setScale(2));
                    customerExpenseDetailBean.setExpenseType(bean.getExpenseType());

                    customerExpenseDetailWriterMapper.updateByPrimaryKeySelective(customerExpenseDetailBean);
                }
            }
        }

        if(dto.getDeleteId() != null && dto.getDeleteId().size() > 0) {
            customerExpenseDetailWriterMapper.deleteByIds(StringUtils.join(dto.getDeleteId(), ","));
        }

        payCycleWriterMapper.updateGenerateState(0, payCycleBean.getId());
        customerPayrollWriterMapper.updatePayRollStatus(dto.getCustomerId(), payCycleBean.getId());
        return true;
    }

    @Override
    public List<CustomerExpenseDetailBean> selectExpense(long customerId, Long companyId) {
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
        return customerExpenseDetailReaderMapper.selectExpense(customerId, payCycleBean.getId());
    }

    @Override
    public RealExpenseDto selectExpense(CustomersBean customersBean, long cycleId, BigDecimal yf) {
        BigDecimal customerCurrentExpense = customersBean.getCustomerCurrentExpense();

        BigDecimal customerCurrentSalary = customersBean.getCustomerCurrentSalary();

        if(yf == null)
            yf = new BigDecimal(0);

        if(customerCurrentExpense == null)
            customerCurrentExpense = new BigDecimal(0);

        if(customerCurrentSalary == null)
            customerCurrentSalary = new BigDecimal(0);

        //实际额度
//        BigDecimal expenseMax = yf.min(customerCurrentExpense);
        BigDecimal expenseMax = BigDecimalUtil.min(customerCurrentExpense, yf, customerCurrentSalary);

        //总报销金额
        BigDecimal expenseSum = customerExpenseDetailReaderMapper.selectCustomerExpenseSum(customersBean.getCustomerId(), cycleId);
        if(expenseSum == null)
            expenseSum = new BigDecimal(0);

        //显示报销金额
        BigDecimal realExpense = expenseMax.min(expenseSum);

        //显示绩效补充
        BigDecimal lastExpenseMax = expenseMax.subtract(realExpense);

        return new RealExpenseDto(expenseMax, expenseSum, realExpense, lastExpenseMax, customerCurrentExpense);
    }

    @Override
    public int selectUnSetCount(Long companyId, long cycleId, Date startDate) {
        return customerExpenseDetailReaderMapper.selectUnsetCount(companyId, cycleId, startDate);
    }

}
