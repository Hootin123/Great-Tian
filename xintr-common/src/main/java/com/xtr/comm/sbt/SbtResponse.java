package com.xtr.comm.sbt;

/**
 * <p>社保通响应封装</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 9:07.
 */
public class SbtResponse {

    private String data;

    public SbtResponse(String data){
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SbtResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}
