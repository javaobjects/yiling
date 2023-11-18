package com.yiling.order.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderReturnMapper;
import com.yiling.order.order.dto.AgreementOrderReturnDetailDTO;
import com.yiling.order.order.dto.OrderReturnDTO;
import com.yiling.order.order.dto.OrderReturnDetailDTO;
import com.yiling.order.order.dto.ReturnOrderNumberDTO;
import com.yiling.order.order.dto.request.OrderDeliveryRequest;
import com.yiling.order.order.dto.request.OrderDetailRequest;
import com.yiling.order.order.dto.request.OrderReturnApplyRequest;
import com.yiling.order.order.dto.request.OrderReturnCountRequest;
import com.yiling.order.order.dto.request.OrderReturnPageListRequest;
import com.yiling.order.order.dto.request.OrderReturnPageRequest;
import com.yiling.order.order.dto.request.OrderReturnPullErpPageRequest;
import com.yiling.order.order.dto.request.QueryOrderReturnPageRequest;
import com.yiling.order.order.dto.request.QueryOrderUseAgreementRequest;
import com.yiling.order.order.dto.request.ReturnNumberRequest;
import com.yiling.order.order.dto.request.UpdateErpOrderReturnRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderReturnDO;
import com.yiling.order.order.entity.OrderReturnDetailBatchDO;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.enums.OrderReturnStatusEnum;
import com.yiling.order.order.enums.OrderReturnTypeEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.service.OrderReturnService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * <p>
 * 退货单 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderReturnServiceImpl extends BaseServiceImpl<OrderReturnMapper, OrderReturnDO> implements OrderReturnService {

    private final OrderServiceImpl orderServiceImpl;

    private final OrderReturnDetailServiceImpl orderReturnDetailServiceImpl;

    private final OrderReturnDetailBatchServiceImpl orderReturnDetailBatchServiceImpl;

    /**
     * 退货单请求参数的处理
     *
     * @param request 退货单申请数据
     * @return 处理后的请求参数
     */
    @Override
    public OrderReturnApplyRequest operateOrderReturnRequest(OrderReturnApplyRequest request) {
        log.info("createOrderReturn operateOrderReturnRequest start request:[{}]", JSONUtil.toJsonStr(request));
        List<OrderDetailRequest> detailList = new ArrayList<>();
        List<OrderDetailRequest> orderDetailList = request.getOrderDetailList();
        for (OrderDetailRequest orderDetailRequest : orderDetailList) {
            List<OrderDeliveryRequest> orderDeliveryList = orderDetailRequest.getOrderDeliveryList();
            if (CollUtil.isEmpty(orderDeliveryList)) {
                continue;
            }
            List<OrderDeliveryRequest> deliveryRequestList = orderDeliveryList.stream().filter(e -> null != e.getReturnQuantity() && e.getReturnQuantity() > 0).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(deliveryRequestList)) {
                orderDetailRequest.setOrderDeliveryList(deliveryRequestList);
                detailList.add(orderDetailRequest);
            }
        }
        request.setOrderDetailList(detailList);
        log.info("createOrderReturn operateOrderReturnRequest end request:[{}]", JSONUtil.toJsonStr(request));
        return request;
    }

    @Override
    public List<OrderReturnDTO> listPassedByOrderId(Long orderId) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getOrderId, orderId).eq(OrderReturnDO::getReturnStatus, OrderReturnStatusEnum.ORDER_RETURN_PASS.getCode()).eq(OrderReturnDO::getDelFlag, 0);
        List<OrderReturnDO> list = this.list(wrapper);
        return PojoUtils.map(list, OrderReturnDTO.class);
    }

    @Override
    public int countByOrderNoAndType(String orderNo, Integer returnType) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getOrderNo, orderNo);
        if (null != returnType) {
            wrapper.lambda().eq(OrderReturnDO::getReturnType, returnType);
        }
        return this.count(wrapper);
    }

    @Override
    public int countByOrderIdAndStatus(Long orderId, Integer returnStatus) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getOrderId, orderId);
        if (null != returnStatus) {
            wrapper.lambda().eq(OrderReturnDO::getReturnStatus, returnStatus);
        }
        wrapper.lambda().eq(OrderReturnDO::getDelFlag, 0);
        return this.count(wrapper);
    }

    @Override
    public List<OrderReturnDO> orderReturnByOrderIdsAndStatus(List<Long> orderIds, Integer returnStatus) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderReturnDO::getOrderId, orderIds);
        if (null != returnStatus) {
            wrapper.lambda().eq(OrderReturnDO::getReturnStatus, returnStatus);
        }
        wrapper.lambda().eq(OrderReturnDO::getDelFlag, 0);

        return list(wrapper);
    }

    @Override
    public Page<OrderReturnDO> pageList(OrderReturnPageListRequest request) {
        log.info("orderReturn pageList request:[{}]", JSONUtil.toJsonStr(request));
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        if (null != request.getOrderTypeEnum()) {
            wrapper.lambda().eq(OrderReturnDO::getOrderReturnType, request.getOrderTypeEnum().getCode());
        }
        if (null != request.getReturnSourceEnum()) {
            wrapper.lambda().eq(OrderReturnDO::getReturnSource, request.getReturnSourceEnum().getCode());
        }
        if (StrUtil.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().eq(OrderReturnDO::getOrderNo, request.getOrderNo());
        }
        if (StrUtil.isNotBlank(request.getOrderReturnNo())) {
            wrapper.lambda().eq(OrderReturnDO::getReturnNo, request.getOrderReturnNo());
        }
        if (StringUtils.isNotEmpty(request.getSellerEname())) {
            wrapper.lambda().like(OrderReturnDO::getSellerEname, request.getSellerEname());
        }
        if (StringUtils.isNotBlank(request.getBuyerEname())) {
            wrapper.lambda().like(OrderReturnDO::getBuyerEname, request.getBuyerEname());
        }
        if (request.getStartTime() != null) {
            wrapper.lambda().ge(OrderReturnDO::getCreateTime, DateUtil.beginOfDay(request.getStartTime()));
        }
        if (request.getEndTime() != null) {
            wrapper.lambda().lt(OrderReturnDO::getCreateTime, DateUtil.endOfDay(request.getEndTime()));
        }
        if (request.getReturnType() != null && request.getReturnType() != 0) {
            wrapper.lambda().eq(OrderReturnDO::getReturnType, request.getReturnType());
        }
        if (request.getReturnStatus() != null && request.getReturnStatus() != 0) {
            wrapper.lambda().eq(OrderReturnDO::getReturnStatus, request.getReturnStatus());
        }
        if (CollectionUtil.isNotEmpty(request.getUserIdList())) {
            wrapper.lambda().in(OrderReturnDO::getContacterId, request.getUserIdList());
        }
        if (CollectionUtil.isNotEmpty(request.getSellerEidList())) {
            wrapper.lambda().in(OrderReturnDO::getSellerEid, request.getSellerEidList());
        }
        if (CollectionUtil.isNotEmpty(request.getBuyerEidList())) {
            wrapper.lambda().in(OrderReturnDO::getBuyerEid, request.getBuyerEidList());
        }
        if(request.getOrderId() != null && request.getOrderId() != 0 ){
            wrapper.lambda().eq(OrderReturnDO::getOrderId, request.getOrderId());
        }

        if(request.getDepartmentId() != null ){
            wrapper.lambda().eq(OrderReturnDO::getDepartmentId, request.getDepartmentId());
        }

        if (null != request.getSortType() && 1 == request.getSortType()) {
            wrapper.lambda().last("ORDER BY CASE return_status WHEN 1 THEN 1 WHEN 3 THEN 2 WHEN 2 THEN 3 END, create_time desc ");
        } else {
            wrapper.lambda().orderByDesc(OrderReturnDO::getCreateTime);
        }
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public List<OrderReturnDTO> getOrderReturnListByOrderId(Long orderId) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getOrderId, orderId);
        wrapper.lambda().eq(OrderReturnDO::getDelFlag, 0);
        List<OrderReturnDO> list = this.list(wrapper);
        return PojoUtils.map(list, OrderReturnDTO.class);
    }

    /**
     * 查询所有订单的退货单信息审核通过的状态
     *
     * @param request 查询条件
     * @return 退货明细信息
     */
    @Override
    public List<AgreementOrderReturnDetailDTO> getOrderReturnDetailByEidAndTime(QueryOrderUseAgreementRequest request) {
        return this.getBaseMapper().getOrderReturnDetailByEidAndTime(request);
    }

    /**
     * 查询条件拼接
     *
     * @param request 退货单数量查询条件
     * @return 退货单查询条件
     */
    private QueryWrapper<OrderReturnDO> getReturnNumberQueryWrapper(ReturnNumberRequest request) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        if (null != request.getOrderTypeEnum()) {
            wrapper.lambda().eq(OrderReturnDO::getOrderReturnType, request.getOrderTypeEnum().getCode());
        }
        if (null != request.getReturnSourceEnum()) {
            wrapper.lambda().eq(OrderReturnDO::getReturnSource, request.getReturnSourceEnum().getCode());
        }
        if (CollectionUtil.isNotEmpty(request.getBuyerIdList())) {
            wrapper.lambda().in(OrderReturnDO::getBuyerEid, request.getBuyerIdList());
        }
        if (CollectionUtil.isNotEmpty(request.getUserIdList())) {
            wrapper.lambda().in(OrderReturnDO::getContacterId, request.getUserIdList());
        }
        if (CollectionUtil.isNotEmpty(request.getSellerIdList())) {
            wrapper.lambda().in(OrderReturnDO::getSellerEid, request.getSellerIdList());
        }
        if(request.getDepartmentId() != null){
            wrapper.lambda().eq(OrderReturnDO::getDepartmentId, request.getDepartmentId());
        }
        return wrapper;
    }

    @Override
    public ReturnOrderNumberDTO getOrderNumber(ReturnNumberRequest request) {
        log.info("getOrderNumber, request:[{}]", request);
        ReturnOrderNumberDTO returnOrderNumberDTO = new ReturnOrderNumberDTO();

        QueryWrapper<OrderReturnDO> wrapper = getReturnNumberQueryWrapper(request);
        // 总共退货单数
        int totalCount = count(wrapper);
        returnOrderNumberDTO.setAllReturnOrderNum(totalCount);
        // 今日退货单数
        wrapper.lambda().ge(OrderReturnDO::getCreateTime, DateUtil.parse(DateUtil.today()));
        int count = count(wrapper);
        returnOrderNumberDTO.setTodayReturnOrderNum(count);
        // 昨日退货单数
        QueryWrapper<OrderReturnDO> wrapperYesterday = getReturnNumberQueryWrapper(request);
        wrapperYesterday.lambda().ge(OrderReturnDO::getCreateTime, DateUtil.offsetDay(DateUtil.parse(DateUtil.today()), -1)).lt(OrderReturnDO::getCreateTime, DateUtil.parse(DateUtil.today()));
        int yesterdayCount = count(wrapperYesterday);
        returnOrderNumberDTO.setYesterdayReturnOrderNum(yesterdayCount);
        return returnOrderNumberDTO;
    }

    @Override
    public Page<OrderReturnDTO> getERPPullOrderReturn(OrderReturnPullErpPageRequest request) {
        Page<OrderReturnDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        Page<OrderReturnDO> page = this.baseMapper.selectErpPullReturnOrder(objectPage, request);
        return PojoUtils.map(page, OrderReturnDTO.class);
    }

    @Override
    public Boolean updateERPOrderReturnByOrderId(List<UpdateErpOrderReturnRequest> request) {
        for (UpdateErpOrderReturnRequest orderRequest : request) {
            Assert.notNull(orderRequest.getId(), "保存或更新：订单Id不能为空！");
            Assert.notNull(orderRequest.getErpPushStatus(), "保存或更新：ERP推送状态不能为空！");
        }
        for (UpdateErpOrderReturnRequest updateErpOrderReturnRequest : request) {
            OrderReturnDO orderReturnDO = this.getById(updateErpOrderReturnRequest.getId());
            orderReturnDO.setErpPushStatus(updateErpOrderReturnRequest.getErpPushStatus());
            orderReturnDO.setErpPushTime(new Date());
            orderReturnDO.setErpPushRemark(updateErpOrderReturnRequest.getErpPushRemark());
            this.updateById(orderReturnDO);
        }
        return true;
    }

    @Override
    public Page<OrderReturnDTO> queryOrderReturnPage(QueryOrderReturnPageRequest request) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        if (null != request.getOrderId()) {
            wrapper.lambda().eq(OrderReturnDO::getOrderId, request.getOrderId());
        }
        Page<OrderReturnDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderReturnDTO> orderReturnPage = PojoUtils.map(page, OrderReturnDTO.class);
        if (CollectionUtil.isNotEmpty(orderReturnPage.getRecords())) {
            List<OrderReturnDTO> list = orderReturnPage.getRecords();
            list.forEach(e -> {
                OrderDO orderDO = orderServiceImpl.getById(e.getOrderId());
                e.setPaymentMethod(orderDO.getPaymentMethod());
                e.setPaymentStatus(orderDO.getPaymentStatus());
                List<OrderReturnDetailDO> orderReturnDetailList = orderReturnDetailServiceImpl.getOrderReturnDetailByReturnId(e.getId());
                if (CollectionUtil.isNotEmpty(orderReturnDetailList)) {
                    int total = 0;
                    for (OrderReturnDetailDO orderReturnDetailDO : orderReturnDetailList) {
                        total = total + orderReturnDetailDO.getReturnQuantity();
                    }
                    e.setReturnGoodsNum(total);
                    e.setReturnGoods(orderReturnDetailList.size() + 1);
                }
            });
        }
        return orderReturnPage;
    }

    /**
     * 查询目标订单所有某状态的某种退货单明细
     *
     * @param orderId 订单id
     * @param returnType 退货单类型
     * @param returnStatus 退货单状态
     * @return 退货单明细
     */
    @Override
    public List<OrderReturnDetailDTO> getOrderReturnDetailByOrderIdAndTypeAndStatus(Long orderId, Integer returnType, Integer returnStatus) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getOrderId, orderId);
        if (null != returnType) {
            wrapper.lambda().eq(OrderReturnDO::getReturnType, returnType);
        }
        if (null != returnStatus) {
            wrapper.lambda().eq(OrderReturnDO::getReturnStatus, returnStatus);
        }
        wrapper.lambda().eq(OrderReturnDO::getDelFlag, 0);
        List<OrderReturnDO> orderReturnDOList = this.list(wrapper);
        if (CollectionUtil.isEmpty(orderReturnDOList)) {
            return null;
        }
        List<Long> returnIdList = orderReturnDOList.stream().map(OrderReturnDO::getId).collect(Collectors.toList());
        List<OrderReturnDetailDO> list = orderReturnDetailServiceImpl.getOrderReturnDetailByReturnIds(returnIdList);
        return PojoUtils.map(list, OrderReturnDetailDTO.class);
    }

    /**
     * 根据退货单状态获取，卖家订单数量
     *
     * @param returnStatus 退货单状态
     * @param eidList 企业集合
     * @param type 1：以岭管理员 2：以岭本部非管理员 3：非以岭人员
     * @param userId 登录用户id
     * @return 退货单数量
     */
    @Override
    public Integer getSellerOrderReturnNumberByReturnStatus(Integer returnStatus, List<Long> eidList, Integer type, Long userId) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDO::getReturnStatus, returnStatus)
                .eq(OrderReturnDO :: getOrderReturnType, OrderTypeEnum.POP.getCode());
        if (2 == type) {
            wrapper.lambda().eq(OrderReturnDO::getContacterId, userId);
        } else {
            wrapper.lambda().in(OrderReturnDO::getSellerEid, eidList);
        }
        return count(wrapper);
    }

    @Override
    public boolean deleteReturnOrder(Long orderId) {

        List<OrderReturnDTO> returnOrderList = this.getOrderReturnListByOrderId(orderId);

        if (CollectionUtil.isEmpty(returnOrderList)) {

            return true;
        }

        // 退货单ID
        List<Long> deleteReturnIdList = returnOrderList.stream().map(OrderReturnDTO::getId).collect(Collectors.toList());

        QueryWrapper<OrderReturnDO> deleteReturnWrapper = new QueryWrapper<>();
        deleteReturnWrapper.lambda().in(OrderReturnDO::getId, deleteReturnIdList);
        OrderReturnDO orderReturnDo = new OrderReturnDO();
        orderReturnDo.setOpUserId(0L);

        this.batchDeleteWithFill(orderReturnDo, deleteReturnWrapper);

        QueryWrapper<OrderReturnDetailDO> deleteDetailWrapper = new QueryWrapper<>();
        deleteDetailWrapper.lambda().in(OrderReturnDetailDO::getReturnId, deleteReturnIdList);
        OrderReturnDetailDO orderReturnDetailDo = new OrderReturnDetailDO();
        orderReturnDetailDo.setOpUserId(0L);

        orderReturnDetailServiceImpl.batchDeleteWithFill(orderReturnDetailDo, deleteDetailWrapper);

        QueryWrapper<OrderReturnDetailBatchDO> deleteBatchWrapper = new QueryWrapper<>();
        deleteBatchWrapper.lambda().in(OrderReturnDetailBatchDO::getReturnId, deleteReturnIdList);

        OrderReturnDetailBatchDO orderReturnDetailBatch = new OrderReturnDetailBatchDO();
        orderReturnDetailBatch.setOpUserId(0L);

        orderReturnDetailBatchServiceImpl.batchDeleteWithFill(orderReturnDetailBatch, deleteBatchWrapper);

        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderId);
        orderDO.setReturnAmount(BigDecimal.ZERO);
        orderDO.setReturnCashDicountAmount(BigDecimal.ZERO);
        orderDO.setReturnTicketDiscountAmount(BigDecimal.ZERO);

        // 清空退回的金额
        orderServiceImpl.updateById(orderDO);

        return true;
    }

    @Override
    public Map<Long, List<OrderReturnDetailDO>> queryByOrderIdList(List<Long> orderIdList) {
        QueryWrapper<OrderReturnDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(OrderReturnDO::getOrderId, orderIdList);
        queryWrapper.lambda().ne(OrderReturnDO::getReturnStatus, OrderReturnStatusEnum.ORDER_RETURN_REJECT.getCode());
        List<OrderReturnDO> orderReturnDOList = this.list(queryWrapper);
        if (CollectionUtil.isEmpty(orderReturnDOList)) {
            return null;
        }
        List<Long> returnIdList = orderReturnDOList.stream().map(OrderReturnDO::getId).collect(Collectors.toList());
        List<OrderReturnDetailDO> list = orderReturnDetailServiceImpl.getOrderReturnDetailByReturnIds(returnIdList);

        Map<Long, List<OrderReturnDetailDO>> mapReturn = new HashMap<>();
        if (CollectionUtil.isNotEmpty(list)) {
            for (OrderReturnDetailDO one : list) {
                if (mapReturn.containsKey(one.getReturnId())) {
                    List<OrderReturnDetailDO> orderReturnDetailDOS = mapReturn.get(one.getReturnId());
                    orderReturnDetailDOS.add(one);
                } else {
                    mapReturn.put(one.getReturnId(), new ArrayList<OrderReturnDetailDO>() {{
                        add(one);
                    }});
                }
            }
        }
        Map<Long, List<OrderReturnDetailDO>> map = new HashMap<>();
        for (OrderReturnDO one : orderReturnDOList) {
            List<OrderReturnDetailDO> orderReturnDetailDOS = mapReturn.get(one.getId());
            if (CollectionUtil.isNotEmpty(orderReturnDetailDOS)) {
                if (map.containsKey(one.getOrderId())) {

                    List<OrderReturnDetailDO> orderReturnDetailDOSOne = map.get(one.getOrderId());
                    orderReturnDetailDOSOne.addAll(orderReturnDetailDOS);

                } else {
                    map.put(one.getOrderId(), orderReturnDetailDOS);
                }
            }

        }
        return map;
    }

    @Override
    public Map<Long, Integer> countByOrderIdList(List<Long> orderIdList) {
        // SELECT order_id,count(1) AS count  FROM order_return WHERE return_status = 1  GROUP BY order_id ;
        Map<Long, Integer> returnMap = new HashMap<>();
        int count;
        for (Long orderId : orderIdList) {
            QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(OrderReturnDO::getOrderId, orderId).eq(OrderReturnDO::getReturnStatus, OrderReturnStatusEnum.ORDER_RETURN_PASS.getCode()).eq(OrderReturnDO::getDelFlag, 0);
            count = this.count(wrapper);
            returnMap.put(orderId, count);
        }
        return returnMap;
    }

    @Override
    public Page<OrderReturnDTO> pageByCondition(OrderReturnPageRequest request) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        if (request.getReturnStatus() != null && request.getReturnStatus() != 0) {
            wrapper.lambda().eq(OrderReturnDO::getReturnStatus, request.getReturnStatus());
        }
        if (null != request.getCurrentUserId()) {
            wrapper.lambda().eq(OrderReturnDO::getCreateUser, request.getCurrentUserId());
        }
        if (null != request.getCurrentEid()) {
            wrapper.lambda().eq(OrderReturnDO::getBuyerEid, request.getCurrentEid());
        }
        if (StringUtils.isNotEmpty(request.getCondition())) {
            // 退货单号和供应商名称  退货号-精确查询，供应商名称-模糊查询
            wrapper.lambda().and(Wrapper -> Wrapper.like(OrderReturnDO::getReturnNo, request.getCondition()).or().like(OrderReturnDO::getSellerEname, request.getCondition()));
        }
        wrapper.lambda().eq(OrderReturnDO::getOrderReturnType, request.getOrderReturnType()).orderByDesc(OrderReturnDO::getCreateTime);
        Page<OrderReturnDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return PojoUtils.map(page, OrderReturnDTO.class);
    }

    @Override
    public int countByCondition(OrderReturnCountRequest request) {
        QueryWrapper<OrderReturnDO> wrapper = new QueryWrapper<>();
        if (null != request.getBuyerEid()) {
            wrapper.lambda().eq(OrderReturnDO::getBuyerEid, request.getBuyerEid());
        }
        if (null != request.getSellerEid()) {
            wrapper.lambda().eq(OrderReturnDO::getSellerEid, request.getSellerEid());
        }
        if (null != request.getReturnStatus() && 0 != request.getReturnStatus()) {
            wrapper.lambda().eq(OrderReturnDO::getReturnStatus, request.getReturnStatus());
        }
        if (null != request.getReturnSource() && 0 != request.getReturnSource()) {
            wrapper.lambda().eq(OrderReturnDO::getReturnSource, request.getReturnSource());
        }
        return this.count(wrapper);
    }
}
