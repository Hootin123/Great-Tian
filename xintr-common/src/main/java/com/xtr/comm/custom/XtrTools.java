package com.xtr.comm.custom;

import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

/**
 * <p>velocity工具方法</p>
 *
 * @author 任齐
 * @createTime: 2016/7/3 11:27
 */
public class XtrTools {

    /**
     * 日期转星期，如：周三
     * @param date
     * @return
     */
    public String weekday(Date date){
        if(null != date){
            return "周" + DateUtil.getDayOfWeek(date, 1);
        }
        return "";
    }

    /**
     * 货币转换
     * @param money
     * @return
     */
    public String money(BigDecimal money){
        if(null == money){
            return "0.00";
        }

        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMaximumFractionDigits(2);
        String str = fmt.format(money);
        int d = str.indexOf(".");
        if(d == -1){
            return str + ".00";
        } else{
            if(str.substring(d+1).length() < 2){
                return str + "0";
            }
        }
        return str;
    }

    /**
     * 货币转换
     * @param money
     * @return
     */
    public String money(Double money){
        if(null == money){
            return "0.00";
        }

        NumberFormat fmt = NumberFormat.getInstance();
        fmt.setMaximumFractionDigits(2);
        String str = fmt.format(money);
        int d = str.indexOf(".");
        if(d == -1){
            return str + ".00";
        } else{
            if(str.substring(d+1).length() < 2){
                return str + "0";
            }
        }
        return str;
    }

    /**
     * 货币转换
     * @param money
     * @return
     */
    public String money(String money){
        if(null == money || money.equals("")){
            return "0.00";
        }
        return StringUtils.toMoney(Double.parseDouble(money));
    }

    public String formatNumber(Object number, int xsd) {
        String str = String.valueOf(number);
        if(null == str || str.equals("") || str.equals("null")){
            return "0.00";
        }
        return new BigDecimal(str).setScale(xsd).toString();

    }

    public boolean isLogin(HttpSession session){
        return getUserSessionInfo(session) != null;
    }

    public String getLoginName(HttpSession session){
        if(session == null)
            return null;
        Object logname = session.getAttribute("userName");
        if(logname != null) {
            return (String) logname;
        }
        return null;
    }

    public<T> T getUserSessionInfo(HttpSession session){
        if(session == null)
            return null;
        return (T) session.getAttribute(CommonConstants.SESSION_USER);
    }

    public<T> T getCompanySessionInfo(HttpSession session){
        if(session == null)
            return null;
        return (T) session.getAttribute(CommonConstants.LOGIN_COMPANY_KEY);
    }

}
