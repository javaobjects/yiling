package com.yiling.payment.pay.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundParamListRequest extends BaseRequest {
    private static final long serialVersionUID=1L;
    /**
     * 批量退款申请参数
     */
    private List<RefundParamRequest> refundParamRequestList;

}
