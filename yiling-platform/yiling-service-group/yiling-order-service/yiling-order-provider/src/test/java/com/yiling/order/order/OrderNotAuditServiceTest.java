package com.yiling.order.order;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.pojo.Result;
import com.yiling.order.BaseTest;
import com.yiling.order.order.bo.OrderModifyAuditChangeBO;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.service.OrderModifyAuditService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @date: 2021/8/6
 */
@Slf4j
public class OrderNotAuditServiceTest extends BaseTest {

    @Autowired
    private OrderModifyAuditService modifyOrderAuditService;

    /*@Test
    public void test () {

        UpdateOrderNotAuditRequest orderNotAuditRequest = new UpdateOrderNotAuditRequest();
        orderNotAuditRequest.setOrderId(124l);
        orderNotAuditRequest.setDeliveryNo("SF00001");
        orderNotAuditRequest.setDeliveryType(1);
        orderNotAuditRequest.setDeliveryCompany("以岭药业");

        List<SaveOrderDeliveryInfoRequest> orderDeliveryGoodsInfoList = Lists.newArrayList();

        SaveOrderDeliveryInfoRequest saveOrderDeliveryInfoRequest = new SaveOrderDeliveryInfoRequest();
        saveOrderDeliveryInfoRequest.setDetailId(151l);
        saveOrderDeliveryInfoRequest.setGoodsId(3338l);

        List<DeliveryInfoRequest> deliveryInfoList = Lists.newArrayList();
        DeliveryInfoRequest infoRequest = new DeliveryInfoRequest();
        infoRequest.setBatchNo("B2006002");
        infoRequest.setExpiryDate(new Date());
        infoRequest.setDeliveryQuantity(1);




        deliveryInfoList.add(infoRequest);

        saveOrderDeliveryInfoRequest.setDeliveryInfoList(deliveryInfoList);

        SaveOrderDeliveryInfoRequest saveOrderDeliveryInfoRequest1 = new SaveOrderDeliveryInfoRequest();
        saveOrderDeliveryInfoRequest1.setDetailId(152l);
        saveOrderDeliveryInfoRequest1.setGoodsId(3339l);

        List<DeliveryInfoRequest> deliveryInfoList1 = Lists.newArrayList();
        DeliveryInfoRequest infoRequest1 = new DeliveryInfoRequest();
        infoRequest1.setBatchNo("A1908096");
        infoRequest1.setExpiryDate(new Date());
        infoRequest1.setDeliveryQuantity(1);
        deliveryInfoList1.add(infoRequest1);

        saveOrderDeliveryInfoRequest1.setDeliveryInfoList(deliveryInfoList1);
        orderDeliveryGoodsInfoList.add(saveOrderDeliveryInfoRequest);
        orderDeliveryGoodsInfoList.add(saveOrderDeliveryInfoRequest1);

        orderNotAuditRequest.setOrderDeliveryGoodsInfoList(orderDeliveryGoodsInfoList);

        Result<OrderModifyAuditChangeBO> result = null;

        try {

            result = modifyOrderAuditService.modifyOrderNotAudit(orderNotAuditRequest);

            Thread.currentThread().wait(1000000000);
        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println("===========" + JSON.toJSON(result));

    }*/



    @Test
    public void test_v2() {
        ModifyOrderNotAuditRequest request = new ModifyOrderNotAuditRequest();
        request.setOrderId(384l);
        request.setErpDeliveryNo("XSCK-01-202108-1604");
        request.setBatchNo("B2002245");
        Result<OrderModifyAuditChangeBO>  result = modifyOrderAuditService.modifyOrderNotAudit_v2(request);

        System.out.println(JSON.toJSON(request) + "========test_v2================================");

    }

}
