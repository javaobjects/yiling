package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新增客户EAS信息 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCustomerEasInfoRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 客户ID
     */
    @NotNull
    private Long customerEid;

    /**
     * EAS名称
     */
    @NotNull
    private String easName;

    /**
     * EAS编码
     */
    @NotNull
    private String easCode;
}
