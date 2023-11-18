package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.marketing.promotion.api.PromotionActivityApi;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityDTO;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityGoodsDTO;
import com.yiling.marketing.promotion.dto.request.OrderUsePaymentActivityGoodsRequest;
import com.yiling.marketing.promotion.dto.request.OrderUsePaymentActivityRequest;
import com.yiling.marketing.promotion.enums.PromotionSponsorTypeEnum;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付促销活动优惠
 *
 * @author zhigang.guo
 * @date: 2022/9/22
 */
@Component
@Order(5)
@Slf4j
public class PaymentDiscountProcessHandler extends AbstractDiscountProcessHandler implements DiscountProcessHandler {

    @DubboReference
    PromotionActivityApi promotionActivityApi;


    @Override
    protected void processDiscount(CalOrderDiscountContextBO discountContextBo) {

        // 目前只有B2B订单参与赠品
        if (OrderTypeEnum.B2B != discountContextBo.getOrderTypeEnum()) {

            return;
        }

        // 不是大运河不参与优惠
        if (PlatformEnum.B2B != discountContextBo.getPlatformEnum()) {

            return;
        }

        if (log.isDebugEnabled()) {

            log.debug("计算订单支付促销活动,订单来源:{},参数:{}", discountContextBo.getPlatformEnum(), discountContextBo);
        }

        List<CreateOrderRequest> createOrderRequestList = discountContextBo.getCreateOrderRequestList();

        // 商家支付促销活动
        Map<Long, Long> shopPaymentActivityIdMap = discountContextBo.getShopPaymentActivityIdMap();
        // 商家支付促销规则Id
        Map<Long, Long> shopActivityRuleIdIdMap = discountContextBo.getShopActivityRuleIdIdMap();

        // 过滤掉为0的情况
        Long platformPaymentActivityId = discountContextBo.getPlatformPaymentActivityId();
        // 平台支付促销规则Id
        Long platformActivityRuleIdId = discountContextBo.getPlatformActivityRuleIdId();


        if (MapUtil.isEmpty(shopPaymentActivityIdMap) && (platformPaymentActivityId == null || platformPaymentActivityId == 0
                || platformActivityRuleIdId == null || platformActivityRuleIdId == 0)) {

            return;
        }

        /*********************************构建查询支付促销活动信息参数*****************************/
        List<OrderUsePaymentActivityGoodsRequest> goodsSkuDetailList = createOrderRequestList.stream().map(t -> t.getOrderDetailList().stream().map(orderDetail -> {
            Long shopActivityId = shopPaymentActivityIdMap.get(t.getDistributorEid());
            Long shopActivityRuleIdId = shopActivityRuleIdIdMap.get(t.getDistributorEid());

            // 如果整个商家没有使用促销活动,无需分摊优惠金额
            if (ObjectUtil.isNull(shopActivityId) && ObjectUtil.isNull(platformPaymentActivityId)) {

                return null;
            }

            OrderUsePaymentActivityGoodsRequest goodsDetailRequest = new OrderUsePaymentActivityGoodsRequest();
            goodsDetailRequest.setShopActivityId(shopActivityId);
            goodsDetailRequest.setShopRuleId(shopActivityRuleIdId);
            goodsDetailRequest.setPaymentMethod(t.getPaymentMethod().intValue());
            goodsDetailRequest.setEid(t.getSellerEid());
            goodsDetailRequest.setGoodsId(orderDetail.getDistributorGoodsId());
            goodsDetailRequest.setGoodsSkuId(orderDetail.getGoodsSkuId());

            // 设置分摊的唯一标识Id,如果秒杀，组合促销和正常商品同时下单时存在skuId重复情况
            goodsDetailRequest.setRelationId(StringUtils.join(t.getOrderNo(), Constants.SEPARATOR_MIDDLELINE, orderDetail.getGoodsSkuId()));
            // 减去优惠劵之后的优惠金额
            goodsDetailRequest.setGoodsAmount(NumberUtil.sub(orderDetail.getGoodsAmount(), orderDetail.getCouponDiscountAmount(), orderDetail.getPlatformCouponDiscountAmount()));
            // 商品件数
            goodsDetailRequest.setGoodsNumber(BigDecimal.valueOf(orderDetail.getGoodsQuantity()));

            return goodsDetailRequest;

        }).filter(Objects::nonNull).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());


        OrderUsePaymentActivityRequest request = new OrderUsePaymentActivityRequest();
        request.setOpUserId(discountContextBo.getOpUserId());
        request.setCurrentEid(discountContextBo.getBuyerEid());
        request.setPlatform(discountContextBo.getPlatformEnum().getCode());
        request.setGoodsSkuDetailList(goodsSkuDetailList);
        request.setPlatformActivityId(platformPaymentActivityId);
        request.setPlatformRuleId(platformActivityRuleIdId);


        OrderUsePaymentActivityDTO orderUsePaymentActivityDto = promotionActivityApi.orderUsePaymentActivityShareAmountBudget(request);

        log.info("调动营销支付促销活动，验证，分摊,参数:[{}],结果:[{}]", request, orderUsePaymentActivityDto);

        Map<Long, OrderUsePaymentActivityEnterpriseDTO> orderUsePaymentActivityEnterpriseDTOMap = orderUsePaymentActivityDto.getEnterpriseGoodsList().stream().collect(Collectors.toMap(OrderUsePaymentActivityEnterpriseDTO::getEid, Function.identity()));
        for (CreateOrderRequest createOrderRequest : createOrderRequestList) {
            OrderUsePaymentActivityEnterpriseDTO orderUsePaymentActivityEnterpriseDTO = orderUsePaymentActivityEnterpriseDTOMap.get(createOrderRequest.getDistributorEid());
            if (orderUsePaymentActivityEnterpriseDTO == null) {
                continue;
            }

            List<CreateOrderPromotionActivityRequest> promotionActivityRequestList = createOrderRequest.getPromotionActivityRequestList();
            // 表明当前订单参与了商家支付促销活动
            if (CompareUtil.compare(orderUsePaymentActivityEnterpriseDTO.getBusinessDiscountAmount(), BigDecimal.ZERO) > 0) {
                CreateOrderPromotionActivityRequest activityRequest = new CreateOrderPromotionActivityRequest();
                activityRequest.setActivityId(orderUsePaymentActivityEnterpriseDTO.getShopActivityId());
                activityRequest.setActivityName(orderUsePaymentActivityEnterpriseDTO.getActivityName());
                activityRequest.setActivityBear(orderUsePaymentActivityEnterpriseDTO.getBear());
                activityRequest.setActivityType(PromotionActivityTypeEnum.PAYMENT.getCode());
                activityRequest.setSponsorType(PromotionSponsorTypeEnum.MERCHANT.getType());
                activityRequest.setActivityRuleId(orderUsePaymentActivityEnterpriseDTO.getShopRuleId());
                activityRequest.setActivityPlatformPercent(orderUsePaymentActivityEnterpriseDTO.getPlatformRatio());
                promotionActivityRequestList.add(activityRequest);
            }
            // 表明当前订单参与了平台支付促销活动
            if (CompareUtil.compare(orderUsePaymentActivityEnterpriseDTO.getPlatformDiscountAmount(), BigDecimal.ZERO) > 0) {
                CreateOrderPromotionActivityRequest activityRequest1 = new CreateOrderPromotionActivityRequest();
                activityRequest1.setActivityId(orderUsePaymentActivityDto.getPlatformPaymentActivityId());
                activityRequest1.setActivityName(orderUsePaymentActivityDto.getActivityName());
                activityRequest1.setActivityBear(orderUsePaymentActivityDto.getBear());
                activityRequest1.setSponsorType(PromotionSponsorTypeEnum.PLATFORM.getType());
                activityRequest1.setActivityType(PromotionActivityTypeEnum.PAYMENT.getCode());
                activityRequest1.setActivityRuleId(orderUsePaymentActivityDto.getPlatformRuleId());
                activityRequest1.setActivityPlatformPercent(orderUsePaymentActivityDto.getPlatformRatio());
                promotionActivityRequestList.add(activityRequest1);
            }

            // 设置订单的支付优惠金额
            Map<String, OrderUsePaymentActivityGoodsDTO> orderUsePaymentActivityGoodsMap = orderUsePaymentActivityEnterpriseDTO.getGoodsDetailList().stream().collect(Collectors.toMap(OrderUsePaymentActivityGoodsDTO::getRelationId, Function.identity()));
            for (CreateOrderDetailRequest createOrderDetailRequest : createOrderRequest.getOrderDetailList()) {
                OrderUsePaymentActivityGoodsDTO orderUsePaymentActivityGoodsDTO = orderUsePaymentActivityGoodsMap.get(StringUtils.join(createOrderRequest.getOrderNo(), Constants.SEPARATOR_MIDDLELINE, createOrderDetailRequest.getGoodsSkuId()));
                if (orderUsePaymentActivityGoodsDTO == null) {
                    continue;
                }
                // 设置支付促销金额到商品明细记录
                createOrderDetailRequest.setShopPaymentDiscountAmount(orderUsePaymentActivityGoodsDTO.getBusinessShareAmount());
                createOrderDetailRequest.setPlatformPaymentDiscountAmount(orderUsePaymentActivityGoodsDTO.getPlatformShareAmount());
            }
            // 店铺支付促销总优惠金额
            BigDecimal totalShopPaymentDiscountAmount = createOrderRequest.getOrderDetailList().stream().map(CreateOrderDetailRequest::getShopPaymentDiscountAmount).reduce(BigDecimal::add).get();
            // 平台支付促销优惠总金额
            BigDecimal totalPlatformPaymentDiscountAmount = createOrderRequest.getOrderDetailList().stream().map(CreateOrderDetailRequest::getPlatformPaymentDiscountAmount).reduce(BigDecimal::add).get();
            // 设置平台支付优惠总金额
            createOrderRequest.setPlatformPaymentDiscountAmount(totalPlatformPaymentDiscountAmount);
            // 设置商家支付优惠总金额
            createOrderRequest.setShopPaymentDiscountAmount(totalShopPaymentDiscountAmount);
            // 设置订单实际支付金额
            createOrderRequest.setPaymentAmount(NumberUtil.sub(createOrderRequest.getPaymentAmount(), createOrderRequest.getPlatformPaymentDiscountAmount(), createOrderRequest.getShopPaymentDiscountAmount()));
            //去除重复，同一个订单，同一个活动只需要记录一次
            createOrderRequest.setPromotionActivityRequestList(promotionActivityRequestList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(t -> StringUtils.join(t.getActivityId(), Constants.SEPARATOR_MIDDLELINE, t.getActivityType())))), ArrayList::new)));
        }

        if (log.isDebugEnabled()) {

            log.debug("计算订单支付促销活动,订单来源:{},结果:{}", discountContextBo.getPlatformEnum(), discountContextBo);
        }

        // 校验订单金额是否出现0元订单
        Boolean orderMoneyCheckError = createOrderRequestList.stream().anyMatch(createOrderRequest -> CompareUtil.compare(createOrderRequest.getPaymentAmount(), BigDecimal.ZERO) <= 0);

        if (orderMoneyCheckError) {
            log.error("拆单订单金额异常,出现零元订单");
            throw new BusinessException(OrderErrorCode.ORDER_MONEY_ERROR);
        }
    }

}
