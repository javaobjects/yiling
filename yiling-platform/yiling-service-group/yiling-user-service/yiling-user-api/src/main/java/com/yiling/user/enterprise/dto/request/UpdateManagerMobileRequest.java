package com.yiling.user.enterprise.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改企业管理员手机号 Request
 *
 * @author: xuan.zhou
 * @date: 2022/3/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateManagerMobileRequest extends BaseRequest {

    /**
     * 企业ID
     */
    @NotNull
    @Min(1)
    private Long eid;

    /**
     * 用户ID
     */
    @NotNull
    @Min(1)
    private Long userId;

    /**
     * 新手机号
     */
    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "^新手机号格式错误")
    private String newMobile;

    /**
     * 姓名
     */
    @NotEmpty
    @Length(min = 1, max = 10)
    private String name;

    /**
     * 新密码
     */
    private String password;
}
