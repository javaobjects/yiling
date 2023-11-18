package com.yiling.marketing.couponactivity.service.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dao.CouponActivityMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEnterpDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetChangeDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.MemberStageDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityCanUseShopDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponHasGetDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityIdAndEidDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivePageRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceivesRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponCanReceiveLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityMemberLimitDO;
import com.yiling.marketing.couponactivity.enums.PayMethodTypeEnum;
import com.yiling.marketing.couponactivity.service.CouponActivityForEnterpriseService;
import com.yiling.marketing.couponactivity.service.CouponActivityMemberLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.api.EnterprisePurchaseApplyApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCanBuyEidRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.shop.api.ShopApi;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Slf4j
@Service
public class CouponActivityForEnterpriseServiceImpl extends BaseServiceImpl<CouponActivityMapper, CouponActivityDO> implements CouponActivityForEnterpriseService {

    @Autowired
    private CouponActivityService couponActivityService;

    @DubboReference
    private ShopApi shopApi;

    @DubboReference
    private EnterprisePurchaseApplyApi enterprisePurchaseApplyApi;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private MemberApi memberApi;

    @DubboReference
    private MemberBuyStageApi memberBuyStageApi;

    @DubboReference
    private OrderFirstInfoApi firstInfoApi;

    @Autowired
    private CouponActivityMemberLimitService couponActivityMemberLimitService;

    @DubboReference
    private CustomerApi customerApi;


    @Autowired
    private CouponService couponService;

    @Override
    public List<CouponActivityHasGetDTO> getCouponActivityListByEid(QueryCouponActivityCanReceiveRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getEid())) {
            return ListUtil.empty();
        }
        int limit = request.getLimit();
        if (limit <= 0) {
            limit = 1;
        }
        if (limit > 100) {
            limit = 100;
        }
        request.setLimit(limit);
        request.setBusinessType(2);
        Long currentEid = request.getCurrentEid();
        // 查询企业类型，当作sql条件
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(currentEid);
        request.setType(enterpriseDTO.getType());
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(currentEid)).orElse(new CurrentMemberForMarketingDTO());
        List<Long> zhanwei = new ArrayList<>(1);
        zhanwei.add(-1L);
        if (CollectionUtils.isNotEmpty(member.getMemeberIds())) {
            request.setMemberIds(member.getMemeberIds());
        } else {
            request.setMemberIds(zhanwei);
        }
        if (CollectionUtils.isNotEmpty(member.getPromoterIds())) {
            request.setPromotionEids(member.getPromoterIds());
        } else {
            request.setPromotionEids(zhanwei);
        }
        log.info("getCouponActivityListByEid request{}"+ JSONUtil.toJsonStr(request));
        List<CouponActivityDTO> list = this.baseMapper.getCanReceiveListByEid(request);
        log.info("getCanReceiveListByEid result{}"+ JSONUtil.toJsonStr(list));
        boolean currentMember = member.getCurrentMember() == 1;
        Iterator<CouponActivityDTO> iterator = list.iterator();
        while (iterator.hasNext()) {
            CouponActivityDTO next = iterator.next();
            boolean flag = checkUserType(next.getUserType(), currentMember);
            if (!flag) {
                iterator.remove();
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            return ListUtil.empty();
        }
        // 过滤掉发放完毕的券。
        List<Long> activityIdLists = list.stream().map(CouponActivityDTO::getId).collect(Collectors.toList());
        //List<CouponHasGetDTO> giveCountList = this.baseMapper.getGiveCountByActivityId(activityIdLists);
        // 判断是否会员，是否新客
        List<CouponActivityDTO> resuclt = new ArrayList<>();
        list.forEach(item -> {
            if (ObjectUtil.isNotEmpty(item.getGiveCount()) && ObjectUtil.isNotEmpty(item.getGiveCount()) && (item.getGiveCount() < item.getTotalCount())) {
                resuclt.add(item);
            }
        });

        list=resuclt;
        if (CollectionUtils.isEmpty(list)) {
            return ListUtil.empty();
        }
        return buildHasGetFlag(request.getCurrentEid(), PojoUtils.map(list, CouponActivityDO.class));
    }

    @Override
    public List<CouponActivityHasGetDTO> getCouponActivityListPageByEid(QueryCouponActivityCanReceivePageRequest request) {
        log.info("getCouponActivityListPageByEid, request -> {}", JSONObject.toJSONString(request));
        List<CouponActivityHasGetDTO> page = new ArrayList<>();
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCurrentEid()) || ObjectUtil.isNull(request.getEid())) {
            return page;
        }

        try {
            request.setBusinessType(2);
            Long currentEid = request.getCurrentEid();
            // 查询企业类型，当作sql条件
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(currentEid);
            request.setType(enterpriseDTO.getType());
            CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(currentEid)).orElse(new CurrentMemberForMarketingDTO());
            List<Long> zhanwei = new ArrayList<>(1);
            zhanwei.add(-1L);
            if (CollectionUtils.isNotEmpty(member.getMemeberIds())) {
                request.setMemberIds(member.getMemeberIds());
            } else {
                request.setMemberIds(zhanwei);
            }
            if (CollectionUtils.isNotEmpty(member.getPromoterIds())) {
                request.setPromotionEids(member.getPromoterIds());
            } else {
                request.setPromotionEids(zhanwei);
            }
            request.setPromotionEid(member.getPromoterId());
            log.info("getCanReceiveListPageByEid, request -> {}", JSONUtil.toJsonStr(request));
            List<CouponActivityDTO> couponActivityPage = this.baseMapper.getCanReceiveListPageByEid(request);
            log.info("getCanReceiveListPageByEid, result -> {}", JSONUtil.toJsonStr(request));
            if (CollUtil.isEmpty(couponActivityPage)) {
                return page;
            }
            // 过滤掉发放完毕的券。
            // 判断是否会员，是否新客
            List<CouponActivityDTO> resuclt = new ArrayList<>();
            couponActivityPage.forEach(item->{
                if (ObjectUtil.isNotEmpty(item.getGiveCount()) && ObjectUtil.isNotEmpty(item.getGiveCount()) && (item.getGiveCount() < item.getTotalCount())) {
                        resuclt.add(item);
                    }
                });

            couponActivityPage=resuclt;
            boolean currentMember = member.getCurrentMember() == 1;
            Iterator<CouponActivityDTO> iterator = couponActivityPage.iterator();
            while (iterator.hasNext()) {
                CouponActivityDTO next = iterator.next();
                boolean flag = checkUserType(next.getUserType(), currentMember);
                if (!flag) {
                    iterator.remove();
                }
            }
            if (CollUtil.isEmpty(couponActivityPage)) {
                return page;
            }
            List<CouponActivityDO> records = PojoUtils.map(couponActivityPage, CouponActivityDO.class);
            // 领取达到上限数量的优惠券，视为已领取
            page = buildHasGetFlag(request.getCurrentEid(), records);
            //            if(CollUtil.isNotEmpty(resultList)){
            //                // 排序，不可领取的放在后面
            //                resultList = resultList.stream().sorted(Comparator.comparing(CouponActivityHasGetDTO::getGetFlag)).collect(Collectors.toList());
            //            }
        } catch (Exception e) {
            log.error("获取优惠券活动列表异常 getCouponActivityListByEid，usetId -> {}, eid -> {}, request -> {}, exception -> {}", request.getUserId(), request.getEid(), JSONObject.toJSONString(request), e);
            e.printStackTrace();
        }
        return page;
    }

    @Override
    public List<CouponActivityHasGetDTO> getCouponActivityListPageByEids(QueryCouponActivityCanReceivesRequest request, List<Long> activityIds) {
        log.info("getCouponActivityListPageByEid, request -> {}", JSONObject.toJSONString(request));
        List<CouponActivityHasGetDTO> page = new ArrayList<>();
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCurrentEid()) || CollectionUtils.isEmpty(request.getEids())) {
            return new ArrayList<>();
        }
        try {
            request.setBusinessType(2);
            request.setSize(100);
            List<CouponActivityHasGetChangeDTO> couponActivityPage = this.baseMapper.getCanReceiveListPageByEids(request);
            if (ObjectUtil.isNull(couponActivityPage) || CollUtil.isEmpty(couponActivityPage)) {
                return new ArrayList<>();
            }
            // 过滤掉不符合条件的优惠券
            if (CollectionUtils.isNotEmpty(activityIds)) {
                couponActivityPage = couponActivityPage.stream().filter(item -> !activityIds.contains(item.getId())).collect(Collectors.toList());
            }
            couponActivityPage.forEach(item -> {
                // 产品限制1 全部产品，2部分产品
                Integer goodsLimit = item.getCaGoodsLimit();
                // -1 商家后台 1运营后台
                if (item.getCaPlatform() == -1) {
                    item.setRealEid(item.getEid());
                } else if (item.getCaPlatform() == 1) {
                   /* if (goodsLimit == 1) {
                        item.setRealEid(item.getCaelEid());
                    } else if (goodsLimit == 2) {
                        item.setRealEid(item.getCaglEid());
                    }*/
                    item.setRealEid(item.getCaelEid());
                }
            });

            // 领取达到上限数量的优惠券，视为已领取
            List<CouponActivityHasGetDTO> resultList = buildHasGetFlagById(request.getCurrentEid(), couponActivityPage);
            page = resultList;
        } catch (Exception e) {
            log.error("获取优惠券活动列表异常 getCouponActivityListByEid，usetId -> {}, eid -> {}, request -> {}, exception -> {}", request.getUserId(), request.getEids(), JSONObject.toJSONString(request), e);
            e.printStackTrace();
        }
        return page;
    }

    @Override
    public List<CouponActivityHasGetDTO> getMemberCouponsCenter(QueryCouponActivityCanReceivePageRequest request) {
        Long currentEid = request.getCurrentEid();
        // 查询企业类型，当作sql条件
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(currentEid);
        List<CouponActivityEnterpDTO> result = new ArrayList<>();
        QueryCouponActivityCanReceivesRequest receivePageRequest = new QueryCouponActivityCanReceivesRequest();
        receivePageRequest.setCurrentEid(request.getCurrentEid());
        receivePageRequest.setType(enterpriseDTO.getType());
        // receivePageRequest.setEids(pageEids);
        receivePageRequest.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        log.info("getCouponActivityListPageByEid, request -> {}", JSONObject.toJSONString(request));
        List<CouponActivityHasGetDTO> page = new ArrayList<>();
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(currentEid)).orElse(new CurrentMemberForMarketingDTO());
        receivePageRequest.setBusinessType(2);
        List<Long> zhanwei = new ArrayList<>(1);
        zhanwei.add(-1L);
        if (CollectionUtils.isNotEmpty(member.getMemeberIds())) {
            receivePageRequest.setMemberIds(member.getMemeberIds());
        } else {
            receivePageRequest.setMemberIds(zhanwei);
        }
        if (CollectionUtils.isNotEmpty(member.getPromoterIds())) {
            receivePageRequest.setPromotionEids(member.getPromoterIds());
        } else {
            receivePageRequest.setPromotionEids(zhanwei);
        }
        List<CouponActivityHasGetChangeDTO> couponActivityPage = this.baseMapper.getCanReceiveMemberCouponList(receivePageRequest);
        if (ObjectUtil.isNull(couponActivityPage) || CollUtil.isEmpty(couponActivityPage)) {
            return new ArrayList<>();
        }
        boolean currentMember = member.getCurrentMember() == 1;

        Iterator<CouponActivityHasGetChangeDTO> iterator = couponActivityPage.iterator();
        // 记录下过滤掉的优惠券id，给前端中过滤掉显示
        List<Long> activityIds = new ArrayList<>();
        while (iterator.hasNext()) {
            CouponActivityHasGetChangeDTO next = iterator.next();
            boolean flag = checkUserTypeFroMember(next.getUserType(),currentMember);
            if (!flag) {
                activityIds.add(next.getId());
                iterator.remove();
            }
        }
        if (CollectionUtils.isEmpty(couponActivityPage)) {
            return new ArrayList<>();
        }
        couponActivityPage.forEach(item -> {
            item.setRealEid(item.getCaelEid());
        });
        // 领取达到上限数量的优惠券，视为已领取
        List<CouponActivityHasGetDTO> resultList = buildHasGetFlagById(request.getCurrentEid(), couponActivityPage);
        log.info("resultList, resultList -> {}", JSONObject.toJSONString(resultList));
        // 会员优惠券要查到关联的会员购买规格id
        if(CollectionUtils.isNotEmpty(resultList)){
            List<Long> couponIds = resultList.stream().map(CouponActivityHasGetDTO::getId).collect(Collectors.toList());
            List<CouponActivityMemberLimitDO>couponActivityMemberLimitDOS=getMemberIdByCouponsId(couponIds);
            if(CollectionUtils.isNotEmpty(couponActivityMemberLimitDOS)){
                Map<Long, List<CouponActivityMemberLimitDO>> couponLimits = couponActivityMemberLimitDOS.stream().collect(Collectors.groupingBy(CouponActivityMemberLimitDO::getCouponActivityId));
                resultList.forEach(item->{
                    List<CouponActivityMemberLimitDO> couponActivityMemberLimitDOS1 = couponLimits.get(item.getId());
                    if(CollectionUtils.isNotEmpty(couponActivityMemberLimitDOS)&&CollectionUtils.isNotEmpty(couponActivityMemberLimitDOS1)){
                        List<Long> memberIds = couponActivityMemberLimitDOS1.stream().map(CouponActivityMemberLimitDO::getMemberId).collect(Collectors.toList());
                        item.setMemberIds(memberIds);
                    }
                });
            }
            List<Long> memberIds = new ArrayList<>();
            resultList.forEach(item -> {
                if (CollectionUtils.isNotEmpty(item.getMemberIds())) {
                    memberIds.addAll(item.getMemberIds());
                }
            });
            List<MemberBuyStageDTO> memberBuyStageDTOS = memberBuyStageApi.listByIds(memberIds);
            Map<Long, Long> stageIdAndMemberIdMap = memberBuyStageDTOS.stream().collect(Collectors.toMap(MemberBuyStageDTO::getId, MemberBuyStageDTO::getMemberId));
            resultList.forEach(item -> {
                List<Long> memberIds1 = item.getMemberIds();
                Map<Long, List<Long>> memberStageList = new HashMap<>();
                if(CollectionUtils.isNotEmpty(memberIds1)){
                    memberIds1.forEach(item2 -> {
                        Long memberID = stageIdAndMemberIdMap.get(item2);
                        if(ObjectUtil.isNotNull(memberID)){
                            List<Long> stageIds = memberStageList.get(memberID);
                            if (CollectionUtils.isEmpty(stageIds)) {
                                List<Long> objects = new ArrayList<>();
                                objects.add(item2);
                                memberStageList.put(memberID, objects);
                            } else {
                                stageIds.add(item2);
                            }
                        }

                    });
                    if(ObjectUtil.isNotNull(memberStageList)){
                        Iterator<Map.Entry<Long, List<Long>>> iterator1 = memberStageList.entrySet().iterator();
                        List<MemberStageDTO> objects = new ArrayList<>();
                        while (iterator1.hasNext()) {
                            Map.Entry<Long, List<Long>> next = iterator1.next();
                            MemberStageDTO memberStageDTO = new MemberStageDTO();
                            memberStageDTO.setMemberId(next.getKey());
                            memberStageDTO.setStageIds(next.getValue());
                            objects.add(memberStageDTO);
                        }
                        item.setMemberStageList(objects);
                    }
                }
            });
        }
        return resultList;
    }

    private List<CouponActivityMemberLimitDO> getMemberIdByCouponsId(List<Long> couponIds) {
        QueryWrapper<CouponActivityMemberLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getCouponActivityId,couponIds);
        queryWrapper.lambda().in(CouponActivityMemberLimitDO::getDelFlag,0);
        return couponActivityMemberLimitService.list(queryWrapper);
    }

    @Override
    public Page<CouponActivityEnterpDTO> getCouponsCenter(QueryCouponActivityCanReceivePageRequest request) {
        log.info("getCouponsCenter, param -> {}", JSONObject.toJSONString(request));
        // 1首先获取所有的当前商家建采的商家（如果没有页面不显示优惠券，直接返回）
        // 2 当作参数去sql查询。1先查商家办理的活动（商家后台） 2在查询运营后台办的活动。
        // 3 过滤掉发放完毕的券。标记上到达领券上限的券。
        // 4过滤掉不符合领券条件的券。包含推广人，会员方案，用户类型等等。
        // 调用石晨接口返回建材的商家，最多五十个
        QueryCanBuyEidRequest queryCanBuyEidRequest = new QueryCanBuyEidRequest();
        queryCanBuyEidRequest.setCustomerEid(request.getCurrentEid());
        queryCanBuyEidRequest.setLine(2);
        queryCanBuyEidRequest.setLimit(50);
        List<Long> purchasedEids = customerApi.getEidListByCustomerEid(queryCanBuyEidRequest);
        log.info("getEidListByCustomerEid, purchasedEids -> {}", JSONObject.toJSONString(purchasedEids));
        if (CollectionUtils.isEmpty(purchasedEids)) {
            return new Page<>();
        }
        // 过滤掉销售区域
        Map<Long, Boolean> longBooleanMap = shopApi.checkSaleAreaByCustomerEid(request.getCurrentEid(), purchasedEids);
        purchasedEids = purchasedEids.stream().filter(item -> longBooleanMap.get(item)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(purchasedEids)) {
            return new Page<>();
        }

        if (StringUtils.isNotEmpty(request.getEname())) {
            QueryEnterprisePageListRequest enterprisePageListRequest = new QueryEnterprisePageListRequest();
            enterprisePageListRequest.setName(request.getEname());
            enterprisePageListRequest.setCurrent(1);
            enterprisePageListRequest.setSize(100);
            enterprisePageListRequest.setIds(purchasedEids);
            Page<EnterpriseDTO> page = enterpriseApi.pageList(enterprisePageListRequest);
            List<EnterpriseDTO> enterpriseDTOList = page.getRecords();
            if (CollectionUtils.isEmpty(enterpriseDTOList)) {
                return new Page<>();
            }
            List<Long> searchEid = enterpriseDTOList.stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(searchEid)){
                return new Page<>();
            }
            purchasedEids = purchasedEids.stream().filter(item -> searchEid.contains(item)).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(purchasedEids)) {
            return new Page<>();
        }
        QueryCouponCanReceiveLimitRequest receiveLimitRequest = new QueryCouponCanReceiveLimitRequest();
        receiveLimitRequest.setEid(request.getEid());
        receiveLimitRequest.setPlatformType(CouponPlatformTypeEnum.B2B.getCode());
        Long currentEid = request.getCurrentEid();
        receiveLimitRequest.setEids(purchasedEids);
        // 查询企业类型，当作sql条件
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(currentEid);
        receiveLimitRequest.setType(enterpriseDTO.getType());
        CurrentMemberForMarketingDTO member = Optional.ofNullable(memberApi.getCurrentMemberForMarketing(currentEid)).orElse(new CurrentMemberForMarketingDTO());
        // 返回的表示优惠券可以领取（下面过滤后可以领取的），但是不一定可以使用的券。
        // 获取推广方会员id和会员方案id
        List<Long> zhanwei = new ArrayList<>(1);
        zhanwei.add(-1L);
        if (CollectionUtils.isNotEmpty(member.getMemeberIds())) {
            receiveLimitRequest.setMemberIds(member.getMemeberIds());
        } else {
            receiveLimitRequest.setMemberIds(zhanwei);
        }
        if (CollectionUtils.isNotEmpty(member.getPromoterIds())) {
            receiveLimitRequest.setPromotionEids(member.getPromoterIds());
        } else {
            receiveLimitRequest.setPromotionEids(zhanwei);
        }
        log.info("getShopIdCanGiveCoupon, param -> {}", JSONObject.toJSONString(receiveLimitRequest));
        //List<CouponActivityCanUseShopDTO> shopIdCanGiveCoupon = this.baseMapper.getShopIdCanGiveCoupon(receiveLimitRequest);
        // 获取商家后台创建的券信息，基本没有
        List<CouponActivityCanUseShopDTO> businessActivity = this.baseMapper.getBusinessPlatFormEffecvtiveActivity(receiveLimitRequest);
        log.info("查询商家后台优惠券, result -> {}", JSONObject.toJSONString(businessActivity));
        // 获取运营后台创建的券信息
        List<CouponActivityCanUseShopDTO> marketingActivity = this.baseMapper.getMarketingPlatFormEffecvtiveActivity(receiveLimitRequest);
        log.info("查询运营后台优惠券, result -> {}", JSONObject.toJSONString(marketingActivity));
        if (CollectionUtils.isEmpty(marketingActivity)&&CollectionUtils.isEmpty(businessActivity)) {
            return new Page<>();
        }
        //过滤掉发放完毕的优惠券
        List<CouponActivityCanUseShopDTO> union = CollectionUtil.unionAll(businessActivity, marketingActivity);
        List<Long> activityIDS = union.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
        List<CouponDTO> hasGiveCountByCouponActivityList = couponService.getHasGiveCountByCouponActivityList(activityIDS);
        List<CouponHasGetDTO> giveCountList=PojoUtils.map(hasGiveCountByCouponActivityList,CouponHasGetDTO.class);
        if (giveCountList != null) {
            Map<Long, Integer> giveCountMap = new HashMap<>();
            giveCountList.forEach(item->{
                giveCountMap.put(item.getId(),item.getNum());
            });
            if (CollectionUtils.isNotEmpty(businessActivity)) {
                businessActivity=businessActivity.stream().filter(item -> {
                    Integer hasGetNum = giveCountMap.get(item.getId());
                    if (ObjectUtil.isNull(hasGetNum) || hasGetNum < item.getTotalCount()) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(marketingActivity)){
                marketingActivity=marketingActivity.stream().filter(item -> {
                    Integer hasGetNum = giveCountMap.get(item.getId());
                    if (ObjectUtil.isNull(hasGetNum) || hasGetNum < item.getTotalCount()) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
            }
        }
        //过滤掉发放完毕的优惠券结束

        if(CollectionUtils.isNotEmpty(businessActivity)){
            // 商家后台过滤掉当前用户不能领取的
            List<CouponActivityCanUseShopDTO> partCanUse = businessActivity.stream().filter(item -> item.getUserType() == 5).collect(Collectors.toList());
            List<CouponActivityCanUseShopDTO> allCanUse = businessActivity.stream().filter(item -> item.getUserType() == 1).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(partCanUse)){
                List<Long> activityIds = partCanUse.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
                List<Long> idsByUserType = this.baseMapper.getActivityIdsByUserType(activityIds,request.getCurrentEid());
                if(CollectionUtils.isEmpty(idsByUserType)){
                    partCanUse=null;
                }else {
                    partCanUse=partCanUse.stream().filter(item->idsByUserType.contains(item.getId())).collect(Collectors.toList());
                }
            }
            businessActivity=CollectionUtil.unionAll(allCanUse, partCanUse);
        }
        List<CouponActivityIdAndEidDTO> idsByUserType=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(marketingActivity)){
            // 第一步过滤用户类型限制 全部用户，普通用户 全部会员，（指定会员方案，指定推广方会员）这两个第二部过滤
            Iterator<CouponActivityCanUseShopDTO> iterator1 = marketingActivity.iterator();
            while (iterator1.hasNext()) {
                CouponActivityCanUseShopDTO next = iterator1.next();
                boolean flag = checkUserType(next.getUserType(), member.getCurrentMember()==1);
                if (!flag) {
                    iterator1.remove();
                }
            }
            // 先过滤优惠券活动的商家设置 ,过滤领券活动推广人，过滤会员方案，过滤指定用户。 建采商家是否有有会员活动
            List<Long> activityIds = marketingActivity.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
            idsByUserType = this.baseMapper.getActivityIdsByEids(activityIds,purchasedEids);
            if(CollectionUtils.isEmpty(idsByUserType)){
                marketingActivity=null;
            }else {
                List<Long> collects = idsByUserType.stream().map(CouponActivityIdAndEidDTO::getId).collect(Collectors.toList());
                marketingActivity=marketingActivity.stream().filter(item->collects.contains(item.getId())).collect(Collectors.toList());
            }
            // 建材的商家下面有活动，现在看能不能领取
            if(CollectionUtils.isNotEmpty(marketingActivity)){
                // 1全部用户可用
                List<CouponActivityCanUseShopDTO> collect1 = marketingActivity.stream().filter(item -> item.getEnterpriseRang() == 1).collect(Collectors.toList());
                // 2指定用户可用
                List<CouponActivityCanUseShopDTO> collect2 = marketingActivity.stream().filter(item -> item.getEnterpriseRang() == 2).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect2)){
                    List<Long> activityIdList = collect2.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
                    List<Long> activityIdsByUserType = this.baseMapper.getAutogetActivityIdsByUserType(activityIdList,request.getCurrentEid());
                    if(CollectionUtils.isEmpty(activityIdsByUserType)){
                        collect2=null;
                    }else {
                        collect2=collect2.stream().filter(item->activityIdsByUserType.contains(item.getId())).collect(Collectors.toList());
                    }
                }
                // 3 范围用户可用 指定范围需要过滤的数据
                List<CouponActivityCanUseShopDTO> collect3 = marketingActivity.stream().filter(item -> item.getEnterpriseRang() == 3).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect3)){
                    List<Long> notInclude = new ArrayList<>();
                    // 会员方案限制，可能很多时候为空
                    List<CouponActivityCanUseShopDTO> collect4 = marketingActivity.stream().filter(item -> item.getUserType() == 7).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(collect4)){
                        List<Long> activityIdList = collect4.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
                        List<Long> byMemberLimit = this.baseMapper.getActivityIdsByMemberLimit(activityIdList,receiveLimitRequest.getMemberIds());
                        if(CollectionUtils.isEmpty(byMemberLimit)){
                            notInclude=activityIdList;
                        }else {
                            Collection<Long> subtract = CollectionUtil.subtract(activityIdList, byMemberLimit);
                            notInclude=CollectionUtil.newArrayList(subtract);
                        }
                    }
                    // 推广人限制，可能很多时候为空
                    List<CouponActivityCanUseShopDTO> collect5 = marketingActivity.stream().filter(item -> item.getUserType() == 8).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(collect5)){
                        List<Long> activityIdList = collect5.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
                        List<Long> activityIdsByUserType = this.baseMapper.getActivityIdsByPromoterLimit(activityIdList,receiveLimitRequest.getPromotionEids());
                        if(CollectionUtils.isEmpty(activityIdsByUserType)){
                            notInclude=CollectionUtil.unionAll(notInclude,activityIdList);
                        }else {
                            Collection<Long> subtract = CollectionUtil.subtract(activityIdList, activityIdsByUserType);
                            notInclude=CollectionUtil.unionAll(notInclude,CollectionUtil.newArrayList(subtract));
                        }
                    }
                    List<Long> finalNotInclude = notInclude;
                    collect3=collect3.stream().filter(item->!finalNotInclude.contains(item.getId())).collect(Collectors.toList());
                    // 过滤掉其他用户类型限制    1 - 全部用户；2 - 仅普通用户；3 - 全部会员用户；4 - 部分指定会员
                    if(CollectionUtil.isNotEmpty(collect3)){
                        boolean currentMember = member.getCurrentMember() == 1;
                        Iterator<CouponActivityCanUseShopDTO> iterator = collect3.iterator();
                        while (iterator.hasNext()) {
                            CouponActivityCanUseShopDTO next = iterator.next();
                            boolean flag = checkUserType(next.getUserType(), currentMember);
                            if (!flag) {
                                iterator.remove();
                            }
                        }
                    }
                }
                marketingActivity=CollectionUtil.unionAll(collect1,collect2,collect3);
            }
        }
        // 可以使用的优惠券，包含店铺id和优惠券id集合
        List<CouponActivityCanUseShopDTO> finalActivitys = CollectionUtil.unionAll(marketingActivity, businessActivity);
        if (CollectionUtils.isEmpty(finalActivitys)) {
            return new Page<>();
        }
        if (CollectionUtils.isEmpty(idsByUserType)) {
            return new Page<>();
        }

        // 把商家后台的eid和优惠券id也关联起来
        List<CouponActivityIdAndEidDTO> finalIdsByUserType=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(businessActivity)){
            businessActivity.forEach(item->{
                CouponActivityIdAndEidDTO couponActivityIdAndEidDTO = new CouponActivityIdAndEidDTO();
                couponActivityIdAndEidDTO.setEid(item.getEid());
                couponActivityIdAndEidDTO.setId(item.getId());
                finalIdsByUserType.add(couponActivityIdAndEidDTO);
            });
        }
        idsByUserType = CollectionUtil.unionAll(idsByUserType, finalIdsByUserType);
        List<Long> collect2 = finalActivitys.stream().map(CouponActivityCanUseShopDTO::getId).collect(Collectors.toList());
        idsByUserType = idsByUserType.stream().filter(item -> collect2.contains(item.getId())).collect(Collectors.toList());
        List<Long> availableEid = idsByUserType.stream().map(CouponActivityIdAndEidDTO::getEid).collect(Collectors.toList());
        List<Long> eids = new ArrayList<>();
        for(int i=0;i<purchasedEids.size();i++){
            if(availableEid.contains(purchasedEids.get(i))){
                eids.add(purchasedEids.get(i));
            }
        }
        Integer current = request.getCurrent();
        Integer pageSize = request.getSize();
        int begin = pageSize * (current - 1);
        int end = pageSize * (current - 1) + pageSize;
        if (end > eids.size()) {
            end = eids.size();
        }
        if (begin > eids.size()) {
            return new Page<>();
        }
        List<Long> pageEids = eids.subList(begin, end);
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(pageEids);
        Map<Long, String> idNameMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));

        // 获取企业对应的优惠券信息
       // finalActivityIds
        List<CouponActivityIdAndEidDTO> availableActivity = idsByUserType.stream().filter(item -> pageEids.contains(item.getEid())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(availableActivity)) {
            return new Page<>();
        }
        // 构建返回给前端的信息。是否到达领取限制，优惠券名称，商家名称，是否还能领取
        List<Long> idAllList = availableActivity.stream().map(CouponActivityIdAndEidDTO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(CouponActivityDO::getId, idAllList);
        // 展示给前端的优惠券活动信息
        List<CouponActivityDO> list = couponActivityService.list(queryWrapper);
        list.forEach(item->{
            if (StringUtils.isEmpty(item.getPayMethodSelected())) {
                String payMethodDescribe = Arrays.stream(PayMethodTypeEnum.values()).map(PayMethodTypeEnum::getName).collect(Collectors.joining("、"));
                item.setPayMethodSelected(payMethodDescribe);
            }else {
                String payMethodDescribe2 = Arrays.stream(item.getPayMethodSelected().split("\\,")).map(item2 -> PayMethodTypeEnum.getByCode(Integer.valueOf(item2))).collect(Collectors.joining("、"));
                item.setPayMethodSelected(payMethodDescribe2);
            }
        });
        // 优惠券领券活动可以领取的数量
        Map<Long, Integer> canGetNumMap = couponActivityService.getCanGetNumMap(list);
        // 用户已经领取优惠券的数量
        Map<Long, Integer> couponHasGetCountMap = couponActivityService.getCouponHasGetCountMap(currentEid, idAllList);
        List<CouponActivityHasGetDTO> resultList = new ArrayList<>();
        List<CouponActivityDetailDTO> couponActivityDetailList = PojoUtils.map(list, CouponActivityDetailDTO.class);
        buildCouponActivityHasGetDTO(couponActivityDetailList, couponHasGetCountMap, canGetNumMap, resultList);
        log.info("couponActivityListPageByEids, resultList -> {}", JSONObject.toJSONString(resultList));
        if (CollectionUtils.isEmpty(resultList)) {
            return new Page<>();
        }
        Map<Long, List<CouponActivityIdAndEidDTO>> couponActivity1 = idsByUserType.stream().collect(Collectors.groupingBy(CouponActivityIdAndEidDTO::getEid));
        // 查询当前用户达到领取上限的优惠券。并标记
        List<CouponActivityEnterpDTO> result = new ArrayList<>();
        for (Long eid : pageEids) {
            CouponActivityEnterpDTO couponActivityEnterpDTO = new CouponActivityEnterpDTO();
            List<CouponActivityIdAndEidDTO> couponActivityIdAndEidDTOS = couponActivity1.get(eid);
            if(CollectionUtils.isNotEmpty(couponActivityIdAndEidDTOS)){
                List<Long> collect = couponActivityIdAndEidDTOS.stream().map(CouponActivityIdAndEidDTO::getId).collect(Collectors.toList());
                List<CouponActivityHasGetDTO> collect1 = resultList.stream().filter(item -> collect.contains(item.getId())).collect(Collectors.toList());
                List<CouponActivityHasGetDTO> couponActivityHasGetDTOList = collect1.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CouponActivityHasGetDTO::getId))), ArrayList::new));
                couponActivityEnterpDTO.setCouponActivityHasGet(new ArrayList<>(couponActivityHasGetDTOList));
                couponActivityEnterpDTO.setEid(eid);
                couponActivityEnterpDTO.setEname(idNameMap.get(eid));
                result.add(couponActivityEnterpDTO);
            }
        }

        Page<CouponActivityEnterpDTO> couponActivityHasGetDTOPage = new Page<>();
        couponActivityHasGetDTOPage.setTotal(eids.size());
        couponActivityHasGetDTOPage.setCurrent(request.getCurrent());
        couponActivityHasGetDTOPage.setSize(request.getSize());
        couponActivityHasGetDTOPage.setRecords(result);
        return couponActivityHasGetDTOPage;
    }

    private boolean checkUserType(Integer userType, Boolean currentMember) {
        Boolean flag = true;
        //1 - 全部用户；2 - 仅普通用户；3 - 全部会员用户；4 - 部分指定会员;5 - 部分用户，6新客
        if (userType == 2 && (currentMember)) {
            flag = false;
        }
        if (userType == 3 && !currentMember) {
            flag = false;
        }
        if (userType == 4 && !currentMember) {
            flag = false;
        }
        return flag;
    }

    private boolean checkUserTypeFroMember(Integer userType, Boolean currentMember) {
        Boolean flag = true;
        //1 - 全部用户；2 - 仅普通用户；3 - 全部会员用户；4 - 部分指定会员;5 - 部分用户，6新客
        if (userType == 2 && (currentMember)) {
            flag = false;
        }
        if (userType == 3 && !currentMember) {
            flag = false;
        }
        if (userType == 4 && !currentMember) {
            flag = false;
        }
        return flag;
    }

    private List<CouponActivityHasGetDTO> buildHasGetFlagById(Long currentEid, List<CouponActivityHasGetChangeDTO> records) {
        List<Long> idAllList = records.stream().map(CouponActivityHasGetChangeDTO::getId).collect(Collectors.toList());
        // 查询平台、商家优惠券规则设置
        List<CouponActivityDO> couponActivityDOS = PojoUtils.map(records, CouponActivityDO.class);
        Map<Long, Integer> canGetNumMap = couponActivityService.getCanGetNumMap(couponActivityDOS);
        // 这些代码只展示了领券活动有效期内的优惠券，不在优惠券领取活动内的，在我的优惠券里面展示
        /* 已领取的优惠券数量统计 */
        Map<Long, Integer> couponHasGetCountMap = couponActivityService.getCouponHasGetCountMap(currentEid, idAllList);
        /* 领取达到上限数量的优惠券，视为已领取 */
        List<CouponActivityHasGetDTO> resultList = new ArrayList<>();
        List<CouponActivityDetailDTO> couponActivityDetailList = PojoUtils.map(records, CouponActivityDetailDTO.class);
        buildCouponActivityHasGetDTO(couponActivityDetailList, couponHasGetCountMap, canGetNumMap, resultList);
        return resultList;
    }

    private List<CouponActivityHasGetDTO> buildHasGetFlag(Long currentEid, List<CouponActivityDO> records) {
        List<Long> idAllList = records.stream().map(CouponActivityDO::getId).collect(Collectors.toList());
        // 查询平台、商家优惠券规则设置
        Map<Long, Integer> canGetNumMap = couponActivityService.getCanGetNumMap(records);
        /* 已领取的优惠券数量统计 */
        Map<Long, Integer> couponHasGetCountMap = couponActivityService.getCouponHasGetCountMap(currentEid, idAllList);
        /* 领取达到上限数量的优惠券，视为已领取 */
        List<CouponActivityHasGetDTO> resultList = new ArrayList<>();
        List<CouponActivityDetailDTO> couponActivityDetailList = PojoUtils.map(records, CouponActivityDetailDTO.class);
        buildCouponActivityHasGetDTO(couponActivityDetailList, couponHasGetCountMap, canGetNumMap, resultList);
        return resultList;
    }

    private void buildCouponActivityHasGetDTO(List<CouponActivityDetailDTO> couponActivityDetailList, Map<Long, Integer> couponHasGetCountMap, Map<Long, Integer> canGetNumMap, List<CouponActivityHasGetDTO> resultList) {
        if (CollUtil.isEmpty(couponActivityDetailList)) {
            return;
        }

        CouponActivityHasGetDTO couponActivityHasGetDto;
        for (CouponActivityDetailDTO couponActivityDetail : couponActivityDetailList) {
            couponActivityHasGetDto = PojoUtils.map(couponActivityDetail, CouponActivityHasGetDTO.class);
            CouponActivityDTO dto = couponActivityService.buildCouponActivityDtoForCouponRules(couponActivityDetail);
            // 组装规则
            Map<String, String> rulesMap = couponActivityService.buildCouponRulesMobile(dto);
            couponActivityHasGetDto.setThresholdValueRules(rulesMap.get("thresholdValue"));
            couponActivityHasGetDto.setDiscountValueRules(rulesMap.get("discountValue"));
            couponActivityHasGetDto.setDiscountMaxRules(rulesMap.get("discountMax"));
            // 有效期
            String effectiveTime = CouponUtil.buildEffectiveTime(couponActivityDetail, true);
            couponActivityHasGetDto.setGiveOutEffectiveRules(effectiveTime);
            Integer hasGetCount = couponHasGetCountMap.get(couponActivityDetail.getId());
            if (ObjectUtil.isNull(hasGetCount)) {
                hasGetCount = 0;
            }
            // 是否可领取
            Integer canGetNum = canGetNumMap.get(couponActivityDetail.getId());
            if (ObjectUtil.isNotNull(canGetNum) && canGetNum > 0 && hasGetCount < canGetNum) {
                couponActivityHasGetDto.setGetFlag(false);
            } else {
                couponActivityHasGetDto.setGetFlag(true);
            }
            couponActivityHasGetDto.setConditionGoods(couponActivityDetail.getGoodsLimit() == 1 && couponActivityDetail.getEnterpriseLimit() == 1 ? 1 : 2);
            couponActivityHasGetDto.setGoodsLimit(couponActivityDetail.getGoodsLimit() == 1 && couponActivityDetail.getEnterpriseLimit() == 1 ? 1 : 2);
            resultList.add(couponActivityHasGetDto);
        }
    }

}
