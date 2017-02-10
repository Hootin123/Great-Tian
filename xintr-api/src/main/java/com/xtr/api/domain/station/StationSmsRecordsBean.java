package com.xtr.api.domain.station;

import java.io.Serializable;
import java.util.Date;

public class StationSmsRecordsBean implements Serializable {
    /**
     *  id,所属表字段为station_sms_records.record_id
     */
    private Long recordId;

    /**
     *  内容,所属表字段为station_sms_records.record_cont
     */
    private String recordCont;

    /**
     *  手机号码,所属表字段为station_sms_records.record_phone
     */
    private String recordPhone;

    /**
     *  1发给用户  2发给企业管理员,所属表字段为station_sms_records.record_type
     */
    private Integer recordType;

    /**
     *  record_type 为1时是用户id 为2时是企业管理员id,所属表字段为station_sms_records.record_user_id
     */
    private Long recordUserId;

    /**
     *  短信模板id,所属表字段为station_sms_records.record_mb_id
     */
    private Integer recordMbId;

    /**
     *  创建时间,所属表字段为station_sms_records.record_addtime
     */
    private Date recordAddtime;

    /**
     * 获取 id 字段:station_sms_records.record_id
     *
     * @return station_sms_records.record_id, id
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * 设置 id 字段:station_sms_records.record_id
     *
     * @param recordId station_sms_records.record_id, id
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取 内容 字段:station_sms_records.record_cont
     *
     * @return station_sms_records.record_cont, 内容
     */
    public String getRecordCont() {
        return recordCont;
    }

    /**
     * 设置 内容 字段:station_sms_records.record_cont
     *
     * @param recordCont station_sms_records.record_cont, 内容
     */
    public void setRecordCont(String recordCont) {
        this.recordCont = recordCont == null ? null : recordCont.trim();
    }

    /**
     * 获取 手机号码 字段:station_sms_records.record_phone
     *
     * @return station_sms_records.record_phone, 手机号码
     */
    public String getRecordPhone() {
        return recordPhone;
    }

    /**
     * 设置 手机号码 字段:station_sms_records.record_phone
     *
     * @param recordPhone station_sms_records.record_phone, 手机号码
     */
    public void setRecordPhone(String recordPhone) {
        this.recordPhone = recordPhone == null ? null : recordPhone.trim();
    }

    /**
     * 获取 1发给用户  2发给企业管理员 字段:station_sms_records.record_type
     *
     * @return station_sms_records.record_type, 1发给用户  2发给企业管理员
     */
    public Integer getRecordType() {
        return recordType;
    }

    /**
     * 设置 1发给用户  2发给企业管理员 字段:station_sms_records.record_type
     *
     * @param recordType station_sms_records.record_type, 1发给用户  2发给企业管理员
     */
    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    /**
     * 获取 record_type 为1时是用户id 为2时是企业管理员id 字段:station_sms_records.record_user_id
     *
     * @return station_sms_records.record_user_id, record_type 为1时是用户id 为2时是企业管理员id
     */
    public Long getRecordUserId() {
        return recordUserId;
    }

    /**
     * 设置 record_type 为1时是用户id 为2时是企业管理员id 字段:station_sms_records.record_user_id
     *
     * @param recordUserId station_sms_records.record_user_id, record_type 为1时是用户id 为2时是企业管理员id
     */
    public void setRecordUserId(Long recordUserId) {
        this.recordUserId = recordUserId;
    }

    /**
     * 获取 短信模板id 字段:station_sms_records.record_mb_id
     *
     * @return station_sms_records.record_mb_id, 短信模板id
     */
    public Integer getRecordMbId() {
        return recordMbId;
    }

    /**
     * 设置 短信模板id 字段:station_sms_records.record_mb_id
     *
     * @param recordMbId station_sms_records.record_mb_id, 短信模板id
     */
    public void setRecordMbId(Integer recordMbId) {
        this.recordMbId = recordMbId;
    }

    /**
     * 获取 创建时间 字段:station_sms_records.record_addtime
     *
     * @return station_sms_records.record_addtime, 创建时间
     */
    public Date getRecordAddtime() {
        return recordAddtime;
    }

    /**
     * 设置 创建时间 字段:station_sms_records.record_addtime
     *
     * @param recordAddtime station_sms_records.record_addtime, 创建时间
     */
    public void setRecordAddtime(Date recordAddtime) {
        this.recordAddtime = recordAddtime;
    }
}