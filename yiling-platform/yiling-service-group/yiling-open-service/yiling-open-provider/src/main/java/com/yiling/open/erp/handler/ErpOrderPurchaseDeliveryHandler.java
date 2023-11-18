package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpOrderPurchaseDeliveryDTO;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.entity.ErpOrderPurchaseDeliveryDO;
import com.yiling.open.erp.entity.ErpOrderSendDO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpOrderPurchaseDeliveryService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service
@Slf4j
public class ErpOrderPurchaseDeliveryHandler extends ErpServiceImpl {

    @Autowired
    public ErpOrderPurchaseDeliveryService erpOrderPurchaseDeliveryService;

    @Resource(name = "erpOrderPurchaseDeliveryService")
    public ErpEntityService erpEntityService;


    public Boolean handleOrderPurchaseDeliveryMqSync(Long suId, String taskNo, List<ErpOrderPurchaseDeliveryDTO> erpOrderPurchaseDeliveryList) {
        //如果是send_type有值需要加上排序3排在最前面
        if (Constants.YILING_EID.equals(suId)) {
            erpOrderPurchaseDeliveryList = erpOrderPurchaseDeliveryList.stream().sorted(Comparator.comparing(ErpOrderPurchaseDeliveryDTO::getDeliveryNo)).collect(Collectors.toList());
        }

        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpOrderPurchaseDeliveryDTO erpOrderPurchaseDelivery : erpOrderPurchaseDeliveryList) {
            ErpOrderPurchaseDeliveryDO erpOrderPurchaseDeliveryDO = PojoUtils.map(erpOrderPurchaseDelivery, ErpOrderPurchaseDeliveryDO.class);
            entityList.add(erpOrderPurchaseDeliveryDO);
        }
        long start = System.currentTimeMillis();
        boolean bool = this.syncBaseData(suId, taskNo, entityList);
        long time = System.currentTimeMillis() - start;
        if (time > 3000) {
            log.warn("ERP数据同步耗时:{}ms,suId:{},taskNo:{}", time, suId, taskNo);
        }
        return bool;
    }

    @Override
    public ErpEntityService getErpEntityManager() {
        return erpEntityService;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        ErpOrderPurchaseDeliveryDO erpOrderPurchaseDeliveryDO = (ErpOrderPurchaseDeliveryDO) baseErpEntity;
        long start = System.currentTimeMillis();
        String lockName = "mph-erp-online-lock-order-send:" + erpOrderPurchaseDeliveryDO.getDeliveryNo();
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            return erpOrderPurchaseDeliveryService.onlineData(baseErpEntity);
        } catch (InterruptedException e) {
            throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "ERP数据同步出错，lockName:" + lockName);
        } finally {
            long time = System.currentTimeMillis() - start;
            if (time > 3000) {
                log.warn("ERP数据同步耗时:{}ms,lockName:", time, lockName);
            }
            redisDistributedLock.releaseLock(lockName, lockId);
        }

    }
}
