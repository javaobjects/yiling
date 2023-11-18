package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpFlowSealedLockOrUnlockRequest extends BaseRequest {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 封存状态，字典(erp_flow_sealed_status)：1-已解封 2-已封存 0-全部
     */
    private Integer status;

}
