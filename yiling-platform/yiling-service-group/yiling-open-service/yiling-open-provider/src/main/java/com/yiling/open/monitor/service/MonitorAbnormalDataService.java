package com.yiling.open.monitor.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.open.monitor.dto.MonitorAbnormalDataDTO;
import com.yiling.open.monitor.dto.request.QueryErpMonitorSaleExceptionPageRequest;
import com.yiling.open.monitor.entity.MonitorAbnormalDataDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-08
 */
public interface MonitorAbnormalDataService extends BaseService<MonitorAbnormalDataDO> {

    boolean insertMonitorAbnormalData(MonitorAbnormalDataDO monitorAbnormalDataDO);

    Page<MonitorAbnormalDataDO> page(QueryErpMonitorSaleExceptionPageRequest request);

    /**
     * 根据开始、结束时间获取销售异常数据数量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Long getSaleExceptionCount(Date startTime, Date endTime);

    /**
     * 根据id列表获取
     *
     * @param idList
     * @return
     */
    List<MonitorAbnormalDataDO> getByIdList(List<Long> idList);

}
