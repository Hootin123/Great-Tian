package com.xtr.api.service.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerExpenseDetailBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.customer.CustomerExpenseDto;
import com.xtr.api.dto.customer.RealExpenseDto;
import com.xtr.comm.basic.BusinessException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 报销管理
 * @Author Xuewu
 * @Date 2016/9/5.
 */
public interface CustomerExpenseService {


    /**
     * 列表查询
     * @param dto
     * @param startDate
     * @return
     */
    ResultResponse selectPage(CustomerExpenseDto dto, Date startDate);


    /**
     * 调额
     * @param customerId
     * @param expense
     * @return
     * @throws BusinessException
     */
    boolean updateExpense(long customerId, BigDecimal expense) throws BusinessException;

    /**
     * 批量调额
     * @param memberCompanyId
     * @param gt
     * @param lt
     * @param bl
     * @return
     */
    int batchUpdateExpense(long memberCompanyId, BigDecimal gt, BigDecimal lt, float bl) throws BusinessException;

    /**
     * 报销
     * @param dto
     * @param memberCompanyId
     * @return
     * @throws BusinessException
     */
    boolean expense(CustomerExpenseDto dto, Long memberCompanyId) throws BusinessException;

    /**
     * 查询用户报销记录
     * @param customerId
     * @param memberCompanyId
     * @return
     */
    List<CustomerExpenseDetailBean> selectExpense(long customerId, Long memberCompanyId);

    /**
     * 计算实际报销金额
     * @param customersBean 员工
     * @param cycleId 周期ID
     * @param yf 应发金额
     * @return
     */
    RealExpenseDto selectExpense(CustomersBean customersBean, long cycleId, BigDecimal yf);

    /**
     * 查询未定额人数
     * @param companyId
     * @param startDate
     * @return
     */
    int selectUnSetCount(Long companyId, long cycleId, Date startDate);
}
