package com.yiling.user.integral.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.integral.dto.IntegralOrderPlatformGoodsDTO;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderPlatformGoodsDO;

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
     * @param giveRuleId 规则id
     * @return 指定平台SKU
     */
    List<IntegralOrderPlatformGoodsDO> listPlatformGoodsByGiveRuleId(Long giveRuleId);

    /**
     * 根据发放规则ID查询指定平台SKU
     *
     * @param giveRuleIdList 规则id
     * @return 指定平台SKU
     */
    Map<Long, List<IntegralOrderPlatformGoodsDTO>> getMapPlatformGoodsByGiveRuleIdList(List<Long> giveRuleIdList);

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
     * 根据规则ID集合和商品标准库ID集合查询指定平台SKU
     *
     * @param giveRuleId
     * @param standardIdList
     * @return
     */
    List<IntegralOrderPlatformGoodsDO> listByRuleIdAndStandardIdList(Long giveRuleId, List<Long> standardIdList);

    /**
     * 根据规则ID集合和商品标准库ID集合查询指定平台SKU
     *
     * @param giveRuleIdList
     * @param sellSpecificationsIdList
     * @return
     */
    List<IntegralOrderPlatformGoodsDO> listByRuleIdListAndSpecificationsIdList(List<Long> giveRuleIdList, List<Long> sellSpecificationsIdList);

}
