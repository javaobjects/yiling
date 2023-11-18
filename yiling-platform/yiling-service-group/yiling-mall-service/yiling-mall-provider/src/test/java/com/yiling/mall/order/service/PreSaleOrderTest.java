package com.yiling.mall.order.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.mall.BaseTest;
import com.yiling.mall.cart.dto.QuickBuyGoodsDTO;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.PresaleOrderSubmitRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2022/10/13
 */
@Slf4j
public class PreSaleOrderTest extends BaseTest {
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderProcessApi orderProcessApi;
    @Autowired
    private OrderRefundService orderRefundService;


    @Test
    public void add () {
        QuickBuyGoodsDTO quickBuyGoodsDTO = new QuickBuyGoodsDTO();
        quickBuyGoodsDTO.setDistributorEid(35l);
        quickBuyGoodsDTO.setDistributorGoodsId(3457l);
        quickBuyGoodsDTO.setGoodsSkuId(28l);
        quickBuyGoodsDTO.setGoodsId(3457l);
        quickBuyGoodsDTO.setQuantity(10);
        quickBuyGoodsDTO.setPlatformType(1);
        quickBuyGoodsDTO.setPromotionActivityType(PromotionActivityTypeEnum.PRESALE.getCode());
        quickBuyGoodsDTO.setPromotionActivityId(11l);

        redisService.set("order:presale:555", JSONUtil.toJsonStr(quickBuyGoodsDTO),10000);
    }

    @Test
    public void submit () {

        PresaleOrderSubmitRequest orderSubmitRequest = new PresaleOrderSubmitRequest();
        orderSubmitRequest.setPaymentMethod(4);
        orderSubmitRequest.setBuyerMessage("111");
        orderSubmitRequest.setOrderTypeEnum(OrderTypeEnum.B2B);
        orderSubmitRequest.setOrderSourceEnum(OrderSourceEnum.B2B_APP);
        orderSubmitRequest.setBuyerEid(54l);
        orderSubmitRequest.setAddressId(50l);
        orderSubmitRequest.setDistributorEid(35l);


        OrderSubmitBO orderSubmitBO = orderProcessApi.preSaleOrderSubmit(orderSubmitRequest);

        ThreadUtil.sleep(10000000);

        System.out.println(orderSubmitBO);
    }


    @Test
    public void refund () {
/*
        RefundOrderRequest refundOrderRequest = new RefundOrderRequest();
        refundOrderRequest.setOrderId(33113l);
        refundOrderRequest.setOrderNo("D20220919172345229375");
        refundOrderRequest.setReturnId(333l);
        refundOrderRequest.setReturnNo("aaabbbbccc");
        refundOrderRequest.setRefundType(1);
        refundOrderRequest.setPaymentAmount(new BigDecimal("11110.00"));
        refundOrderRequest.setSellerEid(35l);
        refundOrderRequest.setBuyerEid(54l);
        refundOrderRequest.setTotalAmount(new BigDecimal("11110.00"));
        refundOrderRequest.setRefundAmount(new BigDecimal("11110.00"));
        refundOrderRequest.setReason("测试取消");


        boolean refundOrder = orderRefundService.refundOrder(refundOrderRequest);

        System.out.println(refundOrder);*/

        ThreadUtil.sleep(1000000000);

    }
}
