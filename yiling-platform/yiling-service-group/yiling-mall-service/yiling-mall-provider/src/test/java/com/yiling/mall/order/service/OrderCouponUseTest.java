package com.yiling.mall.order.service;

import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.BaseTest;
import com.yiling.mall.order.dto.request.OrderCouponUseReturnRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2021/8/6
 */
@Slf4j
public class OrderCouponUseTest extends BaseTest {

    @Autowired
    private OrderCouponUseService orderCouponUseService;


    @Test
    public void test () {
        OrderCouponUseReturnRequest request = new OrderCouponUseReturnRequest();
        request.setCouponIdList(Arrays.asList(332L));
        request.setOpUserId(1L);
        orderCouponUseService.orderReturnCoupon(request);

    }

}
