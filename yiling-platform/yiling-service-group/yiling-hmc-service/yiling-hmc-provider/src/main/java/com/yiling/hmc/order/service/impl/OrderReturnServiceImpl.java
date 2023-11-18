package com.yiling.hmc.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.pojo.Result;
import com.yiling.hmc.order.dao.OrderReturnMapper;
import com.yiling.hmc.order.dto.request.OrderReturnApplyRequest;
import com.yiling.hmc.order.dto.request.OrderReturnPageRequest;
import com.yiling.hmc.order.entity.OrderDO;
import com.yiling.hmc.order.entity.OrderDetailDO;
import com.yiling.hmc.order.entity.OrderReturnDO;
import com.yiling.hmc.order.entity.OrderReturnDetailDO;
import com.yiling.hmc.order.enums.HmcOrderOperateTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderReturnStatusEnum;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.service.OrderDetailService;
import com.yiling.hmc.order.service.OrderOperateService;
import com.yiling.hmc.order.service.OrderReturnDetailService;
import com.yiling.hmc.order.service.OrderReturnService;
import com.yiling.hmc.order.service.OrderService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 退货表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderReturnServiceImpl extends BaseServiceImpl<OrderReturnMapper, OrderReturnDO> implements OrderReturnService {

    @DubboReference
    protected NoApi noApi;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private final OrderReturnDetailService orderReturnDetailService;

    private final OrderOperateService orderOperateService;

    @Override
    public OrderReturnDO queryByOrderId(Long orderId) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getOrderId, orderId);
        wrapper.lambda().last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public Page<OrderReturnDO> pageList(OrderReturnPageRequest request) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getReturnNo())) {
            wrapper.lambda().eq(OrderReturnDO::getReturnNo, request.getReturnNo());
        }
        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().eq(OrderReturnDO::getOrderNo, request.getOrderNo());
        }
        if (StringUtils.isNotBlank(request.getThirdReturnNo())) {
            wrapper.lambda().eq(OrderReturnDO::getThirdReturnNo, request.getThirdReturnNo());
        }
        if (null != request.getEid()) {
            wrapper.lambda().eq(OrderReturnDO::getEid, request.getEid());
        }
        if (StringUtils.isNotBlank(request.getEname())) {
            wrapper.lambda().like(OrderReturnDO::getEname, request.getEname());
        }
        wrapper.lambda().orderByDesc(OrderReturnDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public Result apply(OrderReturnApplyRequest request) {
        OrderDO orderDO = orderService.getById(request.getOrderId());
        // 1.判断订单是否存在,状态是否正确,是否收货
        if (null == orderDO) {
            return Result.failed("订单不存在");
        }
        // 订单药品有没有给到用户，给到用户后不允许退单
        boolean isErrorOrderStatus = HmcOrderStatusEnum.UN_PICK_UP != HmcOrderStatusEnum.getByCode(orderDO.getOrderStatus()) && HmcOrderStatusEnum.UN_DELIVERED != HmcOrderStatusEnum.getByCode(orderDO.getOrderStatus()) && HmcOrderStatusEnum.UN_RECEIVED != HmcOrderStatusEnum.getByCode(orderDO.getOrderStatus());
        if (isErrorOrderStatus) {
            return Result.failed("订单状态不对,不允许退单");
        }

        // 2.新增退货单
        OrderReturnDO orderReturnDO = saveOrderReturn(orderDO, request);

        // 3.新增退货单明细
        saveOrderReturnDetail(orderDO, orderReturnDO, request);

        // 4.(退款)修改订单状态为取消已退
        saveOrderInOrderReturnApply(orderDO, request);

        // 5.新增日志
        orderOperateService.saveOrderOperate(orderDO.getId(), HmcOrderOperateTypeEnum.RETURN, request.getLogContent(), request.getOpUserId(), request.getOpTime());

        return Result.success();
    }

    /**
     * 退货时订单状态的修改（取消已退）
     *
     * @param orderDO 订单信息
     * @param request 退货单申请请求参数
     */
    private void saveOrderInOrderReturnApply(OrderDO orderDO, OrderReturnApplyRequest request) {
        OrderDO order = new OrderDO();
        order.setId(orderDO.getId());
        order.setOrderStatus(HmcOrderStatusEnum.RETURN_CANCELED.getCode());
        order.setOpUserId(request.getOpUserId());
        order.setOpTime(request.getOpTime());
        orderService.updateById(order);
    }

    /**
     * 新增退货单明细
     *
     * @param orderDO 订单信息
     * @param orderReturnDO 退货单信息
     * @param request 请求参数
     */
    private void saveOrderReturnDetail(OrderDO orderDO, OrderReturnDO orderReturnDO, OrderReturnApplyRequest request) {
        List<OrderDetailDO> orderDetailDOList = orderDetailService.listByOrderId(orderDO.getId());
        List<OrderReturnDetailDO> insertList = new ArrayList<>();
        orderDetailDOList.forEach(item -> {
            OrderReturnDetailDO orderReturnDetailDO = new OrderReturnDetailDO();
            orderReturnDetailDO.setReturnId(orderReturnDO.getId());
            orderReturnDetailDO.setDetailId(item.getId());
            orderReturnDetailDO.setGoodsId(item.getGoodsId());
            orderReturnDetailDO.setGoodsName(item.getGoodsName());
            orderReturnDetailDO.setReturnQuality(item.getGoodsQuantity());
            orderReturnDetailDO.setReturnPrice(item.getInsurancePrice());
            orderReturnDetailDO.setOpUserId(request.getOpUserId());
            orderReturnDetailDO.setOpTime(request.getOpTime());
            insertList.add(orderReturnDetailDO);
        });
        if (CollUtil.isNotEmpty(insertList)) {
            orderReturnDetailService.saveBatch(insertList);
        }
    }

    /**
     * 新增退货单
     *
     * @param orderDO 订单信息
     * @param request 请求参数
     * @return 退货单信息
     */
    private OrderReturnDO saveOrderReturn(OrderDO orderDO, OrderReturnApplyRequest request) {
        OrderReturnDO orderReturnDO = new OrderReturnDO();
        orderReturnDO.setReturnNo(noApi.gen(NoEnum.ORDER_RETURN_NO));
        orderReturnDO.setOrderId(orderDO.getId());
        orderReturnDO.setOrderNo(orderDO.getOrderNo());
        orderReturnDO.setThirdReturnNo("");
        orderReturnDO.setEid(orderDO.getEid());
        orderReturnDO.setEname(orderDO.getEname());
        orderReturnDO.setReturnStatus(HmcOrderReturnStatusEnum.RETURNED.getCode());
        orderReturnDO.setReturnAmount(orderDO.getTotalAmount());
        orderReturnDO.setContent(request.getLogContent());
        orderReturnDO.setOpUserId(request.getOpUserId());
        orderReturnDO.setOpTime(request.getOpTime());
        this.save(orderReturnDO);
        return orderReturnDO;
    }
}
