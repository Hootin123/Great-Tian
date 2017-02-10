package com.xtr.core.persistence.reader.customer;

import com.xtr.api.domain.customer.LegalHolidayBean;
import org.apache.ibatis.annotations.Param;

public interface LegalHolidayReaderMapper {

    /**
     * 根据年月查询
     * @param year
     * @param month
     * @return
     */
    LegalHolidayBean selectByYearAndMonth(@Param("year") int year, @Param("month") int month);

}