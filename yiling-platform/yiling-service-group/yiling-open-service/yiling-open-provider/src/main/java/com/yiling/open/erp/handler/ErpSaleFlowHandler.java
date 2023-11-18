package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.dto.ErpSaleFlowDTO;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.entity.ErpSaleFlowDO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpSaleFlowService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户信息消息处理类
 *
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@Service
public class ErpSaleFlowHandler extends ErpServiceImpl {

    @Autowired
    public ErpSaleFlowService erpSaleFlowService;

    @Resource(name = "erpSaleFlowService")
    public ErpEntityService erpEntityService;

    public Boolean handleSaleFlowMqSync(Long suId, String taskNo, List<ErpSaleFlowDTO> erpSaleFlowList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpSaleFlowDTO erpSaleFlowDTO : erpSaleFlowList) {
            ErpSaleFlowDO erpSaleFlowDO = PojoUtils.map(erpSaleFlowDTO, ErpSaleFlowDO.class);
            entityList.add(erpSaleFlowDO);
        }

        long start = System.currentTimeMillis();
        boolean bool = this.syncBaseData(suId, taskNo, entityList);
        long time = System.currentTimeMillis() - start;
        if (time > 3000) {
            log.warn("ERP数据同步耗时:{}ms, suId:{}, taskNo:{}", time, suId, taskNo);
        }
        return bool;
    }

    public void handleUnLockSaleFlowMqSync(Long rkSuId, String month) {
        if(ObjectUtil.isNull(rkSuId) || StrUtil.isBlank(month)){
            return;
        }
        ErpFlowSealedUnLockRequest request = new ErpFlowSealedUnLockRequest();
        request.setRkSuId(rkSuId);
        request.buildStartMonth(month);
        request.buildEndMonth(month);
        erpSaleFlowService.unLockSynSaleFlow(request);
    }

    @Override
    public ErpEntityService getErpEntityManager() {
        return erpEntityService;
    }

    @Override
    public boolean onlineData(BaseErpEntity baseErpEntity) {
        //todo 需要加上分布式锁 到最细
        long start = System.currentTimeMillis();
        String lockName = "mph-erp-online-lock-flow-sale:" + baseErpEntity.getSuId() + "-" + baseErpEntity.getErpPrimaryKey();
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }
            return erpSaleFlowService.onlineData(baseErpEntity);
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

//    @Override
//    public void handleDataTag(BaseErpEntity entity, Integer dataTag) {
//        ErpSaleFlowDO erpSaleFlowDO = (ErpSaleFlowDO) entity;
//        erpSaleFlowDO.setDataTag(dataTag);
//    }
}
