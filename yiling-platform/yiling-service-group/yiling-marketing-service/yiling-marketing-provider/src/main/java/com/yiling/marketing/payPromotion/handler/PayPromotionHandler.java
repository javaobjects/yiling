package com.yiling.marketing.payPromotion.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayCalculateRuleDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayEnterpriseGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionActivityDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionParticipateDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayBuyerLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayCalculateRuleService;
import com.yiling.marketing.payPromotion.service.MarketingPayEnterpriseGoodsLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayMemberLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPlatformGoodsLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromoterMemberLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionActivityService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionParticipateService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionSellerLimitService;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityEidOrGoodsIdDTO;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.paypromotion.enums.PayPromotionErrorCode;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityDTO;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityEnterpriseDTO;
import com.yiling.marketing.promotion.dto.OrderUsePaymentActivityGoodsDTO;
import com.yiling.marketing.promotion.dto.PaymentActivityUseDTO;
import com.yiling.marketing.promotion.dto.PaymentActivityUseDetailDTO;
import com.yiling.marketing.promotion.dto.request.OrderUsePaymentActivityGoodsRequest;
import com.yiling.marketing.promotion.dto.request.OrderUsePaymentActivityRequest;
import com.yiling.marketing.promotion.dto.request.QueryPaymentActivityDetailRequest;
import com.yiling.marketing.promotion.dto.request.QueryPaymentActivityRequest;
import com.yiling.marketing.promotion.enums.PromotionSponsorTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionEnterpriseTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.OrderFirstInfoDTO;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付促销
 *
 * @author: yong.zhang
 * @date: 2023/4/21 0021
 */
@Slf4j
@Service
public class PayPromotionHandler {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;

    @DubboReference
    OrderFirstInfoApi firstInfoApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    MarketingPayPromotionSellerLimitService marketingPayPromotionSellerLimitService;

    @Autowired
    private MarketingPayPromotionActivityService payPromotionActivityService;

    @Autowired
    private MarketingPayBuyerLimitService marketingPayBuyerLimitService;

    @Autowired
    private MarketingPayPromoterMemberLimitService marketingPayPromoterMemberLimitService;

    @Autowired
    private MarketingPayMemberLimitService marketingPayMemberLimitService;

    @Autowired
    private MarketingPayPlatformGoodsLimitService marketingPayPlatformGoodsLimitService;

    @Autowired
    private MarketingPayEnterpriseGoodsLimitService marketingPayEnterpriseGoodsLimitService;

    @Autowired
    private MarketingPayCalculateRuleService marketingPayCalculateRuleService;

    @Autowired
    private MarketingPayPromotionParticipateService marketingPayPromotionParticipateService;

    /**
     * 根据批次订单商品信息获取满足条件的支付促销活动
     *
     * @param request 批次订单的商品信息
     * @return 满足条件的支付促销活动
     */
    public PaymentActivityUseDTO getPaymentActivityUseList(QueryPaymentActivityRequest request) {
        log.info("getPaymentActivityUseList start request:[{}]", JSON.toJSONString(request));
        PaymentActivityUseDTO paymentActivityUseDTO = new PaymentActivityUseDTO();
        // 根据企业id、购买商品id、小计金额 查询可以使用的促销活动
        Date now = new Date();
        List<QueryPaymentActivityDetailRequest> goodsDetailList = request.getGoodsDetailList();
        List<Long> eidList = goodsDetailList.stream().map(QueryPaymentActivityDetailRequest::getEid).collect(Collectors.toList());
        Map<Long, List<QueryPaymentActivityDetailRequest>> goodsDetailListMap = goodsDetailList.stream().collect(Collectors.groupingBy(QueryPaymentActivityDetailRequest::getEid));
        List<MarketingPayPromotionActivityDO> merchantActivityDOList = payPromotionActivityService.listEffectiveActivity(null, now, eidList);
        if (CollUtil.isEmpty(merchantActivityDOList)) {
            log.info("getPaymentActivityUseList 有效活动为空");
            return paymentActivityUseDTO;
        }
        List<MarketingPayPromotionActivityDO> matchActivityDOList = matchActivityBuyer(merchantActivityDOList, request.getCurrentEid());
        if (CollUtil.isEmpty(matchActivityDOList)) {
            log.info("getPaymentActivityUseList match buyer 有效活动为空");
            return paymentActivityUseDTO;
        }

        List<PaymentActivityUseDetailDTO> businessList = new ArrayList<>();
        List<PaymentActivityUseDetailDTO> platformList = new ArrayList<>();
        List<MarketingPayPromotionActivityDO> merchantMatchActivityDOList = matchActivityDOList.stream().filter(e -> PromotionSponsorTypeEnum.MERCHANT.getType().equals(e.getSponsorType())).collect(Collectors.toList());
        List<MarketingPayPromotionActivityDO> platformMatchActivityDOList = matchActivityDOList.stream().filter(e -> PromotionSponsorTypeEnum.PLATFORM.getType().equals(e.getSponsorType())).collect(Collectors.toList());

        OrderFirstInfoDTO orderFirstInfoDTO = firstInfoApi.queryOrderFirstInfo(request.getCurrentEid(), OrderTypeEnum.B2B);
        boolean newVisitor = ObjectUtil.isNull(orderFirstInfoDTO);
        goodsDetailListMap.forEach((sellerEid, item) -> {
            QueryPaymentActivityDetailRequest activityDetailRequest = item.get(0);
            Integer paymentMethod = activityDetailRequest.getPaymentMethod();
            for (MarketingPayPromotionActivityDO activityDO : merchantMatchActivityDOList) {
                if (activityDO.getConditionOther().contains("1") && !newVisitor) {
                    log.info("getPaymentActivityUseList 非新客用户，不允许参与新客活动 活动id:[{}]", activityDO.getId());
                    continue;
                }

                // 支付方式校验
                if (!activityDO.getPayType().contains(paymentMethod + "")) {
                    log.info("getPaymentActivityUseList 支付方式不满足活动条件 活动id:[{}]，支付方式:[{}]", activityDO.getId(), paymentMethod);
                    continue;
                }
                PaymentActivityUseDetailDTO paymentActivityUseDetailDTO = getByGoodsDetailList(activityDO, request.getCurrentEid(), sellerEid, item);
                if (Objects.nonNull(paymentActivityUseDetailDTO)) {
                    businessList.add(paymentActivityUseDetailDTO);
                }
            }
        });
        paymentActivityUseDTO.setBusinessList(businessList);

        for (MarketingPayPromotionActivityDO activityDO : platformMatchActivityDOList) {
            if (activityDO.getConditionOther().contains("1") && !newVisitor) {
                log.info("getPaymentActivityUseList 非新客用户，不允许参与新客活动 活动id:[{}]", activityDO.getId());
                continue;
            }

            List<QueryPaymentActivityDetailRequest> activityDetailRequestList = goodsDetailList.stream().filter(e -> activityDO.getPayType().contains(e.getPaymentMethod() + "")).collect(Collectors.toList());

            PaymentActivityUseDetailDTO paymentActivityUseDetailDTO = getByGoodsDetailList(activityDO, request.getCurrentEid(), null, activityDetailRequestList);
            if (Objects.nonNull(paymentActivityUseDetailDTO)) {
                platformList.add(paymentActivityUseDetailDTO);
            }
        }

        paymentActivityUseDTO.setPlatformList(platformList);
        log.info("getPaymentActivityUseList result:[{}]", JSON.toJSONString(paymentActivityUseDTO));
        return paymentActivityUseDTO;
    }

    /**
     * 获取订单满足支付促销的活动信息(平台活动和商家活动)
     *
     * @param activityDO 支付促销活动信息
     * @param buyerEid 买家企业id
     * @param sellerEid 卖家企业id
     * @param item 满足条件的商品信息
     * @return 满足条件的支付促销活动信息
     */
    private PaymentActivityUseDetailDTO getByGoodsDetailList(MarketingPayPromotionActivityDO activityDO, Long buyerEid, Long sellerEid, List<QueryPaymentActivityDetailRequest> item) {
        {
            Integer maxGiveNum = activityDO.getMaxGiveNum();
            Integer records = marketingPayPromotionParticipateService.countRecordByActivityIdAndEid(activityDO.getId(), buyerEid);
            if (maxGiveNum != 0 && records >= maxGiveNum) {
                return null;
            }
        }

        List<QueryPaymentActivityDetailRequest> goodsDetailList = new ArrayList<>();
        // 商家范围筛选
        StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(activityDO.getConditionSellerType());
        if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
            List<MarketingPayPromotionSellerLimitDO> payPromotionSellerLimitDOList = marketingPayPromotionSellerLimitService.listByActivityId(activityDO.getId());
            List<Long> sellerEidList = payPromotionSellerLimitDOList.stream().map(MarketingPayPromotionSellerLimitDO::getEid).collect(Collectors.toList());
            goodsDetailList = item.stream().filter(e -> sellerEidList.contains(e.getEid())).collect(Collectors.toList());
        } else {
            goodsDetailList = item;
        }

        if (CollUtil.isEmpty(goodsDetailList)) {
            log.info("getPaymentActivityUseList getByGoodsDetailList match seller fail ,活动id:[{}]", activityDO.getId());
            return null;
        }

        StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(activityDO.getConditionGoodsType());
        List<Long> goodsIdList = goodsDetailList.stream().map(QueryPaymentActivityDetailRequest::getGoodsId).collect(Collectors.toList());
        BigDecimal amount = BigDecimal.ZERO;
        {
            // 商品范围筛选
            if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ALL) {
                amount = goodsDetailList.stream().map(i -> {
                    if (i.getGoodsAmount() == null) {
                        return BigDecimal.ZERO;
                    } else {
                        return i.getGoodsAmount();
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
            } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIdList);
                List<Long> sellSpecificationsIdList = goodsDTOList.stream().map(GoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
                List<MarketingPayPlatformGoodsLimitDO> payPlatformGoodsLimitDOList = marketingPayPlatformGoodsLimitService.listByActivityIdAndSellSpecificationsIdList(activityDO.getId(), sellSpecificationsIdList);
                if (CollUtil.isEmpty(payPlatformGoodsLimitDOList)) {
                    log.info("getPaymentActivityUseList getByGoodsDetailList match platform goods fail ,活动id:[{}]", activityDO.getId());
                    return null;
                }
                Map<Long, Long> goodsIdMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getSellSpecificationsId, (k1, k2) -> k1));
                List<Long> matchSellSpecificationsIdList = payPlatformGoodsLimitDOList.stream().map(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
                for (QueryPaymentActivityDetailRequest queryPaymentActivityDetailRequest : goodsDetailList) {
                    Long sellSpecificationsId = goodsIdMap.get(queryPaymentActivityDetailRequest.getGoodsId());
                    if (matchSellSpecificationsIdList.contains(sellSpecificationsId)) {
                        amount = amount.add(queryPaymentActivityDetailRequest.getGoodsAmount());
                    }
                }
            } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                List<MarketingPayEnterpriseGoodsLimitDO> payEnterpriseGoodsLimitDOList = marketingPayEnterpriseGoodsLimitService.listByActivityIdAndGoodsIdList(activityDO.getId(), goodsIdList);
                if (CollUtil.isEmpty(payEnterpriseGoodsLimitDOList)) {
                    log.info("getPaymentActivityUseList getByGoodsDetailList match enterprise goods fail ,活动id:[{}]", activityDO.getId());
                    return null;
                }
                List<Long> matchGoodsIdList = payEnterpriseGoodsLimitDOList.stream().map(MarketingPayEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                for (QueryPaymentActivityDetailRequest queryPaymentActivityDetailRequest : goodsDetailList) {
                    if (matchGoodsIdList.contains(queryPaymentActivityDetailRequest.getGoodsId())) {
                        amount = amount.add(queryPaymentActivityDetailRequest.getGoodsAmount());
                    }
                }
            }
        }
        log.info("getPaymentActivityUseList getByGoodsDetailList start get goods amount 活动id:[{}],amount:[{}]", activityDO.getId(), amount);

        // 生效条件(1-按金额)
        Integer conditionEffective = activityDO.getConditionEffective();
        if (1 == conditionEffective) {
            PaymentActivityUseDetailDTO paymentActivityUseDetailDTO = new PaymentActivityUseDetailDTO();
            List<MarketingPayCalculateRuleDO> payCalculateRuleDOList = marketingPayCalculateRuleService.listRuleByActivityId(activityDO.getId());
            BigDecimal finalAmount = amount;
            List<MarketingPayCalculateRuleDO> marketingPayCalculateRuleDOList = payCalculateRuleDOList.stream().sorted(Comparator.comparing(MarketingPayCalculateRuleDO::getThresholdValue)).collect(Collectors.toList());
            StringBuilder builder = new StringBuilder("金额");
            for (MarketingPayCalculateRuleDO marketingPayCalculateRuleDO : marketingPayCalculateRuleDOList) {
                builder.append("满").append(marketingPayCalculateRuleDO.getThresholdValue()).append("元");
                if (1 == marketingPayCalculateRuleDO.getType()) {
                    builder.append("减").append(marketingPayCalculateRuleDO.getDiscountValue()).append("元,");
                } else if (2 == marketingPayCalculateRuleDO.getType()) {
                    builder.append("打").append(marketingPayCalculateRuleDO.getDiscountValue().divide(new BigDecimal("10"), 2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()).append("折,");
                }
            }
            String discountValueRules = builder.toString();


            List<MarketingPayCalculateRuleDO> matchCalculateRuleDOList = marketingPayCalculateRuleDOList.stream().filter(e -> finalAmount.compareTo(e.getThresholdValue()) >= 0).collect(Collectors.toList());
            MarketingPayCalculateRuleDO calculateRuleDO;
            if (CollUtil.isNotEmpty(matchCalculateRuleDOList)) {
                calculateRuleDO = matchCalculateRuleDOList.get(matchCalculateRuleDOList.size() - 1);
                paymentActivityUseDetailDTO.setEnabled(true);
                paymentActivityUseDetailDTO.setDiffAmount(BigDecimal.ZERO);
            } else {
                calculateRuleDO = marketingPayCalculateRuleDOList.get(0);
                paymentActivityUseDetailDTO.setEnabled(false);
                paymentActivityUseDetailDTO.setDiffAmount(calculateRuleDO.getThresholdValue().subtract(finalAmount));
            }
            paymentActivityUseDetailDTO.setEid(sellerEid);
            paymentActivityUseDetailDTO.setActivityId(activityDO.getId());
            paymentActivityUseDetailDTO.setRuleId(calculateRuleDO.getId());
            paymentActivityUseDetailDTO.setType(calculateRuleDO.getType());
            paymentActivityUseDetailDTO.setName(activityDO.getName());
            paymentActivityUseDetailDTO.setSponsorType(activityDO.getSponsorType());
            paymentActivityUseDetailDTO.setDiscountValue(calculateRuleDO.getDiscountValue());
            paymentActivityUseDetailDTO.setDiscountValueRules(discountValueRules.substring(0, discountValueRules.length() - 1));
            //促销规则类型（1-满减券；2-满折券）
            if (1 == calculateRuleDO.getType()) {
                paymentActivityUseDetailDTO.setPaymentDiscount(calculateRuleDO.getDiscountValue());
            } else if (2 == calculateRuleDO.getType()) {
                BigDecimal paymentDiscount = finalAmount.subtract(finalAmount.multiply(calculateRuleDO.getDiscountValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP));
                paymentActivityUseDetailDTO.setPaymentDiscount(paymentDiscount);
            }
            BigDecimal discountMax = calculateRuleDO.getDiscountMax();
            if (discountMax.compareTo(BigDecimal.ZERO) > 0 && paymentActivityUseDetailDTO.getPaymentDiscount().compareTo(discountMax) > 0) {
                paymentActivityUseDetailDTO.setPaymentDiscount(discountMax);
            }
            List<Integer> payTypeList = JSON.parseArray(activityDO.getPayType(), Integer.class);
            paymentActivityUseDetailDTO.setPaymentMethodList(payTypeList);
            paymentActivityUseDetailDTO.setCreateTime(activityDO.getCreateTime());
            paymentActivityUseDetailDTO.setBeginTime(activityDO.getBeginTime());
            paymentActivityUseDetailDTO.setEndTime(activityDO.getEndTime());
            log.info("getPaymentActivityUseList getByGoodsDetailList end response:[{}]", JSON.toJSONString(paymentActivityUseDetailDTO));
            return paymentActivityUseDetailDTO;
        }
        return null;
    }

    /**
     * 支付促销分摊
     *
     * @param request 订单需要分摊的信息(一个批次多个订单)
     * @return 分摊结果
     */
    public OrderUsePaymentActivityDTO orderUsePaymentActivityShareAmountBudget(OrderUsePaymentActivityRequest request) {
        log.info("orderUsePaymentActivityShareAmountBudget start request:[{}]", JSON.toJSONString(request));
        OrderUsePaymentActivityDTO orderUsePaymentActivityDTO = new OrderUsePaymentActivityDTO();
        // 根据企业id、活动Id、商品明细 计算商品分摊优惠金额
        // 分摊维度到具体商品维度
        List<OrderUsePaymentActivityGoodsRequest> goodsSkuDetailList = request.getGoodsSkuDetailList();
        Map<Long, List<OrderUsePaymentActivityGoodsRequest>> goodsSkuDetailListMap = goodsSkuDetailList.stream().collect(Collectors.groupingBy(OrderUsePaymentActivityGoodsRequest::getEid));
        // 校验是否允许参与活动
        OrderFirstInfoDTO orderFirstInfoDTO = firstInfoApi.queryOrderFirstInfo(request.getCurrentEid(), OrderTypeEnum.B2B);
        boolean newVisitor = ObjectUtil.isNull(orderFirstInfoDTO);

        Long platformActivityId = request.getPlatformActivityId();
        Long platformRuleId = request.getPlatformRuleId();
        List<OrderUsePaymentActivityGoodsDTO> platformResultGoodsDetailList = PojoUtils.map(goodsSkuDetailList, OrderUsePaymentActivityGoodsDTO.class);
        Map<String, OrderUsePaymentActivityGoodsDTO> orderUsePaymentActivityGoodsDTOMap = platformResultGoodsDetailList.stream().collect(Collectors.toMap(OrderUsePaymentActivityGoodsDTO::getRelationId, e -> e, (k1, k2) -> k1));

        if (Objects.nonNull(platformActivityId) && Objects.nonNull(platformRuleId)) {
            MarketingPayPromotionActivityDO platformActivityDO = payPromotionActivityService.getById(platformActivityId);

            List<OrderUsePaymentActivityGoodsRequest> platformGoodsSkuDetailList = goodsSkuDetailList.stream().filter(e -> platformActivityDO.getPayType().contains(e.getPaymentMethod() + "")).collect(Collectors.toList());
            if (platformActivityDO.getConditionOther().contains("1") && !newVisitor) {
                log.info("orderUsePaymentActivityShareAmountBudget 非新客用户，不允许参与新客活动 活动id:[{}]", platformActivityDO.getId());
                throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
            }
            checkOrderActivity(platformActivityDO, request.getCurrentEid(), platformGoodsSkuDetailList, platformRuleId, orderUsePaymentActivityGoodsDTOMap);

            orderUsePaymentActivityDTO.setActivityName(platformActivityDO.getName());
            orderUsePaymentActivityDTO.setPlatformPaymentActivityId(platformActivityId);
            orderUsePaymentActivityDTO.setPlatformRuleId(platformRuleId);
            orderUsePaymentActivityDTO.setBear(platformActivityDO.getBear());
            orderUsePaymentActivityDTO.setPlatformRatio(1 == platformActivityDO.getBear() ? new BigDecimal("100") : BigDecimal.ZERO);
            orderUsePaymentActivityDTO.setBusinessRatio(2 == platformActivityDO.getBear() ? new BigDecimal("100") : BigDecimal.ZERO);
        }
        List<OrderUsePaymentActivityGoodsDTO> usePaymentActivityGoodsDTOList = orderUsePaymentActivityGoodsDTOMap.values().stream().collect(Collectors.toList());
        Map<Long, List<OrderUsePaymentActivityGoodsDTO>> longListMap = usePaymentActivityGoodsDTOList.stream().collect(Collectors.groupingBy(OrderUsePaymentActivityGoodsDTO::getEid));

        List<OrderUsePaymentActivityEnterpriseDTO> enterpriseGoodsList = new ArrayList<>();
        goodsSkuDetailListMap.forEach((sellerEid, item) -> {
            OrderUsePaymentActivityGoodsRequest activityGoodsRequest = item.get(0);
            Integer paymentMethod = activityGoodsRequest.getPaymentMethod();
            Long shopActivityId = activityGoodsRequest.getShopActivityId();
            Long shopRuleId = activityGoodsRequest.getShopRuleId();
            List<OrderUsePaymentActivityGoodsDTO> activityGoodsDTOList = longListMap.get(sellerEid);
            Map<String, OrderUsePaymentActivityGoodsDTO> activityGoodsDTOMap = activityGoodsDTOList.stream().collect(Collectors.toMap(OrderUsePaymentActivityGoodsDTO::getRelationId, e -> e, (k1, k2) -> k1));
            OrderUsePaymentActivityEnterpriseDTO orderUsePaymentActivityEnterpriseDTO = new OrderUsePaymentActivityEnterpriseDTO();
            orderUsePaymentActivityEnterpriseDTO.setEid(sellerEid);
            if (Objects.nonNull(shopActivityId) && Objects.nonNull(shopRuleId)) {

                MarketingPayPromotionActivityDO shopActivityDO = payPromotionActivityService.getById(shopActivityId);
                // 支付方式校验
                if (!shopActivityDO.getPayType().contains(paymentMethod + "")) {
                    log.info("orderUsePaymentActivityShareAmountBudget 非新客用户，不允许参与新客活动 活动id:[{}], 卖家:[{}]", shopActivityDO.getId(), sellerEid);
                    throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
                }
                if (shopActivityDO.getConditionOther().contains("1") && !newVisitor) {
                    log.info("orderUsePaymentActivityShareAmountBudget 非新客用户，不允许参与新客活动 活动id:[{}], 卖家:[{}]", shopActivityDO.getId(), sellerEid);
                    throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
                }
                checkOrderActivity(shopActivityDO, request.getCurrentEid(), item, shopRuleId, activityGoodsDTOMap);

                orderUsePaymentActivityEnterpriseDTO.setActivityName(shopActivityDO.getName());
                orderUsePaymentActivityEnterpriseDTO.setShopActivityId(shopActivityId);
                orderUsePaymentActivityEnterpriseDTO.setShopRuleId(shopRuleId);
                orderUsePaymentActivityEnterpriseDTO.setBear(shopActivityDO.getBear());
                orderUsePaymentActivityEnterpriseDTO.setPlatformRatio(1 == shopActivityDO.getBear() ? new BigDecimal("100") : BigDecimal.ZERO);
                orderUsePaymentActivityEnterpriseDTO.setBusinessRatio(2 == shopActivityDO.getBear() ? new BigDecimal("100") : BigDecimal.ZERO);
            }
            List<OrderUsePaymentActivityGoodsDTO> resultGoodsDetailList = activityGoodsDTOMap.values().stream().collect(Collectors.toList());
            BigDecimal businessDiscountAmount = BigDecimal.ZERO;
            BigDecimal platformDiscountAmount = BigDecimal.ZERO;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (OrderUsePaymentActivityGoodsDTO orderUsePaymentActivityGoodsDTO : resultGoodsDetailList) {
                if (Objects.isNull(orderUsePaymentActivityGoodsDTO.getBusinessShareAmount())) {
                    orderUsePaymentActivityGoodsDTO.setBusinessShareAmount(BigDecimal.ZERO);
                }
                if (Objects.isNull(orderUsePaymentActivityGoodsDTO.getPlatformShareAmount())) {
                    orderUsePaymentActivityGoodsDTO.setPlatformShareAmount(BigDecimal.ZERO);
                }
                businessDiscountAmount = businessDiscountAmount.add(orderUsePaymentActivityGoodsDTO.getBusinessShareAmount());
                platformDiscountAmount = platformDiscountAmount.add(orderUsePaymentActivityGoodsDTO.getPlatformShareAmount());
                totalAmount = totalAmount.add(orderUsePaymentActivityGoodsDTO.getGoodsAmount());
            }
            orderUsePaymentActivityEnterpriseDTO.setBusinessDiscountAmount(businessDiscountAmount);
            orderUsePaymentActivityEnterpriseDTO.setPlatformDiscountAmount(platformDiscountAmount);
            orderUsePaymentActivityEnterpriseDTO.setTotalAmount(totalAmount);
            orderUsePaymentActivityEnterpriseDTO.setGoodsDetailList(resultGoodsDetailList);
            enterpriseGoodsList.add(orderUsePaymentActivityEnterpriseDTO);
        });
        BigDecimal totalBusinessDiscountAmount = BigDecimal.ZERO;
        BigDecimal totalPlatformDiscountAmount = BigDecimal.ZERO;
        for (OrderUsePaymentActivityEnterpriseDTO orderUsePaymentActivityEnterpriseDTO : enterpriseGoodsList) {
            totalBusinessDiscountAmount = totalBusinessDiscountAmount.add(orderUsePaymentActivityEnterpriseDTO.getBusinessDiscountAmount());
            totalPlatformDiscountAmount = totalPlatformDiscountAmount.add(orderUsePaymentActivityEnterpriseDTO.getPlatformDiscountAmount());
        }
        orderUsePaymentActivityDTO.setTotalBusinessDiscountAmount(totalBusinessDiscountAmount);
        orderUsePaymentActivityDTO.setTotalPlatformDiscountAmount(totalPlatformDiscountAmount);
        orderUsePaymentActivityDTO.setEnterpriseGoodsList(enterpriseGoodsList);

        log.info("orderUsePaymentActivityShareAmountBudget result:[{}]", JSON.toJSONString(orderUsePaymentActivityDTO));
        return orderUsePaymentActivityDTO;
    }

    @Transactional
    public boolean savePayPromotionRecord(List<SavePayPromotionRecordRequest> requestList) {
        log.info("savePayPromotionRecord request:[{}]", JSON.toJSONString(requestList));
        // 1.保存记录；2.参与次数新增
        List<MarketingPayPromotionParticipateDO> shopRecord = new ArrayList<>();
        Long platformActivityId = null;
        int platformActivityTimes = 0;
        Long opUserId = 0L;
        Date opTime = new Date();
        List<Long> shopActivityIdList = new ArrayList<>();
        for (SavePayPromotionRecordRequest request : requestList) {
            opUserId = request.getOpUserId();
            opTime = request.getOpTime();
            if (Objects.nonNull(request.getShopActivityId())) {
                MarketingPayPromotionParticipateDO promotionParticipateDO = PojoUtils.map(request, MarketingPayPromotionParticipateDO.class);
                promotionParticipateDO.setParticipateTime(new Date());
                shopActivityIdList.add(request.getShopActivityId());
                promotionParticipateDO.setMarketingPayId(request.getShopActivityId());
                promotionParticipateDO.setRuleId(request.getShopRuleId());
                shopRecord.add(promotionParticipateDO);
            }
            if (Objects.nonNull(request.getPlatformActivityId())) {
                MarketingPayPromotionParticipateDO promotionParticipateDO = PojoUtils.map(request, MarketingPayPromotionParticipateDO.class);
                promotionParticipateDO.setParticipateTime(new Date());
                platformActivityId = request.getPlatformActivityId();
                platformActivityTimes++;
                promotionParticipateDO.setMarketingPayId(request.getPlatformActivityId());
                promotionParticipateDO.setRuleId(request.getPlatformRuleId());
                shopRecord.add(promotionParticipateDO);
            }
        }
        if (CollUtil.isNotEmpty(shopRecord)) {
            log.info("savePayPromotionRecord marketingPayPromotionParticipateService saveBatch:[{}]", JSON.toJSONString(shopRecord));
            marketingPayPromotionParticipateService.saveBatch(shopRecord);
        }
        if (Objects.nonNull(platformActivityId)) {
            log.info("savePayPromotionRecord payPromotionActivityService addRecordTimes platformActivityId:[{}],platformActivityTimes:[{}]", platformActivityId, platformActivityTimes);
            payPromotionActivityService.addRecordTimes(platformActivityId, platformActivityTimes, opUserId, opTime);
        }
        if (CollUtil.isNotEmpty(shopActivityIdList)) {
            log.info("savePayPromotionRecord payPromotionActivityService shopActivityIdList:[{}]", shopActivityIdList);
            for (Long shopActivityId : shopActivityIdList) {
                payPromotionActivityService.addRecordTimes(shopActivityId, 1, opUserId, opTime);
            }
        }
        return true;
    }

    /**
     * 校验支付促销活动（支付促销金额分摊）
     *
     * @param activityDO 支付促销活动信息
     * @param buyerEid 购买者企业id
     * @param item 购买的商品信息
     * @param ruleId 规则id(支付促销满足的活动规则阶梯)
     * @param orderUsePaymentActivityGoodsDTOMap 分摊信息
     */
    private void checkOrderActivity(MarketingPayPromotionActivityDO activityDO, Long buyerEid, List<OrderUsePaymentActivityGoodsRequest> item, Long ruleId, Map<String, OrderUsePaymentActivityGoodsDTO> orderUsePaymentActivityGoodsDTOMap) {
        log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity start ,活动id:[{}], ruleId:[{}], buyerEid:[{}], goodsList:[{}]", activityDO.getId(), ruleId, buyerEid, JSON.toJSONString(item));
        {
            Integer maxGiveNum = activityDO.getMaxGiveNum();
            Integer records = marketingPayPromotionParticipateService.countRecordByActivityIdAndEid(activityDO.getId(), buyerEid);
            if (maxGiveNum != 0 && records >= maxGiveNum) {
                throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
            }
        }

        List<OrderUsePaymentActivityGoodsRequest> goodsDetailList;
        // 商家范围筛选
        StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(activityDO.getConditionSellerType());
        if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
            List<MarketingPayPromotionSellerLimitDO> payPromotionSellerLimitDOList = marketingPayPromotionSellerLimitService.listByActivityId(activityDO.getId());
            List<Long> sellerEidList = payPromotionSellerLimitDOList.stream().map(MarketingPayPromotionSellerLimitDO::getEid).collect(Collectors.toList());
            goodsDetailList = item.stream().filter(e -> sellerEidList.contains(e.getEid())).collect(Collectors.toList());
        } else {
            goodsDetailList = item;
        }

        if (CollUtil.isEmpty(goodsDetailList)) {
            log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity match seller fail ,活动id:[{}]", activityDO.getId());
            throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
        }

        StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(activityDO.getConditionGoodsType());
        List<Long> goodsIdList = goodsDetailList.stream().map(OrderUsePaymentActivityGoodsRequest::getGoodsId).collect(Collectors.toList());
        BigDecimal amount = BigDecimal.ZERO;
        {
            // 商品范围筛选
            if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ALL) {
                amount = goodsDetailList.stream().map(i -> {
                    if (i.getGoodsAmount() == null) {
                        return BigDecimal.ZERO;
                    } else {
                        return i.getGoodsAmount();
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
            } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIdList);
                List<Long> sellSpecificationsIdList = goodsDTOList.stream().map(GoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
                List<MarketingPayPlatformGoodsLimitDO> payPlatformGoodsLimitDOList = marketingPayPlatformGoodsLimitService.listByActivityIdAndSellSpecificationsIdList(activityDO.getId(), sellSpecificationsIdList);
                if (CollUtil.isEmpty(payPlatformGoodsLimitDOList)) {
                    log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity match platform goods fail ,活动id:[{}]", activityDO.getId());
                    throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
                }
                Map<Long, Long> goodsIdMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getSellSpecificationsId, (k1, k2) -> k1));
                List<Long> matchSellSpecificationsIdList = payPlatformGoodsLimitDOList.stream().map(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
                for (OrderUsePaymentActivityGoodsRequest queryPaymentActivityDetailRequest : goodsDetailList) {
                    Long sellSpecificationsId = goodsIdMap.get(queryPaymentActivityDetailRequest.getGoodsId());
                    if (matchSellSpecificationsIdList.contains(sellSpecificationsId)) {
                        amount = amount.add(queryPaymentActivityDetailRequest.getGoodsAmount());
                    }
                }
            } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                List<MarketingPayEnterpriseGoodsLimitDO> payEnterpriseGoodsLimitDOList = marketingPayEnterpriseGoodsLimitService.listByActivityIdAndGoodsIdList(activityDO.getId(), goodsIdList);
                if (CollUtil.isEmpty(payEnterpriseGoodsLimitDOList)) {
                    log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity match enterprise goods fail ,活动id:[{}]", activityDO.getId());
                    throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
                }
                List<Long> matchGoodsIdList = payEnterpriseGoodsLimitDOList.stream().map(MarketingPayEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                for (OrderUsePaymentActivityGoodsRequest queryPaymentActivityDetailRequest : goodsDetailList) {
                    if (matchGoodsIdList.contains(queryPaymentActivityDetailRequest.getGoodsId())) {
                        amount = amount.add(queryPaymentActivityDetailRequest.getGoodsAmount());
                    }
                }
            }
        }
        log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity get goods amount 活动id:[{}],amount:[{}]", activityDO.getId(), amount);
        {
            Integer conditionEffective = activityDO.getConditionEffective();
            if (1 == conditionEffective) {
                MarketingPayCalculateRuleDO calculateRuleDO = marketingPayCalculateRuleService.getById(ruleId);
                if (amount.compareTo(BigDecimal.ZERO) == 0) {
                    log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity amount fail 1 活动id:[{}],amount:[{}]", activityDO.getId(), amount);
                    throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
                }
                if (Objects.isNull(calculateRuleDO) || calculateRuleDO.getThresholdValue().compareTo(amount) > 0) {
                    log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity amount fail 2 活动id:[{}],amount:[{}]", activityDO.getId(), amount);
                    throw new BusinessException(PayPromotionErrorCode.PAY_PROMOTION_NOT_MATCH);
                }
                // 金额分摊
                // 分摊总金额
                BigDecimal discountValue = BigDecimal.ZERO;
                if (1 == calculateRuleDO.getType()) {
                    discountValue = calculateRuleDO.getDiscountValue();
                } else if (2 == calculateRuleDO.getType()) {
                    discountValue = amount.subtract(amount.multiply(calculateRuleDO.getDiscountValue()).divide(new BigDecimal("100")));
                }

                BigDecimal discountMax = calculateRuleDO.getDiscountMax();
                if (discountMax.compareTo(BigDecimal.ZERO) > 0 && discountValue.compareTo(discountMax) > 0) {
                    discountValue = discountMax;
                }
                BigDecimal diffDiscountValue = discountValue;
                for (int i = 0; i < goodsDetailList.size(); i++) {
                    OrderUsePaymentActivityGoodsRequest activityGoodsRequest = goodsDetailList.get(i);
                    BigDecimal discountAmount;
                    if (i == goodsDetailList.size() - 1) {
                        discountAmount = diffDiscountValue;
                    } else {
                        discountAmount = discountValue.multiply(activityGoodsRequest.getGoodsAmount()).divide(amount, 2, RoundingMode.HALF_UP);
                    }
                    diffDiscountValue = diffDiscountValue.subtract(discountAmount);
                    OrderUsePaymentActivityGoodsDTO orderUsePaymentActivityGoodsDTO = orderUsePaymentActivityGoodsDTOMap.get(activityGoodsRequest.getRelationId());
                    if (PromotionSponsorTypeEnum.PLATFORM == PromotionSponsorTypeEnum.getByType(activityDO.getSponsorType())) {
                        orderUsePaymentActivityGoodsDTO.setPlatformShareAmount(discountAmount);
                    } else if (PromotionSponsorTypeEnum.MERCHANT == PromotionSponsorTypeEnum.getByType(activityDO.getSponsorType())) {
                        orderUsePaymentActivityGoodsDTO.setBusinessShareAmount(discountAmount);
                    }
                }
            }
        }
        log.info("orderUsePaymentActivityShareAmountBudget checkOrderActivity stop orderUsePaymentActivityGoodsDTOMap:[{}]", JSON.toJSONString(orderUsePaymentActivityGoodsDTOMap));
    }

    /**
     * 根据购买者筛选符合购买者条件的支付促销活动
     *
     * @param activityDOList 支付促销活动信息
     * @param buyerEid 购买者企业id
     * @return 符合条件的支付促销活动
     */
    private List<MarketingPayPromotionActivityDO> matchActivityBuyer(List<MarketingPayPromotionActivityDO> activityDOList, Long buyerEid) {
        log.info("matchStrategyBuyer start buyerEid:[{}], strategyActivityDOList:[{}]", buyerEid, JSONUtil.toJsonStr(activityDOList));
        List<MarketingPayPromotionActivityDO> resultDO = activityDOList.stream().filter(e -> e.getConditionBuyerType() == 1).collect(Collectors.toList());
        if (CollUtil.isEmpty(resultDO)) {
            resultDO = new ArrayList<>();
        }
        List<MarketingPayPromotionActivityDO> assignBuyerActivity = activityDOList.stream().filter(e -> e.getConditionBuyerType() == 2).collect(Collectors.toList());
        List<MarketingPayPromotionActivityDO> rangeBuyerActivity = activityDOList.stream().filter(e -> e.getConditionBuyerType() == 3).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(assignBuyerActivity)) {
            List<Long> assignIdList = assignBuyerActivity.stream().map(MarketingPayPromotionActivityDO::getId).collect(Collectors.toList());
            List<MarketingPayBuyerLimitDO> buyerLimitDOList = marketingPayBuyerLimitService.listByActivityIdListAndEid(assignIdList, buyerEid);
            if (CollUtil.isNotEmpty(buyerLimitDOList)) {
                List<Long> assignActivityIdList = buyerLimitDOList.stream().map(MarketingPayBuyerLimitDO::getMarketingPayId).collect(Collectors.toList());
                List<MarketingPayPromotionActivityDO> assignBuyerActivityList = assignBuyerActivity.stream().filter(e -> assignActivityIdList.contains(e.getId())).collect(Collectors.toList());
                resultDO.addAll(assignBuyerActivityList);
            }
        }

        if (CollUtil.isNotEmpty(rangeBuyerActivity)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
            // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
            if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                log.info("matchStrategyBuyer match buyer fail 1-工业 2-商业 buyerEid:[{}]", buyerEid);
                return resultDO;
            }
            for (MarketingPayPromotionActivityDO activityDO : rangeBuyerActivity) {
                StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(activityDO.getConditionEnterpriseType());
                if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                    String conditionEnterpriseTypeValue = activityDO.getConditionEnterpriseTypeValue();
                    if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                        log.info("matchStrategyBuyer match buyer assign type fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                        continue;
                    }
                }

                StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(activityDO.getConditionUserType());
                if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                    boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                    if (enterpriseMemberStatus) {
                        log.info("matchStrategyBuyer match buyer common fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                    boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                    if (!enterpriseMemberStatus) {
                        log.info("matchStrategyBuyer match buyer all member fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                    List<Integer> conditionUserMemberTypeList = JSON.parseArray(activityDO.getConditionUserMemberType(), Integer.class);
                    if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                        log.info("matchStrategyBuyer match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                        continue;
                    }

                    List<Long> promoterIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                        // 指定推广方会员
                        List<MarketingPayPromoterMemberLimitDO> payPromoterMemberLimitDOList = marketingPayPromoterMemberLimitService.listByActivityIdAndEidList(activityDO.getId());
                        promoterIdList = payPromoterMemberLimitDOList.stream().map(MarketingPayPromoterMemberLimitDO::getEid).collect(Collectors.toList());
                        if (CollUtil.isEmpty(promoterIdList)) {
                            log.info("matchStrategyBuyer match buyer promoter member 1 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                            continue;
                        }
                    }

                    List<Long> memberIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                        // 指定方案会员
                        List<MarketingPayMemberLimitDO> payMemberLimitDOList = marketingPayMemberLimitService.listMemberByActivityId(activityDO.getId());
                        memberIdList = payMemberLimitDOList.stream().map(MarketingPayMemberLimitDO::getMemberId).collect(Collectors.toList());
                        if (CollUtil.isEmpty(memberIdList)) {
                            log.info("matchStrategyBuyer match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                            continue;
                        }
                    }
                    QueryMemberListRecordRequest memberListRecordRequest = new QueryMemberListRecordRequest();
                    memberListRecordRequest.setMemberIdList(memberIdList);
                    memberListRecordRequest.setPromoterIdList(promoterIdList);
                    memberListRecordRequest.setEid(buyerEid);
                    memberListRecordRequest.setCurrentValid(true);
                    List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRecordRequest);
                    if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                        log.info("matchStrategyBuyer match buyer member 3 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, activityDO.getId());
                        continue;
                    }
                }
                resultDO.add(activityDO);

            }
        }

        log.info("matchStrategyBuyer end buyerEid:[{}] --> [{}]", buyerEid, JSONUtil.toJsonStr(resultDO));
        return resultDO;
    }

    /**
     * 根据支付促销活动id查询可用商品列表分页
     *
     * @param activityId 活动id
     * @param buyerEid 商家企业id
     * @return 可用商品列表分页
     */
    public PayPromotionActivityEidOrGoodsIdDTO getGoodsListPageByActivityId(Long activityId, Long buyerEid, Long sellerEid) {
        PayPromotionActivityEidOrGoodsIdDTO activityEidOrGoodsIdDTO = new PayPromotionActivityEidOrGoodsIdDTO();
        MarketingPayPromotionActivityDO payPromotionActivityDO = payPromotionActivityService.getById(activityId);
        if (Objects.isNull(payPromotionActivityDO)) {
            return activityEidOrGoodsIdDTO;
        }
        activityEidOrGoodsIdDTO.setTitle(payPromotionActivityDO.getName());
        StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(payPromotionActivityDO.getConditionGoodsType());

        List<Long> sellerEidList = new ArrayList<>();
        sellerEidList.add(sellerEid);

        if (conditionGoodsTypeEnum != StrategyConditionGoodsTypeEnum.ALL) {
            if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                List<MarketingPayPlatformGoodsLimitDO> platformGoodsLimitDOList = marketingPayPlatformGoodsLimitService.listByActivityIdAndSellSpecificationsIdList(activityId, null);
                List<Long> sellSpecificationsIdList = platformGoodsLimitDOList.stream().map(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
                List<GoodsDTO> goodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIdList, sellerEidList);
                List<Long> goodsIdList = goodsDTOList.stream().map(GoodsDTO::getId).collect(Collectors.toList());
                activityEidOrGoodsIdDTO.setGoodsIdList(goodsIdList);
            } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                List<MarketingPayEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = marketingPayEnterpriseGoodsLimitService.listByActivityIdAndGoodsIdList(activityId, null);
                List<MarketingPayEnterpriseGoodsLimitDO> payEnterpriseGoodsLimitDOList = enterpriseGoodsLimitDOList.stream().filter(e -> sellerEidList.contains(e.getEid())).collect(Collectors.toList());
                List<Long> goodsIdList = payEnterpriseGoodsLimitDOList.stream().map(MarketingPayEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                activityEidOrGoodsIdDTO.setGoodsIdList(goodsIdList);
            } else {
                throw new BusinessException(StrategyErrorCode.STRATEGY_ERROR);
            }
        } else {
            activityEidOrGoodsIdDTO.setEidList(sellerEidList);
        }
        return activityEidOrGoodsIdDTO;
    }

}
