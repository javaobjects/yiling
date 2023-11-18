package com.yiling.dataflow.relation.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.FlowSaleDTO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseListPageRequest;
import com.yiling.dataflow.relation.dto.FlowCustomerMappingDTO;
import com.yiling.dataflow.relation.dto.request.QueryFlowCustomerMappingListPageRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/7/19
 */
public interface FlowCustomerMappingApi {
    /**
     * 查询列表分页
     * @param request
     * @return
     */
    Page<FlowCustomerMappingDTO> page(QueryFlowCustomerMappingListPageRequest request);

//    FlowCustomerMappingDTO getByFlowCustomerNameAndBusinessCode(String customerName,String businessCode);
}
