package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveDictDataForm extends BaseForm {

    /**
     * 字典类型ID（关联dict_type.id字段）
     */
    @NotNull(message = "不能为空")
    @ApiModelProperty("字典类型ID")
    private Long typeId;

    /**
     * 字典标签
     */
    @ApiModelProperty("字典标签")
    @NotBlank(message = "不能为空")
    private String label;

    /**
     * 字典键值
     */
    @ApiModelProperty("字典键值")
    @NotNull(message = "不能为空")
    private String value;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String description;

    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    private Integer sort;
}
