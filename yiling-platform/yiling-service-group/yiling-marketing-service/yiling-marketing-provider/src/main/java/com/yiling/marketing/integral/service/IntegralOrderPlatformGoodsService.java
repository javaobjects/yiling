package com.yiling.marketing.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.integral.entity.IntegralGiveRuleDO;
import com.yiling.marketing.integral.entity.IntegralOrderPlatformGoodsDO;
import com.yiling.marketing.strategy.entity.StrategyPlatformGoodsLimitDO;
import com.yiling.user.integral.dto.request.AddIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGivePlatformGoodsPageRequest;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分平台SKU表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderPlatformGoodsService extends BaseService<IntegralOrderPlatformGoodsDO> {

    /**
     * 添加指定平台SKU
     *
     * @param request 添加指定平台SKU
     * @return 成功/失败
     */
    boolean add(AddIntegralGivePlatformGoodsRequest request);

    /**
     * 删除指定平台SKU
     *
     * @param request 删除指定平台SKU
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGivePlatformGoodsRequest request);

    /**
     * 订单送积分-平台SKU-已添加指定平台SKU分页列表查询
     *
     * @param request 查询条件
     * @return 已添加指定平台SKU列表
     */
    Page<IntegralOrderPlatformGoodsDO> pageList(QueryIntegralGivePlatformGoodsPageRequest request);

    /**
     * 复制平台SKU
     *
     * @param integralGiveRuleDO
     * @param oldId
     * @param opUserId
     */
    void copy(IntegralGiveRuleDO integralGiveRuleDO, Long oldId, Long opUserId);

    /**
     * 根据发放规则ID查询指定平台SKU数量
     *
     * @param giveRuleId 发放规则ID
     * @return 指定平台SKU数量
     */
    Integer countPlatformGoodsByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则ID查询指定平台SKU
     *
     * @param giveRuleId 活动id
     * @return 指定平台SKU
     */
    List<IntegralOrderPlatformGoodsDO> listPlatformGoodsByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则ID和商品标准库ID集合查询指定平台SKU
     *
     * @param giveRuleId 发放规则ID
     * @param sellSpecificationsIdList 规格ID集合
     * @return 指定平台SKU
     */
    List<IntegralOrderPlatformGoodsDO> listByRuleIdAndSellSpecificationsIdList(Long giveRuleId, List<Long> sellSpecificationsIdList);

    /**
     * 根据发放规则ID和商品规格id查询指定平台SKU
     *
     * @param giveRuleId 发放规则ID
     * @param sellSpecificationsId 规格id
     * @return 指定平台SKU
     */
    IntegralOrderPlatformGoodsDO queryByRuleIdAndSellSpecificationsId(Long giveRuleId, Long sellSpecificationsId);

    /**
     * 根据活动id和商品标准库ID集合查询指定平台SKU
     *
     * @param giveRuleId
     * @param standardIdList
     * @return
     */
    List<IntegralOrderPlatformGoodsDO> listByRuleIdAndStandardIdList(Long giveRuleId, List<Long> standardIdList);

}
