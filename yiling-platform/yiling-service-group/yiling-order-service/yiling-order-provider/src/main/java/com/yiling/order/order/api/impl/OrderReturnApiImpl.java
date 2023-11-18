package com.yiling.order.order.api.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderReturnApi;
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
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.service.OrderModifyAuditService;
import com.yiling.order.order.service.OrderReturnService;
import com.yiling.order.order.service.ReturnService;

import lombok.RequiredArgsConstructor;

/**
 * 订单退货api
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderReturnApiImpl implements OrderReturnApi {

    private final OrderReturnService orderReturnService;

    private final OrderModifyAuditService orderModifyAuditService;

    @Resource(name = "popReturnService")
    private ReturnService popReturnService;

    @Resource(name = "b2bReturnService")
    private ReturnService b2bReturnService;

    @Override
    public OrderReturnDTO supplierApplyOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        return popReturnService.supplierApplyOrderReturn(orderReturnApplyRequest, orderDTO);
    }

    @Override
    public Boolean beforeDamageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest) {
        return popReturnService.beforeDamageOrderReturn(orderReturnApplyRequest);
    }

    @Override
    public Boolean damageOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        return popReturnService.damageOrderReturn(orderReturnApplyRequest, orderDTO);
    }

    @Override
    public Boolean purchaseApplyReturnOrder(OrderReturnApplyRequest orderReturnApplyRequest, OrderDTO orderDTO) {
        return popReturnService.purchaseApplyReturnOrder(orderReturnApplyRequest, orderDTO);
    }

    @Override
    public OrderReturnDTO checkOrderReturn(OrderReturnApplyRequest orderReturnApplyRequest, boolean isYiLing) {
        OrderReturnVerifyRequest request = PojoUtils.map(orderReturnApplyRequest, OrderReturnVerifyRequest.class);
        request.setReturnId(orderReturnApplyRequest.getOrderReturnId());
        request.setIsSuccess(0);
        request.setIsYiLingInvoice(isYiLing ? 1 : 0);
        return popReturnService.verifyOrderReturn(request);
    }

    @Override
    public Boolean rejectReturnOrder(RejectReturnOrderRequest rejectReturnOrderRequest) {
        OrderReturnVerifyRequest request = PojoUtils.map(rejectReturnOrderRequest, OrderReturnVerifyRequest.class);
        request.setIsSuccess(1);
        OrderReturnDTO orderReturnDTO = popReturnService.verifyOrderReturn(request);
        return null != orderReturnDTO;
    }

    @Override
    public List<OrderReturnDTO> listByOrderId(Long orderId) {
        return orderReturnService.listPassedByOrderId(orderId);
    }

    @Override
    public int countByOrderNoAndType(String orderNo, Integer returnType) {
        return orderReturnService.countByOrderNoAndType(orderNo, returnType);
    }

    @Override
    public int countByOrderIdAndStatus(Long orderId, Integer returnStatus) {
        return orderReturnService.countByOrderIdAndStatus(orderId, returnStatus);
    }

    @Override
    public OrderReturnDTO selectById(Long id) {
        return PojoUtils.map(orderReturnService.getById(id), OrderReturnDTO.class);
    }

    @Override
    public Page<OrderReturnDTO> pageList(OrderReturnPageListRequest request) {
        Page<OrderReturnDO> page = orderReturnService.pageList(request);
        return PojoUtils.map(page, OrderReturnDTO.class);
    }

    @Override
    public List<AgreementOrderReturnDetailDTO> getOrderReturnDetailByEidAndTime(QueryOrderUseAgreementRequest request) {
        return orderReturnService.getOrderReturnDetailByEidAndTime(request);
    }

    @Override
    public ReturnOrderNumberDTO getOrderNumber(ReturnNumberRequest request) {
        return orderReturnService.getOrderNumber(request);
    }

    @Override
    public Page<OrderReturnDTO> getERPPullOrderReturn(OrderReturnPullErpPageRequest request) {
        return orderReturnService.getERPPullOrderReturn(request);
    }

    @Override
    public Boolean updateERPOrderReturnByOrderId(List<UpdateErpOrderReturnRequest> request) {
        return orderReturnService.updateERPOrderReturnByOrderId(request);
    }

    @Override
    public Page<OrderReturnDTO> queryOrderReturnPage(QueryOrderReturnPageRequest request) {
        return orderReturnService.queryOrderReturnPage(request);
    }

    /**
     * 根据退货单状态获取，卖家订单数量
     *
     * @param returnStatus 退货单状态
     * @param eidList 企业集合
     * @param type 1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId 登录用户id
     * @return
     */
    @Override
    public Integer getSellerOrderReturnNumberByReturnStatus(Integer returnStatus, List<Long> eidList, Integer type, Long userId) {
        return orderReturnService.getSellerOrderReturnNumberByReturnStatus(returnStatus, eidList, type, userId);
    }

    @Override
    public boolean insertReturnOrderForModifyAudit(String orderNo) {

        return orderModifyAuditService.insertReturnOrderForModifyAudit(orderNo);
    }

    @Override
    public boolean deleteOrderReturn(Long orderId) {


        return orderReturnService.deleteReturnOrder(orderId);
    }

    @Override
    public Map<Long, List<OrderReturnDetailDTO>> queryByOrderIdList(List<Long> orderIdList) {
        Map<Long, List<OrderReturnDetailDO>> longListMap = orderReturnService.queryByOrderIdList(orderIdList);
        Map<Long, List<OrderReturnDetailDTO>> map = PojoUtils.map(longListMap, OrderReturnDetailDTO.class);
        return map;
    }

    @Override
    public OrderReturnDTO deliverOrderReturn(OrderReturnApplyRequest request) {
        return b2bReturnService.supplierApplyOrderReturn(request, null);
    }

    @Override
    public Boolean applyOrderReturn(OrderReturnApplyRequest request) {
        return b2bReturnService.purchaseApplyReturnOrder(request, null);
    }

    @Override
    public OrderReturnDTO verifyOrderReturn(OrderReturnVerifyRequest request) {
        return b2bReturnService.verifyOrderReturn(request);
    }

    @Override
    public Map<Long, Integer> countByOrderIdList(List<Long> orderIdList) {
        return orderReturnService.countByOrderIdList(orderIdList);
    }

    @Override
    public Page<OrderReturnDTO> pageByCondition(OrderReturnPageRequest request) {
        return orderReturnService.pageByCondition(request);
    }
}
