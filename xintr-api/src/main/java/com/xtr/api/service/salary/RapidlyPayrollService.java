package com.xtr.api.service.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.dto.gateway.response.NotifyResponse;

import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/10/28 10:15
 */
public interface RapidlyPayrollService {

    /**
     * 批量插入工资单
     *
     * @param record
     */
    int batchInsert(RapidlyPayrollExcelBean record);

    /**
     * 分页查询工资单
     *
     * @param rapidlyPayrollBean
     * @return
     */
    ResultResponse selectPageList(RapidlyPayrollBean rapidlyPayrollBean);

    /**
     * 通过companyId,excelId查询所有工资单
     *
     * @param excelId
     * @param companyId
     * @return
     */
    List<RapidlyPayrollBean> selectByExcelId(Long companyId, Long excelId);

    /**
     * 生成工资单
     *
     * @param rapidlyPayrollExcelBean
     * @return
     */
    ResultResponse payOff(RapidlyPayrollExcelBean rapidlyPayrollExcelBean);

    /**
     * 快速发工资员工体现京东回调处理
     *
     * @param response
     */
    void doJdResponse(NotifyResponse response);
}
