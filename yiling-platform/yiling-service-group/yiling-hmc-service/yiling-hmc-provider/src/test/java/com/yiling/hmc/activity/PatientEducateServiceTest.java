package com.yiling.hmc.activity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.QueryActivityPatientEducateRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityPatientEducationRequest;
import com.yiling.hmc.activity.entity.ActivityPatientEducateDO;
import com.yiling.hmc.activity.service.ActivityPatientEducateService;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: gxl
 * @date: 2022/4/8
 */
public class PatientEducateServiceTest extends BaseTest {

    @Autowired
    ActivityPatientEducateService service;

    @Test
    public void getById() {
        ActivityPatientEducateDTO dto = service.queryById(1L);
        System.out.println(dto);
    }

    @Test
    public void pageList() {
        QueryActivityPatientEducateRequest request = new QueryActivityPatientEducateRequest();
        request.setActivityName("te");
        Page<ActivityPatientEducateDTO> result = service.pageList(request);
        System.out.println(result.getRecords());
    }

    @Test
    public void save() {
        SaveActivityPatientEducationRequest request = new SaveActivityPatientEducationRequest();
        request.setActivityName("test");
        request.setBeginTime(new Date());
        request.setEndTime(new Date());
        request.setActivityDesc("this is test activity");
        request.setOpUserId(0L);
        request.setOpTime(new Date());
        ActivityPatientEducateDTO dto = service.saveActivityPatientEducate(request);
        System.out.println(dto);
    }
}