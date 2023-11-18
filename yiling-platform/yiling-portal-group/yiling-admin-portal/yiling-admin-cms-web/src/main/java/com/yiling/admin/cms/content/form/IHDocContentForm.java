package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 添加IHDoc端内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class IHDocContentForm {

    @NotNull
    @ApiModelProperty(value = "内容id")
    private Long contentId;

    @NotNull
    @ApiModelProperty(value = "业务线id")
    private Long lineId;

    @NotNull
    @ApiModelProperty(value = "模块id")
    private Long moduleId;

    @NotNull
    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @NotNull
    @ApiModelProperty(value = "内容权限 1-仅登录，2-需认证")
    private Integer contentAuth;

}
