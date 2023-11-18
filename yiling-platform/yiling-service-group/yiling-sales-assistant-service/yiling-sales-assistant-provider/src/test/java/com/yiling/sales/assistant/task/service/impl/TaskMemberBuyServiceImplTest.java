package com.yiling.sales.assistant.task.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.sales.assistant.BaseTest;
import com.yiling.sales.assistant.task.dto.request.app.InviteTaskMemberRequest;
import com.yiling.sales.assistant.task.service.TaskMemberBuyService;

/**
 * @author: ray
 * @date: 2021/12/29
 */
class TaskMemberBuyServiceImplTest extends BaseTest {


    @Autowired
    private TaskMemberBuyService taskMemberBuyService;

    @Test
    void generateOssKey() {
        InviteTaskMemberRequest inviteTaskMemberRequest = new InviteTaskMemberRequest();

        FileInfo url =  taskMemberBuyService.generateQrCode("https://www.baidu.com/","二维码.jpg");
        System.out.println(url.getUrl());
    }
}