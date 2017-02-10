package com.xtr.comm.constant;

/**
 * <p>系统常量类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/14 16:13
 */
public class Constant {

    /**
     * 工资单
     */
    public static final String PAYROLL_FILE = "PAYROLL_FILE";

    /**
     * 急速发工资
     */
    public static final String RAPIDLY_PAYROLL = "RAPIDLY_PAYROLL";

    /**
     * 组织与成员
     */
    public static final String MEMBER_FILE = "MEMBER_FILE";
    /*邮箱*/
    public static final String PRE_EMAIL = "email.";

    /**
     * 商户注册相同手机号每天发送验证短信数上限
     */
    public static final int SMS_LIMIT = 50;
    /***
     *图片验证码
     * 用于组成缓存中图片验证KEY的一部分
     */
    public static final String VCOD_EIMG = "VCOD_EIMG";

    /**
     * 商户注册延迟时间
     */
    public static final long REGISTER_DELAY_TIME = 3600 * 24 * 2;

    /**
     * 超级管理员常量
     */
    public static enum SuperAdmin {
        NO(0, "否"), YES(1, "是");
        public int key;
        public String value;

        private SuperAdmin(int key, String value) {
            this.key = key;
            this.value = value;
        }

        public static SuperAdmin get(int key) {
            SuperAdmin[] values = SuperAdmin.values();
            for (SuperAdmin object : values) {
                if (object.key == key) {
                    return object;
                }
            }
            return null;
        }
    }
}
