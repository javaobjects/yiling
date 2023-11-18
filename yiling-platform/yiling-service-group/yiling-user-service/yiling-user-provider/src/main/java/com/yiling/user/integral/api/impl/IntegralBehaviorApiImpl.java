package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.api.IntegralBehaviorApi;
import com.yiling.user.integral.bo.IntegralBehaviorBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.request.QueryIntegralBehaviorRequest;
import com.yiling.user.integral.service.IntegralBehaviorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分行为 API 实现
 *
 * @author: lun.yu
 * @date: 2022-12-30
 */
@Slf4j
@DubboService
public class IntegralBehaviorApiImpl implements IntegralBehaviorApi {

    @Autowired
    IntegralBehaviorService integralBehaviorService;

    @Override
    public Page<IntegralBehaviorBO> queryListPage(QueryIntegralBehaviorRequest request) {
        return integralBehaviorService.queryListPage(request);
    }

    @Override
    public IntegralBehaviorDTO getById(Long id) {
        return PojoUtils.map(integralBehaviorService.getById(id), IntegralBehaviorDTO.class);
    }
}
