package com.xtr.core.persistence.reader.company;

import com.xtr.api.domain.company.CompanyRechargeHistorysBean;

public interface CompanyRechargeHistorysReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,company_recharge_historys
     *
     * @param historyId
     */
    CompanyRechargeHistorysBean selectByPrimaryKey(Long historyId);

}