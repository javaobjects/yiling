package com.yiling.marketing.strategy.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategySellerLimitDO;

/**
 * <p>
 * 策略满赠商家表 服务类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
public interface StrategySellerLimitService extends BaseService<StrategySellerLimitDO> {

    /**
     * 添加商家
     *
     * @param request 添加商家内容
     * @return 成功/失败
     */
    boolean add(AddStrategySellerLimitRequest request);

    /**
     * 复制商家
     *
     * @param strategyActivityDO 营销活动主信息
     * @param oldId 被拷贝的营销活动id
     * @param opUserId 操作人
     * @param opTime 操作时间
     */
    void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime);

    /**
     * 删除商家
     *
     * @param request 删除商家内容
     * @return 成功/失败
     */
    boolean delete(DeleteStrategySellerLimitRequest request);

    /**
     * 策略满赠商家-已添加商家列表查询
     *
     * @param request 查询条件
     * @return 已添加商家列表
     */
    Page<StrategySellerLimitDO> pageList(QueryStrategySellerLimitPageRequest request);

    /**
     * 根据活动id查询商家范围数量
     *
     * @param strategyActivityId 活动id
     * @return 商家范围数量
     */
    Integer countSellerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id查询商家范围
     *
     * @param strategyActivityId 活动id
     * @return 商家范围
     */
    List<StrategySellerLimitDO> listSellerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和企业id查询策略满赠商家表
     *
     * @param strategyActivityId 活动id
     * @param eidList 企业id
     * @return 活动商家信息
     */
    List<StrategySellerLimitDO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList);

    /**
     * 根据活动id查询商家范围数量
     *
     * @param strategyActivityId 活动id
     * @return 商家范围数量
     */
    Integer countSellerByActivityIdForPayPromotion(Long strategyActivityId);
}
