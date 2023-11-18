package com.yiling.hmc.admin.mr.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 医药代表可售商品列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@Data
public class SalesGoodsPageListItemVO {

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
     * 售卖规格ID
     */
    @ApiModelProperty("售卖规格ID")
    private Long  sellSpecificationsId;

    /**
     * 售卖规格名称
     */
    @ApiModelProperty("售卖规格名称")
    private String sellSpecifications;

    /**
     * 商品状态：1-上架 2-下架 5-待审核 6-驳回
     */
    @ApiModelProperty("商品状态：1-上架 2-下架 5-待审核 6-驳回")
    private Integer goodsStatus;

    /**
     * 是否已添加
     */
    @ApiModelProperty("是否已添加")
    private Boolean addedFlag;
}
