package com.yiling.hmc.insurance.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 确认已拿请求对象
 * @author: fan.shen
 *
 * @date: 2022/7/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ConfirmTookRequest extends BaseRequest {

    /**
     * 订单id
     */
    private Long id;

}
