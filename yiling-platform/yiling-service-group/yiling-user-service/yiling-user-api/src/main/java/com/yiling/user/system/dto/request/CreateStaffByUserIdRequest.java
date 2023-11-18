package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建员工账号 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateStaffByUserIdRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 当用户信息存在时，选择忽略
     */
    private boolean ignoreExists = false;
}
