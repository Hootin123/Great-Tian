package com.xtr.api.service.salary;

import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;

import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/10/28 10:08
 */
public interface RapidlyPayrollExcelService {

    /**
     * 根据指定主键获取一条数据库记录,rapidly_payroll_excel
     *
     * @param id
     */
    RapidlyPayrollExcelBean selectByPrimaryKey(Long id);

    /**
     * 新写入数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    RapidlyPayrollExcelBean insert(RapidlyPayrollExcelBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    int updateByPrimaryKeySelective(RapidlyPayrollExcelBean record);

    /**
     * 更新急速发工资工资发放状态为已发放
     *
     * @param id
     * @return
     */
    RapidlyPayrollExcelBean updateRapidlyPayOff(Long id);
}
