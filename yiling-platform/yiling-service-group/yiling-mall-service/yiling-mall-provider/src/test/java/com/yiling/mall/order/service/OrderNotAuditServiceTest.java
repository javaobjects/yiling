package com.yiling.mall.order.service;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yiling.framework.common.pojo.Result;
import com.yiling.mall.BaseTest;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.dto.request.DeliveryInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2021/8/6
 */
@Slf4j
public class OrderNotAuditServiceTest extends BaseTest {

    @Autowired
    private OrderProcessApi orderProcessApi;


    @Test
    public void test () {

        UpdateOrderNotAuditRequest orderNotAuditRequest = new UpdateOrderNotAuditRequest();
        orderNotAuditRequest.setOrderId(141l);
        orderNotAuditRequest.setDeliveryNo("SF00001");
        orderNotAuditRequest.setDeliveryType(1);
        orderNotAuditRequest.setDeliveryCompany("以岭药业");
        //orderNotAuditRequest.setErpDeliveryNo("XSCK-01-202108-1530");

        List<SaveOrderDeliveryInfoRequest> orderDeliveryGoodsInfoList = Lists.newArrayList();

        SaveOrderDeliveryInfoRequest saveOrderDeliveryInfoRequest = new SaveOrderDeliveryInfoRequest();
        saveOrderDeliveryInfoRequest.setDetailId(173l);
        saveOrderDeliveryInfoRequest.setGoodsId(3339l);

        List<DeliveryInfoRequest> deliveryInfoList = Lists.newArrayList();
        DeliveryInfoRequest infoRequest = new DeliveryInfoRequest();
        infoRequest.setBatchNo("A2101109");
        infoRequest.setExpiryDate(new Date());
        infoRequest.setDeliveryQuantity(4);

        deliveryInfoList.add(infoRequest);
        saveOrderDeliveryInfoRequest.setDeliveryInfoList(deliveryInfoList);
        orderDeliveryGoodsInfoList.add(saveOrderDeliveryInfoRequest);

        SaveOrderDeliveryInfoRequest saveOrderDeliveryInfoRequest1 = new SaveOrderDeliveryInfoRequest();
        saveOrderDeliveryInfoRequest1.setDetailId(152l);
        saveOrderDeliveryInfoRequest1.setGoodsId(3339l);

        List<DeliveryInfoRequest> deliveryInfoList1 = Lists.newArrayList();
        DeliveryInfoRequest infoRequest1 = new DeliveryInfoRequest();
        infoRequest1.setBatchNo("A1908096");
        infoRequest1.setExpiryDate(new Date());
        infoRequest1.setDeliveryQuantity(4);
        deliveryInfoList1.add(infoRequest1);

        saveOrderDeliveryInfoRequest1.setDeliveryInfoList(deliveryInfoList1);
        orderDeliveryGoodsInfoList.add(saveOrderDeliveryInfoRequest);
        orderDeliveryGoodsInfoList.add(saveOrderDeliveryInfoRequest1);

        orderNotAuditRequest.setOrderDeliveryGoodsInfoList(orderDeliveryGoodsInfoList);
        orderNotAuditRequest.setOrderDeliveryGoodsInfoList(orderDeliveryGoodsInfoList);

        Result<Boolean> result = null;

        try {

            result = orderProcessApi.modifyOrderNotAudit(orderNotAuditRequest);

            Thread.currentThread().wait(1000000000);

        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println("===========" + JSON.toJSON(result));
    }

}
