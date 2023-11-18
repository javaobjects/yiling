package com.yiling.mall.strategy.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.yiling.marketing.strategy.enums.StrategyConditionBuyerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionEnterpriseTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
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
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/9/22
 */
@Slf4j
@Service
public class StrategyEndAutoJobHandler {

    @DubboReference
    StrategyActivityApi strategyActivityApi;

    @DubboReference
    StrategySellerApi strategySellerApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderFirstInfoApi firstInfoApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StrategyBuyerApi strategyBuyerApi;

    @DubboReference
    MemberApi memberApi;

    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;

    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @DubboReference
    StrategyMemberApi strategyMemberApi;

    @DubboReference
    StrategyPromoterMemberApi strategyPromoterMemberApi;

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

    public void strategyEndAutoJobHandler(Date startTime, Date stopTime) {
        log.info("strategyEndAutoJobHandler startTime:{} ,stopTime:{}", startTime, stopTime);
        // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配)
        List<StrategyActivityDTO> strategyActivityDOList = strategyActivityApi.listStopAmountStrategyActivity(startTime, stopTime);
        log.info("strategyEndAutoJobHandler match strategyActivityDOList:[{}]", JSONUtil.toJsonStr(strategyActivityDOList));
        for (StrategyActivityDTO strategyActivityDO : strategyActivityDOList) {
            log.info("strategyEndAutoJobHandler start 活动id:[{}]", strategyActivityDO.getId());
            String platformSelected = strategyActivityDO.getPlatformSelected();
            if (!platformSelected.contains("1") && !platformSelected.contains("2")) {
                log.info("strategyEndAutoJobHandler platformSelected fail 活动id:[{}]", strategyActivityDO.getId());
                continue;
            }
            QueryOrderTimeIntervalRequest request = new QueryOrderTimeIntervalRequest();
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
            if (platformSelected.contains("1") && !platformSelected.contains("2")) {
                request.setOrderSource(OrderSourceEnum.B2B_APP.getCode());
            } else if (!platformSelected.contains("1") && platformSelected.contains("2")) {
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
            if (2 == strategyActivityDO.getOrderAmountStatusType()) {
                orderStatusList.add(10);
                orderStatusList.add(20);
            }
            request.setOrderStatusList(orderStatusList);
            request.setCustomerConfirmStatus(-40);
            // 根据订单来源查

            StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDO.getConditionSellerType());
            if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
                List<StrategySellerLimitDTO> sellerLimitDOList = strategySellerApi.listSellerByActivityId(strategyActivityDO.getId());
                List<Long> sellerEidList = sellerLimitDOList.stream().map(StrategySellerLimitDTO::getEid).collect(Collectors.toList());
                request.setSellerEidList(sellerEidList);
                log.info("strategyEndAutoJobHandler 活动id:[{}], request :[{}]", strategyActivityDO.getId(), request);
            }
            Map<Long, List<OrderDTO>> orderListByBuyerEidMap = orderApi.getTimeIntervalTypeOrder(request);
            orderListByBuyerEidMap.forEach((key, value) -> {
                log.info("strategyEndAutoJobHandler operate key:[{}],value:[{}],活动id:[{}]", key, JSONUtil.toJsonStr(value), strategyActivityDO.getId());
                if (strategyActivityDO.getConditionOther().contains("1")) {
                    Boolean newVisitor = firstInfoApi.checkNewVisitor(key, OrderTypeEnum.B2B);
                    if (!newVisitor) {
                        log.info("strategyEndAutoJobHandler 非新客用户，不允许参与新客活动 活动id:[{}],eid:[{}]", strategyActivityDO.getId(), key);
                        return;
                    }
                }

                {
                    StrategyConditionBuyerTypeEnum conditionBuyerTypeEnum = StrategyConditionBuyerTypeEnum.getByType(strategyActivityDO.getConditionBuyerType());
                    EnterpriseDTO enterpriseDTO = enterpriseApi.getById(key);
                    // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
                    if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                        log.info("sendGiftAfterOrder match buyer fail 1-工业 2-商业 ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                        return;
                    }
                    if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ASSIGN) {
                        List<StrategyBuyerLimitDTO> strategyBuyerLimitDOList = strategyBuyerApi.listByActivityIdAndEidList(strategyActivityDO.getId(), new ArrayList<Long>() {{
                            add(key);
                        }});
                        if (CollUtil.isEmpty(strategyBuyerLimitDOList)) {
                            log.info("strategyEndAutoJobHandler match buyer assign fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                            return;
                        }
                    } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.RANGE) {
                        StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(strategyActivityDO.getConditionEnterpriseType());
                        if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                            String conditionEnterpriseTypeValue = strategyActivityDO.getConditionEnterpriseTypeValue();
                            if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                                log.info("strategyEndAutoJobHandler match buyer assign type fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                return;
                            }
                        }
                        StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(strategyActivityDO.getConditionUserType());
                        if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                            boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(key);
                            if (enterpriseMemberStatus) {
                                log.info("strategyEndAutoJobHandler match buyer common fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                return;
                            }
                        } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                            boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(key);
                            if (!enterpriseMemberStatus) {
                                log.info("strategyEndAutoJobHandler match buyer all member fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                return;
                            }
                        } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                            List<Integer> conditionUserMemberTypeList = JSON.parseArray(strategyActivityDO.getConditionUserMemberType(), Integer.class);
                            if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                                log.info("strategyEndAutoJobHandler match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                return;
                            }
                            List<Long> promoterIdList = new ArrayList<>();
                            if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                                // 指定推广方会员
                                List<StrategyPromoterMemberLimitDTO> promoterMemberLimitDOList = strategyPromoterMemberApi.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                                promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDTO::getEid).collect(Collectors.toList());
                                if (CollUtil.isEmpty(promoterIdList)) {
                                    log.info("strategyEndAutoJobHandler match buyer promoter member 1 fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                    return;
                                }
                            }

                            List<Long> memberIdList = new ArrayList<>();
                            if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                                // 指定方案会员
                                List<StrategyMemberLimitDTO> strategyMemberLimitDOList = strategyMemberApi.listMemberByActivityId(strategyActivityDO.getId());
                                memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDTO::getMemberId).collect(Collectors.toList());
                                if (CollUtil.isEmpty(memberIdList)) {
                                    log.info("strategyEndAutoJobHandler match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                    return;
                                }
                            }
                            QueryMemberListRecordRequest memberListRecordRequest = new QueryMemberListRecordRequest();
                            memberListRecordRequest.setMemberIdList(memberIdList);
                            memberListRecordRequest.setPromoterIdList(promoterIdList);
                            memberListRecordRequest.setEid(key);
                            memberListRecordRequest.setCurrentValid(true);
                            List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRecordRequest);
                            if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                                log.info("strategyEndAutoJobHandler match buyer member 3 fail ,buyerEid:[{}], 活动id:[{}]", key, strategyActivityDO.getId());
                                return;
                            }
                        }
                    }
                }
                //                log.info("strategyEndAutoJobHandler matched buyer 活动id:[{}],buyer:[{}]", strategyActivityDO.getId(), key);
                BigDecimal amount = BigDecimal.ZERO;
                {
                    StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(strategyActivityDO.getConditionGoodsType());
                    List<Long> orderIdList = value.stream().map(OrderDTO::getId).collect(Collectors.toList());
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
                log.info("strategyEndAutoJobHandler get goods amount 活动id:[{}],buyer:[{}],amount:[{}]", strategyActivityDO.getId(), key, amount);
                {
                    List<StrategyAmountLadderDTO> amountLadderDOList = strategyActivityApi.listAmountLadderByActivityId(strategyActivityDO.getId());
                    BigDecimal finalAmount = amount;
                    List<StrategyAmountLadderDTO> matchAmountLadderDOList = amountLadderDOList.stream().filter(e -> finalAmount.compareTo(e.getAmountLimit()) >= 0).collect(Collectors.toList());
                    if (CollUtil.isEmpty(matchAmountLadderDOList)) {
                        log.info("strategyEndAutoJobHandler matchAmountLadderDOList fail 活动id:[{}],buyer:[{}],amount:[{}]", strategyActivityDO.getId(), key, finalAmount);
                        return;
                    }
                    List<StrategyAmountLadderDTO> amountLadderList = matchAmountLadderDOList.stream().sorted(Comparator.comparing(StrategyAmountLadderDTO::getAmountLimit).reversed()).collect(Collectors.toList());
                    StrategyAmountLadderDTO amountLadderDO = amountLadderList.get(0);
                    List<StrategyGiftDTO> strategyGiftDOList = strategyGiftApi.listGiftByActivityIdAndLadderId(strategyActivityDO.getId(), amountLadderDO.getId());
                    QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
                    activityRecordListRequest.setMarketingStrategyId(strategyActivityDO.getId());
                    activityRecordListRequest.setStrategyType(strategyActivityDO.getStrategyType());
                    activityRecordListRequest.setLadderId(amountLadderDO.getId());
                    activityRecordListRequest.setEid(key);
                    List<StrategyActivityRecordDTO> activityRecordDOList = strategyActivityRecordApi.listByCondition(activityRecordListRequest);
                    if (CollUtil.isEmpty(activityRecordDOList)) {
                        // 赠送赠品
                        OrderDTO orderDTO = value.get(value.size() - 1);
                        strategyActivityApi.sendGift(strategyActivityDO, key, strategyGiftDOList, amountLadderDO.getId(), orderDTO.getId(), null);
                    } else {
                        log.info("strategyEndAutoJobHandler fail 赠送赠品记录已存在 活动id:[{}],buyer:[{}],记录:[{}]", strategyActivityDO.getId(), key, activityRecordDOList);
                    }
                }
                log.info("strategyEndAutoJobHandler stop 活动id:[{}],buyer:[{}]", strategyActivityDO.getId(), key);
            });
            log.info("strategyEndAutoJobHandler stop 活动id:[{}]", strategyActivityDO.getId());
        }
    }
}
