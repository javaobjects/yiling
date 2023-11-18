package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.SaveStrategyGiftRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;

/**
 * <p>
 * 营销活动赠品表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyGiftService extends BaseService<StrategyGiftDO> {

    /**
     * 保存接口（新增和修改）
     *
     * @param marketingStrategyId 活动id
     * @param ladderId 阶梯id
     * @param requestList 保存的数据
     * @return 成功/失败
     */
    boolean save(Long marketingStrategyId, Long ladderId, List<SaveStrategyGiftRequest> requestList, Long opUserId, Date opTime);

    /**
     * 复制订单累计金额满赠内容阶梯
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param ladderId 新的阶梯id
     * @param oldLadderId 老的阶梯id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long ladderId, Long oldLadderId, Long opUserId, Date opTime);

    /**
     * 查询营销活动赠品信息
     *
     * @param strategyActivityId 营销活动id
     * @param ladderId 活动阶梯id
     * @return 营销活动赠品信息
     */
    List<StrategyGiftDO> listGiftByActivityIdAndLadderId(Long strategyActivityId, Long ladderId);

}
