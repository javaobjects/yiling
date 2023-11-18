package com.yiling.admin.data.center.standard.form;

import org.hibernate.validator.constraints.Length;

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
public class StandardInstructionsGoodsForm extends BaseForm {

    /**
     * 普通药品说明书信息
     */
    @ApiModelProperty(value = "普通药品说明书id")
    private Long id;

    /**
     * 药品成分
     */
    @ApiModelProperty(value = "药品成分")
    private String drugDetails;

    /**
     * 药品性状
     */
    @ApiModelProperty(value = "药品性状")
    private String drugProperties;

    /**
     * 适应症
     */
    @ApiModelProperty(value = "适应症")
    private String indications;

    /**
     * 用法与用量
     */
    @ApiModelProperty(value = "用法与用量")
    @Length(max = 500)
    private String usageDosage;

    /**
     * 不良反应
     */
    @ApiModelProperty(value = "不良反应")
    private String adverseEvents;

    /**
     * 禁忌症
     */
    @ApiModelProperty(value = "禁忌症")
    @Length(max = 500)
    private String contraindication;

    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private String noteEvents;

    /**
     * 药物相互作用
     */
    @ApiModelProperty(value = "药物相互作用")
    private String interreaction;

    /**
     * 存储条件
     */
    @ApiModelProperty(value = "存储条件")
    private String storageConditions;

    /**
     * 保质期
     */
    @ApiModelProperty(value = "保质期")
    private String shelfLife;

    /**
     * 执行标准
     */
    @ApiModelProperty(value = "执行标准")
    private String executiveStandard;
}
