package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利范围控销区域 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-04
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateControlAreaForm extends BaseForm {

    /**
     * 控销区域Json串
     */
    @ApiModelProperty("控销区域Json串")
    private String jsonContent;

}
