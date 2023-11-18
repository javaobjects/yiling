package com.yiling.sales.assistant.app.enterprise.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 客户商品列表 VO
 *
 * @author: lun.yu
 * @date: 2021/9/24
 */
@Builder
@Data
@ApiModel("客户商品列表VO")
public class UserProductListItemVO {

    @ApiModelProperty("商品ID")
    private String goodsId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品规格")
    private String goodsSpecification;

    @ApiModelProperty("发货数量")
    private Integer deliveryQuantity;

    @ApiModelProperty("收货数量")
    private Date receiveQuantity;

}
