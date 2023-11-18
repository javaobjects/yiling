package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 内容排序/置顶参数
 *
 * @author: fan.shen
 * @date: 2022-12-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "修改引用")
public class ContentReferStatusForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "引用状态 1-引用，2-取消引用")
    private Integer referStatus;

}