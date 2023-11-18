package com.yiling.admin.hmc.insurance.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保险服务商删除
 *
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceCompanyDeleteForm extends BaseForm {

    @ApiModelProperty("保险服务商id")
    @NotNull(message = "保险服务商未选中")
    private Long id;
}
