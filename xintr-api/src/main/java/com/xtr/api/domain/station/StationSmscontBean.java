package com.xtr.api.domain.station;

import java.io.Serializable;

public class StationSmscontBean implements Serializable{
    /**
     *  ,所属表字段为station_smscont.sms_id
     */
    private Integer smsId;

    /**
     *  ,所属表字段为station_smscont.sms_type
     */
    private Integer smsType;

    /**
     *  ,所属表字段为station_smscont.sms_cont
     */
    private String smsCont;

    /**
     * 获取  字段:station_smscont.sms_id
     *
     * @return station_smscont.sms_id,
     */
    public Integer getSmsId() {
        return smsId;
    }

    /**
     * 设置  字段:station_smscont.sms_id
     *
     * @param smsId station_smscont.sms_id, 
     */
    public void setSmsId(Integer smsId) {
        this.smsId = smsId;
    }

    /**
     * 获取  字段:station_smscont.sms_type
     *
     * @return station_smscont.sms_type,
     */
    public Integer getSmsType() {
        return smsType;
    }

    /**
     * 设置  字段:station_smscont.sms_type
     *
     * @param smsType station_smscont.sms_type, 
     */
    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    /**
     * 获取  字段:station_smscont.sms_cont
     *
     * @return station_smscont.sms_cont,
     */
    public String getSmsCont() {
        return smsCont;
    }

    /**
     * 设置  字段:station_smscont.sms_cont
     *
     * @param smsCont station_smscont.sms_cont, 
     */
    public void setSmsCont(String smsCont) {
        this.smsCont = smsCont == null ? null : smsCont.trim();
    }
}