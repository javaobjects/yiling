package com.yiling.open.mail;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.yiling.open.BaseTest;

import com.yiling.open.mail.service.ErpOrderMailService;


/**
 * @author shichen
 * @类名 MailTest
 * @描述
 * @创建时间 2022/3/9
 * @修改人 shichen
 * @修改时间 2022/3/9
 **/
public class MailTest extends BaseTest {
    @Autowired
    private ErpOrderMailService erpOrderMailService;

    @Test
    public void sendTest(){

        erpOrderMailService.sendMailByPushOrderFail(Lists.newArrayList(33070L));
    }
}
