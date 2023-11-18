package com.yiling.dataflow.flow.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.flow.entity.FlowBiTaskDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-04-06
 */
@Repository
public interface FlowBiTaskMapper extends BaseMapper<FlowBiTaskDO> {

    /**
     * 根据任务时间物理删除
     *
     * @param taskTimeEnd
     * @param eid
     * @return
     */
    Integer deleteByTaskTime(@Param("taskTimeEnd") String taskTimeEnd, @Param("eid") Long eid);
}
