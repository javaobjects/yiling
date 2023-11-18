package com.yiling.hmc.order;

import cn.hutool.core.date.DateUtil;
import com.yiling.hmc.order.service.MarketOrderService;
import com.yiling.hmc.wechat.dto.request.MarketOrderPayNotifyRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.insurance.dto.request.ConfirmTookRequest;
import com.yiling.hmc.order.enums.HmcCreateSourceEnum;
import com.yiling.hmc.order.enums.HmcDeliveryTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcOrderTypeEnum;
import com.yiling.hmc.order.enums.HmcPaymentMethodEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.hmc.settlement.service.InsuranceSettlementService;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;
import com.yiling.hmc.wechat.service.InsuranceRecordPayService;

import java.math.BigDecimal;

/**
 * @author: fan.shen
 * @date: 2022/7/7
 */
public class OrderServiceTest extends BaseTest {

    @Autowired
    private InsuranceRecordPayService insuranceRecordPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FileService fileService;


    @Autowired
    private InsuranceSettlementService insuranceSettlementService;

    @Autowired
    MarketOrderService marketOrderService;

    @Test
    public void test2() {
        MarketOrderPayNotifyRequest request = new MarketOrderPayNotifyRequest();
        request.setOrderId(4L);
        request.setPayTime(DateUtil.date());
        request.setThirdPayNo("123");
        request.setThirdPayAmount(BigDecimal.valueOf(3580));

        Boolean aBoolean = marketOrderService.payNotify(request);
        System.out.println(aBoolean);
    }

    @Test
    public void getUrl() {
        String idCardFrontPhoto = fileService.getUrl("dev/idCardFrontPhoto/2022/07/08/a0078792ef5248e4b21be918520f9954.jpg", FileTypeEnum.ID_CARD_FRONT_PHOTO);
        System.out.println(idCardFrontPhoto);
    }

    @Test
    public void testTrial() {

        Long recordPayId = 97L;
        Long currentUserId = 1L;

        InsuranceRecordPayDTO payDTO = insuranceRecordPayService.queryById(recordPayId);

        OrderSubmitRequest request = new OrderSubmitRequest();
        request.setInsuranceRecordId(payDTO.getInsuranceRecordId());
        request.setUserId(currentUserId);
        request.setOpUserId(currentUserId);

        request.setHmcOrderStatus(HmcOrderStatusEnum.UN_PAY);
        request.setPaymentStatusEnum(HmcPaymentStatusEnum.UN_PAY);
        request.setPaymentMethodEnum(HmcPaymentMethodEnum.INSURANCE_PAY);
        request.setPaymentTime(request.getOpTime());
        request.setOrderType(HmcOrderTypeEnum.MEDICINE);
        request.setDeliveryType(HmcDeliveryTypeEnum.SELF_PICKUP);
        request.setCreateSource(HmcCreateSourceEnum.ADMIN_HMC);

        orderService.createOrder(request);
    }

    @Test
    public void testSyncOrderStatus() {
        orderService.syncOrderStatus(89L);
    }

    @Test
    public void confirmTook() {
        ConfirmTookRequest request = new ConfirmTookRequest();
        request.setId(89L);
        request.setOpUserId(26L);
        boolean b = orderService.confirmTook(request);
        System.out.println(b);
    }

    @Test
    public void syncOrder() {
        SyncOrderRequest request = new SyncOrderRequest();
        request.setId(89L);
        boolean result = insuranceSettlementService.syncOrder(request);
        System.out.println(result);
    }
}