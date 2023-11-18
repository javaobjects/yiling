package com.yiling.dataflow.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.FlowWashPharmacyPurchaseReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashPharmacyPurchaseReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashPharmacyPurchaseReportDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 零售购进报表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
public interface FlowWashPharmacyPurchaseReportService extends BaseService<FlowWashPharmacyPurchaseReportDO> {

    /**
     * 零售购进报表
     *
     * @param request
     * @return
     */
    Page<FlowWashPharmacyPurchaseReportDTO> pageList(FlowWashPharmacyPurchaseReportPageRequest request);

    /**
     * 清除零售购进报表数据
     * @param request
     * @return
     */
    boolean removeByCrmId(RemoveFlowWashStockReportRequest request);

}
