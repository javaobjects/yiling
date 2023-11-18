package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.api.ErpGoodsGroupPriceApi;
import com.yiling.open.erp.service.ErpGoodsGroupPriceService;

/**
 * @author: shuang.zhang
 * @date: 2021/8/16
 */
@DubboService
public class ErpGoodsGroupPriceApiImpl implements ErpGoodsGroupPriceApi {

    @Autowired
    ErpGoodsGroupPriceService erpGoodsGroupPriceService;

    @Override
    public void syncGoodsGroupPrice() {
        erpGoodsGroupPriceService.syncGoodsGroupPrice();
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsGroupPriceService.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

}
