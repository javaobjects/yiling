package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralLotteryConfigRequest;
import com.yiling.user.integral.entity.IntegralLotteryConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分参与抽奖活动配置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
public interface IntegralLotteryConfigService extends BaseService<IntegralLotteryConfigDO> {

    /**
     * 根据消耗规则ID查询积分参与抽奖活动配置
     *
     * @param useRuleId
     * @return
     */
    IntegralLotteryConfigDTO getByUseRuleId(Long useRuleId);

    /**
     * 根据消耗规则ID集合和抽奖活动ID，批量查询抽奖配置信息
     *
     * @param useRuleIdList
     * @param lotteryActivityId
     * @return
     */
    List<IntegralLotteryConfigDTO> getByRuleListAndActivityId(List<Long> useRuleIdList, Long lotteryActivityId);

    /**
     * 保存参与抽奖配置
     *
     * @param request
     * @return
     */
    boolean saveLotteryConfig(SaveIntegralLotteryConfigRequest request);

    /**
     * 根据抽奖活动ID获取积分消耗规则
     *
     * @param lotteryActivityId
     * @return
     */
    IntegralLotteryConfigDTO getRuleByLotteryActivityId(Long lotteryActivityId);
}
