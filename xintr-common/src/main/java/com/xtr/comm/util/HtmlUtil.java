//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xtr.comm.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class HtmlUtil {
    public HtmlUtil() {
    }

    public static void writerJson(HttpServletResponse response, String jsonStr) {
        writer(response, jsonStr);
    }

    public static void writerJson(HttpServletResponse response, Object object) {
        try {
            response.setContentType("application/json");
            writer(response, JSON.toJSONString(object));
        } catch (JSONException var3) {
            var3.printStackTrace();
        }

    }

    public static void writerHtml(HttpServletResponse response, String htmlStr) {
        writer(response, htmlStr);
    }

    private static void writer(HttpServletResponse response, String str) {
        try {
            new StringBuffer();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = null;
            out = response.getWriter();
            out.print(str);
            out.flush();
            out.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }
}
