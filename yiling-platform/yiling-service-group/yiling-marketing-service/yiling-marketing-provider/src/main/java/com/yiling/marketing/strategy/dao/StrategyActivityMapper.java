package com.yiling.marketing.strategy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;

/**
 * <p>
 * 营销活动主表 Dao 接口
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Repository
public interface StrategyActivityMapper extends BaseMapper<StrategyActivityDO> {

    /**
     * 营销活动分页查询
     *
     * @param page 分页信息
     * @param request 查询条件
     * @return 营销活动主表信息
     */
    Page<StrategyActivityDO> pageList(Page<StrategyActivityDO> page, @Param("request") QueryStrategyActivityPageRequest request);

    /**
     * 查看正在进行中有效的活动，同时包含某类会员
     *
     * @param stageMemberId 会员-时间维度
     * @param platformSelected 选择平台（1-B2B；2-销售助手）逗号隔开
     * @return 策略满赠活动
     */
    List<StrategyActivityDO> listByStageMemberId(@Param("stageMemberId") Long stageMemberId, @Param("platformSelected") String platformSelected);

    /**
     * 根据赠品查询此赠品的活动信息
     *
     * @param request 查询条件
     * @return 活动信息
     */
    List<StrategyActivityDO> listStrategyByGiftId(@Param("request") QueryLotteryStrategyRequest request);

    /**
     * 根据赠品分页查询此赠品的活动信息
     *
     * @param page 分页信息
     * @param request 查询条件
     * @return 活动信息
     */
    Page<StrategyActivityDO> pageLotteryStrategy(Page<StrategyActivityDO> page, @Param("request") QueryLotteryStrategyPageRequest request);

    /**
     * 根据赠品查询此赠品的活动信息数量
     *
     * @param lotteryActivityId 抽奖活动id
     * @return 活动信息数量
     */
    Integer countStrategyByGiftId(@Param("lotteryActivityId") Long lotteryActivityId);
}
