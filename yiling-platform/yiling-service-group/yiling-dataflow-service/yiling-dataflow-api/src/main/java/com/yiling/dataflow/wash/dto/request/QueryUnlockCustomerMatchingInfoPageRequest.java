package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryUnlockCustomerMatchingInfoPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8938765431054077008L;

    private String customerName;
}
