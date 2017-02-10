package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class FileResourcesBean extends BaseObject implements Serializable {
    /**
     * id,所属表字段为file_resources.id
     */
    private Long id;

    /**
     * file_type,所属表字段为file_resources.file_type
     */
    private Integer fileType;

    /**
     * file_url,所属表字段为file_resources.file_url
     */
    private String fileUrl;

    /**
     * id,所属表字段为file_resources.company_id
     */
    private Long companyId;

    /**
     * 成员id,所属表字段为file_resources.member_id
     */
    private Long memberId;

    /**
     * 记录添加时间,所属表字段为file_resources.file_addime
     */
    private Date fileAddime;

    /**
     * 获取 id 字段:file_resources.id
     *
     * @return file_resources.id, id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id 字段:file_resources.id
     *
     * @param id file_resources.id, id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 fileType 字段:file_resources.file_type
     *
     * @return file_resources.file_type, fileType
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * 设置 fileType 字段:file_resources.file_type
     *
     * @param fileType file_resources.file_type, fileType
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * 获取 fileUrl 字段:file_resources.file_url
     *
     * @return file_resources.file_url, fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置 fileUrl 字段:file_resources.file_url
     *
     * @param fileUrl file_resources.file_url, fileUrl
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取 companyId 字段:file_resources.company_id
     *
     * @return file_resources.company_id, companyId
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 companyId 字段:file_resources.company_id
     *
     * @param companyId file_resources.company_id, companyId
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 memberId 字段:file_resources.member_id
     *
     * @return file_resources.member_id, memberId
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置 memberId 字段:file_resources.member_id
     *
     * @param memberId file_resources.member_id, memberId
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取 fileAddime 字段:file_resources.file_addime
     *
     * @return file_resources.file_addime, fileAddime
     */
    public Date getFileAddime() {
        return fileAddime;
    }

    /**
     * 设置 fileAddime 字段:file_resources.file_addime
     *
     * @param fileAddime file_resources.file_addime, fileAddime
     */
    public void setFileAddime(Date fileAddime) {
        this.fileAddime = fileAddime;
    }
}