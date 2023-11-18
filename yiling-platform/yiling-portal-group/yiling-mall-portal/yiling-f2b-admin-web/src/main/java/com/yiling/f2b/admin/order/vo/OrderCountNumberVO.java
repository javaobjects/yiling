package com.yiling.f2b.admin.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单统计数量返回VO
 *
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
public class OrderCountNumberVO implements java.io.Serializable {
    /**
     * 今天订单数量
     */
    @ApiModelProperty(value = "今天订单数量")
    private Integer todayOrderNum;

    /**
     * 昨天订单数量
     */
    @ApiModelProperty(value = "昨天订单数量")
    private Integer yesterdayOrderNum;

    /**
     * 进一年数据
     */
    @ApiModelProperty(value = "进一年数据")
    private Integer yearOrderNum;

}

