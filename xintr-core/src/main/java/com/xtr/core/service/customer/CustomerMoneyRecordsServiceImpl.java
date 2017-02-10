package com.xtr.core.service.customer;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.customer.CustomerMoneyRecordsBean;
import com.xtr.api.service.customer.CustomerMoneyRecordsService;
import com.xtr.core.persistence.reader.customer.CustomerMoneyRecordsReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerMoneyRecordsWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/14 14:50
 */
@Service("customerMoneyRecordsService")
public class CustomerMoneyRecordsServiceImpl implements CustomerMoneyRecordsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMoneyRecordsServiceImpl.class);

    @Resource
    private CustomerMoneyRecordsReaderMapper customerMoneyRecordsReaderMapper;

    @Resource
    private CustomerMoneyRecordsWriterMapper customerMoneyRecordsWriterMapper;

    /**
     * 根据用户过滤条件查询资金列表
     * @param customerMoneyRecordsBean
     * @return
     */
    public AppResponse selectByCustomerCondition(CustomerMoneyRecordsBean customerMoneyRecordsBean){
        LOGGER.info("根据客户过滤条件获取我的消息列表,传递参数:"+ JSON.toJSONString(customerMoneyRecordsBean));
        AppResponse resultResponse = new AppResponse();
        if (customerMoneyRecordsBean != null) {
            PageBounds pageBounds = new PageBounds(customerMoneyRecordsBean.getPageIndex(), customerMoneyRecordsBean.getPageSize());
            List list = customerMoneyRecordsReaderMapper.selectByCustomerCondition(customerMoneyRecordsBean, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
//        return customerMoneyRecordsReaderMapper.selectByCustomerCondition(customerMoneyRecordsBean);
    }

    /**
     * 根据企业id删除
     *
     * @param companyId
     */
    @Override
    public void deleteByCompanyId(Long companyId) {
        if(null != companyId){
            customerMoneyRecordsWriterMapper.deleteByCompanyId(companyId);
        }
    }
}
