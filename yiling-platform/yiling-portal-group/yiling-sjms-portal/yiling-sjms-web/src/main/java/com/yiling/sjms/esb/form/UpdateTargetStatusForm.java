package com.yiling.sjms.esb.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 更新上传指标状态 Form
 *
 * @author: lun.yu
 * @date: 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTargetStatusForm extends BaseForm {

    /**
     * 业务架构ID
     */
    @NotNull
    @ApiModelProperty(value = "业务架构ID", required = true)
    private Long id;

    /**
     * 是否可以上传指标：0-否 1-是
     */
    @NotNull
    @ApiModelProperty(value = "是否可以上传指标：0-否 1-是", required = true)
    private Integer targetStatus;

}
