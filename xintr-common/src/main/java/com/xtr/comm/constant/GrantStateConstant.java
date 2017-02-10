package com.xtr.comm.constant;

/**
 * <p>工资单状态常量类</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/1 17:14
 */

public interface GrantStateConstant {

    //    0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
    /**
     * 待处理
     */
    Integer GRANT_STATE_0 = 0;

    /**
     * 发放中
     */
    Integer GRANT_STATE_1 = 1;

    /**
     * 已发放
     */
    Integer GRANT_STATE_2 = 2;

    /**
     * 挂起
     */
    Integer GRANT_STATE_3 = 3;

    /**
     * 仅下发工资单
     */
    Integer GRANT_STATE_4 = 4;

    /**
     * 撤销
     */
    Integer GRANT_STATE_5 = 5;
}
