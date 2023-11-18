package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.mall.order.bo.SplitOrderEnum;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeGoodsRequest;
import com.yiling.marketing.promotion.dto.request.PromotionJudgeRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderGiftRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/** 赠品优惠计算
 * @author zhigang.guo
 * @date: 2022/9/22
 */
@Component
@Order(1)
@Slf4j
public class GiftDiscountProcessHandler extends AbstractDiscountProcessHandler implements DiscountProcessHandler  {


    @DubboReference
    PromotionActivityApi promotionActivityApi;

    /**
     * 订单是否可以参与赠品活动
     * @param createOrderRequest 订单信息
     * @return 订单是否可以参与赠品
     */
    private Boolean isCanGetGift(CreateOrderRequest createOrderRequest,Map<Long,Boolean> orderHasGiftMap) {

        // 目前只有B2B订单参与赠品
        if (OrderTypeEnum.B2B != OrderTypeEnum.getByCode(createOrderRequest.getOrderType())) {

            return false;
        }

        // 如果确认订单时显示无赠品信息,无需求请求营销接口,因为大部分请求是无赠品信息,无需浪费接口不必要性能
        if (MapUtil.isNotEmpty(orderHasGiftMap) && !orderHasGiftMap.getOrDefault(createOrderRequest.getDistributorEid(),true)) {

            return false;
        }

        if (CollectionUtil.isEmpty(createOrderRequest.getPromotionActivityRequestList())) {

            return true;
        }

        // 普通订单可以参与赠品,非普通订单不能参与赠品
        if (SplitOrderEnum.NORMAL == SplitOrderEnum.getByCode(createOrderRequest.getSplitOrderType())) {

            return true;
        }

        return false;

    }

    @Override
    protected void processDiscount(CalOrderDiscountContextBO discountContextBo) {

        List<CreateOrderRequest> createOrderRequestList = discountContextBo.getCreateOrderRequestList();
        // 订单是否存在赠品
        Map<Long,Boolean> orderHasGiftMap = discountContextBo.getOrderHasGiftMap();

        // 可以获取赠品的订单
        List<CreateOrderRequest> canGetGiftList = createOrderRequestList.stream().filter(createOrderRequest -> this.isCanGetGift(createOrderRequest,orderHasGiftMap)).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(canGetGiftList)) {

            return ;
        }

        if (log.isDebugEnabled()) {

            log.debug("计算订单赠品活动,参数:{}",discountContextBo);
        }

        for (CreateOrderRequest createOrderRequest : canGetGiftList) {
            Map<Long, List<CreateOrderDetailRequest>> goodDetailMap = createOrderRequest.getOrderDetailList().stream().collect(Collectors.groupingBy(CreateOrderDetailRequest::getDistributorGoodsId));

            List<PromotionJudgeGoodsRequest> goodsRequestList = goodDetailMap.entrySet().stream().map(entry -> {
                List<CreateOrderDetailRequest> requestList = entry.getValue();
                // 商品货款金额
                BigDecimal totalGoodAmount = requestList.stream().map(CreateOrderDetailRequest::getGoodsAmount).reduce(BigDecimal::add).get();
                PromotionJudgeGoodsRequest goodsRequest = new PromotionJudgeGoodsRequest();
                goodsRequest.setGoodsId(entry.getKey());
                goodsRequest.setAmount(totalGoodAmount);

                return goodsRequest;

            }).collect(Collectors.toList());


            PromotionJudgeRequest request = new PromotionJudgeRequest();
            request.setEid(createOrderRequest.getSellerEid());
            request.setOpUserId(discountContextBo.getOpUserId());
            request.setUserId(discountContextBo.getOpUserId().intValue());
            request.setPlatform(discountContextBo.getPlatformEnum().getCode());
            request.setGoodsRequestList(goodsRequestList);
            request.setAmount(goodsRequestList.stream().map(t -> t.getAmount()).reduce(BigDecimal::add).get());

            List<PromotionGoodsGiftLimitDTO> resultList = promotionActivityApi.judgePromotion(request).getData();

            if (log.isDebugEnabled()) {

                log.info("调用营销接口查询订单参与的满赠活动,参数:[{}],返回结果:[{}]",request,resultList);
            }

            if (CollectionUtil.isEmpty(resultList)) {
                continue;
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
                giftRequest.setPrice(giftLimitDTO.getPrice());
                giftRequest.setPromotionLimitId(giftLimitDTO.getId());
                giftRequestList.add(giftRequest);

                // 满赠赠品活动
                if (activityIds.add(giftLimitDTO.getPromotionActivityId())) {
                    CreateOrderPromotionActivityRequest activityRequest = new CreateOrderPromotionActivityRequest();
                    activityRequest.setActivityId(giftLimitDTO.getPromotionActivityId());
                    activityRequest.setActivityName(giftLimitDTO.getPromotionName());
                    activityRequest.setActivityBear(giftLimitDTO.getBear());
                    activityRequest.setActivityType(PromotionActivityTypeEnum.FULL_GIFT.getCode());
                    activityRequest.setSponsorType(giftLimitDTO.getSponsorType());
                    activityRequest.setActivityPlatformPercent(giftLimitDTO.getPlatformPercent());
                    promotionActivityRequestList.add(activityRequest);
                }
            }
            createOrderRequest.setOrderGiftRequestList(giftRequestList);
            createOrderRequest.setPromotionActivityRequestList(promotionActivityRequestList);
        }


        if (log.isDebugEnabled()) {
            log.debug("计算订单赠品活动,订单来源:{},结果:{}",discountContextBo.getPlatformEnum(),discountContextBo);
        }
    }
}
