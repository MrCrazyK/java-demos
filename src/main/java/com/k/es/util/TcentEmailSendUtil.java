package com.k.es.util;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/10/10 16:18
 **/

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class TcentEmailSendUtil {

    private static String phone = "18136630618";
    private static String account = "zhaopin@nwepdi.com";// 登录账户
    private static String password = "授权码";// 登录密码
    private static String host = "smtp.exmail.qq.com";// 服务器地址
    private static String port = "465";// 端口
    private static String protocol = "smtp";// 协议

    //初始化参数
    public static Session initProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        // 使用smtp身份验证
        properties.put("mail.smtp.auth", "true");
        // 使用SSL,企业邮箱必需 start
        // 开启安全协议
        MailSSLSocketFactory mailSSLSocketFactory = null;
        try {
            mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.port", port);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, password);
            }
        });
        // 使用SSL,企业邮箱必需 end
        // TODO 显示debug信息 正式环境注释掉
        session.setDebug(true);
        return session;
    }

    /**
     * @param sender       发件人别名
     * @param subject      邮件主题
     * @param content      邮件内容
     * @param receiverList 接收者列表,多个接收者之间用","隔开
     * @param files        附件列表
     */
    public static void send(String sender, String subject, String content, String receiverList, List<File> files) {
        try {
            Session session = initProperties();
            MimeMessage mimeMessage = new MimeMessage(session);
            // 发件人,可以设置发件人的别名
            mimeMessage.setFrom(new InternetAddress(account, sender));
            // 收件人,多人接收
            InternetAddress[] internetAddressTo = InternetAddress.parse(receiverList);
            mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressTo);
            // 主题
            mimeMessage.setSubject(subject);
            // 时间
            mimeMessage.setSentDate(new Date());
            // 容器类 附件
            MimeMultipart mimeMultipart = new MimeMultipart();
            // 可以包装文本,图片,附件
            MimeBodyPart bodyPart = new MimeBodyPart();
            // 设置内容
            bodyPart.setContent(content, "text/html; charset=UTF-8");
            mimeMultipart.addBodyPart(bodyPart);
            if (files != null) {
                files.forEach(file -> {
                    if (file != null) {
                        addFile(file, mimeMultipart);
                    }
                });
            }
            mimeMessage.setContent(mimeMultipart);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFile(File file, MimeMultipart mimeMultipart) {
        // 添加图片&附件
        BodyPart attachmentBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        try {
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            // MimeUtility.encodeWord可以避免文件名乱码
            attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
            mimeMultipart.addBodyPart(attachmentBodyPart);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param subject 标题
     * @param content 内容
     * @param address 地址
     */
    public static void send(String subject, String content, String address) {
        send("西北院招聘平台", subject, content, address, null);
    }

    public static boolean emailSend(String name, String companyName, String companyPhone, String password,
                                    String companyKey, String email) {
        String content = "123123";
        send("***平台", "注册回执", content, email, null);
        return true;
    }

    public static void main(String[] args) {
        List<File> files = new ArrayList<>();
        files.add(new File("C:\\Users\\11253"
                + "\\Desktop\\济南市一网通办总门户（总门户前端开发）-评审通过准则V1.4.xls"));
        files.add(new File("C:\\Users\\11253"
                + "\\Desktop\\jusapce2.0.0.mpp"));
        send("西北院招聘平台", "(开发测试邮件)面试通知", "明天来面试"
                + "</br>http://zwfw.jinan.gov.cn/module/download/downfile"
                + ".jsp?classid=0&showname=%E5%8E%86%E4%B8%8B%E5%8C%BA-%E5%BF%AB%E9%80%92%E7%94%B3%E8%AF%B7"
                + ".xls&filename=d74fb7b4f29c4c978f46b450bde479b6.xls", "1125397419@qq.com", files);

//        emailSend("小孟", "花儿", "18866668888", "123456789", "wahaha", "1125397419@qq.com");
    }

}
