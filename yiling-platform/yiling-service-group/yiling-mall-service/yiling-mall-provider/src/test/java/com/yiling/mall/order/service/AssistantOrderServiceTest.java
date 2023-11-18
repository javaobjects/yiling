package com.yiling.mall.order.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.mall.BaseTest;
import com.yiling.mall.order.dto.request.OrderConfirmRequest;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionReduceStockDTO;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeGoodsRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderGiftRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;
import lombok.extern.slf4j.Slf4j;

import com.alibaba.fastjson.JSON;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.service
 * @date: 2021/9/24
 */
@Slf4j
public class AssistantOrderServiceTest extends BaseTest {
    @Autowired
    private SaOrderService saOrderService;



    /**
     * 查询赠品信息
     * @param createOrderRequestList
     */
    public static  void calculateOrderGift(List<CreateOrderRequest> createOrderRequestList, Map<String,Boolean> canUseGiftMap, List<PromotionReduceStockDTO> promotionReduceList) {


        System.out.println(createOrderRequestList);

        // 可以获取赠品的订单
        List<CreateOrderRequest> canGetGiftList = createOrderRequestList.stream().filter(t -> !(canUseGiftMap.get(t.getOrderNo() != null && !canUseGiftMap.get(t.getOrderNo())))).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(canGetGiftList)) {
            return;
        }
        Map<Long,List<CreateOrderRequest>>  canGetGiftMap =  canGetGiftList.stream().collect(Collectors.groupingBy(CreateOrderRequest::getDistributorEid));
        canGetGiftMap.keySet().forEach(distributorEid -> {
            List<CreateOrderRequest> distributorList = canGetGiftMap.get(distributorEid);
            // 通过金额排序,取出金额最大一条
            distributorList = distributorList.stream().sorted(Comparator.comparing(CreateOrderRequest::getPaymentAmount).reversed()).collect(Collectors.toList());
            CreateOrderRequest createOrderRequest = distributorList.stream().findFirst().get();
            // 计算赠品时，需要将相同配送商的卖家商品信息合并
            List<CreateOrderDetailRequest> orderDetailList = new ArrayList<>();
            distributorList.forEach(t -> {
                orderDetailList.addAll(t.getOrderDetailList());
            });
            PromotionJudgeRequest request = new PromotionJudgeRequest();
            request.setEid(createOrderRequest.getSellerEid());
            request.setOpUserId(createOrderRequest.getOpUserId());
            request.setUserId(createOrderRequest.getContacterId().intValue());
            request.setPlatform(PlatformEnum.B2B.getCode());
            Map<Long,List<CreateOrderDetailRequest>> goodDetailMap = orderDetailList.stream().collect(Collectors.groupingBy(CreateOrderDetailRequest::getDistributorGoodsId));
            List<PromotionJudgeGoodsRequest> goodsRequestList = Lists.newArrayList();
            for (Long goodId : goodDetailMap.keySet()) {
                List<CreateOrderDetailRequest> requestList = goodDetailMap.get(goodId);
                BigDecimal totalGoodAmount = requestList.stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get();
                BigDecimal totalPlatefromAmount = requestList.stream().map(CreateOrderDetailRequest::getPlatformCouponDiscountAmount).reduce(BigDecimal::add).get();
                BigDecimal totalShopAmount = requestList.stream().map(CreateOrderDetailRequest::getCouponDiscountAmount).reduce(BigDecimal::add).get();
                BigDecimal discountTotalAmount = NumberUtil.add(totalPlatefromAmount,totalShopAmount);
                PromotionJudgeGoodsRequest goodsRequest = new PromotionJudgeGoodsRequest();
                goodsRequest.setGoodsId(goodId);
                goodsRequest.setAmount(NumberUtil.sub(totalGoodAmount,discountTotalAmount));
                goodsRequestList.add(goodsRequest);
            }
            request.setGoodsRequestList(goodsRequestList);
            request.setAmount(goodsRequestList.stream().map(t -> t.getAmount()).reduce(BigDecimal::add).get());
            log.info(".calculateOrderGift request ->{}" , JSON.toJSON(request));
            List<PromotionGoodsGiftLimitDTO> resultList = new ArrayList<>();
            PromotionGoodsGiftLimitDTO promotionGoodsGiftLimitDTO = new PromotionGoodsGiftLimitDTO();
            promotionGoodsGiftLimitDTO.setGiftName("giftName");
            promotionGoodsGiftLimitDTO.setBear(1);
            promotionGoodsGiftLimitDTO.setGoodsGiftId(1l);
            promotionGoodsGiftLimitDTO.setPlatformPercent(new BigDecimal("0"));
            promotionGoodsGiftLimitDTO.setPromotionAmount(BigDecimal.ZERO);
            promotionGoodsGiftLimitDTO.setPromotionActivityId(1l);
            promotionGoodsGiftLimitDTO.setPromotionName("11");
            promotionGoodsGiftLimitDTO.setPromotionStock(1);
            promotionGoodsGiftLimitDTO.setUsedStock(1);
            resultList.add(promotionGoodsGiftLimitDTO);

            log.info(".calculateOrderGift result ->{}" , JSON.toJSON(resultList));
            if (CollectionUtil.isEmpty(resultList)) {
                return;
            }
            List<CreateOrderGiftRequest> giftRequestList = Lists.newArrayList();
            List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = createOrderRequest.getPromotionActivityRequestList();
            Set<Long> activityIds = Sets.newHashSet();
            for (PromotionGoodsGiftLimitDTO giftLimitDTO : resultList) {
                CreateOrderGiftRequest giftRequest = new CreateOrderGiftRequest();
                giftRequest.setPromotionActivityId(giftLimitDTO.getPromotionActivityId());
                giftRequest.setGoodsGiftId(giftLimitDTO.getGoodsGiftId());
                giftRequest.setGiftName(giftLimitDTO.getGiftName());
                giftRequest.setOpUserId(createOrderRequest.getOpUserId());
                giftRequest.setOpTime(createOrderRequest.getOpTime());
                giftRequestList.add(giftRequest);

                PromotionReduceStockDTO promotionReduceStockDTO = new PromotionReduceStockDTO();
                promotionReduceStockDTO.setPromotionActivityId(giftLimitDTO.getPromotionActivityId());
                promotionReduceStockDTO.setGoodsGiftId(giftLimitDTO.getGoodsGiftId());
                promotionReduceList.add(promotionReduceStockDTO);
                // 满赠赠品活动
                if (activityIds.add(giftLimitDTO.getPromotionActivityId())) {
                    CreateOrderPromotionActivityRequest activityRequest = new CreateOrderPromotionActivityRequest();
                    activityRequest.setActivityId(giftLimitDTO.getPromotionActivityId());
                    activityRequest.setActivityName(giftLimitDTO.getPromotionName());
                    activityRequest.setActivityBear(giftLimitDTO.getBear());
                    activityRequest.setActivityType(PromotionActivityTypeEnum.FULL_GIFT.getCode());
                    activityRequest.setActivityPlatformPercent(giftLimitDTO.getPlatformPercent());
                    promotionActivityRequestList.add(activityRequest);
                }
            }
            createOrderRequest.setOrderGiftRequestList(giftRequestList);
            createOrderRequest.setPromotionActivityRequestList(promotionActivityRequestList);

        });
    }


    @Test
    public void testCustomerConfirmOrder() {

        OrderConfirmRequest confirmRequest = new OrderConfirmRequest();
        confirmRequest.setCancelOrderIds(ListUtil.toList(10131l));
        confirmRequest.setBuyerEid(202159l);
        confirmRequest.setOpUserId(0l);

        OrderConfirmRequest.DistributorOrderDTO distributorOrderDTO = new OrderConfirmRequest.DistributorOrderDTO();
        distributorOrderDTO.setOrderId(10132l);
        distributorOrderDTO.setOrderNo("D20230131114703908051");
        distributorOrderDTO.setSellerEid(222814l);
        distributorOrderDTO.setPaymentMethod(4);
        distributorOrderDTO.setBuyerMessage("");

        confirmRequest.setDistributorOrderList(ListUtil.toList(distributorOrderDTO));


        saOrderService.b2bConfirmCustomerOrder(confirmRequest);


    }
}
