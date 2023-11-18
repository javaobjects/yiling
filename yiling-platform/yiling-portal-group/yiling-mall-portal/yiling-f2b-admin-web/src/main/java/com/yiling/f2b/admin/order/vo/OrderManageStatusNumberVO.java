package com.yiling.f2b.admin.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审核订单数量统计
 * @author:wei.wang
 * @date:2021/7/12
 */
@Data
public class OrderManageStatusNumberVO {
    /**
     * 待审核数量
     */
    @ApiModelProperty(value = "待审核数量")
    private Integer reviewingNumber;

    /**
     * 已审核数量
     */
    @ApiModelProperty(value = "已审核数量")
    private Integer reviewNumber;

    /**
     * 驳回数量
     */
    @ApiModelProperty(value = "驳回数量")
    private Integer rejectNumber;
}
