package com.yiling.marketing.integralmessage.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 删除积分兑换消息 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteIntegralMessageRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

}
