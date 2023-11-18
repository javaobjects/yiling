package com.yiling.dataflow.wash.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryFlowMonthWashTaskPageRequest;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向清洗任务表 Mapper 接口
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Repository
public interface FlowMonthWashTaskMapper extends BaseMapper<FlowMonthWashTaskDO> {

    Page<FlowMonthWashTaskDO> listPage(@Param("request") QueryFlowMonthWashTaskPageRequest request, Page<FlowMonthWashTaskDO> page);

    List<Long> getCrmEnterIdsByFmwcIdAndClassify(@Param("fmwcId")Long fmwcId,@Param("flowClassify")Integer flowClassify);
}
