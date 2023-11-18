package com.yiling.sjms.esb.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据部门ID获取事业部下的指定层级业务架构 Form
 *
 * @author: lun.yu
 * @date: 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEsbBzOrgForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "部门ID", required = true)
    private Long orgId;

    @NotNull
    @ApiModelProperty(value = "获取的数据层级：2-业务省区 3-区办", required = true)
    private Integer tagType;


}
