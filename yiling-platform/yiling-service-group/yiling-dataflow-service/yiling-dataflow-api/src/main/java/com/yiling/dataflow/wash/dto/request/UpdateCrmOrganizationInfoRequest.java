package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateCrmOrganizationInfoRequest extends BaseRequest {

    private static final long serialVersionUID = 802808990096540858L;

    private Long id;

    private Long crmOrganizationId;

    private String crmOrganizationName;
}
