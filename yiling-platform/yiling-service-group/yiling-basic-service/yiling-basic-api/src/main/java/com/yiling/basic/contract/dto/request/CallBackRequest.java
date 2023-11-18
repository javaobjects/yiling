package com.yiling.basic.contract.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CallBackRequest extends BaseRequest {

    private static final long serialVersionUID = 9035052404775395733L;

    private Long contractId;

    private Long tenantId;

    private String tenantName;

    private String sn;

    private String status;

    private String type;

    private String contact;
}
