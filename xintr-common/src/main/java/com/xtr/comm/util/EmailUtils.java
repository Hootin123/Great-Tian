package com.xtr.comm.util;

import org.apache.commons.mail.HtmlEmail;

/**
 * Created by abiao on 2016/7/11.
 */
public class EmailUtils {
    /**
     * 发邮件
     * @param host 邮件服务器
     * @param servername 发送者邮箱
     * @param serverpwd 发送者邮箱密码
     * @param address 发送邮件地址
     * @param content 内容
     * @param title 标题
     * @return
     */
    public static boolean sendMail(String host,String servername,String serverpwd,String address,String content,String title,int port) {
        HtmlEmail email = new HtmlEmail();
        email.setTLS(true);
        email.setHostName(host);
        email.setAuthentication(servername, serverpwd);

        if(port>0)
            email.setSmtpPort(port);

        try {
            email.setFrom(servername,"薪太软");
            email.addTo(address);
            email.setCharset("UTF-8");
            email.setSubject(title);
            email.setHtmlMsg(content);
            //email.setHtmlMsg(content.replace("[vcode]", vcode));
            // email.setTextMsg(content);
            email.send();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
