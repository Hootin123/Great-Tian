package com.xtr.core.persistence.reader.customer;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.customer.CustomerMoneyRecordsBean;

import java.util.List;

public interface CustomerMoneyRecordsReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,customer_money_records
     *
     * @param recordId
     */
    CustomerMoneyRecordsBean selectByPrimaryKey(Long recordId);

    /**
     * 根据用户过滤条件查询资金列表
     * @param customerMoneyRecordsBean
     * @return
     */
    List<CustomerMoneyRecordsBean> selectByCustomerCondition(CustomerMoneyRecordsBean customerMoneyRecordsBean,PageBounds pageBounds);

}