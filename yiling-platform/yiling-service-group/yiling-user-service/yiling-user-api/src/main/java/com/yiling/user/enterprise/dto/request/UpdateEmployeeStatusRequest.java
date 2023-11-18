package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改员工状态 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateEmployeeStatusRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    private Integer status;
}
