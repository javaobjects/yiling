package com.yiling.open.third;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.third.service.FlowInterfaceConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/12
 */
@Slf4j
public class InterFaceServiceTest extends BaseTest {

    @Autowired
    private FlowInterfaceConfigService flowInterfaceConfigService;

    @Test
    public void Test1() throws Exception {
        flowInterfaceConfigService.executeFlowInterface();
    }

}
