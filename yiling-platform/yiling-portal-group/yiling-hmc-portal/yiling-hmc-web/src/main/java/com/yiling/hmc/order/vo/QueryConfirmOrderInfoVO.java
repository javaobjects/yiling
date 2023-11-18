package com.yiling.hmc.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.hmc.address.vo.QueryAddressVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryConfirmOrderInfoVO extends BaseVO {

    /**
     * 企业ID
     */
    @ApiModelProperty("企业ID")
    private Long eid;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String ename;

    /**
     * 订单总额 = 运费 + 商品总额
     */
    @ApiModelProperty("订单总额")
    private BigDecimal orderTotalAmount;

    @ApiModelProperty("收货地址信息")
    private QueryAddressVO addressVO;

    @ApiModelProperty("药品信息")
    private MarketOrderDetailVO detailVO;
}
