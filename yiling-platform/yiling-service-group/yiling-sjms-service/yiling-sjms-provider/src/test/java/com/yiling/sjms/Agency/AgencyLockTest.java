package com.yiling.sjms.Agency;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.agency.dto.request.ApproveAgencyLockFormRequest;
import com.yiling.sjms.agency.service.AgencyLockFormService;

/**
 * @author: gxl
 * @date: 2023/3/6
 */
public class AgencyLockTest extends BaseTest {
    @Autowired
    private AgencyLockFormService agencyLockFormService;

    @Test
    public void test1(){
        ApproveAgencyLockFormRequest request = new ApproveAgencyLockFormRequest();
        //request.setId(1);
        agencyLockFormService.approveToChange(request);
    }
}