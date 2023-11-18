package com.yiling.user.shop.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.shop.dao.ShopPaymentMethodMapper;
import com.yiling.user.shop.entity.ShopPaymentMethodDO;
import com.yiling.user.shop.service.ShopPaymentMethodService;
import com.yiling.user.system.entity.PaymentMethodDO;
import com.yiling.user.system.service.PaymentMethodService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 店铺支付方式 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2021/11/17
 */
@Slf4j
@Service
public class ShopPaymentMethodServiceImpl extends BaseServiceImpl<ShopPaymentMethodMapper, ShopPaymentMethodDO> implements ShopPaymentMethodService {

    @Autowired
    PaymentMethodService paymentMethodService;

    @Override
    public List<PaymentMethodDO> listByEid(Long eid) {
        List<PaymentMethodDO> shopPaymentMethodList = this.listByEids(ListUtil.toList(eid)).get(eid);
        if (CollUtil.isEmpty(shopPaymentMethodList)) {
            return ListUtil.empty();
        }
        return shopPaymentMethodList;
    }

    @Override
    public Map<Long, List<PaymentMethodDO>> listByEids(List<Long> eids) {
        QueryWrapper<ShopPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(ShopPaymentMethodDO::getEid, eids);

        List<ShopPaymentMethodDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        // 获取所有可用支付方式
        List<PaymentMethodDO> enabledPaymentMethodList = paymentMethodService.listByPlatform(PlatformEnum.B2B, EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(enabledPaymentMethodList)) {
            return MapUtil.empty();
        }

        List<Long> enabledPaymentMethodIds = enabledPaymentMethodList.stream().map(PaymentMethodDO::getCode).collect(Collectors.toList());
        List<ShopPaymentMethodDO> shopEnabledPaymentMethodList = list.stream().filter(e -> enabledPaymentMethodIds.contains(e.getPaymentMethod().longValue())).collect(Collectors.toList());
        Map<Long, List<ShopPaymentMethodDO>> shopEnabledPaymentMethodMap = shopEnabledPaymentMethodList.stream().collect(Collectors.groupingBy(ShopPaymentMethodDO::getEid));

        Map<Long, List<PaymentMethodDO>> result = MapUtil.newHashMap();
        for (Long eid : shopEnabledPaymentMethodMap.keySet()) {
            List<Integer> shopPaymentMethodIds = shopEnabledPaymentMethodMap.get(eid).stream().map(ShopPaymentMethodDO::getPaymentMethod).distinct().collect(Collectors.toList());
            List<PaymentMethodDO> shopPaymentMethodList = enabledPaymentMethodList.stream().filter(e -> shopPaymentMethodIds.contains(e.getCode().intValue())).collect(Collectors.toList());
            result.put(eid, shopPaymentMethodList);
        }

        return result;
    }
}
