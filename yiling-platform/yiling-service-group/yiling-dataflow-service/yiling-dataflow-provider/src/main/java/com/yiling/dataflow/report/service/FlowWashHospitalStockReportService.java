package com.yiling.dataflow.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.report.dto.FlowWashHospitalStockReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashHospitalStockReportPageRequest;
import com.yiling.dataflow.report.dto.request.RemoveFlowWashStockReportRequest;
import com.yiling.dataflow.report.entity.FlowWashHospitalStockReportDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 医疗进销存报表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2023-03-04
 */
public interface FlowWashHospitalStockReportService extends BaseService<FlowWashHospitalStockReportDO> {


    /**
     * 医疗进销存库存报表
     *
     * @param request
     * @return
     */
    Page<FlowWashHospitalStockReportDTO> pageList(FlowWashHospitalStockReportPageRequest request);


    /**
     * 清除医疗进销存库存报表数据
     * @param request
     * @return
     */
    boolean removeByCrmId(RemoveFlowWashStockReportRequest request);
}
