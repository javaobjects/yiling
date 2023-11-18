package com.yiling.dataflow.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class QueryDepartProductGroupByUploadNameRequest extends BaseRequest {
    private List<String> uploadNames;
    private Integer supplyChainRole;
}
