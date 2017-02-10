package com.xtr.comm.sbt.api;

import java.io.Serializable;

/**
 * <p>人员材料附件</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 17:23.
 */
public class Attachment implements Serializable {

    private String type = "idcarda";
    private String data;
    private String suf = "jpg";

    public Attachment(String data) {
        this.data = data;
    }

    public Attachment(String data, String suf) {
        this.data = data;
        this.suf = suf;
    }

    public Attachment(String type, String data, String suf) {
        this.type = type;
        this.data = data;
        this.suf = suf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSuf() {
        return suf;
    }

    public void setSuf(String suf) {
        this.suf = suf;
    }
}
