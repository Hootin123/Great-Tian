package com.xtr.comm.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/18 17:54
 */
public class CompanyProtocolConstants {

    //前台显示操作栏按钮
    public static final int PROTOCOL_OPERATIONSHOW_SHOWDF = 1;//显示代发签约
    public static final int PROTOCOL_OPERATIONSHOW_SHOWFF = 2;//显示垫发签约
    public static final int PROTOCOL_OPERATIONSHOW_NOSHOW = 3;//不显示签约

    //企业协议类型
    public static final int PROTOCOL_CURRENTSTATE_WAIT = 1;//待审批
    public static final int PROTOCOL_CURRENTSTATE_FINISH = 2;//签约
    public static final int PROTOCOL_CURRENTSTATE_WILL = 3;//即将到期
    public static final int PROTOCOL_CURRENTSTATE_ALREADY = 4;//合约到期
    public static final int PROTOCOL_CURRENTSTATE_FREESON = 5;//冻结
    public static final int PROTOCOL_CURRENTSTATE_UNFREESON = 6;//解冻

    //签约类型
    public static final int PROTOCOL_TYPE_DF = 1;//代发协议
    public static final int PROTOCOL_TYPE_FF = 2;//垫发协议
    public static final int PROTOCOL_TYPE_DJ = 3;//代缴社保协议
    public static final int PROTOCOL_TYPE_BX = 4;//报销管理协议

    //生效状态
    public static final int PROTOCOL_USEFUL_YES = 1;//生效
    public static final int PROTOCOL_USEFUL_NO = 2;//未生效

    //子账户表账户性质
    public static final int SUBACCOUNT_PROPERTY_CUSTOMER = 1;//个人
    public static final int SUBACCOUNT_PROPERTY_COMPANY = 2;//企业

    //征信资料限定最大的文件夹数量(数据库没有对应的数量)
    public static final int PROTOCOL_CREDIT_MAXNUM = 20;
    //征信资料临时目录名
    public static final String PROTOCOL_CREDIT_TEMPDIRNAME = "companyCreditTemp";
    //征集资料打包的后缀名
    public static final String PROTOCOL_CREDIT_ENDNAME = ".zip";

    //企业协议新增操作列显示
    public static final int PROTOCOL_ADDPROTOCOL_YES = 1;//显示未签约,点击签约
    public static final int PROTOCOL_ADDPROTOCOL_NO = 2;//显示未签约,点击签约置灰
    public static final int PROTOCOL_ADDPROTOCOL_SHOW = 3;//显示各自的状态

    //企业签约申请状态
    public static final int PROTOCOL_APPLYSTATE_FINISH = 1;//已签约
    public static final int PROTOCOL_APPLYSTATE_NO = 2;//未签约
    public static final int PROTOCOL_APPLYSTATE_YES = 3;//已提交签约申请
    public static final int PROTOCOL_APPLYSTATE_RENEW = 4;//续约

    //协议类型集合
    public static final Integer[] PROTOCOLTYPEARRAY={PROTOCOL_TYPE_DF,PROTOCOL_TYPE_FF,PROTOCOL_TYPE_DJ,PROTOCOL_TYPE_BX};
    public static final Map<Integer,String> PROTOCOLTYPEMAP=new HashMap<>();
    static {//特别注意,更改这儿的同时,protocolManageInfo.js也要更改,而且是更改两个地方,然后companyManageDetail.js也要更改,然后companyProtocolsServiceImpl也要改(改签约的操作列)
        PROTOCOLTYPEMAP.put(PROTOCOL_TYPE_DF,"代发协议");
        PROTOCOLTYPEMAP.put(PROTOCOL_TYPE_FF,"垫发协议");
        PROTOCOLTYPEMAP.put(PROTOCOL_TYPE_DJ,"代缴社保协议");
        PROTOCOLTYPEMAP.put(PROTOCOL_TYPE_BX,"报销管理协议");
    }
    public static final Map<Integer,String> PROTOCOLTYPEMAPHAVE=new HashMap<>();
    static {
        PROTOCOLTYPEMAPHAVE.put(PROTOCOL_TYPE_DF,"已签约代发");
        PROTOCOLTYPEMAPHAVE.put(PROTOCOL_TYPE_FF,"已签约垫发");
        PROTOCOLTYPEMAPHAVE.put(PROTOCOL_TYPE_DJ,"已签约代缴社保");
        PROTOCOLTYPEMAPHAVE.put(PROTOCOL_TYPE_BX,"已签约报销管理");
    }
}
