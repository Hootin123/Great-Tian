package com.xtr.comm.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/11 10:24
 */
public class MathUtils {

    public static BigDecimal toMoney(BigDecimal money){
        DecimalFormat df = new DecimalFormat("#.##");
        return new BigDecimal(df.format(money));
    }

    public static Double toMoney(Double money){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(money));
    }

}
