package com.yiling.marketing.lotteryactivity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveScopeDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveScopeDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityGiveScopeMapper;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinRuleDO;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveScopeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 抽奖活动B端赠送范围表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Service
public class LotteryActivityGiveScopeServiceImpl extends BaseServiceImpl<LotteryActivityGiveScopeMapper, LotteryActivityGiveScopeDO> implements LotteryActivityGiveScopeService {

    @Override
    public LotteryActivityGiveScopeDTO getByLotteryActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityGiveScopeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveScopeDO::getLotteryActivityId, lotteryActivityId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), LotteryActivityGiveScopeDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByLotteryActivityId(Long lotteryActivityId, Long opUserId) {
        LambdaQueryWrapper<LotteryActivityGiveScopeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityGiveScopeDO::getLotteryActivityId, lotteryActivityId);

        LotteryActivityGiveScopeDO giveScopeDO = new LotteryActivityGiveScopeDO();
        giveScopeDO.setOpUserId(opUserId);
        this.batchDeleteWithFill(giveScopeDO, wrapper);
    }

}
