package com.yiling.f2b.admin.procrelation.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGoodsToTemplateForm extends BaseForm {

    /**
     * 商品id
     */
    @NotNull
    @ApiModelProperty(value = "模板id")
    private Long templateId;

    /**
     * 工业id
     */
    @NotNull
    @ApiModelProperty(value = "工业id")
    private Long factoryEid;

    /**
     * 商品id
     */
    @NotNull
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    /**
     * 标准库ID
     */
    @NotNull
    @ApiModelProperty(value = "标准库ID")
    private Long standardId;

    /**
     * 售卖规格ID
     */
    @NotNull
    @ApiModelProperty(value = "售卖规格ID")
    private Long sellSpecificationsId;

    /**
     * 商品名称
     */
    @NotBlank
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 售卖规格
     */
    @NotBlank
    @ApiModelProperty(value = "售卖规格")
    private String sellSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    @NotNull
    @ApiModelProperty(value = "专利类型 0-全部 1-非专利 2-专利")
    private Integer isPatent;
}
