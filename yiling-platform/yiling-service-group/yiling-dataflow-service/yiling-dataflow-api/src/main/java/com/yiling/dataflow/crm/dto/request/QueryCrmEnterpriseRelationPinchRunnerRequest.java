package com.yiling.dataflow.crm.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/4/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseRelationPinchRunnerRequest extends BaseRequest {

    /**
     * 数据权限
     */
    private SjmsUserDatascopeBO userDatascopeBO;

    /**
     * ID
     */
    private Long id;

}
