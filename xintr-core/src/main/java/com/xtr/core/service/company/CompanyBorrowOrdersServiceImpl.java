package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.BorrowInfoBean;
import com.xtr.api.domain.company.CompanyBorrowBillsBean;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;
import com.xtr.api.dto.company.CompanyPayWithDto;
import com.xtr.api.service.company.CompanyBorrowOrdersService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.company.CompanyBorrowBillsReaderMapper;
import com.xtr.core.persistence.reader.company.CompanyBorrowOrdersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyBorrowOrdersWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>企业借款订单</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 12:06
 */
@Service("companyBorrowOrdersService")
public class CompanyBorrowOrdersServiceImpl implements CompanyBorrowOrdersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyBorrowOrdersServiceImpl.class);

    @Resource
    private CompanyBorrowOrdersReaderMapper companyBorrowOrdersReaderMapper;

    @Resource
    private CompanyBorrowOrdersWriterMapper companyBorrowOrdersWriterMapper;

    @Resource
    private CompanyBorrowBillsReaderMapper companyBorrowBillsReaderMapper;

    /**
     * 根据主键查询企业订单数据
     *
     * @param orderId 企业借款订单id
     */
    public CompanyBorrowOrdersBean selectByPrimaryKey(Long orderId) {
        return companyBorrowOrdersReaderMapper.selectByPrimaryKey(orderId);
    }

    /**
     * 查询企业借款订单列表
     *
     * @param companyBorrowOrdersBean
     * @return
     */
    public List<CompanyBorrowOrdersBean> selectList(CompanyBorrowOrdersBean companyBorrowOrdersBean) {

        return null;
    }

    /**
     * 根据企业借款订单id查询还款详情
     *
     * @param orderId 企业借款订单id
     */
    public CompanyPayWithDto selectCompanyBorrowOrderDetail(Long orderId) throws BusinessException {
        if(null == orderId){
            throw new BusinessException("企业借款订单id不能为空");
        }

        CompanyPayWithDto companyPayWithDto = new CompanyPayWithDto();
        CompanyBorrowOrdersBean order = this.selectByPrimaryKey(orderId);
        companyPayWithDto.setOrder(order);

        List<CompanyBorrowBillsBean> companyBorrowBillsBeans = companyBorrowBillsReaderMapper.selectListByOrderId(orderId);
        companyPayWithDto.setBills(companyBorrowBillsBeans);
        return companyPayWithDto;
    }

    /**
     * 分页查询企业借款订单
     *
     * @param companyBorrowOrdersBean
     */
    public ResultResponse selectPageList(CompanyBorrowOrdersBean companyBorrowOrdersBean) {
        ResultResponse resultResponse = new ResultResponse();
        List<CompanyBorrowOrdersBean> list = companyBorrowOrdersReaderMapper.selectPageList(companyBorrowOrdersBean);
        resultResponse.setData(list);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 新写入数据库记录,company_borrow_orders
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addBorrowOrder(CompanyBorrowOrdersBean companyBorrowOrdersBean) throws BusinessException {
        if(null == companyBorrowOrdersBean){
            throw new BusinessException("企业借款订单参数为空");
        }
        LOGGER.info("接受参数：" + JSON.toJSONString(companyBorrowOrdersBean));
        companyBorrowOrdersBean.setOrderAddtime(new Date());
        int result = companyBorrowOrdersWriterMapper.insert(companyBorrowOrdersBean);
        if (result <= 0) {
            throw new BusinessException("企业借款订单保存失败");
        }
        return result;
    }

    /**
     * 分页查询企业流水
     *
     * @param companyBorrowOrdersBean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResultResponse selectPageBorrowOrdersList(CompanyBorrowOrdersBean companyBorrowOrdersBean, int pageIndex, int pageSize) {
        ResultResponse resultResponse = new ResultResponse();
        if(null != companyBorrowOrdersBean){
            PageBounds pageBounds = new PageBounds(pageIndex, pageSize);
            PageList list = companyBorrowOrdersReaderMapper.selectPageBorrowOrdersList(companyBorrowOrdersBean, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = list.getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
            LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        }
        return resultResponse;
    }

    @Override
    public ResultResponse selectPageListAll(CompanyBorrowOrdersBean companyBorrowOrdersBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(companyBorrowOrdersBean.getPageIndex(), companyBorrowOrdersBean.getPageSize());
        List list = companyBorrowOrdersReaderMapper.selectPageList(companyBorrowOrdersBean, pageBounds);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        for(int i=0;i<list.size();i++){
            CompanyBorrowOrdersBean obj= (CompanyBorrowOrdersBean) list.get(i);
            if(obj.getOrderExpectDay()!=null){
                if(obj.getOrderInterestType()!=null && obj.getOrderInterestType()==1){
                    obj.setOrderExpectDayValue(sdf.format(new Date(obj.getOrderExpectDay().getTime()+(obj.getOrderInterestCycle()*24*60*60*1000l))));
                }else{
                    obj.setOrderExpectDayValue(sdf.format(new Date(obj.getOrderExpectDay().getTime()+(obj.getOrderInterestCycle()*30*24*60*60*1000l))));
                }
            }
        }
        resultResponse.setData(list);
        Paginator paginator = ((PageList) list).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    @Override
    public int addBorrowInfoBean(BorrowInfoBean borrowInfoBean) throws BusinessException {
        if (null == borrowInfoBean) {
            throw new BusinessException("上传文件参数为空");
        }
        LOGGER.info("接受参数：" + JSON.toJSONString(borrowInfoBean));
        int result = companyBorrowOrdersWriterMapper.insertBorrowInfoBeanById(borrowInfoBean);
        if (result <= 0) {
            throw new BusinessException("上传文件保存失败");
        }
        result=Integer.parseInt(borrowInfoBean.getId().toString());
        return result;
    }

    @Override
    public List<BorrowInfoBean> selectBorrowInfoBeanList(BorrowInfoBean borrowInfoBean) {
        return companyBorrowOrdersReaderMapper.selectBorrowInfoBeanList(borrowInfoBean);
    }

    @Override
    public void updateCompanyBorrowOrdersBeanId(CompanyBorrowOrdersBean companyBorrowOrdersBean) throws BusinessException {
        int result = companyBorrowOrdersWriterMapper.updateByPrimaryKeySelective(companyBorrowOrdersBean);
        if (result <= 0) {
            throw new BusinessException("修改垫付认证失败");
        }
    }

    @Override
    public CompanyBorrowOrdersBean selectByOrderNumber(String orderNumber) {
        return companyBorrowOrdersReaderMapper.selectByOrderNumber(orderNumber);
    }

}
