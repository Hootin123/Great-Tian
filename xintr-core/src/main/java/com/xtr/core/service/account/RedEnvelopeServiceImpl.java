package com.xtr.core.service.account;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.RedActivityConstant;
import com.xtr.comm.enums.RedEvelopeScopeEnum;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.account.RedEnvelopeReaderMapper;
import com.xtr.core.persistence.writer.account.RedEnvelopeWriterMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>红包服务实现</p>
 *
 * @author 任齐
 * @createTime: 2016/8/2 16:44
 */
@Service("redEnvelopeService")
public class RedEnvelopeServiceImpl implements RedEnvelopeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedEnvelopeService.class);

    @Resource
    private RedEnvelopeReaderMapper redEnvelopeReaderMapper;

    @Resource
    private RedEnvelopeWriterMapper redEnvelopeWriterMapper;

    @Resource
    private CustomersService customersService;

    /**
     * 发红包
     *
     * @param redEnvelopeBean
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendRedEnvelope(RedEnvelopeBean redEnvelopeBean) throws BusinessException {

        if (null == redEnvelopeBean) {
            throw new BusinessException("红包参数为空");
        }

        LOGGER.info("保存红包接受参数:" + JSON.toJSONString(redEnvelopeBean));

        if (StringUtils.isBlank(redEnvelopeBean.getName())) {
            throw new BusinessException("红包名称参数为空");
        }

        if (null == redEnvelopeBean.getCompanyId()) {
            throw new BusinessException("企业id参数为空");
        }

        if (null == redEnvelopeBean.getRedMoney()) {
            throw new BusinessException("红包金额参数为空");
        }

        if (null == redEnvelopeBean.getRedScope()) {
            throw new BusinessException("红包使用范围参数为空");
        }

        if (null == redEnvelopeBean.getUseEndTime()) {
            throw new BusinessException("红包使用结束时间参数为空");
        }

        if (null == redEnvelopeBean.getUseStartTime()) {
            redEnvelopeBean.setUseStartTime(new Date());
        }

        try {

            redEnvelopeBean.setAddTime(new Date());

            redEnvelopeWriterMapper.insert(redEnvelopeBean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException("红包保存失败");
        }

    }

    /**
     * 注册发红包
     *
     * @param companyId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendRegisterEnvelope(Long companyId) throws BusinessException {

        Date currentDate = new Date();

        // 3个月有效期
        Date endDate = DateUtil.addDateOfMonth(currentDate, 3);

        RedEnvelopeBean red1 = new RedEnvelopeBean();
        red1.setCompanyId(companyId);
        red1.setName("66元红包");
        red1.setRedMoney(new BigDecimal("66.00"));
        red1.setRedScope(RedEvelopeScopeEnum.SOCIAL.getCode());
        red1.setSourceType(1);
        red1.setRedCount(1);
        red1.setActivityId(RedActivityConstant.AC_66);
        red1.setAddTime(currentDate);
        red1.setUseStartTime(currentDate);
        red1.setUseEndTime(endDate);
        red1.setDescription("仅限代缴社保使用|使用期限内，本红包可用于缴纳社保公积金，但要求缴纳社保公积金的人数超过15人|本红包不与其他红包或优惠活动叠加使用|最近解释权归薪太软所有");
        red1.setState(1);

        RedEnvelopeBean red2 = new RedEnvelopeBean();
        red2.setCompanyId(companyId);
        red2.setName("22元红包");
        red2.setRedMoney(new BigDecimal("22.00"));
        red2.setRedScope(RedEvelopeScopeEnum.SOCIAL.getCode());
        red2.setSourceType(1);
        red2.setRedCount(1);
        red2.setActivityId(RedActivityConstant.AC_22);
        red2.setAddTime(currentDate);
        red2.setUseStartTime(currentDate);
        red2.setUseEndTime(endDate);
        red2.setDescription("仅限代缴社保使用|使用期限内，本红包可用于缴纳社保公积金，但要求缴纳社保公积金的人数超过5人|本红包不与其他红包或优惠活动叠加使用|最近解释权归薪太软所有");
        red2.setState(1);

        try {
            this.sendRedEnvelope(red1);
            this.sendRedEnvelope(red2);
        } catch (BusinessException e) {
            throw e;
        }

    }

    /**
     * 分页查询红包记录
     *
     * @param redEnvelopeBean
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public ResultResponse getRedEnvelopePage(RedEnvelopeBean redEnvelopeBean, int pageIndex, int pageSize) {

        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(pageIndex, pageSize);

        if(null != redEnvelopeBean){
            PageList<RedEnvelopeBean> list = redEnvelopeReaderMapper.selectPageList(redEnvelopeBean, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = list.getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        }

        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 根据红包id查询红包记录
     *
     * @param redId
     * @return
     */
    @Override
    public RedEnvelopeBean getByRedId(Long redId) {
        if (null == redId) {
            return null;
        }
        return redEnvelopeReaderMapper.selectByPrimaryKey(redId);
    }

    /**
     * 查询企业可以使用的一个红包
     *
     * @param companyId 企业id
     * @param type      业务类型
     * @return
     */
    @Override
    public RedEnvelopeBean getByCompanyId(Long companyId, int type) {

        if(null != companyId){
            return redEnvelopeReaderMapper.selectByCompanyId(companyId, type);
        }

        return null;
    }

    /**
     * 检查红包状态
     * 不存在|已过期
     *
     * @param redId
     * @throws BusinessException
     */
    @Override
    public void checkRedEnvelopeState(Long companyId, Long redId) throws BusinessException {

        RedEnvelopeBean redEnvelopeBean = this.getByRedId(redId);

        if (null == redEnvelopeBean) {
            throw new BusinessException("不存在的红包");
        }

        if (redEnvelopeBean.getState() == 2) {
            throw new BusinessException("红包已经被使用");
        }

        if (redEnvelopeBean.getUseEndTime().compareTo(new Date()) == -1) {
            throw new BusinessException("红包已经过期");
        }

    }

    /**
     * 更新红包使用状态
     *
     * @param redId
     * @param state
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateState(Long redId, Integer state) throws BusinessException {

        if (null == redId) {
            throw new BusinessException("红包id参数为空");
        }

        if (null == state) {
            throw new BusinessException("红包状态参数为空");
        }

        RedEnvelopeBean redEnvelopeBean = new RedEnvelopeBean();
        redEnvelopeBean.setRedId(redId);
        redEnvelopeBean.setState(state);

        try {
            redEnvelopeWriterMapper.updateByPrimaryKeySelective(redEnvelopeBean);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException("红包状态更新失败");
        }

    }

    /**
     * 查询企业红包余额
     *
     * @param companyId
     * @param scope
     * @return
     */
    @Override
    public BigDecimal getAmout(Long companyId, Integer scope) {
        if (null == companyId) {
            return new BigDecimal("0.00");
        }
        BigDecimal amout = redEnvelopeReaderMapper.selectAmout(companyId, scope);
        if (null == amout) {
            return new BigDecimal("0.00");
        }
        return amout;
    }

}
