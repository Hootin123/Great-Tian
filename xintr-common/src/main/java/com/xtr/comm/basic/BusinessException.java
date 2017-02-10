
package com.xtr.comm.basic;

/**
 * <p>业务异常类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:07
 */
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = -2737105097339793457L;
	
	private String code;

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String code, Throwable cause) {
		this(code,"",cause);
	}
	
	public BusinessException(String code, String  message) {
		super(message);
		this.code = code;
	}
	
	public BusinessException(String code, String  message,Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public BusinessException(String message) {
		super(message);
	}
	
	public String getCode() {
		return code;
	}


}
