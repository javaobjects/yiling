package com.yiling.admin.data.center.report.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveParamFlowGoodsRelationForm extends BaseForm {

    /**
     * 主键ID
     */
    @Min(1)
    @NotNull(message = "主键ID不能为空")
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 以岭商品ID
     */
    @ApiModelProperty(value = "以岭商品ID")
    private Long ylGoodsId;

    /**
     * 以岭商品名称
     */
    @ApiModelProperty(value = "以岭商品名称")
    private String ylGoodsName;

    /**
     * 以岭商品规格
     */
    @ApiModelProperty(value = "以岭商品规格")
    private String ylGoodsSpecifications;

    /**
     * 商品关系标签：0-无标签 1-以岭品 2-非以岭品 3-中药饮片
     */
    @ApiModelProperty(value = "商品关系标签：1-以岭品 2-非以岭品 3-中药饮片 0-无标签")
    private Integer goodsRelationLabel;

}
