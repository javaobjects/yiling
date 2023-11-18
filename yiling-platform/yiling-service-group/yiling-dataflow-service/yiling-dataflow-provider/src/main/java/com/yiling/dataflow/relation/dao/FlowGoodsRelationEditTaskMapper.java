package com.yiling.dataflow.relation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsRelationTaskPageRequest;
import com.yiling.dataflow.relation.entity.FlowGoodsRelationEditTaskDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author: houjie.sun
 * @date: 2022/10/8
 */
@Repository
public interface FlowGoodsRelationEditTaskMapper extends BaseMapper<FlowGoodsRelationEditTaskDO> {

    Page<FlowGoodsRelationEditTaskDO> page(Page page, @Param("request") QueryFlowGoodsRelationTaskPageRequest request);

    Long getCountByEid(@Param("flowGoodsRelationId") Long flowGoodsRelationId, @Param("syncStatus") Integer syncStatus);

    Integer updateSyncStatusByIdList(@Param("idList") List<Long> idList, @Param("syncStatus") Integer syncStatus);

}
