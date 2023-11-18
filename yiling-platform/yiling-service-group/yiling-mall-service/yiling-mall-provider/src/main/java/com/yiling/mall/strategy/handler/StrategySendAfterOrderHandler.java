package com.yiling.mall.strategy.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.api.StrategyActivityRecordApi;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.api.StrategyEnterpriseGoodsApi;
import com.yiling.marketing.strategy.api.StrategyGiftApi;
import com.yiling.marketing.strategy.api.StrategyMemberApi;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityRecordDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.StrategyMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordListRequest;
import com.yiling.marketing.strategy.enums.StrategyConditionEnterpriseTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderFirstInfoDTO;
import com.yiling.order.order.dto.request.QueryOrderTimeIntervalRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/9/22
 */
@Slf4j
@Service
public class StrategySendAfterOrderHandler {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderFirstInfoApi firstInfoApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategyBuyerApi strategyBuyerApi;

    @DubboReference
    MemberApi memberApi;

    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    StrategyPromoterMemberApi strategyPromoterMemberApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;

    @DubboReference
    StrategyPlatformGoodsApi strategyPlatformGoodsApi;

    @DubboReference
    StrategyEnterpriseGoodsApi strategyEnterpriseGoodsApi;

    @DubboReference
    StrategyGiftApi strategyGiftApi;

    @DubboReference
    StrategyActivityRecordApi strategyActivityRecordApi;

    @DubboReference
    GoodsApi goodsApi;


    public void sendGiftAfterOrder(Long orderId, Integer orderAmountStatusType) {
        log.info("sendGiftAfterOrder start orderId:[{}], orderAmountStatusType:[{}]", orderId, orderAmountStatusType);
        OrderDTO orderDTO = orderApi.getOrderInfo(orderId);
        if (Objects.isNull(orderDTO) || 1 == orderDTO.getOrderType()) {
            return;
        }
        String platformSelected;
        OrderSourceEnum orderSourceEnum = OrderSourceEnum.getByCode(orderDTO.getOrderSource());
        if (orderSourceEnum == OrderSourceEnum.B2B_APP) {
            platformSelected = "1";
        } else if (orderSourceEnum == OrderSourceEnum.SA) {
            platformSelected = "2";
        } else {
            return;
        }
        //选择平台（1-B2B；2-销售助手）逗号隔开
        // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配)
        // 1.按单累计匹配：订单满足条件时，即时计算累计金额从低到高匹配阶梯，依次执行。
        // 2.活动结束整体匹配：活动结束后计算订单累计金额，从高到低匹配阶梯，执行匹配到的最高阶梯。
        List<Integer> orderAmountLadderTypeList = new ArrayList<Integer>() {{
            add(1);
            add(3);
        }};
        List<StrategyActivityDTO> strategyActivityDOList = strategyActivityApi.listEffectiveStrategyByTime(StrategyTypeEnum.ORDER_AMOUNT.getType(), platformSelected, orderAmountLadderTypeList, orderAmountStatusType, orderDTO.getCreateTime());
        if (CollUtil.isEmpty(strategyActivityDOList)) {
            log.info("sendGiftAfterOrder 有效活动为空 orderId:[{}]", orderId);
            return;
        }
        List<StrategyActivityDTO> matchStrategyBuyer = matchStrategyBuyer(strategyActivityDOList, orderDTO.getBuyerEid());
        if (CollUtil.isEmpty(matchStrategyBuyer)) {
            log.info("sendGiftAfterOrder match buyer 有效活动为空 orderId:[{}]", orderId);
            return;
        }
        boolean newVisitor;
        OrderFirstInfoDTO orderFirstInfoDTO = firstInfoApi.queryOrderFirstInfo(orderDTO.getBuyerEid(), OrderTypeEnum.B2B);
        if (ObjectUtil.isNull(orderFirstInfoDTO)) {
            newVisitor = true;
        } else if (orderId.equals(orderFirstInfoDTO.getOrderId())) {
            newVisitor = true;
        } else {
            newVisitor = false;
        }
        for (StrategyActivityDTO strategyActivityDO : matchStrategyBuyer) {
            String platformSelectedStr = strategyActivityDO.getPlatformSelected();
            {
                if (!platformSelectedStr.contains("1") && !platformSelectedStr.contains("2")) {
                    log.info("sendGiftAfterOrder platformSelected fail 活动id:[{}]", strategyActivityDO.getId());
                    continue;
                }
                if (strategyActivityDO.getConditionOther().contains("1") && !newVisitor) {
                    log.info("sendGiftAfterOrder 非新客用户，不允许参与新客活动 活动id:[{}], 订单id:[{}]", strategyActivityDO.getId(), orderId);
                    continue;
                }
                // 限制支付方式
                if (strategyActivityDO.getOrderAmountPayType() == 2) {
                    // 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
                    // 订单累计金额-限制支付方式值(支付方式：1-线下支付 2-账期 3-预付款 4-在线支付和订单支付方式保持一致),逗号隔开
                    String orderAmountPaymentType = strategyActivityDO.getOrderAmountPaymentType();
                    if (!orderAmountPaymentType.contains(orderDTO.getPaymentMethod().toString())) {
                        log.info("sendGiftAfterOrder match orderAmountPaymentType fail ,活动id:[{}], 订单id:[{}]", strategyActivityDO.getId(), orderId);
                        continue;
                    }
                }
            }
            //            log.info("sendGiftAfterOrder start match seller 活动id:[{}],订单id:[{}],活动信息:[{}]", strategyActivityDO.getId(), orderId, JSONUtil.toJsonStr(strategyActivityDO));
            List<Long> sellerEidList = new ArrayList<>();
            {
                // 商家范围筛选
                StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDO.getConditionSellerType());
                if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
                    List<StrategySellerLimitDTO> strategySellerLimitDOList = strategySellerApi.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                    sellerEidList = strategySellerLimitDOList.stream().map(StrategySellerLimitDTO::getEid).collect(Collectors.toList());
                    if (!sellerEidList.contains(orderDTO.getSellerEid())) {
                        log.info("sendGiftAfterOrder match seller fail ,活动id:[{}],订单id:[{}]", strategyActivityDO.getId(), orderId);
                        continue;
                    }
                }
            }
            //            log.info("sendGiftAfterOrder start match goods 活动id:[{}],订单id:[{}]", strategyActivityDO.getId(), orderId);
            StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(strategyActivityDO.getConditionGoodsType());
            {
                // 商品范围筛选
                List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailInfo(orderDTO.getId());
                if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                    List<Long> goodsId = orderDetailDTOList.stream().map(OrderDetailDTO::getDistributorGoodsId).collect(Collectors.toList());
                    List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsId);
                    List<Long> sellSpecificationsIdList = goodsDTOList.stream().map(GoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
                    List<StrategyPlatformGoodsLimitDTO> platformGoodsLimitDOList = strategyPlatformGoodsApi.listByActivityIdAndSellSpecificationsIdList(strategyActivityDO.getId(), sellSpecificationsIdList);
                    if (CollUtil.isEmpty(platformGoodsLimitDOList)) {
                        log.info("sendGiftAfterOrder match platform goods fail ,活动id:[{}],订单id:[{}]", strategyActivityDO.getId(), orderId);
                        continue;
                    }
                } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                    List<Long> goodsIdList = orderDetailDTOList.stream().map(OrderDetailDTO::getDistributorGoodsId).collect(Collectors.toList());
                    List<StrategyEnterpriseGoodsLimitDTO> enterpriseGoodsLimitDOList = strategyEnterpriseGoodsApi.listByActivityIdAndGoodsIdList(strategyActivityDO.getId(), goodsIdList);
                    if (CollUtil.isEmpty(enterpriseGoodsLimitDOList)) {
                        log.info("sendGiftAfterOrder match enterprise goods fail ,活动id:[{}],订单id:[{}]", strategyActivityDO.getId(), orderId);
                        continue;
                    }
                }
            }
            //            log.info("sendGiftAfterOrder start get goods amount 活动id:[{}],订单id:[{}]", strategyActivityDO.getId(), orderId);
            // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配;3-按单匹配)
            Integer orderAmountLadderType = strategyActivityDO.getOrderAmountLadderType();
            BigDecimal amount = BigDecimal.ZERO;
            {
                Map<Long, List<OrderDTO>> longListMap = null;
                if (1 == orderAmountLadderType) {
                    QueryOrderTimeIntervalRequest request = new QueryOrderTimeIntervalRequest();
                    if (CollUtil.isNotEmpty(sellerEidList)) {
                        request.setSellerEidList(sellerEidList);
                    }
                    request.setBuyerEidList(new ArrayList<Long>() {{
                        add(orderDTO.getBuyerEid());
                    }});
                    request.setStartCreateTime(strategyActivityDO.getBeginTime());
                    request.setEndCreateTime(strategyActivityDO.getEndTime());
                    request.setOrderType(OrderTypeEnum.B2B.getCode());
                    // 订单累计金额-限制支付方式(1-全部支付方式;2-部分支付方式)
                    Integer orderAmountPayType = strategyActivityDO.getOrderAmountPayType();
                    if (2 == orderAmountPayType) {
                        List<Integer> orderAmountPaymentType = JSON.parseArray(strategyActivityDO.getOrderAmountPaymentType(), Integer.class);
                        request.setPaymentMethodList(orderAmountPaymentType);
                    }
                    // 选择平台（1-B2B；2-销售助手）逗号隔开
                    if (platformSelectedStr.contains("1") && !platformSelectedStr.contains("2")) {
                        request.setOrderSource(OrderSourceEnum.B2B_APP.getCode());
                    } else if (!platformSelectedStr.contains("1") && platformSelectedStr.contains("2")) {
                        request.setOrderSource(OrderSourceEnum.SA.getCode());
                    }
                    // 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
                    List<Integer> orderStatusList = new ArrayList<Integer>() {{
                        add(25);
                        add(30);
                        add(40);
                        add(100);
                    }};
                    // 订单累计金额-限制订单状态(1-发货计算；2-下单计算)
                    if (1 == strategyActivityDO.getOrderAmountStatusType()) {
                        request.setOrderStatusList(orderStatusList);
                    }
                    request.setCustomerConfirmStatus(-40);
                    log.info("sendGiftAfterOrder get goods amount orderIdList request:[{}]", request);
                    longListMap = orderApi.getTimeIntervalTypeOrder(request);
                    log.info("sendGiftAfterOrder get goods amount orderListMap:[{}]", longListMap);
                } else if (3 == orderAmountLadderType) {
                    longListMap = new HashMap<>();
                    longListMap.put(orderDTO.getBuyerEid(), new ArrayList<OrderDTO>() {{
                        add(orderDTO);
                    }});
                }


                List<OrderDTO> orderDTOList = longListMap.get(orderDTO.getBuyerEid());
                if (CollUtil.isEmpty(orderDTOList)) {
                    log.info("sendGiftAfterOrder match order fail ,活动id:[{}],,订单id:[{}]", strategyActivityDO.getId(), orderId);
                    continue;
                }
                List<Long> orderIdList = orderDTOList.stream().map(OrderDTO::getId).collect(Collectors.toList());
                List<OrderDetailChangeDTO> orderDetailChangeDTOList = orderDetailChangeApi.listByOrderIds(orderIdList);
                if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ALL) {
                    for (OrderDetailChangeDTO orderDetailChangeDTO : orderDetailChangeDTOList) {
                        if (1 == strategyActivityDO.getOrderAmountStatusType()) {
                            BigDecimal deliverAmount = orderDetailChangeDTO.getDeliveryAmount().subtract(orderDetailChangeDTO.getDeliveryTicketDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCashDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCouponDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryPlatformCouponDiscountAmount());
                            BigDecimal returnAmount = orderDetailChangeDTO.getReturnAmount().subtract(orderDetailChangeDTO.getReturnTicketDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCashDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCouponDiscountAmount()).subtract(orderDetailChangeDTO.getReturnPlatformCouponDiscountAmount());
                            amount = amount.add(deliverAmount).subtract(returnAmount);
                        } else if (2 == strategyActivityDO.getOrderAmountStatusType()) {
                            BigDecimal orderAmount = orderDetailChangeDTO.getGoodsAmount().subtract(orderDetailChangeDTO.getTicketDiscountAmount()).subtract(orderDetailChangeDTO.getCashDiscountAmount()).subtract(orderDetailChangeDTO.getCouponDiscountAmount()).subtract(orderDetailChangeDTO.getPlatformCouponDiscountAmount());
                            amount = amount.add(orderAmount);
                        }
                    }
                } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                    List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailByOrderIds(orderIdList);
                    List<Long> goodsId = orderDetailDTOList.stream().map(OrderDetailDTO::getDistributorGoodsId).collect(Collectors.toList());
                    List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsId);
                    Map<Long, Long> goodsIdMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, GoodsDTO::getSellSpecificationsId, (k1, k2) -> k1));
                    List<Long> sellSpecificationsIdList = goodsDTOList.stream().map(GoodsDTO::getSellSpecificationsId).collect(Collectors.toList());
                    List<StrategyPlatformGoodsLimitDTO> platformGoodsLimitDOList = strategyPlatformGoodsApi.listByActivityIdAndSellSpecificationsIdList(strategyActivityDO.getId(), sellSpecificationsIdList);
                    List<Long> matchSellSpecificationsIdList = platformGoodsLimitDOList.stream().map(StrategyPlatformGoodsLimitDTO::getSellSpecificationsId).collect(Collectors.toList());
                    Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeDTOList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, e -> e, (k1, k2) -> k1));
                    for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
                        Long sellSpecificationsId = goodsIdMap.get(orderDetailDTO.getDistributorGoodsId());
                        if (matchSellSpecificationsIdList.contains(sellSpecificationsId)) {
                            OrderDetailChangeDTO orderDetailChangeDTO = orderDetailChangeMap.get(orderDetailDTO.getId());
                            if (1 == strategyActivityDO.getOrderAmountStatusType()) {
                                BigDecimal deliverAmount = orderDetailChangeDTO.getDeliveryAmount().subtract(orderDetailChangeDTO.getDeliveryTicketDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCashDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCouponDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryPlatformCouponDiscountAmount());
                                BigDecimal returnAmount = orderDetailChangeDTO.getReturnAmount().subtract(orderDetailChangeDTO.getReturnTicketDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCashDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCouponDiscountAmount()).subtract(orderDetailChangeDTO.getReturnPlatformCouponDiscountAmount());
                                amount = amount.add(deliverAmount).subtract(returnAmount);
                            } else if (2 == strategyActivityDO.getOrderAmountStatusType()) {
                                BigDecimal orderAmount = orderDetailChangeDTO.getGoodsAmount().subtract(orderDetailChangeDTO.getTicketDiscountAmount()).subtract(orderDetailChangeDTO.getCashDiscountAmount()).subtract(orderDetailChangeDTO.getCouponDiscountAmount()).subtract(orderDetailChangeDTO.getPlatformCouponDiscountAmount());
                                amount = amount.add(orderAmount);
                            }
                        }
                    }
                } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                    List<OrderDetailDTO> orderDetailDTOList = orderDetailApi.getOrderDetailByOrderIds(orderIdList);
                    List<Long> goodsIdList = orderDetailDTOList.stream().map(OrderDetailDTO::getDistributorGoodsId).collect(Collectors.toList());
                    List<StrategyEnterpriseGoodsLimitDTO> enterpriseGoodsLimitDOList = strategyEnterpriseGoodsApi.listByActivityIdAndGoodsIdList(strategyActivityDO.getId(), goodsIdList);
                    List<Long> matchGoodsIdList = enterpriseGoodsLimitDOList.stream().map(StrategyEnterpriseGoodsLimitDTO::getGoodsId).collect(Collectors.toList());
                    Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeDTOList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, e -> e, (k1, k2) -> k1));
                    for (OrderDetailDTO orderDetailDTO : orderDetailDTOList) {
                        if (matchGoodsIdList.contains(orderDetailDTO.getDistributorGoodsId())) {
                            OrderDetailChangeDTO orderDetailChangeDTO = orderDetailChangeMap.get(orderDetailDTO.getId());
                            if (1 == strategyActivityDO.getOrderAmountStatusType()) {
                                BigDecimal deliverAmount = orderDetailChangeDTO.getDeliveryAmount().subtract(orderDetailChangeDTO.getDeliveryTicketDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCashDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryCouponDiscountAmount()).subtract(orderDetailChangeDTO.getDeliveryPlatformCouponDiscountAmount());
                                BigDecimal returnAmount = orderDetailChangeDTO.getReturnAmount().subtract(orderDetailChangeDTO.getReturnTicketDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCashDiscountAmount()).subtract(orderDetailChangeDTO.getReturnCouponDiscountAmount()).subtract(orderDetailChangeDTO.getReturnPlatformCouponDiscountAmount());
                                amount = amount.add(deliverAmount).subtract(returnAmount);
                            } else if (2 == strategyActivityDO.getOrderAmountStatusType()) {
                                BigDecimal orderAmount = orderDetailChangeDTO.getGoodsAmount().subtract(orderDetailChangeDTO.getTicketDiscountAmount()).subtract(orderDetailChangeDTO.getCashDiscountAmount()).subtract(orderDetailChangeDTO.getCouponDiscountAmount()).subtract(orderDetailChangeDTO.getPlatformCouponDiscountAmount());
                                amount = amount.add(orderAmount);
                            }
                        }
                    }
                }
            }
            log.info("sendGiftAfterOrder start get goods amount 活动id:[{}],订单id:[{}],amount:[{}]", strategyActivityDO.getId(), orderId, amount);
            {
                List<StrategyAmountLadderDTO> amountLadderDOList = strategyActivityApi.listAmountLadderByActivityId(strategyActivityDO.getId());
                BigDecimal finalAmount = amount;
                List<StrategyAmountLadderDTO> matchAmountLadderDOList = amountLadderDOList.stream().filter(e -> finalAmount.compareTo(e.getAmountLimit()) >= 0).collect(Collectors.toList());
                if (CollUtil.isEmpty(matchAmountLadderDOList)) {
                    log.info("sendGiftAfterOrder matchAmountLadderDOList fail 活动id:[{}],buyer:[{}],amount:[{}]", strategyActivityDO.getId(), orderDTO.getBuyerEid(), finalAmount);
                    continue;
                }
                if (1 == orderAmountLadderType) {
                    for (StrategyAmountLadderDTO strategyAmountLadderDO : matchAmountLadderDOList) {
                        List<StrategyGiftDTO> strategyGiftDOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDO.getId(), strategyAmountLadderDO.getId());
                        QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
                        activityRecordListRequest.setMarketingStrategyId(strategyActivityDO.getId());
                        activityRecordListRequest.setStrategyType(strategyActivityDO.getStrategyType());
                        activityRecordListRequest.setLadderId(strategyAmountLadderDO.getId());
                        activityRecordListRequest.setEid(orderDTO.getBuyerEid());
                        List<StrategyActivityRecordDTO> activityRecordDOList = strategyActivityRecordApi.listByCondition(activityRecordListRequest);
                        if (CollUtil.isEmpty(activityRecordDOList)) {
                            // 赠送赠品
                            strategyActivityApi.sendGift(strategyActivityDO, orderDTO.getBuyerEid(), strategyGiftDOList, strategyAmountLadderDO.getId(), orderId, null);
                        } else {
                            log.info("sendGiftAfterOrder fail 赠送赠品记录已存在1 活动id:[{}],订单id:[{}],记录:[{}]", strategyActivityDO.getId(), orderId, activityRecordDOList);
                        }
                    }
                } else if (3 == orderAmountLadderType) {
                    // 按单匹配：订单满足条件时，按当前订单金额从低到高匹配阶梯，执行匹配到的最高阶梯。
                    List<StrategyAmountLadderDTO> amountLadderList = matchAmountLadderDOList.stream().sorted(Comparator.comparing(StrategyAmountLadderDTO::getAmountLimit).reversed()).collect(Collectors.toList());
                    StrategyAmountLadderDTO amountLadderDO = amountLadderList.get(0);
                    List<StrategyGiftDTO> strategyGiftDOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDO.getId(), amountLadderDO.getId());
                    QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
                    activityRecordListRequest.setMarketingStrategyId(strategyActivityDO.getId());
                    activityRecordListRequest.setStrategyType(strategyActivityDO.getStrategyType());
                    activityRecordListRequest.setLadderId(amountLadderDO.getId());
                    activityRecordListRequest.setEid(orderDTO.getBuyerEid());
                    activityRecordListRequest.setOrderId(orderId);
                    List<StrategyActivityRecordDTO> activityRecordDOList = strategyActivityRecordApi.listByCondition(activityRecordListRequest);
                    if (CollUtil.isEmpty(activityRecordDOList)) {
                        // 赠送赠品
                        strategyActivityApi.sendGift(strategyActivityDO, orderDTO.getBuyerEid(), strategyGiftDOList, amountLadderDO.getId(), orderDTO.getId(), null);
                    } else {
                        log.info("sendGiftAfterOrder fail 赠送赠品记录已存在2 活动id:[{}],订单id:[{}],记录:[{}]", strategyActivityDO.getId(), orderId, activityRecordDOList);
                    }
                }
            }
            log.info("sendGiftAfterOrder stop 活动id:[{}],订单id:[{}]", strategyActivityDO.getId(), orderId);
        }
    }

    private List<StrategyActivityDTO> matchStrategyBuyer(List<StrategyActivityDTO> strategyActivityDOList, Long buyerEid) {
        log.info("matchStrategyBuyer start buyerEid:[{}], strategyActivityDOList:[{}]", buyerEid, JSONUtil.toJsonStr(strategyActivityDOList));
        List<StrategyActivityDTO> resultDO = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 1).collect(Collectors.toList());

        List<StrategyActivityDTO> assignBuyerStrategy = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 2).collect(Collectors.toList());
        List<StrategyActivityDTO> rangeBuyerStrategy = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 3).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(assignBuyerStrategy)) {
            List<Long> assignIdList = assignBuyerStrategy.stream().map(StrategyActivityDTO::getId).collect(Collectors.toList());
            List<StrategyBuyerLimitDTO> assignStrategyBuyerLimitDOList = strategyBuyerApi.listByActivityIdListAndEid(assignIdList, buyerEid);
            if (CollUtil.isNotEmpty(assignStrategyBuyerLimitDOList)) {
                List<Long> assignStrategyIdList = assignStrategyBuyerLimitDOList.stream().map(StrategyBuyerLimitDTO::getMarketingStrategyId).collect(Collectors.toList());
                List<StrategyActivityDTO> assignBuyerStrategyList = assignBuyerStrategy.stream().filter(e -> assignStrategyIdList.contains(e.getId())).collect(Collectors.toList());
                resultDO.addAll(assignBuyerStrategyList);
            }
        }

        if (CollUtil.isNotEmpty(rangeBuyerStrategy)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
            // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
            if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                log.info("matchStrategyBuyer match buyer fail 1-工业 2-商业 buyerEid:[{}]", buyerEid);
                return resultDO;
            }
            for (StrategyActivityDTO strategyActivityDO : rangeBuyerStrategy) {
                StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(strategyActivityDO.getConditionEnterpriseType());
                if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                    String conditionEnterpriseTypeValue = strategyActivityDO.getConditionEnterpriseTypeValue();
                    if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                        log.info("matchStrategyBuyer match buyer assign type fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                }
                StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(strategyActivityDO.getConditionUserType());
                if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                    boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                    if (enterpriseMemberStatus) {
                        log.info("matchStrategyBuyer match buyer common fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                    boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                    if (!enterpriseMemberStatus) {
                        log.info("matchStrategyBuyer match buyer all member fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                    List<Integer> conditionUserMemberTypeList = JSON.parseArray(strategyActivityDO.getConditionUserMemberType(), Integer.class);
                    if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                        log.info("matchStrategyBuyer match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }

                    List<Long> promoterIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                        // 指定推广方会员
                        List<StrategyPromoterMemberLimitDTO> promoterMemberLimitDOList = strategyPromoterMemberApi.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                        promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDTO::getEid).collect(Collectors.toList());
                        if (CollUtil.isEmpty(promoterIdList)) {
                            log.info("matchStrategyBuyer match buyer promoter member 1 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }

                    List<Long> memberIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                        // 指定方案会员
                        List<StrategyMemberLimitDTO> strategyMemberLimitDOList = strategyMemberApi.listMemberByActivityId(strategyActivityDO.getId());
                        memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDTO::getMemberId).collect(Collectors.toList());
                        if (CollUtil.isEmpty(memberIdList)) {
                            log.info("matchStrategyBuyer match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
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
                        log.info("matchStrategyBuyer match buyer member 3 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                }
                resultDO.add(strategyActivityDO);
            }
        }

        log.info("matchStrategyBuyer end buyerEid:[{}] --> [{}]", buyerEid, JSONUtil.toJsonStr(resultDO));
        return resultDO;
    }

}
