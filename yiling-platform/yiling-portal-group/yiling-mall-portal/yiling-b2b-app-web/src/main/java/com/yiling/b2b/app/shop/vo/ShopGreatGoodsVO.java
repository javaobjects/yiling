package com.yiling.b2b.app.shop.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺列表的商品信息 VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class ShopGreatGoodsVO extends ShopListItemGoodsVO {

    /**
     * 企业名称（生产厂家）
     */
    @ApiModelProperty("企业名称（生产厂家）")
    private String ename;


}
