package com.yiling.order.order.api.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderReportApi;
import com.yiling.order.order.bo.BuyerOrderSumBO;
import com.yiling.order.order.dto.ContacterOrderSumDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderFullDTO;
import com.yiling.order.order.dto.OrderSumDTO;
import com.yiling.order.order.dto.request.GoodDetailSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderSumQueryPageRequest;
import com.yiling.order.order.dto.request.OrderSumQueryRequest;
import com.yiling.order.order.dto.request.QueryBuyerOrderDetailPageRequest;
import com.yiling.order.order.dto.request.QueryBuyerOrderPageRequest;
import com.yiling.order.order.dto.request.QueryBuyerReceiveOrderDetailPageRequest;
import com.yiling.order.order.dto.request.QueryBuyerReceiveOrderPageRequest;
import com.yiling.order.order.dto.request.ReceiveOrderSumQueryRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.service.OrderDetailService;
import com.yiling.order.order.service.OrderService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单统计报表
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.api.impl
 * @date: 2021/9/30
 */
@DubboService
@Slf4j
public class OrderReportApiImpl implements OrderReportApi {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderDetailApi orderDetailApi;

    /**
     * 下单金额统计
     * @param queryRequest 下单人信息
     * @return
     */
    @Override
    public OrderSumDTO sumReceiveOrderReportByCreateUserId(ReceiveOrderSumQueryRequest queryRequest) {

        Assert.notEmpty(queryRequest.getUserIdList(), "下单人信息不能为空!");

        OrderSumQueryRequest orderSumQueryRequest = PojoUtils.map(queryRequest,OrderSumQueryRequest.class);
        orderSumQueryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));

        return orderService.sumOrderReportByUserIdList(orderSumQueryRequest,1);
    }

    /**
     * 根据商务联系人统计订单信息
     * @param queryRequest 商务联系人信息
     * @return
     */
    @Override
    public OrderSumDTO sumReceiveOrderReportByContacterId(ReceiveOrderSumQueryRequest queryRequest) {

        Assert.notEmpty(queryRequest.getUserIdList(), "商务联人信息不能为空!");
        OrderSumQueryRequest orderSumQueryRequest = PojoUtils.map(queryRequest,OrderSumQueryRequest.class);
        orderSumQueryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));

        return orderService.sumOrderReportByUserIdList(orderSumQueryRequest,2);
    }

    /**
     * 查询下单人的已收货订单信息
     * @param queryRequest 下单人信息
     * @return
     */
    @Override
    public List<OrderDTO> selectReceiveOrderListByCreateUserId(ReceiveOrderSumQueryRequest queryRequest) {

        Assert.notEmpty(queryRequest.getUserIdList(), "下单人信息不能为空!");
        OrderSumQueryRequest orderSumQueryRequest = PojoUtils.map(queryRequest,OrderSumQueryRequest.class);
        orderSumQueryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));
        List<OrderDO> resultList = orderService.selectOrderListByCreateUserId(orderSumQueryRequest);

        return PojoUtils.map(resultList,OrderDTO.class);
    }

    /**
     * 查询下单人的已收货订单信息分页
     * @param queryRequest 下单人信息
     * @return
     */
    @Override
    public Page<OrderDTO> selectReceiveOrderPageByCreateUserId(OrderSumQueryPageRequest queryRequest) {

        Assert.notEmpty(queryRequest.getUserIdList(), "下单人信息不能为空!");
        OrderSumQueryPageRequest orderSumQueryRequest = PojoUtils.map(queryRequest,OrderSumQueryPageRequest.class);
        orderSumQueryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));
        Page<OrderDO> resultList = orderService.selectOrderPageByCreateUserId(orderSumQueryRequest);

        return PojoUtils.map(resultList,OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> selectReceiveOrderPageByContacterId(OrderSumQueryPageRequest queryRequest) {

        Assert.notEmpty(queryRequest.getUserIdList(), "商务联系人信息不能为空!");
        OrderSumQueryPageRequest orderSumQueryRequest = PojoUtils.map(queryRequest,OrderSumQueryPageRequest.class);
        orderSumQueryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));
        Page<OrderDO> resultList = orderService.selectOrderPageByContacterId(orderSumQueryRequest);

        return PojoUtils.map(resultList,OrderDTO.class);
    }

    @Override
    public List<OrderDTO> selectOrderListByContacterId(ReceiveOrderSumQueryRequest queryRequest) {

        Assert.notEmpty(queryRequest.getUserIdList(), "商务联系人信息不能为空!");
        OrderSumQueryRequest request = PojoUtils.map(queryRequest,OrderSumQueryRequest.class);
        request.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));
        List<OrderDO>  resultList = orderService.selectOrderListByContacterId(request);

        return PojoUtils.map(resultList,OrderDTO.class);
    }

    /**
     * 根据商品ID查询已收货订单信息
     * @param distributorGoodsId 商品Id
     * @return
     */
    @Override
    public List<OrderDTO> selectReceiveOrderFullInfoByDistributorGoodsId(Long distributorGoodsId) {

        Assert.notNull(distributorGoodsId, "商品Id不能为空!");
        List<OrderDO> orderList =  orderService.selectReceiveOrderByDistributorGoodId(distributorGoodsId);

        if (CollectionUtil.isEmpty(orderList)) {

            return Collections.emptyList();
        }

        return PojoUtils.map(orderList,OrderDTO.class);
    }



    /**
     * 根据以岭商品ID查询收货订单信息
     * @param goodId 以岭商品ID
     * @param buyerEidList 买家ID
     * @return
     */
    @Override
    public List<OrderDTO> selectReceiveOrderFullInfoByGoodsId(Long goodId, List<Long> buyerEidList) {

        Assert.notNull(goodId, "商品Id不能为空!");
        List<OrderDO> orderList =  orderService.selectReceiveOrderByGoodId(goodId,buyerEidList);

        if (CollectionUtil.isEmpty(orderList)) {

            return Collections.emptyList();
        }

        return PojoUtils.map(orderList,OrderDTO.class);
    }

    /**
     * 通过商品Id查询订单信息（分页）,type 1表示根据以岭商品ID查询，2表示为根据配送商商品ID查询
     * @param goodDetailPageRequest
     * @return
     */
    @Override
    public Page<OrderDTO> pageReceiveOrderFullInfoByGoodsId(GoodDetailSumQueryPageRequest goodDetailPageRequest) {

        Assert.notNull(goodDetailPageRequest.getGoodId(), "商品Id不能为空!");
        Assert.notNull(goodDetailPageRequest.getType(), "商品类型字段不能为空!");

        Page<OrderDO>  page = orderService.pageReceiveOrderByGoodId(goodDetailPageRequest);

        if (page == null || CollectionUtil.isEmpty(page.getRecords())) {

            return new Page<>(goodDetailPageRequest.getCurrent(),goodDetailPageRequest.getSize());
        }

        return PojoUtils.map(page,OrderDTO.class);
    }

    /**
     * 通过企业ID获取卖家已收货订单信息
     * @param buyerReceiveOrderPageRequest 企业IDs
     * @return
     */
    @Override
    public Page<OrderFullDTO> selectBuyerReceiveOrdersByEids(QueryBuyerReceiveOrderPageRequest buyerReceiveOrderPageRequest) {

        Assert.notEmpty(buyerReceiveOrderPageRequest.getBuyerEidList(), "买家企业不能为空!");
        QueryBuyerOrderPageRequest queryBuyerOrderPageRequest = PojoUtils.map(buyerReceiveOrderPageRequest,QueryBuyerOrderPageRequest.class);
        queryBuyerOrderPageRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));

        Page<OrderDO> orderDOPage = orderService.selectBuyerOrdersByEids(queryBuyerOrderPageRequest);

        if (ObjectUtil.isNull(orderDOPage) || orderDOPage.getTotal() == 0) {

            return new Page<>(buyerReceiveOrderPageRequest.getCurrent(),buyerReceiveOrderPageRequest.getSize());
        }

        Page<OrderFullDTO> pageResult = new Page<OrderFullDTO>();
        pageResult.setTotal(orderDOPage.getTotal());
        pageResult.setCurrent(orderDOPage.getCurrent());
        pageResult.setSize(orderDOPage.getSize());

        List<OrderFullDTO> records =  orderDOPage.getRecords().stream().map(t -> {
            OrderFullDTO orderFullDTO =  PojoUtils.map(t,OrderFullDTO.class);
            List<OrderDetailDTO> detailResult = orderDetailService.getOrderDetailInfo(t.getId());
            orderFullDTO.setOrderDetailDTOList(detailResult);
            orderFullDTO.setGoodsNum(detailResult.stream().mapToLong(OrderDetailDTO::getGoodsQuantity).sum());
            orderFullDTO.setTypeNum(detailResult.size());
            return orderFullDTO;
        }).collect(Collectors.toList());

        pageResult.setRecords(records);

        return pageResult;
    }

    /**
     * 根据订单ID获取订单信息(包括明细)
     * @param orderId 订单Id
     * @return
     */
    @Override
    public OrderFullDTO selectOrderFullInfoByOrderId(Long orderId) {
        OrderDTO orderDTO = orderService.getOrderInfo(orderId);
        if (ObjectUtil.isNull(orderDTO)) {
            return null;
        }

        OrderFullDTO orderFullDTO =  PojoUtils.map(orderDTO,OrderFullDTO.class);
        List<OrderDetailDTO> detailResult = orderDetailApi.getOrderDetailInfo(orderId);
        orderFullDTO.setOrderDetailDTOList(detailResult);
        orderFullDTO.setGoodsNum(detailResult.stream().mapToLong(OrderDetailDTO::getGoodsQuantity).sum());
        orderFullDTO.setTypeNum(detailResult.size());
        return orderFullDTO;
    }

    /**
     * 按照商品维度，统计买家已收货商品信息
     * @param pageRequest 买家信息参数
     * @return
     */
    @Override
    public BuyerOrderSumBO selectReceiveGoodsReportByBuyerListInfo(QueryBuyerReceiveOrderDetailPageRequest pageRequest) {

        Assert.notEmpty(pageRequest.getBuyerEidList(), "买家信息不能为空!");
        QueryBuyerOrderDetailPageRequest request = PojoUtils.map(pageRequest,QueryBuyerOrderDetailPageRequest.class);
        request.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));

        return orderDetailService.selectGoodsReportByBuyerListInfo(request);
    }


    /**
     * 根据商务联系人统计，订单金额，以及收货数量
     * @param contacterIdList 商务联系人信息（商务联系人信息必填）
     * @return
     */
    @Override
    public List<ContacterOrderSumDTO> sumReceiveOrderReportByContacterId(List<Long> contacterIdList) {

        if (CollectionUtil.isEmpty(contacterIdList)) {

            return Collections.emptyList();
        }

        return  contacterIdList.stream().map(contacterId -> {
            OrderSumQueryRequest queryRequest = new OrderSumQueryRequest();
            queryRequest.setUserIdList(ListUtil.toList(contacterId));
            queryRequest.setOrderStatusList(ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));
            OrderSumDTO  orderSumDto = orderService.sumOrderReportByUserIdList(queryRequest,2);

            if (orderSumDto == null || CompareUtil.compare(orderSumDto.getTotalOrderMoney(), BigDecimal.ZERO) <= 0) {
                return null;
            }
            Integer receiveOrderNumber  = orderDetailService.getRecevieGoodsOrderContacterId(contacterId,ListUtil.toList(OrderStatusEnum.RECEIVED.getCode(),OrderStatusEnum.FINISHED.getCode()));
            ContacterOrderSumDTO customerOrderSumDTO = ContacterOrderSumDTO.builder().contacterId(contacterId).totalOrderMoney(orderSumDto.getTotalOrderMoney()).totalReceiveTotalQuantity(receiveOrderNumber).build();
            return customerOrderSumDTO;

        }).filter(e -> ObjectUtil.isNotEmpty(e)).collect(Collectors.toList());
    }

    /**
     * 通过买家企业ID统计计已收货订单金额
     * @param buyerEid 企业ID
     * @return
     */
    @Override
    public OrderSumDTO sumReceiveOrderReportByBuyerEid(Long buyerEid) {

        return orderService.sumOrderReportByBuyerEid(buyerEid,ListUtil.toList(OrderStatusEnum.FINISHED.getCode(),OrderStatusEnum.FINISHED.getCode()));
    }
}
