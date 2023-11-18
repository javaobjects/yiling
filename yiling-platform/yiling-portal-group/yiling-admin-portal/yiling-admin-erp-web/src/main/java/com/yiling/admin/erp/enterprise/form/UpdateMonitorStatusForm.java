package com.yiling.admin.erp.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMonitorStatusForm extends BaseForm {

    /**
     * 企业id
     */
    @NotNull
    @ApiModelProperty(value = "企业ID")
    private Long rkSuId;

    /**
     * 监控状态：0-未开启 1-开启
     */
    @NotNull
    @ApiModelProperty(value = "监控状态：0-未开启 1-开启")
    private Integer monitorStatus;

}
