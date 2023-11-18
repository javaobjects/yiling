package com.yiling.dataflow.crm.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmManorParamRequest extends BaseRequest {
    private Long id;

    private List<Long> idList;
    /**
     * 辖区编码
     */
    private String manorNo;

    /**
     * 辖区名称
     */
    private String name;
}
