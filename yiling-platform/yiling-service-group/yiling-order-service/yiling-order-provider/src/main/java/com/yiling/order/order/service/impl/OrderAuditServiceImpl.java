package com.yiling.order.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.dao.OrderMapper;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderManageStatusNumberDTO;
import com.yiling.order.order.dto.request.QueryOrderExpectPageRequest;
import com.yiling.order.order.dto.request.QueryOrderManagePageRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.entity.OrderLogDO;
import com.yiling.order.order.entity.OrderStatusLogDO;
import com.yiling.order.order.enums.CustomerConfirmEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.service.OrderAuditService;
import com.yiling.order.order.service.OrderLogService;
import com.yiling.order.order.service.OrderService;
import com.yiling.order.order.service.OrderStatusLogService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

/**
 * @author:wei.wang
 * @date:2021/7/21
 */
@Service
public class OrderAuditServiceImpl extends BaseServiceImpl<OrderMapper, OrderDO> implements OrderAuditService {
    @DubboReference
    protected MqMessageSendApi      mqMessageSendApi;
    @Autowired
    private OrderStatusLogService   orderStatusLogService;
    @Autowired
    private OrderLogService         orderLogService;
    @Autowired
    private OrderService            orderService;
    @Autowired
    private SpringAsyncConfig       springAsyncConfig;
    @Lazy
    @Autowired
    private OrderAuditServiceImpl   _this;

    /**
     * 获取预期订单列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderExpectInfo(QueryOrderExpectPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getDistributorEname())) {
            wrapper.lambda().like(OrderDO::getSellerEname, request.getDistributorEname());
        }
        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }
        if (request.getAuditStatus() != null && request.getAuditStatus() != 0) {
            wrapper.lambda().eq(OrderDO::getAuditStatus, request.getAuditStatus());
        }
        if (request.getStartCreatTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreatTime()));
        }

        if (request.getEndCreatTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreatTime()));
        }
        if(request.getOrderType() != null && request.getOrderType() != 0){
            wrapper.lambda().eq(OrderDO :: getOrderType,request.getOrderType());
        }
        if(CollectionUtil.isNotEmpty(request.getContacterIdList())){
            wrapper.lambda().in(OrderDO :: getContacterId,request.getContacterIdList());
        }
        if(request.getBuyerEid()!= null && request.getBuyerEid() !=0 ){
            wrapper.lambda().eq(OrderDO::getBuyerEid,request.getBuyerEid());
        }
        if(CollectionUtil.isNotEmpty(request.getSellerEidList())){
            wrapper.lambda().in(OrderDO::getSellerEid,request.getSellerEidList());
        }
        if(request.getId() != null && request.getId() != 0){
            wrapper.lambda().eq(OrderDO::getId,request.getId());
        }
        wrapper.lambda().ne(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode())
                .orderByDesc(OrderDO :: getCreateTime);
        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> dtoPage = PojoUtils.map(page, OrderDTO.class);
        return dtoPage;
    }

    /**
     * 预订单取消
     *
     * @param orderId
     * @return
     */
    @Override
    public Boolean cancelOrderExpect(Long orderId,Long opUserId) {
        OrderDO order = getById(orderId);
        if (order == null) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS);
        }

        if (!(OrderAuditStatusEnum.UNSUBMIT.getCode().equals(order.getAuditStatus()) ||
                OrderAuditStatusEnum.UNAUDIT.getCode().equals(order.getAuditStatus()))) {
            throw new BusinessException(OrderErrorCode.ORDER_AUDIT_STATUS_INCORRECT);
        }
        if(OrderStatusEnum.UNAUDITED != OrderStatusEnum.getByCode(order.getOrderStatus())){
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_ERROR);
        }
        OrderDO one = new OrderDO();
        one.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        one.setOpUserId(opUserId);
        one.setOpTime(new Date());
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDO :: getId,order.getId())
                .eq(OrderDO :: getAuditStatus,order.getAuditStatus())
                .eq(OrderDO :: getOrderStatus,OrderStatusEnum.UNAUDITED.getCode());

        OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
        orderStatusLog.setOrderId(order.getId())
                .setOrderStatus(OrderStatusEnum.CANCELED.getCode())
                .setRemark("订单取消")
                .setCreateUser(opUserId)
                .setCreateTime(DateUtil.date());
        orderStatusLogService.save(orderStatusLog);

        OrderLogDO orderLogInfo = new OrderLogDO();
        orderLogInfo.setLogContent("订单取消")
                .setLogTime(new Date())
                .setOrderId(orderId);
        orderLogService.save(orderLogInfo);

        return update(one,wrapper);
    }

    /**
     * 获取除取消订单外的所有订单数量
     * @return
     */
    @Override
    public OrderManageStatusNumberDTO getOrderReviewStatusNumber(List<Long> sellerEidList,Long departmentId,Integer departmentType) {

        //待审核数据
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        {
            wrapper.lambda().ne(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode()).eq(OrderDO::getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode()).eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode());
            wrapper.lambda().in(OrderDO::getSellerEid, sellerEidList);
            wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.UNAUDIT.getCode());

            if (departmentType == 2) {

                if (departmentId != null) {
                    wrapper.lambda().ne(OrderDO::getDepartmentId, departmentId);
                }
            } else {
                if (departmentId != null) {
                    wrapper.lambda().eq(OrderDO::getDepartmentId, departmentId);
                }
            }
        }

        //审核驳回数据
        QueryWrapper<OrderDO> rejectWrapper = new QueryWrapper<>();
        {
            rejectWrapper.lambda().ne(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode()).eq(OrderDO::getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode()).eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode());
            rejectWrapper.lambda().eq(OrderDO::getAuditStatus, (OrderAuditStatusEnum.AUDIT_REJECT.getCode()));

            rejectWrapper.lambda().in(OrderDO::getSellerEid, sellerEidList);

            if (departmentType == 2) {
                if (departmentId != null) {
                    rejectWrapper.lambda().ne(OrderDO::getDepartmentId, departmentId);
                }
            } else {
                if (departmentId != null) {
                    rejectWrapper.lambda().eq(OrderDO::getDepartmentId, departmentId);
                }
            }
        }

        //审核通过数据
        QueryWrapper<OrderDO> reviewWrapper = new QueryWrapper<>();
        {
            reviewWrapper.lambda().ne(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode()).eq(OrderDO::getCustomerConfirmStatus, CustomerConfirmEnum.CONFIRMED.getCode()).eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode());
            reviewWrapper.lambda().eq(OrderDO::getAuditStatus, (OrderAuditStatusEnum.AUDIT_PASS.getCode()));

            reviewWrapper.lambda().in(OrderDO::getSellerEid, sellerEidList);

            if (departmentType == 2) {
                if (departmentId != null) {
                    reviewWrapper.lambda().ne(OrderDO::getDepartmentId, departmentId);
                }
            } else {
                if (departmentId != null) {
                    reviewWrapper.lambda().eq(OrderDO::getDepartmentId, departmentId);
                }
            }
        }

        OrderManageStatusNumberDTO result = new OrderManageStatusNumberDTO();
        //待审核数据
        CompletableFuture<Integer> reviewingNumber = CompletableFuture.supplyAsync(() -> orderService.count(wrapper), springAsyncConfig.getAsyncExecutor()).whenComplete((t, throwable) -> result.setReviewingNumber(t));
        //审核驳回数据
        CompletableFuture<Integer> rejectNumber = CompletableFuture.supplyAsync(() -> orderService.count(rejectWrapper), springAsyncConfig.getAsyncExecutor()).whenComplete((t, throwable) -> result.setRejectNumber(t));
        //审核通过数据
        CompletableFuture<Integer> reviewNumber = CompletableFuture.supplyAsync(() -> orderService.count(reviewWrapper), springAsyncConfig.getAsyncExecutor()).whenComplete((t, throwable) -> result.setReviewNumber(t));

        CompletableFuture.allOf(reviewingNumber, rejectNumber, reviewNumber).join();

        return result;
    }

    /**
     * 订单审核列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDTO> getOrderManagePage(QueryOrderManagePageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();

        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(OrderDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }

        if (StringUtils.isNotBlank(request.getOrderNo())) {
            wrapper.lambda().like(OrderDO::getOrderNo, request.getOrderNo());
        }

        if (StringUtils.isNotBlank(request.getBuyerEname())) {
            wrapper.lambda().like(OrderDO::getBuyerEname, request.getBuyerEname());
        }

        if (request.getPaymentMethod() != null && request.getPaymentMethod() != 0) {
            wrapper.lambda().eq(OrderDO::getPaymentMethod, request.getPaymentMethod());
        }

        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(OrderDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(request.getAuditStatus() != null && request.getAuditStatus()!= 0){
            wrapper.lambda().eq(OrderDO::getAuditStatus, request.getAuditStatus());
        }else{
            wrapper.lambda().ge(OrderDO::getAuditStatus, OrderAuditStatusEnum.UNAUDIT.getCode());
        }


       if(StringUtils.isNotBlank(request.getProvinceCode())){
           wrapper.lambda().eq(OrderDO::getBuyerProvinceCode, request.getProvinceCode());
       }

        if(StringUtils.isNotBlank(request.getCityCode())){
            wrapper.lambda().eq(OrderDO::getBuyerCityCode, request.getCityCode());
        }

        if(StringUtils.isNotBlank(request.getRegionCode())){
            wrapper.lambda().eq(OrderDO::getBuyerRegionCode, request.getRegionCode());
        }

        if(request.getOrderType() != null && request.getOrderType() !=0){
            wrapper.lambda().eq(OrderDO :: getOrderType,request.getOrderType());
        }

        if (request.getDepartmentType() == 2){

            if(request.getDepartmentId() != null  ){
                wrapper.lambda().ne(OrderDO :: getDepartmentId,request.getDepartmentId());
            }

        } else {
            if(request.getDepartmentId() != null ){
                wrapper.lambda().eq(OrderDO :: getDepartmentId,request.getDepartmentId());
            }else{
                return new Page<OrderDTO>();
            }
        }

        if(request.getDepartmentIdCode() != null && request.getDepartmentIdCode() != 0){
            wrapper.lambda().eq(OrderDO :: getDepartmentId,request.getDepartmentIdCode());
        }

        if(StringUtils.isNotBlank(request.getContacterName())){
            wrapper.lambda().like(OrderDO :: getContacterName,request.getContacterName());
        }

        if (CollectionUtil.isNotEmpty(request.getSellerEidList())) {
            wrapper.lambda().in(OrderDO :: getSellerEid,request.getSellerEidList());
        }

        if(request.getId() != null && request.getId() !=0){
            wrapper.lambda().eq(OrderDO :: getId,request.getId());
        }

        wrapper.lambda().ne(OrderDO :: getOrderStatus,OrderStatusEnum.CANCELED.getCode())
                .eq(OrderDO :: getCustomerConfirmStatus,CustomerConfirmEnum.CONFIRMED.getCode())
                .orderByDesc(OrderDO :: getCreateTime);
        Page<OrderDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<OrderDTO> orderPage = PojoUtils.map(page, OrderDTO.class);
        return orderPage;
    }


    @Override
    public boolean updateAuditStatus(Long id, OrderAuditStatusEnum originalStatus, OrderAuditStatusEnum newStatus, Long opUserId, String remark) {
        MqMessageBO mqMessageBO = _this.updateAuditStatusSave(id, originalStatus, newStatus, opUserId, remark);
        if(mqMessageBO != null){
            mqMessageSendApi.send(mqMessageBO);
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public MqMessageBO updateAuditStatusSave(Long id, OrderAuditStatusEnum originalStatus, OrderAuditStatusEnum newStatus, Long opUserId, String remark) {
        OrderDO orderDTO = this.getById(id);

        if (originalStatus != null && OrderAuditStatusEnum.getByCode(orderDTO.getAuditStatus()) != originalStatus) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }

        OrderDO entity = new OrderDO();
        entity.setAuditStatus(newStatus.getCode());
        entity.setOpUserId(opUserId);
        if(newStatus == OrderAuditStatusEnum.AUDIT_PASS || newStatus == OrderAuditStatusEnum.AUDIT_REJECT){
            entity.setAuditRejectReason(remark);
            entity.setAuditUser(opUserId);
            entity.setAuditTime(new Date());
        }


        QueryWrapper<OrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderDO::getId, id)
                .eq(OrderDO::getAuditStatus, originalStatus.getCode())
                .eq(OrderDO::getOrderStatus,OrderStatusEnum.UNAUDITED.getCode())
                .last("limit 1");

        boolean result = this.update(entity, queryWrapper);
        if (!result) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }
        MqMessageBO mqMessageBO = null;
        // 发送订单已审核消息
        if (newStatus == OrderAuditStatusEnum.AUDIT_PASS || newStatus == OrderAuditStatusEnum.AUDIT_REJECT) {
            mqMessageBO = new MqMessageBO(Constants.TOPIC_ORDER_AUDITED, "", orderDTO.getOrderNo());
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        }

        OrderStatusLogDO orderStatusLog = new OrderStatusLogDO();
        orderStatusLog.setOrderId(orderDTO.getId())
                .setOrderStatus(orderDTO.getOrderStatus())
                .setRemark("审核状态"+newStatus.getName());
        orderStatusLogService.save(orderStatusLog);

        return mqMessageBO;
    }

}
