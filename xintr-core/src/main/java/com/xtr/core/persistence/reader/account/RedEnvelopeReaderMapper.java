package com.xtr.core.persistence.reader.account;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.account.RedEnvelopeBean;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface RedEnvelopeReaderMapper {

    /**
     * 根据红包主键查询红包对象
     *
     * @param redId
     * @return
     */
    RedEnvelopeBean selectByPrimaryKey(Long redId);

    /**
     * 分页查询红包记录
     *
     * @param redEnvelopeBean
     * @param pageBounds
     * @return
     */
    PageList<RedEnvelopeBean> selectPageList(RedEnvelopeBean redEnvelopeBean, PageBounds pageBounds);

    /**
     * 查询企业红包余额
     *
     * @param companyId
     * @param scope
     * @return
     */
    BigDecimal selectAmout(@Param("companyId") Long companyId, @Param("scope") Integer scope);

    /**
     * 查询企业可以使用的一个红包
     *
     * @param companyId
     * @param scope
     * @return
     */
    RedEnvelopeBean selectByCompanyId(@Param("companyId") Long companyId, @Param("scope") Integer scope);
}