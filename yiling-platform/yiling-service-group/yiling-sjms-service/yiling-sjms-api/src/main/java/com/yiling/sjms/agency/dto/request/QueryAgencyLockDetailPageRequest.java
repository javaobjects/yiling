package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAgencyLockDetailPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -3938525304309195816L;

    /**
     * form表主键
     */
    private Long formId;

}