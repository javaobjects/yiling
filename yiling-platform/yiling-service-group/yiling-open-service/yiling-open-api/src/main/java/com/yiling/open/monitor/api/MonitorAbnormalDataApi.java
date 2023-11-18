package com.yiling.open.monitor.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.monitor.dto.MonitorAbnormalDataDTO;
import com.yiling.open.monitor.dto.request.QueryErpMonitorSaleExceptionPageRequest;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
public interface MonitorAbnormalDataApi {

    Page<MonitorAbnormalDataDTO> getSaleExceptionListPage(QueryErpMonitorSaleExceptionPageRequest request);

    List<MonitorAbnormalDataDTO> getByIdList(List<Long> idList);
}
