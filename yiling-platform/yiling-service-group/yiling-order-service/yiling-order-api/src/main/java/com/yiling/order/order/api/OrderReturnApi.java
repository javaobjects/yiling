package com.yiling.order.order.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.AgreementOrderReturnDetailDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.ReturnOrderNumberDTO;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.dto.request.OrderReturnPageRequest;
import com.yiling.order.order.dto.request.OrderReturnPullErpPageRequest;
import com.yiling.order.order.dto.request.OrderReturnVerifyRequest;
import com.yiling.order.order.dto.request.QueryOrderReturnPageRequest;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.dto.request.RejectReturnOrderRequest;
import com.yiling.order.order.dto.request.ReturnNumberRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderReturnRequest;

/**
 * 退货单 API
 *
 * @author: tingwei.chen
 * @date: 2021/6/22
 */
public interface OrderReturnApi {

    /**
     * 供应商退货单申请
     *
     * @param orderReturnApplyRequest 退货单申请请求参数
     * @param orderDTO 订单信息
     * @return 退货单信息
     */
    OrderReturnDTO supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 破损退货单创建前置操作
     *
     * @param orderReturnApplyRequest
     * @return
     */
    Boolean beforeDamageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest);

    /**
     * 录入破损退货单
     *
     * @param orderReturnApplyRequest
     */
    Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 采购商退货单申请
     *
     * @param orderReturnApplyRequest 退货单申请请求参数
     * @return 成功/失败
     */
    Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO);

    /**
     * 销售订单退货单审核
     *
     * @param orderReturnApplyRequest
     * @param isYiLing
     * @return
     */
    OrderReturnDTO checkOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, boolean isYiLing);

    /**
     * 退货单驳回
     *
     * @param rejectReturnOrderRequest
     */
    Boolean rejectReturnOrder(RejectReturnOrderRequest rejectReturnOrderRequest);

    /**
     * 根据订单Id查询出所有通过审核的退货单
     *
     * @param orderId 订单id
     * @return 退货单信息
     */
    List<OrderReturnDTO> listByOrderId(Long orderId);

    /**
     * 查询某订单号某种类型退货的数量
     *
     * @param orderNo 订单编号
     * @param returnType 退货单类型
     * @return 数量
     */
    int countByOrderNoAndType(String orderNo, Integer returnType);

    /**
     * 查询某订单某审核状态的退货单数量
     *
     * @param orderId 订单id
     * @param returnStatus 退货单状态：1-待审核 2-审核通过 3-审核驳回
     * @return 数量
     */
    int countByOrderIdAndStatus(Long orderId, Integer returnStatus);

    /**
     * 根据退货单id查询退货信息
     *
     * @param id 退货单ID
     * @return 退货单信息
     */
    OrderReturnDTO selectById(Long id);

    /**
     * 退货单分页列表查询
     *
     * @param request 请求参数
     * @return 退货单分页信息
     */
    Page<OrderReturnDTO> pageList(OrderReturnPageListRequest request);

    /**
     * 根据采购商EID和退款单审核时间区间 返回已通过退款审核的退款明细
     *
     * @param request 查询条件
     * @return 退货单组装信息
     */
    List<AgreementOrderReturnDetailDTO> getOrderReturnDetailByEidAndTime(QueryOrderUseAgreementRequest request);

    /**
     * 查询出今天，昨天和总共退货单数量
     *
     * @param request 查询条件
     * @return 今天，昨天和总共退货单数量对象
     */
    ReturnOrderNumberDTO getOrderNumber(ReturnNumberRequest request);

    /**
     * 查询EAS待同步的退货单列表--查询的时候要加已经开发票的判定(订单表)
     *
     * @param request 查询条件
     * @return 退货单分页信息
     */
    Page<OrderReturnDTO> getERPPullOrderReturn(OrderReturnPullErpPageRequest request);

    /**
     * 更新退货单ERP同步状态
     *
     * @param request 集体更新退货单ERP推送信息
     * @return 成功/失败
     */
    Boolean updateERPOrderReturnByOrderId(List<UpdateErpOrderReturnRequest> request);

    /**
     * 查询退货单列表
     *
     * @param request 查询条件
     * @return 退货单信息
     */
    Page<OrderReturnDTO> queryOrderReturnPage(QueryOrderReturnPageRequest request);

    /**
     * 根据退货单状态获取，卖家订单数量
     *
     * @param returnStatus 退货单状态
     * @param eidList 企业集合
     * @param type 1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId 登录用户id
     * @return 符合条件订单数量
     */
    Integer getSellerOrderReturnNumberByReturnStatus(Integer returnStatus, List<Long> eidList, Integer type, Long userId);


    /**
     * 反审生成退货单
     *
     * @param orderNo 订单编号
     * @return 成功/失败
     */
    boolean insertReturnOrderForModifyAudit(String orderNo);

    /**
     * 清除退货单数据
     *
     * @param orderId 订单id
     * @return 成功/失败
     */
    boolean deleteOrderReturn(Long orderId);

    /**
     * 根据订单集合查询出所有的退货明细
     *
     * @param orderIdList 订单id集合
     * @return 退货明细集合
     */
    Map<Long, List<OrderReturnDetailDTO>> queryByOrderIdList(List<Long> orderIdList);

    /**
     * 申请退货-部分发货
     *
     * @param request 申请退货请求参数
     * @return 成功/失败
     */
    OrderReturnDTO deliverOrderReturn(OrderReturnApplyRequest request);

    /**
     * 申请退货--采购商申请
     *
     * @param request 申请退货请求参数
     * @return 成功/失败
     */
    Boolean applyOrderReturn(OrderReturnApplyRequest request);

    /**
     * 退货单审核
     *
     * @param request 退货单审核请求参数-B2B
     * @return 退货单信息
     */
    OrderReturnDTO verifyOrderReturn(OrderReturnVerifyRequest request);

    /**
     * 批量根据订单id统计退货单数量
     *
     * @param orderIdList 订单id集合
     * @return (订单id, 退货单数量)
     */
    Map<Long, Integer> countByOrderIdList(List<Long> orderIdList);

    /**
     * 分页查询退货单信息
     *
     * @param request 分页查询请求参数
     * @return 退货单分页数据
     */
    Page<OrderReturnDTO> pageByCondition(OrderReturnPageRequest request);
}
