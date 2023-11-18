package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.request.SaveIntegralMultipleConfigRequest;
import com.yiling.user.integral.entity.IntegralOrderGiveMultipleConfigDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分倍数配置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
public interface IntegralOrderGiveMultipleConfigService extends BaseService<IntegralOrderGiveMultipleConfigDO> {

    /**
     * 生成订单送积分倍数配置信息
     *
     * @param giveRuleId
     * @return
     */
    List<GenerateMultipleConfigDTO> generateMultipleConfig(Long giveRuleId);

    /**
     * 保存订单积分倍数配置
     *
     * @param request
     * @return
     */
    boolean saveMultipleConfig(SaveIntegralMultipleConfigRequest request);

    /**
     * 获取订单送积分倍数配置
     *
     * @param giveRuleId
     * @return
     */
    List<GenerateMultipleConfigDTO> getMultipleConfig(Long giveRuleId);
}
