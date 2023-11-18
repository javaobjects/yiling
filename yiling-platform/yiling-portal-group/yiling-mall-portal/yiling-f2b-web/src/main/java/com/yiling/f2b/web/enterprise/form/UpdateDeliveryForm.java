package com.yiling.f2b.web.enterprise.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改收货信息 Form
 * 
 * @author lun.yu
 * @date 2022-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateDeliveryForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "id", required = true)
    private Long id;


}
