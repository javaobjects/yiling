package com.yiling.marketing.lotteryactivity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityRulePageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRuleDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 抽奖活动规则表（内置数据表） 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
public interface LotteryActivityRuleService extends BaseService<LotteryActivityRuleDO> {

    /**
     * 查询抽奖活动规则分页列表
     *
     * @param request
     * @return
     */
    Page<LotteryActivityRuleDTO> queryRulePage(QueryLotteryActivityRulePageRequest request);
}
