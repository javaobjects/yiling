package com.yiling.open.erp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.open.erp.dto.ErpClientDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpGoodsBatchMapper;
import com.yiling.open.erp.dao.ErpGoodsMapper;
import com.yiling.open.erp.dao.ErpOrderSendMapper;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpGoodsBatchDO;
import com.yiling.open.erp.entity.ErpOrderSendDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.enums.SendTypeEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.handler.ErpOrderSendHandler;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpOrderSendService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.request.DeliveryInfoRequest;
import com.yiling.order.order.dto.request.ModifyOrderNotAuditRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryInfoRequest;
import com.yiling.order.order.dto.request.SaveOrderDeliveryListInfoRequest;
import com.yiling.order.order.dto.request.UpdateOrderNotAuditRequest;
import com.yiling.order.order.enums.OrderStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Service(value = "erpOrderSendService")
public class ErpOrderSendServiceImpl extends ErpEntityServiceImpl implements ErpOrderSendService {

    @Autowired
    private ErpOrderSendMapper erpOrderSendMapper;

    @Autowired
    private ErpClientService erpClientService;

    @Autowired
    private RocketMqProducerService rocketMqProducerService;

    @Autowired
    private ErpGoodsMapper erpGoodsMapper;

    @DubboReference
    private OrderDetailApi orderDetailApi;

    @Autowired
    private ErpGoodsBatchMapper erpGoodsBatchMapper;

    @DubboReference
    private OrderApi orderApi;

    @Autowired
    private ErpOrderSendHandler erpOrderSendHandler;

    @DubboReference
    private OrderProcessApi orderProcessApi;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpOrderSendDO erpOrderSend = (ErpOrderSendDO) baseErpEntity;

        //1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(erpOrderSend.getSuId(), erpOrderSend.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        return synErpOrderSendPartSync(erpOrderSend, erpClient);
    }

    @Override
    public void syncOrderSend() {
        List<ErpOrderSendDO> orderSendList = erpOrderSendMapper.syncOrderSend();
        for (ErpOrderSendDO erpOrderSend : orderSendList) {
            int i = erpOrderSendMapper.updateSyncStatusByStatusAndId(erpOrderSend.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                erpOrderSendHandler.onlineData(erpOrderSend);
            }
        }
    }

    public boolean synErpOrderSendPartSync(ErpOrderSendDO erpOrderSend, ErpClientDTO erpClient) {
        try {
            //判断op库的数据是否存在，存在直接返回
            if (erpOrderSend.getOperType() == 3) {
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "发货单只支持新增数据");
                return false;
            }

            //判断op库的数据是反审核并且是添加状态
            if (erpOrderSend.getOperType() == 1 && erpOrderSend.getSendType() == 3) {
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.SUCCESS.getCode(), "发货单反审核抵消");
                return false;
            }

            //判断是否处理成功
            ErpOrderSendDO newErpOrderSend = erpOrderSendMapper.findById(erpOrderSend.getId());
            if (newErpOrderSend != null && newErpOrderSend.getSyncStatus().equals(SyncStatus.SUCCESS.getCode())) {
                return false;
            }

            // 查找OrderDTO信息
            OrderDTO orderDTO = getOrder(erpOrderSend);

            Integer orderStatus = null;
            if (orderDTO != null) {
                orderStatus = orderDTO.getOrderStatus();
            }

            // 订单数据不存在
            if (orderDTO == null || orderStatus == null) {
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单数据不存在");
                return false;
            }

            List<ErpClientDTO> erpClientDOList = erpClientService.selectBySuId(erpClient.getSuId());
            List<Long> rkSuIdList = erpClientDOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList());
            if (!rkSuIdList.contains(orderDTO.getSellerEid())) {
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "自己的订单数据不存在");
                return false;
            }

            // 查找MphOrderDetail信息
            Long oid = orderDTO.getId();
            // 获取订单明细列表
            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailInfo(oid);
            if (CollUtil.isEmpty(orderDetailList)) {
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "MPH订单明细不存在");
                return false;
            }

            //需要重新同步商品库存信息
            List<String> insnList = new ArrayList<>();

            Boolean serviceResult = null;
            //判断发货数据类型并且是发货数量等于0
            if (SendTypeEnum.getByCode(erpOrderSend.getSendType()).equals(SendTypeEnum.CLOSE) && erpOrderSend.getSendNum().compareTo(BigDecimal.ZERO) == 0) {
                if (orderStatus < 20 || orderStatus >= 30) {
                    erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单状态不正确");
                    return false;
                }
                //关闭订单
                log.info("包裹调用取消订单接口请求:{}", orderDTO.getId());
                serviceResult = orderProcessApi.closeDelivery(0L, orderDTO.getId());
                for (OrderDetailDTO orderDetailDTO : orderDetailList) {
                    insnList.add(orderDetailDTO.getGoodsErpCode());
                }
            } else if (SendTypeEnum.getByCode(erpOrderSend.getSendType()).equals(SendTypeEnum.DELETE)) {
                if(!checkOrderDetailId(erpOrderSend.getOrderDetailId())){
                    erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "出库单明细ID（orderDetailId）不存在");
                    return false;
                }
                if (orderStatus < 20 || orderStatus >= 40) {
                    erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单状态不正确");
                    return false;
                }
                //反审核
                ModifyOrderNotAuditRequest request = new ModifyOrderNotAuditRequest();
                request.setOrderId(orderDTO.getId());
                request.setErpDeliveryNo(erpOrderSend.getDeliveryNumber());
                request.setBatchNo(erpOrderSend.getSendBatchNo());
                request.setOpUserId(0L);
                log.info("包裹调用发货单反审核接口请求:{}", JSON.toJSONString(request));
                serviceResult = orderProcessApi.modifyOrderNotAudit_v2(request);
                OrderDetailDTO orderDetail = orderDetailApi.getOrderDetailById(erpOrderSend.getOrderDetailId());
                insnList.add(orderDetail.getGoodsErpCode());
            } else if (SendTypeEnum.getByCode(erpOrderSend.getSendType()).equals(SendTypeEnum.NORMAL) || (SendTypeEnum.getByCode(erpOrderSend.getSendType()).equals(SendTypeEnum.CLOSE) && erpOrderSend.getOperType() == 1)) {
                if(!checkOrderDetailId(erpOrderSend.getOrderDetailId())){
                    erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "出库单明细ID（orderDetailId）不存在");
                    return false;
                }
                if (orderStatus < 20 || orderStatus > 30) {
                    erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单状态不正确");
                    return false;
                }
                //正常部分发货
                SaveOrderDeliveryListInfoRequest orderSendRequest = new SaveOrderDeliveryListInfoRequest();
                Map<Long, OrderDetailDTO> orderDetailDTOMap = orderDetailList.stream().collect(Collectors.toMap(OrderDetailDTO::getId, Function.identity()));
                OrderDetailDTO orderDetail = orderDetailDTOMap.get(erpOrderSend.getOrderDetailId());
                insnList.add(orderDetail.getGoodsErpCode());
                //发货单明细
                List<SaveOrderDeliveryInfoRequest> orderSendInfoList = new ArrayList<>();
                //发货批次明细
                List<DeliveryInfoRequest> deliveryInfoList = new ArrayList<>();
                DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest();
                deliveryInfoRequest.setErpDeliveryNo(erpOrderSend.getDeliveryNumber());
                deliveryInfoRequest.setDeliveryQuantity(erpOrderSend.getSendNum().intValue());
                deliveryInfoRequest.setExpiryDate(erpOrderSend.getEffectiveTime());
                deliveryInfoRequest.setProduceDate(erpOrderSend.getProductTime());
                deliveryInfoRequest.setBatchNo(erpOrderSend.getSendBatchNo());
                deliveryInfoRequest.setEasSendOrderId(erpOrderSend.getEasSendOrderId());
                deliveryInfoList.add(deliveryInfoRequest);

                SaveOrderDeliveryInfoRequest saveOrderDeliveryInfoRequest = new SaveOrderDeliveryInfoRequest();
                saveOrderDeliveryInfoRequest.setGoodsId(orderDetail.getGoodsId());
                saveOrderDeliveryInfoRequest.setDetailId(orderDetail.getId());
                saveOrderDeliveryInfoRequest.setDeliveryInfoList(deliveryInfoList);
                orderSendInfoList.add(saveOrderDeliveryInfoRequest);

                orderSendRequest.setOrderId(orderDTO.getId());
                orderSendRequest.setOrderDeliveryGoodsInfoList(orderSendInfoList);
                orderSendRequest.setOpUserId(0L);
                log.info("包裹调用发货单接口请求:{}", JSON.toJSONString(orderSendRequest));
                serviceResult = orderProcessApi.delivery(orderSendRequest);
                //erp对接--支持商家erp一次性发货需求
                if (SendTypeEnum.getByCode(erpOrderSend.getSendType()).equals(SendTypeEnum.CLOSE) && erpOrderSend.getSendNum().compareTo(BigDecimal.ZERO) > 0 && erpOrderSend.getOperType().equals(OperTypeEnum.ADD.getCode())) {
                    // 查找OrderDTO信息
                    orderDTO = getOrder(erpOrderSend);
                    if (orderDTO.getOrderStatus().equals(OrderStatusEnum.UNDELIVERED.getCode()) || orderDTO.getOrderStatus().equals(OrderStatusEnum.PARTDELIVERED.getCode())) {
                        //判断本次请求是否为最后一次处理
                        List<ErpOrderSendDO> osList = erpOrderSendMapper.findSendOrderByOdId(erpOrderSend.getSuId(), erpOrderSend.getSuDeptNo(), new ArrayList<>(orderDetailDTOMap.keySet()), null);
                        osList = osList.stream().filter(e -> !e.getSyncStatus().equals(SyncStatus.SUCCESS.getCode())).collect(Collectors.toList());
                        if (osList.size() == 1) {
                            //关闭订单
                            log.info("一次性包裹调用取消订单接口请求:{}", orderDTO.getId());
                            OrderDTO  order = orderApi.getOrderInfo(erpOrderSend.getOrderId());
                            if (order==null||order.getOrderStatus() < 20 || order.getOrderStatus() >= 30) {
                                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单状态不正确");
                                return false;
                            }
                            serviceResult = orderProcessApi.closeDelivery(0L, orderDTO.getId());
                        }
                    }
                }
            }

            if (serviceResult == null) {
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.SUCCESS.getCode(), "同步成功,空跑数据");
                return false;
            }

            if (!serviceResult) {
                log.error("包裹调用发货接口报错！ 退出{}", false);
                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "接口返回错误");
                return false;
            }
            erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.SUCCESS.getCode(), "同步成功");
            refreshErpInventoryList(insnList, erpClient.getRkSuId());
        } catch (BusinessException be) {
            erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), StrUtil.subPre(be.getMessage(),100));
        } catch (Exception e) {
            erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(),  StrUtil.subPre(e.getMessage(),100));
            log.error("发货单同步出现错误date={}", JSON.toJSONString(erpOrderSend), e);
        }
        return true;
    }

    public boolean checkOrderDetailId(Long orderDetailId){
        // 出库单明细ID不能为空
        if(ObjectUtil.isNull(orderDetailId) || 0 == orderDetailId.intValue()){
            return false;
        }
        return true;
    }

//    public boolean synErpOrderSendSync(ErpOrderSendDO erpOrderSend, ErpClientDO erpClient) {
//        try {
//            //判断op库的数据是否存在，存在直接返回
//            if (erpOrderSend.getOperType() == 3) {
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "发货单只支持新增数据");
//                return false;
//            }
//
//            //判断是否处理成功
//            ErpOrderSendDO newErpOrderSend = erpOrderSendMapper.findById(erpOrderSend.getId());
//            if (newErpOrderSend != null && newErpOrderSend.getSyncStatus().equals(SyncStatus.SUCCESS.getCode())) {
//                return false;
//            }
//
//            // 查找OrderDTO信息
//            OrderDTO orderDTO = getOrder(erpOrderSend);
//
//            Integer orderStatus = null;
//            if (orderDTO != null) {
//                orderStatus = orderDTO.getOrderStatus();
//            }
//
//            // 订单数据不存在
//            if (orderDTO == null || orderStatus == null) {
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单数据不存在");
//                return false;
//            }
//
//            List<ErpClientDO> erpClientDOList = erpClientService.selectBySuId(erpClient.getSuId());
//            List<Long> rkSuIdList = erpClientDOList.stream().map(e -> e.getRkSuId()).collect(Collectors.toList());
//            if (!rkSuIdList.contains(orderDTO.getSellerEid())) {
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "自己的订单数据不存在");
//                return false;
//            }
//
//            if (orderStatus < 20 || orderStatus >= 100) {
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单状态不正确");
//                return false;
//            }
//
//            // 查找MphOrderDetail信息
//            Long oid = orderDTO.getId();
//            // 获取订单明细列表
//            List<OrderDetailDTO> orderDetailList = orderDetailApi.getOrderDetailInfo(oid);
//            if (CollUtil.isEmpty(orderDetailList)) {
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "MPH订单明细不存在");
//                return false;
//            }
//
//            // 订单明细里面的od_id
//            Map<Long, OrderDetailDTO> modIdMap = new HashMap<>();
//            for (OrderDetailDTO orderDetail : orderDetailList) {
//                Long odId = orderDetail.getId();
//                modIdMap.put(odId, orderDetail);
//            }
//            // 获取ErpOrderSend相关的发货单信息
//            List<ErpOrderSendDO> osList = erpOrderSendMapper.findSendOrderByOdId(erpOrderSend.getSuId(), erpOrderSend.getSuDeptNo(), new ArrayList<Long>(modIdMap.keySet()), null);
//
//            Map<Long, List<ErpOrderSendDO>> eodIdMap = new HashMap<>();
//            for (ErpOrderSendDO tmpOrderSend : osList) {
//                // 发货单里面必须包含odId
//                Long odId = tmpOrderSend.getOrderDetailId();
//                if (odId != null) {
//                    List<ErpOrderSendDO> tmpOrderSends = null;
//                    if (eodIdMap.containsKey(odId)) {
//                        tmpOrderSends = eodIdMap.get(odId);
//                    } else {
//                        tmpOrderSends = new ArrayList<>();
//                    }
//                    tmpOrderSends.add(tmpOrderSend);
//                    eodIdMap.put(odId, tmpOrderSends);
//                }
//            }
//
//            // 判断发货数量是否小数，然后是否整数
//            if (!isOrderSendIntegerNumber(eodIdMap)) {
//                log.warn("发货数量是小数，发货单{}", JSON.toJSONString(erpOrderSend));
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "发货数量是小数");
//                return false;
//            }
//
//            if (!isOrderDetailEqualOrderSend(modIdMap, eodIdMap, erpClient)) {
//                log.warn("不满足发货条件，发货单{}", JSON.toJSONString(erpOrderSend));
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "不满足发货条件");
//                return false;
//            }
//
//            //组装发货接口对象
//            SaveOrderDeliveryListInfoRequest orderSendRequest = new SaveOrderDeliveryListInfoRequest();
//            //发货单明细
//            List<SaveOrderDeliveryInfoRequest> orderSendInfoList = new ArrayList<>();
//
//
//            // 已处理的发货单
//            List<ErpOrderSendDO> doErpOrderSend = new ArrayList<>();
//            //发货的商品内码
//            List<String> insnList = new ArrayList<>();
//            //标记odId对应的发货单提示信息
//            List<Long> moreNumOdIdList = new ArrayList<>();
//            for (OrderDetailDTO od : orderDetailList) {
//                Integer odNumber = od.getGoodsQuantity();
//                // 发货数量
//                int eodNum = 0;
//                Long odId = od.getId();
//                List<ErpOrderSendDO> erpOrderSends = eodIdMap.get(odId);
//
//                if (erpOrderSends != null) {
//                    // 统计发货数量
//                    eodNum = getOrderSendNumber(erpOrderSends);
//                    doErpOrderSend.addAll(erpOrderSends);
//                }
//                // 如果订单数量小于等于发货数量，则取订单数量
//                if (odNumber < eodNum) {
//                    eodNum = odNumber;
//                    //标记odId对应的发货单提示信息
//                    moreNumOdIdList.add(odId);
//                }
//                // 发货数量大于0时,则生成发货单
//                if (eodNum > 0) {
//                    SaveOrderDeliveryInfoRequest saveOrderDeliveryInfoRequest = new SaveOrderDeliveryInfoRequest();
//                    saveOrderDeliveryInfoRequest.setGoodsId(od.getGoodsId());
//                    saveOrderDeliveryInfoRequest.setDetailId(od.getId());
//                    List<DeliveryInfoRequest> deliveryInfoList = new ArrayList<>();
//                    //用来计算批次数量超过了商品数量以后就不再累加批次信息
//                    BigDecimal temNum = BigDecimal.ZERO;
//                    //用来组装相同批号信息
//                    Map<String, DeliveryInfoRequest> deliveryInfoRequestMap = new HashMap<>();
//                    for (ErpOrderSendDO erpOrderSendInfo : erpOrderSends) {
//                        if (erpOrderSendInfo.getSendType() == 3) {
//                            continue;
//                        }
//
//                        //出库单号
//                        String erpDeliveryNo = erpOrderSendInfo.getDeliveryNumber();
//                        String easSendOrderId = erpOrderSendInfo.getEasSendOrderId();
//                        BigDecimal sendNum = erpOrderSendInfo.getSendNum();
//                        //批次数量超过了发货数量
//                        if ((temNum.add(sendNum)).compareTo(new BigDecimal(eodNum)) > 0) {
//                            sendNum = new BigDecimal(eodNum).subtract(temNum);
//                            if (sendNum.compareTo(BigDecimal.ZERO) <= 0) {
//                                break;
//                            }
//                        }
//                        temNum = temNum.add(sendNum);
//                        DeliveryInfoRequest deliveryInfoRequest = null;
//                        String key = erpDeliveryNo + "|" + easSendOrderId + "|" + erpOrderSendInfo.getSendBatchNo();
//                        if (deliveryInfoRequestMap.containsKey(key)) {
//                            deliveryInfoRequest = deliveryInfoRequestMap.get(key);
//                            Integer deliveryQuantity = deliveryInfoRequest.getDeliveryQuantity();
//                            deliveryInfoRequest.setDeliveryQuantity(deliveryQuantity + sendNum.intValue());
//                        } else {
//                            deliveryInfoRequest = new DeliveryInfoRequest();
//                            deliveryInfoRequest.setBatchNo(erpOrderSendInfo.getSendBatchNo());
//                            deliveryInfoRequest.setExpiryDate(erpOrderSendInfo.getEffectiveTime());
//                            deliveryInfoRequest.setDeliveryQuantity(sendNum.intValue());
//                            deliveryInfoRequest.setErpDeliveryNo(erpDeliveryNo);
//                            deliveryInfoRequest.setEasSendOrderId(erpOrderSendInfo.getEasSendOrderId());
//                            deliveryInfoRequestMap.put(key, deliveryInfoRequest);
//                        }
//                    }
//                    deliveryInfoList.addAll(deliveryInfoRequestMap.values());
//                    insnList.add(od.getGoodsErpCode());
//                    saveOrderDeliveryInfoRequest.setDeliveryInfoList(deliveryInfoList);
//                    orderSendInfoList.add(saveOrderDeliveryInfoRequest);
//                }
//            }
//
//            if (CollUtil.isNotEmpty(moreNumOdIdList)) {
//                log.error("发货数量大于订货数量！ 退出" + JSON.toJSONString(orderSendInfoList));
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "发货数量大于订货数量");
//                return false;
//            }
//
//            //            if (CollectionUtils.isEmpty(orderSendInfoList)) {
//            //                log.error("收货单信息为空，请检查数据");
//            //                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "收货单信息为空，请检查数据");
//            //                return false;
//            //            }
//
//            orderSendRequest.setOpUserId(0L);
//            orderSendRequest.setOrderId(oid);
//            orderSendRequest.setOrderDeliveryGoodsInfoList(orderSendInfoList);
//
//            //判断订单信息是否已经发货
//            Boolean serviceResult = false;
//            String message = "";
//            if (orderDTO.getOrderStatus() > 20) {
//                if (erpClient.getSuId().equals(ErpConstants.YILING_EID.intValue())) {
//                    UpdateOrderNotAuditRequest updateOrderNotAuditRequest = PojoUtils.map(orderSendRequest, UpdateOrderNotAuditRequest.class);
//                    log.warn("包裹调用发货单反审核接口请求：" + JSON.toJSONString(orderSendRequest));
//                    Result<Boolean> result = orderProcessApi.modifyOrderNotAudit(updateOrderNotAuditRequest);
//                    log.info("包裹调用发货接口报错！ 退出" + JSON.toJSONString(result));
//                    serviceResult = result.getCode().equals(200);
//                    message = result.getMessage();
//                } else {
//                    log.error("订单信息已经发货！ 退出" + JSON.toJSONString(orderSendRequest));
//                    erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "订单信息已经发货");
//                    return false;
//                }
//            } else {
//                log.warn("包裹调用发货接口请求：" + JSON.toJSONString(orderSendRequest));
//                serviceResult = orderProcessApi.delivery(orderSendRequest);
//            }
//
//            if (serviceResult == null || !serviceResult) {
//                log.error("包裹调用发货接口报错！ 退出" + serviceResult);
//                erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), message);
//                return false;
//            }
//
//            for (ErpOrderSendDO tmpOrderSend : osList) {
//                String syncMsg = "同步成功！";
//                erpOrderSendMapper.updateSyncStatusAndMsg(tmpOrderSend.getId(), SyncStatus.SUCCESS.getCode(), syncMsg);
//            }
//            refreshErpInventoryList(insnList, erpClient.getRkSuId());
//        } catch (Exception e) {
//            erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), e.getMessage());
//            log.error("发货单同步出现错误date=" + JSON.toJSONString(erpOrderSend), e);
//        }
//        return true;
//    }
//
//    public void sendDelayOrderSend(ErpOrderSendDO erpOrderSend) {
//        MqDelayLevel mqDelayLevel = null;
//        Date date = null;
//        if (erpOrderSend.getFailedCount() < 168) {
//            mqDelayLevel = MqDelayLevel.ONE_HOUR;
//            date = DateUtil.offset(new Date(), DateField.HOUR, 1);
//            erpOrderSend.setFailedCount(erpOrderSend.getFailedCount() + 1);
//            rocketMqProducerService.sendDelay(ErpTopicName.ErpOrderSend.getTopicName(), erpOrderSend.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(Arrays.asList(erpOrderSend)), mqDelayLevel);
//            erpOrderSendMapper.updateMsgAndNextTimeById(erpOrderSend.getId(), date, "发货单重试");
//        } else {
//            erpOrderSendMapper.updateSyncStatusAndMsg(erpOrderSend.getId(), SyncStatus.FAIL.getCode(), "发货单超过7还没有处理，请人工处理");
//        }
//    }

    /**
     * 重新刷新op库存信息
     *
     * @param inSnList
     * @param rkSuId
     */
    @Override
    public void refreshErpInventoryList(List<String> inSnList, Long rkSuId) {
        try {
            ErpClientDTO erpClient = erpClientService.selectByRkSuId(rkSuId);

            List<ErpGoodsBatchDO> erpGoodsBatchList = erpGoodsBatchMapper.findBySyncStatusAndInSnList(inSnList, erpClient.getSuDeptNo(), erpClient.getSuId());
            if (CollUtil.isEmpty(erpGoodsBatchList)) {
                return;
            }

            for (ErpGoodsBatchDO erpGoodsBatch : erpGoodsBatchList) {
                erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatch.getId(), SyncStatus.SYNCING.getCode(), "Erp发货重新处理");
                SendResult sendResult = rocketMqProducerService.sendSync(ErpTopicName.ErpGoodsBatch.getTopicName(), erpClient.getSuId() + "", DateUtil.formatDate(new Date()), JSON.toJSONString(Arrays.asList(PojoUtils.map(erpGoodsBatch, ErpGoodsBatchDTO.class))));
                if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
                    erpGoodsBatchMapper.updateSyncStatusAndMsg(erpGoodsBatch.getId(), SyncStatus.UNSYNC.getCode(), "mq发送失败，未处理");
                }
            }
        } catch (Exception e) {
            log.error("发货单发货成功重新刷新库存报错:", e);
        }
    }


    /**
     * 获取Mph的订单主表
     *
     * @param erpOrderSend
     * @return
     */
    public OrderDTO getOrder(ErpOrderSendDO erpOrderSend) {
        OrderDTO orderDTO = null;
        if (erpOrderSend.getOrderId() != null && erpOrderSend.getOrderId() > 0) {
            orderDTO = orderApi.getOrderInfo(erpOrderSend.getOrderId());
        }
        // 医药网的订单明细ID
        Long odId = erpOrderSend.getOrderDetailId();
        Long oid = null;
        // 首先通过订单明细Id获取对应的订单信息
        if (odId != null && odId > 0) {
            OrderDetailDTO orderDetail = orderDetailApi.getOrderDetailById(odId);
            if (orderDetail != null) {
                oid = orderDetail.getOrderId();
            }
            if (oid != null && oid > 0) {
                orderDTO = orderApi.getOrderInfo(oid);
            }
            if(orderDTO==null){
                return null;
            }
            if(!orderDTO.getId().equals(oid)){
                return null;
            }
        }
        return orderDTO;
    }

//    /**
//     * 判断发货单和订单明细是否立即处理条件（满足条数一致并且数量一致）
//     *
//     * @param modIdMap 订单明细
//     * @param eodIdMap 发货单明细
//     * @return Boolean 返回false不处理，返回true立即处理
//     */
//    public boolean isOrderDetailEqualOrderSend(Map<Long, OrderDetailDTO> modIdMap, Map<Long, List<ErpOrderSendDO>> eodIdMap, ErpClientDO erpClient) {
//        boolean flag = true;
//        //首先判断isFinish
//        for (Map.Entry<Long, List<ErpOrderSendDO>> entry : eodIdMap.entrySet()) {
//            List<ErpOrderSendDO> erpOrderSendList = entry.getValue();
//            for (ErpOrderSendDO erpOrderSend : erpOrderSendList) {
//                if (erpOrderSend.getSendType() == 2) {
//                    return true;
//                }
//            }
//        }
//
//        if (modIdMap.size() <= eodIdMap.size()) {// 满足条数一致
//            for (Map.Entry<Long, OrderDetailDTO> entry : modIdMap.entrySet()) {
//                Long odId = entry.getKey();
//                OrderDetailDTO orderDetail = entry.getValue();
//                int modNum = orderDetail.getGoodsQuantity(); // 订单购买数量
//                int eodNum = 0;// 发货单实际发货数量
//                List<ErpOrderSendDO> erpOrderSendList = eodIdMap.get(odId);
//                if (erpOrderSendList != null) {
//                    eodNum = getOrderSendNumberAndRejectNumber(erpOrderSendList);
//                }
//                if (modNum > eodNum) {
//                    flag = false;
//                    break;
//                }
//            }
//        } else {
//            flag = false;
//        }
//        return flag;
//    }

//    // 判断发货数量是否小数，然后是否整数
//    public boolean isOrderSendIntegerNumber(Map<Long, List<ErpOrderSendDO>> eodIdMap) {
//        boolean flag = true;
//        for (Map.Entry<Long, List<ErpOrderSendDO>> entry : eodIdMap.entrySet()) {
//            List<ErpOrderSendDO> erpOrderSendList = entry.getValue();
//            BigDecimal number = BigDecimal.ZERO;
//            for (ErpOrderSendDO erpOrderSend : erpOrderSendList) {
//                if (erpOrderSend.getSendNum() != null && erpOrderSend.getSendNum().compareTo(BigDecimal.ZERO) > 0) {
//                    number = number.add(erpOrderSend.getSendNum());
//                }
//            }
//            //判断是否是小数
//            if (!isIntegerNumber(number)) {
//                flag = false;
//                break;
//            }
//        }
//        return flag;
//    }

//    public boolean isIntegerNumber(BigDecimal number) {
//        return number == null || new BigDecimal(number.intValue()).compareTo(number) == 0;
//    }


//    /**
//     * 统计所有发货数量（包含驳回和发货的数量）
//     */
//    public int getOrderSendNumberAndRejectNumber(List<ErpOrderSendDO> list) {
//        BigDecimal number = BigDecimal.ZERO;
//        if (list != null && list.size() > 0) {
//            for (ErpOrderSendDO erpOrderSend : list) {
//                if (erpOrderSend.getSendType() != 3 && erpOrderSend.getSendNum() != null && erpOrderSend.getSendNum().compareTo(BigDecimal.ZERO) > 0) {
//                    number = number.add(erpOrderSend.getSendNum());
//                }
//            }
//        }
//        return number.intValue();
//    }

//    /**
//     * 统计需要发货的数量
//     */
//    public int getOrderSendNumber(List<ErpOrderSendDO> list) {
//        BigDecimal number = BigDecimal.ZERO;
//        if (list != null && list.size() > 0) {
//            for (ErpOrderSendDO erpOrderSend : list) {
//                if (erpOrderSend.getSendType() != 3) {
//                    if (erpOrderSend.getSendNum() != null && erpOrderSend.getSendNum().compareTo(BigDecimal.ZERO) > 0) {
//                        number = number.add(erpOrderSend.getSendNum());
//                    }
//                }
//            }
//        }
//        return number.intValue();
//    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpOrderSendMapper;
    }
}
