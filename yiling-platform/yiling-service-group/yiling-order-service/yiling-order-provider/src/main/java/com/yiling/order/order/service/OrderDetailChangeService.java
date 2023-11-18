package com.yiling.order.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.bo.OrderDetailChangeBO;
import com.yiling.order.order.entity.OrderDetailChangeDO;

/**
 * <p>
 * 订单明细变更信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-08-04
 */
public interface OrderDetailChangeService extends BaseService<OrderDetailChangeDO> {

    /**
     * 根据订单明细ID获取订单明细变更信息
     *
     * @param detailId 订单明细ID
     * @return
     */
    OrderDetailChangeDO getByDetailId(Long detailId);

    /**
     * 根据订单ID获取订单明细变更列表
     *
     * @param orderId 订单ID
     * @return
     */
    List<OrderDetailChangeDO> listByOrderId(Long orderId);

    /**
     *根据订单IDS获取订单明细变更信息
     * @param orderIds 订单集合
     * @return
     */
    List<OrderDetailChangeDO> listByOrderIds(List<Long> orderIds);

    /**
     * 更新现折金额
     *
     * @param detailId 订单明细ID
     * @param cashDiscountAmount 现折金额
     * @return
     */
    boolean updateCashDiscountAmountByDetailId(Long detailId, BigDecimal cashDiscountAmount);

    /**
     * 更新票折金额
     *
     * @param detailId 订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag 是否在收货后
     * @return
     */
    boolean updateTicketDiscountAmountByDetailId(Long detailId, BigDecimal ticketDiscountAmount, boolean afterReceiveFlag);

    /**
     * 根据订单ID获取订单明细变更列表Map
     *
     * @param orderId 订单ID
     * @return key：订单明细ID，value：订单明细变更信息
     */
    Map<Long, OrderDetailChangeDO> listMapByOrderId(Long orderId);

    /**
     * 更新发货数据
     *
     * @param detailId 订单明细ID
     * @param deliveryQuantity 发货数量
     * @return
     */
    boolean updateDeliveryData(Long detailId, int deliveryQuantity);

    /**
     * 更新部分发货数据
     * @param detailId 订单明细ID
     * @param deliveryQuantity 发货数量
     * @return
     */
    boolean updatePartDeliveryData(Long detailId, int deliveryQuantity);

    /**
     * 更新部分发货退货数据
     * @param detailId 订单明细ID
     * @return
     */
    boolean updatePartDeliveryReturnData(Long detailId);

    /**
     * 更新收货数据
     *
     * @param detailId 订单明细ID
     * @param receiveQuantity 收货数量
     * @return
     */
    boolean updateReceiveData(Long detailId, int receiveQuantity);



    /**
     * 更新退货数据
     *
     * @param detailId 订单明细ID
     * @param returnQuantity 退货数量
     * @param saveFlag 计算后的数据是否保存
     * @return
     */
    OrderDetailChangeBO updateReturnData(Long detailId, int returnQuantity, boolean saveFlag);

    /**
     * 清理发货数据（同时清理掉后面节点的数据）
     *
     * @param orderId 订单ID
     * @return
     */
    boolean clearDeliveryDataByOrderId(Long orderId);


    /**
     *  清理退货金额
     * @param orderId
     * @return
     */
    boolean clearReturnDataByOrderId(Long orderId);


    /**
     * 仅仅更新发货金额(不计算退货的金额)
     * @param detailId
     * @param deliveryQuantity
     * @return
     */
    boolean updateDeliveryInfo(Long detailId, int deliveryQuantity);


    /**
     * 更新优惠劵金额
     * @param detailId 订单明细ID
     * @param couponDiscountAmount 商家优惠劵
     * @param platformCouponDiscountAmount 平台优惠劵
     * @return
     */
    boolean updateCouponDiscountAmountByDetailId(Long detailId, BigDecimal platformCouponDiscountAmount,BigDecimal couponDiscountAmount,BigDecimal goodsPrice);

    /**
     * 转应收单是更新票折金额
     *
     * @param detailId 订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag 是否在收货后
     * @return
     */
    boolean updateErpReceivableNoTicketAmount(Long detailId,BigDecimal ticketDiscountAmount,boolean afterReceiveFlag);

    /**
     * 删除应收单是更新票折金额
     *
     * @param detailId 订单明细ID
     * @param ticketDiscountAmount 票折金额
     * @param afterReceiveFlag 是否在收货后
     * @return
     */
    boolean updateErpReceivableNoCancelTicketAmount(Long detailId,BigDecimal ticketDiscountAmount,boolean afterReceiveFlag);
}
