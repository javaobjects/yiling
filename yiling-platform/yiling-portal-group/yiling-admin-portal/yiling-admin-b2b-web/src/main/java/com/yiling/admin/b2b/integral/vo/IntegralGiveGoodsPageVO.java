package com.yiling.admin.b2b.integral.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单送积分-店铺SKU VO
 *
 * @author: lun.yu
 * @date: 2023-01-04
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralGiveGoodsPageVO extends BaseVO {
    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String manufacturer;

    /**
     * 商品类型，字典：standard_goods_type（1-普通药品；2-中药饮片；3-中药材；4-消杀；5-健食品；6-食品）
     */
    @ApiModelProperty("商品类型，字典：standard_goods_type（1-普通药品；2-中药饮片；3-中药材；4-消杀；5-健食品；6-食品）")
    private Integer goodsType;

    /**
     * 销售规格
     */
    @ApiModelProperty("销售规格")
    private String sellSpecifications;

    /**
     * 商品基价
     */
    @ApiModelProperty("商品基价")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @ApiModelProperty("库存数量")
    private Long goodsInventory;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String sellUnit;

    /**
     * 商品状态 1上架 2下架 3待设置
     */
    @ApiModelProperty("商品状态 1上架 2下架 3待设置")
    private Integer goodsStatus;

    /**
     * 是否以岭品 true是 false不是
     */
    @ApiModelProperty("是否以岭品 1是 2不是")
    private Integer yilingGoodsFlag;
}
