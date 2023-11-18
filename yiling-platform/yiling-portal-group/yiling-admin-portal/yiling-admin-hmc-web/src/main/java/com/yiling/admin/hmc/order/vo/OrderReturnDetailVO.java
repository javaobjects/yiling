package com.yiling.admin.hmc.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 退货单明细表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
public class OrderReturnDetailVO extends BaseVO {

    @ApiModelProperty("退货单id")
    private Long returnId;

    @ApiModelProperty("订单明细id")
    private Long detailId;

    @ApiModelProperty("药品id")
    private Long goodsId;

    @ApiModelProperty("药品名称")
    private String goodsName;

    @ApiModelProperty("退货数量")
    private Long returnQuality;

    @ApiModelProperty("退单价格")
    private BigDecimal returnPrice;

    @ApiModelProperty("药品规格")
    private String goodsSpecifications;


    // ====================================================

    @ApiModelProperty("是否管控")
    private Integer isControl;

    @ApiModelProperty("管控采购渠道")
    private String controlChannel;

}
