package com.yiling.basic.mail.service;


import org.springframework.mail.javamail.JavaMailSender;

import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.mail.bo.MailConfigBO;

/**
 * @author shichen
 * @类名 MailService
 * @描述
 * @创建时间 2022/3/9
 * @修改人 shichen
 * @修改时间 2022/3/9
 **/
public interface MailService {
    public Boolean sendHtmlMail(MailConfigBO mailConfig, String mailId, JSONArray jsonArray);

    public Boolean sendHtmlMailBySelf(JavaMailSender javaMailSender, MailConfigBO mailConfig, String mailId, JSONArray jsonList);
}
