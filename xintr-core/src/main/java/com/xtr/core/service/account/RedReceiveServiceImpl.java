package com.xtr.core.service.account;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedReceiveBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.account.RedReceiveService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.account.RedReceiveReaderMapper;
import com.xtr.core.persistence.writer.account.RedReceiveWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>红包使用记录服务实现</p>
 *
 * @author 任齐
 * @createTime: 2016/8/2 17:02
 */
@Service("redReceiveService")
public class RedReceiveServiceImpl implements RedReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedReceiveService.class);

    @Resource
    private RedReceiveReaderMapper redReceiveReaderMapper;

    @Resource
    private RedReceiveWriterMapper redReceiveWriterMapper;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    /**
     * 根据红包id或订单id查询红包使用记录
     *
     * @param redId
     * @param orderId
     * @return
     */
    @Override
    public RedReceiveBean getRedReceive(Long redId, Long orderId) {

        if(null == redId && null == orderId){
            return null;
        }

        RedReceiveBean redReceiveBean = new RedReceiveBean();
        redReceiveBean.setRedId(redId);
        redReceiveBean.setOrderId(orderId);
        return redReceiveReaderMapper.selectOneRedReceive(redReceiveBean);
    }

    /**
     * 分页查询红包使用记录
     *
     * @param redReceiveBean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultResponse getRedEnvelopePage(RedReceiveBean redReceiveBean, int pageIndex, int pageSize) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);

        PageList<RedReceiveBean> list = redReceiveReaderMapper.selectPageList(redReceiveBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 使用红包(插入一条红包使用记录)
     *
     * @param redReceiveBean
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void useRedEnvelope(RedReceiveBean redReceiveBean) throws BusinessException {
        if(null == redReceiveBean){
            throw new BusinessException("使用红包参数为空");
        }

        LOGGER.info("使用红包接受参数：" + JSON.toJSONString(redReceiveBean));

        if (null == redReceiveBean.getCompanyId()) {
            throw new BusinessException("企业id参数为空");
        }

        if (null == redReceiveBean.getReceiveMoney()) {
            throw new BusinessException("红包金额参数为空");
        }

        if (null == redReceiveBean.getRedId()) {
            throw new BusinessException("红包id参数为空");
        }

        if (null == redReceiveBean.getOrderId()) {
            throw new BusinessException("订单id参数为空");
        }

        try {

            redReceiveBean.setAddTime(new Date());

            redEnvelopeService.checkRedEnvelopeState(redReceiveBean.getCompanyId(), redReceiveBean.getRedId());

            // 检查使用使用过该红包
            RedReceiveBean temp = this.getRedReceive(redReceiveBean.getRedId(), redReceiveBean.getOrderId());
            if(null != temp){
                LOGGER.info("订单id【" + redReceiveBean.getOrderId() + "】重复使用红包【" + redReceiveBean.getRedId() + "】");
                throw new BusinessException("红包已经被使用，无法重复使用");
            }

            // 更新红包使用状态
            redEnvelopeService.updateState(redReceiveBean.getRedId(), 2);

            redReceiveWriterMapper.insert(redReceiveBean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException("红包使用记录保存失败");
        }

    }


}
