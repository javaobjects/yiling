package com.yiling.mall.order.event;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.yiling.basic.location.api.IPLocationApi;
import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.framework.common.enums.AppEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponRequest;
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDeviceInfoApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.CreateOrderDeviceInfoRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.ReduceQuotaRequest;
import com.yiling.user.system.api.AppLoginInfoApi;
import com.yiling.user.system.dto.AppLoginInfoDTO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单创建完成逻辑处理
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.event
 * @date: 2022/1/5
 */
@Component
@Slf4j
public class OrderCreateListener {
    @DubboReference
    PaymentDaysAccountApi           paymentDaysAccountApi;
    @DubboReference
    CouponActivityApi               couponActivityApi;
    @DubboReference
    PromotionActivityApi            promotionActivityApi;
    @DubboReference
    OrderDetailApi                  orderDetailApi;
    @DubboReference
    IPLocationApi                   ipLocationApi;
    @DubboReference
    AppLoginInfoApi                 appLoginInfoApi;
    @DubboReference
    OrderDeviceInfoApi              orderDeviceInfoApi;
    @DubboReference
    PayPromotionActivityApi         payPromotionActivityApi;

    /**
     * 扣减支付账期
     * @param createOrderEvent
     */
    @EventListener(condition = "#createOrderEvent.reducePaymentDayAccount == true")
    @Order(1)
    public void reducePaymentDayAccount(CreateOrderEvent createOrderEvent) {

        if (log.isDebugEnabled()) {

            log.debug("..reducePaymentDayAccount...{}", createOrderEvent);
        }

        List<OrderDTO> paymentDayOrderList = createOrderEvent.getOrderList().stream().filter(e -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(e.getPaymentMethod().longValue())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(paymentDayOrderList)) {
            return;
        }
        for (OrderDTO orderDto: paymentDayOrderList) {
            ReduceQuotaRequest reduceQuotaRequest = new ReduceQuotaRequest();
            reduceQuotaRequest.setEid(orderDto.getSellerEid());
            reduceQuotaRequest.setCustomerEid(orderDto.getBuyerEid());
            reduceQuotaRequest.setOrderId(orderDto.getId());
            reduceQuotaRequest.setOrderNo(orderDto.getOrderNo());
            reduceQuotaRequest.setUseAmount(orderDto.getPaymentAmount());
            reduceQuotaRequest.setOpUserId(0L);
            reduceQuotaRequest.setTime(new Date());

            PlatformEnum platformEnum = PlatformEnum.POP;

            switch (createOrderEvent.getOrderTypeEnum()) {
                case POP:
                    platformEnum = PlatformEnum.POP; break;
                case B2B:
                    platformEnum = PlatformEnum.B2B;break;
            }
            reduceQuotaRequest.setPlatformEnum(platformEnum);
            paymentDaysAccountApi.reduceQuota(reduceQuotaRequest);
        }
    }


    /**
     * 保存特价秒杀商品信息
     * @param createOrderEvent
     */
    @EventListener(condition = "#createOrderEvent.haveSpecialProduct == true")
    @Order(2)
    public void saveSpecialRecord(CreateOrderEvent createOrderEvent) {
        List<OrderDTO> orderDTOList = createOrderEvent.getOrderList();
        List<Long> orderIdList =  orderDTOList.stream().map(t -> t.getId()).collect(Collectors.toList());
        Map<Long,OrderDTO>  orderDTOMap = orderDTOList.stream().collect(Collectors.toMap(OrderDTO::getId,t -> t));
        // 过滤掉非秒杀&特价的商品信息
        List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailByOrderIds(orderIdList);
        orderDetailDTOList = orderDetailDTOList.stream().filter(t -> PromotionActivityTypeEnum.SPECIAL == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())
                || PromotionActivityTypeEnum.LIMIT == PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType())
        ).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderDetailDTOList)) {
            return;
        }
        PromotionSaveBuyRecordRequest saveRequest = new PromotionSaveBuyRecordRequest();
        List<PromotionBuyRecord> buyRecordList = Lists.newArrayList();
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
            PromotionBuyRecord buyRecord = new PromotionBuyRecord();
            buyRecord.setEid(orderDTOMap.get(orderDetailDTO.getOrderId()).getBuyerEid());
            buyRecord.setOrderId(orderDetailDTO.getOrderId());
            buyRecord.setGoodsId(orderDetailDTO.getDistributorGoodsId());
            buyRecord.setGoodsQuantity(orderDetailDTO.getGoodsQuantity());
            buyRecord.setPromotionActivityId(orderDetailDTO.getPromotionActivityId());
            buyRecordList.add(buyRecord);
        }
        saveRequest.setBuyRecordList(buyRecordList);
        boolean saveResult = promotionActivityApi.saveBuyRecord(saveRequest);

        if (log.isDebugEnabled()) {
            log.debug("..saveSpecialRecord...request:{},result:{}",JSON.toJSONString(saveRequest),JSON.toJSON(saveResult));
        }

        if (!saveResult) {
            throw new BusinessException(OrderErrorCode.ORDER_PROMOTION_SPECIAL_ERROR);
        }
    }

    /**
     * 扣减使用优惠劵
     * @param createOrderEvent
     */
    @Order(3)
    @EventListener
    public void orderUseCoupon (CreateOrderEvent createOrderEvent) {
        // 平台优惠劵Id
        Long platformCustomerCouponId = CompareUtil.compare(0l,createOrderEvent.getPlatformCustomerCouponId(),true) >= 0 ? null :  createOrderEvent.getPlatformCustomerCouponId();

        if (CollectionUtil.isNotEmpty(createOrderEvent.getShopCustomerCouponIds()) || platformCustomerCouponId != null) {
            OrderDTO orderDTO = createOrderEvent.getOrderList().get(0);
            OrderUseCouponRequest useCouponRequest = new OrderUseCouponRequest();
            useCouponRequest.setEid(orderDTO.getBuyerEid());
            useCouponRequest.setCouponIdList(createOrderEvent.getShopCustomerCouponIds());
            useCouponRequest.setPlatformCouponId(platformCustomerCouponId);
            useCouponRequest.setOpUserId(orderDTO.getCreateUser());
            Boolean result = couponActivityApi.orderUseCoupon(useCouponRequest);

            if (log.isDebugEnabled()) {

                log.debug("couponActivityApi orderUseCoupon request:[{}],result:[{}]", useCouponRequest,result);
            }
        }
    }

    /**
     * 促销活动扣减
     * @param createOrderEvent
     */
    @Order(4)
    @EventListener
    public void promotion(CreateOrderEvent createOrderEvent) {
        List<PromotionReduceStockDTO> promotionReduceStockList =  createOrderEvent.getPromotionReduceStockList();
        if (CollectionUtil.isEmpty(promotionReduceStockList)) {
            return;
        }

        PromotionReduceRequest reduceRequest = new PromotionReduceRequest();
        reduceRequest.setReduceStockList(promotionReduceStockList);
        promotionActivityApi.promotionReduceStock(reduceRequest);
        boolean result = promotionActivityApi.promotionReduceStock(reduceRequest);

        if (log.isDebugEnabled()) {

            log.debug("promotionActivityApi promotionReduceStock request:[{}],result:[{}]", reduceRequest,result);
        }
    }


    /**
     * 保存支付促销活动信息,请求营销系统
     * @param createOrderEvent
     */
    @Order(5)
    @EventListener
    public void savePaymentPromotionRecord(CreateOrderEvent createOrderEvent) {
        List<SavePayPromotionRecordRequest> paymentReduceStockList = createOrderEvent.getPaymentReduceStockList();

        if (CollectionUtil.isEmpty(paymentReduceStockList)) {

            return;
        }

        boolean payPromotionRecord = payPromotionActivityApi.savePayPromotionRecord(paymentReduceStockList);

        if (log.isDebugEnabled()) {

            log.debug("payPromotionActivityApi savePayPromotionRecord request:[{}],result:[{}]", paymentReduceStockList,payPromotionRecord);
        }

        Preconditions.checkArgument(payPromotionRecord,"保存支付促销活动信息失败!");
    }


    /**
     * 创建下单用户的设备地址信息
     * @param createOrderEvent
     */
    @SneakyThrows
    @Order(6)
    @Async
    @EventListener(condition = "#createOrderEvent.saveOrderDevice == true")
    public void createOrderTerminalInfo(CreateOrderEvent createOrderEvent) {

        // 兼容无数据的请求
        if (StringUtils.isBlank(createOrderEvent.getIp())) {
            return;
        }

        String batchNo = createOrderEvent.getOrderList().stream().findFirst().get().getBatchNo();

        CreateOrderDeviceInfoRequest createOrderDeviceInfoRequest = new CreateOrderDeviceInfoRequest();
        createOrderDeviceInfoRequest.setIp(createOrderEvent.getIp());
        createOrderDeviceInfoRequest.setBatchNo(batchNo);
        createOrderDeviceInfoRequest.setUserAgent(createOrderEvent.getUserAgent());

        // 查询ip归属地区
        IPLocationBO query = ipLocationApi.query(createOrderEvent.getIp());

        if (log.isDebugEnabled()) {

            log.debug("调用ip查询归属地接口-入参:{},出参:{}",createOrderEvent.getIp(),query.toString());
        }

        createOrderDeviceInfoRequest.setIpProvinceName(query.getProvinceName());
        createOrderDeviceInfoRequest.setIpRegionName(query.getRegionName());
        createOrderDeviceInfoRequest.setIpCityName(query.getCityName());
        createOrderDeviceInfoRequest.setIpLocation(query.getLocation());
        createOrderDeviceInfoRequest.setIpOperatorName(query.getOperatorName());


        Long appId;

        switch (createOrderEvent.getOrderSourceEnum()) {
           case B2B_APP: appId = AppEnum.B2B_APP.getAppId().longValue(); createOrderDeviceInfoRequest.setMobileFlag(1); break;
           case SA: appId = AppEnum.SALES_ASSISTANT_APP.getAppId().longValue(); createOrderDeviceInfoRequest.setMobileFlag(1); break;
           case POP_PC:
               createOrderDeviceInfoRequest.setMobileFlag(2);
                orderDeviceInfoApi.save(createOrderDeviceInfoRequest);
                return;
           default: appId = 0l; break;
        }

        // 查询用户设备登录信息
        AppLoginInfoDTO appLoginInfoDTO =  appLoginInfoApi.getLatestLoginInfoByUserId(appId,createOrderEvent.getUserId());

        if (log.isDebugEnabled()) {

            log.debug("查询用户最后登录信息-入参:{},出参:{}",createOrderEvent.getUserId(),appLoginInfoDTO);
        }

        if (appLoginInfoDTO != null) {
            createOrderDeviceInfoRequest.setTerminalType(appLoginInfoDTO.getTerminalType());
            createOrderDeviceInfoRequest.setManufacturer(appLoginInfoDTO.getManufacturer());
            createOrderDeviceInfoRequest.setBrand(appLoginInfoDTO.getBrand());
            createOrderDeviceInfoRequest.setModel(appLoginInfoDTO.getModel());
            createOrderDeviceInfoRequest.setOsVersion(appLoginInfoDTO.getOsVersion());
            createOrderDeviceInfoRequest.setSdkVersion(appLoginInfoDTO.getSdkVersion());
            createOrderDeviceInfoRequest.setScreenSize(appLoginInfoDTO.getScreenSize());
            createOrderDeviceInfoRequest.setResolution(appLoginInfoDTO.getResolution());
            createOrderDeviceInfoRequest.setUdid(appLoginInfoDTO.getUdid());
            createOrderDeviceInfoRequest.setAppVersion(appLoginInfoDTO.getAppVersion());
            createOrderDeviceInfoRequest.setChannelCode(appLoginInfoDTO.getChannelCode());
        }

        orderDeviceInfoApi.save(createOrderDeviceInfoRequest);

    }

}
