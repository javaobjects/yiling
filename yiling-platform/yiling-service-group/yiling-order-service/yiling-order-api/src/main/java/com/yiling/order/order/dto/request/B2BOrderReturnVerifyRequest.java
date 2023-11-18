package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 退货单审核请求参数
 *
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnVerifyRequest extends BaseRequest {

    /**
     * 退货单id
     */
    private Long returnId;

    /**
     * 审核是否通过 0-通过 1-驳回
     */
    private Integer isSuccess;

    /**
     * 驳回原因
     */
    private String failReason;
}
