package com.yiling.user.integral.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;
import com.yiling.user.integral.entity.IntegralPeriodConfigDO;
import com.yiling.user.integral.dao.IntegralPeriodConfigMapper;
import com.yiling.user.integral.service.IntegralPeriodConfigService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分周期配置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Service
public class IntegralPeriodConfigServiceImpl extends BaseServiceImpl<IntegralPeriodConfigMapper, IntegralPeriodConfigDO> implements IntegralPeriodConfigService {

    @Override
    public List<IntegralPeriodConfigDTO> getIntegralPeriodConfigList(Long giveRuleId) {
        LambdaQueryWrapper<IntegralPeriodConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralPeriodConfigDO::getGiveRuleId, giveRuleId);
        wrapper.orderByAsc(IntegralPeriodConfigDO::getDays);
        return PojoUtils.map(this.list(wrapper), IntegralPeriodConfigDTO.class);
    }
}
