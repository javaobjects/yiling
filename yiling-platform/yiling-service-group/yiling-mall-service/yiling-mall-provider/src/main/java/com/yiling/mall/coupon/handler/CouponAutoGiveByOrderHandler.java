package com.yiling.mall.coupon.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.mall.coupon.util.CouponListenerUtil;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponGetEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponOrderStatusEnum;
import com.yiling.marketing.common.enums.CouponPayMethodTypeEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoDetailRequest;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveGoodsLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityGiveDetailRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单发货后自动发券
 *
 * @author: houjie.sun
 * @date: 2021/11/25
 */
@Slf4j
@Service
public class CouponAutoGiveByOrderHandler {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    OrderDetailChangeApi orderDetailChangeApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    CouponActivityAutoGiveApi autoGiveApi;
    @Autowired
    private CouponListenerUtil couponListenerUtil;

    /**
     * 订单累积金额 发券规则校验
     *
     * @param order
     * @return
     */
    public List<CouponActivityAutoGiveDetailDTO> checkAccumulateAmountGiveRules(OrderDTO order, EnterpriseDTO enterprise, QueryCouponActivityGiveDetailRequest request, List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList, List<BigDecimal> orderTotalAmountList) {
        /* 订单 */
        String orderNo = order.getOrderNo();
        // 订单累计金额
        Long buyerEid = order.getBuyerEid();
        BigDecimal orderTotalAmount = orderApi.getTotalAmountByBuyerEid(buyerEid);
        if (ObjectUtil.isNull(orderTotalAmount) || orderTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
            log.info( "订单累计金额为0，不能发放优惠券，orederNo:[{" + orderNo + "}]");
            return new ArrayList<>();
        }
        orderTotalAmountList.add(orderTotalAmount);
        // 支付方式
        Integer orderPaymentMethod = order.getPaymentMethod();
        // 状态
        Integer orderStatus = order.getOrderStatus();
        // 下单来源
        Integer orderSource = null;
        if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(order.getOrderType()) && OrderSourceEnum.B2B_APP == OrderSourceEnum.getByCode(order.getOrderSource())) {
            orderSource = PlatformEnum.B2B.getCode();
            // 销售助手只有b2b才发券
        } else if (OrderTypeEnum.B2B == OrderTypeEnum.getByCode(order.getOrderType()) && OrderSourceEnum.SA == OrderSourceEnum.getByCode(order.getOrderSource())) {
            orderSource = PlatformEnum.SALES_ASSIST.getCode();
        }

        /* 订单商品明细 */
        List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(order.getId());
        if (CollUtil.isEmpty(orderDetailChangeList)) {
            log.info("当前订单商品明细不存在，订单累积金额, 不能发放优惠券，orederNo:[{" + orderNo + "}]");
            return new ArrayList<>();
        }

        // 企业类型
        Integer enterpriseType = enterprise.getType();

        /* 会员信息 */
        CurrentMemberForMarketingDTO member = memberApi.getCurrentMemberForMarketing(buyerEid);
        boolean isMember = false;
        if (ObjectUtil.isNotNull(member) && ObjectUtil.equal(1, member.getCurrentMember())) {
            isMember = true;
        }

        /* 平台自动发券活动 */
        autoGiveDetaiList = autoGiveApi.getAllByCondition(request);
        if (CollUtil.isEmpty(autoGiveDetaiList)) {
            log.info("自动发券活动不存在，订单累积金额, 不能发放优惠券，orederNo:[{" + orderNo + "}]");
            return new ArrayList<>();
        }

        /* 优惠券活动 */
        List<CouponActivityDTO> effectiveCouponActivityList = couponListenerUtil.getEffectiveCouponActivity(autoGiveDetaiList);
        if (CollUtil.isEmpty(effectiveCouponActivityList)) {
            log.info("优惠券活动不存在，订单累积金额, 不能发放优惠券，orederNo:[{" + orderNo + "}]");
            return new ArrayList<>();
        }

        /* 自动发放活动记录 历史累计金额 */
        CouponActivityAutoGiveRecordDTO autoGiveRecord = autoGiveApi.getAutoGiveRecordLastOneByEid(buyerEid);
        BigDecimal cumulativeAmountHistory = orderTotalAmount;
        if (ObjectUtil.isNotNull(autoGiveRecord) && ObjectUtil.isNotNull(autoGiveRecord.getCumulativeAmount())) {
            cumulativeAmountHistory = cumulativeAmountHistory.subtract(autoGiveRecord.getCumulativeAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        List<Long> autoGiveIdList = autoGiveDetaiList.stream().map(CouponActivityAutoGiveDetailDTO::getId).distinct().collect(Collectors.toList());
        // 自动发放活动指定的部分会员
        Map<Long, List<CouponActivityAutoGiveEnterpriseLimitDTO>> enterpriseLimitMap = new HashMap<>();
        List<CouponActivityAutoGiveEnterpriseLimitDTO> enterpriseLimitList = autoGiveApi.getEnterpriseLimitByAutoGiveIdList(autoGiveIdList);
        if (CollUtil.isNotEmpty(enterpriseLimitList)) {
            enterpriseLimitMap = enterpriseLimitList.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveEnterpriseLimitDTO::getCouponActivityAutoGiveId));
        }

        // 自动发放活动指定的部分商品
        Map<Long, List<CouponActivityAutoGiveGoodsLimitDTO>> goodsLimitMap = new HashMap<>();
        List<CouponActivityAutoGiveGoodsLimitDTO> goodsLimitList = autoGiveApi.getGoodsLimitByAutoGiveIdList(autoGiveIdList);
        if (CollUtil.isNotEmpty(goodsLimitList)) {
            goodsLimitMap = goodsLimitList.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveGoodsLimitDTO::getCouponActivityAutoId));
        }

        // 校验：累计金额、支付方式、订单状态、下单平台、企业类型、用户类型
        Iterator<CouponActivityAutoGiveDetailDTO> autoGiveIt = autoGiveDetaiList.iterator();
        long nowTime = System.currentTimeMillis();
        while (autoGiveIt.hasNext()) {
            CouponActivityAutoGiveDetailDTO next = autoGiveIt.next();

            // 活动时间有效期
            Date beginTime = next.getBeginTime();
            Date endTime = next.getEndTime();
            if (beginTime.getTime() > nowTime || endTime.getTime() <= nowTime) {
                autoGiveIt.remove();
                continue;
            }

            // 关联优惠券活动
            if (CollUtil.isEmpty(next.getCouponActivityList())) {
                autoGiveIt.remove();
                continue;
            }

            // 累计金额
            BigDecimal cumulative = new BigDecimal(next.getCumulative().toString());
            if (cumulativeAmountHistory.compareTo(cumulative) < 0) {
                autoGiveIt.remove();
                continue;
            }

            // 支付方式
            Integer paymethodLimit = next.getConditionPaymethod();
            CouponPayMethodTypeEnum couponPayMethodTypeEnum = CouponPayMethodTypeEnum.transTypeFromPaymentMethodEnum(orderPaymentMethod);
            if (ObjectUtil.isNull(couponPayMethodTypeEnum)) {
                autoGiveIt.remove();
                continue;
            } else {
                if (ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), paymethodLimit)) {
                    // 全部支付方式，是否在全部支持的类型中
                    if (!CouponPayMethodTypeEnum.isFitWithNotLimit(couponPayMethodTypeEnum.getCode())) {
                        autoGiveIt.remove();
                        continue;
                    }
                } else if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), paymethodLimit)) {
                    // 部分支付方式，是否在已选择的类型中
                    List<String> paymethodValueList = next.getConditionPaymethodValueList();
                    if (!paymethodValueList.contains(couponPayMethodTypeEnum.getCode())) {
                        autoGiveIt.remove();
                        continue;
                    }
                }
            }

            // 订单状态
            Integer orderStatusLimit = next.getConditionOrderStatus();
            if (ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), orderStatusLimit)) {
                // 全部订单状态，是否在全部支持的类型中
                if (!CouponOrderStatusEnum.isFitWithNotLimit(orderStatus)) {
                    autoGiveIt.remove();
                    continue;
                }
            } else if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), paymethodLimit)) {
                // 部分订单状态，是否在已选择的类型中
                List<String> orderStatusValueList = next.getConditionOrderStatusValueList();
                if (!orderStatusValueList.contains(orderStatus)) {
                    autoGiveIt.remove();
                    continue;
                }
            }

            // 下单平台
            Integer orderPlatformLimit = next.getConditionOrderPlatform();
            CouponPlatformTypeEnum platformTypeEnum = CouponPlatformTypeEnum.transTypeFromPlatformEnum(orderSource);
            if (ObjectUtil.isNull(platformTypeEnum)) {
                autoGiveIt.remove();
                continue;
            } else {
                Integer orderSource1 = platformTypeEnum.getCode();
                if (ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), orderPlatformLimit)) {
                    // 全部下单平台，是否在全部支持的类型中
                    if (!CouponPlatformTypeEnum.isFitWithNotLimit(orderSource1)) {
                        autoGiveIt.remove();
                        continue;
                    }
                } else if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), orderPlatformLimit)) {
                    // 部分下单平台，是否在已选择的类型中
                    List<String> paymethodValueList = next.getConditionPaymethodValueList();
                    if (!paymethodValueList.contains(orderSource1)) {
                        autoGiveIt.remove();
                        continue;
                    }
                }
            }

            // 企业类型
            Integer enterpriseTypeLimit = next.getConditionEnterpriseType();
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), enterpriseTypeLimit)) {
                // 部分企业类型，是否在已选择的类型中
                List<String> enterpriseTypeValueList = next.getConditionEnterpriseTypeValueList();
                if (!enterpriseTypeValueList.contains(enterpriseType)) {
                    autoGiveIt.remove();
                    continue;
                }
            }

            // 用户类型
            Integer userTypeLimit = next.getConditionUserType();
            if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.ENTERPRISE.getCode(), userTypeLimit)) {
                // 普通用户
                if (isMember) {
                    autoGiveIt.remove();
                    continue;
                }
            } else if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.MEMBER.getCode(), userTypeLimit)) {
                // 全部会员
                if (!isMember) {
                    autoGiveIt.remove();
                    continue;
                }
            } else if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), userTypeLimit)) {
                // 部分会员
                if (MapUtil.isNotEmpty(enterpriseLimitMap)) {
                    List<CouponActivityAutoGiveEnterpriseLimitDTO> autoGiveEnterpriseLimitList = enterpriseLimitMap.get(next.getId());
                    List<Long> enterpriseLimitIdList = autoGiveEnterpriseLimitList.stream().map(CouponActivityAutoGiveEnterpriseLimitDTO::getEid).distinct().collect(Collectors.toList());
                    if (!enterpriseLimitIdList.contains(buyerEid)) {
                        autoGiveIt.remove();
                        continue;
                    }
                }
            }

            // 是否指定商品
            Integer goodsLimit = next.getConditionGoods();
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), goodsLimit)) {
                // 部分商品
                if (MapUtil.isNotEmpty(goodsLimitMap)) {
                    List<CouponActivityAutoGiveGoodsLimitDTO> autoGiveGoodsLimitList = goodsLimitMap.get(next.getId());
                    List<Long> goodsLimitIdList = autoGiveGoodsLimitList.stream().map(CouponActivityAutoGiveGoodsLimitDTO::getGoodsId).distinct().collect(Collectors.toList());
                    // 订单商品是否可用
                    boolean goodsFlag = false;
                    for (OrderDetailChangeDTO orderDetailChange : orderDetailChangeList) {
                        if (goodsLimitIdList.contains(orderDetailChange.getGoodsId())) {
                            goodsFlag = true;
                        }
                    }
                    if (!goodsFlag) {
                        autoGiveIt.remove();
                        continue;
                    }
                }
            }
        }
        log.info("[订单累计金额]发放活动匹配结果, autoGiveDetaiList:{}", autoGiveDetaiList);
        if (CollUtil.isEmpty(autoGiveDetaiList)) {
            log.info("[订单累计金额]当前订单未匹配到发放活动，orederNo:" + orderNo);
            return new ArrayList<>();
        }
        return autoGiveDetaiList;
    }

    public boolean orderDeliveryHandler(QueryCouponActivityGiveDetailRequest request, OrderDTO orderDTO, EnterpriseDTO enterprise, List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList, List<BigDecimal> orderTotalAmountList) {

        /* 开始发放 */
        long nowTime = System.currentTimeMillis();
        Date date = new Date();
        // 活动已发放记录
        Map<Long, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordMap = couponListenerUtil.getAutoGiveRecordMap(orderDTO.getBuyerEid(), autoGiveDetaiList);
        log.info("活动发放记录"+ JSON.toJSONString(autoGiveRecordMap));
        // 优惠券活动
        List<CouponActivityAutoGiveCouponDetailDTO> autoGiveCouponDetailList = couponListenerUtil.getAutoGiveCouponDetailList(autoGiveDetaiList);
        // 可发放的优惠券活动
        List<CouponActivityDetailDTO> couponActivityCanGiveList = new ArrayList<>();
        Map<Long, CouponActivityDetailDTO> couponActivityCanGiveMap = new HashMap<>();
        // 优惠券活动已生成券数量
        Map<Long, Integer> giveCountMap = new HashMap<>();
        // 优惠券活动校验
        couponListenerUtil.verifiedCouponActivity(nowTime, autoGiveCouponDetailList, couponActivityCanGiveList, couponActivityCanGiveMap, giveCountMap);
        log.info("第一次校验可发放的优惠券活动"+ JSON.toJSONString(autoGiveCouponDetailList));
        // 发放优惠券
        List<SaveCouponRequest> couponList = new ArrayList<>();
        // 发放记录
        List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList = new ArrayList<>();
        // 待更新发放活动
        List<Long> updateAutoGiveIdList = new ArrayList<>();
        // 发放企业信息
        List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList = new ArrayList<>();

        // 处理发放
        couponListenerUtil.handleGive(enterprise, orderTotalAmountList, autoGiveDetaiList, date, autoGiveRecordMap, couponActivityCanGiveMap, giveCountMap, couponList, newAutoGiveRecordList, updateAutoGiveIdList, giveDetailList, CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode());
        log.info("第二次校验可发放的优惠券活动id"+ JSON.toJSONString(updateAutoGiveIdList));
        // 执行发放
        if(CollectionUtils.isEmpty(couponList)){
            log.warn("没有符合条件的优惠券可发放");
            return true;
        }
        return couponListenerUtil.couponExecute(date, couponList, newAutoGiveRecordList, updateAutoGiveIdList, giveDetailList, false);
    }

}
