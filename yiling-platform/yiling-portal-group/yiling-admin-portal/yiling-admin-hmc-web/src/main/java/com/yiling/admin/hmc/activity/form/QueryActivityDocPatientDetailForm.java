package com.yiling.admin.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 查询患者接口
 * @author: fan.shen
 * @data: 2023-02-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryActivityDocPatientDetailForm extends BaseForm {

    @NotNull
    @ApiModelProperty("id")
    private Integer id;

}
