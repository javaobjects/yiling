package com.yiling.dataflow.crm.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

@Data
public class QueryDataScopeRequest extends BaseRequest {
    private SjmsUserDatascopeBO sjmsUserDatascopeBO;
}
