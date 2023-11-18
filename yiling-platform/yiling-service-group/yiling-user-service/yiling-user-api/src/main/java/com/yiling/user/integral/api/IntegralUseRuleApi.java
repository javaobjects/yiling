package com.yiling.user.integral.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralLotteryConfigRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeLotteryRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;

/**
 * 积分消耗规则 API
 *
 * @author: lun.yu
 * @date: 2023-01-06
 */
public interface IntegralUseRuleApi {

    /**
     * 积分消耗规则分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralRuleItemBO> queryListPage(QueryIntegralRulePageRequest request);

    /**
     * 停用规则
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateRuleStatusRequest request);

    /**
     * 保存积分消耗规则基本信息
     *
     * @param request
     * @return
     */
    IntegralUseRuleDTO saveBasic(SaveIntegralRuleBasicRequest request);

    /**
     * 根据ID获取消耗规则
     *
     * @param id
     * @return
     */
    IntegralUseRuleDTO getById(Long id);

    /**
     * 复制消耗规则
     *
     * @param id
     * @param opUserId
     * @return
     */
    Long copy(Long id, Long opUserId);

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

    /**
     * 积分兑换抽奖次数
     *
     * @param request
     * @return
     */
    boolean exchangeLotteryTimes(UpdateIntegralExchangeLotteryRequest request);

}
