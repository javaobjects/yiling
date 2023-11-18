package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.strategy.dto.StrategyActivityRecordDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftFailDTO;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordListRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordPageRequest;

/**
 * 营销活动记录表Api
 *
 * @author: yong.zhang
 * @date: 2022/9/6
 */
public interface StrategyActivityRecordApi {

    /**
     * 活动参与明细分页查询
     *
     * @param request 查询条件
     * @return 营销活动记录
     */
    Page<StrategyActivityRecordDTO> pageList(QueryStrategyActivityRecordPageRequest request);

    /**
     * 根据活动id查询活动被参与次数
     *
     * @param strategyActivityId 活动id
     * @return 活动被参与次数
     */
    Integer countRecordByActivityId(Long strategyActivityId);

    /**
     * 活动参与明细列表查询
     *
     * @param request 查询条件
     * @return 营销活动记录
     */
    List<StrategyActivityRecordDTO> listByCondition(QueryStrategyActivityRecordListRequest request);

    List<StrategyGiftFailDTO> listByRecordId(Long activityRecordId);
}
