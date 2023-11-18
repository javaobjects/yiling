package com.yiling.hmc.remind.service;

import com.yiling.hmc.remind.entity.MedsRemindTaskDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 用药提醒任务表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
public interface MedsRemindTaskService extends BaseService<MedsRemindTaskDO> {

    /**
     * 根据任务id批量查询
     * @param remindTaskIdList
     */
    List<MedsRemindTaskDO> batchQueryByIdList(List<Long> remindTaskIdList);
}
