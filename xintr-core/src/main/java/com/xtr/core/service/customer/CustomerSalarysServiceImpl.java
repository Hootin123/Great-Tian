package com.xtr.core.service.customer;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.dto.customer.CustomerUnPayOrderDto;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.customer.CustomerSalarysReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerSalarysWriterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>工资单服务实现类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 18:22
 */
@Service("customerSalarysService")
public class CustomerSalarysServiceImpl implements CustomerSalarysService {

    @Resource
    private CustomerSalarysReaderMapper customerSalarysReaderMapper;

    @Resource
    private CustomerSalarysWriterMapper customerSalarysWriterMapper;


    /**
     * 根据指定主键获取一条数据库记录,customer_salarys
     *
     * @param salaryId
     */
    public CustomerSalarysBean selectByPrimaryKey(Long salaryId) throws BusinessException {
        if (salaryId != null) {
            return customerSalarysReaderMapper.selectByPrimaryKey(salaryId);
        } else {
            throw new BusinessException("主键为空");
        }
    }

    /**
     * 新写入数据库记录,customer_salarys
     *
     * @param record
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(CustomerSalarysBean record) throws BusinessException {
        if (record != null) {
            int result = customerSalarysWriterMapper.insert(record);
            if (result <= 0) {
                throw new BusinessException("新增工资单失败");
            }
            return result;
        } else {
            throw new BusinessException("参数为空");
        }
    }

    /**
     * 根据企业id删除工资单
     *
     * @param companyId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteByCompanyId(Long companyId) {
        if(null != companyId){
            customerSalarysWriterMapper.deleteByCompanyId(companyId);
        }
    }

    /**
     * 查询工资单信息
     *
     * @param customerSalarysBean
     * @return
     */
    public List<CustomerSalarysBean> selectCustomerSalarys(CustomerSalarysBean customerSalarysBean) throws BusinessException {
        if (customerSalarysBean != null){
            return customerSalarysReaderMapper.selectCustomerSalarys(customerSalarysBean);
        }else{
            throw new BusinessException("参数为空");
        }
    }

    /**
     * 根据excelId获取工资单
     *
     * @param excelId
     * @return
     * @throws BusinessException
     */
    public List<CustomerSalarysBean> selectByExcelId(Long excelId) throws BusinessException{
        if (excelId != null){
            return customerSalarysReaderMapper.selectByExcelId(excelId);
        }else {
            throw new BusinessException("参数为空");
        }
    }

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_salarys
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(CustomerSalarysBean record){
        return customerSalarysWriterMapper.updateByPrimaryKeySelective(record);
    }

}
