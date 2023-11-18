package com.yiling.dataflow.flowcollect.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.dto.FlowMonthInventoryDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthInventoryRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthPurchaseRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthInventoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 月流向库存上传数据表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-06
 */
public interface FlowMonthInventoryService extends BaseService<FlowMonthInventoryDO> {

    /**
     * 保存月流向库存数据
     *
     * @param request
     * @return
     */
    boolean updateFlowMonthInventoryAndTask(Long opUserId,Long recordId) ;

    /**
     * 查询月流向库存分页列表
     *
     * @param request
     * @return
     */
    Page<FlowMonthInventoryDTO> queryFlowMonthInventoryPage(QueryFlowMonthPageRequest request);

}
