package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Date;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivitySignRecordDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivitySignRecordMapper;
import com.yiling.marketing.lotteryactivity.service.LotteryActivitySignRecordService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 抽奖活动签到记录表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-15
 */
@Service
public class LotteryActivitySignRecordServiceImpl extends BaseServiceImpl<LotteryActivitySignRecordMapper, LotteryActivitySignRecordDO> implements LotteryActivitySignRecordService {

    @Override
    public boolean checkTodaySign(Long activityId, Integer platformType, Long uid) {
        LambdaQueryWrapper<LotteryActivitySignRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivitySignRecordDO::getLotteryActivityId, activityId);
        wrapper.eq(LotteryActivitySignRecordDO::getPlatformType, platformType);
        wrapper.eq(LotteryActivitySignRecordDO::getUid, uid);
        wrapper.ge(LotteryActivitySignRecordDO::getSignTime, DateUtil.beginOfDay(new Date()));
        wrapper.le(LotteryActivitySignRecordDO::getSignTime, DateUtil.endOfDay(new Date()));
        LotteryActivitySignRecordDO signRecordDO = this.getOne(wrapper);

        if (Objects.nonNull(signRecordDO)) {
            return true;
        }
        return false;
    }
}
