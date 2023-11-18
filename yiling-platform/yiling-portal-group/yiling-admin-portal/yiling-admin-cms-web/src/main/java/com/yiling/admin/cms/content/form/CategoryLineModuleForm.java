package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 业务线关联模块
 * </p>
 *
 * @author gxl
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryLineModuleForm extends BaseForm {

    /**
     * 业务线id
     */
    @NotBlank
    @ApiModelProperty(value = "业务线id")
    private Long lineId;

    /**
     * 业务线名称
     */
    @NotBlank
    @ApiModelProperty(value = "业务线名称")
    private String lineName;

    /**
     * 模块id
     */
    @NotBlank
    @ApiModelProperty(value = "模块id")
    private Long moduleId;

    /**
     * 模块名称
     */
    @NotBlank
    @ApiModelProperty(value = "模块名称")
    private String moduleName;

}
