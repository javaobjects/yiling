package com.yiling.order.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.yiling.order.BaseTest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderPromotionActivityApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.bo.CombinationBuyNumberBO;
import com.yiling.order.order.dto.OrderDeliveryReportCountDTO;
import com.yiling.order.order.dto.OrderNumberDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.dto.request.QueryBackOrderInfoRequest;
import com.yiling.order.order.dto.request.QueryOrderDeliveryReportRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.service.OrderService;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author:wei.wang
 * @date:2022/1/11
 */
@Slf4j
public class OrderServiceTest extends BaseTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PresaleOrderApi presaleOrderApi;

    @Autowired
    private OrderApi  orderApi;

    @Autowired
    private OrderPromotionActivityApi orderPromotionActivityApi;

    @Test
    public void shareAllDeliveryErpCashAmountTest() {
        Boolean aBoolean = orderService.shareAllDeliveryErpCashAmount();
        System.out.println("aBoolean");
    }

    @Test
    public void getOrderDeliveryReportCountTest() {
        QueryOrderDeliveryReportRequest reportRequest = new QueryOrderDeliveryReportRequest();
        reportRequest.setStartDeliverTime(DateUtil.parse("2022-04-01"));
        reportRequest.setEndDeliverTime(DateUtil.parse("2022-04-28"));
        List<Long> list = new ArrayList<>();
        list.add(74L);
        reportRequest.setEidList(list);

        List<OrderDeliveryReportCountDTO> orderDeliveryReportCount = orderService.getOrderDeliveryReportCount(reportRequest);
        System.out.println(JSON.toJSONString(orderDeliveryReportCount));
    }

    @Test
    public  void getCentreOrderNumberCountTest(){
        QueryBackOrderInfoRequest request = new QueryBackOrderInfoRequest();

        request.setStartCreateTime(DateUtil.parse("2022-05-01"));
        request.setEndCreateTime(DateUtil.parse("2022-05-31"));
        request.setStartDeliveryTime(DateUtil.parse("2022-05-01"));
        request.setEndDeliveryTime(DateUtil.parse("2022-05-31"));
        List<Long> list = new ArrayList<>();
        list.add(22L); list.add(23L);
        request.setBuyerEid(list);
        request.setOrderSource(1);
        request.setOrderType(1);

        OrderNumberDTO centreOrderNumberCount = orderService.getCentreOrderNumberCount(request);
        System.out.println(centreOrderNumberCount);
    }

    @Test
    public  void verificationReceiveB2BOrderTest(){
        //Boolean aBoolean = orderService.verificationReceiveB2BOrder();
        //System.out.println(aBoolean);
    }

    @Test
    public void presaleOrderListTest() {

        List<PresaleOrderDTO> d20221017160830548325 = presaleOrderApi.listByOrderNos(Collections.singletonList("D20221017160830548325"));

        System.out.println(d20221017160830548325);

    }


    @Test
    public void getUserCountNumber() {

        QueryUserBuyNumberRequest request = new QueryUserBuyNumberRequest();
        request.setBuyerEid(57l);
        request.setGoodId(3303l);
        request.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.MONTH);


        QueryUserBuyNumberRequest request1 = new QueryUserBuyNumberRequest();
        request1.setBuyerEid(57l);
        request1.setGoodId(3457l);
        request1.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.WEEK);

        QueryUserBuyNumberRequest request2 = new QueryUserBuyNumberRequest();
        request2.setBuyerEid(57l);
        request2.setGoodId(3456l);
        request2.setStartTime(DateUtil.offsetDay(new Date(),-2));
        request2.setEndTime(DateUtil.offsetDay(new Date(),2));
        request2.setSelectRuleEnum(QueryUserBuyNumberRequest.SelectRuleEnum.CUSTOMIZE);


        Map<Long,Long> userBuyNumberMap = orderApi.getUserBuyNumber(ListUtil.toList(request,request1,request2));


        System.out.println(userBuyNumberMap + "========");
    }


    @Test
    public void combinationBuyNumberQtyTest() {

        CombinationBuyNumberBO bo = orderPromotionActivityApi.sumCombinationActivityNumber(54l, 84l);

        System.out.println("=================" + JSON.toJSONString(bo));
    }

}
