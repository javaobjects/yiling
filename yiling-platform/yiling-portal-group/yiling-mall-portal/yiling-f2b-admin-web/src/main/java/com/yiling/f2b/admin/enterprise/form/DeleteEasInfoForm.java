package com.yiling.f2b.admin.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 删除EAS信息 Form
 *
 * @author: lun.yu
 * @date: 2021/12/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteEasInfoForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

}
