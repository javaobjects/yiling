package com.yiling.sjms.sjsp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.agency.dto.request.SubmitAgencyFormRequest;
import com.yiling.sjms.agency.service.AgencyFormService;
import com.yiling.sjms.demo.dto.request.ApproveDemoFormRequest;
import com.yiling.sjms.demo.dto.request.CreateDemoFormRequest;
import com.yiling.sjms.demo.dto.request.RejectDemoFormRequest;
import com.yiling.sjms.demo.dto.request.SubmitDemoFormRequest;
import com.yiling.sjms.demo.service.DemoFormService;
import com.yiling.sjms.form.enums.FormTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/2/24
 */
@Slf4j
public class SjspTest extends BaseTest {

    @Autowired
    AgencyFormService agencyFormService;


    @Test
    public void submit() {
        SubmitAgencyFormRequest request = new SubmitAgencyFormRequest();
        request.setFormId(268L);
        request.setEmpId("YX06158");
        request.setFormTypeEnum(FormTypeEnum.ENTERPRISE_ADD);
        agencyFormService.submit(request);
        log.info("...");
    }


}
