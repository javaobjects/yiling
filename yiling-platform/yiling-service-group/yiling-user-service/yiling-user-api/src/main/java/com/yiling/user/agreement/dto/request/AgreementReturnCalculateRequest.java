package com.yiling.user.agreement.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/7/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementReturnCalculateRequest extends BaseRequest {
    private static final long serialVersionUID = -5045331341880627054L;

    private Long returnOrderId;

    /**
     * 协议政策
     */
    private BigDecimal policyValue;
}
