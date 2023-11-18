package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.enterprise.service.EnterpriseChannelPaymentMethodService;
import com.yiling.user.enterprise.service.EnterpriseCustomerPaymentMethodService;
import com.yiling.user.shop.service.ShopPaymentMethodService;
import com.yiling.user.system.dto.PaymentMethodDTO;
import com.yiling.user.system.entity.PaymentMethodDO;
import com.yiling.user.system.service.PaymentMethodService;

/**
 * 支付方式 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/30
 */
@DubboService
public class PaymentMethodApiImpl implements PaymentMethodApi {

    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    EnterpriseChannelPaymentMethodService enterpriseChannelPaymentMethodService;
    @Autowired
    EnterpriseCustomerPaymentMethodService enterpriseCustomerPaymentMethodService;
    @Autowired
    ShopPaymentMethodService shopPaymentMethodService;

    @Override
    public List<PaymentMethodDTO> listByPlatform(PlatformEnum platformEnum) {
        List<PaymentMethodDO> list = paymentMethodService.listByPlatform(platformEnum, EnableStatusEnum.ENABLED);
        return PojoUtils.map(list, PaymentMethodDTO.class);
    }

    @Override
    public List<PaymentMethodDTO> listByChannelId(Long channelId) {
        List<PaymentMethodDO> list = enterpriseChannelPaymentMethodService.listByChannelId(channelId);
        return PojoUtils.map(list, PaymentMethodDTO.class);
    }

    @Override
    public List<PaymentMethodDTO> listByShopEid(Long eid) {
        List<PaymentMethodDO> list = shopPaymentMethodService.listByEid(eid);
        return PojoUtils.map(list, PaymentMethodDTO.class);
    }

    @Override
    public Map<Long, List<PaymentMethodDTO>> listByEidAndCustomerEids(Long eid, List<Long> customerEids, PlatformEnum platformEnum) {
        Map<Long, List<PaymentMethodDO>> map = enterpriseCustomerPaymentMethodService.listByEidAndCustomerEids(eid, customerEids, platformEnum);
        return PojoUtils.map(map, PaymentMethodDTO.class);
    }

    @Override
    public Map<Long, List<PaymentMethodDTO>> listByCustomerEidAndEids(Long customerEid, List<Long> eids, PlatformEnum platformEnum) {
        Map<Long, List<PaymentMethodDO>> map = enterpriseCustomerPaymentMethodService.listByCustomerEidAndEids(customerEid, eids, platformEnum);
        return PojoUtils.map(map, PaymentMethodDTO.class);
    }

    @Override
    public List<PaymentMethodDTO> listByEidAndCustomerEid(Long eid, Long customerEid, PlatformEnum platformEnum) {
        return PojoUtils.map(enterpriseCustomerPaymentMethodService.listByEidAndCustomerEid(eid, customerEid, platformEnum), PaymentMethodDTO.class);
    }

}
