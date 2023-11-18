package com.yiling.dataflow.flowcollect.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthSalesDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthSalesRequest;

/**
 * 月流向销售数据 API
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
public interface FlowMonthSalesApi {

    /**
     * 保存月流向销售数据
     *
     * @param recordId
     * @return
     */
    boolean updateFlowMonthSalesAndTask(Long opUserId,Long recordId);


    boolean saveBatch(List<SaveFlowMonthSalesRequest> requests);

    /**
     * 查询月流向销售分页列表
     *
     * @param request
     * @return
     */
    Page<FlowMonthSalesDTO> queryFlowMonthSalePage(QueryFlowMonthPageRequest request);

}
