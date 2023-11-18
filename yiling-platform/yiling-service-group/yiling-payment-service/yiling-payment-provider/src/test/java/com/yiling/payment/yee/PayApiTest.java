package com.yiling.payment.yee;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.payment.BaseTest;
import com.yiling.payment.channel.bocom.BocomPayService;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.enums.PaySourceEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.yee
 * @date: 2021/12/31
 */

@Slf4j
public class PayApiTest extends BaseTest {

    @Autowired
    private BocomPayService bocomPayService;



    @Test
    public void createPayTradeTest() {

        YeePayOrder order = new YeePayOrder();
        order.setUserIp("192.168.1.1");

        bocomPayService.createPreOrder(order,PaySourceEnum.BOCOM_PAY_WECHAT);

    }
}

