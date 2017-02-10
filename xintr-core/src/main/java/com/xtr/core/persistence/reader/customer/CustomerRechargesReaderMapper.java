package com.xtr.core.persistence.reader.customer;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.dto.customer.CustomWithDrawlsBatchDto;
import com.xtr.api.dto.customer.CustomerReachrgeDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerRechargesReaderMapper {

    CustomerRechargesBean selectByPrimaryKey(Long rechargeId);

    PageList<CustomWithDrawlsBatchDto> selectBatchPageList(@Param("startTime") String startTime,
                                                           @Param("state") Integer state,
                                                           @Param("auditName") String auditName, PageBounds pageBounds);

    PageList<CustomerRechargesBean> selectPageList(CustomerRechargesBean customerRechargesBean, PageBounds pageBounds);

    List<CustomerReachrgeDto> selectDetailList(@Param("batchNumber") String batchNumber);

    int getTodayOrderNum(@Param("rechargeType") Integer rechargeType);

    /**
     * 根据第三方平台获取所有待审批的提现申请
     *
     * @return
     */
    List<CustomerRechargesBean> getAllWithdrawals(Integer rechargeStation);

    CustomerRechargesBean selectByNumber(@Param("rNumber") String outTradeNo);

    /**
     *根据第三方平台获取所有社保公积金补退的提现申请
     * @param rechargeStation
     * @return
     */
    List<CustomerRechargesBean> getBackWithdrawals(Integer rechargeStation);
}