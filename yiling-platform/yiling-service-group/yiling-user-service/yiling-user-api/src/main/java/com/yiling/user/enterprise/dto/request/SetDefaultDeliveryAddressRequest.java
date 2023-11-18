package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设置默认收货地址 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SetDefaultDeliveryAddressRequest extends BaseRequest {

    private static final long serialVersionUID = 5086910206294791692L;

    /**
     * 收货地址ID
     */
    private Long id;

    /***
     * 企业ID
     */
    private Long eid;

}
