package com.yiling.open.cms.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


/**
 * 获取医生二维码 Form
 *
 * @author: fan.shen
 * @date: 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GetDoctorQrCodeForm extends BaseForm {

    /**
     * 医生id
     */
    @NotNull
    @ApiModelProperty(value = "医生id", required = true)
    private Long doctorId;

}
