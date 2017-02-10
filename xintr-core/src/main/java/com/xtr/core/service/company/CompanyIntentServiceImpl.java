package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.dto.company.CompanyIntentDto;
import com.xtr.api.service.company.CompanyIntentService;
import com.xtr.core.persistence.reader.company.CompanyIntentReaderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/12 14:15
 */
@Service("companyIntentService")
public class CompanyIntentServiceImpl implements CompanyIntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyIntentServiceImpl.class);

    @Resource
    private CompanyIntentReaderMapper companyIntentReaderMapper;

    /**
     * 根据过滤条件获取合作意向信息
     * @param companyIntentDto
     * @return
     */
    public ResultResponse selectCompanyIntentInfoPageList(CompanyIntentDto companyIntentDto){
        LOGGER.info("根据过滤条件获取合作意向的参数信息:"+ JSON.toJSONString(companyIntentDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companyIntentDto != null) {
            PageBounds pageBounds = new PageBounds(companyIntentDto.getPageIndex(), companyIntentDto.getPageSize());
            List list = companyIntentReaderMapper.selectCompanyIntentInfoPageList(companyIntentDto, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }
}
