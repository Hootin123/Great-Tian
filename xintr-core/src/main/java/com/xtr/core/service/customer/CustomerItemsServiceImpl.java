package com.xtr.core.service.customer;

import com.xtr.api.service.customer.CustomerItemsService;
import com.xtr.core.persistence.writer.customer.CustomerItemsWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
@Service("customerItemsService")
public class CustomerItemsServiceImpl implements CustomerItemsService {

    @Resource
    private CustomerItemsWriterMapper customerItemsWriterMapper;
    public void addList(Map<String, Object> param) {
        customerItemsWriterMapper.addList(param);


    }
}
