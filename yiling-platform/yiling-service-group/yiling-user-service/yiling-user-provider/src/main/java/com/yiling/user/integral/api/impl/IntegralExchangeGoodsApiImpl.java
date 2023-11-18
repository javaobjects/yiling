package com.yiling.user.integral.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralExchangeGoodsApi;
import com.yiling.user.integral.bo.IntegralExchangeGoodsItemBO;
import com.yiling.user.integral.dto.IntegralExchangeGoodsDTO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeGoodsPageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralExchangeGoodsRequest;
import com.yiling.user.integral.dto.request.UpdateShelfStatusRequest;
import com.yiling.user.integral.entity.IntegralExchangeGoodsMemberDO;
import com.yiling.user.integral.service.IntegralExchangeGoodsMemberService;
import com.yiling.user.integral.service.IntegralExchangeGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换商品 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
@Slf4j
@DubboService
public class IntegralExchangeGoodsApiImpl implements IntegralExchangeGoodsApi {

    @Autowired
    IntegralExchangeGoodsService integralExchangeGoodsService;
    @Autowired
    IntegralExchangeGoodsMemberService integralExchangeGoodsMemberService;

    @Override
    public Page<IntegralExchangeGoodsItemBO> queryListPage(QueryIntegralExchangeGoodsPageRequest request) {
        return integralExchangeGoodsService.queryListPage(request);
    }

    @Override
    public boolean updateStatus(UpdateShelfStatusRequest request) {
        return integralExchangeGoodsService.updateStatus(request);
    }

    @Override
    public boolean saveExchangeGoods(SaveIntegralExchangeGoodsRequest request) {
        return integralExchangeGoodsService.saveExchangeGoods(request);
    }

    @Override
    public IntegralExchangeGoodsDTO getById(Long id) {
        return PojoUtils.map(integralExchangeGoodsService.getById(id), IntegralExchangeGoodsDTO.class);
    }

    @Override
    public List<Long> getMemberByExchangeGoodsId(Long exchangeGoodsId) {
        return integralExchangeGoodsMemberService.getMemberByExchangeGoodsId(exchangeGoodsId);
    }

    @Override
    public Map<Long, List<Long>> getMemberByExchangeGoodsIdList(List<Long> exchangeGoodsIdList) {
        return integralExchangeGoodsMemberService.getMemberByExchangeGoodsIdList(exchangeGoodsIdList);
    }

}
