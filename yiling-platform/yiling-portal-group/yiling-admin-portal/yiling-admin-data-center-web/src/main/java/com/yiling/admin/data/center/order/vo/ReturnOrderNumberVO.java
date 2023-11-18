package com.yiling.admin.data.center.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author:tingwei.chen
 * @date:2021/6/18
 */
@Data
@Accessors(chain = true)
public class ReturnOrderNumberVO implements java.io.Serializable {
    /**
     * 今天退货单数量
     */
    @ApiModelProperty(value = "今天退货单数量")
    private Integer todayReturnOrderNum;

    /**
     * 昨天退货订单数量
     */
    @ApiModelProperty(value = "昨天退货订单数量")
    private Integer yesterdayReturnOrderNum;

    /**
     * 所有退货单数量
     */
    @ApiModelProperty(value = "所有退货单数量")
    private Integer AllReturnOrderNum;

}

