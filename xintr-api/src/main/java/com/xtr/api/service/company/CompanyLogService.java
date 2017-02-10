package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;
import com.xtr.api.domain.company.CompanyLogBean;

/**
 * <p>类说明</p>
 *
 * @createTime: 2016/8/18 16:52
 */

public interface CompanyLogService {

    /**
     * 新写入数据库记录,sys_log
     *
     * @param record
     */
    int insert(CompanyLogBean record);

    /**
     * 分页查询日志信息
     *
     * @param sysLogBean
     * @return
     */
    ResultResponse selectPageList(CompanyLogBean sysLogBean);
}
