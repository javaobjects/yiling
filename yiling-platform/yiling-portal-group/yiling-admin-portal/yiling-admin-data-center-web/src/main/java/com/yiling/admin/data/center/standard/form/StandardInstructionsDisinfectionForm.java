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
public class StandardInstructionsDisinfectionForm extends BaseForm {

    /**
     * 消杀品说明书
     */
    @ApiModelProperty(value = "消杀品说明书id")
    private Long id;

    /**
     * 成分
     */
    @ApiModelProperty(value = "成分")
    private String drugDetails;

    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String noteEvents;

    /**
     * 保质期
     */
    @ApiModelProperty(value = "保质期")
    private String expirationDate;

    /**
     * 使用方法
     */
    @ApiModelProperty(value = "使用方法")
    private String usageDosage;

    /**
     * 灭菌类别
     */
    @ApiModelProperty(value = "灭菌类别")
    private String sterilizationCategory;

}
