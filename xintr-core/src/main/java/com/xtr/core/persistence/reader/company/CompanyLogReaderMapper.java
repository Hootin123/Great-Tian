package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.company.CompanyLogBean;

public interface CompanyLogReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,company_log
     *
     * @param id
     */
    CompanyLogBean selectByPrimaryKey(Long id);

    /**
     * 分页查询日志
     *
     * @param companyLogBean
     * @param pageBounds
     * @return
     */
    PageList<CompanyLogBean> listPage(CompanyLogBean companyLogBean, PageBounds pageBounds);
}