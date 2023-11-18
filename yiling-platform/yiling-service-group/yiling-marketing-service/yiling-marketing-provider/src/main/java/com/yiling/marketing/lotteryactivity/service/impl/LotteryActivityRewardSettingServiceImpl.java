package com.yiling.marketing.lotteryactivity.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRewardSettingDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityRewardSettingMapper;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRewardSettingLogDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingLogService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动-奖品设置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Slf4j
@Service
public class LotteryActivityRewardSettingServiceImpl extends BaseServiceImpl<LotteryActivityRewardSettingMapper, LotteryActivityRewardSettingDO> implements LotteryActivityRewardSettingService {

    @Autowired
    private LotteryActivityRewardSettingLogService lotteryActivityRewardSettingLogService;

    @Override
    public List<LotteryActivityRewardSettingDTO> getByLotteryActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityRewardSettingDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityRewardSettingDO::getLotteryActivityId, lotteryActivityId);
        wrapper.orderByAsc(LotteryActivityRewardSettingDO::getLevel);

        List<LotteryActivityRewardSettingDO> list = this.list(wrapper);
        return PojoUtils.map(list, LotteryActivityRewardSettingDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRewardSetting(UpdateRewardSettingRequest request) {
        List<UpdateRewardSettingRequest.SaveRewardSettingRequest> rewardSettingList = request.getActivityRewardSettingList();
        if (CollUtil.isEmpty(rewardSettingList)) {
            return false;
        }
        // 校验中奖概率之和 和 每天最大抽中数量列的值，必须是中奖数量值的整数倍
        BigDecimal sumHit = rewardSettingList.stream().map(UpdateRewardSettingRequest.SaveRewardSettingRequest::getHitProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sumHit.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BusinessException(LotteryActivityErrorCode.HIT_PROBABILITY_SUM_ERROR);
        }

        LotteryActivityRewardSettingDO settingDO = this.getById(rewardSettingList.get(0).getId());
        List<LotteryActivityRewardSettingDTO> rewardSettingDTOS = this.getByLotteryActivityId(settingDO.getLotteryActivityId());
        Map<Long, LotteryActivityRewardSettingDTO> rewardSettingDTOMap = rewardSettingDTOS.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));

        for (UpdateRewardSettingRequest.SaveRewardSettingRequest settingRequest : rewardSettingList) {
            LotteryActivityRewardSettingDTO rewardSettingDTO = rewardSettingDTOMap.get(settingRequest.getId());
            // 每天最大抽中数量列的值，必须是中奖数量值的整数倍
            if (settingRequest.getEveryMaxNumber() > 0 && rewardSettingDTO.getRewardNumber() > 0) {
                int remainder = settingRequest.getEveryMaxNumber() % rewardSettingDTO.getRewardNumber();
                if (remainder != 0) {
                    throw new BusinessException(LotteryActivityErrorCode.EVERY_MAX_NUMBER_MUST_INTEGER);
                }
            }
        }

        List<LotteryActivityRewardSettingLogDO> list = ListUtil.toList();
        // 更新中奖概率和最大抽中数量
        rewardSettingList.forEach(rewardSettingRequest -> {

            LotteryActivityRewardSettingDTO rewardSettingDTO = rewardSettingDTOMap.get(rewardSettingRequest.getId());
            if (rewardSettingDTO.getHitProbability().compareTo(rewardSettingRequest.getHitProbability()) != 0 || !rewardSettingDTO.getEveryMaxNumber().equals(rewardSettingRequest.getEveryMaxNumber())) {
                LotteryActivityRewardSettingDO rewardSettingDO = new LotteryActivityRewardSettingDO();
                rewardSettingDO.setId(rewardSettingRequest.getId());
                rewardSettingDO.setEveryMaxNumber(rewardSettingRequest.getEveryMaxNumber());
                rewardSettingDO.setHitProbability(rewardSettingRequest.getHitProbability());
                rewardSettingDO.setOpUserId(request.getOpUserId());
                this.updateById(rewardSettingDO);
                log.info("抽奖活动ID={} 奖品设置ID={} 修改奖品设置的中奖概率为={} 每天最大中奖数量为={}", settingDO.getLotteryActivityId(),
                        rewardSettingRequest.getId(), rewardSettingRequest.getHitProbability(), rewardSettingRequest.getEveryMaxNumber());

                // 增加修改日志记录
                LotteryActivityRewardSettingLogDO settingLogDO = new LotteryActivityRewardSettingLogDO();
                settingLogDO.setRewardSettingId(rewardSettingRequest.getId());
                settingLogDO.setLotteryActivityId(rewardSettingDTO.getLotteryActivityId());
                settingLogDO.setBeforeEveryMaxNumber(rewardSettingDTO.getEveryMaxNumber());
                settingLogDO.setAfterEveryMaxNumber(rewardSettingRequest.getEveryMaxNumber());
                settingLogDO.setBeforeHitProbability(rewardSettingDTO.getHitProbability());
                settingLogDO.setAfterHitProbability(rewardSettingRequest.getHitProbability());
                settingLogDO.setOpUserId(request.getOpUserId());
                list.add(settingLogDO);
            }

        });

        if (CollUtil.isNotEmpty(list)) {
            lotteryActivityRewardSettingLogService.saveBatch(list);
        }

        return true;
    }
}
