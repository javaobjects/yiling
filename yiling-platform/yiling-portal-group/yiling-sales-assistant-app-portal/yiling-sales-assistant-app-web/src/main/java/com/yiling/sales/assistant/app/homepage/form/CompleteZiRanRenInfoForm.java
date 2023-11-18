package com.yiling.sales.assistant.app.homepage.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sales.assistant.app.system.form.LocationTreeForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 完善自然人个人信息 Form
 *
 * @author: xuan.zhou
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompleteZiRanRenInfoForm extends BaseForm {

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @NotEmpty
    @Length(max = 18)
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;

    @ApiModelProperty(value = "销售区域（为空表示“全国”）")
    private List<LocationTreeForm> salesAreaTree;

    @NotEmpty
    @ApiModelProperty(value = "身份证正面照文件KEY", required = true)
    private String idCardFrontPhotoKey;

    @NotEmpty
    @ApiModelProperty(value = "身份证反面照文件KEY", required = true)
    private String idCardBackPhotoKey;
}
