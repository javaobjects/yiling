package com.yiling.dataflow.check.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.check.entity.FlowPurchaseCheckTaskDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author: houjie.sun
 * @date: 2022/9/9
 */
@Repository
public interface FlowPurchaseCheckTaskMapper extends BaseMapper<FlowPurchaseCheckTaskDO> {

    Long getPurchaseExceptionCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
