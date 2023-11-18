package com.yiling.dataflow.relation.api.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.relation.api.FlowCustomerMappingApi;
import com.yiling.dataflow.relation.dto.FlowCustomerMappingDTO;
import com.yiling.dataflow.relation.dto.request.QueryFlowCustomerMappingListPageRequest;
import com.yiling.dataflow.relation.service.FlowCustomerMappingService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/6/14
 */
@DubboService
public class FlowCustomerMappingApiImpl implements FlowCustomerMappingApi {

    @Autowired
    private FlowCustomerMappingService flowCustomerMappingService;


    @Override
    public Page<FlowCustomerMappingDTO> page(QueryFlowCustomerMappingListPageRequest request) {
        return flowCustomerMappingService.page(request);
    }

//    @Override
//    public FlowCustomerMappingDTO getByFlowCustomerNameAndBusinessCode(String customerName, String businessCode) {
//        return flowCustomerMappingService.getByFlowCustomerNameAndBusinessCode(customerName, businessCode);
//    }
}
