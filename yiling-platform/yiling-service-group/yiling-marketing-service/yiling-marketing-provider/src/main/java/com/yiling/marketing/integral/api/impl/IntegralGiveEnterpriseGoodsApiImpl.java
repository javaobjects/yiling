package com.yiling.marketing.integral.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.api.IntegralGiveEnterpriseGoodsApi;
import com.yiling.marketing.integral.dto.request.AddIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.DeleteIntegralGiveEnterpriseGoodsRequest;
import com.yiling.marketing.integral.dto.request.QueryIntegralEnterpriseGoodsPageRequest;
import com.yiling.marketing.integral.entity.IntegralOrderEnterpriseGoodsDO;
import com.yiling.marketing.integral.service.IntegralOrderEnterpriseGoodsService;
import com.yiling.user.integral.dto.IntegralOrderEnterpriseGoodsDTO;

/**
 * 订单送积分-店铺SKU Api实现
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@DubboService
public class IntegralGiveEnterpriseGoodsApiImpl implements IntegralGiveEnterpriseGoodsApi {

    @Autowired
    IntegralOrderEnterpriseGoodsService enterpriseGoodsService;

    @Override
    public List<Long> listGoodsIdByRuleId(Long strategyActivityId, List<Long> goodsIdList) {
        return enterpriseGoodsService.listGoodsIdByRuleId(strategyActivityId, goodsIdList);
    }

    @Override
    public boolean add(AddIntegralGiveEnterpriseGoodsRequest request) {
        return enterpriseGoodsService.add(request);
    }

    @Override
    public boolean delete(DeleteIntegralGiveEnterpriseGoodsRequest request) {
        return enterpriseGoodsService.delete(request);
    }

    @Override
    public Page<IntegralOrderEnterpriseGoodsDTO> pageList(QueryIntegralEnterpriseGoodsPageRequest request) {
        Page<IntegralOrderEnterpriseGoodsDO> doPage = enterpriseGoodsService.pageList(request);
        return PojoUtils.map(doPage, IntegralOrderEnterpriseGoodsDTO.class);
    }

    @Override
    public Integer countEnterpriseGoodsByRuleId(Long giveRuleId) {
        return enterpriseGoodsService.countEnterpriseGoodsByRuleId(giveRuleId);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDTO> listEnterpriseGoodsByRuleId(Long strategyActivityId) {
        List<IntegralOrderEnterpriseGoodsDO> doList = enterpriseGoodsService.listEnterpriseGoodsByRuleId(strategyActivityId);
        return PojoUtils.map(doList, IntegralOrderEnterpriseGoodsDTO.class);
    }

    @Override
    public List<IntegralOrderEnterpriseGoodsDTO> listByRuleIdAndGoodsIdList(Long strategyActivityId, List<Long> goodsIdList) {
        List<IntegralOrderEnterpriseGoodsDO> doList = enterpriseGoodsService.listByRuleIdAndGoodsIdList(strategyActivityId, goodsIdList);
        return PojoUtils.map(doList, IntegralOrderEnterpriseGoodsDTO.class);
    }
}
