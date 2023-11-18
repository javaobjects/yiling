package com.yiling.mall.coupon;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.BaseTest;
import com.yiling.mall.coupon.handler.CouponAutoGiveByOrderHandler;
import com.yiling.mall.coupon.listener.CouponAutoGiveByOrderMqListener;
import com.yiling.mall.strategy.handler.StrategyEndAutoJobHandler;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

/**
 * @author: houjie.sun
 * @date: 2021/11/25
 */
public class CouponServiceTest extends BaseTest {

    @Autowired
    private CouponAutoGiveByOrderHandler couponAutoGiveByOrderHandler;
    @Autowired
    CouponAutoGiveByOrderMqListener couponAutoGiveByOrderMqListener;

    @Autowired
    StrategyEndAutoJobHandler strategyEndAutoJobHandler;

    @Test
    public void testsss() {

    }

    @Test
    public void test2() {
        MessageExt messageExt = new MessageExt();
        messageExt.setBody("D20220708162848155336".getBytes(StandardCharsets.UTF_8));
        messageExt.setTags("topic_order_coupon_automatic_send");
        messageExt.setTopic("topic_order_coupon_automatic_send");
        couponAutoGiveByOrderMqListener.consume(messageExt, new ConsumeConcurrentlyContext(null));
    }

    @Test
    public void test() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNo("D20211221160941170363");
        orderDTO.setBuyerEid(272L);
        orderDTO.setId(2166L);
        orderDTO.setPaymentMethod(4);
        orderDTO.setOrderStatus(30);
        orderDTO.setOrderSource(3);

        EnterpriseDTO enterprise = new EnterpriseDTO();
        enterprise.setId(272L);
        enterprise.setName("华润医药连锁总店有限公司武汉分部");

        QueryCouponActivityGiveDetailRequest request = new QueryCouponActivityGiveDetailRequest();
        request.setType(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode());

        List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList = new ArrayList<>();
        List<BigDecimal> orderTotalAmountList = new ArrayList<>();
        /*String errorMsg = couponAutoGiveByOrderHandler.checkAccumulateAmountGiveRules(orderDTO, enterprise, request, autoGiveDetaiList,
                orderTotalAmountList);*/

        boolean isSuccess = couponAutoGiveByOrderHandler.orderDeliveryHandler(request, orderDTO, enterprise, autoGiveDetaiList, orderTotalAmountList);
    }
}
