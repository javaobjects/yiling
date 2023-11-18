package com.yiling.basic.mail.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;
import com.yiling.basic.mail.bo.MailConfigBO;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/11/14
 */
@Slf4j
public class MailServiceTest extends BaseTest {

    @Autowired
    MailService mailService;

    @Test
    public void sendMail() {
        while (true) {
            this.send();
            ThreadUtil.sleep(10000);
        }
    }

    private void send() {
        MailConfigBO mailConfigBO = new MailConfigBO();
        mailConfigBO.setFrom("shichen@yiling.cn");
        mailConfigBO.setFromName("石晨");
        mailConfigBO.setTo(new String[]{ "zhouxuan@yiling.cn" });
        mailConfigBO.setSubject("测试");
        mailConfigBO.setText("测试");
        mailConfigBO.setHtmlFlag(false);

        boolean result = mailService.sendHtmlMail(mailConfigBO, IdUtil.fastUUID(), null);
        log.info("result = {}", result);
    }
}
