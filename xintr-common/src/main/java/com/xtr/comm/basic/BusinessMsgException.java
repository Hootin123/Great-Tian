package com.xtr.comm.basic;

/**
 * <p>可以输出到前台的错误</p>
 *
 * @author 任齐
 * @createTime: 2016/8/19 15:13.
 */
public class BusinessMsgException extends BusinessException {

    public BusinessMsgException(String message) {
        super(message);
    }

    public BusinessMsgException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
