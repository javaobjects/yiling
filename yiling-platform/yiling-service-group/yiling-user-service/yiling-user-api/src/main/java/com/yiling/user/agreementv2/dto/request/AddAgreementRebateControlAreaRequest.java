package com.yiling.user.agreementv2.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利范围控销区域 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateControlAreaRequest extends BaseRequest {

    /**
     * 协议返利范围ID
     */
    private Long rebateScopeId;

    /**
     * 控销区域描述
     */
    private String description;

    /**
     * 控销区域Json串
     */
    private String jsonContent;

}
