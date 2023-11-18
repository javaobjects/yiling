package com.yiling.b2b.app.shop.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺列表 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class ShopListItemVO extends BaseVO {

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;

    /**
     * 店铺企业ID
     */
    @ApiModelProperty("店铺企业ID")
    private Long shopEid;

    /**
     * 店铺logo
     */
    @ApiModelProperty("店铺logo")
    private String shopLogo;

    /**
     * 起配金额
     */
    @ApiModelProperty("起配金额")
    private BigDecimal startAmount;

    /**
     * 商品种类
     */
    @ApiModelProperty("商品种类")
    private Integer goodsKind;

    /**
     * 商品列表
     */
    @ApiModelProperty("商品列表")
    private List<ShopListItemGoodsVO> itemProductList;



}
