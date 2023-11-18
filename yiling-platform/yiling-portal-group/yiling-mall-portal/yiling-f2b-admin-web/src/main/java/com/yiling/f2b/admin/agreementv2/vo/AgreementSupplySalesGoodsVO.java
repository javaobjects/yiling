package com.yiling.f2b.admin.agreementv2.vo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议供销商品 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementSupplySalesGoodsVO extends BaseVO {

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
