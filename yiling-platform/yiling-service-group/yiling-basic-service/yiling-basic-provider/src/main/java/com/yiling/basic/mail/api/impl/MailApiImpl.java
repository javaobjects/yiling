package com.yiling.basic.mail.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.mail.api.MailApi;
import com.yiling.basic.mail.bo.MailConfigBO;
import com.yiling.basic.mail.enums.MailEnum;
import com.yiling.basic.mail.service.MailSendRecordService;
import com.yiling.basic.mail.service.MailService;
import com.yiling.framework.common.base.BaseDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 MailApiImpl
 * @描述
 * @创建时间 2022/3/9
 * @修改人 shichen
 * @修改时间 2022/3/9
 **/
@DubboService
@Slf4j
public class MailApiImpl implements MailApi {
    @Autowired
    private MailSendRecordService mailSendRecordService;

    @Autowired
    private MailService mailService;

    @Override
    public Boolean sendHtmlMail(MailConfigBO mailConfig, String mailId, JSONArray jsonArray) {
        return mailService.sendHtmlMail(mailConfig,mailId,jsonArray);
    }

    @Override
    public List<Long> getSendSuccessBusinessIds(MailEnum mailEnum, List<Long> businessIds) {
        return mailSendRecordService.getSendSuccessBusinessIds(mailEnum,businessIds);
    }
}
