package com.xtr.api.service.validate;

import java.util.List;
import java.util.Map;

/**
 * <p>校验回调函数</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 15:16
 */
public interface ValidateService {

    /**
     * 验证
     *
     * @param obj
     * @throws Exception
     */
    void validate(Object obj) throws Exception;
}
