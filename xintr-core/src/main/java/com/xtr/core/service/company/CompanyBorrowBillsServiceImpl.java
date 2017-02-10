package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.xtr.api.basic.GenericCriteria;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyBorrowBillsBean;
import com.xtr.api.dto.company.CompanyRepayAccountDto;
import com.xtr.api.service.company.CompanyBorrowBillsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.company.CompanyBorrowBillsReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyBorrowBillsWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>企业借款账单</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 12:06
 */
@Service("companyBorrowBillsService")
public class CompanyBorrowBillsServiceImpl implements CompanyBorrowBillsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyBorrowBillsServiceImpl.class);

    @Resource
    private CompanyBorrowBillsReaderMapper companyBorrowBillsReaderMapper;

    @Resource
    private CompanyBorrowBillsWriterMapper companyBorrowBillsWriterMapper;

    /**
     * 根据企业借款订单查询账单列表
     *
     * @param borrowOrderId 企业借款订单id
     */
    public List<CompanyBorrowBillsBean> selectListByOrderId(Long borrowOrderId) {
        if (null != borrowOrderId) {
            return companyBorrowBillsReaderMapper.selectListByOrderId(borrowOrderId);
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param companyBorrowBillsBean
     */
    public ResultResponse selectPageList(CompanyBorrowBillsBean companyBorrowBillsBean) {
        ResultResponse resultResponse = new ResultResponse();
        List<CompanyBorrowBillsBean> list = companyBorrowBillsReaderMapper.selectPageList(companyBorrowBillsBean);
        resultResponse.setData(list);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 根据企业id查询还款记录
     *
     * @param companyId
     * @param pageBounds
     * @return
     */
    public PageList<CompanyRepayAccountDto> selectRepayPageList(Long companyId, Pager pageBounds) {
        if (null == companyId) {
            return null;
        }
        return companyBorrowBillsReaderMapper.selectRepayPageList(companyId, PageBounds.create(pageBounds));
    }

    /**
     * 新写入数据库记录,company_borrow_bills
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addBorrowBills(CompanyBorrowBillsBean companyBorrowBillsBean) throws BusinessException {
        if (null == companyBorrowBillsBean) {
            throw new BusinessException("企业借款账单参数为空");
        }
        LOGGER.info("接受参数：" + JSON.toJSONString(companyBorrowBillsBean));
        companyBorrowBillsBean.setBillAddtime(new Date());
        int result = companyBorrowBillsWriterMapper.insert(companyBorrowBillsBean);
        if (result <= 0) {
            throw new BusinessException("企业借款账单保存失败");
        }
        return result;
    }

    /**
     * 查询企业最近一个月的还款
     *
     * @param companyId
     * @param state     不传为所有，1:最近一个月
     * @return
     */
    public BigDecimal selectRepayMoney(Long companyId, Integer state) {
        BigDecimal bigDecimal = null;
        if (null != companyId) {
            GenericCriteria genericCriteria = new GenericCriteria();
            GenericCriteria.Criteria criteria = genericCriteria.createCriteria();
            criteria.equalTo("bill_company_id", companyId);
            criteria.equalTo("bill_sign", 0);

            if (null != state && state == 1) {
                Date start = new Date();
                Date end = DateUtil.addDate(start, 30);
                criteria.between("bill_repaytime", start, end);
            }

            bigDecimal = companyBorrowBillsReaderMapper.selectRepayMoney(genericCriteria);
        }
        if (null == bigDecimal) {
            bigDecimal = new BigDecimal(0.00);
        }
        return bigDecimal;
    }
}
