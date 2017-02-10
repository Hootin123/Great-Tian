package com.xtr.api.domain.company;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业成员,可以进行企业管理的人员,非个人用户(员工)
 */
public class CompanyMembersBean implements Serializable{

    /**
     * 图片验证码
     */

    private boolean isShowBchg;

    private String verifycode;

    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    /**
     * 短信验证码
     */

    private String smscode;

    /**
     * 校验码
     * */
    private String checkNumber;



    /**
    * 图形验证码
     * **/
    private String vcodeimg;


    /**
     * 成员id,所属表字段为company_members.member_id
     */
    private Long memberId;

    /**
     *  成员单位id,所属表字段为company_members.member_company_id
     */
    private Long memberCompanyId;

    /**
     *  子公司id,所属表字段为company_members.member_dep_id
     */
    private Long memberDepId;

    /**
     *  名称,所属表字段为company_members.member_name
     */
    private String memberName;

    /**
     *  权限id ,1,2,3......,,所属表字段为company_members.member_power_ids
     */
    private String memberPowerIds;

    /**
     *  权限名称,所属表字段为company_members.member_power_names
     */
    private String memberPowerNames;

    /**
     *  登录账号,所属表字段为company_members.member_logname
     */
    private String memberLogname;

    public void setConfirmMemberPassword(String confirmMemberPassword) {
        this.confirmMemberPassword = confirmMemberPassword;
    }

    public String getConfirmMemberPassword() {

        return confirmMemberPassword;
    }

    /**
     *  登录密码,所属表字段为company_members.member_password

     */
    private String memberPassword;
    /**
     * 登录确认密码,用于控制层密码校验
     */
    private String  confirmMemberPassword;



    /**
     *  登录密码强度,所属表字段为company_members.member_password_strength
     */
    private Integer memberPasswordStrength;

    /**
     *  身份证,所属表字段为company_members.member_idcard
     */
    private String memberIdcard;

    /**
     *  电话,所属表字段为company_members.member_phone
     */
    private String memberPhone;

    /**
     *  邮箱,所属表字段为company_members.member_email
     */
    private String memberEmail;

    /**
     *  角色名,所属表字段为company_members.member_rolename
     */
    private String memberRolename;


    /**
     *  登录密码错误次数,所属表字段为company_members.member_login_errtimes
     */
    private Integer memberLoginErrtimes;

    /**
     *  出错时间,所属表字段为company_members.member_login_err_time
     */
    private Date memberLoginErrTime;

    /**
     *  是否默认管理员 0不是 1是 默认管理员只能进行权限分配,所属表字段为company_members.member_isdefault
     */
    private Integer memberIsdefault;

    /**
     *  0不可用 1可用,所属表字段为company_members.member_sign
     */
    private Integer memberSign;



    /**
     *  注册时间,所属表字段为company_members.register_time
     */
    private Date registerTime;

    /**
     *  企业名称,所属表字段为company_members.company_name
     */
    private String companyName;

    private Integer memberStatus;

    private String roleName;

    private String idValue;

    public Integer getCompanyIsAuth() {
        return companyIsAuth;
    }

    public void setCompanyIsAuth(Integer companyIsAuth) {
        this.companyIsAuth = companyIsAuth;
    }

    /**
     *  认证状态 0未认证 1已认证,所属表字段为company_members.member_isauth
     */
    private Integer companyIsAuth;
    //最近登录时间
    private Date memberLoginRecentTime;

    public Date getMemberLoginRecentTime() {
        return memberLoginRecentTime;
    }

    public void setMemberLoginRecentTime(Date memberLoginRecentTime) {
        this.memberLoginRecentTime = memberLoginRecentTime;
    }

    /**
     * 获取 成员id 字段:company_members.member_id
     *
     * @return company_members.member_id, 成员id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置 成员id 字段:company_members.member_id
     *
     * @param memberId company_members.member_id, 成员id
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取 成员单位id 字段:company_members.member_company_id
     *
     * @return company_members.member_company_id, 成员单位id
     */
    public Long getMemberCompanyId() {
        return memberCompanyId;
    }

    /**
     * 设置 成员单位id 字段:company_members.member_company_id
     *
     * @param memberCompanyId company_members.member_company_id, 成员单位id
     */
    public void setMemberCompanyId(Long memberCompanyId) {
        this.memberCompanyId = memberCompanyId;
    }

    /**
     * 获取 子公司id 字段:company_members.member_dep_id
     *
     * @return company_members.member_dep_id, 子公司id
     */
    public Long getMemberDepId() {
        return memberDepId;
    }

    /**
     * 设置 子公司id 字段:company_members.member_dep_id
     *
     * @param memberDepId company_members.member_dep_id, 子公司id
     */
    public void setMemberDepId(Long memberDepId) {
        this.memberDepId = memberDepId;
    }

    /**
     * 获取 名称 字段:company_members.member_name
     *
     * @return company_members.member_name, 名称
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * 设置 名称 字段:company_members.member_name
     *
     * @param memberName company_members.member_name, 名称
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName == null ? null : memberName.trim();
    }

    /**
     * 获取 权限id ,1,2,3......, 字段:company_members.member_power_ids
     *
     * @return company_members.member_power_ids, 权限id ,1,2,3......,
     */
    public String getMemberPowerIds() {
        return memberPowerIds;
    }

    /**
     * 设置 权限id ,1,2,3......, 字段:company_members.member_power_ids
     *
     * @param memberPowerIds company_members.member_power_ids, 权限id ,1,2,3......,
     */
    public void setMemberPowerIds(String memberPowerIds) {
        this.memberPowerIds = memberPowerIds == null ? null : memberPowerIds.trim();
    }

    /**
     * 获取 权限名称 字段:company_members.member_power_names
     *
     * @return company_members.member_power_names, 权限名称
     */
    public String getMemberPowerNames() {
        return memberPowerNames;
    }

    /**
     * 设置 权限名称 字段:company_members.member_power_names
     *
     * @param memberPowerNames company_members.member_power_names, 权限名称
     */
    public void setMemberPowerNames(String memberPowerNames) {
        this.memberPowerNames = memberPowerNames == null ? null : memberPowerNames.trim();
    }

    /**
     * 获取 登录账号 字段:company_members.member_logname
     *
     * @return company_members.member_logname, 登录账号
     */
    public String getMemberLogname() {
        return memberLogname;
    }

    /**
     * 设置 登录账号 字段:company_members.member_logname
     *
     * @param memberLogname company_members.member_logname, 登录账号
     */
    public void setMemberLogname(String memberLogname) {
        this.memberLogname = memberLogname == null ? null : memberLogname.trim();
    }

    /**
     * 获取 登录密码 字段:company_members.member_password
     *
     * @return company_members.member_password, 登录密码
     */
    public String getMemberPassword() {
        return memberPassword;
    }

    /**
     * 设置 登录密码 字段:company_members.member_password
     *
     * @param memberPassword company_members.member_password, 登录密码
     */
    public void setMemberPassword(String memberPassword) {
        this.memberPassword = memberPassword == null ? null : memberPassword.trim();
    }

    /**
     * 获取 登录密码强度 字段:company_members.member_password_strength
     *
     * @return company_members.member_password_strength, 登录密码强度
     */
    public Integer getMemberPasswordStrength() {
        return memberPasswordStrength;
    }

    /**
     * 设置 登录密码强度 字段:company_members.member_password_strength
     *
     * @param memberPasswordStrength company_members.member_password_strength, 登录密码强度
     */
    public void setMemberPasswordStrength(Integer memberPasswordStrength) {
        this.memberPasswordStrength = memberPasswordStrength;
    }

    /**
     * 获取 身份证 字段:company_members.member_idcard
     *
     * @return company_members.member_idcard, 身份证
     */
    public String getMemberIdcard() {
        return memberIdcard;
    }

    /**
     * 设置 身份证 字段:company_members.member_idcard
     *
     * @param memberIdcard company_members.member_idcard, 身份证
     */
    public void setMemberIdcard(String memberIdcard) {
        this.memberIdcard = memberIdcard == null ? null : memberIdcard.trim();
    }

    /**
     * 获取 电话 字段:company_members.member_phone
     *
     * @return company_members.member_phone, 电话
     */
    public String getMemberPhone() {
        return memberPhone;
    }

    /**
     * 设置 电话 字段:company_members.member_phone
     *
     * @param memberPhone company_members.member_phone, 电话
     */
    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone == null ? null : memberPhone.trim();
    }

    /**
     * 获取 邮箱 字段:company_members.member_email
     *
     * @return company_members.member_email, 邮箱
     */
    public String getMemberEmail() {
        return memberEmail;
    }

    /**
     * 设置 邮箱 字段:company_members.member_email
     *
     * @param memberEmail company_members.member_email, 邮箱
     */
    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail == null ? null : memberEmail.trim();
    }

    /**
     * 获取 角色名 字段:company_members.member_rolename
     *
     * @return company_members.member_rolename, 角色名
     */
    public String getMemberRolename() {
        return memberRolename;
    }

    /**
     * 设置 角色名 字段:company_members.member_rolename
     *
     * @param memberRolename company_members.member_rolename, 角色名
     */
    public void setMemberRolename(String memberRolename) {
        this.memberRolename = memberRolename == null ? null : memberRolename.trim();
    }

    /**
     * 获取 登录密码错误次数 字段:company_members.member_login_errtimes
     *
     * @return company_members.member_login_errtimes, 登录密码错误次数
     */
    public Integer getMemberLoginErrtimes() {
        return memberLoginErrtimes;
    }

    /**
     * 设置 登录密码错误次数 字段:company_members.member_login_errtimes
     *
     * @param memberLoginErrtimes company_members.member_login_errtimes, 登录密码错误次数
     */
    public void setMemberLoginErrtimes(Integer memberLoginErrtimes) {
        this.memberLoginErrtimes = memberLoginErrtimes;
    }

    /**
     * 获取 出错时间 字段:company_members.member_login_err_time
     *
     * @return company_members.member_login_err_time, 出错时间
     */
    public Date getMemberLoginErrTime() {
        return memberLoginErrTime;
    }

    /**
     * 设置 出错时间 字段:company_members.member_login_err_time
     *
     * @param memberLoginErrTime company_members.member_login_err_time, 出错时间
     */
    public void setMemberLoginErrTime(Date memberLoginErrTime) {
        this.memberLoginErrTime = memberLoginErrTime;
    }

    /**
     * 获取 是否默认管理员 0不是 1是 默认管理员只能进行权限分配 字段:company_members.member_isdefault
     *
     * @return company_members.member_isdefault, 是否默认管理员 0不是 1是 默认管理员只能进行权限分配
     */
    public Integer getMemberIsdefault() {
        return memberIsdefault;
    }

    /**
     * 设置 是否默认管理员 0不是 1是 默认管理员只能进行权限分配 字段:company_members.member_isdefault
     *
     * @param memberIsdefault company_members.member_isdefault, 是否默认管理员 0不是 1是 默认管理员只能进行权限分配
     */
    public void setMemberIsdefault(Integer memberIsdefault) {
        this.memberIsdefault = memberIsdefault;
    }

    /**
     * 获取 0不可用 1可用 字段:company_members.member_sign
     *
     * @return company_members.member_sign, 0不可用 1可用
     */
    public Integer getMemberSign() {
        return memberSign;
    }

    /**
     * 设置 0不可用 1可用 字段:company_members.member_sign
     *
     * @param memberSign company_members.member_sign, 0不可用 1可用
     */

    /**
     * 企业注册用户地推推荐人编号 01~15
     */
    private String memberPushNumber;

    public String getMemberPushNumber() {
        return memberPushNumber;
    }

    public void setMemberPushNumber(String memberPushNumber) {
        this.memberPushNumber = memberPushNumber;
    }

    public void setMemberSign(Integer memberSign) {
        this.memberSign = memberSign;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }



    public String getVcodeimg() {
        return vcodeimg;
    }

    public void setVcodeimg(String vcodeimg) {
        this.vcodeimg = vcodeimg;
    }


    /**
     * 获取 注册时间 字段:company_members.register_time
     *
     * @return company_members.register_time, 注册时间
     */
    public Date getRegisterTime() {
        return registerTime;
    }

    /**
     * 设置 注册时间 字段:company_members.register_time
     *
     * @param registerTime company_members.register_time, 注册时间
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * 获取 企业名称 字段:company_members.company_name
     *
     * @return company_members.company_name, 企业名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置 企业名称 字段:company_members.company_name
     *
     * @param companyName company_members.company_name, 企业名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public boolean isShowBchg() {
        return isShowBchg;
    }

    public void setShowBchg(boolean showBchg) {
        isShowBchg = showBchg;
    }
}