package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.dataflow.order.dto.FlowPurchaseDTO;
import com.yiling.open.erp.dto.ErpClientDTO;

/**
 * @author: houjie.sun
 * @date: 2022/11/5
 */
public interface ErpPurchaseFlowAsyncService {

    void handlerFlowPurchaseInventory(Integer operType, ErpClientDTO erpClient, List<FlowPurchaseDTO> oldFlowPurchaseList, List<FlowPurchaseDTO> newFlowPurchaseList);

}
