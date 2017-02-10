package com.xtr.comm.sbt.api;

import java.io.Serializable;

/**
 * <p>社保城市对象</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 14:06.
 */
public class City implements Serializable {

    private long id;
    private String city;
    private String cname;
    private String pname;
    private String prov;
    private String region;
    private int rule;
    private int subdl;
    private String lastDate;
    private String month;
    private long timestamp;

    public City() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public int getSubdl() {
        return subdl;
    }

    public void setSubdl(int subdl) {
        this.subdl = subdl;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
