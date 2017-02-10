package com.xtr.core.service.app;

import com.xtr.api.domain.app.AppVersionBean;
import com.xtr.api.service.app.AppVersionService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.app.AppVersionReaderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>App版本控制</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/8 11:10
 */
@Service("appVersionService")
public class AppVersionServiceImpl implements AppVersionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppVersionServiceImpl.class);

    @Resource
    private AppVersionReaderMapper appVersionReaderMapper;

    /**
     * 根据APP类型查询版本信息
     *
     * @return
     */
    public AppVersionBean selectAppVersion(Integer appType) {
        if (appType != null) {
            return appVersionReaderMapper.selectAppVersion(appType);
        } else {
            throw new BusinessException("参数不能为空");
        }
    }
}
