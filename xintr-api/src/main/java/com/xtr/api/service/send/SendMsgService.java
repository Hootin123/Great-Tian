package com.xtr.api.service.send;

import com.xtr.api.basic.SendMsgResponse;

import java.io.IOException;

/**
 * <p>短信发送接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/11 9:18
 */
public interface SendMsgService {

    /**
     * 短信发送
     *
     * @param mobile
     * @param content
     * @return
     */
    SendMsgResponse sendMsg(String mobile, String content) throws IOException;
}
