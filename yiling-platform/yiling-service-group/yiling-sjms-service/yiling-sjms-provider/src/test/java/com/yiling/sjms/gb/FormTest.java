package com.yiling.sjms.gb;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.form.enums.FormStatusEnum;
import com.yiling.sjms.form.service.FormService;
import com.yiling.sjms.gb.dto.request.UpdateGBFormInfoRequest;
import com.yiling.sjms.gb.service.GbFormService;

import lombok.extern.slf4j.Slf4j;

/**
 * DEMO TEST
 *
 * @author xuan.zhou
 * @date 2022/5/12
 */
@Slf4j
public class FormTest extends BaseTest {


    @Autowired
    GbFormService gbFormService;
    @Autowired
    FormService formService;

    @Test
    public void  updateGBFormById(){
        UpdateGBFormInfoRequest request = new UpdateGBFormInfoRequest();
        request.setId(332L);
        request.setOriginalStatus(FormStatusEnum.UNSUBMIT);
        request.setNewStatus(FormStatusEnum.AUDITING);
        gbFormService.updateStatusById(request);
    }



}
