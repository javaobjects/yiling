package com.yiling.open.thirdbase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.thirdbase.service.ErpBaseInterfaceConfigService;

/**
 * @author: houjie.sun
 * @date: 2023/6/20
 */
public class ErpBaseInterfaceConfigTest extends BaseTest {

    @Autowired
    private ErpBaseInterfaceConfigService erpBaseInterfaceConfigService;

    @Test
    public void queryBaseInfoHandlerTest() {
        erpBaseInterfaceConfigService.executeBaseInterface();
    }
}
