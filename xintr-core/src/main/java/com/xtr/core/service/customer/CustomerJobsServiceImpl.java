package com.xtr.core.service.customer;

import com.xtr.api.service.customer.CustomerJobsService;
import com.xtr.core.persistence.writer.customer.CustomerJobsWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
@Service("customerJobsService")
public class CustomerJobsServiceImpl implements CustomerJobsService{
        @Resource
    private CustomerJobsWriterMapper customerJobsWriterMapper;

/*    public Map<String, Object> find(Map<String, Object> param) {
        return null;
    }*/

    public int add(Map<String, Object> param) {
        return customerJobsWriterMapper.add(param);
    }
/*
    public List<Map<String, Object>> findListPage(Map<String, Object> param) {
        return null;
    }

    public int findListCount(Map<String, Object> param) {
        return 0;
    }

    public void addJobList(Map<String, Object> param) {

    }

    public List<Map<String, Object>> findGroupPage(Map<String, Object> param) {
        return null;
    }

    public int findGroupCount(Map<String, Object> param) {
        return 0;
    }*/
}
