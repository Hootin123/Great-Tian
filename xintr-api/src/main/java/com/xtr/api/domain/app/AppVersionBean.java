package com.xtr.api.domain.app;

import java.io.Serializable;

public class AppVersionBean implements Serializable {
    /**
     * 主键,所属表字段为app_version.id
     */
    private Integer id;

    /**
     * app类型 0-android,1-ios ,所属表字段为app_version.app_type
     */
    private Integer appType;

    /**
     * 版本号,所属表字段为app_version.version
     */
    private String version;

    /**
     * app更新地址
     */
    private String url;

    /**
     * 更新类型 0:强制更新  1:普通更新
     */
    private String type;

    /**
     * 获取 主键 字段:app_version.id
     *
     * @return app_version.id, 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置 主键 字段:app_version.id
     *
     * @param id app_version.id, 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取 app类型 0-android,1-ios  字段:app_version.app_type
     *
     * @return app_version.app_type, app类型 0-android,1-ios
     */
    public Integer getAppType() {
        return appType;
    }

    /**
     * 设置 app类型 0-android,1-ios  字段:app_version.app_type
     *
     * @param appType app_version.app_type, app类型 0-android,1-ios
     */
    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    /**
     * 获取 版本号 字段:app_version.version
     *
     * @return app_version.version, 版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置 版本号 字段:app_version.version
     *
     * @param version app_version.version, 版本号
     */
    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}