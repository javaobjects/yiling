package com.yiling.open.cms.content.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateContentCategoryRankForm extends BaseForm {

    @NotNull
    @ApiModelProperty("id")
    private Long id;

    @NotEmpty
    @ApiModelProperty(value = "栏目集合")
    private List<AddOrUpdateContentCategoryForm> categoryList;

    @ApiModelProperty(value = "操作人")
    private Long opUserId;
}
