package com.yiling.dataflow.crm.dto.request;

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
public class QueryCrmRetailEnterpriseNumberGoodsRelationRequest extends QueryPageListRequest {

}
