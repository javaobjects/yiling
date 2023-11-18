package com.yiling.mall.coupon.handler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.mall.coupon.bo.AutoGiveCouponActivityBO;
import com.yiling.marketing.common.enums.ActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityAutoGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityGiveRecordStatusEnum;
import com.yiling.marketing.common.enums.CouponActivityPromotionCodeEnum;
import com.yiling.marketing.common.enums.CouponActivityRepeatGiveTypeEnum;
import com.yiling.marketing.common.enums.CouponActivityStatusEnum;
import com.yiling.marketing.common.enums.CouponErrorCode;
import com.yiling.marketing.common.enums.CouponGetEnterpriseLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.common.enums.CouponLimitTypeEnum;
import com.yiling.marketing.common.enums.CouponStatusEnum;
import com.yiling.marketing.common.enums.CouponUsedStatusEnum;
import com.yiling.marketing.common.util.CouponUtil;
import com.yiling.marketing.coupon.api.CouponApi;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDTO;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;
import com.yiling.marketing.couponactivityautogive.api.CouponActivityAutoGiveApi;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveCouponDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveEnterpriseLimitDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGivePageDTO;
import com.yiling.marketing.couponactivityautogive.dto.CouponActivityAutoGiveRecordDTO;
import com.yiling.marketing.couponactivityautogive.dto.request.QueryCouponActivityAutoGiveRequest;
import com.yiling.marketing.couponactivityautogive.dto.request.UpdateAutoGiveRecordStatusRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 会员每月自动发券
 *
 * @author: houjie.sun
 * @date: 2021/12/1
 */
@Slf4j
@Service
public class CouponAutoGiveByMemberMonthHandler {

    @DubboReference
    private CouponActivityApi couponActivityApi;
    @DubboReference
    private CouponApi couponApi;
    @DubboReference
    private CouponActivityAutoGiveApi autoGiveApi;
    @DubboReference
    private EnterpriseMemberApi enterpriseMemberApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${coupon.memberAutoGive.byMonthTime:yyyy-MM-01}")
    private String couponMemberAutoGiveByMonthTime;

    public static final String MEMBER_GIVE_TASK = "member_give_task_month";
//    public static final String COUPON_MEMBER_AUTO_GIVE_BY_MONTH_TIME = "yyyy-MM-01";
    public static final String COUPON_REMARK = "member_auto_give_by_month";
    public static final String ERROR_MSG_PREFIX = "[自动发券-会员每月发放], couponAutoGiveMonthByMemberHandler, ";

    private String checkCouponActivity(CouponActivityDTO couponActivity, List<Integer> countList, List<Map<String, Long>> giveCountList) {
        if (!ObjectUtil.equal(CouponActivityStatusEnum.ENABLED.getCode(), couponActivity.getStatus())) {
            return "此优惠券活动不是“启用”状态，[" + couponActivity.getId() + "]";
        }
        Long id = couponActivity.getId();
        Date endTime = couponActivity.getEndTime();
        long nowTime = System.currentTimeMillis();
        Integer useDateType = couponActivity.getUseDateType();
        // 固定时间
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
            if (endTime.getTime() <= nowTime) {
                return "此优惠券活动已结束，[" + couponActivity.getId() + "]";
            }
        }

        // 已生成券数量
        Map<Long, Integer> giveCountMap = new HashMap<>();
        if (CollUtil.isNotEmpty(giveCountList)) {
            for (Map<String, Long> map : giveCountList) {
                giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
            }
        }
        Integer totalCount = couponActivity.getTotalCount();
        Integer count = giveCountMap.get(id);
        if (ObjectUtil.isNull(count) || count < 0) {
            count = 0;
        }
        countList.add(count);
        if (count >= totalCount) {
            return MessageFormat.format(CouponErrorCode.COUPON_ACTIVITY_TOTAL_COUNT_ERROR.getMessage(), couponActivity.getId(),
                    couponActivity.getTotalCount());
        }
        return null;
    }

    /**
     * 会员自动发放，每月发放
     *
     * @return
     */
    public Boolean memberAutoGiveByMonth() {

        log.info(ERROR_MSG_PREFIX.concat("开始执行任务"));

        int size = 500;
        Date date = new Date();

        // 查询所有会员
        List<EnterpriseMemberBO> memberList = new ArrayList<>();
        Page<EnterpriseMemberBO> memberPage;
        QueryEnterpriseMemberRequest memberRequest = new QueryEnterpriseMemberRequest();
        int memberCurrent = 1;
        do {

            memberRequest.setCurrent(memberCurrent);
            memberRequest.setSize(size);
            memberPage = enterpriseMemberApi.queryEnterpriseMemberPage(memberRequest);
            if (ObjectUtil.isNull(memberPage) || CollUtil.isEmpty(memberPage.getRecords())) {
                break;
            }
            memberList.addAll(memberPage.getRecords());
            memberCurrent++;
        } while (ObjectUtil.isNotNull(memberPage) && CollUtil.isNotEmpty(memberPage.getRecords()));
        if (CollUtil.isEmpty(memberList)) {
            log.warn(ERROR_MSG_PREFIX.concat("会员信息不存在"));
            return true;
        }

        // 查询会员的企业信息
        List<Long> eidList = memberList.stream().map(EnterpriseMemberBO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);
        if (CollUtil.isEmpty(enterpriseList)) {
            log.warn(ERROR_MSG_PREFIX.concat("会员对应的企业信息不存在"));
            return true;
        }
        Map<Long, EnterpriseDTO> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e, (v1, v2) -> v1));
        // 企业信息正常的会员
        List<Long> enterpriseIdList = enterpriseList.stream().map(EnterpriseDTO::getId).distinct().collect(Collectors.toList());
        memberList = memberList.stream().filter(m -> enterpriseIdList.contains(m.getEid())).collect(Collectors.toList());
        if (CollUtil.isEmpty(memberList)) {
            log.warn(ERROR_MSG_PREFIX.concat("会员对应的企业信息已失效或不存在"));
            return true;
        }

        // 查询自动发放活动，会员自动发放、每月发放、已开始、未结束、没有推广码的、有关联优惠券活动
        Page<CouponActivityAutoGivePageDTO> page;
        QueryCouponActivityAutoGiveRequest request = new QueryCouponActivityAutoGiveRequest();
        request.setStatus(CouponActivityStatusEnum.ENABLED.getCode());
        request.setType(CouponActivityAutoGiveTypeEnum.MEMBER_AUTO.getCode());
        request.setRepeatGive(CouponActivityRepeatGiveTypeEnum.MANY.getCode());
        request.setActivityStatus(ActivityStatusEnum.RUNNING.getCode());
        request.setConditionPromotionCode(CouponActivityPromotionCodeEnum.ALL.getCode());

        int autoGiveCurrent = 1;
        do {
            request.setCurrent(autoGiveCurrent);
            request.setSize(size);
            page = autoGiveApi.queryListPage(request);
            if (ObjectUtil.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
                if(autoGiveCurrent == 1){
                    log.warn(ERROR_MSG_PREFIX.concat("自动发放活动不存在（已开启、会员自动发券、每月发放、进行中、推广码为不使用或否）"));
                }
                break;
            }
            List<CouponActivityAutoGivePageDTO> autoGiveList = page.getRecords();
            List<Long> autoGiveIdList = autoGiveList.stream().map(CouponActivityAutoGivePageDTO::getId).distinct().collect(Collectors.toList());

            // 已发放记录
            Map<Long, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordMap = new HashMap<>();
            // 可发放活动
            List<Long> canGiveAutoIdList = new ArrayList<>();
            List<CouponActivityAutoGiveRecordDTO> autoGiveRecordList = autoGiveApi.getRecordListByAutoGiveIds(autoGiveIdList);
            if (CollUtil.isNotEmpty(autoGiveRecordList)) {
                autoGiveRecordMap = autoGiveRecordList.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveRecordDTO::getCouponActivityAutoGiveId));
                // 1号没有发放记录的才能发放
                String nowDate = DateUtil.format(date, couponMemberAutoGiveByMonthTime);
                List<Long> givedRecordList = autoGiveRecordList.stream().filter(r -> ObjectUtil.equal(nowDate, DateUtil.format(r.getCreateTime(), "yyyy-MM-dd"))).map(CouponActivityAutoGiveRecordDTO::getCouponActivityAutoGiveId).distinct().collect(Collectors.toList());
                if (CollUtil.isNotEmpty(givedRecordList)) {
                    canGiveAutoIdList = autoGiveIdList.stream().filter(autoGiveId -> !givedRecordList.contains(autoGiveId)).distinct().collect(Collectors.toList());
                } else {
                    canGiveAutoIdList = autoGiveIdList;
                }
            } else {
                canGiveAutoIdList = autoGiveIdList;
            }

            // 发放活动指定部分会员的
            List<Long> partMemberAutoGiveIdList = autoGiveList.stream().filter(a -> ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), a.getConditionUserType())).map(CouponActivityAutoGivePageDTO::getId).distinct().collect(Collectors.toList());
            Map<Long, List<CouponActivityAutoGiveEnterpriseLimitDTO>> memberLimitMap = new HashMap<>();
            if (CollUtil.isNotEmpty(partMemberAutoGiveIdList)) {
                List<CouponActivityAutoGiveEnterpriseLimitDTO> memberLimitList = autoGiveApi.getEnterpriseLimitByAutoGiveIdList(partMemberAutoGiveIdList);
                memberLimitMap = memberLimitList.stream().collect(Collectors.groupingBy(CouponActivityAutoGiveEnterpriseLimitDTO::getCouponActivityAutoGiveId));
            }

            // 关联的优惠券活动
            List<CouponActivityAutoGiveCouponDTO> autoGiveCouponList = autoGiveApi.getAutoGiveCouponByAutoGiveIdList(autoGiveIdList);
            if (CollUtil.isEmpty(autoGiveCouponList)) {
                log.warn(ERROR_MSG_PREFIX.concat("自动发放活动").concat(autoGiveIdList.toString()).concat("没有关联的优惠券活动"));
                continue;
            }
            Map<Long, List<CouponActivityAutoGiveCouponDTO>> autoGiveCouponMap = autoGiveCouponList.stream().collect(Collectors.groupingBy(r -> r.getCouponActivityAutoGiveId()));

            List<Long> couponActivityIdList = autoGiveCouponList.stream().filter(c -> ObjectUtil.isNotNull(c.getCouponActivityId())).map(CouponActivityAutoGiveCouponDTO::getCouponActivityId).distinct().collect(Collectors.toList());
            if (CollUtil.isEmpty(couponActivityIdList)) {
                log.warn(ERROR_MSG_PREFIX.concat("自动发放活动").concat(autoGiveIdList.toString()).concat("关联的优惠券活动id字段为空"));
                continue;
            }
            List<CouponActivityDTO> couponActivityList = couponActivityApi.getEffectiveCouponActivityByIdList(couponActivityIdList, 0, 0);
            if (CollUtil.isEmpty(couponActivityList)) {
                log.warn(ERROR_MSG_PREFIX.concat("自动发放活动").concat(autoGiveIdList.toString()).concat("关联的优惠券活动id字段为空"));
                continue;
            }
            Map<Long, CouponActivityDTO> couponActivityMap = couponActivityList.stream().collect(Collectors.toMap(CouponActivityDTO::getId, c -> c, (v1,v2) -> v1));

            // 发放批次号
            String patchNo = CouponUtil.getMillisecondTime();

            for (CouponActivityAutoGivePageDTO autoGive : autoGiveList) {
                if (!canGiveAutoIdList.contains(autoGive.getId())) {
                    log.warn(ERROR_MSG_PREFIX.concat(autoGiveIdList.toString()).concat("可发放活动为空").concat("发放活动id："+autoGive.getId()));
                    continue;
                }

                // 会员设置类型
                Integer conditionUserType = autoGive.getConditionUserType();
                // 会员企业类型
                List<EnterpriseMemberBO> tempMemberList = new ArrayList<>();
                // 指定部分会员的
                if (ObjectUtil.equal(CouponGetEnterpriseLimitTypeEnum.PART_MEMBER.getCode(), conditionUserType)) {
                    tempMemberList = getEnterpriseMemberList(memberList, autoGive.getId(), memberLimitMap);
                    if (CollUtil.isEmpty(tempMemberList)) {
                        log.warn(ERROR_MSG_PREFIX.concat("可发放会员为空").concat("发放活动id："+autoGive.getId()));
                        continue;
                    }
                } else {
                    tempMemberList = memberList;
                }
                // 企业类型，设置部分企业类型可用的
                List<EnterpriseMemberBO> enterpriseMemberList = new ArrayList<>();
                if (ObjectUtil.equal(CouponLimitTypeEnum.PART.getCode(), autoGive.getConditionEnterpriseType())) {
                    String conditionEnterpriseTypeValue = autoGive.getConditionEnterpriseTypeValue();
                    if (StrUtil.isBlank(conditionEnterpriseTypeValue)) {
                        log.warn(ERROR_MSG_PREFIX.concat("可发放企业类型为空1").concat("发放活动id："+autoGive.getId()));
                        continue;
                    }
                    List<String> enterpriseTypeSelectedList = StrUtil.isBlank(conditionEnterpriseTypeValue) ? ListUtil.empty() : Arrays.asList(conditionEnterpriseTypeValue.split(","));
                    if (CollUtil.isEmpty(enterpriseTypeSelectedList)) {
                        log.warn(ERROR_MSG_PREFIX.concat("可发放企业类型为空2").concat("发放活动id："+autoGive.getId()));
                        continue;
                    }
                    for (EnterpriseMemberBO enterprise : tempMemberList) {
                        EnterpriseDTO enterpriseDTO = enterpriseMap.get(enterprise.getEid());
                        if (ObjectUtil.isNull(enterpriseDTO)) {
                            log.warn(ERROR_MSG_PREFIX.concat("可发放企业为空1").concat("发放活动id："+autoGive.getId()));
                            continue;
                        }
                        if (enterpriseTypeSelectedList.contains(enterpriseDTO.getType().toString())) {
                            enterpriseMemberList.add(enterprise);
                        }
                    }
                } else {
                    enterpriseMemberList.addAll(tempMemberList);
                }
                if(CollUtil.isEmpty(enterpriseMemberList)){
                    log.warn(ERROR_MSG_PREFIX.concat("可发放企业为空2").concat("发放活动id："+autoGive.getId()));
                    continue;
                }

                // 共计发放月数校验
                checkAutoGiveMonthSum(autoGiveRecordMap, autoGive, enterpriseMemberList);
                if(CollUtil.isEmpty(enterpriseMemberList)){
                    log.warn(ERROR_MSG_PREFIX.concat("可发放企业为空3").concat("发放活动id："+autoGive.getId()));
                    continue;
                }

                // 可发放的优惠券活动
                List<CouponActivityAutoGiveCouponDTO> autoGiveCouponDOS = autoGiveCouponMap.get(autoGive.getId());
                if (CollUtil.isEmpty(autoGiveCouponDOS)) {
                    log.warn(ERROR_MSG_PREFIX.concat("可发放的优惠券活动为空").concat("发放活动id："+autoGive.getId()));
                    continue;
                }
                // 自动发放关联的优惠券活动设置
                Map<Long, CouponActivityAutoGiveCouponDTO> couponAutoGiveMap = new HashMap<>();
                Map<Long, CouponActivityAutoGiveCouponDTO> couponAutoGiveMapTemp = autoGiveCouponDOS.stream().collect(Collectors.toMap(a -> a.getCouponActivityId(), a -> a, (v1, v2) -> v1));
                couponAutoGiveMap.putAll(couponAutoGiveMapTemp);

                // 发放列表
                List<AutoGiveCouponActivityBO> memberAutoGiveBoList = new ArrayList<>();

                for (CouponActivityAutoGiveCouponDTO autoGiveCouponDO : autoGiveCouponDOS) {
                    CouponActivityDTO couponActivity = couponActivityMap.get(autoGiveCouponDO.getCouponActivityId());
                    if(ObjectUtil.isNull(couponActivity)){
                        log.warn(ERROR_MSG_PREFIX.concat("可发放的优惠券活动为空").concat("发放活动id："+autoGive.getId()));
                        continue;
                    }

                    /* 优惠券活动校验 */
                    // 优惠券活动已发放数量
                    List<Map<String, Long>> giveCountList = couponApi.getGiveCountByCouponActivityId(couponActivityIdList);
                    // 优惠券活动校验
                        List<Integer> countList = new ArrayList<>();
//                    String couponActivityErrorMsg = couponActivityApi.checkCouponActivity(couponActivity, countList, giveCountList);
                     String couponActivityErrorMsg = checkCouponActivity(couponActivity, countList, giveCountList);
                        if (StrUtil.isNotBlank(couponActivityErrorMsg)) {
                            log.warn(ERROR_MSG_PREFIX.concat("自动发放活动").concat(couponActivityErrorMsg).concat("发放活动id："+autoGive.getId()));
                            continue;
                        }

                    Integer totalCount = couponActivity.getTotalCount();
                    if (ObjectUtil.isNull(totalCount) || totalCount.intValue() <= 0) {
                        log.warn(ERROR_MSG_PREFIX.concat("自动发放活动").concat("优惠券活动总数量不能为0").concat("发放活动id："+autoGive.getId()));
                        continue;
                    }
                    Integer haveGiveCount = countList.get(0);
                    if (ObjectUtil.isNotNull(haveGiveCount) && haveGiveCount >= totalCount) {
                        log.warn(ERROR_MSG_PREFIX.concat("自动发放活动达到优惠券活动总数量限制").concat("发放活动id："+autoGive.getId()));
                        continue;
                    }

                    AutoGiveCouponActivityBO bo = new AutoGiveCouponActivityBO();
                    BeanUtil.copyProperties(couponActivity, bo);
                    bo.setCouponActivityId(couponActivity.getId());
                    bo.setAutoGiveId(autoGive.getId());
                    bo.setAutoGiveName(autoGive.getName());
                    bo.setAutoGiveType(autoGive.getType());
                    bo.setAutoGiveStatus(autoGive.getStatus());
                    bo.setAutoGiveBeginTime(autoGive.getBeginTime());
                    bo.setAutoGiveEndTime(autoGive.getEndTime());
                    bo.setCumulative(autoGive.getCumulative());
                    bo.setMaxGiveNum(autoGive.getMaxGiveNum());
                    memberAutoGiveBoList.add(bo);
                }
                // 发放处理
                giveHandler(memberAutoGiveBoList, couponAutoGiveMap, enterpriseMemberList, date, patchNo);
            }

            autoGiveCurrent++;
        } while (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords()));

        return true;
    }

    private void checkAutoGiveMonthSum(Map<Long, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordMap,
                                       CouponActivityAutoGivePageDTO autoGive, List<EnterpriseMemberBO> enterpriseMemberList) {
        Date beginTime = autoGive.getBeginTime();
        Date endTime = autoGive.getEndTime();
        long monthDiff = DateUtil.betweenMonth(beginTime, endTime, false);
        // 已发放记录
        List<CouponActivityAutoGiveRecordDTO> autoGiveRecords = autoGiveRecordMap.get(autoGive.getId());
        if (CollUtil.isNotEmpty(autoGiveRecords)){
            Date date = new Date();

            Map<String, List<CouponActivityAutoGiveRecordDTO>> autoGiveRecordByEidMap =
                    autoGiveRecords.stream()
                    .filter(r -> ObjectUtil.equal(COUPON_REMARK, r.getRemark()))
                    .collect(Collectors.groupingBy(r -> getKey(r.getEid(), r.getCreateTime())));
            if(MapUtil.isNotEmpty(autoGiveRecordByEidMap)){
                Iterator<EnterpriseMemberBO> iterator = enterpriseMemberList.iterator();
                while (iterator.hasNext()) {
                    EnterpriseMemberBO enterpriseMember = iterator.next();
                    List<CouponActivityAutoGiveRecordDTO> autoGiveRecordList = autoGiveRecordByEidMap.get(getKey(enterpriseMember.getEid(), date));
                    if(CollUtil.isEmpty(autoGiveRecordList)){
                        continue;
                    }
                    Map<String, CouponActivityAutoGiveRecordDTO> recordMap = autoGiveRecordList.stream().collect(Collectors.toMap(r -> getKey(r.getEid(), r.getCreateTime()), r -> r, (k1, k2) -> k1));
                    if (recordMap.size() >= monthDiff) {
                        log.warn(ERROR_MSG_PREFIX.concat("自动发放活动发放月份已达到"));
                        iterator.remove();
                    }
                }
            }
        }
    }

    private String getKey(Long eid, Date date){
        return eid.toString().concat("_").concat(DateUtil.format(date, "yyyy-MM-dd"));
    }

    @GlobalTransactional
    private void giveExecute(List<SaveCouponRequest> couponList, List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList) {
        if (CollUtil.isNotEmpty(couponList) && CollUtil.isNotEmpty(newAutoGiveRecordList)) {
            List<SaveCouponRequest> saveCouponList = new ArrayList<>();
            // 按发放数量生成优惠券
            SaveCouponRequest saveCouponRequest;
            for (SaveCouponRequest couponRequest : couponList) {
                if (couponRequest.getExpectGiveNumber() > 1) {
                    for (int i = 0; i < couponRequest.getExpectGiveNumber(); i++) {
                        saveCouponRequest = new SaveCouponRequest();
                        BeanUtil.copyProperties(couponRequest, saveCouponRequest);
                        saveCouponRequest.setExpectGiveNumber(1);
                        saveCouponList.add(saveCouponRequest);
                    }
                } else {
                    saveCouponList.add(couponRequest);
                }
            }
            // 生成发放记录，状态待发放
            List<CouponActivityAutoGiveRecordDTO> autoGiveRecordList = autoGiveApi.saveAutoGiveRecordWithWaitStatus(newAutoGiveRecordList);
            // 生成优惠券
            couponApi.insertBatch(saveCouponList);
            // 发放记录,更新状态
            List<UpdateAutoGiveRecordStatusRequest> updateRecordStatusList = new ArrayList<>();
            buildUpdateRecordStatusList(autoGiveRecordList, updateRecordStatusList);
            autoGiveApi.updateRecordStatus(updateRecordStatusList);
        }
    }

    private void buildUpdateRecordStatusList(List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList, List<UpdateAutoGiveRecordStatusRequest> updateRecordStatusList) {
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

    private void giveHandler(List<AutoGiveCouponActivityBO> autoGiveCouponActivityBOList, Map<Long, CouponActivityAutoGiveCouponDTO> couponAutoGiveMap,
                             List<EnterpriseMemberBO> enterpriseMemberList, Date date, String patchNo) {
        for (AutoGiveCouponActivityBO autoGiveCouponActivityBO : autoGiveCouponActivityBOList) {
            Integer totalCount = autoGiveCouponActivityBO.getTotalCount();
            // 自动发放活动设置发放数量
            CouponActivityAutoGiveCouponDTO autoGiveCouponDO = couponAutoGiveMap.get(autoGiveCouponActivityBO.getCouponActivityId());
            if (ObjectUtil.isNull(autoGiveCouponDO)) {
                return;
            }
            int maxGiveNum = autoGiveCouponDO.getGiveNum();

            // 发放优惠券
            List<SaveCouponRequest> couponList = new ArrayList<>();
            // 发放记录
            List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList = new ArrayList<>();
            // 为会员发放
            for (EnterpriseMemberBO enterpriseMemberDTO : enterpriseMemberList) {
                // 优惠券活动已发放数量
                List<Map<String, Long>> giveCountList = couponApi.getGiveCountByCouponActivityId(new ArrayList(){{add(autoGiveCouponActivityBO.getCouponActivityId());}});
                // 已生成券数量
                Map<Long, Integer> giveCountMap = new HashMap<>();
                if (CollUtil.isNotEmpty(giveCountList)) {
                    for (Map<String, Long> map : giveCountList) {
                        giveCountMap.put(map.get("couponActivityId").longValue(), map.get("giveCount").intValue());
                    }
                }
                Integer haveGiveCount = giveCountMap.get(autoGiveCouponActivityBO.getCouponActivityId());
                if (ObjectUtil.isNull(haveGiveCount) || haveGiveCount < 0) {
                    haveGiveCount = 0;
                }
                // 优惠券发放剩余数量
                int surplusNum = totalCount - haveGiveCount;
                if (surplusNum <= 0) {
                    log.warn(ERROR_MSG_PREFIX.concat("自动发放活动id:").concat(autoGiveCouponActivityBO.getAutoGiveId()+", ")
                            .concat("优惠券活动id:").concat(autoGiveCouponActivityBO.getCouponActivityId()+"").concat("优惠券活动剩余发放数量为0"));
                    continue;
                }
                // 可发放优惠券数量
                int canGiveNum = surplusNum >= maxGiveNum ? maxGiveNum : surplusNum;
                /* 组装发放数据 */
                // 优惠券
                buildMemberCoupon(couponList, autoGiveCouponActivityBO, enterpriseMemberDTO, date, canGiveNum);
                // 优惠券发放记录
                buildMemberCouponRecord(newAutoGiveRecordList, autoGiveCouponActivityBO, enterpriseMemberDTO, date, patchNo, canGiveNum);
            }

            // 执行发放
            try {
                giveExecute(couponList, newAutoGiveRecordList);
            } catch (Exception e){
                log.error(ERROR_MSG_PREFIX.concat(", 保存数据异常"));
                e.printStackTrace();
            }
        }
    }

    private List<EnterpriseMemberBO> getEnterpriseMemberList(List<EnterpriseMemberBO> memberList, Long autoGiveId,
                                                              Map<Long, List<CouponActivityAutoGiveEnterpriseLimitDTO>> memberLimitMap) {
        List<CouponActivityAutoGiveEnterpriseLimitDTO> autoGiveMemberLimitList = memberLimitMap.get(autoGiveId);
        if (CollUtil.isEmpty(autoGiveMemberLimitList)) {
            return null;
        }
        List<Long> memberEidList = autoGiveMemberLimitList.stream().map(CouponActivityAutoGiveEnterpriseLimitDTO::getEid).distinct().collect(Collectors.toList());
        List<EnterpriseMemberBO> partMemberList = memberList.stream().filter(m -> memberEidList.contains(m.getEid())).collect(Collectors.toList());
        return partMemberList;
    }

    private void buildMemberCouponRecord(List<CouponActivityAutoGiveRecordDTO> newAutoGiveRecordList, AutoGiveCouponActivityBO autoGiveCouponActivityBO, EnterpriseMemberBO enterpriseMemberDTO, Date date, String patchNo, int canGiveNum) {
        CouponActivityAutoGiveRecordDTO newRecord = new CouponActivityAutoGiveRecordDTO();
        newRecord.setCouponActivityAutoGiveId(autoGiveCouponActivityBO.getAutoGiveId());
        newRecord.setCouponActivityAutoGiveName(autoGiveCouponActivityBO.getAutoGiveName());
        newRecord.setCouponActivityId(autoGiveCouponActivityBO.getCouponActivityId());
        newRecord.setCouponActivityName(autoGiveCouponActivityBO.getName());
        newRecord.setEid(enterpriseMemberDTO.getEid());
        newRecord.setEname(enterpriseMemberDTO.getEname());
        newRecord.setGiveNum(canGiveNum);
        newRecord.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        newRecord.setStatus(CouponActivityGiveRecordStatusEnum.WAIT.getCode());
        newRecord.setCreateTime(date);
        newRecord.setOwnEid(autoGiveCouponActivityBO.getEid());
        newRecord.setOwnEname(autoGiveCouponActivityBO.getEname());
        newRecord.setBatchNumber(patchNo);
        newRecord.setRemark(COUPON_REMARK);
        newAutoGiveRecordList.add(newRecord);
    }

    private void buildMemberCoupon(List<SaveCouponRequest> couponList, AutoGiveCouponActivityBO autoGiveCouponActivityBO, EnterpriseMemberBO enterpriseMemberDTO, Date date, int canGiveNum) {
        Map<String, Date> timeMap = CouponUtil.buildCouponBeginEndTime(date, autoGiveCouponActivityBO.getBeginTime(), autoGiveCouponActivityBO.getEndTime(), autoGiveCouponActivityBO.getUseDateType(), autoGiveCouponActivityBO.getExpiryDays());
        Date beginTime = timeMap.get("beginTime");
        Date endTime = timeMap.get("endTime");

        SaveCouponRequest coupon = new SaveCouponRequest();
        coupon.setCouponActivityAutoId(autoGiveCouponActivityBO.getAutoGiveId());
        coupon.setCouponActivityAutoName(autoGiveCouponActivityBO.getAutoGiveName());
        coupon.setCouponActivityId(autoGiveCouponActivityBO.getCouponActivityId());
        coupon.setCouponActivityName(autoGiveCouponActivityBO.getName());
        coupon.setEid(enterpriseMemberDTO.getEid());
        coupon.setEname(enterpriseMemberDTO.getEname());
        coupon.setGetType(CouponGetTypeEnum.AUTO_GIVE.getCode());
        coupon.setGetTime(date);
        coupon.setBeginTime(beginTime);
        coupon.setEndTime(endTime);
        coupon.setUsedStatus(CouponUsedStatusEnum.NOT_USED.getCode());
        coupon.setStatus(CouponStatusEnum.NORMAL_COUPON.getCode());
        coupon.setCreateTime(date);
        coupon.setExpectGiveNumber(canGiveNum);
        coupon.setRemark(COUPON_REMARK);
        couponList.add(coupon);
    }

}
