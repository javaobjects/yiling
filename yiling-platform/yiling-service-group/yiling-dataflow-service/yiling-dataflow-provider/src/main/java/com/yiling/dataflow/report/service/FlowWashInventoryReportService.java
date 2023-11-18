package com.yiling.dataflow.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.FlowWashInventoryReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashInventoryReportPageRequest;
import com.yiling.dataflow.report.entity.FlowWashInventoryReportDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向库存合并报表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-02
 */
public interface FlowWashInventoryReportService extends BaseService<FlowWashInventoryReportDO> {


    /**
     * 流向库存合并报表
     *
     * @param request
     * @return
     */
    Page<FlowWashInventoryReportDTO> pageList(FlowWashInventoryReportPageRequest request);

    /**
     * 根据清洗任务Id删除报表数据
     * @param fmwtId
     * @return
     */
    Boolean removeByFmwtId(Long fmwtId);

}
