package com.yiling.marketing.strategy.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordListRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityRecordDO;

/**
 * <p>
 * 营销活动记录表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-06
 */
public interface StrategyActivityRecordService extends BaseService<StrategyActivityRecordDO> {

    /**
     * 活动参与明细分页查询
     *
     * @param request 查询条件
     * @return 营销活动记录
     */
    Page<StrategyActivityRecordDO> pageList(QueryStrategyActivityRecordPageRequest request);

    /**
     * 根据活动id查询活动被参与次数
     *
     * @param strategyActivityId 活动id
     * @return 活动被参与次数
     */
    Integer countRecordByActivityId(Long strategyActivityId);

    /**
     * 查看某用户某活动的参与次数
     *
     * @param strategyActivityId 活动id
     * @param eid 客户id
     * @return 活动参与次数
     */
    Integer countRecordByActivityIdAndEid(Long strategyActivityId, Long ladderId, Long eid, Long orderId);

    /**
     * 活动参与明细列表查询
     *
     * @param request 查询条件
     * @return 营销活动记录
     */
    List<StrategyActivityRecordDO> listByCondition(QueryStrategyActivityRecordListRequest request);

    /**
     * 活动参与明细列表查询-查第1条--主要是为了查看有没有符合条件的数据
     *
     * @param request 查询条件
     * @return 营销活动记录
     */
    StrategyActivityRecordDO getFirstByCondition(QueryStrategyActivityRecordListRequest request);
}
