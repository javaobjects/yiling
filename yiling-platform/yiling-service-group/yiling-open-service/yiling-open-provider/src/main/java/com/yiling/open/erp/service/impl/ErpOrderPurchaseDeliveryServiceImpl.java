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
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpEntityMapper;
import com.yiling.open.erp.dao.ErpOrderPurchaseDeliveryMapper;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.entity.ErpClientDO;
import com.yiling.open.erp.entity.ErpGoodsBatchDO;
import com.yiling.open.erp.entity.ErpOrderPurchaseDeliveryDO;
import com.yiling.open.erp.entity.ErpOrderSendDO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.enums.SendTypeEnum;
import com.yiling.open.erp.enums.SyncStatus;
import com.yiling.open.erp.handler.ErpOrderPurchaseDeliveryHandler;
import com.yiling.open.erp.service.ErpClientService;
import com.yiling.open.erp.service.ErpOrderPurchaseDeliveryService;
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
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 入库单明细表 服务实现类
 * </p>
 *
 * @author houjie.sun
 * @date 2022-03-18
 */
@Slf4j
@Service(value = "erpOrderPurchaseDeliveryService")
public class ErpOrderPurchaseDeliveryServiceImpl extends ErpEntityServiceImpl implements ErpOrderPurchaseDeliveryService {

    @Autowired
    private ErpClientService erpClientService;
    @Autowired
    private ErpOrderPurchaseDeliveryMapper erpOrderPurchaseDeliveryMapper;
    @Autowired
    private ErpOrderPurchaseDeliveryHandler erpOrderPurchaseDeliveryHandler;

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpOrderPurchaseDeliveryDO orderPurchaseDelivery = (ErpOrderPurchaseDeliveryDO) baseErpEntity;

        //1.查询规则信息
        ErpClientDTO erpClient = erpClientService.getErpClientBySuIdAndSuDeptNo(orderPurchaseDelivery.getSuId(), orderPurchaseDelivery.getSuDeptNo());
        if (erpClient == null || erpClient.getSyncStatus() == null || erpClient.getSyncStatus() == 0) {
            erpOrderPurchaseDeliveryMapper.updateSyncStatusAndMsg(orderPurchaseDelivery.getId(), SyncStatus.UNSYNC.getCode(), "未开启同步规则");
            return false;
        }
        return synErpOrderPurchaseDeliverySync(orderPurchaseDelivery, erpClient);
    }

    @Override
    public void syncOrderPurchaseDelivery() {
        List<ErpOrderPurchaseDeliveryDO> orderPurchaseDeliveryList = erpOrderPurchaseDeliveryMapper.syncOrderPurchaseDelivery();
        for (ErpOrderPurchaseDeliveryDO orderPurchaseDelivery : orderPurchaseDeliveryList) {
            int i = erpOrderPurchaseDeliveryMapper.updateSyncStatusByStatusAndId(orderPurchaseDelivery.getId(), SyncStatus.SYNCING.getCode(), SyncStatus.UNSYNC.getCode(), "job处理");
            if (i > 0) {
                erpOrderPurchaseDeliveryHandler.onlineData(orderPurchaseDelivery);
            }
        }
    }

    @Override
    public ErpEntityMapper getErpEntityDao() {
        return erpOrderPurchaseDeliveryMapper;
    }

    public boolean synErpOrderPurchaseDeliverySync(ErpOrderPurchaseDeliveryDO orderPurchaseDelivery, ErpClientDTO erpClient) {
        try {
            //判断op库的数据是否存在，存在直接返回
            if (orderPurchaseDelivery.getOperType() == 3) {
                erpOrderPurchaseDeliveryMapper.updateSyncStatusAndMsg(orderPurchaseDelivery.getId(), SyncStatus.FAIL.getCode(), "采购入库单不支持删除数据");
                return false;
            }

            //判断是否处理成功
            ErpOrderPurchaseDeliveryDO newOrderPurchaseDelivery = erpOrderPurchaseDeliveryMapper.findById(orderPurchaseDelivery.getId());
            if (ObjectUtil.isNotNull(newOrderPurchaseDelivery) && newOrderPurchaseDelivery.getSyncStatus().equals(SyncStatus.SUCCESS.getCode())) {
                return false;
            }

            // todo 更新采购订单中erp收货数量


            erpOrderPurchaseDeliveryMapper.updateSyncStatusAndMsg(orderPurchaseDelivery.getId(), SyncStatus.SUCCESS.getCode(), "同步成功");
        } catch (Exception e) {
            erpOrderPurchaseDeliveryMapper.updateSyncStatusAndMsg(orderPurchaseDelivery.getId(), SyncStatus.FAIL.getCode(), e.getMessage());
            log.error("采购入库单同步出现错误date={}", JSON.toJSONString(orderPurchaseDelivery), e);
        }
        return true;
    }


}
