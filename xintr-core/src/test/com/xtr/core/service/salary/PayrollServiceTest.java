package com.xtr.core.service.salary;

import com.xtr.BaseTest;
import com.xtr.api.dto.salary.CustomerPayrollQueryDto;
import com.xtr.api.service.customer.CustomerUpdateSalaryService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.comm.util.DateUtil;
import org.junit.Test;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/29 16:25.
 */
public class PayrollServiceTest extends BaseTest {

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CustomerUpdateSalaryService customerUpdateSalaryService;

    @Test
    public void testSelect() throws ParseException {

//        CustomerPayrollQueryDto queryDto = new CustomerPayrollQueryDto();
//        queryDto.setCompanyId(302L);
//        queryDto.setPayCycleId(1L);
//        queryDto.setPageSize(10);
//        payrollAccountService.selectCustomerPayroll(queryDto);
        System.out.println(customerUpdateSalaryService.calculateWorkNumberDay(DateUtil.string2Date("2016-08-26 00:00:00",DateUtil.dateTimeString), DateUtil.string2Date("2016-09-25 00:00:00",DateUtil.dateTimeString)));

    }

}
