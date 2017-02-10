package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;

import java.util.Date;

/**
 * <p>企业用户操作日志</p>
 *
 * @author 任齐
 * @createTime: 2016/7/11 15:58
 */
public interface CompanyMemberLogsService {

    /**
     * 分页查询日志
     *
     * @param uname
     * @param date_str
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultResponse selectPageList(Long memberId, String uname, String date_str, int pageIndex, int pageSize);

}
