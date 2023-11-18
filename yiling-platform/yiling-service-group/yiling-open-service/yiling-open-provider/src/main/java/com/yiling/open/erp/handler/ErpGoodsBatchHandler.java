package com.yiling.open.erp.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dao.ErpGoodsBatchMapper;
import com.yiling.open.erp.dto.ErpGoodsBatchDTO;
import com.yiling.open.erp.entity.ErpGoodsBatchDO;
import com.yiling.open.erp.enums.OpenErrorCode;
import com.yiling.open.erp.service.ErpEntityService;
import com.yiling.open.erp.service.ErpGoodsBatchService;
import com.yiling.open.erp.service.impl.ErpServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Service
@Slf4j
public class ErpGoodsBatchHandler extends ErpServiceImpl {

    @Autowired(required = false)
    public ErpGoodsBatchService erpGoodsBatchService;

    @Autowired
    private ErpGoodsBatchMapper erpGoodsBatchMapper;

    @Resource(name = "erpGoodsBatchService")
    public ErpEntityService erpEntityService;


    public Boolean handleGoodsBatchMqSync(Long suId, String taskNo, List<ErpGoodsBatchDTO> erpGoodsBatchList) {
        List<BaseErpEntity> entityList = new ArrayList<>();
        for (ErpGoodsBatchDTO erpGoodsBatch : erpGoodsBatchList) {
            ErpGoodsBatchDO erpGoodsBatchDO = PojoUtils.map(erpGoodsBatch, ErpGoodsBatchDO.class);
            entityList.add(erpGoodsBatchDO);
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
        ErpGoodsBatchDO erpGoodsBatchDO = (ErpGoodsBatchDO) baseErpEntity;
        ErpGoodsBatchDO tmpErpGoodsBatch = erpGoodsBatchMapper.getById(erpGoodsBatchDO.getId());
        if (tmpErpGoodsBatch == null) {
            log.error("主键查询OP库信息空值,id=" + erpGoodsBatchDO.getId());
            return false;
        }
        long start = System.currentTimeMillis();
        String lockName = "mph-erp-online-lock-goods-batch:" + tmpErpGoodsBatch.getSuId() + "-" + tmpErpGoodsBatch.getInSn();
        String lockId = "";
        try {
            lockId = redisDistributedLock.lock(lockName, 60, 60, TimeUnit.SECONDS);
            if (StringUtils.isEmpty(lockId)) {
                throw new BusinessException(OpenErrorCode.ERP_SYNC_ERROR, "系统繁忙，获取锁失败，lockName:" + lockName);
            }

            return erpGoodsBatchService.onlineData(baseErpEntity);
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
