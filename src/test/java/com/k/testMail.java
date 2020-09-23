package com.k;

import com.k.es.util.MailUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/9/23 9:55
 **/
public class testMail {
    public static void main(String[] args) throws AddressException, MessagingException {

        MailUtils.sendMail("mrk9@vip.qq.com ", "9527", "去吉野家吗？");

    }
}
