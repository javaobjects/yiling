package com.yiling.f2b.admin.order.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商品信息和批次信息集合VO
 * @author:wei.wang
 * @date:2021/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailDeliveryVO extends OrderDetailVO {
    @ApiModelProperty(value = "批次信息")
    private List<OrderDeliveryVO> orderDeliveryList;
}
