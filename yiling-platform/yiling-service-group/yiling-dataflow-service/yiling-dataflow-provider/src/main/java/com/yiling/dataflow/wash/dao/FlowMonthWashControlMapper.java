package com.yiling.dataflow.wash.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.wash.entity.FlowMonthWashControlDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向日程表 Mapper 接口
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Repository
public interface FlowMonthWashControlMapper extends BaseMapper<FlowMonthWashControlDO> {

    int countFlowMonthWashControlByDataTime(@Param("dataStartTime")Date dataStartTime,@Param("dataEndTime")Date dataEndTime);

}
