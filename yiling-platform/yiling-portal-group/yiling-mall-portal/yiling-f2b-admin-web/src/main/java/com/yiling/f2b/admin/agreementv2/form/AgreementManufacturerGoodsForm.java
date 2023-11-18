package com.yiling.f2b.admin.agreementv2.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新增厂家商品 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/23
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementManufacturerGoodsForm extends BaseForm {

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 规格商品ID
     */
    @ApiModelProperty("规格商品ID")
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String specifications;

    /**
     * 生产厂家名称
     */
    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

}
