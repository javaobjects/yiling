package com.yiling.marketing.strategy.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;

/**
 * 策略满赠-商家Api
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
public interface StrategySellerApi {

    /**
     * 添加商家
     *
     * @param request 添加商家内容
     * @return 成功/失败
     */
    boolean add(AddStrategySellerLimitRequest request);

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
    Page<StrategySellerLimitDTO> pageList(QueryStrategySellerLimitPageRequest request);

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
    List<StrategySellerLimitDTO> listSellerByActivityId(Long strategyActivityId);

    /**
     * 根据活动id和企业id查询策略满赠商家表
     *
     * @param strategyActivityId 活动id
     * @param eidList 企业id
     * @return 活动商家信息
     */
    List<StrategySellerLimitDTO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList);


    /**
     * 根据活动id查询商家范围数量
     *
     * @param strategyActivityId 活动id
     * @return 商家范围数量
     */
    Integer countSellerByActivityIdForPayPromotion(Long strategyActivityId);

}
