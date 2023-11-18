package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新积分规则状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRuleStatusRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 执行状态：1-启用 2-停用 3-草稿
     */
    private Integer status;

}
