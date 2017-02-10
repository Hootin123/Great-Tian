package com.xtr.core.persistence.reader.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.sys.SysLogBean;

public interface SysLogReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,sys_log
     *
     * @param id
     */
    SysLogBean selectByPrimaryKey(Long id);

    /**
     * 分页查询日志
     *
     * @param sysLogBean
     * @param pageBounds
     * @return
     */
    PageList<SysLogBean> listPage(SysLogBean sysLogBean, PageBounds pageBounds);
}