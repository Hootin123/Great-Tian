package com.xtr.api.service.customer;

import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.customer.CustomerMoneyRecordsBean;

import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/14 14:51
 */
public interface CustomerMoneyRecordsService {

    /**
     * 根据用户过滤条件查询资金列表
     * @param customerMoneyRecordsBean
     * @return
     */
    AppResponse selectByCustomerCondition(CustomerMoneyRecordsBean customerMoneyRecordsBean);

    /**
     * 根据企业id删除
     *
     * @param companyId
     */
    void deleteByCompanyId(Long companyId);
}
