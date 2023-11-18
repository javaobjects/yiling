package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinDetailDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityVirtualHitDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityVirtualHitMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashStatusEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinDetailService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityVirtualHitService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动-虚拟中奖名单生成表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-31
 */
@Slf4j
@Service
public class LotteryActivityVirtualHitServiceImpl extends BaseServiceImpl<LotteryActivityVirtualHitMapper, LotteryActivityVirtualHitDO> implements LotteryActivityVirtualHitService {

    @Autowired
    LotteryActivityJoinDetailService lotteryActivityJoinDetailService;
    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    LotteryActivityRewardSettingService lotteryActivityRewardSettingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateVirtualHit() {
        LambdaQueryWrapper<LotteryActivityVirtualHitDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityVirtualHitDO::getStatus, 0);
        List<LotteryActivityVirtualHitDO> virtualHitDOList = this.list(wrapper);

        Date now = new Date();
        virtualHitDOList.forEach(lotteryActivityVirtualHitDO -> {
            if (lotteryActivityVirtualHitDO.getHitDate().before(now)) {

                LotteryActivityJoinDetailDO joinDetailDO = new LotteryActivityJoinDetailDO();
                // 获取奖品信息
                List<LotteryActivityRewardSettingDTO> rewardSettingDTOS = lotteryActivityRewardSettingService.getByLotteryActivityId(lotteryActivityVirtualHitDO.getLotteryActivityId());
                for (LotteryActivityRewardSettingDTO rewardSettingDTO : rewardSettingDTOS) {
                    if (rewardSettingDTO.getRewardId().compareTo(lotteryActivityVirtualHitDO.getRewardId()) == 0 && rewardSettingDTO.getLevel().equals(lotteryActivityVirtualHitDO.getLevel())) {
                        joinDetailDO.setRewardType(rewardSettingDTO.getRewardType());
                        joinDetailDO.setRewardNumber(rewardSettingDTO.getRewardNumber());
                        joinDetailDO.setShowName(rewardSettingDTO.getShowName());
                        joinDetailDO.setRewardName(rewardSettingDTO.getRewardName());
                        break;
                    }
                }

                // 生成中奖信息
                joinDetailDO.setLotteryActivityId(lotteryActivityVirtualHitDO.getLotteryActivityId());
                joinDetailDO.setActivityName(lotteryActivityService.getById(lotteryActivityVirtualHitDO.getLotteryActivityId()).getActivityName());
                joinDetailDO.setRewardId(lotteryActivityVirtualHitDO.getRewardId());
                joinDetailDO.setLevel(lotteryActivityVirtualHitDO.getLevel());
                joinDetailDO.setUname(lotteryActivityVirtualHitDO.getUname());
                joinDetailDO.setPlatformType(LotteryActivityPlatformEnum.B2B.getCode());
                joinDetailDO.setLotteryTime(lotteryActivityVirtualHitDO.getHitDate());
                joinDetailDO.setStatus(LotteryActivityCashStatusEnum.UN_CASH.getCode());
                joinDetailDO.setRemark("运营要求生成的虚拟中奖名单");
                lotteryActivityJoinDetailService.save(joinDetailDO);

                // 更新状态为已执行
                LotteryActivityVirtualHitDO virtualHitDO = new LotteryActivityVirtualHitDO();
                virtualHitDO.setId(lotteryActivityVirtualHitDO.getId());
                virtualHitDO.setStatus(1);
                this.updateById(virtualHitDO);
                log.info("运营要求生成的虚拟中奖名单={}", JSONObject.toJSONString(joinDetailDO));
            }
        });

        return true;
    }

}
