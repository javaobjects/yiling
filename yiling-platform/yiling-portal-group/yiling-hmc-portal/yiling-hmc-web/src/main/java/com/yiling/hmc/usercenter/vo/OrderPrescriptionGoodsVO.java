package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单详情VO
 * </p>
 *
 * @author fan.shen
 * @date 2022/4/12
 */
@Data
public class OrderPrescriptionGoodsVO extends BaseVO {

    /**
     * 兑付订单id
     */
    @ApiModelProperty("兑付订单id")
    private Long orderId;

    /**
     * 兑付订单处方id
     */
    @ApiModelProperty("兑付订单处方id")
    private Long orderPrescriptionId;

    /**
     * 药品名称
     */
    @ApiModelProperty("药品名称")
    private String goodsName;

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    private String specifications;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer goodsQuantity;

    /**
     * 用法用量
     */
    @ApiModelProperty("用法用量")
    private String usageInfo;

    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    private BigDecimal goodsPrice;

}
