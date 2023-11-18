package com.yiling.marketing.couponactivity.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityCoexistPromotionEnum;
import com.yiling.marketing.common.enums.CouponActivitySponsorTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponPayMethodTypeEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListDetailRequest;
import com.yiling.marketing.coupon.dto.request.QueryCouponCanUseListRequest;
import com.yiling.marketing.coupon.dto.request.UseMemberCouponRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dao.CouponActivityMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanUseDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetEnterpriseDTO;
import com.yiling.marketing.couponactivity.dto.OrderUseCouponBudgetGoodsDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetGoodsDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponBudgetRequest;
import com.yiling.marketing.couponactivity.dto.request.OrderUseCouponRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityForPurchaseOrderService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Slf4j
@Service
public class CouponActivityForPurchaseOrderServiceImpl extends BaseServiceImpl<CouponActivityMapper, CouponActivityDO>
                                                       implements CouponActivityForPurchaseOrderService {

    @Autowired
    private CouponService                        couponService;
    @Autowired
    private CouponActivityService                couponActivityService;
    @Autowired
    private CouponActivityEnterpriseLimitService couponActivityEnterpriseLimitService;
    @Autowired
    private CouponActivityGoodsLimitService      couponActivityGoodsLimitService;

    @Override
    public List<Integer> suportCouponPayMethodList() {
        log.info("suportCouponPayMethodList result - >", JSON.toJSONString(CouponPayMethodTypeEnum.transTypeToPaymentMethodEnum()));
        return CouponPayMethodTypeEnum.transTypeToPaymentMethodEnum();
    }

    @Override
    public CouponActivityCanUseDTO getCouponCanUseList(QueryCouponCanUseListRequest request) {
        long timeBegin = System.currentTimeMillis();
        log.info("getCouponCanUseList request -> {}", JSONObject.toJSONString(request));
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getPlatform()) || ObjectUtil.isNull(request.getCurrentEid())
            || CollUtil.isEmpty(request.getGoodsDetailList())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        TreeSet<QueryCouponCanUseListDetailRequest> goodsDetailSet = new TreeSet<>((o1, o2) -> o1.getGoodsId().compareTo(o2.getGoodsId()));
        goodsDetailSet.addAll(request.getGoodsDetailList());
        List<QueryCouponCanUseListDetailRequest> goodsDetailList = new ArrayList<>(goodsDetailSet);
        CouponActivityCanUseDTO couponActivityCanUse = new CouponActivityCanUseDTO();
        try {
            Long currentEid = request.getCurrentEid();
            List<Long> eids = goodsDetailList.stream().map(QueryCouponCanUseListDetailRequest::getEid).distinct().collect(Collectors.toList());
            Map<Long, List<QueryCouponCanUseListDetailRequest>> eidMap = goodsDetailList.stream()
                .collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getEid));
            Map<Long, List<QueryCouponCanUseListDetailRequest>> goodsIdMap = goodsDetailList.stream()
                .collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getGoodsId));

            // 查询已领取可使用优惠券
            List<CouponDO> couponList = couponService.getEffectiveCanUseListByEid(currentEid, eids);
            if (CollUtil.isEmpty(couponList)) {
                return null;
            }
//            Map<Long, List<CouponDO>> couponMap = couponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));

            // 查询优惠券活动
            List<Long> couponActivityIds = couponList.stream().map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
            List<CouponActivityDO> couponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIds, 1, 1);
            log.info("getCouponCanUseList couponActivityList -> {}", JSONObject.toJSONString(couponActivityList));
            if (CollUtil.isEmpty(couponActivityList)) {
                return null;
            }
            // 过滤平台类型
            CouponPlatformTypeEnum platformTypeEnum = CouponPlatformTypeEnum.transTypeFromPlatformEnum(request.getPlatform());
            if (ObjectUtil.isNull(platformTypeEnum)) {
                return null;
            }
            List<CouponActivityDO> effectiveCouponActivityList = new ArrayList<>();
            couponActivityList.forEach(c -> {
                if (ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getPlatformLimit())) {
                    effectiveCouponActivityList.add(c);
                } else if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getPlatformLimit())) {
                    List<Integer> selecteds = Convert.toList(Integer.class, c.getPlatformSelected());
                    if (selecteds.contains(platformTypeEnum.getCode())) {
                        effectiveCouponActivityList.add(c);
                    }
                }
            });
            if (CollUtil.isEmpty(effectiveCouponActivityList)) {
                return null;
            }

            // 优惠券-优惠券活动关系
            Map<Long, List<CouponDO>> couponAndActivityMap = couponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
            // 商品-优惠券活动关系
            Map<Long, List<CouponActivityDO>> goodsAndActivityMap = new HashMap<>();

            // 平台创建的优惠券活动，全部供应商、全部商品可使用的
            Map<Long, CouponActivityDO> notEnterpriseLimiMap = new HashMap<>();
            List<CouponActivityDO> notEnterpriseLimitList = effectiveCouponActivityList.stream()
                .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getEnterpriseLimit())
                             && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit())
                             && ObjectUtil.isNotNull(c.getEid()) && c.getEid().intValue() == 0)
                .collect(Collectors.toList());
            log.info("getCouponCanUseList notEnterpriseLimitList -> {}", JSONObject.toJSONString(notEnterpriseLimitList));
            if (CollUtil.isNotEmpty(notEnterpriseLimitList)) {
                notEnterpriseLimiMap = notEnterpriseLimitList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
                // 全部商品可使用优惠券，后续校验门槛金额
                for (QueryCouponCanUseListDetailRequest goodsDetailRequest : goodsDetailList) {
                    for (CouponActivityDO couponActivityDO : notEnterpriseLimitList) {
                        // 商品可使用优惠券
                        List<CouponActivityDO> couponActivityLsit = goodsAndActivityMap.get(goodsDetailRequest.getGoodsId());
                        if(CollUtil.isEmpty(couponActivityLsit)){
                            goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), new ArrayList(){{add(couponActivityDO);}});
                        } else {
                            couponActivityLsit.add(couponActivityDO);
                            goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), couponActivityLsit);
                        }
                    }
                }
            }

            // 平台创建的优惠券活动，部分供应商、全部商品可使用的
            List<CouponActivityDO> enterpriseLimitList = effectiveCouponActivityList.stream()
                .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getEnterpriseLimit())
                             && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit())
                             && ObjectUtil.isNotNull(c.getEid()) && c.getEid().intValue() == 0)
                .collect(Collectors.toList());
            log.info("getCouponCanUseList enterpriseLimitList -> {}", JSONObject.toJSONString(enterpriseLimitList));
            if (CollUtil.isNotEmpty(enterpriseLimitList)) {
                List<Long> enterpriseLimitActivityIdList = enterpriseLimitList.stream().map(CouponActivityDO::getId).distinct()
                    .collect(Collectors.toList());
                // 查询可使用供应商信息
                List<CouponActivityEnterpriseLimitDO> enterpriseLimitInfoList = couponActivityEnterpriseLimitService
                    .getByCouponActivityIdList(enterpriseLimitActivityIdList);
                log.info("getCouponCanUseList enterpriseLimitInfoList -> {}", JSONObject.toJSONString(enterpriseLimitInfoList));

                if (CollUtil.isNotEmpty(enterpriseLimitInfoList)) {
                    // 是否包含当前店铺
                    List<CouponActivityEnterpriseLimitDO> enterpriseActivityList = enterpriseLimitInfoList.stream().filter(e -> eids.contains(e.getEid())).collect(Collectors.toList());
                    if(CollUtil.isNotEmpty(enterpriseActivityList)){
                        Map<Long,List<CouponActivityEnterpriseLimitDO>> enterpriseActivityMap = enterpriseActivityList.stream().collect(Collectors.groupingBy(CouponActivityEnterpriseLimitDO::getCouponActivityId));
                        for (QueryCouponCanUseListDetailRequest goodsDetailRequest : goodsDetailList) {
                            for (CouponActivityDO couponActivityDO : enterpriseLimitList) {
                                List<CouponActivityEnterpriseLimitDO> enterpriseLimit = enterpriseActivityMap.get(couponActivityDO.getId());
                                if(CollUtil.isEmpty(enterpriseLimit)){
                                    continue;
                                }
                                List<Long> eidLimitList = enterpriseLimit.stream().map(CouponActivityEnterpriseLimitDO::getEid).distinct().collect(Collectors.toList());
                                if(!eidLimitList.contains(goodsDetailRequest.getEid())){
                                    continue;
                                }
                                // 商品可使用优惠券
                                List<CouponActivityDO> couponActivityLsit = goodsAndActivityMap.get(goodsDetailRequest.getGoodsId());
                                if(CollUtil.isEmpty(couponActivityLsit)){
                                    goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), new ArrayList(){{add(couponActivityDO);}});
                                } else {
                                    couponActivityLsit.add(couponActivityDO);
                                    goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), couponActivityLsit);
                                }
                            }
                        }
                    }
                }
            }

            // 商家创建的优惠券，全部商品可用
            List<CouponActivityDO> shopCouponActivityList = effectiveCouponActivityList.stream()
                .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit()) && ObjectUtil.isNotNull(c.getEid())
                             && c.getEid().intValue() != 0)
                .collect(Collectors.toList());
            Map<Long, List<CouponActivityDO>> eidCouponActivityMap = shopCouponActivityList.stream().collect(Collectors.groupingBy(CouponActivityDO::getEid));
            for (QueryCouponCanUseListDetailRequest goodsDetailRequest : goodsDetailList) {
                List<CouponActivityDO> couponActivityDOS = eidCouponActivityMap.get(goodsDetailRequest.getEid());
                if(CollUtil.isNotEmpty(couponActivityDOS)){
                    // 商品可使用优惠券
                    List<CouponActivityDO> couponActivityLsit = goodsAndActivityMap.get(goodsDetailRequest.getGoodsId());
                    if(CollUtil.isEmpty(couponActivityLsit)){
                        goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), couponActivityDOS);
                    } else {
                        couponActivityLsit.addAll(couponActivityDOS);
                        goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), couponActivityLsit);
                    }
                }
            }

            // 查询可使用商品信息，商家券、平台券
            List<CouponActivityDO> activitygoodsLimitList = effectiveCouponActivityList.stream()
                .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getGoodsLimit())).collect(Collectors.toList());
            List<Long> effectiveCouponActivityIdList = activitygoodsLimitList.stream().map(CouponActivityDO::getId).distinct()
                .collect(Collectors.toList());
            log.info("getCouponCanUseList effectiveCouponActivityIdList -> {}", JSONObject.toJSONString(effectiveCouponActivityIdList));
            //List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByCouponActivityIdList(effectiveCouponActivityIdList);
            List<Long> goodsIds = request.getGoodsDetailList().stream().map(QueryCouponCanUseListDetailRequest::getGoodsId).distinct().collect(Collectors.toList());
            List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByCouponActivityIdAndGoodsId(effectiveCouponActivityIdList,goodsIds);
            //log.info("getCouponCanUseList goodsLimitList -> {}", JSONObject.toJSONString(goodsLimitList));
            if (CollUtil.isNotEmpty(goodsLimitList)) {
                Map<Long, List<CouponActivityGoodsLimitDO>> goodsLimitMap = goodsLimitList.stream().collect(Collectors.groupingBy(CouponActivityGoodsLimitDO::getGoodsId));
                for (QueryCouponCanUseListDetailRequest goodsDetailRequest : goodsDetailList) {
                    List<CouponActivityGoodsLimitDO> goodsLimits = goodsLimitMap.get(goodsDetailRequest.getGoodsId());
                    if(CollUtil.isEmpty(goodsLimits)){
                        continue;
                    }
                    Map<Long, List<CouponActivityGoodsLimitDO>> goodsActivityMap = goodsLimits.stream().collect(Collectors.groupingBy(CouponActivityGoodsLimitDO::getCouponActivityId));
                    for (CouponActivityDO couponActivityDO : effectiveCouponActivityList) {
                        List<CouponActivityGoodsLimitDO> goodsLimitDOList = goodsActivityMap.get(couponActivityDO.getId());
                        if(CollUtil.isEmpty(goodsLimitDOList)){
                            continue;
                        }
                        // 商品可使用优惠券
                        List<CouponActivityDO> couponActivityLsit = goodsAndActivityMap.get(goodsDetailRequest.getGoodsId());
                        if(CollUtil.isEmpty(couponActivityLsit)){
                            goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), new ArrayList(){{add(couponActivityDO);}});
                        } else {
                            couponActivityLsit.add(couponActivityDO);
                            goodsAndActivityMap.put(goodsDetailRequest.getGoodsId(), couponActivityLsit);
                        }
                    }
                }
            }

            log.info("getCouponCanUseList goodsAndActivityMap -> {}", JSONObject.toJSONString(goodsAndActivityMap));
            if(MapUtil.isEmpty(goodsAndActivityMap)){
                return null;
            }
            for (Map.Entry<Long, List<CouponActivityDO>> entry : goodsAndActivityMap.entrySet()) {
                TreeSet<CouponActivityDO> canUseSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
                canUseSet.addAll(entry.getValue());
                List<CouponActivityDO> list = new ArrayList<>(canUseSet);
                goodsAndActivityMap.put(entry.getKey(), list);
            }

            // 平台券
            List<CouponActivityCanUseDetailDTO> platformList = new ArrayList<>();
            // 商家券
            List<CouponActivityCanUseDetailDTO> businessList = new ArrayList<>();
            // 已选择商家券
            Map<Long, Long> shopCouponIdMap = goodsDetailList.stream().filter(g -> ObjectUtil.isNotNull(g.getShopCouponId()))
                .collect(Collectors.toMap(g -> g.getShopCouponId(), g -> g.getEid(), (v1, v2) -> v1));

            Map<Long, List<QueryCouponCanUseListDetailRequest>> detailRequestMap = goodsDetailList.stream()
                .collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getEid));
            Map<Long, CouponActivityDO> couponActivityMap = effectiveCouponActivityList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
            Map<Long, BigDecimal> allTotalAmountMap = new HashMap<>();

            for (Map.Entry<Long, List<QueryCouponCanUseListDetailRequest>> requestMap : detailRequestMap.entrySet()) {
                // 传过来的订单商品信息，通过企业id分类之后的遍历
                Long eid = requestMap.getKey();
                List<QueryCouponCanUseListDetailRequest> requests = requestMap.getValue();
                // 优惠券活动id-商品
                Map<Long, List<QueryCouponCanUseListDetailRequest>> activitydetailRequestMap = new HashMap<>();
                for (QueryCouponCanUseListDetailRequest detailRequest : requests) {
                    List<CouponActivityDO> couponActivitys = goodsAndActivityMap.get(detailRequest.getGoodsId());
                    if(CollUtil.isEmpty(couponActivitys)){
                        continue;
                    }
                    couponActivitys.forEach(c -> {
                        List<QueryCouponCanUseListDetailRequest> detailRequests = activitydetailRequestMap.get(c.getId());
                        if(ObjectUtil.isNotNull(detailRequests)){
                            detailRequests.add(detailRequest);
                            activitydetailRequestMap.put(c.getId(), detailRequests);
                        } else {
                            activitydetailRequestMap.put(c.getId(), new ArrayList(){{add(detailRequest);}});
                        }
                    });
                }
                // activitydetailRequestMap  活动id   订单商品信息      这个活动有哪些商品
                // goodsAndActivityMap  商品id  活动信息      这个商品有哪些活动
                if(MapUtil.isEmpty(activitydetailRequestMap)){
                    continue;
                }
                List<CouponActivityDO> canUseCouponActivityList = new ArrayList<>();
                for (Map.Entry<Long, List<QueryCouponCanUseListDetailRequest>> activityMap : activitydetailRequestMap.entrySet()) {
                    Long activityId = activityMap.getKey();
                    CouponActivityDO couponActivity = couponActivityMap.get(activityId);
                    if (ObjectUtil.isNull(couponActivity)) {
                        continue;
                    }

                    TreeSet<QueryCouponCanUseListDetailRequest> couponSet = new TreeSet<>((o1, o2) -> o1.getGoodsId().compareTo(o2.getGoodsId()));
                    couponSet.addAll(activityMap.getValue());
                    List<QueryCouponCanUseListDetailRequest> detailRequestList = new ArrayList<>(couponSet);
                    if (CollUtil.isEmpty(detailRequestList)) {
                        continue;
                    }

                    // 券类型
                    Integer sponsorType = couponActivity.getSponsorType();
                    // 支付方式
                    // b2b-788去掉支付方式对优惠券的影响
                    /*Integer payMethodLimit = couponActivity.getPayMethodLimit();
                    Iterator<QueryCouponCanUseListDetailRequest> detailIt = detailRequestList.iterator();
                    Set<String> set = new HashSet<>();
                    while (detailIt.hasNext()) {
                        QueryCouponCanUseListDetailRequest detailRequest = detailIt.next();
                        boolean removeFlag = false;
                        // 支付方式
                        if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), payMethodLimit)) {
                            List<Integer> payMethodList = Convert.toList(Integer.class, couponActivity.getPayMethodSelected());
                            CouponPayMethodTypeEnum couponPayMethodTypeEnum = CouponPayMethodTypeEnum
                                    .transTypeFromPaymentMethodEnum(detailRequest.getPaymentMethod());
                            if (ObjectUtil.isNull(couponPayMethodTypeEnum) || !payMethodList
                                .contains(CouponPayMethodTypeEnum.transTypeFromPaymentMethodEnum(detailRequest.getPaymentMethod()).getCode())) {
//                                throw new BusinessException(CouponErrorCode.PAY_METHOD_ERROR);
                                detailIt.remove();
                                removeFlag = true;
                            }
                        }
                        // 去重
                        if (!removeFlag) {
                            String uniqueKey = detailRequest.getEid() + "_" + detailRequest.getGoodsId();
                            if (set.contains(uniqueKey)) {
                                detailIt.remove();
                            } else {
                                set.add(uniqueKey);
                            }
                        }
                    }*/

                    // 门槛金额
                    BigDecimal thresholdValue = couponActivity.getThresholdValue();
//                    Map<Long, List<QueryCouponCanUseListDetailRequest>> map = detailRequestList.stream()
//                            .collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getEid));

                    // 可用商品总金额 = 购买店铺商品金额 - 优惠金额
                    BigDecimal totalGoodsAmount = BigDecimal.ZERO;
                    BigDecimal totalAmount = BigDecimal.ZERO;
                    for (QueryCouponCanUseListDetailRequest detail : detailRequestList) {
                        BigDecimal goodsAmount = ObjectUtil.isNull(detail.getGoodsAmount()) ? BigDecimal.ZERO : detail.getGoodsAmount();
                        BigDecimal shopDiscountAmount = BigDecimal.ZERO;
                        totalAmount = totalAmount.add(goodsAmount).subtract(shopDiscountAmount).setScale(6, BigDecimal.ROUND_HALF_UP);
                        totalGoodsAmount = totalGoodsAmount.add(goodsAmount).setScale(6, BigDecimal.ROUND_HALF_UP);
                    }

                    if(ObjectUtil.equal(CouponActivitySponsorTypeEnum.PLATFORM.getCode(), sponsorType)){
                        // 平台券,后续校验可用商品总金额
                        BigDecimal allTotalAmount = allTotalAmountMap.get(activityId);
                        if(ObjectUtil.isNotNull(allTotalAmount)){
                            allTotalAmount = allTotalAmount.add(totalAmount).setScale(6, BigDecimal.ROUND_HALF_UP);
                        } else {
                            allTotalAmount = totalAmount;
                        }
                        allTotalAmountMap.put(activityId, allTotalAmount);
                    } else {
                        // 商家券，是否符合满减
                        if (totalGoodsAmount.compareTo(thresholdValue) < 0) {
                            continue;
                        }
                    }
                    canUseCouponActivityList.add(couponActivity);

                    //                /* 分配优惠券 */
                    //                // 取可使用商品的商品金额之和最接近门槛金额的
                    //                List<BigDecimal> totalGoodsAmountList = new ArrayList<>(eidGoodsAmountMap.values());
                    //                BigDecimal similarNumber = CouponUtil.getSimilarNumber(thresholdValue, totalGoodsAmountList, true);
                    //                log.info("getCouponCanUseList similarNumber -> {}", similarNumber);
                    //                if (ObjectUtil.isNull(similarNumber)) {
                    //                    continue;
                    //                }
                    //                // 获取企业id，商品金额相同的随机取一个企业
                    //                List<Long> eidList = new ArrayList();
                    //                for (Long getKey : eidGoodsAmountMap.keySet()) {
                    //                    if (eidGoodsAmountMap.get(getKey).compareTo(similarNumber) == 0) {
                    //                        eidList.add(getKey);
                    //                    }
                    //                }
                    //                long shopEid = eidList.get(0);


                }

                if (CollUtil.isEmpty(canUseCouponActivityList)) {
                    continue;
                }
                    
                Map<Long, CouponActivityDO> canUseCouponActivityMap = canUseCouponActivityList.stream()
                    .collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
                for (CouponDO coupon : couponList) {
                    // 店铺已选择的优惠券（平台创建的多个店铺可使用），不能在被其他店铺使用
                    Long selectEid = shopCouponIdMap.get(coupon.getId());
                    if(ObjectUtil.isNotNull(selectEid) && !ObjectUtil.equal(selectEid, eid)){
                        continue;
                    }
                    // 优惠券活动
                    CouponActivityDO couponActivity = canUseCouponActivityMap.get(coupon.getCouponActivityId());
                    if(ObjectUtil.isNull(couponActivity)){
                        continue;
                    }
                    CouponActivityCanUseDetailDTO couponActivityCanUseDetail = new CouponActivityCanUseDetailDTO();
                    couponActivityCanUseDetail = PojoUtils.map(couponActivity, CouponActivityCanUseDetailDTO.class);
                    CouponActivityDetailDTO couponActivityDetail = PojoUtils.map(couponActivity, CouponActivityDetailDTO.class);
                    CouponActivityDTO dto = couponActivityService.buildCouponActivityDtoForCouponRules(couponActivityDetail);
                    // 组装规则
                    Map<String, String> rulesMap = couponActivityService.buildCouponRulesMobile(dto);
                    couponActivityCanUseDetail.setThresholdValueRules(rulesMap.get("thresholdValue"));
                    couponActivityCanUseDetail.setDiscountValueRules(rulesMap.get("discountValue"));
                    couponActivityCanUseDetail.setDiscountMaxRules(rulesMap.get("discountMax"));
                    couponActivityCanUseDetail.setCouponActivityId(couponActivity.getId());
                    // 替换字段
                    couponActivityCanUseDetail.setId(coupon.getId());
                    couponActivityCanUseDetail.setEid(coupon.getEid());
                    couponActivityCanUseDetail.setEname(coupon.getEname());
                    couponActivityCanUseDetail.setBeginTime(coupon.getBeginTime());
                    couponActivityCanUseDetail.setEndTime(coupon.getEndTime());
                    // 设置过期到期时间
                    couponActivityCanUseDetail.setExpirationDesc(getExpirationDesc(couponActivity.getEndTime()));

                    // 全部商品、部分商品可用标识
                    Integer conditionGoods = couponActivity.getGoodsLimit();
//                    int conditionGoods = 2;
//                    CouponActivityDO couponActivityDO = notEnterpriseLimiMap.get(couponActivity.getId());
//                    if (ObjectUtil.isNotNull(couponActivityDO)) {
//                        conditionGoods = 1;
//                    }
                    couponActivityCanUseDetail.setConditionGoods(conditionGoods);
                    // 此优惠券是否可叠加赠品
                    boolean isSuportPromotionGoods = getSuportPromotionGoodsFlag(couponActivity);
                    couponActivityCanUseDetail.setIsSuportPromotionGoods(isSuportPromotionGoods);
                    couponActivityCanUseDetail.setIsBestCoupon(false);
                    // 每个店铺的优惠券，或平台券
                    if (ObjectUtil.equal(CouponActivitySponsorTypeEnum.PLATFORM.getCode(), couponActivity.getSponsorType())) {
                        platformList.add(couponActivityCanUseDetail);
                    } else {
                        couponActivityCanUseDetail.setShopEid(eid);
                        businessList.add(couponActivityCanUseDetail);
                    }
                }
            }
            couponActivityCanUse.setBusinessList(businessList);
            // 平台券是否符合满减
            TreeSet<CouponActivityCanUseDetailDTO> platformSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
            platformSet.addAll(platformList);
            platformList = new ArrayList<>(platformSet);
            if (CollUtil.isNotEmpty(platformList)) {
                Iterator<CouponActivityCanUseDetailDTO> platformIterator = platformList.iterator();
                while (platformIterator.hasNext()) {
                    CouponActivityCanUseDetailDTO platform = platformIterator.next();
                    // 商品总金额是否符合满减
                    BigDecimal totalAmount = allTotalAmountMap.get(platform.getCouponActivityId());
                    if (ObjectUtil.isNotNull(totalAmount) && totalAmount.compareTo(platform.getThresholdValue()) < 0) {
                        platformIterator.remove();
                    }
                }
            }
            couponActivityCanUse.setPlatformList(platformList);

            if(request.getIsUseBestCoupon()) {
                operateCoupon(couponActivityCanUse, request.getGoodsDetailList());
            }
        } catch (Exception e) {
            log.warn("getCouponCanUseList 进货单获取可使用优惠券异常 usetId -> {}, currentEid -> {}, exception -> {}", request.getOpUserId(),
                request.getCurrentEid(), e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.MOBILE_PURCHASE_ORDER_GET_CAN_USE_COUPON_ERROR, e.getMessage());
        }
        log.info("getCouponCanUseList couponActivityCanUse -> {} 耗时:[{}]ms", JSONObject.toJSONString(couponActivityCanUse), System.currentTimeMillis()-timeBegin);
        return couponActivityCanUse;
    }

    private void operateCoupon(CouponActivityCanUseDTO couponActivityCanUse,List<QueryCouponCanUseListDetailRequest> goodsDetailList) {
        long timeMillis = System.currentTimeMillis();
        log.info("getCouponCanUseList operateCoupon start couponActivityCanUse:[{}]", JSON.toJSONString(couponActivityCanUse));
        {
            // 商家券
            List<Long> bestCouponList = new ArrayList<>();
            Map<Long, List<QueryCouponCanUseListDetailRequest>> goodsDetailMap = goodsDetailList.stream().collect(Collectors.groupingBy(QueryCouponCanUseListDetailRequest::getEid));
            List<CouponActivityCanUseDetailDTO> businessList = couponActivityCanUse.getBusinessList();
            Map<Long, List<CouponActivityCanUseDetailDTO>> businessCouponMap = businessList.stream().collect(Collectors.groupingBy(CouponActivityCanUseDetailDTO::getShopEid));
            businessCouponMap.forEach((shopEid, item) -> {
                List<QueryCouponCanUseListDetailRequest> businessDetailList = goodsDetailMap.get(shopEid);
                Map<Long, BigDecimal> discountMap = operateCouponEveryEid(item, businessDetailList);
                Map<Long, CouponActivityCanUseDetailDTO> platformListMap = item.stream().collect(Collectors.toMap(CouponActivityCanUseDetailDTO::getId, e -> e, (k1, k2) -> k1));
                Long bestCouponId = getBestCouponId(discountMap, platformListMap, bestCouponList);
                if (Objects.isNull(bestCouponId)) {
                    return;
                }
                bestCouponList.add(bestCouponId);
                for (CouponActivityCanUseDetailDTO couponActivityCanUseDetailDTO : businessList) {
                    if (shopEid.equals(couponActivityCanUseDetailDTO.getShopEid()) && bestCouponId.equals(couponActivityCanUseDetailDTO.getId())) {
                        couponActivityCanUseDetailDTO.setIsBestCoupon(true);
                    }
                }
            });
            if (CollUtil.isNotEmpty(bestCouponList)) {
                businessList.removeIf(detailDTO -> bestCouponList.contains(detailDTO.getId()) && !detailDTO.getIsBestCoupon());
                couponActivityCanUse.setBusinessList(businessList);
            }
        }
        {
            // 平台券
            List<CouponActivityCanUseDetailDTO> platformList = couponActivityCanUse.getPlatformList();
            Map<Long, BigDecimal> platformDiscountMap = operateCouponEveryEid(platformList, goodsDetailList);

            Map<Long, CouponActivityCanUseDetailDTO> platformListMap = platformList.stream().collect(Collectors.toMap(CouponActivityCanUseDetailDTO::getId, e -> e, (k1, k2) -> k1));
            Long bestCouponId = getBestCouponId(platformDiscountMap, platformListMap, new ArrayList<>());
            if (Objects.nonNull(bestCouponId)) {
                for (CouponActivityCanUseDetailDTO couponActivityCanUseDetailDTO : platformList) {
                    if (bestCouponId.equals(couponActivityCanUseDetailDTO.getId())) {
                        couponActivityCanUseDetailDTO.setIsBestCoupon(true);
                    }
                }
            }
        }
        log.info("getCouponCanUseList operateCoupon 耗时:[{}]ms", System.currentTimeMillis() - timeMillis);
    }

    private Long getBestCouponId(Map<Long, BigDecimal> platformDiscountMap, Map<Long, CouponActivityCanUseDetailDTO> platformListMap,List<Long> notCouponList) {
        final Long[] topCouponId = new Long[1];
        final BigDecimal[] topAmount = { BigDecimal.ZERO };
        platformDiscountMap.forEach((couponId, amount) -> {
            if (notCouponList.contains(couponId)) {
                return;
            }
            int size = amount.compareTo(topAmount[0]);
            if (size > 0) {
                topCouponId[0] = couponId;
                topAmount[0] = amount;
            } else if (size == 0) {
                CouponActivityCanUseDetailDTO couponActivityCanUseDetailDTO = platformListMap.get(couponId);
                CouponActivityCanUseDetailDTO topCouponActivityCanUseDetailDTO = platformListMap.get(topCouponId[0]);
                Date endTime = couponActivityCanUseDetailDTO.getEndTime();
                Date topEndTime = topCouponActivityCanUseDetailDTO.getEndTime();
                if (null != topEndTime) {
                    if (null == endTime) {
                        topCouponId[0] = couponId;
                        topAmount[0] = amount;
                    } else {
                        if (topEndTime.after(endTime)) {
                            topCouponId[0] = couponId;
                            topAmount[0] = amount;
                        }
                    }
                }
            }
        });
        return topCouponId[0];
    }

    private Map<Long, BigDecimal>  operateCouponEveryEid(List<CouponActivityCanUseDetailDTO> couponCanUseList,List<QueryCouponCanUseListDetailRequest> goodsDetailList) {
        Map<Long, BigDecimal> discountMap = new HashMap<>();
        for (CouponActivityCanUseDetailDTO couponActivityCanUseDetailDTO : couponCanUseList) {
            // 优惠券活动类型（1-满减券；2-满折券）
            Integer type = couponActivityCanUseDetailDTO.getType();
            BigDecimal discountValue = couponActivityCanUseDetailDTO.getDiscountValue();
            if (1 == type) {
                discountMap.put(couponActivityCanUseDetailDTO.getId(), discountValue);
            } else if (2 == type) {
                // 折扣券
                Integer conditionGoods = couponActivityCanUseDetailDTO.getConditionGoods();
                BigDecimal allAmount = BigDecimal.ZERO;
                if (1 == conditionGoods) {
                    allAmount = goodsDetailList.stream().map(QueryCouponCanUseListDetailRequest::getGoodsAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                } else if (2 == conditionGoods) {
                    List<Long> goodsIds = goodsDetailList.stream().map(QueryCouponCanUseListDetailRequest::getGoodsId).distinct().collect(Collectors.toList());
                    List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByCouponActivityIdAndGoodsId(new ArrayList<Long>() {{
                        add(couponActivityCanUseDetailDTO.getCouponActivityId());
                    }}, goodsIds);
                    List<Long> goodsIdList = goodsLimitList.stream().map(CouponActivityGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                    List<QueryCouponCanUseListDetailRequest> useListDetailRequestList = goodsDetailList.stream().filter(e -> goodsIdList.contains(e.getGoodsId())).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(useListDetailRequestList)) {
                        allAmount = useListDetailRequestList.stream().map(QueryCouponCanUseListDetailRequest::getGoodsAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                }
                BigDecimal discountAmount = allAmount.subtract(allAmount.multiply(discountValue).divide(new BigDecimal("100")).setScale(6, BigDecimal.ROUND_HALF_UP));
                discountMap.put(couponActivityCanUseDetailDTO.getId(), discountAmount);
            }
        }
        log.info("operateCouponEveryEid discountMap:[{}]", JSON.toJSONString(discountMap));
        return discountMap;
    }


    /**
     * 获取优惠劵过期到期提醒
     * @param endTime 过期时间
     * @return 到期提醒
     */
    private  String getExpirationDesc(Date endTime) {

        if (endTime == null) {
            return "";
        }

        if (DateUtil.isSameDay(new Date(),endTime)) {

            return "今天到期";
        }

        if (DateUtil.compare(new Date(),endTime) > 0) {

            return "已过期";
        }

        Long diff = DateUtil.betweenDay(new Date(),endTime,true);


        return StringUtils.join(diff,"天后到期");
    }


    private boolean getSuportPromotionGoodsFlag(CouponActivityDO couponActivity) {
        String coexistPromotion = couponActivity.getCoexistPromotion();
        if (StrUtil.isNotBlank(coexistPromotion)) {
            List<Integer> coexistPromotionList = Convert.toList(Integer.class, coexistPromotion);
            if (CollUtil.isNotEmpty(coexistPromotionList) && coexistPromotionList.contains(CouponActivityCoexistPromotionEnum.FULL_GIFT.getCode())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public OrderUseCouponBudgetDTO orderUseCouponShareAmountBudget(OrderUseCouponBudgetRequest request) {
        log.info("orderUseCouponShareAmountBudget request -> {}", JSONObject.toJSONString(request));
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getPlatform()) || ObjectUtil.isNull(request.getCurrentEid())
            || CollUtil.isEmpty(request.getGoodsSkuDetailList())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        try {
            Long currentEid = request.getCurrentEid();
            // 企业商品信息明细
            List<OrderUseCouponBudgetGoodsDetailRequest> enterpriseDetailList = request.getGoodsSkuDetailList();
            enterpriseDetailList.forEach(e -> {
                if (ObjectUtil.isNull(e.getPlatformCouponId()) && ObjectUtil.isNull(e.getCouponId())) {
                    throw new BusinessException(ResultCode.PARAM_MISS);
                }
            });

            long nowTime = System.currentTimeMillis();
            // 平台优惠券
            CouponActivityDO platformCouponActivity = null;
            Optional<OrderUseCouponBudgetGoodsDetailRequest> optional = Optional
                .ofNullable(enterpriseDetailList.stream().filter(e -> ObjectUtil.isNotNull(e.getPlatformCouponId())).findFirst()).orElse(null);
            Long platformCouponId = optional.isPresent() ? optional.get().getPlatformCouponId() : null;
            if (ObjectUtil.isNotNull(platformCouponId)) {
                // 优惠券状态
                // 参数是卡包id不是优惠券id
                CouponDO platformCoupon = couponService.getById(platformCouponId);
                log.info("orderUseCouponShareAmountBudget platformCoupon -> {}", JSONObject.toJSONString(platformCoupon));
                String desc = "平台优惠券";
                checkCouponStatus(currentEid, nowTime, platformCouponId, platformCoupon, desc);

                List<CouponActivityDO> platformCouponActivityList = couponActivityService
                    .listByIds(Arrays.asList(platformCoupon.getCouponActivityId()));
                if (CollUtil.isEmpty(platformCouponActivityList)) {
                    log.warn("orderUseCouponShareAmountBudget , 未查询到此平台优惠券活动, 此优惠券已经被使用，或已过期，请选择其他平台优惠券");
                    String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), "平台券");
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR, errorMsg);
                }
                // 优惠券、活动id关系
                Map<Long, List<CouponDO>> platformRelationMap = new HashMap<>();
                platformRelationMap.put(platformCouponActivityList.get(0).getId(), Arrays.asList(platformCoupon));
                // 活动校验
                checkCouponActivity(request, currentEid, nowTime, desc, platformCouponActivityList, platformRelationMap, true);
                platformCouponActivity = platformCouponActivityList.get(0);
            }
            log.info("orderUseCouponShareAmountBudget platformCouponActivity -> {}", JSONObject.toJSONString(platformCouponActivity));
            List<Long> goodsId = request.getGoodsSkuDetailList().stream().map(OrderUseCouponBudgetGoodsDetailRequest::getGoodsId).distinct().collect(Collectors.toList());
            log.info("orderUseCouponShareAmountBudget goodsId -> {}", JSONObject.toJSONString(goodsId));
            // 商家优惠券
            List<CouponDO> couponList = new ArrayList<>();
            List<CouponActivityDO> businessCouponActivityList = new ArrayList<>();
            List<Long> couponActivityIdList = new ArrayList<>();
            List<OrderUseCouponBudgetGoodsDetailRequest> detailRequestList = enterpriseDetailList.stream()
                .filter(e -> ObjectUtil.isNotNull(e.getCouponId())).collect(Collectors.toList());
            List<Long> couponIdList = CollUtil.isEmpty(detailRequestList) ? null
                : detailRequestList.stream().map(OrderUseCouponBudgetGoodsDetailRequest::getCouponId).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(couponIdList)) {
                // 根据优惠券id（卡包id）查询已领优惠券
                couponList = couponService.getEffectiveListByIdList(couponIdList);
                if (CollUtil.isEmpty(couponList)) {
                    log.warn("orderUseCouponShareAmountBudget , 优惠券已经被使用，或已过期，请选择其他优惠券");
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR);
                }

                // 去重，不同店铺不能使用同一张优惠券
                Map<Long, List<OrderUseCouponBudgetGoodsDetailRequest>> couponIdShopMap = enterpriseDetailList.stream()
                    .filter(c -> ObjectUtil.isNotNull(c.getCouponId()))
                    .collect(Collectors.groupingBy(OrderUseCouponBudgetGoodsDetailRequest::getCouponId));
                for (Map.Entry<Long, List<OrderUseCouponBudgetGoodsDetailRequest>> entry : couponIdShopMap.entrySet()) {
                    Long key = entry.getKey();
                    List<OrderUseCouponBudgetGoodsDetailRequest> value = entry.getValue();
                    List<Long> shopEidList = value.stream().map(OrderUseCouponBudgetGoodsDetailRequest::getEid).distinct()
                        .collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(shopEidList) && shopEidList.size() > 1) {
                        Map<Long, String> couponMap = couponList.stream()
                            .collect(Collectors.toMap(c -> c.getId(), c -> c.getCouponActivityName(), (v1, v2) -> v1));
                        String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_SHOP_REPEAT_ERROR.getMessage(), couponMap.get(key));
                        throw new BusinessException(CouponErrorCode.COUPON_SHOP_REPEAT_ERROR, errorMsg);
                    }
                }

                String desc = "商家优惠券";
                for (CouponDO couponDO : couponList) {
                    checkCouponStatus(currentEid, nowTime, couponDO.getId(), couponDO, desc);
                }

                couponActivityIdList = couponList.stream().map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
                // 查询优惠券活动
                businessCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIdList, 1, 1);
                if (CollUtil.isEmpty(businessCouponActivityList)) {
                    log.warn("orderUseCouponShareAmountBudget , 未查询到此商家优惠券活动, 此优惠券已经被使用，或已过期，请选择其他商家优惠券");
                    String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), "", desc);
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR, errorMsg);
                }

                // 优惠券、活动id关系
                Map<Long, List<CouponDO>> businessRelationMap = couponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
                // 活动校验
                checkCouponActivity(request, currentEid, nowTime, desc, businessCouponActivityList, businessRelationMap, false);
                Map<Long, Integer> payMethodMap = enterpriseDetailList.stream()
                    .collect(Collectors.toMap(e -> e.getCouponId(), e -> e.getPayMethod(), (v1, v2) -> v1));
            }
            log.info("orderUseCouponShareAmountBudget businessCouponActivityList -> {}", JSONObject.toJSONString(businessCouponActivityList));

            // 商家优惠券
            Map<Long, CouponDO> couponMap = new HashMap<>();
            if (CollUtil.isNotEmpty(couponList)) {
                couponMap = couponList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
            }
            // 商家优惠券活动
            Map<Long, CouponActivityDO> couponActivityMap = new HashMap<>();
            if (CollUtil.isNotEmpty(businessCouponActivityList)) {
                couponActivityMap = businessCouponActivityList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1));
            }
            // 可使用优惠券的企业商品，key = eid_goodsSkuId
            // 平台
            Map<String, OrderUseCouponBudgetGoodsDetailRequest> canShareEidGoodsMapPlatform = new HashMap<>();
            // 商家
            Map<String, OrderUseCouponBudgetGoodsDetailRequest> canShareEidGoodsMap = new HashMap<>();

            /* 是否全部供应商、全部商品可用 */
            // 平台券
            if (ObjectUtil.isNotNull(platformCouponActivity)
                && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), platformCouponActivity.getEnterpriseLimit())
                && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), platformCouponActivity.getGoodsLimit())
                && ObjectUtil.isNotNull(platformCouponActivity.getEid()) && platformCouponActivity.getEid().intValue() == 0) {
                log.info("orderUseCouponShareAmountBudget 全部企业商品可使用平台优惠券");
                // 全部商品可使用优惠券，后续校验门槛金额
                for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                    // 可使用优惠券
                    canShareEidGoodsMapPlatform.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                }
            } else {
                /* 其中部分企业、全部商品可用的，后续校验门槛金额 */
                // 平台券
                Long limitEnterpriseIdPlatform = null;
                Map<Long, CouponActivityEnterpriseLimitDO> enterpriseLimitMapPlatform = new HashMap<>();
                if (ObjectUtil.isNotNull(platformCouponActivity)
                    && ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), platformCouponActivity.getEnterpriseLimit())
                    && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), platformCouponActivity.getGoodsLimit())
                    && ObjectUtil.isNotNull(platformCouponActivity.getEid()) && platformCouponActivity.getEid().intValue() == 0) {
                    limitEnterpriseIdPlatform = platformCouponActivity.getId();
                    // 查询可使用企业信息
                    List<CouponActivityEnterpriseLimitDO> enterpriseLimitListPlatform = couponActivityEnterpriseLimitService
                        .getByCouponActivityIdList(Arrays.asList(limitEnterpriseIdPlatform));
                    log.info("orderUseCouponShareAmountBudget 平台券可用的企业, enterpriseLimitListPlatform -> {}",
                        JSONObject.toJSONString(enterpriseLimitListPlatform));
                    if (CollUtil.isNotEmpty(enterpriseLimitListPlatform)) {
                        enterpriseLimitMapPlatform = enterpriseLimitListPlatform.stream()
                            .collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
                        for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                            // 店铺是否可参与平台优惠券
                            CouponActivityEnterpriseLimitDO enterpriseLimitPlatform = enterpriseLimitMapPlatform.get(goodsDetailRequest.getEid());
                            if (ObjectUtil.isNotNull(enterpriseLimitPlatform) && ObjectUtil.isNotNull(goodsDetailRequest.getEid())
                                && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                                canShareEidGoodsMapPlatform.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(),
                                    goodsDetailRequest);
                            }
                        }
                    }
                }

                /* 查询可用商品：全部企业、部分商品；部分企业、部分商品 */
                // 平台券
                if (ObjectUtil.isNotNull(platformCouponActivity) && ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), platformCouponActivity.getGoodsLimit()) && ObjectUtil.isNotNull(platformCouponActivity.getEid()) && platformCouponActivity.getEid().intValue() == 0) {
                    limitEnterpriseIdPlatform = platformCouponActivity.getId();
                    Map<Long, List<CouponActivityGoodsLimitDO>> goodsLimitEidMapPlatform = new HashMap<>();
                    List<CouponActivityGoodsLimitDO> goodsLimitListPlatform = couponActivityGoodsLimitService.getListByCouponActivityIdAndGoodsId(Arrays.asList(limitEnterpriseIdPlatform),goodsId);
                    if (CollUtil.isNotEmpty(goodsLimitListPlatform)) {
                        goodsLimitEidMapPlatform = goodsLimitListPlatform.stream().collect(Collectors.groupingBy(CouponActivityGoodsLimitDO::getEid));
                        for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                            List<CouponActivityGoodsLimitDO> goodsLimitsPlatform = goodsLimitEidMapPlatform.get(goodsDetailRequest.getEid());
                            if (CollUtil.isEmpty(goodsLimitsPlatform)) {
                                continue;
                            }
                            Map<Long, CouponActivityGoodsLimitDO> goodsLimitMapPlatform = goodsLimitsPlatform.stream().collect(Collectors.toMap(g -> g.getGoodsId(), g -> g, (v1, v2) -> v1));
                            CouponActivityGoodsLimitDO goodsLimitPlatform = goodsLimitMapPlatform.get(goodsDetailRequest.getGoodsId());
                            if (ObjectUtil.isNotNull(goodsLimitPlatform)) {
                                OrderUseCouponBudgetGoodsDetailRequest goodsDetail = canShareEidGoodsMapPlatform.get(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId());
                                if (ObjectUtil.isNull(goodsDetail) && ObjectUtil.isNotNull(goodsDetailRequest.getEid()) && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                                    canShareEidGoodsMapPlatform.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                                }
                            }
                        }
                    }
                }
            }

            /* 是否全部供应商、全部商品 */
            // 平台创建的商家券
            List<Long> eidList = enterpriseDetailList.stream().filter(e -> ObjectUtil.isNotNull(e.getCouponId()))
                .map(OrderUseCouponBudgetGoodsDetailRequest::getEid).distinct().collect(Collectors.toList());
            List<Long> notLimitEnterpriseList = businessCouponActivityList.stream()
                .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getEnterpriseLimit())
                             && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit()) && ObjectUtil.isNotNull(c.getEid())
                             && c.getEid().intValue() == 0)
                .map(CouponActivityDO::getId).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(notLimitEnterpriseList) && notLimitEnterpriseList.size() == eidList.size()) {
                log.info("orderUseCouponShareAmountBudget 全部企业商品可使用商家优惠券");
                // 全部商品可使用优惠券，后续校验门槛金额
                for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                    // 可使用优惠券
                    canShareEidGoodsMap.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                }
            } else {
                // 其中全部企业、全部商品可用的，后续校验门槛金额
                for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                    CouponDO coupon = couponMap.get(goodsDetailRequest.getCouponId());
                    if (ObjectUtil.isNull(coupon)) {
                        continue;
                    }
                    CouponActivityDO couponActivity = couponActivityMap.get(coupon.getCouponActivityId());
                    if (ObjectUtil.isNotNull(couponActivity)
                        && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), couponActivity.getEnterpriseLimit())
                        && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), couponActivity.getGoodsLimit())
                        && ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid().intValue() == 0) {
                        log.info("orderUseCouponShareAmountBudget 全部商品可用的企业, eid -> {}", goodsDetailRequest.getEid());
                        // 可使用优惠券
                        if (ObjectUtil.isNotNull(goodsDetailRequest.getEid()) && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                            canShareEidGoodsMap.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                        }
                    }
                }
                /* 其中部分企业、全部商品可用的 */
                // 商家券，平台创建
                List<Long> limitEnterpriseList = businessCouponActivityList.stream()
                    .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getEnterpriseLimit())
                                 && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit()) && ObjectUtil.isNotNull(c.getEid())
                                 && c.getEid().intValue() == 0)
                    .map(CouponActivityDO::getId).distinct().collect(Collectors.toList());
                //                limitEnterpriseList.add(limitEnterpriseIdPlatform);
                // 查询可使用企业信息
                List<CouponActivityEnterpriseLimitDO> enterpriseLimitInfoList = couponActivityEnterpriseLimitService
                    .getByCouponActivityIdList(limitEnterpriseList);
                log.info("orderUseCouponShareAmountBudget 商家券可用的企业, enterpriseLimitInfoList -> {}", JSONObject.toJSONString(enterpriseLimitInfoList));
                if (CollUtil.isNotEmpty(enterpriseLimitInfoList)) {
                    Map<Long, CouponActivityEnterpriseLimitDO> enterpriseLimitMap = enterpriseLimitInfoList.stream()
                        .collect(Collectors.toMap(e -> e.getEid(), e -> e, (v1, v2) -> v1));
                    for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                        // 店铺是否可参与商家优惠券
                        CouponActivityEnterpriseLimitDO enterpriseLimit = enterpriseLimitMap.get(goodsDetailRequest.getEid());
                        if (ObjectUtil.isNotNull(enterpriseLimit) && ObjectUtil.isNotNull(goodsDetailRequest.getEid())
                            && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                            canShareEidGoodsMap.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                        }
                    }
                }
                // 可用商品：全部企业、部分商品；部分企业、部分商品
                Map<Long, List<CouponActivityGoodsLimitDO>> goodsLimitEidMap = new HashMap<>();
                List<Long> limitGoodsList = businessCouponActivityList.stream()
                    .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getGoodsLimit()) && ObjectUtil.isNotNull(c.getEid())
                                 && c.getEid().intValue() == 0)
                    .map(CouponActivityDO::getId).distinct().collect(Collectors.toList());
                if (ObjectUtil.isNotNull(limitGoodsList)) {
                    List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService
                            .getListByCouponActivityIdAndGoodsId(limitGoodsList,goodsId);
                    if (CollUtil.isNotEmpty(goodsLimitList)) {
                        goodsLimitEidMap = goodsLimitList.stream().collect(Collectors.groupingBy(CouponActivityGoodsLimitDO::getEid));
                        for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                            List<CouponActivityGoodsLimitDO> goodsLimits = goodsLimitEidMap.get(goodsDetailRequest.getEid());
                            if(CollUtil.isEmpty(goodsLimits)){
                                continue;
                            }
                            Map<Long, CouponActivityGoodsLimitDO> goodsLimitMap = goodsLimits.stream()
                                    .collect(Collectors.toMap(g -> g.getGoodsId(), g -> g, (v1, v2) -> v1));
                            CouponActivityGoodsLimitDO goodsLimit = goodsLimitMap.get(goodsDetailRequest.getGoodsId());
                            if (ObjectUtil.isNotNull(goodsLimit)) {
                                OrderUseCouponBudgetGoodsDetailRequest goodsDetail = canShareEidGoodsMap
                                        .get(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId());
                                if (ObjectUtil.isNull(goodsDetail) && ObjectUtil.isNotNull(goodsDetailRequest.getEid())
                                        && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                                    canShareEidGoodsMap.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(),
                                            goodsDetailRequest);
                                }
                            }
                        }
                    }
                }

                // 店铺自己创建的优惠券
                List<CouponActivityDO> shopLimitEnterpriseList = businessCouponActivityList.stream()
                    .filter(c -> ObjectUtil.isNotNull(c.getEid()) && c.getEid().intValue() != 0).collect(Collectors.toList());
                // 店铺商品全部可用
                List<CouponActivityDO> shopList = shopLimitEnterpriseList.stream()
                    .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit())).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(shopList)) {
                    Map<Long, CouponActivityDO> shopLimitEnterpriseMap = shopList.stream()
                        .collect(Collectors.toMap(c -> c.getEid(), c -> c, (v1, v2) -> v1));
                    for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                        CouponDO coupon = couponMap.get(goodsDetailRequest.getCouponId());
                        if (ObjectUtil.isNull(coupon)) {
                            continue;
                        }
                        CouponActivityDO couponActivityDO = shopLimitEnterpriseMap.get(goodsDetailRequest.getEid());
                        if (ObjectUtil.isNull(couponActivityDO)) {
                            continue;
                        }
                        // 商品是否可参与商家优惠券
                        if (ObjectUtil.equal(coupon.getCouponActivityId(), couponActivityDO.getId())
                            && ObjectUtil.isNotNull(goodsDetailRequest.getEid()) && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                            canShareEidGoodsMap.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                        }
                    }
                }

                // 店铺商品部分可用
                List<CouponActivityDO> shopCouponActivityLimitList = shopLimitEnterpriseList.stream()
                    .filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getGoodsLimit())).collect(Collectors.toList());
                List<Long> shopLimitActivityIdList = shopCouponActivityLimitList.stream().map(CouponActivityDO::getId).distinct()
                    .collect(Collectors.toList());
                List<CouponActivityGoodsLimitDO> shopGoodsLimitList = couponActivityGoodsLimitService
                    .getListByCouponActivityIdAndGoodsId(shopLimitActivityIdList,goodsId);
                if (CollUtil.isNotEmpty(shopGoodsLimitList)) {
                    Map<Long, List<CouponActivityGoodsLimitDO>> shopGoodsLimitMap = shopGoodsLimitList.stream()
                        .collect(Collectors.groupingBy(CouponActivityGoodsLimitDO::getEid));
                    for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : enterpriseDetailList) {
                        List<CouponActivityGoodsLimitDO> goodsLimits = shopGoodsLimitMap.get(goodsDetailRequest.getEid());
                        if(CollUtil.isEmpty(goodsLimits)){
                            continue;
                        }
                        Map<Long, CouponActivityGoodsLimitDO> goodsLimitMap = goodsLimits.stream()
                            .collect(Collectors.toMap(g -> g.getGoodsId(), g -> g, (v1, v2) -> v1));

                        CouponActivityGoodsLimitDO goodsLimit = goodsLimitMap.get(goodsDetailRequest.getGoodsId());
                        if (ObjectUtil.isNotNull(goodsLimit)) {
                            OrderUseCouponBudgetGoodsDetailRequest goodsDetail = canShareEidGoodsMap
                                .get(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId());
                            if (ObjectUtil.isNull(goodsDetail) && ObjectUtil.isNotNull(goodsDetailRequest.getEid())
                                && ObjectUtil.isNotNull(goodsDetailRequest.getGoodsSkuId())) {
                                canShareEidGoodsMap.put(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId(), goodsDetailRequest);
                            }
                        }
                    }
                }
            }

            /* 校验店铺商品是否可使用平台、商家优惠券 */
            if (ObjectUtil.isNotNull(platformCouponId) && MapUtil.isEmpty(canShareEidGoodsMapPlatform)) {
                log.warn("orderUseCouponShareAmountBudget , 所有店铺商品都不能参与使用此平台优惠券，请选择其他平台优惠券，currentEid -> {}, couponId -> {}", currentEid,
                    platformCouponId);
                String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_LIMIT_ERROR.getMessage(), platformCouponActivity.getName());
                throw new BusinessException(CouponErrorCode.COUPON_USED_LIMIT_ERROR, errorMsg);
            }

            /* 商家优惠券，明细按eid分组，每个eid的所有sku如果都不在可用商品中，则此企业不能使用这个商家优惠券 */
            Map<Long, List<OrderUseCouponBudgetGoodsDetailRequest>> enterpriseDetailMap = enterpriseDetailList.stream()
                    .filter(e -> ObjectUtil.isNotNull(e.getCouponId()))
                    .collect(Collectors.groupingBy(OrderUseCouponBudgetGoodsDetailRequest::getEid));
            if (CollUtil.isNotEmpty(businessCouponActivityList) && MapUtil.isNotEmpty(enterpriseDetailMap)) {
                for (Map.Entry<Long, List<OrderUseCouponBudgetGoodsDetailRequest>> entry : enterpriseDetailMap.entrySet()) {
                    boolean canUseFlag = false;
                    Long eid = entry.getKey();
                    List<OrderUseCouponBudgetGoodsDetailRequest> values = entry.getValue();
                    for (OrderUseCouponBudgetGoodsDetailRequest value : values) {
                        OrderUseCouponBudgetGoodsDetailRequest budgetGoodsDetail = canShareEidGoodsMap.get(eid + "_" + value.getGoodsSkuId());
                        if (ObjectUtil.isNotNull(budgetGoodsDetail)) {
                            canUseFlag = true;
                        }
                    }
                    if (!canUseFlag) {
                        long couponId = values.get(0).getCouponId();
                        CouponDO couponDO = couponMap.get(couponId);
                        log.warn("orderUseCouponShareAmountBudget , 此店铺商品都不能参与使用此商家优惠券，请选择其他商家优惠券，currentEid -> {}, shopEid -> {}, couponId -> {}",
                            currentEid, eid, couponId);
                        String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_BUSINESS_ERROR.getMessage(),
                            couponDO.getCouponActivityName());
                        throw new BusinessException(CouponErrorCode.COUPON_USED_BUSINESS_ERROR, errorMsg);
                    }
                }
            }

            /* 进行分摊 */
            if (MapUtil.isNotEmpty(canShareEidGoodsMap) || MapUtil.isNotEmpty(canShareEidGoodsMapPlatform)) {
                OrderUseCouponBudgetDTO orderUseCouponBudget = new OrderUseCouponBudgetDTO();
                orderUseCouponBudget.setPlatformCouponId(platformCouponId);

                /* 商家优惠券分摊 */
                // 所有商家优惠总金额
                BigDecimal totalBusinessDiscountAmount = BigDecimal.ZERO;

                List<OrderUseCouponBudgetEnterpriseDTO> enterpriseGoodsList = new ArrayList<>();
                int twoScale = 2;

                // 总体数据按照 eid + couponId 分组 .filter(g -> ObjectUtil.isNotNull(g.getCouponId()))
                Map<String, List<OrderUseCouponBudgetGoodsDetailRequest>> goodsDetailMap = enterpriseDetailList.stream()
                    .collect(Collectors.groupingBy(g -> g.getEid() + "_" + g.getCouponId()));
                // 商家优惠券可分摊商品数据按照 eid + couponId 分组
                Map<String, List<OrderUseCouponBudgetGoodsDetailRequest>> canShareGoodsMap = new HashMap<>();
                if (MapUtil.isNotEmpty(canShareEidGoodsMap)) {
                    canShareGoodsMap = canShareEidGoodsMap.values().stream().collect(Collectors.groupingBy(g -> g.getEid() + "_" + g.getCouponId()));
                }
                totalBusinessDiscountAmount = businessBudget(couponMap, couponActivityMap, canShareEidGoodsMap, enterpriseGoodsList, twoScale,
                    goodsDetailMap, canShareGoodsMap);

                /* 平台优惠券分摊 */
                // 平台券门槛金额
                BigDecimal platformThresholdValue = BigDecimal.ZERO;
                // 平台券优惠总金额
                BigDecimal platformDiscountValue = BigDecimal.ZERO;
                long platformCouponActivityId = 0;
                String platformCouponActivityName = "";
                BigDecimal platformRatio = BigDecimal.ZERO;
                BigDecimal businessRatio = BigDecimal.ZERO;

                // 可使用平台券的商品优惠后总金额
                BigDecimal canSharePlatformGoodsAmountSum = BigDecimal.ZERO;
                for (OrderUseCouponBudgetEnterpriseDTO goodsList : enterpriseGoodsList) {
                    Long eid = goodsList.getEid();
                    for (OrderUseCouponBudgetGoodsDetailDTO goods : goodsList.getGoodsSkuDetailList()) {
                        OrderUseCouponBudgetGoodsDetailRequest budgetGoods = canShareEidGoodsMapPlatform.get(eid + "_" + goods.getGoodsSkuId());
                        if (ObjectUtil.isNotNull(budgetGoods)) {
                            /*canSharePlatformGoodsAmountSum = canSharePlatformGoodsAmountSum.add(goods.getGoodsSkuDiscountAmount()).setScale(twoScale,
                                BigDecimal.ROUND_HALF_UP);*/
                            // 可以分摊的金额，取商品原价，不取店铺优惠券分摊后的价格
                            canSharePlatformGoodsAmountSum = canSharePlatformGoodsAmountSum.add(goods.getGoodsSkuAmount()).setScale(twoScale,
                                    BigDecimal.ROUND_HALF_UP);
                        }
                    }
                }

                if (ObjectUtil.isNotNull(platformCouponActivity)
                    && canSharePlatformGoodsAmountSum.compareTo(platformCouponActivity.getThresholdValue()) >= 0) {
                    platformCouponActivityId = platformCouponActivity.getId();
                    platformCouponActivityName = platformCouponActivity.getName();
                    platformRatio = platformCouponActivity.getPlatformRatio();
                    businessRatio = platformCouponActivity.getBusinessRatio();

                    Integer couponType = platformCouponActivity.getType();
                    // 平台券优惠总金额，满减券、满折券  获取优惠金额
                    platformDiscountValue = getDiscountAmount(platformCouponActivity, canSharePlatformGoodsAmountSum);

                    // 平台券优惠剩余金额
                    BigDecimal platformRemainAmount = platformDiscountValue;
                    // 所有可参与平台优惠券的商品总金额
                    //                    BigDecimal totalAmountSum = canShareEidGoodsMapPlatform.values().stream().map(g -> new BigDecimal(g.getGoodsSkuAmount().toString())).reduce(BigDecimal.ZERO, BigDecimal::add);

                    int goodsIndex = 0;
                    for (OrderUseCouponBudgetEnterpriseDTO enterpriseGoods : enterpriseGoodsList) {
                        Long shopEid = enterpriseGoods.getEid();
                        // 当前企业分摊的平台优惠金额
                        BigDecimal platformDiscountAmount = BigDecimal.ZERO;
                        for (OrderUseCouponBudgetGoodsDetailDTO goodsDetail : enterpriseGoods.getGoodsSkuDetailList()) {
                            OrderUseCouponBudgetGoodsDetailRequest budgetGoodsDetail = canShareEidGoodsMapPlatform
                                .get(shopEid + "_" + goodsDetail.getGoodsSkuId());
                            if (ObjectUtil.isNotNull(budgetGoodsDetail)) {
                                goodsIndex++;
                                // 商家优惠后金额
                                //BigDecimal goodsSkuDiscountAmount = goodsDetail.getGoodsSkuDiscountAmount();
                                BigDecimal goodsSkuDiscountAmount = goodsDetail.getGoodsSkuAmount();
                                // 分摊金额
                                BigDecimal platformShareAmount;
                                if (goodsIndex < canShareEidGoodsMapPlatform.size()) {
                                    platformShareAmount = platformDiscountValue
                                        .multiply(goodsSkuDiscountAmount.divide(canSharePlatformGoodsAmountSum, 6, BigDecimal.ROUND_HALF_UP))
                                        .setScale(twoScale, BigDecimal.ROUND_HALF_UP);
                                    // 平台剩余优惠总额
                                    platformRemainAmount = platformRemainAmount.subtract(platformShareAmount).setScale(twoScale,
                                        BigDecimal.ROUND_HALF_UP);
                                } else {
                                    platformShareAmount = platformRemainAmount;
                                    log.info("orderUseCouponShareAmountBudget, 平台券优惠最后余数 -> {}", platformShareAmount);
                                }
                                platformDiscountAmount = platformDiscountAmount.add(platformShareAmount).setScale(twoScale, BigDecimal.ROUND_HALF_UP);
                                // 商家+平台优惠后金额
                                /*goodsSkuDiscountAmount = goodsSkuDiscountAmount.subtract(platformShareAmount).setScale(twoScale,
                                    BigDecimal.ROUND_HALF_UP);*/
                                goodsSkuDiscountAmount = goodsDetail.getGoodsSkuDiscountAmount().subtract(platformShareAmount).setScale(twoScale,
                                        BigDecimal.ROUND_HALF_UP);
                                goodsDetail.setPlatformShareAmount(platformShareAmount);
                                goodsDetail.setGoodsSkuDiscountAmount(goodsSkuDiscountAmount);
                            }
                        }
                        // 当前企业分摊的平台优惠金额
                        enterpriseGoods.setPlatformDiscountAmount(platformDiscountAmount);
                        log.info("orderUseCouponShareAmountBudget, enterpriseGoods -> {}", JSON.toJSONString(enterpriseGoods));
                    }
                }

                // 平台优惠券活动基本信息
                orderUseCouponBudget.setPlatformCouponId(platformCouponId);
                orderUseCouponBudget.setPlatformCouponActivityId(platformCouponActivityId);
                orderUseCouponBudget.setPlatformCouponName(platformCouponActivityName);
                orderUseCouponBudget.setPlatformRatio(platformRatio);
                orderUseCouponBudget.setBusinessRatio(businessRatio);
                // 明细
                orderUseCouponBudget.setEnterpriseGoodsList(enterpriseGoodsList);
                // 商家优惠券抵扣金额
                orderUseCouponBudget.setTotalBusinessDiscountAmount(totalBusinessDiscountAmount);
                // 平台优惠券抵扣金额
                orderUseCouponBudget.setTotalPlatformDiscountAmount(platformDiscountValue);
                log.info("orderUseCouponShareAmountBudget, orderUseCouponBudget -> {}", JSON.toJSONString(orderUseCouponBudget));
                return orderUseCouponBudget;
            }
        } catch (Exception e) {
            log.warn("orderUseCouponShareAmountBudget 优惠券分摊异常 usetId -> {}, exception -> {}", request.getOpUserId(), e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.ORDER_USE_COUPON_SHARE_AMOUNT_BUDGET_ERROR, e.getMessage());
        }
        return null;
    }

    private BigDecimal businessBudget(Map<Long, CouponDO> couponMap, Map<Long, CouponActivityDO> couponActivityMap,
                                      Map<String, OrderUseCouponBudgetGoodsDetailRequest> canShareEidGoodsMap,
                                      List<OrderUseCouponBudgetEnterpriseDTO> enterpriseGoodsList, int twoScale,
                                      Map<String, List<OrderUseCouponBudgetGoodsDetailRequest>> goodsDetailMap,
                                      Map<String, List<OrderUseCouponBudgetGoodsDetailRequest>> canShareGoodsMap) {
        BigDecimal totalBusinessDiscountAmount = BigDecimal.ZERO;
        OrderUseCouponBudgetEnterpriseDTO budgetEnterprise;
        for (Map.Entry<String, List<OrderUseCouponBudgetGoodsDetailRequest>> entry : goodsDetailMap.entrySet()) {
            String eidCouponIdKey = entry.getKey();
            List<OrderUseCouponBudgetGoodsDetailRequest> goodsDetailList = entry.getValue();
            OrderUseCouponBudgetGoodsDetailRequest goodsDetailOne = goodsDetailList.get(0);
            // 商品总金额
            BigDecimal totalAmount = BigDecimal.ZERO;

            // 当前企业可分摊商品总金额
            List<OrderUseCouponBudgetGoodsDetailRequest> goodsDetailRequestList = canShareGoodsMap.get(eidCouponIdKey);
            BigDecimal canShareGoodsAmountSum = BigDecimal.ZERO;
            if (CollUtil.isNotEmpty(goodsDetailRequestList)) {
                canShareGoodsAmountSum = goodsDetailRequestList.stream().map(g -> new BigDecimal(g.getGoodsSkuAmount().toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            budgetEnterprise = new OrderUseCouponBudgetEnterpriseDTO();
            // 基本信息
            Long eid = goodsDetailOne.getEid();
            Long couponId = goodsDetailOne.getCouponId();
            budgetEnterprise.setEid(eid);
            budgetEnterprise.setCouponId(couponId);
            budgetEnterprise.setPlatformDiscountAmount(BigDecimal.ZERO);

            // 门槛金额
            BigDecimal thresholdValue = BigDecimal.ZERO;
            // 优惠总金额（满减券直接取值，满折券在折扣金额、最高优惠金额中取小者）
            BigDecimal discountValue = BigDecimal.ZERO;
            // 商家券优惠剩余金额
            BigDecimal businessRemainAmount = BigDecimal.ZERO;
            // 承担比例
            BigDecimal platformRatio = BigDecimal.ZERO;
            BigDecimal businessRatio = BigDecimal.ZERO;
            // 此优惠券是否可叠加赠品
            boolean isSuportPromotionGoods = false;

            /* 商家券 */
            CouponDO coupon = couponMap.get(couponId);
            // 优惠券活动
            CouponActivityDO couponActivity = null;
            if (ObjectUtil.isNotNull(coupon)) {
                couponActivity = couponActivityMap.get(coupon.getCouponActivityId());
            }

            if (ObjectUtil.isNotNull(couponActivity)) {
                thresholdValue = couponActivity.getThresholdValue();
                platformRatio = couponActivity.getPlatformRatio();
                businessRatio = couponActivity.getBusinessRatio();
                // 满减券、满折券  获取优惠金额
                discountValue = getDiscountAmount(couponActivity, canShareGoodsAmountSum);
                businessRemainAmount = discountValue;
                budgetEnterprise.setCouponActivityId(couponActivity.getId());
                isSuportPromotionGoods = getSuportPromotionGoodsFlag(couponActivity);
            }
            boolean shareFlag = true;
            if (ObjectUtil.isNull(couponActivity) || canShareGoodsAmountSum.compareTo(couponActivity.getThresholdValue()) < 0) {
                shareFlag = false;
                if (ObjectUtil.isNotNull(coupon)) {
                    throw new BusinessException(CouponErrorCode.THRESHOLD_VALUE_NOT_FIT_ERROR);
                }
            }

            // 所有商家优惠总金额 是否已添加
            boolean totalBusinessDiscountFlag = false;

            // 当前企业可分摊的商品数量
            long canShareCount = goodsDetailList.stream()
                .filter(g -> ObjectUtil.isNotNull(canShareEidGoodsMap.get(g.getEid() + "_" + g.getGoodsSkuId()))).count();
            int enterpriseIndex = 0;

            List<OrderUseCouponBudgetGoodsDetailDTO> goodsSkuDetailList = new ArrayList<>();
            OrderUseCouponBudgetGoodsDetailDTO budgetGoodsDetail;
            for (OrderUseCouponBudgetGoodsDetailRequest goodsDetailRequest : goodsDetailList) {
                OrderUseCouponBudgetGoodsDetailRequest goods = canShareEidGoodsMap
                    .get(goodsDetailRequest.getEid() + "_" + goodsDetailRequest.getGoodsSkuId());

                // 基本信息
                budgetGoodsDetail = new OrderUseCouponBudgetGoodsDetailDTO();
                budgetGoodsDetail.setGoodsId(goodsDetailRequest.getGoodsId());
                budgetGoodsDetail.setGoodsSkuId(goodsDetailRequest.getGoodsSkuId());
                budgetGoodsDetail.setGoodsSkuAmount(goodsDetailRequest.getGoodsSkuAmount());
                totalAmount = totalAmount.add(goodsDetailRequest.getGoodsSkuAmount()).setScale(twoScale, BigDecimal.ROUND_HALF_UP);

                if (!shareFlag || ObjectUtil.isNull(goods)) {
                    // 订单支付金额不满足分摊
                    budgetGoodsDetail.setBusinessShareAmount(BigDecimal.ZERO);
                    budgetGoodsDetail.setPlatformShareAmount(BigDecimal.ZERO);
                    budgetGoodsDetail.setGoodsSkuDiscountAmount(goodsDetailRequest.getGoodsSkuAmount());
                    goodsSkuDetailList.add(budgetGoodsDetail);
                } else {
                    enterpriseIndex++;

                    // 所有商家优惠总金额
                    if (!totalBusinessDiscountFlag) {
                        totalBusinessDiscountAmount = totalBusinessDiscountAmount.add(discountValue).setScale(twoScale, BigDecimal.ROUND_HALF_UP);
                        totalBusinessDiscountFlag = true;
                    }

                    // 商品金额
                    BigDecimal goodsSkuAmount = goodsDetailRequest.getGoodsSkuAmount();
                    // 分摊金额
                    BigDecimal shareAmount;
                    if (enterpriseIndex < canShareCount) {
                        shareAmount = discountValue.multiply(goodsSkuAmount.divide(canShareGoodsAmountSum, 6, BigDecimal.ROUND_HALF_UP))
                            .setScale(twoScale, BigDecimal.ROUND_HALF_UP);
                        shareAmount = shareAmount.compareTo(goodsSkuAmount) >= 0 ? goodsSkuAmount : shareAmount;
                        // 商家剩余优惠金额
                        businessRemainAmount = businessRemainAmount.subtract(shareAmount).setScale(twoScale, BigDecimal.ROUND_HALF_UP);
                    } else {
                        shareAmount = businessRemainAmount;
                    }
                    // 优惠后金额
                    BigDecimal goodsSkuDiscountAmount = goodsSkuAmount.subtract(shareAmount).setScale(twoScale, BigDecimal.ROUND_HALF_UP);

                    budgetGoodsDetail.setBusinessShareAmount(shareAmount);
                    budgetGoodsDetail.setPlatformShareAmount(BigDecimal.ZERO);
                    budgetGoodsDetail.setGoodsSkuDiscountAmount(goodsSkuDiscountAmount);
                    goodsSkuDetailList.add(budgetGoodsDetail);
                    log.info("orderUseCouponShareAmountBudget, budgetGoodsDetail -> {}", JSON.toJSONString(budgetGoodsDetail));
                }
            }
            budgetEnterprise.setPlatformRatio(platformRatio);
            budgetEnterprise.setBusinessRatio(businessRatio);
            budgetEnterprise.setTotalAmount(totalAmount);
            budgetEnterprise.setBusinessDiscountAmount(discountValue);
            budgetEnterprise.setGoodsSkuDetailList(goodsSkuDetailList);
            enterpriseGoodsList.add(budgetEnterprise);
            log.info("orderUseCouponShareAmountBudget, budgetEnterprise -> {}", JSON.toJSONString(budgetEnterprise));
        }
        return totalBusinessDiscountAmount;
    }

    private BigDecimal getDiscountAmount(CouponActivityDO couponActivity, BigDecimal canShareGoodsAmountSum) {
        BigDecimal discountValue = BigDecimal.ZERO;
        if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), couponActivity.getType())) {
            // 满减券
            discountValue = couponActivity.getDiscountValue();
        } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), couponActivity.getType())) {
            // 满折券
            // 折扣
            BigDecimal discount = couponActivity.getDiscountValue();
            // 折扣金额
            BigDecimal discountAmount = canShareGoodsAmountSum
                .multiply(new BigDecimal("1").subtract(discount.divide(new BigDecimal("100"), 6, BigDecimal.ROUND_HALF_UP)));
            // 最高优惠金额
            BigDecimal discountMaxAmount = couponActivity.getDiscountMax();
            // 最高优惠金额=0, 则不限制最高优惠，取折扣金额
            if(ObjectUtil.isNull(discountMaxAmount) || discountMaxAmount.compareTo(BigDecimal.ZERO) == 0){
                discountValue = discountAmount;
            } else {
                // 最高优惠金额、折扣金额，取最小值
                discountValue = discountAmount.compareTo(discountMaxAmount) >= 0 ? discountMaxAmount : discountAmount;
            }
        }
        return  discountValue.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void checkCouponActivity(OrderUseCouponBudgetRequest request, Long currentEid, long nowTime, String desc,
                                     List<CouponActivityDO> platformCouponActivityList, Map<Long, List<CouponDO>> relationMap,
                                     boolean platformCouponFlag) {
        Date date = new Date();
        for (CouponActivityDO couponActivity : platformCouponActivityList) {
            // 平台类型
            Integer platformLimit = couponActivity.getPlatformLimit();
            String couponName = platformCouponFlag ? "平台优惠券:".concat(couponActivity.getName()) : "店铺优惠券:".concat(couponActivity.getName());
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), platformLimit)) {
                CouponPlatformTypeEnum platformTypeEnum = CouponPlatformTypeEnum.transTypeFromPlatformEnum(request.getPlatform());
                boolean platformSelectedFlag = true;
                String platformSelected = couponActivity.getPlatformSelected();
                List<Integer> platformSelectedList = Convert.toList(Integer.class, platformSelected);
                if (CollUtil.isNotEmpty(platformSelectedList) && !platformSelectedList.contains(platformTypeEnum.getCode())) {
                    platformSelectedFlag = false;
                }
                if (ObjectUtil.isNull(platformTypeEnum) || !platformSelectedFlag) {
                    log.warn(
                        "orderUseCouponShareAmountBudget , 此优惠券活动不支持的平台类型，不能使用，请选择其他平台优惠券,  currentEid -> {}, platformCouponId -> {}, platform -> {}",
                        currentEid, couponActivity.getId(), request.getPlatform());
                    String nameByCodeList = CouponPlatformTypeEnum.getNameByCodeList(platformSelectedList);
                    String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_PLATFORM_ERROR.getMessage(), couponName, nameByCodeList);
                    throw new BusinessException(CouponErrorCode.COUPON_USED_PLATFORM_ERROR, errorMsg);
                }
            }
            // 状态
            Integer status = couponActivity.getStatus();
            if (ObjectUtil.equal(CouponActivityStatusEnum.SCRAP.getCode(), status)) {
                log.warn("orderUseCouponShareAmountBudget , 此优惠券活动已废弃，不能使用，请选择其他" + desc + ",  currentEid -> {}, couponId -> {}", currentEid,
                    couponActivity.getId());
                String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_STATUS_ERROR.getMessage(), couponActivity.getId(), desc);
                throw new BusinessException(CouponErrorCode.COUPON_STATUS_ERROR, errorMsg);
            }
            // 时间
//            if (couponActivity.getBeginTime().getTime() > nowTime) {
//                log.error("orderUseCouponShareAmountBudget , 此优惠券活动未开始，不能使用，请选择其他" + desc + ",  currentEid -> {}, couponId -> {}", currentEid,
//                    couponActivity.getId());
//                String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_BEGIN_TIME_ERROR.getMessage(), couponActivity.getId(), desc);
//                throw new BusinessException(CouponErrorCode.COUPON_USED_BEGIN_TIME_ERROR, errorMsg);
//            }
//            if (couponActivity.getEndTime().getTime() < nowTime) {
//                log.error("orderUseCouponShareAmountBudget , 此优惠券已过期，不能使用，请选择其他" + desc + ", currentEid -> {}, couponId -> {}", currentEid,
//                    couponActivity.getId());
//                String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_END_TIME_ERROR.getMessage(), couponActivity.getId(), desc);
//                throw new BusinessException(CouponErrorCode.COUPON_USED_END_TIME_ERROR, errorMsg);
//            }

            // 支付方式
            if (MapUtil.isNotEmpty(relationMap)) {
                Integer payMethodLimit = couponActivity.getPayMethodLimit();

                /*if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), payMethodLimit)) {
                    List<Integer> payMethodSelecteds = Convert.toList(Integer.class, couponActivity.getPayMethodSelected());
                    List<Long> couponIdList = relationMap.get(couponActivity.getId()).stream().map(CouponDO::getId).collect(Collectors.toList());
                    if (CollUtil.isEmpty(couponIdList)) {
                        log.warn("orderUseCouponShareAmountBudget , 付款的支付方式不能为空，不能使用，请选择其他" + desc
                                  + ",  currentEid -> {}, platformCouponId -> {}, payMethod -> {}",
                            currentEid, couponActivity.getId(), null);
                        String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_PAY_METHOD_ERROR.getMessage(), couponActivity.getName(),
                            "", desc);
                        throw new BusinessException(CouponErrorCode.COUPON_USED_PAY_METHOD_ERROR, errorMsg);
                    }
                    request.getGoodsSkuDetailList().forEach(g -> {
                        if (ObjectUtil.isNull(g.getPayMethod())) {
                            throw new BusinessException(CouponErrorCode.COUPON_USED_PAY_METHOD_ERROR, "店铺[id:" + g.getEid() + "], 支付方式不能为空");
                        }
                    });

                    List<Integer> payMethodList = null;
                    if (couponActivity.getSponsorType() == 1) {
                        payMethodList = request.getGoodsSkuDetailList().stream().filter(g -> couponIdList.contains(g.getPlatformCouponId())).map(g -> CouponPayMethodTypeEnum.transTypeFromPaymentMethodEnum(g.getPayMethod()).getCode()).collect(Collectors.toList());
                    }
                    if (couponActivity.getSponsorType() == 2) {
                        payMethodList = request.getGoodsSkuDetailList().stream().filter(g -> couponIdList.contains(g.getCouponId())).map(g -> CouponPayMethodTypeEnum.transTypeFromPaymentMethodEnum(g.getPayMethod()).getCode()).collect(Collectors.toList());
                    }
                    // 匹配
                    String payMethodName = CouponPayMethodTypeEnum.getNameByCodeList(payMethodSelecteds);
                    for (Integer payMethod : payMethodList) {
                        if(!payMethodSelecteds.contains(payMethod)){
                            log.warn("orderUseCouponShareAmountBudget , 此优惠券活动不支持的支付方式，不能使用，请选择其他" + desc
                                            + ",  currentEid -> {}, platformCouponId -> {}, payMethod -> {}",
                                    currentEid, couponActivity.getId(), payMethod);

                            String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_PAY_METHOD_ERROR.getMessage(), couponName,
                                    payMethodName, desc);
                            throw new BusinessException(CouponErrorCode.COUPON_USED_PAY_METHOD_ERROR, errorMsg);
                        }
                    }
                }*/
            }
        }
    }

    private OrderUseCouponBudgetDTO initOrderUseCouponBudgetDTO(OrderUseCouponBudgetRequest request) {
        OrderUseCouponBudgetDTO orderUseCouponBudget = PojoUtils.map(request, OrderUseCouponBudgetDTO.class);
        long platformCouponActivityId = 0;
        String platformCouponActivityName = "";
        BigDecimal platformRatio = BigDecimal.ZERO;
        BigDecimal businessRatio = BigDecimal.ZERO;

        orderUseCouponBudget.setPlatformCouponActivityId(platformCouponActivityId);
        orderUseCouponBudget.setPlatformCouponName(platformCouponActivityName);
        orderUseCouponBudget.setPlatformRatio(platformRatio);
        orderUseCouponBudget.setBusinessRatio(businessRatio);
        orderUseCouponBudget.setTotalBusinessDiscountAmount(BigDecimal.ZERO);
        orderUseCouponBudget.setTotalPlatformDiscountAmount(BigDecimal.ZERO);
        for (OrderUseCouponBudgetEnterpriseDTO enterpriseResponse : orderUseCouponBudget.getEnterpriseGoodsList()) {
            enterpriseResponse.setCouponActivityId(platformCouponActivityId);
            enterpriseResponse.setCouponName(platformCouponActivityName);
            enterpriseResponse.setPlatformRatio(platformRatio);
            enterpriseResponse.setBusinessRatio(businessRatio);
            enterpriseResponse.setBusinessDiscountAmount(BigDecimal.ZERO);
            enterpriseResponse.setPlatformDiscountAmount(BigDecimal.ZERO);
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (OrderUseCouponBudgetGoodsDetailDTO goodsResponse : enterpriseResponse.getGoodsSkuDetailList()) {
                goodsResponse.setBusinessShareAmount(BigDecimal.ZERO);
                goodsResponse.setPlatformShareAmount(BigDecimal.ZERO);
                goodsResponse.setGoodsSkuDiscountAmount(goodsResponse.getGoodsSkuAmount());
                totalAmount = totalAmount.add(goodsResponse.getGoodsSkuAmount());
            }
            enterpriseResponse.setTotalAmount(totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return orderUseCouponBudget;
    }

    private void checkCouponStatus(Long currentEid, long nowTime, Long couponId, CouponDO coupon, String des) {
        if (ObjectUtil.isNull(coupon) || ObjectUtil.equal(CouponUsedStatusEnum.USED.getCode(), coupon.getUsedStatus())
            || ObjectUtil.equal(CouponStatusEnum.SCRAP_COUPON.getCode(), coupon.getStatus())) {
            log.warn("orderUseCouponShareAmountBudget , 未查询到此优惠券, 此优惠券已经被使用，或已废弃，请选择其他优惠券, currentEid -> {}, platformCouponId -> {}", currentEid,
                couponId);
            String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), couponId, des);
            throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR, errorMsg);
        }
        if (!ObjectUtil.equal(currentEid, coupon.getEid())) {
            log.warn("orderUseCouponShareAmountBudget , 您不能使用此优惠券，请选择其他优惠券,  currentEid -> {}, platformCouponId -> {}", currentEid, couponId);
            String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OWN_ERROR.getMessage(), couponId, des);
            throw new BusinessException(CouponErrorCode.COUPON_USED_OWN_ERROR, errorMsg);
        }
        if (coupon.getBeginTime().getTime() > nowTime) {
            log.warn("orderUseCouponShareAmountBudget , 此优惠券活动未开始，不能使用，请选择其他平台优惠券,  currentEid -> {}, platformCouponId -> {}", currentEid, couponId);
            String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_BEGIN_TIME_ERROR.getMessage(), couponId, des);
            throw new BusinessException(CouponErrorCode.COUPON_USED_BEGIN_TIME_ERROR, errorMsg);
        }
        if (coupon.getEndTime().getTime() < nowTime) {
            log.warn("orderUseCouponShareAmountBudget , 此优惠券已过期，不能使用，请选择其他平台优惠券, currentEid -> {}, platformCouponId -> {}", currentEid, couponId);
            String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_END_TIME_ERROR.getMessage(), couponId, des);
            throw new BusinessException(CouponErrorCode.COUPON_USED_END_TIME_ERROR, errorMsg);
        }
    }

    @Override
    public Boolean orderUseCoupon(OrderUseCouponRequest request) {
        log.info("orderUseCoupon request -> {}", JSON.toJSONString(request));
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getOpUserId())
            || (ObjectUtil.isNull(request.getPlatformCouponId()) && CollUtil.isEmpty(request.getCouponIdList()))) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        try {
            Long platformCouponId = request.getPlatformCouponId();
            List<Long> couponIdList = request.getCouponIdList();

            // 商家优惠券
            List<Long> couponActivityIdList = new ArrayList<>();
            if (CollUtil.isNotEmpty(couponIdList)) {
                List<CouponDO> couponList = couponService.getEffectiveListByIdList(couponIdList);
                if (CollUtil.isEmpty(couponList)) {
                    log.warn("orderUseCoupon , 优惠券已经被使用，或已过期，请选择其他优惠券, id -> {}", couponIdList.toString());
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR);
                }
                couponActivityIdList = couponList.stream().map(CouponDO::getCouponActivityId).distinct().collect(Collectors.toList());
                // 查询优惠券活动
                List<CouponActivityDO> businessCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIdList, 1,
                    1);
                if (CollUtil.isEmpty(businessCouponActivityList)) {
                    log.warn("orderUseCoupon , 未查询到此商家优惠券活动, 此优惠券已经被使用，或已过期，请选择其他优惠券");
                    String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), "商家优惠券");
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR, errorMsg);
                }
            }

            // 平台优惠券
            CouponDO platformCoupon = null;
            CouponActivityDO platformCouponActivity = null;
            if (ObjectUtil.isNotNull(platformCouponId)) {
                List<CouponDO> platformCouponList = couponService.getEffectiveListByIdList(Arrays.asList(platformCouponId));
                if (CollUtil.isEmpty(platformCouponList)) {
                    log.warn("orderUseCoupon , 优惠券已经被使用，或已过期，请选择其他优惠券, id -> {}", platformCouponId);
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR);
                }
                platformCoupon = platformCouponList.get(0);

                List<Long> platformCouponActivityIdList = platformCouponList.stream().map(CouponDO::getCouponActivityId).distinct()
                    .collect(Collectors.toList());
                // 查询优惠券活动
                List<CouponActivityDO> platformCouponActivityList = couponActivityService
                    .getEffectiveCouponActivityByIdList(platformCouponActivityIdList, 1, 1);
                if (CollUtil.isEmpty(platformCouponActivityList)) {
                    log.warn("orderUseCoupon , 未查询到此平台优惠券活动, 此优惠券已经被使用，或已过期，请选择其他优惠券");
                    String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), "平台优惠券");
                    throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR, errorMsg);
                }
                platformCouponActivity = platformCouponActivityList.get(0);
            }

            // 使用优惠券
            Long eid = request.getEid();
            // 更新优惠券卡包
            List<Long> couponIds = new ArrayList<>();
            if (ObjectUtil.isNotNull(platformCouponId)) {
                couponIds.add(platformCouponId);
            }
            if (CollUtil.isNotEmpty(couponIdList)) {
                couponIds.addAll(couponIdList);
            }
            if (CollUtil.isNotEmpty(couponIds)) {
                Boolean result = couponService.useCoupon(couponIds, request.getOpUserId());
                if (!result) {
                    log.warn("orderUseCoupon, useCoupon false, request -> {}", JSON.toJSONString(request));
                }
            }

            // 更新优惠券活动
            //            Map<Long, List<CouponDO>> couponCountMap = new HashMap<>();
            //            if (CollUtil.isNotEmpty(couponList)) {
            //                couponList.stream().collect(Collectors.groupingBy(CouponDO::getCouponActivityId));
            //            }
            //
            //            Map<Long, Integer> countMap = new HashMap<>();
            //            if (ObjectUtil.isNotNull(platformCouponActivity)) {
            //                countMap.put(platformCouponActivity.getId(), 1);
            //            }
            //            if (MapUtil.isNotEmpty(couponCountMap)) {
            //                for (Long couponActivityId : couponCountMap.keySet()) {
            //                    int size = couponCountMap.get(couponActivityId).size();
            //                    countMap.put(couponActivityId, size);
            //                }
            //            }
            //            log.info("orderUseCoupon countMap -> {}", JSON.toJSONString(countMap));
            //            couponActivityService.updateUseCountByIds(countMap, request.getOpUserId());
        } catch (Exception e) {
            log.warn("orderUseCoupon 提交订单使用优惠券异常, request -> {}, exception -> {}", request, e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.ORDER_USE_COUPON_ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean useMemberCoupon(UseMemberCouponRequest request) {
        log.info("购买会员使用会员券，参数 UseMemberCouponRequest request -> {}", JSON.toJSONString(request));
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCurrentUserId())
                || (ObjectUtil.isNull(request.getId()))) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        try {
            CouponDO couponDO = couponService.getById(request.getId());
            if (ObjectUtil.isEmpty(couponDO)) {
                log.warn("memeberUseCoupon , 优惠券已经被使用，或已过期，请选择其他优惠券, id -> {}", couponDO);
                throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR);
            }
            boolean available = couponDO.getBeginTime().before(new Date()) && couponDO.getEndTime().after(new Date());
            boolean useStatus = couponDO.getStatus()==1&&couponDO.getUsedStatus()==1;
            if (!available||!useStatus) {
                log.warn("memeberUseCoupon , 未查询到此平台优惠券活动, 此优惠券已经被使用，或已过期，请选择其他优惠券");
                String errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), "平台优惠券");
                throw new BusinessException(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR, errorMsg);
            }
            // 更新优惠券卡包
            List<Long> couponIds = new ArrayList<>();
             couponIds.add(request.getId());
             if (CollUtil.isNotEmpty(couponIds)) {
                Boolean result = couponService.useCoupon(couponIds, request.getCurrentUserId());
                if (!result) {
                    log.warn("memeberUseCoupon, useCoupon false, request -> {}", JSON.toJSONString(request));
                }
            }
        } catch (Exception e) {
            log.warn("orderUseCoupon 提交订单使用优惠券异常, request -> {}, exception -> {}", request, e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.ORDER_USE_COUPON_ERROR, e.getMessage());
        }
        return true;
    }

}
