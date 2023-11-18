package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议控销区域 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementControlAreaForm extends BaseForm {

    /**
     * 控销区域Json串
     */
    @ApiModelProperty(value = "控销区域Json串",required = true)
    private String jsonContent;

}
