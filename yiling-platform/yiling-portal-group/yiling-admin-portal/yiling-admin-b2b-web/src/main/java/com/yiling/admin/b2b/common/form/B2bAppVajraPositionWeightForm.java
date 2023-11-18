package com.yiling.admin.b2b.common.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

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
public class B2bAppVajraPositionWeightForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序")
    @NotNull(message = "请输入权重")
    @Range(message = "权重范围为 {min} 到 {max} 之间", min = 1, max = 200)
    private Integer sort;
}
