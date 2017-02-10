package com.xtr.core.service.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyNotesBean;
import com.xtr.api.service.company.CompanyNotesService;
import com.xtr.core.persistence.reader.company.CompanyNotesReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyNotesWriterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuewu on 2016/7/28.
 *
 */
@Service("companyNotesService")
public class CompanyNotesServiceImpl implements CompanyNotesService {

    @Autowired
    private CompanyNotesReaderMapper companyNotesReaderMapper;

    @Autowired
    private CompanyNotesWriterMapper companyNotesWriterMapper;

    @Override
    public boolean save(CompanyNotesBean companyNotesBean) throws Exception {
        return companyNotesWriterMapper.insert(companyNotesBean) == 1;
    }

    @Override
    public ResultResponse selectPage(long cId, int page, int pageSize) {
        ResultResponse rr = new ResultResponse();
        PageBounds pageBounds = new PageBounds(page, pageSize);
        PageList<CompanyNotesBean> pageList = companyNotesReaderMapper.selectPage(cId, pageBounds);
        rr.setSuccess(true);
        rr.setData(pageList);
        rr.setPaginator(pageList.getPaginator());
        return rr;
    }
}
