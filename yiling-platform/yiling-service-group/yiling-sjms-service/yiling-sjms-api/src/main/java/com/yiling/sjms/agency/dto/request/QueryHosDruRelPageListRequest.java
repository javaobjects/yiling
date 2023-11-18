package com.yiling.sjms.agency.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryHosDruRelPageListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -4374529036700916085L;

    /**
     * form表主键
     */
    private Long formId;

    private Long drugstoreOrgId;

    private Long hospitalOrgId;

    private Long crmGoodsCode;
}
