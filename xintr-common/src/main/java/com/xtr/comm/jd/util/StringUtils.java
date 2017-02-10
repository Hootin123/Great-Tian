package com.xtr.comm.jd.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: baowp
 * Date: 1/3/14
 * Time: 5:42 PM
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * get the node text
     *
     * @param xml
     * @param nodeName
     * @return
     */
    public static String extract(String xml, String nodeName) {
        return extract(xml, new String[]{nodeName})[0];
    }

    /**
     * get the nodes text array
     *
     * @param xml
     * @param nodeName
     * @return
     */
    public static String[] extract(String xml, String... nodeName) {
        String result[] = new String[nodeName.length];
        int i = 0;
        for (String node : nodeName) {
            String prefix = "<" + node + ">";
            String suffix = "</" + node + ">";
            int start = xml.indexOf(prefix) + prefix.length();
            int end = xml.indexOf(suffix, start);
            result[i++] = xml.substring(start, end);
        }
        return result;
    }

    /**
     * if has <nodeName> then set the value,else append <nodeName>value</nodeName> into parent node
     *
     * @param xml
     * @param parent
     * @param nodeName
     * @param value
     * @return
     */
    public static String setNode(String xml, String parent, String nodeName, String value) {
        if (xml.indexOf("<" + nodeName + ">") > -1)
            return setNodeText(xml, nodeName, value);
        else return appendNode(xml, parent, nodeName, value);
    }

    /**
     * set node(named nodeName) text into the xml
     *
     * @param xml
     * @param nodeName
     * @param value
     * @return
     */
    public static String setNodeText(String xml, String nodeName, String value) {
        String prefix = "<" + nodeName + ">";
        String suffix = "</" + nodeName + ">";
        return xml.replaceAll(prefix + ".*" + suffix, prefix + value + suffix);
    }

    /**
     * append a new node <nodeName>value</nodeName> into parent node
     *
     * @param xml
     * @param parent
     * @param nodeName
     * @param value
     * @return
     */
    public static String appendNode(String xml, String parent, String nodeName, String value) {
        String node = "<" + nodeName + ">" + value + "</" + nodeName + ">";
        StringBuilder sb = new StringBuilder(xml);
        int index = sb.indexOf("</" + parent + ">");
        sb.insert(index, node);
        return sb.toString();
    }

    /**
     * 在字符串左侧按长度填充字符
     *
     * @param s   原字符串
     * @param len 填充后的总字符串长度
     * @param c   填充字符
     * @return 填充完的字符串
     */
    public static String padleft(String s, int len, char c) {
        s = s.trim();
        if (s.length() > len)
            throw new IllegalArgumentException("invalid len " + s.length() + "/" + len);
        StringBuilder d = new StringBuilder(len);
        int fill = len - s.length();
        while (fill-- > 0)
            d.append(c);
        d.append(s);
        return d.toString();
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String firstLetterToUpper(String str) {
        char[] array = str.toCharArray();
        array[0] -= 32;
        return String.valueOf(array);
    }

    /**
     * 截取异常堆栈信息
     *
     * @param throwable
     * @param limit
     * @return
     */
    public static String toStringExp(String msg, Throwable throwable, int limit) {
        StringWriter sb = new StringWriter();
        if (msg != null) {
            sb.append(msg).append(" {");
        }
        PrintWriter pw = new PrintWriter(sb);
        try {
            throwable.printStackTrace(pw);
        } finally {
            pw.close();
        }
        StringBuffer buffer = sb.getBuffer();
        if (buffer == null)
            return null;
        if (limit <= 0 || buffer.length() <= limit) {
            return buffer.toString();
        } else {
            return buffer.delete(limit, buffer.length()).append("......").append(" }").toString();
        }
    }
}
