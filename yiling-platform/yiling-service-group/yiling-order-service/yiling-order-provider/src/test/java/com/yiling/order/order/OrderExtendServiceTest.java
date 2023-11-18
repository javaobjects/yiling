package com.yiling.order.order;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.BaseTest;
import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.service.OrderErpService;
import com.yiling.order.order.service.OrderExtendService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderExtendServiceTest extends BaseTest {
    @Autowired
    private OrderExtendService orderExtendService;

    @Test
    public void getB2BCountAmountTest() {
        QueryB2BOrderCountRequest request = new QueryB2BOrderCountRequest();
        request.setStandardIdList(Lists.newArrayList(27L));
        request.setOrderType(0);
        request.setStartTime(new Date());
        request.setEndTime(new Date());
        request.setVipFlag(0);
        request.setSellerEid(0L);
        request.setPaymentMethod(0);
        request.setProvinceCode("130001");
        request.setCityCode("130003");
        request.setRegionCode("1300035");
        request.setOrderSource(3);
        request.setType(0);
        OrderB2BCountAmountDTO b2BCountAmount = orderExtendService.getB2BCountAmount(request);
        System.out.println(JSON.toJSONString(b2BCountAmount));
    }

    @Test
    public void getB2BCountQuantityTest() {
        QueryB2BOrderCountRequest request = new QueryB2BOrderCountRequest();
        request.setStandardIdList(Lists.newArrayList(27L));
        request.setOrderType(0);
        request.setStartTime(new Date());
        request.setEndTime(new Date());
        request.setVipFlag(0);
        request.setSellerEid(0L);
        request.setPaymentMethod(0);
        request.setProvinceCode("130001");
        request.setCityCode("130003");
        request.setRegionCode("1300035");
        request.setOrderSource(3);
        request.setType(2);
        Integer b2BCountQuantity = orderExtendService.getB2BCountQuantity(request);
        System.out.println(b2BCountQuantity);
    }
}
