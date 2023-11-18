package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.paypromotion.dto.PayPromotionPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.api.StrategyPlatformGoodsApi;
import com.yiling.marketing.strategy.dto.StrategyPlatformGoodsLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPlatformGoodsLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPlatformGoodsLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyPlatformGoodsLimitDO;
import com.yiling.marketing.strategy.service.StrategyPlatformGoodsLimitService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠-平台SKUApi
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyPlatformGoodsApiImpl implements StrategyPlatformGoodsApi {

    private final StrategyPlatformGoodsLimitService platformGoodsLimitService;

    @Override
    public List<Long> listSellSpecificationsIdByStrategyId(Long strategyActivityId, List<Long> sellSpecificationsIdList) {
        return platformGoodsLimitService.listSellSpecificationsIdByStrategyId(strategyActivityId, sellSpecificationsIdList);
    }

    @Override
    public boolean add(AddStrategyPlatformGoodsLimitRequest request) {
        return platformGoodsLimitService.add(request);
    }

    @Override
    public boolean addForPayPromotion(AddStrategyPlatformGoodsLimitRequest request) {
        return platformGoodsLimitService.addForPayPromotion(request);
    }

    @Override
    public boolean delete(DeleteStrategyPlatformGoodsLimitRequest request) {
        return platformGoodsLimitService.delete(request);
    }
    @Override
    public boolean deleteForPayPromotionPlatformSku(DeleteStrategyPlatformGoodsLimitRequest request) {
        return platformGoodsLimitService.deleteForPayPromotionPlatformSku(request);
    }
    @Override
    public Page<StrategyPlatformGoodsLimitDTO> pageList(QueryStrategyPlatformGoodsLimitPageRequest request) {
        Page<StrategyPlatformGoodsLimitDO> doPage = platformGoodsLimitService.pageList(request);
        return PojoUtils.map(doPage, StrategyPlatformGoodsLimitDTO.class);
    }

    @Override
    public Page<PayPromotionPlatformGoodsLimitDTO> pageListFroPayPromotion(QueryStrategyPlatformGoodsLimitPageRequest request) {
        Page<MarketingPayPlatformGoodsLimitDO> doPage = platformGoodsLimitService.pageListFroPayPromotion(request);
        return PojoUtils.map(doPage, PayPromotionPlatformGoodsLimitDTO.class);
    }
    @Override
    public Integer countPlatformGoodsByActivityId(Long strategyActivityId) {
        return platformGoodsLimitService.countPlatformGoodsByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyPlatformGoodsLimitDTO> listPlatformGoodsByActivityId(Long strategyActivityId) {
        List<StrategyPlatformGoodsLimitDO> doList = platformGoodsLimitService.listPlatformGoodsByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyPlatformGoodsLimitDTO.class);
    }

    @Override
    public List<StrategyPlatformGoodsLimitDTO> listByActivityIdAndSellSpecificationsIdList(Long strategyActivityId, List<Long> sellSpecificationsIdList) {
        List<StrategyPlatformGoodsLimitDO> doList = platformGoodsLimitService.listByActivityIdAndSellSpecificationsIdList(strategyActivityId, sellSpecificationsIdList);
        return PojoUtils.map(doList, StrategyPlatformGoodsLimitDTO.class);
    }

    @Override
    public List<StrategyPlatformGoodsLimitDTO> listByActivityIdAndStandardIdList(Long strategyActivityId, List<Long> standardIdList) {
        List<StrategyPlatformGoodsLimitDO> doList = platformGoodsLimitService.listByActivityIdAndStandardIdList(strategyActivityId, standardIdList);
        return PojoUtils.map(doList, StrategyPlatformGoodsLimitDTO.class);
    }

    @Override
    public Integer countPlatformGoodsByActivityIdForPayPromotion(Long strategyActivityId) {
        return platformGoodsLimitService.countPlatformGoodsByActivityIdForPayPromotion(strategyActivityId);
    }
}
