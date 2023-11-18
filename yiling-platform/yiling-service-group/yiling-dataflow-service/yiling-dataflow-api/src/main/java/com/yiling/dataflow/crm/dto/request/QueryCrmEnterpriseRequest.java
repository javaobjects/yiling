package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmEnterpriseRequest extends BaseRequest {

    /**
     * 企业名称
     */
    private String likeName;

    /**
     * 企业类型EnterpriseTypeNameEnum.name
     */
    private String typeName;
}
