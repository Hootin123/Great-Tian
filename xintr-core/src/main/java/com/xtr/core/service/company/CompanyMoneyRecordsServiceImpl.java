package com.xtr.core.service.company;

import javax.annotation.Resource;

import com.github.miemiedev.mybatis.paginator.domain.*;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.writer.company.CompanyMoneyRecordsWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import com.xtr.api.service.company.CompanyMoneyRecordsService;
import com.xtr.core.persistence.reader.company.CompanyMoneyRecordsReaderMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>企业资金变动记录</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 12:01
 */
@Service("companyMoneyRecordsService")
public class CompanyMoneyRecordsServiceImpl implements CompanyMoneyRecordsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyMoneyRecordsServiceImpl.class);
	
    @Resource
    private CompanyMoneyRecordsReaderMapper companyMoneyRecordsReaderMapper;

    @Resource
    private CompanyMoneyRecordsWriterMapper companyMoneyRecordsWriterMapper;
    
    /**
     * 根据主键查询数据库记录
     * 
     * @param recordId
     */
    public CompanyMoneyRecordsBean selectByPrimaryKey(Long recordId) {
        return companyMoneyRecordsReaderMapper.selectByPrimaryKey(recordId);
    }


    /**
     * 分页查询
     *
     * @param companyMoneyRecordsBean
     * @return
     */
	public ResultResponse selectPageList(CompanyMoneyRecordsBean companyMoneyRecordsBean) {
		ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(companyMoneyRecordsBean.getPageIndex(), companyMoneyRecordsBean.getPageSize(), Order.formString("record_addtime.desc"));

        PageList<CompanyMoneyRecordsBean> list = companyMoneyRecordsReaderMapper.selectPageList(companyMoneyRecordsBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
	}

    /**
     * 保存企业资金变动记录
     *
     * @param companyMoneyRecordsBean
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addMoneyRecord(CompanyMoneyRecordsBean companyMoneyRecordsBean) throws BusinessException {
        if(null == companyMoneyRecordsBean){
            throw new BusinessException("企业资金变动记录参数为空");
        }
        try {
            return companyMoneyRecordsWriterMapper.insert(companyMoneyRecordsBean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 根据企业id删除企业资金变动记录
     *
     * @param companyId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int deleteByCompanyId(Long companyId) throws BusinessException {
        if(null == companyId){
            throw new BusinessException("企业id不能为空");
        }

        return companyMoneyRecordsWriterMapper.deleteByCompanyId(companyId);
    }
}
