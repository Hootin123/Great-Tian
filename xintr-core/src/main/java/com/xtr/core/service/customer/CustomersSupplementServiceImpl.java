package com.xtr.core.service.customer;

import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.api.dto.customer.CustomersSupplementDto;
import com.xtr.api.service.customer.CustomersSupplementService;
import com.xtr.core.persistence.reader.customer.CustomersSupplementReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomersSupplementWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by allycw3 on 2016/10/9.
 */
@Service("customersSupplementService")
public class CustomersSupplementServiceImpl implements CustomersSupplementService {

    @Resource
    private CustomersSupplementReaderMapper customersSupplementReaderMapper;

    @Resource
    private CustomersSupplementWriterMapper customersSupplementWriterMapper;
    /**
     * 根据企业订单ID获取补收补退信息
     * @param supplementCompanyOrderId
     * @return
     */
    public List<CustomersSupplementDto> selectByCompanyOrderId(long supplementCompanyOrderId){
        return customersSupplementReaderMapper.selectByCompanyOrderId(supplementCompanyOrderId);
    }

    /**
     * 根据企业订单ID获取符合补退的信息
     * @return
     */
    public List<CustomersSupplementBean> selectBackInfoByCompanyOrderId(){
        return customersSupplementReaderMapper.selectBackInfoByCompanyOrderId();
    }

    /**
     *
     * @param list
     * @return
     */
    public int insertBatch(List<CustomersSupplementBean> list){
        return customersSupplementWriterMapper.insertBatch(list);
    }

    /**
     * 更新是否补退到员工账户状态
     * @param customersSupplementBean
     * @return
     */
    public int updateByPrimaryKeySelective(CustomersSupplementBean customersSupplementBean){
        return customersSupplementWriterMapper.updateByPrimaryKeySelective(customersSupplementBean);
    }

    /**
     * 根据企业订单ID和员工获取员工的补收,补退详情
     * @param CustomersSupplementBean
     * @return
     */
    public CustomersSupplementBean selectByCompanyOrderIdAndCustomerId(CustomersSupplementBean CustomersSupplementBean){
        return customersSupplementReaderMapper.selectByCompanyOrderIdAndCustomerId(CustomersSupplementBean);
    }
}
