package com.yiling.dataflow.relation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.relation.dto.FlowCustomerMappingDTO;
import com.yiling.dataflow.relation.dto.request.QueryFlowCustomerMappingListPageRequest;
import com.yiling.dataflow.relation.entity.FlowCustomerMappingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-18
 */
public interface FlowCustomerMappingService extends BaseService<FlowCustomerMappingDO> {

    Page<FlowCustomerMappingDTO> page(QueryFlowCustomerMappingListPageRequest request);

//    FlowCustomerMappingDTO getByFlowCustomerNameAndBusinessCode(String customerName, String businessCode);
}
