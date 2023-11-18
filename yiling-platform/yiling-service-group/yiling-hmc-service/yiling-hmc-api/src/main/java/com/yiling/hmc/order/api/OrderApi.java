package com.yiling.hmc.order.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.yiling.hmc.wechat.dto.request.ConfirmOrderRequest;
import com.yiling.hmc.wechat.dto.request.OrderNotifyRequest;
import com.yiling.hmc.wechat.dto.request.OrderSubmitRequest;

/**
 * 订单API
 *
 * @author: yong.zhang
 * @date: 2022/3/25
 */
public interface OrderApi {

    /**
     * 通过订单id查询订单信息
     *
     * @param id 订单id
     * @return 订单信息
     */
    OrderDTO queryById(Long id);

    /**
     * 通过订单id查询订单信息
     *
     * @param idList 订单id集合
     * @return 订单信息
     */
    List<OrderDTO> listByIdList(List<Long> idList);

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    OrderDTO queryByOrderNo(String orderNo);

    /**
     * 订单信息分页查询
     *
     * @param request 查询条件
     * @return 订单信息
     */
    Page<OrderDTO> pageList(OrderPageRequest request);

    /**
     * 订单同步到保司页面的分页查询
     *
     * @param request 查询条件
     * @return 订单信息
     */
    Page<OrderDTO> syncPageList(SyncOrderPageRequest request);

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
     * 查询用户是否有兑付的订单
     *
     * @param insuranceRecordId
     * @return
     */
    Boolean hasOrder(Long insuranceRecordId);

    /**
     * 获取待确认的订单
     *
     * @param currentUserId
     * @return
     */
    OrderDTO getProcessingOrder(Long currentUserId);

    /**
     * 获取未开方的订单
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
     * 兑付回调
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
     * 保存理赔资料
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
}
