package com.yiling.mall.order.service;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.mall.BaseTest;
import com.yiling.mall.order.dto.request.RefundOrderRequest;
import com.yiling.order.order.enums.RefundSourceEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2022/4/14
 */
@Slf4j
public class MemberOrderRefundTest extends BaseTest {

    @Autowired
    private OrderRefundService orderRefundService;


    @Test
    public void  saveRecord() {

        RefundOrderRequest request = new RefundOrderRequest();
        request.setOrderId(1092l);
        request.setReturnId(1l);
        request.setReturnNo("0004");
        request.setOrderNo("D20220304103422204699");
        request.setRefundType(1);
        request.setRefundAmount(new BigDecimal("0.01"));
        request.setReason("取消订单");
        request.setOpUserId(0l);
        request.setOpTime(new Date());
        request.setRefundSource(RefundSourceEnum.MEMBER.getCode());

        boolean result = orderRefundService.refundOrder(request);

        System.out.println(result + "==================");
    }
}
