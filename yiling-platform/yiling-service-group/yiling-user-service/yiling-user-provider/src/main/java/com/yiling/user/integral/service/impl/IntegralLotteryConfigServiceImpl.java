package com.yiling.user.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.SaveIntegralLotteryConfigRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralLotteryConfigDO;
import com.yiling.user.integral.dao.IntegralLotteryConfigMapper;
import com.yiling.user.integral.entity.IntegralSignPeriodDO;
import com.yiling.user.integral.entity.IntegralUseRuleDO;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.integral.service.IntegralLotteryConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralUseRuleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分参与抽奖活动配置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Slf4j
@Service
public class IntegralLotteryConfigServiceImpl extends BaseServiceImpl<IntegralLotteryConfigMapper, IntegralLotteryConfigDO> implements IntegralLotteryConfigService {

    @Autowired
    IntegralUseRuleService integralUseRuleService;

    @Override
    public IntegralLotteryConfigDTO getByUseRuleId(Long useRuleId) {
        LambdaQueryWrapper<IntegralLotteryConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralLotteryConfigDO::getUseRuleId, useRuleId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralLotteryConfigDTO.class);
    }

    @Override
    public List<IntegralLotteryConfigDTO> getByRuleListAndActivityId(List<Long> useRuleIdList, Long lotteryActivityId) {
        if (CollUtil.isEmpty(useRuleIdList)) {
            return ListUtil.toList();
        }

        LambdaQueryWrapper<IntegralLotteryConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(IntegralLotteryConfigDO::getUseRuleId, useRuleIdList);
        wrapper.eq(IntegralLotteryConfigDO::getLotteryActivityId, lotteryActivityId);
        return PojoUtils.map(this.list(wrapper), IntegralLotteryConfigDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveLotteryConfig(SaveIntegralLotteryConfigRequest request) {
        // 单个活动ID，同一时间，只允许在一个有效的规则ID中存在
        List<IntegralUseRuleDTO> validUseRuleList = integralUseRuleService.getValidUseRuleList();
        if (CollUtil.isNotEmpty(validUseRuleList)) {
            // 当前消耗规则的信息
            IntegralUseRuleDO useRuleDO = integralUseRuleService.getById(request.getUseRuleId());

            // 获取当前有效并且使用的是该抽奖活动的规则
            Map<Long, IntegralUseRuleDTO> validUseRuleMap = validUseRuleList.stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity()));
            List<Long> useRuleIdList = validUseRuleList.stream().map(IntegralUseRuleDTO::getId).filter(useRuleId -> useRuleId.compareTo(request.getUseRuleId()) != 0).collect(Collectors.toList());
            List<IntegralLotteryConfigDTO> lotteryConfigDTOList = this.getByRuleListAndActivityId(useRuleIdList, request.getLotteryActivityId());

            lotteryConfigDTOList.forEach(lotteryConfigDTO -> {
                IntegralUseRuleDTO integralUseRuleDTO = validUseRuleMap.get(lotteryConfigDTO.getUseRuleId());

                if (this.isOverlap(useRuleDO.getStartTime(), useRuleDO.getEndTime(), integralUseRuleDTO.getStartTime(), integralUseRuleDTO.getEndTime())) {
                    throw new BusinessException(UserErrorCode.TIME_RANGE_NOT_MORE_SIGN_RULE);
                }
            });
        }

        // 更新规则状态为启用
        UpdateRuleStatusRequest statusRequest = new UpdateRuleStatusRequest();
        statusRequest.setId(request.getUseRuleId());
        statusRequest.setStatus(IntegralRuleStatusEnum.ENABLED.getCode());
        statusRequest.setOpUserId(request.getOpUserId());
        integralUseRuleService.updateStatus(statusRequest);

        IntegralLotteryConfigDO lotteryConfigDO = PojoUtils.map(request, IntegralLotteryConfigDO.class);

        IntegralLotteryConfigDTO lotteryConfigDTO = this.getByUseRuleId(request.getUseRuleId());
        if (Objects.isNull(lotteryConfigDTO)) {
            return this.save(lotteryConfigDO);
        } else {
            lotteryConfigDO.setId(lotteryConfigDTO.getId());
            return this.updateById(lotteryConfigDO);
        }
    }

    @Override
    public IntegralLotteryConfigDTO getRuleByLotteryActivityId(Long lotteryActivityId) {
        List<Long> ruleIdList = integralUseRuleService.getValidUseRuleList().stream().map(IntegralUseRuleDTO::getId).distinct().collect(Collectors.toList());
        if (CollUtil.isEmpty(ruleIdList)) {
            return null;
        }

        LambdaQueryWrapper<IntegralLotteryConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralLotteryConfigDO::getLotteryActivityId, lotteryActivityId);
        wrapper.in(IntegralLotteryConfigDO::getUseRuleId, ruleIdList);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralLotteryConfigDTO.class);
    }

    /**
     * 两个时间段是否有重叠
     *
     * @param realStartTime 第一个时间段的开始时间
     * @param realEndTime 第一个时间段的结束时间
     * @param startTime 第二个时间段的开始时间
     * @param endTime 第二个时间段的结束时间
     * @return true 表示时间段有重合
     */
    public boolean isOverlap(Date realStartTime, Date realEndTime, Date startTime, Date endTime) {
        return startTime.before(realEndTime) && endTime.after(realStartTime);
    }

}
