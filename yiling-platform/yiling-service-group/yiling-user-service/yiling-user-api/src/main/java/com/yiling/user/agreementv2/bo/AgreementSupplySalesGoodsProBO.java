package com.yiling.user.agreementv2.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销商品详细 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesGoodsProBO extends BaseVO implements Serializable {

    /**
     * 商品组ID
     */
    @ApiModelProperty(value = "商品组ID")
    private Long controlGoodsGroupId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 规格商品ID
     */
    @ApiModelProperty(value = "规格商品ID")
    private Long specificationGoodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String specifications;

    /**
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String producerManufacturer;

    /**
     * 供货含税单价
     */
    @ApiModelProperty("供货含税单价")
    private BigDecimal supplyTaxPrice;

    /**
     * 出库价含税单价
     */
    @ApiModelProperty("出库价含税单价")
    private BigDecimal exitWarehouseTaxPrice;

    /**
     * 零售价含税单价
     */
    @ApiModelProperty("零售价含税单价")
    private BigDecimal retailTaxPrice;

    /**
     * 是否独家
     */
    @ApiModelProperty("是否独家")
    private Boolean exclusiveFlag;

}
