package com.yiling.admin.data.center.standard.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsMaterialsForm extends BaseForm {

    /**
     * 中药材说明书信息
     */
    @ApiModelProperty(value = "中药材说明书id")
    private Long id;

    /**
     * 性状
     */
    @ApiModelProperty(value = "性状")
    private String drugProperties;

    /**
     * 性味
     */
    @ApiModelProperty(value = "性味")
    private String propertyFlavor;

    /**
     * 功效
     */
    @ApiModelProperty(value = "功效")
    private String effect;

    /**
     * 用法与用量
     */
    @ApiModelProperty(value = "用法与用量")
    private String usageDosage;

    /**
     * 保证期
     */
    @ApiModelProperty(value = "保证期")
    private String expirationDate;

    /**
     * 储藏
     */
    @ApiModelProperty(value = "储藏")
    private String store;

}
