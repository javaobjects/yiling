package com.yiling.admin.erp.enterprise.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteCacheClientTaskConfigForm extends BaseForm {

    @ApiModelProperty(value = "商业编号", example = "1")
    private Long suId;

    @ApiModelProperty(value = "任务编号", example = "1")
    private String taskNo;

}