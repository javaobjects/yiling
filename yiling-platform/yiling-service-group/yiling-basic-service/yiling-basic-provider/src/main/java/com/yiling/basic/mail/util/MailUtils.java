package com.yiling.basic.mail.util;

import com.yiling.basic.mail.bo.MailConfigBO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * @author shichen
 * @类名 MailUtils
 * @描述
 * @创建时间 2022/2/9
 * @修改人 shichen
 * @修改时间 2022/2/9
 **/
public class MailUtils {

    public static void sendMail(MailConfigBO mail, JavaMailSender javaMailSender)throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String from;
        if(StringUtils.isNotBlank(mail.getFromName())){
            from = new InternetAddress(MimeUtility.encodeText(mail.getFromName())+"<"+mail.getFrom()+">").toString();
        }else {
            from =mail.getFrom();
        }
        helper.setFrom(from);
        helper.setTo(mail.getTo());
        helper.setSubject(mail.getSubject());
        helper.setText(mail.getText(), mail.getHtmlFlag());
        javaMailSender.send(message);
    }
}
