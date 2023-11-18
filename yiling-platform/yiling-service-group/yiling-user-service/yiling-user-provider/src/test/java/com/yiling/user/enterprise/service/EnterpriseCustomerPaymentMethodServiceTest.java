package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.BaseTest;
import com.yiling.user.system.entity.PaymentMethodDO;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/7/13
 */
@Slf4j
public class EnterpriseCustomerPaymentMethodServiceTest extends BaseTest {

    @Autowired
    private EnterpriseCustomerPaymentMethodService enterpriseCustomerPaymentMethodService;

    @Test
    public void listByCustomerEidAndEids() {
        Long customerEid = 6L;
        List<Long> eids = ListUtil.toList(2L);

        Map<Long, List<PaymentMethodDO>> map = enterpriseCustomerPaymentMethodService.listByCustomerEidAndEids(customerEid, eids, PlatformEnum.POP);
        log.info("map = {}", JSONUtil.toJsonStr(map));
    }
}
