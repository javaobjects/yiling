package com.yiling.user.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.integral.api.IntegralInstructionConfigApi;
import com.yiling.user.integral.api.IntegralRecordApi;
import com.yiling.user.integral.bo.IntegralGiveUseRecordBO;
import com.yiling.user.integral.dto.IntegralInstructionConfigDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRecordRequest;
import com.yiling.user.integral.dto.request.SaveIntegralInstructionConfigRequest;
import com.yiling.user.integral.service.IntegralGiveUseRecordService;
import com.yiling.user.integral.service.IntegralInstructionConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分发放/扣减记录 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-10
 */
@Slf4j
@DubboService
public class IntegralInstructionConfigApiImpl implements IntegralInstructionConfigApi {

    @Autowired
    IntegralInstructionConfigService integralInstructionConfigService;

    @Override
    public boolean saveConfig(SaveIntegralInstructionConfigRequest request) {
        return integralInstructionConfigService.saveConfig(request);
    }

    @Override
    public IntegralInstructionConfigDTO get(Long id, Integer platform) {
        return integralInstructionConfigService.get(id, platform);
    }
}
