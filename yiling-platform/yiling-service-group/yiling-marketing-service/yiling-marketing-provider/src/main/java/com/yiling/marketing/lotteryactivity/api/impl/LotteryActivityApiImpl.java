package com.yiling.marketing.lotteryactivity.api.impl;

import java.util.List;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityHitRandomGenerateDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityRulePageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityBasicRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivitySettingRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityAccessRecordService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityHitRandomGenerateService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivitySignRecordService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;

/**
 * <p>
 * 抽奖活动 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@DubboService
public class LotteryActivityApiImpl implements LotteryActivityApi {

    @Autowired
    private LotteryActivityService lotteryActivityService;
    @Autowired
    private LotteryActivityRuleService lotteryActivityRuleService;
    @Autowired
    private LotteryActivitySignRecordService lotteryActivitySignRecordService;
    @Autowired
    private LotteryActivityHitRandomGenerateService lotteryActivityHitRandomGenerateService;
    @Autowired
    LotteryActivityAccessRecordService lotteryActivityAccessRecordService;
    @Autowired
    LotteryActivityJoinRuleService lotteryActivityJoinRuleService;
    @Autowired
    LotteryActivityRewardSettingService lotteryActivityRewardSettingService;

    @Override
    public Page<LotteryActivityItemBO> queryListPage(QueryLotteryActivityPageRequest request) {
        return lotteryActivityService.queryListPage(request);
    }

    @Override
    public LotteryActivityDetailBO get(Long id, LotteryActivityPlatformEnum platformEnum) {
        return lotteryActivityService.get(id, platformEnum);
    }

    @Override
    public LotteryActivityDTO getById(Long id) {
        return PojoUtils.map(lotteryActivityService.getById(id), LotteryActivityDTO.class);
    }

    @Override
    public boolean stopActivity(Long id, Long opUserId) {
        LotteryActivityDO lotteryActivityDO = Optional.ofNullable(lotteryActivityService.getById(id)).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST));
        if (lotteryActivityDO.getStatus().equals(EnableStatusEnum.DISABLED.getCode())) {
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_HAD_STOP);
        }

        LotteryActivityDO activityDO = new LotteryActivityDO();
        activityDO.setId(id);
        activityDO.setStatus(EnableStatusEnum.DISABLED.getCode());
        activityDO.setOpUserId(opUserId);
        lotteryActivityService.updateById(activityDO);

        return true;
    }

    @Override
    public boolean saveActivitySetting(SaveLotteryActivitySettingRequest activitySettingRequest) {
        return lotteryActivityService.saveActivitySetting(activitySettingRequest);
    }

    @Override
    public Page<LotteryActivityRuleDTO> queryRulePage(QueryLotteryActivityRulePageRequest request) {
        return lotteryActivityRuleService.queryRulePage(request);
    }

    @Override
    public boolean checkTodaySign(Long activityId, Integer platformType, Long uid) {
        return lotteryActivitySignRecordService.checkTodaySign(activityId, platformType, uid);
    }

    @Override
    public boolean checkTodayAccess(Long activityId, Integer platformType, Long uid) {
        return lotteryActivityAccessRecordService.checkTodayAccess(activityId, platformType, uid);
    }

    @Override
    public boolean checkActivityAccess(Long activityId, Integer platformType, Long uid) {
        return lotteryActivityAccessRecordService.checkActivityAccess(activityId, platformType, uid);
    }

    @Override
    public LotteryActivityJoinRuleDTO getByLotteryActivityId(Long activityId) {
        return lotteryActivityJoinRuleService.getByLotteryActivityId(activityId);
    }

    @Override
    public List<LotteryActivityHitRandomGenerateDTO> getCurrentGenerate(Long activityId) {
        return lotteryActivityHitRandomGenerateService.getCurrentGenerate(activityId);
    }

    @Override
    public void generateHitTask() {
        lotteryActivityHitRandomGenerateService.generateHitTask();
    }

    @Override
    public Long copyLottery(Long id, Long opUserId) {
        return lotteryActivityService.copyLottery(id, opUserId);
    }

    @Override
    public boolean updateRewardSetting(UpdateRewardSettingRequest request) {
        return lotteryActivityRewardSettingService.updateRewardSetting(request);
    }

    @Override
    public List<String> getNameList(Integer limit) {
        return lotteryActivityService.getNameList(limit);
    }

    @Override
    public LotteryActivityDTO saveActivityBasic(SaveLotteryActivityBasicRequest activityBasicRequest) {
        return lotteryActivityService.saveActivityBasic(activityBasicRequest);
    }

}
