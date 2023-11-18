package com.yiling.marketing.integral.service.impl;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.dao.IntegralUseRuleMapper;
import com.yiling.marketing.integral.entity.IntegralBehaviorDO;
import com.yiling.marketing.integral.entity.IntegralUseRuleDO;
import com.yiling.marketing.integral.service.IntegralBehaviorService;
import com.yiling.marketing.integral.service.IntegralLotteryConfigService;
import com.yiling.marketing.integral.service.IntegralUseRuleService;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.integral.bo.IntegralLotteryConfigBO;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.enums.IntegralGiveRuleProgressEnum;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分消耗规则表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Slf4j
@Service
public class IntegralUseRuleServiceImpl extends BaseServiceImpl<IntegralUseRuleMapper, IntegralUseRuleDO> implements IntegralUseRuleService {

    @Autowired
    IntegralBehaviorService integralBehaviorService;
    @Autowired
    IntegralLotteryConfigService integralLotteryConfigService;
    @Autowired
    LotteryActivityService lotteryActivityService;

    @Override
    public IntegralUseRuleDetailBO get(Long id) {
        IntegralUseRuleDO useRuleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));

        // 基本信息
        IntegralUseRuleDetailBO useRuleDetailBO = PojoUtils.map(useRuleDO, IntegralUseRuleDetailBO.class);
        IntegralBehaviorDO behaviorDO = Optional.ofNullable(integralBehaviorService.getById(useRuleDetailBO.getBehaviorId())).orElse(new IntegralBehaviorDO());
        useRuleDetailBO.setBehaviorName(behaviorDO.getName());

        // 参与抽奖
        IntegralLotteryConfigDTO lotteryConfigDTO = integralLotteryConfigService.getByUseRuleId(id);
        IntegralLotteryConfigBO integralLotteryConfigBO = PojoUtils.map(lotteryConfigDTO, IntegralLotteryConfigBO.class);
        if (Objects.nonNull(lotteryConfigDTO)) {
            // 查询抽奖活动
            LotteryActivityDO lotteryActivityDO = lotteryActivityService.getById(lotteryConfigDTO.getLotteryActivityId());
            integralLotteryConfigBO.setLotteryActivityName(lotteryActivityDO.getActivityName());
            integralLotteryConfigBO.setLotteryActivityEndTime(lotteryActivityDO.getEndTime());
            // 进度
            Date now = new Date();
            if (lotteryActivityDO.getStartTime().after(now)) {
                integralLotteryConfigBO.setLotteryActivityProgress(IntegralGiveRuleProgressEnum.UNDO.getCode());
            } else if (lotteryActivityDO.getEndTime().before(now)) {
                integralLotteryConfigBO.setLotteryActivityProgress(IntegralGiveRuleProgressEnum.END.getCode());
            } else {
                integralLotteryConfigBO.setLotteryActivityProgress(IntegralGiveRuleProgressEnum.GOING.getCode());
            }
            // 停用的也设置为已结束
            if (lotteryActivityDO.getStatus().equals(IntegralRuleStatusEnum.DISABLED.getCode())) {
                integralLotteryConfigBO.setLotteryActivityProgress(IntegralGiveRuleProgressEnum.END.getCode());
            }
        }

        useRuleDetailBO.setIntegralLotteryConfig(integralLotteryConfigBO);

        return useRuleDetailBO;
    }


}
