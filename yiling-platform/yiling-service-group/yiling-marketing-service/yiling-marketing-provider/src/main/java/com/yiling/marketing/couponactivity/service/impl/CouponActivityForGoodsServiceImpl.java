package com.yiling.marketing.couponactivity.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponActivityResultTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponPlatformTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.dto.CouponDTO;
import com.yiling.marketing.coupon.dto.request.CouponAutoGiveRequest;
import com.yiling.marketing.coupon.entity.CouponDO;
import com.yiling.marketing.coupon.service.CouponService;
import com.yiling.marketing.couponactivity.dao.CouponActivityMapper;
import com.yiling.marketing.couponactivity.dto.CouponActivityCanAndOwnDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityEidOrGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityHasGetDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityListFiveByGoodsIdDTO;
import com.yiling.marketing.couponactivity.dto.GetCouponActivityResultDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityCanUseShopDTO;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityExistFlagRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponActivityReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.CouponHasGetDTO;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityCanReceiveRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponActivityDetailRequest;
import com.yiling.marketing.couponactivity.dto.request.QueryCouponCanReceiveLimitRequest;
import com.yiling.marketing.couponactivity.entity.CouponActivityDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseLimitDO;
import com.yiling.marketing.couponactivity.entity.CouponActivityGoodsLimitDO;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseGetRulesService;
import com.yiling.marketing.couponactivity.service.CouponActivityEnterpriseLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityForGoodsService;
import com.yiling.marketing.couponactivity.service.CouponActivityGoodsLimitService;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.couponactivityautoget.dao.CouponActivityAutoGetMapper;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetCouponDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetDO;
import com.yiling.marketing.couponactivityautoget.entity.CouponActivityAutoGetRecordDO;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetCouponService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetRecordService;
import com.yiling.marketing.couponactivityautoget.service.CouponActivityAutoGetService;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGetCouponDTO;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.dto.request.CreateOrderPromotionActivityRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * @author: houjie.sun
 * @date: 2021/11/8
 */
@Slf4j
@Service
public class CouponActivityForGoodsServiceImpl extends BaseServiceImpl<CouponActivityMapper, CouponActivityDO> implements CouponActivityForGoodsService {

    @Autowired
    private CouponActivityService couponActivityService;
    @Autowired
    private CouponActivityGoodsLimitService couponActivityGoodsLimitService;
    @Autowired
    private CouponActivityEnterpriseLimitService couponActivityEnterpriseLimitService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponActivityAutoGetRecordService autoGetRecordService;
    @Autowired
    private CouponActivityAutoGetService autoGetService;
    @Autowired
    private CouponActivityAutoGetCouponService autoGetCouponService;
    @Autowired
    private CouponActivityEnterpriseGetRulesService enterpriseGetRulesService;
    @DubboReference
    OrderFirstInfoApi orderFirstInfoApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    MemberApi memberApi;
    @Autowired
    private CouponActivityAutoGetMapper activityAutoGetMapper;


    @Override
    public List<CouponActivityListFiveByGoodsIdDTO> getListFiveByGoodsIdAndEid(Long goodsId, Long eid, Integer limit, Integer platformType) {
        if (ObjectUtil.isNull(goodsId)) {
            return ListUtil.empty();
        }
        if (limit <= 0) {
            limit = 1;
        }
        if (limit > 100) {
            limit = 100;
        }

        List<CouponActivityListFiveByGoodsIdDTO> list = new ArrayList<>();
        try {
            QueryCouponCanReceiveLimitRequest request = new QueryCouponCanReceiveLimitRequest();
            request.setEid(eid);
            request.setGoodsId(goodsId);
            request.setPlatformType(platformType);
            request.setLimit(limit);
            List<CouponActivityDO> couponActivityTempList = this.baseMapper.getCanReceiveListByEidAndGoodsId(request);
            if (CollUtil.isEmpty(couponActivityTempList)) {
                return ListUtil.empty();
            }
            List<CouponActivityDO> couponActivityList = getCouponActivityUnique(couponActivityTempList);
            list = PojoUtils.map(couponActivityList, CouponActivityListFiveByGoodsIdDTO.class);

            //        try {
            //            CouponActivityListFiveByGoodsIdDTO couponActivityListFiveDto;
            //            List<Long> couponActivityIdList;
            //            List<CouponActivityDetailDTO> couponActivityDetailList;
            ////            TreeSet<CouponActivityDetailDTO> couponActivityDetailSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
            //            Set<Long> set = new HashSet<>();
            //
            //            // 根据goodsId查询优惠券活动限制商品表
            //            List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByGoodsIdAndEid(goodsId, eid);
            //            if (CollUtil.isNotEmpty(goodsLimitList)) {
            //                couponActivityIdList = goodsLimitList.stream().map(CouponActivityGoodsLimitDO::getCouponActivityId).collect(Collectors.toList());
            //                List<CouponActivityDO> effectiveCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIdList);
            ////                List<CouponActivityDetailDTO> detailList =
            ////                couponActivityDetailSet.addAll(detailList);
            //                couponActivityDetailList = PojoUtils.map(effectiveCouponActivityList, CouponActivityDetailDTO.class);
            //                buildCouponActivityListFiveByGoodsIdDTO(limit, set, list, couponActivityDetailList);
            //            }
            //            if (CollUtil.isNotEmpty(set) && set.size() == limit) {
            //                return list;
            //            }
            //
            //            // 根据eid查询优惠券活动限制企业表
            //            List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitService.getByEid(eid);
            //            if (CollUtil.isNotEmpty(enterpriseLimitList)) {
            //                couponActivityIdList = enterpriseLimitList.stream().map(CouponActivityEnterpriseLimitDO::getCouponActivityId)
            //                    .collect(Collectors.toList());
            //
            //                List<CouponActivityDO> effectiveCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIdList);
            ////                List<CouponActivityDetailDTO> detailList =
            ////                couponActivityDetailSet.addAll(detailList);
            //                couponActivityDetailList = PojoUtils.map(effectiveCouponActivityList, CouponActivityDetailDTO.class);
            //                buildCouponActivityListFiveByGoodsIdDTO(limit, set, list, couponActivityDetailList);
            //            }
            //            if (CollUtil.isNotEmpty(set) && set.size() == limit) {
            //                return list;
            //            }
            //
            //            // 查询未限制企业的优惠券活动
            //            List<CouponActivityDO> effectiveWithoutEnterpriseList = couponActivityService.getEffectiveListWithoutEnterpriseLimit();
            //            if (CollUtil.isNotEmpty(effectiveWithoutEnterpriseList)) {
            ////                List<CouponActivityDetailDTO> detailList =
            ////                couponActivityDetailSet.addAll(detailList);
            //                couponActivityDetailList = PojoUtils.map(effectiveWithoutEnterpriseList, CouponActivityDetailDTO.class);
            //                buildCouponActivityListFiveByGoodsIdDTO(limit, set, list, couponActivityDetailList);
            //            }
            //
            //            // 平台创建的，必须关联自主领取活动
            //            if(CollUtil.isNotEmpty(list)){
            //                // 查询自主领取活动
            //                List<Long> idList = list.stream().filter(c -> ObjectUtil.equal(c.getCreateUser(), 0))
            //                        .map(CouponActivityListFiveByGoodsIdDTO::getId).collect(Collectors.toList());
            //                if(CollUtil.isNotEmpty(idList)){
            //                    // 自主领取活动关联关系
            //                    List<CouponActivityAutoGetCouponDTO> autoGetCouponList = autoGetCouponService.getByCouponActivityIdList(idList);
            //                    if(CollUtil.isEmpty(autoGetCouponList)){
            //                        // 去除平台创建的优惠券活动
            //                        removeWithoutAutoGet(list);
            //                    } else {
            //                        // 自主领取活动
            //                        List<Long> autoGetIdList = autoGetCouponList.stream().map(CouponActivityAutoGetCouponDTO::getCouponActivityAutoGetId).collect(Collectors.toList());
            //                        if(CollUtil.isNotEmpty(autoGetIdList)){
            //                            List<CouponActivityAutoGetDTO> autoGetList = autoGetService.getAutoGetByIdList(autoGetIdList);
            //                            if(CollUtil.isEmpty(autoGetList)){
            //                                removeWithoutAutoGet(list);
            //                            } else {
            //                                // 平台创建的优惠券活动，去除没有关联自主领取活动的
            //                                Map<Long, List<CouponActivityAutoGetDTO>> autoGetMap = autoGetList.stream().collect(Collectors.groupingBy(CouponActivityAutoGetDTO::getId));
            //                                Iterator<CouponActivityAutoGetCouponDTO> autoGetIt = autoGetCouponList.iterator();
            //                                while (autoGetIt.hasNext()) {
            //                                    CouponActivityAutoGetCouponDTO next = autoGetIt.next();
            //                                    List<CouponActivityAutoGetDTO> autoGet = autoGetMap.get(next.getCouponActivityAutoGetId());
            //                                    if(ObjectUtil.isNull(autoGet)){
            //                                        autoGetIt.remove();
            //                                    }
            //                                }
            //
            //                                Map<Long, List<CouponActivityAutoGetCouponDTO>> autoGetCouponMap = autoGetCouponList.stream().collect(Collectors.groupingBy(CouponActivityAutoGetCouponDTO::getCouponActivityId));
            //                                Iterator<CouponActivityListFiveByGoodsIdDTO> couponActivityIt = list.iterator();
            //                                while (couponActivityIt.hasNext()) {
            //                                    CouponActivityListFiveByGoodsIdDTO next = couponActivityIt.next();
            //                                    List<CouponActivityAutoGetCouponDTO> autoGetCoupon = autoGetCouponMap.get(next.getId());
            //                                    if(ObjectUtil.isNull(autoGetCoupon)){
            //                                        couponActivityIt.remove();
            //                                    }
            //                                }
            //                            }
            //                        }
            //                    }
            //                }
            //            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.MOBILE_GOODS_QUERY_CAN_COUPON_ACTIVITY_ERROR, e.getMessage());
        }
        return list;
    }

    private List<CouponActivityDO> getCouponActivityUnique(List<CouponActivityDO> couponActivityTempList) {
        TreeSet<CouponActivityDO> treeSet = new TreeSet<>((o1, o2) -> o1.getId().compareTo(o2.getId()));
        treeSet.addAll(couponActivityTempList);
        return new ArrayList<>(treeSet);
    }

    private void removeWithoutAutoGet(List<CouponActivityListFiveByGoodsIdDTO> list) {
        Iterator<CouponActivityListFiveByGoodsIdDTO> it = list.iterator();
        while (it.hasNext()) {
            CouponActivityListFiveByGoodsIdDTO next = it.next();
            if (ObjectUtil.equal(next.getCreateUser(), 0L)) {
                it.remove();
            }
        }
    }

    @Override
    public CouponActivityCanAndOwnDTO getCanAndOwnListByEid(QueryCouponActivityCanReceiveRequest request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCurrentEid()) || ObjectUtil.isNull(request.getEid()) || ObjectUtil.isNull(request.getPlatformType())) {
            return null;
        }

        CouponActivityCanAndOwnDTO couponActivity;
        try {

            //            // 查询优惠券活动，取并集：1、限制商品为当前goodsId  2、限制企业为当前eid  3、不限制企业的
            //            /* 限制商品为当前goodsId */
            //            List<Long> couponActivityIdGoodsLimitList = new ArrayList<>();
            //            List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByGoodsIdAndEid(goodsId, eid);
            //            if (CollUtil.isNotEmpty(goodsLimitList)) {
            //                couponActivityIdGoodsLimitList = goodsLimitList.stream().map(CouponActivityGoodsLimitDO::getCouponActivityId)
            //                    .collect(Collectors.toList());
            //            }
            //
            //            /* 限制企业为当前eid */
            //            List<Long> couponActivityIdEnterpriseLimitList = new ArrayList<>();
            //            // 优惠券限制企业关系
            //            List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitService.getByEid(eid);
            //            if (CollUtil.isNotEmpty(enterpriseLimitList)) {
            //                couponActivityIdEnterpriseLimitList = enterpriseLimitList.stream().map(CouponActivityEnterpriseLimitDO::getCouponActivityId)
            //                    .collect(Collectors.toList());
            //            }
            //
            //            /* 不限制企业的 */
            //            List<CouponActivityDO> effectiveWithoutEnterpriseLimitList = couponActivityService.getEffectiveListWithoutEnterpriseLimit();
            //
            //            /* 取并集 */
            //            List<Long> idAllTempList = new ArrayList<>();
            //            idAllTempList.addAll(couponActivityIdGoodsLimitList);
            //            idAllTempList.addAll(couponActivityIdEnterpriseLimitList);
            //            List<Long> idAllList = idAllTempList.stream().distinct().collect(Collectors.toList());
            //            // 查询有限制的优惠券活动
            //            List<CouponActivityDetailDTO> allList = new ArrayList<>();
            //            List<CouponActivityDO> effectiveCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(idAllList);
            //            List<CouponActivityDetailDTO> detailLimitList = PojoUtils.map(effectiveCouponActivityList, CouponActivityDetailDTO.class);
            //            List<CouponActivityDetailDTO> detailWithoutLimitList = PojoUtils.map(effectiveWithoutEnterpriseLimitList, CouponActivityDetailDTO.class);
            //            allList.addAll(detailLimitList);
            //            allList.addAll(detailWithoutLimitList);
            //            if (CollUtil.isNotEmpty(allList)) {
            //                List<CouponActivityDetailDTO> distincList = allList.stream()
            //                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(CouponActivityDetailDTO::getId))), ArrayList::new));
            //                allList = new ArrayList<>();
            //                allList.addAll(distincList);
            //            }

            request.setBusinessType(1);
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
            log.info("getCanReceiveListByEid param is"+ JSONUtil.toJsonStr(request));
            List<CouponActivityDTO> couponActivityTempList = this.baseMapper.getCanReceiveListByEid(request);
            if (CollUtil.isEmpty(couponActivityTempList)) {
                return null;
            }
            // 如果加了goodsId还要看是否商品可用
            if(request.getGoodsId()!=null){
                List<CouponActivityDTO> collect = couponActivityTempList.stream().filter(item -> item.getGoodsLimit() == 2).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(collect)){
                    // 部分商品可用的优惠券id集合
                    List<Long> partCanuseActivityIds = collect.stream().map(CouponActivityDTO::getId).collect(Collectors.toList());
                    List<Long> canUserIds = this.baseMapper.getCanUseIdByGoodsIdAndActivityIds(partCanuseActivityIds,request.getGoodsId());
                    Collection<Long> subtract = CollectionUtil.subtract(partCanuseActivityIds, canUserIds);
                    //不能商品使用的优惠券
                    if(CollectionUtils.isNotEmpty(subtract)){
                        List<Long> notUse = CollectionUtil.newArrayList(subtract);
                        couponActivityTempList= couponActivityTempList.stream().filter(item->!notUse.contains(item.getId())).collect(Collectors.toList());
                    }
                }
            }
            if (CollUtil.isEmpty(couponActivityTempList)) {
                return null;
            }
            // 过滤掉发放完毕的券。
            List<Long> activityIdLists = couponActivityTempList.stream().map(CouponActivityDTO::getId).collect(Collectors.toList());
            List<CouponDTO> hasGiveCountByCouponActivityList = couponService.getHasGiveCountByCouponActivityList(activityIdLists);
            List<CouponHasGetDTO> giveCountList=PojoUtils.map(hasGiveCountByCouponActivityList,CouponHasGetDTO.class);
            // 判断是否会员，是否新客
            List<CouponActivityDTO> resuclt = new ArrayList<>();
            if (giveCountList != null) {
                Map<Long, Integer> giveCountMap = new HashMap<>();
                giveCountList.forEach(item->{
                    giveCountMap.put(item.getId(),item.getNum());
                });
                couponActivityTempList.forEach(item->{
                    Integer integer = giveCountMap.get(item.getId());
                    if(ObjectUtil.isNull(integer)||integer<item.getTotalCount()){
                        resuclt.add(item);
                    }
                });
            }
            couponActivityTempList=resuclt;

            boolean currentMember = member.getCurrentMember() == 1;
            Iterator<CouponActivityDTO> iterator = couponActivityTempList.iterator();
            while (iterator.hasNext()) {
                CouponActivityDTO next = iterator.next();
                boolean flag = checkUserType(next.getUserType(), currentMember);
                if (!flag) {
                    iterator.remove();
                }
            }
            if (CollUtil.isEmpty(couponActivityTempList)) {
                return null;
            }
            List<CouponActivityDO> couponActivityList = getCouponActivityUnique(PojoUtils.map(couponActivityTempList, CouponActivityDO.class));
            List<Long> idAllList = couponActivityList.stream().map(CouponActivityDO::getId).collect(Collectors.toList());

            // 查询平台、商家优惠券规则设置
            Map<Long, Integer> canGetNumMap = couponActivityService.getCanGetNumMap(couponActivityList);

            /* 已领取的优惠券数量统计 */
            Map<Long, Integer> couponHasGetCountMap = couponActivityService.getCouponHasGetCountMap(request.getCurrentEid(), idAllList);

            /* 领取达到上限数量的优惠券，视为已领取 */
            List<CouponActivityDetailDTO> couponActivityDetailList = PojoUtils.map(couponActivityList, CouponActivityDetailDTO.class);
            // 如果传递了goodsId，还要过滤能用于当前goodsId的券。
            couponActivity = new CouponActivityCanAndOwnDTO();
            List<CouponActivityHasGetDTO> canList = new ArrayList<>();
            List<CouponActivityHasGetDTO> ownList = new ArrayList<>();
            buildCouponActivityHasGetDTO(couponActivityDetailList, couponHasGetCountMap, canGetNumMap, canList, ownList);
            couponActivity.setCanList(canList);
            couponActivity.setOwnList(ownList);
            log.info("canList is"+ JSONUtil.toJsonStr(canList)+"ownList is"+ JSONUtil.toJsonStr(ownList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.MOBILE_GOODS_QUERY_GET_COUPON_ACTIVITY_ERROR, e.getMessage());
        }
        return couponActivity;
    }

    private boolean checkUserType(Integer userType, boolean currentMember) {
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

    @Override
    public CouponActivityEidOrGoodsIdDTO getGoodsListByGoodsIdAndEid(Long goodsId, Long eid) {
        if (ObjectUtil.isNull(goodsId) || ObjectUtil.isNull(eid)) {
            return null;
        }

        CouponActivityEidOrGoodsIdDTO dto = new CouponActivityEidOrGoodsIdDTO();
        try {

            /* 1.选择全部企业、全部商品的优惠券活动，所有商品可参加 */
            List<CouponActivityDO> withoutEnterpriseLimitList = couponActivityService.getEffectiveListWithoutEnterpriseLimit();
            if (CollUtil.isNotEmpty(withoutEnterpriseLimitList)) {
                // 查询所有企业id
                dto.setAllEidFlag(true);
                return dto;
            }

            /* 限制商品为当前goodsId */
            Map<Long, List<CouponActivityGoodsLimitDO>> goodsLimitMap = new HashMap<>();
            List<CouponActivityGoodsLimitDO> goodsLimitAllList = new ArrayList<>();
            List<Long> couponActivityIdGoodsLimitList;
            List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByGoodsIdAndEid(goodsId, eid);
            if (CollUtil.isNotEmpty(goodsLimitList)) {
                couponActivityIdGoodsLimitList = goodsLimitList.stream().map(CouponActivityGoodsLimitDO::getCouponActivityId).collect(Collectors.toList());
                // 有效的优惠券活动
                List<CouponActivityDO> effectiveCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIdGoodsLimitList, 0, 0);
                if (CollUtil.isNotEmpty(effectiveCouponActivityList)) {
                    couponActivityIdGoodsLimitList = effectiveCouponActivityList.stream().map(CouponActivityDO::getId).distinct().collect(Collectors.toList());
                    // 根据优惠券活动id查询所有的可使用商品表
                    for (CouponActivityGoodsLimitDO goodsLimit : goodsLimitList) {
                        if (couponActivityIdGoodsLimitList.contains(goodsLimit.getCouponActivityId())) {
                            goodsLimitAllList.add(goodsLimit);
                        }
                    }
                    if (CollUtil.isNotEmpty(goodsLimitAllList)) {
                        goodsLimitMap = goodsLimitAllList.stream().collect(Collectors.groupingBy(CouponActivityGoodsLimitDO::getEid));
                    }
                }
            }

            /* 限制企业为当前eid的优惠券活动 */
            Map<Long, List<CouponActivityEnterpriseLimitDO>> enterpriseLimitMap = new HashMap<>();
            List<CouponActivityEnterpriseLimitDO> enterpriseLimitAllList = new ArrayList<>();
            List<Long> activityEnterpriseLimitIdList = new ArrayList<>();
            // 优惠券限制企业关系
            List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitService.getByEid(eid);
            if (CollUtil.isNotEmpty(enterpriseLimitList)) {
                activityEnterpriseLimitIdList = enterpriseLimitList.stream().map(CouponActivityEnterpriseLimitDO::getCouponActivityId).collect(Collectors.toList());
                // 有效的优惠券活动
                List<CouponActivityDO> effectiveCouponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(activityEnterpriseLimitIdList, 0, 0);
                if (CollUtil.isNotEmpty(effectiveCouponActivityList)) {
                    // 部分商家、全部商品可用
                    activityEnterpriseLimitIdList = effectiveCouponActivityList.stream().filter(c -> (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getEnterpriseLimit()) || (ObjectUtil.isNotNull(c.getEid()) && c.getEid().intValue() != 0)) && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit())).map(CouponActivityDO::getId).distinct().collect(Collectors.toList());
                    // 根据优惠券活动id，查询所有可使用企业关系表
                    if (CollUtil.isNotEmpty(activityEnterpriseLimitIdList)) {
                        for (CouponActivityEnterpriseLimitDO enterpriseLimit : enterpriseLimitList) {
                            if (activityEnterpriseLimitIdList.contains(enterpriseLimit.getCouponActivityId())) {
                                enterpriseLimitAllList.add(enterpriseLimit);
                            }
                        }
                        if (CollUtil.isNotEmpty(enterpriseLimitAllList)) {
                            enterpriseLimitMap = enterpriseLimitAllList.stream().collect(Collectors.groupingBy(CouponActivityEnterpriseLimitDO::getEid));
                        }
                    }
                }
            }

            // 当前eid创建的优惠券活动
            boolean eidFlag = false;
            List<CouponActivityDO> couponActivityList = couponActivityService.getCouponActivityByEeid(new ArrayList() {{
                add(eid);
            }});
            if (CollUtil.isNotEmpty(couponActivityList)) {
                eidFlag = true;
            }

            if (MapUtil.isEmpty(goodsLimitMap) && MapUtil.isEmpty(enterpriseLimitMap) && !eidFlag) {
                return null;
            }

            /* 企业关系中已存在的eid，在商品关系中去除 */
            List<Long> goodsIdList = new ArrayList<>();
            List<Long> eidList = new ArrayList<>();
            if (MapUtil.isNotEmpty(goodsLimitMap) && MapUtil.isEmpty(enterpriseLimitMap)) {
                goodsIdList = goodsLimitAllList.stream().map(CouponActivityGoodsLimitDO::getGoodsId).distinct().collect(Collectors.toList());
                dto.setGoodsIdList(goodsIdList);
                return dto;
            }
            if (MapUtil.isEmpty(goodsLimitMap) && MapUtil.isNotEmpty(enterpriseLimitMap)) {
                eidList = enterpriseLimitAllList.stream().map(CouponActivityEnterpriseLimitDO::getEid).distinct().collect(Collectors.toList());
                dto.setEidList(eidList);
                return dto;
            }
            if (MapUtil.isNotEmpty(goodsLimitMap) && MapUtil.isNotEmpty(enterpriseLimitMap)) {
                for (Map.Entry<Long, List<CouponActivityEnterpriseLimitDO>> entry : enterpriseLimitMap.entrySet()) {
                    List<CouponActivityGoodsLimitDO> goodsLimit = goodsLimitMap.get(entry.getKey());
                    if (ObjectUtil.isNotNull(goodsLimit)) {
                        goodsLimitMap.remove(entry.getKey());
                    }
                }
                // goodsId
                if (MapUtil.isNotEmpty(goodsLimitMap)) {
                    goodsIdList = goodsLimitAllList.stream().map(CouponActivityGoodsLimitDO::getGoodsId).distinct().collect(Collectors.toList());
                    dto.setGoodsIdList(goodsIdList);
                }
                // eid
                eidList = new ArrayList<>(enterpriseLimitMap.keySet());
                if (eidFlag) {
                    eidList.add(eid);
                }
                eidList = eidList.stream().distinct().collect(Collectors.toList());
                dto.setEidList(eidList);
                return dto;
            }
        } catch (Exception e) {
            log.error("移动端获取优惠券活动相关的商品列出异常，goodsId -> {}, eid -> {}, exception -> {}", goodsId, eid, e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.MOBILE_GOODS_QUERY_GET_COUPON_ACTIVITY_GOODS_ERROR);
        }
        return null;
    }

    @Override
    @GlobalTransactional
    public Boolean receiveByCouponActivityId(CouponActivityReceiveRequest request) {
        log.info("3getCoupon"+request);
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityId()) || request.getCouponActivityId() == 0 || ObjectUtil.isNull(request.getEid()) || ObjectUtil.isNull(request.getPlatformType())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        try {
            QueryCouponActivityDetailRequest detailRequest = new QueryCouponActivityDetailRequest();
            detailRequest.setId(request.getCouponActivityId());
            //CouponActivityDetailDTO couponActivity = couponActivityService.getCouponActivityById(detailRequest);
            CouponActivityDO couponActivityDO = couponActivityService.getById(request.getCouponActivityId());
            CouponActivityDetailDTO couponActivity = PojoUtils.map(couponActivityDO, CouponActivityDetailDTO.class);
            List<Long> autoGetIdList = new ArrayList<>();
            String faileReason = checkCouponActivityForGet(couponActivity, request, autoGetIdList);
            int recordStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
            if (StrUtil.isNotBlank(faileReason)) {
                // 领取失败
                //                recordStatus = CouponActivityResultTypeEnum.FAIL.getCode();
                //                autoGetRecordService.saveGetRecord(request.getCouponActivityId(), request.getEid(), request.getUserId(), 1, recordStatus,
                //                    faileReason);

                String errMsg = MessageFormat.format(CouponErrorCode.GET_COUPON_ERROR.getMessage(), faileReason);
                throw new BusinessException(CouponErrorCode.GET_COUPON_ERROR, errMsg);
            }

            // 领取优惠券
            String suffix = ", couponActivityId:" + couponActivity.getId() + ", userId:" + request.getUserId();
            Date date = new Date();
            // 根据优惠券有效期类型设置时间
            Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, couponActivity.getBeginTime(), couponActivity.getEndTime(), couponActivity.getUseDateType(), couponActivity.getExpiryDays());
            Date beginTime = timeMap.get("beginTime");
            Date endTime = timeMap.get("endTime");

            CouponAutoGiveRequest getRequest = new CouponAutoGiveRequest();
            if (ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid().intValue() == 0) {
                // 平台自动领取活动
                getRequest.setCouponActivityAutoId(autoGetIdList.get(0));
            } else {
                // 商家自动领取活动
                getRequest.setCouponActivityBusinessAutoId(autoGetIdList.get(0));
            }
            getRequest.setCouponActivityId(request.getCouponActivityId());
            getRequest.setCouponActivityName(couponActivity.getName());
            getRequest.setEid(request.getEid());
            getRequest.setEname(request.getEname());
            getRequest.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
            getRequest.setGetUserId(request.getUserId());
            getRequest.setGetUserName(request.getUserName());
            getRequest.setGetTime(date);
            getRequest.setBeginTime(beginTime);
            getRequest.setEndTime(endTime);
            getRequest.setCreateUser(request.getUserId());
            getRequest.setCreateTime(date);
            boolean couponFlag = couponService.autoGive(getRequest);
            // 领取失败
            if (!couponFlag) {
                log.info("领取优惠券失败，couponActivityId -> {}, usetId -> {}, eid -> {}", request.getCouponActivityId(), request.getUserId(), request.getEid());
                throw new BusinessException(CouponErrorCode.GET_COUPON_ERROR);
            }
            // 领取成功
            Boolean aBoolean = autoGetRecordService.saveGetRecord(request.getCouponActivityId(), request.getEid(), request.getUserId(), 1, recordStatus, "");
            couponActivityService.updateHasGiveNum(request.getCouponActivityId(),1);
            return aBoolean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.MOBILE_GOODS_GET_COUPON_ERROR, e.getMessage());
        }
    }

    @Override
    public Map<Long, List<Integer>> getCouponActivityExistFlag(CouponActivityExistFlagRequest request) {
        log.info("getCouponActivityExistFlag={}",request);
        if (ObjectUtil.isNull(request) || CollUtil.isEmpty(request.getDetailList())) {
            return MapUtil.empty();
        }
        Map<Long, List<Integer>> result = new HashMap<>();
        Map<Long, List<CouponActivityDO>> map = new HashMap<>();
        List<CouponActivityExistFlagDetailRequest> detailList = request.getDetailList();
        for (CouponActivityExistFlagDetailRequest detail : detailList) {
            if (ObjectUtil.isNull(detail.getEid()) || ObjectUtil.isNull(detail.getGoodsId())) {
                throw new BusinessException(ResultCode.PARAM_MISS);
            }
            map.put(detail.getGoodsId(), new ArrayList<CouponActivityDO>());
        }
        Map<Long, List<CouponActivityExistFlagDetailRequest>> eidMap = detailList.stream().collect(Collectors.groupingBy(CouponActivityExistFlagDetailRequest::getEid));

        try {
            /* 选择全部企业、全部商品的优惠券活动，所有商品可参加*/
            List<CouponActivityDO> withoutEnterpriseLimitList = couponActivityService.getEffectiveListWithoutEnterpriseLimit();
            if (CollUtil.isNotEmpty(withoutEnterpriseLimitList)) {
                List<CouponActivityDO> tempResult = new ArrayList<>();
                List<CouponActivityDO> collect = withoutEnterpriseLimitList.stream().filter(item -> item.getUseDateType() == 1 && (item.getBeginTime().before(new Date()) && item.getEndTime().after(new Date()))).collect(Collectors.toList());
                tempResult.addAll(collect);
                List<CouponActivityDO> collect1 = withoutEnterpriseLimitList.stream().filter(item -> item.getUseDateType() == 2).collect(Collectors.toList());
                tempResult.addAll(collect1);
                withoutEnterpriseLimitList = tempResult;
            }
            if (CollUtil.isNotEmpty(withoutEnterpriseLimitList)) {

                for (CouponActivityExistFlagDetailRequest detail : detailList) {
                    List<CouponActivityDO> couponActivityDOS = map.get(detail.getGoodsId());
                    if(CollectionUtil.isEmpty(couponActivityDOS)){
                        map.put(detail.getGoodsId(),withoutEnterpriseLimitList);
                    }else {
                        couponActivityDOS.addAll(withoutEnterpriseLimitList);
                        map.put(detail.getGoodsId(),couponActivityDOS);
                    }
                }
            }

            List<Long> eidList = detailList.stream().map(CouponActivityExistFlagDetailRequest::getEid).collect(Collectors.toList());
            List<Long> goodsIdList = detailList.stream().map(CouponActivityExistFlagDetailRequest::getGoodsId).collect(Collectors.toList());
            Map<Long, Boolean> tempMap = new HashMap<>();

            /* 限制商品为当前goodsId */
            List<Long> couponActivityIdGoodsLimitList;
            List<CouponActivityGoodsLimitDO> goodsLimitList = couponActivityGoodsLimitService.getListByGoodsIdAndEidList(eidList, goodsIdList);
            if (CollUtil.isNotEmpty(goodsLimitList)) {
                couponActivityIdGoodsLimitList = goodsLimitList.stream().map(CouponActivityGoodsLimitDO::getCouponActivityId).collect(Collectors.toList());
                // 根据查询优惠券活动
                List<CouponActivityDO> couponActivityList = couponActivityService.getEffectiveCouponActivityByIdList(couponActivityIdGoodsLimitList, 0, 0);
                if (CollUtil.isNotEmpty(couponActivityList)) {
                    List<CouponActivityDO> tempResult = new ArrayList<>();
                    List<CouponActivityDO> collect = couponActivityList.stream().filter(item -> item.getUseDateType() == 1 && (item.getBeginTime().before(new Date()) && item.getEndTime().after(new Date()))).collect(Collectors.toList());
                    tempResult.addAll(collect);
                    List<CouponActivityDO> collect1 = couponActivityList.stream().filter(item -> item.getUseDateType() == 2).collect(Collectors.toList());
                    tempResult.addAll(collect1);
                    couponActivityList = tempResult;
                }
                if (CollUtil.isNotEmpty(couponActivityList)) {
                    Map<Long, List<CouponActivityDO>> couponActivityMap = couponActivityList.stream().collect(Collectors.groupingBy(CouponActivityDO::getId));
                    for (CouponActivityGoodsLimitDO goodsLimit : goodsLimitList) {
                        List<CouponActivityDO> couponActivity = couponActivityMap.get(goodsLimit.getCouponActivityId());
                        if (ObjectUtil.isNotNull(couponActivity)) {
                            List<CouponActivityDO> couponActivityDOS = map.get(goodsLimit.getGoodsId());
                            if(CollectionUtil.isEmpty(couponActivityDOS)){
                                map.put(goodsLimit.getGoodsId(),couponActivity);
                            }else {
                                couponActivityDOS.addAll(couponActivity);
                                map.put(goodsLimit.getGoodsId(),couponActivityDOS);
                            }
                        }
                    }
                }
            }

            /* 限制企业为当前eid的优惠券活动 */
            List<Long> activityEnterpriseLimitIdList;
            // 优惠券限制企业关系
            List<CouponActivityEnterpriseLimitDO> enterpriseLimitList = couponActivityEnterpriseLimitService.getByEidList(eidList);
            if (CollUtil.isNotEmpty(enterpriseLimitList)) {
                activityEnterpriseLimitIdList = enterpriseLimitList.stream().map(CouponActivityEnterpriseLimitDO::getCouponActivityId).collect(Collectors.toList());
                // 根据优惠券活动id，查询所有可使用企业关系表
                LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.in(CouponActivityDO::getId, activityEnterpriseLimitIdList);
                queryWrapper.eq(CouponActivityDO::getGoodsLimit, 1);
                queryWrapper.eq(CouponActivityDO::getStatus, CouponActivityStatusEnum.ENABLED.getCode());
                List<CouponActivityDO> list = this.list(queryWrapper);
                if (CollUtil.isNotEmpty(list)) {
                    List<CouponActivityDO> tempResult = new ArrayList<>();
                    List<CouponActivityDO> collect = list.stream().filter(item -> item.getUseDateType() == 1 && (item.getBeginTime().before(new Date()) && item.getEndTime().after(new Date()))).collect(Collectors.toList());
                    tempResult.addAll(collect);
                    List<CouponActivityDO> collect1 = list.stream().filter(item -> item.getUseDateType() == 2).collect(Collectors.toList());
                    tempResult.addAll(collect1);
                    list = tempResult;
                }
                // 已发放数量
                    Iterator<CouponActivityDO> it = list.iterator();
                    while (it.hasNext()) {
                        CouponActivityDO couponActivity = it.next();

                        Integer totalCount = couponActivity.getTotalCount();
                        Integer giveCount = couponActivity.getGiveCount();
                        if (ObjectUtil.isNull(totalCount) || totalCount < 0) {
                            it.remove();
                        }
                        if (totalCount <= giveCount) {
                            it.remove();
                        }
                    }


                List<CouponActivityDO> couponActivityList = list;
                if (CollUtil.isNotEmpty(couponActivityList)) {
                    // 部分商家、全部商品可用
                    Map<Long, List<CouponActivityDO>> couponActivityMap = couponActivityList.stream().filter(c -> (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), c.getEnterpriseLimit()) || (ObjectUtil.isNotNull(c.getEid()) && c.getEid().intValue() != 0)) && ObjectUtil.equal(CouponLimitTypeEnum.ALL.getCode(), c.getGoodsLimit())).collect(Collectors.groupingBy(CouponActivityDO::getId));
                    for (CouponActivityEnterpriseLimitDO enterpriseLimit : enterpriseLimitList) {
                        List<CouponActivityDO> couponActivity = couponActivityMap.get(enterpriseLimit.getCouponActivityId());
                        if (ObjectUtil.isNotNull(couponActivity)) {
                            List<CouponActivityExistFlagDetailRequest> detailRequests = eidMap.get(enterpriseLimit.getEid());
                            if (ObjectUtil.isNotNull(detailRequests)) {
                                detailRequests.forEach(d -> {
                                    List<CouponActivityDO> couponActivityDOS = map.get(d.getGoodsId());
                                    if(CollectionUtil.isEmpty(couponActivityDOS)){
                                        map.put(d.getGoodsId(),couponActivity);
                                    }else {
                                        couponActivityDOS.addAll(couponActivity);
                                        map.put(d.getGoodsId(),couponActivityDOS);
                                    }
                                });
                            }
                        }
                    }
                }
            }

            // 当前eid创建的优惠券活动

            LambdaQueryWrapper<CouponActivityDO> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.in(CouponActivityDO::getGoodsLimit, 1);
            queryWrapper.in(CouponActivityDO::getEid, eidList);
            List<CouponActivityDO> couponActivityList=couponActivityService.list(queryWrapper);
            if (CollUtil.isNotEmpty(couponActivityList)) {
                List<CouponActivityDO> tempResult = new ArrayList<>();
                List<CouponActivityDO> collect = couponActivityList.stream().filter(item -> item.getUseDateType() == 1 && (item.getBeginTime().before(new Date()) && item.getEndTime().after(new Date()))).collect(Collectors.toList());
                tempResult.addAll(collect);
                List<CouponActivityDO> collect1 = couponActivityList.stream().filter(item -> item.getUseDateType() == 2).collect(Collectors.toList());
                tempResult.addAll(collect1);
                couponActivityList=tempResult;
            }
            if (CollUtil.isNotEmpty(couponActivityList)) {
                for (CouponActivityDO couponActivityDO : couponActivityList) {
                    List<CouponActivityExistFlagDetailRequest> detailRequests = eidMap.get(couponActivityDO.getEid());
                    if (ObjectUtil.isNotNull(detailRequests)) {
                        detailRequests.forEach(d -> {
                            List<CouponActivityDO> couponActivityDOS = map.get(d.getGoodsId());
                            if(CollectionUtil.isEmpty(couponActivityDOS)){
                                map.put(d.getGoodsId(), Arrays.asList(couponActivityDO));
                            }else {
                                couponActivityDOS.add(couponActivityDO);
                                map.put(d.getGoodsId(),couponActivityDOS);
                            }
                        });
                    }
                }
            }
            // 获取与请求对应的eid
            for (CouponActivityExistFlagDetailRequest detail : detailList) {
                List<CouponActivityDO> couponActivityDOS = map.get(detail.getGoodsId());
                if(CollectionUtil.isEmpty(couponActivityDOS)){
                    result.put(detail.getGoodsId(), null);
                }else {
                    couponActivityDOS = couponActivityDOS.stream().distinct().collect(Collectors.toList());
                    List<Integer> types = couponActivityDOS.stream().map(CouponActivityDO::getType).distinct().collect(Collectors.toList());
                    log.info("getCouponActivityExistFlag==={}对应的优惠券列表id{}",detail.getGoodsId(),couponActivityDOS.stream().map(CouponActivityDO::getId).distinct().collect(Collectors.toList()));
                    result.put(detail.getGoodsId(), types);
                }
            }
        } catch (Exception e) {
            log.error("根据企业ID、商品ID列表 查询是否存在优惠券活动异常，exception -> {}", e);
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.QUERY_COUPON_ACTIVITY_EXIST_ERROR, e.getMessage());
        }
        return result;
    }


    @Override
    @GlobalTransactional
    public GetCouponActivityResultDTO receiveByCouponActivityIdForApp(CouponActivityReceiveRequest request) {
        log.info("receiveByCouponActivityId, request -> {}", JSON.toJSONString(request));
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getCouponActivityId()) || request.getCouponActivityId() == 0 || ObjectUtil.isNull(request.getEid()) || ObjectUtil.isNull(request.getPlatformType())) {
            throw new BusinessException(ResultCode.PARAM_MISS);
        }

        try {
            QueryCouponActivityDetailRequest detailRequest = new QueryCouponActivityDetailRequest();
            detailRequest.setId(request.getCouponActivityId());
            CouponActivityDetailDTO couponActivity =PojoUtils.map(couponActivityService.getById(request.getCouponActivityId()),CouponActivityDetailDTO.class);
            // 校验当前是否到每天达领取上限
            CouponActivityAutoGetCouponDTO byCouponActivityId = autoGetCouponService.getByCouponActivityId(couponActivity.getId());
            if (ObjectUtil.isNotNull(byCouponActivityId)) {
                Integer giveNumDaily = byCouponActivityId.getGiveNumDaily();
                if (ObjectUtil.isNotNull(giveNumDaily) && giveNumDaily.intValue() != 0) {
                    // 查看今天已经领取的数量
                    List<CouponDO> hasGetDailyNum = this.baseMapper.getHasGetDailyNum(request.getCouponActivityId(), request.getEid(), byCouponActivityId.getCouponActivityAutoGetId());
                    if (CollectionUtil.isNotEmpty(hasGetDailyNum)) {
                        List<CouponDO> sameDay = hasGetDailyNum.stream().filter(item -> DateUtil.isSameDay(item.getGetTime(), new Date())).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(sameDay) && sameDay.size() >= giveNumDaily.intValue()) {
                            GetCouponActivityResultDTO activityResultDTO = new GetCouponActivityResultDTO();
                            activityResultDTO.setGetSussess(false);
                            activityResultDTO.setToLimitDaily(true);
                            return activityResultDTO;
                        }
                    }
                }
            }
            // 校验当前是否到每天达领取上限结束
            List<Long> autoGetIdList = new ArrayList<>();
            String faileReason = checkCouponActivityForGet(couponActivity, request, autoGetIdList);
            int recordStatus = CouponActivityResultTypeEnum.SUCCESS.getCode();
            if (StrUtil.isNotBlank(faileReason)) {
                // 领取失败
                //                recordStatus = CouponActivityResultTypeEnum.FAIL.getCode();
                //                autoGetRecordService.saveGetRecord(request.getCouponActivityId(), request.getEid(), request.getUserId(), 1, recordStatus,
                //                    faileReason);

                String errMsg = MessageFormat.format(CouponErrorCode.GET_COUPON_ERROR.getMessage(), faileReason);
                throw new BusinessException(CouponErrorCode.GET_COUPON_ERROR, errMsg);
            }

            // 领取优惠券
            String suffix = ", couponActivityId:" + couponActivity.getId() + ", userId:" + request.getUserId();
            Date date = new Date();
            // 根据优惠券有效期类型设置时间
            Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, couponActivity.getBeginTime(), couponActivity.getEndTime(), couponActivity.getUseDateType(), couponActivity.getExpiryDays());
            Date beginTime = timeMap.get("beginTime");
            Date endTime = timeMap.get("endTime");

            CouponAutoGiveRequest getRequest = new CouponAutoGiveRequest();
            if (ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid().intValue() == 0) {
                // 平台自动领取活动
                getRequest.setCouponActivityAutoId(autoGetIdList.get(0));
            } else {
                // 商家自动领取活动
                getRequest.setCouponActivityBusinessAutoId(autoGetIdList.get(0));
            }
            getRequest.setCouponActivityId(request.getCouponActivityId());
            getRequest.setCouponActivityName(couponActivity.getName());
            getRequest.setEid(request.getEid());
            getRequest.setEname(request.getEname());
            getRequest.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
            getRequest.setGetUserId(request.getUserId());
            getRequest.setGetUserName(request.getUserName());
            getRequest.setGetTime(date);
            getRequest.setBeginTime(beginTime);
            getRequest.setEndTime(endTime);
            getRequest.setCreateUser(request.getUserId());
            getRequest.setCreateTime(date);
            boolean couponFlag = couponService.autoGive(getRequest);
            // 领取失败
            if (!couponFlag) {
                log.info("领取优惠券失败，couponActivityId -> {}, usetId -> {}, eid -> {}", request.getCouponActivityId(), request.getUserId(), request.getEid());
                throw new BusinessException(CouponErrorCode.GET_COUPON_ERROR);
            }
            couponActivityService.updateHasGiveNum(request.getCouponActivityId(),1);
            // 领取成功
            Boolean getRecord = autoGetRecordService.saveGetRecord(request.getCouponActivityId(), request.getEid(), request.getUserId(), 1, recordStatus, "");
            String hasGetCountMsg = receiveCheckHasGetCount(couponActivity, request.getEid(), suffix);
            log.info("是否领取成功{}，是否到达领取上限{}", getRecord, StringUtils.isNotEmpty(hasGetCountMsg));
            GetCouponActivityResultDTO activityResultDTO = new GetCouponActivityResultDTO();
            activityResultDTO.setGetSussess(getRecord);
            activityResultDTO.setToLimit(StringUtils.isNotEmpty(hasGetCountMsg));
            return activityResultDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CouponErrorCode.MOBILE_GOODS_GET_COUPON_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean oneKeyReceive(QueryCouponActivityCanReceiveRequest request) {
        log.info("4getCoupon"+request);
        QueryCouponActivityCanReceiveRequest queryCouponActivityCanReceiveRequest = new QueryCouponActivityCanReceiveRequest();
        queryCouponActivityCanReceiveRequest.setEid(request.getShopEid());
        queryCouponActivityCanReceiveRequest.setCurrentEid(request.getCurrentEid());
        queryCouponActivityCanReceiveRequest.setPlatformType(CouponPlatformTypeEnum.SALES_ASSIST.getCode());
        request.setPlatformType(CouponPlatformTypeEnum.SALES_ASSIST.getCode());
        CouponActivityCanAndOwnDTO canAndOwnListByEid = getCanAndOwnListByEid(queryCouponActivityCanReceiveRequest);
        if (ObjectUtil.isNull(canAndOwnListByEid) || CollectionUtils.isEmpty(canAndOwnListByEid.getCanList())) {
            return false;
        }
        List<CouponActivityHasGetDTO> canList = canAndOwnListByEid.getCanList();
        log.info("可以领取的优惠券有{}", JSON.toJSONString(canList));
        canList = canList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CouponActivityHasGetDTO::getId))), ArrayList::new));
        List<Long> activityIds = canList.stream().map(CouponActivityHasGetDTO::getId).collect(Collectors.toList());
        Date date = new Date();
        // 获取当前优惠券活动关联的生效的领券活动,id是优惠券活动id
        List<CouponActivityAutoGetCouponDO> couponDOList = activityAutoGetMapper.getAvailableAutogetAcouponByActivityId(activityIds);
        if(CollectionUtils.isEmpty(couponDOList)){
            return false;
        }
        Map<Long, CouponActivityAutoGetCouponDO> couponDOMap =couponDOList.stream().collect(Collectors.toMap(CouponActivityAutoGetCouponDO::getCouponActivityId, Function.identity()));
        List<CouponDO> couponDOList1 = new ArrayList<>();
        List<CouponActivityAutoGetRecordDO> couponActivityAutoGetRecordDOS = new ArrayList<>();
        List<CouponActivityDO> couponDOList2 = new ArrayList<>();
        canList.forEach(couponActivity -> {
            CouponActivityAutoGetCouponDO couponActivityAutoGetCouponDO = couponDOMap.get(couponActivity.getId());

            // 根据优惠券有效期类型设置时间
            Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, couponActivity.getBeginTime(), couponActivity.getEndTime(), couponActivity.getUseDateType(), couponActivity.getExpiryDays());
            Date beginTime = timeMap.get("beginTime");
            Date endTime = timeMap.get("endTime");
            CouponDO getRequest = new CouponDO();
            CouponActivityAutoGetRecordDO autoGetRecord = new CouponActivityAutoGetRecordDO();

            if (ObjectUtil.isNotNull(couponActivity.getEid()) && couponActivity.getEid().intValue() == 0) {
                // 平台自动领取活动
                getRequest.setCouponActivityAutoId(couponActivityAutoGetCouponDO==null?null:couponActivityAutoGetCouponDO.getId());
            } else {
                // 商家自动领取活动
                getRequest.setCouponActivityBusinessAutoId(couponActivityAutoGetCouponDO==null?null:couponActivityAutoGetCouponDO.getId());
            }
            getRequest.setCouponActivityId(couponActivity.getId());
            getRequest.setCouponActivityName(couponActivity.getName());
            getRequest.setEid(request.getEid());
            getRequest.setEname(request.getEname());
            getRequest.setGetType(CouponGetTypeEnum.AUTO_GET.getCode());
            getRequest.setGetUserId(request.getUserId());
            getRequest.setGetUserName(request.getUserName());
            getRequest.setGetTime(date);
            getRequest.setBeginTime(beginTime);
            getRequest.setEndTime(endTime);
            getRequest.setCreateUser(request.getUserId());
            getRequest.setCreateTime(date);
            getRequest.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
            getRequest.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());

            autoGetRecord.setEid(request.getEid());
            autoGetRecord.setCouponActivityId(couponActivity.getId());
            autoGetRecord.setGiveNum(1);
            autoGetRecord.setStatus(1);
            autoGetRecord.setFaileReason("");
            autoGetRecord.setCreateUser(request.getUserId());
            autoGetRecord.setCreateTime(new Date());
            CouponActivityDO couponActivityDO = new CouponActivityDO();
            couponActivityDO.setId(couponActivity.getId());
            couponActivityDO.setGiveCount(couponActivity.getCanGetNum());
            couponDOList2.add(couponActivityDO);
            if(couponActivity.getCanGetNum()!=null){
                // 券可以一次领三张，一次领完
                int num = couponActivity.getCanGetNum().intValue();
                log.info("优惠券id为{}，可以领取{}张", couponActivity.getId(),num);
                for(int i=0;i<num;i++){
                    couponDOList1.add(getRequest);
                    couponActivityAutoGetRecordDOS.add(autoGetRecord);
                }
            }
        });
        couponService.saveBatch(couponDOList1);
        autoGetRecordService.saveBatch(couponActivityAutoGetRecordDOS);
        // 更新优惠券表，已经发放数量更新
        couponDOList2.forEach(item->{
            couponActivityService.updateHasGiveNum(item.getId(),item.getGiveCount());
        });
        return true;
    }

    private String checkCouponActivityForGet(CouponActivityDetailDTO couponActivity, CouponActivityReceiveRequest request, List<Long> autoGetIdList) {
        Long userId = request.getUserId();
        Long eid = request.getEid();
        Integer etype = request.getEtype();
        Integer currentMember = request.getCurrentMember();

        String suffix = "";
        if (ObjectUtil.isNull(couponActivity)) {
            log.info("优惠券活动不存在，请检查");
            return CouponErrorCode.COUPON_ACTIVITY_NOT_EXIST_ERROR.getMessage();
        }
        suffix = ", couponActivityId:" + couponActivity.getId() + ", userId:" + userId;
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivity.getStatus())) {
            log.info("此优惠券活动不是“启用”状态，不能领取" + suffix);
            return CouponErrorCode.MOBILE_COUPON_ACTIVITY_STATUS_GET_ERROR.getMessage() + couponActivity.getName();
        }
        // 优惠券时间
        /*if(ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), couponActivity.getUseDateType())){
            Date beginTime = couponActivity.getBeginTime();
            Date endTime = couponActivity.getEndTime();
            long nowTime = System.currentTimeMillis();
            if(beginTime.getTime() > nowTime){
                return "此优惠券活动未开始，不能领取";
            }
            if(endTime.getTime() <= nowTime){
                return "此优惠券活动已结束，不能领取";
            }
        }*/
        // 优惠券活动使用平台
        List<String> platformSelectedList = couponActivity.getPlatformSelectedList();
        if (CollUtil.isNotEmpty(platformSelectedList)) {
            if (ObjectUtil.isNull(request.getPlatformType()) && !platformSelectedList.contains(request.getPlatformType().toString())) {
                return "此优惠券活动限制使用平台，不能领取";
            }
        }

        // 已生成券数量
        Map<Long, Integer> giveCountMap = new HashMap<>();
        List<Map<String, Long>> giveCountList = couponService.getGiveCountByCouponActivityId(Arrays.asList(couponActivity.getId()));
        if (CollUtil.isNotEmpty(giveCountList)) {
            for (Map<String, Long> map : giveCountList) {
                giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
            }
        }
        Integer totalCount = couponActivity.getTotalCount();
        Integer count = giveCountMap.get(couponActivity.getId());
        if (ObjectUtil.isNull(count) || count < 0) {
            count = 0;
        }
        if (count >= totalCount) {
            log.info("此优惠券已经全部发放完，不能领取" + suffix);
            return CouponErrorCode.MOBILE_COUPON_ACTIVITY_TOTAL_COUNT_GET_ERROR.getMessage();
        }
        // 领取活动校验
        String autoGetMsg = autoGetService.couponActivityAutoGetCheck(couponActivity, userId, eid, etype, currentMember, autoGetIdList);
        if (StrUtil.isNotBlank(autoGetMsg)) {
            return autoGetMsg;
        }

        /* 是否已领取达到数量上限 */
        String hasGetCountMsg = receiveCheckHasGetCount(couponActivity, eid, suffix);
        if (StrUtil.isNotBlank(hasGetCountMsg)) {
            return hasGetCountMsg;
        }
        return null;
    }

    public String receiveCheckHasGetCount(CouponActivityDetailDTO couponActivity, Long eid, String suffix) {
        // 查询平台、商家优惠券规则设置
        CouponActivityDO couponActivityDO = PojoUtils.map(couponActivity, CouponActivityDO.class);
        List<CouponActivityDO> doList = new ArrayList();
        doList.add(couponActivityDO);
        Map<Long, Integer> canGetNumMap = couponActivityService.getCanGetNumMap(doList);
        // 已领取的优惠券数量统计
        Map<Long, Integer> couponHasGetCountMap = couponActivityService.getCouponHasGetCountMap(eid, Arrays.asList(couponActivity.getId()));
        /* 领取达到上限数量的优惠券，视为已领取 */
        // 已领取
        Integer hasGetCount = couponHasGetCountMap.get(couponActivity.getId());
        if (ObjectUtil.isNull(hasGetCount) || hasGetCount < 0) {
            hasGetCount = 0;
        }
        // 可领取
        Integer canGetNum = canGetNumMap.get(couponActivity.getId());
        if (ObjectUtil.isNull(canGetNum) || hasGetCount.intValue() >= canGetNum.intValue()) {
            log.info("此优惠券已经领取达到数量上限，不能领取" + suffix);
            return "此优惠券已经领取完毕，不能再次领取";
        }
        return null;
    }

    private void buildCouponActivityHasGetDTO(List<CouponActivityDetailDTO> couponActivityDetailList, Map<Long, Integer> couponHasGetCountMap, Map<Long, Integer> canGetNumMap, List<CouponActivityHasGetDTO> canList, List<CouponActivityHasGetDTO> ownList) {
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
            // 是否已领取
            Integer hasGetCount = couponHasGetCountMap.get(couponActivityDetail.getId());
            couponActivityHasGetDto.setGoodsLimit(couponActivityDetail.getGoodsLimit() == 1 && couponActivityDetail.getEnterpriseLimit() == 1 ? 1 : 2);
            if (ObjectUtil.isNull(hasGetCount)) {
                hasGetCount = 0;
            }
            // 是否可领取
            Integer canGetNum = canGetNumMap.get(couponActivityDetail.getId());
            if (ObjectUtil.isNotNull(canGetNum) && canGetNum.intValue() > 0 && canGetNum.intValue() > hasGetCount.intValue()) {
                couponActivityHasGetDto.setGetFlag(false);
                couponActivityHasGetDto.setCanGetNum(canGetNum.intValue() - hasGetCount.intValue());
                canList.add(couponActivityHasGetDto);
            } else {
                couponActivityHasGetDto.setGetFlag(true);
                ownList.add(couponActivityHasGetDto);
            }
        }
    }

    private void buildCouponActivityListFiveByGoodsIdDTO(Integer limit, Set<Long> set, List<CouponActivityListFiveByGoodsIdDTO> list, List<CouponActivityDetailDTO> couponActivityDetailList) {
        if (CollUtil.isEmpty(couponActivityDetailList)) {
            return;
        }

        List<CouponActivityListFiveByGoodsIdDTO> dtoList = new ArrayList<>();
        CouponActivityListFiveByGoodsIdDTO couponActivityListFiveDto;
        for (CouponActivityDetailDTO couponActivityDetail : couponActivityDetailList) {
            couponActivityListFiveDto = new CouponActivityListFiveByGoodsIdDTO();
            couponActivityListFiveDto.setId(couponActivityDetail.getId());
            couponActivityListFiveDto.setName(couponActivityDetail.getName());
            couponActivityListFiveDto.setEid(couponActivityDetail.getEid());
            couponActivityListFiveDto.setCreateUser(couponActivityDetail.getCreateUser());
            CouponActivityDTO dto = couponActivityService.buildCouponActivityDtoForCouponRules(couponActivityDetail);
            couponActivityListFiveDto.setCouponRules(couponActivityService.buildCouponRules(dto));
            dtoList.add(couponActivityListFiveDto);
            set.add(couponActivityDetail.getId());
            if (set.size() == limit) {
                break;
            }
        }
        if (CollUtil.isNotEmpty(dtoList)) {
            list.addAll(dtoList);
            List<CouponActivityListFiveByGoodsIdDTO> distincList = list.stream().collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(CouponActivityListFiveByGoodsIdDTO::getId))), ArrayList::new));
            list = new ArrayList<>();
            list.addAll(distincList);
        }
    }

}
