package com.yiling.marketing.integralmessage.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新积分消息排序 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateIntegralMessageOrderRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 排序
     */
    private Integer sort;

}
