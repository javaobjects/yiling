package com.yiling.order.order.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单完整信息，包含明细信息，以及种类
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderFullDTO extends OrderDTO {

    private static final long serialVersionUID=-5015589816754040971L;

    /**
     * 商品总数量
     */
    private Long goodsNum;

    /**
     * 商品类别数量
     */
    private Integer typeNum;

    /**
     * 商品明细信息
     */
    private List<OrderDetailDTO> orderDetailDTOList;

}
