package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.SaveStrategyCycleLadderRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyCycleLadderDO;

/**
 * <p>
 * 营销活动时间周期满赠内容阶梯 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyCycleLadderService extends BaseService<StrategyCycleLadderDO> {

    /**
     * 保存接口（新增和修改）
     *
     * @param marketingStrategyId 活动id
     * @param requestList 保存的数据
     * @return 成功/失败
     */
    boolean save(Long marketingStrategyId, List<SaveStrategyCycleLadderRequest> requestList, Long opUserId, Date opTime);

    /**
     * 复制时间周期满赠内容阶梯
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 根据活动id查询营销活动时间周期满赠内容阶梯
     *
     * @param strategyActivityId 活动id
     * @return 营销活动时间周期满赠内容阶梯
     */
    List<StrategyCycleLadderDO> listCycleLadderByActivityId(Long strategyActivityId);
}
