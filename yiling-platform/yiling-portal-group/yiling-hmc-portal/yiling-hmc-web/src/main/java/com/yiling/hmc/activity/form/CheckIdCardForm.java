package com.yiling.hmc.activity.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 校验身份证是否存在 Form
 *
 * @author: fan.shen
 * @date: 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckIdCardForm extends BaseForm {

    /**
     * 身份证
     */
    @ApiModelProperty(value = "身份证", required = true)
    private String idCard;

}
