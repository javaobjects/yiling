package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralOrderEnterpriseGoodsDO;

/**
 * <p>
 * 订单送积分-店铺SKU表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderEnterpriseGoodsService extends BaseService<IntegralOrderEnterpriseGoodsDO> {

    /**
     * 复制指定店铺SKU
     *
     * @param integralGiveRuleDO 发放规则主信息
     * @param oldId 被拷贝的发放规则id
     * @param opUserId 操作人
     */
    void copy(IntegralGiveRuleDO integralGiveRuleDO, Long oldId, Long opUserId);

    /**
     * 根据发放规则id查询指定店铺SKU数量
     *
     * @param giveRuleId 发放规则id
     * @return 指定店铺SKU数量
     */
    Integer countEnterpriseGoodsByRuleId(Long giveRuleId);

    /**
     * 根据发放规则id查询指定店铺SKU
     *
     * @param giveRuleId 发放规则id
     * @return 指定店铺SKU
     */
    List<IntegralOrderEnterpriseGoodsDO> listEnterpriseGoodsByRuleId(Long giveRuleId);

    /**
     * 根据发放规则id和商品id查询指定店铺SKU
     *
     * @param giveRuleId 发放规则id
     * @param goodsId 商品id
     * @return 指定店铺SKU
     */
    IntegralOrderEnterpriseGoodsDO queryByRuleIdAndGoodsId(Long giveRuleId, Long goodsId);

    /**
     * 根据发放规则id和商品id集合查询指定店铺SKU
     *
     * @param giveRuleId 发放规则id
     * @param goodsIdList 商品id集合
     * @return 指定店铺SKU
     */
    List<IntegralOrderEnterpriseGoodsDO> listByRuleIdAndGoodsIdList(Long giveRuleId, List<Long> goodsIdList);

    /**
     * 根据发放规则id查询指定店铺SKU
     *
     * @param giveRuleIdList 发放规则id
     * @return 指定店铺SKU
     */
    List<IntegralOrderEnterpriseGoodsDO> listEnterpriseGoodsByRuleIdList(List<Long> giveRuleIdList);

}
