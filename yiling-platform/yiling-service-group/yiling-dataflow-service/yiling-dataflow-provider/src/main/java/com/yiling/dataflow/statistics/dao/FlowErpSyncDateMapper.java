package com.yiling.dataflow.statistics.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.statistics.entity.FlowErpSyncDateDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author: houjie.sun
 * @date: 2023/1/3
 */
@Repository
public interface FlowErpSyncDateMapper extends BaseMapper<FlowErpSyncDateDO> {

    Integer deleteByEidAndTaskTime(@Param("eid") Long eid, @Param("taskTime") String taskTime);

    Integer deleteByEidListAndTaskTime(@Param("eidList") List<Long> eidList, @Param("taskTime") String taskTime);

    Date getMaxTaskTimeByEid(@Param("eid") Long eid);

}
