package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CompanysBean extends BaseObject implements Serializable {
    /**
     * id,所属表字段为companys.company_id
     */
    private Long companyId;

    /**
     * 企业名称,所属表字段为companys.company_name
     */
    private String companyName;

    /**
     * 企业编号,所属表字段为companys.company_number
     */
    private String companyNumber;

    /**
     * 企业注册地址,所属表字段为companys.company_address
     */
    private String companyAddress;

    /**
     * 经营地址,所属表字段为companys.company_open_address
     */
    private String companyOpenAddress;

    /**
     * 企业法人,所属表字段为companys.company_corporation
     */
    private String companyCorporation;

    /**
     * 法人电话,所属表字段为companys.company_corporation_phone
     */
    private String companyCorporationPhone;

    /**
     * 企业法人身份证,所属表字段为companys.company_corporation_idcard
     */
    private String companyCorporationIdcard;

    /**
     * 身份证正面,所属表字段为companys.company_corporation_idcard_img_front
     */
    private String companyCorporationIdcardImgFront;

    /**
     * 身份证背面,所属表字段为companys.company_corporation_idcard_img_back
     */
    private String companyCorporationIdcardImgBack;

    /**
     * 组织机构代码,所属表字段为companys.company_organization_code
     */
    private String companyOrganizationCode;

    /**
     * 组织机构扫面件、复印件,所属表字段为companys.company_organization_img
     */
    private String companyOrganizationImg;

    /**
     * logo,所属表字段为companys.company_logo
     */
    private String companyLogo;

    /**
     * 0不可用 1可用,所属表字段为companys.company_sign
     */
    private Integer companySign;

    /**
     * 0不可申请垫付融资 1可申请,所属表字段为companys.company_canapply
     */
    private Integer companyCanapply;

    /**
     * 记录添加时间,所属表字段为companys.company_addime
     */
    private Date companyAddime;

    /**
     * 记录修改时间,所属表字段为companys.company_editime
     */
    private Date companyEditime;

    /**
     * 修改者id,所属表字段为companys.company_edit_member
     */
    private Long companyEditMember;

    /**
     * 0待审核 1审核未通过(初审) 2审核通过(初审) 3.审核未通过(复审) 4.审核通过(复审),所属表字段为companys.company_audit_status
     */
    private Integer companyAuditStatus;

    /**
     * 审核时间(初审),所属表字段为companys.company_audit_time
     */
    private Date companyAuditTime;

    /**
     * 审核人备注,所属表字段为companys.company_audit_remark
     */
    private String companyAuditRemark;

    /**
     * 审核人id(初审),所属表字段为companys.company_audit_member
     */
    private Long companyAuditMember;

    /**
     * 复审时间,所属表字段为companys.company_againAudit_time
     */
    private Date companyAgainauditTime;

    /**
     * 复审人备注,所属表字段为companys.company_againAudit_remark
     */
    private String companyAgainauditRemark;

    /**
     * 复审人,所属表字段为companys.company_againAudit_member
     */
    private Long companyAgainauditMember;

    /**
     * 信用等级 0无信用 1最高信用 后面依次递减,所属表字段为companys.company_credit
     */
    private Integer companyCredit;

    /**
     * 可申请垫发总工资百分比,扩大100倍存取,所属表字段为companys.company_borrow_percent
     */
    private BigDecimal companyBorrowPercent;

    /**
     * 联系人,所属表字段为companys.company_contact_name
     */
    private String companyContactName;

    /**
     * 联系电话,所属表字段为companys.company_contact_tel
     */
    private String companyContactTel;

    /**
     * 联系人职务,所属表字段为companys.company_contact_place
     */
    private String companyContactPlace;

    /**
     * 基本账户银行,所属表字段为companys.company_bank
     */
    private String companyBank;

    /**
     * 基本账户,所属表字段为companys.company_banknumber
     */
    private String companyBanknumber;

    /**
     * 银行地址,所属表字段为companys.company_bankaddress
     */
    private String companyBankaddress;

    /**
     * 一般账户银行,所属表字段为companys.company_bank_normal
     */
    private String companyBankNormal;

    /**
     * 一般账户,所属表字段为companys.company_banknumber_normal
     */
    private String companyBanknumberNormal;

    /**
     * 借贷卡,所属表字段为companys.company_banknumber_borrow
     */
    private String companyBanknumberBorrow;

    /**
     * 借款利率,所属表字段为companys.company_borrow_rate
     */
    private BigDecimal companyBorrowRate;

    /**
     * 服务费百分比,所属表字段为companys.company_borrow_server_rate
     */
    private BigDecimal companyBorrowServerRate;

    /**
     * 保证金比例,所属表字段为companys.company_borrow_deposit
     */
    private BigDecimal companyBorrowDeposit;

    /**
     * 发薪日 0没设置(需提醒) >0每月发薪日,所属表字段为companys.company_salary_date
     */
    private Integer companySalaryDate;

    /**
     * 认证状态 0未认证 1已认证,所属表字段为companys.company_isauth
     */
    private Integer companyIsauth;

    /**
     * 工资发放方式 1集团统一发放 2子单位自理,所属表字段为companys.company_salary_paytype
     */
    private Integer companySalaryPaytype;

    /**
     * 成立时间,所属表字段为companys.company_founded_time
     */
    private Date companyFoundedTime;

    /**
     * 注册资本,所属表字段为companys.company_regist_money
     */
    private BigDecimal companyRegistMoney;

    /**
     * 实收资本,所属表字段为companys.company_actual_money
     */
    private BigDecimal companyActualMoney;

    /**
     * 营业期限,所属表字段为companys.company_open_endtime
     */
    private Date companyOpenEndtime;

    /**
     * 主营业务,所属表字段为companys.company_main_business
     */
    private String companyMainBusiness;

    /**
     * 所属行业,所属表字段为companys.company_belong_industry
     */
    private String companyBelongIndustry;

    /**
     * 经济性质 1国有独资 2国有控股 3集体 4民营 5股份合作 6外商独资 7中外合资 8中外合作 9其他,所属表字段为companys.company_assets_nature
     */
    private Integer companyAssetsNature;

    /**
     * 公司形式 1有限责任公司 2股份有限公司 3独资企业(含个体) 4合伙企业,所属表字段为companys.company_type
     */
    private Integer companyType;

    /**
     * 员工总数,所属表字段为companys.company_employ_count
     */
    private Integer companyEmployCount;

    /**
     * 年均月工资,所属表字段为companys.company_salary_divide
     */
    private BigDecimal companySalaryDivide;

    /**
     * 信用记录是否正常 0不正常 1正常,所属表字段为companys.company_credit_status
     */
    private Integer companyCreditStatus;

    /**
     * 融资参数利率,所属表字段为companys.company_financ_rate
     */
    private BigDecimal companyFinancRate;

    /**
     * 融资保证金比例,所属表字段为companys.company_financ_deposit
     */
    private BigDecimal companyFinancDeposit;

    /**
     * 融资服务费百分比,所属表字段为companys.company_financ_server_rate
     */
    private BigDecimal companyFinancServerRate;

    /**
     * 代发工资服务费率,所属表字段为companys.company_entrust_server_rate
     */
    private BigDecimal companyEntrustServerRate;

    /**
     * 保证金状态(0.未缴纳 1.已缴纳 2.已退),所属表字段为companys.company_deposit_status
     */
    private Integer companyDepositStatus;

    /**
     * 已交保证金 0未交,所属表字段为companys.company_deposit
     */
    private BigDecimal companyDeposit;

    /**
     * 融资额度，显示用,所属表字段为companys.company_quota
     */
    private BigDecimal companyQuota;

    /**
     * 实际可用融资额度,所属表字段为companys.company_actual_quota
     */
    private BigDecimal companyActualQuota;

    /**
     * 合作开始时间,所属表字段为companys.company_cooperate_starttime
     */
    private Date companyCooperateStarttime;

    /**
     * 合作结束时间,所属表字段为companys.company_cooperate_endtime
     */
    private Date companyCooperateEndtime;

    /**
     * 是否上传认证资质(0.否 1.是),所属表字段为companys.company_isupload
     */
    private Integer companyIsupload;
    /**
     * 提交支付宝账号的姓名
     */
    private String companyTruename;
    /***
     * 支付宝账号
     */
    private String companyAlipayNumber;
    /***
     * 是否已经收集过资料
     */
    private Integer companyIsCollectInfo;

    private String openEndtime;

    private String code;

    //充值通道:0没有 1连连 2京东 3易付宝 4网银
    private Integer companyRechargeChannel;
    //提现银行账户名
    private String companyDepositBankAccountName;
    //提现银行卡号
    private String companyDepositBankNo;
    //入职需知
    private String companyEnterRequire;
    //是否让员工查看入职需知 1是 2否
    private Integer companyIsViewRequire;
    /**
     * 认证金额
     */
    private BigDecimal companyCertificationAmount;

    /**
     * 认证金额错误次数
     */
    private Integer amountNumber;

    /**
     * 公司规模
     */
    private String companyScale;

    /**
     * 了解薪太软的渠道
     */
    private Integer companyChannel;

    /**
     * 开户银行
     */
    private String companyDepositBank;

    /**
     * 开户支行名称
     */
    private String companyDepositBankaddress;

    /**
     * 0待审核 1审核未通过 2审核通过
     */
    private Integer companyDatumStatus;

    /**
     *驳回原因
     */
    private String companyDatumDismissreason;

    /**
     * 垫发审核时间
     */
    private Date companyDatumTime;

    /**
     * 垫付审核人
     */
    private Long companyDatumOper;

    private Integer companyPayStatus;

    private String companyPayNumber;

    private String companyPayReason;

    /**
     * 认证联行行号
     */
    private String companyAssociatedCode;

    public String getCompanyTruename() {
        return companyTruename;
    }

    public void setCompanyTruename(String companyTruename) {
        this.companyTruename = companyTruename;
    }

    public String getCompanyAlipayNumber() {
        return companyAlipayNumber;
    }

    public void setCompanyAlipayNumber(String companyAlipayNumber) {
        this.companyAlipayNumber = companyAlipayNumber;
    }

    public Integer getCompanyIsCollectInfo() {
        return companyIsCollectInfo;
    }

    public void setCompanyIsCollectInfo(Integer companyIsCollectInfo) {
        this.companyIsCollectInfo = companyIsCollectInfo;
    }

    public String getOpenEndtime() {
        return openEndtime;
    }

    public void setOpenEndtime(String openEndtime) {
        this.openEndtime = openEndtime == null ? null : openEndtime.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取 id 字段:companys.company_id
     *
     * @return companys.company_id, id
     */
    public Long getCompanyId() {
        return companyId;
    }

    /**
     * 设置 id 字段:companys.company_id
     *
     * @param companyId companys.company_id, id
     */
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * 获取 企业名称 字段:companys.company_name
     *
     * @return companys.company_name, 企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置 企业名称 字段:companys.company_name
     *
     * @param companyName companys.company_name, 企业名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    /**
     * 获取 企业编号 字段:companys.company_number
     *
     * @return companys.company_number, 企业编号
     */
    public String getCompanyNumber() {
        return companyNumber;
    }

    /**
     * 设置 企业编号 字段:companys.company_number
     *
     * @param companyNumber companys.company_number, 企业编号
     */
    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber == null ? null : companyNumber.trim();
    }

    /**
     * 获取 企业注册地址 字段:companys.company_address
     *
     * @return companys.company_address, 企业注册地址
     */
    public String getCompanyAddress() {
        return companyAddress;
    }

    /**
     * 设置 企业注册地址 字段:companys.company_address
     *
     * @param companyAddress companys.company_address, 企业注册地址
     */
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    /**
     * 获取 经营地址 字段:companys.company_open_address
     *
     * @return companys.company_open_address, 经营地址
     */
    public String getCompanyOpenAddress() {
        return companyOpenAddress;
    }

    /**
     * 设置 经营地址 字段:companys.company_open_address
     *
     * @param companyOpenAddress companys.company_open_address, 经营地址
     */
    public void setCompanyOpenAddress(String companyOpenAddress) {
        this.companyOpenAddress = companyOpenAddress == null ? null : companyOpenAddress.trim();
    }

    /**
     * 获取 企业法人 字段:companys.company_corporation
     *
     * @return companys.company_corporation, 企业法人
     */
    public String getCompanyCorporation() {
        return companyCorporation;
    }

    /**
     * 设置 企业法人 字段:companys.company_corporation
     *
     * @param companyCorporation companys.company_corporation, 企业法人
     */
    public void setCompanyCorporation(String companyCorporation) {
        this.companyCorporation = companyCorporation == null ? null : companyCorporation.trim();
    }

    /**
     * 获取 法人电话 字段:companys.company_corporation_phone
     *
     * @return companys.company_corporation_phone, 法人电话
     */
    public String getCompanyCorporationPhone() {
        return companyCorporationPhone;
    }

    /**
     * 设置 法人电话 字段:companys.company_corporation_phone
     *
     * @param companyCorporationPhone companys.company_corporation_phone, 法人电话
     */
    public void setCompanyCorporationPhone(String companyCorporationPhone) {
        this.companyCorporationPhone = companyCorporationPhone == null ? null : companyCorporationPhone.trim();
    }

    /**
     * 获取 企业法人身份证 字段:companys.company_corporation_idcard
     *
     * @return companys.company_corporation_idcard, 企业法人身份证
     */
    public String getCompanyCorporationIdcard() {
        return companyCorporationIdcard;
    }

    /**
     * 设置 企业法人身份证 字段:companys.company_corporation_idcard
     *
     * @param companyCorporationIdcard companys.company_corporation_idcard, 企业法人身份证
     */
    public void setCompanyCorporationIdcard(String companyCorporationIdcard) {
        this.companyCorporationIdcard = companyCorporationIdcard == null ? null : companyCorporationIdcard.trim();
    }

    /**
     * 获取 身份证正面 字段:companys.company_corporation_idcard_img_front
     *
     * @return companys.company_corporation_idcard_img_front, 身份证正面
     */
    public String getCompanyCorporationIdcardImgFront() {
        return companyCorporationIdcardImgFront;
    }

    /**
     * 设置 身份证正面 字段:companys.company_corporation_idcard_img_front
     *
     * @param companyCorporationIdcardImgFront companys.company_corporation_idcard_img_front, 身份证正面
     */
    public void setCompanyCorporationIdcardImgFront(String companyCorporationIdcardImgFront) {
        this.companyCorporationIdcardImgFront = companyCorporationIdcardImgFront == null ? null : companyCorporationIdcardImgFront.trim();
    }

    /**
     * 获取 身份证背面 字段:companys.company_corporation_idcard_img_back
     *
     * @return companys.company_corporation_idcard_img_back, 身份证背面
     */
    public String getCompanyCorporationIdcardImgBack() {
        return companyCorporationIdcardImgBack;
    }

    /**
     * 设置 身份证背面 字段:companys.company_corporation_idcard_img_back
     *
     * @param companyCorporationIdcardImgBack companys.company_corporation_idcard_img_back, 身份证背面
     */
    public void setCompanyCorporationIdcardImgBack(String companyCorporationIdcardImgBack) {
        this.companyCorporationIdcardImgBack = companyCorporationIdcardImgBack == null ? null : companyCorporationIdcardImgBack.trim();
    }

    /**
     * 获取 组织机构代码 字段:companys.company_organization_code
     *
     * @return companys.company_organization_code, 组织机构代码
     */
    public String getCompanyOrganizationCode() {
        return companyOrganizationCode;
    }

    /**
     * 设置 组织机构代码 字段:companys.company_organization_code
     *
     * @param companyOrganizationCode companys.company_organization_code, 组织机构代码
     */
    public void setCompanyOrganizationCode(String companyOrganizationCode) {
        this.companyOrganizationCode = companyOrganizationCode == null ? null : companyOrganizationCode.trim();
    }

    /**
     * 获取 组织机构扫面件、复印件 字段:companys.company_organization_img
     *
     * @return companys.company_organization_img, 组织机构扫面件、复印件
     */
    public String getCompanyOrganizationImg() {
        return companyOrganizationImg;
    }

    /**
     * 设置 组织机构扫面件、复印件 字段:companys.company_organization_img
     *
     * @param companyOrganizationImg companys.company_organization_img, 组织机构扫面件、复印件
     */
    public void setCompanyOrganizationImg(String companyOrganizationImg) {
        this.companyOrganizationImg = companyOrganizationImg == null ? null : companyOrganizationImg.trim();
    }

    /**
     * 获取 logo 字段:companys.company_logo
     *
     * @return companys.company_logo, logo
     */
    public String getCompanyLogo() {
        return companyLogo;
    }

    /**
     * 设置 logo 字段:companys.company_logo
     *
     * @param companyLogo companys.company_logo, logo
     */
    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo == null ? null : companyLogo.trim();
    }

    /**
     * 获取 0不可用 1可用 字段:companys.company_sign
     *
     * @return companys.company_sign, 0不可用 1可用
     */
    public Integer getCompanySign() {
        return companySign;
    }

    /**
     * 设置 0不可用 1可用 字段:companys.company_sign
     *
     * @param companySign companys.company_sign, 0不可用 1可用
     */
    public void setCompanySign(Integer companySign) {
        this.companySign = companySign;
    }

    /**
     * 获取 0不可申请垫付融资 1可申请 字段:companys.company_canapply
     *
     * @return companys.company_canapply, 0不可申请垫付融资 1可申请
     */
    public Integer getCompanyCanapply() {
        return companyCanapply;
    }

    /**
     * 设置 0不可申请垫付融资 1可申请 字段:companys.company_canapply
     *
     * @param companyCanapply companys.company_canapply, 0不可申请垫付融资 1可申请
     */
    public void setCompanyCanapply(Integer companyCanapply) {
        this.companyCanapply = companyCanapply;
    }

    /**
     * 获取 记录添加时间 字段:companys.company_addime
     *
     * @return companys.company_addime, 记录添加时间
     */
    public Date getCompanyAddime() {
        return companyAddime;
    }

    /**
     * 设置 记录添加时间 字段:companys.company_addime
     *
     * @param companyAddime companys.company_addime, 记录添加时间
     */
    public void setCompanyAddime(Date companyAddime) {
        this.companyAddime = companyAddime;
    }

    /**
     * 获取 记录修改时间 字段:companys.company_editime
     *
     * @return companys.company_editime, 记录修改时间
     */
    public Date getCompanyEditime() {
        return companyEditime;
    }

    /**
     * 设置 记录修改时间 字段:companys.company_editime
     *
     * @param companyEditime companys.company_editime, 记录修改时间
     */
    public void setCompanyEditime(Date companyEditime) {
        this.companyEditime = companyEditime;
    }

    /**
     * 获取 修改者id 字段:companys.company_edit_member
     *
     * @return companys.company_edit_member, 修改者id
     */
    public Long getCompanyEditMember() {
        return companyEditMember;
    }

    /**
     * 设置 修改者id 字段:companys.company_edit_member
     *
     * @param companyEditMember companys.company_edit_member, 修改者id
     */
    public void setCompanyEditMember(Long companyEditMember) {
        this.companyEditMember = companyEditMember;
    }

    /**
     * 获取 0待审核 1审核未通过(初审) 2审核通过(初审) 3.审核未通过(复审) 4.审核通过(复审) 字段:companys.company_audit_status
     *
     * @return companys.company_audit_status, 0待审核 1审核未通过(初审) 2审核通过(初审) 3.审核未通过(复审) 4.审核通过(复审)
     */
    public Integer getCompanyAuditStatus() {
        return companyAuditStatus;
    }

    /**
     * 设置 0待审核 1审核未通过(初审) 2审核通过(初审) 3.审核未通过(复审) 4.审核通过(复审) 字段:companys.company_audit_status
     *
     * @param companyAuditStatus companys.company_audit_status, 0待审核 1审核未通过(初审) 2审核通过(初审) 3.审核未通过(复审) 4.审核通过(复审)
     */
    public void setCompanyAuditStatus(Integer companyAuditStatus) {
        this.companyAuditStatus = companyAuditStatus;
    }

    /**
     * 获取 审核时间(初审) 字段:companys.company_audit_time
     *
     * @return companys.company_audit_time, 审核时间(初审)
     */
    public Date getCompanyAuditTime() {
        return companyAuditTime;
    }

    /**
     * 设置 审核时间(初审) 字段:companys.company_audit_time
     *
     * @param companyAuditTime companys.company_audit_time, 审核时间(初审)
     */
    public void setCompanyAuditTime(Date companyAuditTime) {
        this.companyAuditTime = companyAuditTime;
    }

    /**
     * 获取 审核人备注 字段:companys.company_audit_remark
     *
     * @return companys.company_audit_remark, 审核人备注
     */
    public String getCompanyAuditRemark() {
        return companyAuditRemark;
    }

    /**
     * 设置 审核人备注 字段:companys.company_audit_remark
     *
     * @param companyAuditRemark companys.company_audit_remark, 审核人备注
     */
    public void setCompanyAuditRemark(String companyAuditRemark) {
        this.companyAuditRemark = companyAuditRemark == null ? null : companyAuditRemark.trim();
    }

    /**
     * 获取 审核人id(初审) 字段:companys.company_audit_member
     *
     * @return companys.company_audit_member, 审核人id(初审)
     */
    public Long getCompanyAuditMember() {
        return companyAuditMember;
    }

    /**
     * 设置 审核人id(初审) 字段:companys.company_audit_member
     *
     * @param companyAuditMember companys.company_audit_member, 审核人id(初审)
     */
    public void setCompanyAuditMember(Long companyAuditMember) {
        this.companyAuditMember = companyAuditMember;
    }

    /**
     * 获取 复审时间 字段:companys.company_againAudit_time
     *
     * @return companys.company_againAudit_time, 复审时间
     */
    public Date getCompanyAgainauditTime() {
        return companyAgainauditTime;
    }

    /**
     * 设置 复审时间 字段:companys.company_againAudit_time
     *
     * @param companyAgainauditTime companys.company_againAudit_time, 复审时间
     */
    public void setCompanyAgainauditTime(Date companyAgainauditTime) {
        this.companyAgainauditTime = companyAgainauditTime;
    }

    /**
     * 获取 复审人备注 字段:companys.company_againAudit_remark
     *
     * @return companys.company_againAudit_remark, 复审人备注
     */
    public String getCompanyAgainauditRemark() {
        return companyAgainauditRemark;
    }

    /**
     * 设置 复审人备注 字段:companys.company_againAudit_remark
     *
     * @param companyAgainauditRemark companys.company_againAudit_remark, 复审人备注
     */
    public void setCompanyAgainauditRemark(String companyAgainauditRemark) {
        this.companyAgainauditRemark = companyAgainauditRemark == null ? null : companyAgainauditRemark.trim();
    }

    /**
     * 获取 复审人 字段:companys.company_againAudit_member
     *
     * @return companys.company_againAudit_member, 复审人
     */
    public Long getCompanyAgainauditMember() {
        return companyAgainauditMember;
    }

    /**
     * 设置 复审人 字段:companys.company_againAudit_member
     *
     * @param companyAgainauditMember companys.company_againAudit_member, 复审人
     */
    public void setCompanyAgainauditMember(Long companyAgainauditMember) {
        this.companyAgainauditMember = companyAgainauditMember;
    }

    /**
     * 获取 信用等级 0无信用 1最高信用 后面依次递减 字段:companys.company_credit
     *
     * @return companys.company_credit, 信用等级 0无信用 1最高信用 后面依次递减
     */
    public Integer getCompanyCredit() {
        return companyCredit;
    }

    /**
     * 设置 信用等级 0无信用 1最高信用 后面依次递减 字段:companys.company_credit
     *
     * @param companyCredit companys.company_credit, 信用等级 0无信用 1最高信用 后面依次递减
     */
    public void setCompanyCredit(Integer companyCredit) {
        this.companyCredit = companyCredit;
    }

    /**
     * 获取 可申请垫发总工资百分比,扩大100倍存取 字段:companys.company_borrow_percent
     *
     * @return companys.company_borrow_percent, 可申请垫发总工资百分比,扩大100倍存取
     */
    public BigDecimal getCompanyBorrowPercent() {
        return companyBorrowPercent;
    }

    /**
     * 设置 可申请垫发总工资百分比,扩大100倍存取 字段:companys.company_borrow_percent
     *
     * @param companyBorrowPercent companys.company_borrow_percent, 可申请垫发总工资百分比,扩大100倍存取
     */
    public void setCompanyBorrowPercent(BigDecimal companyBorrowPercent) {
        this.companyBorrowPercent = companyBorrowPercent;
    }

    /**
     * 获取 联系人 字段:companys.company_contact_name
     *
     * @return companys.company_contact_name, 联系人
     */
    public String getCompanyContactName() {
        return companyContactName;
    }

    /**
     * 设置 联系人 字段:companys.company_contact_name
     *
     * @param companyContactName companys.company_contact_name, 联系人
     */
    public void setCompanyContactName(String companyContactName) {
        this.companyContactName = companyContactName == null ? null : companyContactName.trim();
    }

    /**
     * 获取 联系电话 字段:companys.company_contact_tel
     *
     * @return companys.company_contact_tel, 联系电话
     */
    public String getCompanyContactTel() {
        return companyContactTel;
    }

    /**
     * 设置 联系电话 字段:companys.company_contact_tel
     *
     * @param companyContactTel companys.company_contact_tel, 联系电话
     */
    public void setCompanyContactTel(String companyContactTel) {
        this.companyContactTel = companyContactTel == null ? null : companyContactTel.trim();
    }

    /**
     * 获取 联系人职务 字段:companys.company_contact_place
     *
     * @return companys.company_contact_place, 联系人职务
     */
    public String getCompanyContactPlace() {
        return companyContactPlace;
    }

    /**
     * 设置 联系人职务 字段:companys.company_contact_place
     *
     * @param companyContactPlace companys.company_contact_place, 联系人职务
     */
    public void setCompanyContactPlace(String companyContactPlace) {
        this.companyContactPlace = companyContactPlace == null ? null : companyContactPlace.trim();
    }

    /**
     * 获取 基本账户银行 字段:companys.company_bank
     *
     * @return companys.company_bank, 基本账户银行
     */
    public String getCompanyBank() {
        return companyBank;
    }

    /**
     * 设置 基本账户银行 字段:companys.company_bank
     *
     * @param companyBank companys.company_bank, 基本账户银行
     */
    public void setCompanyBank(String companyBank) {
        this.companyBank = companyBank == null ? null : companyBank.trim();
    }

    /**
     * 获取 基本账户 字段:companys.company_banknumber
     *
     * @return companys.company_banknumber, 基本账户
     */
    public String getCompanyBanknumber() {
        return companyBanknumber;
    }

    /**
     * 设置 基本账户 字段:companys.company_banknumber
     *
     * @param companyBanknumber companys.company_banknumber, 基本账户
     */
    public void setCompanyBanknumber(String companyBanknumber) {
        this.companyBanknumber = companyBanknumber == null ? null : companyBanknumber.trim();
    }

    /**
     * 获取 银行地址 字段:companys.company_bankaddress
     *
     * @return companys.company_bankaddress, 银行地址
     */
    public String getCompanyBankaddress() {
        return companyBankaddress;
    }

    /**
     * 设置 银行地址 字段:companys.company_bankaddress
     *
     * @param companyBankaddress companys.company_bankaddress, 银行地址
     */
    public void setCompanyBankaddress(String companyBankaddress) {
        this.companyBankaddress = companyBankaddress == null ? null : companyBankaddress.trim();
    }

    /**
     * 获取 一般账户银行 字段:companys.company_bank_normal
     *
     * @return companys.company_bank_normal, 一般账户银行
     */
    public String getCompanyBankNormal() {
        return companyBankNormal;
    }

    /**
     * 设置 一般账户银行 字段:companys.company_bank_normal
     *
     * @param companyBankNormal companys.company_bank_normal, 一般账户银行
     */
    public void setCompanyBankNormal(String companyBankNormal) {
        this.companyBankNormal = companyBankNormal == null ? null : companyBankNormal.trim();
    }

    /**
     * 获取 一般账户 字段:companys.company_banknumber_normal
     *
     * @return companys.company_banknumber_normal, 一般账户
     */
    public String getCompanyBanknumberNormal() {
        return companyBanknumberNormal;
    }

    /**
     * 设置 一般账户 字段:companys.company_banknumber_normal
     *
     * @param companyBanknumberNormal companys.company_banknumber_normal, 一般账户
     */
    public void setCompanyBanknumberNormal(String companyBanknumberNormal) {
        this.companyBanknumberNormal = companyBanknumberNormal == null ? null : companyBanknumberNormal.trim();
    }

    /**
     * 获取 借贷卡 字段:companys.company_banknumber_borrow
     *
     * @return companys.company_banknumber_borrow, 借贷卡
     */
    public String getCompanyBanknumberBorrow() {
        return companyBanknumberBorrow;
    }

    /**
     * 设置 借贷卡 字段:companys.company_banknumber_borrow
     *
     * @param companyBanknumberBorrow companys.company_banknumber_borrow, 借贷卡
     */
    public void setCompanyBanknumberBorrow(String companyBanknumberBorrow) {
        this.companyBanknumberBorrow = companyBanknumberBorrow == null ? null : companyBanknumberBorrow.trim();
    }

    /**
     * 获取 借款利率 字段:companys.company_borrow_rate
     *
     * @return companys.company_borrow_rate, 借款利率
     */
    public BigDecimal getCompanyBorrowRate() {
        return companyBorrowRate;
    }

    /**
     * 设置 借款利率 字段:companys.company_borrow_rate
     *
     * @param companyBorrowRate companys.company_borrow_rate, 借款利率
     */
    public void setCompanyBorrowRate(BigDecimal companyBorrowRate) {
        this.companyBorrowRate = companyBorrowRate;
    }

    /**
     * 获取 服务费百分比 字段:companys.company_borrow_server_rate
     *
     * @return companys.company_borrow_server_rate, 服务费百分比
     */
    public BigDecimal getCompanyBorrowServerRate() {
        return companyBorrowServerRate;
    }

    /**
     * 设置 服务费百分比 字段:companys.company_borrow_server_rate
     *
     * @param companyBorrowServerRate companys.company_borrow_server_rate, 服务费百分比
     */
    public void setCompanyBorrowServerRate(BigDecimal companyBorrowServerRate) {
        this.companyBorrowServerRate = companyBorrowServerRate;
    }

    /**
     * 获取 保证金比例 字段:companys.company_borrow_deposit
     *
     * @return companys.company_borrow_deposit, 保证金比例
     */
    public BigDecimal getCompanyBorrowDeposit() {
        return companyBorrowDeposit;
    }

    /**
     * 设置 保证金比例 字段:companys.company_borrow_deposit
     *
     * @param companyBorrowDeposit companys.company_borrow_deposit, 保证金比例
     */
    public void setCompanyBorrowDeposit(BigDecimal companyBorrowDeposit) {
        this.companyBorrowDeposit = companyBorrowDeposit;
    }

    /**
     * 获取 发薪日 0没设置(需提醒) >0每月发薪日 字段:companys.company_salary_date
     *
     * @return companys.company_salary_date, 发薪日 0没设置(需提醒) >0每月发薪日
     */
    public Integer getCompanySalaryDate() {
        return companySalaryDate;
    }

    /**
     * 设置 发薪日 0没设置(需提醒) >0每月发薪日 字段:companys.company_salary_date
     *
     * @param companySalaryDate companys.company_salary_date, 发薪日 0没设置(需提醒) >0每月发薪日
     */
    public void setCompanySalaryDate(Integer companySalaryDate) {
        this.companySalaryDate = companySalaryDate;
    }

    /**
     * 获取 认证状态 0未认证 1已认证 字段:companys.company_isauth
     *
     * @return companys.company_isauth, 认证状态 0未认证 1已认证
     */
    public Integer getCompanyIsauth() {
        return companyIsauth;
    }

    /**
     * 设置 认证状态 0未认证 1已认证 字段:companys.company_isauth
     *
     * @param companyIsauth companys.company_isauth, 认证状态 0未认证 1已认证
     */
    public void setCompanyIsauth(Integer companyIsauth) {
        this.companyIsauth = companyIsauth;
    }

    /**
     * 获取 工资发放方式 1集团统一发放 2子单位自理 字段:companys.company_salary_paytype
     *
     * @return companys.company_salary_paytype, 工资发放方式 1集团统一发放 2子单位自理
     */
    public Integer getCompanySalaryPaytype() {
        return companySalaryPaytype;
    }

    /**
     * 设置 工资发放方式 1集团统一发放 2子单位自理 字段:companys.company_salary_paytype
     *
     * @param companySalaryPaytype companys.company_salary_paytype, 工资发放方式 1集团统一发放 2子单位自理
     */
    public void setCompanySalaryPaytype(Integer companySalaryPaytype) {
        this.companySalaryPaytype = companySalaryPaytype;
    }

    /**
     * 获取 成立时间 字段:companys.company_founded_time
     *
     * @return companys.company_founded_time, 成立时间
     */
    public Date getCompanyFoundedTime() {
        return companyFoundedTime;
    }

    /**
     * 设置 成立时间 字段:companys.company_founded_time
     *
     * @param companyFoundedTime companys.company_founded_time, 成立时间
     */
    public void setCompanyFoundedTime(Date companyFoundedTime) {
        this.companyFoundedTime = companyFoundedTime;
    }

    /**
     * 获取 注册资本 字段:companys.company_regist_money
     *
     * @return companys.company_regist_money, 注册资本
     */
    public BigDecimal getCompanyRegistMoney() {
        return companyRegistMoney;
    }

    /**
     * 设置 注册资本 字段:companys.company_regist_money
     *
     * @param companyRegistMoney companys.company_regist_money, 注册资本
     */
    public void setCompanyRegistMoney(BigDecimal companyRegistMoney) {
        this.companyRegistMoney = companyRegistMoney;
    }

    /**
     * 获取 实收资本 字段:companys.company_actual_money
     *
     * @return companys.company_actual_money, 实收资本
     */
    public BigDecimal getCompanyActualMoney() {
        return companyActualMoney;
    }

    /**
     * 设置 实收资本 字段:companys.company_actual_money
     *
     * @param companyActualMoney companys.company_actual_money, 实收资本
     */
    public void setCompanyActualMoney(BigDecimal companyActualMoney) {
        this.companyActualMoney = companyActualMoney;
    }

    /**
     * 获取 营业期限 字段:companys.company_open_endtime
     *
     * @return companys.company_open_endtime, 营业期限
     */
    public Date getCompanyOpenEndtime() {
        return companyOpenEndtime;
    }

    /**
     * 设置 营业期限 字段:companys.company_open_endtime
     *
     * @param companyOpenEndtime companys.company_open_endtime, 营业期限
     */
    public void setCompanyOpenEndtime(Date companyOpenEndtime) {
        this.companyOpenEndtime = companyOpenEndtime;
    }

    /**
     * 获取 主营业务 字段:companys.company_main_business
     *
     * @return companys.company_main_business, 主营业务
     */
    public String getCompanyMainBusiness() {
        return companyMainBusiness;
    }

    /**
     * 设置 主营业务 字段:companys.company_main_business
     *
     * @param companyMainBusiness companys.company_main_business, 主营业务
     */
    public void setCompanyMainBusiness(String companyMainBusiness) {
        this.companyMainBusiness = companyMainBusiness == null ? null : companyMainBusiness.trim();
    }

    /**
     * 获取 所属行业 字段:companys.company_belong_industry
     *
     * @return companys.company_belong_industry, 所属行业
     */
    public String getCompanyBelongIndustry() {
        return companyBelongIndustry;
    }

    /**
     * 设置 所属行业 字段:companys.company_belong_industry
     *
     * @param companyBelongIndustry companys.company_belong_industry, 所属行业
     */
    public void setCompanyBelongIndustry(String companyBelongIndustry) {
        this.companyBelongIndustry = companyBelongIndustry == null ? null : companyBelongIndustry.trim();
    }

    /**
     * 获取 经济性质 1国有独资 2国有控股 3集体 4民营 5股份合作 6外商独资 7中外合资 8中外合作 9其他 字段:companys.company_assets_nature
     *
     * @return companys.company_assets_nature, 经济性质 1国有独资 2国有控股 3集体 4民营 5股份合作 6外商独资 7中外合资 8中外合作 9其他
     */
    public Integer getCompanyAssetsNature() {
        return companyAssetsNature;
    }

    /**
     * 设置 经济性质 1国有独资 2国有控股 3集体 4民营 5股份合作 6外商独资 7中外合资 8中外合作 9其他 字段:companys.company_assets_nature
     *
     * @param companyAssetsNature companys.company_assets_nature, 经济性质 1国有独资 2国有控股 3集体 4民营 5股份合作 6外商独资 7中外合资 8中外合作 9其他
     */
    public void setCompanyAssetsNature(Integer companyAssetsNature) {
        this.companyAssetsNature = companyAssetsNature;
    }

    /**
     * 获取 公司形式 1有限责任公司 2股份有限公司 3独资企业(含个体) 4合伙企业 字段:companys.company_type
     *
     * @return companys.company_type, 公司形式 1有限责任公司 2股份有限公司 3独资企业(含个体) 4合伙企业
     */
    public Integer getCompanyType() {
        return companyType;
    }

    /**
     * 设置 公司形式 1有限责任公司 2股份有限公司 3独资企业(含个体) 4合伙企业 字段:companys.company_type
     *
     * @param companyType companys.company_type, 公司形式 1有限责任公司 2股份有限公司 3独资企业(含个体) 4合伙企业
     */
    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    /**
     * 获取 员工总数 字段:companys.company_employ_count
     *
     * @return companys.company_employ_count, 员工总数
     */
    public Integer getCompanyEmployCount() {
        return companyEmployCount;
    }

    /**
     * 设置 员工总数 字段:companys.company_employ_count
     *
     * @param companyEmployCount companys.company_employ_count, 员工总数
     */
    public void setCompanyEmployCount(Integer companyEmployCount) {
        this.companyEmployCount = companyEmployCount;
    }

    /**
     * 获取 年均月工资 字段:companys.company_salary_divide
     *
     * @return companys.company_salary_divide, 年均月工资
     */
    public BigDecimal getCompanySalaryDivide() {
        return companySalaryDivide;
    }

    /**
     * 设置 年均月工资 字段:companys.company_salary_divide
     *
     * @param companySalaryDivide companys.company_salary_divide, 年均月工资
     */
    public void setCompanySalaryDivide(BigDecimal companySalaryDivide) {
        this.companySalaryDivide = companySalaryDivide;
    }

    /**
     * 获取 信用记录是否正常 0不正常 1正常 字段:companys.company_credit_status
     *
     * @return companys.company_credit_status, 信用记录是否正常 0不正常 1正常
     */
    public Integer getCompanyCreditStatus() {
        return companyCreditStatus;
    }

    /**
     * 设置 信用记录是否正常 0不正常 1正常 字段:companys.company_credit_status
     *
     * @param companyCreditStatus companys.company_credit_status, 信用记录是否正常 0不正常 1正常
     */
    public void setCompanyCreditStatus(Integer companyCreditStatus) {
        this.companyCreditStatus = companyCreditStatus;
    }

    /**
     * 获取 融资参数利率 字段:companys.company_financ_rate
     *
     * @return companys.company_financ_rate, 融资参数利率
     */
    public BigDecimal getCompanyFinancRate() {
        return companyFinancRate;
    }

    /**
     * 设置 融资参数利率 字段:companys.company_financ_rate
     *
     * @param companyFinancRate companys.company_financ_rate, 融资参数利率
     */
    public void setCompanyFinancRate(BigDecimal companyFinancRate) {
        this.companyFinancRate = companyFinancRate;
    }

    /**
     * 获取 融资保证金比例 字段:companys.company_financ_deposit
     *
     * @return companys.company_financ_deposit, 融资保证金比例
     */
    public BigDecimal getCompanyFinancDeposit() {
        return companyFinancDeposit;
    }

    /**
     * 设置 融资保证金比例 字段:companys.company_financ_deposit
     *
     * @param companyFinancDeposit companys.company_financ_deposit, 融资保证金比例
     */
    public void setCompanyFinancDeposit(BigDecimal companyFinancDeposit) {
        this.companyFinancDeposit = companyFinancDeposit;
    }

    /**
     * 获取 融资服务费百分比 字段:companys.company_financ_server_rate
     *
     * @return companys.company_financ_server_rate, 融资服务费百分比
     */
    public BigDecimal getCompanyFinancServerRate() {
        return companyFinancServerRate;
    }

    /**
     * 设置 融资服务费百分比 字段:companys.company_financ_server_rate
     *
     * @param companyFinancServerRate companys.company_financ_server_rate, 融资服务费百分比
     */
    public void setCompanyFinancServerRate(BigDecimal companyFinancServerRate) {
        this.companyFinancServerRate = companyFinancServerRate;
    }

    /**
     * 获取 代发工资服务费率 字段:companys.company_entrust_server_rate
     *
     * @return companys.company_entrust_server_rate, 代发工资服务费率
     */
    public BigDecimal getCompanyEntrustServerRate() {
        return companyEntrustServerRate;
    }

    /**
     * 设置 代发工资服务费率 字段:companys.company_entrust_server_rate
     *
     * @param companyEntrustServerRate companys.company_entrust_server_rate, 代发工资服务费率
     */
    public void setCompanyEntrustServerRate(BigDecimal companyEntrustServerRate) {
        this.companyEntrustServerRate = companyEntrustServerRate;
    }

    /**
     * 获取 保证金状态(0.未缴纳 1.已缴纳 2.已退) 字段:companys.company_deposit_status
     *
     * @return companys.company_deposit_status, 保证金状态(0.未缴纳 1.已缴纳 2.已退)
     */
    public Integer getCompanyDepositStatus() {
        return companyDepositStatus;
    }

    /**
     * 设置 保证金状态(0.未缴纳 1.已缴纳 2.已退) 字段:companys.company_deposit_status
     *
     * @param companyDepositStatus companys.company_deposit_status, 保证金状态(0.未缴纳 1.已缴纳 2.已退)
     */
    public void setCompanyDepositStatus(Integer companyDepositStatus) {
        this.companyDepositStatus = companyDepositStatus;
    }

    /**
     * 获取 已交保证金 0未交 字段:companys.company_deposit
     *
     * @return companys.company_deposit, 已交保证金 0未交
     */
    public BigDecimal getCompanyDeposit() {
        return companyDeposit;
    }

    /**
     * 设置 已交保证金 0未交 字段:companys.company_deposit
     *
     * @param companyDeposit companys.company_deposit, 已交保证金 0未交
     */
    public void setCompanyDeposit(BigDecimal companyDeposit) {
        this.companyDeposit = companyDeposit;
    }

    /**
     * 获取 融资额度，显示用 字段:companys.company_quota
     *
     * @return companys.company_quota, 融资额度，显示用
     */
    public BigDecimal getCompanyQuota() {
        return companyQuota;
    }

    /**
     * 设置 融资额度，显示用 字段:companys.company_quota
     *
     * @param companyQuota companys.company_quota, 融资额度，显示用
     */
    public void setCompanyQuota(BigDecimal companyQuota) {
        this.companyQuota = companyQuota;
    }

    /**
     * 获取 实际可用融资额度 字段:companys.company_actual_quota
     *
     * @return companys.company_actual_quota, 实际可用融资额度
     */
    public BigDecimal getCompanyActualQuota() {
        return companyActualQuota;
    }

    /**
     * 设置 实际可用融资额度 字段:companys.company_actual_quota
     *
     * @param companyActualQuota companys.company_actual_quota, 实际可用融资额度
     */
    public void setCompanyActualQuota(BigDecimal companyActualQuota) {
        this.companyActualQuota = companyActualQuota;
    }

    /**
     * 获取 合作开始时间 字段:companys.company_cooperate_starttime
     *
     * @return companys.company_cooperate_starttime, 合作开始时间
     */
    public Date getCompanyCooperateStarttime() {
        return companyCooperateStarttime;
    }

    /**
     * 设置 合作开始时间 字段:companys.company_cooperate_starttime
     *
     * @param companyCooperateStarttime companys.company_cooperate_starttime, 合作开始时间
     */
    public void setCompanyCooperateStarttime(Date companyCooperateStarttime) {
        this.companyCooperateStarttime = companyCooperateStarttime;
    }

    /**
     * 获取 合作结束时间 字段:companys.company_cooperate_endtime
     *
     * @return companys.company_cooperate_endtime, 合作结束时间
     */
    public Date getCompanyCooperateEndtime() {
        return companyCooperateEndtime;
    }

    /**
     * 设置 合作结束时间 字段:companys.company_cooperate_endtime
     *
     * @param companyCooperateEndtime companys.company_cooperate_endtime, 合作结束时间
     */
    public void setCompanyCooperateEndtime(Date companyCooperateEndtime) {
        this.companyCooperateEndtime = companyCooperateEndtime;
    }

    /**
     * 获取 是否上传认证资质(0.否 1.是) 字段:companys.company_isupload
     *
     * @return companys.company_isupload, 是否上传认证资质(0.否 1.是)
     */
    public Integer getCompanyIsupload() {
        return companyIsupload;
    }

    /**
     * 设置 是否上传认证资质(0.否 1.是) 字段:companys.company_isupload
     *
     * @param companyIsupload companys.company_isupload, 是否上传认证资质(0.否 1.是)
     */



    public void setCompanyIsupload(Integer companyIsupload) {
        this.companyIsupload = companyIsupload;
    }

    public Integer getCompanyRechargeChannel() {
        return companyRechargeChannel;
    }

    public void setCompanyRechargeChannel(Integer companyRechargeChannel) {
        this.companyRechargeChannel = companyRechargeChannel;
    }

    public String getCompanyDepositBankAccountName() {
        return companyDepositBankAccountName;
    }

    public void setCompanyDepositBankAccountName(String companyDepositBankAccountName) {
        this.companyDepositBankAccountName = companyDepositBankAccountName;
    }

    public String getCompanyDepositBankNo() {
        return companyDepositBankNo;
    }

    public void setCompanyDepositBankNo(String companyDepositBankNo) {
        this.companyDepositBankNo = companyDepositBankNo;
    }

    public BigDecimal getCompanyCertificationAmount() {
        return companyCertificationAmount;
    }

    public void setCompanyCertificationAmount(BigDecimal companyCertificationAmount) {
        this.companyCertificationAmount = companyCertificationAmount;
    }

    public Integer getAmountNumber() {
        return amountNumber;
    }

    public void setAmountNumber(Integer amountNumber) {
        this.amountNumber = amountNumber;
    }

    public String getCompanyScale() {
        return companyScale;
    }

    public void setCompanyScale(String companyScale) {
        this.companyScale = companyScale;
    }

    public Integer getCompanyChannel() {
        return companyChannel;
    }

    public void setCompanyChannel(Integer companyChannel) {
        this.companyChannel = companyChannel;
    }

    public String getCompanyDepositBank() {
        return companyDepositBank;
    }

    public void setCompanyDepositBank(String companyDepositBank) {
        this.companyDepositBank = companyDepositBank;
    }

    public String getCompanyDepositBankaddress() {
        return companyDepositBankaddress;
    }

    public void setCompanyDepositBankaddress(String companyDepositBankaddress) {
        this.companyDepositBankaddress = companyDepositBankaddress;
    }

    public Integer getCompanyDatumStatus() {
        return companyDatumStatus;
    }

    public void setCompanyDatumStatus(Integer companyDatumStatus) {
        this.companyDatumStatus = companyDatumStatus;
    }

    public String getCompanyDatumDismissreason() {
        return companyDatumDismissreason;
    }

    public void setCompanyDatumDismissreason(String companyDatumDismissreason) {
        this.companyDatumDismissreason = companyDatumDismissreason;
    }

    public Date getCompanyDatumTime() {
        return companyDatumTime;
    }

    public void setCompanyDatumTime(Date companyDatumTime) {
        this.companyDatumTime = companyDatumTime;
    }

    public Long getCompanyDatumOper() {
        return companyDatumOper;
    }

    public void setCompanyDatumOper(Long companyDatumOper) {
        this.companyDatumOper = companyDatumOper;
    }

    public String getCompanyEnterRequire() {
        return companyEnterRequire;
    }

    public void setCompanyEnterRequire(String companyEnterRequire) {
        this.companyEnterRequire = companyEnterRequire;
    }

    public Integer getCompanyIsViewRequire() {
        return companyIsViewRequire;
    }

    public void setCompanyIsViewRequire(Integer companyIsViewRequire) {
        this.companyIsViewRequire = companyIsViewRequire;
    }

    public Integer getCompanyPayStatus() {
        return companyPayStatus;
    }

    public void setCompanyPayStatus(Integer companyPayStatus) {
        this.companyPayStatus = companyPayStatus;
    }

    public String getCompanyPayNumber() {
        return companyPayNumber;
    }

    public void setCompanyPayNumber(String companyPayNumber) {
        this.companyPayNumber = companyPayNumber;
    }

    public String getCompanyPayReason() {
        return companyPayReason;
    }

    public void setCompanyPayReason(String companyPayReason) {
        this.companyPayReason = companyPayReason;
    }

    public String getCompanyAssociatedCode() {
        return companyAssociatedCode;
    }

    public void setCompanyAssociatedCode(String companyAssociatedCode) {
        this.companyAssociatedCode = companyAssociatedCode;
    }
}