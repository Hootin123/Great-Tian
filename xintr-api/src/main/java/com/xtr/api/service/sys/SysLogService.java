package com.xtr.api.service.sys;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysLogBean;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/8 16:52
 */

public interface SysLogService {

    /**
     * 新写入数据库记录,sys_log
     *
     * @param record
     */
    int insert(SysLogBean record);

    /**
     * 分页查询日志信息
     *
     * @param sysLogBean
     * @return
     */
    ResultResponse selectPageList(SysLogBean sysLogBean);
}
