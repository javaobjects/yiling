package com.yiling.marketing.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.integral.dto.request.AddIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.DeleteIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.QueryIntegralEnterpriseGoodsPageRequest;
import com.yiling.marketing.integral.entity.IntegralGiveRuleDO;
import com.yiling.marketing.integral.entity.IntegralOrderEnterpriseGoodsDO;
import com.yiling.framework.common.base.BaseService;

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
     * 根据发放规则id和商品id，查询发放规则绑定的店铺sku
     *
     * @param giveRuleId 发放规则id
     * @param goodsIdList 商品id集合
     * @return 商品id集合
     */
    List<Long> listGoodsIdByRuleId(Long giveRuleId, List<Long> goodsIdList);

    /**
     * 添加指定店铺SKU
     *
     * @param request 添加指定平台SKU
     * @return 成功/失败
     */
    boolean add(AddIntegralGiveEnterpriseGoodsRequest request);

    /**
     * 删除指定店铺SKU
     *
     * @param request 删除指定平台SKU
     * @return 成功/失败
     */
    boolean delete(DeleteIntegralGiveEnterpriseGoodsRequest request);

    /**
     * 复制指定店铺SKU
     *
     * @param integralGiveRuleDO 发放规则主信息
     * @param oldId 被拷贝的发放规则id
     * @param opUserId 操作人
     */
    void copy(IntegralGiveRuleDO integralGiveRuleDO, Long oldId, Long opUserId);

    /**
     * 订单送积分-店铺SKU-已添加指定店铺SKU列表查询
     *
     * @param request 查询条件
     * @return 已添加指定店铺SKU列表
     */
    Page<IntegralOrderEnterpriseGoodsDO> pageList(QueryIntegralEnterpriseGoodsPageRequest request);

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

}
