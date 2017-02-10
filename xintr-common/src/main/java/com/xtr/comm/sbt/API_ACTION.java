package com.xtr.comm.sbt;

/**
 * <p>社保通接口枚举</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 9:08.
 */
public enum API_ACTION {

    BASE_GETCITIES, BASE_GETBASIC, BASE_CALCULATE, BASE_CALCOVERDUE, ORDER_PLACE, ORDER_MANAGER, ORDER_ADDITIONAL;

    public String getActionName() {
        return toString().toLowerCase().replace('_', '/');
    }

}
