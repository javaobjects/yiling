package com.yiling.framework.common.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 简单商品信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/15
 */
@Data
public class SimpleGoodsVO {

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String name;

    /**
     * 商品类型 1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7医疗器械
     */
    @ApiModelProperty(value = "商品类型 1普通药品 2中药饮片 3中药材 4消杀 5保健食品 6食品 7医疗器械", example = "1")
    private Integer goodsType;

    /**
     * 封面图URL
     */
    @ApiModelProperty("封面图URL")
    private String pictureUrl;

    /**
     * 批准文号
     */
    @ApiModelProperty("批准文号")
    private String licenseNo;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String specification;

    /**
     * 生产厂家
     */
    @ApiModelProperty("生产厂家")
    private String manufacturer;

    /**
     * 单位
     */
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 超卖标识：0-非超卖  1-超卖
     */
    @ApiModelProperty(value = "超卖标识：0-非超卖  1-超卖", example = "1")
    private Integer overSoldType;

}
