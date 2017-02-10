package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanySocialOrdersBean;
import com.xtr.api.dto.company.CompanySocialOrdersDto;
import com.xtr.api.service.company.CompanySocialOrdersService;
import com.xtr.core.persistence.reader.company.CompanySocialOrdersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanySocialOrdersWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>企业上传工资文档</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:15
 */
@Service("companySocialOrdersService")
public class CompanySocialOrdersServiceImpl implements CompanySocialOrdersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanySocialOrdersServiceImpl.class);

    @Resource
    private CompanySocialOrdersReaderMapper companySocialOrdersReaderMapper;

    @Resource
    private CompanySocialOrdersWriterMapper companySocialOrdersWriterMapper;


    public long insert(CompanySocialOrdersBean companySocialOrdersBean) {
        long orderId=0;
        int cnt=companySocialOrdersWriterMapper.insertSelective(companySocialOrdersBean);
        orderId=companySocialOrdersBean.getOrderId();

        return orderId;
    }

    public ResultResponse selectPageList(CompanySocialOrdersBean companySocialOrdersBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(companySocialOrdersBean.getPageIndex(), companySocialOrdersBean.getPageSize());
        List<CompanySocialOrdersDto> list = companySocialOrdersReaderMapper.selectPageList(companySocialOrdersBean,pageBounds);
        resultResponse.setData(list);
        Paginator paginator = ((PageList) list).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    public int update(CompanySocialOrdersBean companySocialOrdersBean) {
        return companySocialOrdersWriterMapper.updateByPrimaryKeySelective(companySocialOrdersBean);
    }

    public CompanySocialOrdersBean find(long orderId) {
        return companySocialOrdersReaderMapper.find(orderId);
    }

    @Override
    public CompanySocialOrdersDto getByOrderId(Long orderId) {
        if(null != orderId){
            return companySocialOrdersReaderMapper.selectByOrderId(orderId);
        }
        return null;
    }

    /**
     * 根据订单编号查询
     *
     * @param orderRechagrId
     * @return
     */
    @Override
    public CompanySocialOrdersDto getByOrderRechagrId(Long orderRechagrId) {
        if(null != orderRechagrId){
            return companySocialOrdersReaderMapper.selectByOrderRechagrId(orderRechagrId);
        }
        return null;
    }
}
