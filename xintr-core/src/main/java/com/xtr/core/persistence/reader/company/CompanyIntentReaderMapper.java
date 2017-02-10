package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.dto.company.CompanyIntentDto;

import java.util.List;

public interface CompanyIntentReaderMapper {

//    /**
//     * 根据指定主键获取一条数据库记录,company_intent
//     *
//     * @param intentId
//     */
//    CompanyIntentBean selectByPrimaryKey(Long intentId);

    /**
     * 根据过滤条件获取合作意向信息
     * @param companyIntentDto
     * @return
     */
    List<CompanyIntentDto> selectCompanyIntentInfoPageList(CompanyIntentDto companyIntentDto,PageBounds pageBounds);
}