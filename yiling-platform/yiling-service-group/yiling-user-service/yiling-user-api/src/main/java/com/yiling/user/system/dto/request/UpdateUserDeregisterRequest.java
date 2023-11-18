package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新用户注销账号 Request
 *
 * @author: lun.yu
 * @date: 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUserDeregisterRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 注销状态：1-待注销 2-已注销 3-已撤销
     */
    private Integer status;

}
