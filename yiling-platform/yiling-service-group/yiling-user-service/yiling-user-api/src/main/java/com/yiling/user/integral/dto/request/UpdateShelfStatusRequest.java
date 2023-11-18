package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新积分兑换商品上下架状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateShelfStatusRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    private Integer status;

}
