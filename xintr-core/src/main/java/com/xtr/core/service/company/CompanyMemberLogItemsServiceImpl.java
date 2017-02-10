package com.xtr.core.service.company;

import com.xtr.api.service.company.CompanyMemberLogItemsService;
import com.xtr.core.persistence.writer.company.CompanyMemberLogItemsWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
@Service("companyMemberLogItemsService")
public class CompanyMemberLogItemsServiceImpl implements CompanyMemberLogItemsService {
    @Resource
    private CompanyMemberLogItemsWriterMapper companyMemberLogItemsWriterMapper;
    public void add(Map<String, Object> param) {

    }
}
