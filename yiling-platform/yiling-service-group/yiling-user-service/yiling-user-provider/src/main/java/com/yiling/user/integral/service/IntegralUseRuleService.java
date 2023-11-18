package com.yiling.user.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeLotteryRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.entity.IntegralUseRuleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分消耗规则表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
public interface IntegralUseRuleService extends BaseService<IntegralUseRuleDO> {

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
     * 获取所有有效的消耗规则（未结束且状态为启用）
     *
     * @return
     */
    List<IntegralUseRuleDTO> getValidUseRuleList();

    /**
     * 复制消耗规则
     *
     * @param id
     * @param opUserId
     * @return
     */
    Long copy(Long id, Long opUserId);

    /**
     * 积分兑换抽奖次数
     *
     * @param request
     * @return
     */
    boolean exchangeLotteryTimes(UpdateIntegralExchangeLotteryRequest request);
}
