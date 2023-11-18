package com.yiling.mall.order.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yiling.mall.BaseTest;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.mall.order.api.SaOrderApi;
import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.OrderConfirmRequest;
import com.yiling.mall.order.dto.request.OrderSubmitRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

import cn.hutool.core.collection.ListUtil;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.service
 * @date: 2022/1/10
 */
public class OrderSubmitServiceTest extends BaseTest {

    @Autowired
    private OrderProcessApi orderProcessApi;

    @Autowired
    private SaOrderApi saOrderApi;





    @Test
    public void testOrderSubmit() {

        OrderSubmitRequest request = new OrderSubmitRequest();
        request.setOpUserId(91l);
        request.setOpTime(new Date());
        request.setPlatformCustomerCouponId(null);
        request.setOrderTypeEnum(OrderTypeEnum.B2B);
        request.setOrderSourceEnum(OrderSourceEnum.B2B_APP);
        request.setBuyerEid(54l);
        request.setContacterId(72l);
        request.setAddressId(50l);
        request.setDepartmentId(null);


        List<OrderSubmitRequest.DistributorOrderDTO> distributorOrderDTOList = Lists.newArrayList();


        OrderSubmitRequest.DistributorOrderDTO dto = new OrderSubmitRequest.DistributorOrderDTO();
        dto.setDistributorEid(35l);
        dto.setBuyerMessage("test");
        dto.setPaymentMethod(4);
        dto.setPaymentType(1);
        dto.setContractNumber("12324");
        dto.setContractFileKeyList(Collections.singletonList("/abc/ttt.jpg"));
        dto.setCartIds(ListUtil.toList(573l,574l));

        distributorOrderDTOList.add(dto);
        request.setDistributorOrderList(distributorOrderDTOList);

        OrderSubmitBO submitBO = orderProcessApi.submit(request);

        System.out.println(JSON.toJSONString(submitBO));

    }


    @Test
    public void conifrmB2bOrder() {

        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setBuyerEid(222815l);
        confirmRequest.setOpUserId(0l);
        confirmRequest.setCancelOrderIds(ListUtil.empty());
        List<OrderConfirmRequest.DistributorOrderDTO> distributorOrderList = Lists.newArrayList();

        OrderConfirmRequest.DistributorOrderDTO distributorOrderDTO = new OrderConfirmRequest.DistributorOrderDTO();
        distributorOrderDTO.setOrderId(8043l);
        distributorOrderDTO.setOrderNo("D20220927145507418510");
        distributorOrderDTO.setPaymentMethod(4);
        distributorOrderDTO.setBuyerMessage("1111");
        distributorOrderDTO.setSellerEid(222804l);
        distributorOrderDTO.setDistributorEid(222804l);

        distributorOrderList.add(distributorOrderDTO);



        confirmRequest.setPlatformCustomerCouponId(11241l);
        confirmRequest.setDistributorOrderList(distributorOrderList);


        OrderSubmitBO orderSubmitBO = saOrderApi.b2bConfirmCustomerOrder(confirmRequest);

        System.out.println(orderSubmitBO + "==================");

    }


}
