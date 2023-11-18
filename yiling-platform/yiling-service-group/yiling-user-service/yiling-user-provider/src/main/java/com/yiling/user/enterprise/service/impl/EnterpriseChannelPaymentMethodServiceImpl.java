package com.yiling.user.enterprise.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.enterprise.dao.EnterpriseChannelPaymentMethodMapper;
import com.yiling.user.enterprise.entity.EnterpriseChannelPaymentMethodDO;
import com.yiling.user.enterprise.service.EnterpriseChannelPaymentMethodService;
import com.yiling.user.system.entity.PaymentMethodDO;
import com.yiling.user.system.service.PaymentMethodService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * 渠道与支付方式关联表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
public class EnterpriseChannelPaymentMethodServiceImpl extends BaseServiceImpl<EnterpriseChannelPaymentMethodMapper, EnterpriseChannelPaymentMethodDO> implements EnterpriseChannelPaymentMethodService {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Override
    public List<PaymentMethodDO> listByChannelId(Long channelId) {
        List<PaymentMethodDO> channelPaymentMethodList = this.mapByChannelIds(ListUtil.toList(channelId)).get(channelId);
        if (CollUtil.isEmpty(channelPaymentMethodList)) {
            return ListUtil.empty();
        }
        return channelPaymentMethodList;
    }

    @Override
    public Map<Long, List<PaymentMethodDO>> mapByChannelIds(List<Long> channelIds) {
        QueryWrapper<EnterpriseChannelPaymentMethodDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(EnterpriseChannelPaymentMethodDO::getChannelId, channelIds);
        List<EnterpriseChannelPaymentMethodDO> list = this.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            return MapUtil.empty();
        }

        List<PaymentMethodDO> enabledPaymentMethodList = paymentMethodService.listByPlatform(PlatformEnum.POP, EnableStatusEnum.ENABLED);
        if (CollUtil.isEmpty(enabledPaymentMethodList)) {
            return MapUtil.empty();
        }

        List<Long> enabledPaymentMethodIds = enabledPaymentMethodList.stream().map(PaymentMethodDO::getCode).collect(Collectors.toList());
        List<EnterpriseChannelPaymentMethodDO> channelEnabledPaymentMethodList = list.stream().filter(e -> enabledPaymentMethodIds.contains(e.getPaymentMethodId())).collect(Collectors.toList());
        Map<Long, List<EnterpriseChannelPaymentMethodDO>> channelEnabledPaymentMethodMap = channelEnabledPaymentMethodList.stream().collect(Collectors.groupingBy(EnterpriseChannelPaymentMethodDO::getChannelId));

        Map<Long, List<PaymentMethodDO>> result = MapUtil.newHashMap();
        for (Long channelId : channelEnabledPaymentMethodMap.keySet()) {
            List<Long> channelPayMethodIds = channelEnabledPaymentMethodMap.get(channelId).stream().map(EnterpriseChannelPaymentMethodDO::getPaymentMethodId).distinct().collect(Collectors.toList());
            List<PaymentMethodDO> channelPaymentMethodList = enabledPaymentMethodList.stream().filter(e -> channelPayMethodIds.contains(e.getCode())).collect(Collectors.toList());
            result.put(channelId, channelPaymentMethodList);
        }

        return result;
    }

}
