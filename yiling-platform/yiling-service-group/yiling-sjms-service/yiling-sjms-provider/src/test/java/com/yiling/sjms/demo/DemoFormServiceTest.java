package com.yiling.sjms.demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.demo.dto.request.ApproveDemoFormRequest;
import com.yiling.sjms.demo.dto.request.CreateDemoFormRequest;
import com.yiling.sjms.demo.dto.request.RejectDemoFormRequest;
import com.yiling.sjms.demo.dto.request.SubmitDemoFormRequest;
import com.yiling.sjms.demo.service.DemoFormService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Slf4j
public class DemoFormServiceTest extends BaseTest {

    @Autowired
    DemoFormService demoFormService;

    @Test
    public void create() {
        CreateDemoFormRequest request = new CreateDemoFormRequest();
        request.setName1("name1");
        request.setCode("GB-123");
        request.setName("团购提报-123");
        request.setType(1);
        request.setRemark("测试");
        request.setOpUserId(220L);

        Long formId = demoFormService.create(request);
        log.info("formId={}", formId);
    }

    @Test
    public void submit() {
        SubmitDemoFormRequest request = new SubmitDemoFormRequest();
        request.setName2("name2");
        request.setId(205L);
        request.setFlowTplId("tpl-id-123");
        request.setFlowTplName("tpl-name-123");
        request.setFlowVersion("tpl-version-123");
        request.setFlowId("instance-123");
        request.setOpUserId(220L);

        demoFormService.submit(request);
        log.info("submit over");
    }

    @Test
    public void approve() {
        ApproveDemoFormRequest request = new ApproveDemoFormRequest();
        request.setName3("name3");
        request.setId(205L);
        request.setOpUserId(220L);

        demoFormService.approve(request);
        log.info("approve over");
    }

    @Test
    public void reject() {
        RejectDemoFormRequest request = new RejectDemoFormRequest();
        request.setName4("name4");
        request.setId(205L);
        request.setRejectReason("不知道");
        request.setOpUserId(220L);

        demoFormService.reject(request);
        log.info("reject over");
    }

    @Test
    public void delete() {
        demoFormService.delete(205L, 220L);
        log.info("delete over");
    }
}
