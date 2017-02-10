package com.xtr.api.dto.gateway.base;

import java.lang.reflect.Method;

/**
 * 缓存getter方法 对象
 */
public class CachedMethod {

	public CachedMethod(String reqName, Method getterMethod, Method setterMethod) {
		this.getterMethod = getterMethod;
		this.reqName = reqName;
		this.setterMethod = setterMethod;
	}

	private String reqName;
	private Method getterMethod;
	private Method setterMethod;

	public String getReqName() {
		return reqName;
	}

	public void setReqName(String reqName) {
		this.reqName = reqName;
	}

	public Method getGetterMethod() {
		return getterMethod;
	}

	public void setGetterMethod(Method getterMethod) {
		this.getterMethod = getterMethod;
	}

	public Method getSetterMethod() {
		return setterMethod;
	}

	public void setSetterMethod(Method setterMethod) {
		this.setterMethod = setterMethod;
	}

}
