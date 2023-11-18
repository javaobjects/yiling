package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

/**
 * 保存辖区基本信息form
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmManorInfoForm extends BaseForm {
    private Long id;
    /**
     * 辖区编码
     */
    @ApiModelProperty("辖区编码")
    @NotBlank(message = "辖区编码不能为空")
    @Length(max = 100)
    private String manorNo;

    /**
     * 辖区名称
     */
    @ApiModelProperty("辖区名称")
    @NotBlank(message = "辖区名称不能为空")
    @Length(max = 100)
    private String name;
}
