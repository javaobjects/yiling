package com.yiling.open.monitor.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.monitor.api.MonitorAbnormalDataApi;
import com.yiling.open.monitor.dto.MonitorAbnormalDataDTO;
import com.yiling.open.monitor.dto.request.QueryErpMonitorSaleExceptionPageRequest;
import com.yiling.open.monitor.service.MonitorAbnormalDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/10/19
 */
@Slf4j
@DubboService
public class MonitorAbnormalDataApiImpl implements MonitorAbnormalDataApi {

    @Autowired
    private MonitorAbnormalDataService monitorAbnormalDataService;

    @Override
    public Page<MonitorAbnormalDataDTO> getSaleExceptionListPage(QueryErpMonitorSaleExceptionPageRequest request) {
        return PojoUtils.map(monitorAbnormalDataService.page(request), MonitorAbnormalDataDTO.class);
    }

    @Override
    public List<MonitorAbnormalDataDTO> getByIdList(List<Long> idList) {
        return PojoUtils.map(monitorAbnormalDataService.getByIdList(idList), MonitorAbnormalDataDTO.class);
    }
}
