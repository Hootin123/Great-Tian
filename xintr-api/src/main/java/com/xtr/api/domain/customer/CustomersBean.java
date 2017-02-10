package com.xtr.api.domain.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomersBean extends BaseObject implements Serializable {
    /**
     * id,所属表字段为customers.customer_id
     */
    private Long customerId;

    /**
     * 员工企业id,所属表字段为customers.customer_company_id
     */
    private Long customerCompanyId;

    /**
     * 企业名称,所属表字段为customers.customer_company_name
     */
    private String customerCompanyName;

    /**
     * 子单位id 子公司不一定是部门直接上级,所属表字段为customers.customer_dep_parentid
     */
    private Long customerDepParentid;

    /**
     * ,所属表字段为customers.customer_dep_parentname
     */
    private String customerDepParentname;

    /**
     * 部门id,所属表字段为customers.customer_dep_id
     */
    private Long customerDepId;

    /**
     * 部门/子公司名称,所属表字段为customers.customer_dep_name
     */
    private String customerDepName;

    /**
     * 员工编号,所属表字段为customers.customer_number
     */
    private String customerNumber;

    /**
     * 登录名,所属表字段为customers.customer_logname
     */
    private String customerLogname;

    /**
     * 密码md5加密,所属表字段为customers.customer_password
     */
    private String customerPassword;

    /**
     * 登录密码强度,所属表字段为customers.customer_password_strength
     */
    private Integer customerPasswordStrength;

    /**
     * 姓名,所属表字段为customers.customer_turename
     */
    private String customerTurename;

    /**
     * 入职时间,所属表字段为customers.customer_join_time
     */
    private Date customerJoinTime;

    /**
     * 工资,所属表字段为customers.customer_salary
     */
    private BigDecimal customerSalary;

    /**
     * 职位,所属表字段为customers.customer_place
     */
    private String customerPlace;

    /**
     * 工种,所属表字段为customers.customer_worktype
     */
    private String customerWorktype;

    /**
     * 0女 1男,所属表字段为customers.customer_sex
     */
    private Integer customerSex;

    /**
     * 出生日期,所属表字段为customers.customer_birthday
     */
    private Date customerBirthday;

    /**
     * 身份证,所属表字段为customers.customer_idcard
     */
    private String customerIdcard;

    /**
     * ,所属表字段为customers.customer_idcard_img_front
     */
    private String customerIdcardImgFront;

    /**
     * ,所属表字段为customers.customer_idcard_img_back
     */
    private String customerIdcardImgBack;

    /**
     * 手机,所属表字段为customers.customer_phone
     */
    private String customerPhone;

    /**
     * 邮箱,所属表字段为customers.customer_email
     */
    private String customerEmail;

    /**
     * qq号,所属表字段为customers.customer_qq
     */
    private String customerQq;

    /**
     * 银行名称,所属表字段为customers.customer_bank
     */
    private String customerBank;

    /**
     * 支行地址,所属表字段为customers.customer_bankarea
     */
    private String customerBankarea;

    /**
     * 卡号,所属表字段为customers.customer_banknumber
     */
    private String customerBanknumber;

    /**
     * 联系信息,所属表字段为customers.customer_contacts
     */
    private String customerContacts;

    /**
     * 学籍教育信息,所属表字段为customers.customer_edu
     */
    private String customerEdu;

    /**
     * 家庭地址,所属表字段为customers.customer_address
     */
    private String customerAddress;

    /**
     * 客户信用等级 0没有信用 1最高信用 2依次递减,所属表字段为customers.customer_credit
     */
    private Integer customerCredit;

    /**
     * 可预支天数,所属表字段为customers.customer_credit_days
     */
    private Integer customerCreditDays;

    /**
     * 可预支额度,所属表字段为customers.customer_credit_money
     */
    private BigDecimal customerCreditMoney;

    /**
     * 单日可预支额度,所属表字段为customers.customer_credit_money_oneday
     */
    private BigDecimal customerCreditMoneyOneday;

    /**
     * 0不可以 1可用,所属表字段为customers.customer_sign
     */
    private Integer customerSign;

    /**
     * 是否可发工资 0不可发 1可发,所属表字段为customers.customer_ispay
     */
    private Integer customerIspay;

    /**
     * 支付密码,所属表字段为customers.customer_pay_pwd
     */
    private String customerPayPwd;

    /**
     * 支付密码强度,所属表字段为customers.customer_pay_pwd_strength
     */
    private Integer customerPayPwdStrength;

    /**
     * 登录密码错误次数,所属表字段为customers.customer_login_errtimes
     */
    private Integer customerLoginErrtimes;

    /**
     * 支付密码错误次数,所属表字段为customers.customer_pay_errtimes
     */
    private Integer customerPayErrtimes;

    /**
     * 推荐人推广码,所属表字段为customers.customer_references
     */
    private String customerReferences;

    /**
     * 推广人id,所属表字段为customers.customer_references_id
     */
    private Long customerReferencesId;

    /**
     * 推广来源搜索引擎,所属表字段为customers.customer_spread_source
     */
    private String customerSpreadSource;

    /**
     * 推广来源关键字,所属表字段为customers.customer_spread_source_key
     */
    private String customerSpreadSourceKey;

    /**
     * 注册活动 0直接注册 1............,所属表字段为customers.customer_activity
     */
    private Integer customerActivity;

    /**
     * 注册客户端 1web 2wap 3ios 4android,所属表字段为customers.customer_client
     */
    private Integer customerClient;

    /**
     * 注册时间,所属表字段为customers.customer_addtime
     */
    private Date customerAddtime;

    /**
     * 工作状态 0离职 1试用 2正式,所属表字段为customers.customer_job_status
     */
    private Integer customerJobStatus;

    /**
     * 登录地址,所属表字段为customers.customer_login_area
     */
    private String customerLoginArea;

    /**
     * 登录ip,所属表字段为customers.customer_login_ip
     */
    private String customerLoginIp;

    /**
     * 上次登录时间,所属表字段为customers.customer_login_time_last
     */
    private Date customerLoginTimeLast;

    /**
     * 本次登录时间,所属表字段为customers.customer_login_time_now
     */
    private Date customerLoginTimeNow;

    /**
     * 添加方式 1b端添加  2b端批量导入 3用户注册,所属表字段为customers.customer_addtype
     */
    private Integer customerAddtype;

    /**
     * 社保卡号,所属表字段为customers.customer_social_number
     */
    private String customerSocialNumber;

    /**
     * 推广码,所属表字段为customers.customer_promote
     */
    private String customerPromote;

    /**
     * 实名认证(0.未认证 1.已认证),所属表字段为customers.customer_trueName_isAuthentication
     */
    private Integer customerTruenameIsauthentication;

    /**
     * 短信验证（0.未验证 1.已验证）,所属表字段为customers.customer_phone_isAuthentication
     */
    private Integer customerPhoneIsauthentication;

    /**
     * 用户头像,所属表字段为customers.customer_img
     */
    private String customerImg;

    /**
     * 职级,所属表字段为customers.customer_place_level
     */
    private String customerPlaceLevel;

    /**
     * 生日月份,所属表字段为customers.customer_birthday_month
     */
    private String customerBirthdayMonth;

    /**
     * 户籍性质,所属表字段为customers.customer_registry
     */
    private String customerRegistry;

    /**
     * 生育情况,所属表字段为customers.customer_birth_state
     */
    private String customerBirthState;

    /**
     * 婚姻状况,所属表字段为customers.customer_marriage
     */
    private String customerMarriage;

    /**
     * 政治面貌,所属表字段为customers.customer_political
     */
    private String customerPolitical;

    /**
     * 毕业学校,所属表字段为customers.customer_school
     */
    private String customerSchool;

    /**
     * 学历,所属表字段为customers.customer_degree
     */
    private String customerDegree;

    /**
     * 专业,所属表字段为customers.customer_major
     */
    private String customerMajor;

    /**
     * 毕业时间,所属表字段为customers.customer_graduate_date
     */
    private Date customerGraduateDate;

    /**
     * 参加工作时间,所属表字段为customers.customer_first_job_date
     */
    private Date customerFirstJobDate;

    /**
     * 合同开始日期,所属表字段为customers.customer_agreement_startdate
     */
    private Date customerAgreementStartdate;

    /**
     * 合同结束日期,所属表字段为customers.customer_agreement_enddate
     */
    private Date customerAgreementEnddate;

    /**
     * 合同性质,所属表字段为customers.customer_agreement_type
     */
    private String customerAgreementType;

    /**
     * 试用期,所属表字段为customers.customer_probation
     */
    private String customerProbation;

    /**
     * 户籍地址,所属表字段为customers.customer_registry_address
     */
    private String customerRegistryAddress;

    /**
     * 员工上传文件id,所属表字段为customers.customer_excel_id
     */
    private Long customerExcelId;
    //英文名
    private String customerEnglishName;
    //年龄
    private Integer customerAge;
    //是否补全资料 1是 2否
    private Integer customerIsComplement;

    //是否定额
    private Integer customerIsExpense;

    //当前月报销额度
    private BigDecimal customerCurrentExpense;

    //当前月工资
    private BigDecimal customerCurrentSalary;

    //试用期工资
    private BigDecimal customerProbationSalary;

    //转正工资
    private BigDecimal customerRegularSalary;

    //定薪日期
    private Date customerRegularTime;

    //是否跳到入职规范页面 1是 2否
    private Integer customerIsRedirect;

    //社保通资料审核状态 1未审核 2待审核 3审核成功 4审核失败
    private Integer customerSocialApproveState;
    //失败原因
    private String customerSocialFailReason;

    /**
     * 调薪后基本工资
     */
    private BigDecimal newSalary;
    //未补全资料发短信时间
    private Date customerSendmsgTime;


    public Date getCustomerSendmsgTime() {
        return customerSendmsgTime;
    }

    public void setCustomerSendmsgTime(Date customerSendmsgTime) {
        this.customerSendmsgTime = customerSendmsgTime;
    }

    public Integer getCustomerSocialApproveState() {
        return customerSocialApproveState;
    }

    public void setCustomerSocialApproveState(Integer customerSocialApproveState) {
        this.customerSocialApproveState = customerSocialApproveState;
    }

    public String getCustomerSocialFailReason() {
        return customerSocialFailReason;
    }

    public void setCustomerSocialFailReason(String customerSocialFailReason) {
        this.customerSocialFailReason = customerSocialFailReason;
    }

    public Integer getCustomerIsRedirect() {
        return customerIsRedirect;
    }

    public void setCustomerIsRedirect(Integer customerIsRedirect) {
        this.customerIsRedirect = customerIsRedirect;
    }

    public String getCustomerBirthdayMonth() {
        return customerBirthdayMonth;
    }

    public void setCustomerBirthdayMonth(String customerBirthdayMonth) {
        this.customerBirthdayMonth = customerBirthdayMonth;
    }

    public String getCustomerEnglishName() {
        return customerEnglishName;
    }

    public void setCustomerEnglishName(String customerEnglishName) {
        this.customerEnglishName = customerEnglishName;
    }

    public Integer getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(Integer customerAge) {
        this.customerAge = customerAge;
    }

    /**
     * 获取 id 字段:customers.customer_id
     *
     * @return customers.customer_id, id
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置 id 字段:customers.customer_id
     *
     * @param customerId customers.customer_id, id
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取 员工企业id 字段:customers.customer_company_id
     *
     * @return customers.customer_company_id, 员工企业id
     */
    public Long getCustomerCompanyId() {
        return customerCompanyId;
    }

    /**
     * 设置 员工企业id 字段:customers.customer_company_id
     *
     * @param customerCompanyId customers.customer_company_id, 员工企业id
     */
    public void setCustomerCompanyId(Long customerCompanyId) {
        this.customerCompanyId = customerCompanyId;
    }

    /**
     * 获取 企业名称 字段:customers.customer_company_name
     *
     * @return customers.customer_company_name, 企业名称
     */
    public String getCustomerCompanyName() {
        return customerCompanyName;
    }

    /**
     * 设置 企业名称 字段:customers.customer_company_name
     *
     * @param customerCompanyName customers.customer_company_name, 企业名称
     */
    public void setCustomerCompanyName(String customerCompanyName) {
        this.customerCompanyName = customerCompanyName == null ? null : customerCompanyName.trim();
    }

    /**
     * 获取 子单位id 子公司不一定是部门直接上级 字段:customers.customer_dep_parentid
     *
     * @return customers.customer_dep_parentid, 子单位id 子公司不一定是部门直接上级
     */
    public Long getCustomerDepParentid() {
        return customerDepParentid;
    }

    /**
     * 设置 子单位id 子公司不一定是部门直接上级 字段:customers.customer_dep_parentid
     *
     * @param customerDepParentid customers.customer_dep_parentid, 子单位id 子公司不一定是部门直接上级
     */
    public void setCustomerDepParentid(Long customerDepParentid) {
        this.customerDepParentid = customerDepParentid;
    }

    /**
     * 获取  字段:customers.customer_dep_parentname
     *
     * @return customers.customer_dep_parentname,
     */
    public String getCustomerDepParentname() {
        return customerDepParentname;
    }

    /**
     * 设置  字段:customers.customer_dep_parentname
     *
     * @param customerDepParentname customers.customer_dep_parentname,
     */
    public void setCustomerDepParentname(String customerDepParentname) {
        this.customerDepParentname = customerDepParentname == null ? null : customerDepParentname.trim();
    }

    /**
     * 获取 部门id 字段:customers.customer_dep_id
     *
     * @return customers.customer_dep_id, 部门id
     */
    public Long getCustomerDepId() {
        return customerDepId;
    }

    /**
     * 设置 部门id 字段:customers.customer_dep_id
     *
     * @param customerDepId customers.customer_dep_id, 部门id
     */
    public void setCustomerDepId(Long customerDepId) {
        this.customerDepId = customerDepId;
    }

    /**
     * 获取 部门/子公司名称 字段:customers.customer_dep_name
     *
     * @return customers.customer_dep_name, 部门/子公司名称
     */
    public String getCustomerDepName() {
        return customerDepName;
    }

    /**
     * 设置 部门/子公司名称 字段:customers.customer_dep_name
     *
     * @param customerDepName customers.customer_dep_name, 部门/子公司名称
     */
    public void setCustomerDepName(String customerDepName) {
        this.customerDepName = customerDepName == null ? null : customerDepName.trim();
    }

    /**
     * 获取 员工编号 字段:customers.customer_number
     *
     * @return customers.customer_number, 员工编号
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * 设置 员工编号 字段:customers.customer_number
     *
     * @param customerNumber customers.customer_number, 员工编号
     */
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber == null ? null : customerNumber.trim();
    }

    /**
     * 获取 登录名 字段:customers.customer_logname
     *
     * @return customers.customer_logname, 登录名
     */
    public String getCustomerLogname() {
        return customerLogname;
    }

    /**
     * 设置 登录名 字段:customers.customer_logname
     *
     * @param customerLogname customers.customer_logname, 登录名
     */
    public void setCustomerLogname(String customerLogname) {
        this.customerLogname = customerLogname == null ? null : customerLogname.trim();
    }

    /**
     * 获取 密码md5加密 字段:customers.customer_password
     *
     * @return customers.customer_password, 密码md5加密
     */
    public String getCustomerPassword() {
        return customerPassword;
    }

    /**
     * 设置 密码md5加密 字段:customers.customer_password
     *
     * @param customerPassword customers.customer_password, 密码md5加密
     */
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword == null ? null : customerPassword.trim();
    }

    /**
     * 获取 登录密码强度 字段:customers.customer_password_strength
     *
     * @return customers.customer_password_strength, 登录密码强度
     */
    public Integer getCustomerPasswordStrength() {
        return customerPasswordStrength;
    }

    /**
     * 设置 登录密码强度 字段:customers.customer_password_strength
     *
     * @param customerPasswordStrength customers.customer_password_strength, 登录密码强度
     */
    public void setCustomerPasswordStrength(Integer customerPasswordStrength) {
        this.customerPasswordStrength = customerPasswordStrength;
    }

    /**
     * 获取 姓名 字段:customers.customer_turename
     *
     * @return customers.customer_turename, 姓名
     */
    public String getCustomerTurename() {
        return customerTurename;
    }

    /**
     * 设置 姓名 字段:customers.customer_turename
     *
     * @param customerTurename customers.customer_turename, 姓名
     */
    public void setCustomerTurename(String customerTurename) {
        this.customerTurename = customerTurename == null ? null : customerTurename.trim();
    }

    /**
     * 获取 入职时间 字段:customers.customer_join_time
     *
     * @return customers.customer_join_time, 入职时间
     */
    public Date getCustomerJoinTime() {
        return customerJoinTime;
    }

    /**
     * 设置 入职时间 字段:customers.customer_join_time
     *
     * @param customerJoinTime customers.customer_join_time, 入职时间
     */
    public void setCustomerJoinTime(Date customerJoinTime) {
        this.customerJoinTime = customerJoinTime;
    }

    /**
     * 获取 工资 字段:customers.customer_salary
     *
     * @return customers.customer_salary, 工资
     */
    public BigDecimal getCustomerSalary() {
        return customerSalary;
    }

    /**
     * 设置 工资 字段:customers.customer_salary
     *
     * @param customerSalary customers.customer_salary, 工资
     */
    public void setCustomerSalary(BigDecimal customerSalary) {
        this.customerSalary = customerSalary;
    }

    /**
     * 获取 职位 字段:customers.customer_place
     *
     * @return customers.customer_place, 职位
     */
    public String getCustomerPlace() {
        return customerPlace;
    }

    /**
     * 设置 职位 字段:customers.customer_place
     *
     * @param customerPlace customers.customer_place, 职位
     */
    public void setCustomerPlace(String customerPlace) {
        this.customerPlace = customerPlace == null ? null : customerPlace.trim();
    }

    /**
     * 获取 工种 字段:customers.customer_worktype
     *
     * @return customers.customer_worktype, 工种
     */
    public String getCustomerWorktype() {
        return customerWorktype;
    }

    /**
     * 设置 工种 字段:customers.customer_worktype
     *
     * @param customerWorktype customers.customer_worktype, 工种
     */
    public void setCustomerWorktype(String customerWorktype) {
        this.customerWorktype = customerWorktype == null ? null : customerWorktype.trim();
    }

    /**
     * 获取 0女 1男 字段:customers.customer_sex
     *
     * @return customers.customer_sex, 0女 1男
     */
    public Integer getCustomerSex() {
        return customerSex;
    }

    /**
     * 设置 0女 1男 字段:customers.customer_sex
     *
     * @param customerSex customers.customer_sex, 0女 1男
     */
    public void setCustomerSex(Integer customerSex) {
        this.customerSex = customerSex;
    }

    /**
     * 获取 出生日期 字段:customers.customer_birthday
     *
     * @return customers.customer_birthday, 出生日期
     */
    public Date getCustomerBirthday() {
        return customerBirthday;
    }

    /**
     * 设置 出生日期 字段:customers.customer_birthday
     *
     * @param customerBirthday customers.customer_birthday, 出生日期
     */
    public void setCustomerBirthday(Date customerBirthday) {
        this.customerBirthday = customerBirthday;
    }

    /**
     * 获取 身份证 字段:customers.customer_idcard
     *
     * @return customers.customer_idcard, 身份证
     */
    public String getCustomerIdcard() {
        return customerIdcard;
    }

    /**
     * 设置 身份证 字段:customers.customer_idcard
     *
     * @param customerIdcard customers.customer_idcard, 身份证
     */
    public void setCustomerIdcard(String customerIdcard) {
        this.customerIdcard = customerIdcard == null ? null : customerIdcard.trim();
    }

    /**
     * 获取  字段:customers.customer_idcard_img_front
     *
     * @return customers.customer_idcard_img_front,
     */
    public String getCustomerIdcardImgFront() {
        return customerIdcardImgFront;
    }

    /**
     * 设置  字段:customers.customer_idcard_img_front
     *
     * @param customerIdcardImgFront customers.customer_idcard_img_front,
     */
    public void setCustomerIdcardImgFront(String customerIdcardImgFront) {
        this.customerIdcardImgFront = customerIdcardImgFront == null ? null : customerIdcardImgFront.trim();
    }

    /**
     * 获取  字段:customers.customer_idcard_img_back
     *
     * @return customers.customer_idcard_img_back,
     */
    public String getCustomerIdcardImgBack() {
        return customerIdcardImgBack;
    }

    /**
     * 设置  字段:customers.customer_idcard_img_back
     *
     * @param customerIdcardImgBack customers.customer_idcard_img_back,
     */
    public void setCustomerIdcardImgBack(String customerIdcardImgBack) {
        this.customerIdcardImgBack = customerIdcardImgBack == null ? null : customerIdcardImgBack.trim();
    }

    /**
     * 获取 手机 字段:customers.customer_phone
     *
     * @return customers.customer_phone, 手机
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * 设置 手机 字段:customers.customer_phone
     *
     * @param customerPhone customers.customer_phone, 手机
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone == null ? null : customerPhone.trim();
    }

    /**
     * 获取 邮箱 字段:customers.customer_email
     *
     * @return customers.customer_email, 邮箱
     */
    public String getCustomerEmail() {
        return customerEmail;
    }

    /**
     * 设置 邮箱 字段:customers.customer_email
     *
     * @param customerEmail customers.customer_email, 邮箱
     */
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail == null ? null : customerEmail.trim();
    }

    /**
     * 获取 qq号 字段:customers.customer_qq
     *
     * @return customers.customer_qq, qq号
     */
    public String getCustomerQq() {
        return customerQq;
    }

    /**
     * 设置 qq号 字段:customers.customer_qq
     *
     * @param customerQq customers.customer_qq, qq号
     */
    public void setCustomerQq(String customerQq) {
        this.customerQq = customerQq == null ? null : customerQq.trim();
    }

    /**
     * 获取 银行名称 字段:customers.customer_bank
     *
     * @return customers.customer_bank, 银行名称
     */
    public String getCustomerBank() {
        return customerBank;
    }

    /**
     * 设置 银行名称 字段:customers.customer_bank
     *
     * @param customerBank customers.customer_bank, 银行名称
     */
    public void setCustomerBank(String customerBank) {
        this.customerBank = customerBank == null ? null : customerBank.trim();
    }

    /**
     * 获取 支行地址 字段:customers.customer_bankarea
     *
     * @return customers.customer_bankarea, 支行地址
     */
    public String getCustomerBankarea() {
        return customerBankarea;
    }

    /**
     * 设置 支行地址 字段:customers.customer_bankarea
     *
     * @param customerBankarea customers.customer_bankarea, 支行地址
     */
    public void setCustomerBankarea(String customerBankarea) {
        this.customerBankarea = customerBankarea == null ? null : customerBankarea.trim();
    }

    /**
     * 获取 卡号 字段:customers.customer_banknumber
     *
     * @return customers.customer_banknumber, 卡号
     */
    public String getCustomerBanknumber() {
        return customerBanknumber;
    }

    /**
     * 设置 卡号 字段:customers.customer_banknumber
     *
     * @param customerBanknumber customers.customer_banknumber, 卡号
     */
    public void setCustomerBanknumber(String customerBanknumber) {
        this.customerBanknumber = customerBanknumber == null ? null : customerBanknumber.trim();
    }

    /**
     * 获取 联系信息 字段:customers.customer_contacts
     *
     * @return customers.customer_contacts, 联系信息
     */
    public String getCustomerContacts() {
        return customerContacts;
    }

    /**
     * 设置 联系信息 字段:customers.customer_contacts
     *
     * @param customerContacts customers.customer_contacts, 联系信息
     */
    public void setCustomerContacts(String customerContacts) {
        this.customerContacts = customerContacts == null ? null : customerContacts.trim();
    }

    /**
     * 获取 学籍教育信息 字段:customers.customer_edu
     *
     * @return customers.customer_edu, 学籍教育信息
     */
    public String getCustomerEdu() {
        return customerEdu;
    }

    /**
     * 设置 学籍教育信息 字段:customers.customer_edu
     *
     * @param customerEdu customers.customer_edu, 学籍教育信息
     */
    public void setCustomerEdu(String customerEdu) {
        this.customerEdu = customerEdu == null ? null : customerEdu.trim();
    }

    /**
     * 获取 家庭地址 字段:customers.customer_address
     *
     * @return customers.customer_address, 家庭地址
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * 设置 家庭地址 字段:customers.customer_address
     *
     * @param customerAddress customers.customer_address, 家庭地址
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress == null ? null : customerAddress.trim();
    }

    /**
     * 获取 客户信用等级 0没有信用 1最高信用 2依次递减 字段:customers.customer_credit
     *
     * @return customers.customer_credit, 客户信用等级 0没有信用 1最高信用 2依次递减
     */
    public Integer getCustomerCredit() {
        return customerCredit;
    }

    /**
     * 设置 客户信用等级 0没有信用 1最高信用 2依次递减 字段:customers.customer_credit
     *
     * @param customerCredit customers.customer_credit, 客户信用等级 0没有信用 1最高信用 2依次递减
     */
    public void setCustomerCredit(Integer customerCredit) {
        this.customerCredit = customerCredit;
    }

    /**
     * 获取 可预支天数 字段:customers.customer_credit_days
     *
     * @return customers.customer_credit_days, 可预支天数
     */
    public Integer getCustomerCreditDays() {
        return customerCreditDays;
    }

    /**
     * 设置 可预支天数 字段:customers.customer_credit_days
     *
     * @param customerCreditDays customers.customer_credit_days, 可预支天数
     */
    public void setCustomerCreditDays(Integer customerCreditDays) {
        this.customerCreditDays = customerCreditDays;
    }

    /**
     * 获取 可预支额度 字段:customers.customer_credit_money
     *
     * @return customers.customer_credit_money, 可预支额度
     */
    public BigDecimal getCustomerCreditMoney() {
        return customerCreditMoney;
    }

    /**
     * 设置 可预支额度 字段:customers.customer_credit_money
     *
     * @param customerCreditMoney customers.customer_credit_money, 可预支额度
     */
    public void setCustomerCreditMoney(BigDecimal customerCreditMoney) {
        this.customerCreditMoney = customerCreditMoney;
    }

    /**
     * 获取 单日可预支额度 字段:customers.customer_credit_money_oneday
     *
     * @return customers.customer_credit_money_oneday, 单日可预支额度
     */
    public BigDecimal getCustomerCreditMoneyOneday() {
        return customerCreditMoneyOneday;
    }

    /**
     * 设置 单日可预支额度 字段:customers.customer_credit_money_oneday
     *
     * @param customerCreditMoneyOneday customers.customer_credit_money_oneday, 单日可预支额度
     */
    public void setCustomerCreditMoneyOneday(BigDecimal customerCreditMoneyOneday) {
        this.customerCreditMoneyOneday = customerCreditMoneyOneday;
    }

    /**
     * 获取 0不可以 1可用 字段:customers.customer_sign
     *
     * @return customers.customer_sign, 0不可以 1可用
     */
    public Integer getCustomerSign() {
        return customerSign;
    }

    /**
     * 设置 0不可以 1可用 字段:customers.customer_sign
     *
     * @param customerSign customers.customer_sign, 0不可以 1可用
     */
    public void setCustomerSign(Integer customerSign) {
        this.customerSign = customerSign;
    }

    /**
     * 获取 是否可发工资 0不可发 1可发 字段:customers.customer_ispay
     *
     * @return customers.customer_ispay, 是否可发工资 0不可发 1可发
     */
    public Integer getCustomerIspay() {
        return customerIspay;
    }

    /**
     * 设置 是否可发工资 0不可发 1可发 字段:customers.customer_ispay
     *
     * @param customerIspay customers.customer_ispay, 是否可发工资 0不可发 1可发
     */
    public void setCustomerIspay(Integer customerIspay) {
        this.customerIspay = customerIspay;
    }

    /**
     * 获取 支付密码 字段:customers.customer_pay_pwd
     *
     * @return customers.customer_pay_pwd, 支付密码
     */
    public String getCustomerPayPwd() {
        return customerPayPwd;
    }

    /**
     * 设置 支付密码 字段:customers.customer_pay_pwd
     *
     * @param customerPayPwd customers.customer_pay_pwd, 支付密码
     */
    public void setCustomerPayPwd(String customerPayPwd) {
        this.customerPayPwd = customerPayPwd == null ? null : customerPayPwd.trim();
    }

    /**
     * 获取 支付密码强度 字段:customers.customer_pay_pwd_strength
     *
     * @return customers.customer_pay_pwd_strength, 支付密码强度
     */
    public Integer getCustomerPayPwdStrength() {
        return customerPayPwdStrength;
    }

    /**
     * 设置 支付密码强度 字段:customers.customer_pay_pwd_strength
     *
     * @param customerPayPwdStrength customers.customer_pay_pwd_strength, 支付密码强度
     */
    public void setCustomerPayPwdStrength(Integer customerPayPwdStrength) {
        this.customerPayPwdStrength = customerPayPwdStrength;
    }

    /**
     * 获取 登录密码错误次数 字段:customers.customer_login_errtimes
     *
     * @return customers.customer_login_errtimes, 登录密码错误次数
     */
    public Integer getCustomerLoginErrtimes() {
        return customerLoginErrtimes;
    }

    /**
     * 设置 登录密码错误次数 字段:customers.customer_login_errtimes
     *
     * @param customerLoginErrtimes customers.customer_login_errtimes, 登录密码错误次数
     */
    public void setCustomerLoginErrtimes(Integer customerLoginErrtimes) {
        this.customerLoginErrtimes = customerLoginErrtimes;
    }

    /**
     * 获取 支付密码错误次数 字段:customers.customer_pay_errtimes
     *
     * @return customers.customer_pay_errtimes, 支付密码错误次数
     */
    public Integer getCustomerPayErrtimes() {
        return customerPayErrtimes;
    }

    /**
     * 设置 支付密码错误次数 字段:customers.customer_pay_errtimes
     *
     * @param customerPayErrtimes customers.customer_pay_errtimes, 支付密码错误次数
     */
    public void setCustomerPayErrtimes(Integer customerPayErrtimes) {
        this.customerPayErrtimes = customerPayErrtimes;
    }

    /**
     * 获取 推荐人推广码 字段:customers.customer_references
     *
     * @return customers.customer_references, 推荐人推广码
     */
    public String getCustomerReferences() {
        return customerReferences;
    }

    /**
     * 设置 推荐人推广码 字段:customers.customer_references
     *
     * @param customerReferences customers.customer_references, 推荐人推广码
     */
    public void setCustomerReferences(String customerReferences) {
        this.customerReferences = customerReferences == null ? null : customerReferences.trim();
    }

    /**
     * 获取 推广人id 字段:customers.customer_references_id
     *
     * @return customers.customer_references_id, 推广人id
     */
    public Long getCustomerReferencesId() {
        return customerReferencesId;
    }

    /**
     * 设置 推广人id 字段:customers.customer_references_id
     *
     * @param customerReferencesId customers.customer_references_id, 推广人id
     */
    public void setCustomerReferencesId(Long customerReferencesId) {
        this.customerReferencesId = customerReferencesId;
    }

    /**
     * 获取 推广来源搜索引擎 字段:customers.customer_spread_source
     *
     * @return customers.customer_spread_source, 推广来源搜索引擎
     */
    public String getCustomerSpreadSource() {
        return customerSpreadSource;
    }

    /**
     * 设置 推广来源搜索引擎 字段:customers.customer_spread_source
     *
     * @param customerSpreadSource customers.customer_spread_source, 推广来源搜索引擎
     */
    public void setCustomerSpreadSource(String customerSpreadSource) {
        this.customerSpreadSource = customerSpreadSource == null ? null : customerSpreadSource.trim();
    }

    /**
     * 获取 推广来源关键字 字段:customers.customer_spread_source_key
     *
     * @return customers.customer_spread_source_key, 推广来源关键字
     */
    public String getCustomerSpreadSourceKey() {
        return customerSpreadSourceKey;
    }

    /**
     * 设置 推广来源关键字 字段:customers.customer_spread_source_key
     *
     * @param customerSpreadSourceKey customers.customer_spread_source_key, 推广来源关键字
     */
    public void setCustomerSpreadSourceKey(String customerSpreadSourceKey) {
        this.customerSpreadSourceKey = customerSpreadSourceKey == null ? null : customerSpreadSourceKey.trim();
    }

    /**
     * 获取 注册活动 0直接注册 1............ 字段:customers.customer_activity
     *
     * @return customers.customer_activity, 注册活动 0直接注册 1............
     */
    public Integer getCustomerActivity() {
        return customerActivity;
    }

    /**
     * 设置 注册活动 0直接注册 1............ 字段:customers.customer_activity
     *
     * @param customerActivity customers.customer_activity, 注册活动 0直接注册 1............
     */
    public void setCustomerActivity(Integer customerActivity) {
        this.customerActivity = customerActivity;
    }

    /**
     * 获取 注册客户端 1web 2wap 3ios 4android 字段:customers.customer_client
     *
     * @return customers.customer_client, 注册客户端 1web 2wap 3ios 4android
     */
    public Integer getCustomerClient() {
        return customerClient;
    }

    /**
     * 设置 注册客户端 1web 2wap 3ios 4android 字段:customers.customer_client
     *
     * @param customerClient customers.customer_client, 注册客户端 1web 2wap 3ios 4android
     */
    public void setCustomerClient(Integer customerClient) {
        this.customerClient = customerClient;
    }

    /**
     * 获取 注册时间 字段:customers.customer_addtime
     *
     * @return customers.customer_addtime, 注册时间
     */
    public Date getCustomerAddtime() {
        return customerAddtime;
    }

    /**
     * 设置 注册时间 字段:customers.customer_addtime
     *
     * @param customerAddtime customers.customer_addtime, 注册时间
     */
    public void setCustomerAddtime(Date customerAddtime) {
        this.customerAddtime = customerAddtime;
    }

    /**
     * 获取 工作状态 0离职 1试用 2正式 字段:customers.customer_job_status
     *
     * @return customers.customer_job_status, 工作状态 0离职 1试用 2正式
     */
    public Integer getCustomerJobStatus() {
        return customerJobStatus;
    }

    /**
     * 设置 工作状态 0离职 1试用 2正式 字段:customers.customer_job_status
     *
     * @param customerJobStatus customers.customer_job_status, 工作状态 0离职 1试用 2正式
     */
    public void setCustomerJobStatus(Integer customerJobStatus) {
        this.customerJobStatus = customerJobStatus;
    }

    /**
     * 获取 登录地址 字段:customers.customer_login_area
     *
     * @return customers.customer_login_area, 登录地址
     */
    public String getCustomerLoginArea() {
        return customerLoginArea;
    }

    /**
     * 设置 登录地址 字段:customers.customer_login_area
     *
     * @param customerLoginArea customers.customer_login_area, 登录地址
     */
    public void setCustomerLoginArea(String customerLoginArea) {
        this.customerLoginArea = customerLoginArea == null ? null : customerLoginArea.trim();
    }

    /**
     * 获取 登录ip 字段:customers.customer_login_ip
     *
     * @return customers.customer_login_ip, 登录ip
     */
    public String getCustomerLoginIp() {
        return customerLoginIp;
    }

    /**
     * 设置 登录ip 字段:customers.customer_login_ip
     *
     * @param customerLoginIp customers.customer_login_ip, 登录ip
     */
    public void setCustomerLoginIp(String customerLoginIp) {
        this.customerLoginIp = customerLoginIp == null ? null : customerLoginIp.trim();
    }

    /**
     * 获取 上次登录时间 字段:customers.customer_login_time_last
     *
     * @return customers.customer_login_time_last, 上次登录时间
     */
    public Date getCustomerLoginTimeLast() {
        return customerLoginTimeLast;
    }

    /**
     * 设置 上次登录时间 字段:customers.customer_login_time_last
     *
     * @param customerLoginTimeLast customers.customer_login_time_last, 上次登录时间
     */
    public void setCustomerLoginTimeLast(Date customerLoginTimeLast) {
        this.customerLoginTimeLast = customerLoginTimeLast;
    }

    /**
     * 获取 本次登录时间 字段:customers.customer_login_time_now
     *
     * @return customers.customer_login_time_now, 本次登录时间
     */
    public Date getCustomerLoginTimeNow() {
        return customerLoginTimeNow;
    }

    /**
     * 设置 本次登录时间 字段:customers.customer_login_time_now
     *
     * @param customerLoginTimeNow customers.customer_login_time_now, 本次登录时间
     */
    public void setCustomerLoginTimeNow(Date customerLoginTimeNow) {
        this.customerLoginTimeNow = customerLoginTimeNow;
    }

    /**
     * 获取 添加方式 1b端添加  2b端批量导入 3用户注册 字段:customers.customer_addtype
     *
     * @return customers.customer_addtype, 添加方式 1b端添加  2b端批量导入 3用户注册
     */
    public Integer getCustomerAddtype() {
        return customerAddtype;
    }

    /**
     * 设置 添加方式 1b端添加  2b端批量导入 3用户注册 字段:customers.customer_addtype
     *
     * @param customerAddtype customers.customer_addtype, 添加方式 1b端添加  2b端批量导入 3用户注册
     */
    public void setCustomerAddtype(Integer customerAddtype) {
        this.customerAddtype = customerAddtype;
    }

    /**
     * 获取 社保卡号 字段:customers.customer_social_number
     *
     * @return customers.customer_social_number, 社保卡号
     */
    public String getCustomerSocialNumber() {
        return customerSocialNumber;
    }

    /**
     * 设置 社保卡号 字段:customers.customer_social_number
     *
     * @param customerSocialNumber customers.customer_social_number, 社保卡号
     */
    public void setCustomerSocialNumber(String customerSocialNumber) {
        this.customerSocialNumber = customerSocialNumber == null ? null : customerSocialNumber.trim();
    }

    /**
     * 获取 推广码 字段:customers.customer_promote
     *
     * @return customers.customer_promote, 推广码
     */
    public String getCustomerPromote() {
        return customerPromote;
    }

    /**
     * 设置 推广码 字段:customers.customer_promote
     *
     * @param customerPromote customers.customer_promote, 推广码
     */
    public void setCustomerPromote(String customerPromote) {
        this.customerPromote = customerPromote == null ? null : customerPromote.trim();
    }

    /**
     * 获取 实名认证(0.未认证 1.已认证) 字段:customers.customer_trueName_isAuthentication
     *
     * @return customers.customer_trueName_isAuthentication, 实名认证(0.未认证 1.已认证)
     */
    public Integer getCustomerTruenameIsauthentication() {
        return customerTruenameIsauthentication;
    }

    /**
     * 设置 实名认证(0.未认证 1.已认证) 字段:customers.customer_trueName_isAuthentication
     *
     * @param customerTruenameIsauthentication customers.customer_trueName_isAuthentication, 实名认证(0.未认证 1.已认证)
     */
    public void setCustomerTruenameIsauthentication(Integer customerTruenameIsauthentication) {
        this.customerTruenameIsauthentication = customerTruenameIsauthentication;
    }

    /**
     * 获取 短信验证（0.未验证 1.已验证） 字段:customers.customer_phone_isAuthentication
     *
     * @return customers.customer_phone_isAuthentication, 短信验证（0.未验证 1.已验证）
     */
    public Integer getCustomerPhoneIsauthentication() {
        return customerPhoneIsauthentication;
    }

    /**
     * 设置 短信验证（0.未验证 1.已验证） 字段:customers.customer_phone_isAuthentication
     *
     * @param customerPhoneIsauthentication customers.customer_phone_isAuthentication, 短信验证（0.未验证 1.已验证）
     */
    public void setCustomerPhoneIsauthentication(Integer customerPhoneIsauthentication) {
        this.customerPhoneIsauthentication = customerPhoneIsauthentication;
    }

    /**
     * 获取 用户头像 字段:customers.customer_img
     *
     * @return customers.customer_img, 用户头像
     */
    public String getCustomerImg() {
        return customerImg;
    }

    /**
     * 设置 用户头像 字段:customers.customer_img
     *
     * @param customerImg customers.customer_img, 用户头像
     */
    public void setCustomerImg(String customerImg) {
        this.customerImg = customerImg == null ? null : customerImg.trim();
    }

    /**
     * 获取 职级 字段:customers.customer_place_level
     *
     * @return customers.customer_place_level, 职级
     */
    public String getCustomerPlaceLevel() {
        return customerPlaceLevel;
    }

    /**
     * 设置 职级 字段:customers.customer_place_level
     *
     * @param customerPlaceLevel customers.customer_place_level, 职级
     */
    public void setCustomerPlaceLevel(String customerPlaceLevel) {
        this.customerPlaceLevel = customerPlaceLevel == null ? null : customerPlaceLevel.trim();
    }

    /**
     * 获取 户籍性质 字段:customers.customer_registry
     *
     * @return customers.customer_registry, 户籍性质
     */
    public String getCustomerRegistry() {
        return customerRegistry;
    }

    /**
     * 设置 户籍性质 字段:customers.customer_registry
     *
     * @param customerRegistry customers.customer_registry, 户籍性质
     */
    public void setCustomerRegistry(String customerRegistry) {
        this.customerRegistry = customerRegistry == null ? null : customerRegistry.trim();
    }

    /**
     * 获取 生育情况 字段:customers.customer_birth_state
     *
     * @return customers.customer_birth_state, 生育情况
     */
    public String getCustomerBirthState() {
        return customerBirthState;
    }

    /**
     * 设置 生育情况 字段:customers.customer_birth_state
     *
     * @param customerBirthState customers.customer_birth_state, 生育情况
     */
    public void setCustomerBirthState(String customerBirthState) {
        this.customerBirthState = customerBirthState == null ? null : customerBirthState.trim();
    }

    /**
     * 获取 婚姻状况 字段:customers.customer_marriage
     *
     * @return customers.customer_marriage, 婚姻状况
     */
    public String getCustomerMarriage() {
        return customerMarriage;
    }

    /**
     * 设置 婚姻状况 字段:customers.customer_marriage
     *
     * @param customerMarriage customers.customer_marriage, 婚姻状况
     */
    public void setCustomerMarriage(String customerMarriage) {
        this.customerMarriage = customerMarriage == null ? null : customerMarriage.trim();
    }

    /**
     * 获取 政治面貌 字段:customers.customer_political
     *
     * @return customers.customer_political, 政治面貌
     */
    public String getCustomerPolitical() {
        return customerPolitical;
    }

    /**
     * 设置 政治面貌 字段:customers.customer_political
     *
     * @param customerPolitical customers.customer_political, 政治面貌
     */
    public void setCustomerPolitical(String customerPolitical) {
        this.customerPolitical = customerPolitical == null ? null : customerPolitical.trim();
    }

    /**
     * 获取 毕业学校 字段:customers.customer_school
     *
     * @return customers.customer_school, 毕业学校
     */
    public String getCustomerSchool() {
        return customerSchool;
    }

    /**
     * 设置 毕业学校 字段:customers.customer_school
     *
     * @param customerSchool customers.customer_school, 毕业学校
     */
    public void setCustomerSchool(String customerSchool) {
        this.customerSchool = customerSchool == null ? null : customerSchool.trim();
    }

    /**
     * 获取 学历 字段:customers.customer_degree
     *
     * @return customers.customer_degree, 学历
     */
    public String getCustomerDegree() {
        return customerDegree;
    }

    /**
     * 设置 学历 字段:customers.customer_degree
     *
     * @param customerDegree customers.customer_degree, 学历
     */
    public void setCustomerDegree(String customerDegree) {
        this.customerDegree = customerDegree == null ? null : customerDegree.trim();
    }

    /**
     * 获取 专业 字段:customers.customer_major
     *
     * @return customers.customer_major, 专业
     */
    public String getCustomerMajor() {
        return customerMajor;
    }

    /**
     * 设置 专业 字段:customers.customer_major
     *
     * @param customerMajor customers.customer_major, 专业
     */
    public void setCustomerMajor(String customerMajor) {
        this.customerMajor = customerMajor == null ? null : customerMajor.trim();
    }

    /**
     * 获取 毕业时间 字段:customers.customer_graduate_date
     *
     * @return customers.customer_graduate_date, 毕业时间
     */
    public Date getCustomerGraduateDate() {
        return customerGraduateDate;
    }

    /**
     * 设置 毕业时间 字段:customers.customer_graduate_date
     *
     * @param customerGraduateDate customers.customer_graduate_date, 毕业时间
     */
    public void setCustomerGraduateDate(Date customerGraduateDate) {
        this.customerGraduateDate = customerGraduateDate;
    }

    /**
     * 获取 参加工作时间 字段:customers.customer_first_job_date
     *
     * @return customers.customer_first_job_date, 参加工作时间
     */
    public Date getCustomerFirstJobDate() {
        return customerFirstJobDate;
    }

    /**
     * 设置 参加工作时间 字段:customers.customer_first_job_date
     *
     * @param customerFirstJobDate customers.customer_first_job_date, 参加工作时间
     */
    public void setCustomerFirstJobDate(Date customerFirstJobDate) {
        this.customerFirstJobDate = customerFirstJobDate;
    }

    /**
     * 获取 合同开始日期 字段:customers.customer_agreement_startdate
     *
     * @return customers.customer_agreement_startdate, 合同开始日期
     */
    public Date getCustomerAgreementStartdate() {
        return customerAgreementStartdate;
    }

    /**
     * 设置 合同开始日期 字段:customers.customer_agreement_startdate
     *
     * @param customerAgreementStartdate customers.customer_agreement_startdate, 合同开始日期
     */
    public void setCustomerAgreementStartdate(Date customerAgreementStartdate) {
        this.customerAgreementStartdate = customerAgreementStartdate;
    }

    /**
     * 获取 合同结束日期 字段:customers.customer_agreement_enddate
     *
     * @return customers.customer_agreement_enddate, 合同结束日期
     */
    public Date getCustomerAgreementEnddate() {
        return customerAgreementEnddate;
    }

    /**
     * 设置 合同结束日期 字段:customers.customer_agreement_enddate
     *
     * @param customerAgreementEnddate customers.customer_agreement_enddate, 合同结束日期
     */
    public void setCustomerAgreementEnddate(Date customerAgreementEnddate) {
        this.customerAgreementEnddate = customerAgreementEnddate;
    }

    /**
     * 获取 合同性质 字段:customers.customer_agreement_type
     *
     * @return customers.customer_agreement_type, 合同性质
     */
    public String getCustomerAgreementType() {
        return customerAgreementType;
    }

    /**
     * 设置 合同性质 字段:customers.customer_agreement_type
     *
     * @param customerAgreementType customers.customer_agreement_type, 合同性质
     */
    public void setCustomerAgreementType(String customerAgreementType) {
        this.customerAgreementType = customerAgreementType == null ? null : customerAgreementType.trim();
    }

    /**
     * 获取 试用期 字段:customers.customer_probation
     *
     * @return customers.customer_probation, 试用期
     */
    public String getCustomerProbation() {
        return customerProbation;
    }

    /**
     * 设置 试用期 字段:customers.customer_probation
     *
     * @param customerProbation customers.customer_probation, 试用期
     */
    public void setCustomerProbation(String customerProbation) {
        this.customerProbation = customerProbation == null ? null : customerProbation.trim();
    }

    /**
     * 获取 户籍地址 字段:customers.customer_registry_address
     *
     * @return customers.customer_registry_address, 户籍地址
     */
    public String getCustomerRegistryAddress() {
        return customerRegistryAddress;
    }

    /**
     * 设置 户籍地址 字段:customers.customer_registry_address
     *
     * @param customerRegistryAddress customers.customer_registry_address, 户籍地址
     */
    public void setCustomerRegistryAddress(String customerRegistryAddress) {
        this.customerRegistryAddress = customerRegistryAddress == null ? null : customerRegistryAddress.trim();
    }

    /**
     * 获取 员工上传文件id 字段:customers.customer_excel_id
     *
     * @return customers.customer_excel_id, 员工上传文件id
     */
    public Long getCustomerExcelId() {
        return customerExcelId;
    }

    /**
     * 设置 员工上传文件id 字段:customers.customer_excel_id
     *
     * @param customerExcelId customers.customer_excel_id, 员工上传文件id
     */
    public void setCustomerExcelId(Long customerExcelId) {
        this.customerExcelId = customerExcelId;
    }

    public Integer getCustomerIsComplement() {
        return customerIsComplement;
    }

    public void setCustomerIsComplement(Integer customerIsComplement) {
        this.customerIsComplement = customerIsComplement;
    }

    public BigDecimal getCustomerCurrentSalary() {
        return customerCurrentSalary;
    }

    public void setCustomerCurrentSalary(BigDecimal customerCurrentSalary) {
        this.customerCurrentSalary = customerCurrentSalary;
    }

    public BigDecimal getCustomerProbationSalary() {
        return customerProbationSalary;
    }

    public void setCustomerProbationSalary(BigDecimal customerProbationSalary) {
        this.customerProbationSalary = customerProbationSalary;
    }

    public BigDecimal getCustomerRegularSalary() {
        return customerRegularSalary;
    }

    public void setCustomerRegularSalary(BigDecimal customerRegularSalary) {
        this.customerRegularSalary = customerRegularSalary;
    }

    public Date getCustomerRegularTime() {
        return customerRegularTime;
    }

    public void setCustomerRegularTime(Date customerRegularTime) {
        this.customerRegularTime = customerRegularTime;
    }

    public BigDecimal getCustomerCurrentExpense() {
        return customerCurrentExpense;
    }

    public void setCustomerCurrentExpense(BigDecimal customerCurrentExpense) {
        this.customerCurrentExpense = customerCurrentExpense;
    }

    public Integer getCustomerIsExpense() {
        return customerIsExpense;
    }

    public void setCustomerIsExpense(Integer customerIsExpense) {
        this.customerIsExpense = customerIsExpense;
    }

    public BigDecimal getNewSalary() {
        return newSalary;
    }

    public void setNewSalary(BigDecimal newSalary) {
        this.newSalary = newSalary;
    }

}