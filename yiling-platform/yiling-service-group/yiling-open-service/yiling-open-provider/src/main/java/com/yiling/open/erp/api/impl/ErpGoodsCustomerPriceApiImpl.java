package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.api.ErpGoodsCustomerPriceApi;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;

/**
 * @author: shuang.zhang
 * @date: 2021/8/16
 */
@DubboService
public class ErpGoodsCustomerPriceApiImpl implements ErpGoodsCustomerPriceApi {

    @Autowired
    ErpGoodsCustomerPriceService  erpGoodsCustomerPriceService;

    @Override
    public Boolean syncEasGoodsCustomerPrice() {
        return erpGoodsCustomerPriceService.syncEasGoodsCustomerPrice();
    }

    @Override
    public void syncGoodsCustomerPrice() {
        erpGoodsCustomerPriceService.syncGoodsCustomerPrice();
    }

    @Override
    public Boolean syncDayunheGoodsCustomerPrice() {
        return erpGoodsCustomerPriceService.syncDayunheGoodsCustomerPrice();
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsCustomerPriceService.updateOperTypeGoodsBatchFlowBySuId(suId);
    }
}
