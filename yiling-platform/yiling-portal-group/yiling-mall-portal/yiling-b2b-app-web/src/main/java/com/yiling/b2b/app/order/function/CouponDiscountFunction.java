package com.yiling.b2b.app.order.function;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.yiling.b2b.app.order.vo.CouponActivityCanUseDetailVO;
import com.yiling.b2b.app.order.vo.OrderCouponCanUseVO;
import com.yiling.b2b.app.order.vo.OrderDistributorVO;
import com.yiling.b2b.app.order.vo.OrderSettlementPageVO;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListDetailRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetGoodsDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetGoodsDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.order.order.enums.PromotionActivityTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 结算页面订单计算优惠劵折扣
 *
 * @author zhigang.guo
 * @date: 2023/4/14
 */
@Slf4j
@Component
public class CouponDiscountFunction {

    @DubboReference
    CouponActivityApi couponActivityApi;
    @Autowired
    RedisService redisService;

    /**
     * 计算优惠劵信息并返回优惠金额
     *
     * @param shopCustomerCouponMap 商家优惠劵
     * @param customerPlatformCouponId 平台优惠劵
     * @param currentEid 当前登录企业
     * @param pageVO 返回前台页面参数
     * @param isBestCoupon 是否获取最优优惠劵
     */
    public void calOrderCouponDiscount(Long currentEid, OrderSettlementPageVO pageVO, Map<Long, Long> shopCustomerCouponMap, Long customerPlatformCouponId, Boolean isBestCoupon) {

        // 配送商商品信息
        List<OrderDistributorVO> orderDistributorVOList = pageVO.getOrderDistributorList();
        // 查询订单可用优惠劵活动，并设置优惠劵
        this.selectOrderCanUseCoupon(orderDistributorVOList, shopCustomerCouponMap, customerPlatformCouponId, currentEid, pageVO, isBestCoupon);
        // 查询订单优惠劵分摊参数
        List<OrderUseCouponBudgetGoodsDetailRequest> goodsDetailRequests = this.buildOrderCouponShareMoneyParam(orderDistributorVOList, shopCustomerCouponMap, pageVO);

        if (CollectionUtil.isEmpty(goodsDetailRequests)) {

            return;
        }

        // 查询优惠劵使用金额
        OrderUseCouponBudgetRequest useCouponRequest = new OrderUseCouponBudgetRequest();
        useCouponRequest.setCurrentEid(currentEid);
        useCouponRequest.setPlatform(PlatformEnum.B2B.getCode());
        useCouponRequest.setGoodsSkuDetailList(goodsDetailRequests);
        OrderUseCouponBudgetDTO budgetDTO = couponActivityApi.orderUseCouponShareAmountBudget(useCouponRequest);

        log.debug("查询订单优惠劵分摊入参:{},返回结果:{}", useCouponRequest, budgetDTO);

        // 设置订单使用优惠劵金额
        List<OrderUseCouponBudgetGoodsDetailDTO> goodsSkuDetailList = Lists.newArrayList();
        Map<Long, OrderUseCouponBudgetEnterpriseDTO> shopUseMap = budgetDTO.getEnterpriseGoodsList().stream().peek(t -> goodsSkuDetailList.addAll(t.getGoodsSkuDetailList())).collect(Collectors.toMap(OrderUseCouponBudgetEnterpriseDTO::getEid, Function.identity()));
        orderDistributorVOList.forEach(orderDistributorVO -> {
            OrderUseCouponBudgetEnterpriseDTO enterpriseDTO = shopUseMap.get(orderDistributorVO.getDistributorEid());
            Optional.ofNullable(enterpriseDTO).ifPresent(e -> {
                orderDistributorVO.setCouponDiscountMoney(enterpriseDTO.getBusinessDiscountAmount());
                orderDistributorVO.setPlatformCouponDiscountMoney(enterpriseDTO.getPlatformDiscountAmount());
                orderDistributorVO.setPaymentAmount(orderDistributorVO.getTotalAmount().subtract(enterpriseDTO.getBusinessDiscountAmount()));
            });
        });
        pageVO.setPlatformCouponDiscountAmount(budgetDTO.getTotalPlatformDiscountAmount());
        pageVO.setCustomerPlatformCouponId(budgetDTO.getPlatformCouponId());
        pageVO.setShopCouponDiscountAmount(budgetDTO.getTotalBusinessDiscountAmount());
        // 设置明细优惠券,优惠金额
        Map<Long, OrderUseCouponBudgetGoodsDetailDTO> goodsSkuDetailMap = goodsSkuDetailList.stream().collect(Collectors.toMap(OrderUseCouponBudgetGoodsDetailDTO::getGoodsSkuId, Function.identity()));
        // 秒杀以及特价,组合包不参与优惠劵活动
        EnumSet<PromotionActivityTypeEnum> promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.SPECIAL, PromotionActivityTypeEnum.LIMIT, PromotionActivityTypeEnum.COMBINATION);

        orderDistributorVOList.stream().forEach(vo -> vo.getOrderGoodsList().stream().filter(t -> !promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(t.getPromotionActivityType()))).forEach(goodsVo -> goodsVo.setCouponDiscountMoney(Optional.ofNullable(goodsSkuDetailMap.get(goodsVo.getGoodsSkuId())).map(t -> NumberUtil.add(t.getPlatformShareAmount(), t.getBusinessShareAmount())).orElse(BigDecimal.ZERO))));
    }


    // 构建订单,请求查询优惠劵参数
    private List<QueryCouponCanUseListDetailRequest> buildOrderQueryCouponParam(List<OrderDistributorVO> orderDistributorVOList) {

        // 秒杀以及特价,组合包不参与优惠劵活动
        EnumSet<PromotionActivityTypeEnum> promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.SPECIAL, PromotionActivityTypeEnum.LIMIT, PromotionActivityTypeEnum.COMBINATION);
        return orderDistributorVOList.stream().map(vo -> vo.getOrderGoodsList().stream().map(goodsVO -> {
            // 促销商品不参优惠劵活动
            if (promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(goodsVO.getPromotionActivityType()))) {

                return null;
            }

            OrderDistributorVO.PaymentMethodVO paymentMethodVO = vo.getPaymentMethodList().stream().filter(e -> e.getSelected()).findFirst().orElse(new OrderDistributorVO.PaymentMethodVO());
            QueryCouponCanUseListDetailRequest detailRequest = new QueryCouponCanUseListDetailRequest();
            detailRequest.setEid(vo.getDistributorEid());
            detailRequest.setPaymentMethod(Optional.ofNullable(paymentMethodVO.getId()).map(t -> t.intValue()).orElse(null));
            detailRequest.setGoodsAmount(NumberUtil.mul(goodsVO.getPrice(), goodsVO.getGoodsQuantity()));
            detailRequest.setGoodsId(goodsVO.getGoodsId());

            return detailRequest;

        }).filter(Objects::nonNull).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
    }


    /**
     * 构建订单分摊优惠劵参数
     *
     * @param orderDistributorVOList
     * @param shopCustomerCouponMap
     * @param pageVO
     * @return
     */
    private List<OrderUseCouponBudgetGoodsDetailRequest> buildOrderCouponShareMoneyParam(List<OrderDistributorVO> orderDistributorVOList, Map<Long, Long> shopCustomerCouponMap, OrderSettlementPageVO pageVO) {

        // 平台优惠劵Id
        List<Long> customerPlatformCouponIdList = pageVO.getPlatfromCouponActivity().stream().map(t -> t.getId()).collect(Collectors.toList());
        // 秒杀以及特价,组合包不参与优惠劵活动
        EnumSet<PromotionActivityTypeEnum> promotionActivityTypeEnumEnumSet = EnumSet.of(PromotionActivityTypeEnum.SPECIAL, PromotionActivityTypeEnum.LIMIT, PromotionActivityTypeEnum.COMBINATION);

        return orderDistributorVOList.stream().map(vo -> vo.getOrderGoodsList().stream().map(goodsVO -> {
            // 促销商品不参优惠劵活动
            if (promotionActivityTypeEnumEnumSet.contains(PromotionActivityTypeEnum.getByCode(goodsVO.getPromotionActivityType()))) {

                return null;
            }

            // 当前商铺能选择商家优惠劵Id
            List<Long> customerCouponIdList = vo.getShopCouponActivity().stream().map(t -> t.getId()).collect(Collectors.toList());
            OrderDistributorVO.PaymentMethodVO paymentMethodVO = vo.getPaymentMethodList().stream().filter(e -> e.getSelected()).findFirst().orElse(new OrderDistributorVO.PaymentMethodVO());
            Long customerCouponId = shopCustomerCouponMap.get(vo.getDistributorEid());
            Long customerPlatformCouponId = pageVO.getCustomerPlatformCouponId();

            // 请求参数
            if ((ObjectUtil.isNotNull(customerPlatformCouponId) && customerPlatformCouponIdList.contains(customerPlatformCouponId)) || ObjectUtil.isNotNull(customerCouponId) && customerCouponIdList.contains(customerCouponId)) {

                OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest = new OrderUseCouponBudgetGoodsDetailRequest();
                goodsDetailRequest.setEid(vo.getDistributorEid());
                goodsDetailRequest.setPayMethod(Optional.ofNullable(paymentMethodVO.getId()).map(t -> t.intValue()).orElse(null));
                goodsDetailRequest.setGoodsSkuAmount(NumberUtil.mul(goodsVO.getPrice(), goodsVO.getGoodsQuantity()));
                goodsDetailRequest.setGoodsId(goodsVO.getGoodsId());
                goodsDetailRequest.setGoodsSkuId(goodsVO.getGoodsSkuId());
                goodsDetailRequest.setPlatformCouponId(customerPlatformCouponIdList.contains(customerPlatformCouponId) ? customerPlatformCouponId : null);
                goodsDetailRequest.setCouponId(customerCouponIdList.contains(customerCouponId) ? customerCouponId : null);

                return goodsDetailRequest;
            }

            return null;

        }).filter(Objects::nonNull).collect(Collectors.toList())).flatMap(Collection::stream).collect(Collectors.toList());
    }


    /**
     * 营销优惠劵查询请求
     *
     * @param currentEid
     * @param shopCustomerCouponMap
     * @param goodsDetailList
     * @param isBest
     * @return
     */
    private CouponActivityCanUseDTO couponActivityCanUseRequest(Long currentEid, Map<Long, Long> shopCustomerCouponMap, List<QueryCouponCanUseListDetailRequest> goodsDetailList, BigDecimal totalAmount, Boolean isBest) {

        String userSessionId = StringUtils.join("order:settlement:session:", currentEid);

        if (!isBest) {
            Object data = redisService.get(userSessionId);
            if (ObjectUtil.isNotNull(data)) {
                OrderCouponCanUseVO orderCouponCanUseVO = this.buildOrderCanUseCouponVO(data.toString());
                // 如果结算金额没有变动,先从缓存中获取优惠劵信息,减少没必要的请求,后面分摊还会校验优惠劵的准确性
                if (CompareUtil.compare(orderCouponCanUseVO.getTotalAmount(), totalAmount) == 0) {

                    return orderCouponCanUseVO.getActivityCanUseDTO();
                }
            }
        }

        Map<Long, List<QueryCouponCanUseListDetailRequest>> queryCouponCanUseListDetailRequestMap = goodsDetailList.stream().collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getGoodsId));
        List<QueryCouponCanUseListDetailRequest> couponCanUseDetailRequestList = queryCouponCanUseListDetailRequestMap.entrySet().stream().map(t -> {
            List<QueryCouponCanUseListDetailRequest> detailRequestList = t.getValue();
            // 商品小计金额
            BigDecimal goodsAmount = detailRequestList.stream().map(QueryCouponCanUseListDetailRequest::getGoodsAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            QueryCouponCanUseListDetailRequest detailRequest = PojoUtils.map(detailRequestList.stream().findFirst().get(), QueryCouponCanUseListDetailRequest.class);
            detailRequest.setGoodsAmount(goodsAmount);
            detailRequest.setShopCouponId(shopCustomerCouponMap.get(detailRequest.getEid()));

            return detailRequest;

        }).collect(Collectors.toList());

        QueryCouponCanUseListRequest canUseListRequest = new QueryCouponCanUseListRequest();
        canUseListRequest.setCurrentEid(currentEid);
        canUseListRequest.setPlatform(PlatformEnum.B2B.getCode());
        canUseListRequest.setIsUseBestCoupon(isBest);
        canUseListRequest.setGoodsDetailList(couponCanUseDetailRequestList);


        CouponActivityCanUseDTO activityCanUseDTO = couponActivityApi.getCouponCanUseList(canUseListRequest);

        log.debug("查询用户可以使用优惠劵,入参:{},返回结果:{}", canUseListRequest, activityCanUseDTO);

        // 加入缓存,过滤没必要的请求
        OrderCouponCanUseVO orderCouponCanUseVO = new OrderCouponCanUseVO();
        orderCouponCanUseVO.setTotalAmount(totalAmount);
        orderCouponCanUseVO.setActivityCanUseDTO(activityCanUseDTO);
        redisService.set(userSessionId, JSONUtil.toJsonStr(orderCouponCanUseVO), 60 * 15);

        return activityCanUseDTO;
    }

    /**
     * 订单结算优惠劵信息
     *
     * @param userData
     * @return
     */
    private OrderCouponCanUseVO buildOrderCanUseCouponVO(String userData) {

        //去除转义符
        userData = userData.replaceAll("\\\\", "");
        OrderCouponCanUseVO orderCouponCanUseVO = JSONUtil.toBean(userData, OrderCouponCanUseVO.class);

        return orderCouponCanUseVO;
    }

    /**
     * 查询用户可以使用的优惠券
     *
     * @param orderDistributorVOList
     * @param shopCustomerCouponMap
     * @param customerPlatformCouponId
     * @param currentEid
     * @param pageVO
     */
    private void selectOrderCanUseCoupon(List<OrderDistributorVO> orderDistributorVOList, Map<Long, Long> shopCustomerCouponMap, Long customerPlatformCouponId, Long currentEid, OrderSettlementPageVO pageVO, Boolean isBest) {

        List<QueryCouponCanUseListDetailRequest> goodsDetailList = this.buildOrderQueryCouponParam(orderDistributorVOList);

        if (CollectionUtil.isEmpty(goodsDetailList)) {

            return;
        }

        CouponActivityCanUseDTO activityCanUseDTO = this.couponActivityCanUseRequest(currentEid, shopCustomerCouponMap, goodsDetailList, pageVO.getTotalAmount(), isBest);

        if (activityCanUseDTO == null) {

            return;
        }

        List<CouponActivityCanUseDetailDTO> platformCouponList = activityCanUseDTO.getPlatformList();

        if (CollectionUtil.isNotEmpty(platformCouponList)) {

            List<CouponActivityCanUseDetailVO> couponActivityList = platformCouponList.stream().map(t -> buildCouponActivityCanUseDetailVO(t, customerPlatformCouponId, isBest)).collect(Collectors.toList());
            CouponActivityCanUseDetailVO detailVO = couponActivityList.stream().filter(e -> e.getIsSelected()).findFirst().orElse(new CouponActivityCanUseDetailVO());
            pageVO.setCustomerPlatformCouponId(detailVO.getId());
            pageVO.setPlatfromCouponActivity(couponActivityList);
            pageVO.setPlatformCouponCount(platformCouponList.size());
            pageVO.setIsUsePlatformCoupon(true);
        }

        List<CouponActivityCanUseDetailDTO> shopCouponList = activityCanUseDTO.getBusinessList();

        if (CollectionUtil.isEmpty(shopCouponList)) {

            return;
        }

        Map<Long, List<CouponActivityCanUseDetailDTO>> shopCanUseMap = shopCouponList.stream().collect(Collectors.groupingBy(CouponActivityCanUseDetailDTO::getShopEid));
        for (OrderDistributorVO orderDistributorVO : orderDistributorVOList) {
            // 商家可使用优惠劵列表
            List<CouponActivityCanUseDetailDTO> shopCanUserList = shopCanUseMap.get(orderDistributorVO.getDistributorEid());

            if (CollectionUtil.isEmpty(shopCanUserList)) {

                continue;
            }

            List<CouponActivityCanUseDetailVO> couponActivityList = shopCanUserList.stream().map(t -> buildCouponActivityCanUseDetailVO(t, orderDistributorVO.getCustomerShopCouponId(), isBest)).collect(Collectors.toList());
            CouponActivityCanUseDetailVO detailVO = couponActivityList.stream().filter(e -> e.getIsSelected()).findFirst().orElse(null);
            orderDistributorVO.setShopCouponCount(shopCanUserList.size());
            orderDistributorVO.setIsUseShopCoupon(true);
            orderDistributorVO.setShopCouponActivity(couponActivityList);

            if (detailVO == null) {

                continue;
            }

            // 反写选择的优惠劵
            shopCustomerCouponMap.put(orderDistributorVO.getDistributorEid(), detailVO.getId());
            orderDistributorVO.setCustomerShopCouponId(detailVO.getId());
        }
    }


    /**
     * 构建订单结算页面优惠劵
     *
     * @param couponActivityCanUseDto
     * @param couponId
     * @param isBest
     * @return
     */
    private CouponActivityCanUseDetailVO buildCouponActivityCanUseDetailVO(CouponActivityCanUseDetailDTO couponActivityCanUseDto, Long couponId, Boolean isBest) {

        CouponActivityCanUseDetailVO detailVO = PojoUtils.map(couponActivityCanUseDto, CouponActivityCanUseDetailVO.class);

        detailVO.setIsSelected(false);
        detailVO.setIsBestCoupon(false);

        if (ObjectUtil.equal(detailVO.getId(), couponId)) {
            detailVO.setIsSelected(true);
        } else if (isBest && couponActivityCanUseDto.getIsBestCoupon() != null && couponActivityCanUseDto.getIsBestCoupon()) {
            detailVO.setIsSelected(true);
            detailVO.setIsBestCoupon(true);
        }

        return detailVO;
    }
}
