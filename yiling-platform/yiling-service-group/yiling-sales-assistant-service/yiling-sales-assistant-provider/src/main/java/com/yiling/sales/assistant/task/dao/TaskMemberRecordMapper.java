package com.yiling.sales.assistant.task.dao;

import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.sales.assistant.task.entity.TaskMemberRecordDO;

/**
 * <p>
 * 会员推广任务记录 Dao 接口
 * </p>
 *
 * @author gxl
 * @date 2021-12-21
 */
@Repository
public interface TaskMemberRecordMapper extends BaseMapper<TaskMemberRecordDO> {
    /**
     * 会员退款后逻辑删除
     * @param memberRecordDO
     * @return
     */
    int updateRecordById(TaskMemberRecordDO memberRecordDO);
}
