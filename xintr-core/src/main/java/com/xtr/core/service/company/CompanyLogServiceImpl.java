package com.xtr.core.service.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyLogBean;
import com.xtr.api.service.company.CompanyLogService;
import com.xtr.core.persistence.reader.company.CompanyLogReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyLogWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @createTime: 2016/8/8 16:53
 */
@Service("companyLogService")
public class CompanyLogServiceImpl implements CompanyLogService {

    @Resource
    private CompanyLogReaderMapper companyLogReaderMapper;

    @Resource
    private CompanyLogWriterMapper companyLogWriterMapper;

    /**
     * 新写入数据库记录,company_log
     *
     * @param record
     */
    public int insert(CompanyLogBean record) {
        return companyLogWriterMapper.insert(record);
    }

    /**
     * 分页查询日志信息
     *
     * @param companyLogBean
     * @return
     */
    public ResultResponse selectPageList(CompanyLogBean companyLogBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(companyLogBean.getPageIndex(), companyLogBean.getPageSize());
        PageList<CompanyLogBean> list = companyLogReaderMapper.listPage(companyLogBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        return resultResponse;
    }
}
