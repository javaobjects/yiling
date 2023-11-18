package com.yiling.marketing.integral.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.api.IntegralGivePlatformGoodsApi;
import com.yiling.marketing.integral.entity.IntegralOrderPlatformGoodsDO;
import com.yiling.marketing.integral.service.IntegralOrderPlatformGoodsService;
import com.yiling.user.integral.dto.IntegralOrderPlatformGoodsDTO;
import com.yiling.user.integral.dto.request.AddIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.DeleteIntegralGivePlatformGoodsRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGivePlatformGoodsPageRequest;

/**
 * 订单送积分-平台SKU Api实现
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@DubboService
public class IntegralGivePlatformGoodsApiImpl implements IntegralGivePlatformGoodsApi {

    @Autowired
    IntegralOrderPlatformGoodsService platformGoodsService;

    @Override
    public boolean add(AddIntegralGivePlatformGoodsRequest request) {
        return platformGoodsService.add(request);
    }

    @Override
    public boolean delete(DeleteIntegralGivePlatformGoodsRequest request) {
        return platformGoodsService.delete(request);
    }

    @Override
    public Page<IntegralOrderPlatformGoodsDTO> pageList(QueryIntegralGivePlatformGoodsPageRequest request) {
        Page<IntegralOrderPlatformGoodsDO> doPage = platformGoodsService.pageList(request);
        return PojoUtils.map(doPage, IntegralOrderPlatformGoodsDTO.class);
    }

    @Override
    public Integer countPlatformGoodsByGiveRuleId(Long giveRuleId) {
        return platformGoodsService.countPlatformGoodsByGiveRuleId(giveRuleId);
    }


    @Override
    public List<IntegralOrderPlatformGoodsDTO> listByRuleIdAndSellSpecificationsIdList(Long giveRuleId, List<Long> sellSpecificationsIdList) {
        List<IntegralOrderPlatformGoodsDO> doList = platformGoodsService.listByRuleIdAndSellSpecificationsIdList(giveRuleId, sellSpecificationsIdList);
        return PojoUtils.map(doList, IntegralOrderPlatformGoodsDTO.class);
    }

}
