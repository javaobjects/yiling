package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthInventoryDTO;
import com.yiling.dataflow.flowcollect.dto.FlowMonthPurchaseDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthPurchaseRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthPurchaseDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 月流向采购上传数据表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
public interface FlowMonthPurchaseService extends BaseService<FlowMonthPurchaseDO> {

    /**
     * 保存月流向采购数据
     *
     * @param recordId
     * @return
     */
    boolean updateFlowMonthPurchaseAndTask(Long opUserId,Long recordId);

    /**
     * 查询月流向采购分页列表
     *
     * @param request
     * @return
     */
    Page<FlowMonthPurchaseDTO> queryFlowMonthPurchasePage(QueryFlowMonthPageRequest request);

}
