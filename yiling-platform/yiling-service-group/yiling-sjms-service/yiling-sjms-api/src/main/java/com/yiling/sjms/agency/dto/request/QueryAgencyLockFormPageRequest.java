package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/2/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgencyLockFormPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8357635753062108976L;

    /**
     * form表主键
     */
    private Long formId;
}