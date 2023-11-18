package com.yiling.open.erp;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.BaseTest;
import com.yiling.open.erp.service.ErpOrderPushService;

/**
 * @author: houjie.sun
 * @date: 2021/9/2
 */
public class ErpOrderPushTest extends BaseTest {

    @Autowired
    private ErpOrderPushService erpOrderPushService;

    @Test
    public void ErpOrderPushTest1() {
        erpOrderPushService.erpOrderPush(Arrays.asList(148L));
    }

    @Test
    public void ErpOrderPushTest2(){
        erpOrderPushService.getOrderPurchaseSendBySuId(1235L,5);
    }
}
