package com.yiling.dataflow.relation.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/8/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowSupplierMappingRequest  extends BaseRequest {

    /**
     * 流向数据供应商名称
     */
    private String flowEnterpriseName;

    /**
     * 供应商id
     */
    private Long supplierId;

}
