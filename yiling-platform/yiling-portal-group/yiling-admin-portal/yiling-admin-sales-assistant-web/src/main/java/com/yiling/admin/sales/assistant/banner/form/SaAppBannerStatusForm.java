package com.yiling.admin.sales.assistant.banner.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaAppBannerStatusForm extends BaseForm {

    @ApiModelProperty(value = "id")
    @NotNull(message = "未选中数据")
    private Long    id;

    @ApiModelProperty(value = "状态：1-启用 2-停用")
    @NotNull(message = "选择的状态不能为空")
    private Integer bannerStatus;
}
