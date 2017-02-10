package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.company.CompanyMoneyRecordsBean;

public interface CompanyMoneyRecordsReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,company_money_records
     *
     * @param recordId
     */
    CompanyMoneyRecordsBean selectByPrimaryKey(Long recordId);

    /**
     * 根据resourceId查询企业资金变动记录
     *
     * @param resourceId
     * @return
     */
    CompanyMoneyRecordsBean selectByResourceId(Long resourceId);

    /**
     * 分页查询
     * 
     * @param companyMoneyRecordsBean
     * @param pageBounds
     */
    PageList<CompanyMoneyRecordsBean> selectPageList(CompanyMoneyRecordsBean companyMoneyRecordsBean, PageBounds pageBounds);

}