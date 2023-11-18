package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 创建健康管理中心用户应用 Request
 *
 * @author: fan.shen
 * @date: 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateHmcUserAppRequest extends BaseRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * appId
     */
    private String appId;

    /**
     * 小程序openId
     */
    private String openId;

}
