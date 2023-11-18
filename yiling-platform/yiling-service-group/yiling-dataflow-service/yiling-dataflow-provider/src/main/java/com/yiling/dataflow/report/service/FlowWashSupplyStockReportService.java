package com.yiling.dataflow.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.FlowWashSupplyStockReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSupplyStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashSupplyStockReportDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 商业进销存报表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
public interface FlowWashSupplyStockReportService extends BaseService<FlowWashSupplyStockReportDO> {

    /**
     * 商业进销存报表
     *
     * @param request
     * @return
     */
    Page<FlowWashSupplyStockReportDTO> pageList(FlowWashSupplyStockReportPageRequest request);

    /**
     * 清除商业进销存库存报表数据
      * @param request
     * @return
     */
    boolean removeByCrmId(RemoveFlowWashStockReportRequest request);

}
