package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmHosDruRelEffectiveListRequest extends BaseRequest {

    private static final long serialVersionUID = -5027689228961968297L;

    private Long drugstoreOrgId;

    private Long hospitalOrgId;

    private Long categoryId;

    private Long crmGoodsCode;
}
