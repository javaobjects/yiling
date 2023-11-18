package com.yiling.sjms.monthflow;

import java.util.Objects;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.flow.dto.MonthFlowFormDTO;
import com.yiling.sjms.flow.service.MonthFlowFormService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2023/6/29
 */
@Slf4j
public class MonthFlowFormServiceTest extends BaseTest {
    @Autowired
    private MonthFlowFormService monthFlowFormService;
    @Test
    public  void test1(){
        monthFlowFormService.approveTo(538L);
    }

}