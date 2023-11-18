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
 * @date: 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "内容排序/置顶参数")
public class ContentRankTopForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "栏目排序")
    private Integer categoryRank;

    @ApiModelProperty(value = "是否置顶 0-否，1-是")
    private Integer topFlag;

}