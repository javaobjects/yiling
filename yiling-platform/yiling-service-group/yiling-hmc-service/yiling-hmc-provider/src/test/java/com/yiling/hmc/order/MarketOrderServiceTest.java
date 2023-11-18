package com.yiling.hmc.order;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.BaseTest;
import com.yiling.hmc.insurance.dto.request.ConfirmTookRequest;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.dto.request.QueryAdminMarkerOrderPageRequest;
import com.yiling.hmc.order.dto.request.UpdateMarketOrderRequest;
import com.yiling.hmc.order.enums.*;
import com.yiling.hmc.order.service.MarketOrderService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.hmc.settlement.dto.request.SyncOrderRequest;
import com.yiling.hmc.settlement.service.InsuranceSettlementService;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.HmcDiagnosisOrderPaySuccessNotifyRequest;
import com.yiling.hmc.wechat.dto.request.MarketOrderPayNotifyRequest;
import com.yiling.hmc.wechat.dto.request.MarketOrderSubmitRequest;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;
import com.yiling.hmc.wechat.service.InsuranceRecordPayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author: fan.shen
 * @date: 2022/7/7
 */
@Slf4j
public class MarketOrderServiceTest extends BaseTest {

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
    public void testReceive() {
        UpdateMarketOrderRequest request = new UpdateMarketOrderRequest();
        request.setOrderStatus(6);
        request.setOpUserId(17336L);
        request.setReceiveTime(DateUtil.date());
        Boolean aBoolean = marketOrderService.updateMarketOrder(request);
    }

    @Test
    public void testDiagnosisPayNotify() {
        HmcDiagnosisOrderPaySuccessNotifyRequest payNotifyRequest = new HmcDiagnosisOrderPaySuccessNotifyRequest();

        // {
        // "merTranNo":"TEST_PT20230529090404604950",
        // "diagnosisRecordId":564,
        // "thirdPartyTranNo":"4200001860202305291136469879",
        // "totalAmount":"8.00",
        // "buyerPayAmount":"0.01",
        // "bankTranNo":"010220230529090405000760344Y"
        // }

        payNotifyRequest.setDiagnosisRecordId(564);
        payNotifyRequest.setMerTranNo("TEST_PT20230529090404604950");
        payNotifyRequest.setBankTranNo("010220230529090405000760344Y");
        payNotifyRequest.setThirdPartyTranNo("4200001860202305291136469879");
        payNotifyRequest.setTotalAmount("8.00");
        payNotifyRequest.setBuyerPayAmount("0.01");
        Boolean aBoolean = marketOrderService.diagnosisOrderPayNotify(payNotifyRequest);
    }

    @Test
    public void testQueryAdminMarketOrderPage() {
        QueryAdminMarkerOrderPageRequest request = new QueryAdminMarkerOrderPageRequest();
        Page<AdminMarketOrderDTO> adminMarketOrderDTOPage = marketOrderService.queryAdminMarketOrderPage(request);
        log.info(JSON.toJSONString(adminMarketOrderDTOPage));
    }

    @Test
    public void testCreate() {
        MarketOrderSubmitRequest request = new MarketOrderSubmitRequest();
        request.setOpUserId(171L);
        request.setHmcOrderStatus(HmcOrderStatusEnum.UN_PAY);
        request.setPaymentStatusEnum(HmcPaymentStatusEnum.UN_PAY);
        request.setPaymentMethodEnum(HmcPaymentMethodEnum.WECHAT_PAY);
        request.setDeliveryType(HmcDeliveryTypeEnum.FREIGHT);
        request.setCreateSource(HmcCreateSourceEnum.HMC_MP);

        request.setActivityId(1012L);
        request.setDoctorId(363L);
        request.setAddressId(1L);
        request.setGoodsQuantity(1);
        request.setEid(37L);
        request.setGoodsId(106196L);
        request.setRemark("发货吧，快快快");
        MarketOrderDTO order = marketOrderService.createOrder(request);
        log.info(JSONUtil.toJsonStr(order));
    }

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