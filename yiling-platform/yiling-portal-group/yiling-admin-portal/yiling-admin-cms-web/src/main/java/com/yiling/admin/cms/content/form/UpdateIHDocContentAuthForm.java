package com.yiling.admin.cms.content.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 更新IHDoc文章权限
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class UpdateIHDocContentAuthForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "内容权限 1-仅登录，2-需认证")
    private Integer contentAuth;
}
