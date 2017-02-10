package com.xtr.api.service.app;

import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.app.AppQuestionFeedbackBean;
import com.xtr.comm.basic.BusinessException;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/15 11:50
 */
public interface AppQuestionFeedbackService {

    /**
     * 新增APP问题反馈
     * @param appQuestionFeedbackBean
     * @return
     * @throws BusinessException
     */
    int insert(AppQuestionFeedbackBean appQuestionFeedbackBean)throws BusinessException;
}
