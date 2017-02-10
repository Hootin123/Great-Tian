package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMsgsBean;
import com.xtr.api.domain.company.CompanyNotesBean;

import java.util.List;

/**
 * Created by xuewu on 2016/7/26.
 * 企业联系小记
 */
public interface CompanyNotesService {

    boolean save(CompanyNotesBean companyNotesBean) throws Exception;

    ResultResponse selectPage(long cId, int page, int pageSize);

}
