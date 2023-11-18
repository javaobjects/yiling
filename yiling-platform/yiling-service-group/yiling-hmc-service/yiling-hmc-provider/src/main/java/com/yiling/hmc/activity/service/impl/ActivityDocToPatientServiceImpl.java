package com.yiling.hmc.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.hmc.activity.entity.ActivityDocToPatientDO;
import com.yiling.hmc.activity.dao.ActivityDocToPatientMapper;
import com.yiling.hmc.activity.service.ActivityDocToPatientService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * C端医带患活动 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-01-13
 */
@Service
public class ActivityDocToPatientServiceImpl extends BaseServiceImpl<ActivityDocToPatientMapper, ActivityDocToPatientDO> implements ActivityDocToPatientService {

    @Override
    public ActivityDocToPatientDO getByActivityId(Long activityId) {
        QueryWrapper<ActivityDocToPatientDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ActivityDocToPatientDO::getActivityId, activityId);
        return this.getOne(wrapper);
    }
}
