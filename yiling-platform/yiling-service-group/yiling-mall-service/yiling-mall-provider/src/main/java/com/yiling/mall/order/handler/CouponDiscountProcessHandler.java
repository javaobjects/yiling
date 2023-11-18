package com.yiling.mall.order.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.mall.order.bo.CalOrderDiscountContextBO;
import com.yiling.marketing.common.enums.CouponActivitySponsorTypeEnum;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetGoodsDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetGoodsDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.order.order.dto.request.CreateOrderCouponUseRequest;
import com.yiling.order.order.dto.request.CreateOrderDetailRequest;
import com.yiling.order.order.dto.request.CreateOrderRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 优惠劵活动优惠
 *
 * @author zhigang.guo
 * @date: 2022/9/22
 */
@Component
@Order(2)
@Slf4j
public class CouponDiscountProcessHandler extends AbstractDiscountProcessHandler implements DiscountProcessHandler {

    @DubboReference
    CouponActivityApi couponActivityApi;

    @Override
    protected  void processDiscount(CalOrderDiscountContextBO discountContextBo) {

        // 目前优惠劵只使用到B2B订单
        if (OrderTypeEnum.B2B != discountContextBo.getOrderTypeEnum()) {

            return;
        }

        List<CreateOrderRequest> createOrderRequestList = discountContextBo.getCreateOrderRequestList();
        // 商家优惠劵促销活动
        Map<Long, Long> shopCustomerCouponIdMap = discountContextBo.getShopCustomerCouponIdMap();

        // 平台劵优惠劵Id
        Long platformCustomerCouponId = discountContextBo.getPlatformCustomerCouponId();

        if (MapUtil.isEmpty(shopCustomerCouponIdMap) && platformCustomerCouponId == null) {
            return;
        }

        if (log.isDebugEnabled()) {

            log.debug("计算订单优惠劵活动,订单来源:{},参数:{}",discountContextBo.getPlatformEnum(),discountContextBo);
        }

        // 秒杀&特价&组合优惠不参与优惠劵的使用
        EnumSet<PromotionActivityTypeEnum> promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.SPECIAL, PromotionActivityTypeEnum.LIMIT, PromotionActivityTypeEnum.COMBINATION);
        createOrderRequestList = createOrderRequestList.stream().filter(t -> !t.getOrderDetailList().stream().anyMatch(z -> promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(z.getPromotionActivityType())))).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(createOrderRequestList)) {
            discountContextBo.setPlatformCustomerCouponId(null);
            shopCustomerCouponIdMap.clear();
            return;
        }

        OrderUseCouponBudgetRequest request = new OrderUseCouponBudgetRequest();
        request.setOpUserId(discountContextBo.getOpUserId());
        request.setCurrentEid(discountContextBo.getBuyerEid());
        request.setPlatform(discountContextBo.getPlatformEnum().getCode());
        List<OrderUseCouponBudgetGoodsDetailRequest> goodsSkuDetailList = Lists.newArrayList();
        for (CreateOrderRequest createOrderRequest : createOrderRequestList) {
            Long shopCustomerCouponId = shopCustomerCouponIdMap.get(createOrderRequest.getSellerEid());
            if ((shopCustomerCouponId == null && platformCustomerCouponId == null)) {
                continue;
            }
            for (CreateOrderDetailRequest createOrderDetailRequest : createOrderRequest.getOrderDetailList()) {
                OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest = new OrderUseCouponBudgetGoodsDetailRequest();
                goodsDetailRequest.setCouponId(shopCustomerCouponId);
                goodsDetailRequest.setPayMethod(createOrderRequest.getPaymentMethod().intValue());
                goodsDetailRequest.setEid(createOrderRequest.getSellerEid());
                goodsDetailRequest.setGoodsId(createOrderDetailRequest.getDistributorGoodsId());
                goodsDetailRequest.setGoodsSkuId(createOrderDetailRequest.getGoodsSkuId());
                goodsDetailRequest.setGoodsSkuAmount(createOrderDetailRequest.getGoodsAmount());
                goodsDetailRequest.setPlatformCouponId(platformCustomerCouponId);
                goodsSkuDetailList.add(goodsDetailRequest);
            }
        }
        if (CollectionUtil.isEmpty(goodsSkuDetailList)) {
            discountContextBo.setPlatformCustomerCouponId(null);
            shopCustomerCouponIdMap.clear();
            return;
        }

        request.setGoodsSkuDetailList(goodsSkuDetailList);
        // 校验优惠劵合法条件，以及分摊优惠劵金额
        OrderUseCouponBudgetDTO orderUseCouponBudgetDto = couponActivityApi.orderUseCouponShareAmountBudget(request);

        if (log.isDebugEnabled()) {
            log.debug("调用营销系统,计算订单优惠分摊，入参:[{}],结果:[{}]", request, orderUseCouponBudgetDto);
        }

        Map<Long, OrderUseCouponBudgetEnterpriseDTO> orderUseCouponBudgetEnterpriseDTOMap = orderUseCouponBudgetDto.getEnterpriseGoodsList().stream().collect(Collectors.toMap(OrderUseCouponBudgetEnterpriseDTO::getEid, Function.identity()));
        // 如果不满足,平台劵不满足，当做平台劵没有使用
        if (CompareUtil.compare(orderUseCouponBudgetDto.getTotalPlatformDiscountAmount(), BigDecimal.ZERO) <= 0) {
            discountContextBo.setPlatformCustomerCouponId(null);
        }
        for (CreateOrderRequest createOrderRequest : createOrderRequestList) {
            Long shopCustomerCouponId = shopCustomerCouponIdMap.get(createOrderRequest.getSellerEid());
            if ((shopCustomerCouponId == null && platformCustomerCouponId == null)) {
                continue;
            }
            OrderUseCouponBudgetEnterpriseDTO enterpriseDTo = orderUseCouponBudgetEnterpriseDTOMap.get(createOrderRequest.getSellerEid());
            if (enterpriseDTo == null) {
                continue;
            }
            createOrderRequest.setPlatformCouponDiscountAmount(enterpriseDTo.getPlatformDiscountAmount());
            createOrderRequest.setCouponDiscountAmount(enterpriseDTo.getBusinessDiscountAmount());
            List<CreateOrderCouponUseRequest> orderCouponUseList = new ArrayList<>();
            Optional.ofNullable(enterpriseDTo).ifPresent(e -> {
                Map<Long, OrderUseCouponBudgetGoodsDetailDTO> orderUserCouponMap = enterpriseDTo.getGoodsSkuDetailList().stream().collect(Collectors.toMap(OrderUseCouponBudgetGoodsDetailDTO::getGoodsSkuId, Function.identity()));
                // 设置平台优惠券
                if (e.getPlatformDiscountAmount() != null && CompareUtil.compare(e.getPlatformDiscountAmount(), BigDecimal.ZERO) > 0) {
                    CreateOrderCouponUseRequest platformUserRequest = new CreateOrderCouponUseRequest();
                    platformUserRequest.setAmount(e.getPlatformDiscountAmount());
                    platformUserRequest.setCouponId(orderUseCouponBudgetDto.getPlatformCouponId());
                    platformUserRequest.setPlatformRatio(orderUseCouponBudgetDto.getPlatformRatio());
                    platformUserRequest.setBusinessRatio(orderUseCouponBudgetDto.getBusinessRatio());
                    platformUserRequest.setCouponId(orderUseCouponBudgetDto.getPlatformCouponId());
                    platformUserRequest.setCouponActivityId(orderUseCouponBudgetDto.getPlatformCouponActivityId());
                    platformUserRequest.setCouponName(orderUseCouponBudgetDto.getPlatformCouponName());
                    platformUserRequest.setCouponType(CouponActivitySponsorTypeEnum.PLATFORM.getCode());
                    platformUserRequest.setOpUserId(createOrderRequest.getOpUserId());
                    platformUserRequest.setOpTime(createOrderRequest.getOpTime());
                    orderCouponUseList.add(platformUserRequest);
                }
                // 设置商家优惠券
                if (e.getBusinessDiscountAmount() != null && CompareUtil.compare(e.getBusinessDiscountAmount(), BigDecimal.ZERO) > 0) {
                    CreateOrderCouponUseRequest couponUseRequest = new CreateOrderCouponUseRequest();
                    couponUseRequest.setAmount(e.getBusinessDiscountAmount());
                    couponUseRequest.setCouponId(e.getCouponId());
                    couponUseRequest.setPlatformRatio(e.getPlatformRatio());
                    couponUseRequest.setBusinessRatio(e.getBusinessRatio());
                    couponUseRequest.setCouponId(e.getCouponId());
                    couponUseRequest.setCouponActivityId(e.getCouponActivityId());
                    couponUseRequest.setCouponName(e.getCouponName());
                    couponUseRequest.setCouponType(CouponActivitySponsorTypeEnum.BUSINESS.getCode());
                    couponUseRequest.setOpUserId(createOrderRequest.getOpUserId());
                    couponUseRequest.setOpTime(createOrderRequest.getOpTime());
                    orderCouponUseList.add(couponUseRequest);
                }
                // 设置订单实际支付金额
                createOrderRequest.setPaymentAmount(e.getTotalAmount().subtract(e.getBusinessDiscountAmount()).subtract(e.getPlatformDiscountAmount()));
                for (CreateOrderDetailRequest detailRequest : createOrderRequest.getOrderDetailList()) {
                    OrderUseCouponBudgetGoodsDetailDTO goodsDetailDTO = orderUserCouponMap.get(detailRequest.getGoodsSkuId());
                    Optional.ofNullable(goodsDetailDTO).ifPresent(business -> {
                        detailRequest.setCouponDiscountAmount(business.getBusinessShareAmount());
                        detailRequest.setPlatformCouponDiscountAmount(business.getPlatformShareAmount());
                    });
                }
            });
            createOrderRequest.setOrderCouponUseList(orderCouponUseList);
        }

        if (log.isDebugEnabled()) {

            log.debug("计算订单优惠券活动,订单来源:{},结果:{}",discountContextBo.getPlatformEnum(),discountContextBo);
        }

        // 校验订单金额是否出现0元订单
        Boolean orderMoneyCheckError = createOrderRequestList.stream().anyMatch(t -> CompareUtil.compare(t.getPaymentAmount(), BigDecimal.ZERO) <= 0);

        if (orderMoneyCheckError) {

            log.error("拆单订单金额异常,出现零元订单");
            throw new BusinessException(OrderErrorCode.ORDER_MONEY_ERROR);
        }
    }

}
