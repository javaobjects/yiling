package com.yiling.order.order.dto;

import lombok.Data;

/**
 *
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
public class OrderGoodsTypeAndNumberDTO implements java.io.Serializable{
    /**
     * 购买商品种类
     */
    private Integer goodsOrderNum;

    /**
     * 购买商品件数
     */
    private Integer goodsOrderPieceNum;

    /**
     * 发货商品种类
     */
    private Integer deliveryOrderNum;

    /**
     * 发货商品件数
     */
    private Integer deliveryOrderPieceNum;


    /**
     * 收获商品种类
     */
    private Integer receiveOrderNum;

    /**
     * 收获商品件数
     */
    private Integer receiveOrderPieceNum;
}
