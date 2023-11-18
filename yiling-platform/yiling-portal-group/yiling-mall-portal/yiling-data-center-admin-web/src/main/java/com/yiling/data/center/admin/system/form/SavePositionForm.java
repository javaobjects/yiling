package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 新增/修改职位信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePositionForm extends BaseForm {

    @ApiModelProperty(value = "职位ID，新增时为空")
    private Long id;

    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "职位名称", required = true)
    private String name;

    @Length(max = 200)
    @ApiModelProperty("职位描述")
    private String description;
}
