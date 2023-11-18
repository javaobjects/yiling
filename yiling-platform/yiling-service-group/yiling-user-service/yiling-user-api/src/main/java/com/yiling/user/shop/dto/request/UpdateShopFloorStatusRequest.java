package com.yiling.user.shop.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * <p>
 * B2B-更新店铺楼层状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-20
 */
@Data
public class UpdateShopFloorStatusRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 楼层状态：1-启用 2-停用
     */
    private Integer status;

}
