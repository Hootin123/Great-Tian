package com.xtr.comm.util;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class URLUtils {

    /**
     * 组装按钮下URL
     *
     * @param menuUrl
     * @param actionUrls
     * @return
     */
    public static void getBtnAccessUrls(String menuUrl, String actionUrls, List<String> accessUrls) {
        if (menuUrl == null || actionUrls == null || accessUrls == null) {
            return;
        }
//        String url = "save.do| action.do |/user/manger/abcd.do";
        String[] actions = StringUtils.split(actionUrls, "|");
        String menuUri = StringUtils.substringBeforeLast(menuUrl, "/");
        for (String action : actions) {
            action = StringUtils.trim(action);
            if (StringUtils.startsWith(action, "/"))
                accessUrls.add(action);
            else
                accessUrls.add(menuUri + "/" + action);
        }
    }

    public static void main(String[] args) {
        String menu = "/sysMneu/dataList.do";
        String url = "save.do| action.do |/user/manger/abcd.do";
        String[] actions = StringUtils.split(url, "|");
        String menuUri = StringUtils.substringBeforeLast(menu, "/");
        for (String action : actions) {
            action = StringUtils.trim(action);
            if (StringUtils.startsWith(action, "/"))
                System.out.println(action);
            else
                System.out.println(menuUri + "/" + action);
        }
    }


}
