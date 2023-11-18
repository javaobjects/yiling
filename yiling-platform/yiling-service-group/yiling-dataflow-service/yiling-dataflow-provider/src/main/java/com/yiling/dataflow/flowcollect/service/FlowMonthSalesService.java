package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthSalesDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthSalesRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthSalesDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 月流向销售上传数据表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
public interface FlowMonthSalesService extends BaseService<FlowMonthSalesDO> {

    /**
     * 查询月流向销售分页列表
     *
     * @param request
     * @return
     */
    Page<FlowMonthSalesDTO> queryFlowMonthSalePage(QueryFlowMonthPageRequest request);

    /**
     * 保存月流向销售数据并生成流向任务
     *
     * @param recordId
     * @return
     */
    boolean updateFlowMonthSalesAndTask(Long opUserId,Long recordId);
}
