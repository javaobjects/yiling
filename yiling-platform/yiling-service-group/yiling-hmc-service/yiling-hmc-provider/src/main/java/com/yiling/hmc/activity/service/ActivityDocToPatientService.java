package com.yiling.hmc.activity.service;

import com.yiling.hmc.activity.entity.ActivityDocToPatientDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * C端医带患活动 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-01-13
 */
public interface ActivityDocToPatientService extends BaseService<ActivityDocToPatientDO> {

    ActivityDocToPatientDO getByActivityId(Long activityId);
}
