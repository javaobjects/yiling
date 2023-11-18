package com.yiling.hmc.order.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMarketOrderPageForm extends QueryPageListForm {

    /**
     * 订单状态:0-全部 1-预订单待支付,2-已取消,3-待自提,4-待发货,5-待收货,6-已收货,7-已完成,8-取消已退
     */
    @ApiModelProperty("订单状态:0-全部 1-预订单待支付,2-已取消,3-待自提,4-待发货,5-待收货,6-已收货,7-已完成,8-取消已退")
    private Integer orderStatus;
}
