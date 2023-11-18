package com.yiling.marketing.integralmessage.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新积分消息状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIntegralMessageStatusRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 状态：1-启用 2-禁用
     */
    private Integer status;

}
