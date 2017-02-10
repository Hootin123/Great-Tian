package com.xtr.core.service.app;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.app.AppQuestionFeedbackBean;
import com.xtr.api.service.app.AppQuestionFeedbackService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.app.AppQuestionFeedbackReaderMapper;
import com.xtr.core.persistence.writer.app.AppQuestionFeedbackWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/15 11:49
 */
@Service("appQuestionFeedbackService")
public class AppQuestionFeedbackServiceImpl implements AppQuestionFeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppQuestionFeedbackServiceImpl.class);

    @Resource
    private AppQuestionFeedbackWriterMapper appQuestionFeedbackWriterMapper;

    @Resource
    private AppQuestionFeedbackReaderMapper appQuestionFeedbackReaderMapper;

    /**
     * 新增APP问题反馈
     * @param appQuestionFeedbackBean
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(AppQuestionFeedbackBean appQuestionFeedbackBean)throws BusinessException{
        LOGGER.info("APP新增问题反馈,传递的参数:"+ JSON.toJSONString(appQuestionFeedbackBean));
        return appQuestionFeedbackWriterMapper.insert(appQuestionFeedbackBean);

    }
}
