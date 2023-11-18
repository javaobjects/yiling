package com.yiling.f2b.admin.agreementv2.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利商品 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateGoodsForm extends BaseForm {

    /**
     * 商品ID
     */
    @NotNull
    @ApiModelProperty(value = "商品ID", required = true)
    private Long goodsId;

    /**
     * 规格商品ID
     */
    @NotNull
    @ApiModelProperty(value = "规格商品ID", required = true)
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    @NotEmpty
    @ApiModelProperty(value = "商品名称",required = true)
    private String goodsName;

    /**
     * 规格
     */
    @NotEmpty
    @ApiModelProperty(value = "规格", required = true)
    private String specifications;

    /**
     * 生产厂家名称
     */
    @ApiModelProperty("生产厂家名称")
    private String producerManufacturerName;

}
