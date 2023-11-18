package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityAccessRecordMapper;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveScopeDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityAccessRecordDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGetTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGiveEnterpriseTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGiveScopeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGiveUserTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityLoopGiveEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityAccessRecordService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseTypeService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveMemberService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGivePromoterService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveScopeService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动访问记录表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-16
 */
@Slf4j
@Service
public class LotteryActivityAccessRecordServiceImpl extends BaseServiceImpl<LotteryActivityAccessRecordMapper, LotteryActivityAccessRecordDO> implements LotteryActivityAccessRecordService {

    @Autowired
    LotteryActivityTimesService lotteryActivityTimesService;
    @Autowired
    LotteryActivityJoinRuleService lotteryActivityJoinRuleService;
    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    LotteryActivityGiveScopeService lotteryActivityGiveScopeService;
    @Autowired
    LotteryActivityGiveEnterpriseService lotteryActivityGiveEnterpriseService;
    @Autowired
    LotteryActivityGiveEnterpriseTypeService lotteryActivityGiveEnterpriseTypeService;
    @Autowired
    LotteryActivityGiveMemberService lotteryActivityGiveMemberService;
    @Autowired
    LotteryActivityGivePromoterService lotteryActivityGivePromoterService;

    @DubboReference
    UserApi userApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    MemberApi memberApi;
    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;

    @Override
    public boolean checkTodayAccess(Long activityId, Integer platformType, Long uid) {
        try {
            LotteryActivityDO activityDO = lotteryActivityService.getById(activityId);
            Date now = new Date();
            // 活动停用、未开始、已结束，都不增加访问记录，也就是不赠送抽奖次数
            if (activityDO.getStatus().equals(EnableStatusEnum.DISABLED.getCode()) || activityDO.getStartTime().after(now) || activityDO.getEndTime().before(now)) {
                return true;
            }

            LotteryActivityAccessRecordDO accessRecordDO = this.getAccessRecord(activityId, platformType, uid, true);
            if (Objects.nonNull(accessRecordDO)) {
                return true;
            }

            // 检查是否设置了访问获取抽奖次数的规则
            LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityJoinRuleService.getByLotteryActivityId(activityId);
            if (Objects.isNull(joinRuleDTO) || joinRuleDTO.getEveryGive() <= 0) {
                return true;
            }

            // 增加抽奖次数
            this.addTimes(activityId, platformType, uid, joinRuleDTO.getEveryGive(), LotteryActivityGetTypeEnum.EVERY);
            // 保存访问记录
            this.saveAccessRecord(activityId, platformType, uid);
        } catch (Exception e) {
            log.error("C端校验是否访问过活动ID={} 出错：{}", activityId, e.getMessage(), e);
        }

        return true;

    }

    @Override
    public boolean checkActivityAccess(Long activityId, Integer platformType, Long uid) {
        try {
            LotteryActivityDO activityDO = lotteryActivityService.getById(activityId);
            Date now = new Date();
            // 活动停用、未开始、已结束，都不增加访问记录，也就是不赠送抽奖次数
            if (activityDO.getStatus().equals(EnableStatusEnum.DISABLED.getCode()) || activityDO.getStartTime().after(now) || activityDO.getEndTime().before(now)) {
                return true;
            }

            // 赠送次数小于等于0，也不处理
            LotteryActivityGiveScopeDTO giveScopeDTO = lotteryActivityGiveScopeService.getByLotteryActivityId(activityId);
            if (Objects.isNull(giveScopeDTO) || giveScopeDTO.getGiveTimes() <= 0) {
                return true;
            }

            // 检查是否符合赠送要求
            if (this.checkGiveFlag(activityId, platformType, uid, giveScopeDTO))
                return true;

            // 校验是否设置为重复执行赠送（即每天赠送）
            LotteryActivityGetTypeEnum getTypeEnum = LotteryActivityGetTypeEnum.START_GIVE;
            if (LotteryActivityLoopGiveEnum.getByCode(giveScopeDTO.getLoopGive()) == LotteryActivityLoopGiveEnum.EVERY_GIVE) {
                LotteryActivityAccessRecordDO dayAccessRecord = this.getAccessRecord(activityId, platformType, uid, true);
                if (Objects.nonNull(dayAccessRecord)) {
                    return true;
                }
                getTypeEnum = LotteryActivityGetTypeEnum.EVERY;

            } else {
                // 获取到访问记录
                LotteryActivityAccessRecordDO accessRecordDO = this.getAccessRecord(activityId, platformType, uid, false);
                if (Objects.nonNull(accessRecordDO)) {
                    return true;
                }
            }

            // 赠送抽奖次数
            this.addTimes(activityId, platformType, uid, giveScopeDTO.getGiveTimes(), getTypeEnum);
            // 增加或更新访问记录
            saveAccessRecord(activityId, platformType, uid);

        } catch (Exception e) {
            log.error("B端校验是否访问过活动ID={} 出错：{}", activityId, e.getMessage(), e);
        }

        return true;
    }

    /**
     * 新增或更新访问记录
     *
     * @param activityId
     * @param platformType
     * @param uid
     */
    private void saveAccessRecord(Long activityId, Integer platformType, Long uid) {
        LotteryActivityAccessRecordDO recordDO = this.getAccessRecord(activityId, platformType, uid, false);
        if (Objects.nonNull(recordDO)) {
            recordDO.setAccessTime(new Date());
            recordDO.setOpUserId(uid);
            this.updateById(recordDO);

        } else {
            LotteryActivityAccessRecordDO activityAccessRecordDO = new LotteryActivityAccessRecordDO();
            activityAccessRecordDO.setLotteryActivityId(activityId);
            activityAccessRecordDO.setPlatformType(platformType);
            activityAccessRecordDO.setUid(uid);
            activityAccessRecordDO.setAccessTime(new Date());
            activityAccessRecordDO.setOpUserId(uid);
            this.save(activityAccessRecordDO);
        }
    }

    /**
     * 检查是否符合赠送要求
     *
     * @param activityId
     * @param platformType
     * @param uid
     * @param giveScopeDTO
     * @return
     */
    private boolean checkGiveFlag(Long activityId, Integer platformType, Long uid, LotteryActivityGiveScopeDTO giveScopeDTO) {
        if (LotteryActivityGiveScopeEnum.getByCode(giveScopeDTO.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_CUSTOMER) {
            List<Long> eidList = lotteryActivityGiveEnterpriseService.getGiveEnterpriseByActivityId(activityId);
            if (CollUtil.isNotEmpty(eidList) && !eidList.contains(uid)) {
                log.info("抽奖活动ID={} 平台={} 用户ID={} 赠送条件为指定客户不符合赠送抽奖次数要求", activityId, platformType, uid);
                return true;
            }
        } else if (LotteryActivityGiveScopeEnum.getByCode(giveScopeDTO.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_SCOPE_CUSTOMER) {

            // 赠送企业类型
            if (LotteryActivityGiveEnterpriseTypeEnum.ASSIGN_TYPE == LotteryActivityGiveEnterpriseTypeEnum.getByCode(giveScopeDTO.getGiveEnterpriseType())) {
                List<Integer> enterpriseTypeList = lotteryActivityGiveEnterpriseTypeService.getByLotteryActivityId(activityId);
                EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(uid)).orElse(new EnterpriseDTO());
                if (!enterpriseTypeList.contains(enterpriseDTO.getType())) {
                    return true;
                }
            }
            if (LotteryActivityGiveUserTypeEnum.NORMAL_USER == LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType())) {
                // 普通用户
                List<MemberBuyRecordDTO> currentMemberList = memberBuyRecordApi.getCurrentValidMemberRecord(uid);
                if (CollUtil.isNotEmpty(currentMemberList)) {
                    log.info("抽奖活动ID={} 平台={} 用户ID={} 赠送条件为普通用户不符合赠送抽奖次数要求", activityId, platformType, uid);
                    return true;
                }

            } else if (LotteryActivityGiveUserTypeEnum.ALL_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType())) {
                // 全部会员
                List<MemberBuyRecordDTO> currentMemberList = memberBuyRecordApi.getCurrentValidMemberRecord(uid);
                if (CollUtil.isEmpty(currentMemberList)) {
                    log.info("抽奖活动ID={} 平台={} 用户ID={} 赠送条件为全部会员不符合赠送抽奖次数要求", activityId, platformType, uid);
                    return true;
                }

            } else if (LotteryActivityGiveUserTypeEnum.ASSIGN_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType())) {
                // 指定方案会员
                List<Long> giveMemberList = lotteryActivityGiveMemberService.getGiveMemberByActivityId(activityId);
                List<Long> memberList = memberApi.getMemberListByEid(uid).stream().map(MemberEnterpriseBO::getMemberId).collect(Collectors.toList());
                List<Long> intersection = giveMemberList.stream().filter(memberList::contains).collect(Collectors.toList());
                if (CollUtil.isEmpty(intersection)) {
                    log.info("抽奖活动ID={} 平台={} 用户ID={} 赠送条件为指定方案会员不符合赠送抽奖次数要求", activityId, platformType, uid);
                    return true;
                }

            } else if (LotteryActivityGiveUserTypeEnum.ASSIGN_PROMOTER_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType())) {
                // 指定推广方会员
                List<Long> givePromoterList = lotteryActivityGivePromoterService.getGivePromoterByActivityId(activityId);
                List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getCurrentValidMemberRecord(uid);
                List<Long> promoterIdList = memberBuyRecordDTOList.stream().map(MemberBuyRecordDTO::getPromoterId).collect(Collectors.toList());
                List<Long> idList = givePromoterList.stream().filter(promoterIdList::contains).collect(Collectors.toList());
                if (CollUtil.isEmpty(memberBuyRecordDTOList) || CollUtil.isEmpty(idList)) {
                    log.info("抽奖活动ID={} 平台={} 用户ID={} 赠送条件为指定推广方会员不符合赠送抽奖次数要求", activityId, platformType, uid);
                    return true;
                }
            }

        }

        return false;
    }

    public void addTimes(Long activityId, Integer platformType, Long uid, Integer giveTimes, LotteryActivityGetTypeEnum getTypeEnum) {
        String activityName = Optional.ofNullable(lotteryActivityService.getById(activityId)).orElse(new LotteryActivityDO()).getActivityName();
        String uname;
        if (platformType == 1) {
            uname = Optional.ofNullable(enterpriseApi.getById(uid)).orElse(new EnterpriseDTO()).getName();
        } else {
            uname = Optional.ofNullable(userApi.getById(uid)).orElse(new UserDTO()).getNickName();
        }

        // 增加访问获取抽奖次数
        AddLotteryTimesRequest timesRequest = new AddLotteryTimesRequest();
        timesRequest.setLotteryActivityId(activityId);
        timesRequest.setActivityName(activityName);
        timesRequest.setTimes(giveTimes);
        timesRequest.setUid(uid);
        timesRequest.setUname(uname);
        timesRequest.setPlatformType(platformType);
        timesRequest.setGetType(getTypeEnum.getCode());
        timesRequest.setOpUserId(uid);
        lotteryActivityTimesService.addLotteryTimes(timesRequest);
    }

    /**
     * 获取访问记录
     *
     * @param activityId 活动ID
     * @param platformType 平台类型
     * @param uid 用户ID
     * @param currentDay 是否只查当日的访问记录
     * @return 访问记录对象
     */
    private LotteryActivityAccessRecordDO getAccessRecord(Long activityId, Integer platformType, Long uid, boolean currentDay) {
        LambdaQueryWrapper<LotteryActivityAccessRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityAccessRecordDO::getLotteryActivityId, activityId);
        wrapper.eq(LotteryActivityAccessRecordDO::getPlatformType, platformType);
        wrapper.eq(LotteryActivityAccessRecordDO::getUid, uid);
        if (currentDay) {
            wrapper.ge(LotteryActivityAccessRecordDO::getAccessTime, DateUtil.beginOfDay(new Date()));
            wrapper.le(LotteryActivityAccessRecordDO::getAccessTime, DateUtil.endOfDay(new Date()));
        }
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }


}
