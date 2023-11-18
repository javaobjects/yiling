package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 身份证照片正面照 OCR
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IdCardFrontPhotoOcrForm extends BaseForm {

    /**
     * 身份证照片正面照 base64
     */
    @ApiModelProperty(value = "身份证照片正面照 base64")
    private String idCardFrontPhotoBase64;

}