package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改密码 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdatePasswordRequest extends BaseRequest {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 新密码
     */
    private String password;
}
