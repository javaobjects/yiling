package com.yiling.order.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderInvoicePullErpDTO;
import com.yiling.order.order.dto.request.OrderPullErpPageRequest;
import com.yiling.order.order.service.OrderInvoiceApplyService;
import com.yiling.order.order.service.OrderService;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.BaseTest;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.request.CompleteErpOrderInvoiceRequest;
import com.yiling.order.order.service.OrderErpService;
import com.yiling.order.order.service.OrderService;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:wei.wang
 * @date:2021/8/13
 */
@Slf4j
public class OrderErpServiceTest extends BaseTest {
    @Autowired
    private OrderErpService orderErpService;
    @Autowired
    private OrderService    orderService;
    @Autowired
    private OrderInvoiceApplyService orderInvoiceApplyService;

    @Test
    public void completeErpOrderInvoiceTest() {
        List<CompleteErpOrderInvoiceRequest> request = new ArrayList<>();
        CompleteErpOrderInvoiceRequest one = new CompleteErpOrderInvoiceRequest();
        one.setInvoiceNo("00054099");
        one.setErpReceivableNo("AR-01-202209-0030");
        one.setInvoiceAmount(BigDecimal.valueOf(22.72));
        request.add(one);
        /*CompleteErpOrderInvoiceRequest one1 = new CompleteErpOrderInvoiceRequest();
        one1.setInvoiceNo("6981557");
        one1.setErpReceivableNo("ceww202206MMA-2");
        one1.setInvoiceAmount(BigDecimal.valueOf(20.01));
        request.add(one1);
        CompleteErpOrderInvoiceRequest one2 = new CompleteErpOrderInvoiceRequest();
        one2.setInvoiceNo("6981517");
        one2.setErpReceivableNo("ceww2022012B-2");
        one2.setInvoiceAmount(BigDecimal.valueOf(33.01));
        request.add(one2);*/
        orderErpService.completeErpOrderInvoice(request);
    }

    @Test
    public void cancelErpOrderInvoiceTest() {
        List<String> list = new ArrayList<>();
        list.add("A78795");
        list.add("A78796");
        orderErpService.cancelErpOrderInvoice(list);
    }

    @Test
    public void getOrderByErpReceivableNoTest() {

        OrderDTO orderByErpReceivableNo = orderService.getOrderByErpReceivableNo("AR-01-202112-0009");
        System.out.println(orderByErpReceivableNo);
    }
    @Test
    public void test1(){
        orderService.updateCustomerErpCode(9L,6L,"517--22");
    }

    @Test
    public void updateErpReceivableNoByDeliveryNoTest() {

        Map<String, Object> map = orderErpService.updateErpReceivableNoByDeliveryNo("ceww2022012B-1", "ceww2022012B-1");
        System.out.println("数据展示"+JSON.toJSONString(map));
    }

    @Test
    public void removeErpReceivableNoTest() {

        Map<String, Object> map = orderErpService.removeErpReceivableNo("AR-01-202209-0030");
        System.out.println("数据展示"+JSON.toJSONString(map));
    }

    @Test
    public void getErpPullOrderInvoiceTest(){
        OrderPullErpPageRequest request = new OrderPullErpPageRequest();
        List<Long> eid = new ArrayList<>();
        eid.add(3l);
        request.setSellerEids(eid);
        request.setStartCreateTime(DateUtil.parse("2022-05-01"));
        request.setEndCreateTime(DateUtil.parse("2022-06-20"));

        Page<OrderInvoicePullErpDTO> erpPullOrderInvoice = orderInvoiceApplyService.getErpPullOrderInvoice(request);
        System.out.println(JSON.toJSONString(erpPullOrderInvoice));
    }
}
