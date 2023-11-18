package com.yiling.mall.coupon.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityRepeatGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivitySponsorTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.dto.request.SaveCouponActivityGiveEnterpriseInfoDetailRequest;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveDetailDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2021/11/25
 */
@Slf4j
@Service
public class CouponListenerUtil {

    @DubboReference
    CouponActivityApi         couponActivityApi;
    @DubboReference
    CouponActivityAutoGiveApi autoGiveApi;
    @DubboReference
    CouponApi                 couponApi;

    public List<CouponActivityDTO> getEffectiveCouponActivity(List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList) {
        List<Long> autoGiveIdList = autoGiveDetaiList.stream().map(CouponActivityAutoGiveDetailDTO::getId).collect(Collectors.toList());
        log.info("自动发券活动{}, 开始进行规则校验", autoGiveIdList);

        Set<Long> couponActivityIdSet = new HashSet<>();
        for (CouponActivityAutoGiveDetailDTO autoGiveDetail : autoGiveDetaiList) {
            List<CouponActivityAutoGiveCouponDetailDTO> autoGiveCouponList = autoGiveDetail.getCouponActivityList();
            if (CollUtil.isNotEmpty(autoGiveCouponList)) {
                autoGiveCouponList.stream().forEach(a -> {
                    couponActivityIdSet.add(a.getCouponActivityId());
                });
            }
        }
        return couponActivityApi.getEffectiveCouponActivityByIdList(new ArrayList<>(couponActivityIdSet), 0, 0);
    }

    public Map<Long, List<CouponActivityAutoGiveRecordDTO>> getAutoGiveRecordMap(Long eid, List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList) {
        Map<Long, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordMap = new HashMap<>();
        List<Long> autoGiveIdList = autoGiveDetaiList.stream().map(CouponActivityAutoGiveDetailDTO::getId).distinct().collect(Collectors.toList());
        List<CouponActivityAutoGiveRecordDTO> autoGiveRecordList = autoGiveApi.getRecordListByEidAndAutoGiveIds(eid, autoGiveIdList);
        if (CollUtil.isNotEmpty(autoGiveRecordList)) {
            autoGiveRecordMap = autoGiveRecordList.stream()
                .collect(Collectors.groupingBy(CouponActivityAutoGiveRecordDTO::getCouponActivityAutoGiveId));
        }
        return autoGiveRecordMap;
    }

    public List<CouponActivityAutoGiveCouponDetailDTO> getAutoGiveCouponDetailList(List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList) {
        List<CouponActivityAutoGiveCouponDetailDTO> autoGiveCouponDetailList = new ArrayList<>();
        for (CouponActivityAutoGiveDetailDTO autoGive : autoGiveDetaiList) {
            List<CouponActivityAutoGiveCouponDetailDTO> couponActivityList = autoGive.getCouponActivityList();
            if(CollUtil.isEmpty(couponActivityList)){
                autoGiveCouponDetailList.addAll(ListUtil.empty());
            } else {
                for (CouponActivityAutoGiveCouponDetailDTO couponActivityAutoGiveCouponDetailDTO : couponActivityList) {
                    couponActivityAutoGiveCouponDetailDTO.setPromotionEid(autoGive.getPromotionEid());
                }
                autoGiveCouponDetailList.addAll(couponActivityList);
            }
        }
        return autoGiveCouponDetailList;
    }

    public void verifiedCouponActivity(long nowTime, List<CouponActivityAutoGiveCouponDetailDTO> autoGiveCouponDetailList,
                                       List<CouponActivityDetailDTO> couponActivityCanGiveList,
                                       Map<Long, CouponActivityDetailDTO> couponActivityCanGiveMap, Map<Long, Integer> giveCountMap) {
        log.info("autoGiveCouponDetailList"+JSON.toJSONString(autoGiveCouponDetailList));
        if (CollUtil.isNotEmpty(autoGiveCouponDetailList)) {
            List<Long> couponActivityIdList = autoGiveCouponDetailList.stream().map(CouponActivityAutoGiveCouponDetailDTO::getCouponActivityId).distinct().collect(Collectors.toList());
            List<CouponActivityDetailDTO> couponActivityList = couponActivityApi.getCouponActivityById(couponActivityIdList);
            log.info("couponActivityList"+JSON.toJSONString(couponActivityList));
            // 已生成券数量
            List<Map<String, Long>> giveCountList = couponApi.getGiveCountByCouponActivityId(couponActivityIdList);
            if (CollUtil.isNotEmpty(giveCountList)) {
                for (Map<String, Long> map : giveCountList) {
                    giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
                }
            }
            // 优惠券活动关联的企业，推广企业的优惠券活动为平台创建、商家类型
            List<Long> effActivityIdList = couponActivityList.stream()
                    .filter(c -> ObjectUtil.equal(c.getEid(), Long.valueOf(0)) && ObjectUtil.equal(CouponActivitySponsorTypeEnum.BUSINESS.getCode(), c.getSponsorType()))
                    .map(CouponActivityDetailDTO::getId).distinct().collect(Collectors.toList());

            // 推广企业id
            Map<Long, Long> activityPromotionEidMap = autoGiveCouponDetailList.stream().filter(item-> Objects.nonNull(item.getPromotionEid())).
                    collect(Collectors.toMap(CouponActivityAutoGiveCouponDetailDTO::getCouponActivityId, CouponActivityAutoGiveCouponDetailDTO::getPromotionEid, (k1, k2) -> k1));

            Iterator<CouponActivityDetailDTO> couponActivityIt = couponActivityList.iterator();
            log.info("couponActivityList2"+JSON.toJSONString(couponActivityList));
            while (couponActivityIt.hasNext()) {
                CouponActivityDetailDTO next = couponActivityIt.next();
                // 非启用
                if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), next.getStatus())) {
                    log.warn("[verifiedCouponActivity]非启用状态:{}", next);
                    couponActivityIt.remove();
                    continue;
                }
                // 已结束
                Integer useDateType = next.getUseDateType();
                if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
                    Date endTime = next.getEndTime();
                    if (endTime.getTime() <= nowTime) {
                        log.warn("[verifiedCouponActivity]已结束:{}", next);
                        couponActivityIt.remove();
                        continue;
                    }
                }
                // 数量
                Integer totalCount = next.getTotalCount();
                if (ObjectUtil.isNull(totalCount) || totalCount < 0) {
                    log.warn("[verifiedCouponActivity]totalCount小于0:{}", next);
                    couponActivityIt.remove();
                    continue;
                }
                Integer count = giveCountMap.get(next.getId());
                if (ObjectUtil.isNull(count) || count < 0) {
                    count = 0;
                }
                if (count >= totalCount) {
                    log.warn("[verifiedCouponActivity]已发数量超出总数，next:{},count：{}，totalCount：{}", next,count,totalCount);
                    couponActivityIt.remove();
                    continue;
                }
                // 是否推广企业的优惠券活动
                Long promotionEid = activityPromotionEidMap.get(next.getId());
                if(ObjectUtil.isNotNull(promotionEid) && !ObjectUtil.equal(promotionEid, Long.valueOf(0))){
                    if(!effActivityIdList.contains(next.getId())){
                        log.warn("[verifiedCouponActivity]非平台创建的商家活动不发，活动id:{},推广企业id：{}", next.getId(),promotionEid);
                        couponActivityIt.remove();
                        continue;
                    }

                }
            }
            couponActivityCanGiveList.addAll(couponActivityList);
        }
        if (CollUtil.isNotEmpty(couponActivityCanGiveList)) {
            couponActivityCanGiveMap.putAll(couponActivityCanGiveList.stream().collect(Collectors.toMap(c -> c.getId(), c -> c, (v1, v2) -> v1)));
        }
        log.info("verifiedCouponActivity校验结果"+ JSON.toJSONString(couponActivityCanGiveList));
    }

    public void handleGive(EnterpriseDTO enterprise, List<BigDecimal> orderTotalAmountList, List<CouponActivityAutoGiveDetailDTO> autoGiveDetaiList,
                           Date date, Map<Long, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordMap,
                           Map<Long, CouponActivityDetailDTO> couponActivityCanGiveMap, Map<Long, Integer> giveCountMap,
                           List<SaveCouponRequest> couponList, List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList,
                           List<Long> updateAutoGiveIdList, List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList, Integer autoGiveType) {
        // 计算单个优惠券对个人的限制。一个人优惠券自动发放活动对单个人可以发放几次的限制
        for (CouponActivityAutoGiveDetailDTO autoGiveDetail : autoGiveDetaiList) {
            log.info("autoGiveDetail"+ JSON.toJSONString(autoGiveDetail));

            // 已自动发放记录
            List<CouponActivityAutoGiveRecordDTO> autoGiveRecords = autoGiveRecordMap.get(autoGiveDetail.getId());
            if (CollUtil.isEmpty(autoGiveRecords)) {
                autoGiveRecords = ListUtil.empty();
            }

            Integer repeatGive = autoGiveDetail.getRepeatGive();
            BigDecimal orderTotalAmount = BigDecimal.ZERO;
            if (CollUtil.isNotEmpty(orderTotalAmountList)) {
                orderTotalAmount = orderTotalAmountList.get(0);
            }

            if (ObjectUtil.equal(CouponActivityRepeatGiveTypeEnum.ONE.getCode(), repeatGive)) {
                // 仅发一次
                if (CollUtil.isNotEmpty(autoGiveRecords)) {
                    continue;
                }
                // 发放
                doAutoGive(enterprise, date, couponActivityCanGiveMap, giveCountMap, autoGiveDetail, couponList, newAutoGiveRecordList,
                    orderTotalAmount, giveDetailList);

                updateAutoGiveIdList.add(autoGiveDetail.getId());
            } else if (ObjectUtil.equal(CouponActivityRepeatGiveTypeEnum.MANY.getCode(), repeatGive)) {
                // 重复发放
                Integer maxGiveNum = autoGiveDetail.getMaxGiveNum();
                if (ObjectUtil.isNull(maxGiveNum) || maxGiveNum <= 0) {
                    continue;
                }
                // 发放次数
                Integer giveCount = autoGiveDetail.getGiveCount();
                if (ObjectUtil.isNull(giveCount) || giveCount <= 0) {
                    giveCount = 0;
                }
                if (giveCount >= maxGiveNum) {
                    continue;
                }
                // 会员自动发券、推广企业自动发券，每月发放1次
                if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode(), autoGiveType)
                    || ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ENTERPRISE_POPULARIZE.getCode(), autoGiveType)) {
                    // 当月已发放
                    long currentMonthCount = autoGiveRecords.stream().filter(r -> isCurrentMonth(date, r.getCreateTime())).count();
                    if(currentMonthCount > 0){
                        continue;
                    }
                    // 自动发放月份
//                    buildAutoGiveByMonth(autoGiveDetail);

                } else if (ObjectUtil.equal(CouponActivityAutoGiveTypeEnum.ORDER_ACCUMULATE_AMOUNT.getCode(), autoGiveType)) {
                    if ( CollUtil.isNotEmpty(autoGiveRecords) && autoGiveRecords.size() >= maxGiveNum) {
                        continue;
                    }
                }

                // 发放
                doAutoGive(enterprise, date, couponActivityCanGiveMap, giveCountMap, autoGiveDetail, couponList, newAutoGiveRecordList,
                    orderTotalAmount, giveDetailList);

                updateAutoGiveIdList.add(autoGiveDetail.getId());
            }
        }
    }

    public void buildAutoGiveByMonth(CouponActivityAutoGiveDetailDTO autoGiveDetail){
        // 待发放月份
        Date beginTime = new Date();
        Date endTime = autoGiveDetail.getEndTime();
        long monthDiff = DateUtil.betweenMonth(beginTime, endTime, false);
        List<String> monthList = new ArrayList<>();
        if(monthDiff <= 1){
            return;
        }
        DateTime dateTime = DateUtil.offsetDay(beginTime, 1);



    }


    public static void main(String[] args) {
        Date beginTime = DateUtil.parse("2021-07-02 10:10:10", "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtil.parse("2021-10-06 10:10:10", "yyyy-MM-dd HH:mm:ss");
        long monthDiff = DateUtil.betweenMonth(beginTime, endTime, false);
        System.out.println(">>>>> monthDiff: " +monthDiff);

        DateTime nextMonth = DateUtil.offsetMonth(beginTime, 1);
        System.out.println(">>>>> nextMonth: " +DateUtil.beginOfMonth(nextMonth));

    }

    private boolean isCurrentMonth(Date nowDate, Date date) {
        String month1 = DateUtil.format(nowDate, "yyyy-MM");
        String month2 = DateUtil.format(date, "yyyy-MM");
        if(ObjectUtil.equal(month1, month2)){
            return true;
        }
        return false;
    }

    private void doAutoGive(EnterpriseDTO enterprise, Date date, Map<Long, CouponActivityDetailDTO> couponActivityCanGiveMap,
                            Map<Long, Integer> giveCountMap, CouponActivityAutoGiveDetailDTO autoGiveDetail, List<SaveCouponRequest> couponList,
                            List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList, BigDecimal orderTotalAmount,
                            List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList) {
        List<CouponActivityAutoGiveCouponDetailDTO> couponActivityList = autoGiveDetail.getCouponActivityList();
        for (CouponActivityAutoGiveCouponDetailDTO autoGiveCouponDetail : couponActivityList) {
            // 可发放优惠券活动
            CouponActivityDetailDTO couponActivityDetail = couponActivityCanGiveMap.get(autoGiveCouponDetail.getCouponActivityId());
            if (ObjectUtil.isNull(couponActivityDetail)) {
                continue;
            }
            // 已生成优惠券数量
            Integer totalCount = couponActivityDetail.getTotalCount();
            if (ObjectUtil.isNull(totalCount) || totalCount < 0) {
                continue;
            }
            Integer count = giveCountMap.get(autoGiveCouponDetail.getCouponActivityId());
            if (ObjectUtil.isNull(count) || count < 0) {
                count = 0;
            }
            Integer canGiveNum = autoGiveCouponDetail.getGiveNum();
            if (ObjectUtil.isNull(canGiveNum) || canGiveNum <= 0) {
                continue;
            }

            int surplusCount = totalCount - count;
            if (surplusCount <= 0) {
                continue;
            }
            canGiveNum = canGiveNum > surplusCount ? surplusCount : canGiveNum;

            Long autoGiveId = autoGiveDetail.getId();
            String autoGiveName = autoGiveDetail.getName();
            Long couponActivityId = couponActivityDetail.getId();
            String couponActivityName = couponActivityDetail.getName();
            Long eid = enterprise.getId();
            String ename = enterprise.getName();

            // 根据优惠券有效期类型设置时间
            Integer useDateType = couponActivityDetail.getUseDateType();
            Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, couponActivityDetail.getBeginTime(), couponActivityDetail.getEndTime(),
                    couponActivityDetail.getUseDateType(), couponActivityDetail.getExpiryDays());
            Date beginTime = timeMap.get("beginTime");
            Date endTime = timeMap.get("endTime");

            // 生成优惠券
            for (int i = 0; i < canGiveNum; i++) {
                SaveCouponRequest coupon = buildSaveCouponRequest(date, autoGiveId, autoGiveName, couponActivityId, couponActivityName, eid, ename,
                    beginTime, endTime);
                couponList.add(coupon);
            }

            // 优惠券发放记录，状态 待发放，发放成功后更新为发放成功
            CouponActivityAutoGiveRecordDTO newRecord = buildCouponActivityAutoGiveRecordDTO(date, orderTotalAmount, couponActivityDetail, canGiveNum,
                autoGiveId, autoGiveName, couponActivityId, couponActivityName, eid, ename);
            newAutoGiveRecordList.add(newRecord);
            log.info("doAutoGive"+ JSON.toJSONString(newAutoGiveRecordList));
            // 发放企业信息
//            SaveCouponActivityGiveEnterpriseInfoDetailRequest infoRequest = buildGiveEnterpriseInfoDetailRequest(enterprise, date,
//                couponActivityDetail, canGiveNum, couponActivityId, orderTotalAmount);
//            giveDetailList.add(infoRequest);
        }
    }

    private SaveCouponActivityGiveEnterpriseInfoDetailRequest buildGiveEnterpriseInfoDetailRequest(EnterpriseDTO enterprise, Date date,
                                                                                                   CouponActivityDetailDTO couponActivityDetail,
                                                                                                   int canGiveNum, Long couponActivityId,
                                                                                                   BigDecimal orderTotalAmount) {
        SaveCouponActivityGiveEnterpriseInfoDetailRequest infoRequest = new SaveCouponActivityGiveEnterpriseInfoDetailRequest();
        infoRequest.setCouponActivityId(couponActivityId);
        infoRequest.setEid(enterprise.getId());
        infoRequest.setEname(enterprise.getName());
        infoRequest.setEtype(enterprise.getType());
        infoRequest.setRegionCode(enterprise.getProvinceCode());
        infoRequest.setRegionName(enterprise.getProvinceName());
        infoRequest.setAuthStatus(enterprise.getAuthStatus());
        infoRequest.setGiveNum(canGiveNum);
        infoRequest.setOwnEid(couponActivityDetail.getEid());
        infoRequest.setOwnEname(couponActivityDetail.getEname());
        infoRequest.setCreateUser(0L);
        infoRequest.setCreateTime(date);
        infoRequest.setCumulativeAmount(orderTotalAmount);
        return infoRequest;
    }

    private CouponActivityAutoGiveRecordDTO buildCouponActivityAutoGiveRecordDTO(Date date, BigDecimal orderTotalAmount,
                                                                                 CouponActivityDetailDTO couponActivityDetail, int canGiveNum,
                                                                                 Long autoGiveId, String autoGiveName, Long couponActivityId,
                                                                                 String couponActivityName, Long eid, String ename) {
        CouponActivityAutoGiveRecordDTO newRecord = new CouponActivityAutoGiveRecordDTO();
        newRecord.setCouponActivityAutoGiveId(autoGiveId);
        newRecord.setCouponActivityAutoGiveName(autoGiveName);
        newRecord.setCouponActivityId(couponActivityId);
        newRecord.setCouponActivityName(couponActivityName);
        newRecord.setEid(eid);
        newRecord.setEname(ename);
        newRecord.setGiveNum(canGiveNum);
        newRecord.setCumulativeAmount(orderTotalAmount);
        newRecord.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        newRecord.setStatus(CouponActivityGiveRecordStatusEnum.WAIT.getCode());
        newRecord.setCreateTime(date);
        newRecord.setOwnEid(couponActivityDetail.getEid());
        newRecord.setOwnEname(couponActivityDetail.getEname());
        newRecord.setBatchNumber(CouponUtil.getMillisecondTime());
        return newRecord;
    }

    private SaveCouponRequest buildSaveCouponRequest(Date date, Long autoGiveId, String autoGiveName, Long couponActivityId,
                                                     String couponActivityName, Long eid, String ename, Date beginTime, Date endTime) {
        SaveCouponRequest coupon = new SaveCouponRequest();
        coupon.setCouponActivityAutoId(autoGiveId);
        coupon.setCouponActivityAutoName(autoGiveName);
        coupon.setCouponActivityId(couponActivityId);
        coupon.setCouponActivityName(couponActivityName);
        coupon.setEid(eid);
        coupon.setEname(ename);
        coupon.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        coupon.setGetTime(date);
        coupon.setBeginTime(beginTime);
        coupon.setEndTime(endTime);
        coupon.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
        coupon.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());
        coupon.setCreateTime(date);
        return coupon;
    }

    /**
     * 执行发放
     * @param date
     * @param couponList
     * @param newAutoGiveRecordList
     * @param updateAutoGiveIdList
     * @param giveDetailList
     * @param isMember 是否会员自动发放
     * @return
     */
    @GlobalTransactional
    public boolean couponExecute(Date date, List<SaveCouponRequest> couponList, List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList,
                                 List<Long> updateAutoGiveIdList, List<SaveCouponActivityGiveEnterpriseInfoDetailRequest> giveDetailList, boolean isMember) {
        boolean isSuccess = false;
        log.info("开始发券添加数据库");
        if(CollectionUtils.isEmpty(newAutoGiveRecordList)){
            log.warn("员自动发放发券没有可以发放的券");
            return true;
        }
        if (CollUtil.isNotEmpty(newAutoGiveRecordList)) {
            log.info("newAutoGiveRecordList"+ JSON.toJSONString(newAutoGiveRecordList));
            // 生成发放记录，状态待发放
            List<CouponActivityAutoGiveRecordDTO> autoGiveRecordList = autoGiveApi.saveAutoGiveRecordWithWaitStatus(newAutoGiveRecordList);
            // 生成优惠券
            if (CollUtil.isNotEmpty(autoGiveRecordList)) {
                isSuccess = couponApi.insertBatch(couponList);
            }
            // 发放记录,更新状态
            if (isSuccess) {
                List<UpdateAutoGiveRecordStatusRequest> updateRecordStatusList = new ArrayList<>();
                buildUpdateRecordStatusList(autoGiveRecordList, updateRecordStatusList);
                isSuccess = autoGiveApi.updateRecordStatus(updateRecordStatusList);
            }
            // 发放成功，更新发放活动的已发放次数
//            if (isSuccess) {
//                UpdateAutoGiveCountRequest updateAutoGiveCountRequest = new UpdateAutoGiveCountRequest();
//                updateAutoGiveCountRequest.setIds(updateAutoGiveIdList);
//                updateAutoGiveCountRequest.setOpTime(date);
//                updateAutoGiveCountRequest.setOpUserId(0L);
//                isSuccess = autoGiveApi.updateGiveCountByIdList(updateAutoGiveCountRequest);
//            }
//            if (isSuccess && isMember) {
//                // 会员每月自动发放插入待执行数据
//            }
        }
        return isSuccess;
    }

    private void buildUpdateRecordStatusList(List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList,
                                             List<UpdateAutoGiveRecordStatusRequest> updateRecordStatusList) {
        if (CollUtil.isEmpty(newAutoGiveRecordList)) {
            return;
        }
        UpdateAutoGiveRecordStatusRequest recordStatusRequest;
        for (CouponActivityAutoGiveRecordDTO record : newAutoGiveRecordList) {
            recordStatusRequest = new UpdateAutoGiveRecordStatusRequest();
            recordStatusRequest.setId(record.getId());
            recordStatusRequest.setStatus(CouponActivityGiveRecordStatusEnum.SUCCESS.getCode());
            updateRecordStatusList.add(recordStatusRequest);
        }
    }

}
