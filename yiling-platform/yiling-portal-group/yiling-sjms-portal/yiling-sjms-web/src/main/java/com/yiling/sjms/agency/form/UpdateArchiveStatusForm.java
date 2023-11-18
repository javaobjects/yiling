package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-02-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateArchiveStatusForm extends BaseForm {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 数据归档：1-开启 2-关闭
     */
    @NotNull
    @ApiModelProperty(value = "数据归档：1-开启 2-关闭")
    private Integer archiveStatus;
}
