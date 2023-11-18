package com.yiling.f2b.admin.agreementv2.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 厂家商品 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022/2/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementManufacturerGoodsVO extends BaseVO {

    /**
     * 厂家ID
     */
    @ApiModelProperty("厂家ID")
    private Long manufacturerId;

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

    /**
     * 品牌厂家名称
     */
    @ApiModelProperty("品牌厂家名称")
    private String brandManufacturerName;

}
