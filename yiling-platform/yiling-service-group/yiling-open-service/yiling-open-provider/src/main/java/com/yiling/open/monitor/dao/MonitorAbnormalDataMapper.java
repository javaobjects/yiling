package com.yiling.open.monitor.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.monitor.entity.MonitorAbnormalDataDO;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-08
 */
@Repository
public interface MonitorAbnormalDataMapper extends BaseMapper<MonitorAbnormalDataDO> {

    Long getSaleExceptionCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
