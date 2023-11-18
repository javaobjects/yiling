package com.yiling.sales.assistant.task.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sales.assistant.task.dto.TaskMemberPromotiondDTO;
import com.yiling.sales.assistant.task.entity.TaskMemberPromotionDO;

/**
 * <p>
 * 会员推广-买赠任务 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-12-16
 */
public interface TaskMemberPromotionService extends BaseService<TaskMemberPromotionDO> {
    /**
     * 根据任务id
     * @param taskId
     * @return
     */
     TaskMemberPromotiondDTO getMemberPromotion(Long taskId);
}
