package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCrmHosDruRelActiveRequest extends BaseRequest {

    private static final long serialVersionUID = -7409503233528366188L;

    private Long drugstoreOrgId;

    private Long crmGoodsCode;
}
