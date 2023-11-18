package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.request.QueryPromotionNumberRequest;

/**
 * 订单明细api查询
 * @author:wei.wang
 * @date:2021/6/22
 */
public interface OrderDetailApi {
    /**
     * 根据orderId批量获取明细信息
     * @param orderIds
     * @return
     */
    List<OrderDetailDTO> getOrderDetailByOrderIds(List<Long> orderIds);

    /**
     * 根据订单查询购买收货返货数量和种类
     * @param orderId
     * @return
     */
    OrderGoodsTypeAndNumberDTO getOrderGoodsTypeAndNumber(Long orderId);

    /**
     * 获取明细信息
     * @param orderId
     * @return
     */
    List<OrderDetailDTO> getOrderDetailInfo(Long orderId);

    /**
     * 根据订单编号查询订单明细
     * @param orderNo
     * @return
     */
    List<OrderDetailDTO> selectlistByOrderNo(String orderNo,List goodsIdList);

    /**
     * 根据订单明细id查询订单明细
     *
     * @param idList
     * @return
     */
    List<OrderDetailDTO> listByIdList(List<Long> idList);

    /**
     * 根据明细id查询明细信息
     * @param id 明细id
     * @return
     */
    OrderDetailDTO getOrderDetailById(Long id);


    /**
     * 查询促销活动商品库存
     * @param promotionNumberRequest 查询促销活动商品库存参数
     * @return
     */
    Integer getPromotionNumberByDistributorGoodsId(QueryPromotionNumberRequest promotionNumberRequest);
}
