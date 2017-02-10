package com.xtr.comm.sbt;

/**
 * <p>社保通请求参数封装</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 9:18.
 */
public class SbtRequest {

    private API_ACTION action;

    private String[] parameters;

    public SbtRequest() {

    }

    public SbtRequest(API_ACTION action, String... parameters) {
        this.action = action;
        this.parameters = parameters;
    }

    public API_ACTION getAction() {
        return action;
    }

    public void setAction(API_ACTION action) {
        this.action = action;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String... parameters) {
        this.parameters = parameters;
    }
}
