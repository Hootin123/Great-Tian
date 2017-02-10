package com.xtr.core.persistence.reader.account;

import com.xtr.api.domain.account.SubAccountBean;
import org.apache.ibatis.annotations.Param;

public interface SubAccountReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,sub_account
     *
     * @param id
     */
    SubAccountBean selectByPrimaryKey(Long id);

    /**
     * 根据custId查询账户详情
     *
     * @param custId
     * @param property
     * @return
     */
    SubAccountBean selectByCustId(@Param("custId") Long custId, @Param("property") Integer property);

    /**
     * 根据SubAccountBean查询账户信息
     *
     * @param subAccountBean
     * @return
     */
    SubAccountBean selectSubAccountBean(SubAccountBean subAccountBean);

}