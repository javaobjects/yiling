package com.yiling.marketing.strategy.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.SaveStrategyAmountLadderRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyAmountLadderDO;

/**
 * <p>
 * 营销活动订单累计金额满赠内容阶梯 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategyAmountLadderService extends BaseService<StrategyAmountLadderDO> {

    /**
     * 保存接口（新增和修改）
     *
     * @param marketingStrategyId 活动id
     * @param requestList 保存的数据
     * @return 成功/失败
     */
    boolean save(Long marketingStrategyId, List<SaveStrategyAmountLadderRequest> requestList, Long opUserId, Date opTime);

    /**
     * 复制订单累计金额满赠内容阶梯
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 根据活动id查询营销活动订单累计金额满赠内容阶梯
     *
     * @param strategyActivityId 活动id
     * @return 营销活动订单累计金额满赠内容阶梯
     */
    List<StrategyAmountLadderDO> listAmountLadderByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和金额查询营销活动订单累计金额满赠内容阶梯
     *
     * @param strategyActivityId 活动id
     * @param amountLimit 阶梯金额
     * @return 营销活动订单累计金额满赠内容阶梯
     */
    List<StrategyAmountLadderDO> listAmountLadderByActivityIdAndAmountLimit(Long strategyActivityId, BigDecimal amountLimit);
}
