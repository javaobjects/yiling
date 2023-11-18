package com.yiling.order.order.service.impl;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.bo.BuyerOrderDetailSumBO;
import com.yiling.order.order.bo.BuyerOrderSumBO;
import com.yiling.order.order.dao.OrderDetailMapper;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.request.BatchQueryUserBuyNumberRequest;
import com.yiling.order.order.dto.request.QueryBuyerOrderDetailPageRequest;
import com.yiling.order.order.dto.request.QueryPromotionNumberRequest;
import com.yiling.order.order.dto.request.QueryUserBuyNumberRequest;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.entity.OrderDetailDO;
import com.yiling.order.order.service.OrderDeliveryService;
import com.yiling.order.order.service.OrderDetailService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 订单明细 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetailMapper, OrderDetailDO> implements OrderDetailService {

    @Autowired
    private OrderDeliveryService orderDeliveryService;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private SpringAsyncConfig  springAsyncConfig;

    /**
     * 根据orderId查询订单明细
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDetailDO> getOrderDetailByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDetailDO::getOrderId, orderIds);
        return this.list(wrapper);
    }

    /**
     * 根据订单查询购买收货返货数量和种类
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderGoodsTypeAndNumberDTO getOrderGoodsTypeAndNumber(Long orderId) {
        OrderGoodsTypeAndNumberDTO dto = new OrderGoodsTypeAndNumberDTO();
        QueryWrapper<OrderDetailDO> wrapper = new QueryWrapper();
        wrapper.lambda().eq(OrderDetailDO::getOrderId, orderId);
        List<OrderDetailDO> list = list(wrapper);
        Set<Long> set = new HashSet();
        Integer goodsOrderPieceNum = 0;
        for (OrderDetailDO one : list) {
            set.add(one.getGoodsId());
            goodsOrderPieceNum = goodsOrderPieceNum + one.getGoodsQuantity();
        }
        dto.setGoodsOrderNum(set.size());
        dto.setGoodsOrderPieceNum(goodsOrderPieceNum);
        getOrderDeliveryNumber(orderId, dto);
        return dto;
    }

    /**
     * 获取明细信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDetailDTO> getOrderDetailInfo(Long orderId) {
        QueryWrapper<OrderDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDetailDO::getOrderId, orderId);
        List<OrderDetailDO> detailList = list(wrapper);
        return PojoUtils.map(detailList, OrderDetailDTO.class);
    }


    /**
     * 获取发货和收货商品种类和熟练
     *
     * @param orderId
     */
    private void getOrderDeliveryNumber(Long orderId, OrderGoodsTypeAndNumberDTO dto) {
        QueryWrapper<OrderDeliveryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryDO::getOrderId, orderId);
        List<OrderDeliveryDO> list = orderDeliveryService.list(wrapper);
        Integer deliveryOrderPieceNum = 0;
        Integer receiveOrderPieceNum = 0;
        Set<Long> deliverySet = new HashSet<>();
        Set<Long> receiveSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(list)) {
            for (OrderDeliveryDO one : list) {
                if (one.getDeliveryQuantity() > 0) {
                    deliverySet.add(one.getGoodsId());

                    if (one.getReceiveQuantity() > 0) {
                        receiveSet.add(one.getGoodsId());
                    }
                }
                deliveryOrderPieceNum = deliveryOrderPieceNum + one.getDeliveryQuantity();
                receiveOrderPieceNum = receiveOrderPieceNum + one.getReceiveQuantity();
            }
            dto.setDeliveryOrderNum(deliverySet.size());
            dto.setDeliveryOrderPieceNum(deliveryOrderPieceNum);
            dto.setReceiveOrderNum(receiveSet.size());
            dto.setReceiveOrderPieceNum(receiveOrderPieceNum);
        }

    }

    int updateReturnQuantity(OrderDetailDO orderDetailDO) {

        return orderDetailMapper.updateReturnQuantity(orderDetailDO);

    }


    @Override
    public BuyerOrderSumBO selectGoodsReportByBuyerListInfo(QueryBuyerOrderDetailPageRequest request) {

        BuyerOrderSumBO buyerOrderSumBo = this.getBaseMapper().selectBuyerSumReportByBuyerEidList(request);
        Page<BuyerOrderDetailSumBO> buyerOrderDetailSumBOPage = this.getBaseMapper().selectOrderDetailReportListByBuyerEidList(request.getPage(), request);
        buyerOrderSumBo.setOrderDetailSumBOPage(buyerOrderDetailSumBOPage);
        buyerOrderSumBo.setOrderNumberType(buyerOrderDetailSumBOPage.getTotal());

        return buyerOrderSumBo;
    }

    @Override
    public Integer getRecevieGoodsOrderContacterId(Long contacterId, List<Integer> orderStatusList) {

        return this.orderDetailMapper.getRecevieGoodsOrderContacterId(contacterId, orderStatusList);
    }


    @Override
    public Integer getPromotionNumberByDistributorGoodsId(QueryPromotionNumberRequest promotionNumberRequest) {

        return orderDetailMapper.getPromotionNumberByDistributorGoodsId(promotionNumberRequest);
    }

    @Override
    public Long getUserBuyNumber(QueryUserBuyNumberRequest queryUserBuyNumberRequest) {

        Assert.notNull(queryUserBuyNumberRequest.getBuyerEid(), "参数buyerEid不能为空");
        Assert.notNull(queryUserBuyNumberRequest.getGoodId(), "商品ID不能为空");
        Assert.notNull(queryUserBuyNumberRequest.getEndTime(), "结束时间不能为空");
        Assert.notNull(queryUserBuyNumberRequest.getStartTime(), "开始时间不能为空");

        return orderDetailMapper.getUserBuyNumber(queryUserBuyNumberRequest);
    }

    @Override
    public Map<Long, Long> getBatchUserBuyNumber(BatchQueryUserBuyNumberRequest request) {

        Assert.notNull(request.getBuyerEid(), "参数buyerEid不能为空");
        Assert.notEmpty(request.getGoodIds(), "商品ID不能为空");
        Assert.notNull(request.getEndTime(), "结束时间不能为空");
        Assert.notNull(request.getStartTime(), "开始时间不能为空");

        // 分批100执行一次
        List<List<Long>> partitionList = Lists.partition(request.getGoodIds(), 100);

        List<OrderDetailDTO> orderDetailDTOAllList = Lists.newArrayList();

        for (List<Long> goodIds : partitionList) {

            BatchQueryUserBuyNumberRequest request1 = new BatchQueryUserBuyNumberRequest();
            request1.setGoodIds(goodIds);
            request1.setStartTime(request.getStartTime());
            request1.setEndTime(request.getEndTime());
            request1.setBuyerEid(request.getBuyerEid());

            List<OrderDetailDTO> orderDetailDTOList =  orderDetailMapper.getBatchUserBuyNumber(request1);

            if (CollectionUtil.isNotEmpty(orderDetailDTOList)) {

                orderDetailDTOAllList.addAll(orderDetailDTOAllList);
            }
        }

        return orderDetailDTOAllList.stream().collect(Collectors.toMap(t -> t.getGoodsId(), z -> z.getGoodsQuantity().longValue()));
    }

    /**
     * 通过选择类型批量查询用户购买数量
     * @param buyNumberRequests 查询用户购买限制条件
     * @param selectRuleEnum 限制条件类型
     * @return
     */
    private Map<Long,Long> getUserBuyNumberBySelectRule (List<QueryUserBuyNumberRequest> buyNumberRequests,QueryUserBuyNumberRequest.SelectRuleEnum selectRuleEnum) {

        List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests = buyNumberRequests.stream().filter(t -> selectRuleEnum == t.getSelectRuleEnum()).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(queryUserBuyNumberRequests)) {

            return MapUtil.empty();
        }

        EnumSet<QueryUserBuyNumberRequest.SelectRuleEnum> canBatchSelectRule = EnumSet.of(QueryUserBuyNumberRequest.SelectRuleEnum.DAY, QueryUserBuyNumberRequest.SelectRuleEnum.WEEK, QueryUserBuyNumberRequest.SelectRuleEnum.MONTH);

        if (canBatchSelectRule.contains(selectRuleEnum)) {

            QueryUserBuyNumberRequest first = queryUserBuyNumberRequests.stream().findFirst().get();
            BatchQueryUserBuyNumberRequest request = new BatchQueryUserBuyNumberRequest();
            request.setBuyerEid(first.getBuyerEid());
            request.setEndTime(first.getEndTime());
            request.setStartTime(first.getStartTime());
            request.setGoodIds(queryUserBuyNumberRequests.stream().map(t -> t.getGoodId()).collect(Collectors.toList()));

            return this.getBatchUserBuyNumber(request);
        }

        // 自定义时间范围单个查询
        return queryUserBuyNumberRequests.stream().collect(Collectors.toMap(t -> t.getGoodId(),z -> this.getUserBuyNumber(z) ));
    }



    @Override
    public Map<Long, Long> getUserBuyNumber(List<QueryUserBuyNumberRequest> queryUserBuyNumberRequests) {

        if (log.isDebugEnabled()) {

            log.debug("getUserBuyNumber 参数:{}",queryUserBuyNumberRequests);
        }

        if (CollectionUtil.isEmpty(queryUserBuyNumberRequests)) {

            return MapUtil.empty();
        }

        Assert.notEmpty(queryUserBuyNumberRequests, "参数queryUserBuyNumberRequests不能为空");

        Set<Long> goodsIds =  queryUserBuyNumberRequests.stream().map(t -> t.getGoodId()).collect(Collectors.toSet());
        if (goodsIds.size() != queryUserBuyNumberRequests.size()) {

            log.error("getUserBuyNumber:{}",queryUserBuyNumberRequests);

            throw new RuntimeException("查询数据重复");
        }

        // 获取自定义具体的时间
        queryUserBuyNumberRequests.forEach(t -> t.convert());

        // 如果单个商品无需多线程处理,直接走单个查询方法
        if (1 == queryUserBuyNumberRequests.size()) {

            QueryUserBuyNumberRequest request = queryUserBuyNumberRequests.stream().findFirst().get();

            return MapUtil.of(request.getGoodId(),this.getUserBuyNumber(request));
        }

        Set<QueryUserBuyNumberRequest.SelectRuleEnum> selectRuleEnums = queryUserBuyNumberRequests.stream().map(t -> t.getSelectRuleEnum()).collect(Collectors.toSet());

        // 如果只有一种查询类型,直接同步查询
        if (1 == selectRuleEnums.size()) {

            return getUserBuyNumberBySelectRule(queryUserBuyNumberRequests,queryUserBuyNumberRequests.stream().findFirst().get().getSelectRuleEnum());
        }

        Map<Long, Long> resultMap = Maps.newHashMap();

        // 查询自定义
        CompletableFuture<Map<Long, Long>> userBuyNumberByCustomize =
                CompletableFuture
                .supplyAsync(() -> this.getUserBuyNumberBySelectRule(queryUserBuyNumberRequests,QueryUserBuyNumberRequest.SelectRuleEnum.CUSTOMIZE), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> resultMap.putAll(result));
        // 查询日
        CompletableFuture<Map<Long, Long>> userBuyNumberByDay =
                CompletableFuture
                .supplyAsync(() -> this.getUserBuyNumberBySelectRule(queryUserBuyNumberRequests,QueryUserBuyNumberRequest.SelectRuleEnum.DAY), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> resultMap.putAll(result));
        // 查询月
        CompletableFuture<Map<Long, Long>> userBuyNumberByMonth =
                CompletableFuture
                .supplyAsync(() -> this.getUserBuyNumberBySelectRule(queryUserBuyNumberRequests,QueryUserBuyNumberRequest.SelectRuleEnum.MONTH), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> resultMap.putAll(result));
        // 查询周
        CompletableFuture<Map<Long, Long>> userBuyNumberByWeek =
                CompletableFuture
                .supplyAsync(() -> this.getUserBuyNumberBySelectRule(queryUserBuyNumberRequests,QueryUserBuyNumberRequest.SelectRuleEnum.WEEK), springAsyncConfig.getAsyncExecutor())
                .whenComplete((result, throwable) -> resultMap.putAll(result));

        CompletableFuture.allOf(userBuyNumberByDay, userBuyNumberByMonth,userBuyNumberByWeek,userBuyNumberByCustomize).join();

        return resultMap;
    }
}
