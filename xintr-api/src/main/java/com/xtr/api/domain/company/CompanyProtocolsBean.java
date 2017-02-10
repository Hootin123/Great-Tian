package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/11 16:58
 */
public class CompanyProtocolsBean extends BaseObject implements Serializable {
    //ID
    private Long protocolId;
    //企业ID
    private Long protocolCompanyId;
    //签约类型：1代发协议 2垫发协议
    private Integer protocolContractType;
    //纸质文件编号
    private String protocolPaperNo;
    //生效状态 1有效 2失效
    private Integer protocolStatus;
    //签约时间
    private Date protocolContractTime;
    //到期时间
    private Date protocolExpireTime;
    //创建时间
    private Date protocolCreateTime;
    //业务联系人
    private String protocolLinkmanName;
    //更新时间
    private Date protocolUpdateTime;
    //协议状态:1待审批 2签约 3即将到期 4合约到期 5冻结
    private Integer protocolCurrentStatus;
    //业务经理
    private String protocolBusinessManager;
    //业务联系人手机
    private String protocolLinkmanPhone;
    //协议编号
    private String protocolCode;
    //垫付利率
    private BigDecimal protocolRate;
    //服务费利率
    private BigDecimal protocolServeRate;
    //垫付比例
    private BigDecimal protocolScale;

    public BigDecimal getProtocolRate() {
        return protocolRate;
    }

    public void setProtocolRate(BigDecimal protocolRate) {
        this.protocolRate = protocolRate;
    }

    public BigDecimal getProtocolServeRate() {
        return protocolServeRate;
    }

    public void setProtocolServeRate(BigDecimal protocolServeRate) {
        this.protocolServeRate = protocolServeRate;
    }

    public BigDecimal getProtocolScale() {
        return protocolScale;
    }

    public void setProtocolScale(BigDecimal protocolScale) {
        this.protocolScale = protocolScale;
    }

    public String getProtocolCode() {
        return protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    public String getProtocolLinkmanPhone() {
        return protocolLinkmanPhone;
    }

    public void setProtocolLinkmanPhone(String protocolLinkmanPhone) {
        this.protocolLinkmanPhone = protocolLinkmanPhone;
    }

    public String getProtocolLinkmanName() {
        return protocolLinkmanName;
    }

    public void setProtocolLinkmanName(String protocolLinkmanName) {
        this.protocolLinkmanName = protocolLinkmanName;
    }



    public Date getProtocolContractTime() {
        return protocolContractTime;
    }

    public void setProtocolContractTime(Date protocolContractTime) {
        this.protocolContractTime = protocolContractTime;
    }

    public Date getProtocolExpireTime() {
        return protocolExpireTime;
    }

    public void setProtocolExpireTime(Date protocolExpireTime) {
        this.protocolExpireTime = protocolExpireTime;
    }

    public Date getProtocolCreateTime() {
        return protocolCreateTime;
    }

    public void setProtocolCreateTime(Date protocolCreateTime) {
        this.protocolCreateTime = protocolCreateTime;
    }

    public Date getProtocolUpdateTime() {
        return protocolUpdateTime;
    }

    public void setProtocolUpdateTime(Date protocolUpdateTime) {
        this.protocolUpdateTime = protocolUpdateTime;
    }

    public Integer getProtocolCurrentStatus() {
        return protocolCurrentStatus;
    }

    public void setProtocolCurrentStatus(Integer protocolCurrentStatus) {
        this.protocolCurrentStatus = protocolCurrentStatus;
    }

    public String getProtocolBusinessManager() {
        return protocolBusinessManager;
    }

    public void setProtocolBusinessManager(String protocolBusinessManager) {
        this.protocolBusinessManager = protocolBusinessManager;
    }




    public Integer getProtocolStatus() {
        return protocolStatus;
    }
    public void setProtocolStatus(Integer protocolStatus) {
        this.protocolStatus = protocolStatus;
    }
    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Long getProtocolCompanyId() {
        return protocolCompanyId;
    }

    public void setProtocolCompanyId(Long protocolCompanyId) {
        this.protocolCompanyId = protocolCompanyId;
    }

    public Integer getProtocolContractType() {
        return protocolContractType;
    }

    public void setProtocolContractType(Integer protocolContractType) {
        this.protocolContractType = protocolContractType;
    }

    public String getProtocolPaperNo() {
        return protocolPaperNo;
    }

    public void setProtocolPaperNo(String protocolPaperNo) {
        this.protocolPaperNo = protocolPaperNo;
    }
}
