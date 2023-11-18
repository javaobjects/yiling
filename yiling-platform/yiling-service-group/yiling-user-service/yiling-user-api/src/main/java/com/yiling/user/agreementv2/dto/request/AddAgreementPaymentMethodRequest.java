package com.yiling.user.agreementv2.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议付款方式 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementPaymentMethodRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 付款方式：1-支票 2-电汇 3-易贷 4-3个月承兑 5-6个月承兑
     */
    private Integer payMethod;

    /**
     * 占比
     */
    private Integer ratio;

}
