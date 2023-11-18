package com.yiling.basic.mail.service.impl;


import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.mail.bo.MailConfigBO;
import com.yiling.basic.mail.config.MailConfig;
import com.yiling.basic.mail.enums.MailEnum;
import com.yiling.basic.mail.enums.MailSendStatusEnum;
import com.yiling.basic.mail.service.MailSendRecordService;
import com.yiling.basic.mail.service.MailService;
import com.yiling.basic.mail.util.MailUtils;
import com.yiling.basic.sms.config.SmsConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 MailServiceImpl
 * @描述
 * @创建时间 2022/3/9
 * @修改人 shichen
 * @修改时间 2022/3/9
 **/
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private MailSendRecordService mailSendRecordService;

    @Autowired
    MailConfig mailConfig;

    @Override
    public Boolean sendHtmlMail(MailConfigBO mailBO, String mailId, JSONArray jsonArray) {
        return this.sendHtmlMailBySelf(javaMailSender, mailBO,mailId,jsonArray);
    }

    @Override
    public Boolean sendHtmlMailBySelf(JavaMailSender javaMailSender, MailConfigBO mailBO, String mailId, JSONArray jsonArray) {
        MailSendStatusEnum statusEnum = MailSendStatusEnum.SUCCESS;
        String sendMsg=MailSendStatusEnum.SUCCESS.getMessage();
        Boolean sendFlag=true;
        try {
            mailBO.setFrom(mailConfig.getUsername());
            MailUtils.sendMail(mailBO,javaMailSender);
        }catch (Exception e){
            log.error(e.getMessage());
            sendMsg=e.getMessage();
            statusEnum = MailSendStatusEnum.FAIL;
            sendFlag=false;
        }
        if(StringUtils.isBlank(mailId)){
            mailId = UUID.randomUUID().toString().replace("-", "");
        }
        mailSendRecordService.saveSendMail(MailEnum.ORDER_PUSH_FAIL,statusEnum,jsonArray,sendMsg,mailId);
        return sendFlag;
    }
}
