package com.yiling.order.order;

import java.util.List;

import com.yiling.order.order.dto.OrderHistoryGoodsDTO;
import com.yiling.order.order.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.BaseTest;
import com.yiling.order.order.bo.OrderDetailChangeBO;
import com.yiling.order.order.service.OrderDetailChangeService;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/8/4
 */
@Slf4j
public class OrderDetailChangeServiceTest extends BaseTest {

    @Autowired
    private OrderDetailChangeService orderDetailChangeService;
    @Autowired
    private OrderService orderService;


    @Test
    public void updateDeliveryData() {
        orderDetailChangeService.updateDeliveryData(1L, 100);
    }

    @Test
    public void updateReceiveData() {
        orderDetailChangeService.updateReceiveData(1L, 90);
    }

    @Test
    public void updateReturnData() {
        OrderDetailChangeBO orderDetailChangeBO = orderDetailChangeService.updateReturnData(1L, 90, true);
        log.info("orderDetailChangeBO = {}", JSONUtil.toJsonStr(orderDetailChangeBO));
    }

}
