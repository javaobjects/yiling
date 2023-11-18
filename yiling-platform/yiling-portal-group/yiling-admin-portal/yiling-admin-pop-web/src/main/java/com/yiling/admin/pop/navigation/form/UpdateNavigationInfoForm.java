package com.yiling.admin.pop.navigation.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateNavigationInfoForm extends BaseForm {

    /**
     * 导航id
     */
    @ApiModelProperty(value = "导航id")
    @NotNull(message = "不能为空")
    private Long id;

    /**
     * 导航名称
     */
    @ApiModelProperty(value = "导航名称")
    private String name;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 链接
     */
    @ApiModelProperty(value = "链接")
    private String link;

    /**
     * 状态1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer state;

}
