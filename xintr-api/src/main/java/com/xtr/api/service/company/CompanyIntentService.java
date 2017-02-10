package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.dto.company.CompanyIntentDto;


/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/12 14:17
 */
public interface CompanyIntentService {

    /**
     * 根据过滤条件获取合作意向信息
     * @param companyIntentDto
     * @return
     */
    ResultResponse selectCompanyIntentInfoPageList(CompanyIntentDto companyIntentDto);
}
