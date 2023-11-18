package com.yiling.dataflow.relation.api;

import com.yiling.dataflow.relation.dto.FlowSupplierMappingDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowSupplierMappingRequest;
import com.yiling.dataflow.statistics.dto.request.SaveFlowBalanceStatisticsRequest;

/**
 * 商家品和以岭品关系 接口
 *
 * @author: houjie.sun
 * @date: 2022/5/23
 */
public interface FlowSupplierMappingApi {

    /**
     * 通过供应商名称获取映射关系
     *
     * @param enterpriseName
     * @return
     */
    FlowSupplierMappingDTO getByEnterpriseName(String enterpriseName);

    /**
     * 保存供应商映射关系
     * @param request
     * @return
     */
    boolean saveFlowSupplierMapping(SaveFlowSupplierMappingRequest request);
}
