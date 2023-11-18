package com.yiling.user.integral.service;

import com.yiling.user.integral.dto.IntegralInstructionConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralInstructionConfigRequest;
import com.yiling.user.integral.entity.IntegralInstructionConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分说明配置 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
public interface IntegralInstructionConfigService extends BaseService<IntegralInstructionConfigDO> {

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
