package com.xtr.core.service.customer;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.customer.CustomerMsgsBean;
import com.xtr.api.service.customer.CustomerMsgsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AppConstants;
import com.xtr.core.persistence.reader.customer.CustomerMsgsReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerMsgsWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/14 15:31
 */
@Service("customerMsgsService")
public class CustomerMsgsServiceImpl implements CustomerMsgsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMsgsServiceImpl.class);

    @Resource
    private CustomerMsgsReaderMapper customerMsgsReaderMapper;

    @Resource
    private CustomerMsgsWriterMapper customerMsgsWriterMapper;
    /**
     * 根据客户过滤条件获取信息列表
     * @param customerMsgsBean
     * @return
     */
    public AppResponse selectMsgByCustomerCondition(CustomerMsgsBean customerMsgsBean)throws BusinessException{
        LOGGER.info("根据客户过滤条件获取我的消息列表,传递参数:"+ JSON.toJSONString(customerMsgsBean));
        AppResponse resultResponse = new AppResponse();
        if (customerMsgsBean != null) {
            PageBounds pageBounds = new PageBounds(customerMsgsBean.getPageIndex(), customerMsgsBean.getPageSize());
            List<CustomerMsgsBean> list = customerMsgsReaderMapper.selectMsgByCustomerCondition(customerMsgsBean, pageBounds);
            //更改查询到的消息为已读状态
            if(list!=null && list.size()>0){
                int count=updateMsgStateByList(list);
                if(count<=0){
                    throw new BusinessException("将消息更新为已读状态失败");
                }
                for(CustomerMsgsBean newCustomerMsgsBean:list){
                    newCustomerMsgsBean.setMsgSign(AppConstants.APP_MSGREAD_YES);
                }
            }
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
//        return customerMsgsReaderMapper.selectMsgByCustomerCondition(customerMsgsBean);
    }

    /**
     * 更新APP消息状态
     * @param list
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateMsgStateByList(List<CustomerMsgsBean> list)throws BusinessException{
        return customerMsgsWriterMapper.updateMsgStateList(list);
    }
    /**
     *  根据指定主键获取一条数据库记录,customer_msgs
     *
     * @param msgId
     */
    public CustomerMsgsBean selectByPrimaryKey(Long msgId){
        return customerMsgsReaderMapper.selectByPrimaryKey(msgId);
    }

    /**
     * 根据客户编码获取各种消息状态的消息列表
     * @param customerMsgsBean
     * @return
     */
    public List<CustomerMsgsBean> selectNoReaderMsgByCustomerId(CustomerMsgsBean customerMsgsBean){
        return customerMsgsReaderMapper.selectNoReaderMsgByCustomerId(customerMsgsBean);
    }

    /**
     * 保存员工消息
     *
     * @param customerMsgsBean
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void saveMsg(CustomerMsgsBean customerMsgsBean) throws BusinessException {

    }
}
