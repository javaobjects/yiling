package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改员工账号状态 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUserStatusRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 状态（参见UserStatusEnum枚举）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
