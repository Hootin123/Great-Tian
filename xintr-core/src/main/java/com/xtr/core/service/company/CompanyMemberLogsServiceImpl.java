package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.dto.company.CompanyMemberLogDto;
import com.xtr.api.service.company.CompanyMemberLogsService;
import com.xtr.core.persistence.reader.company.CompanyMemberLogsReaderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>企业员工操作日志</p>
 *
 * @author 任齐
 * @createTime: 2016/7/11 16:00
 */
@Service("companyMemberLogsService")
public class CompanyMemberLogsServiceImpl implements CompanyMemberLogsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyMemberLogsService.class);

    @Resource
    private CompanyMemberLogsReaderMapper companyMemberLogsReaderMapper;

    /**
     * 分页查询日志
     *
     * @param uname
     * @param date_str
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultResponse selectPageList(Long memberId, String uname, String date_str, int pageIndex, int pageSize) {
        ResultResponse resultResponse = new ResultResponse();

        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);

        PageList<CompanyMemberLogDto> list = companyMemberLogsReaderMapper.selectPage(memberId, uname, date_str, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));

        return resultResponse;
    }
}
