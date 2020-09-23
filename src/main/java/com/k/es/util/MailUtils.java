package com.k.es.util;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/9/23 9:44
 **/
public class MailUtils {

    //变量提升
    private static Session session;

    //静态代码块
    static {
        /*
         *  Properties配置文件的读取类
         *  host，SMTP主机名
         *  port，端口号
         *  auth，用户认证
         *  class，规定要使用SSL加密套接字
         *
         *  Authenticator认证器
         *  授权邮箱，授权码（文末有解释）
         *  应用程序创建会话时注册该子类的实例并调用getPasswordAuthentication，返回一个PasswordAuthentication
         *  所以重写getPasswordAuthentication返回一个该类的对象，该类构造方法PasswordAuthentication(String userName, char[] password)
         *
         *  session邮件会话
         *  收集邮件API使用的属性和默认值，单个默认会话可以由桌面上的多个应用程序共享，也可以创建未共享的会话
         */
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("1125397419@qq.com", "123456");
            }
        };

        session = Session.getInstance(props, authenticator);

    }

    /**
     * @param mailAddress 收件人
     * @param subject     标题
     * @param text        内容
     */
    public static void sendMail(String mailAddress, String subject, String text) throws AddressException, MessagingException {
        /*
         * message邮件载体（需要运行时环境）
         * setFrom，发件人（参数是InternetAddress，属于Java的网络编程）
         * setRecipient，收件人（第一个TO--发送,后面是收件人）
         * setSubject，设置标题
         * send，静态发送邮件
         */
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("1125397419@qq.com"));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
        message.setSubject(subject);
        message.setText(text, "utf-8");
        Transport.send(message);

    }

}
