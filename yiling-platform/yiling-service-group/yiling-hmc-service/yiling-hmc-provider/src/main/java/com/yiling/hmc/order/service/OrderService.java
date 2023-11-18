package com.yiling.hmc.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.insurance.dto.request.ConfirmTookRequest;
import com.yiling.hmc.insurance.dto.request.SaveClaimInformationRequest;
import com.yiling.hmc.order.bo.OrderBO;
import com.yiling.hmc.order.dto.OrderClaimInformationDTO;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.request.OrderDeliverRequest;
import com.yiling.hmc.order.dto.request.OrderPageRequest;
import com.yiling.hmc.order.dto.request.OrderPrescriptionSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceiptsSaveRequest;
import com.yiling.hmc.order.dto.request.OrderReceivedRequest;
import com.yiling.hmc.order.dto.request.QueryCashPageRequest;
import com.yiling.hmc.order.dto.request.SyncOrderPageRequest;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.wechat.dto.request.ConfirmOrderRequest;
import com.yiling.hmc.wechat.dto.request.OrderNotifyRequest;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
public interface OrderService extends BaseService<OrderDO> {

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    OrderDO queryByOrderNo(String orderNo);

    /**
     * 订单信息分页查询
     *
     * @param request 查询条件
     * @return 订单信息
     */
    Page<OrderDO> pageList(OrderPageRequest request);

    /**
     * 订单同步到保司页面的分页查询
     *
     * @param request 查询条件
     * @return 订单信息
     */
    Page<OrderDO> syncPageList(SyncOrderPageRequest request);

    /**
     * 订单已提
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean received(OrderReceivedRequest request);

    /**
     * 订单发货
     *
     * @param request 请求参数
     * @return 成功/失败
     */
    boolean deliver(OrderDeliverRequest request);

    /**
     * 创建订单
     *
     * @param request
     */
    OrderDTO createOrder(OrderSubmitRequest request);

    /**
     * 是否有过兑付订单
     *
     * @return
     */
    Boolean hasOrder(Long insuranceRecordId);

    /**
     * 获取待确认的订单（已开方+未支付）
     *
     * @param currentUserId
     * @return
     */
    OrderDTO getProcessingOrder(Long currentUserId);

    /**
     * 获取未支付的订单
     *
     * @param request
     * @return
     */
    OrderDTO getUnPayOrder(OrderSubmitRequest request);

    /**
     * 获取待自提的订单
     *
     * @param request
     * @return
     */
    OrderDTO getUnPickUPOrder(OrderSubmitRequest request);

    /**
     * 确认订单
     *
     * @param request
     * @return
     */
    boolean confirmOrder(ConfirmOrderRequest request);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo
     * @return
     */
    OrderDTO getByOrderNo(String orderNo);

    /**
     * 根据订单id查询订单
     *
     * @param id
     * @return
     */
    OrderDTO getByOrderId(Long id);

    /**
     * 参保回调
     *
     * @param orderNotifyRequest
     * @return
     */
    Long orderNotify(OrderNotifyRequest orderNotifyRequest);

    /**
     * 查询保单兑付记录
     *
     * @param request
     * @return
     */
    Page<OrderBO> queryCashPage(QueryCashPageRequest request);

    /**
     * 取药提醒
     *
     * @return 成功/失败
     */
    boolean sendMedicineRemind();

    /**
     * 更新拿药计划状态
     *
     * @param orderDO
     * @return
     */
    boolean updateFetchPlanStatus(OrderDO orderDO);

    /**
     * 保存订单票据
     *
     * @param request 保存订单票据请求参数
     * @return 成功/失败
     */
    boolean saveOrderReceipts(OrderReceiptsSaveRequest request);

    /**
     * 保存处方信息
     *
     * @param request 保存处方信息请求参数
     * @return 成功/失败
     */
    boolean saveOrderPrescription(OrderPrescriptionSaveRequest request);

    /**
     * 获取理赔资料
     *
     * @param id
     * @return
     */
    OrderClaimInformationDTO getClaimInformation(Long id);

    /**
     * 提交理赔资料
     *
     * @param request
     * @return
     */
    boolean submitClaimInformation(SaveClaimInformationRequest request);

    /**
     * 确认已拿
     *
     * @param request
     * @return
     */
    boolean confirmTook(ConfirmTookRequest request);

    /**
     * 同步订单状态
     *
     * @param orderId
     */
    void syncOrderStatus(Long orderId);
}
