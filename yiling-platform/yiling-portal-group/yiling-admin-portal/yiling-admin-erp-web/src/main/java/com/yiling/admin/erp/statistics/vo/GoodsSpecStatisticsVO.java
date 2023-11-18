package com.yiling.admin.erp.statistics.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSpecStatisticsVO extends BaseVO {

    @ApiModelProperty("商品+规格id")
    private Long specificationId;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("规格名称")
    private String spec;

    @ApiModelProperty("采购数量")
    private BigDecimal poQuantity;

    @ApiModelProperty("销售数量")
    private BigDecimal soQuantity;

    @ApiModelProperty("月初库存数量")
    private BigDecimal beginMonthQuantity;

    @ApiModelProperty("月末库存数量")
    private BigDecimal endMonthQuantity;
}
