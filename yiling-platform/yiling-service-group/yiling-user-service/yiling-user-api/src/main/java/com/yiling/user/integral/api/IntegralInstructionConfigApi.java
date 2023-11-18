package com.yiling.user.integral.api;

import com.yiling.user.integral.dto.IntegralInstructionConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralInstructionConfigRequest;

/**
 * 积分说明配置 API
 *
 * @author: lun.yu
 * @date: 2023-01-11
 */
public interface IntegralInstructionConfigApi {

    /**
     * 保存积分说明配置
     *
     * @param request
     * @return
     */
    boolean saveConfig(SaveIntegralInstructionConfigRequest request);

    /**
     * 获取积分说明配置
     *
     * @param id
     * @param platform
     * @return
     */
    IntegralInstructionConfigDTO get(Long id, Integer platform);

}
