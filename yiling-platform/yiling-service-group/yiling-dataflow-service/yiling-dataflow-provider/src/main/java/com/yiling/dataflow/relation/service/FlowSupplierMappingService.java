package com.yiling.dataflow.relation.service;

import com.yiling.dataflow.relation.dto.FlowSupplierMappingDTO;
import com.yiling.dataflow.relation.dto.request.SaveFlowSupplierMappingRequest;
import com.yiling.dataflow.relation.entity.FlowSupplierMappingDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-08-10
 */
public interface FlowSupplierMappingService extends BaseService<FlowSupplierMappingDO> {

    /**
     * 通过供应商名称获取映射关系
     *
     * @param enterpriseName
     * @return
     */
    FlowSupplierMappingDO getByEnterpriseName(String enterpriseName);

    /**
     * 保存供应商映射关系
     * @param request
     * @return
     */
    boolean saveFlowSupplierMapping(SaveFlowSupplierMappingRequest request);
}
