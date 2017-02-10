package com.xtr.comm.constant;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/15 13:45
 */
public class CustomerConstants {

    //企业端获取员工人数对应的状态
    public static final int CUSTOMER_PERSONNUM_ENTER = 1;//入职
    public static final int CUSTOMER_PERSONNUM_LEAVE = 2;//离职
    public static final int CUSTOMER_PERSONNUM_WAIT = 3;//待转正
    public static final int CUSTOMER_PERSONNUM_LESSINFO = 4;//未补全资料
    public static final int CUSTOMER_PERSONNUM_ALL = 5;//企业所有员工(除了离职和删除的)
    public static final int CUSTOMER_PERSONNUM_NO_SARALY = 6;//未定薪人数(取当前时间还未定薪的员工,删除和离职的不算)

    //是否补全资料,是否跳转到入职规范页面
    public static final int CUSTOMER_ISCOMPLEMENT_YES = 1;//补全,跳转到入职规范页面
    public static final int CUSTOMER_ISCOMPLEMENT_NO = 2;//未补全,不跳转到入职规范页面

    //上传文件类型判断
    public static final String CUSTOMER_UPLOADTYPE_XLS = ".xls";
    public static final String CUSTOMER_UPLOADTYPE_XLSX = ".xlsx";
    public static final int CUSTOMER_UPLOADLINE_START = 2;//开始读取行
    public static final int CUSTOMER_UPLOADSHEET_START = 0;//读取页签
    public static final int CUSTOMER_UPLOADLINDE_LASTNUM = 10;//每行最大列数
    public static final int CUSTOMER_UPLOADLINDE_NEWLASTNUM = 6;//每行最大列数
    public static final int CUSTOMER_UPLOADLINDE_INCOMPLETENUM = 5;//每行最大列数
    public static final String CUSTOMER_CUSTOMERSTATE_REGULAR = "转正";
    public static final String CUSTOMER_CUSTOMERSTATE_ENTRY = "试用期";
    public static final String CUSTOMER_CUSTOMERSTATE_ENTRY2 = "试用";
    public static final String CUSTOMER_EMPLOYETYPE_OFFICAL = "正式";
    public static final String CUSTOMER_EMPLOYETYPE_TEMPORARY = "劳务";

    //员工状态
    public static final int CUSTOMER_PERSONSTATE_ENTER = 1;//入职
    public static final int CUSTOMER_PERSONSTATE_REGULAR = 2;//转正
    public static final int CUSTOMER_PERSONSTATE_LEAVE = 3;//离职
    public static final int CUSTOMER_PERSONSTATE_DELETE = 4;//删除

    //聘用形式
    public static final int CUSTOMER_EMPLOYMETHOD_OFFICAL = 1;//正式
    public static final int CUSTOMER_EMPLOYMETHOD_TEMPORARY = 2;//劳务

    //记录操作类型
    public static final int CUSTOMER_RECORDTYPE_ENTER = 1;//入职
    public static final int CUSTOMER_RECORDTYPE_REGULAR = 2;//转正
    public static final int CUSTOMER_RECORDTYPE_LEAVE = 3;//离职
    public static final int CUSTOMER_RECORDTYPE_DELETE = 4;//删除
    public static final int CUSTOMER_RECORDTYPE_TRANFER = 5;//调岗
    public static final int CUSTOMER_RECORDTYPE_SALARY = 6;//调薪

    //日期
    public static final SimpleDateFormat SDF_YEAR_MONTH = new SimpleDateFormat("yyyy-MM");
    public static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("dd");
    public static final SimpleDateFormat SDF_YEAR_MONTH_DAY_LINE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_YEAR_MONTH_DAY_POINT = new SimpleDateFormat("yyyy.MM.dd");
    public static final SimpleDateFormat SDF_YEAR_MONTH_DAY_XX = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat SDF_YEAR_MONTH_DAY = new SimpleDateFormat("yyyyMMdd");

    //操作类型
    public static final int CUSTOMER_WILLOPERATION_ADD = 1;//新增
    public static final int CUSTOMER_WILLOPERATION_EDIT = 2;//修改
    //操作状态
    public static final int CUSTOMER_WILLOPERATIONSTATE_ADD = 1;//新增
    public static final int CUSTOMER_WILLOPERATIONSTATE_COPY = 2;//重复
    public static final int CUSTOMER_WILLOPERATIONSTATE_EDIT = 3;//编辑

    //社保通资料审核状态
    public static final int CUSTOMER_SOCIAL_APPROVESTATE_NO = 1;//未审核
    public static final int CUSTOMER_SOCIAL_APPROVESTATE_WILL = 2;//待审核
    public static final int CUSTOMER_SOCIAL_APPROVESTATE_SUCCESS = 3;//审核成功
    public static final int CUSTOMER_SOCIAL_APPROVESTATE_FAIL = 4;//审核失败

    //性别
    public static final int CUSTOMER_SEX_WOMEN = 0;//女
    public static final int CUSTOMER_SEX_MAN = 1;//男

    //新的记录操作类型
    public static final int CUSTOMER_OPERATIONTYPE_ALL = 1;//全部(不包括删除与离职)
    public static final int CUSTOMER_OPERATIONTYPE_REGULAR = 2;//正式(已转正的员工,不包括删除和离职)
    public static final int CUSTOMER_OPERATIONTYPE_TRY = 3;//试用期(还未转正的员工)
    public static final int CUSTOMER_OPERATIONTYPE_WILLLEAVE = 4;//即将离职
    public static final int CUSTOMER_OPERATIONTYPE_LEAVE = 5;//已离职(不包括删除的员工)

    //新增员工操作类型
    public static final int CUSTOMER_ADDCUSTOMER_REGULAR = 2;//转正
    public static final int CUSTOMER_ADDCUSTOMER_ENTER = 1;//入职

    //初始的值
    public static final BigDecimal INIT_BIGDECIMAL_COMPARE = new BigDecimal("0");

    //未补全信息的补全资料类型(1补全工资卡号 2补全工资卡开户行 3补全身份证号 4补全正面图片 5补全反面图片)
    public static final int CUSTOMER_INCOMPLETE_BANKNUMBER = 1;
    public static final int CUSTOMER_INCOMPLETE_BANKNAME = 2;
    public static final int CUSTOMER_INCOMPLETE_IDCARD = 3;
    public static final int CUSTOMER_INCOMPLETE_IMGPRE = 4;
    public static final int CUSTOMER_INCOMPLETE_IMGBACK = 5;
}
