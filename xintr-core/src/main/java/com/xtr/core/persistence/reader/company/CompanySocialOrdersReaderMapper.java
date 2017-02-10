package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.company.CompanySocialOrdersBean;
import com.xtr.api.dto.company.CompanySocialOrdersDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanySocialOrdersReaderMapper {

    /**
     * 分页查询
     * @param companySocialOrdersBean
     * @return
     */
    List<CompanySocialOrdersDto> selectPageList(CompanySocialOrdersBean companySocialOrdersBean, PageBounds pageBounds);

    /**
     * 根据主键查询
     * @param orderId
     * @return
     */
    CompanySocialOrdersDto selectByOrderId(@Param("orderId") long orderId);

    /**
     * 根据主键查询
     * @param orderId
     * @return
     */
    CompanySocialOrdersBean find(@Param("orderId") long orderId);

    CompanySocialOrdersDto selectByOrderRechagrId(@Param("orderRechagrId") Long orderRechagrId);
}