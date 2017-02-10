package com.xtr.core.persistence.reader.app;

import com.xtr.api.domain.app.AppVersionBean;

public interface AppVersionReaderMapper {


    /**
     * 根据指定主键获取一条数据库记录,app_version
     *
     * @param id
     */
    AppVersionBean selectByPrimaryKey(Integer id);

    /**
     * 根据App类型查询版本信息
     *
     * @param appType
     * @return
     */
    AppVersionBean selectAppVersion(Integer appType);
}