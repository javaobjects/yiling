package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.paypromotion.dto.PayPromotionEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.api.StrategyEnterpriseGoodsApi;
import com.yiling.marketing.strategy.dto.StrategyEnterpriseGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyEnterpriseGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyEnterpriseGoodsLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyEnterpriseGoodsLimitDO;
import com.yiling.marketing.strategy.service.StrategyEnterpriseGoodsLimitService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠-店铺SKUApi
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyEnterpriseGoodsApiImpl implements StrategyEnterpriseGoodsApi {

    private final StrategyEnterpriseGoodsLimitService enterpriseGoodsLimitService;

    @Override
    public List<Long> listGoodsIdByStrategyId(Long strategyActivityId, List<Long> goodsIdList) {
        return enterpriseGoodsLimitService.listGoodsIdByStrategyId(strategyActivityId, goodsIdList);
    }

    @Override
    public boolean add(AddStrategyEnterpriseGoodsLimitRequest request) {
        return enterpriseGoodsLimitService.add(request);
    }
    @Override
    public boolean addForPayPromotion(AddStrategyEnterpriseGoodsLimitRequest request) {
        return enterpriseGoodsLimitService.addForPayPromotion(request);
    }

    @Override
    public boolean deleteForPromotion(DeleteStrategyEnterpriseGoodsLimitRequest request) {
        return enterpriseGoodsLimitService.deleteForPromotion(request);
    }

    @Override
    public Page<PayPromotionEnterpriseGoodsLimitDTO> pageListForPayPromotion(QueryStrategyEnterpriseGoodsLimitPageRequest request) {
        Page<PayPromotionEnterpriseGoodsLimitDTO> doPage = enterpriseGoodsLimitService.pageListForPayPromotion(request);
        return PojoUtils.map(doPage, PayPromotionEnterpriseGoodsLimitDTO.class);
    }

    @Override
    public boolean delete(DeleteStrategyEnterpriseGoodsLimitRequest request) {
        return enterpriseGoodsLimitService.delete(request);
    }

    @Override
    public Page<StrategyEnterpriseGoodsLimitDTO> pageList(QueryStrategyEnterpriseGoodsLimitPageRequest request) {
        Page<StrategyEnterpriseGoodsLimitDO> doPage = enterpriseGoodsLimitService.pageList(request);
        return PojoUtils.map(doPage, StrategyEnterpriseGoodsLimitDTO.class);
    }

    @Override
    public Integer countEnterpriseGoodsByActivityId(Long strategyActivityId) {
        return enterpriseGoodsLimitService.countEnterpriseGoodsByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyEnterpriseGoodsLimitDTO> listEnterpriseGoodsByActivityId(Long strategyActivityId) {
        List<StrategyEnterpriseGoodsLimitDO> doList = enterpriseGoodsLimitService.listEnterpriseGoodsByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyEnterpriseGoodsLimitDTO.class);
    }

    @Override
    public List<StrategyEnterpriseGoodsLimitDTO> listByActivityIdAndGoodsIdList(Long strategyActivityId, List<Long> goodsIdList) {
        List<StrategyEnterpriseGoodsLimitDO> doList = enterpriseGoodsLimitService.listByActivityIdAndGoodsIdList(strategyActivityId, goodsIdList);
        return PojoUtils.map(doList, StrategyEnterpriseGoodsLimitDTO.class);
    }

    @Override
    public Integer countEnterpriseGoodsByActivityIdForPayPromotion(Long strategyActivityId) {
        return enterpriseGoodsLimitService.countEnterpriseGoodsByActivityIdForPayPromotion(strategyActivityId);
    }
}
