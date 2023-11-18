package com.yiling.admin.erp.enterprise.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveErpFlowGoodsConfigForm extends BaseForm {

    /**
     * 商业ID
     */
    @NotNull(message = "商业ID不能为空")
    @ApiModelProperty(value = "商业ID")
    private Long eid;

    /**
     * 商业名称
     */
    @NotBlank(message = "商业名称不能为空")
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品内码（供应商的ERP的商品主键）
     */
    @NotBlank(message = "商品内码不能为空")
    @ApiModelProperty(value = "商品内码")
    private String goodsInSn;

    /**
     * 商品规格
     */
    @NotBlank(message = "商品规格不能为空")
    @ApiModelProperty(value = "商品规格")
    private String specifications;

    /**
     * 批准文号（注册证号）
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 生产厂家
     */
    @NotBlank(message = "生产厂家不能为空")
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

}
