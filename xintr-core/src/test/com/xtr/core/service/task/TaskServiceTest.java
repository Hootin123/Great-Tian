package com.xtr.core.service.task;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.xtr.BaseTest;
import com.xtr.api.domain.task.TaskLogBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.task.TaskLogService;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/26 15:24.
 */
public class TaskServiceTest extends BaseTest {

    @Resource
    private TaskLogService taskLogService;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Test
    public void testPager() throws Exception {
//        TaskLogBean taskLogBean = new TaskLogBean();
//        taskLogBean.setPageIndex(2);
//        taskLogBean.setPageSize(10);
//        taskLogService.selectPageList(taskLogBean);
//        taskLogService.selectPageList(taskLogBean);

        CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
//        System.out.println(JSON.toJSONString(payrollAccountService.selectPageList(customerPayrollDto,new Pager(1,10))));
    }
}
