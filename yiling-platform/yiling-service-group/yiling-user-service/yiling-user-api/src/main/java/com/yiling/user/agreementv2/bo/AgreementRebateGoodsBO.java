package com.yiling.user.agreementv2.bo;

import java.io.Serializable;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利商品 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-10
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateGoodsBO extends BaseVO implements Serializable {

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
     * 生产厂家名称
     */
    @ApiModelProperty("生产厂家名称")
    private String producerManufacturerName;

    /**
     * 品牌厂家名称
     */
    @ApiModelProperty("品牌厂家名称")
    private String brandManufacturerName;

}
