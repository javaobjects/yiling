package com.yiling.user.shop.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.system.entity.PaymentMethodDO;

/**
 * 店铺支付方式 Service
 *
 * @author: xuan.zhou
 * @date: 2021/11/17
 */
public interface ShopPaymentMethodService {

    /**
     * 获取店铺支付方式列表
     *
     * @param eid 店铺企业ID
     * @return
     */
    List<PaymentMethodDO> listByEid(Long eid);

    /**
     * 批量获取店铺支付方式列表
     *
     * @param eids 店铺企业ID列表
     * @return
     */
    Map<Long, List<PaymentMethodDO>> listByEids(List<Long> eids);
}
