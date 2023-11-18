package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/23 0023
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgencyFormPageRequest extends QueryPageListRequest {

    /**
     * form表主键
     */
    private Long formId;
}
