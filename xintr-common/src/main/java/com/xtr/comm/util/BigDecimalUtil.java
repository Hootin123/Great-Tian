package com.xtr.comm.util;

import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;

/**
 * @Author Xuewu
 * @Date 2016/8/17.
 */
public class BigDecimalUtil {

    public static boolean isGreaterThanZero(BigDecimal value){
        if(value == null)
            return false;
        if(value.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return true;
    }

    public static boolean isMoney(String str){
        if(str == null)
            return false;
        if(!NumberUtils.isNumber(str)) {
            return false;
        }
        BigDecimal bigDecimal = new BigDecimal(str);
        if(bigDecimal.scale() > 2) {
            return false;
        }
        return isGreaterThanZero(bigDecimal);
    }

    public static BigDecimal min(BigDecimal... values) {
        if(values == null || values.length <1)
            return null;
        BigDecimal min = values[0];
        for (BigDecimal value : values) {
            if(value != null)
                min = min.min(value);
        }
        return min;
    }
}
