package com.xtr.api.service.app;

import com.xtr.api.domain.app.AppVersionBean;

/**
 * <p>App版本控制</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/8 11:09
 */
public interface AppVersionService {


    /**
     * 根据APP类型查询版本信息
     *
     * @param appType
     * @return
     */
    AppVersionBean selectAppVersion(Integer appType);

}
