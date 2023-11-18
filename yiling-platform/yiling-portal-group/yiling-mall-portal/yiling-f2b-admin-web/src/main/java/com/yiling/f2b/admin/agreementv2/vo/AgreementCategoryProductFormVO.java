package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议分类表单 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-17
 */
@Data
@ApiModel
@Accessors(chain = true)
public class AgreementCategoryProductFormVO extends AgreementDetailCommonFormVO implements Serializable {

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
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String producerManufacturer;

    /**
     * 品牌厂家
     */
    @ApiModelProperty("品牌厂家")
    private String brandManufacturer;

}
