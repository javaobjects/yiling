package com.yiling.marketing.couponactivity.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityTypeEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.coupon.dto.request.QueryCouponListPageRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.bo.CouponBO;
import com.yiling.marketing.couponactivity.dao.CouponActivityGoodsLimitMapper;
import com.yiling.marketing.couponactivity.dao.CouponActivityMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityForMemberResultDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityMyCouponDTO;
import com.yiling.marketing.couponactivity.dto.MemberStageDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityGoodsRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityMemberLimitDO;
import com.yiling.marketing.couponactivity.enums.PayMethodLimitEnum;
import com.yiling.marketing.couponactivity.enums.PayMethodTypeEnum;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityForMyService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityMemberLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Slf4j
@Service
public class CouponActivityForMyServiceImpl extends BaseServiceImpl<CouponActivityMapper, CouponActivityDO> implements CouponActivityForMyService {

    @Autowired
    private CouponActivityMapper couponActivityMapper;
    @Autowired
    private CouponActivityEnterpriseLimitService enterpriseLimitService;
    @Autowired
    private CouponActivityGoodsLimitService goodsLimitService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponActivityService couponActivityService;
    @Autowired
    private CouponActivityMemberLimitService couponActivityMemberLimitService;
    @Autowired
    private CouponActivityGoodsLimitMapper goodsLimitMapper;
    @DubboReference
    private MemberBuyStageApi memberBuyStageApi;

    @Override
    public Page<CouponActivityMyCouponDTO> getCouponListPageByEid(QueryCouponListPageRequest request) {
        log.info("getCouponListPageByEid, request -> {}", JSON.toJSONString(request));
        if (ObjectUtil.isNull(request)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        Page<CouponActivityMyCouponDTO> page = new Page();

        try {
            Page<CouponBO> couponBOPage = couponActivityMapper.getCouponAndActivityListPageByEid(request.getPage(), request);
            if (ObjectUtil.isNull(couponBOPage) || CollUtil.isEmpty(couponBOPage.getRecords())) {
                return page;
            }
            List<Long> activityIds = couponBOPage.getRecords().stream().map(CouponBO::getActivityId).collect(Collectors.toList());
            List<CouponActivityDO> couponActivityDOS = couponActivityService.listByIds(activityIds);
            Map<Long, CouponActivityDO> collect = couponActivityDOS.stream().collect(Collectors.toMap(CouponActivityDO::getId, Function.identity()));
            List<CouponBO> myCouponDTOS = new ArrayList<>();
            couponBOPage.getRecords().forEach(item->{
                CouponActivityDO couponActivityDO = collect.get(item.getActivityId());
                if(ObjectUtil.isNotNull(couponActivityDO)){
                    CouponBO myCouponDTO = PojoUtils.map(couponActivityDO, CouponBO.class);
                    myCouponDTO.setId(item.getId());
                    myCouponDTO.setEid(item.getEid());
                    myCouponDTO.setBeginTime(item.getBeginTime());
                    myCouponDTO.setEndTime(item.getEndTime());
                    myCouponDTO.setUsedStatus(request.getUsedStatusType());
                    myCouponDTO.setUserId(item.getUserId());
                    myCouponDTO.setUserName(item.getUserName());
                    myCouponDTO.setActivityId(couponActivityDO.getId());
                    myCouponDTO.setActivityName(couponActivityDO.getName());
                    myCouponDTO.setActivityEid(couponActivityDO.getEid());
                    myCouponDTO.setActivityEname(couponActivityDO.getEname());
                    myCouponDTOS.add(myCouponDTO);
                }
            });
            couponBOPage.setRecords(myCouponDTOS);
            page = PojoUtils.map(couponBOPage, CouponActivityMyCouponDTO.class);
            // 部分商品可用
            //List<Long> activityIdList = couponBOPage.getRecords().stream().filter(c -> ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getGoodsLimit())).map(CouponBO::getActivityId).distinct().collect(Collectors.toList());
            Map<Long, Set<String>> enterpriseNameMap = new HashMap<>();
            // 查询部分商品
            /*List<CouponActivityGoodsLimitDO> goodsLimitList = goodsLimitService.getListByCouponActivityIdList(activityIdList);
            if (CollUtil.isNotEmpty(goodsLimitList)) {
                for (CouponActivityGoodsLimitDO goodsLimit : goodsLimitList) {
                    getEnterpriseNameMap(enterpriseNameMap, goodsLimit.getEid(), goodsLimit.getEname());
                }
            }*/
            // 部分商家可用
            List<Long> activityIdList2 = couponBOPage.getRecords().stream().filter(c -> (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getEnterpriseLimit()) || (ObjectUtil.isNotNull(c.getEid()) && c.getEid().intValue() != 0)) && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit())).map(CouponBO::getActivityId).distinct().collect(Collectors.toList());
            List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = enterpriseLimitService.getByCouponActivityIdList(activityIdList2);
            if (CollUtil.isNotEmpty(enterpriseLimitList)) {
                for (CouponActivityEnterpriseLimitDO enterpriseLimit : enterpriseLimitList) {
                    getEnterpriseNameMap(enterpriseNameMap, enterpriseLimit.getEid(), enterpriseLimit.getEname());
                }
            }

            List<CouponActivityMyCouponDTO> list = new ArrayList<>();
            CouponActivityMyCouponDTO dto;
            int index = 0;
            for (CouponBO couponBO : couponBOPage.getRecords()) {
                dto = PojoUtils.map(couponBO, CouponActivityMyCouponDTO.class);
                // 状态类型
                dto.setUsedStatusType(request.getUsedStatusType());
                buildMyCoupon(dto, couponBO, enterpriseNameMap);
                dto.setGoodsLimit(dto.getGoodsLimit() == 1 && dto.getEnterpriseLimit() == 1 ? 1 : 2);
                list.add(index, dto);
                index++;
            }
            // 是否未使用的列表
            boolean isNotUsed = request.getUsedStatusType() == 1;
            if (CollectionUtils.isNotEmpty(list)) {
                List<Long> couponIds = list.stream().map(CouponActivityMyCouponDTO::getActivityId).collect(Collectors.toList());
                List<CouponActivityMemberLimitDO> couponActivityMemberLimitDOS = getMemberIdByCouponsId(couponIds);
                Map<Long, List<CouponActivityMemberLimitDO>>couponLimits=new HashMap<>();
                if (CollectionUtils.isNotEmpty(couponActivityMemberLimitDOS)) {
                    couponLimits = couponActivityMemberLimitDOS.stream().collect(Collectors.groupingBy(CouponActivityMemberLimitDO::getCouponActivityId));
                }
                Map<Long, List<CouponActivityMemberLimitDO>> finalCouponLimits = couponLimits;
                List<Long> allMemberIds = new ArrayList<>();
                List<Long> finalAllMemberIds = allMemberIds;
                list.forEach(item -> {
                    // 拼凑几天后到期
                    if(isNotUsed){
                        boolean sameDay = DateUtil.isSameDay(new Date(), item.getEndTime());
                        if(sameDay){
                            item.setDueDateDescribe("今日到期");
                        }else {
                            long endDay = DateUtil.betweenDay(new Date(), item.getEndTime(), true);
                            item.setDueDateDescribe(endDay+"天后到期");
                        }
                    }
                    List<CouponActivityMemberLimitDO> couponActivityMemberLimitDOS1 = finalCouponLimits.get(item.getActivityId());
                    if (CollectionUtils.isNotEmpty(couponActivityMemberLimitDOS1)) {
                        List<Long> memberIds = couponActivityMemberLimitDOS1.stream().map(CouponActivityMemberLimitDO::getMemberId).collect(Collectors.toList());
                        // 如果会员优惠券设置关联的可用会员规格id
                        item.setMemberIds(memberIds);
                        finalAllMemberIds.addAll(memberIds);
                    }
                });

                // 给会员优惠券加上具体规格id
                if(CollectionUtils.isNotEmpty(finalAllMemberIds)){
                    allMemberIds = allMemberIds.stream().distinct().collect(Collectors.toList());
                    List<MemberBuyStageDTO> memberBuyStageDTOS = memberBuyStageApi.listByIds(allMemberIds);
                    Map<Long, Long> stageIdAndMemberIdMap = memberBuyStageDTOS.stream().collect(Collectors.toMap(MemberBuyStageDTO::getId, MemberBuyStageDTO::getMemberId));
                    list.forEach(item->{
                        List<Long> memberIds1 = item.getMemberIds();
                        if(CollectionUtils.isNotEmpty(memberIds1)){
                            Map<Long, List<Long>> memberStageList = new HashMap<>();
                            memberIds1.forEach(item2->{
                                Long memberID = stageIdAndMemberIdMap.get(item2);
                                List<Long> stageIds = memberStageList.get(memberID);
                                if(ObjectUtil.isNotNull(memberID)){
                                    if(CollectionUtils.isEmpty(stageIds)){
                                        List<Long> objects = new ArrayList<>();
                                        objects.add(item2);
                                        memberStageList.put(memberID,objects);
                                    }else {
                                        stageIds.add(item2);
                                    }
                                }
                            });
                            if(ObjectUtil.isNotNull(memberStageList)){
                                Iterator<Map.Entry<Long, List<Long>>> iterator1 = memberStageList.entrySet().iterator();
                                List<MemberStageDTO> objects = new ArrayList<>();
                                while (iterator1.hasNext()){
                                    Map.Entry<Long, List<Long>> next = iterator1.next();
                                    MemberStageDTO memberStageDTO = new MemberStageDTO();
                                    memberStageDTO.setMemberId(next.getKey());
                                    memberStageDTO.setStageIds( next.getValue());
                                    objects.add(memberStageDTO);
                                }
                                item.setMemberStageList(objects);
                            }
                        }
                    });
                }
            }
            page.setRecords(list);
        } catch (Exception e) {
            log.error("查询优惠券信息异常，currentUserId -> {}, eid -> {}, exception -> {}", request.getCurrentUserId(), request.getEid(), e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.COUPON_PAGE_QUERY_ERROR);
        }
        return page;
    }

    private List<CouponActivityMemberLimitDO> getMemberIdByCouponsId(List<Long> couponIds) {
        QueryWrapper<CouponActivityMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getCouponActivityId, couponIds);
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getDelFlag, 0);
        return couponActivityMemberLimitService.list(queryWrapper);
    }

    @Override
    public CouponActivityEidOrGoodsIdDTO geGoodsListPageByCouponId(Long couponId, Long eid, QueryCouponActivityGoodsRequest request) {
        log.info("获取优惠券id{}，企业id{}的可以使用商品列表", couponId, eid);
        if (ObjectUtil.isNull(couponId)) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }
        CouponActivityEidOrGoodsIdDTO result = new CouponActivityEidOrGoodsIdDTO();
        String errorMsg = "";
        try {
            // 查询优惠券
            List<CouponDO> couponList = couponService.getEffectiveListByCouponActivityIdList(Arrays.asList(couponId), eid);
            if (CollUtil.isEmpty(couponList)) {
                errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_OR_EXPIRE_ERROR.getMessage(), couponId);
                result.setNotAvailableMessage(errorMsg);
                return result;
            }
            List<CouponDO> effectiveCouponDOList = couponList.stream().filter(item -> {
                return item.getUsedStatus().equals(CouponUsedStatusEnum.NOT_USED.getCode()) && item.getStatus().equals(CouponStatusEnum.NORMAL_COUPON.getCode()) && item.getEndTime().after(new Date()) && item.getBeginTime().before(new Date());
            }).collect(Collectors.toList());
            // 如果生效的未空，就判断失效原因
            CouponDO coupon = couponList.get(0);
            if (CollectionUtils.isEmpty(effectiveCouponDOList)) {
                // 目前全部按照有效期为固定时间的，生产环境没有领券后生效的。所以无论领取了几张券，生效时间和失效时间一样
                List<CouponDO> usedStatus = couponList.stream().filter(item -> item.getUsedStatus().intValue() == CouponUsedStatusEnum.NOT_USED.getCode().intValue()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(usedStatus)) {
                    errorMsg = "优惠券已经被使用";
                    result.setNotAvailableMessage(errorMsg);
                    return result;
                }
                List<CouponDO> notBegin = couponList.stream().filter(item -> item.getBeginTime().after(new Date())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(notBegin)) {
                    errorMsg = "优惠券未到使用时间";
                    result.setNotAvailableMessage(errorMsg);
                    return result;
                }
                List<CouponDO> expired = couponList.stream().filter(item -> item.getEndTime().before(new Date())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(expired)) {
                    errorMsg = "优惠券已经过期";
                    result.setNotAvailableMessage(errorMsg);
                    return result;
                }
                String desc = "优惠券";
                if (ObjectUtil.equal(CouponStatusEnum.SCRAP_COUPON.getCode(), coupon.getStatus())) {
                    errorMsg = MessageFormat.format(CouponErrorCode.COUPON_STATUS_ERROR.getMessage(), coupon.getCouponActivityName(), desc);
                    result.setNotAvailableMessage(errorMsg);
                    return result;
                }
                if (ObjectUtil.equal(CouponUsedStatusEnum.USED.getCode(), coupon.getUsedStatus())) {
                    errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_STATUS_ERROR.getMessage(), coupon.getCouponActivityName(), desc);
                    result.setNotAvailableMessage(errorMsg);
                    return result;
                }
                if (coupon.getEndTime().getTime() <= System.currentTimeMillis()) {
                    errorMsg = MessageFormat.format(CouponErrorCode.COUPON_USED_END_TIME_ERROR.getMessage(), coupon.getCouponActivityName(), desc);
                    result.setNotAvailableMessage(errorMsg);
                    return result;
                }
            }
            // 优惠券活动
            CouponActivityDO couponActivity = couponActivityService.getById(coupon.getCouponActivityId());
            if (ObjectUtil.isNull(couponActivity)) {
                return result;
            }
            List<Long> couponActivityIds = Arrays.asList(couponActivity.getId());

            // 全部商家、全部商品可用，平台创建
            if (ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), couponActivity.getEnterpriseLimit()) && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), couponActivity.getGoodsLimit())) {
                result.setAllEidFlag(true);
            }
            // 部分商家、全部商品可用，平台创建
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), couponActivity.getEnterpriseLimit()) && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), couponActivity.getGoodsLimit())) {
                List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = enterpriseLimitService.getByCouponActivityIdList(couponActivityIds);
                if (CollUtil.isNotEmpty(enterpriseLimitList)) {
                    List<Long> eidList = enterpriseLimitList.stream().filter(e -> ObjectUtil.isNotNull(e.getEid())).map(CouponActivityEnterpriseLimitDO::getEid).distinct().collect(Collectors.toList());
                    result.setEidList(eidList);
                }
            }
            // 商家自己创建的优惠券，且全部商品可用
            if (ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid().intValue() != 0 && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), couponActivity.getGoodsLimit())) {
                List<Long> eidList = new ArrayList<>();
                eidList.add(couponActivity.getEid());
                result.setEidList(eidList);
            }

            // 部分商品可用
            if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), couponActivity.getGoodsLimit())) {
                request.setSize(10);
                request.setCouponActivityId(couponId);
                Page<CouponActivityGoodsLimitDO> goodsLimitList = goodsLimitMapper.pageList(request.getPage(), request);
                if (CollUtil.isNotEmpty(goodsLimitList.getRecords())) {
                    List<Long> goodsIdList = goodsLimitList.getRecords().stream().filter(e -> ObjectUtil.isNotNull(e.getEid())).map(CouponActivityGoodsLimitDO::getGoodsId).distinct().collect(Collectors.toList());
                    result.setGoodsIdList(goodsIdList);
                }
            }
        } catch (Exception e) {
            log.error("去使用优惠券，查询可使用商品、企业异常，couponId -> {}, exception -> {}", couponId, e);
            e.printStackTrace();
            String errMsg = MessageFormat.format(CouponErrorCode.COUPON_TO_USE_ERROR.getMessage(), couponId);
            throw new BusinessException(CouponErrorCode.COUPON_TO_USE_ERROR, errorMsg);
        }
        return result;
    }

    @Override
    public CouponActivityForMemberResultDTO myAvailableMemberCouponList(QueryCouponListPageRequest request) {
        // 获取可用的已经领取的优惠券
        QueryWrapper<CouponDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(CouponDO::getEid, request.getEid()).eq(CouponDO::getUsedStatus, 1).
                eq(CouponDO::getStatus, 1).
                gt(CouponDO::getEndTime, new Date()).lt(CouponDO::getBeginTime, new Date());
        List<CouponDO> couponDOList = couponService.list(objectQueryWrapper);
        if(CollectionUtils.isEmpty(couponDOList)){
            return null;
        }
        List<Long> activityIds = couponDOList.stream().map(CouponDO::getCouponActivityId).collect(Collectors.toList());
        List<CouponActivityDetailDTO> couponActivityById = couponActivityService.getCouponActivityById(activityIds);
        if(CollectionUtils.isEmpty(couponActivityById)){
            return null;
        }
        // 获取会员优惠券id集合
        List<Long> activityId = couponActivityById.stream().filter(item -> item.getMemberType() == 2).map(CouponActivityDetailDTO::getId).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(activityId)){
            return null;
        }
        Map<Long, CouponActivityDetailDTO> activityDetailDTOMap = couponActivityById.stream().collect(Collectors.toMap(CouponActivityDetailDTO::getId, o -> o, (k1, k2) -> k1));

        QueryWrapper<CouponActivityMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getCouponActivityId, activityId);
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getDelFlag, 0);
        List<CouponActivityMemberLimitDO> couponActivityMemberLimitDOS = couponActivityMemberLimitService.list(queryWrapper);
        Map<Long, List<CouponActivityMemberLimitDO>> couponActivityMap=new HashMap<>();
        if(CollectionUtils.isNotEmpty(couponActivityMemberLimitDOS)){
            couponActivityMap = couponActivityMemberLimitDOS.stream().collect(Collectors.groupingBy(CouponActivityMemberLimitDO::getCouponActivityId));
        }
        List<Long> availableIds = new ArrayList<>();
        List<Long> notAvailable=new ArrayList<>();
        List<CouponActivityDetailDTO> activityDetailDTOS = couponActivityById.stream().filter(item -> item.getMemberType() == 2).collect(Collectors.toList());

        Map<Long, List<CouponActivityMemberLimitDO>> finalCouponMap = couponActivityMap;
        activityDetailDTOS.forEach(item->{
            if(item.getMemberLimit()==1){
                availableIds.add(item.getId());
            }else{
                List<CouponActivityMemberLimitDO> couponActivityList = finalCouponMap.get(item.getId());
                if(CollectionUtils.isNotEmpty(couponActivityList)){
                    boolean present = couponActivityList.stream().filter(item2 -> item2.getMemberId() == request.getMemberId()).findAny().isPresent();
                    if(present){
                        availableIds.add(item.getId());
                    }
                }
            }
        });
        Collection<Long> subtract = CollectionUtil.subtract(activityId, availableIds);
        notAvailable = new ArrayList<>(subtract);
        List<CouponActivityForMemberDTO> notAvailableCoupon = new ArrayList<>();
        List<CouponActivityForMemberDTO> availableCoupon = new ArrayList<>();
        Map<Long, CouponActivityDetailDTO> couponMap = couponActivityById.stream().collect(Collectors.toMap(CouponActivityDetailDTO::getId, e -> e));
        List<Long> finalNotAvailable = notAvailable;
        couponDOList.forEach(item -> {
            if (availableIds.contains(item.getCouponActivityId())) {
                CouponActivityDetailDTO couponActivityDetailDTO = couponMap.get(item.getCouponActivityId());
                if (ObjectUtil.isNotNull(couponActivityDetailDTO)) {
                    CouponActivityForMemberDTO couponActivityForMemberDTO = new CouponActivityForMemberDTO();
                    couponActivityForMemberDTO.setBeginTime(item.getBeginTime());
                    couponActivityForMemberDTO.setId(item.getId());
                    couponActivityForMemberDTO.setEndTime(item.getEndTime());
                    couponActivityForMemberDTO.setActivityId(item.getCouponActivityId());
                    couponActivityForMemberDTO.setName(couponActivityDetailDTO.getName());
                    couponActivityForMemberDTO.setDiscountValue(couponActivityDetailDTO.getDiscountValue());
                    couponActivityForMemberDTO.setType(couponActivityDetailDTO.getType());
                    availableCoupon.add(couponActivityForMemberDTO);
                }
            }
            if (finalNotAvailable.contains(item.getCouponActivityId())) {
                CouponActivityDetailDTO couponActivityDetailDTO = couponMap.get(item.getCouponActivityId());
                if (ObjectUtil.isNotNull(couponActivityDetailDTO)) {
                    CouponActivityForMemberDTO couponActivityForMemberDTO = new CouponActivityForMemberDTO();
                    couponActivityForMemberDTO.setBeginTime(item.getBeginTime());
                    couponActivityForMemberDTO.setId(item.getId());
                    couponActivityForMemberDTO.setEndTime(item.getEndTime());
                    couponActivityForMemberDTO.setActivityId(item.getCouponActivityId());
                    couponActivityForMemberDTO.setName(couponActivityDetailDTO.getName());
                    couponActivityForMemberDTO.setDiscountValue(couponActivityDetailDTO.getDiscountValue());
                    couponActivityForMemberDTO.setType(couponActivityDetailDTO.getType());
                    notAvailableCoupon.add(couponActivityForMemberDTO);
                }
            }
        });
        CouponActivityForMemberResultDTO memberCoupon = new CouponActivityForMemberResultDTO();
        memberCoupon.setValibaleMemberCoupon(availableCoupon);
        memberCoupon.setNotValiableMemberCoupon(notAvailableCoupon);
        return memberCoupon;
    }

    public void buildMyCoupon(CouponActivityMyCouponDTO dto, CouponBO couponBO, Map<Long, Set<String>> enterpriseNameMap) {
        Integer type = couponBO.getType();
        BigDecimal thresholdValue = couponBO.getThresholdValue();
        BigDecimal discountValue = couponBO.getDiscountValue();
        BigDecimal discountMax = couponBO.getDiscountMax();
        String enterpriseLimitNames = "";
        String goodsLimitDescribe = "";

        StringBuilder thresholdValueBuild = new StringBuilder();
        StringBuilder discountValueBuild = new StringBuilder();
        StringBuilder discountMaxBuild = new StringBuilder();

        thresholdValueBuild.append("满").append(thresholdValue).append("元可用");
        if (ObjectUtil.equal(CouponActivityTypeEnum.REDUCE.getCode(), type)) {
            // 满减
            discountValueBuild.append(discountValue).append("元");
        } else if (ObjectUtil.equal(CouponActivityTypeEnum.DISCOUNT.getCode(), type)) {
            // 满折
            discountValueBuild.append(discountValue).append("折");
            if (ObjectUtil.isNotNull(discountMax) && discountMax.compareTo(BigDecimal.ZERO) > 0) {
                discountMaxBuild.append("最多优惠").append(discountMax).append("元");
            }
        }

        Set<String> enterpriseNameSet = enterpriseNameMap.get(couponBO.getActivityId());
        if (CollUtil.isNotEmpty(enterpriseNameSet)) {
            enterpriseLimitNames = "仅在".concat(String.join(",", enterpriseNameSet)).concat("购买商品可用");
            goodsLimitDescribe = "部分商品可用";
        } else {
            if (ObjectUtil.isNotNull(couponBO.getActivityEid())) {
                if (couponBO.getActivityEid().intValue() == 0) {
                    goodsLimitDescribe = "平台所有商家可用";
                } else {
                    goodsLimitDescribe = "部分商品可用";
                    enterpriseLimitNames = "仅在".concat(couponBO.getActivityEname()).concat("购买商品可用");
                }
            }
        }
        // 有效期
        String begin = DateUtil.format(couponBO.getBeginTime(), "yyyy-MM-dd HH:mm:ss");
        String end = DateUtil.format(couponBO.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        String effectiveTime = begin.concat(" - ").concat(end);

        dto.setEffectiveTime(effectiveTime);
        dto.setThresholdValueRules(thresholdValueBuild.toString());
        dto.setDiscountValueRules(discountValueBuild.toString());
        dto.setDiscountMaxRules(discountMaxBuild.toString());
        dto.setEnterpriseLimitNames(enterpriseLimitNames);
        dto.setGoodsLimitDescribe(goodsLimitDescribe);
        dto.setPayMethodDescribe(buildPayMethodDescribe(couponBO));
    }

    /**
     * 构建支付方法描述
     *
     * @param couponBO
     * @return
     */
    private String buildPayMethodDescribe(CouponBO couponBO) {
        String payMethodDescribe = "支持";
        Integer payMethodLimit = couponBO.getPayMethodLimit();
        String payMethodSelected = couponBO.getPayMethodSelected();
        if (PayMethodLimitEnum.ALL.getCode().equals(payMethodLimit)) {
            payMethodDescribe += Arrays.stream(PayMethodTypeEnum.values()).map(PayMethodTypeEnum::getName).collect(Collectors.joining("、"));
        }
        if (PayMethodLimitEnum.PART.getCode().equals(payMethodLimit) && StringUtils.isNotBlank(payMethodSelected)) {
            payMethodDescribe += Arrays.stream(payMethodSelected.split("\\,")).map(item -> PayMethodTypeEnum.getByCode(Integer.valueOf(item))).collect(Collectors.joining("、"));
        }
        return payMethodDescribe;
    }

    private void getEnterpriseNameMap(Map<Long, Set<String>> enterpriseNameMap, Long eid, String ename) {
        Set<String> set = enterpriseNameMap.get(eid);
        if (CollUtil.isNotEmpty(set)) {
            set.add(ename);
        } else {
            set = new HashSet<>();
            set.add(ename);
        }
        enterpriseNameMap.put(eid, set);
    }

}
