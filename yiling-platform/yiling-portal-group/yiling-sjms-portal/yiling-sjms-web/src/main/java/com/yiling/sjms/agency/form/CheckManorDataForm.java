package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CheckManorDataForm extends BaseForm {
    @ApiModelProperty("id")
    private Long id;
    /**
     * 辖区编码
     */
    @ApiModelProperty("辖区编码")
    @Length(max = 100)
    private String manorNo;

    /**
     * 辖区名称
     */
    @ApiModelProperty("辖区名称")
    @Length(max = 100)
    private String name;

}
