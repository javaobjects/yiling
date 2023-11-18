package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议控销区域详情 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementControlAreaDetailForm extends BaseForm {

    /**
     * 区域编码
     */
    @NotEmpty
    @ApiModelProperty(value = "区域编码",required = true)
    private String areaCode;

}
