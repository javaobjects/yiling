package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/4/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEnterpriseCrmCodeRequest extends BaseRequest {

    private static final long serialVersionUID = 6767223801196273408L;
    /**
     * id
     */
    private Long id;

    /**
     * crm客户编码
     */
    private String enterpriseCrmCode;
}
