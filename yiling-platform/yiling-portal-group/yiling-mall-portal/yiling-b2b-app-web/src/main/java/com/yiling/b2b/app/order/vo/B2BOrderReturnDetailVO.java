package com.yiling.b2b.app.order.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@Accessors(chain = true)
public class B2BOrderReturnDetailVO {

    @ApiModelProperty(value = "订单明细ID", required = true)
    private Long                              detailId;

    @ApiModelProperty(value = "商品id", required = true)
    private Long                              goodsId;

    @ApiModelProperty(value = "商品skuID", required = true)
    private Long                              goodsSkuId;

    @ApiModelProperty(value = "商品名称", required = true)
    private String                            goodsName;

    @ApiModelProperty(value = "商品图片", required = true)
    private String                            goodsPic;

    @ApiModelProperty(value = "商品规格", required = true)
    private String                            goodsSpecification;

    @ApiModelProperty(value = "商品生产厂家", required = true)
    private String                            goodsManufacturer;

    @ApiModelProperty("商品总数量")
    private Integer                           goodsQuantity;

    @ApiModelProperty(value = "退货数量", required = true)
    private Integer                           returnQuantity;

    @ApiModelProperty(value = "退货商品小计", required = true)
    private BigDecimal                        returnGoodsAmount;

    @ApiModelProperty("优惠金额")
    private BigDecimal                        discountAmount;

    @ApiModelProperty("实退金额")
    private BigDecimal                        returnAmount;

    @ApiModelProperty("明细批次信息")
    private List<B2BOrderReturnDetailBatchVO> returnDetailBatchList;

    @ApiModelProperty("活动类型:0无,2:特价,3:秒杀,4:组合促销")
    private Integer                           promotionActivityType;
}
