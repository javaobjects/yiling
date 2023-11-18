package com.yiling.sjms.crm.form;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveOrUpdateCrmGoodsCategoryForm
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateCrmGoodsCategoryForm extends BaseForm {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 品类编码
     */
    @NotEmpty(message = "")
    @ApiModelProperty(value = "品类编码")
    private String code;

    /**
     * 品类名称
     */
    @NotEmpty(message = "")
    @ApiModelProperty(value = "品类名称")
    private String name;

    /**
     * 上级id
     */
    @ApiModelProperty(value = "上级id")
    private Long parentId;

    /**
     * 是否末级 0：非末级，1：末级
     */
    @ApiModelProperty(value = "是否末级 0：非末级，1：末级")
    private Integer finalStageFlag;
}
